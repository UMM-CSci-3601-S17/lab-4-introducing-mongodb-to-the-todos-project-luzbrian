package umm3601.todo;


import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import com.mongodb.util.JSON;

import org.bson.Document;

import org.bson.types.ObjectId;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.*;


import static com.mongodb.client.model.Filters.eq;

public class toDoController {

    private final MongoCollection<Document> todoCollection;


    public toDoController() throws IOException {

        MongoClient mongoClient = new MongoClient(); // Defaults!

        // Try connecting to a database
        MongoDatabase db = mongoClient.getDatabase("test");

        todoCollection = db.getCollection("todos");

    }

    // List users
    public String listToDos(Map<String, String[]> queryParams) {
        Document filterDoc = new Document();

        if (queryParams.containsKey("owner")) {
            String owner = queryParams.get("owner")[0];
            filterDoc = filterDoc.append("owner", owner);
        }

        //Filter status if defined
        if (queryParams.containsKey("status")) {
            Boolean status;
            String temp = queryParams.get("status")[0];
            if (temp.equals("complete")) {
                status = true;
            } else {
                status = false;
            }
            filterDoc = filterDoc.append("status", status);
        }
        //Filter contains if defined

        // Going to let angular filter the body!

        //Filter category if defined
        if (queryParams.containsKey("category")) {
            String category = queryParams.get("category")[0];
            filterDoc = filterDoc.append("category", category);
        }


        FindIterable<Document> matchingTodos = todoCollection.find(filterDoc);

        return JSON.serialize(matchingTodos);
    }

    // Get a single todo
    public String getTodo(String id) {
        FindIterable<Document> jsonTodos
                = todoCollection
                .find(eq("_id",new ObjectId(id)));

        Iterator<Document> iterator = jsonTodos.iterator();

        Document todo = iterator.next();

        return todo.toJson();
    }


private float completeField(String fields, String val){
    Document countDoc = new Document();
    countDoc.append(fields, val);
    countDoc.append("status", true);
    return todoCollection.count(countDoc);
}

    public String getToDoSummary() {
        Document container = new Document();

        Document temp = new Document();
        float result;

        temp.append("status", true);

        result = todoCollection.count(temp);
        float totalPercent = result/todoCollection.count();

        //float totalPercent =  todoCollection.count(eq("status",true)) / todoCollection.count();
        container.append("percentageTodosComplete", totalPercent);


        AggregateIterable<Document> doc1 = todoCollection.aggregate(Arrays.asList(Aggregates.group("$category")));
        List<String> result1 = new ArrayList<>();

        for(Document document: doc1){
            result1.add(document.getString("_id"));
        }
        Document doc3 = new Document();
        for(String category: result1){
            doc3.append(category, completeField("category", category)/ todoCollection.count(eq("category", category)));
        }
        container.append("categoriesPercentComplete",doc3);


        AggregateIterable<Document> doc2 = todoCollection.aggregate(Arrays.asList(Aggregates.group("$owner")));
        List<String> result2 = new ArrayList<>();

        for(Document document: doc2){
            result2.add(document.getString("_id"));
        }
        Document doc4 = new Document();

        for(String owner: result2){
            doc4.append(owner, completeField("owner", owner)/ todoCollection.count(eq("owner", owner)));
        }
        container.append("ownersPercentComplete", doc4);

        return JSON.serialize(container);
    }


}



package umm3601.todo;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import com.mongodb.util.JSON;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import com.mongodb.Block;

import static com.mongodb.client.model.Filters.eq;

public class toDoController {

    private final MongoCollection<Document> todoCollection;

    Block<Document> printBlock = new Block<Document>() {
        @Override
        public void apply(final Document document) {
            System.out.println(document.toJson());
        }
    };


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
            Boolean temp;
            String status = queryParams.get("status")[0];
            if (status.equals("complete")) {
                temp = true;
            } else {
                temp = false;
            }
            filterDoc = filterDoc.append("status", temp);
        }
        //Filter contains if defined

        // Going to let angular filter the body!
//        if (queryParams.containsKey("contains")) {
//            String containsWord = queryParams.get("contains")[0];
//            filterDoc = filterDoc.append("contains", containsWord);
//        }
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
                .find(eq("_id", id));

        Iterator<Document> iterator = jsonTodos.iterator();

        Document todo = iterator.next();

        return todo.toJson();
    }

}


    //Trying to aggregate todoSummary
//    public String getTodoSummary(Map<String, String[]> queryParams) {
//
//        AggregateIterable<Document> output = todoCollection.aggregate(
//                    Arrays.asList(
//                            Aggregates.match(Filters.eq("owner", "Blanche")),
//                                Aggregates.group("$owner", Accumulators.sum("categoriesPercentComplete", 1))
//                                //Aggregates.group("$owner", Accumulators.sum("ownerPercentComplete", 1))
//                    )
//            ).forEach(printBlock);
//
//
//            //return JSON.serialize(printBlock);
//
//
//                new Document("$owner", "$Blanche")
//
//        }
//}

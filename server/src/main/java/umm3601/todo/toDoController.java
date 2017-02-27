package umm3601.todo;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import com.mongodb.Block;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Filters;

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

// Trying to Figure out how aggregate works to get todo summary to work
//        todoCollection.aggregate(
//                Arrays.asList(
//                        Aggregates.match(Filters.eq("status", "complete")),
//
//
//                        Aggregates.group("owners", Accumulators.sum("count", 1))
//                        Aggregates.group("category", Accumulators.sum("count", 1))
//                )
//        ).forEach(printBlock);


    }

    // List users
    public String listToDos(Map<String, String[]> queryParams) {
        Document filterDoc = new Document();

        if (queryParams.containsKey("owner")) {
            String owner = queryParams.get("owner")[0];
            filterDoc = filterDoc.append("owner", owner);
        }

         //Filter status if defined
        if(queryParams.containsKey("status")) {
            boolean status = Boolean.parseBoolean(queryParams.get("status")[0]);
            filterDoc = filterDoc.append("status", status);
        }
        //Filter contains if defined
        if (queryParams.containsKey("contains")) {
            String contains = queryParams.get("contains")[0];
            filterDoc = filterDoc.append("contains", contains);
        }
        //Filter category if defined
        if(queryParams.containsKey("category")) {
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


//
//    // List Todos
//    public Todo[] listToDos(Map<String, String[]> queryParams) {
//        Todo[] filteredTodos = todos;
//
//        // Filter status if defined
//        if(queryParams.containsKey("status")) {
//            String status = queryParams.get("status")[0];
//            filteredTodos = filterTodosByStatus(filteredTodos, status);
//        }
//
//        ;
//        }// Filter contains if defined
//        if(queryParams.containsKey("contains")) {
//            String contains = queryParams.get("contains")[0];
//            filteredTodos = filterTodosByContains(filteredTodos, contains)
//
//        // Filter owner if defined
//        if(queryParams.containsKey("owner")) {
//            String owner = queryParams.get("owner")[0];
//            filteredTodos = filterTodosByOwner(filteredTodos, owner);
//        }
//
//        // Filter category if defined
//        if(queryParams.containsKey("category")) {
//            String category = queryParams.get("category")[0];
//            filteredTodos = filterTodosByCategory(filteredTodos, category);
//        }
//
//        return filteredTodos;
//    }
//
//    // Get a single id's todos
//    public Todo getToDo(String id) {
//        return Arrays.stream(todos).filter(x -> x._id.equals(id)).findFirst().orElse(null);
//    }
//
//    public Todo[] filterTodosByStatus(Document document, String status) {
//        boolean Status = status.equals("complete") ? true : false;
//        return Arrays.stream(document).filter(x -> x.status == Status).toArray(Todo[]::new);
//    }
//
//    public Document filterTodosByContains(Document filteredTodos, String contain) {
//        return filteredTodos.filter(x -> x.body.contains(contain)).toArray(Todo[]::new);
//    }
//
//    public Todo[] filterTodosByOwner(Todo[] filteredTodos, String owner) {
//        return Arrays.stream(filteredTodos).filter(x -> x.owner.equals(owner)).toArray(Todo[]::new);
//    }
//
//    public Todo[] filterTodosByCategory(Todo[] filteredTodos, String category) {
//        return Arrays.stream(filteredTodos).filter(x -> x.category.equals(category)).toArray(Todo[]::new);
//    }
//}
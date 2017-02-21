package umm3601.todo;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import org.bson.Document;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

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

        FindIterable<Document> matchingTodos = todoCollection.find(filterDoc);

        return JSON.serialize(matchingTodos);
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
//        // Filter contains if defined
//        if(queryParams.containsKey("contains")) {
//            String contains = queryParams.get("contains")[0];
//            filteredTodos = filterTodosByContains(filteredTodos, contains);
//        }
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
//    public Todo[] filterTodosByStatus(Todo[] filteredTodos, String status) {
//        boolean Status = status.equals("complete") ? true : false;
//        return Arrays.stream(filteredTodos).filter(x -> x.status == Status).toArray(Todo[]::new);
//    }
//
//    public Todo[] filterTodosByContains(Todo[] filteredTodos, String contain) {
//        return Arrays.stream(filteredTodos).filter(x -> x.body.contains(contain)).toArray(Todo[]::new);
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
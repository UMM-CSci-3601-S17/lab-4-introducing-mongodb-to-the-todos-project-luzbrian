package umm3601.todo;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.*;
import org.bson.codecs.*;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.json.JsonReader;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class TodoControllerSpec
{
    private toDoController todoController;
    private String samsIdString;

    @Before
    public void clearAndPopulateDB() throws IOException {
        MongoClient mongoClient = new MongoClient();
        MongoDatabase db = mongoClient.getDatabase("test");
        MongoCollection<Document> todoDocuments = db.getCollection("todo");
        todoDocuments.drop();
        List<Document> testTodos = new ArrayList<>();
        testTodos.add(Document.parse("{\n" +
                "                    owner: \"Chris\",\n" +
                "                    status: true,\n" +
                "                    body: \"this is Chris's body\",\n" +
                "                    category: \"homework\"\n" +
                "                }"));
        testTodos.add(Document.parse("{\n" +
                "                    owner: \"Pat\",\n" +
                "                    status: true,\n" +
                "                    body: \"this is Pat's body\",\n" +
                "                    category: \"software design\"\n" +
                "                }"));
        testTodos.add(Document.parse("{\n" +
                "                    owner: \"Jamie\",\n" +
                "                    status: false,\n" +
                "                    body: \"this is Jamie's body\",\n" +
                "                    email: \"software design\"\n" +
                "                }"));
        ObjectId samsId = new ObjectId();
        BasicDBObject sam = new BasicDBObject("_id", samsId);
        sam = sam.append("owner", "Sam")
                .append("status",true)
                .append("body", "this is Sam's body ")
                .append("category", "texting");
        samsIdString = samsId.toHexString();
        todoDocuments.insertMany(testTodos);
        todoDocuments.insertOne(Document.parse(sam.toJson()));

        // It might be important to construct this _after_ the DB is set up
        // in case there are bits in the constructor that care about the state
        // of the database.
        todoController = new toDoController();
    }

    // http://stackoverflow.com/questions/34436952/json-parse-equivalent-in-mongo-driver-3-x-for-java
    private BsonArray parseJsonArray(String json) {
        final CodecRegistry codecRegistry
                = CodecRegistries.fromProviders(Arrays.asList(
                new ValueCodecProvider(),
                new BsonValueCodecProvider(),
                new DocumentCodecProvider()));

        JsonReader reader = new JsonReader(json);
        BsonArrayCodec arrayReader = new BsonArrayCodec(codecRegistry);

        return arrayReader.decode(reader, DecoderContext.builder().build());
    }

    private static String getOwner(BsonValue val) {
        BsonDocument doc = val.asDocument();
        return ((BsonString) doc.get("owner")).getValue();
    }
//
//    @Test
//    public void getAllTodos() {
//        Map<String, String[]> emptyMap = new HashMap<>();
//        String jsonResult = todoController.listToDos(emptyMap);
//        BsonArray docs = parseJsonArray(jsonResult);
//
//        assertEquals("Should be 4 users", 4, docs.size());
//        List<String> names = docs
//                .stream()
//                .map(TodoControllerSpec::getOwner)
//                .sorted()
//                .collect(Collectors.toList());
//        List<String> expectedNames = Arrays.asList("Chris", "Jamie", "Pat", "Sam");
//        assertEquals("Owner names should match", expectedNames, names);
//    }

//    @Test
//    public void getCategory() {
//        Map<String, String[]> argMap = new HashMap<>();
//        argMap.put("category", new String[] { "software design" });
//        String jsonResult = todoController.listToDos(argMap);
//        BsonArray docs = parseJsonArray(jsonResult);
//
//        assertEquals("Should be 2 todos", 2, docs.size());
//        List<String> owners = docs
//                .stream()
//                .map(TodoControllerSpec::getOwner)
//                .sorted()
//                .collect(Collectors.toList());
//        List<String> expectedOwners = Arrays.asList("Jamie", "Pat");
//        assertEquals("Todos should match", expectedOwners, owners);
//    }

}

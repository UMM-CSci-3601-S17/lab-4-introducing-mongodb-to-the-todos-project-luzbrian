package umm3601.mongotest;

import com.mongodb.MongoClient;
import com.mongodb.client.*;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static org.junit.Assert.*;


public class TodoMongoSpec {

    private MongoCollection<Document> todoDocuments;

    @Before
    public void clearAndPopulateDB() {
        MongoClient mongoClient = new MongoClient();
        MongoDatabase db = mongoClient.getDatabase("testingdb");
        todoDocuments = db.getCollection("todos");
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
        todoDocuments.insertMany(testTodos);
    }

    private List<Document> intoList(MongoIterable<Document> documents) {
        List<Document> todos = new ArrayList<>();
        documents.into(todos);
        return todos;
    }

    private int countTodos(FindIterable<Document> documents) {
        List<Document> todos = intoList(documents);
        return todos.size();
    }

    @Test
    public void shouldBeThreeTodos() {
        FindIterable<Document> documents = todoDocuments.find();
        int numberOfTodos = countTodos(documents);
        assertEquals("Should be 3 total todos", 3, numberOfTodos);
    }

    @Test
    public void shouldBeOneChrisasOwner() {
        FindIterable<Document> documents = todoDocuments.find(eq("owner", "Chris"));
        int numberOfTodos = countTodos(documents);
        assertEquals("Should be 1 Chris", 1, numberOfTodos);
    }

    @Test
    public void shouldHaveHomeworkCategory() {
        FindIterable<Document> documents = todoDocuments.find(gt("category", "homework"));
        int numberOfTodos = countTodos(documents);
        assertEquals("Should have homework as their category", 2, numberOfTodos);
    }

    @Test
    public void haveTrueSortedByOwner() {
        FindIterable<Document> documents
                = todoDocuments.find(gt("status", true))
                .sort(Sorts.ascending("owner"));
        List<Document> docs = intoList(documents);
        assertEquals("Should be 2", 2, docs.size());
        assertEquals("First should be Jamie", "Jamie", docs.get(0).get("owner"));
        assertEquals("Second should be Pat", "Pat", docs.get(1).get("owner"));
    }

    @Test
    public void haveFalseStatusandSoftwareCategory() {
        FindIterable<Document> documents
                = todoDocuments.find(and(gt("status", false),
                eq("category", "software design")));
        List<Document> docs = intoList(documents);
        assertEquals("Should be 1", 1, docs.size());
        assertEquals("First should be Pat", "Pat", docs.get(0).get("owner"));
    }

    @Test
    public void justOwnerandBody() {
        FindIterable<Document> documents
                = todoDocuments.find().projection(fields(include("owner", "body")));
        List<Document> docs = intoList(documents);
        assertEquals("Should be 3", 3, docs.size());
        assertEquals("First should be Chris", "Chris", docs.get(0).get("owner"));
        assertNotNull("First should have body", docs.get(0).get("body"));
        assertNull("First shouldn't have 'category'", docs.get(0).get("category"));
        assertNotNull("First should have '_id'", docs.get(0).get("_id"));
    }

    @Test
    public void justOwnerandBodyNoId() {
        FindIterable<Document> documents
                = todoDocuments.find()
                .projection(fields(include("owner", "body"), excludeId()));
        List<Document> docs = intoList(documents);
        assertEquals("Should be 3", 3, docs.size());
        assertEquals("First should be Chris", "Chris", docs.get(0).get("name"));
        assertNotNull("First should have body", docs.get(0).get("email"));
        assertNull("First shouldn't have 'category'", docs.get(0).get("category"));
        assertNull("First should not have '_id'", docs.get(0).get("_id"));
    }

    @Test
    public void justOwnerNoIdSortedByCategory() {
        FindIterable<Document> documents
                = todoDocuments.find()
                .sort(Sorts.ascending("category"))
                .projection(fields(include("owner"), excludeId()));
        List<Document> docs = intoList(documents);
        assertEquals("Should be 3", 3, docs.size());
        assertEquals("First should be Jamie", "Jamie", docs.get(0).get("owner"));
        assertNull("First shouldn't have 'category'", docs.get(0).get("category"));
        assertNull("First should not have '_id'", docs.get(0).get("_id"));
    }

    @Test
    public void ownerCounts() {
        AggregateIterable<Document> documents
                = todoDocuments.aggregate(
                Arrays.asList(
                        /*
                         * Groups data by the "age" field, and then counts
                         * the number of documents with each given age.
                         * This creates a new "constructed document" that
                         * has "age" as it's "_id", and the count as the
                         * "ageCount" field.
                         */
                        Aggregates.group("$owner",
                                Accumulators.sum("ownerCount", 1)),
                        Aggregates.sort(Sorts.ascending("_id"))
                )
        );
        List<Document> docs = intoList(documents);
        assertEquals("Should be three distinct owners", 3, docs.size());
        assertEquals(docs.get(0).get("_id"), "Chris");
        assertEquals(docs.get(0).get("ownerCount"), 1);
        assertEquals(docs.get(1).get("_id"), "Jamie");
        assertEquals(docs.get(1).get("ownerCount"), 2);
        assertEquals(docs.get(1).get("_id"), "Pat");
        assertEquals(docs.get(1).get("ownerCount"), 3);
    }

//    @Test
//    public void averageAge() {
//        AggregateIterable<Document> documents
//                = userDocuments.aggregate(
//                Arrays.asList(
//                        Aggregates.group("$company",
//                                Accumulators.avg("averageAge", "$age")),
//                        Aggregates.sort(Sorts.ascending("_id"))
//                ));
//        List<Document> docs = intoList(documents);
//        assertEquals("Should be three companies", 3, docs.size());
//
//        assertEquals("Frogs, Inc.", docs.get(0).get("_id"));
//        assertEquals(37.0, docs.get(0).get("averageAge"));
//        assertEquals("IBM", docs.get(1).get("_id"));
//        assertEquals(37.0, docs.get(1).get("averageAge"));
//        assertEquals("UMM", docs.get(2).get("_id"));
//        assertEquals(25.0, docs.get(2).get("averageAge"));
//    }

}

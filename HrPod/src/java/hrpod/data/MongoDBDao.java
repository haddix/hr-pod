/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrpod.data;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.log4j.Logger;
import org.bson.Document;

public class MongoDBDao {

    final static Logger logger = Logger.getLogger(MongoDBDao.class);
    private MongoClient mongoClient;
    private MongoDatabase db;
    private String dbName = "hrpod";

    public MongoDBDao() {
        this.mongoClient = new MongoClient(); //Connect to a MongoDB instance running on the localhost on the default port 27017    
        this.db = mongoClient.getDatabase(dbName);
    }

    public String find(String collectionName) {
        String json = null;
        MongoCollection<Document> collection = db.getCollection(collectionName);
        FindIterable<Document> iterable = collection.find(new Document("Job Code", "10075"));
        for(Document document : iterable){
            json = document.toString();
        }
        return json;
    }

    private void insertDocument(String json, String collectionName) {
        MongoCollection<Document> collection = db.getCollection(collectionName);
        collection.insertOne(Document.parse(json));
    }

    public void insertResume(String json) {
        insertDocument(json, "resumes");
    }

    public void insertJobs(String json) {
        insertDocument(json, "jobs");
    }

    public void insertJobsNLP(String json) {
        insertDocument(json, "jobs");
    }

    public void insertJobsHash(String json) {
        insertDocument(json, "jobs");
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrpod.data;

import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteOperation;
import com.mongodb.BulkWriteResult;
import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ParallelScanOptions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import org.bson.Document;

import java.util.List;
import java.util.Set;

import static java.util.concurrent.TimeUnit.SECONDS;

public class MongoDBDao {

    private MongoClient mongoClient;
    private MongoDatabase db;
    private String dbName = "hrpod";

    public MongoDBDao() {
        this.mongoClient = new MongoClient(); //Connect to a MongoDB instance running on the localhost on the default port 27017    
        this.db = mongoClient.getDatabase(dbName);
    }

    private void insertDocument(String json, String collectionName) {
        MongoCollection<Document> collection = db.getCollection(collectionName);
        collection.insertOne(Document.parse(json));
    }
    
    public void insertResume(String json){
        insertDocument(json, "resumes");
    }
    
    public void insertJobPosition(String json){
        insertDocument(json, "positions");
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrpod.businesslogic;

import hrpod.data.MongoDBDao;
import org.apache.log4j.Logger;
import org.json.JSONObject;

/**
 *
 * @author admin
 */
public class GetJobPostingBL {
    
    final static Logger logger = Logger.getLogger(GetJobPostingBL.class);
    private MongoDBDao mongod = new MongoDBDao();
    
    public GetJobPostingBL(){
        
    }
    
    
    public String getJob(String id, String returnType){
        return mongod.find("jobs");
    }
    
}

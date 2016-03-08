/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrpod;

import hrpod.businesslogic.IngestDataBL;
import hrpod.tools.nlp.NLPTools;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

/**
 *
 * @author admin
 */
public class Hrpod {

    final static Logger logger = Logger.getLogger(Hrpod.class);
    
    public static void main(String[] args) {
        // ingest data
        IngestDataBL ingest = new IngestDataBL();
        JSONObject data = ingest.doIngetsResume();
        
        String exp = data.get("experience").toString();
        
        ArrayList words = new NLPTools().getTokens(exp);
        
        logger.info("DONE");
    }
    
}

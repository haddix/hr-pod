/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrpod.businesslogic;

import hrpod.data.MongoDBDao;
import hrpod.tools.PDFTools;
import org.json.simple.JSONObject;

/**
 *
 * @author admin
 */
public class IngestDataBL {
    
    
    public JSONObject doIngetsResume(){
        
        MongoDBDao mongod = new MongoDBDao();
        
        String txt = getPDFs();
        JSONObject json = parseResume(txt);
        json = parseResume(txt);
        
        mongod.insertResume(json.toString());
        
        return json;        
    }
    
    
    
    public String getPDFs(){
        String filePath = "/data/resume1.pdf";
        PDFTools pdfTools = new PDFTools();

        String resume = pdfTools.getStringFromPDF(filePath);
        return resume;
    }
    
    public JSONObject parseResume(String resume){
        String availability = "Availability:";
        String desiredLocations = "Desired\nlocations:";
        String workExperince = "Work\nExperience:";
        String education = "Education:";
        String training = "Job Related\nTraining:";
        String references = "References:";
        String additionalInfo = "Additional\nInformation:";

        //logger.info(resume);
        String introTxt = resume.substring(0, resume.indexOf(availability));
        String availabilityTxt = resume.substring(resume.indexOf(availability) + (availability.length()), resume.indexOf(desiredLocations));
        String desiredLocationsTxt = resume.substring(resume.indexOf(desiredLocations) + (desiredLocations.length()), resume.indexOf(workExperince));
        String workExperinceTxt = resume.substring(resume.indexOf(workExperince) + (workExperince.length()), resume.indexOf(education));
        String educationTxt = resume.substring(resume.indexOf(education) + (education.length()), resume.indexOf(training));
        String trainingTxt = resume.substring(resume.indexOf(training) + (training.length()), resume.indexOf(references));
        String referencesTxt = resume.substring(resume.indexOf(references) + (references.length()), resume.indexOf(additionalInfo));
        String additionalInfoTxt = resume.substring(resume.indexOf(additionalInfo) + (additionalInfo.length()), resume.length());        
        
        JSONObject json = new JSONObject();
        json.put("experience", workExperinceTxt);
        json.put("skills", trainingTxt);
        
        return json;
    }
    
}

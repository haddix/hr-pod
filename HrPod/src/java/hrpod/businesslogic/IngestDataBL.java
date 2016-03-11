/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrpod.businesslogic;

import hrpod.data.MongoDBDao;
import hrpod.tools.PDFTools;
import hrpod.tools.nlp.NLPTools;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author admin
 */
public class IngestDataBL {

    private MongoDBDao mongod = new MongoDBDao();
    final static Logger logger = Logger.getLogger(IngestDataBL.class);
    private NLPTools nlpTools = new NLPTools();

    public void doIngetsJobs(InputStream inputStream) {

        ArrayList<String> jobs = getJobs(inputStream);
        ArrayList<JSONObject> parsedJobs = null;
        try {
            parsedJobs = parseJobs(jobs);
        } catch (Exception e) {
            logger.error("ERROR: ", e);
        }

        
        for (JSONObject job : parsedJobs) {
            mongod.insertJobs(job.toString());
        }
        
        for (JSONObject job : parsedJobs) {
            JSONObject nlpJson = getNLPVersion(job);
        }
    }

    public JSONObject getNLPVersion(JSONObject json) {
        JSONObject nlpJson = null;
        try {
            nlpJson = new JSONObject(json.toString());
            Iterator<?> keys = nlpJson.keys();

            while (keys.hasNext()) {
                String key = (String) keys.next();
                if (nlpJson.get(key) instanceof JSONArray) {
                    JSONArray array = nlpJson.getJSONArray(key);
                    for(int a = 0; a < array.length(); a++){                        
                        array.put(a, nlpTools.getTokens(array.get(a).toString()));
                        logger.info("ITEM = " + array.getString(a));
                    }                    
                }
            }
        } catch (Exception e) {
            logger.error("ERROR", e);
        }
        return nlpJson;
    }

    public ArrayList getJobs(InputStream inputStream) {
        String filePath = "/data/jobs.pdf";
        PDFTools pdfTools = new PDFTools();

        String[] pages = pdfTools.getPagesFromPDF(inputStream);
        ArrayList jobs = new ArrayList();
        //group pages into resumes
        StringBuffer pagesSB = new StringBuffer();
        boolean processingJob = false;
        for (String page : pages) {
            if (page != null) {
                if (page.contains("Job Code:")) {
                    if (processingJob) {
                        jobs.add(pagesSB.toString());
                        processingJob = false;
                        pagesSB = new StringBuffer();
                    } else {
                        processingJob = true;
                        pagesSB.append(page);
                    }
                } else {
                    pagesSB.append(page);
                }
            }
        }

        return jobs;
    }

    public ArrayList<JSONObject> parseJobs(ArrayList<String> jobs) throws JSONException {
        ArrayList<JSONObject> parsedJobs = new ArrayList();

        for (String job : jobs) {
            JSONObject jobJSON = new JSONObject();
            String titleProcessing = null;
            String[] paras = job.split("\n");
            for (int p = 0; p < paras.length - 2; p++) { //skip last 2 paras which have classification and page number                 
                //clean up text
                paras[p] = paras[p].replaceAll("•", "").trim(); //this needs to be handeld differently
                //paras[p].replaceAll("•", ""); //this needs to be handeld differently
                //end cleanup
                if (p == 0) {
                    jobJSON.put("classification", paras[p]);
                } else if (paras[p].matches("^[A-Z][a-zA-Z0-9( )]+:(.)*")) { //match sentances that start with [any word][any spaces] followed by : like "Job Title:"
                    titleProcessing = paras[p].substring(0, paras[p].indexOf(":")).trim();
                    jobJSON.append(paras[p].substring(0, paras[p].indexOf(":")).trim(), paras[p].substring(paras[p].indexOf(":") + 1, paras[p].length()).trim());
                } else if (paras[p].startsWith("FACTOR")) {
                    titleProcessing = paras[p].substring(0, paras[p].indexOf("–")).trim();
                    String[] factors = paras[p].split("–");
                    for (int f = 1; f < factors.length; f++) {
                        jobJSON.append(titleProcessing, factors[f].trim());
                    }
                } else {
                    jobJSON.append(titleProcessing, paras[p].trim());
                }
            }

            parsedJobs.add(jobJSON);
            jobJSON = new JSONObject();
        }

        return parsedJobs;
    }

    public JSONObject doIngetsResume(InputStream inputStream) {

        String txt = getPDFs(inputStream);
        JSONObject json = null;
        try {
            json = parseResume(txt);
        } catch (Exception e) {
            logger.error("ERROR: ", e);
        }

        mongod.insertResume(json.toString());

        return json;
    }

    public String getPDFs(InputStream inputStream) {
        String filePath = "/data/resume1.pdf";
        PDFTools pdfTools = new PDFTools();

        String resume = pdfTools.getStringFromPDF(inputStream);
        return resume;
    }

    public JSONObject parseResume(String resume) throws JSONException {
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

    public JSONObject createJobJSONTemplate() {
        JSONObject job = new JSONObject();
        try {
            job.put("classification", "");
            job.put("Job_Code", "");
            job.put("Title", "");
            job.put("Job_Code", "");
            job.put("Occupational_Group", "");
            job.put("Occupational_Specialty", "");
            job.put("Occupational_Series", "");
            job.put("Effective_Date", "");
            job.put("General_Summary", "");
            job.put("Major_Duties", "");
            job.put("Factors", "");
        } catch (JSONException e) {
            logger.error("JSON ERROR", e);
        } catch (Exception ex) {
            logger.error("ERROR", ex);
        }
        return job;
    }

}

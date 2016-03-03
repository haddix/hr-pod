package opennlp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.ngram.NGramModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Sequence;
import opennlp.tools.util.Span;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.tartarus.snowball.ext.PorterStemmer;

public class Opennlp {

    final static Logger logger = Logger.getLogger(Opennlp.class);

    public static void main(String[] args) throws FileNotFoundException, IOException {

        String filePath = "src/data/resume1.pdf";
        PDFTools pdfTools = new PDFTools();

        String resume = pdfTools.getStringFromPDF(filePath);

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

        try {
            
            String words[] = tokenize(workExperinceTxt);//tokenize into words and phrases
            
            ArrayList<String> wordList = new StopWordRemoval().removeStopWords(words); //remove stop words
            
            wordList = stemmer(wordList);//stem words

            logger.info("DONE");

        }catch (Exception ex) {
            logger.error("ERROR", ex);
        }

    }
    
    
    public static ArrayList<String> stemmer(ArrayList<String> wordList) {
        PorterStemmer stemmer = new PorterStemmer();
        for(int wl = 0; wl < wordList.size(); wl++){
            logger.info("OLD WORD: " + wordList.get(wl));
            ArrayList<String> subWordList = new ArrayList();
            subWordList.addAll(Arrays.asList(wordList.get(wl).split(" ")));
            for(int swl = 0; swl < subWordList.size(); swl++){
                String word = subWordList.get(swl);
                stemmer.setCurrent(word); //set string you need to stem
                stemmer.stem();
                String stemmedWord = stemmer.getCurrent();
                subWordList.set(swl, stemmedWord);
            }            
            wordList.set(wl, StringUtils.join(subWordList, " "));
            logger.info("NEW WORD: " + wordList.get(wl));
        }
        
        return wordList;
        
    }

    public static String[] tokenize(String text) {

        String[] chunkStrings = null;
        
        try {
            String modelPath = "src/models/";
            TokenizerModel tm = new TokenizerModel(new FileInputStream(new File(modelPath + "en-token.bin")));
            TokenizerME wordBreaker = new TokenizerME(tm);
            POSModel pm = new POSModel(new FileInputStream(new File(modelPath + "en-pos-maxent.bin")));
            POSTaggerME posme = new POSTaggerME(pm);
            InputStream modelIn = new FileInputStream(modelPath + "en-chunker.bin");
            ChunkerModel chunkerModel = new ChunkerModel(modelIn);
            ChunkerME chunkerME = new ChunkerME(chunkerModel);

            //words is the tokenized sentence
            String[] words = wordBreaker.tokenize(text);
            //posTags are the parts of speech of every word in the sentence (The chunker needs this info of course)
            String[] posTags = posme.tag(words);
            //chunks are the start end "spans" indices to the chunks in the words array
            Span[] chunks = chunkerME.chunkAsSpans(words, posTags);
            //chunkStrings are the actual chunks
            chunkStrings = Span.spansToStrings(chunks, words);
            //for (int i = 0; i < chunks.length; i++) {
            //    if (chunks[i].getType().equals("NP")) {
            //        System.out.println("NP: \n\t" + chunkStrings[i]);

          //String[] split = chunkStrings[i].split(" ");
                    //List<String> ngrams = ngram(Arrays.asList(split), N, " ");
                    //System.out.println("ngrams:");
                    //for (String gram : ngrams) {
                    //  System.out.println("\t" + gram);
                    //}
                //}
            //}
            
        } catch (IOException e) {
        }
        
        return chunkStrings;
        
    }

}

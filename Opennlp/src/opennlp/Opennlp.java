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
import org.apache.log4j.Logger;
import org.tartarus.snowball.ext.PorterStemmer;

public class Opennlp {

    final static Logger logger = Logger.getLogger(Opennlp.class);
    private static String[] posTags = {"CC", "CD", "DT", "EX", "FW", "IN", "JJ", "JJR", "JJS", "LS", "MD", "NN", "NNS",
        "NNP", "NNPS", "PDT", "POS", "PRP", "PRP$", "RB", "RBR", "RBS", "RP", "SYM", "TO",
        "UH", "VB", "VBD", "VBG", "VBN", "VBP", "VBZ", "WDT", "WP", "WP$", "WRB"};

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

        String introTxt = null;
        String availabilityTxt = null;
        String desiredLocationsTxt = null;
        String workExperinceTxt = null;
        String educationTxt = null;
        String trainingTxt = null;
        String referencesTxt = null;
        String additionalInfoTxt = null;

        //logger.info(resume);
        introTxt = resume.substring(0, resume.indexOf(availability));
        availabilityTxt = resume.substring(resume.indexOf(availability) + (availability.length()), resume.indexOf(desiredLocations));
        desiredLocationsTxt = resume.substring(resume.indexOf(desiredLocations) + (desiredLocations.length()), resume.indexOf(workExperince));
        workExperinceTxt = resume.substring(resume.indexOf(workExperince) + (workExperince.length()), resume.indexOf(education));
        educationTxt = resume.substring(resume.indexOf(education) + (education.length()), resume.indexOf(training));
        trainingTxt = resume.substring(resume.indexOf(training) + (training.length()), resume.indexOf(references));
        referencesTxt = resume.substring(resume.indexOf(references) + (references.length()), resume.indexOf(additionalInfo));
        additionalInfoTxt = resume.substring(resume.indexOf(additionalInfo) + (additionalInfo.length()), resume.length());

        try {

            /*
             tokenize text into a string array and then remove any common words and punctuation
             */
            TokenizerModel tokenModel = new TokenizerModel(new File("src/models/en-token.bin"));
            Tokenizer tokenizer = new TokenizerME(tokenModel);
            StopWordRemoval swr = new StopWordRemoval();
            workExperinceTxt = swr.removeStopWords(tokenizer.tokenize(workExperinceTxt));
            
            String words[] = tokenize(workExperinceTxt);

            PorterStemmer stemmer = new PorterStemmer();
            for (int w = 0; w < words.length; w++) {
                String word = words[w].toString();
                stemmer.setCurrent(word); //set string you need to stem
                stemmer.stem();
                String stemmedWord = stemmer.getCurrent();
                words[w] = stemmedWord;
                logger.info("STEMMED WORD: " + word + " => " + stemmedWord);
            }

            logger.info("DONE");

        } catch (IOException e) {
            logger.error("ERROR: ", e);
        } catch (Exception ex) {
            logger.error("ERROR", ex);
        }

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

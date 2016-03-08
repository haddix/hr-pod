/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrpod.tools.nlp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.tartarus.snowball.ext.PorterStemmer;

/**
 *
 * @author admin
 */
public class NLPTools {

    final static Logger logger = Logger.getLogger(NLPTools.class);
    private static String modelPath = "src/java/hrpod/tools/nlp/models/";

    public ArrayList getTokens(String txt) {
        ArrayList<String> wordList = null;
        try {
            POSModel pm = new POSModel(new FileInputStream(new File(modelPath + "en-pos-maxent.bin")));
            POSTaggerME posme = new POSTaggerME(pm);

            String words[] = tokenize(txt);//tokenize into words and phrases                        

            wordList = new StopWordRemoval().removeStopWords(words); //remove stop words

            wordList = stemmer(wordList);//stem words

            //String[] posTags = posme.tag(wordList.toArray(new String[0]));
            logger.info("DONE");
        } catch (Exception e) {
        }
        return wordList;
    }

    public static ArrayList<String> stemmer(ArrayList<String> wordList) {
        PorterStemmer stemmer = new PorterStemmer();
        for (int wl = 0; wl < wordList.size(); wl++) {
            //logger.info("OLD WORD: " + wordList.get(wl));
            ArrayList<String> subWordList = new ArrayList();
            subWordList.addAll(Arrays.asList(wordList.get(wl).split(" ")));
            for (int swl = 0; swl < subWordList.size(); swl++) {
                String word = subWordList.get(swl);
                stemmer.setCurrent(word); //set string you need to stem
                stemmer.stem();
                String stemmedWord = stemmer.getCurrent();
                subWordList.set(swl, stemmedWord);
            }
            wordList.set(wl, StringUtils.join(subWordList, " "));
            //logger.info("NEW WORD: " + wordList.get(wl));
        }

        return wordList;

    }

    public static String[] tokenize(String text) {

        String[] chunkStrings = null;

        try {
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

    public static List<String> ngram(List<String> input, int n, String separator) {
        if (input.size() <= n) {
            return input;
        }
        List<String> outGrams = new ArrayList<String>();
        for (int i = 0; i < input.size() - (n - 2); i++) {
            String gram = "";
            if ((i + n) <= input.size()) {
                for (int x = i; x < (n + i); x++) {
                    gram += input.get(x) + separator;
                }
                gram = gram.substring(0, gram.lastIndexOf(separator));
                outGrams.add(gram);
            }
        }
        return outGrams;
    }
}


package opennlp;

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

/**
 *
 * Extracts noun phrases from a sentence. To create sentences using OpenNLP use
 * the SentenceDetector classes.
 */
public class OpenNLPNounPhraseExtractor {

  static final int N = 2;

  public static void main(String[] args) {

    try {
      String modelPath = "src/models/";
      TokenizerModel tm = new TokenizerModel(new FileInputStream(new File(modelPath + "en-token.bin")));
      TokenizerME wordBreaker = new TokenizerME(tm);
      POSModel pm = new POSModel(new FileInputStream(new File(modelPath + "en-pos-maxent.bin")));
      POSTaggerME posme = new POSTaggerME(pm);
      InputStream modelIn = new FileInputStream(modelPath + "en-chunker.bin");
      ChunkerModel chunkerModel = new ChunkerModel(modelIn);
      ChunkerME chunkerME = new ChunkerME(chunkerModel);
      //this is your sentence
      String sentence = "Barack Hussein Obama II  is the 44th and current President of the United States, and the first African American to hold the office.";
      //words is the tokenized sentence
      String[] words = wordBreaker.tokenize(sentence);
      //posTags are the parts of speech of every word in the sentence (The chunker needs this info of course)
      String[] posTags = posme.tag(words);
      //chunks are the start end "spans" indices to the chunks in the words array
      Span[] chunks = chunkerME.chunkAsSpans(words, posTags);
      //chunkStrings are the actual chunks
      String[] chunkStrings = Span.spansToStrings(chunks, words);
      for (int i = 0; i < chunks.length; i++) {
        if (chunks[i].getType().equals("NP")) {
          System.out.println("NP: \n\t" + chunkStrings[i]);
          String[] split = chunkStrings[i].split(" ");

          List<String> ngrams = ngram(Arrays.asList(split), N, " ");
          System.out.println("ngrams:");
          for (String gram : ngrams) {
            System.out.println("\t" + gram);
          }

        }
      }


    } catch (IOException e) {
    }
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
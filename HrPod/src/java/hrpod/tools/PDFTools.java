/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrpod.tools;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 *
 * @author admin
 */
public class PDFTools {
    final static Logger logger = Logger.getLogger(PDFTools.class);
    private PDFParser parser;
    private PDFTextStripper pdfStripper;
    private PDDocument pdDoc;
    private COSDocument cosDoc;

    public PDFTools() {

    }

    public String getStringFromPDF(String filePath) {
        
        String text = null;
        
        try {            
            URL url = this.getClass().getResource(filePath); 
            File file = new File(url.getFile());
            parser = new PDFParser(new RandomAccessFile(file, "r"));

            parser.parse();
            cosDoc = parser.getDocument();
            pdfStripper = new PDFTextStripper();
            pdDoc = new PDDocument(cosDoc);
            pdDoc.getNumberOfPages();
            pdfStripper.setStartPage(1);
            //pdfStripper.setEndPage(10);

            pdfStripper.setEndPage(pdDoc.getNumberOfPages());

            text = pdfStripper.getText(pdDoc);

        } catch (IOException e) {
            logger.error("IO ERROR", e);
        } catch(Exception ex){
            logger.error("ERROR", ex);
        }
        
        
        return text;
    }
}

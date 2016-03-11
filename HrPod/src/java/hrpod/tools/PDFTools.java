/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrpod.tools;

import java.io.IOException;
import java.io.InputStream;
import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;



/**
 *
 * @author admin
 */
public class PDFTools {

    final static Logger logger = Logger.getLogger(PDFTools.class);

    public PDFTools() {

    }

    private PDFParser getParser(InputStream inputStream) {
        PDFParser parser = null;
        try {
            parser = new PDFParser(inputStream);
            parser.parse();
        } catch (IOException e) {
            logger.error("IO ERROR", e);
        } catch (Exception ex) {
            logger.error("ERROR", ex);
        }finally{
            try {
                inputStream.close();
            } catch (Exception e) {
                logger.error("ERROR Closing stream:", e);
            }
        }
        return parser;
    }

    public String getStringFromPDF(InputStream inputStream) {

        String text = null;

        try {
            COSDocument cosDoc = getParser(inputStream).getDocument();
            PDFTextStripper pdfStripper = new PDFTextStripper();
            PDDocument pdDoc = new PDDocument(cosDoc);
            pdfStripper.setStartPage(1);
            pdfStripper.setEndPage(pdDoc.getNumberOfPages());

            text = pdfStripper.getText(pdDoc);

        } catch (Exception ex) {
            logger.error("ERROR", ex);
        }

        return text;
    }

    public String[] getPagesFromPDF(InputStream inputStream) {
        String[] pages = null;
        try {
            COSDocument cosDoc = getParser(inputStream).getDocument();
            PDFTextStripper pdfStripper = new PDFTextStripper();
            PDDocument pdDoc = new PDDocument(cosDoc);
            int pagesCount = pdDoc.getNumberOfPages();
            pages = new String[pagesCount];
            
            for(int p = 1; p < pagesCount; p++){
                pdfStripper.setStartPage(p);
                pdfStripper.setEndPage(p+1);
                pages[p-1] = pdfStripper.getText(pdDoc);
            }

        } catch (IOException e) {
            logger.error("IO ERROR", e);
        } catch (Exception ex) {
            logger.error("ERROR", ex);
        }
        return pages;
    }
}

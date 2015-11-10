/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/31262460/how-to-add-x-offset-to-text-pattern-with-every-x-step-itext
 */
package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.basics.font.FontConstants;
import com.itextpdf.canvas.PdfCanvas;
import com.itextpdf.canvas.PdfPatternCanvas;
import com.itextpdf.core.color.PatternColor;
import com.itextpdf.core.font.PdfFont;
import com.itextpdf.core.pdf.PdfDocument;
import com.itextpdf.core.pdf.PdfWriter;
import com.itextpdf.core.pdf.colorspace.PdfPattern;
import com.itextpdf.core.testutils.annotations.type.SampleTest;
import com.itextpdf.model.Document;
import com.itextpdf.samples.GenericTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class TextPattern extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/objects/text_pattern.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new TextPattern().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(new FileOutputStream(DEST)));
        Document doc = new Document(pdfDoc);

        PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());
        PdfFont font = PdfFont.createStandardFont(pdfDoc, FontConstants.HELVETICA);
        String filltext = "this is the fill text! ";
        float filltextWidth = font.getWidthPoint(filltext, 6);

        PdfPattern.Tiling tilingPattern = new PdfPattern.Tiling(filltextWidth, 60, filltextWidth, 60);
        PdfPatternCanvas patternCanvas = new PdfPatternCanvas(tilingPattern, pdfDoc);
        patternCanvas.beginText().setFontAndSize(font, 6.f);
        float x = 0;
        for (float y = 0; y < 60; y += 10) {
            patternCanvas.setTextMatrix(x - filltextWidth, y);
            patternCanvas.showText(filltext);
            patternCanvas.setTextMatrix(x, y);
            patternCanvas.showText(filltext);
            x += (filltextWidth / 6);
        }
        patternCanvas.endText();

        canvas.rectangle(0, 0, 595, 842);
        canvas.setFillColor(new PatternColor(tilingPattern));
        canvas.fill();

        doc.close();
    }
}
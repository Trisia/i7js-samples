/**
 * Example written by Bruno Lowagie.
 */
package com.itextpdf.samples.sandbox.fonts;

import com.itextpdf.canvas.PdfCanvas;
import com.itextpdf.canvas.PdfCanvasConstants;
import com.itextpdf.core.color.DeviceRgb;
import com.itextpdf.canvas.font.PdfType3Font;
import com.itextpdf.core.pdf.PdfDocument;
import com.itextpdf.core.pdf.PdfPage;
import com.itextpdf.core.pdf.PdfWriter;
import com.itextpdf.core.testutils.annotations.type.SampleTest;
import com.itextpdf.model.Document;
import com.itextpdf.model.element.Paragraph;
import com.itextpdf.samples.GenericTest;

import java.io.File;
import java.io.FileOutputStream;

import org.junit.Ignore;
import org.junit.experimental.categories.Category;

@Ignore
@Category(SampleTest.class)
public class Logo extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/fonts/logo.pdf";
    public static final String FONT = "./src/test/resources/sandbox/fonts/FreeSans.ttf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new Logo().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(new FileOutputStream(DEST)));
        Document doc = new Document(pdfDoc);

        float linewidth = 125;

        PdfType3Font t3 = new PdfType3Font(pdfDoc, true);
        PdfCanvas i = t3.createGlyph('I', 700, 0, 0, 1200, 600);
        i.setLineWidth(10);
        i.setStrokeColor(new DeviceRgb(0xf9, 0x9d, 0x25));
        i.setLineWidth(linewidth);
        i.setLineCapStyle(PdfCanvasConstants.LineCapStyle.ROUND);
        i.moveTo(600, 36);
        i.lineTo(600, 564);
        i.stroke();

        PdfCanvas t = t3.createGlyph('T', 1170, 0, 0, 1200, 600);
        t.setLineWidth(10);
        t.setStrokeColor(new DeviceRgb(0x08, 0x49, 0x75));
        t.setLineWidth(linewidth);
        t.setLineCapStyle(PdfCanvasConstants.LineCapStyle.ROUND);
        t.moveTo(144, 564);
        t.lineTo(1056, 564);
        t.moveTo(600, 36);
        t.lineTo(600, 564);
        t.stroke();

        PdfCanvas e = t3.createGlyph('E', 1150, 0, 0, 1200, 600);
        e.setLineWidth(10);
        e.setStrokeColor(new DeviceRgb(0xf8, 0x9b, 0x22));
        e.setLineWidth(linewidth);
        e.setLineCapStyle(PdfCanvasConstants.LineCapStyle.ROUND);
        e.moveTo(144, 36);
        e.lineTo(1056, 36);
        e.moveTo(144, 300);
        e.lineTo(1056, 300);
        e.moveTo(144, 564);
        e.lineTo(1056, 564);
        e.stroke();

        PdfCanvas x = t3.createGlyph('X', 1160, 0, 0, 1200, 600);
        x.setStrokeColor(new DeviceRgb(0x10, 0x46, 0x75));
        x.setLineWidth(10);
        x.setLineWidth(linewidth);
        x.setLineCapStyle(PdfCanvasConstants.LineCapStyle.ROUND);
        x.moveTo(144, 36);
        x.lineTo(1056, 564);
        x.moveTo(144, 564);
        x.lineTo(1056, 36);
        x.stroke();

        Paragraph p = new Paragraph("ITEXT").setFont(t3).setFontSize(20);

        PdfPage page = pdfDoc.addNewPage();
        PdfCanvas canvas = new PdfCanvas(page);
        canvas.saveState()
                .beginText()
                .setFontAndSize(t3, 20)
                .moveText(50, 800)
                .showText("ITEXT")
                        // TODO showText doesn't notice "\n"
                .showText("I\nT\nE\nX\nT")
                .endText();
        // TODO Document.add on type3-fonted text throws NullPointerException: no encoding
        doc.add(p);
        p = new Paragraph("I\nT\nE\nX\nT").setFixedLeading(20).setFont(t3).setFontSize(20);
        doc.add(p);

        pdfDoc.close();
    }
}
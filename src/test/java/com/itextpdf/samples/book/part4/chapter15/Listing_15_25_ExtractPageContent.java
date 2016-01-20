package com.itextpdf.samples.book.part4.chapter15;

import com.itextpdf.basics.io.ByteArrayOutputStream;
import com.itextpdf.core.parser.EventData;
import com.itextpdf.core.parser.EventListener;
import com.itextpdf.core.parser.EventType;
import com.itextpdf.core.parser.LineSegment;
import com.itextpdf.core.parser.PdfContentStreamProcessor;
import com.itextpdf.core.parser.TextRenderInfo;
import com.itextpdf.core.parser.Vector;
import com.itextpdf.core.pdf.PdfDocument;
import com.itextpdf.core.pdf.PdfReader;
import com.itextpdf.core.pdf.PdfWriter;
import com.itextpdf.core.testutils.annotations.type.SampleTest;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Set;

import org.junit.Ignore;
import org.junit.experimental.categories.Category;
import org.xml.sax.SAXException;

@Ignore
@Category(SampleTest.class)
public class Listing_15_25_ExtractPageContent {
    public static final String DEST = "./samples/target/test/resources/book/part4/chapter15/Listing_15_25_ExtractPageContent.txt";
    public static final String PREFACE = "./samples/src/test/resources/book/part4/chapter15/preface.pdf";
    public static void main(String args[]) throws IOException, SQLException, ParserConfigurationException, SAXException {
        new Listing_15_25_ExtractPageContent().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException, ParserConfigurationException, SAXException {
        parsePdf(PREFACE, dest);
    }

    /**
     * Parses a PDF to a plain text file.
     * @param src the original PDF
     * @param txt the resulting text
     * @throws IOException
     */
    public void parsePdf(String src, String txt) throws IOException {
        // TODO No PdfReaderContentParser
        // PdfReaderContentParser parser = new PdfReaderContentParser(reader);
        PrintWriter out = new PrintWriter(new FileOutputStream(txt));
        // TODO No predefined strategies like TextExtractionStrategy here
        // TextExtractionStrategy strategy;
        // for (int i = 1; i <= reader.getNumberOfPages(); i++) {
        //     strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
        //     out.println(strategy.getResultantText());
        // }
        // out.flush();
        // out.close();
        // reader.close();
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src), new PdfWriter(new ByteArrayOutputStream()));
        SimpleTextStrategyListener listener = new SimpleTextStrategyListener();
        PdfContentStreamProcessor parser = new PdfContentStreamProcessor(listener);
        for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
            parser.processPageContent(pdfDoc.getPage(i));
            out.println(listener.getResultantText());
        }
        out.flush();
        out.close();
        pdfDoc.close();

    }


    class SimpleTextStrategyListener implements EventListener {
        private Vector lastStart;
        private Vector lastEnd;
        private final StringBuffer result = new StringBuffer();

        public String getResultantText() {
            return this.result.toString();
        }

        public void eventOccurred(EventData data, EventType type) {
            switch (type) {
                case RENDER_TEXT:
                    TextRenderInfo renderInfo = (TextRenderInfo) data;
                    boolean firstRender = this.result.length() == 0;
                    boolean hardReturn = false;
                    LineSegment segment = renderInfo.getBaseline();
                    Vector start = segment.getStartPoint();
                    Vector end = segment.getEndPoint();
                    if(!firstRender) {
                        Vector x1 = this.lastStart;
                        Vector x2 = this.lastEnd;
                        float dist = x2.subtract(x1).cross(x1.subtract(start)).lengthSquared() / x2.subtract(x1).lengthSquared();
                        float sameLineThreshold = 1.0F;
                        if(dist > sameLineThreshold) {
                            hardReturn = true;
                        }
                    }

                    if(hardReturn) {
                        this.appendTextChunk("\n");
                    } else if(!firstRender && this.result.charAt(this.result.length() - 1) != 32 && renderInfo.getText().length() > 0 && renderInfo.getText().charAt(0) != 32) {
                        float spacing = this.lastEnd.subtract(start).length();
                        if(spacing > renderInfo.getSingleSpaceWidth() / 2.0F) {
                            this.appendTextChunk(" ");
                        }
                    }

                    this.appendTextChunk(renderInfo.getText());
                    this.lastStart = start;
                    this.lastEnd = end;
                    break;
                default:
                    break;
            }
        }

        protected final void appendTextChunk(CharSequence text) {
            this.result.append(text);
        }


        public Set<EventType> getSupportedEvents() {
            return null;
        }
    }
}
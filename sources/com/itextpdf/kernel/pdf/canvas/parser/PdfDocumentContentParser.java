package com.itextpdf.kernel.pdf.canvas.parser;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.canvas.parser.listener.IEventListener;
import java.util.HashMap;
import java.util.Map;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/PdfDocumentContentParser.class */
public class PdfDocumentContentParser {
    private final PdfDocument pdfDocument;

    public PdfDocumentContentParser(PdfDocument pdfDocument) {
        this.pdfDocument = pdfDocument;
    }

    public <E extends IEventListener> E processContent(int pageNumber, E renderListener, Map<String, IContentOperator> additionalContentOperators) {
        PdfCanvasProcessor processor = new PdfCanvasProcessor(renderListener, additionalContentOperators);
        processor.processPageContent(this.pdfDocument.getPage(pageNumber));
        return renderListener;
    }

    public <E extends IEventListener> E processContent(int i, E e) {
        return (E) processContent(i, e, new HashMap());
    }
}

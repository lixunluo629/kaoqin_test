package com.itextpdf.kernel.pdf.canvas.parser;

import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.parser.listener.ITextExtractionStrategy;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;
import java.util.HashMap;
import java.util.Map;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/PdfTextExtractor.class */
public final class PdfTextExtractor {
    private PdfTextExtractor() {
    }

    public static String getTextFromPage(PdfPage page, ITextExtractionStrategy strategy, Map<String, IContentOperator> additionalContentOperators) {
        PdfCanvasProcessor parser = new PdfCanvasProcessor(strategy, additionalContentOperators);
        parser.processPageContent(page);
        return strategy.getResultantText();
    }

    public static String getTextFromPage(PdfPage page, ITextExtractionStrategy strategy) {
        return getTextFromPage(page, strategy, new HashMap());
    }

    public static String getTextFromPage(PdfPage page) {
        return getTextFromPage(page, new LocationTextExtractionStrategy());
    }
}

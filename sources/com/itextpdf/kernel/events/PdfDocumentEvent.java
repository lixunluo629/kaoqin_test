package com.itextpdf.kernel.events;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/events/PdfDocumentEvent.class */
public class PdfDocumentEvent extends Event {
    public static final String START_PAGE = "StartPdfPage";
    public static final String INSERT_PAGE = "InsertPdfPage";
    public static final String REMOVE_PAGE = "RemovePdfPage";
    public static final String END_PAGE = "EndPdfPage";
    protected PdfPage page;
    private PdfDocument document;

    public PdfDocumentEvent(String type, PdfDocument document) {
        super(type);
        this.document = document;
    }

    public PdfDocumentEvent(String type, PdfPage page) {
        super(type);
        this.page = page;
        this.document = page.getDocument();
    }

    public PdfDocument getDocument() {
        return this.document;
    }

    public PdfPage getPage() {
        return this.page;
    }
}

package com.itextpdf.kernel.utils;

import com.itextpdf.kernel.pdf.PdfDocument;
import java.util.ArrayList;
import java.util.List;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/utils/PdfMerger.class */
public class PdfMerger {
    private PdfDocument pdfDocument;
    private boolean closeSrcDocuments;
    private boolean mergeTags;
    private boolean mergeOutlines;

    public PdfMerger(PdfDocument pdfDocument) {
        this(pdfDocument, true, true);
    }

    public PdfMerger(PdfDocument pdfDocument, boolean mergeTags, boolean mergeOutlines) {
        this.pdfDocument = pdfDocument;
        this.mergeTags = mergeTags;
        this.mergeOutlines = mergeOutlines;
    }

    public PdfMerger setCloseSourceDocuments(boolean closeSourceDocuments) {
        this.closeSrcDocuments = closeSourceDocuments;
        return this;
    }

    public PdfMerger merge(PdfDocument from, int fromPage, int toPage) {
        List<Integer> pages = new ArrayList<>(toPage - fromPage);
        for (int pageNum = fromPage; pageNum <= toPage; pageNum++) {
            pages.add(Integer.valueOf(pageNum));
        }
        return merge(from, pages);
    }

    public PdfMerger merge(PdfDocument from, List<Integer> pages) {
        if (this.mergeTags && from.isTagged()) {
            this.pdfDocument.setTagged();
        }
        if (this.mergeOutlines && from.hasOutlines()) {
            this.pdfDocument.initializeOutlines();
        }
        from.copyPagesTo(pages, this.pdfDocument);
        if (this.closeSrcDocuments) {
            from.close();
        }
        return this;
    }

    public void close() {
        this.pdfDocument.close();
    }
}

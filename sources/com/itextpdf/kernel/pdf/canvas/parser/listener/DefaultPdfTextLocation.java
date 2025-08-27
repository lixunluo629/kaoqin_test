package com.itextpdf.kernel.pdf.canvas.parser.listener;

import com.itextpdf.kernel.geom.Rectangle;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/listener/DefaultPdfTextLocation.class */
public class DefaultPdfTextLocation implements IPdfTextLocation {
    private int pageNr;
    private Rectangle rectangle;
    private String text;

    public DefaultPdfTextLocation(int pageNr, Rectangle rect, String text) {
        this.pageNr = pageNr;
        this.rectangle = rect;
        this.text = text;
    }

    @Override // com.itextpdf.kernel.pdf.canvas.parser.listener.IPdfTextLocation
    public Rectangle getRectangle() {
        return this.rectangle;
    }

    public DefaultPdfTextLocation setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
        return this;
    }

    @Override // com.itextpdf.kernel.pdf.canvas.parser.listener.IPdfTextLocation
    public String getText() {
        return this.text;
    }

    public DefaultPdfTextLocation setText(String text) {
        this.text = text;
        return this;
    }

    @Override // com.itextpdf.kernel.pdf.canvas.parser.listener.IPdfTextLocation
    public int getPageNumber() {
        return this.pageNr;
    }

    public DefaultPdfTextLocation setPageNr(int pageNr) {
        this.pageNr = pageNr;
        return this;
    }
}

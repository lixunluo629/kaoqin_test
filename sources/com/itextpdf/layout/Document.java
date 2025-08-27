package com.itextpdf.layout;

import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.element.ILargeElement;
import com.itextpdf.layout.renderer.DocumentRenderer;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.RootRenderer;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/Document.class */
public class Document extends RootElement<Document> {

    @Deprecated
    protected float leftMargin;

    @Deprecated
    protected float rightMargin;

    @Deprecated
    protected float topMargin;

    @Deprecated
    protected float bottomMargin;

    public Document(PdfDocument pdfDoc) {
        this(pdfDoc, pdfDoc.getDefaultPageSize());
    }

    public Document(PdfDocument pdfDoc, PageSize pageSize) {
        this(pdfDoc, pageSize, true);
    }

    public Document(PdfDocument pdfDoc, PageSize pageSize, boolean immediateFlush) {
        this.leftMargin = 36.0f;
        this.rightMargin = 36.0f;
        this.topMargin = 36.0f;
        this.bottomMargin = 36.0f;
        this.pdfDocument = pdfDoc;
        this.pdfDocument.setDefaultPageSize(pageSize);
        this.immediateFlush = immediateFlush;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        if (this.rootRenderer != null) {
            this.rootRenderer.close();
        }
        this.pdfDocument.close();
    }

    public Document add(AreaBreak areaBreak) {
        checkClosingStatus();
        this.childElements.add(areaBreak);
        ensureRootRendererNotNull().addChild(areaBreak.createRendererSubTree());
        if (this.immediateFlush) {
            this.childElements.remove(this.childElements.size() - 1);
        }
        return this;
    }

    @Override // com.itextpdf.layout.RootElement
    public Document add(IBlockElement element) {
        checkClosingStatus();
        super.add(element);
        if (element instanceof ILargeElement) {
            ((ILargeElement) element).setDocument(this);
            ((ILargeElement) element).flushContent();
        }
        return this;
    }

    public PdfDocument getPdfDocument() {
        return this.pdfDocument;
    }

    public void setRenderer(DocumentRenderer documentRenderer) {
        this.rootRenderer = documentRenderer;
    }

    public void flush() {
        this.rootRenderer.flush();
    }

    public void relayout() {
        if (this.immediateFlush) {
            throw new IllegalStateException("Operation not supported with immediate flush");
        }
        IRenderer nextRelayoutRenderer = this.rootRenderer != null ? this.rootRenderer.getNextRenderer() : null;
        if (nextRelayoutRenderer == null || !(nextRelayoutRenderer instanceof RootRenderer)) {
            nextRelayoutRenderer = new DocumentRenderer(this, this.immediateFlush);
        }
        while (this.pdfDocument.getNumberOfPages() > 0) {
            this.pdfDocument.removePage(this.pdfDocument.getNumberOfPages());
        }
        this.rootRenderer = (RootRenderer) nextRelayoutRenderer;
        for (IElement element : this.childElements) {
            createAndAddRendererSubTree(element);
        }
    }

    public float getLeftMargin() {
        Float property = (Float) getProperty(44);
        return (property != null ? property : (Float) getDefaultProperty(44)).floatValue();
    }

    public void setLeftMargin(float leftMargin) {
        setProperty(44, Float.valueOf(leftMargin));
        this.leftMargin = leftMargin;
    }

    public float getRightMargin() {
        Float property = (Float) getProperty(45);
        return (property != null ? property : (Float) getDefaultProperty(45)).floatValue();
    }

    public void setRightMargin(float rightMargin) {
        setProperty(45, Float.valueOf(rightMargin));
        this.rightMargin = rightMargin;
    }

    public float getTopMargin() {
        Float property = (Float) getProperty(46);
        return (property != null ? property : (Float) getDefaultProperty(46)).floatValue();
    }

    public void setTopMargin(float topMargin) {
        setProperty(46, Float.valueOf(topMargin));
        this.topMargin = topMargin;
    }

    public float getBottomMargin() {
        Float property = (Float) getProperty(43);
        return (property != null ? property : (Float) getDefaultProperty(43)).floatValue();
    }

    public void setBottomMargin(float bottomMargin) {
        setProperty(43, Float.valueOf(bottomMargin));
        this.bottomMargin = bottomMargin;
    }

    public void setMargins(float topMargin, float rightMargin, float bottomMargin, float leftMargin) {
        setTopMargin(topMargin);
        setRightMargin(rightMargin);
        setBottomMargin(bottomMargin);
        setLeftMargin(leftMargin);
    }

    public Rectangle getPageEffectiveArea(PageSize pageSize) {
        float x = pageSize.getLeft() + getLeftMargin();
        float y = pageSize.getBottom() + getBottomMargin();
        float width = (pageSize.getWidth() - getLeftMargin()) - getRightMargin();
        float height = (pageSize.getHeight() - getBottomMargin()) - getTopMargin();
        return new Rectangle(x, y, width, height);
    }

    @Override // com.itextpdf.layout.RootElement, com.itextpdf.layout.ElementPropertyContainer, com.itextpdf.layout.IPropertyContainer
    public <T1> T1 getDefaultProperty(int i) {
        switch (i) {
            case 43:
            case 44:
            case 45:
            case 46:
                return (T1) Float.valueOf(36.0f);
            default:
                return (T1) super.getDefaultProperty(i);
        }
    }

    @Override // com.itextpdf.layout.RootElement
    protected RootRenderer ensureRootRendererNotNull() {
        if (this.rootRenderer == null) {
            this.rootRenderer = new DocumentRenderer(this, this.immediateFlush);
        }
        return this.rootRenderer;
    }

    protected void checkClosingStatus() {
        if (getPdfDocument().isClosed()) {
            throw new PdfException(PdfException.DocumentClosedItIsImpossibleToExecuteAction);
        }
    }
}

package com.itextpdf.layout;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.renderer.CanvasRenderer;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.RootRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/Canvas.class */
public class Canvas extends RootElement<Canvas> {
    protected PdfCanvas pdfCanvas;
    protected Rectangle rootArea;
    protected PdfPage page;
    private boolean isCanvasOfPage;

    public Canvas(PdfPage page, Rectangle rootArea) {
        this(initPdfCanvasOrThrowIfPageIsFlushed(page), page.getDocument(), rootArea);
        enableAutoTagging(page);
        this.isCanvasOfPage = true;
    }

    public Canvas(PdfCanvas pdfCanvas, PdfDocument pdfDocument, Rectangle rootArea) {
        this.pdfDocument = pdfDocument;
        this.pdfCanvas = pdfCanvas;
        this.rootArea = rootArea;
    }

    public Canvas(PdfCanvas pdfCanvas, PdfDocument pdfDocument, Rectangle rootArea, boolean immediateFlush) {
        this(pdfCanvas, pdfDocument, rootArea);
        this.immediateFlush = immediateFlush;
    }

    public Canvas(PdfFormXObject formXObject, PdfDocument pdfDocument) {
        this(new PdfCanvas(formXObject, pdfDocument), pdfDocument, formXObject.getBBox().toRectangle());
    }

    public PdfDocument getPdfDocument() {
        return this.pdfDocument;
    }

    public Rectangle getRootArea() {
        return this.rootArea;
    }

    public PdfCanvas getPdfCanvas() {
        return this.pdfCanvas;
    }

    public void setRenderer(CanvasRenderer canvasRenderer) {
        this.rootRenderer = canvasRenderer;
    }

    public PdfPage getPage() {
        return this.page;
    }

    public void enableAutoTagging(PdfPage page) {
        if (isCanvasOfPage() && this.page != page) {
            Logger logger = LoggerFactory.getLogger((Class<?>) Canvas.class);
            logger.error(LogMessageConstant.PASSED_PAGE_SHALL_BE_ON_WHICH_CANVAS_WILL_BE_RENDERED);
        }
        this.page = page;
    }

    public boolean isAutoTaggingEnabled() {
        return this.page != null;
    }

    public boolean isCanvasOfPage() {
        return this.isCanvasOfPage;
    }

    public void relayout() {
        if (this.immediateFlush) {
            throw new IllegalStateException("Operation not supported with immediate flush");
        }
        IRenderer nextRelayoutRenderer = this.rootRenderer != null ? this.rootRenderer.getNextRenderer() : null;
        if (nextRelayoutRenderer == null || !(nextRelayoutRenderer instanceof RootRenderer)) {
            nextRelayoutRenderer = new CanvasRenderer(this, this.immediateFlush);
        }
        this.rootRenderer = (RootRenderer) nextRelayoutRenderer;
        for (IElement element : this.childElements) {
            createAndAddRendererSubTree(element);
        }
    }

    public void flush() {
        this.rootRenderer.flush();
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        if (this.rootRenderer != null) {
            this.rootRenderer.close();
        }
    }

    @Override // com.itextpdf.layout.RootElement
    protected RootRenderer ensureRootRendererNotNull() {
        if (this.rootRenderer == null) {
            this.rootRenderer = new CanvasRenderer(this, this.immediateFlush);
        }
        return this.rootRenderer;
    }

    private static PdfCanvas initPdfCanvasOrThrowIfPageIsFlushed(PdfPage page) {
        if (page.isFlushed()) {
            throw new PdfException(PdfException.CannotDrawElementsOnAlreadyFlushedPages);
        }
        return new PdfCanvas(page);
    }
}

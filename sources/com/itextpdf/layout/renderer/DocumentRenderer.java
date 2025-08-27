package com.itextpdf.layout.renderer;

import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.layout.RootLayoutArea;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.Transform;
import com.itextpdf.layout.tagging.LayoutTaggingHelper;
import java.util.ArrayList;
import java.util.List;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/renderer/DocumentRenderer.class */
public class DocumentRenderer extends RootRenderer {
    protected Document document;
    protected List<Integer> wrappedContentPage;

    public DocumentRenderer(Document document) {
        this(document, true);
    }

    public DocumentRenderer(Document document, boolean immediateFlush) {
        this.wrappedContentPage = new ArrayList();
        this.document = document;
        this.immediateFlush = immediateFlush;
        this.modelElement = document;
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer, com.itextpdf.layout.renderer.IRenderer
    public LayoutArea getOccupiedArea() {
        throw new IllegalStateException("Not applicable for DocumentRenderer");
    }

    @Override // com.itextpdf.layout.renderer.IRenderer
    public IRenderer getNextRenderer() {
        return new DocumentRenderer(this.document, this.immediateFlush);
    }

    @Override // com.itextpdf.layout.renderer.RootRenderer
    protected LayoutArea updateCurrentArea(LayoutResult overflowResult) {
        flushWaitingDrawingElements();
        LayoutTaggingHelper taggingHelper = (LayoutTaggingHelper) getProperty(108);
        if (taggingHelper != null) {
            taggingHelper.releaseFinishedHints();
        }
        AreaBreak areaBreak = (overflowResult == null || overflowResult.getAreaBreak() == null) ? null : overflowResult.getAreaBreak();
        if (areaBreak != null && areaBreak.getType() == AreaBreakType.LAST_PAGE) {
            while (this.currentPageNumber < this.document.getPdfDocument().getNumberOfPages()) {
                moveToNextPage();
            }
        } else {
            moveToNextPage();
        }
        PageSize customPageSize = areaBreak != null ? areaBreak.getPageSize() : null;
        while (this.document.getPdfDocument().getNumberOfPages() >= this.currentPageNumber && this.document.getPdfDocument().getPage(this.currentPageNumber).isFlushed()) {
            this.currentPageNumber++;
        }
        PageSize lastPageSize = ensureDocumentHasNPages(this.currentPageNumber, customPageSize);
        if (lastPageSize == null) {
            lastPageSize = new PageSize(this.document.getPdfDocument().getPage(this.currentPageNumber).getTrimBox());
        }
        RootLayoutArea rootLayoutArea = new RootLayoutArea(this.currentPageNumber, getCurrentPageEffectiveArea(lastPageSize));
        this.currentArea = rootLayoutArea;
        return rootLayoutArea;
    }

    @Override // com.itextpdf.layout.renderer.RootRenderer
    protected void flushSingleRenderer(IRenderer resultRenderer) {
        Transform transformProp = (Transform) resultRenderer.getProperty(53);
        if (!this.waitingDrawingElements.contains(resultRenderer)) {
            processWaitingDrawing(resultRenderer, transformProp, this.waitingDrawingElements);
            if (FloatingHelper.isRendererFloating(resultRenderer) || transformProp != null) {
                return;
            }
        }
        if (!resultRenderer.isFlushed() && null != resultRenderer.getOccupiedArea()) {
            int pageNum = resultRenderer.getOccupiedArea().getPageNumber();
            PdfDocument pdfDocument = this.document.getPdfDocument();
            ensureDocumentHasNPages(pageNum, null);
            PdfPage correspondingPage = pdfDocument.getPage(pageNum);
            if (correspondingPage.isFlushed()) {
                throw new PdfException(PdfException.CannotDrawElementsOnAlreadyFlushedPages);
            }
            boolean wrapOldContent = pdfDocument.getReader() != null && pdfDocument.getWriter() != null && correspondingPage.getContentStreamCount() > 0 && correspondingPage.getLastContentStream().getLength() > 0 && !this.wrappedContentPage.contains(Integer.valueOf(pageNum)) && pdfDocument.getNumberOfPages() >= pageNum;
            this.wrappedContentPage.add(Integer.valueOf(pageNum));
            if (pdfDocument.isTagged()) {
                pdfDocument.getTagStructureContext().getAutoTaggingPointer().setPageForTagging(correspondingPage);
            }
            resultRenderer.draw(new DrawContext(pdfDocument, new PdfCanvas(correspondingPage, wrapOldContent), pdfDocument.isTagged()));
        }
    }

    protected PageSize addNewPage(PageSize customPageSize) {
        if (customPageSize != null) {
            this.document.getPdfDocument().addNewPage(customPageSize);
        } else {
            this.document.getPdfDocument().addNewPage();
        }
        return customPageSize != null ? customPageSize : this.document.getPdfDocument().getDefaultPageSize();
    }

    private PageSize ensureDocumentHasNPages(int n, PageSize customPageSize) {
        PageSize pageSizeAddNewPage = null;
        while (true) {
            PageSize lastPageSize = pageSizeAddNewPage;
            if (this.document.getPdfDocument().getNumberOfPages() < n) {
                pageSizeAddNewPage = addNewPage(customPageSize);
            } else {
                return lastPageSize;
            }
        }
    }

    private Rectangle getCurrentPageEffectiveArea(PageSize pageSize) {
        float leftMargin = getPropertyAsFloat(44).floatValue();
        float bottomMargin = getPropertyAsFloat(43).floatValue();
        float topMargin = getPropertyAsFloat(46).floatValue();
        float rightMargin = getPropertyAsFloat(45).floatValue();
        return new Rectangle(pageSize.getLeft() + leftMargin, pageSize.getBottom() + bottomMargin, (pageSize.getWidth() - leftMargin) - rightMargin, (pageSize.getHeight() - bottomMargin) - topMargin);
    }

    private void moveToNextPage() {
        if (this.immediateFlush && this.currentPageNumber > 1) {
            this.document.getPdfDocument().getPage(this.currentPageNumber - 1).flush();
        }
        this.currentPageNumber++;
    }
}

package com.itextpdf.layout.renderer;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.canvas.CanvasArtifact;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.draw.ILineDrawer;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/renderer/LineSeparatorRenderer.class */
public class LineSeparatorRenderer extends BlockRenderer {
    public LineSeparatorRenderer(LineSeparator lineSeparator) {
        super(lineSeparator);
    }

    @Override // com.itextpdf.layout.renderer.BlockRenderer, com.itextpdf.layout.renderer.IRenderer
    public LayoutResult layout(LayoutContext layoutContext) {
        Rectangle parentBBox = layoutContext.getArea().getBBox().mo825clone();
        if (getProperty(55) != null) {
            parentBBox.moveDown(1000000.0f - parentBBox.getHeight()).setHeight(1000000.0f);
        }
        ILineDrawer lineDrawer = (ILineDrawer) getProperty(35);
        float height = lineDrawer != null ? lineDrawer.getLineWidth() : 0.0f;
        this.occupiedArea = new LayoutArea(layoutContext.getArea().getPageNumber(), parentBBox.mo825clone());
        applyMargins(this.occupiedArea.getBBox(), false);
        Float calculatedWidth = retrieveWidth(layoutContext.getArea().getBBox().getWidth());
        if (calculatedWidth == null) {
            calculatedWidth = Float.valueOf(this.occupiedArea.getBBox().getWidth());
        }
        if ((this.occupiedArea.getBBox().getHeight() < height || this.occupiedArea.getBBox().getWidth() < calculatedWidth.floatValue()) && !hasOwnProperty(26)) {
            return new LayoutResult(3, null, null, this, this);
        }
        this.occupiedArea.getBBox().setWidth(calculatedWidth.floatValue()).moveUp(this.occupiedArea.getBBox().getHeight() - height).setHeight(height);
        applyMargins(this.occupiedArea.getBBox(), true);
        if (getProperty(55) != null) {
            applyRotationLayout(layoutContext.getArea().getBBox().mo825clone());
            if (isNotFittingLayoutArea(layoutContext.getArea()) && !Boolean.TRUE.equals(getPropertyAsBoolean(26))) {
                return new LayoutResult(3, null, null, this, this);
            }
        }
        return new LayoutResult(1, this.occupiedArea, this, null);
    }

    @Override // com.itextpdf.layout.renderer.IRenderer
    public IRenderer getNextRenderer() {
        return new LineSeparatorRenderer((LineSeparator) this.modelElement);
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer
    public void drawChildren(DrawContext drawContext) {
        ILineDrawer lineDrawer = (ILineDrawer) getProperty(35);
        if (lineDrawer != null) {
            PdfCanvas canvas = drawContext.getCanvas();
            boolean isTagged = drawContext.isTaggingEnabled();
            if (isTagged) {
                canvas.openTag(new CanvasArtifact());
            }
            Rectangle area = getOccupiedAreaBBox();
            applyMargins(area, false);
            lineDrawer.draw(canvas, area);
            if (isTagged) {
                canvas.closeTag();
            }
        }
    }
}

package com.itextpdf.layout.renderer;

import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.geom.AffineTransform;
import com.itextpdf.kernel.geom.Matrix;
import com.itextpdf.kernel.geom.NoninvertibleTransformException;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.IPropertyContainer;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.property.BorderCollapsePropertyValue;
import com.itextpdf.layout.property.UnitValue;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/renderer/CellRenderer.class */
public class CellRenderer extends BlockRenderer {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !CellRenderer.class.desiredAssertionStatus();
    }

    public CellRenderer(Cell modelElement) {
        super(modelElement);
        if (!$assertionsDisabled && modelElement == null) {
            throw new AssertionError();
        }
        setProperty(60, Integer.valueOf(modelElement.getRowspan()));
        setProperty(16, Integer.valueOf(modelElement.getColspan()));
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer, com.itextpdf.layout.renderer.IRenderer
    public IPropertyContainer getModelElement() {
        return super.getModelElement();
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer
    protected Float retrieveWidth(float parentBoxWidth) {
        return null;
    }

    @Override // com.itextpdf.layout.renderer.BlockRenderer
    protected AbstractRenderer createSplitRenderer(int layoutResult) {
        CellRenderer splitRenderer = (CellRenderer) getNextRenderer();
        splitRenderer.parent = this.parent;
        splitRenderer.modelElement = this.modelElement;
        splitRenderer.occupiedArea = this.occupiedArea;
        splitRenderer.isLastRendererForModelElement = false;
        splitRenderer.addAllProperties(getOwnProperties());
        return splitRenderer;
    }

    @Override // com.itextpdf.layout.renderer.BlockRenderer
    protected AbstractRenderer createOverflowRenderer(int layoutResult) {
        CellRenderer overflowRenderer = (CellRenderer) getNextRenderer();
        overflowRenderer.parent = this.parent;
        overflowRenderer.modelElement = this.modelElement;
        overflowRenderer.addAllProperties(getOwnProperties());
        return overflowRenderer;
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer
    public void drawBackground(DrawContext drawContext) {
        PdfCanvas canvas = drawContext.getCanvas();
        Matrix ctm = canvas.getGraphicsState().getCtm();
        Float angle = getPropertyAsFloat(55);
        boolean avoidRotation = null != angle && hasProperty(6);
        boolean restoreRotation = hasOwnProperty(55);
        if (avoidRotation) {
            try {
                AffineTransform transform = new AffineTransform(ctm.get(0), ctm.get(1), ctm.get(3), ctm.get(4), ctm.get(6), ctm.get(7)).createInverse();
                transform.concatenate(new AffineTransform());
                canvas.concatMatrix(transform);
                setProperty(55, null);
            } catch (NoninvertibleTransformException e) {
                throw new PdfException(PdfException.NoninvertibleMatrixCannotBeProcessed, (Throwable) e);
            }
        }
        super.drawBackground(drawContext);
        if (avoidRotation) {
            if (restoreRotation) {
                setProperty(55, angle);
            } else {
                deleteOwnProperty(55);
            }
            canvas.concatMatrix(new AffineTransform(ctm.get(0), ctm.get(1), ctm.get(3), ctm.get(4), ctm.get(6), ctm.get(7)));
        }
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer
    public void drawBorder(DrawContext drawContext) {
        if (BorderCollapsePropertyValue.SEPARATE.equals(this.parent.getProperty(114))) {
            super.drawBorder(drawContext);
        }
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer
    protected Rectangle applyBorderBox(Rectangle rect, Border[] borders, boolean reverse) {
        if (BorderCollapsePropertyValue.SEPARATE.equals(this.parent.getProperty(114))) {
            super.applyBorderBox(rect, borders, reverse);
        }
        return rect;
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer
    protected Rectangle applyMargins(Rectangle rect, UnitValue[] margins, boolean reverse) {
        if (BorderCollapsePropertyValue.SEPARATE.equals(this.parent.getProperty(114))) {
            applySpacings(rect, reverse);
        }
        return rect;
    }

    protected Rectangle applySpacings(Rectangle rect, boolean reverse) {
        float fFloatValue;
        if (BorderCollapsePropertyValue.SEPARATE.equals(this.parent.getProperty(114))) {
            Float verticalBorderSpacing = (Float) this.parent.getProperty(116);
            Float horizontalBorderSpacing = (Float) this.parent.getProperty(115);
            float[] cellSpacings = new float[4];
            for (int i = 0; i < cellSpacings.length; i++) {
                int i2 = i;
                if (0 == i % 2) {
                    fFloatValue = null != verticalBorderSpacing ? verticalBorderSpacing.floatValue() : 0.0f;
                } else {
                    fFloatValue = null != horizontalBorderSpacing ? horizontalBorderSpacing.floatValue() : 0.0f;
                }
                cellSpacings[i2] = fFloatValue;
            }
            applySpacings(rect, cellSpacings, reverse);
        }
        return rect;
    }

    protected Rectangle applySpacings(Rectangle rect, float[] spacings, boolean reverse) {
        if (BorderCollapsePropertyValue.SEPARATE.equals(this.parent.getProperty(114))) {
            rect.applyMargins(spacings[0] / 2.0f, spacings[1] / 2.0f, spacings[2] / 2.0f, spacings[3] / 2.0f, reverse);
        }
        return rect;
    }

    @Override // com.itextpdf.layout.renderer.IRenderer
    public IRenderer getNextRenderer() {
        return new CellRenderer((Cell) getModelElement());
    }
}

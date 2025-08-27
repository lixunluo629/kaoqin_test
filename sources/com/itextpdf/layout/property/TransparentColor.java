package com.itextpdf.layout.property;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/property/TransparentColor.class */
public class TransparentColor {
    private Color color;
    private float opacity;

    public TransparentColor(Color color) {
        this.color = color;
        this.opacity = 1.0f;
    }

    public TransparentColor(Color color, float opacity) {
        this.color = color;
        this.opacity = opacity;
    }

    public Color getColor() {
        return this.color;
    }

    public float getOpacity() {
        return this.opacity;
    }

    public void applyFillTransparency(PdfCanvas canvas) {
        applyTransparency(canvas, false);
    }

    public void applyStrokeTransparency(PdfCanvas canvas) {
        applyTransparency(canvas, true);
    }

    private void applyTransparency(PdfCanvas canvas, boolean isStroke) {
        if (isTransparent()) {
            PdfExtGState extGState = new PdfExtGState();
            if (isStroke) {
                extGState.setStrokeOpacity(this.opacity);
            } else {
                extGState.setFillOpacity(this.opacity);
            }
            canvas.setExtGState(extGState);
        }
    }

    private boolean isTransparent() {
        return this.opacity < 1.0f;
    }
}

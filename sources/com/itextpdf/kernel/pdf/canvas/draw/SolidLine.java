package com.itextpdf.kernel.pdf.canvas.draw;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/draw/SolidLine.class */
public class SolidLine implements ILineDrawer {
    private float lineWidth;
    private Color color;

    public SolidLine() {
        this.lineWidth = 1.0f;
        this.color = ColorConstants.BLACK;
    }

    public SolidLine(float lineWidth) {
        this.lineWidth = 1.0f;
        this.color = ColorConstants.BLACK;
        this.lineWidth = lineWidth;
    }

    @Override // com.itextpdf.kernel.pdf.canvas.draw.ILineDrawer
    public void draw(PdfCanvas canvas, Rectangle drawArea) {
        canvas.saveState().setStrokeColor(this.color).setLineWidth(this.lineWidth).moveTo(drawArea.getX(), drawArea.getY() + (this.lineWidth / 2.0f)).lineTo(drawArea.getX() + drawArea.getWidth(), drawArea.getY() + (this.lineWidth / 2.0f)).stroke().restoreState();
    }

    @Override // com.itextpdf.kernel.pdf.canvas.draw.ILineDrawer
    public float getLineWidth() {
        return this.lineWidth;
    }

    @Override // com.itextpdf.kernel.pdf.canvas.draw.ILineDrawer
    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }

    @Override // com.itextpdf.kernel.pdf.canvas.draw.ILineDrawer
    public Color getColor() {
        return this.color;
    }

    @Override // com.itextpdf.kernel.pdf.canvas.draw.ILineDrawer
    public void setColor(Color color) {
        this.color = color;
    }
}

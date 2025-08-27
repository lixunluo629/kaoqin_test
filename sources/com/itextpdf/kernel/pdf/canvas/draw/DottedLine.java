package com.itextpdf.kernel.pdf.canvas.draw;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/draw/DottedLine.class */
public class DottedLine implements ILineDrawer {
    protected float gap;
    private float lineWidth;
    private Color color;

    public DottedLine() {
        this.gap = 4.0f;
        this.lineWidth = 1.0f;
        this.color = ColorConstants.BLACK;
    }

    public DottedLine(float lineWidth, float gap) {
        this.gap = 4.0f;
        this.lineWidth = 1.0f;
        this.color = ColorConstants.BLACK;
        this.lineWidth = lineWidth;
        this.gap = gap;
    }

    public DottedLine(float lineWidth) {
        this.gap = 4.0f;
        this.lineWidth = 1.0f;
        this.color = ColorConstants.BLACK;
        this.lineWidth = lineWidth;
    }

    @Override // com.itextpdf.kernel.pdf.canvas.draw.ILineDrawer
    public void draw(PdfCanvas canvas, Rectangle drawArea) {
        canvas.saveState().setLineWidth(this.lineWidth).setStrokeColor(this.color).setLineDash(0.0f, this.gap, this.gap / 2.0f).setLineCapStyle(1).moveTo(drawArea.getX(), drawArea.getY() + (this.lineWidth / 2.0f)).lineTo(drawArea.getX() + drawArea.getWidth(), drawArea.getY() + (this.lineWidth / 2.0f)).stroke().restoreState();
    }

    public float getGap() {
        return this.gap;
    }

    public void setGap(float gap) {
        this.gap = gap;
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

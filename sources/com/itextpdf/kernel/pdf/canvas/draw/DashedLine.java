package com.itextpdf.kernel.pdf.canvas.draw;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/draw/DashedLine.class */
public class DashedLine implements ILineDrawer {
    private float lineWidth;
    private Color color;

    public DashedLine() {
        this.lineWidth = 1.0f;
        this.color = ColorConstants.BLACK;
    }

    public DashedLine(float lineWidth) {
        this.lineWidth = 1.0f;
        this.color = ColorConstants.BLACK;
        this.lineWidth = lineWidth;
    }

    @Override // com.itextpdf.kernel.pdf.canvas.draw.ILineDrawer
    public void draw(PdfCanvas canvas, Rectangle drawArea) {
        canvas.saveState().setLineWidth(this.lineWidth).setStrokeColor(this.color).setLineDash(2.0f, 2.0f).moveTo(drawArea.getX(), drawArea.getY() + (this.lineWidth / 2.0f)).lineTo(drawArea.getX() + drawArea.getWidth(), drawArea.getY() + (this.lineWidth / 2.0f)).stroke().restoreState();
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

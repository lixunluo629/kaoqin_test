package com.itextpdf.layout.borders;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.borders.Border;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/borders/DottedBorder.class */
public class DottedBorder extends Border {
    private static final float GAP_MODIFIER = 1.5f;

    public DottedBorder(float width) {
        super(width);
    }

    public DottedBorder(Color color, float width) {
        super(color, width);
    }

    public DottedBorder(Color color, float width, float opacity) {
        super(color, width, opacity);
    }

    @Override // com.itextpdf.layout.borders.Border
    public int getType() {
        return 2;
    }

    @Override // com.itextpdf.layout.borders.Border
    public void draw(PdfCanvas canvas, float x1, float y1, float x2, float y2, Border.Side defaultSide, float borderWidthBefore, float borderWidthAfter) {
        float initialGap = this.width * GAP_MODIFIER;
        float dx = x2 - x1;
        float dy = y2 - y1;
        double borderLength = Math.sqrt((dx * dx) + (dy * dy));
        float adjustedGap = super.getDotsGap(borderLength, initialGap + this.width);
        if (adjustedGap > this.width) {
            adjustedGap -= this.width;
        }
        float[] startingPoints = getStartingPointsForBorderSide(x1, y1, x2, y2, defaultSide);
        float x12 = startingPoints[0];
        float y12 = startingPoints[1];
        float x22 = startingPoints[2];
        float y22 = startingPoints[3];
        canvas.saveState().setLineWidth(this.width).setStrokeColor(this.transparentColor.getColor());
        this.transparentColor.applyStrokeTransparency(canvas);
        canvas.setLineDash(this.width, adjustedGap, this.width + (adjustedGap / 2.0f)).moveTo(x12, y12).lineTo(x22, y22).stroke().restoreState();
    }

    @Override // com.itextpdf.layout.borders.Border
    public void draw(PdfCanvas canvas, float x1, float y1, float x2, float y2, float horizontalRadius1, float verticalRadius1, float horizontalRadius2, float verticalRadius2, Border.Side defaultSide, float borderWidthBefore, float borderWidthAfter) {
        float initialGap = this.width * GAP_MODIFIER;
        float dx = x2 - x1;
        float dy = y2 - y1;
        double borderLength = Math.sqrt((dx * dx) + (dy * dy));
        float adjustedGap = super.getDotsGap(borderLength, initialGap);
        if (adjustedGap > this.width) {
            adjustedGap -= this.width;
        }
        canvas.saveState().setLineWidth(this.width).setStrokeColor(this.transparentColor.getColor());
        this.transparentColor.applyStrokeTransparency(canvas);
        canvas.setLineDash(this.width, adjustedGap, this.width + (adjustedGap / 2.0f));
        Rectangle boundingRectangle = new Rectangle(x1, y1, x2 - x1, y2 - y1);
        float[] horizontalRadii = {horizontalRadius1, horizontalRadius2};
        float[] verticalRadii = {verticalRadius1, verticalRadius2};
        drawDiscontinuousBorders(canvas, boundingRectangle, horizontalRadii, verticalRadii, defaultSide, borderWidthBefore, borderWidthAfter);
    }

    @Override // com.itextpdf.layout.borders.Border
    public void drawCellBorder(PdfCanvas canvas, float x1, float y1, float x2, float y2, Border.Side defaultSide) {
        float initialGap = this.width * GAP_MODIFIER;
        float dx = x2 - x1;
        float dy = y2 - y1;
        double borderLength = Math.sqrt((dx * dx) + (dy * dy));
        float adjustedGap = super.getDotsGap(borderLength, initialGap + this.width);
        if (adjustedGap > this.width) {
            adjustedGap -= this.width;
        }
        canvas.saveState().setLineWidth(this.width).setStrokeColor(this.transparentColor.getColor());
        this.transparentColor.applyStrokeTransparency(canvas);
        canvas.setLineDash(this.width, adjustedGap, this.width + (adjustedGap / 2.0f)).moveTo(x1, y1).lineTo(x2, y2).stroke().restoreState();
    }

    @Override // com.itextpdf.layout.borders.Border
    @Deprecated
    protected float getDotsGap(double distance, float initialGap) {
        return super.getDotsGap(distance, initialGap);
    }
}

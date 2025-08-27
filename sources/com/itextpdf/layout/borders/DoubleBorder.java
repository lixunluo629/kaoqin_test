package com.itextpdf.layout.borders;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.borders.Border;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/borders/DoubleBorder.class */
public class DoubleBorder extends Border {
    public DoubleBorder(float width) {
        super(width);
    }

    public DoubleBorder(Color color, float width) {
        super(color, width);
    }

    public DoubleBorder(Color color, float width, float opacity) {
        super(color, width, opacity);
    }

    @Override // com.itextpdf.layout.borders.Border
    public int getType() {
        return 3;
    }

    @Override // com.itextpdf.layout.borders.Border
    public void draw(PdfCanvas canvas, float x1, float y1, float x2, float y2, Border.Side defaultSide, float borderWidthBefore, float borderWidthAfter) {
        float x3 = 0.0f;
        float y3 = 0.0f;
        float x4 = 0.0f;
        float y4 = 0.0f;
        float thirdOfWidth = this.width / 3.0f;
        float thirdOfWidthBefore = borderWidthBefore / 3.0f;
        float thirdOfWidthAfter = borderWidthAfter / 3.0f;
        Border.Side borderSide = getBorderSide(x1, y1, x2, y2, defaultSide);
        switch (borderSide) {
            case TOP:
                x3 = x2 + thirdOfWidthAfter;
                y3 = y2 + thirdOfWidth;
                x4 = x1 - thirdOfWidthBefore;
                y4 = y1 + thirdOfWidth;
                break;
            case RIGHT:
                x3 = x2 + thirdOfWidth;
                y3 = y2 - thirdOfWidthAfter;
                x4 = x1 + thirdOfWidth;
                y4 = y1 + thirdOfWidthBefore;
                break;
            case BOTTOM:
                x3 = x2 - thirdOfWidthAfter;
                y3 = y2 - thirdOfWidth;
                x4 = x1 + thirdOfWidthBefore;
                y4 = y1 - thirdOfWidth;
                break;
            case LEFT:
                x3 = x2 - thirdOfWidth;
                y3 = y2 + thirdOfWidthAfter;
                x4 = x1 - thirdOfWidth;
                y4 = y1 - thirdOfWidthBefore;
                break;
        }
        canvas.saveState().setFillColor(this.transparentColor.getColor());
        this.transparentColor.applyFillTransparency(canvas);
        canvas.moveTo(x1, y1).lineTo(x2, y2).lineTo(x3, y3).lineTo(x4, y4).lineTo(x1, y1).fill();
        switch (borderSide) {
            case TOP:
                x2 += 2.0f * thirdOfWidthAfter;
                y2 += 2.0f * thirdOfWidth;
                x3 += 2.0f * thirdOfWidthAfter;
                y3 += 2.0f * thirdOfWidth;
                x4 -= 2.0f * thirdOfWidthBefore;
                y4 += 2.0f * thirdOfWidth;
                x1 -= 2.0f * thirdOfWidthBefore;
                y1 += 2.0f * thirdOfWidth;
                break;
            case RIGHT:
                x2 += 2.0f * thirdOfWidth;
                y2 -= 2.0f * thirdOfWidthAfter;
                x3 += 2.0f * thirdOfWidth;
                y3 -= 2.0f * thirdOfWidthAfter;
                x4 += 2.0f * thirdOfWidth;
                y4 += 2.0f * thirdOfWidthBefore;
                x1 += 2.0f * thirdOfWidth;
                y1 += 2.0f * thirdOfWidthBefore;
                break;
            case BOTTOM:
                x2 -= 2.0f * thirdOfWidthAfter;
                y2 -= 2.0f * thirdOfWidth;
                x3 -= 2.0f * thirdOfWidthAfter;
                y3 -= 2.0f * thirdOfWidth;
                x4 += 2.0f * thirdOfWidthBefore;
                y4 -= 2.0f * thirdOfWidth;
                x1 += 2.0f * thirdOfWidthBefore;
                y1 -= 2.0f * thirdOfWidth;
                break;
            case LEFT:
                x2 -= 2.0f * thirdOfWidth;
                y2 += 2.0f * thirdOfWidthAfter;
                x3 -= 2.0f * thirdOfWidth;
                y3 += 2.0f * thirdOfWidthAfter;
                x4 -= 2.0f * thirdOfWidth;
                y4 -= 2.0f * thirdOfWidthBefore;
                x1 -= 2.0f * thirdOfWidth;
                y1 -= 2.0f * thirdOfWidthBefore;
                break;
        }
        canvas.moveTo(x1, y1).lineTo(x2, y2).lineTo(x3, y3).lineTo(x4, y4).lineTo(x1, y1).fill().restoreState();
    }

    @Override // com.itextpdf.layout.borders.Border
    public void drawCellBorder(PdfCanvas canvas, float x1, float y1, float x2, float y2, Border.Side defaultSide) {
        float thirdOfWidth = this.width / 3.0f;
        Border.Side borderSide = getBorderSide(x1, y1, x2, y2, defaultSide);
        switch (borderSide) {
            case TOP:
                y1 -= thirdOfWidth;
                y2 = y1;
                break;
            case RIGHT:
                x1 -= thirdOfWidth;
                x2 -= thirdOfWidth;
                y1 += thirdOfWidth;
                y2 -= thirdOfWidth;
                break;
        }
        canvas.saveState().setLineWidth(thirdOfWidth).setStrokeColor(this.transparentColor.getColor());
        this.transparentColor.applyStrokeTransparency(canvas);
        canvas.moveTo(x1, y1).lineTo(x2, y2).stroke().restoreState();
        switch (borderSide) {
            case TOP:
                y2 += 2.0f * thirdOfWidth;
                y1 += 2.0f * thirdOfWidth;
                break;
            case RIGHT:
                x2 += 2.0f * thirdOfWidth;
                x1 += 2.0f * thirdOfWidth;
                break;
            case BOTTOM:
                x2 -= 2.0f * thirdOfWidth;
                y2 -= 2.0f * thirdOfWidth;
                x1 += 2.0f * thirdOfWidth;
                y1 -= 2.0f * thirdOfWidth;
                break;
            case LEFT:
                y2 += 2.0f * thirdOfWidth;
                x1 -= 2.0f * thirdOfWidth;
                y1 -= 2.0f * thirdOfWidth;
                break;
        }
        canvas.saveState().setLineWidth(thirdOfWidth).setStrokeColor(this.transparentColor.getColor());
        this.transparentColor.applyStrokeTransparency(canvas);
        canvas.moveTo(x1, y1).lineTo(x2, y2).stroke().restoreState();
    }
}

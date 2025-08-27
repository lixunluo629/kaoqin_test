package com.itextpdf.layout.borders;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.geom.Point;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.borders.Border;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/borders/SolidBorder.class */
public class SolidBorder extends Border {
    public SolidBorder(float width) {
        super(width);
    }

    public SolidBorder(Color color, float width) {
        super(color, width);
    }

    public SolidBorder(Color color, float width, float opacity) {
        super(color, width, opacity);
    }

    @Override // com.itextpdf.layout.borders.Border
    public int getType() {
        return 0;
    }

    @Override // com.itextpdf.layout.borders.Border
    public void draw(PdfCanvas canvas, float x1, float y1, float x2, float y2, Border.Side defaultSide, float borderWidthBefore, float borderWidthAfter) {
        float x3 = 0.0f;
        float y3 = 0.0f;
        float x4 = 0.0f;
        float y4 = 0.0f;
        Border.Side borderSide = getBorderSide(x1, y1, x2, y2, defaultSide);
        switch (borderSide) {
            case TOP:
                x3 = x2 + borderWidthAfter;
                y3 = y2 + this.width;
                x4 = x1 - borderWidthBefore;
                y4 = y1 + this.width;
                break;
            case RIGHT:
                x3 = x2 + this.width;
                y3 = y2 - borderWidthAfter;
                x4 = x1 + this.width;
                y4 = y1 + borderWidthBefore;
                break;
            case BOTTOM:
                x3 = x2 - borderWidthAfter;
                y3 = y2 - this.width;
                x4 = x1 + borderWidthBefore;
                y4 = y1 - this.width;
                break;
            case LEFT:
                x3 = x2 - this.width;
                y3 = y2 + borderWidthAfter;
                x4 = x1 - this.width;
                y4 = y1 - borderWidthBefore;
                break;
        }
        canvas.saveState().setFillColor(this.transparentColor.getColor());
        this.transparentColor.applyFillTransparency(canvas);
        canvas.moveTo(x1, y1).lineTo(x2, y2).lineTo(x3, y3).lineTo(x4, y4).lineTo(x1, y1).fill().restoreState();
    }

    @Override // com.itextpdf.layout.borders.Border
    public void draw(PdfCanvas canvas, float x1, float y1, float x2, float y2, float horizontalRadius1, float verticalRadius1, float horizontalRadius2, float verticalRadius2, Border.Side defaultSide, float borderWidthBefore, float borderWidthAfter) {
        float x3 = 0.0f;
        float y3 = 0.0f;
        float x4 = 0.0f;
        float y4 = 0.0f;
        Border.Side borderSide = getBorderSide(x1, y1, x2, y2, defaultSide);
        switch (borderSide) {
            case TOP:
                x3 = x2 + borderWidthAfter;
                y3 = y2 + this.width;
                x4 = x1 - borderWidthBefore;
                y4 = y1 + this.width;
                float innerRadiusBefore = Math.max(0.0f, horizontalRadius1 - borderWidthBefore);
                float innerRadiusFirst = Math.max(0.0f, verticalRadius1 - this.width);
                float innerRadiusSecond = Math.max(0.0f, verticalRadius2 - this.width);
                float innerRadiusAfter = Math.max(0.0f, horizontalRadius2 - borderWidthAfter);
                if (innerRadiusBefore > innerRadiusFirst) {
                    x1 = (float) getIntersectionPoint(new Point(x1, y1), new Point(x4, y4), new Point(x4, y1 - innerRadiusFirst), new Point(x1 + innerRadiusBefore, y1 - innerRadiusFirst)).getX();
                    y1 -= innerRadiusFirst;
                } else if (0.0f != innerRadiusBefore && 0.0f != innerRadiusFirst) {
                    y1 = (float) getIntersectionPoint(new Point(x1, y1), new Point(x4, y4), new Point(x1 + innerRadiusBefore, y1), new Point(x1 + innerRadiusBefore, y1 - innerRadiusFirst)).getY();
                    x1 += innerRadiusBefore;
                }
                if (innerRadiusAfter <= innerRadiusSecond) {
                    if (0.0f != innerRadiusAfter && 0.0f != innerRadiusSecond) {
                        y2 = (float) getIntersectionPoint(new Point(x2, y2), new Point(x3, y3), new Point(x2 - innerRadiusAfter, y2), new Point(x2 - innerRadiusAfter, y2 - innerRadiusSecond)).getY();
                        x2 -= innerRadiusAfter;
                        break;
                    }
                } else {
                    x2 = (float) getIntersectionPoint(new Point(x2, y2), new Point(x3, y3), new Point(x2, y2 - innerRadiusSecond), new Point(x2 - innerRadiusAfter, y2 - innerRadiusSecond)).getX();
                    y2 -= innerRadiusSecond;
                    break;
                }
                break;
            case RIGHT:
                x3 = x2 + this.width;
                y3 = y2 - borderWidthAfter;
                x4 = x1 + this.width;
                y4 = y1 + borderWidthBefore;
                float innerRadiusBefore2 = Math.max(0.0f, verticalRadius1 - borderWidthBefore);
                float innerRadiusFirst2 = Math.max(0.0f, horizontalRadius1 - this.width);
                float innerRadiusSecond2 = Math.max(0.0f, horizontalRadius2 - this.width);
                float innerRadiusAfter2 = Math.max(0.0f, verticalRadius2 - borderWidthAfter);
                if (innerRadiusFirst2 > innerRadiusBefore2) {
                    x1 = (float) getIntersectionPoint(new Point(x1, y1), new Point(x4, y4), new Point(x1, y1 - innerRadiusBefore2), new Point(x1 - innerRadiusFirst2, y1 - innerRadiusBefore2)).getX();
                    y1 -= innerRadiusBefore2;
                } else if (0.0f != innerRadiusBefore2 && 0.0f != innerRadiusFirst2) {
                    y1 = (float) getIntersectionPoint(new Point(x1, y1), new Point(x4, y4), new Point(x1 - innerRadiusFirst2, y1), new Point(x1 - innerRadiusFirst2, y1 - innerRadiusBefore2)).getY();
                    x1 -= innerRadiusFirst2;
                }
                if (innerRadiusAfter2 <= innerRadiusSecond2) {
                    if (0.0f != innerRadiusAfter2 && 0.0f != innerRadiusSecond2) {
                        x2 = (float) getIntersectionPoint(new Point(x2, y2), new Point(x3, y3), new Point(x2, y2 + innerRadiusAfter2), new Point(x2 - innerRadiusSecond2, y2 + innerRadiusAfter2)).getX();
                        y2 += innerRadiusAfter2;
                        break;
                    }
                } else {
                    y2 = (float) getIntersectionPoint(new Point(x2, y2), new Point(x3, y3), new Point(x2 - innerRadiusSecond2, y2), new Point(x2 - innerRadiusSecond2, y2 + innerRadiusAfter2)).getY();
                    x2 -= innerRadiusSecond2;
                    break;
                }
                break;
            case BOTTOM:
                x3 = x2 - borderWidthAfter;
                y3 = y2 - this.width;
                x4 = x1 + borderWidthBefore;
                y4 = y1 - this.width;
                float innerRadiusBefore3 = Math.max(0.0f, horizontalRadius1 - borderWidthBefore);
                float innerRadiusFirst3 = Math.max(0.0f, verticalRadius1 - this.width);
                float innerRadiusSecond3 = Math.max(0.0f, verticalRadius2 - this.width);
                float innerRadiusAfter3 = Math.max(0.0f, horizontalRadius2 - borderWidthAfter);
                if (innerRadiusFirst3 > innerRadiusBefore3) {
                    y1 = (float) getIntersectionPoint(new Point(x1, y1), new Point(x4, y4), new Point(x1 - innerRadiusBefore3, y1), new Point(x1 - innerRadiusBefore3, y1 + innerRadiusFirst3)).getY();
                    x1 -= innerRadiusBefore3;
                } else if (0.0f != innerRadiusBefore3 && 0.0f != innerRadiusFirst3) {
                    x1 = (float) getIntersectionPoint(new Point(x1, y1), new Point(x4, y4), new Point(x1, y1 + innerRadiusFirst3), new Point(x1 - innerRadiusBefore3, y1 + innerRadiusFirst3)).getX();
                    y1 += innerRadiusFirst3;
                }
                if (innerRadiusAfter3 <= innerRadiusSecond3) {
                    if (0.0f != innerRadiusAfter3 && 0.0f != innerRadiusSecond3) {
                        y2 = (float) getIntersectionPoint(new Point(x2, y2), new Point(x3, y3), new Point(x2 + innerRadiusAfter3, y2), new Point(x2 + innerRadiusAfter3, y2 + innerRadiusSecond3)).getY();
                        x2 += innerRadiusAfter3;
                        break;
                    }
                } else {
                    x2 = (float) getIntersectionPoint(new Point(x2, y2), new Point(x3, y3), new Point(x2, y2 + innerRadiusSecond3), new Point(x2 + innerRadiusAfter3, y2 + innerRadiusSecond3)).getX();
                    y2 += innerRadiusSecond3;
                    break;
                }
                break;
            case LEFT:
                x3 = x2 - this.width;
                y3 = y2 + borderWidthAfter;
                x4 = x1 - this.width;
                y4 = y1 - borderWidthBefore;
                float innerRadiusBefore4 = Math.max(0.0f, verticalRadius1 - borderWidthBefore);
                float innerRadiusFirst4 = Math.max(0.0f, horizontalRadius1 - this.width);
                float innerRadiusSecond4 = Math.max(0.0f, horizontalRadius2 - this.width);
                float innerRadiusAfter4 = Math.max(0.0f, verticalRadius2 - borderWidthAfter);
                if (innerRadiusFirst4 > innerRadiusBefore4) {
                    x1 = (float) getIntersectionPoint(new Point(x1, y1), new Point(x4, y4), new Point(x1, y1 + innerRadiusBefore4), new Point(x1 + innerRadiusFirst4, y1 + innerRadiusBefore4)).getX();
                    y1 += innerRadiusBefore4;
                } else if (0.0f != innerRadiusBefore4 && 0.0f != innerRadiusFirst4) {
                    y1 = (float) getIntersectionPoint(new Point(x1, y1), new Point(x4, y4), new Point(x1 + innerRadiusFirst4, y1), new Point(x1 + innerRadiusFirst4, y1 + innerRadiusBefore4)).getY();
                    x1 += innerRadiusFirst4;
                }
                if (innerRadiusAfter4 <= innerRadiusSecond4) {
                    if (0.0f != innerRadiusAfter4 && 0.0f != innerRadiusSecond4) {
                        x2 = (float) getIntersectionPoint(new Point(x2, y2), new Point(x3, y3), new Point(x2, y2 - innerRadiusAfter4), new Point(x2 + innerRadiusSecond4, y2 - innerRadiusAfter4)).getX();
                        y2 -= innerRadiusAfter4;
                        break;
                    }
                } else {
                    y2 = (float) getIntersectionPoint(new Point(x2, y2), new Point(x3, y3), new Point(x2 + innerRadiusSecond4, y2), new Point(x2 + innerRadiusSecond4, y2 - innerRadiusAfter4)).getY();
                    x2 += innerRadiusSecond4;
                    break;
                }
                break;
        }
        canvas.saveState().setFillColor(this.transparentColor.getColor());
        this.transparentColor.applyFillTransparency(canvas);
        canvas.moveTo(x1, y1).lineTo(x2, y2).lineTo(x3, y3).lineTo(x4, y4).lineTo(x1, y1).fill().restoreState();
    }

    @Override // com.itextpdf.layout.borders.Border
    public void drawCellBorder(PdfCanvas canvas, float x1, float y1, float x2, float y2, Border.Side defaultSide) {
        canvas.saveState().setStrokeColor(this.transparentColor.getColor());
        this.transparentColor.applyStrokeTransparency(canvas);
        canvas.setLineWidth(this.width).moveTo(x1, y1).lineTo(x2, y2).stroke().restoreState();
    }
}

package com.itextpdf.layout.borders;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.io.util.MessageFormatUtil;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.Point;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.property.TransparentColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/borders/Border.class */
public abstract class Border {
    public static final Border NO_BORDER = null;
    private static final float CURV = 0.447f;
    public static final int SOLID = 0;
    public static final int DASHED = 1;
    public static final int DOTTED = 2;
    public static final int DOUBLE = 3;
    public static final int ROUND_DOTS = 4;
    public static final int _3D_GROOVE = 5;
    public static final int _3D_INSET = 6;
    public static final int _3D_OUTSET = 7;
    public static final int _3D_RIDGE = 8;
    protected TransparentColor transparentColor;
    protected float width;
    protected int type;
    private int hash;

    /* loaded from: layout-7.1.10.jar:com/itextpdf/layout/borders/Border$Side.class */
    public enum Side {
        NONE,
        TOP,
        RIGHT,
        BOTTOM,
        LEFT
    }

    public abstract void draw(PdfCanvas pdfCanvas, float f, float f2, float f3, float f4, Side side, float f5, float f6);

    public abstract void drawCellBorder(PdfCanvas pdfCanvas, float f, float f2, float f3, float f4, Side side);

    public abstract int getType();

    protected Border(float width) {
        this(ColorConstants.BLACK, width);
    }

    protected Border(Color color, float width) {
        this.transparentColor = new TransparentColor(color);
        this.width = width;
    }

    protected Border(Color color, float width, float opacity) {
        this.transparentColor = new TransparentColor(color, opacity);
        this.width = width;
    }

    public void draw(PdfCanvas canvas, float x1, float y1, float x2, float y2, float borderRadius, Side defaultSide, float borderWidthBefore, float borderWidthAfter) {
        draw(canvas, x1, y1, x2, y2, borderRadius, borderRadius, borderRadius, borderRadius, defaultSide, borderWidthBefore, borderWidthAfter);
    }

    public void draw(PdfCanvas canvas, float x1, float y1, float x2, float y2, float horizontalRadius1, float verticalRadius1, float horizontalRadius2, float verticalRadius2, Side defaultSide, float borderWidthBefore, float borderWidthAfter) {
        Logger logger = LoggerFactory.getLogger((Class<?>) Border.class);
        logger.warn(MessageFormatUtil.format(LogMessageConstant.METHOD_IS_NOT_IMPLEMENTED_BY_DEFAULT_OTHER_METHOD_WILL_BE_USED, "Border#draw(PdfCanvas, float, float, float, float, float, float, float, float, Side, float, float", "Border#draw(PdfCanvas, float, float, float, float, Side, float, float)"));
        draw(canvas, x1, y1, x2, y2, defaultSide, borderWidthBefore, borderWidthAfter);
    }

    public Color getColor() {
        return this.transparentColor.getColor();
    }

    public float getOpacity() {
        return this.transparentColor.getOpacity();
    }

    public float getWidth() {
        return this.width;
    }

    public void setColor(Color color) {
        this.transparentColor = new TransparentColor(color, this.transparentColor.getOpacity());
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public boolean equals(Object anObject) {
        if (this == anObject) {
            return true;
        }
        if (anObject instanceof Border) {
            Border anotherBorder = (Border) anObject;
            if (anotherBorder.getType() != getType() || !anotherBorder.getColor().equals(getColor()) || anotherBorder.getWidth() != getWidth() || anotherBorder.transparentColor.getOpacity() != this.transparentColor.getOpacity()) {
                return false;
            }
            return true;
        }
        return false;
    }

    public int hashCode() {
        int h = this.hash;
        if (h == 0) {
            h = (((((int) getWidth()) * 31) + getColor().hashCode()) * 31) + ((int) this.transparentColor.getOpacity());
            this.hash = h;
        }
        return h;
    }

    protected Side getBorderSide(float x1, float y1, float x2, float y2, Side defaultSide) {
        boolean isLeft = false;
        boolean isRight = false;
        if (Math.abs(y2 - y1) > 5.0E-4f) {
            isLeft = y2 - y1 > 0.0f;
            isRight = y2 - y1 < 0.0f;
        }
        boolean isTop = false;
        boolean isBottom = false;
        if (Math.abs(x2 - x1) > 5.0E-4f) {
            isTop = x2 - x1 > 0.0f;
            isBottom = x2 - x1 < 0.0f;
        }
        if (isTop) {
            return isLeft ? Side.LEFT : Side.TOP;
        }
        if (isRight) {
            return Side.RIGHT;
        }
        if (isBottom) {
            return Side.BOTTOM;
        }
        if (isLeft) {
            return Side.LEFT;
        }
        return defaultSide;
    }

    protected Point getIntersectionPoint(Point lineBeg, Point lineEnd, Point clipLineBeg, Point clipLineEnd) {
        double A1 = lineBeg.getY() - lineEnd.getY();
        double A2 = clipLineBeg.getY() - clipLineEnd.getY();
        double B1 = lineEnd.getX() - lineBeg.getX();
        double B2 = clipLineEnd.getX() - clipLineBeg.getX();
        double C1 = (lineBeg.getX() * lineEnd.getY()) - (lineBeg.getY() * lineEnd.getX());
        double C2 = (clipLineBeg.getX() * clipLineEnd.getY()) - (clipLineBeg.getY() * clipLineEnd.getX());
        double M = (B1 * A2) - (B2 * A1);
        return new Point(((B2 * C1) - (B1 * C2)) / M, ((C2 * A1) - (C1 * A2)) / M);
    }

    protected float getDotsGap(double distance, float initialGap) {
        double gapsNum = Math.ceil(distance / initialGap);
        if (gapsNum == 0.0d) {
            return initialGap;
        }
        return (float) (distance / gapsNum);
    }

    protected void drawDiscontinuousBorders(PdfCanvas canvas, Rectangle boundingRectangle, float[] horizontalRadii, float[] verticalRadii, Side defaultSide, float borderWidthBefore, float borderWidthAfter) {
        float x1 = boundingRectangle.getX();
        float y1 = boundingRectangle.getY();
        float x2 = boundingRectangle.getRight();
        float y2 = boundingRectangle.getTop();
        float horizontalRadius1 = horizontalRadii[0];
        float horizontalRadius2 = horizontalRadii[1];
        float verticalRadius1 = verticalRadii[0];
        float verticalRadius2 = verticalRadii[1];
        float x0 = boundingRectangle.getX();
        float y0 = boundingRectangle.getY();
        float x3 = boundingRectangle.getRight();
        float y3 = boundingRectangle.getTop();
        float widthHalf = this.width / 2.0f;
        Side borderSide = getBorderSide(x1, y1, x2, y2, defaultSide);
        switch (borderSide) {
            case TOP:
                float innerRadiusBefore = Math.max(0.0f, horizontalRadius1 - borderWidthBefore);
                float innerRadiusFirst = Math.max(0.0f, verticalRadius1 - this.width);
                float innerRadiusSecond = Math.max(0.0f, verticalRadius2 - this.width);
                float innerRadiusAfter = Math.max(0.0f, horizontalRadius2 - borderWidthAfter);
                float x02 = x0 - (borderWidthBefore / 2.0f);
                float y02 = y0 - innerRadiusFirst;
                float x32 = x3 + (borderWidthAfter / 2.0f);
                float y32 = y3 - innerRadiusSecond;
                Point clipPoint1 = getIntersectionPoint(new Point(x1 - borderWidthBefore, y1 + this.width), new Point(x1, y1), new Point(x02, y02), new Point(x02 + 10.0f, y02));
                Point clipPoint2 = getIntersectionPoint(new Point(x2 + borderWidthAfter, y2 + this.width), new Point(x2, y2), new Point(x32, y32), new Point(x32 - 10.0f, y32));
                if (clipPoint1.x > clipPoint2.x) {
                    Point clipPoint = getIntersectionPoint(new Point(x1 - borderWidthBefore, y1 + this.width), clipPoint1, clipPoint2, new Point(x2 + borderWidthAfter, y2 + this.width));
                    canvas.moveTo(x1 - borderWidthBefore, y1 + this.width).lineTo(clipPoint.x, clipPoint.y).lineTo(x2 + borderWidthAfter, y2 + this.width).lineTo(x1 - borderWidthBefore, y1 + this.width);
                } else {
                    canvas.moveTo(x1 - borderWidthBefore, y1 + this.width).lineTo(clipPoint1.x, clipPoint1.y).lineTo(clipPoint2.x, clipPoint2.y).lineTo(x2 + borderWidthAfter, y2 + this.width).lineTo(x1 - borderWidthBefore, y1 + this.width);
                }
                canvas.clip().endPath();
                float x12 = x1 + innerRadiusBefore;
                float y12 = y1 + widthHalf;
                float x22 = x2 - innerRadiusAfter;
                float y22 = y2 + widthHalf;
                canvas.moveTo(x02, y02).curveTo(x02, y02 + (innerRadiusFirst * CURV), x12 - (innerRadiusBefore * CURV), y12, x12, y12).lineTo(x22, y22).curveTo(x22 + (innerRadiusAfter * CURV), y22, x32, y32 + (innerRadiusSecond * CURV), x32, y32);
                break;
            case RIGHT:
                float innerRadiusBefore2 = Math.max(0.0f, verticalRadius1 - borderWidthBefore);
                float innerRadiusFirst2 = Math.max(0.0f, horizontalRadius1 - this.width);
                float innerRadiusSecond2 = Math.max(0.0f, horizontalRadius2 - this.width);
                float innerRadiusAfter2 = Math.max(0.0f, verticalRadius2 - borderWidthAfter);
                float x03 = x0 - innerRadiusFirst2;
                float y03 = y0 + (borderWidthBefore / 2.0f);
                float x33 = x3 - innerRadiusSecond2;
                float y33 = y3 - (borderWidthAfter / 2.0f);
                Point clipPoint12 = getIntersectionPoint(new Point(x1 + this.width, y1 + borderWidthBefore), new Point(x1, y1), new Point(x03, y03), new Point(x03, y03 - 10.0f));
                Point clipPoint22 = getIntersectionPoint(new Point(x2 + this.width, y2 - borderWidthAfter), new Point(x2, y2), new Point(x33, y33), new Point(x33, y33 - 10.0f));
                if (clipPoint12.y < clipPoint22.y) {
                    Point clipPoint3 = getIntersectionPoint(new Point(x1 + this.width, y1 + borderWidthBefore), clipPoint12, clipPoint22, new Point(x2 + this.width, y2 - borderWidthAfter));
                    canvas.moveTo(x1 + this.width, y1 + borderWidthBefore).lineTo(clipPoint3.x, clipPoint3.y).lineTo(x2 + this.width, y2 - borderWidthAfter).lineTo(x1 + this.width, y1 + borderWidthBefore).clip().endPath();
                } else {
                    canvas.moveTo(x1 + this.width, y1 + borderWidthBefore).lineTo(clipPoint12.x, clipPoint12.y).lineTo(clipPoint22.x, clipPoint22.y).lineTo(x2 + this.width, y2 - borderWidthAfter).lineTo(x1 + this.width, y1 + borderWidthBefore).clip().endPath();
                }
                canvas.clip().endPath();
                float x13 = x1 + widthHalf;
                float y13 = y1 - innerRadiusBefore2;
                float x23 = x2 + widthHalf;
                canvas.moveTo(x03, y03).curveTo(x03 + (innerRadiusFirst2 * CURV), y03, x13, y13 + (innerRadiusBefore2 * CURV), x13, y13).lineTo(x23, y2 + innerRadiusAfter2).curveTo(x23, r0 - (innerRadiusAfter2 * CURV), x33 + (innerRadiusSecond2 * CURV), y33, x33, y33);
                break;
            case BOTTOM:
                float innerRadiusBefore3 = Math.max(0.0f, horizontalRadius1 - borderWidthBefore);
                float innerRadiusFirst3 = Math.max(0.0f, verticalRadius1 - this.width);
                float innerRadiusSecond3 = Math.max(0.0f, verticalRadius2 - this.width);
                float innerRadiusAfter3 = Math.max(0.0f, horizontalRadius2 - borderWidthAfter);
                float x04 = x0 + (borderWidthBefore / 2.0f);
                float y04 = y0 + innerRadiusFirst3;
                float x34 = x3 - (borderWidthAfter / 2.0f);
                float y34 = y3 + innerRadiusSecond3;
                Point clipPoint13 = getIntersectionPoint(new Point(x1 + borderWidthBefore, y1 - this.width), new Point(x1, y1), new Point(x04, y04), new Point(x04 - 10.0f, y04));
                Point clipPoint23 = getIntersectionPoint(new Point(x2 - borderWidthAfter, y2 - this.width), new Point(x2, y2), new Point(x34, y34), new Point(x34 + 10.0f, y34));
                if (clipPoint13.x < clipPoint23.x) {
                    Point clipPoint4 = getIntersectionPoint(new Point(x1 + borderWidthBefore, y1 - this.width), clipPoint13, clipPoint23, new Point(x2 - borderWidthAfter, y2 - this.width));
                    canvas.moveTo(x1 + borderWidthBefore, y1 - this.width).lineTo(clipPoint4.x, clipPoint4.y).lineTo(x2 - borderWidthAfter, y2 - this.width).lineTo(x1 + borderWidthBefore, y1 - this.width);
                } else {
                    canvas.moveTo(x1 + borderWidthBefore, y1 - this.width).lineTo(clipPoint13.x, clipPoint13.y).lineTo(clipPoint23.x, clipPoint23.y).lineTo(x2 - borderWidthAfter, y2 - this.width).lineTo(x1 + borderWidthBefore, y1 - this.width);
                }
                canvas.clip().endPath();
                float x14 = x1 - innerRadiusBefore3;
                float y14 = y1 - widthHalf;
                float x24 = x2 + innerRadiusAfter3;
                float y23 = y2 - widthHalf;
                canvas.moveTo(x04, y04).curveTo(x04, y04 - (innerRadiusFirst3 * CURV), x14 + (innerRadiusBefore3 * CURV), y14, x14, y14).lineTo(x24, y23).curveTo(x24 - (innerRadiusAfter3 * CURV), y23, x34, y34 - (innerRadiusSecond3 * CURV), x34, y34);
                break;
            case LEFT:
                float innerRadiusBefore4 = Math.max(0.0f, verticalRadius1 - borderWidthBefore);
                float innerRadiusFirst4 = Math.max(0.0f, horizontalRadius1 - this.width);
                float innerRadiusSecond4 = Math.max(0.0f, horizontalRadius2 - this.width);
                float innerRadiusAfter4 = Math.max(0.0f, verticalRadius2 - borderWidthAfter);
                float x05 = x0 + innerRadiusFirst4;
                float y05 = y0 - (borderWidthBefore / 2.0f);
                float x35 = x3 + innerRadiusSecond4;
                float y35 = y3 + (borderWidthAfter / 2.0f);
                Point clipPoint14 = getIntersectionPoint(new Point(x1 - this.width, y1 - borderWidthBefore), new Point(x1, y1), new Point(x05, y05), new Point(x05, y05 + 10.0f));
                Point clipPoint24 = getIntersectionPoint(new Point(x2 - this.width, y2 + borderWidthAfter), new Point(x2, y2), new Point(x35, y35), new Point(x35, y35 + 10.0f));
                if (clipPoint14.y > clipPoint24.y) {
                    Point clipPoint5 = getIntersectionPoint(new Point(x1 - this.width, y1 - borderWidthBefore), clipPoint14, clipPoint24, new Point(x2 - this.width, y2 + borderWidthAfter));
                    canvas.moveTo(x1 - this.width, y1 - borderWidthBefore).lineTo(clipPoint5.x, clipPoint5.y).lineTo(x2 - this.width, y2 + borderWidthAfter).lineTo(x1 - this.width, y1 - borderWidthBefore);
                } else {
                    canvas.moveTo(x1 - this.width, y1 - borderWidthBefore).lineTo(clipPoint14.x, clipPoint14.y).lineTo(clipPoint24.x, clipPoint24.y).lineTo(x2 - this.width, y2 + borderWidthAfter).lineTo(x1 - this.width, y1 - borderWidthBefore);
                }
                canvas.clip().endPath();
                float x15 = x1 - widthHalf;
                float y15 = y1 + innerRadiusBefore4;
                float x25 = x2 - widthHalf;
                canvas.moveTo(x05, y05).curveTo(x05 - (innerRadiusFirst4 * CURV), y05, x15, y15 - (innerRadiusBefore4 * CURV), x15, y15).lineTo(x25, y2 - innerRadiusAfter4).curveTo(x25, r0 + (innerRadiusAfter4 * CURV), x35 - (innerRadiusSecond4 * CURV), y35, x35, y35);
                break;
        }
        canvas.stroke().restoreState();
    }

    protected float[] getStartingPointsForBorderSide(float x1, float y1, float x2, float y2, Side defaultSide) {
        float widthHalf = this.width / 2.0f;
        Side borderSide = getBorderSide(x1, y1, x2, y2, defaultSide);
        switch (borderSide) {
            case TOP:
                y1 += widthHalf;
                y2 += widthHalf;
                break;
            case RIGHT:
                x1 += widthHalf;
                x2 += widthHalf;
                break;
            case BOTTOM:
                y1 -= widthHalf;
                y2 -= widthHalf;
                break;
            case LEFT:
                x1 -= widthHalf;
                x2 -= widthHalf;
                break;
        }
        return new float[]{x1, y1, x2, y2};
    }
}

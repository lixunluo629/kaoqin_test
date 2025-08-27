package com.itextpdf.kernel.geom;

import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfPage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/geom/Rectangle.class */
public class Rectangle implements Cloneable, Serializable {
    private static final long serialVersionUID = 8025677415569233446L;
    private static float EPS = 1.0E-4f;
    protected float x;
    protected float y;
    protected float width;
    protected float height;

    public Rectangle(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rectangle(float width, float height) {
        this(0.0f, 0.0f, width, height);
    }

    public Rectangle(Rectangle rect) {
        this(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }

    public static Rectangle getCommonRectangle(Rectangle... rectangles) {
        float ury = -3.4028235E38f;
        float llx = Float.MAX_VALUE;
        float lly = Float.MAX_VALUE;
        float urx = -3.4028235E38f;
        for (Rectangle rectangle : rectangles) {
            if (rectangle != null) {
                Rectangle rec = rectangle.mo825clone();
                if (rec.getY() < lly) {
                    lly = rec.getY();
                }
                if (rec.getX() < llx) {
                    llx = rec.getX();
                }
                if (rec.getY() + rec.getHeight() > ury) {
                    ury = rec.getY() + rec.getHeight();
                }
                if (rec.getX() + rec.getWidth() > urx) {
                    urx = rec.getX() + rec.getWidth();
                }
            }
        }
        return new Rectangle(llx, lly, urx - llx, ury - lly);
    }

    public static Rectangle getRectangleOnRotatedPage(Rectangle rect, PdfPage page) {
        Rectangle resultRect = rect;
        int rotation = page.getRotation();
        if (0 != rotation) {
            Rectangle pageSize = page.getPageSize();
            switch ((rotation / 90) % 4) {
                case 1:
                    resultRect = new Rectangle(pageSize.getWidth() - resultRect.getTop(), resultRect.getLeft(), resultRect.getHeight(), resultRect.getWidth());
                    break;
                case 2:
                    resultRect = new Rectangle(pageSize.getWidth() - resultRect.getRight(), pageSize.getHeight() - resultRect.getTop(), resultRect.getWidth(), resultRect.getHeight());
                    break;
                case 3:
                    resultRect = new Rectangle(resultRect.getLeft(), pageSize.getHeight() - resultRect.getRight(), resultRect.getHeight(), resultRect.getWidth());
                    break;
            }
        }
        return resultRect;
    }

    public static Rectangle calculateBBox(List<Point> points) {
        List<Double> xs = new ArrayList<>();
        List<Double> ys = new ArrayList<>();
        for (Point point : points) {
            xs.add(Double.valueOf(point.getX()));
            ys.add(Double.valueOf(point.getY()));
        }
        double left = ((Double) Collections.min(xs)).doubleValue();
        double bottom = ((Double) Collections.min(ys)).doubleValue();
        double right = ((Double) Collections.max(xs)).doubleValue();
        double top = ((Double) Collections.max(ys)).doubleValue();
        return new Rectangle((float) left, (float) bottom, (float) (right - left), (float) (top - bottom));
    }

    public Point[] toPointsArray() {
        return new Point[]{new Point(this.x, this.y), new Point(this.x + this.width, this.y), new Point(this.x + this.width, this.y + this.height), new Point(this.x, this.y + this.height)};
    }

    public Rectangle getIntersection(Rectangle rect) {
        Rectangle result = null;
        float llx = Math.max(this.x, rect.x);
        float lly = Math.max(this.y, rect.y);
        float urx = Math.min(getRight(), rect.getRight());
        float ury = Math.min(getTop(), rect.getTop());
        float width = urx - llx;
        float height = ury - lly;
        if (Float.compare(width, 0.0f) >= 0 && Float.compare(height, 0.0f) >= 0) {
            if (Float.compare(width, 0.0f) < 0) {
                width = 0.0f;
            }
            if (Float.compare(height, 0.0f) < 0) {
                height = 0.0f;
            }
            result = new Rectangle(llx, lly, width, height);
        }
        return result;
    }

    public boolean contains(Rectangle rect) {
        float llx = getX();
        float lly = getY();
        float urx = llx + getWidth();
        float ury = lly + getHeight();
        float rllx = rect.getX();
        float rlly = rect.getY();
        float rurx = rllx + rect.getWidth();
        float rury = rlly + rect.getHeight();
        return llx - EPS <= rllx && lly - EPS <= rlly && rurx <= urx + EPS && rury <= ury + EPS;
    }

    public boolean overlaps(Rectangle rect) {
        return getX() + getWidth() >= rect.getX() && getY() + getHeight() >= rect.getY() && getX() <= rect.getX() + rect.getWidth() && getY() <= rect.getY() + rect.getHeight();
    }

    public Rectangle setBbox(float llx, float lly, float urx, float ury) {
        if (llx > urx) {
            llx = urx;
            urx = llx;
        }
        if (lly > ury) {
            lly = ury;
            ury = lly;
        }
        this.x = llx;
        this.y = lly;
        this.width = urx - llx;
        this.height = ury - lly;
        return this;
    }

    public float getX() {
        return this.x;
    }

    public Rectangle setX(float x) {
        this.x = x;
        return this;
    }

    public float getY() {
        return this.y;
    }

    public Rectangle setY(float y) {
        this.y = y;
        return this;
    }

    public float getWidth() {
        return this.width;
    }

    public Rectangle setWidth(float width) {
        this.width = width;
        return this;
    }

    public float getHeight() {
        return this.height;
    }

    public Rectangle setHeight(float height) {
        this.height = height;
        return this;
    }

    public Rectangle increaseHeight(float extra) {
        this.height += extra;
        return this;
    }

    public Rectangle decreaseHeight(float extra) {
        this.height -= extra;
        return this;
    }

    public float getLeft() {
        return this.x;
    }

    public float getRight() {
        return this.x + this.width;
    }

    public float getTop() {
        return this.y + this.height;
    }

    public float getBottom() {
        return this.y;
    }

    public Rectangle moveDown(float move) {
        this.y -= move;
        return this;
    }

    public Rectangle moveUp(float move) {
        this.y += move;
        return this;
    }

    public Rectangle moveRight(float move) {
        this.x += move;
        return this;
    }

    public Rectangle moveLeft(float move) {
        this.x -= move;
        return this;
    }

    public Rectangle applyMargins(float topIndent, float rightIndent, float bottomIndent, float leftIndent, boolean reverse) {
        this.x += leftIndent * (reverse ? -1 : 1);
        this.width -= (leftIndent + rightIndent) * (reverse ? -1 : 1);
        this.y += bottomIndent * (reverse ? -1 : 1);
        this.height -= (topIndent + bottomIndent) * (reverse ? -1 : 1);
        return this;
    }

    public boolean intersectsLine(float x1, float y1, float x2, float y2) {
        double rx1 = getX();
        double ry1 = getY();
        double rx2 = rx1 + getWidth();
        double ry2 = ry1 + getHeight();
        return (rx1 <= ((double) x1) && ((double) x1) <= rx2 && ry1 <= ((double) y1) && ((double) y1) <= ry2) || (rx1 <= ((double) x2) && ((double) x2) <= rx2 && ry1 <= ((double) y2) && ((double) y2) <= ry2) || linesIntersect(rx1, ry1, rx2, ry2, (double) x1, (double) y1, (double) x2, (double) y2) || linesIntersect(rx2, ry1, rx1, ry2, (double) x1, (double) y1, (double) x2, (double) y2);
    }

    public String toString() {
        return "Rectangle: " + getWidth() + 'x' + getHeight();
    }

    @Override // 
    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public Rectangle mo825clone() {
        try {
            return (Rectangle) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public boolean equalsWithEpsilon(Rectangle that) {
        return equalsWithEpsilon(that, EPS);
    }

    public boolean equalsWithEpsilon(Rectangle that, float eps) {
        float dx = Math.abs(this.x - that.x);
        float dy = Math.abs(this.y - that.y);
        float dw = Math.abs(this.width - that.width);
        float dh = Math.abs(this.height - that.height);
        return dx < eps && dy < eps && dw < eps && dh < eps;
    }

    private static boolean linesIntersect(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        double x22 = x2 - x1;
        double y22 = y2 - y1;
        double x32 = x3 - x1;
        double y32 = y3 - y1;
        double x42 = x4 - x1;
        double y42 = y4 - y1;
        double AvB = (x22 * y32) - (x32 * y22);
        double AvC = (x22 * y42) - (x42 * y22);
        if (AvB != 0.0d || AvC != 0.0d) {
            double BvC = (x32 * y42) - (x42 * y32);
            return AvB * AvC <= 0.0d && BvC * ((AvB + BvC) - AvC) <= 0.0d;
        }
        if (x22 == 0.0d) {
            if (y22 != 0.0d) {
                return y42 * y32 <= 0.0d || (y32 * y22 >= 0.0d && (y22 <= 0.0d ? y32 >= y22 || y42 >= y22 : y32 <= y22 || y42 <= y22));
            }
            return false;
        }
        if (x42 * x32 > 0.0d) {
            if (x32 * x22 >= 0.0d) {
                if (x22 > 0.0d) {
                }
            }
            return false;
        }
        return true;
    }

    public static List<Rectangle> createBoundingRectanglesFromQuadPoint(PdfArray quadPoints) throws PdfException {
        List<Rectangle> boundingRectangles = new ArrayList<>();
        if (quadPoints.size() % 8 != 0) {
            throw new PdfException(PdfException.QuadPointArrayLengthIsNotAMultipleOfEight);
        }
        for (int i = 0; i < quadPoints.size(); i += 8) {
            float[] quadPointEntry = Arrays.copyOfRange(quadPoints.toFloatArray(), i, i + 8);
            PdfArray quadPointEntryFA = new PdfArray(quadPointEntry);
            boundingRectangles.add(createBoundingRectangleFromQuadPoint(quadPointEntryFA));
        }
        return boundingRectangles;
    }

    public static Rectangle createBoundingRectangleFromQuadPoint(PdfArray quadPoints) throws PdfException {
        if (quadPoints.size() % 8 != 0) {
            throw new PdfException(PdfException.QuadPointArrayLengthIsNotAMultipleOfEight);
        }
        float llx = Float.MAX_VALUE;
        float lly = Float.MAX_VALUE;
        float urx = -3.4028235E38f;
        float ury = -3.4028235E38f;
        for (int j = 0; j < 8; j += 2) {
            float x = quadPoints.getAsNumber(j).floatValue();
            float y = quadPoints.getAsNumber(j + 1).floatValue();
            if (x < llx) {
                llx = x;
            }
            if (x > urx) {
                urx = x;
            }
            if (y < lly) {
                lly = y;
            }
            if (y > ury) {
                ury = y;
            }
        }
        return new Rectangle(llx, lly, urx - llx, ury - lly);
    }
}

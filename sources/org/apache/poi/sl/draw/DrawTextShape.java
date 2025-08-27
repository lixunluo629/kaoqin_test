package org.apache.poi.sl.draw;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import org.apache.poi.sl.usermodel.Insets2D;
import org.apache.poi.sl.usermodel.PlaceableShape;
import org.apache.poi.sl.usermodel.ShapeContainer;
import org.apache.poi.sl.usermodel.TextParagraph;
import org.apache.poi.sl.usermodel.TextRun;
import org.apache.poi.sl.usermodel.TextShape;

/* loaded from: poi-3.17.jar:org/apache/poi/sl/draw/DrawTextShape.class */
public class DrawTextShape extends DrawSimpleShape {
    public DrawTextShape(TextShape<?, ?> shape) {
        super(shape);
    }

    @Override // org.apache.poi.sl.draw.DrawShape, org.apache.poi.sl.draw.Drawable
    public void drawContent(Graphics2D graphics) {
        double y;
        DrawFactory.getInstance(graphics).fixFonts(graphics);
        TextShape<?, ?> s = getShape();
        Rectangle2D anchor = DrawShape.getAnchor(graphics, s);
        Insets2D insets = s.getInsets();
        double x = anchor.getX() + insets.left;
        double y2 = anchor.getY();
        AffineTransform tx = graphics.getTransform();
        boolean vertFlip = s.getFlipVertical();
        boolean horzFlip = s.getFlipHorizontal();
        ShapeContainer<?, ?> parent = s.getParent();
        while (true) {
            ShapeContainer<?, ?> sc = parent;
            if (!(sc instanceof PlaceableShape)) {
                break;
            }
            PlaceableShape<?, ?> ps = (PlaceableShape) sc;
            vertFlip ^= ps.getFlipVertical();
            horzFlip ^= ps.getFlipHorizontal();
            parent = ps.getParent();
        }
        if (horzFlip ^ vertFlip) {
            double ax = anchor.getX();
            double ay = anchor.getY();
            graphics.translate(ax + anchor.getWidth(), ay);
            graphics.scale(-1.0d, 1.0d);
            graphics.translate(-ax, -ay);
        }
        Double textRot = s.getTextRotation();
        if (textRot != null && textRot.doubleValue() != 0.0d) {
            double cx = anchor.getCenterX();
            double cy = anchor.getCenterY();
            graphics.translate(cx, cy);
            graphics.rotate(Math.toRadians(textRot.doubleValue()));
            graphics.translate(-cx, -cy);
        }
        switch (s.getVerticalAlignment()) {
            case TOP:
            default:
                y = y2 + insets.top;
                break;
            case BOTTOM:
                double textHeight = getTextHeight(graphics);
                y = y2 + ((anchor.getHeight() - textHeight) - insets.bottom);
                break;
            case MIDDLE:
                double textHeight2 = getTextHeight(graphics);
                double delta = ((anchor.getHeight() - textHeight2) - insets.top) - insets.bottom;
                y = y2 + insets.top + (delta / 2.0d);
                break;
        }
        TextShape.TextDirection textDir = s.getTextDirection();
        if (textDir == TextShape.TextDirection.VERTICAL || textDir == TextShape.TextDirection.VERTICAL_270) {
            double deg = textDir == TextShape.TextDirection.VERTICAL ? 90.0d : 270.0d;
            double cx2 = anchor.getCenterX();
            double cy2 = anchor.getCenterY();
            graphics.translate(cx2, cy2);
            graphics.rotate(Math.toRadians(deg));
            graphics.translate(-cx2, -cy2);
            double w = anchor.getWidth();
            double h = anchor.getHeight();
            double dx = (w - h) / 2.0d;
            graphics.translate(dx, -dx);
        }
        drawParagraphs(graphics, x, y);
        graphics.setTransform(tx);
    }

    public double drawParagraphs(Graphics2D graphics, double x, double y) {
        double y2;
        DrawFactory fact = DrawFactory.getInstance(graphics);
        Iterator<? extends TextParagraph<?, ?, ? extends TextRun>> paragraphs = getShape().iterator();
        boolean isFirstLine = true;
        int autoNbrIdx = 0;
        while (paragraphs.hasNext()) {
            TextParagraph<?, ?, ? extends TextRun> p = paragraphs.next();
            DrawTextParagraph dp = fact.getDrawable(p);
            TextParagraph.BulletStyle bs = p.getBulletStyle();
            if (bs == null || bs.getAutoNumberingScheme() == null) {
                autoNbrIdx = -1;
            } else {
                Integer startAt = bs.getAutoNumberingStartAt();
                if (startAt == null) {
                    startAt = 1;
                }
                if (startAt.intValue() > autoNbrIdx) {
                    autoNbrIdx = startAt.intValue();
                }
            }
            dp.setAutoNumberingIdx(autoNbrIdx);
            dp.breakText(graphics);
            if (isFirstLine) {
                y2 = y + dp.getFirstLineLeading();
            } else {
                Double spaceBefore = p.getSpaceBefore();
                if (spaceBefore == null) {
                    spaceBefore = Double.valueOf(0.0d);
                }
                if (spaceBefore.doubleValue() > 0.0d) {
                    y2 = y + (spaceBefore.doubleValue() * 0.01d * dp.getFirstLineHeight());
                } else {
                    y2 = y + (-spaceBefore.doubleValue());
                }
            }
            isFirstLine = false;
            dp.setPosition(x, y2);
            dp.draw(graphics);
            y = y2 + dp.getY();
            if (paragraphs.hasNext()) {
                Double spaceAfter = p.getSpaceAfter();
                if (spaceAfter == null) {
                    spaceAfter = Double.valueOf(0.0d);
                }
                if (spaceAfter.doubleValue() > 0.0d) {
                    y += spaceAfter.doubleValue() * 0.01d * dp.getLastLineHeight();
                } else {
                    y += -spaceAfter.doubleValue();
                }
            }
            autoNbrIdx++;
        }
        return y - y;
    }

    public double getTextHeight() {
        return getTextHeight(null);
    }

    public double getTextHeight(Graphics2D oldGraphics) {
        BufferedImage img = new BufferedImage(1, 1, 1);
        Graphics2D graphics = img.createGraphics();
        if (oldGraphics != null) {
            graphics.addRenderingHints(oldGraphics.getRenderingHints());
            graphics.setTransform(oldGraphics.getTransform());
        }
        DrawFactory.getInstance(graphics).fixFonts(graphics);
        return drawParagraphs(graphics, 0.0d, 0.0d);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.poi.sl.draw.DrawSimpleShape, org.apache.poi.sl.draw.DrawShape
    public TextShape<?, ? extends TextParagraph<?, ?, ? extends TextRun>> getShape() {
        return (TextShape) this.shape;
    }
}

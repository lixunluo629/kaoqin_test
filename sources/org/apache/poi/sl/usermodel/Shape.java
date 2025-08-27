package org.apache.poi.sl.usermodel;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import org.apache.poi.sl.usermodel.Shape;
import org.apache.poi.sl.usermodel.TextParagraph;

/* loaded from: poi-3.17.jar:org/apache/poi/sl/usermodel/Shape.class */
public interface Shape<S extends Shape<S, P>, P extends TextParagraph<S, P, ?>> {
    ShapeContainer<S, P> getParent();

    Sheet<S, P> getSheet();

    Rectangle2D getAnchor();

    void draw(Graphics2D graphics2D, Rectangle2D rectangle2D);
}

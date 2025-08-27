package org.apache.poi.sl.usermodel;

import java.awt.geom.Rectangle2D;
import org.apache.poi.sl.usermodel.Shape;
import org.apache.poi.sl.usermodel.TextParagraph;

/* loaded from: poi-3.17.jar:org/apache/poi/sl/usermodel/PlaceableShape.class */
public interface PlaceableShape<S extends Shape<S, P>, P extends TextParagraph<S, P, ?>> {
    ShapeContainer<S, P> getParent();

    Sheet<S, P> getSheet();

    Rectangle2D getAnchor();

    void setAnchor(Rectangle2D rectangle2D);

    double getRotation();

    void setRotation(double d);

    void setFlipHorizontal(boolean z);

    void setFlipVertical(boolean z);

    boolean getFlipHorizontal();

    boolean getFlipVertical();
}

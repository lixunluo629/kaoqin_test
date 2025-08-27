package org.apache.poi.sl.usermodel;

import java.awt.Color;
import org.apache.poi.sl.draw.geom.CustomGeometry;
import org.apache.poi.sl.draw.geom.IAdjustableShape;
import org.apache.poi.sl.usermodel.Shape;
import org.apache.poi.sl.usermodel.TextParagraph;

/* loaded from: poi-3.17.jar:org/apache/poi/sl/usermodel/SimpleShape.class */
public interface SimpleShape<S extends Shape<S, P>, P extends TextParagraph<S, P, ?>> extends Shape<S, P>, IAdjustableShape, PlaceableShape<S, P> {
    FillStyle getFillStyle();

    LineDecoration getLineDecoration();

    StrokeStyle getStrokeStyle();

    void setStrokeStyle(Object... objArr);

    CustomGeometry getGeometry();

    ShapeType getShapeType();

    void setShapeType(ShapeType shapeType);

    Placeholder getPlaceholder();

    void setPlaceholder(Placeholder placeholder);

    Shadow<S, P> getShadow();

    Color getFillColor();

    void setFillColor(Color color);

    Hyperlink<S, P> getHyperlink();

    Hyperlink<S, P> createHyperlink();
}

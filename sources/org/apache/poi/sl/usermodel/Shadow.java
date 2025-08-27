package org.apache.poi.sl.usermodel;

import org.apache.poi.sl.usermodel.PaintStyle;
import org.apache.poi.sl.usermodel.Shape;
import org.apache.poi.sl.usermodel.TextParagraph;

/* loaded from: poi-3.17.jar:org/apache/poi/sl/usermodel/Shadow.class */
public interface Shadow<S extends Shape<S, P>, P extends TextParagraph<S, P, ?>> {
    SimpleShape<S, P> getShadowParent();

    double getDistance();

    double getAngle();

    double getBlur();

    PaintStyle.SolidPaint getFillStyle();
}

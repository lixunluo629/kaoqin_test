package org.apache.poi.sl.usermodel;

import org.apache.poi.sl.usermodel.Shape;
import org.apache.poi.sl.usermodel.TextParagraph;

/* loaded from: poi-3.17.jar:org/apache/poi/sl/usermodel/Background.class */
public interface Background<S extends Shape<S, P>, P extends TextParagraph<S, P, ?>> extends Shape<S, P> {
    FillStyle getFillStyle();
}

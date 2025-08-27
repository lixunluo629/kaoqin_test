package org.apache.poi.sl.usermodel;

import org.apache.poi.sl.usermodel.Shape;
import org.apache.poi.sl.usermodel.TextParagraph;

/* loaded from: poi-3.17.jar:org/apache/poi/sl/usermodel/Hyperlink.class */
public interface Hyperlink<S extends Shape<S, P>, P extends TextParagraph<S, P, ?>> extends org.apache.poi.common.usermodel.Hyperlink {
    void linkToEmail(String str);

    void linkToUrl(String str);

    void linkToSlide(Slide<S, P> slide);

    void linkToNextSlide();

    void linkToPreviousSlide();

    void linkToFirstSlide();

    void linkToLastSlide();
}

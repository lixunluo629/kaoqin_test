package org.apache.poi.sl.usermodel;

import java.awt.Graphics2D;
import org.apache.poi.sl.usermodel.Shape;
import org.apache.poi.sl.usermodel.TextParagraph;

/* loaded from: poi-3.17.jar:org/apache/poi/sl/usermodel/Sheet.class */
public interface Sheet<S extends Shape<S, P>, P extends TextParagraph<S, P, ?>> extends ShapeContainer<S, P> {
    SlideShow<S, P> getSlideShow();

    boolean getFollowMasterGraphics();

    MasterSheet<S, P> getMasterSheet();

    Background<S, P> getBackground();

    void draw(Graphics2D graphics2D);
}

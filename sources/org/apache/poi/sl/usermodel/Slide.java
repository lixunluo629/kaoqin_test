package org.apache.poi.sl.usermodel;

import org.apache.poi.sl.usermodel.Shape;
import org.apache.poi.sl.usermodel.TextParagraph;

/* loaded from: poi-3.17.jar:org/apache/poi/sl/usermodel/Slide.class */
public interface Slide<S extends Shape<S, P>, P extends TextParagraph<S, P, ?>> extends Sheet<S, P> {
    Notes<S, P> getNotes();

    void setNotes(Notes<S, P> notes);

    boolean getFollowMasterBackground();

    void setFollowMasterBackground(boolean z);

    boolean getFollowMasterColourScheme();

    void setFollowMasterColourScheme(boolean z);

    boolean getFollowMasterObjects();

    void setFollowMasterObjects(boolean z);

    int getSlideNumber();

    String getTitle();

    boolean getDisplayPlaceholder(Placeholder placeholder);
}

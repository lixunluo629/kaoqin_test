package org.apache.poi.ss.usermodel;

import org.apache.poi.util.Removal;

@Removal(version = "3.18")
@Deprecated
/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/Textbox.class */
public interface Textbox {
    public static final short OBJECT_TYPE_TEXT = 6;

    RichTextString getString();

    void setString(RichTextString richTextString);

    int getMarginLeft();

    void setMarginLeft(int i);

    int getMarginRight();

    void setMarginRight(int i);

    int getMarginTop();

    void setMarginTop(int i);

    int getMarginBottom();

    void setMarginBottom(int i);
}

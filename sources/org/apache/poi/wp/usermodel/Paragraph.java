package org.apache.poi.wp.usermodel;

/* loaded from: poi-3.17.jar:org/apache/poi/wp/usermodel/Paragraph.class */
public interface Paragraph {
    int getIndentFromRight();

    void setIndentFromRight(int i);

    int getIndentFromLeft();

    void setIndentFromLeft(int i);

    int getFirstLineIndent();

    void setFirstLineIndent(int i);

    int getFontAlignment();

    void setFontAlignment(int i);

    boolean isWordWrapped();

    void setWordWrapped(boolean z);
}

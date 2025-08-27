package org.apache.poi.wp.usermodel;

/* loaded from: poi-3.17.jar:org/apache/poi/wp/usermodel/CharacterRun.class */
public interface CharacterRun {
    boolean isBold();

    void setBold(boolean z);

    boolean isItalic();

    void setItalic(boolean z);

    boolean isSmallCaps();

    void setSmallCaps(boolean z);

    boolean isCapitalized();

    void setCapitalized(boolean z);

    boolean isStrikeThrough();

    void setStrikeThrough(boolean z);

    boolean isDoubleStrikeThrough();

    void setDoubleStrikethrough(boolean z);

    boolean isShadowed();

    void setShadow(boolean z);

    boolean isEmbossed();

    void setEmbossed(boolean z);

    boolean isImprinted();

    void setImprinted(boolean z);

    int getFontSize();

    void setFontSize(int i);

    int getCharacterSpacing();

    void setCharacterSpacing(int i);

    int getKerning();

    void setKerning(int i);

    boolean isHighlighted();

    String getFontName();

    String text();
}

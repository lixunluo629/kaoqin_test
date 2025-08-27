package org.apache.poi.sl.usermodel;

import java.awt.Color;
import org.apache.poi.common.usermodel.fonts.FontGroup;
import org.apache.poi.common.usermodel.fonts.FontInfo;
import org.apache.poi.util.Internal;

/* loaded from: poi-3.17.jar:org/apache/poi/sl/usermodel/TextRun.class */
public interface TextRun {

    /* loaded from: poi-3.17.jar:org/apache/poi/sl/usermodel/TextRun$FieldType.class */
    public enum FieldType {
        SLIDE_NUMBER,
        DATE_TIME
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/sl/usermodel/TextRun$TextCap.class */
    public enum TextCap {
        NONE,
        SMALL,
        ALL
    }

    String getRawText();

    void setText(String str);

    TextCap getTextCap();

    PaintStyle getFontColor();

    void setFontColor(Color color);

    void setFontColor(PaintStyle paintStyle);

    Double getFontSize();

    void setFontSize(Double d);

    String getFontFamily();

    String getFontFamily(FontGroup fontGroup);

    void setFontFamily(String str);

    void setFontFamily(String str, FontGroup fontGroup);

    FontInfo getFontInfo(FontGroup fontGroup);

    void setFontInfo(FontInfo fontInfo, FontGroup fontGroup);

    boolean isBold();

    void setBold(boolean z);

    boolean isItalic();

    void setItalic(boolean z);

    boolean isUnderlined();

    void setUnderlined(boolean z);

    boolean isStrikethrough();

    void setStrikethrough(boolean z);

    boolean isSubscript();

    boolean isSuperscript();

    byte getPitchAndFamily();

    Hyperlink<?, ?> getHyperlink();

    Hyperlink<?, ?> createHyperlink();

    @Internal
    FieldType getFieldType();
}

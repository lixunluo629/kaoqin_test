package org.apache.poi.sl.usermodel;

import java.awt.Color;
import java.util.List;
import org.apache.poi.sl.usermodel.Shape;
import org.apache.poi.sl.usermodel.TextParagraph;
import org.apache.poi.sl.usermodel.TextRun;

/* loaded from: poi-3.17.jar:org/apache/poi/sl/usermodel/TextParagraph.class */
public interface TextParagraph<S extends Shape<S, P>, P extends TextParagraph<S, P, T>, T extends TextRun> extends Iterable<T> {

    /* loaded from: poi-3.17.jar:org/apache/poi/sl/usermodel/TextParagraph$BulletStyle.class */
    public interface BulletStyle {
        String getBulletCharacter();

        String getBulletFont();

        Double getBulletFontSize();

        void setBulletFontColor(Color color);

        void setBulletFontColor(PaintStyle paintStyle);

        PaintStyle getBulletFontColor();

        AutoNumberingScheme getAutoNumberingScheme();

        Integer getAutoNumberingStartAt();
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/sl/usermodel/TextParagraph$FontAlign.class */
    public enum FontAlign {
        AUTO,
        TOP,
        CENTER,
        BASELINE,
        BOTTOM
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/sl/usermodel/TextParagraph$TextAlign.class */
    public enum TextAlign {
        LEFT,
        CENTER,
        RIGHT,
        JUSTIFY,
        JUSTIFY_LOW,
        DIST,
        THAI_DIST
    }

    Double getSpaceBefore();

    void setSpaceBefore(Double d);

    Double getSpaceAfter();

    void setSpaceAfter(Double d);

    Double getLeftMargin();

    void setLeftMargin(Double d);

    Double getRightMargin();

    void setRightMargin(Double d);

    Double getIndent();

    void setIndent(Double d);

    int getIndentLevel();

    void setIndentLevel(int i);

    Double getLineSpacing();

    void setLineSpacing(Double d);

    String getDefaultFontFamily();

    Double getDefaultFontSize();

    TextAlign getTextAlign();

    void setTextAlign(TextAlign textAlign);

    FontAlign getFontAlign();

    BulletStyle getBulletStyle();

    void setBulletStyle(Object... objArr);

    Double getDefaultTabSize();

    TextShape<S, P> getParentShape();

    List<T> getTextRuns();

    boolean isHeaderOrFooter();
}

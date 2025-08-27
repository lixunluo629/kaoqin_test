package org.apache.poi.sl.usermodel;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import org.apache.poi.sl.usermodel.Shape;
import org.apache.poi.sl.usermodel.TextParagraph;

/* loaded from: poi-3.17.jar:org/apache/poi/sl/usermodel/TextShape.class */
public interface TextShape<S extends Shape<S, P>, P extends TextParagraph<S, P, ?>> extends SimpleShape<S, P>, Iterable<P> {

    /* loaded from: poi-3.17.jar:org/apache/poi/sl/usermodel/TextShape$TextAutofit.class */
    public enum TextAutofit {
        NONE,
        NORMAL,
        SHAPE
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/sl/usermodel/TextShape$TextDirection.class */
    public enum TextDirection {
        HORIZONTAL,
        VERTICAL,
        VERTICAL_270,
        STACKED
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/sl/usermodel/TextShape$TextPlaceholder.class */
    public enum TextPlaceholder {
        TITLE,
        BODY,
        CENTER_TITLE,
        CENTER_BODY,
        HALF_BODY,
        QUARTER_BODY,
        NOTES,
        OTHER
    }

    String getText();

    TextRun setText(String str);

    TextRun appendText(String str, boolean z);

    List<? extends TextParagraph<S, P, ?>> getTextParagraphs();

    Insets2D getInsets();

    void setInsets(Insets2D insets2D);

    double getTextHeight();

    double getTextHeight(Graphics2D graphics2D);

    VerticalAlignment getVerticalAlignment();

    void setVerticalAlignment(VerticalAlignment verticalAlignment);

    boolean isHorizontalCentered();

    void setHorizontalCentered(Boolean bool);

    boolean getWordWrap();

    void setWordWrap(boolean z);

    TextDirection getTextDirection();

    void setTextDirection(TextDirection textDirection);

    Double getTextRotation();

    void setTextRotation(Double d);

    void setTextPlaceholder(TextPlaceholder textPlaceholder);

    TextPlaceholder getTextPlaceholder();

    Rectangle2D resizeToFitText();

    Rectangle2D resizeToFitText(Graphics2D graphics2D);
}

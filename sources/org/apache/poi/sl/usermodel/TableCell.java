package org.apache.poi.sl.usermodel;

import java.awt.Color;
import org.apache.poi.sl.usermodel.Shape;
import org.apache.poi.sl.usermodel.StrokeStyle;
import org.apache.poi.sl.usermodel.TextParagraph;

/* loaded from: poi-3.17.jar:org/apache/poi/sl/usermodel/TableCell.class */
public interface TableCell<S extends Shape<S, P>, P extends TextParagraph<S, P, ?>> extends TextShape<S, P> {

    /* loaded from: poi-3.17.jar:org/apache/poi/sl/usermodel/TableCell$BorderEdge.class */
    public enum BorderEdge {
        bottom,
        left,
        top,
        right
    }

    StrokeStyle getBorderStyle(BorderEdge borderEdge);

    void setBorderStyle(BorderEdge borderEdge, StrokeStyle strokeStyle);

    void setBorderWidth(BorderEdge borderEdge, double d);

    void setBorderColor(BorderEdge borderEdge, Color color);

    void setBorderCompound(BorderEdge borderEdge, StrokeStyle.LineCompound lineCompound);

    void setBorderDash(BorderEdge borderEdge, StrokeStyle.LineDash lineDash);

    void removeBorder(BorderEdge borderEdge);

    int getGridSpan();

    int getRowSpan();

    boolean isMerged();
}

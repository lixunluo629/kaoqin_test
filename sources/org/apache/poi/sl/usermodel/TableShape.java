package org.apache.poi.sl.usermodel;

import org.apache.poi.sl.usermodel.Shape;
import org.apache.poi.sl.usermodel.TextParagraph;

/* loaded from: poi-3.17.jar:org/apache/poi/sl/usermodel/TableShape.class */
public interface TableShape<S extends Shape<S, P>, P extends TextParagraph<S, P, ?>> extends Shape<S, P>, PlaceableShape<S, P> {
    int getNumberOfColumns();

    int getNumberOfRows();

    TableCell<S, P> getCell(int i, int i2);

    double getColumnWidth(int i);

    void setColumnWidth(int i, double d);

    double getRowHeight(int i);

    void setRowHeight(int i, double d);
}

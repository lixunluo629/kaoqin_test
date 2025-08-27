package org.apache.poi.ss.usermodel;

import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/CellRange.class */
public interface CellRange<C extends Cell> extends Iterable<C> {
    int getWidth();

    int getHeight();

    int size();

    String getReferenceText();

    C getTopLeftCell();

    C getCell(int i, int i2);

    C[] getFlattenedCells();

    C[][] getCells();

    @Override // java.lang.Iterable
    Iterator<C> iterator();
}

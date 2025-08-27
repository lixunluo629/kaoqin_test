package org.apache.poi.ss.usermodel;

import java.util.Iterator;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/Row.class */
public interface Row extends Iterable<Cell> {

    /* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/Row$MissingCellPolicy.class */
    public enum MissingCellPolicy {
        RETURN_NULL_AND_BLANK,
        RETURN_BLANK_AS_NULL,
        CREATE_NULL_AS_BLANK
    }

    Cell createCell(int i);

    Cell createCell(int i, int i2);

    Cell createCell(int i, CellType cellType);

    void removeCell(Cell cell);

    void setRowNum(int i);

    int getRowNum();

    Cell getCell(int i);

    Cell getCell(int i, MissingCellPolicy missingCellPolicy);

    short getFirstCellNum();

    short getLastCellNum();

    int getPhysicalNumberOfCells();

    void setHeight(short s);

    void setZeroHeight(boolean z);

    boolean getZeroHeight();

    void setHeightInPoints(float f);

    short getHeight();

    float getHeightInPoints();

    boolean isFormatted();

    CellStyle getRowStyle();

    void setRowStyle(CellStyle cellStyle);

    Iterator<Cell> cellIterator();

    Sheet getSheet();

    int getOutlineLevel();
}

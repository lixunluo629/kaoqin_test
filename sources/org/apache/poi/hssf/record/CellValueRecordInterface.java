package org.apache.poi.hssf.record;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/CellValueRecordInterface.class */
public interface CellValueRecordInterface {
    int getRow();

    short getColumn();

    void setRow(int i);

    void setColumn(short s);

    void setXFIndex(short s);

    short getXFIndex();
}

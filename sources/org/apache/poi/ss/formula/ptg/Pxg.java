package org.apache.poi.ss.formula.ptg;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/ptg/Pxg.class */
public interface Pxg {
    int getExternalWorkbookNumber();

    String getSheetName();

    void setSheetName(String str);

    String toFormulaString();
}

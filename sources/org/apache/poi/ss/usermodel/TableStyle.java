package org.apache.poi.ss.usermodel;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/TableStyle.class */
public interface TableStyle {
    String getName();

    int getIndex();

    boolean isBuiltin();

    DifferentialStyleProvider getStyle(TableStyleType tableStyleType);
}

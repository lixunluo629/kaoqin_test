package org.apache.poi.ss.usermodel;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/TableStyleInfo.class */
public interface TableStyleInfo {
    boolean isShowColumnStripes();

    boolean isShowRowStripes();

    boolean isShowFirstColumn();

    boolean isShowLastColumn();

    String getName();

    TableStyle getStyle();
}

package org.apache.poi.ss.usermodel;

import java.util.regex.Pattern;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/Table.class */
public interface Table {
    public static final Pattern isStructuredReference = Pattern.compile("[a-zA-Z_\\\\][a-zA-Z0-9._]*\\[.*\\]");

    int getStartColIndex();

    int getStartRowIndex();

    int getEndColIndex();

    int getEndRowIndex();

    String getName();

    String getStyleName();

    int findColumnIndex(String str);

    String getSheetName();

    boolean isHasTotalsRow();

    int getTotalsRowCount();

    int getHeaderRowCount();

    TableStyleInfo getStyle();

    boolean contains(Cell cell);
}

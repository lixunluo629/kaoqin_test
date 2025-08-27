package org.apache.poi.ss.usermodel;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/Hyperlink.class */
public interface Hyperlink extends org.apache.poi.common.usermodel.Hyperlink {
    int getFirstRow();

    void setFirstRow(int i);

    int getLastRow();

    void setLastRow(int i);

    int getFirstColumn();

    void setFirstColumn(int i);

    int getLastColumn();

    void setLastColumn(int i);
}

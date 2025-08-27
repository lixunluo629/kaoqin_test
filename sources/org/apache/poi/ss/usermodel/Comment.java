package org.apache.poi.ss.usermodel;

import org.apache.poi.ss.util.CellAddress;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/Comment.class */
public interface Comment {
    void setVisible(boolean z);

    boolean isVisible();

    CellAddress getAddress();

    void setAddress(CellAddress cellAddress);

    void setAddress(int i, int i2);

    int getRow();

    void setRow(int i);

    int getColumn();

    void setColumn(int i);

    String getAuthor();

    void setAuthor(String str);

    RichTextString getString();

    void setString(RichTextString richTextString);

    ClientAnchor getClientAnchor();
}

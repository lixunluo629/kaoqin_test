package org.apache.poi.ss.usermodel;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/Name.class */
public interface Name {
    String getSheetName();

    String getNameName();

    void setNameName(String str);

    String getRefersToFormula();

    void setRefersToFormula(String str);

    boolean isFunctionName();

    boolean isDeleted();

    void setSheetIndex(int i);

    int getSheetIndex();

    String getComment();

    void setComment(String str);

    void setFunction(boolean z);
}

package org.apache.poi.ss.usermodel;

import org.apache.poi.ss.util.CellRangeAddressList;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/DataValidation.class */
public interface DataValidation {

    /* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/DataValidation$ErrorStyle.class */
    public static final class ErrorStyle {
        public static final int STOP = 0;
        public static final int WARNING = 1;
        public static final int INFO = 2;
    }

    DataValidationConstraint getValidationConstraint();

    void setErrorStyle(int i);

    int getErrorStyle();

    void setEmptyCellAllowed(boolean z);

    boolean getEmptyCellAllowed();

    void setSuppressDropDownArrow(boolean z);

    boolean getSuppressDropDownArrow();

    void setShowPromptBox(boolean z);

    boolean getShowPromptBox();

    void setShowErrorBox(boolean z);

    boolean getShowErrorBox();

    void createPromptBox(String str, String str2);

    String getPromptBoxTitle();

    String getPromptBoxText();

    void createErrorBox(String str, String str2);

    String getErrorBoxTitle();

    String getErrorBoxText();

    CellRangeAddressList getRegions();
}

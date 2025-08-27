package org.apache.poi.ss.usermodel;

import org.apache.poi.ss.util.CellRangeAddressList;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/DataValidationHelper.class */
public interface DataValidationHelper {
    DataValidationConstraint createFormulaListConstraint(String str);

    DataValidationConstraint createExplicitListConstraint(String[] strArr);

    DataValidationConstraint createNumericConstraint(int i, int i2, String str, String str2);

    DataValidationConstraint createTextLengthConstraint(int i, String str, String str2);

    DataValidationConstraint createDecimalConstraint(int i, String str, String str2);

    DataValidationConstraint createIntegerConstraint(int i, String str, String str2);

    DataValidationConstraint createDateConstraint(int i, String str, String str2, String str3);

    DataValidationConstraint createTimeConstraint(int i, String str, String str2);

    DataValidationConstraint createCustomConstraint(String str);

    DataValidation createValidation(DataValidationConstraint dataValidationConstraint, CellRangeAddressList cellRangeAddressList);
}

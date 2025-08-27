package org.apache.poi.hssf.usermodel;

import org.apache.poi.ss.usermodel.AutoFilter;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/usermodel/HSSFAutoFilter.class */
public final class HSSFAutoFilter implements AutoFilter {
    private HSSFSheet _sheet;

    HSSFAutoFilter(HSSFSheet sheet) {
        this._sheet = sheet;
    }
}

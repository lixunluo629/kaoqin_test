package org.apache.poi.xssf.usermodel;

import org.apache.poi.ss.usermodel.AutoFilter;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/usermodel/XSSFAutoFilter.class */
public final class XSSFAutoFilter implements AutoFilter {
    private XSSFSheet _sheet;

    XSSFAutoFilter(XSSFSheet sheet) {
        this._sheet = sheet;
    }
}

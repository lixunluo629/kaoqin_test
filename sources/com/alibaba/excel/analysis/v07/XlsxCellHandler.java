package com.alibaba.excel.analysis.v07;

import org.xml.sax.Attributes;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/analysis/v07/XlsxCellHandler.class */
public interface XlsxCellHandler {
    boolean support(String str);

    void startHandle(String str, Attributes attributes);

    void endHandle(String str);
}

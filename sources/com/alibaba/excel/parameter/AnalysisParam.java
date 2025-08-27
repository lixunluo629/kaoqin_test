package com.alibaba.excel.parameter;

import com.alibaba.excel.support.ExcelTypeEnum;
import java.io.InputStream;

@Deprecated
/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/parameter/AnalysisParam.class */
public class AnalysisParam {
    private ExcelTypeEnum excelTypeEnum;
    private InputStream in;
    private Object customContent;

    public AnalysisParam(InputStream in, ExcelTypeEnum excelTypeEnum, Object customContent) {
        this.in = in;
        this.excelTypeEnum = excelTypeEnum;
        this.customContent = customContent;
    }

    public ExcelTypeEnum getExcelTypeEnum() {
        return this.excelTypeEnum;
    }

    public void setExcelTypeEnum(ExcelTypeEnum excelTypeEnum) {
        this.excelTypeEnum = excelTypeEnum;
    }

    public Object getCustomContent() {
        return this.customContent;
    }

    public void setCustomContent(Object customContent) {
        this.customContent = customContent;
    }

    public InputStream getIn() {
        return this.in;
    }

    public void setIn(InputStream in) {
        this.in = in;
    }
}

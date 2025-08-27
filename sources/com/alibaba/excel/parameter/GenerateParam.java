package com.alibaba.excel.parameter;

import com.alibaba.excel.support.ExcelTypeEnum;
import java.io.OutputStream;

@Deprecated
/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/parameter/GenerateParam.class */
public class GenerateParam {
    private OutputStream outputStream;
    private String sheetName;
    private Class clazz;
    private ExcelTypeEnum type;
    private boolean needHead = true;

    public GenerateParam(String sheetName, Class clazz, OutputStream outputStream) {
        this.outputStream = outputStream;
        this.sheetName = sheetName;
        this.clazz = clazz;
    }

    public OutputStream getOutputStream() {
        return this.outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public String getSheetName() {
        return this.sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public Class getClazz() {
        return this.clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public ExcelTypeEnum getType() {
        return this.type;
    }

    public void setType(ExcelTypeEnum type) {
        this.type = type;
    }

    public boolean isNeedHead() {
        return this.needHead;
    }

    public void setNeedHead(boolean needHead) {
        this.needHead = needHead;
    }
}

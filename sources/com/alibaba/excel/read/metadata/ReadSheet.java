package com.alibaba.excel.read.metadata;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/read/metadata/ReadSheet.class */
public class ReadSheet extends ReadBasicParameter {
    private Integer sheetNo;
    private String sheetName;

    public ReadSheet() {
    }

    public ReadSheet(Integer sheetNo) {
        this.sheetNo = sheetNo;
    }

    public ReadSheet(Integer sheetNo, String sheetName) {
        this.sheetNo = sheetNo;
        this.sheetName = sheetName;
    }

    public Integer getSheetNo() {
        return this.sheetNo;
    }

    public void setSheetNo(Integer sheetNo) {
        this.sheetNo = sheetNo;
    }

    public String getSheetName() {
        return this.sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public void copyBasicParameter(ReadSheet other) {
        if (other == null) {
            return;
        }
        setHeadRowNumber(other.getHeadRowNumber());
        setCustomReadListenerList(other.getCustomReadListenerList());
        setHead(other.getHead());
        setClazz(other.getClazz());
        setCustomConverterList(other.getCustomConverterList());
        setAutoTrim(other.getAutoTrim());
        setUse1904windowing(other.getUse1904windowing());
    }

    public String toString() {
        return "ReadSheet{sheetNo=" + this.sheetNo + ", sheetName='" + this.sheetName + "'} " + super.toString();
    }
}

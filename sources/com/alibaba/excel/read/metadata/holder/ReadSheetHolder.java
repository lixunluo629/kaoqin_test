package com.alibaba.excel.read.metadata.holder;

import com.alibaba.excel.enums.HolderEnum;
import com.alibaba.excel.read.metadata.ReadSheet;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/read/metadata/holder/ReadSheetHolder.class */
public class ReadSheetHolder extends AbstractReadHolder {
    private ReadSheet readSheet;
    private ReadWorkbookHolder parentReadWorkbookHolder;
    private Integer sheetNo;
    private String sheetName;
    private Integer approximateTotalRowNumber;

    public ReadSheetHolder(ReadSheet readSheet, ReadWorkbookHolder readWorkbookHolder) {
        super(readSheet, readWorkbookHolder, readWorkbookHolder.getReadWorkbook().getConvertAllFiled());
        this.readSheet = readSheet;
        this.parentReadWorkbookHolder = readWorkbookHolder;
        this.sheetNo = readSheet.getSheetNo();
        this.sheetName = readSheet.getSheetName();
    }

    public ReadSheet getReadSheet() {
        return this.readSheet;
    }

    public void setReadSheet(ReadSheet readSheet) {
        this.readSheet = readSheet;
    }

    public ReadWorkbookHolder getParentReadWorkbookHolder() {
        return this.parentReadWorkbookHolder;
    }

    public void setParentReadWorkbookHolder(ReadWorkbookHolder parentReadWorkbookHolder) {
        this.parentReadWorkbookHolder = parentReadWorkbookHolder;
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

    @Deprecated
    public Integer getTotal() {
        return this.approximateTotalRowNumber;
    }

    public Integer getApproximateTotalRowNumber() {
        return this.approximateTotalRowNumber;
    }

    public void setApproximateTotalRowNumber(Integer approximateTotalRowNumber) {
        this.approximateTotalRowNumber = approximateTotalRowNumber;
    }

    @Override // com.alibaba.excel.metadata.Holder
    public HolderEnum holderType() {
        return HolderEnum.SHEET;
    }

    public String toString() {
        return "ReadSheetHolder{sheetNo=" + this.sheetNo + ", sheetName='" + this.sheetName + "'} " + super.toString();
    }
}

package com.alibaba.excel.write.metadata.holder;

import com.alibaba.excel.enums.HolderEnum;
import com.alibaba.excel.enums.WriteLastRowTypeEnum;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.ss.usermodel.Sheet;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/write/metadata/holder/WriteSheetHolder.class */
public class WriteSheetHolder extends AbstractWriteHolder {
    private WriteSheet writeSheet;
    private Sheet sheet;
    private Sheet cachedSheet;
    private Integer sheetNo;
    private String sheetName;
    private WriteWorkbookHolder parentWriteWorkbookHolder;
    private Map<Integer, WriteTableHolder> hasBeenInitializedTable;
    private WriteLastRowTypeEnum writeLastRowTypeEnum;

    public WriteSheetHolder(WriteSheet writeSheet, WriteWorkbookHolder writeWorkbookHolder) {
        super(writeSheet, writeWorkbookHolder, writeWorkbookHolder.getWriteWorkbook().getConvertAllFiled());
        this.writeSheet = writeSheet;
        if (writeSheet.getSheetNo() == null && StringUtils.isEmpty(writeSheet.getSheetName())) {
            this.sheetNo = 0;
        } else {
            this.sheetNo = writeSheet.getSheetNo();
        }
        this.sheetName = writeSheet.getSheetName();
        this.parentWriteWorkbookHolder = writeWorkbookHolder;
        this.hasBeenInitializedTable = new HashMap();
        if (writeWorkbookHolder.getTempTemplateInputStream() != null) {
            this.writeLastRowTypeEnum = WriteLastRowTypeEnum.TEMPLATE_EMPTY;
        } else {
            this.writeLastRowTypeEnum = WriteLastRowTypeEnum.COMMON_EMPTY;
        }
    }

    public WriteSheet getWriteSheet() {
        return this.writeSheet;
    }

    public void setWriteSheet(WriteSheet writeSheet) {
        this.writeSheet = writeSheet;
    }

    public Sheet getSheet() {
        return this.sheet;
    }

    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
    }

    public Integer getSheetNo() {
        return this.sheetNo;
    }

    public Sheet getCachedSheet() {
        return this.cachedSheet;
    }

    public void setCachedSheet(Sheet cachedSheet) {
        this.cachedSheet = cachedSheet;
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

    public WriteWorkbookHolder getParentWriteWorkbookHolder() {
        return this.parentWriteWorkbookHolder;
    }

    public void setParentWriteWorkbookHolder(WriteWorkbookHolder parentWriteWorkbookHolder) {
        this.parentWriteWorkbookHolder = parentWriteWorkbookHolder;
    }

    public Map<Integer, WriteTableHolder> getHasBeenInitializedTable() {
        return this.hasBeenInitializedTable;
    }

    public void setHasBeenInitializedTable(Map<Integer, WriteTableHolder> hasBeenInitializedTable) {
        this.hasBeenInitializedTable = hasBeenInitializedTable;
    }

    public WriteLastRowTypeEnum getWriteLastRowTypeEnum() {
        return this.writeLastRowTypeEnum;
    }

    public void setWriteLastRowTypeEnum(WriteLastRowTypeEnum writeLastRowTypeEnum) {
        this.writeLastRowTypeEnum = writeLastRowTypeEnum;
    }

    public int getNewRowIndexAndStartDoWrite() {
        int newRowIndex = 0;
        switch (this.writeLastRowTypeEnum) {
            case TEMPLATE_EMPTY:
                newRowIndex = Math.max(this.sheet.getLastRowNum(), this.cachedSheet.getLastRowNum());
                if (newRowIndex != 0 || this.cachedSheet.getRow(0) != null) {
                    newRowIndex++;
                    break;
                }
                break;
            case HAS_DATA:
                int newRowIndex2 = Math.max(this.sheet.getLastRowNum(), this.cachedSheet.getLastRowNum());
                newRowIndex = newRowIndex2 + 1;
                break;
        }
        this.writeLastRowTypeEnum = WriteLastRowTypeEnum.HAS_DATA;
        return newRowIndex;
    }

    @Override // com.alibaba.excel.metadata.Holder
    public HolderEnum holderType() {
        return HolderEnum.SHEET;
    }
}

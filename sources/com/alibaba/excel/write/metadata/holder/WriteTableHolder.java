package com.alibaba.excel.write.metadata.holder;

import com.alibaba.excel.enums.HolderEnum;
import com.alibaba.excel.write.metadata.WriteTable;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/write/metadata/holder/WriteTableHolder.class */
public class WriteTableHolder extends AbstractWriteHolder {
    private WriteSheetHolder parentWriteSheetHolder;
    private Integer tableNo;
    private WriteTable writeTable;

    public WriteTableHolder(WriteTable writeTable, WriteSheetHolder writeSheetHolder, WriteWorkbookHolder writeWorkbookHolder) {
        super(writeTable, writeSheetHolder, writeWorkbookHolder.getWriteWorkbook().getConvertAllFiled());
        this.parentWriteSheetHolder = writeSheetHolder;
        this.tableNo = writeTable.getTableNo();
        this.writeTable = writeTable;
    }

    public WriteSheetHolder getParentWriteSheetHolder() {
        return this.parentWriteSheetHolder;
    }

    public void setParentWriteSheetHolder(WriteSheetHolder parentWriteSheetHolder) {
        this.parentWriteSheetHolder = parentWriteSheetHolder;
    }

    public Integer getTableNo() {
        return this.tableNo;
    }

    public void setTableNo(Integer tableNo) {
        this.tableNo = tableNo;
    }

    public WriteTable getWriteTable() {
        return this.writeTable;
    }

    public void setWriteTable(WriteTable writeTable) {
        this.writeTable = writeTable;
    }

    @Override // com.alibaba.excel.metadata.Holder
    public HolderEnum holderType() {
        return HolderEnum.TABLE;
    }
}

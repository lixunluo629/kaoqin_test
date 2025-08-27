package com.alibaba.excel.write.style;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.util.StyleUtil;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/write/style/AbstractVerticalCellStyleStrategy.class */
public abstract class AbstractVerticalCellStyleStrategy extends AbstractCellStyleStrategy {
    private Workbook workbook;
    private Map<Integer, CellStyle> headCellStyleCache = new HashMap();
    private Map<Integer, CellStyle> contentCellStyleCache = new HashMap();

    protected abstract WriteCellStyle headCellStyle(Head head);

    protected abstract WriteCellStyle contentCellStyle(Head head);

    @Override // com.alibaba.excel.write.style.AbstractCellStyleStrategy
    protected void initCellStyle(Workbook workbook) {
        this.workbook = workbook;
    }

    @Override // com.alibaba.excel.write.style.AbstractCellStyleStrategy
    protected void setHeadCellStyle(Cell cell, Head head, Integer relativeRowIndex) {
        int columnIndex = head.getColumnIndex().intValue();
        if (this.headCellStyleCache.containsKey(Integer.valueOf(columnIndex))) {
            CellStyle cellStyle = this.headCellStyleCache.get(Integer.valueOf(columnIndex));
            if (cellStyle != null) {
                cell.setCellStyle(cellStyle);
                return;
            }
            return;
        }
        WriteCellStyle headCellStyle = headCellStyle(head);
        if (headCellStyle == null) {
            this.headCellStyleCache.put(Integer.valueOf(columnIndex), null);
            return;
        }
        CellStyle cellStyle2 = StyleUtil.buildHeadCellStyle(this.workbook, headCellStyle);
        this.headCellStyleCache.put(Integer.valueOf(columnIndex), cellStyle2);
        cell.setCellStyle(cellStyle2);
    }

    @Override // com.alibaba.excel.write.style.AbstractCellStyleStrategy
    protected void setContentCellStyle(Cell cell, Head head, Integer relativeRowIndex) {
        int columnIndex = head.getColumnIndex().intValue();
        if (this.contentCellStyleCache.containsKey(Integer.valueOf(columnIndex))) {
            CellStyle cellStyle = this.contentCellStyleCache.get(Integer.valueOf(columnIndex));
            if (cellStyle != null) {
                cell.setCellStyle(cellStyle);
                return;
            }
            return;
        }
        WriteCellStyle contentCellStyle = contentCellStyle(head);
        if (contentCellStyle == null) {
            this.contentCellStyleCache.put(Integer.valueOf(columnIndex), null);
            return;
        }
        CellStyle cellStyle2 = StyleUtil.buildContentCellStyle(this.workbook, contentCellStyle);
        this.contentCellStyleCache.put(Integer.valueOf(columnIndex), cellStyle2);
        cell.setCellStyle(cellStyle2);
    }
}

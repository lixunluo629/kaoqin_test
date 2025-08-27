package com.alibaba.excel.write.style;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.util.StyleUtil;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/write/style/HorizontalCellStyleStrategy.class */
public class HorizontalCellStyleStrategy extends AbstractCellStyleStrategy {
    private WriteCellStyle headWriteCellStyle;
    private List<WriteCellStyle> contentWriteCellStyleList;
    private CellStyle headCellStyle;
    private List<CellStyle> contentCellStyleList;

    public HorizontalCellStyleStrategy(WriteCellStyle headWriteCellStyle, List<WriteCellStyle> contentWriteCellStyleList) {
        this.headWriteCellStyle = headWriteCellStyle;
        this.contentWriteCellStyleList = contentWriteCellStyleList;
    }

    public HorizontalCellStyleStrategy(WriteCellStyle headWriteCellStyle, WriteCellStyle contentWriteCellStyle) {
        this.headWriteCellStyle = headWriteCellStyle;
        this.contentWriteCellStyleList = new ArrayList();
        this.contentWriteCellStyleList.add(contentWriteCellStyle);
    }

    @Override // com.alibaba.excel.write.style.AbstractCellStyleStrategy
    protected void initCellStyle(Workbook workbook) {
        if (this.headWriteCellStyle != null) {
            this.headCellStyle = StyleUtil.buildHeadCellStyle(workbook, this.headWriteCellStyle);
        }
        if (this.contentWriteCellStyleList != null && !this.contentWriteCellStyleList.isEmpty()) {
            this.contentCellStyleList = new ArrayList();
            for (WriteCellStyle writeCellStyle : this.contentWriteCellStyleList) {
                this.contentCellStyleList.add(StyleUtil.buildContentCellStyle(workbook, writeCellStyle));
            }
        }
    }

    @Override // com.alibaba.excel.write.style.AbstractCellStyleStrategy
    protected void setHeadCellStyle(Cell cell, Head head, Integer relativeRowIndex) {
        if (this.headCellStyle == null) {
            return;
        }
        cell.setCellStyle(this.headCellStyle);
    }

    @Override // com.alibaba.excel.write.style.AbstractCellStyleStrategy
    protected void setContentCellStyle(Cell cell, Head head, Integer relativeRowIndex) {
        if (this.contentCellStyleList == null || this.contentCellStyleList.isEmpty()) {
            return;
        }
        cell.setCellStyle(this.contentCellStyleList.get(relativeRowIndex.intValue() % this.contentCellStyleList.size()));
    }
}

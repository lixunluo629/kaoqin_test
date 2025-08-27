package com.alibaba.excel.write.metadata.fill;

import com.alibaba.excel.enums.WriteTemplateAnalysisCellTypeEnum;
import java.util.List;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/write/metadata/fill/AnalysisCell.class */
public class AnalysisCell {
    private int columnIndex;
    private int rowIndex;
    private List<String> variableList;
    private List<String> prepareDataList;
    private Boolean onlyOneVariable;
    private WriteTemplateAnalysisCellTypeEnum cellType;
    private Boolean firstColumn;

    public int getColumnIndex() {
        return this.columnIndex;
    }

    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public int getRowIndex() {
        return this.rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public List<String> getVariableList() {
        return this.variableList;
    }

    public void setVariableList(List<String> variableList) {
        this.variableList = variableList;
    }

    public List<String> getPrepareDataList() {
        return this.prepareDataList;
    }

    public void setPrepareDataList(List<String> prepareDataList) {
        this.prepareDataList = prepareDataList;
    }

    public Boolean getOnlyOneVariable() {
        return this.onlyOneVariable;
    }

    public void setOnlyOneVariable(Boolean onlyOneVariable) {
        this.onlyOneVariable = onlyOneVariable;
    }

    public WriteTemplateAnalysisCellTypeEnum getCellType() {
        return this.cellType;
    }

    public void setCellType(WriteTemplateAnalysisCellTypeEnum cellType) {
        this.cellType = cellType;
    }

    public Boolean getFirstColumn() {
        return this.firstColumn;
    }

    public void setFirstColumn(Boolean firstColumn) {
        this.firstColumn = firstColumn;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AnalysisCell that = (AnalysisCell) o;
        return this.columnIndex == that.columnIndex && this.rowIndex == that.rowIndex;
    }

    public int hashCode() {
        int result = this.columnIndex;
        return (31 * result) + this.rowIndex;
    }
}

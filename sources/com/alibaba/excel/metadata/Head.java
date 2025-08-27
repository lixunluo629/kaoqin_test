package com.alibaba.excel.metadata;

import com.alibaba.excel.metadata.property.ColumnWidthProperty;
import java.util.ArrayList;
import java.util.List;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/metadata/Head.class */
public class Head {
    private Integer columnIndex;
    private String fieldName;
    private List<String> headNameList;
    private Boolean forceIndex;
    private Boolean forceName;
    private ColumnWidthProperty columnWidthProperty;

    public Head(Integer columnIndex, String fieldName, List<String> headNameList, Boolean forceIndex, Boolean forceName) {
        this.columnIndex = columnIndex;
        this.fieldName = fieldName;
        this.headNameList = headNameList == null ? new ArrayList() : headNameList;
        this.forceIndex = forceIndex;
        this.forceName = forceName;
    }

    public Integer getColumnIndex() {
        return this.columnIndex;
    }

    public void setColumnIndex(Integer columnIndex) {
        this.columnIndex = columnIndex;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public List<String> getHeadNameList() {
        return this.headNameList;
    }

    public void setHeadNameList(List<String> headNameList) {
        this.headNameList = headNameList;
    }

    public ColumnWidthProperty getColumnWidthProperty() {
        return this.columnWidthProperty;
    }

    public void setColumnWidthProperty(ColumnWidthProperty columnWidthProperty) {
        this.columnWidthProperty = columnWidthProperty;
    }

    public Boolean getForceIndex() {
        return this.forceIndex;
    }

    public void setForceIndex(Boolean forceIndex) {
        this.forceIndex = forceIndex;
    }

    public Boolean getForceName() {
        return this.forceName;
    }

    public void setForceName(Boolean forceName) {
        this.forceName = forceName;
    }
}

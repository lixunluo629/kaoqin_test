package com.alibaba.excel.metadata.property;

import com.alibaba.excel.annotation.write.style.ColumnWidth;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/metadata/property/ColumnWidthProperty.class */
public class ColumnWidthProperty {
    private Integer width;

    public ColumnWidthProperty(Integer width) {
        this.width = width;
    }

    public static ColumnWidthProperty build(ColumnWidth columnWidth) {
        if (columnWidth == null || columnWidth.value() < 0) {
            return null;
        }
        return new ColumnWidthProperty(Integer.valueOf(columnWidth.value()));
    }

    public Integer getWidth() {
        return this.width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }
}

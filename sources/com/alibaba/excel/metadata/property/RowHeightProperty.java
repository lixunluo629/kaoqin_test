package com.alibaba.excel.metadata.property;

import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/metadata/property/RowHeightProperty.class */
public class RowHeightProperty {
    private Short height;

    public RowHeightProperty(Short height) {
        this.height = height;
    }

    public static RowHeightProperty build(HeadRowHeight headRowHeight) {
        if (headRowHeight == null || headRowHeight.value() < 0) {
            return null;
        }
        return new RowHeightProperty(Short.valueOf(headRowHeight.value()));
    }

    public static RowHeightProperty build(ContentRowHeight contentRowHeight) {
        if (contentRowHeight == null || contentRowHeight.value() < 0) {
            return null;
        }
        return new RowHeightProperty(Short.valueOf(contentRowHeight.value()));
    }

    public Short getHeight() {
        return this.height;
    }

    public void setHeight(Short height) {
        this.height = height;
    }
}

package com.alibaba.excel.metadata.property;

import com.alibaba.excel.annotation.format.DateTimeFormat;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/metadata/property/DateTimeFormatProperty.class */
public class DateTimeFormatProperty {
    private String format;
    private Boolean use1904windowing;

    public DateTimeFormatProperty(String format, Boolean use1904windowing) {
        this.format = format;
        this.use1904windowing = use1904windowing;
    }

    public static DateTimeFormatProperty build(DateTimeFormat dateTimeFormat) {
        if (dateTimeFormat == null) {
            return null;
        }
        return new DateTimeFormatProperty(dateTimeFormat.value(), Boolean.valueOf(dateTimeFormat.use1904windowing()));
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Boolean getUse1904windowing() {
        return this.use1904windowing;
    }

    public void setUse1904windowing(Boolean use1904windowing) {
        this.use1904windowing = use1904windowing;
    }
}

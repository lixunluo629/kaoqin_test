package com.alibaba.excel.metadata.property;

import com.alibaba.excel.annotation.format.NumberFormat;
import java.math.RoundingMode;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/metadata/property/NumberFormatProperty.class */
public class NumberFormatProperty {
    private String format;
    private RoundingMode roundingMode;

    public NumberFormatProperty(String format, RoundingMode roundingMode) {
        this.format = format;
        this.roundingMode = roundingMode;
    }

    public static NumberFormatProperty build(NumberFormat numberFormat) {
        if (numberFormat == null) {
            return null;
        }
        return new NumberFormatProperty(numberFormat.value(), numberFormat.roundingMode());
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public RoundingMode getRoundingMode() {
        return this.roundingMode;
    }

    public void setRoundingMode(RoundingMode roundingMode) {
        this.roundingMode = roundingMode;
    }
}

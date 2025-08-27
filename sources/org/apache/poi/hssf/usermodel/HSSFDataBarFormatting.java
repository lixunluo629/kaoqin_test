package org.apache.poi.hssf.usermodel;

import org.apache.poi.hssf.record.CFRule12Record;
import org.apache.poi.hssf.record.cf.DataBarThreshold;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.ss.usermodel.DataBarFormatting;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/usermodel/HSSFDataBarFormatting.class */
public final class HSSFDataBarFormatting implements DataBarFormatting {
    private final HSSFSheet sheet;
    private final CFRule12Record cfRule12Record;
    private final org.apache.poi.hssf.record.cf.DataBarFormatting databarFormatting;

    protected HSSFDataBarFormatting(CFRule12Record cfRule12Record, HSSFSheet sheet) {
        this.sheet = sheet;
        this.cfRule12Record = cfRule12Record;
        this.databarFormatting = this.cfRule12Record.getDataBarFormatting();
    }

    @Override // org.apache.poi.ss.usermodel.DataBarFormatting
    public boolean isLeftToRight() {
        return !this.databarFormatting.isReversed();
    }

    @Override // org.apache.poi.ss.usermodel.DataBarFormatting
    public void setLeftToRight(boolean ltr) {
        this.databarFormatting.setReversed(!ltr);
    }

    @Override // org.apache.poi.ss.usermodel.DataBarFormatting
    public int getWidthMin() {
        return this.databarFormatting.getPercentMin();
    }

    @Override // org.apache.poi.ss.usermodel.DataBarFormatting
    public void setWidthMin(int width) {
        this.databarFormatting.setPercentMin((byte) width);
    }

    @Override // org.apache.poi.ss.usermodel.DataBarFormatting
    public int getWidthMax() {
        return this.databarFormatting.getPercentMax();
    }

    @Override // org.apache.poi.ss.usermodel.DataBarFormatting
    public void setWidthMax(int width) {
        this.databarFormatting.setPercentMax((byte) width);
    }

    @Override // org.apache.poi.ss.usermodel.DataBarFormatting
    public HSSFExtendedColor getColor() {
        return new HSSFExtendedColor(this.databarFormatting.getColor());
    }

    @Override // org.apache.poi.ss.usermodel.DataBarFormatting
    public void setColor(Color color) {
        HSSFExtendedColor hcolor = (HSSFExtendedColor) color;
        this.databarFormatting.setColor(hcolor.getExtendedColor());
    }

    @Override // org.apache.poi.ss.usermodel.DataBarFormatting
    public HSSFConditionalFormattingThreshold getMinThreshold() {
        return new HSSFConditionalFormattingThreshold(this.databarFormatting.getThresholdMin(), this.sheet);
    }

    @Override // org.apache.poi.ss.usermodel.DataBarFormatting
    public HSSFConditionalFormattingThreshold getMaxThreshold() {
        return new HSSFConditionalFormattingThreshold(this.databarFormatting.getThresholdMax(), this.sheet);
    }

    @Override // org.apache.poi.ss.usermodel.DataBarFormatting
    public boolean isIconOnly() {
        return this.databarFormatting.isIconOnly();
    }

    @Override // org.apache.poi.ss.usermodel.DataBarFormatting
    public void setIconOnly(boolean only) {
        this.databarFormatting.setIconOnly(only);
    }

    public HSSFConditionalFormattingThreshold createThreshold() {
        return new HSSFConditionalFormattingThreshold(new DataBarThreshold(), this.sheet);
    }
}

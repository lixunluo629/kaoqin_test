package org.apache.poi.hssf.record;

import org.apache.poi.util.BitField;
import org.apache.poi.util.BitFieldFactory;
import org.apache.poi.util.LittleEndianOutput;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/PrintSetupRecord.class */
public final class PrintSetupRecord extends StandardRecord {
    public static final short sid = 161;
    private short field_1_paper_size;
    private short field_2_scale;
    private short field_3_page_start;
    private short field_4_fit_width;
    private short field_5_fit_height;
    private short field_6_options;
    private static final BitField lefttoright = BitFieldFactory.getInstance(1);
    private static final BitField landscape = BitFieldFactory.getInstance(2);
    private static final BitField validsettings = BitFieldFactory.getInstance(4);
    private static final BitField nocolor = BitFieldFactory.getInstance(8);
    private static final BitField draft = BitFieldFactory.getInstance(16);
    private static final BitField notes = BitFieldFactory.getInstance(32);
    private static final BitField noOrientation = BitFieldFactory.getInstance(64);
    private static final BitField usepage = BitFieldFactory.getInstance(128);
    private short field_7_hresolution;
    private short field_8_vresolution;
    private double field_9_headermargin;
    private double field_10_footermargin;
    private short field_11_copies;

    public PrintSetupRecord() {
    }

    public PrintSetupRecord(RecordInputStream in) {
        this.field_1_paper_size = in.readShort();
        this.field_2_scale = in.readShort();
        this.field_3_page_start = in.readShort();
        this.field_4_fit_width = in.readShort();
        this.field_5_fit_height = in.readShort();
        this.field_6_options = in.readShort();
        this.field_7_hresolution = in.readShort();
        this.field_8_vresolution = in.readShort();
        this.field_9_headermargin = in.readDouble();
        this.field_10_footermargin = in.readDouble();
        this.field_11_copies = in.readShort();
    }

    public void setPaperSize(short size) {
        this.field_1_paper_size = size;
    }

    public void setScale(short scale) {
        this.field_2_scale = scale;
    }

    public void setPageStart(short start) {
        this.field_3_page_start = start;
    }

    public void setFitWidth(short width) {
        this.field_4_fit_width = width;
    }

    public void setFitHeight(short height) {
        this.field_5_fit_height = height;
    }

    public void setOptions(short options) {
        this.field_6_options = options;
    }

    public void setLeftToRight(boolean ltor) {
        this.field_6_options = lefttoright.setShortBoolean(this.field_6_options, ltor);
    }

    public void setLandscape(boolean ls) {
        this.field_6_options = landscape.setShortBoolean(this.field_6_options, ls);
    }

    public void setValidSettings(boolean valid) {
        this.field_6_options = validsettings.setShortBoolean(this.field_6_options, valid);
    }

    public void setNoColor(boolean mono) {
        this.field_6_options = nocolor.setShortBoolean(this.field_6_options, mono);
    }

    public void setDraft(boolean d) {
        this.field_6_options = draft.setShortBoolean(this.field_6_options, d);
    }

    public void setNotes(boolean printnotes) {
        this.field_6_options = notes.setShortBoolean(this.field_6_options, printnotes);
    }

    public void setNoOrientation(boolean orientation) {
        this.field_6_options = noOrientation.setShortBoolean(this.field_6_options, orientation);
    }

    public void setUsePage(boolean page) {
        this.field_6_options = usepage.setShortBoolean(this.field_6_options, page);
    }

    public void setHResolution(short resolution) {
        this.field_7_hresolution = resolution;
    }

    public void setVResolution(short resolution) {
        this.field_8_vresolution = resolution;
    }

    public void setHeaderMargin(double headermargin) {
        this.field_9_headermargin = headermargin;
    }

    public void setFooterMargin(double footermargin) {
        this.field_10_footermargin = footermargin;
    }

    public void setCopies(short copies) {
        this.field_11_copies = copies;
    }

    public short getPaperSize() {
        return this.field_1_paper_size;
    }

    public short getScale() {
        return this.field_2_scale;
    }

    public short getPageStart() {
        return this.field_3_page_start;
    }

    public short getFitWidth() {
        return this.field_4_fit_width;
    }

    public short getFitHeight() {
        return this.field_5_fit_height;
    }

    public short getOptions() {
        return this.field_6_options;
    }

    public boolean getLeftToRight() {
        return lefttoright.isSet(this.field_6_options);
    }

    public boolean getLandscape() {
        return landscape.isSet(this.field_6_options);
    }

    public boolean getValidSettings() {
        return validsettings.isSet(this.field_6_options);
    }

    public boolean getNoColor() {
        return nocolor.isSet(this.field_6_options);
    }

    public boolean getDraft() {
        return draft.isSet(this.field_6_options);
    }

    public boolean getNotes() {
        return notes.isSet(this.field_6_options);
    }

    public boolean getNoOrientation() {
        return noOrientation.isSet(this.field_6_options);
    }

    public boolean getUsePage() {
        return usepage.isSet(this.field_6_options);
    }

    public short getHResolution() {
        return this.field_7_hresolution;
    }

    public short getVResolution() {
        return this.field_8_vresolution;
    }

    public double getHeaderMargin() {
        return this.field_9_headermargin;
    }

    public double getFooterMargin() {
        return this.field_10_footermargin;
    }

    public short getCopies() {
        return this.field_11_copies;
    }

    @Override // org.apache.poi.hssf.record.Record
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[PRINTSETUP]\n");
        buffer.append("    .papersize      = ").append((int) getPaperSize()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("    .scale          = ").append((int) getScale()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("    .pagestart      = ").append((int) getPageStart()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("    .fitwidth       = ").append((int) getFitWidth()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("    .fitheight      = ").append((int) getFitHeight()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("    .options        = ").append((int) getOptions()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("        .ltor       = ").append(getLeftToRight()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("        .landscape  = ").append(getLandscape()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("        .valid      = ").append(getValidSettings()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("        .mono       = ").append(getNoColor()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("        .draft      = ").append(getDraft()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("        .notes      = ").append(getNotes()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("        .noOrientat = ").append(getNoOrientation()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("        .usepage    = ").append(getUsePage()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("    .hresolution    = ").append((int) getHResolution()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("    .vresolution    = ").append((int) getVResolution()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("    .headermargin   = ").append(getHeaderMargin()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("    .footermargin   = ").append(getFooterMargin()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("    .copies         = ").append((int) getCopies()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("[/PRINTSETUP]\n");
        return buffer.toString();
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    public void serialize(LittleEndianOutput out) {
        out.writeShort(getPaperSize());
        out.writeShort(getScale());
        out.writeShort(getPageStart());
        out.writeShort(getFitWidth());
        out.writeShort(getFitHeight());
        out.writeShort(getOptions());
        out.writeShort(getHResolution());
        out.writeShort(getVResolution());
        out.writeDouble(getHeaderMargin());
        out.writeDouble(getFooterMargin());
        out.writeShort(getCopies());
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    protected int getDataSize() {
        return 34;
    }

    @Override // org.apache.poi.hssf.record.Record
    public short getSid() {
        return (short) 161;
    }

    @Override // org.apache.poi.hssf.record.Record
    public Object clone() {
        PrintSetupRecord rec = new PrintSetupRecord();
        rec.field_1_paper_size = this.field_1_paper_size;
        rec.field_2_scale = this.field_2_scale;
        rec.field_3_page_start = this.field_3_page_start;
        rec.field_4_fit_width = this.field_4_fit_width;
        rec.field_5_fit_height = this.field_5_fit_height;
        rec.field_6_options = this.field_6_options;
        rec.field_7_hresolution = this.field_7_hresolution;
        rec.field_8_vresolution = this.field_8_vresolution;
        rec.field_9_headermargin = this.field_9_headermargin;
        rec.field_10_footermargin = this.field_10_footermargin;
        rec.field_11_copies = this.field_11_copies;
        return rec;
    }
}

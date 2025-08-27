package org.apache.poi.hssf.usermodel;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.hssf.record.HyperlinkRecord;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.util.Internal;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/usermodel/HSSFHyperlink.class */
public class HSSFHyperlink implements Hyperlink {
    protected final HyperlinkRecord record;
    protected final HyperlinkType link_type;

    @Internal(since = "3.15 beta 3")
    protected HSSFHyperlink(HyperlinkType type) {
        this.link_type = type;
        this.record = new HyperlinkRecord();
        switch (type) {
            case URL:
            case EMAIL:
                this.record.newUrlLink();
                return;
            case FILE:
                this.record.newFileLink();
                return;
            case DOCUMENT:
                this.record.newDocumentLink();
                return;
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }
    }

    protected HSSFHyperlink(HyperlinkRecord record) {
        this.record = record;
        this.link_type = getType(record);
    }

    private static HyperlinkType getType(HyperlinkRecord record) {
        HyperlinkType link_type;
        if (record.isFileLink()) {
            link_type = HyperlinkType.FILE;
        } else if (record.isDocumentLink()) {
            link_type = HyperlinkType.DOCUMENT;
        } else if (record.getAddress() != null && record.getAddress().startsWith("mailto:")) {
            link_type = HyperlinkType.EMAIL;
        } else {
            link_type = HyperlinkType.URL;
        }
        return link_type;
    }

    protected HSSFHyperlink(Hyperlink other) {
        if (other instanceof HSSFHyperlink) {
            HSSFHyperlink hlink = (HSSFHyperlink) other;
            this.record = hlink.record.clone();
            this.link_type = getType(this.record);
        } else {
            this.link_type = other.getTypeEnum();
            this.record = new HyperlinkRecord();
            setFirstRow(other.getFirstRow());
            setFirstColumn(other.getFirstColumn());
            setLastRow(other.getLastRow());
            setLastColumn(other.getLastColumn());
        }
    }

    @Override // org.apache.poi.ss.usermodel.Hyperlink
    public int getFirstRow() {
        return this.record.getFirstRow();
    }

    @Override // org.apache.poi.ss.usermodel.Hyperlink
    public void setFirstRow(int row) {
        this.record.setFirstRow(row);
    }

    @Override // org.apache.poi.ss.usermodel.Hyperlink
    public int getLastRow() {
        return this.record.getLastRow();
    }

    @Override // org.apache.poi.ss.usermodel.Hyperlink
    public void setLastRow(int row) {
        this.record.setLastRow(row);
    }

    @Override // org.apache.poi.ss.usermodel.Hyperlink
    public int getFirstColumn() {
        return this.record.getFirstColumn();
    }

    @Override // org.apache.poi.ss.usermodel.Hyperlink
    public void setFirstColumn(int col) {
        this.record.setFirstColumn((short) col);
    }

    @Override // org.apache.poi.ss.usermodel.Hyperlink
    public int getLastColumn() {
        return this.record.getLastColumn();
    }

    @Override // org.apache.poi.ss.usermodel.Hyperlink
    public void setLastColumn(int col) {
        this.record.setLastColumn((short) col);
    }

    @Override // org.apache.poi.common.usermodel.Hyperlink
    public String getAddress() {
        return this.record.getAddress();
    }

    public String getTextMark() {
        return this.record.getTextMark();
    }

    public void setTextMark(String textMark) {
        this.record.setTextMark(textMark);
    }

    public String getShortFilename() {
        return this.record.getShortFilename();
    }

    public void setShortFilename(String shortFilename) {
        this.record.setShortFilename(shortFilename);
    }

    @Override // org.apache.poi.common.usermodel.Hyperlink
    public void setAddress(String address) {
        this.record.setAddress(address);
    }

    @Override // org.apache.poi.common.usermodel.Hyperlink
    public String getLabel() {
        return this.record.getLabel();
    }

    @Override // org.apache.poi.common.usermodel.Hyperlink
    public void setLabel(String label) {
        this.record.setLabel(label);
    }

    @Override // org.apache.poi.common.usermodel.Hyperlink
    public int getType() {
        return this.link_type.getCode();
    }

    @Override // org.apache.poi.common.usermodel.Hyperlink
    public HyperlinkType getTypeEnum() {
        return this.link_type;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof HSSFHyperlink)) {
            return false;
        }
        HSSFHyperlink otherLink = (HSSFHyperlink) other;
        return this.record == otherLink.record;
    }

    public int hashCode() {
        return this.record.hashCode();
    }
}

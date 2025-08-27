package org.apache.poi.xssf.binary;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.Internal;

@Internal
/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/binary/XSSFHyperlinkRecord.class */
public class XSSFHyperlinkRecord {
    private final CellRangeAddress cellRangeAddress;
    private final String relId;
    private String location;
    private String toolTip;
    private String display;

    XSSFHyperlinkRecord(CellRangeAddress cellRangeAddress, String relId, String location, String toolTip, String display) {
        this.cellRangeAddress = cellRangeAddress;
        this.relId = relId;
        this.location = location;
        this.toolTip = toolTip;
        this.display = display;
    }

    void setLocation(String location) {
        this.location = location;
    }

    void setToolTip(String toolTip) {
        this.toolTip = toolTip;
    }

    void setDisplay(String display) {
        this.display = display;
    }

    CellRangeAddress getCellRangeAddress() {
        return this.cellRangeAddress;
    }

    public String getRelId() {
        return this.relId;
    }

    public String getLocation() {
        return this.location;
    }

    public String getToolTip() {
        return this.toolTip;
    }

    public String getDisplay() {
        return this.display;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        XSSFHyperlinkRecord that = (XSSFHyperlinkRecord) o;
        if (this.cellRangeAddress != null) {
            if (!this.cellRangeAddress.equals(that.cellRangeAddress)) {
                return false;
            }
        } else if (that.cellRangeAddress != null) {
            return false;
        }
        if (this.relId != null) {
            if (!this.relId.equals(that.relId)) {
                return false;
            }
        } else if (that.relId != null) {
            return false;
        }
        if (this.location != null) {
            if (!this.location.equals(that.location)) {
                return false;
            }
        } else if (that.location != null) {
            return false;
        }
        if (this.toolTip != null) {
            if (!this.toolTip.equals(that.toolTip)) {
                return false;
            }
        } else if (that.toolTip != null) {
            return false;
        }
        return this.display != null ? this.display.equals(that.display) : that.display == null;
    }

    public int hashCode() {
        int result = this.cellRangeAddress != null ? this.cellRangeAddress.hashCode() : 0;
        return (31 * ((31 * ((31 * ((31 * result) + (this.relId != null ? this.relId.hashCode() : 0))) + (this.location != null ? this.location.hashCode() : 0))) + (this.toolTip != null ? this.toolTip.hashCode() : 0))) + (this.display != null ? this.display.hashCode() : 0);
    }

    public String toString() {
        return "XSSFHyperlinkRecord{cellRangeAddress=" + this.cellRangeAddress + ", relId='" + this.relId + "', location='" + this.location + "', toolTip='" + this.toolTip + "', display='" + this.display + "'}";
    }
}

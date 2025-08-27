package org.apache.poi.ddf;

/* loaded from: poi-3.17.jar:org/apache/poi/ddf/EscherTertiaryOptRecord.class */
public class EscherTertiaryOptRecord extends AbstractEscherOptRecord {
    public static final short RECORD_ID = -3806;

    @Override // org.apache.poi.ddf.EscherRecord
    public String getRecordName() {
        return "TertiaryOpt";
    }
}

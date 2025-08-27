package org.apache.poi.hssf.record;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/RecordBase.class */
public abstract class RecordBase {
    public abstract int serialize(int i, byte[] bArr);

    public abstract int getRecordSize();
}

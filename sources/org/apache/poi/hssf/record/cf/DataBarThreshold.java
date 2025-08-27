package org.apache.poi.hssf.record.cf;

import org.apache.poi.util.LittleEndianInput;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/cf/DataBarThreshold.class */
public final class DataBarThreshold extends Threshold implements Cloneable {
    public DataBarThreshold() {
    }

    public DataBarThreshold(LittleEndianInput in) {
        super(in);
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public DataBarThreshold m3389clone() {
        DataBarThreshold rec = new DataBarThreshold();
        super.copyTo(rec);
        return rec;
    }
}

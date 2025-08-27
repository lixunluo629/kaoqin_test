package org.apache.poi.hssf.record.cf;

import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/cf/IconMultiStateThreshold.class */
public final class IconMultiStateThreshold extends Threshold implements Cloneable {
    public static final byte EQUALS_EXCLUDE = 0;
    public static final byte EQUALS_INCLUDE = 1;
    private byte equals;

    public IconMultiStateThreshold() {
        this.equals = (byte) 1;
    }

    public IconMultiStateThreshold(LittleEndianInput in) {
        super(in);
        this.equals = in.readByte();
        in.readInt();
    }

    public byte getEquals() {
        return this.equals;
    }

    public void setEquals(byte equals) {
        this.equals = equals;
    }

    @Override // org.apache.poi.hssf.record.cf.Threshold
    public int getDataLength() {
        return super.getDataLength() + 5;
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public IconMultiStateThreshold m3393clone() {
        IconMultiStateThreshold rec = new IconMultiStateThreshold();
        super.copyTo(rec);
        rec.equals = this.equals;
        return rec;
    }

    @Override // org.apache.poi.hssf.record.cf.Threshold
    public void serialize(LittleEndianOutput out) {
        super.serialize(out);
        out.writeByte(this.equals);
        out.writeInt(0);
    }
}

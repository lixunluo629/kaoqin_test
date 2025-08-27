package org.apache.poi.ss.formula.ptg;

import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/ptg/MemAreaPtg.class */
public final class MemAreaPtg extends OperandPtg {
    public static final short sid = 38;
    private static final int SIZE = 7;
    private final int field_1_reserved;
    private final int field_2_subex_len;

    public MemAreaPtg(int subexLen) {
        this.field_1_reserved = 0;
        this.field_2_subex_len = subexLen;
    }

    public MemAreaPtg(LittleEndianInput in) {
        this.field_1_reserved = in.readInt();
        this.field_2_subex_len = in.readShort();
    }

    public int getLenRefSubexpression() {
        return this.field_2_subex_len;
    }

    @Override // org.apache.poi.ss.formula.ptg.Ptg
    public void write(LittleEndianOutput out) {
        out.writeByte(38 + getPtgClass());
        out.writeInt(this.field_1_reserved);
        out.writeShort(this.field_2_subex_len);
    }

    @Override // org.apache.poi.ss.formula.ptg.Ptg
    public int getSize() {
        return 7;
    }

    @Override // org.apache.poi.ss.formula.ptg.Ptg
    public String toFormulaString() {
        return "";
    }

    @Override // org.apache.poi.ss.formula.ptg.Ptg
    public byte getDefaultOperandClass() {
        return (byte) 32;
    }

    @Override // org.apache.poi.ss.formula.ptg.Ptg
    public final String toString() {
        StringBuffer sb = new StringBuffer(64);
        sb.append(getClass().getName()).append(" [len=");
        sb.append(this.field_2_subex_len);
        sb.append("]");
        return sb.toString();
    }
}

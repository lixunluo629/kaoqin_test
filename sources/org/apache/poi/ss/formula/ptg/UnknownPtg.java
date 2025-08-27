package org.apache.poi.ss.formula.ptg;

import org.apache.poi.util.LittleEndianOutput;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/ptg/UnknownPtg.class */
public class UnknownPtg extends Ptg {
    private short size = 1;
    private final int _sid;

    public UnknownPtg(int sid) {
        this._sid = sid;
    }

    @Override // org.apache.poi.ss.formula.ptg.Ptg
    public boolean isBaseToken() {
        return true;
    }

    @Override // org.apache.poi.ss.formula.ptg.Ptg
    public void write(LittleEndianOutput out) {
        out.writeByte(this._sid);
    }

    @Override // org.apache.poi.ss.formula.ptg.Ptg
    public int getSize() {
        return this.size;
    }

    @Override // org.apache.poi.ss.formula.ptg.Ptg
    public String toFormulaString() {
        return "UNKNOWN";
    }

    @Override // org.apache.poi.ss.formula.ptg.Ptg
    public byte getDefaultOperandClass() {
        return (byte) 32;
    }
}

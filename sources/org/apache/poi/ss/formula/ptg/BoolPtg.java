package org.apache.poi.ss.formula.ptg;

import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/ptg/BoolPtg.class */
public final class BoolPtg extends ScalarConstantPtg {
    public static final int SIZE = 2;
    public static final byte sid = 29;
    private static final BoolPtg FALSE = new BoolPtg(false);
    private static final BoolPtg TRUE = new BoolPtg(true);
    private final boolean _value;

    private BoolPtg(boolean b) {
        this._value = b;
    }

    public static BoolPtg valueOf(boolean b) {
        return b ? TRUE : FALSE;
    }

    public static BoolPtg read(LittleEndianInput in) {
        return valueOf(in.readByte() == 1);
    }

    public boolean getValue() {
        return this._value;
    }

    @Override // org.apache.poi.ss.formula.ptg.Ptg
    public void write(LittleEndianOutput out) {
        out.writeByte(29 + getPtgClass());
        out.writeByte(this._value ? 1 : 0);
    }

    @Override // org.apache.poi.ss.formula.ptg.Ptg
    public int getSize() {
        return 2;
    }

    @Override // org.apache.poi.ss.formula.ptg.Ptg
    public String toFormulaString() {
        return this._value ? "TRUE" : "FALSE";
    }
}

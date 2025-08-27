package org.apache.poi.ss.formula.ptg;

import org.apache.poi.util.LittleEndianOutput;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/ptg/ParenthesisPtg.class */
public final class ParenthesisPtg extends ControlPtg {
    private static final int SIZE = 1;
    public static final byte sid = 21;
    public static final ControlPtg instance = new ParenthesisPtg();

    private ParenthesisPtg() {
    }

    @Override // org.apache.poi.ss.formula.ptg.Ptg
    public void write(LittleEndianOutput out) {
        out.writeByte(21 + getPtgClass());
    }

    @Override // org.apache.poi.ss.formula.ptg.Ptg
    public int getSize() {
        return 1;
    }

    @Override // org.apache.poi.ss.formula.ptg.Ptg
    public String toFormulaString() {
        return "()";
    }

    public String toFormulaString(String[] operands) {
        return "(" + operands[0] + ")";
    }
}

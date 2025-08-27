package org.apache.poi.ss.formula.ptg;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import org.apache.poi.util.LittleEndianOutput;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/ptg/MissingArgPtg.class */
public final class MissingArgPtg extends ScalarConstantPtg {
    private static final int SIZE = 1;
    public static final byte sid = 22;
    public static final Ptg instance = new MissingArgPtg();

    private MissingArgPtg() {
    }

    @Override // org.apache.poi.ss.formula.ptg.Ptg
    public void write(LittleEndianOutput out) {
        out.writeByte(22 + getPtgClass());
    }

    @Override // org.apache.poi.ss.formula.ptg.Ptg
    public int getSize() {
        return 1;
    }

    @Override // org.apache.poi.ss.formula.ptg.Ptg
    public String toFormulaString() {
        return SymbolConstants.SPACE_SYMBOL;
    }
}

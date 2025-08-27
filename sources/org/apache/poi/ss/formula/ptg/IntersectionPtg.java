package org.apache.poi.ss.formula.ptg;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import org.apache.poi.util.LittleEndianOutput;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/ptg/IntersectionPtg.class */
public final class IntersectionPtg extends OperationPtg {
    public static final byte sid = 15;
    public static final OperationPtg instance = new IntersectionPtg();

    private IntersectionPtg() {
    }

    @Override // org.apache.poi.ss.formula.ptg.Ptg
    public final boolean isBaseToken() {
        return true;
    }

    @Override // org.apache.poi.ss.formula.ptg.Ptg
    public int getSize() {
        return 1;
    }

    @Override // org.apache.poi.ss.formula.ptg.Ptg
    public void write(LittleEndianOutput out) {
        out.writeByte(15 + getPtgClass());
    }

    @Override // org.apache.poi.ss.formula.ptg.Ptg
    public String toFormulaString() {
        return SymbolConstants.SPACE_SYMBOL;
    }

    @Override // org.apache.poi.ss.formula.ptg.OperationPtg
    public String toFormulaString(String[] operands) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(operands[0]);
        buffer.append(SymbolConstants.SPACE_SYMBOL);
        buffer.append(operands[1]);
        return buffer.toString();
    }

    @Override // org.apache.poi.ss.formula.ptg.OperationPtg
    public int getNumberOfOperands() {
        return 2;
    }
}

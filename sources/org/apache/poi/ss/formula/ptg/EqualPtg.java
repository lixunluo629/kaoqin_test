package org.apache.poi.ss.formula.ptg;

import com.moredian.onpremise.core.common.constants.SymbolConstants;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/ptg/EqualPtg.class */
public final class EqualPtg extends ValueOperatorPtg {
    public static final byte sid = 11;
    public static final ValueOperatorPtg instance = new EqualPtg();

    private EqualPtg() {
    }

    @Override // org.apache.poi.ss.formula.ptg.ValueOperatorPtg
    protected byte getSid() {
        return (byte) 11;
    }

    @Override // org.apache.poi.ss.formula.ptg.OperationPtg
    public int getNumberOfOperands() {
        return 2;
    }

    @Override // org.apache.poi.ss.formula.ptg.OperationPtg
    public String toFormulaString(String[] operands) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(operands[0]);
        buffer.append(SymbolConstants.EQUAL_SYMBOL);
        buffer.append(operands[1]);
        return buffer.toString();
    }
}

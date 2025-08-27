package org.apache.poi.ss.formula.ptg;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/ptg/LessEqualPtg.class */
public final class LessEqualPtg extends ValueOperatorPtg {
    public static final byte sid = 10;
    public static final ValueOperatorPtg instance = new LessEqualPtg();

    private LessEqualPtg() {
    }

    @Override // org.apache.poi.ss.formula.ptg.ValueOperatorPtg
    protected byte getSid() {
        return (byte) 10;
    }

    @Override // org.apache.poi.ss.formula.ptg.OperationPtg
    public int getNumberOfOperands() {
        return 2;
    }

    @Override // org.apache.poi.ss.formula.ptg.OperationPtg
    public String toFormulaString(String[] operands) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(operands[0]);
        buffer.append("<=");
        buffer.append(operands[1]);
        return buffer.toString();
    }
}

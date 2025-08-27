package org.apache.poi.ss.formula.ptg;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/ptg/NotEqualPtg.class */
public final class NotEqualPtg extends ValueOperatorPtg {
    public static final byte sid = 14;
    public static final ValueOperatorPtg instance = new NotEqualPtg();

    private NotEqualPtg() {
    }

    @Override // org.apache.poi.ss.formula.ptg.ValueOperatorPtg
    protected byte getSid() {
        return (byte) 14;
    }

    @Override // org.apache.poi.ss.formula.ptg.OperationPtg
    public int getNumberOfOperands() {
        return 2;
    }

    @Override // org.apache.poi.ss.formula.ptg.OperationPtg
    public String toFormulaString(String[] operands) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(operands[0]);
        buffer.append("<>");
        buffer.append(operands[1]);
        return buffer.toString();
    }
}

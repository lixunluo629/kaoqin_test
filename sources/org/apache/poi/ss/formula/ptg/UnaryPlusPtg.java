package org.apache.poi.ss.formula.ptg;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/ptg/UnaryPlusPtg.class */
public final class UnaryPlusPtg extends ValueOperatorPtg {
    public static final byte sid = 18;
    private static final String ADD = "+";
    public static final ValueOperatorPtg instance = new UnaryPlusPtg();

    private UnaryPlusPtg() {
    }

    @Override // org.apache.poi.ss.formula.ptg.ValueOperatorPtg
    protected byte getSid() {
        return (byte) 18;
    }

    @Override // org.apache.poi.ss.formula.ptg.OperationPtg
    public int getNumberOfOperands() {
        return 1;
    }

    @Override // org.apache.poi.ss.formula.ptg.OperationPtg
    public String toFormulaString(String[] operands) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("+");
        buffer.append(operands[0]);
        return buffer.toString();
    }
}

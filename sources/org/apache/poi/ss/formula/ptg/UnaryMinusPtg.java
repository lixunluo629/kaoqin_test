package org.apache.poi.ss.formula.ptg;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/ptg/UnaryMinusPtg.class */
public final class UnaryMinusPtg extends ValueOperatorPtg {
    public static final byte sid = 19;
    private static final String MINUS = "-";
    public static final ValueOperatorPtg instance = new UnaryMinusPtg();

    private UnaryMinusPtg() {
    }

    @Override // org.apache.poi.ss.formula.ptg.ValueOperatorPtg
    protected byte getSid() {
        return (byte) 19;
    }

    @Override // org.apache.poi.ss.formula.ptg.OperationPtg
    public int getNumberOfOperands() {
        return 1;
    }

    @Override // org.apache.poi.ss.formula.ptg.OperationPtg
    public String toFormulaString(String[] operands) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("-");
        buffer.append(operands[0]);
        return buffer.toString();
    }
}

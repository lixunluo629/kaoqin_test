package org.apache.poi.ss.formula.ptg;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/ptg/GreaterThanPtg.class */
public final class GreaterThanPtg extends ValueOperatorPtg {
    public static final byte sid = 13;
    private static final String GREATERTHAN = ">";
    public static final ValueOperatorPtg instance = new GreaterThanPtg();

    private GreaterThanPtg() {
    }

    @Override // org.apache.poi.ss.formula.ptg.ValueOperatorPtg
    protected byte getSid() {
        return (byte) 13;
    }

    @Override // org.apache.poi.ss.formula.ptg.OperationPtg
    public int getNumberOfOperands() {
        return 2;
    }

    @Override // org.apache.poi.ss.formula.ptg.OperationPtg
    public String toFormulaString(String[] operands) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(operands[0]);
        buffer.append(GREATERTHAN);
        buffer.append(operands[1]);
        return buffer.toString();
    }
}

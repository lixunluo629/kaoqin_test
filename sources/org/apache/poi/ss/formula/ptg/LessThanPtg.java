package org.apache.poi.ss.formula.ptg;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/ptg/LessThanPtg.class */
public final class LessThanPtg extends ValueOperatorPtg {
    public static final byte sid = 9;
    private static final String LESSTHAN = "<";
    public static final ValueOperatorPtg instance = new LessThanPtg();

    private LessThanPtg() {
    }

    @Override // org.apache.poi.ss.formula.ptg.ValueOperatorPtg
    protected byte getSid() {
        return (byte) 9;
    }

    @Override // org.apache.poi.ss.formula.ptg.OperationPtg
    public int getNumberOfOperands() {
        return 2;
    }

    @Override // org.apache.poi.ss.formula.ptg.OperationPtg
    public String toFormulaString(String[] operands) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(operands[0]);
        buffer.append(LESSTHAN);
        buffer.append(operands[1]);
        return buffer.toString();
    }
}

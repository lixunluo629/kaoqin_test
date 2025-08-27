package org.apache.poi.ss.formula.ptg;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/ptg/DividePtg.class */
public final class DividePtg extends ValueOperatorPtg {
    public static final byte sid = 6;
    public static final ValueOperatorPtg instance = new DividePtg();

    private DividePtg() {
    }

    @Override // org.apache.poi.ss.formula.ptg.ValueOperatorPtg
    protected byte getSid() {
        return (byte) 6;
    }

    @Override // org.apache.poi.ss.formula.ptg.OperationPtg
    public int getNumberOfOperands() {
        return 2;
    }

    @Override // org.apache.poi.ss.formula.ptg.OperationPtg
    public String toFormulaString(String[] operands) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(operands[0]);
        buffer.append("/");
        buffer.append(operands[1]);
        return buffer.toString();
    }
}

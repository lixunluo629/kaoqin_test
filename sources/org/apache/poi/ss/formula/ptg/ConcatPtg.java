package org.apache.poi.ss.formula.ptg;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/ptg/ConcatPtg.class */
public final class ConcatPtg extends ValueOperatorPtg {
    public static final byte sid = 8;
    private static final String CONCAT = "&";
    public static final ValueOperatorPtg instance = new ConcatPtg();

    private ConcatPtg() {
    }

    @Override // org.apache.poi.ss.formula.ptg.ValueOperatorPtg
    protected byte getSid() {
        return (byte) 8;
    }

    @Override // org.apache.poi.ss.formula.ptg.OperationPtg
    public int getNumberOfOperands() {
        return 2;
    }

    @Override // org.apache.poi.ss.formula.ptg.OperationPtg
    public String toFormulaString(String[] operands) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(operands[0]);
        buffer.append("&");
        buffer.append(operands[1]);
        return buffer.toString();
    }
}

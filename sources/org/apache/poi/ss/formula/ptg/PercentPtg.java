package org.apache.poi.ss.formula.ptg;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/ptg/PercentPtg.class */
public final class PercentPtg extends ValueOperatorPtg {
    public static final int SIZE = 1;
    public static final byte sid = 20;
    private static final String PERCENT = "%";
    public static final ValueOperatorPtg instance = new PercentPtg();

    private PercentPtg() {
    }

    @Override // org.apache.poi.ss.formula.ptg.ValueOperatorPtg
    protected byte getSid() {
        return (byte) 20;
    }

    @Override // org.apache.poi.ss.formula.ptg.OperationPtg
    public int getNumberOfOperands() {
        return 1;
    }

    @Override // org.apache.poi.ss.formula.ptg.OperationPtg
    public String toFormulaString(String[] operands) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(operands[0]);
        buffer.append("%");
        return buffer.toString();
    }
}

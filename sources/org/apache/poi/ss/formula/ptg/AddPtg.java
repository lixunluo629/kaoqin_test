package org.apache.poi.ss.formula.ptg;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/ptg/AddPtg.class */
public final class AddPtg extends ValueOperatorPtg {
    public static final byte sid = 3;
    private static final String ADD = "+";
    public static final ValueOperatorPtg instance = new AddPtg();

    private AddPtg() {
    }

    @Override // org.apache.poi.ss.formula.ptg.ValueOperatorPtg
    protected byte getSid() {
        return (byte) 3;
    }

    @Override // org.apache.poi.ss.formula.ptg.OperationPtg
    public int getNumberOfOperands() {
        return 2;
    }

    @Override // org.apache.poi.ss.formula.ptg.OperationPtg
    public String toFormulaString(String[] operands) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(operands[0]);
        buffer.append("+");
        buffer.append(operands[1]);
        return buffer.toString();
    }
}

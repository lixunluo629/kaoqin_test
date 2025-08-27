package org.apache.poi.ss.formula.ptg;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/ptg/OperationPtg.class */
public abstract class OperationPtg extends Ptg {
    public static final int TYPE_UNARY = 0;
    public static final int TYPE_BINARY = 1;
    public static final int TYPE_FUNCTION = 2;

    public abstract String toFormulaString(String[] strArr);

    public abstract int getNumberOfOperands();

    @Override // org.apache.poi.ss.formula.ptg.Ptg
    public byte getDefaultOperandClass() {
        return (byte) 32;
    }
}

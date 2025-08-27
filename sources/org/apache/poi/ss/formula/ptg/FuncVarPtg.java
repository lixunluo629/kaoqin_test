package org.apache.poi.ss.formula.ptg;

import org.apache.poi.ss.formula.function.FunctionMetadata;
import org.apache.poi.ss.formula.function.FunctionMetadataRegistry;
import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/ptg/FuncVarPtg.class */
public final class FuncVarPtg extends AbstractFunctionPtg {
    public static final byte sid = 34;
    private static final int SIZE = 4;
    public static final OperationPtg SUM = create("SUM", 1);

    private FuncVarPtg(int functionIndex, int returnClass, byte[] paramClasses, int numArgs) {
        super(functionIndex, returnClass, paramClasses, numArgs);
    }

    public static FuncVarPtg create(LittleEndianInput in) {
        return create(in.readByte(), in.readShort());
    }

    public static FuncVarPtg create(String pName, int numArgs) {
        return create(numArgs, lookupIndex(pName));
    }

    private static FuncVarPtg create(int numArgs, int functionIndex) {
        FunctionMetadata fm = FunctionMetadataRegistry.getFunctionByIndex(functionIndex);
        if (fm == null) {
            return new FuncVarPtg(functionIndex, 32, new byte[]{32}, numArgs);
        }
        return new FuncVarPtg(functionIndex, fm.getReturnClassCode(), fm.getParameterClassCodes(), numArgs);
    }

    @Override // org.apache.poi.ss.formula.ptg.Ptg
    public void write(LittleEndianOutput out) {
        out.writeByte(34 + getPtgClass());
        out.writeByte(getNumberOfOperands());
        out.writeShort(getFunctionIndex());
    }

    @Override // org.apache.poi.ss.formula.ptg.AbstractFunctionPtg, org.apache.poi.ss.formula.ptg.Ptg
    public int getSize() {
        return 4;
    }
}

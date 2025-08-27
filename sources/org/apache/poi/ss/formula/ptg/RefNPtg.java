package org.apache.poi.ss.formula.ptg;

import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/ptg/RefNPtg.class */
public final class RefNPtg extends Ref2DPtgBase {
    public static final byte sid = 44;

    @Override // org.apache.poi.ss.formula.ptg.Ref2DPtgBase, org.apache.poi.ss.formula.ptg.Ptg
    public /* bridge */ /* synthetic */ void write(LittleEndianOutput x0) {
        super.write(x0);
    }

    public RefNPtg(LittleEndianInput in) {
        super(in);
    }

    @Override // org.apache.poi.ss.formula.ptg.Ref2DPtgBase
    protected byte getSid() {
        return (byte) 44;
    }
}

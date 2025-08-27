package org.apache.poi.ss.formula.ptg;

import org.apache.poi.util.LittleEndianInput;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/ptg/AreaNPtg.class */
public final class AreaNPtg extends Area2DPtgBase {
    public static final short sid = 45;

    public AreaNPtg(LittleEndianInput in) {
        super(in);
    }

    @Override // org.apache.poi.ss.formula.ptg.Area2DPtgBase
    protected byte getSid() {
        return (byte) 45;
    }
}

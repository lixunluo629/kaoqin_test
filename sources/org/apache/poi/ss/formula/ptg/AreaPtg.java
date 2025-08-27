package org.apache.poi.ss.formula.ptg;

import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.Removal;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/ptg/AreaPtg.class */
public final class AreaPtg extends Area2DPtgBase {
    public static final short sid = 37;

    public AreaPtg(int firstRow, int lastRow, int firstColumn, int lastColumn, boolean firstRowRelative, boolean lastRowRelative, boolean firstColRelative, boolean lastColRelative) {
        super(firstRow, lastRow, firstColumn, lastColumn, firstRowRelative, lastRowRelative, firstColRelative, lastColRelative);
    }

    public AreaPtg(LittleEndianInput in) {
        super(in);
    }

    @Removal(version = "3.19")
    @Deprecated
    public AreaPtg(String arearef) {
        this(new AreaReference(arearef, SpreadsheetVersion.EXCEL97));
    }

    public AreaPtg(AreaReference arearef) {
        super(arearef);
    }

    @Override // org.apache.poi.ss.formula.ptg.Area2DPtgBase
    protected byte getSid() {
        return (byte) 37;
    }
}

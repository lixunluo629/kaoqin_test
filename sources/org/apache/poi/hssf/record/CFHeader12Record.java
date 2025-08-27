package org.apache.poi.hssf.record;

import org.apache.poi.hssf.record.common.FtrHeader;
import org.apache.poi.hssf.record.common.FutureRecord;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.LittleEndianOutput;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/CFHeader12Record.class */
public final class CFHeader12Record extends CFHeaderBase implements FutureRecord, Cloneable {
    public static final short sid = 2169;
    private FtrHeader futureHeader;

    public CFHeader12Record() {
        createEmpty();
        this.futureHeader = new FtrHeader();
        this.futureHeader.setRecordType((short) 2169);
    }

    public CFHeader12Record(CellRangeAddress[] regions, int nRules) {
        super(regions, nRules);
        this.futureHeader = new FtrHeader();
        this.futureHeader.setRecordType((short) 2169);
    }

    public CFHeader12Record(RecordInputStream in) {
        this.futureHeader = new FtrHeader(in);
        read(in);
    }

    @Override // org.apache.poi.hssf.record.CFHeaderBase
    protected String getRecordName() {
        return "CFHEADER12";
    }

    @Override // org.apache.poi.hssf.record.CFHeaderBase, org.apache.poi.hssf.record.StandardRecord
    protected int getDataSize() {
        return FtrHeader.getDataSize() + super.getDataSize();
    }

    @Override // org.apache.poi.hssf.record.CFHeaderBase, org.apache.poi.hssf.record.StandardRecord
    public void serialize(LittleEndianOutput out) {
        this.futureHeader.setAssociatedRange(getEnclosingCellRange());
        this.futureHeader.serialize(out);
        super.serialize(out);
    }

    @Override // org.apache.poi.hssf.record.Record
    public short getSid() {
        return (short) 2169;
    }

    @Override // org.apache.poi.hssf.record.common.FutureRecord
    public short getFutureRecordType() {
        return this.futureHeader.getRecordType();
    }

    @Override // org.apache.poi.hssf.record.common.FutureRecord
    public FtrHeader getFutureHeader() {
        return this.futureHeader;
    }

    @Override // org.apache.poi.hssf.record.common.FutureRecord
    public CellRangeAddress getAssociatedRange() {
        return this.futureHeader.getAssociatedRange();
    }

    @Override // org.apache.poi.hssf.record.CFHeaderBase, org.apache.poi.hssf.record.Record
    public CFHeader12Record clone() {
        CFHeader12Record result = new CFHeader12Record();
        result.futureHeader = (FtrHeader) this.futureHeader.clone();
        super.copyTo(result);
        return result;
    }
}

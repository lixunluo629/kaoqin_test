package org.apache.poi.hssf.record;

import org.apache.poi.util.IntList;
import org.apache.poi.util.LittleEndianOutput;
import org.apache.poi.util.RecordFormatException;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/IndexRecord.class */
public final class IndexRecord extends StandardRecord implements Cloneable {
    public static final short sid = 523;
    private int field_2_first_row;
    private int field_3_last_row_add1;
    private int field_4_zero;
    private IntList field_5_dbcells;

    public IndexRecord() {
    }

    public IndexRecord(RecordInputStream in) throws RecordFormatException {
        int field_1_zero = in.readInt();
        if (field_1_zero != 0) {
            throw new RecordFormatException("Expected zero for field 1 but got " + field_1_zero);
        }
        this.field_2_first_row = in.readInt();
        this.field_3_last_row_add1 = in.readInt();
        this.field_4_zero = in.readInt();
        int nCells = in.remaining() / 4;
        this.field_5_dbcells = new IntList(nCells);
        for (int i = 0; i < nCells; i++) {
            this.field_5_dbcells.add(in.readInt());
        }
    }

    public void setFirstRow(int row) {
        this.field_2_first_row = row;
    }

    public void setLastRowAdd1(int row) {
        this.field_3_last_row_add1 = row;
    }

    public void addDbcell(int cell) {
        if (this.field_5_dbcells == null) {
            this.field_5_dbcells = new IntList();
        }
        this.field_5_dbcells.add(cell);
    }

    public void setDbcell(int cell, int value) {
        this.field_5_dbcells.set(cell, value);
    }

    public int getFirstRow() {
        return this.field_2_first_row;
    }

    public int getLastRowAdd1() {
        return this.field_3_last_row_add1;
    }

    public int getNumDbcells() {
        if (this.field_5_dbcells == null) {
            return 0;
        }
        return this.field_5_dbcells.size();
    }

    public int getDbcellAt(int cellnum) {
        return this.field_5_dbcells.get(cellnum);
    }

    @Override // org.apache.poi.hssf.record.Record
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[INDEX]\n");
        buffer.append("    .firstrow       = ").append(Integer.toHexString(getFirstRow())).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("    .lastrowadd1    = ").append(Integer.toHexString(getLastRowAdd1())).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        for (int k = 0; k < getNumDbcells(); k++) {
            buffer.append("    .dbcell_").append(k).append(" = ").append(Integer.toHexString(getDbcellAt(k))).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        }
        buffer.append("[/INDEX]\n");
        return buffer.toString();
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    public void serialize(LittleEndianOutput out) {
        out.writeInt(0);
        out.writeInt(getFirstRow());
        out.writeInt(getLastRowAdd1());
        out.writeInt(this.field_4_zero);
        for (int k = 0; k < getNumDbcells(); k++) {
            out.writeInt(getDbcellAt(k));
        }
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    protected int getDataSize() {
        return 16 + (getNumDbcells() * 4);
    }

    public static int getRecordSizeForBlockCount(int blockCount) {
        return 20 + (4 * blockCount);
    }

    @Override // org.apache.poi.hssf.record.Record
    public short getSid() {
        return (short) 523;
    }

    @Override // org.apache.poi.hssf.record.Record
    public IndexRecord clone() {
        IndexRecord rec = new IndexRecord();
        rec.field_2_first_row = this.field_2_first_row;
        rec.field_3_last_row_add1 = this.field_3_last_row_add1;
        rec.field_4_zero = this.field_4_zero;
        rec.field_5_dbcells = new IntList();
        rec.field_5_dbcells.addAll(this.field_5_dbcells);
        return rec;
    }
}

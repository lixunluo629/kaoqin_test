package org.apache.poi.hssf.record;

import org.apache.poi.hssf.util.RKUtil;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;
import org.apache.poi.util.RecordFormatException;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/MulRKRecord.class */
public final class MulRKRecord extends StandardRecord {
    public static final short sid = 189;
    private final int field_1_row;
    private final short field_2_first_col;
    private final RkRec[] field_3_rks;
    private final short field_4_last_col;

    public int getRow() {
        return this.field_1_row;
    }

    public short getFirstColumn() {
        return this.field_2_first_col;
    }

    public short getLastColumn() {
        return this.field_4_last_col;
    }

    public int getNumColumns() {
        return (this.field_4_last_col - this.field_2_first_col) + 1;
    }

    public short getXFAt(int coffset) {
        return this.field_3_rks[coffset].xf;
    }

    public double getRKNumberAt(int coffset) {
        return RKUtil.decodeNumber(this.field_3_rks[coffset].rk);
    }

    public MulRKRecord(RecordInputStream in) {
        this.field_1_row = in.readUShort();
        this.field_2_first_col = in.readShort();
        this.field_3_rks = RkRec.parseRKs(in);
        this.field_4_last_col = in.readShort();
    }

    @Override // org.apache.poi.hssf.record.Record
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[MULRK]\n");
        buffer.append("\t.row\t = ").append(HexDump.shortToHex(getRow())).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("\t.firstcol= ").append(HexDump.shortToHex(getFirstColumn())).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("\t.lastcol = ").append(HexDump.shortToHex(getLastColumn())).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        for (int k = 0; k < getNumColumns(); k++) {
            buffer.append("\txf[").append(k).append("] = ").append(HexDump.shortToHex(getXFAt(k))).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
            buffer.append("\trk[").append(k).append("] = ").append(getRKNumberAt(k)).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        }
        buffer.append("[/MULRK]\n");
        return buffer.toString();
    }

    @Override // org.apache.poi.hssf.record.Record
    public short getSid() {
        return (short) 189;
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    public void serialize(LittleEndianOutput out) {
        throw new RecordFormatException("Sorry, you can't serialize MulRK in this release");
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    protected int getDataSize() {
        throw new RecordFormatException("Sorry, you can't serialize MulRK in this release");
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/MulRKRecord$RkRec.class */
    private static final class RkRec {
        public static final int ENCODED_SIZE = 6;
        public final short xf;
        public final int rk;

        private RkRec(RecordInputStream in) {
            this.xf = in.readShort();
            this.rk = in.readInt();
        }

        public static RkRec[] parseRKs(RecordInputStream in) {
            int nItems = (in.remaining() - 2) / 6;
            RkRec[] retval = new RkRec[nItems];
            for (int i = 0; i < nItems; i++) {
                retval[i] = new RkRec(in);
            }
            return retval;
        }
    }
}

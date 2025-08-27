package org.apache.poi.hssf.record;

import org.apache.poi.ss.usermodel.FormulaError;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;
import org.apache.poi.util.RecordFormatException;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/BoolErrRecord.class */
public final class BoolErrRecord extends CellRecord implements Cloneable {
    public static final short sid = 517;
    private int _value;
    private boolean _isError;

    public BoolErrRecord() {
    }

    public BoolErrRecord(RecordInputStream in) {
        super(in);
        switch (in.remaining()) {
            case 2:
                this._value = in.readByte();
                break;
            case 3:
                this._value = in.readUShort();
                break;
            default:
                throw new RecordFormatException("Unexpected size (" + in.remaining() + ") for BOOLERR record.");
        }
        int flag = in.readUByte();
        switch (flag) {
            case 0:
                this._isError = false;
                return;
            case 1:
                this._isError = true;
                return;
            default:
                throw new RecordFormatException("Unexpected isError flag (" + flag + ") for BOOLERR record.");
        }
    }

    public void setValue(boolean value) {
        this._value = value ? 1 : 0;
        this._isError = false;
    }

    public void setValue(byte value) {
        setValue(FormulaError.forInt(value));
    }

    public void setValue(FormulaError value) {
        switch (value) {
            case NULL:
            case DIV0:
            case VALUE:
            case REF:
            case NAME:
            case NUM:
            case NA:
                this._value = value.getCode();
                this._isError = true;
                return;
            default:
                throw new IllegalArgumentException("Error Value can only be 0,7,15,23,29,36 or 42. It cannot be " + ((int) value.getCode()) + " (" + value + ")");
        }
    }

    public boolean getBooleanValue() {
        return this._value != 0;
    }

    public byte getErrorValue() {
        return (byte) this._value;
    }

    public boolean isBoolean() {
        return !this._isError;
    }

    public boolean isError() {
        return this._isError;
    }

    @Override // org.apache.poi.hssf.record.CellRecord
    protected String getRecordName() {
        return "BOOLERR";
    }

    @Override // org.apache.poi.hssf.record.CellRecord
    protected void appendValueText(StringBuilder sb) {
        if (isBoolean()) {
            sb.append("  .boolVal = ");
            sb.append(getBooleanValue());
        } else {
            sb.append("  .errCode = ");
            sb.append(FormulaError.forInt(getErrorValue()).getString());
            sb.append(" (").append(HexDump.byteToHex(getErrorValue())).append(")");
        }
    }

    @Override // org.apache.poi.hssf.record.CellRecord
    protected void serializeValue(LittleEndianOutput out) {
        out.writeByte(this._value);
        out.writeByte(this._isError ? 1 : 0);
    }

    @Override // org.apache.poi.hssf.record.CellRecord
    protected int getValueDataSize() {
        return 2;
    }

    @Override // org.apache.poi.hssf.record.Record
    public short getSid() {
        return (short) 517;
    }

    @Override // org.apache.poi.hssf.record.Record
    public BoolErrRecord clone() {
        BoolErrRecord rec = new BoolErrRecord();
        copyBaseFields(rec);
        rec._value = this._value;
        rec._isError = this._isError;
        return rec;
    }
}

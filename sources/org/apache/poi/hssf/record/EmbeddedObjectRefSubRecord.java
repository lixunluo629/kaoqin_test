package org.apache.poi.hssf.record;

import java.io.ByteArrayInputStream;
import org.apache.poi.ss.formula.ptg.Area3DPtg;
import org.apache.poi.ss.formula.ptg.AreaPtg;
import org.apache.poi.ss.formula.ptg.Ptg;
import org.apache.poi.ss.formula.ptg.Ref3DPtg;
import org.apache.poi.ss.formula.ptg.RefPtg;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianInputStream;
import org.apache.poi.util.LittleEndianOutput;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;
import org.apache.poi.util.RecordFormatException;
import org.apache.poi.util.StringUtil;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/EmbeddedObjectRefSubRecord.class */
public final class EmbeddedObjectRefSubRecord extends SubRecord implements Cloneable {
    public static final short sid = 9;
    private int field_1_unknown_int;
    private Ptg field_2_refPtg;
    private byte[] field_2_unknownFormulaData;
    private boolean field_3_unicode_flag;
    private String field_4_ole_classname;
    private Byte field_4_unknownByte;
    private Integer field_5_stream_id;
    private byte[] field_6_unknown;
    private static POILogger logger = POILogFactory.getLogger((Class<?>) EmbeddedObjectRefSubRecord.class);
    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    public EmbeddedObjectRefSubRecord() {
        this.field_2_unknownFormulaData = new byte[]{2, 108, 106, 22, 1};
        this.field_6_unknown = EMPTY_BYTE_ARRAY;
        this.field_4_ole_classname = null;
    }

    public short getSid() {
        return (short) 9;
    }

    public EmbeddedObjectRefSubRecord(LittleEndianInput in, int size) {
        int stringByteCount;
        int streamIdOffset = in.readShort();
        int remaining = size - 2;
        int dataLenAfterFormula = remaining - streamIdOffset;
        int formulaSize = in.readUShort();
        this.field_1_unknown_int = in.readInt();
        int remaining2 = (remaining - 2) - 4;
        byte[] formulaRawBytes = readRawData(in, formulaSize);
        int remaining3 = remaining2 - formulaSize;
        this.field_2_refPtg = readRefPtg(formulaRawBytes);
        if (this.field_2_refPtg == null) {
            this.field_2_unknownFormulaData = formulaRawBytes;
        } else {
            this.field_2_unknownFormulaData = null;
        }
        if (remaining3 >= dataLenAfterFormula + 3) {
            int tag = in.readByte();
            if (tag != 3) {
                throw new RecordFormatException("Expected byte 0x03 here");
            }
            int nChars = in.readUShort();
            stringByteCount = 1 + 2;
            if (nChars > 0) {
                this.field_3_unicode_flag = (in.readByte() & 1) != 0;
                int stringByteCount2 = stringByteCount + 1;
                if (this.field_3_unicode_flag) {
                    this.field_4_ole_classname = StringUtil.readUnicodeLE(in, nChars);
                    stringByteCount = stringByteCount2 + (nChars * 2);
                } else {
                    this.field_4_ole_classname = StringUtil.readCompressedUnicode(in, nChars);
                    stringByteCount = stringByteCount2 + nChars;
                }
            } else {
                this.field_4_ole_classname = "";
            }
        } else {
            this.field_4_ole_classname = null;
            stringByteCount = 0;
        }
        int remaining4 = remaining3 - stringByteCount;
        if ((stringByteCount + formulaSize) % 2 != 0) {
            int b = in.readByte();
            remaining4--;
            if (this.field_2_refPtg != null && this.field_4_ole_classname == null) {
                this.field_4_unknownByte = Byte.valueOf((byte) b);
            }
        }
        int nUnexpectedPadding = remaining4 - dataLenAfterFormula;
        if (nUnexpectedPadding > 0) {
            logger.log(7, "Discarding " + nUnexpectedPadding + " unexpected padding bytes ");
            readRawData(in, nUnexpectedPadding);
            remaining4 -= nUnexpectedPadding;
        }
        if (dataLenAfterFormula >= 4) {
            this.field_5_stream_id = Integer.valueOf(in.readInt());
            remaining4 -= 4;
        } else {
            this.field_5_stream_id = null;
        }
        this.field_6_unknown = readRawData(in, remaining4);
    }

    private static Ptg readRefPtg(byte[] formulaRawBytes) {
        LittleEndianInput in = new LittleEndianInputStream(new ByteArrayInputStream(formulaRawBytes));
        byte ptgSid = in.readByte();
        switch (ptgSid) {
            case 36:
                return new RefPtg(in);
            case 37:
                return new AreaPtg(in);
            case 58:
                return new Ref3DPtg(in);
            case 59:
                return new Area3DPtg(in);
            default:
                return null;
        }
    }

    private static byte[] readRawData(LittleEndianInput in, int size) {
        if (size < 0) {
            throw new IllegalArgumentException("Negative size (" + size + ")");
        }
        if (size == 0) {
            return EMPTY_BYTE_ARRAY;
        }
        byte[] result = new byte[size];
        in.readFully(result);
        return result;
    }

    private int getStreamIDOffset(int formulaSize) {
        int result = 6 + formulaSize;
        if (this.field_4_ole_classname != null) {
            result += 3;
            int stringLen = this.field_4_ole_classname.length();
            if (stringLen > 0) {
                int result2 = result + 1;
                if (this.field_3_unicode_flag) {
                    result = result2 + (stringLen * 2);
                } else {
                    result = result2 + stringLen;
                }
            }
        }
        if (result % 2 != 0) {
            result++;
        }
        return result;
    }

    private int getDataSize(int idOffset) {
        int result = 2 + idOffset;
        if (this.field_5_stream_id != null) {
            result += 4;
        }
        return result + this.field_6_unknown.length;
    }

    @Override // org.apache.poi.hssf.record.SubRecord
    protected int getDataSize() {
        int formulaSize = this.field_2_refPtg == null ? this.field_2_unknownFormulaData.length : this.field_2_refPtg.getSize();
        int idOffset = getStreamIDOffset(formulaSize);
        return getDataSize(idOffset);
    }

    @Override // org.apache.poi.hssf.record.SubRecord
    public void serialize(LittleEndianOutput out) {
        int formulaSize = this.field_2_refPtg == null ? this.field_2_unknownFormulaData.length : this.field_2_refPtg.getSize();
        int idOffset = getStreamIDOffset(formulaSize);
        int dataSize = getDataSize(idOffset);
        out.writeShort(9);
        out.writeShort(dataSize);
        out.writeShort(idOffset);
        out.writeShort(formulaSize);
        out.writeInt(this.field_1_unknown_int);
        if (this.field_2_refPtg == null) {
            out.write(this.field_2_unknownFormulaData);
        } else {
            this.field_2_refPtg.write(out);
        }
        int pos = 12 + formulaSize;
        if (this.field_4_ole_classname != null) {
            out.writeByte(3);
            int stringLen = this.field_4_ole_classname.length();
            out.writeShort(stringLen);
            pos = pos + 1 + 2;
            if (stringLen > 0) {
                out.writeByte(this.field_3_unicode_flag ? 1 : 0);
                int pos2 = pos + 1;
                if (this.field_3_unicode_flag) {
                    StringUtil.putUnicodeLE(this.field_4_ole_classname, out);
                    pos = pos2 + (stringLen * 2);
                } else {
                    StringUtil.putCompressedUnicode(this.field_4_ole_classname, out);
                    pos = pos2 + stringLen;
                }
            }
        }
        switch (idOffset - (pos - 6)) {
            case 0:
                break;
            case 1:
                out.writeByte(this.field_4_unknownByte == null ? 0 : this.field_4_unknownByte.intValue());
                pos++;
                break;
            default:
                throw new IllegalStateException("Bad padding calculation (" + idOffset + ", " + pos + ")");
        }
        if (this.field_5_stream_id != null) {
            out.writeInt(this.field_5_stream_id.intValue());
            int i = pos + 4;
        }
        out.write(this.field_6_unknown);
    }

    public Integer getStreamId() {
        return this.field_5_stream_id;
    }

    public String getOLEClassName() {
        return this.field_4_ole_classname;
    }

    public byte[] getObjectData() {
        return this.field_6_unknown;
    }

    @Override // org.apache.poi.hssf.record.SubRecord
    /* renamed from: clone */
    public EmbeddedObjectRefSubRecord mo3334clone() {
        return this;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[ftPictFmla]\n");
        sb.append("    .f2unknown     = ").append(HexDump.intToHex(this.field_1_unknown_int)).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        if (this.field_2_refPtg == null) {
            sb.append("    .f3unknown     = ").append(HexDump.toHex(this.field_2_unknownFormulaData)).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        } else {
            sb.append("    .formula       = ").append(this.field_2_refPtg).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        }
        if (this.field_4_ole_classname != null) {
            sb.append("    .unicodeFlag   = ").append(this.field_3_unicode_flag).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
            sb.append("    .oleClassname  = ").append(this.field_4_ole_classname).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        }
        if (this.field_4_unknownByte != null) {
            sb.append("    .f4unknown   = ").append(HexDump.byteToHex(this.field_4_unknownByte.intValue())).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        }
        if (this.field_5_stream_id != null) {
            sb.append("    .streamId      = ").append(HexDump.intToHex(this.field_5_stream_id.intValue())).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        }
        if (this.field_6_unknown.length > 0) {
            sb.append("    .f7unknown     = ").append(HexDump.toHex(this.field_6_unknown)).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        }
        sb.append("[/ftPictFmla]");
        return sb.toString();
    }

    public void setUnknownFormulaData(byte[] formularData) {
        this.field_2_unknownFormulaData = formularData;
    }

    public void setOleClassname(String oleClassname) {
        this.field_4_ole_classname = oleClassname;
    }

    public void setStorageId(int storageId) {
        this.field_5_stream_id = Integer.valueOf(storageId);
    }
}

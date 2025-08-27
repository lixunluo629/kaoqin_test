package org.apache.poi.hssf.record;

import org.apache.poi.util.BitField;
import org.apache.poi.util.BitFieldFactory;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;
import org.apache.poi.util.StringUtil;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/FontRecord.class */
public final class FontRecord extends StandardRecord {
    public static final short sid = 49;
    public static final short SS_NONE = 0;
    public static final short SS_SUPER = 1;
    public static final short SS_SUB = 2;
    public static final byte U_NONE = 0;
    public static final byte U_SINGLE = 1;
    public static final byte U_DOUBLE = 2;
    public static final byte U_SINGLE_ACCOUNTING = 33;
    public static final byte U_DOUBLE_ACCOUNTING = 34;
    private short field_1_font_height;
    private short field_2_attributes;
    private static final BitField italic = BitFieldFactory.getInstance(2);
    private static final BitField strikeout = BitFieldFactory.getInstance(8);
    private static final BitField macoutline = BitFieldFactory.getInstance(16);
    private static final BitField macshadow = BitFieldFactory.getInstance(32);
    private short field_3_color_palette_index;
    private short field_4_bold_weight;
    private short field_5_super_sub_script;
    private byte field_6_underline;
    private byte field_7_family;
    private byte field_8_charset;
    private byte field_9_zero;
    private String field_11_font_name;

    public FontRecord() {
        this.field_9_zero = (byte) 0;
    }

    public FontRecord(RecordInputStream in) {
        this.field_9_zero = (byte) 0;
        this.field_1_font_height = in.readShort();
        this.field_2_attributes = in.readShort();
        this.field_3_color_palette_index = in.readShort();
        this.field_4_bold_weight = in.readShort();
        this.field_5_super_sub_script = in.readShort();
        this.field_6_underline = in.readByte();
        this.field_7_family = in.readByte();
        this.field_8_charset = in.readByte();
        this.field_9_zero = in.readByte();
        int field_10_font_name_len = in.readUByte();
        int unicodeFlags = in.readUByte();
        if (field_10_font_name_len > 0) {
            if (unicodeFlags == 0) {
                this.field_11_font_name = in.readCompressedUnicode(field_10_font_name_len);
                return;
            } else {
                this.field_11_font_name = in.readUnicodeLEString(field_10_font_name_len);
                return;
            }
        }
        this.field_11_font_name = "";
    }

    public void setFontHeight(short height) {
        this.field_1_font_height = height;
    }

    public void setAttributes(short attributes) {
        this.field_2_attributes = attributes;
    }

    public void setItalic(boolean italics) {
        this.field_2_attributes = italic.setShortBoolean(this.field_2_attributes, italics);
    }

    public void setStrikeout(boolean strike) {
        this.field_2_attributes = strikeout.setShortBoolean(this.field_2_attributes, strike);
    }

    public void setMacoutline(boolean mac) {
        this.field_2_attributes = macoutline.setShortBoolean(this.field_2_attributes, mac);
    }

    public void setMacshadow(boolean mac) {
        this.field_2_attributes = macshadow.setShortBoolean(this.field_2_attributes, mac);
    }

    public void setColorPaletteIndex(short cpi) {
        this.field_3_color_palette_index = cpi;
    }

    public void setBoldWeight(short bw) {
        this.field_4_bold_weight = bw;
    }

    public void setSuperSubScript(short sss) {
        this.field_5_super_sub_script = sss;
    }

    public void setUnderline(byte u) {
        this.field_6_underline = u;
    }

    public void setFamily(byte f) {
        this.field_7_family = f;
    }

    public void setCharset(byte charset) {
        this.field_8_charset = charset;
    }

    public void setFontName(String fn) {
        this.field_11_font_name = fn;
    }

    public short getFontHeight() {
        return this.field_1_font_height;
    }

    public short getAttributes() {
        return this.field_2_attributes;
    }

    public boolean isItalic() {
        return italic.isSet(this.field_2_attributes);
    }

    public boolean isStruckout() {
        return strikeout.isSet(this.field_2_attributes);
    }

    public boolean isMacoutlined() {
        return macoutline.isSet(this.field_2_attributes);
    }

    public boolean isMacshadowed() {
        return macshadow.isSet(this.field_2_attributes);
    }

    public short getColorPaletteIndex() {
        return this.field_3_color_palette_index;
    }

    public short getBoldWeight() {
        return this.field_4_bold_weight;
    }

    public short getSuperSubScript() {
        return this.field_5_super_sub_script;
    }

    public byte getUnderline() {
        return this.field_6_underline;
    }

    public byte getFamily() {
        return this.field_7_family;
    }

    public byte getCharset() {
        return this.field_8_charset;
    }

    public String getFontName() {
        return this.field_11_font_name;
    }

    @Override // org.apache.poi.hssf.record.Record
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[FONT]\n");
        sb.append("    .fontheight    = ").append(HexDump.shortToHex(getFontHeight())).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        sb.append("    .attributes    = ").append(HexDump.shortToHex(getAttributes())).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        sb.append("       .italic     = ").append(isItalic()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        sb.append("       .strikout   = ").append(isStruckout()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        sb.append("       .macoutlined= ").append(isMacoutlined()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        sb.append("       .macshadowed= ").append(isMacshadowed()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        sb.append("    .colorpalette  = ").append(HexDump.shortToHex(getColorPaletteIndex())).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        sb.append("    .boldweight    = ").append(HexDump.shortToHex(getBoldWeight())).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        sb.append("    .supersubscript= ").append(HexDump.shortToHex(getSuperSubScript())).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        sb.append("    .underline     = ").append(HexDump.byteToHex(getUnderline())).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        sb.append("    .family        = ").append(HexDump.byteToHex(getFamily())).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        sb.append("    .charset       = ").append(HexDump.byteToHex(getCharset())).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        sb.append("    .fontname      = ").append(getFontName()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        sb.append("[/FONT]\n");
        return sb.toString();
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    public void serialize(LittleEndianOutput out) {
        out.writeShort(getFontHeight());
        out.writeShort(getAttributes());
        out.writeShort(getColorPaletteIndex());
        out.writeShort(getBoldWeight());
        out.writeShort(getSuperSubScript());
        out.writeByte(getUnderline());
        out.writeByte(getFamily());
        out.writeByte(getCharset());
        out.writeByte(this.field_9_zero);
        int fontNameLen = this.field_11_font_name.length();
        out.writeByte(fontNameLen);
        boolean hasMultibyte = StringUtil.hasMultibyte(this.field_11_font_name);
        out.writeByte(hasMultibyte ? 1 : 0);
        if (fontNameLen > 0) {
            if (hasMultibyte) {
                StringUtil.putUnicodeLE(this.field_11_font_name, out);
            } else {
                StringUtil.putCompressedUnicode(this.field_11_font_name, out);
            }
        }
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    protected int getDataSize() {
        int fontNameLen = this.field_11_font_name.length();
        if (fontNameLen < 1) {
            return 16;
        }
        boolean hasMultibyte = StringUtil.hasMultibyte(this.field_11_font_name);
        return 16 + (fontNameLen * (hasMultibyte ? 2 : 1));
    }

    @Override // org.apache.poi.hssf.record.Record
    public short getSid() {
        return (short) 49;
    }

    public void cloneStyleFrom(FontRecord source) {
        this.field_1_font_height = source.field_1_font_height;
        this.field_2_attributes = source.field_2_attributes;
        this.field_3_color_palette_index = source.field_3_color_palette_index;
        this.field_4_bold_weight = source.field_4_bold_weight;
        this.field_5_super_sub_script = source.field_5_super_sub_script;
        this.field_6_underline = source.field_6_underline;
        this.field_7_family = source.field_7_family;
        this.field_8_charset = source.field_8_charset;
        this.field_9_zero = source.field_9_zero;
        this.field_11_font_name = source.field_11_font_name;
    }

    public int hashCode() {
        int result = (31 * 1) + (this.field_11_font_name == null ? 0 : this.field_11_font_name.hashCode());
        return (31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * result) + this.field_1_font_height)) + this.field_2_attributes)) + this.field_3_color_palette_index)) + this.field_4_bold_weight)) + this.field_5_super_sub_script)) + this.field_6_underline)) + this.field_7_family)) + this.field_8_charset)) + this.field_9_zero;
    }

    public boolean sameProperties(FontRecord other) {
        return this.field_1_font_height == other.field_1_font_height && this.field_2_attributes == other.field_2_attributes && this.field_3_color_palette_index == other.field_3_color_palette_index && this.field_4_bold_weight == other.field_4_bold_weight && this.field_5_super_sub_script == other.field_5_super_sub_script && this.field_6_underline == other.field_6_underline && this.field_7_family == other.field_7_family && this.field_8_charset == other.field_8_charset && this.field_9_zero == other.field_9_zero && stringEquals(this.field_11_font_name, other.field_11_font_name);
    }

    public boolean equals(Object o) {
        if (o instanceof FontRecord) {
            return sameProperties((FontRecord) o);
        }
        return false;
    }

    private static boolean stringEquals(String s1, String s2) {
        return s1 == s2 || (s1 != null && s1.equals(s2));
    }
}

package com.itextpdf.io.font.otf;

import com.itextpdf.io.util.MessageFormatUtil;
import com.itextpdf.io.util.TextUtil;
import java.io.Serializable;
import java.util.Arrays;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/otf/Glyph.class */
public class Glyph implements Serializable {
    private static final long serialVersionUID = 1627806639423114471L;
    private static final char REPLACEMENT_CHARACTER = 65533;
    private static final char[] REPLACEMENT_CHARACTERS = {65533};
    private static final String REPLACEMENT_CHARACTER_STRING = String.valueOf((char) 65533);
    private final int code;
    private final int width;
    private int[] bbox;
    private int unicode;
    private char[] chars;
    private final boolean isMark;
    short xPlacement;
    short yPlacement;
    short xAdvance;
    short yAdvance;
    short anchorDelta;

    public Glyph(int code, int width, int unicode) {
        this(code, width, unicode, null, false);
    }

    public Glyph(int code, int width, char[] chars) {
        this(code, width, codePoint(chars), chars, false);
    }

    public Glyph(int code, int width, int unicode, int[] bbox) {
        this(code, width, unicode, null, false);
        this.bbox = bbox;
    }

    public Glyph(int width, int unicode) {
        this(-1, width, unicode, getChars(unicode), false);
    }

    public Glyph(int code, int width, int unicode, char[] chars, boolean IsMark) {
        this.bbox = null;
        this.xPlacement = (short) 0;
        this.yPlacement = (short) 0;
        this.xAdvance = (short) 0;
        this.yAdvance = (short) 0;
        this.anchorDelta = (short) 0;
        this.code = code;
        this.width = width;
        this.unicode = unicode;
        this.isMark = IsMark;
        this.chars = chars != null ? chars : getChars(unicode);
    }

    public Glyph(Glyph glyph) {
        this.bbox = null;
        this.xPlacement = (short) 0;
        this.yPlacement = (short) 0;
        this.xAdvance = (short) 0;
        this.yAdvance = (short) 0;
        this.anchorDelta = (short) 0;
        this.code = glyph.code;
        this.width = glyph.width;
        this.chars = glyph.chars;
        this.unicode = glyph.unicode;
        this.isMark = glyph.isMark;
        this.bbox = glyph.bbox;
        this.xPlacement = glyph.xPlacement;
        this.yPlacement = glyph.yPlacement;
        this.xAdvance = glyph.xAdvance;
        this.yAdvance = glyph.yAdvance;
        this.anchorDelta = glyph.anchorDelta;
    }

    public Glyph(Glyph glyph, int xPlacement, int yPlacement, int xAdvance, int yAdvance, int anchorDelta) {
        this(glyph);
        this.xPlacement = (short) xPlacement;
        this.yPlacement = (short) yPlacement;
        this.xAdvance = (short) xAdvance;
        this.yAdvance = (short) yAdvance;
        this.anchorDelta = (short) anchorDelta;
    }

    public Glyph(Glyph glyph, int unicode) {
        this(glyph.code, glyph.width, unicode, getChars(unicode), glyph.isMark());
    }

    public int getCode() {
        return this.code;
    }

    public int getWidth() {
        return this.width;
    }

    public int[] getBbox() {
        return this.bbox;
    }

    public boolean hasValidUnicode() {
        return this.unicode > -1;
    }

    public int getUnicode() {
        return this.unicode;
    }

    public void setUnicode(int unicode) {
        this.unicode = unicode;
        this.chars = getChars(unicode);
    }

    public char[] getChars() {
        return this.chars;
    }

    public void setChars(char[] chars) {
        this.chars = chars;
    }

    public boolean isMark() {
        return this.isMark;
    }

    public short getXPlacement() {
        return this.xPlacement;
    }

    public void setXPlacement(short xPlacement) {
        this.xPlacement = xPlacement;
    }

    public short getYPlacement() {
        return this.yPlacement;
    }

    public void setYPlacement(short yPlacement) {
        this.yPlacement = yPlacement;
    }

    public short getXAdvance() {
        return this.xAdvance;
    }

    public void setXAdvance(short xAdvance) {
        this.xAdvance = xAdvance;
    }

    public short getYAdvance() {
        return this.yAdvance;
    }

    public void setYAdvance(short yAdvance) {
        this.yAdvance = yAdvance;
    }

    public short getAnchorDelta() {
        return this.anchorDelta;
    }

    public void setAnchorDelta(short anchorDelta) {
        this.anchorDelta = anchorDelta;
    }

    public boolean hasOffsets() {
        return (this.xPlacement == 0 && this.yPlacement == 0 && this.xAdvance == 0 && this.yAdvance == 0) ? false : true;
    }

    public boolean hasPlacement() {
        return (this.xPlacement == 0 && this.yPlacement == 0) ? false : true;
    }

    public boolean hasAdvance() {
        return (this.xAdvance == 0 && this.yAdvance == 0) ? false : true;
    }

    public int hashCode() {
        int result = (31 * 1) + (this.chars == null ? 0 : Arrays.hashCode(this.chars));
        return (31 * ((31 * result) + this.code)) + this.width;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Glyph other = (Glyph) obj;
        return Arrays.equals(this.chars, other.chars) && this.code == other.code && this.width == other.width;
    }

    public String getUnicodeString() {
        if (this.chars != null) {
            return String.valueOf(this.chars);
        }
        return REPLACEMENT_CHARACTER_STRING;
    }

    public char[] getUnicodeChars() {
        if (this.chars != null) {
            return this.chars;
        }
        return REPLACEMENT_CHARACTERS;
    }

    public String toString() {
        Object[] objArr = new Object[4];
        objArr[0] = toHex(this.code);
        objArr[1] = this.chars != null ? Arrays.toString(this.chars) : "null";
        objArr[2] = toHex(this.unicode);
        objArr[3] = Integer.valueOf(this.width);
        return MessageFormatUtil.format("[id={0}, chars={1}, uni={2}, width={3}]", objArr);
    }

    private static String toHex(int ch2) {
        String s = "0000" + Integer.toHexString(ch2);
        return s.substring(Math.min(4, s.length() - 4));
    }

    private static int codePoint(char[] a) {
        if (a != null) {
            if (a.length == 1 && Character.isValidCodePoint(a[0])) {
                return a[0];
            }
            if (a.length == 2 && Character.isHighSurrogate(a[0]) && Character.isLowSurrogate(a[1])) {
                return Character.toCodePoint(a[0], a[1]);
            }
            return -1;
        }
        return -1;
    }

    private static char[] getChars(int unicode) {
        if (unicode > -1) {
            return TextUtil.convertFromUtf32(unicode);
        }
        return null;
    }
}

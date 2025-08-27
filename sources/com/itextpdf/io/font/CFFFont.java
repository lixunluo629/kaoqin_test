package com.itextpdf.io.font;

import ch.qos.logback.core.pattern.color.ANSIConstants;
import ch.qos.logback.core.rolling.helper.DateTokenConverter;
import com.alibaba.excel.constant.ExcelXmlConstants;
import com.drew.metadata.adobe.AdobeJpegReader;
import com.itextpdf.io.IOException;
import com.itextpdf.io.source.RandomAccessFileOrArray;
import com.itextpdf.io.source.RandomAccessSourceFactory;
import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import java.util.Iterator;
import java.util.LinkedList;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.poi.hssf.model.InternalWorkbook;
import org.apache.poi.ss.formula.functions.Complex;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/CFFFont.class */
public class CFFFont {
    static final String[] operatorNames = {"version", "Notice", "FullName", "FamilyName", "Weight", "FontBBox", "BlueValues", "OtherBlues", "FamilyBlues", "FamilyOtherBlues", "StdHW", "StdVW", "UNKNOWN_12", "UniqueID", "XUID", "charset", "Encoding", "CharStrings", StandardRoles.PRIVATE, "Subrs", "defaultWidthX", "nominalWidthX", "UNKNOWN_22", "UNKNOWN_23", "UNKNOWN_24", "UNKNOWN_25", "UNKNOWN_26", "UNKNOWN_27", "UNKNOWN_28", "UNKNOWN_29", "UNKNOWN_30", "UNKNOWN_31", "Copyright", "isFixedPitch", "ItalicAngle", "UnderlinePosition", "UnderlineThickness", "PaintType", "CharstringType", "FontMatrix", "StrokeWidth", "BlueScale", "BlueShift", "BlueFuzz", "StemSnapH", "StemSnapV", "ForceBold", "UNKNOWN_12_15", "UNKNOWN_12_16", "LanguageGroup", "ExpansionFactor", "initialRandomSeed", "SyntheticBase", "PostScript", "BaseFontName", "BaseFontBlend", "UNKNOWN_12_24", "UNKNOWN_12_25", "UNKNOWN_12_26", "UNKNOWN_12_27", "UNKNOWN_12_28", "UNKNOWN_12_29", "ROS", "CIDFontVersion", "CIDFontRevision", "CIDFontType", "CIDCount", "UIDBase", "FDArray", "FDSelect", "FontName"};
    static final String[] standardStrings = {FontEncoding.NOTDEF, "space", "exclam", "quotedbl", "numbersign", "dollar", "percent", "ampersand", "quoteright", "parenleft", "parenright", "asterisk", "plus", "comma", "hyphen", "period", "slash", "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "colon", "semicolon", "less", "equal", "greater", "question", "at", "A", "B", "C", "D", "E", "F", "G", StandardRoles.H, "I", "J", "K", StandardRoles.L, "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "bracketleft", "backslash", "bracketright", "asciicircum", "underscore", "quoteleft", "a", "b", ExcelXmlConstants.CELL_TAG, DateTokenConverter.CONVERTER_KEY, "e", ExcelXmlConstants.CELL_FORMULA_TAG, "g", "h", "i", Complex.SUPPORTED_SUFFIX, "k", "l", ANSIConstants.ESC_END, "n", "o", "p", "q", ExcelXmlConstants.POSITION, ExcelXmlConstants.CELL_DATA_FORMAT_TAG, "t", "u", ExcelXmlConstants.CELL_VALUE_TAG, "w", "x", "y", CompressorStreamFactory.Z, "braceleft", "bar", "braceright", "asciitilde", "exclamdown", "cent", "sterling", "fraction", "yen", "florin", "section", "currency", "quotesingle", "quotedblleft", "guillemotleft", "guilsinglleft", "guilsinglright", "fi", "fl", "endash", "dagger", "daggerdbl", "periodcentered", "paragraph", "bullet", "quotesinglbase", "quotedblbase", "quotedblright", "guillemotright", "ellipsis", "perthousand", "questiondown", "grave", "acute", "circumflex", "tilde", "macron", "breve", "dotaccent", "dieresis", "ring", "cedilla", "hungarumlaut", "ogonek", "caron", "emdash", "AE", "ordfeminine", "Lslash", "Oslash", "OE", "ordmasculine", "ae", "dotlessi", "lslash", "oslash", "oe", "germandbls", "onesuperior", "logicalnot", "mu", "trademark", "Eth", "onehalf", "plusminus", "Thorn", "onequarter", "divide", "brokenbar", "degree", "thorn", "threequarters", "twosuperior", "registered", "minus", "eth", "multiply", "threesuperior", "copyright", "Aacute", "Acircumflex", "Adieresis", "Agrave", "Aring", "Atilde", "Ccedilla", "Eacute", "Ecircumflex", "Edieresis", "Egrave", "Iacute", "Icircumflex", "Idieresis", "Igrave", "Ntilde", "Oacute", "Ocircumflex", "Odieresis", "Ograve", "Otilde", "Scaron", "Uacute", "Ucircumflex", "Udieresis", "Ugrave", "Yacute", "Ydieresis", "Zcaron", "aacute", "acircumflex", "adieresis", "agrave", "aring", "atilde", "ccedilla", "eacute", "ecircumflex", "edieresis", "egrave", "iacute", "icircumflex", "idieresis", "igrave", "ntilde", "oacute", "ocircumflex", "odieresis", "ograve", "otilde", "scaron", "uacute", "ucircumflex", "udieresis", "ugrave", "yacute", "ydieresis", "zcaron", "exclamsmall", "Hungarumlautsmall", "dollaroldstyle", "dollarsuperior", "ampersandsmall", "Acutesmall", "parenleftsuperior", "parenrightsuperior", "twodotenleader", "onedotenleader", "zerooldstyle", "oneoldstyle", "twooldstyle", "threeoldstyle", "fouroldstyle", "fiveoldstyle", "sixoldstyle", "sevenoldstyle", "eightoldstyle", "nineoldstyle", "commasuperior", "threequartersemdash", "periodsuperior", "questionsmall", "asuperior", "bsuperior", "centsuperior", "dsuperior", "esuperior", "isuperior", "lsuperior", "msuperior", "nsuperior", "osuperior", "rsuperior", "ssuperior", "tsuperior", "ff", "ffi", "ffl", "parenleftinferior", "parenrightinferior", "Circumflexsmall", "hyphensuperior", "Gravesmall", "Asmall", "Bsmall", "Csmall", "Dsmall", "Esmall", "Fsmall", "Gsmall", "Hsmall", "Ismall", "Jsmall", "Ksmall", "Lsmall", "Msmall", "Nsmall", "Osmall", "Psmall", "Qsmall", "Rsmall", "Ssmall", "Tsmall", "Usmall", "Vsmall", "Wsmall", "Xsmall", "Ysmall", "Zsmall", "colonmonetary", "onefitted", "rupiah", "Tildesmall", "exclamdownsmall", "centoldstyle", "Lslashsmall", "Scaronsmall", "Zcaronsmall", "Dieresissmall", "Brevesmall", "Caronsmall", "Dotaccentsmall", "Macronsmall", "figuredash", "hypheninferior", "Ogoneksmall", "Ringsmall", "Cedillasmall", "questiondownsmall", "oneeighth", "threeeighths", "fiveeighths", "seveneighths", "onethird", "twothirds", "zerosuperior", "foursuperior", "fivesuperior", "sixsuperior", "sevensuperior", "eightsuperior", "ninesuperior", "zeroinferior", "oneinferior", "twoinferior", "threeinferior", "fourinferior", "fiveinferior", "sixinferior", "seveninferior", "eightinferior", "nineinferior", "centinferior", "dollarinferior", "periodinferior", "commainferior", "Agravesmall", "Aacutesmall", "Acircumflexsmall", "Atildesmall", "Adieresissmall", "Aringsmall", "AEsmall", "Ccedillasmall", "Egravesmall", "Eacutesmall", "Ecircumflexsmall", "Edieresissmall", "Igravesmall", "Iacutesmall", "Icircumflexsmall", "Idieresissmall", "Ethsmall", "Ntildesmall", "Ogravesmall", "Oacutesmall", "Ocircumflexsmall", "Otildesmall", "Odieresissmall", "OEsmall", "Oslashsmall", "Ugravesmall", "Uacutesmall", "Ucircumflexsmall", "Udieresissmall", "Yacutesmall", "Thornsmall", "Ydieresissmall", "001.000", "001.001", "001.002", "001.003", "Black", "Bold", InternalWorkbook.OLD_WORKBOOK_DIR_ENTRY_NAME, "Light", "Medium", "Regular", "Roman", "Semibold"};
    int nextIndexOffset;
    protected String key;
    protected RandomAccessFileOrArray buf;
    private int offSize;
    protected int nameIndexOffset;
    protected int topdictIndexOffset;
    protected int stringIndexOffset;
    protected int gsubrIndexOffset;
    protected int[] nameOffsets;
    protected int[] topdictOffsets;
    protected int[] stringOffsets;
    protected int[] gsubrOffsets;
    protected Font[] fonts;
    protected Object[] args = new Object[48];
    protected int arg_count = 0;
    RandomAccessSourceFactory rasFactory = new RandomAccessSourceFactory();

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/CFFFont$IndexBaseItem.class */
    protected static final class IndexBaseItem extends Item {
    }

    public String getString(char sid) {
        if (sid < standardStrings.length) {
            return standardStrings[sid];
        }
        if (sid >= (standardStrings.length + this.stringOffsets.length) - 1) {
            return null;
        }
        int j = sid - standardStrings.length;
        int p = getPosition();
        seek(this.stringOffsets[j]);
        StringBuffer s = new StringBuffer();
        for (int k = this.stringOffsets[j]; k < this.stringOffsets[j + 1]; k++) {
            s.append(getCard8());
        }
        seek(p);
        return s.toString();
    }

    char getCard8() {
        try {
            byte i = this.buf.readByte();
            return (char) (i & 255);
        } catch (Exception e) {
            throw new IOException("I/O exception.", (Throwable) e);
        }
    }

    char getCard16() {
        try {
            return this.buf.readChar();
        } catch (java.io.IOException e) {
            throw new IOException("I/O exception.", (Throwable) e);
        }
    }

    int getOffset(int offSize) {
        int offset = 0;
        for (int i = 0; i < offSize; i++) {
            offset = (offset * 256) + getCard8();
        }
        return offset;
    }

    void seek(int offset) {
        try {
            this.buf.seek(offset);
        } catch (java.io.IOException e) {
            throw new IOException("I/O exception.", (Throwable) e);
        }
    }

    short getShort() {
        try {
            return this.buf.readShort();
        } catch (java.io.IOException e) {
            throw new IOException("I/O exception.", (Throwable) e);
        }
    }

    int getInt() {
        try {
            return this.buf.readInt();
        } catch (java.io.IOException e) {
            throw new IOException("I/O exception.", (Throwable) e);
        }
    }

    int getPosition() {
        try {
            return (int) this.buf.getPosition();
        } catch (java.io.IOException e) {
            throw new IOException("I/O exception.", (Throwable) e);
        }
    }

    int[] getIndex(int nextIndexOffset) {
        seek(nextIndexOffset);
        int count = getCard16();
        int[] offsets = new int[count + 1];
        if (count == 0) {
            offsets[0] = -1;
            int i = nextIndexOffset + 2;
            return offsets;
        }
        int indexOffSize = getCard8();
        for (int j = 0; j <= count; j++) {
            offsets[j] = ((((nextIndexOffset + 2) + 1) + ((count + 1) * indexOffSize)) - 1) + getOffset(indexOffSize);
        }
        return offsets;
    }

    protected void getDictItem() {
        for (int i = 0; i < this.arg_count; i++) {
            this.args[i] = null;
        }
        this.arg_count = 0;
        this.key = null;
        boolean gotKey = false;
        while (!gotKey) {
            char b0 = getCard8();
            if (b0 == 29) {
                int item = getInt();
                this.args[this.arg_count] = Integer.valueOf(item);
                this.arg_count++;
            } else if (b0 == 28) {
                short item2 = getShort();
                this.args[this.arg_count] = Integer.valueOf(item2);
                this.arg_count++;
            } else if (b0 >= ' ' && b0 <= 246) {
                this.args[this.arg_count] = Integer.valueOf(b0 - 139);
                this.arg_count++;
            } else if (b0 >= 247 && b0 <= 250) {
                char b1 = getCard8();
                short item3 = (short) (((b0 - 247) * 256) + b1 + 108);
                this.args[this.arg_count] = Integer.valueOf(item3);
                this.arg_count++;
            } else if (b0 >= 251 && b0 <= 254) {
                char b12 = getCard8();
                short item4 = (short) ((((-(b0 - 251)) * 256) - b12) - 108);
                this.args[this.arg_count] = Integer.valueOf(item4);
                this.arg_count++;
            } else if (b0 == 30) {
                StringBuilder item5 = new StringBuilder("");
                boolean done = false;
                char buffer = 0;
                byte avail = 0;
                int nibble = 0;
                while (!done) {
                    if (avail == 0) {
                        buffer = getCard8();
                        avail = 2;
                    }
                    if (avail == 1) {
                        nibble = buffer / 16;
                        avail = (byte) (avail - 1);
                    }
                    if (avail == 2) {
                        nibble = buffer % 16;
                        avail = (byte) (avail - 1);
                    }
                    switch (nibble) {
                        case 10:
                            item5.append(".");
                            break;
                        case 11:
                            item5.append("E");
                            break;
                        case 12:
                            item5.append("E-");
                            break;
                        case 13:
                        default:
                            if (nibble >= 0 && nibble <= 9) {
                                item5.append(nibble);
                                break;
                            } else {
                                item5.append("<NIBBLE ERROR: ").append(nibble).append('>');
                                done = true;
                                break;
                            }
                            break;
                        case 14:
                            item5.append("-");
                            break;
                        case 15:
                            done = true;
                            break;
                    }
                }
                this.args[this.arg_count] = item5.toString();
                this.arg_count++;
            } else if (b0 <= 21) {
                gotKey = true;
                if (b0 != '\f') {
                    this.key = operatorNames[b0];
                } else {
                    this.key = operatorNames[' ' + getCard8()];
                }
            }
        }
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/CFFFont$Item.class */
    protected static abstract class Item {
        protected int myOffset = -1;

        protected Item() {
        }

        public void increment(int[] currentOffset) {
            this.myOffset = currentOffset[0];
        }

        public void emit(byte[] buffer) {
        }

        public void xref() {
        }
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/CFFFont$OffsetItem.class */
    protected static abstract class OffsetItem extends Item {
        public int value;

        protected OffsetItem() {
        }

        public void set(int offset) {
            this.value = offset;
        }
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/CFFFont$RangeItem.class */
    protected static final class RangeItem extends Item {
        public int offset;
        public int length;
        private RandomAccessFileOrArray buf;

        public RangeItem(RandomAccessFileOrArray buf, int offset, int length) {
            this.offset = offset;
            this.length = length;
            this.buf = buf;
        }

        @Override // com.itextpdf.io.font.CFFFont.Item
        public void increment(int[] currentOffset) {
            super.increment(currentOffset);
            currentOffset[0] = currentOffset[0] + this.length;
        }

        @Override // com.itextpdf.io.font.CFFFont.Item
        public void emit(byte[] buffer) {
            try {
                this.buf.seek(this.offset);
                for (int i = this.myOffset; i < this.myOffset + this.length; i++) {
                    buffer[i] = this.buf.readByte();
                }
            } catch (java.io.IOException e) {
                throw new IOException("I/O exception.", (Throwable) e);
            }
        }
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/CFFFont$IndexOffsetItem.class */
    protected static final class IndexOffsetItem extends OffsetItem {
        public final int size;

        public IndexOffsetItem(int size, int value) {
            this.size = size;
            this.value = value;
        }

        public IndexOffsetItem(int size) {
            this.size = size;
        }

        @Override // com.itextpdf.io.font.CFFFont.Item
        public void increment(int[] currentOffset) {
            super.increment(currentOffset);
            currentOffset[0] = currentOffset[0] + this.size;
        }

        @Override // com.itextpdf.io.font.CFFFont.Item
        public void emit(byte[] buffer) {
            if (this.size >= 1 && this.size <= 4) {
                for (int i = 0; i < this.size; i++) {
                    buffer[this.myOffset + i] = (byte) ((this.value >>> (((this.size - 1) - i) << 3)) & 255);
                }
            }
        }
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/CFFFont$IndexMarkerItem.class */
    protected static final class IndexMarkerItem extends Item {
        private OffsetItem offItem;
        private IndexBaseItem indexBase;

        public IndexMarkerItem(OffsetItem offItem, IndexBaseItem indexBase) {
            this.offItem = offItem;
            this.indexBase = indexBase;
        }

        @Override // com.itextpdf.io.font.CFFFont.Item
        public void xref() {
            this.offItem.set((this.myOffset - this.indexBase.myOffset) + 1);
        }
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/CFFFont$SubrMarkerItem.class */
    protected static final class SubrMarkerItem extends Item {
        private OffsetItem offItem;
        private IndexBaseItem indexBase;

        public SubrMarkerItem(OffsetItem offItem, IndexBaseItem indexBase) {
            this.offItem = offItem;
            this.indexBase = indexBase;
        }

        @Override // com.itextpdf.io.font.CFFFont.Item
        public void xref() {
            this.offItem.set(this.myOffset - this.indexBase.myOffset);
        }
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/CFFFont$DictOffsetItem.class */
    protected static final class DictOffsetItem extends OffsetItem {
        public final int size = 5;

        @Override // com.itextpdf.io.font.CFFFont.Item
        public void increment(int[] currentOffset) {
            super.increment(currentOffset);
            currentOffset[0] = currentOffset[0] + this.size;
        }

        @Override // com.itextpdf.io.font.CFFFont.Item
        public void emit(byte[] buffer) {
            if (this.size == 5) {
                buffer[this.myOffset] = 29;
                buffer[this.myOffset + 1] = (byte) ((this.value >>> 24) & 255);
                buffer[this.myOffset + 2] = (byte) ((this.value >>> 16) & 255);
                buffer[this.myOffset + 3] = (byte) ((this.value >>> 8) & 255);
                buffer[this.myOffset + 4] = (byte) ((this.value >>> 0) & 255);
            }
        }
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/CFFFont$UInt24Item.class */
    protected static final class UInt24Item extends Item {
        public int value;

        public UInt24Item(int value) {
            this.value = value;
        }

        @Override // com.itextpdf.io.font.CFFFont.Item
        public void increment(int[] currentOffset) {
            super.increment(currentOffset);
            currentOffset[0] = currentOffset[0] + 3;
        }

        @Override // com.itextpdf.io.font.CFFFont.Item
        public void emit(byte[] buffer) {
            buffer[this.myOffset + 0] = (byte) ((this.value >>> 16) & 255);
            buffer[this.myOffset + 1] = (byte) ((this.value >>> 8) & 255);
            buffer[this.myOffset + 2] = (byte) ((this.value >>> 0) & 255);
        }
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/CFFFont$UInt32Item.class */
    protected static final class UInt32Item extends Item {
        public int value;

        public UInt32Item(int value) {
            this.value = value;
        }

        @Override // com.itextpdf.io.font.CFFFont.Item
        public void increment(int[] currentOffset) {
            super.increment(currentOffset);
            currentOffset[0] = currentOffset[0] + 4;
        }

        @Override // com.itextpdf.io.font.CFFFont.Item
        public void emit(byte[] buffer) {
            buffer[this.myOffset + 0] = (byte) ((this.value >>> 24) & 255);
            buffer[this.myOffset + 1] = (byte) ((this.value >>> 16) & 255);
            buffer[this.myOffset + 2] = (byte) ((this.value >>> 8) & 255);
            buffer[this.myOffset + 3] = (byte) ((this.value >>> 0) & 255);
        }
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/CFFFont$UInt16Item.class */
    protected static final class UInt16Item extends Item {
        public char value;

        public UInt16Item(char value) {
            this.value = value;
        }

        @Override // com.itextpdf.io.font.CFFFont.Item
        public void increment(int[] currentOffset) {
            super.increment(currentOffset);
            currentOffset[0] = currentOffset[0] + 2;
        }

        @Override // com.itextpdf.io.font.CFFFont.Item
        public void emit(byte[] buffer) {
            buffer[this.myOffset + 0] = (byte) ((this.value >> '\b') & 255);
            buffer[this.myOffset + 1] = (byte) ((this.value >> 0) & 255);
        }
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/CFFFont$UInt8Item.class */
    protected static final class UInt8Item extends Item {
        public char value;

        public UInt8Item(char value) {
            this.value = value;
        }

        @Override // com.itextpdf.io.font.CFFFont.Item
        public void increment(int[] currentOffset) {
            super.increment(currentOffset);
            currentOffset[0] = currentOffset[0] + 1;
        }

        @Override // com.itextpdf.io.font.CFFFont.Item
        public void emit(byte[] buffer) {
            buffer[this.myOffset + 0] = (byte) (this.value & 255);
        }
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/CFFFont$StringItem.class */
    protected static final class StringItem extends Item {
        public String s;

        public StringItem(String s) {
            this.s = s;
        }

        @Override // com.itextpdf.io.font.CFFFont.Item
        public void increment(int[] currentOffset) {
            super.increment(currentOffset);
            currentOffset[0] = currentOffset[0] + this.s.length();
        }

        @Override // com.itextpdf.io.font.CFFFont.Item
        public void emit(byte[] buffer) {
            for (int i = 0; i < this.s.length(); i++) {
                buffer[this.myOffset + i] = (byte) (this.s.charAt(i) & 255);
            }
        }
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/CFFFont$DictNumberItem.class */
    protected static final class DictNumberItem extends Item {
        public final int value;
        public int size = 5;

        public DictNumberItem(int value) {
            this.value = value;
        }

        @Override // com.itextpdf.io.font.CFFFont.Item
        public void increment(int[] currentOffset) {
            super.increment(currentOffset);
            currentOffset[0] = currentOffset[0] + this.size;
        }

        @Override // com.itextpdf.io.font.CFFFont.Item
        public void emit(byte[] buffer) {
            if (this.size == 5) {
                buffer[this.myOffset] = 29;
                buffer[this.myOffset + 1] = (byte) ((this.value >>> 24) & 255);
                buffer[this.myOffset + 2] = (byte) ((this.value >>> 16) & 255);
                buffer[this.myOffset + 3] = (byte) ((this.value >>> 8) & 255);
                buffer[this.myOffset + 4] = (byte) ((this.value >>> 0) & 255);
            }
        }
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/CFFFont$MarkerItem.class */
    protected static final class MarkerItem extends Item {
        OffsetItem p;

        public MarkerItem(OffsetItem pointerToMarker) {
            this.p = pointerToMarker;
        }

        @Override // com.itextpdf.io.font.CFFFont.Item
        public void xref() {
            this.p.set(this.myOffset);
        }
    }

    protected RangeItem getEntireIndexRange(int indexOffset) {
        seek(indexOffset);
        int count = getCard16();
        if (count == 0) {
            return new RangeItem(this.buf, indexOffset, 2);
        }
        int indexOffSize = getCard8();
        seek(indexOffset + 2 + 1 + (count * indexOffSize));
        int size = getOffset(indexOffSize) - 1;
        return new RangeItem(this.buf, indexOffset, 3 + ((count + 1) * indexOffSize) + size);
    }

    public byte[] getCID(String fontName) {
        byte stringsIndexOffSize;
        int j = 0;
        while (j < this.fonts.length && !fontName.equals(this.fonts[j].name)) {
            j++;
        }
        if (j == this.fonts.length) {
            return null;
        }
        LinkedList<Item> l = new LinkedList<>();
        seek(0);
        getCard8();
        getCard8();
        int hdrSize = getCard8();
        getCard8();
        this.nextIndexOffset = hdrSize;
        l.addLast(new RangeItem(this.buf, 0, hdrSize));
        int nglyphs = -1;
        int nstrings = -1;
        if (!this.fonts[j].isCID) {
            seek(this.fonts[j].charstringsOffset);
            nglyphs = getCard16();
            seek(this.stringIndexOffset);
            nstrings = getCard16() + standardStrings.length;
        }
        l.addLast(new UInt16Item((char) 1));
        l.addLast(new UInt8Item((char) 1));
        l.addLast(new UInt8Item((char) 1));
        l.addLast(new UInt8Item((char) (1 + this.fonts[j].name.length())));
        l.addLast(new StringItem(this.fonts[j].name));
        l.addLast(new UInt16Item((char) 1));
        l.addLast(new UInt8Item((char) 2));
        l.addLast(new UInt16Item((char) 1));
        OffsetItem topdictIndex1Ref = new IndexOffsetItem(2);
        l.addLast(topdictIndex1Ref);
        IndexBaseItem topdictBase = new IndexBaseItem();
        l.addLast(topdictBase);
        OffsetItem charsetRef = new DictOffsetItem();
        OffsetItem charstringsRef = new DictOffsetItem();
        OffsetItem fdarrayRef = new DictOffsetItem();
        OffsetItem fdselectRef = new DictOffsetItem();
        if (!this.fonts[j].isCID) {
            l.addLast(new DictNumberItem(nstrings));
            l.addLast(new DictNumberItem(nstrings + 1));
            l.addLast(new DictNumberItem(0));
            l.addLast(new UInt8Item('\f'));
            l.addLast(new UInt8Item((char) 30));
            l.addLast(new DictNumberItem(nglyphs));
            l.addLast(new UInt8Item('\f'));
            l.addLast(new UInt8Item('\"'));
        }
        l.addLast(fdarrayRef);
        l.addLast(new UInt8Item('\f'));
        l.addLast(new UInt8Item('$'));
        l.addLast(fdselectRef);
        l.addLast(new UInt8Item('\f'));
        l.addLast(new UInt8Item('%'));
        l.addLast(charsetRef);
        l.addLast(new UInt8Item((char) 15));
        l.addLast(charstringsRef);
        l.addLast(new UInt8Item((char) 17));
        seek(this.topdictOffsets[j]);
        while (getPosition() < this.topdictOffsets[j + 1]) {
            int p1 = getPosition();
            getDictItem();
            int p2 = getPosition();
            if (!"Encoding".equals(this.key) && !StandardRoles.PRIVATE.equals(this.key) && !"FDSelect".equals(this.key) && !"FDArray".equals(this.key) && !"charset".equals(this.key) && !"CharStrings".equals(this.key)) {
                l.addLast(new RangeItem(this.buf, p1, p2 - p1));
            }
        }
        l.addLast(new IndexMarkerItem(topdictIndex1Ref, topdictBase));
        if (this.fonts[j].isCID) {
            l.addLast(getEntireIndexRange(this.stringIndexOffset));
        } else {
            String fdFontName = this.fonts[j].name + "-OneRange";
            if (fdFontName.length() > 127) {
                fdFontName = fdFontName.substring(0, 127);
            }
            String extraStrings = "AdobeIdentity" + fdFontName;
            int origStringsLen = this.stringOffsets[this.stringOffsets.length - 1] - this.stringOffsets[0];
            int stringsBaseOffset = this.stringOffsets[0] - 1;
            if (origStringsLen + extraStrings.length() <= 255) {
                stringsIndexOffSize = 1;
            } else if (origStringsLen + extraStrings.length() <= 65535) {
                stringsIndexOffSize = 2;
            } else {
                stringsIndexOffSize = origStringsLen + extraStrings.length() <= 16777215 ? (byte) 3 : (byte) 4;
            }
            l.addLast(new UInt16Item((char) ((this.stringOffsets.length - 1) + 3)));
            l.addLast(new UInt8Item((char) stringsIndexOffSize));
            for (int stringOffset : this.stringOffsets) {
                l.addLast(new IndexOffsetItem(stringsIndexOffSize, stringOffset - stringsBaseOffset));
            }
            int currentStringsOffset = (this.stringOffsets[this.stringOffsets.length - 1] - stringsBaseOffset) + AdobeJpegReader.PREAMBLE.length();
            l.addLast(new IndexOffsetItem(stringsIndexOffSize, currentStringsOffset));
            int currentStringsOffset2 = currentStringsOffset + "Identity".length();
            l.addLast(new IndexOffsetItem(stringsIndexOffSize, currentStringsOffset2));
            l.addLast(new IndexOffsetItem(stringsIndexOffSize, currentStringsOffset2 + fdFontName.length()));
            l.addLast(new RangeItem(this.buf, this.stringOffsets[0], origStringsLen));
            l.addLast(new StringItem(extraStrings));
        }
        l.addLast(getEntireIndexRange(this.gsubrIndexOffset));
        if (!this.fonts[j].isCID) {
            l.addLast(new MarkerItem(fdselectRef));
            l.addLast(new UInt8Item((char) 3));
            l.addLast(new UInt16Item((char) 1));
            l.addLast(new UInt16Item((char) 0));
            l.addLast(new UInt8Item((char) 0));
            l.addLast(new UInt16Item((char) nglyphs));
            l.addLast(new MarkerItem(charsetRef));
            l.addLast(new UInt8Item((char) 2));
            l.addLast(new UInt16Item((char) 1));
            l.addLast(new UInt16Item((char) (nglyphs - 1)));
            l.addLast(new MarkerItem(fdarrayRef));
            l.addLast(new UInt16Item((char) 1));
            l.addLast(new UInt8Item((char) 1));
            l.addLast(new UInt8Item((char) 1));
            OffsetItem privateIndex1Ref = new IndexOffsetItem(1);
            l.addLast(privateIndex1Ref);
            IndexBaseItem privateBase = new IndexBaseItem();
            l.addLast(privateBase);
            l.addLast(new DictNumberItem(this.fonts[j].privateLength));
            OffsetItem privateRef = new DictOffsetItem();
            l.addLast(privateRef);
            l.addLast(new UInt8Item((char) 18));
            l.addLast(new IndexMarkerItem(privateIndex1Ref, privateBase));
            l.addLast(new MarkerItem(privateRef));
            l.addLast(new RangeItem(this.buf, this.fonts[j].privateOffset, this.fonts[j].privateLength));
            if (this.fonts[j].privateSubrs >= 0) {
                l.addLast(getEntireIndexRange(this.fonts[j].privateSubrs));
            }
        }
        l.addLast(new MarkerItem(charstringsRef));
        l.addLast(getEntireIndexRange(this.fonts[j].charstringsOffset));
        int[] currentOffset = {0};
        Iterator<Item> it = l.iterator();
        while (it.hasNext()) {
            Item item = it.next();
            item.increment(currentOffset);
        }
        Iterator<Item> it2 = l.iterator();
        while (it2.hasNext()) {
            Item item2 = it2.next();
            item2.xref();
        }
        int size = currentOffset[0];
        byte[] b = new byte[size];
        Iterator<Item> it3 = l.iterator();
        while (it3.hasNext()) {
            Item item3 = it3.next();
            item3.emit(b);
        }
        return b;
    }

    public boolean isCID(String fontName) {
        for (int j = 0; j < this.fonts.length; j++) {
            if (fontName.equals(this.fonts[j].name)) {
                return this.fonts[j].isCID;
            }
        }
        return false;
    }

    public boolean exists(String fontName) {
        for (int j = 0; j < this.fonts.length; j++) {
            if (fontName.equals(this.fonts[j].name)) {
                return true;
            }
        }
        return false;
    }

    public String[] getNames() {
        String[] names = new String[this.fonts.length];
        for (int i = 0; i < this.fonts.length; i++) {
            names[i] = this.fonts[i].name;
        }
        return names;
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/CFFFont$Font.class */
    protected final class Font {
        public String name;
        public String fullName;
        public int[] fdprivateOffsets;
        public int[] fdprivateLengths;
        public int[] fdprivateSubrs;
        public int nglyphs;
        public int nstrings;
        public int CharsetLength;
        public int[] charstringsOffsets;
        public int[] charset;
        public int[] FDSelect;
        public int FDSelectLength;
        public int FDSelectFormat;
        public int FDArrayCount;
        public int FDArrayOffsize;
        public int[] FDArrayOffsets;
        public int[] PrivateSubrsOffset;
        public int[][] PrivateSubrsOffsetsArray;
        public int[] SubrsOffsets;
        public boolean isCID = false;
        public int privateOffset = -1;
        public int privateLength = -1;
        public int privateSubrs = -1;
        public int charstringsOffset = -1;
        public int encodingOffset = -1;
        public int charsetOffset = -1;
        public int fdarrayOffset = -1;
        public int fdselectOffset = -1;
        public int CharstringType = 2;

        protected Font() {
        }
    }

    public CFFFont(byte[] cff) {
        this.buf = new RandomAccessFileOrArray(this.rasFactory.createSource(cff));
        seek(0);
        getCard8();
        getCard8();
        int hdrSize = getCard8();
        this.offSize = getCard8();
        this.nameIndexOffset = hdrSize;
        this.nameOffsets = getIndex(this.nameIndexOffset);
        this.topdictIndexOffset = this.nameOffsets[this.nameOffsets.length - 1];
        this.topdictOffsets = getIndex(this.topdictIndexOffset);
        this.stringIndexOffset = this.topdictOffsets[this.topdictOffsets.length - 1];
        this.stringOffsets = getIndex(this.stringIndexOffset);
        this.gsubrIndexOffset = this.stringOffsets[this.stringOffsets.length - 1];
        this.gsubrOffsets = getIndex(this.gsubrIndexOffset);
        this.fonts = new Font[this.nameOffsets.length - 1];
        for (int j = 0; j < this.nameOffsets.length - 1; j++) {
            this.fonts[j] = new Font();
            seek(this.nameOffsets[j]);
            this.fonts[j].name = "";
            for (int k = this.nameOffsets[j]; k < this.nameOffsets[j + 1]; k++) {
                StringBuilder sb = new StringBuilder();
                Font font = this.fonts[j];
                font.name = sb.append(font.name).append(getCard8()).toString();
            }
        }
        for (int j2 = 0; j2 < this.topdictOffsets.length - 1; j2++) {
            seek(this.topdictOffsets[j2]);
            while (getPosition() < this.topdictOffsets[j2 + 1]) {
                getDictItem();
                if (this.key == "FullName") {
                    this.fonts[j2].fullName = getString((char) ((Integer) this.args[0]).intValue());
                } else if (this.key == "ROS") {
                    this.fonts[j2].isCID = true;
                } else if (this.key == StandardRoles.PRIVATE) {
                    this.fonts[j2].privateLength = ((Integer) this.args[0]).intValue();
                    this.fonts[j2].privateOffset = ((Integer) this.args[1]).intValue();
                } else if (this.key == "charset") {
                    this.fonts[j2].charsetOffset = ((Integer) this.args[0]).intValue();
                } else if (this.key == "CharStrings") {
                    this.fonts[j2].charstringsOffset = ((Integer) this.args[0]).intValue();
                    int p = getPosition();
                    this.fonts[j2].charstringsOffsets = getIndex(this.fonts[j2].charstringsOffset);
                    seek(p);
                } else if (this.key == "FDArray") {
                    this.fonts[j2].fdarrayOffset = ((Integer) this.args[0]).intValue();
                } else if (this.key == "FDSelect") {
                    this.fonts[j2].fdselectOffset = ((Integer) this.args[0]).intValue();
                } else if (this.key == "CharstringType") {
                    this.fonts[j2].CharstringType = ((Integer) this.args[0]).intValue();
                }
            }
            if (this.fonts[j2].privateOffset >= 0) {
                seek(this.fonts[j2].privateOffset);
                while (getPosition() < this.fonts[j2].privateOffset + this.fonts[j2].privateLength) {
                    getDictItem();
                    if (this.key == "Subrs") {
                        this.fonts[j2].privateSubrs = ((Integer) this.args[0]).intValue() + this.fonts[j2].privateOffset;
                    }
                }
            }
            if (this.fonts[j2].fdarrayOffset >= 0) {
                int[] fdarrayOffsets = getIndex(this.fonts[j2].fdarrayOffset);
                this.fonts[j2].fdprivateOffsets = new int[fdarrayOffsets.length - 1];
                this.fonts[j2].fdprivateLengths = new int[fdarrayOffsets.length - 1];
                for (int k2 = 0; k2 < fdarrayOffsets.length - 1; k2++) {
                    seek(fdarrayOffsets[k2]);
                    while (getPosition() < fdarrayOffsets[k2 + 1]) {
                        getDictItem();
                        if (this.key == StandardRoles.PRIVATE) {
                            this.fonts[j2].fdprivateLengths[k2] = ((Integer) this.args[0]).intValue();
                            this.fonts[j2].fdprivateOffsets[k2] = ((Integer) this.args[1]).intValue();
                        }
                    }
                }
            }
        }
    }

    void ReadEncoding(int nextIndexOffset) {
        seek(nextIndexOffset);
        getCard8();
    }
}

package net.dongliu.apk.parser.struct;

import net.dongliu.apk.parser.utils.Unsigned;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/struct/StringPoolHeader.class */
public class StringPoolHeader extends ChunkHeader {
    private int stringCount;
    private int styleCount;
    public static final int SORTED_FLAG = 1;
    public static final int UTF8_FLAG = 256;
    private long flags;
    private int stringsStart;
    private int stylesStart;

    public StringPoolHeader(int headerSize, long chunkSize) {
        super(1, headerSize, chunkSize);
    }

    public int getStringCount() {
        return this.stringCount;
    }

    public void setStringCount(long stringCount) {
        this.stringCount = Unsigned.ensureUInt(stringCount);
    }

    public int getStyleCount() {
        return this.styleCount;
    }

    public void setStyleCount(long styleCount) {
        this.styleCount = Unsigned.ensureUInt(styleCount);
    }

    public long getFlags() {
        return this.flags;
    }

    public void setFlags(long flags) {
        this.flags = flags;
    }

    public long getStringsStart() {
        return this.stringsStart;
    }

    public void setStringsStart(long stringsStart) {
        this.stringsStart = Unsigned.ensureUInt(stringsStart);
    }

    public long getStylesStart() {
        return this.stylesStart;
    }

    public void setStylesStart(long stylesStart) {
        this.stylesStart = Unsigned.ensureUInt(stylesStart);
    }
}

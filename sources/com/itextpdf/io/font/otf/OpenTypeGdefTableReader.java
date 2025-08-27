package com.itextpdf.io.font.otf;

import com.itextpdf.io.source.RandomAccessFileOrArray;
import java.io.IOException;
import java.io.Serializable;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/otf/OpenTypeGdefTableReader.class */
public class OpenTypeGdefTableReader implements Serializable {
    private static final long serialVersionUID = 1564505797329158035L;
    private final int FLAG_IGNORE_BASE = 2;
    private final int FLAG_IGNORE_LIGATURE = 4;
    private final int FLAG_IGNORE_MARK = 8;
    private final int tableLocation;
    private final RandomAccessFileOrArray rf;
    private OtfClass glyphClass;
    private OtfClass markAttachmentClass;

    public OpenTypeGdefTableReader(RandomAccessFileOrArray rf, int tableLocation) {
        this.rf = rf;
        this.tableLocation = tableLocation;
    }

    public void readTable() throws IOException {
        if (this.tableLocation > 0) {
            this.rf.seek(this.tableLocation);
            this.rf.readUnsignedInt();
            int glyphClassDefOffset = this.rf.readUnsignedShort();
            this.rf.readUnsignedShort();
            this.rf.readUnsignedShort();
            int markAttachClassDefOffset = this.rf.readUnsignedShort();
            if (glyphClassDefOffset > 0) {
                this.glyphClass = OtfClass.create(this.rf, glyphClassDefOffset + this.tableLocation);
            }
            if (markAttachClassDefOffset > 0) {
                this.markAttachmentClass = OtfClass.create(this.rf, markAttachClassDefOffset + this.tableLocation);
            }
        }
    }

    public boolean isSkip(int glyph, int flag) {
        if (this.glyphClass != null && (flag & 14) != 0) {
            int cla = this.glyphClass.getOtfClass(glyph);
            if (cla == 1 && (flag & 2) != 0) {
                return true;
            }
            if (cla == 3 && (flag & 8) != 0) {
                return true;
            }
            if (cla == 2 && (flag & 4) != 0) {
                return true;
            }
        }
        return this.markAttachmentClass != null && this.markAttachmentClass.getOtfClass(glyph) > 0 && (flag >> 8) > 0 && this.markAttachmentClass.getOtfClass(glyph) != (flag >> 8);
    }

    public OtfClass getGlyphClassTable() {
        return this.glyphClass;
    }
}

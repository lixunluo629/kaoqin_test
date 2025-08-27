package com.itextpdf.io.font.otf;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.io.source.RandomAccessFileOrArray;
import com.itextpdf.io.util.IntHashtable;
import com.itextpdf.io.util.MessageFormatUtil;
import java.io.IOException;
import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/otf/OtfClass.class */
public class OtfClass implements Serializable {
    public static final int GLYPH_BASE = 1;
    public static final int GLYPH_LIGATURE = 2;
    public static final int GLYPH_MARK = 3;
    private static final long serialVersionUID = -7584495836452964728L;
    private IntHashtable mapClass = new IntHashtable();

    private OtfClass(RandomAccessFileOrArray rf, int classLocation) throws IOException {
        rf.seek(classLocation);
        int classFormat = rf.readUnsignedShort();
        if (classFormat == 1) {
            int startGlyph = rf.readUnsignedShort();
            int glyphCount = rf.readUnsignedShort();
            int endGlyph = startGlyph + glyphCount;
            for (int k = startGlyph; k < endGlyph; k++) {
                int cl = rf.readUnsignedShort();
                this.mapClass.put(k, cl);
            }
            return;
        }
        if (classFormat == 2) {
            int classRangeCount = rf.readUnsignedShort();
            for (int k2 = 0; k2 < classRangeCount; k2++) {
                int glyphEnd = rf.readUnsignedShort();
                int cl2 = rf.readUnsignedShort();
                for (int glyphStart = rf.readUnsignedShort(); glyphStart <= glyphEnd; glyphStart++) {
                    this.mapClass.put(glyphStart, cl2);
                }
            }
            return;
        }
        throw new IOException("Invalid class format " + classFormat);
    }

    public static OtfClass create(RandomAccessFileOrArray rf, int classLocation) {
        OtfClass otfClass;
        try {
            otfClass = new OtfClass(rf, classLocation);
        } catch (IOException e) {
            Logger logger = LoggerFactory.getLogger((Class<?>) OtfClass.class);
            logger.error(MessageFormatUtil.format(LogMessageConstant.OPENTYPE_GDEF_TABLE_ERROR, e.getMessage()));
            otfClass = null;
        }
        return otfClass;
    }

    public int getOtfClass(int glyph) {
        return this.mapClass.get(glyph);
    }

    public boolean isMarkOtfClass(int glyph) {
        return hasClass(glyph) && getOtfClass(glyph) == 3;
    }

    public boolean hasClass(int glyph) {
        return this.mapClass.containsKey(glyph);
    }

    public int getOtfClass(int glyph, boolean strict) {
        if (strict) {
            if (this.mapClass.containsKey(glyph)) {
                return this.mapClass.get(glyph);
            }
            return -1;
        }
        return this.mapClass.get(glyph);
    }
}

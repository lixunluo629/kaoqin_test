package com.itextpdf.io.font;

import com.itextpdf.io.source.RandomAccessFileOrArray;
import com.itextpdf.io.source.RandomAccessSourceFactory;
import com.itextpdf.io.util.FileUtil;
import java.io.IOException;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/TrueTypeCollection.class */
public class TrueTypeCollection {
    protected RandomAccessFileOrArray raf;
    private String ttcPath;
    private byte[] ttc;
    private int TTCSize = 0;
    private boolean cached = true;

    public TrueTypeCollection(byte[] ttc) throws IOException {
        this.raf = new RandomAccessFileOrArray(new RandomAccessSourceFactory().createSource(ttc));
        this.ttc = ttc;
        initFontSize();
    }

    public TrueTypeCollection(String ttcPath) throws IOException {
        if (!FileUtil.fileExists(ttcPath)) {
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.FontFile1NotFound).setMessageParams(ttcPath);
        }
        this.raf = new RandomAccessFileOrArray(new RandomAccessSourceFactory().createBestSource(ttcPath));
        this.ttcPath = ttcPath;
        initFontSize();
    }

    public FontProgram getFontByTccIndex(int ttcIndex) throws IOException {
        if (ttcIndex > this.TTCSize - 1) {
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.TtcIndexDoesNotExistInThisTtcFile);
        }
        if (this.ttcPath != null) {
            return FontProgramFactory.createFont(this.ttcPath, ttcIndex, this.cached);
        }
        return FontProgramFactory.createFont(this.ttc, ttcIndex, this.cached);
    }

    public int getTTCSize() {
        return this.TTCSize;
    }

    public boolean isCached() {
        return this.cached;
    }

    public void setCached(boolean cached) {
        this.cached = cached;
    }

    private void initFontSize() throws IOException {
        String mainTag = this.raf.readString(4, "Cp1252");
        if (!mainTag.equals("ttcf")) {
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.InvalidTtcFile);
        }
        this.raf.skipBytes(4);
        this.TTCSize = this.raf.readInt();
    }
}

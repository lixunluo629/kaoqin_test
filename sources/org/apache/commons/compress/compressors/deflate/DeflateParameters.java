package org.apache.commons.compress.compressors.deflate;

/* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/compressors/deflate/DeflateParameters.class */
public class DeflateParameters {
    private boolean zlibHeader = true;
    private int compressionLevel = -1;

    public boolean withZlibHeader() {
        return this.zlibHeader;
    }

    public void setWithZlibHeader(boolean zlibHeader) {
        this.zlibHeader = zlibHeader;
    }

    public int getCompressionLevel() {
        return this.compressionLevel;
    }

    public void setCompressionLevel(int compressionLevel) {
        if (compressionLevel < -1 || compressionLevel > 9) {
            throw new IllegalArgumentException("Invalid Deflate compression level: " + compressionLevel);
        }
        this.compressionLevel = compressionLevel;
    }
}

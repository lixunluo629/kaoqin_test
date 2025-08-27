package com.itextpdf.io.image;

import java.net.URL;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/image/BmpImageData.class */
public class BmpImageData extends RawImageData {
    private int size;
    private boolean noHeader;

    protected BmpImageData(URL url, boolean noHeader, int size) {
        super(url, ImageType.BMP);
        this.noHeader = noHeader;
        this.size = size;
    }

    protected BmpImageData(byte[] bytes, boolean noHeader, int size) {
        super(bytes, ImageType.BMP);
        this.noHeader = noHeader;
        this.size = size;
    }

    public int getSize() {
        return this.size;
    }

    public boolean isNoHeader() {
        return this.noHeader;
    }
}

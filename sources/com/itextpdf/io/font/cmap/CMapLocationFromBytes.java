package com.itextpdf.io.font.cmap;

import com.itextpdf.io.source.PdfTokenizer;
import com.itextpdf.io.source.RandomAccessFileOrArray;
import com.itextpdf.io.source.RandomAccessSourceFactory;
import java.io.IOException;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/cmap/CMapLocationFromBytes.class */
public class CMapLocationFromBytes implements ICMapLocation {
    private byte[] data;

    public CMapLocationFromBytes(byte[] data) {
        this.data = data;
    }

    @Override // com.itextpdf.io.font.cmap.ICMapLocation
    public PdfTokenizer getLocation(String location) throws IOException {
        return new PdfTokenizer(new RandomAccessFileOrArray(new RandomAccessSourceFactory().createSource(this.data)));
    }
}

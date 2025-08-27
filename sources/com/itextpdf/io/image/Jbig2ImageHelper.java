package com.itextpdf.io.image;

import com.itextpdf.io.codec.Jbig2SegmentReader;
import com.itextpdf.io.source.IRandomAccessSource;
import com.itextpdf.io.source.RandomAccessFileOrArray;
import com.itextpdf.io.source.RandomAccessSourceFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/image/Jbig2ImageHelper.class */
class Jbig2ImageHelper {
    private byte[] globals;

    Jbig2ImageHelper() {
    }

    public static byte[] getGlobalSegment(RandomAccessFileOrArray ra) {
        try {
            Jbig2SegmentReader sr = new Jbig2SegmentReader(ra);
            sr.read();
            return sr.getGlobal(true);
        } catch (Exception e) {
            return null;
        }
    }

    public static void processImage(ImageData jbig2) {
        if (jbig2.getOriginalType() != ImageType.JBIG2) {
            throw new IllegalArgumentException("JBIG2 image expected");
        }
        Jbig2ImageData image = (Jbig2ImageData) jbig2;
        try {
            if (image.getData() == null) {
                image.loadData();
            }
            IRandomAccessSource ras = new RandomAccessSourceFactory().createSource(image.getData());
            RandomAccessFileOrArray raf = new RandomAccessFileOrArray(ras);
            Jbig2SegmentReader sr = new Jbig2SegmentReader(raf);
            sr.read();
            Jbig2SegmentReader.Jbig2Page p = sr.getPage(image.getPage());
            raf.close();
            image.setHeight(p.pageBitmapHeight);
            image.setWidth(p.pageBitmapWidth);
            image.setBpc(1);
            image.setColorSpace(1);
            Object globals = sr.getGlobal(true);
            if (globals != null) {
                Map<String, Object> decodeParms = new HashMap<>();
                decodeParms.put("JBIG2Globals", globals);
                image.decodeParms = decodeParms;
            }
            image.setFilter("JBIG2Decode");
            image.setColorSpace(1);
            image.setBpc(1);
            image.data = p.getData(true);
        } catch (IOException e) {
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.Jbig2ImageException, (Throwable) e);
        }
    }
}

package com.itextpdf.io.image;

import com.itextpdf.io.IOException;
import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.io.codec.Jbig2SegmentReader;
import com.itextpdf.io.source.IRandomAccessSource;
import com.itextpdf.io.source.RandomAccessFileOrArray;
import com.itextpdf.io.source.RandomAccessSourceFactory;
import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/image/Jbig2ImageData.class */
public class Jbig2ImageData extends ImageData {
    private int page;

    protected Jbig2ImageData(URL url, int page) {
        super(url, ImageType.JBIG2);
        this.page = page;
    }

    protected Jbig2ImageData(byte[] bytes, int page) {
        super(bytes, ImageType.JBIG2);
        this.page = page;
    }

    public int getPage() {
        return this.page;
    }

    public static int getNumberOfPages(byte[] bytes) {
        IRandomAccessSource ras = new RandomAccessSourceFactory().createSource(bytes);
        return getNumberOfPages(new RandomAccessFileOrArray(ras));
    }

    public static int getNumberOfPages(RandomAccessFileOrArray raf) {
        try {
            Jbig2SegmentReader sr = new Jbig2SegmentReader(raf);
            sr.read();
            return sr.numberOfPages();
        } catch (Exception e) {
            throw new IOException(IOException.Jbig2ImageException, (Throwable) e);
        }
    }

    @Override // com.itextpdf.io.image.ImageData
    public boolean canImageBeInline() {
        Logger logger = LoggerFactory.getLogger((Class<?>) ImageData.class);
        logger.warn(LogMessageConstant.IMAGE_HAS_JBIG2DECODE_FILTER);
        return false;
    }
}

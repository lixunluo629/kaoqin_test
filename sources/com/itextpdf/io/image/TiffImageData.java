package com.itextpdf.io.image;

import com.itextpdf.io.IOException;
import com.itextpdf.io.codec.TIFFDirectory;
import com.itextpdf.io.source.IRandomAccessSource;
import com.itextpdf.io.source.RandomAccessFileOrArray;
import com.itextpdf.io.source.RandomAccessSourceFactory;
import java.net.URL;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/image/TiffImageData.class */
public class TiffImageData extends RawImageData {
    private boolean recoverFromImageError;
    private int page;
    private boolean direct;

    protected TiffImageData(URL url, boolean recoverFromImageError, int page, boolean direct) {
        super(url, ImageType.TIFF);
        this.recoverFromImageError = recoverFromImageError;
        this.page = page;
        this.direct = direct;
    }

    protected TiffImageData(byte[] bytes, boolean recoverFromImageError, int page, boolean direct) {
        super(bytes, ImageType.TIFF);
        this.recoverFromImageError = recoverFromImageError;
        this.page = page;
        this.direct = direct;
    }

    private static ImageData getImage(URL url, boolean recoverFromImageError, int page, boolean direct) {
        return new TiffImageData(url, recoverFromImageError, page, direct);
    }

    private static ImageData getImage(byte[] bytes, boolean recoverFromImageError, int page, boolean direct) {
        return new TiffImageData(bytes, recoverFromImageError, page, direct);
    }

    public static int getNumberOfPages(RandomAccessFileOrArray raf) {
        try {
            return TIFFDirectory.getNumDirectories(raf);
        } catch (Exception e) {
            throw new IOException(IOException.TiffImageException, (Throwable) e);
        }
    }

    public static int getNumberOfPages(byte[] bytes) {
        IRandomAccessSource ras = new RandomAccessSourceFactory().createSource(bytes);
        return getNumberOfPages(new RandomAccessFileOrArray(ras));
    }

    public boolean isRecoverFromImageError() {
        return this.recoverFromImageError;
    }

    public int getPage() {
        return this.page;
    }

    public boolean isDirect() {
        return this.direct;
    }

    public void setOriginalType(ImageType originalType) {
        this.originalType = originalType;
    }
}

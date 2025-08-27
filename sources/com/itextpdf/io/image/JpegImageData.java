package com.itextpdf.io.image;

import java.net.URL;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/image/JpegImageData.class */
public class JpegImageData extends ImageData {
    protected JpegImageData(URL url) {
        super(url, ImageType.JPEG);
    }

    protected JpegImageData(byte[] bytes) {
        super(bytes, ImageType.JPEG);
    }
}

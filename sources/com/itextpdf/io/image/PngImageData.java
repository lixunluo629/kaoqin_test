package com.itextpdf.io.image;

import java.net.URL;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/image/PngImageData.class */
public class PngImageData extends RawImageData {
    protected PngImageData(byte[] bytes) {
        super(bytes, ImageType.PNG);
    }

    protected PngImageData(URL url) {
        super(url, ImageType.PNG);
    }
}

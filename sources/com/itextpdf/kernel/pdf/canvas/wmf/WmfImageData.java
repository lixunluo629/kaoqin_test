package com.itextpdf.kernel.pdf.canvas.wmf;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageType;
import com.itextpdf.io.util.UrlUtil;
import com.itextpdf.kernel.PdfException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/wmf/WmfImageData.class */
public class WmfImageData extends ImageData {
    private static final byte[] wmf = {-41, -51};

    public WmfImageData(String fileName) throws MalformedURLException {
        this(UrlUtil.toURL(fileName));
    }

    public WmfImageData(URL url) throws IOException {
        super(url, ImageType.WMF);
        byte[] imageType = readImageType(url);
        if (!imageTypeIs(imageType, wmf)) {
            throw new PdfException(PdfException.NotAWmfImage);
        }
    }

    public WmfImageData(byte[] bytes) {
        super(bytes, ImageType.WMF);
        byte[] imageType = readImageType(bytes);
        if (!imageTypeIs(imageType, wmf)) {
            throw new PdfException(PdfException.NotAWmfImage);
        }
    }

    private static boolean imageTypeIs(byte[] imageType, byte[] compareWith) {
        for (int i = 0; i < compareWith.length; i++) {
            if (imageType[i] != compareWith[i]) {
                return false;
            }
        }
        return true;
    }

    private static byte[] readImageType(URL source) throws IOException {
        InputStream is = null;
        try {
            try {
                is = source.openStream();
                byte[] bytes = new byte[8];
                is.read(bytes);
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                    }
                }
                return bytes;
            } catch (Throwable th) {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e2) {
                    }
                }
                throw th;
            }
        } catch (IOException e3) {
            throw new PdfException("I/O exception.", (Throwable) e3);
        }
    }

    private static byte[] readImageType(byte[] bytes) {
        return Arrays.copyOfRange(bytes, 0, 8);
    }
}

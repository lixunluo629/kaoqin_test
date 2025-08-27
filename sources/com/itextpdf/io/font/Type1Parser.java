package com.itextpdf.io.font;

import com.itextpdf.io.font.constants.FontResources;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.source.RandomAccessFileOrArray;
import com.itextpdf.io.source.RandomAccessSourceFactory;
import com.itextpdf.io.util.ResourceUtil;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/Type1Parser.class */
class Type1Parser implements Serializable {
    private static final long serialVersionUID = -8484541242371901414L;
    private static final String AFM_HEADER = "StartFontMetrics";
    private String afmPath;
    private String pfbPath;
    private byte[] pfbData;
    private byte[] afmData;
    private boolean isBuiltInFont;
    private RandomAccessSourceFactory sourceFactory = new RandomAccessSourceFactory();

    public Type1Parser(String metricsPath, String binaryPath, byte[] afm, byte[] pfb) {
        this.afmData = afm;
        this.pfbData = pfb;
        this.afmPath = metricsPath;
        this.pfbPath = binaryPath;
    }

    public RandomAccessFileOrArray getMetricsFile() throws IOException {
        this.isBuiltInFont = false;
        if (StandardFonts.isStandardFont(this.afmPath)) {
            this.isBuiltInFont = true;
            byte[] buf = new byte[1024];
            InputStream resource = null;
            try {
                String resourcePath = FontResources.AFMS + this.afmPath + ".afm";
                InputStream resource2 = ResourceUtil.getResourceStream(resourcePath);
                if (resource2 == null) {
                    throw new com.itextpdf.io.IOException("{0} was not found as resource.").setMessageParams(resourcePath);
                }
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                while (true) {
                    int read = resource2.read(buf);
                    if (read < 0) {
                        break;
                    }
                    stream.write(buf, 0, read);
                }
                byte[] buf2 = stream.toByteArray();
                if (resource2 != null) {
                    try {
                        resource2.close();
                    } catch (Exception e) {
                    }
                }
                return new RandomAccessFileOrArray(this.sourceFactory.createSource(buf2));
            } catch (Throwable th) {
                if (0 != 0) {
                    try {
                        resource.close();
                    } catch (Exception e2) {
                    }
                }
                throw th;
            }
        }
        if (this.afmPath != null) {
            if (this.afmPath.toLowerCase().endsWith(".afm")) {
                return new RandomAccessFileOrArray(this.sourceFactory.createBestSource(this.afmPath));
            }
            if (!this.afmPath.toLowerCase().endsWith(".pfm")) {
                throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException._1IsNotAnAfmOrPfmFontFile).setMessageParams(this.afmPath);
            }
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            RandomAccessFileOrArray rf = new RandomAccessFileOrArray(this.sourceFactory.createBestSource(this.afmPath));
            Pfm2afm.convert(rf, ba);
            rf.close();
            return new RandomAccessFileOrArray(this.sourceFactory.createSource(ba.toByteArray()));
        }
        if (this.afmData != null) {
            RandomAccessFileOrArray rf2 = new RandomAccessFileOrArray(this.sourceFactory.createSource(this.afmData));
            if (isAfmFile(rf2)) {
                return rf2;
            }
            ByteArrayOutputStream ba2 = new ByteArrayOutputStream();
            try {
                try {
                    Pfm2afm.convert(rf2, ba2);
                    rf2.close();
                    return new RandomAccessFileOrArray(this.sourceFactory.createSource(ba2.toByteArray()));
                } catch (Exception e3) {
                    throw new com.itextpdf.io.IOException("Invalid afm or pfm font file.");
                }
            } catch (Throwable th2) {
                rf2.close();
                throw th2;
            }
        }
        throw new com.itextpdf.io.IOException("Invalid afm or pfm font file.");
    }

    public RandomAccessFileOrArray getPostscriptBinary() throws IOException {
        if (this.pfbData != null) {
            return new RandomAccessFileOrArray(this.sourceFactory.createSource(this.pfbData));
        }
        if (this.pfbPath != null && this.pfbPath.toLowerCase().endsWith(".pfb")) {
            return new RandomAccessFileOrArray(this.sourceFactory.createBestSource(this.pfbPath));
        }
        this.pfbPath = this.afmPath.substring(0, this.afmPath.length() - 3) + "pfb";
        return new RandomAccessFileOrArray(this.sourceFactory.createBestSource(this.pfbPath));
    }

    public boolean isBuiltInFont() {
        return this.isBuiltInFont;
    }

    public String getAfmPath() {
        return this.afmPath;
    }

    private boolean isAfmFile(RandomAccessFileOrArray raf) throws IOException {
        StringBuilder builder = new StringBuilder(AFM_HEADER.length());
        for (int i = 0; i < AFM_HEADER.length(); i++) {
            try {
                builder.append((char) raf.readByte());
            } catch (EOFException e) {
                raf.seek(0L);
                return false;
            }
        }
        raf.seek(0L);
        return AFM_HEADER.equals(builder.toString());
    }
}

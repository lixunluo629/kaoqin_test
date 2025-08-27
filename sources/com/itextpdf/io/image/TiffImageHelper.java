package com.itextpdf.io.image;

import com.itextpdf.io.codec.CCITTG4Encoder;
import com.itextpdf.io.codec.TIFFConstants;
import com.itextpdf.io.codec.TIFFDirectory;
import com.itextpdf.io.codec.TIFFFaxDecoder;
import com.itextpdf.io.codec.TIFFField;
import com.itextpdf.io.codec.TIFFLZWDecoder;
import com.itextpdf.io.colors.IccProfile;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.io.source.DeflaterOutputStream;
import com.itextpdf.io.source.IRandomAccessSource;
import com.itextpdf.io.source.RandomAccessFileOrArray;
import com.itextpdf.io.source.RandomAccessSourceFactory;
import com.itextpdf.io.util.FilterUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/image/TiffImageHelper.class */
class TiffImageHelper {
    TiffImageHelper() {
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/image/TiffImageHelper$TiffParameters.class */
    private static class TiffParameters {
        TiffImageData image;
        boolean jpegProcessing;
        Map<String, Object> additional;

        TiffParameters(TiffImageData image) {
            this.image = image;
        }
    }

    public static void processImage(ImageData image) {
        if (image.getOriginalType() != ImageType.TIFF) {
            throw new IllegalArgumentException("TIFF image expected");
        }
        try {
            if (image.getData() == null) {
                image.loadData();
            }
            IRandomAccessSource ras = new RandomAccessSourceFactory().createSource(image.getData());
            RandomAccessFileOrArray raf = new RandomAccessFileOrArray(ras);
            TiffParameters tiff = new TiffParameters((TiffImageData) image);
            processTiffImage(raf, tiff);
            raf.close();
            if (!tiff.jpegProcessing) {
                RawImageHelper.updateImageAttributes(tiff.image, tiff.additional);
            }
        } catch (IOException e) {
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.TiffImageException, (Throwable) e);
        }
    }

    private static void processTiffImage(RandomAccessFileOrArray s, TiffParameters tiff) {
        TIFFDirectory dir;
        boolean recoverFromImageError = tiff.image.isRecoverFromImageError();
        int page = tiff.image.getPage();
        boolean direct = tiff.image.isDirect();
        if (page < 1) {
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.PageNumberMustBeGtEq1);
        }
        try {
            dir = new TIFFDirectory(s, page - 1);
        } catch (Exception e) {
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.CannotReadTiffImage);
        }
        if (dir.isTagPresent(322)) {
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.TilesAreNotSupported);
        }
        int compression = 1;
        if (dir.isTagPresent(259)) {
            compression = (int) dir.getFieldAsLong(259);
        }
        switch (compression) {
            case 2:
            case 3:
            case 4:
            case 32771:
                float rotation = 0.0f;
                if (dir.isTagPresent(274)) {
                    int rot = (int) dir.getFieldAsLong(274);
                    if (rot == 3 || rot == 4) {
                        rotation = 3.1415927f;
                    } else if (rot == 5 || rot == 8) {
                        rotation = 1.5707964f;
                    } else if (rot == 6 || rot == 7) {
                        rotation = -1.5707964f;
                    }
                }
                long tiffT4Options = 0;
                long tiffT6Options = 0;
                int fillOrder = 1;
                int h = (int) dir.getFieldAsLong(257);
                int w = (int) dir.getFieldAsLong(256);
                float XYRatio = 0.0f;
                int resolutionUnit = 2;
                if (dir.isTagPresent(296)) {
                    resolutionUnit = (int) dir.getFieldAsLong(296);
                }
                int dpiX = getDpi(dir.getField(282), resolutionUnit);
                int dpiY = getDpi(dir.getField(283), resolutionUnit);
                if (resolutionUnit == 1) {
                    if (dpiY != 0) {
                        XYRatio = dpiX / dpiY;
                    }
                    dpiX = 0;
                    dpiY = 0;
                }
                int rowsStrip = h;
                if (dir.isTagPresent(278)) {
                    rowsStrip = (int) dir.getFieldAsLong(278);
                }
                if (rowsStrip <= 0 || rowsStrip > h) {
                    rowsStrip = h;
                }
                long[] offset = getArrayLongShort(dir, 273);
                long[] size = getArrayLongShort(dir, 279);
                if ((size == null || (size.length == 1 && (size[0] == 0 || size[0] + offset[0] > s.length()))) && h == rowsStrip) {
                    size = new long[]{s.length() - ((int) offset[0])};
                }
                TIFFField fillOrderField = dir.getField(266);
                if (fillOrderField != null) {
                    fillOrder = fillOrderField.getAsInt(0);
                }
                boolean z = fillOrder == 2;
                int parameters = 0;
                if (dir.isTagPresent(262)) {
                    long photo = dir.getFieldAsLong(262);
                    if (photo == 1) {
                        parameters = 0 | 1;
                    }
                }
                int imagecomp = 0;
                switch (compression) {
                    case 2:
                    case 32771:
                        imagecomp = 257;
                        parameters |= 10;
                        break;
                    case 3:
                        imagecomp = 257;
                        parameters |= 12;
                        TIFFField t4OptionsField = dir.getField(292);
                        if (t4OptionsField != null) {
                            tiffT4Options = t4OptionsField.getAsLong(0);
                            if ((tiffT4Options & 1) != 0) {
                                imagecomp = 258;
                            }
                            if ((tiffT4Options & 4) != 0) {
                                parameters |= 2;
                                break;
                            }
                        }
                        break;
                    case 4:
                        imagecomp = 256;
                        TIFFField t6OptionsField = dir.getField(TIFFConstants.TIFFTAG_GROUP4OPTIONS);
                        if (t6OptionsField != null) {
                            tiffT6Options = t6OptionsField.getAsLong(0);
                            break;
                        }
                        break;
                }
                if (direct && rowsStrip == h) {
                    byte[] im = new byte[(int) size[0]];
                    s.seek(offset[0]);
                    s.readFully(im);
                    RawImageHelper.updateRawImageParameters(tiff.image, w, h, false, imagecomp, parameters, im, null);
                    tiff.image.setInverted(true);
                } else {
                    int rowsLeft = h;
                    CCITTG4Encoder g4 = new CCITTG4Encoder(w);
                    for (int k = 0; k < offset.length; k++) {
                        byte[] im2 = new byte[(int) size[k]];
                        s.seek(offset[k]);
                        s.readFully(im2);
                        int height = Math.min(rowsStrip, rowsLeft);
                        TIFFFaxDecoder decoder = new TIFFFaxDecoder(fillOrder, w, height);
                        decoder.setRecoverFromImageError(recoverFromImageError);
                        byte[] outBuf = new byte[((w + 7) / 8) * height];
                        switch (compression) {
                            case 2:
                            case 32771:
                                decoder.decode1D(outBuf, im2, 0, height);
                                g4.fax4Encode(outBuf, height);
                                continue;
                                rowsLeft -= rowsStrip;
                            case 3:
                                try {
                                    decoder.decode2D(outBuf, im2, 0, height, tiffT4Options);
                                } catch (RuntimeException e2) {
                                    tiffT4Options ^= 4;
                                    try {
                                        decoder.decode2D(outBuf, im2, 0, height, tiffT4Options);
                                    } catch (RuntimeException e3) {
                                        if (!recoverFromImageError) {
                                            throw e2;
                                        }
                                        if (rowsStrip == 1) {
                                            throw e2;
                                        }
                                        byte[] im3 = new byte[(int) size[0]];
                                        s.seek(offset[0]);
                                        s.readFully(im3);
                                        RawImageHelper.updateRawImageParameters(tiff.image, w, h, false, imagecomp, parameters, im3, null);
                                        tiff.image.setInverted(true);
                                        tiff.image.setDpi(dpiX, dpiY);
                                        tiff.image.setXYRatio(XYRatio);
                                        if (rotation != 0.0f) {
                                            tiff.image.setRotation(rotation);
                                            return;
                                        }
                                        return;
                                    }
                                }
                                g4.fax4Encode(outBuf, height);
                                continue;
                                rowsLeft -= rowsStrip;
                            case 4:
                                try {
                                    decoder.decodeT6(outBuf, im2, 0, height, tiffT6Options);
                                } catch (com.itextpdf.io.IOException e4) {
                                    if (!recoverFromImageError) {
                                        throw e4;
                                    }
                                }
                                g4.fax4Encode(outBuf, height);
                                continue;
                                rowsLeft -= rowsStrip;
                            default:
                                rowsLeft -= rowsStrip;
                        }
                    }
                    byte[] g4pic = g4.close();
                    RawImageHelper.updateRawImageParameters(tiff.image, w, h, false, 256, parameters & 1, g4pic, null);
                }
                tiff.image.setDpi(dpiX, dpiY);
                if (dir.isTagPresent(34675)) {
                    try {
                        TIFFField fd = dir.getField(34675);
                        IccProfile icc_prof = IccProfile.getInstance(fd.getAsBytes());
                        if (icc_prof.getNumComponents() == 1) {
                            tiff.image.setProfile(icc_prof);
                        }
                    } catch (RuntimeException e5) {
                    }
                }
                if (rotation != 0.0f) {
                    tiff.image.setRotation(rotation);
                }
                return;
            default:
                processTiffImageColor(dir, s, tiff);
                return;
        }
        throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.CannotReadTiffImage);
    }

    private static void processTiffImageColor(TIFFDirectory dir, RandomAccessFileOrArray s, TiffParameters tiff) {
        TIFFField predictorField;
        try {
            int compression = 1;
            if (dir.isTagPresent(259)) {
                compression = (int) dir.getFieldAsLong(259);
            }
            int predictor = 1;
            TIFFLZWDecoder lzwDecoder = null;
            switch (compression) {
                case 1:
                case 5:
                case 6:
                case 7:
                case 8:
                case 32773:
                case TIFFConstants.COMPRESSION_DEFLATE /* 32946 */:
                    int photometric = (int) dir.getFieldAsLong(262);
                    switch (photometric) {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                        case 5:
                            break;
                        case 4:
                        default:
                            if (compression != 6 && compression != 7) {
                                throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.Photometric1IsNotSupported).setMessageParams(Integer.valueOf(photometric));
                            }
                            break;
                    }
                    float rotation = 0.0f;
                    if (dir.isTagPresent(274)) {
                        int rot = (int) dir.getFieldAsLong(274);
                        if (rot == 3 || rot == 4) {
                            rotation = 3.1415927f;
                        } else if (rot == 5 || rot == 8) {
                            rotation = 1.5707964f;
                        } else if (rot == 6 || rot == 7) {
                            rotation = -1.5707964f;
                        }
                    }
                    if (dir.isTagPresent(284) && dir.getFieldAsLong(284) == 2) {
                        throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.PlanarImagesAreNotSupported);
                    }
                    int extraSamples = 0;
                    if (dir.isTagPresent(338)) {
                        extraSamples = 1;
                    }
                    int samplePerPixel = 1;
                    if (dir.isTagPresent(277)) {
                        samplePerPixel = (int) dir.getFieldAsLong(277);
                    }
                    int bitsPerSample = 1;
                    if (dir.isTagPresent(258)) {
                        bitsPerSample = (int) dir.getFieldAsLong(258);
                    }
                    switch (bitsPerSample) {
                        case 1:
                        case 2:
                        case 4:
                        case 8:
                            int h = (int) dir.getFieldAsLong(257);
                            int w = (int) dir.getFieldAsLong(256);
                            int resolutionUnit = 2;
                            if (dir.isTagPresent(296)) {
                                resolutionUnit = (int) dir.getFieldAsLong(296);
                            }
                            int dpiX = getDpi(dir.getField(282), resolutionUnit);
                            int dpiY = getDpi(dir.getField(283), resolutionUnit);
                            int fillOrder = 1;
                            TIFFField fillOrderField = dir.getField(266);
                            if (fillOrderField != null) {
                                fillOrder = fillOrderField.getAsInt(0);
                            }
                            boolean reverse = fillOrder == 2;
                            int rowsStrip = h;
                            if (dir.isTagPresent(278)) {
                                rowsStrip = (int) dir.getFieldAsLong(278);
                            }
                            if (rowsStrip <= 0 || rowsStrip > h) {
                                rowsStrip = h;
                            }
                            long[] offset = getArrayLongShort(dir, 273);
                            long[] size = getArrayLongShort(dir, 279);
                            if ((size == null || (size.length == 1 && (size[0] == 0 || size[0] + offset[0] > s.length()))) && h == rowsStrip) {
                                size = new long[]{s.length() - ((int) offset[0])};
                            }
                            if ((compression == 5 || compression == 32946 || compression == 8) && (predictorField = dir.getField(317)) != null) {
                                predictor = predictorField.getAsInt(0);
                                if (predictor != 1 && predictor != 2) {
                                    throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.IllegalValueForPredictorInTiffFile);
                                }
                                if (predictor == 2 && bitsPerSample != 8) {
                                    throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException._1BitSamplesAreNotSupportedForHorizontalDifferencingPredictor).setMessageParams(Integer.valueOf(bitsPerSample));
                                }
                            }
                            if (compression == 5) {
                                lzwDecoder = new TIFFLZWDecoder(w, predictor, samplePerPixel);
                            }
                            int rowsLeft = h;
                            ByteArrayOutputStream stream = null;
                            ByteArrayOutputStream mstream = null;
                            DeflaterOutputStream zip = null;
                            DeflaterOutputStream mzip = null;
                            if (extraSamples > 0) {
                                mstream = new ByteArrayOutputStream();
                                mzip = new DeflaterOutputStream(mstream);
                            }
                            CCITTG4Encoder g4 = null;
                            if (bitsPerSample == 1 && samplePerPixel == 1 && photometric != 3) {
                                g4 = new CCITTG4Encoder(w);
                            } else {
                                stream = new ByteArrayOutputStream();
                                if (compression != 6 && compression != 7) {
                                    zip = new DeflaterOutputStream(stream);
                                }
                            }
                            if (compression == 6) {
                                if (!dir.isTagPresent(513)) {
                                    throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.MissingTagsForOjpegCompression);
                                }
                                int jpegOffset = (int) dir.getFieldAsLong(513);
                                int jpegLength = ((int) s.length()) - jpegOffset;
                                if (dir.isTagPresent(514)) {
                                    jpegLength = ((int) dir.getFieldAsLong(514)) + ((int) size[0]);
                                }
                                byte[] jpeg = new byte[Math.min(jpegLength, ((int) s.length()) - jpegOffset)];
                                int posFilePointer = (int) s.getPosition();
                                s.seek(posFilePointer + jpegOffset);
                                s.readFully(jpeg);
                                tiff.image.data = jpeg;
                                tiff.image.setOriginalType(ImageType.JPEG);
                                JpegImageHelper.processImage(tiff.image);
                                tiff.jpegProcessing = true;
                            } else if (compression == 7) {
                                if (size.length > 1) {
                                    throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.CompressionJpegIsOnlySupportedWithASingleStripThisImageHas1Strips).setMessageParams(Integer.valueOf(size.length));
                                }
                                byte[] jpeg2 = new byte[(int) size[0]];
                                s.seek(offset[0]);
                                s.readFully(jpeg2);
                                TIFFField jpegtables = dir.getField(347);
                                if (jpegtables != null) {
                                    byte[] temp = jpegtables.getAsBytes();
                                    int tableoffset = 0;
                                    int tablelength = temp.length;
                                    if (temp[0] == -1 && temp[1] == -40) {
                                        tableoffset = 2;
                                        tablelength -= 2;
                                    }
                                    if (temp[temp.length - 2] == -1 && temp[temp.length - 1] == -39) {
                                        tablelength -= 2;
                                    }
                                    byte[] tables = new byte[tablelength];
                                    System.arraycopy(temp, tableoffset, tables, 0, tablelength);
                                    byte[] jpegwithtables = new byte[jpeg2.length + tables.length];
                                    System.arraycopy(jpeg2, 0, jpegwithtables, 0, 2);
                                    System.arraycopy(tables, 0, jpegwithtables, 2, tables.length);
                                    System.arraycopy(jpeg2, 2, jpegwithtables, tables.length + 2, jpeg2.length - 2);
                                    jpeg2 = jpegwithtables;
                                }
                                tiff.image.data = jpeg2;
                                tiff.image.setOriginalType(ImageType.JPEG);
                                JpegImageHelper.processImage(tiff.image);
                                tiff.jpegProcessing = true;
                                if (photometric == 2) {
                                    tiff.image.setColorTransform(0);
                                }
                            } else {
                                for (int k = 0; k < offset.length; k++) {
                                    byte[] im = new byte[(int) size[k]];
                                    s.seek(offset[k]);
                                    s.readFully(im);
                                    int height = Math.min(rowsStrip, rowsLeft);
                                    byte[] outBuf = null;
                                    if (compression != 1) {
                                        outBuf = new byte[((((w * bitsPerSample) * samplePerPixel) + 7) / 8) * height];
                                    }
                                    if (reverse) {
                                        TIFFFaxDecoder.reverseBits(im);
                                    }
                                    switch (compression) {
                                        case 1:
                                            outBuf = im;
                                            break;
                                        case 5:
                                            lzwDecoder.decode(im, outBuf, height);
                                            break;
                                        case 8:
                                        case TIFFConstants.COMPRESSION_DEFLATE /* 32946 */:
                                            FilterUtil.inflateData(im, outBuf);
                                            applyPredictor(outBuf, predictor, w, height, samplePerPixel);
                                            break;
                                        case 32773:
                                            decodePackbits(im, outBuf);
                                            break;
                                    }
                                    if (bitsPerSample == 1 && samplePerPixel == 1 && photometric != 3) {
                                        g4.fax4Encode(outBuf, height);
                                    } else if (extraSamples > 0) {
                                        processExtraSamples(zip, mzip, outBuf, samplePerPixel, bitsPerSample, w, height);
                                    } else {
                                        zip.write(outBuf);
                                    }
                                    rowsLeft -= rowsStrip;
                                }
                                if (bitsPerSample == 1 && samplePerPixel == 1 && photometric != 3) {
                                    RawImageHelper.updateRawImageParameters(tiff.image, w, h, false, 256, photometric == 1 ? 1 : 0, g4.close(), null);
                                } else {
                                    zip.close();
                                    RawImageHelper.updateRawImageParameters(tiff.image, w, h, samplePerPixel - extraSamples, bitsPerSample, stream.toByteArray());
                                    tiff.image.setDeflated(true);
                                }
                            }
                            tiff.image.setDpi(dpiX, dpiY);
                            if (compression != 6 && compression != 7) {
                                if (dir.isTagPresent(34675)) {
                                    try {
                                        TIFFField fd = dir.getField(34675);
                                        IccProfile icc_prof = IccProfile.getInstance(fd.getAsBytes());
                                        if (samplePerPixel - extraSamples == icc_prof.getNumComponents()) {
                                            tiff.image.setProfile(icc_prof);
                                        }
                                    } catch (RuntimeException e) {
                                    }
                                }
                                if (dir.isTagPresent(320)) {
                                    TIFFField fd2 = dir.getField(320);
                                    char[] rgb = fd2.getAsChars();
                                    byte[] palette = new byte[rgb.length];
                                    int gColor = rgb.length / 3;
                                    int bColor = gColor * 2;
                                    for (int k2 = 0; k2 < gColor; k2++) {
                                        palette[k2 * 3] = (byte) (rgb[k2] >> '\b');
                                        palette[(k2 * 3) + 1] = (byte) (rgb[k2 + gColor] >> '\b');
                                        palette[(k2 * 3) + 2] = (byte) (rgb[k2 + bColor] >> '\b');
                                    }
                                    boolean colormapBroken = true;
                                    int k3 = 0;
                                    while (true) {
                                        if (k3 < palette.length) {
                                            if (palette[k3] == 0) {
                                                k3++;
                                            } else {
                                                colormapBroken = false;
                                            }
                                        }
                                    }
                                    if (colormapBroken) {
                                        for (int k4 = 0; k4 < gColor; k4++) {
                                            palette[k4 * 3] = (byte) rgb[k4];
                                            palette[(k4 * 3) + 1] = (byte) rgb[k4 + gColor];
                                            palette[(k4 * 3) + 2] = (byte) rgb[k4 + bColor];
                                        }
                                    }
                                    Object[] indexed = {"/Indexed", "/DeviceRGB", Integer.valueOf(gColor - 1), PdfEncodings.convertToString(palette, null)};
                                    tiff.additional = new HashMap();
                                    tiff.additional.put("ColorSpace", indexed);
                                }
                            }
                            if (photometric == 0) {
                                tiff.image.setInverted(true);
                            }
                            if (rotation != 0.0f) {
                                tiff.image.setRotation(rotation);
                            }
                            if (extraSamples > 0) {
                                mzip.close();
                                RawImageData mimg = (RawImageData) ImageDataFactory.createRawImage(null);
                                RawImageHelper.updateRawImageParameters(mimg, w, h, 1, bitsPerSample, mstream.toByteArray());
                                mimg.makeMask();
                                mimg.setDeflated(true);
                                tiff.image.setImageMask(mimg);
                            }
                            return;
                        case 3:
                        case 5:
                        case 6:
                        case 7:
                        default:
                            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.BitsPerSample1IsNotSupported).setMessageParams(Integer.valueOf(bitsPerSample));
                    }
                default:
                    throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.Compression1IsNotSupported).setMessageParams(Integer.valueOf(compression));
            }
        } catch (Exception e2) {
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.CannotGetTiffImageColor);
        }
    }

    private static int getDpi(TIFFField fd, int resolutionUnit) {
        if (fd == null) {
            return 0;
        }
        long[] res = fd.getAsRational(0);
        float frac = res[0] / res[1];
        int dpi = 0;
        switch (resolutionUnit) {
            case 1:
            case 2:
                dpi = (int) (frac + 0.5d);
                break;
            case 3:
                dpi = (int) ((frac * 2.54d) + 0.5d);
                break;
        }
        return dpi;
    }

    private static void processExtraSamples(DeflaterOutputStream zip, DeflaterOutputStream mzip, byte[] outBuf, int samplePerPixel, int bitsPerSample, int width, int height) throws IOException {
        if (bitsPerSample == 8) {
            byte[] mask = new byte[width * height];
            int mptr = 0;
            int optr = 0;
            int total = width * height * samplePerPixel;
            int i = 0;
            while (true) {
                int k = i;
                if (k < total) {
                    for (int s = 0; s < samplePerPixel - 1; s++) {
                        int i2 = optr;
                        optr++;
                        outBuf[i2] = outBuf[k + s];
                    }
                    int i3 = mptr;
                    mptr++;
                    mask[i3] = outBuf[(k + samplePerPixel) - 1];
                    i = k + samplePerPixel;
                } else {
                    zip.write(outBuf, 0, optr);
                    mzip.write(mask, 0, mptr);
                    return;
                }
            }
        } else {
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.ExtraSamplesAreNotSupported);
        }
    }

    private static long[] getArrayLongShort(TIFFDirectory dir, int tag) {
        long[] offset;
        TIFFField field = dir.getField(tag);
        if (field == null) {
            return null;
        }
        if (field.getType() == 4) {
            offset = field.getAsLongs();
        } else {
            char[] temp = field.getAsChars();
            offset = new long[temp.length];
            for (int k = 0; k < temp.length; k++) {
                offset[k] = temp[k];
            }
        }
        return offset;
    }

    private static void decodePackbits(byte[] data, byte[] dst) {
        int srcCount = 0;
        int dstCount = 0;
        while (dstCount < dst.length) {
            try {
                int i = srcCount;
                srcCount++;
                byte b = data[i];
                if (b >= 0 && b <= Byte.MAX_VALUE) {
                    for (int i2 = 0; i2 < b + 1; i2++) {
                        int i3 = dstCount;
                        dstCount++;
                        int i4 = srcCount;
                        srcCount++;
                        dst[i3] = data[i4];
                    }
                } else if ((b & 128) != 0 && b != Byte.MIN_VALUE) {
                    srcCount++;
                    byte repeat = data[srcCount];
                    for (int i5 = 0; i5 < ((b ^ (-1)) & 255) + 2; i5++) {
                        int i6 = dstCount;
                        dstCount++;
                        dst[i6] = repeat;
                    }
                } else {
                    srcCount++;
                }
            } catch (Exception e) {
                return;
            }
        }
    }

    private static void applyPredictor(byte[] uncompData, int predictor, int w, int h, int samplesPerPixel) {
        if (predictor != 2) {
            return;
        }
        for (int j = 0; j < h; j++) {
            int count = samplesPerPixel * ((j * w) + 1);
            for (int i = samplesPerPixel; i < w * samplesPerPixel; i++) {
                int i2 = count;
                uncompData[i2] = (byte) (uncompData[i2] + uncompData[count - samplesPerPixel]);
                count++;
            }
        }
    }
}

package com.itextpdf.io.image;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.io.colors.IccProfile;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.io.util.FilterUtil;
import com.itextpdf.io.util.StreamUtil;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.LoggerFactory;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/image/PngImageHelper.class */
class PngImageHelper {
    public static final String IHDR = "IHDR";
    public static final String PLTE = "PLTE";
    public static final String IDAT = "IDAT";
    public static final String IEND = "IEND";
    public static final String tRNS = "tRNS";
    public static final String pHYs = "pHYs";
    public static final String gAMA = "gAMA";
    public static final String cHRM = "cHRM";
    public static final String sRGB = "sRGB";
    public static final String iCCP = "iCCP";
    private static final int TRANSFERSIZE = 4096;
    private static final int PNG_FILTER_NONE = 0;
    private static final int PNG_FILTER_SUB = 1;
    private static final int PNG_FILTER_UP = 2;
    private static final int PNG_FILTER_AVERAGE = 3;
    private static final int PNG_FILTER_PAETH = 4;
    public static final int[] PNGID = {137, 80, 78, 71, 13, 10, 26, 10};
    private static final String[] intents = {"/Perceptual", "/RelativeColorimetric", "/Saturation", "/AbsoluteColorimetric"};

    PngImageHelper() {
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/image/PngImageHelper$PngParameters.class */
    private static class PngParameters {
        PngImageData image;
        InputStream dataStream;
        int width;
        int height;
        int bitDepth;
        int colorType;
        int compressionMethod;
        int filterMethod;
        int interlaceMethod;
        byte[] imageData;
        byte[] smask;
        byte[] trans;
        int dpiX;
        int dpiY;
        float XYRatio;
        boolean genBWMask;
        boolean palShades;
        int inputBands;
        int bytesPerPixel;
        byte[] colorTable;
        float xW;
        float yW;
        float xR;
        float yR;
        float xG;
        float yG;
        float xB;
        float yB;
        String intent;
        IccProfile iccProfile;
        Map<String, Object> additional = new HashMap();
        ByteArrayOutputStream idat = new ByteArrayOutputStream();
        int transRedGray = -1;
        int transGreen = -1;
        int transBlue = -1;
        float gamma = 1.0f;
        boolean hasCHRM = false;

        PngParameters(PngImageData image) {
            this.image = image;
        }
    }

    public static void processImage(ImageData image) throws IOException {
        if (image.getOriginalType() != ImageType.PNG) {
            throw new IllegalArgumentException("PNG image expected");
        }
        InputStream pngStream = null;
        try {
            try {
                if (image.getData() == null) {
                    image.loadData();
                }
                pngStream = new ByteArrayInputStream(image.getData());
                image.imageSize = image.getData().length;
                PngParameters png = new PngParameters((PngImageData) image);
                processPng(pngStream, png);
                if (pngStream != null) {
                    try {
                        pngStream.close();
                    } catch (IOException e) {
                    }
                }
                RawImageHelper.updateImageAttributes(png.image, png.additional);
            } catch (IOException e2) {
                throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.PngImageException, (Throwable) e2);
            }
        } catch (Throwable th) {
            if (pngStream != null) {
                try {
                    pngStream.close();
                } catch (IOException e3) {
                }
            }
            throw th;
        }
    }

    private static void processPng(InputStream pngStream, PngParameters png) throws IOException {
        readPng(pngStream, png);
        if (png.iccProfile != null && png.iccProfile.getNumComponents() != getExpectedNumberOfColorComponents(png)) {
            LoggerFactory.getLogger((Class<?>) PngImageHelper.class).warn(LogMessageConstant.PNG_IMAGE_HAS_ICC_PROFILE_WITH_INCOMPATIBLE_NUMBER_OF_COLOR_COMPONENTS);
        }
        try {
            int pal0 = 0;
            int palIdx = 0;
            png.palShades = false;
            if (png.trans != null) {
                int k = 0;
                while (true) {
                    if (k < png.trans.length) {
                        int n = png.trans[k] & 255;
                        if (n == 0) {
                            pal0++;
                            palIdx = k;
                        }
                        if (n == 0 || n == 255) {
                            k++;
                        } else {
                            png.palShades = true;
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
            if ((png.colorType & 4) != 0) {
                png.palShades = true;
            }
            png.genBWMask = !png.palShades && (pal0 > 1 || png.transRedGray >= 0);
            if (!png.palShades && !png.genBWMask && pal0 == 1) {
                png.additional.put("Mask", new int[]{palIdx, palIdx});
            }
            boolean needDecode = png.interlaceMethod == 1 || png.bitDepth == 16 || (png.colorType & 4) != 0 || png.palShades || png.genBWMask;
            switch (png.colorType) {
                case 0:
                    png.inputBands = 1;
                    break;
                case 2:
                    png.inputBands = 3;
                    break;
                case 3:
                    png.inputBands = 1;
                    break;
                case 4:
                    png.inputBands = 2;
                    break;
                case 6:
                    png.inputBands = 4;
                    break;
            }
            if (needDecode) {
                decodeIdat(png);
            }
            int components = png.inputBands;
            if ((png.colorType & 4) != 0) {
                components--;
            }
            int bpc = png.bitDepth;
            if (bpc == 16) {
                bpc = 8;
            }
            if (png.imageData != null) {
                if (png.colorType == 3) {
                    RawImageHelper.updateRawImageParameters(png.image, png.width, png.height, components, bpc, png.imageData);
                } else {
                    RawImageHelper.updateRawImageParameters(png.image, png.width, png.height, components, bpc, png.imageData, null);
                }
            } else {
                RawImageHelper.updateRawImageParameters(png.image, png.width, png.height, components, bpc, png.idat.toByteArray());
                png.image.setDeflated(true);
                Map<String, Object> decodeparms = new HashMap<>();
                decodeparms.put("BitsPerComponent", Integer.valueOf(png.bitDepth));
                decodeparms.put("Predictor", 15);
                decodeparms.put("Columns", Integer.valueOf(png.width));
                decodeparms.put("Colors", Integer.valueOf((png.colorType == 3 || (png.colorType & 2) == 0) ? 1 : 3));
                png.image.decodeParms = decodeparms;
            }
            if (png.additional.get("ColorSpace") == null) {
                png.additional.put("ColorSpace", getColorspace(png));
            }
            if (png.intent != null) {
                png.additional.put("Intent", png.intent);
            }
            if (png.iccProfile != null) {
                png.image.setProfile(png.iccProfile);
            }
            if (png.palShades) {
                RawImageData im2 = (RawImageData) ImageDataFactory.createRawImage(null);
                RawImageHelper.updateRawImageParameters(im2, png.width, png.height, 1, 8, png.smask);
                im2.makeMask();
                png.image.setImageMask(im2);
            }
            if (png.genBWMask) {
                RawImageData im22 = (RawImageData) ImageDataFactory.createRawImage(null);
                RawImageHelper.updateRawImageParameters(im22, png.width, png.height, 1, 1, png.smask);
                im22.makeMask();
                png.image.setImageMask(im22);
            }
            png.image.setDpi(png.dpiX, png.dpiY);
            png.image.setXYRatio(png.XYRatio);
        } catch (Exception e) {
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.PngImageException, (Throwable) e);
        }
    }

    private static Object getColorspace(PngParameters png) {
        if (png.iccProfile != null) {
            if ((png.colorType & 2) == 0) {
                return "/DeviceGray";
            }
            return "/DeviceRGB";
        }
        if (png.gamma == 1.0f && !png.hasCHRM) {
            if ((png.colorType & 2) == 0) {
                return "/DeviceGray";
            }
            return "/DeviceRGB";
        }
        Object[] array = new Object[2];
        Map<String, Object> map = new HashMap<>();
        if ((png.colorType & 2) != 0) {
            float[] wp = {1.0f, 1.0f, 1.0f};
            array[0] = "/CalRGB";
            if (png.gamma != 1.0f) {
                float[] gm = {png.gamma, png.gamma, png.gamma};
                map.put("Gamma", gm);
            }
            if (png.hasCHRM) {
                float z = png.yW * ((((png.xG - png.xB) * png.yR) - ((png.xR - png.xB) * png.yG)) + ((png.xR - png.xG) * png.yB));
                float YA = (png.yR * ((((png.xG - png.xB) * png.yW) - ((png.xW - png.xB) * png.yG)) + ((png.xW - png.xG) * png.yB))) / z;
                float XA = (YA * png.xR) / png.yR;
                float ZA = YA * (((1.0f - png.xR) / png.yR) - 1.0f);
                float YB = ((-png.yG) * ((((png.xR - png.xB) * png.yW) - ((png.xW - png.xB) * png.yR)) + ((png.xW - png.xR) * png.yB))) / z;
                float XB = (YB * png.xG) / png.yG;
                float ZB = YB * (((1.0f - png.xG) / png.yG) - 1.0f);
                float YC = (png.yB * ((((png.xR - png.xG) * png.yW) - ((png.xW - png.xG) * png.yW)) + ((png.xW - png.xR) * png.yG))) / z;
                float XC = (YC * png.xB) / png.yB;
                float ZC = YC * (((1.0f - png.xB) / png.yB) - 1.0f);
                float XW = XA + XB + XC;
                float ZW = ZA + ZB + ZC;
                float[] wpa = {XW, 1.0f, ZW};
                wp = wpa;
                float[] matrix = {XA, YA, ZA, XB, YB, ZB, XC, YC, ZC};
                map.put("Matrix", matrix);
            }
            map.put("WhitePoint", wp);
            array[1] = map;
        } else {
            if (png.gamma == 1.0f) {
                return "/DeviceGray";
            }
            array[0] = "/CalGray";
            map.put("Gamma", Float.valueOf(png.gamma));
            map.put("WhitePoint", new int[]{1, 1, 1});
            array[1] = map;
        }
        return array;
    }

    private static int getExpectedNumberOfColorComponents(PngParameters png) {
        return (png.colorType & 2) == 0 ? 1 : 3;
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x004c, code lost:
    
        throw new java.io.IOException("corrupted.png.file");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static void readPng(java.io.InputStream r8, com.itextpdf.io.image.PngImageHelper.PngParameters r9) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 1258
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.itextpdf.io.image.PngImageHelper.readPng(java.io.InputStream, com.itextpdf.io.image.PngImageHelper$PngParameters):void");
    }

    private static boolean checkMarker(String s) {
        if (s.length() != 4) {
            return false;
        }
        for (int k = 0; k < 4; k++) {
            char c = s.charAt(k);
            if ((c < 'a' || c > 'z') && (c < 'A' || c > 'Z')) {
                return false;
            }
        }
        return true;
    }

    private static void decodeIdat(PngParameters png) throws IOException {
        int nbitDepth = png.bitDepth;
        if (nbitDepth == 16) {
            nbitDepth = 8;
        }
        int size = -1;
        png.bytesPerPixel = png.bitDepth == 16 ? 2 : 1;
        switch (png.colorType) {
            case 0:
                size = (((nbitDepth * png.width) + 7) / 8) * png.height;
                break;
            case 2:
                size = png.width * 3 * png.height;
                png.bytesPerPixel *= 3;
                break;
            case 3:
                if (png.interlaceMethod == 1) {
                    size = (((nbitDepth * png.width) + 7) / 8) * png.height;
                }
                png.bytesPerPixel = 1;
                break;
            case 4:
                size = png.width * png.height;
                png.bytesPerPixel *= 2;
                break;
            case 6:
                size = png.width * 3 * png.height;
                png.bytesPerPixel *= 4;
                break;
        }
        if (size >= 0) {
            png.imageData = new byte[size];
        }
        if (png.palShades) {
            png.smask = new byte[png.width * png.height];
        } else if (png.genBWMask) {
            png.smask = new byte[((png.width + 7) / 8) * png.height];
        }
        ByteArrayInputStream bai = new ByteArrayInputStream(png.idat.toByteArray());
        png.dataStream = FilterUtil.getInflaterInputStream(bai);
        if (png.interlaceMethod != 1) {
            decodePass(0, 0, 1, 1, png.width, png.height, png);
            return;
        }
        decodePass(0, 0, 8, 8, (png.width + 7) / 8, (png.height + 7) / 8, png);
        decodePass(4, 0, 8, 8, (png.width + 3) / 8, (png.height + 7) / 8, png);
        decodePass(0, 4, 4, 8, (png.width + 3) / 4, (png.height + 3) / 8, png);
        decodePass(2, 0, 4, 4, (png.width + 1) / 4, (png.height + 3) / 4, png);
        decodePass(0, 2, 2, 4, (png.width + 1) / 2, (png.height + 1) / 4, png);
        decodePass(1, 0, 2, 2, png.width / 2, (png.height + 1) / 2, png);
        decodePass(0, 1, 1, 2, png.width, png.height / 2, png);
    }

    private static void decodePass(int xOffset, int yOffset, int xStep, int yStep, int passWidth, int passHeight, PngParameters png) throws IOException {
        if (passWidth == 0 || passHeight == 0) {
            return;
        }
        int bytesPerRow = (((png.inputBands * passWidth) * png.bitDepth) + 7) / 8;
        byte[] curr = new byte[bytesPerRow];
        byte[] prior = new byte[bytesPerRow];
        int srcY = 0;
        int i = yOffset;
        while (true) {
            int dstY = i;
            if (srcY < passHeight) {
                int filter = 0;
                try {
                    filter = png.dataStream.read();
                    StreamUtil.readFully(png.dataStream, curr, 0, bytesPerRow);
                } catch (Exception e) {
                }
                switch (filter) {
                    case 0:
                        break;
                    case 1:
                        decodeSubFilter(curr, bytesPerRow, png.bytesPerPixel);
                        break;
                    case 2:
                        decodeUpFilter(curr, prior, bytesPerRow);
                        break;
                    case 3:
                        decodeAverageFilter(curr, prior, bytesPerRow, png.bytesPerPixel);
                        break;
                    case 4:
                        decodePaethFilter(curr, prior, bytesPerRow, png.bytesPerPixel);
                        break;
                    default:
                        throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.UnknownPngFilter);
                }
                processPixels(curr, xOffset, xStep, dstY, passWidth, png);
                byte[] tmp = prior;
                prior = curr;
                curr = tmp;
                srcY++;
                i = dstY + yStep;
            } else {
                return;
            }
        }
    }

    private static void processPixels(byte[] curr, int xOffset, int step, int y, int width, PngParameters png) {
        int[] outPixel = getPixel(curr, png);
        int sizes = 0;
        switch (png.colorType) {
            case 0:
            case 3:
            case 4:
                sizes = 1;
                break;
            case 2:
            case 6:
                sizes = 3;
                break;
        }
        if (png.imageData != null) {
            int dstX = xOffset;
            int yStride = (((sizes * png.width) * (png.bitDepth == 16 ? 8 : png.bitDepth)) + 7) / 8;
            for (int srcX = 0; srcX < width; srcX++) {
                setPixel(png.imageData, outPixel, png.inputBands * srcX, sizes, dstX, y, png.bitDepth, yStride);
                dstX += step;
            }
        }
        if (png.palShades) {
            if ((png.colorType & 4) != 0) {
                if (png.bitDepth == 16) {
                    for (int k = 0; k < width; k++) {
                        int i = (k * png.inputBands) + sizes;
                        outPixel[i] = outPixel[i] >>> 8;
                    }
                }
                int yStride2 = png.width;
                int dstX2 = xOffset;
                for (int srcX2 = 0; srcX2 < width; srcX2++) {
                    setPixel(png.smask, outPixel, (png.inputBands * srcX2) + sizes, 1, dstX2, y, 8, yStride2);
                    dstX2 += step;
                }
                return;
            }
            int yStride3 = png.width;
            int[] v = new int[1];
            int dstX3 = xOffset;
            for (int srcX3 = 0; srcX3 < width; srcX3++) {
                int idx = outPixel[srcX3];
                if (idx < png.trans.length) {
                    v[0] = png.trans[idx];
                } else {
                    v[0] = 255;
                }
                setPixel(png.smask, v, 0, 1, dstX3, y, 8, yStride3);
                dstX3 += step;
            }
            return;
        }
        if (png.genBWMask) {
            switch (png.colorType) {
                case 0:
                    int yStride4 = (png.width + 7) / 8;
                    int[] v2 = new int[1];
                    int dstX4 = xOffset;
                    for (int srcX4 = 0; srcX4 < width; srcX4++) {
                        int g = outPixel[srcX4];
                        v2[0] = g == png.transRedGray ? 1 : 0;
                        setPixel(png.smask, v2, 0, 1, dstX4, y, 1, yStride4);
                        dstX4 += step;
                    }
                    break;
                case 2:
                    int yStride5 = (png.width + 7) / 8;
                    int[] v3 = new int[1];
                    int dstX5 = xOffset;
                    for (int srcX5 = 0; srcX5 < width; srcX5++) {
                        int markRed = png.inputBands * srcX5;
                        v3[0] = (outPixel[markRed] == png.transRedGray && outPixel[markRed + 1] == png.transGreen && outPixel[markRed + 2] == png.transBlue) ? 1 : 0;
                        setPixel(png.smask, v3, 0, 1, dstX5, y, 1, yStride5);
                        dstX5 += step;
                    }
                    break;
                case 3:
                    int yStride6 = (png.width + 7) / 8;
                    int[] v4 = new int[1];
                    int dstX6 = xOffset;
                    for (int srcX6 = 0; srcX6 < width; srcX6++) {
                        int idx2 = outPixel[srcX6];
                        v4[0] = (idx2 >= png.trans.length || png.trans[idx2] != 0) ? 0 : 1;
                        setPixel(png.smask, v4, 0, 1, dstX6, y, 1, yStride6);
                        dstX6 += step;
                    }
                    break;
            }
        }
    }

    private static int getPixel(byte[] image, int x, int y, int bitDepth, int bytesPerRow) {
        if (bitDepth == 8) {
            int pos = (bytesPerRow * y) + x;
            return image[pos] & 255;
        }
        int pos2 = (bytesPerRow * y) + (x / (8 / bitDepth));
        int v = image[pos2] >> ((8 - (bitDepth * (x % (8 / bitDepth)))) - bitDepth);
        return v & ((1 << bitDepth) - 1);
    }

    static void setPixel(byte[] image, int[] data, int offset, int size, int x, int y, int bitDepth, int bytesPerRow) {
        if (bitDepth == 8) {
            int pos = (bytesPerRow * y) + (size * x);
            for (int k = 0; k < size; k++) {
                image[pos + k] = (byte) data[k + offset];
            }
            return;
        }
        if (bitDepth == 16) {
            int pos2 = (bytesPerRow * y) + (size * x);
            for (int k2 = 0; k2 < size; k2++) {
                image[pos2 + k2] = (byte) (data[k2 + offset] >>> 8);
            }
            return;
        }
        int pos3 = (bytesPerRow * y) + (x / (8 / bitDepth));
        int v = data[offset] << ((8 - (bitDepth * (x % (8 / bitDepth)))) - bitDepth);
        image[pos3] = (byte) (image[pos3] | ((byte) v));
    }

    private static int[] getPixel(byte[] curr, PngParameters png) {
        switch (png.bitDepth) {
            case 8:
                int[] res = new int[curr.length];
                for (int k = 0; k < res.length; k++) {
                    res[k] = curr[k] & 255;
                }
                return res;
            case 16:
                int[] res2 = new int[curr.length / 2];
                for (int k2 = 0; k2 < res2.length; k2++) {
                    res2[k2] = ((curr[k2 * 2] & 255) << 8) + (curr[(k2 * 2) + 1] & 255);
                }
                return res2;
            default:
                int[] res3 = new int[(curr.length * 8) / png.bitDepth];
                int idx = 0;
                int passes = 8 / png.bitDepth;
                int mask = (1 << png.bitDepth) - 1;
                for (byte b : curr) {
                    for (int j = passes - 1; j >= 0; j--) {
                        int i = idx;
                        idx++;
                        res3[i] = (b >>> (png.bitDepth * j)) & mask;
                    }
                }
                return res3;
        }
    }

    private static void decodeSubFilter(byte[] curr, int count, int bpp) {
        for (int i = bpp; i < count; i++) {
            int val = curr[i] & 255;
            curr[i] = (byte) (val + (curr[i - bpp] & 255));
        }
    }

    private static void decodeUpFilter(byte[] curr, byte[] prev, int count) {
        for (int i = 0; i < count; i++) {
            int raw = curr[i] & 255;
            int prior = prev[i] & 255;
            curr[i] = (byte) (raw + prior);
        }
    }

    private static void decodeAverageFilter(byte[] curr, byte[] prev, int count, int bpp) {
        for (int i = 0; i < bpp; i++) {
            int raw = curr[i] & 255;
            int priorRow = prev[i] & 255;
            curr[i] = (byte) (raw + (priorRow / 2));
        }
        for (int i2 = bpp; i2 < count; i2++) {
            int raw2 = curr[i2] & 255;
            int priorPixel = curr[i2 - bpp] & 255;
            int priorRow2 = prev[i2] & 255;
            curr[i2] = (byte) (raw2 + ((priorPixel + priorRow2) / 2));
        }
    }

    private static int paethPredictor(int a, int b, int c) {
        int p = (a + b) - c;
        int pa = Math.abs(p - a);
        int pb = Math.abs(p - b);
        int pc = Math.abs(p - c);
        if (pa <= pb && pa <= pc) {
            return a;
        }
        if (pb <= pc) {
            return b;
        }
        return c;
    }

    private static void decodePaethFilter(byte[] curr, byte[] prev, int count, int bpp) {
        for (int i = 0; i < bpp; i++) {
            int raw = curr[i] & 255;
            int priorRow = prev[i] & 255;
            curr[i] = (byte) (raw + priorRow);
        }
        for (int i2 = bpp; i2 < count; i2++) {
            int raw2 = curr[i2] & 255;
            int priorPixel = curr[i2 - bpp] & 255;
            int priorRow2 = prev[i2] & 255;
            int priorRowPixel = prev[i2 - bpp] & 255;
            curr[i2] = (byte) (raw2 + paethPredictor(priorPixel, priorRow2, priorRowPixel));
        }
    }

    public static int getInt(InputStream pngStream) throws IOException {
        return (pngStream.read() << 24) + (pngStream.read() << 16) + (pngStream.read() << 8) + pngStream.read();
    }

    public static int getWord(InputStream pngStream) throws IOException {
        return (pngStream.read() << 8) + pngStream.read();
    }

    public static String getString(InputStream pngStream) throws IOException {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            buf.append((char) pngStream.read());
        }
        return buf.toString();
    }
}

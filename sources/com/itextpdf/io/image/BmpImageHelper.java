package com.itextpdf.io.image;

import com.itextpdf.io.font.PdfEncodings;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/image/BmpImageHelper.class */
final class BmpImageHelper {
    private static final int VERSION_2_1_BIT = 0;
    private static final int VERSION_2_4_BIT = 1;
    private static final int VERSION_2_8_BIT = 2;
    private static final int VERSION_2_24_BIT = 3;
    private static final int VERSION_3_1_BIT = 4;
    private static final int VERSION_3_4_BIT = 5;
    private static final int VERSION_3_8_BIT = 6;
    private static final int VERSION_3_24_BIT = 7;
    private static final int VERSION_3_NT_16_BIT = 8;
    private static final int VERSION_3_NT_32_BIT = 9;
    private static final int VERSION_4_1_BIT = 10;
    private static final int VERSION_4_4_BIT = 11;
    private static final int VERSION_4_8_BIT = 12;
    private static final int VERSION_4_16_BIT = 13;
    private static final int VERSION_4_24_BIT = 14;
    private static final int VERSION_4_32_BIT = 15;
    private static final int LCS_CALIBRATED_RGB = 0;
    private static final int LCS_SRGB = 1;
    private static final int LCS_CMYK = 2;
    private static final int BI_RGB = 0;
    private static final int BI_RLE8 = 1;
    private static final int BI_RLE4 = 2;
    private static final int BI_BITFIELDS = 3;

    BmpImageHelper() {
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/image/BmpImageHelper$BmpParameters.class */
    private static class BmpParameters {
        BmpImageData image;
        int width;
        int height;
        Map<String, Object> additional;
        InputStream inputStream;
        long bitmapFileSize;
        long bitmapOffset;
        long compression;
        long imageSize;
        byte[] palette;
        int imageType;
        int numBands;
        boolean isBottomUp;
        int bitsPerPixel;
        int redMask;
        int greenMask;
        int blueMask;
        int alphaMask;
        Map<String, Object> properties = new HashMap();
        long xPelsPerMeter;
        long yPelsPerMeter;

        public BmpParameters(BmpImageData image) {
            this.image = image;
        }
    }

    public static void processImage(ImageData image) {
        if (image.getOriginalType() != ImageType.BMP) {
            throw new IllegalArgumentException("BMP image expected");
        }
        try {
            if (image.getData() == null) {
                image.loadData();
            }
            InputStream bmpStream = new ByteArrayInputStream(image.getData());
            image.imageSize = image.getData().length;
            BmpParameters bmp = new BmpParameters((BmpImageData) image);
            process(bmp, bmpStream);
            if (getImage(bmp)) {
                image.setWidth(bmp.width);
                image.setHeight(bmp.height);
                image.setDpi((int) ((bmp.xPelsPerMeter * 0.0254d) + 0.5d), (int) ((bmp.yPelsPerMeter * 0.0254d) + 0.5d));
            }
            RawImageHelper.updateImageAttributes(bmp.image, bmp.additional);
        } catch (IOException e) {
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.BmpImageException, (Throwable) e);
        }
    }

    private static void process(BmpParameters bmp, InputStream stream) throws IOException {
        bmp.inputStream = stream;
        if (!bmp.image.isNoHeader()) {
            if (readUnsignedByte(bmp.inputStream) != 66 || readUnsignedByte(bmp.inputStream) != 77) {
                throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.InvalidMagicValueForBmpFileMustBeBM);
            }
            bmp.bitmapFileSize = readDWord(bmp.inputStream);
            readWord(bmp.inputStream);
            readWord(bmp.inputStream);
            bmp.bitmapOffset = readDWord(bmp.inputStream);
        }
        long size = readDWord(bmp.inputStream);
        if (size == 12) {
            bmp.width = readWord(bmp.inputStream);
            bmp.height = readWord(bmp.inputStream);
        } else {
            bmp.width = readLong(bmp.inputStream);
            bmp.height = readLong(bmp.inputStream);
        }
        int planes = readWord(bmp.inputStream);
        bmp.bitsPerPixel = readWord(bmp.inputStream);
        bmp.properties.put("color_planes", Integer.valueOf(planes));
        bmp.properties.put("bits_per_pixel", Integer.valueOf(bmp.bitsPerPixel));
        bmp.numBands = 3;
        if (bmp.bitmapOffset == 0) {
            bmp.bitmapOffset = size;
        }
        if (size == 12) {
            bmp.properties.put("bmp_version", "BMP v. 2.x");
            if (bmp.bitsPerPixel == 1) {
                bmp.imageType = 0;
            } else if (bmp.bitsPerPixel == 4) {
                bmp.imageType = 1;
            } else if (bmp.bitsPerPixel == 8) {
                bmp.imageType = 2;
            } else if (bmp.bitsPerPixel == 24) {
                bmp.imageType = 3;
            }
            int numberOfEntries = (int) (((bmp.bitmapOffset - 14) - size) / 3);
            int sizeOfPalette = numberOfEntries * 3;
            if (bmp.bitmapOffset == size) {
                switch (bmp.imageType) {
                    case 0:
                        sizeOfPalette = 6;
                        break;
                    case 1:
                        sizeOfPalette = 48;
                        break;
                    case 2:
                        sizeOfPalette = 768;
                        break;
                    case 3:
                        sizeOfPalette = 0;
                        break;
                }
                bmp.bitmapOffset = size + sizeOfPalette;
            }
            readPalette(sizeOfPalette, bmp);
        } else {
            bmp.compression = readDWord(bmp.inputStream);
            bmp.imageSize = readDWord(bmp.inputStream);
            bmp.xPelsPerMeter = readLong(bmp.inputStream);
            bmp.yPelsPerMeter = readLong(bmp.inputStream);
            long colorsUsed = readDWord(bmp.inputStream);
            long colorsImportant = readDWord(bmp.inputStream);
            switch ((int) bmp.compression) {
                case 0:
                    bmp.properties.put("compression", "BI_RGB");
                    break;
                case 1:
                    bmp.properties.put("compression", "BI_RLE8");
                    break;
                case 2:
                    bmp.properties.put("compression", "BI_RLE4");
                    break;
                case 3:
                    bmp.properties.put("compression", "BI_BITFIELDS");
                    break;
            }
            bmp.properties.put("x_pixels_per_meter", Long.valueOf(bmp.xPelsPerMeter));
            bmp.properties.put("y_pixels_per_meter", Long.valueOf(bmp.yPelsPerMeter));
            bmp.properties.put("colors_used", Long.valueOf(colorsUsed));
            bmp.properties.put("colors_important", Long.valueOf(colorsImportant));
            if (size == 40 || size == 52 || size == 56) {
                switch ((int) bmp.compression) {
                    case 0:
                    case 1:
                    case 2:
                        if (bmp.bitsPerPixel == 1) {
                            bmp.imageType = 4;
                        } else if (bmp.bitsPerPixel == 4) {
                            bmp.imageType = 5;
                        } else if (bmp.bitsPerPixel == 8) {
                            bmp.imageType = 6;
                        } else if (bmp.bitsPerPixel == 24) {
                            bmp.imageType = 7;
                        } else if (bmp.bitsPerPixel == 16) {
                            bmp.imageType = 8;
                            bmp.redMask = 31744;
                            bmp.greenMask = 992;
                            bmp.blueMask = 31;
                            bmp.properties.put("red_mask", Integer.valueOf(bmp.redMask));
                            bmp.properties.put("green_mask", Integer.valueOf(bmp.greenMask));
                            bmp.properties.put("blue_mask", Integer.valueOf(bmp.blueMask));
                        } else if (bmp.bitsPerPixel == 32) {
                            bmp.imageType = 9;
                            bmp.redMask = 16711680;
                            bmp.greenMask = 65280;
                            bmp.blueMask = 255;
                            bmp.properties.put("red_mask", Integer.valueOf(bmp.redMask));
                            bmp.properties.put("green_mask", Integer.valueOf(bmp.greenMask));
                            bmp.properties.put("blue_mask", Integer.valueOf(bmp.blueMask));
                        }
                        if (size >= 52) {
                            bmp.redMask = (int) readDWord(bmp.inputStream);
                            bmp.greenMask = (int) readDWord(bmp.inputStream);
                            bmp.blueMask = (int) readDWord(bmp.inputStream);
                            bmp.properties.put("red_mask", Integer.valueOf(bmp.redMask));
                            bmp.properties.put("green_mask", Integer.valueOf(bmp.greenMask));
                            bmp.properties.put("blue_mask", Integer.valueOf(bmp.blueMask));
                        }
                        if (size == 56) {
                            bmp.alphaMask = (int) readDWord(bmp.inputStream);
                            bmp.properties.put("alpha_mask", Integer.valueOf(bmp.alphaMask));
                        }
                        int numberOfEntries2 = (int) (((bmp.bitmapOffset - 14) - size) / 4);
                        int sizeOfPalette2 = numberOfEntries2 * 4;
                        if (bmp.bitmapOffset == size) {
                            switch (bmp.imageType) {
                                case 4:
                                    sizeOfPalette2 = ((int) (colorsUsed == 0 ? 2L : colorsUsed)) * 4;
                                    break;
                                case 5:
                                    sizeOfPalette2 = ((int) (colorsUsed == 0 ? 16L : colorsUsed)) * 4;
                                    break;
                                case 6:
                                    sizeOfPalette2 = ((int) (colorsUsed == 0 ? 256L : colorsUsed)) * 4;
                                    break;
                                default:
                                    sizeOfPalette2 = 0;
                                    break;
                            }
                            bmp.bitmapOffset = size + sizeOfPalette2;
                        }
                        readPalette(sizeOfPalette2, bmp);
                        bmp.properties.put("bmp_version", "BMP v. 3.x");
                        break;
                    case 3:
                        if (bmp.bitsPerPixel == 16) {
                            bmp.imageType = 8;
                        } else if (bmp.bitsPerPixel == 32) {
                            bmp.imageType = 9;
                        }
                        bmp.redMask = (int) readDWord(bmp.inputStream);
                        bmp.greenMask = (int) readDWord(bmp.inputStream);
                        bmp.blueMask = (int) readDWord(bmp.inputStream);
                        if (size == 56) {
                            bmp.alphaMask = (int) readDWord(bmp.inputStream);
                            bmp.properties.put("alpha_mask", Integer.valueOf(bmp.alphaMask));
                        }
                        bmp.properties.put("red_mask", Integer.valueOf(bmp.redMask));
                        bmp.properties.put("green_mask", Integer.valueOf(bmp.greenMask));
                        bmp.properties.put("blue_mask", Integer.valueOf(bmp.blueMask));
                        if (colorsUsed != 0) {
                            int sizeOfPalette3 = ((int) colorsUsed) * 4;
                            readPalette(sizeOfPalette3, bmp);
                        }
                        bmp.properties.put("bmp_version", "BMP v. 3.x NT");
                        break;
                    default:
                        throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.InvalidBmpFileCompression);
                }
            } else if (size == 108) {
                bmp.properties.put("bmp_version", "BMP v. 4.x");
                bmp.redMask = (int) readDWord(bmp.inputStream);
                bmp.greenMask = (int) readDWord(bmp.inputStream);
                bmp.blueMask = (int) readDWord(bmp.inputStream);
                bmp.alphaMask = (int) readDWord(bmp.inputStream);
                long csType = readDWord(bmp.inputStream);
                int redX = readLong(bmp.inputStream);
                int redY = readLong(bmp.inputStream);
                int redZ = readLong(bmp.inputStream);
                int greenX = readLong(bmp.inputStream);
                int greenY = readLong(bmp.inputStream);
                int greenZ = readLong(bmp.inputStream);
                int blueX = readLong(bmp.inputStream);
                int blueY = readLong(bmp.inputStream);
                int blueZ = readLong(bmp.inputStream);
                long gammaRed = readDWord(bmp.inputStream);
                long gammaGreen = readDWord(bmp.inputStream);
                long gammaBlue = readDWord(bmp.inputStream);
                if (bmp.bitsPerPixel == 1) {
                    bmp.imageType = 10;
                } else if (bmp.bitsPerPixel == 4) {
                    bmp.imageType = 11;
                } else if (bmp.bitsPerPixel == 8) {
                    bmp.imageType = 12;
                } else if (bmp.bitsPerPixel == 16) {
                    bmp.imageType = 13;
                    if (((int) bmp.compression) == 0) {
                        bmp.redMask = 31744;
                        bmp.greenMask = 992;
                        bmp.blueMask = 31;
                    }
                } else if (bmp.bitsPerPixel == 24) {
                    bmp.imageType = 14;
                } else if (bmp.bitsPerPixel == 32) {
                    bmp.imageType = 15;
                    if (((int) bmp.compression) == 0) {
                        bmp.redMask = 16711680;
                        bmp.greenMask = 65280;
                        bmp.blueMask = 255;
                    }
                }
                bmp.properties.put("red_mask", Integer.valueOf(bmp.redMask));
                bmp.properties.put("green_mask", Integer.valueOf(bmp.greenMask));
                bmp.properties.put("blue_mask", Integer.valueOf(bmp.blueMask));
                bmp.properties.put("alpha_mask", Integer.valueOf(bmp.alphaMask));
                int numberOfEntries3 = (int) (((bmp.bitmapOffset - 14) - size) / 4);
                int sizeOfPalette4 = numberOfEntries3 * 4;
                if (bmp.bitmapOffset == size) {
                    switch (bmp.imageType) {
                        case 10:
                            sizeOfPalette4 = ((int) (colorsUsed == 0 ? 2L : colorsUsed)) * 4;
                            break;
                        case 11:
                            sizeOfPalette4 = ((int) (colorsUsed == 0 ? 16L : colorsUsed)) * 4;
                            break;
                        case 12:
                            sizeOfPalette4 = ((int) (colorsUsed == 0 ? 256L : colorsUsed)) * 4;
                            break;
                        default:
                            sizeOfPalette4 = 0;
                            break;
                    }
                    bmp.bitmapOffset = size + sizeOfPalette4;
                }
                readPalette(sizeOfPalette4, bmp);
                switch ((int) csType) {
                    case 0:
                        bmp.properties.put("color_space", "LCS_CALIBRATED_RGB");
                        bmp.properties.put("redX", Integer.valueOf(redX));
                        bmp.properties.put("redY", Integer.valueOf(redY));
                        bmp.properties.put("redZ", Integer.valueOf(redZ));
                        bmp.properties.put("greenX", Integer.valueOf(greenX));
                        bmp.properties.put("greenY", Integer.valueOf(greenY));
                        bmp.properties.put("greenZ", Integer.valueOf(greenZ));
                        bmp.properties.put("blueX", Integer.valueOf(blueX));
                        bmp.properties.put("blueY", Integer.valueOf(blueY));
                        bmp.properties.put("blueZ", Integer.valueOf(blueZ));
                        bmp.properties.put("gamma_red", Long.valueOf(gammaRed));
                        bmp.properties.put("gamma_green", Long.valueOf(gammaGreen));
                        bmp.properties.put("gamma_blue", Long.valueOf(gammaBlue));
                        throw new RuntimeException("Not implemented yet.");
                    case 1:
                        bmp.properties.put("color_space", "LCS_sRGB");
                        break;
                    case 2:
                        bmp.properties.put("color_space", "LCS_CMYK");
                        throw new RuntimeException("Not implemented yet.");
                }
            } else {
                bmp.properties.put("bmp_version", "BMP v. 5.x");
                throw new RuntimeException("Not implemented yet.");
            }
        }
        if (bmp.height > 0) {
            bmp.isBottomUp = true;
        } else {
            bmp.isBottomUp = false;
            bmp.height = Math.abs(bmp.height);
        }
        if (bmp.bitsPerPixel != 1 && bmp.bitsPerPixel != 4 && bmp.bitsPerPixel != 8) {
            if (bmp.bitsPerPixel == 16) {
                bmp.numBands = 3;
                return;
            } else if (bmp.bitsPerPixel == 32) {
                bmp.numBands = bmp.alphaMask == 0 ? 3 : 4;
                return;
            } else {
                bmp.numBands = 3;
                return;
            }
        }
        bmp.numBands = 1;
        if (bmp.imageType == 0 || bmp.imageType == 1 || bmp.imageType == 2) {
            int sizep = bmp.palette.length / 3;
            if (sizep > 256) {
                sizep = 256;
            }
            byte[] r = new byte[sizep];
            byte[] g = new byte[sizep];
            byte[] b = new byte[sizep];
            for (int i = 0; i < sizep; i++) {
                int off = 3 * i;
                b[i] = bmp.palette[off];
                g[i] = bmp.palette[off + 1];
                r[i] = bmp.palette[off + 2];
            }
            return;
        }
        int sizep2 = bmp.palette.length / 4;
        if (sizep2 > 256) {
            sizep2 = 256;
        }
        byte[] r2 = new byte[sizep2];
        byte[] g2 = new byte[sizep2];
        byte[] b2 = new byte[sizep2];
        for (int i2 = 0; i2 < sizep2; i2++) {
            int off2 = 4 * i2;
            b2[i2] = bmp.palette[off2];
            g2[i2] = bmp.palette[off2 + 1];
            r2[i2] = bmp.palette[off2 + 2];
        }
    }

    private static byte[] getPalette(int group, BmpParameters bmp) {
        if (bmp.palette == null) {
            return null;
        }
        byte[] np = new byte[(bmp.palette.length / group) * 3];
        int e = bmp.palette.length / group;
        for (int k = 0; k < e; k++) {
            int src = k * group;
            int dest = k * 3;
            int src2 = src + 1;
            np[dest + 2] = bmp.palette[src];
            np[dest + 1] = bmp.palette[src2];
            np[dest] = bmp.palette[src2 + 1];
        }
        return np;
    }

    private static boolean getImage(BmpParameters bmp) throws IOException {
        switch (bmp.imageType) {
            case 0:
                read1Bit(3, bmp);
                return true;
            case 1:
                read4Bit(3, bmp);
                return true;
            case 2:
                read8Bit(3, bmp);
                return true;
            case 3:
                byte[] bdata = new byte[bmp.width * bmp.height * 3];
                read24Bit(bdata, bmp);
                RawImageHelper.updateRawImageParameters(bmp.image, bmp.width, bmp.height, 3, 8, bdata);
                return true;
            case 4:
                read1Bit(4, bmp);
                return true;
            case 5:
                switch ((int) bmp.compression) {
                    case 0:
                        read4Bit(4, bmp);
                        return true;
                    case 2:
                        readRLE4(bmp);
                        return true;
                    default:
                        throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.InvalidBmpFileCompression);
                }
            case 6:
                switch ((int) bmp.compression) {
                    case 0:
                        read8Bit(4, bmp);
                        return true;
                    case 1:
                        readRLE8(bmp);
                        return true;
                    default:
                        throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.InvalidBmpFileCompression);
                }
            case 7:
                byte[] bdata2 = new byte[bmp.width * bmp.height * 3];
                read24Bit(bdata2, bmp);
                RawImageHelper.updateRawImageParameters(bmp.image, bmp.width, bmp.height, 3, 8, bdata2);
                return true;
            case 8:
                read1632Bit(false, bmp);
                return true;
            case 9:
                read1632Bit(true, bmp);
                return true;
            case 10:
                read1Bit(4, bmp);
                return true;
            case 11:
                switch ((int) bmp.compression) {
                    case 0:
                        read4Bit(4, bmp);
                        return true;
                    case 2:
                        readRLE4(bmp);
                        return true;
                    default:
                        throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.InvalidBmpFileCompression);
                }
            case 12:
                switch ((int) bmp.compression) {
                    case 0:
                        read8Bit(4, bmp);
                        return true;
                    case 1:
                        readRLE8(bmp);
                        return true;
                    default:
                        throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.InvalidBmpFileCompression);
                }
            case 13:
                read1632Bit(false, bmp);
                return true;
            case 14:
                byte[] bdata3 = new byte[bmp.width * bmp.height * 3];
                read24Bit(bdata3, bmp);
                RawImageHelper.updateRawImageParameters(bmp.image, bmp.width, bmp.height, 3, 8, bdata3);
                return true;
            case 15:
                read1632Bit(true, bmp);
                return true;
            default:
                return false;
        }
    }

    private static void indexedModel(byte[] bdata, int bpc, int paletteEntries, BmpParameters bmp) {
        RawImageHelper.updateRawImageParameters(bmp.image, bmp.width, bmp.height, 1, bpc, bdata);
        byte[] np = getPalette(paletteEntries, bmp);
        int len = np.length;
        Object[] colorSpace = {"/Indexed", "/DeviceRGB", Integer.valueOf((len / 3) - 1), PdfEncodings.convertToString(np, null)};
        bmp.additional = new HashMap();
        bmp.additional.put("ColorSpace", colorSpace);
    }

    private static void readPalette(int sizeOfPalette, BmpParameters bmp) throws IOException {
        if (sizeOfPalette == 0) {
            return;
        }
        bmp.palette = new byte[sizeOfPalette];
        int i = 0;
        while (true) {
            int bytesRead = i;
            if (bytesRead < sizeOfPalette) {
                int r = bmp.inputStream.read(bmp.palette, bytesRead, sizeOfPalette - bytesRead);
                if (r < 0) {
                    throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.IncompletePalette);
                }
                i = bytesRead + r;
            } else {
                bmp.properties.put("palette", bmp.palette);
                return;
            }
        }
    }

    private static void read1Bit(int paletteEntries, BmpParameters bmp) throws IOException {
        byte[] bdata = new byte[((bmp.width + 7) / 8) * bmp.height];
        int padding = 0;
        int bytesPerScanline = (int) Math.ceil(bmp.width / 8.0d);
        int remainder = bytesPerScanline % 4;
        if (remainder != 0) {
            padding = 4 - remainder;
        }
        int imSize = (bytesPerScanline + padding) * bmp.height;
        byte[] values = new byte[imSize];
        int i = 0;
        while (true) {
            int bytesRead = i;
            if (bytesRead >= imSize) {
                break;
            } else {
                i = bytesRead + bmp.inputStream.read(values, bytesRead, imSize - bytesRead);
            }
        }
        if (bmp.isBottomUp) {
            for (int i2 = 0; i2 < bmp.height; i2++) {
                System.arraycopy(values, imSize - ((i2 + 1) * (bytesPerScanline + padding)), bdata, i2 * bytesPerScanline, bytesPerScanline);
            }
        } else {
            for (int i3 = 0; i3 < bmp.height; i3++) {
                System.arraycopy(values, i3 * (bytesPerScanline + padding), bdata, i3 * bytesPerScanline, bytesPerScanline);
            }
        }
        indexedModel(bdata, 1, paletteEntries, bmp);
    }

    private static void read4Bit(int paletteEntries, BmpParameters bmp) throws IOException {
        byte[] bdata = new byte[((bmp.width + 1) / 2) * bmp.height];
        int padding = 0;
        int bytesPerScanline = (int) Math.ceil(bmp.width / 2.0d);
        int remainder = bytesPerScanline % 4;
        if (remainder != 0) {
            padding = 4 - remainder;
        }
        int imSize = (bytesPerScanline + padding) * bmp.height;
        byte[] values = new byte[imSize];
        int i = 0;
        while (true) {
            int bytesRead = i;
            if (bytesRead >= imSize) {
                break;
            } else {
                i = bytesRead + bmp.inputStream.read(values, bytesRead, imSize - bytesRead);
            }
        }
        if (bmp.isBottomUp) {
            for (int i2 = 0; i2 < bmp.height; i2++) {
                System.arraycopy(values, imSize - ((i2 + 1) * (bytesPerScanline + padding)), bdata, i2 * bytesPerScanline, bytesPerScanline);
            }
        } else {
            for (int i3 = 0; i3 < bmp.height; i3++) {
                System.arraycopy(values, i3 * (bytesPerScanline + padding), bdata, i3 * bytesPerScanline, bytesPerScanline);
            }
        }
        indexedModel(bdata, 4, paletteEntries, bmp);
    }

    private static void read8Bit(int paletteEntries, BmpParameters bmp) throws IOException {
        byte[] bdata = new byte[bmp.width * bmp.height];
        int padding = 0;
        int bitsPerScanline = bmp.width * 8;
        if (bitsPerScanline % 32 != 0) {
            int padding2 = (((bitsPerScanline / 32) + 1) * 32) - bitsPerScanline;
            padding = (int) Math.ceil(padding2 / 8.0d);
        }
        int imSize = (bmp.width + padding) * bmp.height;
        byte[] values = new byte[imSize];
        int i = 0;
        while (true) {
            int bytesRead = i;
            if (bytesRead >= imSize) {
                break;
            } else {
                i = bytesRead + bmp.inputStream.read(values, bytesRead, imSize - bytesRead);
            }
        }
        if (bmp.isBottomUp) {
            for (int i2 = 0; i2 < bmp.height; i2++) {
                System.arraycopy(values, imSize - ((i2 + 1) * (bmp.width + padding)), bdata, i2 * bmp.width, bmp.width);
            }
        } else {
            for (int i3 = 0; i3 < bmp.height; i3++) {
                System.arraycopy(values, i3 * (bmp.width + padding), bdata, i3 * bmp.width, bmp.width);
            }
        }
        indexedModel(bdata, 8, paletteEntries, bmp);
    }

    private static void read24Bit(byte[] bdata, BmpParameters bmp) throws IOException {
        int r;
        int padding = 0;
        int bitsPerScanline = bmp.width * 24;
        if (bitsPerScanline % 32 != 0) {
            int padding2 = (((bitsPerScanline / 32) + 1) * 32) - bitsPerScanline;
            padding = (int) Math.ceil(padding2 / 8.0d);
        }
        int imSize = (((bmp.width * 3) + 3) / 4) * 4 * bmp.height;
        byte[] values = new byte[imSize];
        int i = 0;
        while (true) {
            int bytesRead = i;
            if (bytesRead >= imSize || (r = bmp.inputStream.read(values, bytesRead, imSize - bytesRead)) < 0) {
                break;
            } else {
                i = bytesRead + r;
            }
        }
        int l = 0;
        if (bmp.isBottomUp) {
            int max = ((bmp.width * bmp.height) * 3) - 1;
            int count = -padding;
            for (int i2 = 0; i2 < bmp.height; i2++) {
                int l2 = (max - (((i2 + 1) * bmp.width) * 3)) + 1;
                count += padding;
                for (int j = 0; j < bmp.width; j++) {
                    int i3 = count;
                    int count2 = count + 1;
                    bdata[l2 + 2] = values[i3];
                    int count3 = count2 + 1;
                    bdata[l2 + 1] = values[count2];
                    count = count3 + 1;
                    bdata[l2] = values[count3];
                    l2 += 3;
                }
            }
            return;
        }
        int count4 = -padding;
        for (int i4 = 0; i4 < bmp.height; i4++) {
            count4 += padding;
            for (int j2 = 0; j2 < bmp.width; j2++) {
                int i5 = count4;
                int count5 = count4 + 1;
                bdata[l + 2] = values[i5];
                int count6 = count5 + 1;
                bdata[l + 1] = values[count5];
                count4 = count6 + 1;
                bdata[l] = values[count6];
                l += 3;
            }
        }
    }

    private static int findMask(int mask) {
        for (int k = 0; k < 32 && (mask & 1) != 1; k++) {
            mask >>>= 1;
        }
        return mask;
    }

    private static int findShift(int mask) {
        int k = 0;
        while (k < 32 && (mask & 1) != 1) {
            mask >>>= 1;
            k++;
        }
        return k;
    }

    private static void read1632Bit(boolean is32, BmpParameters bmp) throws IOException {
        int word;
        int word2;
        int red_mask = findMask(bmp.redMask);
        int red_shift = findShift(bmp.redMask);
        int red_factor = red_mask + 1;
        int green_mask = findMask(bmp.greenMask);
        int green_shift = findShift(bmp.greenMask);
        int green_factor = green_mask + 1;
        int blue_mask = findMask(bmp.blueMask);
        int blue_shift = findShift(bmp.blueMask);
        int blue_factor = blue_mask + 1;
        byte[] bdata = new byte[bmp.width * bmp.height * 3];
        int padding = 0;
        if (!is32) {
            int bitsPerScanline = bmp.width * 16;
            if (bitsPerScanline % 32 != 0) {
                padding = (int) Math.ceil(((((bitsPerScanline / 32) + 1) * 32) - bitsPerScanline) / 8.0d);
            }
        }
        int imSize = (int) bmp.imageSize;
        if (imSize == 0) {
        }
        int l = 0;
        if (bmp.isBottomUp) {
            for (int i = bmp.height - 1; i >= 0; i--) {
                int l2 = bmp.width * 3 * i;
                for (int j = 0; j < bmp.width; j++) {
                    if (is32) {
                        word2 = (int) readDWord(bmp.inputStream);
                    } else {
                        word2 = readWord(bmp.inputStream);
                    }
                    int v = word2;
                    int i2 = l2;
                    int l3 = l2 + 1;
                    bdata[i2] = (byte) ((((v >>> red_shift) & red_mask) * 256) / red_factor);
                    int l4 = l3 + 1;
                    bdata[l3] = (byte) ((((v >>> green_shift) & green_mask) * 256) / green_factor);
                    l2 = l4 + 1;
                    bdata[l4] = (byte) ((((v >>> blue_shift) & blue_mask) * 256) / blue_factor);
                }
                for (int m = 0; m < padding; m++) {
                    bmp.inputStream.read();
                }
            }
        } else {
            for (int i3 = 0; i3 < bmp.height; i3++) {
                for (int j2 = 0; j2 < bmp.width; j2++) {
                    if (is32) {
                        word = (int) readDWord(bmp.inputStream);
                    } else {
                        word = readWord(bmp.inputStream);
                    }
                    int v2 = word;
                    int i4 = l;
                    int l5 = l + 1;
                    bdata[i4] = (byte) ((((v2 >>> red_shift) & red_mask) * 256) / red_factor);
                    int l6 = l5 + 1;
                    bdata[l5] = (byte) ((((v2 >>> green_shift) & green_mask) * 256) / green_factor);
                    l = l6 + 1;
                    bdata[l6] = (byte) ((((v2 >>> blue_shift) & blue_mask) * 256) / blue_factor);
                }
                for (int m2 = 0; m2 < padding; m2++) {
                    bmp.inputStream.read();
                }
            }
        }
        RawImageHelper.updateRawImageParameters(bmp.image, bmp.width, bmp.height, 3, 8, bdata);
    }

    private static void readRLE8(BmpParameters bmp) throws IOException {
        int imSize = (int) bmp.imageSize;
        if (imSize == 0) {
            imSize = (int) (bmp.bitmapFileSize - bmp.bitmapOffset);
        }
        byte[] values = new byte[imSize];
        int i = 0;
        while (true) {
            int bytesRead = i;
            if (bytesRead >= imSize) {
                break;
            } else {
                i = bytesRead + bmp.inputStream.read(values, bytesRead, imSize - bytesRead);
            }
        }
        byte[] val = decodeRLE(true, values, bmp);
        int imSize2 = bmp.width * bmp.height;
        if (bmp.isBottomUp) {
            byte[] temp = new byte[val.length];
            int bytesPerScanline = bmp.width;
            for (int i2 = 0; i2 < bmp.height; i2++) {
                System.arraycopy(val, imSize2 - ((i2 + 1) * bytesPerScanline), temp, i2 * bytesPerScanline, bytesPerScanline);
            }
            val = temp;
        }
        indexedModel(val, 8, 4, bmp);
    }

    private static void readRLE4(BmpParameters bmp) throws IOException {
        int imSize = (int) bmp.imageSize;
        if (imSize == 0) {
            imSize = (int) (bmp.bitmapFileSize - bmp.bitmapOffset);
        }
        byte[] values = new byte[imSize];
        int i = 0;
        while (true) {
            int bytesRead = i;
            if (bytesRead >= imSize) {
                break;
            } else {
                i = bytesRead + bmp.inputStream.read(values, bytesRead, imSize - bytesRead);
            }
        }
        byte[] val = decodeRLE(false, values, bmp);
        if (bmp.isBottomUp) {
            val = new byte[bmp.width * bmp.height];
            int l = 0;
            for (int i2 = bmp.height - 1; i2 >= 0; i2--) {
                int index = i2 * bmp.width;
                int lineEnd = l + bmp.width;
                while (l != lineEnd) {
                    int i3 = l;
                    l++;
                    int i4 = index;
                    index++;
                    val[i3] = val[i4];
                }
            }
        }
        int stride = (bmp.width + 1) / 2;
        byte[] bdata = new byte[stride * bmp.height];
        int ptr = 0;
        int sh = 0;
        for (int h = 0; h < bmp.height; h++) {
            for (int w = 0; w < bmp.width; w++) {
                if ((w & 1) == 0) {
                    int i5 = ptr;
                    ptr++;
                    bdata[sh + (w / 2)] = (byte) (val[i5] << 4);
                } else {
                    int i6 = sh + (w / 2);
                    int i7 = ptr;
                    ptr++;
                    bdata[i6] = (byte) (bdata[i6] | ((byte) (val[i7] & 15)));
                }
            }
            sh += stride;
        }
        indexedModel(bdata, 4, 4, bmp);
    }

    private static byte[] decodeRLE(boolean is8, byte[] values, BmpParameters bmp) {
        int ptr;
        int count;
        byte[] val = new byte[bmp.width * bmp.height];
        int ptr2 = 0;
        int x = 0;
        int q = 0;
        int y = 0;
        while (y < bmp.height && ptr2 < values.length) {
            try {
                int i = ptr2;
                ptr = ptr2 + 1;
                count = values[i] & 255;
            } catch (Exception e) {
            }
            if (count != 0) {
                ptr2 = ptr + 1;
                int bt = values[ptr] & 255;
                if (is8) {
                    for (int i2 = count; i2 != 0; i2--) {
                        int i3 = q;
                        q++;
                        val[i3] = (byte) bt;
                    }
                } else {
                    for (int i4 = 0; i4 < count; i4++) {
                        int i5 = q;
                        q++;
                        val[i5] = (byte) ((i4 & 1) == 1 ? bt & 15 : (bt >>> 4) & 15);
                    }
                }
                x += count;
            } else {
                ptr2 = ptr + 1;
                int count2 = values[ptr] & 255;
                if (count2 != 1) {
                    switch (count2) {
                        case 0:
                            x = 0;
                            y++;
                            q = y * bmp.width;
                            break;
                        case 2:
                            int ptr3 = ptr2 + 1;
                            x += values[ptr2] & 255;
                            ptr2 = ptr3 + 1;
                            y += values[ptr3] & 255;
                            q = (y * bmp.width) + x;
                            break;
                        default:
                            if (is8) {
                                for (int i6 = count2; i6 != 0; i6--) {
                                    int i7 = q;
                                    q++;
                                    int i8 = ptr2;
                                    ptr2++;
                                    val[i7] = (byte) (values[i8] & 255);
                                }
                            } else {
                                int bt2 = 0;
                                for (int i9 = 0; i9 < count2; i9++) {
                                    if ((i9 & 1) == 0) {
                                        int i10 = ptr2;
                                        ptr2++;
                                        bt2 = values[i10] & 255;
                                    }
                                    int i11 = q;
                                    q++;
                                    val[i11] = (byte) ((i9 & 1) == 1 ? bt2 & 15 : (bt2 >>> 4) & 15);
                                }
                            }
                            x += count2;
                            if (is8) {
                                if ((count2 & 1) == 1) {
                                    ptr2++;
                                    break;
                                } else {
                                    break;
                                }
                            } else if ((count2 & 3) != 1 && (count2 & 3) != 2) {
                                break;
                            } else {
                                ptr2++;
                                break;
                            }
                    }
                } else {
                    return val;
                }
            }
        }
        return val;
    }

    private static int readUnsignedByte(InputStream stream) throws IOException {
        return stream.read() & 255;
    }

    private static int readUnsignedShort(InputStream stream) throws IOException {
        int b1 = readUnsignedByte(stream);
        int b2 = readUnsignedByte(stream);
        return ((b2 << 8) | b1) & 65535;
    }

    private static int readShort(InputStream stream) throws IOException {
        int b1 = readUnsignedByte(stream);
        int b2 = readUnsignedByte(stream);
        return (b2 << 8) | b1;
    }

    private static int readWord(InputStream stream) throws IOException {
        return readUnsignedShort(stream);
    }

    private static long readUnsignedInt(InputStream stream) throws IOException {
        int b1 = readUnsignedByte(stream);
        int b2 = readUnsignedByte(stream);
        int b3 = readUnsignedByte(stream);
        int b4 = readUnsignedByte(stream);
        long l = (b4 << 24) | (b3 << 16) | (b2 << 8) | b1;
        return l & (-1);
    }

    private static int readInt(InputStream stream) throws IOException {
        int b1 = readUnsignedByte(stream);
        int b2 = readUnsignedByte(stream);
        int b3 = readUnsignedByte(stream);
        int b4 = readUnsignedByte(stream);
        return (b4 << 24) | (b3 << 16) | (b2 << 8) | b1;
    }

    private static long readDWord(InputStream stream) throws IOException {
        return readUnsignedInt(stream);
    }

    private static int readLong(InputStream stream) throws IOException {
        return readInt(stream);
    }
}

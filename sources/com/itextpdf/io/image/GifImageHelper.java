package com.itextpdf.io.image;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.util.StreamUtil;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.ddf.EscherProperties;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/image/GifImageHelper.class */
public final class GifImageHelper {
    static final int MAX_STACK_SIZE = 4096;

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/image/GifImageHelper$GifParameters.class */
    private static class GifParameters {
        InputStream input;
        boolean gctFlag;
        int bgIndex;
        int bgColor;
        int pixelAspect;
        boolean lctFlag;
        boolean interlace;
        int lctSize;
        int ix;
        int iy;
        int iw;
        int ih;
        byte[] block = new byte[256];
        int blockSize = 0;
        int dispose = 0;
        boolean transparency = false;
        int delay = 0;
        int transIndex;
        short[] prefix;
        byte[] suffix;
        byte[] pixelStack;
        byte[] pixels;
        byte[] m_out;
        int m_bpc;
        int m_gbpc;
        byte[] m_global_table;
        byte[] m_local_table;
        byte[] m_curr_table;
        int m_line_stride;
        byte[] fromData;
        URL fromUrl;
        int currentFrame;
        GifImageData image;

        public GifParameters(GifImageData image) {
            this.image = image;
        }
    }

    public static void processImage(GifImageData image) {
        processImage(image, -1);
    }

    public static void processImage(GifImageData image, int lastFrameNumber) {
        GifParameters gif = new GifParameters(image);
        try {
            if (image.getData() == null) {
                image.loadData();
            }
            InputStream gifStream = new ByteArrayInputStream(image.getData());
            process(gifStream, gif, lastFrameNumber);
        } catch (IOException e) {
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.GifImageException, (Throwable) e);
        }
    }

    private static void process(InputStream stream, GifParameters gif, int lastFrameNumber) throws IOException {
        gif.input = stream;
        readHeader(gif);
        readContents(gif, lastFrameNumber);
        if (gif.currentFrame <= lastFrameNumber) {
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.CannotFind1Frame).setMessageParams(Integer.valueOf(lastFrameNumber));
        }
    }

    private static void readHeader(GifParameters gif) throws IOException {
        StringBuilder id = new StringBuilder("");
        for (int i = 0; i < 6; i++) {
            id.append((char) gif.input.read());
        }
        if (!id.toString().startsWith("GIF8")) {
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.GifSignatureNotFound);
        }
        readLSD(gif);
        if (gif.gctFlag) {
            gif.m_global_table = readColorTable(gif.m_gbpc, gif);
        }
    }

    private static void readLSD(GifParameters gif) throws IOException {
        gif.image.setLogicalWidth(readShort(gif));
        gif.image.setLogicalHeight(readShort(gif));
        int packed = gif.input.read();
        gif.gctFlag = (packed & 128) != 0;
        gif.m_gbpc = (packed & 7) + 1;
        gif.bgIndex = gif.input.read();
        gif.pixelAspect = gif.input.read();
    }

    private static int readShort(GifParameters gif) throws IOException {
        return gif.input.read() | (gif.input.read() << 8);
    }

    private static int readBlock(GifParameters gif) throws IOException {
        gif.blockSize = gif.input.read();
        if (gif.blockSize <= 0) {
            gif.blockSize = 0;
            return 0;
        }
        gif.blockSize = gif.input.read(gif.block, 0, gif.blockSize);
        return gif.blockSize;
    }

    private static byte[] readColorTable(int bpc, GifParameters gif) throws IOException {
        int ncolors = 1 << bpc;
        int nbytes = 3 * ncolors;
        byte[] table = new byte[(1 << newBpc(bpc)) * 3];
        StreamUtil.readFully(gif.input, table, 0, nbytes);
        return table;
    }

    private static int newBpc(int bpc) {
        switch (bpc) {
            case 1:
            case 2:
            case 4:
                return bpc;
            case 3:
                return 4;
            default:
                return 8;
        }
    }

    private static void readContents(GifParameters gif, int lastFrameNumber) throws IOException {
        boolean done = false;
        gif.currentFrame = 0;
        while (!done) {
            int code = gif.input.read();
            switch (code) {
                case 33:
                    int code2 = gif.input.read();
                    switch (code2) {
                        case EscherProperties.GEOTEXT__NOMEASUREALONGPATH /* 249 */:
                            readGraphicControlExt(gif);
                            break;
                        case 255:
                            readBlock(gif);
                            skip(gif);
                            break;
                        default:
                            skip(gif);
                            break;
                    }
                case 44:
                    readFrame(gif);
                    if (gif.currentFrame == lastFrameNumber) {
                        done = true;
                    }
                    gif.currentFrame++;
                    break;
                default:
                    done = true;
                    break;
            }
        }
    }

    private static void readFrame(GifParameters gif) throws IOException {
        gif.ix = readShort(gif);
        gif.iy = readShort(gif);
        gif.iw = readShort(gif);
        gif.ih = readShort(gif);
        int packed = gif.input.read();
        gif.lctFlag = (packed & 128) != 0;
        gif.interlace = (packed & 64) != 0;
        gif.lctSize = 2 << (packed & 7);
        gif.m_bpc = newBpc(gif.m_gbpc);
        if (gif.lctFlag) {
            gif.m_curr_table = readColorTable((packed & 7) + 1, gif);
            gif.m_bpc = newBpc((packed & 7) + 1);
        } else {
            gif.m_curr_table = gif.m_global_table;
        }
        if (gif.transparency && gif.transIndex >= gif.m_curr_table.length / 3) {
            gif.transparency = false;
        }
        if (gif.transparency && gif.m_bpc == 1) {
            byte[] tp = new byte[12];
            System.arraycopy(gif.m_curr_table, 0, tp, 0, 6);
            gif.m_curr_table = tp;
            gif.m_bpc = 2;
        }
        boolean skipZero = decodeImageData(gif);
        if (!skipZero) {
            skip(gif);
        }
        try {
            int len = gif.m_curr_table.length;
            Object[] colorspace = {"/Indexed", "/DeviceRGB", Integer.valueOf((len / 3) - 1), PdfEncodings.convertToString(gif.m_curr_table, null)};
            Map<String, Object> ad = new HashMap<>();
            ad.put("ColorSpace", colorspace);
            RawImageData img = new RawImageData(gif.m_out, ImageType.GIF);
            RawImageHelper.updateRawImageParameters(img, gif.iw, gif.ih, 1, gif.m_bpc, gif.m_out);
            RawImageHelper.updateImageAttributes(img, ad);
            gif.image.addFrame(img);
            if (gif.transparency) {
                img.setTransparency(new int[]{gif.transIndex, gif.transIndex});
            }
        } catch (Exception e) {
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.GifImageException, (Throwable) e);
        }
    }

    private static boolean decodeImageData(GifParameters gif) throws IOException {
        int npix = gif.iw * gif.ih;
        boolean skipZero = false;
        if (gif.prefix == null) {
            gif.prefix = new short[4096];
        }
        if (gif.suffix == null) {
            gif.suffix = new byte[4096];
        }
        if (gif.pixelStack == null) {
            gif.pixelStack = new byte[4097];
        }
        gif.m_line_stride = ((gif.iw * gif.m_bpc) + 7) / 8;
        gif.m_out = new byte[gif.m_line_stride * gif.ih];
        int pass = 1;
        int inc = gif.interlace ? 8 : 1;
        int line = 0;
        int xpos = 0;
        int data_size = gif.input.read();
        int clear = 1 << data_size;
        int end_of_information = clear + 1;
        int available = clear + 2;
        int old_code = -1;
        int code_size = data_size + 1;
        int code_mask = (1 << code_size) - 1;
        for (int code = 0; code < clear; code++) {
            gif.prefix[code] = 0;
            gif.suffix[code] = (byte) code;
        }
        int bi = 0;
        int top = 0;
        int first = 0;
        int count = 0;
        int bits = 0;
        int datum = 0;
        int i = 0;
        while (true) {
            if (i < npix) {
                if (top == 0) {
                    if (bits < code_size) {
                        if (count == 0) {
                            count = readBlock(gif);
                            if (count <= 0) {
                                skipZero = true;
                            } else {
                                bi = 0;
                            }
                        }
                        datum += (gif.block[bi] & 255) << bits;
                        bits += 8;
                        bi++;
                        count--;
                    } else {
                        int code2 = datum & code_mask;
                        datum >>= code_size;
                        bits -= code_size;
                        if (code2 <= available && code2 != end_of_information) {
                            if (code2 != clear) {
                                if (old_code == -1) {
                                    int i2 = top;
                                    top++;
                                    gif.pixelStack[i2] = gif.suffix[code2];
                                    old_code = code2;
                                    first = code2;
                                } else {
                                    if (code2 == available) {
                                        int i3 = top;
                                        top++;
                                        gif.pixelStack[i3] = (byte) first;
                                        code2 = old_code;
                                    }
                                    while (code2 > clear) {
                                        int i4 = top;
                                        top++;
                                        gif.pixelStack[i4] = gif.suffix[code2];
                                        code2 = gif.prefix[code2];
                                    }
                                    first = gif.suffix[code2] & 255;
                                    if (available < 4096) {
                                        int i5 = top;
                                        top++;
                                        gif.pixelStack[i5] = (byte) first;
                                        gif.prefix[available] = (short) old_code;
                                        gif.suffix[available] = (byte) first;
                                        available++;
                                        if ((available & code_mask) == 0 && available < 4096) {
                                            code_size++;
                                            code_mask += available;
                                        }
                                        old_code = code2;
                                    }
                                }
                            } else {
                                code_size = data_size + 1;
                                code_mask = (1 << code_size) - 1;
                                available = clear + 2;
                                old_code = -1;
                            }
                        }
                    }
                }
                top--;
                i++;
                setPixel(xpos, line, gif.pixelStack[top], gif);
                xpos++;
                if (xpos >= gif.iw) {
                    xpos = 0;
                    line += inc;
                    if (line >= gif.ih) {
                        if (gif.interlace) {
                            do {
                                pass++;
                                switch (pass) {
                                    case 2:
                                        line = 4;
                                        break;
                                    case 3:
                                        line = 2;
                                        inc = 4;
                                        break;
                                    case 4:
                                        line = 1;
                                        inc = 2;
                                        break;
                                    default:
                                        line = gif.ih - 1;
                                        inc = 0;
                                        break;
                                }
                            } while (line >= gif.ih);
                        } else {
                            line = gif.ih - 1;
                            inc = 0;
                        }
                    }
                }
            }
        }
        return skipZero;
    }

    private static void setPixel(int x, int y, int v, GifParameters gif) {
        if (gif.m_bpc == 8) {
            gif.m_out[x + (gif.iw * y)] = (byte) v;
        } else {
            int pos = (gif.m_line_stride * y) + (x / (8 / gif.m_bpc));
            int vout = v << ((8 - (gif.m_bpc * (x % (8 / gif.m_bpc)))) - gif.m_bpc);
            byte[] bArr = gif.m_out;
            bArr[pos] = (byte) (bArr[pos] | ((byte) vout));
        }
    }

    private static void readGraphicControlExt(GifParameters gif) throws IOException {
        gif.input.read();
        int packed = gif.input.read();
        gif.dispose = (packed & 28) >> 2;
        if (gif.dispose == 0) {
            gif.dispose = 1;
        }
        gif.transparency = (packed & 1) != 0;
        gif.delay = readShort(gif) * 10;
        gif.transIndex = gif.input.read();
        gif.input.read();
    }

    private static void skip(GifParameters gif) throws IOException {
        do {
            readBlock(gif);
        } while (gif.blockSize > 0);
    }
}

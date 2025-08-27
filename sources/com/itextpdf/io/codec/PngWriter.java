package com.itextpdf.io.codec;

import com.itextpdf.io.image.PngImageHelper;
import com.itextpdf.io.source.ByteUtils;
import com.itextpdf.io.source.DeflaterOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/codec/PngWriter.class */
public class PngWriter {
    private static final byte[] PNG_SIGNTURE = {-119, 80, 78, 71, 13, 10, 26, 10};
    private static final byte[] IHDR = ByteUtils.getIsoBytes(PngImageHelper.IHDR);
    private static final byte[] PLTE = ByteUtils.getIsoBytes(PngImageHelper.PLTE);
    private static final byte[] IDAT = ByteUtils.getIsoBytes(PngImageHelper.IDAT);
    private static final byte[] IEND = ByteUtils.getIsoBytes(PngImageHelper.IEND);
    private static final byte[] iCCP = ByteUtils.getIsoBytes(PngImageHelper.iCCP);
    private static int[] crc_table;
    private OutputStream outp;

    public PngWriter(OutputStream outp) throws IOException {
        this.outp = outp;
        outp.write(PNG_SIGNTURE);
    }

    public void writeHeader(int width, int height, int bitDepth, int colorType) throws IOException {
        ByteArrayOutputStream ms = new ByteArrayOutputStream();
        outputInt(width, ms);
        outputInt(height, ms);
        ms.write(bitDepth);
        ms.write(colorType);
        ms.write(0);
        ms.write(0);
        ms.write(0);
        writeChunk(IHDR, ms.toByteArray());
    }

    public void writeEnd() throws IOException {
        writeChunk(IEND, new byte[0]);
    }

    public void writeData(byte[] data, int stride) throws IOException {
        int k;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DeflaterOutputStream zip = new DeflaterOutputStream(stream);
        int i = 0;
        while (true) {
            k = i;
            if (k >= data.length - stride) {
                break;
            }
            zip.write(0);
            zip.write(data, k, stride);
            i = k + stride;
        }
        int remaining = data.length - k;
        if (remaining > 0) {
            zip.write(0);
            zip.write(data, k, remaining);
        }
        zip.close();
        writeChunk(IDAT, stream.toByteArray());
    }

    public void writePalette(byte[] data) throws IOException {
        writeChunk(PLTE, data);
    }

    public void writeIccProfile(byte[] data) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.write(73);
        stream.write(67);
        stream.write(67);
        stream.write(0);
        stream.write(0);
        DeflaterOutputStream zip = new DeflaterOutputStream(stream);
        zip.write(data);
        zip.close();
        writeChunk(iCCP, stream.toByteArray());
    }

    private static void make_crc_table() {
        int i;
        if (crc_table != null) {
            return;
        }
        int[] crc2 = new int[256];
        for (int n = 0; n < 256; n++) {
            int c = n;
            for (int k = 0; k < 8; k++) {
                if ((c & 1) != 0) {
                    i = (-306674912) ^ (c >>> 1);
                } else {
                    i = c >>> 1;
                }
                c = i;
            }
            crc2[n] = c;
        }
        crc_table = crc2;
    }

    private static int update_crc(int crc, byte[] buf, int offset, int len) {
        int c = crc;
        if (crc_table == null) {
            make_crc_table();
        }
        for (int n = 0; n < len; n++) {
            c = crc_table[(c ^ buf[n + offset]) & 255] ^ (c >>> 8);
        }
        return c;
    }

    private static int crc(byte[] buf, int offset, int len) {
        return update_crc(-1, buf, offset, len) ^ (-1);
    }

    private static int crc(byte[] buf) {
        return update_crc(-1, buf, 0, buf.length) ^ (-1);
    }

    public void outputInt(int n) throws IOException {
        outputInt(n, this.outp);
    }

    public static void outputInt(int n, OutputStream s) throws IOException {
        s.write((byte) (n >> 24));
        s.write((byte) (n >> 16));
        s.write((byte) (n >> 8));
        s.write((byte) n);
    }

    public void writeChunk(byte[] chunkType, byte[] data) throws IOException {
        outputInt(data.length);
        this.outp.write(chunkType, 0, 4);
        this.outp.write(data);
        int c = update_crc(-1, chunkType, 0, chunkType.length);
        outputInt(update_crc(c, data, 0, data.length) ^ (-1));
    }
}

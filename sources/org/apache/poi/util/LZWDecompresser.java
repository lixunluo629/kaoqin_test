package org.apache.poi.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* loaded from: poi-3.17.jar:org/apache/poi/util/LZWDecompresser.class */
public abstract class LZWDecompresser {
    private final boolean maskMeansCompressed;
    private final int codeLengthIncrease;
    private final boolean positionIsBigEndian;

    protected abstract int populateDictionary(byte[] bArr);

    protected abstract int adjustDictionaryOffset(int i);

    protected LZWDecompresser(boolean maskMeansCompressed, int codeLengthIncrease, boolean positionIsBigEndian) {
        this.maskMeansCompressed = maskMeansCompressed;
        this.codeLengthIncrease = codeLengthIncrease;
        this.positionIsBigEndian = positionIsBigEndian;
    }

    public byte[] decompress(InputStream src) throws IOException {
        ByteArrayOutputStream res = new ByteArrayOutputStream();
        decompress(src, res);
        return res.toByteArray();
    }

    public void decompress(InputStream src, OutputStream res) throws IOException {
        int pntr;
        byte[] buffer = new byte[4096];
        int pos = populateDictionary(buffer);
        byte[] dataB = new byte[16 + this.codeLengthIncrease];
        while (true) {
            int flag = src.read();
            if (flag != -1) {
                int i = 1;
                while (true) {
                    int mask = i;
                    if (mask >= 256) {
                        break;
                    }
                    boolean isMaskSet = (flag & mask) > 0;
                    if (isMaskSet ^ this.maskMeansCompressed) {
                        int dataI = src.read();
                        if (dataI != -1) {
                            buffer[pos & 4095] = fromInt(dataI);
                            pos++;
                            res.write(new byte[]{fromInt(dataI)});
                        }
                    } else {
                        int dataIPt1 = src.read();
                        int dataIPt2 = src.read();
                        if (dataIPt1 == -1 || dataIPt2 == -1) {
                            break;
                        }
                        int len = (dataIPt2 & 15) + this.codeLengthIncrease;
                        if (this.positionIsBigEndian) {
                            pntr = (dataIPt1 << 4) + (dataIPt2 >> 4);
                        } else {
                            pntr = dataIPt1 + ((dataIPt2 & 240) << 4);
                        }
                        int pntr2 = adjustDictionaryOffset(pntr);
                        for (int i2 = 0; i2 < len; i2++) {
                            dataB[i2] = buffer[(pntr2 + i2) & 4095];
                            buffer[(pos + i2) & 4095] = dataB[i2];
                        }
                        res.write(dataB, 0, len);
                        pos += len;
                    }
                    i = mask << 1;
                }
            } else {
                return;
            }
        }
    }

    public static byte fromInt(int b) {
        return b < 128 ? (byte) b : (byte) (b - 256);
    }

    public static int fromByte(byte b) {
        if (b >= 0) {
            return b;
        }
        return b + 256;
    }
}

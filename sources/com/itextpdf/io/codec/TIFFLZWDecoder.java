package com.itextpdf.io.codec;

import com.itextpdf.io.IOException;
import org.apache.poi.ss.util.IEEEDouble;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/codec/TIFFLZWDecoder.class */
public class TIFFLZWDecoder {
    byte[][] stringTable;
    byte[] uncompData;
    int tableIndex;
    int bytePointer;
    int bitPointer;
    int dstIndex;
    int w;
    int h;
    int predictor;
    int samplesPerPixel;
    byte[] data = null;
    int bitsToGet = 9;
    int nextData = 0;
    int nextBits = 0;
    int[] andTable = {511, 1023, IEEEDouble.BIASED_EXPONENT_SPECIAL_VALUE, 4095};

    public TIFFLZWDecoder(int w, int predictor, int samplesPerPixel) {
        this.w = w;
        this.predictor = predictor;
        this.samplesPerPixel = samplesPerPixel;
    }

    public byte[] decode(byte[] data, byte[] uncompData, int h) {
        if (data[0] == 0 && data[1] == 1) {
            throw new IOException(IOException.Tiff50StyleLzwCodesAreNotSupported);
        }
        initializeStringTable();
        this.data = data;
        this.h = h;
        this.uncompData = uncompData;
        this.bytePointer = 0;
        this.bitPointer = 0;
        this.dstIndex = 0;
        this.nextData = 0;
        this.nextBits = 0;
        int i = 0;
        while (true) {
            int oldCode = i;
            int code = getNextCode();
            if (code == 257 || this.dstIndex >= uncompData.length) {
                break;
            }
            if (code == 256) {
                initializeStringTable();
                int code2 = getNextCode();
                if (code2 == 257) {
                    break;
                }
                writeString(this.stringTable[code2]);
                i = code2;
            } else if (code < this.tableIndex) {
                byte[] str = this.stringTable[code];
                writeString(str);
                addStringToTable(this.stringTable[oldCode], str[0]);
                i = code;
            } else {
                byte[] str2 = this.stringTable[oldCode];
                byte[] str3 = composeString(str2, str2[0]);
                writeString(str3);
                addStringToTable(str3);
                i = code;
            }
        }
        if (this.predictor == 2) {
            for (int j = 0; j < h; j++) {
                int count = this.samplesPerPixel * ((j * this.w) + 1);
                for (int i2 = this.samplesPerPixel; i2 < this.w * this.samplesPerPixel; i2++) {
                    int i3 = count;
                    uncompData[i3] = (byte) (uncompData[i3] + uncompData[count - this.samplesPerPixel]);
                    count++;
                }
            }
        }
        return uncompData;
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [byte[], byte[][]] */
    public void initializeStringTable() {
        this.stringTable = new byte[4096];
        for (int i = 0; i < 256; i++) {
            this.stringTable[i] = new byte[1];
            this.stringTable[i][0] = (byte) i;
        }
        this.tableIndex = 258;
        this.bitsToGet = 9;
    }

    public void writeString(byte[] str) {
        int max = this.uncompData.length - this.dstIndex;
        if (str.length < max) {
            max = str.length;
        }
        System.arraycopy(str, 0, this.uncompData, this.dstIndex, max);
        this.dstIndex += max;
    }

    public void addStringToTable(byte[] oldString, byte newString) {
        int length = oldString.length;
        byte[] str = new byte[length + 1];
        System.arraycopy(oldString, 0, str, 0, length);
        str[length] = newString;
        byte[][] bArr = this.stringTable;
        int i = this.tableIndex;
        this.tableIndex = i + 1;
        bArr[i] = str;
        if (this.tableIndex == 511) {
            this.bitsToGet = 10;
        } else if (this.tableIndex == 1023) {
            this.bitsToGet = 11;
        } else if (this.tableIndex == 2047) {
            this.bitsToGet = 12;
        }
    }

    public void addStringToTable(byte[] str) {
        byte[][] bArr = this.stringTable;
        int i = this.tableIndex;
        this.tableIndex = i + 1;
        bArr[i] = str;
        if (this.tableIndex == 511) {
            this.bitsToGet = 10;
        } else if (this.tableIndex == 1023) {
            this.bitsToGet = 11;
        } else if (this.tableIndex == 2047) {
            this.bitsToGet = 12;
        }
    }

    public byte[] composeString(byte[] oldString, byte newString) {
        int length = oldString.length;
        byte[] str = new byte[length + 1];
        System.arraycopy(oldString, 0, str, 0, length);
        str[length] = newString;
        return str;
    }

    public int getNextCode() {
        try {
            int i = this.nextData << 8;
            byte[] bArr = this.data;
            int i2 = this.bytePointer;
            this.bytePointer = i2 + 1;
            this.nextData = i | (bArr[i2] & 255);
            this.nextBits += 8;
            if (this.nextBits < this.bitsToGet) {
                int i3 = this.nextData << 8;
                byte[] bArr2 = this.data;
                int i4 = this.bytePointer;
                this.bytePointer = i4 + 1;
                this.nextData = i3 | (bArr2[i4] & 255);
                this.nextBits += 8;
            }
            int code = (this.nextData >> (this.nextBits - this.bitsToGet)) & this.andTable[this.bitsToGet - 9];
            this.nextBits -= this.bitsToGet;
            return code;
        } catch (ArrayIndexOutOfBoundsException e) {
            return 257;
        }
    }
}

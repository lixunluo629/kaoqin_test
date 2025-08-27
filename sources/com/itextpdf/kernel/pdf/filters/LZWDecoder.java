package com.itextpdf.kernel.pdf.filters;

import com.itextpdf.kernel.PdfException;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.poi.ss.util.IEEEDouble;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/filters/LZWDecoder.class */
public class LZWDecoder {
    byte[][] stringTable;
    OutputStream uncompData;
    int tableIndex;
    int bytePointer;
    int bitPointer;
    byte[] data = null;
    int bitsToGet = 9;
    int nextData = 0;
    int nextBits = 0;
    int[] andTable = {511, 1023, IEEEDouble.BIASED_EXPONENT_SPECIAL_VALUE, 4095};

    public void decode(byte[] data, OutputStream uncompData) throws IOException {
        if (data[0] == 0 && data[1] == 1) {
            throw new PdfException(PdfException.LzwFlavourNotSupported);
        }
        initializeStringTable();
        this.data = data;
        this.uncompData = uncompData;
        this.bytePointer = 0;
        this.bitPointer = 0;
        this.nextData = 0;
        this.nextBits = 0;
        int i = 0;
        while (true) {
            int oldCode = i;
            int code = getNextCode();
            if (code != 257) {
                if (code == 256) {
                    initializeStringTable();
                    int code2 = getNextCode();
                    if (code2 != 257) {
                        writeString(this.stringTable[code2]);
                        i = code2;
                    } else {
                        return;
                    }
                } else if (code < this.tableIndex) {
                    byte[] string = this.stringTable[code];
                    writeString(string);
                    addStringToTable(this.stringTable[oldCode], string[0]);
                    i = code;
                } else {
                    byte[] string2 = this.stringTable[oldCode];
                    byte[] string3 = composeString(string2, string2[0]);
                    writeString(string3);
                    addStringToTable(string3);
                    i = code;
                }
            } else {
                return;
            }
        }
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [byte[], byte[][]] */
    public void initializeStringTable() {
        this.stringTable = new byte[8192];
        for (int i = 0; i < 256; i++) {
            this.stringTable[i] = new byte[1];
            this.stringTable[i][0] = (byte) i;
        }
        this.tableIndex = 258;
        this.bitsToGet = 9;
    }

    public void writeString(byte[] string) throws IOException {
        try {
            this.uncompData.write(string);
        } catch (IOException e) {
            throw new PdfException(PdfException.LzwDecoderException, (Throwable) e);
        }
    }

    public void addStringToTable(byte[] oldString, byte newString) {
        int length = oldString.length;
        byte[] string = new byte[length + 1];
        System.arraycopy(oldString, 0, string, 0, length);
        string[length] = newString;
        byte[][] bArr = this.stringTable;
        int i = this.tableIndex;
        this.tableIndex = i + 1;
        bArr[i] = string;
        if (this.tableIndex == 511) {
            this.bitsToGet = 10;
        } else if (this.tableIndex == 1023) {
            this.bitsToGet = 11;
        } else if (this.tableIndex == 2047) {
            this.bitsToGet = 12;
        }
    }

    public void addStringToTable(byte[] string) {
        byte[][] bArr = this.stringTable;
        int i = this.tableIndex;
        this.tableIndex = i + 1;
        bArr[i] = string;
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
        byte[] string = new byte[length + 1];
        System.arraycopy(oldString, 0, string, 0, length);
        string[length] = newString;
        return string;
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

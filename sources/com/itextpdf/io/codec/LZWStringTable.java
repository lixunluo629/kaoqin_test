package com.itextpdf.io.codec;

import java.io.PrintStream;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/codec/LZWStringTable.class */
public class LZWStringTable {
    private static final int RES_CODES = 2;
    private static final short HASH_FREE = -1;
    private static final short NEXT_FIRST = -1;
    private static final int MAXBITS = 12;
    private static final int MAXSTR = 4096;
    private static final short HASHSIZE = 9973;
    private static final short HASHSTEP = 2039;
    short numStrings_;
    byte[] strChr_ = new byte[4096];
    short[] strNxt_ = new short[4096];
    int[] strLen_ = new int[4096];
    short[] strHsh_ = new short[HASHSIZE];

    public int AddCharString(short index, byte b) {
        int hshidx;
        if (this.numStrings_ >= 4096) {
            return 65535;
        }
        int iHash = Hash(index, b);
        while (true) {
            hshidx = iHash;
            if (this.strHsh_[hshidx] == -1) {
                break;
            }
            iHash = (hshidx + HASHSTEP) % HASHSIZE;
        }
        this.strHsh_[hshidx] = this.numStrings_;
        this.strChr_[this.numStrings_] = b;
        if (index == -1) {
            this.strNxt_[this.numStrings_] = -1;
            this.strLen_[this.numStrings_] = 1;
        } else {
            this.strNxt_[this.numStrings_] = index;
            this.strLen_[this.numStrings_] = this.strLen_[index] + 1;
        }
        short s = this.numStrings_;
        this.numStrings_ = (short) (s + 1);
        return s;
    }

    public short FindCharString(short index, byte b) {
        if (index == -1) {
            return (short) (b & 255);
        }
        int iHash = Hash(index, b);
        while (true) {
            int hshidx = iHash;
            short s = this.strHsh_[hshidx];
            if (s != -1) {
                if (this.strNxt_[s] == index && this.strChr_[s] == b) {
                    return s;
                }
                iHash = (hshidx + HASHSTEP) % HASHSIZE;
            } else {
                return (short) -1;
            }
        }
    }

    public void ClearTable(int codesize) {
        this.numStrings_ = (short) 0;
        for (int q = 0; q < HASHSIZE; q++) {
            this.strHsh_[q] = -1;
        }
        int w = (1 << codesize) + 2;
        for (int q2 = 0; q2 < w; q2++) {
            AddCharString((short) -1, (byte) q2);
        }
    }

    public static int Hash(short index, byte lastbyte) {
        return ((((short) (lastbyte << 8)) ^ index) & 65535) % HASHSIZE;
    }

    public int expandCode(byte[] buf, int offset, short code, int skipHead) {
        int expandLen;
        if (offset == -2 && skipHead == 1) {
            skipHead = 0;
        }
        if (code == -1 || skipHead == this.strLen_[code]) {
            return 0;
        }
        int codeLen = this.strLen_[code] - skipHead;
        int bufSpace = buf.length - offset;
        if (bufSpace > codeLen) {
            expandLen = codeLen;
        } else {
            expandLen = bufSpace;
        }
        int skipTail = codeLen - expandLen;
        int idx = offset + expandLen;
        while (idx > offset && code != -1) {
            skipTail--;
            if (skipTail < 0) {
                idx--;
                buf[idx] = this.strChr_[code];
            }
            code = this.strNxt_[code];
        }
        if (codeLen > expandLen) {
            return -expandLen;
        }
        return expandLen;
    }

    public void dump(PrintStream output) {
        for (int i = 258; i < this.numStrings_; i++) {
            output.println(" strNxt_[" + i + "] = " + ((int) this.strNxt_[i]) + " strChr_ " + Integer.toHexString(this.strChr_[i] & 255) + " strLen_ " + Integer.toHexString(this.strLen_[i]));
        }
    }
}

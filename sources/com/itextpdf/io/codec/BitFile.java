package com.itextpdf.io.codec;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/codec/BitFile.class */
class BitFile {
    OutputStream output;
    byte[] buffer = new byte[256];
    int index = 0;
    int bitsLeft = 8;
    boolean blocks;

    public BitFile(OutputStream output, boolean blocks) {
        this.blocks = false;
        this.output = output;
        this.blocks = blocks;
    }

    public void flush() throws IOException {
        int numBytes = this.index + (this.bitsLeft == 8 ? 0 : 1);
        if (numBytes > 0) {
            if (this.blocks) {
                this.output.write(numBytes);
            }
            this.output.write(this.buffer, 0, numBytes);
            this.buffer[0] = 0;
            this.index = 0;
            this.bitsLeft = 8;
        }
    }

    public void writeBits(int bits, int numbits) throws IOException {
        int bitsWritten = 0;
        do {
            if ((this.index == 254 && this.bitsLeft == 0) || this.index > 254) {
                if (this.blocks) {
                    this.output.write(255);
                }
                this.output.write(this.buffer, 0, 255);
                this.buffer[0] = 0;
                this.index = 0;
                this.bitsLeft = 8;
            }
            if (numbits <= this.bitsLeft) {
                if (this.blocks) {
                    byte[] bArr = this.buffer;
                    int i = this.index;
                    bArr[i] = (byte) (bArr[i] | ((byte) ((bits & ((1 << numbits) - 1)) << (8 - this.bitsLeft))));
                    bitsWritten += numbits;
                    this.bitsLeft -= numbits;
                    numbits = 0;
                } else {
                    byte[] bArr2 = this.buffer;
                    int i2 = this.index;
                    bArr2[i2] = (byte) (bArr2[i2] | ((byte) ((bits & ((1 << numbits) - 1)) << (this.bitsLeft - numbits))));
                    bitsWritten += numbits;
                    this.bitsLeft -= numbits;
                    numbits = 0;
                }
            } else if (this.blocks) {
                byte[] bArr3 = this.buffer;
                int i3 = this.index;
                bArr3[i3] = (byte) (bArr3[i3] | ((byte) ((bits & ((1 << this.bitsLeft) - 1)) << (8 - this.bitsLeft))));
                bitsWritten += this.bitsLeft;
                bits >>= this.bitsLeft;
                numbits -= this.bitsLeft;
                byte[] bArr4 = this.buffer;
                int i4 = this.index + 1;
                this.index = i4;
                bArr4[i4] = 0;
                this.bitsLeft = 8;
            } else {
                int topbits = (bits >>> (numbits - this.bitsLeft)) & ((1 << this.bitsLeft) - 1);
                byte[] bArr5 = this.buffer;
                int i5 = this.index;
                bArr5[i5] = (byte) (bArr5[i5] | ((byte) topbits));
                numbits -= this.bitsLeft;
                bitsWritten += this.bitsLeft;
                byte[] bArr6 = this.buffer;
                int i6 = this.index + 1;
                this.index = i6;
                bArr6[i6] = 0;
                this.bitsLeft = 8;
            }
        } while (numbits != 0);
    }
}

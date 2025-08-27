package aj.org.objectweb.asm;

/* loaded from: aspectjweaver-1.8.14.jar:aj/org/objectweb/asm/ByteVector.class */
public class ByteVector {
    byte[] a;
    int b;

    public ByteVector() {
        this.a = new byte[64];
    }

    public ByteVector(int i) {
        this.a = new byte[i];
    }

    public ByteVector putByte(int i) {
        int i2 = this.b;
        if (i2 + 1 > this.a.length) {
            a(1);
        }
        this.a[i2] = (byte) i;
        this.b = i2 + 1;
        return this;
    }

    ByteVector a(int i, int i2) {
        int i3 = this.b;
        if (i3 + 2 > this.a.length) {
            a(2);
        }
        byte[] bArr = this.a;
        int i4 = i3 + 1;
        bArr[i3] = (byte) i;
        bArr[i4] = (byte) i2;
        this.b = i4 + 1;
        return this;
    }

    public ByteVector putShort(int i) {
        int i2 = this.b;
        if (i2 + 2 > this.a.length) {
            a(2);
        }
        byte[] bArr = this.a;
        int i3 = i2 + 1;
        bArr[i2] = (byte) (i >>> 8);
        bArr[i3] = (byte) i;
        this.b = i3 + 1;
        return this;
    }

    ByteVector b(int i, int i2) {
        int i3 = this.b;
        if (i3 + 3 > this.a.length) {
            a(3);
        }
        byte[] bArr = this.a;
        int i4 = i3 + 1;
        bArr[i3] = (byte) i;
        int i5 = i4 + 1;
        bArr[i4] = (byte) (i2 >>> 8);
        bArr[i5] = (byte) i2;
        this.b = i5 + 1;
        return this;
    }

    public ByteVector putInt(int i) {
        int i2 = this.b;
        if (i2 + 4 > this.a.length) {
            a(4);
        }
        byte[] bArr = this.a;
        int i3 = i2 + 1;
        bArr[i2] = (byte) (i >>> 24);
        int i4 = i3 + 1;
        bArr[i3] = (byte) (i >>> 16);
        int i5 = i4 + 1;
        bArr[i4] = (byte) (i >>> 8);
        bArr[i5] = (byte) i;
        this.b = i5 + 1;
        return this;
    }

    public ByteVector putLong(long j) {
        int i = this.b;
        if (i + 8 > this.a.length) {
            a(8);
        }
        byte[] bArr = this.a;
        int i2 = (int) (j >>> 32);
        int i3 = i + 1;
        bArr[i] = (byte) (i2 >>> 24);
        int i4 = i3 + 1;
        bArr[i3] = (byte) (i2 >>> 16);
        int i5 = i4 + 1;
        bArr[i4] = (byte) (i2 >>> 8);
        int i6 = i5 + 1;
        bArr[i5] = (byte) i2;
        int i7 = (int) j;
        int i8 = i6 + 1;
        bArr[i6] = (byte) (i7 >>> 24);
        int i9 = i8 + 1;
        bArr[i8] = (byte) (i7 >>> 16);
        int i10 = i9 + 1;
        bArr[i9] = (byte) (i7 >>> 8);
        bArr[i10] = (byte) i7;
        this.b = i10 + 1;
        return this;
    }

    public ByteVector putUTF8(String str) {
        int length = str.length();
        if (length > 65535) {
            throw new IllegalArgumentException();
        }
        int i = this.b;
        if (i + 2 + length > this.a.length) {
            a(2 + length);
        }
        byte[] bArr = this.a;
        int i2 = i + 1;
        bArr[i] = (byte) (length >>> 8);
        int i3 = i2 + 1;
        bArr[i2] = (byte) length;
        for (int i4 = 0; i4 < length; i4++) {
            char cCharAt = str.charAt(i4);
            if (cCharAt < 1 || cCharAt > 127) {
                this.b = i3;
                return c(str, i4, 65535);
            }
            int i5 = i3;
            i3++;
            bArr[i5] = (byte) cCharAt;
        }
        this.b = i3;
        return this;
    }

    ByteVector c(String str, int i, int i2) {
        int length = str.length();
        int i3 = i;
        for (int i4 = i; i4 < length; i4++) {
            char cCharAt = str.charAt(i4);
            i3 = (cCharAt < 1 || cCharAt > 127) ? cCharAt > 2047 ? i3 + 3 : i3 + 2 : i3 + 1;
        }
        if (i3 > i2) {
            throw new IllegalArgumentException();
        }
        int i5 = (this.b - i) - 2;
        if (i5 >= 0) {
            this.a[i5] = (byte) (i3 >>> 8);
            this.a[i5 + 1] = (byte) i3;
        }
        if ((this.b + i3) - i > this.a.length) {
            a(i3 - i);
        }
        int i6 = this.b;
        for (int i7 = i; i7 < length; i7++) {
            char cCharAt2 = str.charAt(i7);
            if (cCharAt2 >= 1 && cCharAt2 <= 127) {
                int i8 = i6;
                i6++;
                this.a[i8] = (byte) cCharAt2;
            } else if (cCharAt2 > 2047) {
                int i9 = i6;
                int i10 = i6 + 1;
                this.a[i9] = (byte) (224 | ((cCharAt2 >> '\f') & 15));
                int i11 = i10 + 1;
                this.a[i10] = (byte) (128 | ((cCharAt2 >> 6) & 63));
                i6 = i11 + 1;
                this.a[i11] = (byte) (128 | (cCharAt2 & '?'));
            } else {
                int i12 = i6;
                int i13 = i6 + 1;
                this.a[i12] = (byte) (192 | ((cCharAt2 >> 6) & 31));
                i6 = i13 + 1;
                this.a[i13] = (byte) (128 | (cCharAt2 & '?'));
            }
        }
        this.b = i6;
        return this;
    }

    public ByteVector putByteArray(byte[] bArr, int i, int i2) {
        if (this.b + i2 > this.a.length) {
            a(i2);
        }
        if (bArr != null) {
            System.arraycopy(bArr, i, this.a, this.b, i2);
        }
        this.b += i2;
        return this;
    }

    private void a(int i) {
        int length = 2 * this.a.length;
        int i2 = this.b + i;
        byte[] bArr = new byte[length > i2 ? length : i2];
        System.arraycopy(this.a, 0, bArr, 0, this.b);
        this.a = bArr;
    }
}

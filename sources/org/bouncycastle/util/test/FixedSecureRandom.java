package org.bouncycastle.util.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Provider;
import java.security.SecureRandom;
import org.bouncycastle.util.encoders.Hex;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/util/test/FixedSecureRandom.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/util/test/FixedSecureRandom.class */
public class FixedSecureRandom extends SecureRandom {
    private byte[] _data;
    private int _index;
    private int _intPad;

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/util/test/FixedSecureRandom$BigInteger.class */
    public static class BigInteger extends Source {
        public BigInteger(byte[] bArr) {
            super(bArr);
        }

        public BigInteger(int i, byte[] bArr) {
            super(FixedSecureRandom.access$000(i, bArr));
        }

        public BigInteger(String str) {
            this(Hex.decode(str));
        }

        public BigInteger(int i, String str) {
            super(FixedSecureRandom.access$000(i, Hex.decode(str)));
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/util/test/FixedSecureRandom$Data.class */
    public static class Data extends Source {
        public Data(byte[] bArr) {
            super(bArr);
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/util/test/FixedSecureRandom$DummyProvider.class */
    private static class DummyProvider extends Provider {
        DummyProvider() {
            super("BCFIPS_FIXED_RNG", 1.0d, "BCFIPS Fixed Secure Random Provider");
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/util/test/FixedSecureRandom$RandomChecker.class */
    private static class RandomChecker extends SecureRandom {
        byte[] data;
        int index;

        RandomChecker() {
            super(null, new DummyProvider());
            this.data = Hex.decode("01020304ffffffff0506070811111111");
            this.index = 0;
        }

        @Override // java.security.SecureRandom, java.util.Random
        public void nextBytes(byte[] bArr) {
            System.arraycopy(this.data, this.index, bArr, 0, bArr.length);
            this.index += bArr.length;
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/util/test/FixedSecureRandom$Source.class */
    public static class Source {
        byte[] data;

        Source(byte[] bArr) {
            this.data = bArr;
        }
    }

    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public FixedSecureRandom(byte[] bArr) {
        this(false, (byte[][]) new byte[]{bArr});
    }

    public FixedSecureRandom(byte[][] bArr) {
        this(false, bArr);
    }

    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public FixedSecureRandom(boolean z, byte[] bArr) {
        this(z, (byte[][]) new byte[]{bArr});
    }

    public FixedSecureRandom(boolean z, byte[][] bArr) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (int i = 0; i != bArr.length; i++) {
            try {
                byteArrayOutputStream.write(bArr[i]);
            } catch (IOException e) {
                throw new IllegalArgumentException("can't save value array.");
            }
        }
        this._data = byteArrayOutputStream.toByteArray();
        if (z) {
            this._intPad = this._data.length % 4;
        }
    }

    @Override // java.security.SecureRandom, java.util.Random
    public void nextBytes(byte[] bArr) {
        System.arraycopy(this._data, this._index, bArr, 0, bArr.length);
        this._index += bArr.length;
    }

    @Override // java.util.Random
    public int nextInt() {
        int iNextValue = 0 | (nextValue() << 24) | (nextValue() << 16);
        if (this._intPad == 2) {
            this._intPad--;
        } else {
            iNextValue |= nextValue() << 8;
        }
        if (this._intPad == 1) {
            this._intPad--;
        } else {
            iNextValue |= nextValue();
        }
        return iNextValue;
    }

    @Override // java.util.Random
    public long nextLong() {
        return 0 | (nextValue() << 56) | (nextValue() << 48) | (nextValue() << 40) | (nextValue() << 32) | (nextValue() << 24) | (nextValue() << 16) | (nextValue() << 8) | nextValue();
    }

    public boolean isExhausted() {
        return this._index == this._data.length;
    }

    private int nextValue() {
        byte[] bArr = this._data;
        int i = this._index;
        this._index = i + 1;
        return bArr[i] & 255;
    }
}

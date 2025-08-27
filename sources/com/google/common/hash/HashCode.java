package com.google.common.hash;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import com.google.common.primitives.UnsignedInts;
import java.io.Serializable;
import java.security.MessageDigest;
import javax.annotation.Nullable;

@Beta
/* loaded from: guava-18.0.jar:com/google/common/hash/HashCode.class */
public abstract class HashCode {
    private static final char[] hexDigits = "0123456789abcdef".toCharArray();

    public abstract int bits();

    public abstract int asInt();

    public abstract long asLong();

    public abstract long padToLong();

    public abstract byte[] asBytes();

    abstract void writeBytesToImpl(byte[] bArr, int i, int i2);

    abstract boolean equalsSameBits(HashCode hashCode);

    HashCode() {
    }

    public int writeBytesTo(byte[] dest, int offset, int maxLength) {
        int maxLength2 = Ints.min(maxLength, bits() / 8);
        Preconditions.checkPositionIndexes(offset, offset + maxLength2, dest.length);
        writeBytesToImpl(dest, offset, maxLength2);
        return maxLength2;
    }

    byte[] getBytesInternal() {
        return asBytes();
    }

    public static HashCode fromInt(int hash) {
        return new IntHashCode(hash);
    }

    /* loaded from: guava-18.0.jar:com/google/common/hash/HashCode$IntHashCode.class */
    private static final class IntHashCode extends HashCode implements Serializable {
        final int hash;
        private static final long serialVersionUID = 0;

        IntHashCode(int hash) {
            this.hash = hash;
        }

        @Override // com.google.common.hash.HashCode
        public int bits() {
            return 32;
        }

        @Override // com.google.common.hash.HashCode
        public byte[] asBytes() {
            return new byte[]{(byte) this.hash, (byte) (this.hash >> 8), (byte) (this.hash >> 16), (byte) (this.hash >> 24)};
        }

        @Override // com.google.common.hash.HashCode
        public int asInt() {
            return this.hash;
        }

        @Override // com.google.common.hash.HashCode
        public long asLong() {
            throw new IllegalStateException("this HashCode only has 32 bits; cannot create a long");
        }

        @Override // com.google.common.hash.HashCode
        public long padToLong() {
            return UnsignedInts.toLong(this.hash);
        }

        @Override // com.google.common.hash.HashCode
        void writeBytesToImpl(byte[] dest, int offset, int maxLength) {
            for (int i = 0; i < maxLength; i++) {
                dest[offset + i] = (byte) (this.hash >> (i * 8));
            }
        }

        @Override // com.google.common.hash.HashCode
        boolean equalsSameBits(HashCode that) {
            return this.hash == that.asInt();
        }
    }

    public static HashCode fromLong(long hash) {
        return new LongHashCode(hash);
    }

    /* loaded from: guava-18.0.jar:com/google/common/hash/HashCode$LongHashCode.class */
    private static final class LongHashCode extends HashCode implements Serializable {
        final long hash;
        private static final long serialVersionUID = 0;

        LongHashCode(long hash) {
            this.hash = hash;
        }

        @Override // com.google.common.hash.HashCode
        public int bits() {
            return 64;
        }

        @Override // com.google.common.hash.HashCode
        public byte[] asBytes() {
            return new byte[]{(byte) this.hash, (byte) (this.hash >> 8), (byte) (this.hash >> 16), (byte) (this.hash >> 24), (byte) (this.hash >> 32), (byte) (this.hash >> 40), (byte) (this.hash >> 48), (byte) (this.hash >> 56)};
        }

        @Override // com.google.common.hash.HashCode
        public int asInt() {
            return (int) this.hash;
        }

        @Override // com.google.common.hash.HashCode
        public long asLong() {
            return this.hash;
        }

        @Override // com.google.common.hash.HashCode
        public long padToLong() {
            return this.hash;
        }

        @Override // com.google.common.hash.HashCode
        void writeBytesToImpl(byte[] dest, int offset, int maxLength) {
            for (int i = 0; i < maxLength; i++) {
                dest[offset + i] = (byte) (this.hash >> (i * 8));
            }
        }

        @Override // com.google.common.hash.HashCode
        boolean equalsSameBits(HashCode that) {
            return this.hash == that.asLong();
        }
    }

    public static HashCode fromBytes(byte[] bytes) {
        Preconditions.checkArgument(bytes.length >= 1, "A HashCode must contain at least 1 byte.");
        return fromBytesNoCopy((byte[]) bytes.clone());
    }

    static HashCode fromBytesNoCopy(byte[] bytes) {
        return new BytesHashCode(bytes);
    }

    /* loaded from: guava-18.0.jar:com/google/common/hash/HashCode$BytesHashCode.class */
    private static final class BytesHashCode extends HashCode implements Serializable {
        final byte[] bytes;
        private static final long serialVersionUID = 0;

        BytesHashCode(byte[] bytes) {
            this.bytes = (byte[]) Preconditions.checkNotNull(bytes);
        }

        @Override // com.google.common.hash.HashCode
        public int bits() {
            return this.bytes.length * 8;
        }

        @Override // com.google.common.hash.HashCode
        public byte[] asBytes() {
            return (byte[]) this.bytes.clone();
        }

        @Override // com.google.common.hash.HashCode
        public int asInt() {
            Preconditions.checkState(this.bytes.length >= 4, "HashCode#asInt() requires >= 4 bytes (it only has %s bytes).", Integer.valueOf(this.bytes.length));
            return (this.bytes[0] & 255) | ((this.bytes[1] & 255) << 8) | ((this.bytes[2] & 255) << 16) | ((this.bytes[3] & 255) << 24);
        }

        @Override // com.google.common.hash.HashCode
        public long asLong() {
            Preconditions.checkState(this.bytes.length >= 8, "HashCode#asLong() requires >= 8 bytes (it only has %s bytes).", Integer.valueOf(this.bytes.length));
            return padToLong();
        }

        @Override // com.google.common.hash.HashCode
        public long padToLong() {
            long retVal = this.bytes[0] & 255;
            for (int i = 1; i < Math.min(this.bytes.length, 8); i++) {
                retVal |= (this.bytes[i] & 255) << (i * 8);
            }
            return retVal;
        }

        @Override // com.google.common.hash.HashCode
        void writeBytesToImpl(byte[] dest, int offset, int maxLength) {
            System.arraycopy(this.bytes, 0, dest, offset, maxLength);
        }

        @Override // com.google.common.hash.HashCode
        byte[] getBytesInternal() {
            return this.bytes;
        }

        @Override // com.google.common.hash.HashCode
        boolean equalsSameBits(HashCode that) {
            return MessageDigest.isEqual(this.bytes, that.getBytesInternal());
        }
    }

    public static HashCode fromString(String string) {
        Preconditions.checkArgument(string.length() >= 2, "input string (%s) must have at least 2 characters", string);
        Preconditions.checkArgument(string.length() % 2 == 0, "input string (%s) must have an even number of characters", string);
        byte[] bytes = new byte[string.length() / 2];
        for (int i = 0; i < string.length(); i += 2) {
            int ch1 = decode(string.charAt(i)) << 4;
            int ch2 = decode(string.charAt(i + 1));
            bytes[i / 2] = (byte) (ch1 + ch2);
        }
        return fromBytesNoCopy(bytes);
    }

    private static int decode(char ch2) {
        if (ch2 >= '0' && ch2 <= '9') {
            return ch2 - '0';
        }
        if (ch2 >= 'a' && ch2 <= 'f') {
            return (ch2 - 'a') + 10;
        }
        throw new IllegalArgumentException(new StringBuilder(32).append("Illegal hexadecimal character: ").append(ch2).toString());
    }

    public final boolean equals(@Nullable Object object) {
        if (object instanceof HashCode) {
            HashCode that = (HashCode) object;
            return bits() == that.bits() && equalsSameBits(that);
        }
        return false;
    }

    public final int hashCode() {
        if (bits() >= 32) {
            return asInt();
        }
        byte[] bytes = asBytes();
        int val = bytes[0] & 255;
        for (int i = 1; i < bytes.length; i++) {
            val |= (bytes[i] & 255) << (i * 8);
        }
        return val;
    }

    public final String toString() {
        byte[] bytes = asBytes();
        StringBuilder sb = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            sb.append(hexDigits[(b >> 4) & 15]).append(hexDigits[b & 15]);
        }
        return sb.toString();
    }
}

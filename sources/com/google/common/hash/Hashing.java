package com.google.common.hash;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.util.Iterator;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.Checksum;
import javax.annotation.Nullable;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

@Beta
/* loaded from: guava-18.0.jar:com/google/common/hash/Hashing.class */
public final class Hashing {
    private static final int GOOD_FAST_HASH_SEED = (int) System.currentTimeMillis();

    public static HashFunction goodFastHash(int minimumBits) {
        int bits = checkPositiveAndMakeMultipleOf32(minimumBits);
        if (bits == 32) {
            return Murmur3_32Holder.GOOD_FAST_HASH_FUNCTION_32;
        }
        if (bits <= 128) {
            return Murmur3_128Holder.GOOD_FAST_HASH_FUNCTION_128;
        }
        int hashFunctionsNeeded = (bits + 127) / 128;
        HashFunction[] hashFunctions = new HashFunction[hashFunctionsNeeded];
        hashFunctions[0] = Murmur3_128Holder.GOOD_FAST_HASH_FUNCTION_128;
        int seed = GOOD_FAST_HASH_SEED;
        for (int i = 1; i < hashFunctionsNeeded; i++) {
            seed += 1500450271;
            hashFunctions[i] = murmur3_128(seed);
        }
        return new ConcatenatedHashFunction(hashFunctions);
    }

    public static HashFunction murmur3_32(int seed) {
        return new Murmur3_32HashFunction(seed);
    }

    public static HashFunction murmur3_32() {
        return Murmur3_32Holder.MURMUR3_32;
    }

    /* loaded from: guava-18.0.jar:com/google/common/hash/Hashing$Murmur3_32Holder.class */
    private static class Murmur3_32Holder {
        static final HashFunction MURMUR3_32 = new Murmur3_32HashFunction(0);
        static final HashFunction GOOD_FAST_HASH_FUNCTION_32 = Hashing.murmur3_32(Hashing.GOOD_FAST_HASH_SEED);

        private Murmur3_32Holder() {
        }
    }

    public static HashFunction murmur3_128(int seed) {
        return new Murmur3_128HashFunction(seed);
    }

    public static HashFunction murmur3_128() {
        return Murmur3_128Holder.MURMUR3_128;
    }

    /* loaded from: guava-18.0.jar:com/google/common/hash/Hashing$Murmur3_128Holder.class */
    private static class Murmur3_128Holder {
        static final HashFunction MURMUR3_128 = new Murmur3_128HashFunction(0);
        static final HashFunction GOOD_FAST_HASH_FUNCTION_128 = Hashing.murmur3_128(Hashing.GOOD_FAST_HASH_SEED);

        private Murmur3_128Holder() {
        }
    }

    public static HashFunction sipHash24() {
        return SipHash24Holder.SIP_HASH_24;
    }

    /* loaded from: guava-18.0.jar:com/google/common/hash/Hashing$SipHash24Holder.class */
    private static class SipHash24Holder {
        static final HashFunction SIP_HASH_24 = new SipHashFunction(2, 4, 506097522914230528L, 1084818905618843912L);

        private SipHash24Holder() {
        }
    }

    public static HashFunction sipHash24(long k0, long k1) {
        return new SipHashFunction(2, 4, k0, k1);
    }

    public static HashFunction md5() {
        return Md5Holder.MD5;
    }

    /* loaded from: guava-18.0.jar:com/google/common/hash/Hashing$Md5Holder.class */
    private static class Md5Holder {
        static final HashFunction MD5 = new MessageDigestHashFunction(MessageDigestAlgorithms.MD5, "Hashing.md5()");

        private Md5Holder() {
        }
    }

    public static HashFunction sha1() {
        return Sha1Holder.SHA_1;
    }

    /* loaded from: guava-18.0.jar:com/google/common/hash/Hashing$Sha1Holder.class */
    private static class Sha1Holder {
        static final HashFunction SHA_1 = new MessageDigestHashFunction("SHA-1", "Hashing.sha1()");

        private Sha1Holder() {
        }
    }

    public static HashFunction sha256() {
        return Sha256Holder.SHA_256;
    }

    /* loaded from: guava-18.0.jar:com/google/common/hash/Hashing$Sha256Holder.class */
    private static class Sha256Holder {
        static final HashFunction SHA_256 = new MessageDigestHashFunction("SHA-256", "Hashing.sha256()");

        private Sha256Holder() {
        }
    }

    public static HashFunction sha512() {
        return Sha512Holder.SHA_512;
    }

    /* loaded from: guava-18.0.jar:com/google/common/hash/Hashing$Sha512Holder.class */
    private static class Sha512Holder {
        static final HashFunction SHA_512 = new MessageDigestHashFunction("SHA-512", "Hashing.sha512()");

        private Sha512Holder() {
        }
    }

    public static HashFunction crc32c() {
        return Crc32cHolder.CRC_32_C;
    }

    /* loaded from: guava-18.0.jar:com/google/common/hash/Hashing$Crc32cHolder.class */
    private static final class Crc32cHolder {
        static final HashFunction CRC_32_C = new Crc32cHashFunction();

        private Crc32cHolder() {
        }
    }

    public static HashFunction crc32() {
        return Crc32Holder.CRC_32;
    }

    /* loaded from: guava-18.0.jar:com/google/common/hash/Hashing$Crc32Holder.class */
    private static class Crc32Holder {
        static final HashFunction CRC_32 = Hashing.checksumHashFunction(ChecksumType.CRC_32, "Hashing.crc32()");

        private Crc32Holder() {
        }
    }

    public static HashFunction adler32() {
        return Adler32Holder.ADLER_32;
    }

    /* loaded from: guava-18.0.jar:com/google/common/hash/Hashing$Adler32Holder.class */
    private static class Adler32Holder {
        static final HashFunction ADLER_32 = Hashing.checksumHashFunction(ChecksumType.ADLER_32, "Hashing.adler32()");

        private Adler32Holder() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static HashFunction checksumHashFunction(ChecksumType type, String toString) {
        return new ChecksumHashFunction(type, type.bits, toString);
    }

    /* loaded from: guava-18.0.jar:com/google/common/hash/Hashing$ChecksumType.class */
    enum ChecksumType implements Supplier<Checksum> {
        CRC_32(32) { // from class: com.google.common.hash.Hashing.ChecksumType.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.google.common.hash.Hashing.ChecksumType, com.google.common.base.Supplier
            public Checksum get() {
                return new CRC32();
            }
        },
        ADLER_32(32) { // from class: com.google.common.hash.Hashing.ChecksumType.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.google.common.hash.Hashing.ChecksumType, com.google.common.base.Supplier
            public Checksum get() {
                return new Adler32();
            }
        };

        private final int bits;

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.common.base.Supplier
        public abstract Checksum get();

        ChecksumType(int bits) {
            this.bits = bits;
        }
    }

    public static int consistentHash(HashCode hashCode, int buckets) {
        return consistentHash(hashCode.padToLong(), buckets);
    }

    public static int consistentHash(long input, int buckets) {
        int candidate;
        Preconditions.checkArgument(buckets > 0, "buckets must be positive: %s", Integer.valueOf(buckets));
        LinearCongruentialGenerator generator = new LinearCongruentialGenerator(input);
        int i = 0;
        while (true) {
            candidate = i;
            int next = (int) ((candidate + 1) / generator.nextDouble());
            if (next < 0 || next >= buckets) {
                break;
            }
            i = next;
        }
        return candidate;
    }

    public static HashCode combineOrdered(Iterable<HashCode> hashCodes) {
        Iterator<HashCode> iterator = hashCodes.iterator();
        Preconditions.checkArgument(iterator.hasNext(), "Must be at least 1 hash code to combine.");
        int bits = iterator.next().bits();
        byte[] resultBytes = new byte[bits / 8];
        for (HashCode hashCode : hashCodes) {
            byte[] nextBytes = hashCode.asBytes();
            Preconditions.checkArgument(nextBytes.length == resultBytes.length, "All hashcodes must have the same bit length.");
            for (int i = 0; i < nextBytes.length; i++) {
                resultBytes[i] = (byte) ((resultBytes[i] * 37) ^ nextBytes[i]);
            }
        }
        return HashCode.fromBytesNoCopy(resultBytes);
    }

    public static HashCode combineUnordered(Iterable<HashCode> hashCodes) {
        Iterator<HashCode> iterator = hashCodes.iterator();
        Preconditions.checkArgument(iterator.hasNext(), "Must be at least 1 hash code to combine.");
        byte[] resultBytes = new byte[iterator.next().bits() / 8];
        for (HashCode hashCode : hashCodes) {
            byte[] nextBytes = hashCode.asBytes();
            Preconditions.checkArgument(nextBytes.length == resultBytes.length, "All hashcodes must have the same bit length.");
            for (int i = 0; i < nextBytes.length; i++) {
                int i2 = i;
                resultBytes[i2] = (byte) (resultBytes[i2] + nextBytes[i]);
            }
        }
        return HashCode.fromBytesNoCopy(resultBytes);
    }

    static int checkPositiveAndMakeMultipleOf32(int bits) {
        Preconditions.checkArgument(bits > 0, "Number of bits must be positive");
        return (bits + 31) & (-32);
    }

    @VisibleForTesting
    /* loaded from: guava-18.0.jar:com/google/common/hash/Hashing$ConcatenatedHashFunction.class */
    static final class ConcatenatedHashFunction extends AbstractCompositeHashFunction {
        private final int bits;

        ConcatenatedHashFunction(HashFunction... functions) {
            super(functions);
            int bitSum = 0;
            for (HashFunction function : functions) {
                bitSum += function.bits();
            }
            this.bits = bitSum;
        }

        @Override // com.google.common.hash.AbstractCompositeHashFunction
        HashCode makeHash(Hasher[] hashers) {
            byte[] bytes = new byte[this.bits / 8];
            int i = 0;
            for (Hasher hasher : hashers) {
                HashCode newHash = hasher.hash();
                i += newHash.writeBytesTo(bytes, i, newHash.bits() / 8);
            }
            return HashCode.fromBytesNoCopy(bytes);
        }

        @Override // com.google.common.hash.HashFunction
        public int bits() {
            return this.bits;
        }

        public boolean equals(@Nullable Object object) {
            if (object instanceof ConcatenatedHashFunction) {
                ConcatenatedHashFunction other = (ConcatenatedHashFunction) object;
                if (this.bits != other.bits || this.functions.length != other.functions.length) {
                    return false;
                }
                for (int i = 0; i < this.functions.length; i++) {
                    if (!this.functions[i].equals(other.functions[i])) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }

        public int hashCode() {
            int hash = this.bits;
            HashFunction[] arr$ = this.functions;
            for (HashFunction function : arr$) {
                hash ^= function.hashCode();
            }
            return hash;
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/hash/Hashing$LinearCongruentialGenerator.class */
    private static final class LinearCongruentialGenerator {
        private long state;

        public LinearCongruentialGenerator(long seed) {
            this.state = seed;
        }

        public double nextDouble() {
            this.state = (2862933555777941757L * this.state) + 1;
            return (((int) (this.state >>> 33)) + 1) / 2.147483648E9d;
        }
    }

    private Hashing() {
    }
}

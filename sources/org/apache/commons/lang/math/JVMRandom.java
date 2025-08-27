package org.apache.commons.lang.math;

import java.util.Random;

/* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/math/JVMRandom.class */
public final class JVMRandom extends Random {
    private static final long serialVersionUID = 1;
    private static final Random SHARED_RANDOM = new Random();
    private boolean constructed;

    public JVMRandom() {
        this.constructed = false;
        this.constructed = true;
    }

    @Override // java.util.Random
    public synchronized void setSeed(long seed) {
        if (this.constructed) {
            throw new UnsupportedOperationException();
        }
    }

    @Override // java.util.Random
    public synchronized double nextGaussian() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Random
    public void nextBytes(byte[] byteArray) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Random
    public int nextInt() {
        return nextInt(Integer.MAX_VALUE);
    }

    @Override // java.util.Random
    public int nextInt(int n) {
        return SHARED_RANDOM.nextInt(n);
    }

    @Override // java.util.Random
    public long nextLong() {
        return nextLong(Long.MAX_VALUE);
    }

    public static long nextLong(long n) {
        long bits;
        long val;
        if (n <= 0) {
            throw new IllegalArgumentException("Upper bound for nextInt must be positive");
        }
        if ((n & (-n)) == n) {
            return next63bits() >> (63 - bitsRequired(n - serialVersionUID));
        }
        do {
            bits = next63bits();
            val = bits % n;
        } while ((bits - val) + (n - serialVersionUID) < 0);
        return val;
    }

    @Override // java.util.Random
    public boolean nextBoolean() {
        return SHARED_RANDOM.nextBoolean();
    }

    @Override // java.util.Random
    public float nextFloat() {
        return SHARED_RANDOM.nextFloat();
    }

    @Override // java.util.Random
    public double nextDouble() {
        return SHARED_RANDOM.nextDouble();
    }

    private static long next63bits() {
        return SHARED_RANDOM.nextLong() & Long.MAX_VALUE;
    }

    private static int bitsRequired(long num) {
        long y = num;
        int n = 0;
        while (num >= 0) {
            if (y == 0) {
                return n;
            }
            n++;
            num <<= serialVersionUID;
            y >>= serialVersionUID;
        }
        return 64 - n;
    }
}

package org.ehcache.impl.internal.concurrent;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.ehcache.impl.internal.concurrent.JSR166Helper;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ThreadLocalRandom.class */
class ThreadLocalRandom extends Random {
    private static final long GAMMA = -7046029254386353131L;
    private static final int PROBE_INCREMENT = -1640531527;
    private static final long SEEDER_INCREMENT = -4942790177534073029L;
    private static final double DOUBLE_UNIT = 1.1102230246251565E-16d;
    private static final float FLOAT_UNIT = 5.9604645E-8f;
    boolean initialized = true;
    static final String BadBound = "bound must be positive";
    static final String BadRange = "bound must be greater than origin";
    static final String BadSize = "size must be non-negative";
    private static final long serialVersionUID = -5851777807851030925L;
    private static final AtomicInteger probeGenerator = new AtomicInteger();
    private static final AtomicLong seeder = new AtomicLong(initialSeed());
    private static final ThreadLocal<Double> nextLocalGaussian = new ThreadLocal<>();
    static final ThreadLocalRandom instance = new ThreadLocalRandom();
    private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("rnd", Long.TYPE), new ObjectStreamField("initialized", Boolean.TYPE)};
    private static final ThreadLocal<Long> SEED = new ThreadLocal<Long>() { // from class: org.ehcache.impl.internal.concurrent.ThreadLocalRandom.2
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.ThreadLocal
        public Long initialValue() {
            return 0L;
        }
    };
    private static final ThreadLocal<Integer> PROBE = new ThreadLocal<Integer>() { // from class: org.ehcache.impl.internal.concurrent.ThreadLocalRandom.3
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.ThreadLocal
        public Integer initialValue() {
            return 0;
        }
    };
    private static final ThreadLocal<Integer> SECONDARY = new ThreadLocal<Integer>() { // from class: org.ehcache.impl.internal.concurrent.ThreadLocalRandom.4
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.ThreadLocal
        public Integer initialValue() {
            return 0;
        }
    };

    /* JADX WARN: Code restructure failed: missing block: B:20:0x007c, code lost:
    
        r0 = r0.length;
        r0 = java.lang.Math.min(r0 >>> 1, 4);
        r16 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0092, code lost:
    
        if (r16 >= r0) goto L44;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0095, code lost:
    
        r8 = ((r8 << 16) ^ (r0[r16] << 8)) ^ r0[(r0 - 1) - r16];
        r16 = r16 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x00b9, code lost:
    
        if (r0 >= 4) goto L27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x00bc, code lost:
    
        r8 = (r8 << 8) ^ r0[(r0 - 1) - r0];
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x00cd, code lost:
    
        r8 = mix64(r8);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static long initialSeed() throws java.net.SocketException {
        /*
            Method dump skipped, instructions count: 244
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.ehcache.impl.internal.concurrent.ThreadLocalRandom.initialSeed():long");
    }

    private static long mix64(long z) {
        long z2 = (z ^ (z >>> 33)) * (-49064778989728563L);
        long z3 = (z2 ^ (z2 >>> 33)) * (-4265267296055464877L);
        return z3 ^ (z3 >>> 33);
    }

    private static int mix32(long z) {
        long z2 = (z ^ (z >>> 33)) * (-49064778989728563L);
        return (int) (((z2 ^ (z2 >>> 33)) * (-4265267296055464877L)) >>> 32);
    }

    private ThreadLocalRandom() {
    }

    static final void localInit() {
        int p = probeGenerator.addAndGet(PROBE_INCREMENT);
        int probe = p == 0 ? 1 : p;
        long seed = mix64(seeder.getAndAdd(SEEDER_INCREMENT));
        SEED.set(Long.valueOf(seed));
        PROBE.set(Integer.valueOf(probe));
    }

    public static ThreadLocalRandom current() {
        if (PROBE.get().intValue() == 0) {
            localInit();
        }
        return instance;
    }

    @Override // java.util.Random
    public void setSeed(long seed) {
        if (this.initialized) {
            throw new UnsupportedOperationException();
        }
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [java.lang.ThreadLocal, java.lang.ThreadLocal<java.lang.Long>, long] */
    final long nextSeed() {
        ?? r0 = SEED;
        r0.set(Long.valueOf(SEED.get().longValue() + GAMMA));
        return r0;
    }

    @Override // java.util.Random
    protected int next(int bits) {
        return (int) (mix64(nextSeed()) >>> (64 - bits));
    }

    final long internalNextLong(long origin, long bound) {
        long r;
        long r2 = mix64(nextSeed());
        if (origin < bound) {
            long n = bound - origin;
            long m = n - 1;
            if ((n & m) != 0) {
                if (n <= 0) {
                    while (true) {
                        if (r2 >= origin && r2 < bound) {
                            break;
                        }
                        r2 = mix64(nextSeed());
                    }
                } else {
                    long jMix64 = r2;
                    while (true) {
                        long u = jMix64 >>> 1;
                        r = u + m;
                        if (r - (u % n) >= 0) {
                            break;
                        }
                        jMix64 = mix64(nextSeed());
                    }
                    r2 = r + origin;
                }
            } else {
                r2 = (r2 & m) + origin;
            }
        }
        return r2;
    }

    final int internalNextInt(int origin, int bound) {
        int r;
        int r2 = mix32(nextSeed());
        if (origin < bound) {
            int n = bound - origin;
            int m = n - 1;
            if ((n & m) == 0) {
                r2 = (r2 & m) + origin;
            } else if (n <= 0) {
                while (true) {
                    if (r2 >= origin && r2 < bound) {
                        break;
                    }
                    r2 = mix32(nextSeed());
                }
            } else {
                int iMix32 = r2;
                while (true) {
                    int u = iMix32 >>> 1;
                    r = u % n;
                    if ((u + m) - r >= 0) {
                        break;
                    }
                    iMix32 = mix32(nextSeed());
                }
                r2 = r + origin;
            }
        }
        return r2;
    }

    final double internalNextDouble(double origin, double bound) {
        double r = (nextLong() >>> 11) * DOUBLE_UNIT;
        if (origin < bound) {
            r = (r * (bound - origin)) + origin;
            if (r >= bound) {
                r = Double.longBitsToDouble(Double.doubleToLongBits(bound) - 1);
            }
        }
        return r;
    }

    @Override // java.util.Random
    public int nextInt() {
        return mix32(nextSeed());
    }

    @Override // java.util.Random
    public int nextInt(int bound) {
        int r;
        if (bound <= 0) {
            throw new IllegalArgumentException(BadBound);
        }
        int r2 = mix32(nextSeed());
        int m = bound - 1;
        if ((bound & m) == 0) {
            r = r2 & m;
        } else {
            int iMix32 = r2;
            while (true) {
                int u = iMix32 >>> 1;
                int i = u % bound;
                r = i;
                if ((u + m) - i >= 0) {
                    break;
                }
                iMix32 = mix32(nextSeed());
            }
        }
        return r;
    }

    public int nextInt(int origin, int bound) {
        if (origin >= bound) {
            throw new IllegalArgumentException(BadRange);
        }
        return internalNextInt(origin, bound);
    }

    @Override // java.util.Random
    public long nextLong() {
        return mix64(nextSeed());
    }

    public long nextLong(long bound) {
        long r;
        if (bound <= 0) {
            throw new IllegalArgumentException(BadBound);
        }
        long r2 = mix64(nextSeed());
        long m = bound - 1;
        if ((bound & m) == 0) {
            r = r2 & m;
        } else {
            long jMix64 = r2;
            while (true) {
                long u = jMix64 >>> 1;
                long j = u + m;
                r = j;
                if (j - (u % bound) >= 0) {
                    break;
                }
                jMix64 = mix64(nextSeed());
            }
        }
        return r;
    }

    public long nextLong(long origin, long bound) {
        if (origin >= bound) {
            throw new IllegalArgumentException(BadRange);
        }
        return internalNextLong(origin, bound);
    }

    @Override // java.util.Random
    public double nextDouble() {
        return (mix64(nextSeed()) >>> 11) * DOUBLE_UNIT;
    }

    public double nextDouble(double bound) {
        if (bound <= 0.0d) {
            throw new IllegalArgumentException(BadBound);
        }
        double result = (mix64(nextSeed()) >>> 11) * DOUBLE_UNIT * bound;
        return result < bound ? result : Double.longBitsToDouble(Double.doubleToLongBits(bound) - 1);
    }

    public double nextDouble(double origin, double bound) {
        if (origin >= bound) {
            throw new IllegalArgumentException(BadRange);
        }
        return internalNextDouble(origin, bound);
    }

    @Override // java.util.Random
    public boolean nextBoolean() {
        return mix32(nextSeed()) < 0;
    }

    @Override // java.util.Random
    public float nextFloat() {
        return (mix32(nextSeed()) >>> 8) * FLOAT_UNIT;
    }

    @Override // java.util.Random
    public double nextGaussian() {
        Double d = nextLocalGaussian.get();
        if (d != null) {
            nextLocalGaussian.set(null);
            return d.doubleValue();
        }
        while (true) {
            double v1 = (2.0d * nextDouble()) - 1.0d;
            double v2 = (2.0d * nextDouble()) - 1.0d;
            double s = (v1 * v1) + (v2 * v2);
            if (s < 1.0d && s != 0.0d) {
                double multiplier = StrictMath.sqrt(((-2.0d) * StrictMath.log(s)) / s);
                nextLocalGaussian.set(new Double(v2 * multiplier));
                return v1 * multiplier;
            }
        }
    }

    public JSR166Helper.IntStream _ints(long streamSize) {
        if (streamSize < 0) {
            throw new IllegalArgumentException(BadSize);
        }
        return JSR166Helper.StreamSupport.intStream(new RandomIntsSpliterator(0L, streamSize, Integer.MAX_VALUE, 0), false);
    }

    public JSR166Helper.IntStream _ints() {
        return JSR166Helper.StreamSupport.intStream(new RandomIntsSpliterator(0L, Long.MAX_VALUE, Integer.MAX_VALUE, 0), false);
    }

    public JSR166Helper.IntStream _ints(long streamSize, int randomNumberOrigin, int randomNumberBound) {
        if (streamSize < 0) {
            throw new IllegalArgumentException(BadSize);
        }
        if (randomNumberOrigin >= randomNumberBound) {
            throw new IllegalArgumentException(BadRange);
        }
        return JSR166Helper.StreamSupport.intStream(new RandomIntsSpliterator(0L, streamSize, randomNumberOrigin, randomNumberBound), false);
    }

    public JSR166Helper.IntStream _ints(int randomNumberOrigin, int randomNumberBound) {
        if (randomNumberOrigin >= randomNumberBound) {
            throw new IllegalArgumentException(BadRange);
        }
        return JSR166Helper.StreamSupport.intStream(new RandomIntsSpliterator(0L, Long.MAX_VALUE, randomNumberOrigin, randomNumberBound), false);
    }

    public JSR166Helper.LongStream _longs(long streamSize) {
        if (streamSize < 0) {
            throw new IllegalArgumentException(BadSize);
        }
        return JSR166Helper.StreamSupport.longStream(new RandomLongsSpliterator(0L, streamSize, Long.MAX_VALUE, 0L), false);
    }

    public JSR166Helper.LongStream _longs() {
        return JSR166Helper.StreamSupport.longStream(new RandomLongsSpliterator(0L, Long.MAX_VALUE, Long.MAX_VALUE, 0L), false);
    }

    public JSR166Helper.LongStream _longs(long streamSize, long randomNumberOrigin, long randomNumberBound) {
        if (streamSize < 0) {
            throw new IllegalArgumentException(BadSize);
        }
        if (randomNumberOrigin >= randomNumberBound) {
            throw new IllegalArgumentException(BadRange);
        }
        return JSR166Helper.StreamSupport.longStream(new RandomLongsSpliterator(0L, streamSize, randomNumberOrigin, randomNumberBound), false);
    }

    public JSR166Helper.LongStream _longs(long randomNumberOrigin, long randomNumberBound) {
        if (randomNumberOrigin >= randomNumberBound) {
            throw new IllegalArgumentException(BadRange);
        }
        return JSR166Helper.StreamSupport.longStream(new RandomLongsSpliterator(0L, Long.MAX_VALUE, randomNumberOrigin, randomNumberBound), false);
    }

    public JSR166Helper.DoubleStream _doubles(long streamSize) {
        if (streamSize < 0) {
            throw new IllegalArgumentException(BadSize);
        }
        return JSR166Helper.StreamSupport.doubleStream(new RandomDoublesSpliterator(0L, streamSize, Double.MAX_VALUE, 0.0d), false);
    }

    public JSR166Helper.DoubleStream _doubles() {
        return JSR166Helper.StreamSupport.doubleStream(new RandomDoublesSpliterator(0L, Long.MAX_VALUE, Double.MAX_VALUE, 0.0d), false);
    }

    public JSR166Helper.DoubleStream _doubles(long streamSize, double randomNumberOrigin, double randomNumberBound) {
        if (streamSize < 0) {
            throw new IllegalArgumentException(BadSize);
        }
        if (randomNumberOrigin >= randomNumberBound) {
            throw new IllegalArgumentException(BadRange);
        }
        return JSR166Helper.StreamSupport.doubleStream(new RandomDoublesSpliterator(0L, streamSize, randomNumberOrigin, randomNumberBound), false);
    }

    public JSR166Helper.DoubleStream _doubles(double randomNumberOrigin, double randomNumberBound) {
        if (randomNumberOrigin >= randomNumberBound) {
            throw new IllegalArgumentException(BadRange);
        }
        return JSR166Helper.StreamSupport.doubleStream(new RandomDoublesSpliterator(0L, Long.MAX_VALUE, randomNumberOrigin, randomNumberBound), false);
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ThreadLocalRandom$RandomIntsSpliterator.class */
    static final class RandomIntsSpliterator implements JSR166Helper.Spliterator.OfInt {
        long index;
        final long fence;
        final int origin;
        final int bound;

        RandomIntsSpliterator(long index, long fence, int origin, int bound) {
            this.index = index;
            this.fence = fence;
            this.origin = origin;
            this.bound = bound;
        }

        public RandomIntsSpliterator trySplit() {
            long i = this.index;
            long m = (i + this.fence) >>> 1;
            if (m <= i) {
                return null;
            }
            this.index = m;
            return new RandomIntsSpliterator(i, m, this.origin, this.bound);
        }

        public long estimateSize() {
            return this.fence - this.index;
        }

        public int characteristics() {
            return 17728;
        }

        public boolean tryAdvance(JSR166Helper.IntConsumer consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            long i = this.index;
            long f = this.fence;
            if (i < f) {
                consumer.accept(ThreadLocalRandom.current().internalNextInt(this.origin, this.bound));
                this.index = i + 1;
                return true;
            }
            return false;
        }

        public void forEachRemaining(JSR166Helper.IntConsumer consumer) {
            long j;
            if (consumer == null) {
                throw new NullPointerException();
            }
            long i = this.index;
            long f = this.fence;
            if (i < f) {
                this.index = f;
                int o = this.origin;
                int b = this.bound;
                ThreadLocalRandom rng = ThreadLocalRandom.current();
                do {
                    consumer.accept(rng.internalNextInt(o, b));
                    j = i + 1;
                    i = j;
                } while (j < f);
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ThreadLocalRandom$RandomLongsSpliterator.class */
    static final class RandomLongsSpliterator implements JSR166Helper.Spliterator.OfLong {
        long index;
        final long fence;
        final long origin;
        final long bound;

        RandomLongsSpliterator(long index, long fence, long origin, long bound) {
            this.index = index;
            this.fence = fence;
            this.origin = origin;
            this.bound = bound;
        }

        public RandomLongsSpliterator trySplit() {
            long i = this.index;
            long m = (i + this.fence) >>> 1;
            if (m <= i) {
                return null;
            }
            this.index = m;
            return new RandomLongsSpliterator(i, m, this.origin, this.bound);
        }

        public long estimateSize() {
            return this.fence - this.index;
        }

        public int characteristics() {
            return 17728;
        }

        public boolean tryAdvance(JSR166Helper.LongConsumer consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            long i = this.index;
            long f = this.fence;
            if (i < f) {
                consumer.accept(ThreadLocalRandom.current().internalNextLong(this.origin, this.bound));
                this.index = i + 1;
                return true;
            }
            return false;
        }

        public void forEachRemaining(JSR166Helper.LongConsumer consumer) {
            long j;
            if (consumer == null) {
                throw new NullPointerException();
            }
            long i = this.index;
            long f = this.fence;
            if (i < f) {
                this.index = f;
                long o = this.origin;
                long b = this.bound;
                ThreadLocalRandom rng = ThreadLocalRandom.current();
                do {
                    consumer.accept(rng.internalNextLong(o, b));
                    j = i + 1;
                    i = b;
                } while (j < f);
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ThreadLocalRandom$RandomDoublesSpliterator.class */
    static final class RandomDoublesSpliterator implements JSR166Helper.Spliterator.OfDouble {
        long index;
        final long fence;
        final double origin;
        final double bound;

        RandomDoublesSpliterator(long index, long fence, double origin, double bound) {
            this.index = index;
            this.fence = fence;
            this.origin = origin;
            this.bound = bound;
        }

        public RandomDoublesSpliterator trySplit() {
            long i = this.index;
            long m = (i + this.fence) >>> 1;
            if (m <= i) {
                return null;
            }
            this.index = m;
            return new RandomDoublesSpliterator(i, m, this.origin, this.bound);
        }

        public long estimateSize() {
            return this.fence - this.index;
        }

        public int characteristics() {
            return 17728;
        }

        public boolean tryAdvance(JSR166Helper.DoubleConsumer consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            long i = this.index;
            long f = this.fence;
            if (i < f) {
                consumer.accept(ThreadLocalRandom.current().internalNextDouble(this.origin, this.bound));
                this.index = i + 1;
                return true;
            }
            return false;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void forEachRemaining(JSR166Helper.DoubleConsumer consumer) {
            long j;
            if (consumer == null) {
                throw new NullPointerException();
            }
            long i = this.index;
            long f = this.fence;
            if (i < f) {
                this.index = f;
                double o = this.origin;
                double d = this.bound;
                ThreadLocalRandom threadLocalRandomCurrent = ThreadLocalRandom.current();
                do {
                    consumer.accept(threadLocalRandomCurrent.internalNextDouble(o, d));
                    j = i + 1;
                    i = d;
                } while (j < f);
            }
        }
    }

    static final int getProbe() {
        return PROBE.get().intValue();
    }

    static final int advanceProbe(int probe) {
        int probe2 = probe ^ (probe << 13);
        int probe3 = probe2 ^ (probe2 >>> 17);
        int probe4 = probe3 ^ (probe3 << 5);
        PROBE.set(Integer.valueOf(probe4));
        return probe4;
    }

    static final int nextSecondarySeed() {
        int r;
        int r2 = SECONDARY.get().intValue();
        if (r2 != 0) {
            int r3 = r2 ^ (r2 << 13);
            int r4 = r3 ^ (r3 >>> 17);
            r = r4 ^ (r4 << 5);
        } else {
            int iMix32 = mix32(seeder.getAndAdd(SEEDER_INCREMENT));
            r = iMix32;
            if (iMix32 == 0) {
                r = 1;
            }
        }
        SECONDARY.set(Integer.valueOf(r));
        return r;
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        ObjectOutputStream.PutField fields = s.putFields();
        fields.put("rnd", SEED.get());
        fields.put("initialized", true);
        s.writeFields();
    }

    private Object readResolve() {
        return current();
    }
}

package io.netty.util.internal;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.lang.Thread;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/internal/ThreadLocalRandom.class */
public final class ThreadLocalRandom extends Random {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) ThreadLocalRandom.class);
    private static final AtomicLong seedUniquifier = new AtomicLong();
    private static volatile long initialSeedUniquifier;
    private static final Thread seedGeneratorThread;
    private static final BlockingQueue<Long> seedQueue;
    private static final long seedGeneratorStartTime;
    private static volatile long seedGeneratorEndTime;
    private static final long multiplier = 25214903917L;
    private static final long addend = 11;
    private static final long mask = 281474976710655L;
    private long rnd;
    boolean initialized;
    private long pad0;
    private long pad1;
    private long pad2;
    private long pad3;
    private long pad4;
    private long pad5;
    private long pad6;
    private long pad7;
    private static final long serialVersionUID = -5851777807851030925L;

    static {
        initialSeedUniquifier = SystemPropertyUtil.getLong("io.netty.initialSeedUniquifier", 0L);
        if (initialSeedUniquifier == 0) {
            boolean secureRandom = SystemPropertyUtil.getBoolean("java.util.secureRandomSeed", false);
            if (secureRandom) {
                seedQueue = new LinkedBlockingQueue();
                seedGeneratorStartTime = System.nanoTime();
                seedGeneratorThread = new Thread("initialSeedUniquifierGenerator") { // from class: io.netty.util.internal.ThreadLocalRandom.1
                    @Override // java.lang.Thread, java.lang.Runnable
                    public void run() {
                        SecureRandom random = new SecureRandom();
                        byte[] seed = random.generateSeed(8);
                        long unused = ThreadLocalRandom.seedGeneratorEndTime = System.nanoTime();
                        long s = ((seed[0] & 255) << 56) | ((seed[1] & 255) << 48) | ((seed[2] & 255) << 40) | ((seed[3] & 255) << 32) | ((seed[4] & 255) << 24) | ((seed[5] & 255) << 16) | ((seed[6] & 255) << 8) | (seed[7] & 255);
                        ThreadLocalRandom.seedQueue.add(Long.valueOf(s));
                    }
                };
                seedGeneratorThread.setDaemon(true);
                seedGeneratorThread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() { // from class: io.netty.util.internal.ThreadLocalRandom.2
                    @Override // java.lang.Thread.UncaughtExceptionHandler
                    public void uncaughtException(Thread t, Throwable e) {
                        ThreadLocalRandom.logger.debug("An exception has been raised by {}", t.getName(), e);
                    }
                });
                seedGeneratorThread.start();
                return;
            }
            initialSeedUniquifier = mix64(System.currentTimeMillis()) ^ mix64(System.nanoTime());
            seedGeneratorThread = null;
            seedQueue = null;
            seedGeneratorStartTime = 0L;
            return;
        }
        seedGeneratorThread = null;
        seedQueue = null;
        seedGeneratorStartTime = 0L;
    }

    public static void setInitialSeedUniquifier(long initialSeedUniquifier2) {
        initialSeedUniquifier = initialSeedUniquifier2;
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x006b, code lost:
    
        r6 = r16.longValue();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static long getInitialSeedUniquifier() {
        /*
            Method dump skipped, instructions count: 233
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.util.internal.ThreadLocalRandom.getInitialSeedUniquifier():long");
    }

    private static long newSeed() {
        long current;
        long actualCurrent;
        long next;
        do {
            current = seedUniquifier.get();
            actualCurrent = current != 0 ? current : getInitialSeedUniquifier();
            next = actualCurrent * 181783497276652981L;
        } while (!seedUniquifier.compareAndSet(current, next));
        if (current == 0 && logger.isDebugEnabled()) {
            if (seedGeneratorEndTime != 0) {
                logger.debug(String.format("-Dio.netty.initialSeedUniquifier: 0x%016x (took %d ms)", Long.valueOf(actualCurrent), Long.valueOf(TimeUnit.NANOSECONDS.toMillis(seedGeneratorEndTime - seedGeneratorStartTime))));
            } else {
                logger.debug(String.format("-Dio.netty.initialSeedUniquifier: 0x%016x", Long.valueOf(actualCurrent)));
            }
        }
        return next ^ System.nanoTime();
    }

    private static long mix64(long z) {
        long z2 = (z ^ (z >>> 33)) * (-49064778989728563L);
        long z3 = (z2 ^ (z2 >>> 33)) * (-4265267296055464877L);
        return z3 ^ (z3 >>> 33);
    }

    ThreadLocalRandom() {
        super(newSeed());
        this.initialized = true;
    }

    public static ThreadLocalRandom current() {
        return InternalThreadLocalMap.get().random();
    }

    @Override // java.util.Random
    public void setSeed(long seed) {
        if (this.initialized) {
            throw new UnsupportedOperationException();
        }
        this.rnd = (seed ^ multiplier) & mask;
    }

    @Override // java.util.Random
    protected int next(int bits) {
        this.rnd = ((this.rnd * multiplier) + addend) & mask;
        return (int) (this.rnd >>> (48 - bits));
    }

    public int nextInt(int least, int bound) {
        if (least >= bound) {
            throw new IllegalArgumentException();
        }
        return nextInt(bound - least) + least;
    }

    public long nextLong(long n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be positive");
        }
        long offset = 0;
        while (n >= 2147483647L) {
            int bits = next(2);
            long half = n >>> 1;
            long nextn = (bits & 2) == 0 ? half : n - half;
            if ((bits & 1) == 0) {
                offset += n - nextn;
            }
            n = nextn;
        }
        return offset + nextInt((int) n);
    }

    public long nextLong(long least, long bound) {
        if (least >= bound) {
            throw new IllegalArgumentException();
        }
        return nextLong(bound - least) + least;
    }

    public double nextDouble(double n) {
        if (n <= 0.0d) {
            throw new IllegalArgumentException("n must be positive");
        }
        return nextDouble() * n;
    }

    public double nextDouble(double least, double bound) {
        if (least >= bound) {
            throw new IllegalArgumentException();
        }
        return (nextDouble() * (bound - least)) + least;
    }
}

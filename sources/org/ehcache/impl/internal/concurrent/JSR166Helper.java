package org.ehcache.impl.internal.concurrent;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/JSR166Helper.class */
public final class JSR166Helper {

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/JSR166Helper$BiConsumer.class */
    public interface BiConsumer<A, B> {
        void accept(A a, B b);
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/JSR166Helper$Consumer.class */
    public interface Consumer<A> {
        void accept(A a);
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/JSR166Helper$DoubleBinaryOperator.class */
    public interface DoubleBinaryOperator {
        long applyAsDouble(double d, double d2);
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/JSR166Helper$DoubleConsumer.class */
    interface DoubleConsumer {
        void accept(double d);
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/JSR166Helper$DoubleStream.class */
    interface DoubleStream {
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/JSR166Helper$IntBinaryOperator.class */
    public interface IntBinaryOperator {
        int applyAsInt(int i, int i2);
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/JSR166Helper$IntConsumer.class */
    interface IntConsumer {
        void accept(int i);
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/JSR166Helper$IntStream.class */
    interface IntStream {
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/JSR166Helper$LongBinaryOperator.class */
    public interface LongBinaryOperator {
        long applyAsLong(long j, long j2);
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/JSR166Helper$LongConsumer.class */
    interface LongConsumer {
        void accept(long j);
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/JSR166Helper$LongStream.class */
    interface LongStream {
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/JSR166Helper$Spliterator.class */
    public interface Spliterator<T> {
        public static final int DISTINCT = 1;
        public static final int NONNULL = 256;
        public static final int CONCURRENT = 4096;
        public static final int SIZED = 64;
        public static final int IMMUTABLE = 1024;
        public static final int SUBSIZED = 16384;

        /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/JSR166Helper$Spliterator$OfDouble.class */
        public interface OfDouble {
        }

        /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/JSR166Helper$Spliterator$OfInt.class */
        public interface OfInt {
        }

        /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/JSR166Helper$Spliterator$OfLong.class */
        public interface OfLong {
        }

        Spliterator<T> trySplit();

        long estimateSize();

        void forEachRemaining(Consumer<? super T> consumer);

        boolean tryAdvance(Consumer<? super T> consumer);

        int characteristics();
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/JSR166Helper$ToDoubleBiFunction.class */
    public interface ToDoubleBiFunction<A, B> {
        double applyAsDouble(A a, B b);
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/JSR166Helper$ToDoubleFunction.class */
    public interface ToDoubleFunction<A> {
        double applyAsDouble(A a);
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/JSR166Helper$ToIntBiFunction.class */
    public interface ToIntBiFunction<A, B> {
        int applyAsInt(A a, B b);
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/JSR166Helper$ToIntFunction.class */
    public interface ToIntFunction<A> {
        int applyAsInt(A a);
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/JSR166Helper$ToLongBiFunction.class */
    public interface ToLongBiFunction<A, B> {
        long applyAsLong(A a, B b);
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/JSR166Helper$ToLongFunction.class */
    public interface ToLongFunction<A> {
        long applyAsLong(A a);
    }

    private JSR166Helper() {
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/JSR166Helper$StreamSupport.class */
    static class StreamSupport {
        StreamSupport() {
        }

        static IntStream intStream(Spliterator.OfInt soi, boolean b) {
            throw new UnsupportedOperationException();
        }

        static LongStream longStream(Spliterator.OfLong sol, boolean b) {
            throw new UnsupportedOperationException();
        }

        static DoubleStream doubleStream(Spliterator.OfDouble sod, boolean b) {
            throw new UnsupportedOperationException();
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/JSR166Helper$Unsafe.class */
    static final class Unsafe {
        private static final sun.misc.Unsafe SMU;
        private static final Unsafe U;

        private static sun.misc.Unsafe getSMU() {
            try {
                return sun.misc.Unsafe.getUnsafe();
            } catch (SecurityException e) {
                try {
                    return (sun.misc.Unsafe) AccessController.doPrivileged(new PrivilegedExceptionAction<sun.misc.Unsafe>() { // from class: org.ehcache.impl.internal.concurrent.JSR166Helper.Unsafe.1
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.security.PrivilegedExceptionAction
                        public sun.misc.Unsafe run() throws Exception {
                            Field[] arr$ = sun.misc.Unsafe.class.getDeclaredFields();
                            for (Field f : arr$) {
                                f.setAccessible(true);
                                Object x = f.get(null);
                                if (sun.misc.Unsafe.class.isInstance(x)) {
                                    return (sun.misc.Unsafe) sun.misc.Unsafe.class.cast(x);
                                }
                            }
                            throw new NoSuchFieldError("the Unsafe");
                        }
                    });
                } catch (PrivilegedActionException e2) {
                    throw new RuntimeException("Could not initialize intrinsics", e2.getCause());
                }
            }
        }

        static {
            try {
                SMU = getSMU();
                U = new Unsafe();
            } catch (Exception e) {
                throw new Error(e);
            }
        }

        private Unsafe() {
        }

        public static Unsafe getUnsafe() {
            return U;
        }

        public long objectFieldOffset(Field pending) {
            return SMU.objectFieldOffset(pending);
        }

        public int arrayBaseOffset(Class<?> aClass) {
            return SMU.arrayBaseOffset(aClass);
        }

        public int arrayIndexScale(Class<?> aClass) {
            return SMU.arrayIndexScale(aClass);
        }

        public boolean compareAndSwapInt(Object o, long pending, int c, int i) {
            return SMU.compareAndSwapInt(o, pending, c, i);
        }

        public boolean compareAndSwapLong(Object o, long l, long l1, long l2) {
            return SMU.compareAndSwapLong(o, l, l1, l2);
        }

        public boolean compareAndSwapObject(Object o, long l, Object o1, Object o2) {
            return SMU.compareAndSwapObject(o, l, o1, o2);
        }

        public void park(boolean b, long l) {
            SMU.park(b, l);
        }

        public void unpark(Object o) {
            SMU.unpark(o);
        }

        public void putInt(Object o, long l, int i) {
            SMU.putInt(o, l, i);
        }

        public void putOrderedInt(Object o, long qtop, int i) {
            SMU.putOrderedInt(o, qtop, i);
        }

        public void putObject(Object o, long l, Object o1) {
            SMU.putObject(o, l, o1);
        }

        public void putOrderedObject(Object o, long l, Object o1) {
            SMU.putOrderedObject(o, l, o1);
        }

        public Object getObject(Object o, long l) {
            return SMU.getObject(o, l);
        }

        public Object getObjectVolatile(Object o, long l) {
            return SMU.getObjectVolatile(o, l);
        }

        public void putObjectVolatile(Object o, long l, Object o1) {
            SMU.putObjectVolatile(o, l, o1);
        }

        public int getAndAddInt(Object o, long offset, int val) {
            int temp;
            do {
                temp = SMU.getIntVolatile(o, offset);
            } while (!SMU.compareAndSwapInt(o, offset, temp, temp + val));
            return temp;
        }

        public long getAndAddLong(Object o, long offset, long val) {
            long temp;
            do {
                temp = SMU.getLongVolatile(o, offset);
            } while (!SMU.compareAndSwapLong(o, offset, temp, temp + val));
            return temp;
        }

        public Object getAndSetObject(Object o, long offset, Object val) {
            Object temp;
            do {
                temp = SMU.getObjectVolatile(o, offset);
            } while (!SMU.compareAndSwapObject(o, offset, temp, val));
            return temp;
        }
    }
}

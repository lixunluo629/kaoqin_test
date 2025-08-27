package org.apache.commons.io.file;

import java.math.BigInteger;
import java.util.Objects;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/file/Counters.class */
public class Counters {

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/file/Counters$AbstractPathCounters.class */
    private static class AbstractPathCounters implements PathCounters {
        private final Counter byteCounter;
        private final Counter directoryCounter;
        private final Counter fileCounter;

        protected AbstractPathCounters(Counter byteCounter, Counter directoryCounter, Counter fileCounter) {
            this.byteCounter = byteCounter;
            this.directoryCounter = directoryCounter;
            this.fileCounter = fileCounter;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof AbstractPathCounters)) {
                return false;
            }
            AbstractPathCounters other = (AbstractPathCounters) obj;
            return Objects.equals(this.byteCounter, other.byteCounter) && Objects.equals(this.directoryCounter, other.directoryCounter) && Objects.equals(this.fileCounter, other.fileCounter);
        }

        @Override // org.apache.commons.io.file.Counters.PathCounters
        public Counter getByteCounter() {
            return this.byteCounter;
        }

        @Override // org.apache.commons.io.file.Counters.PathCounters
        public Counter getDirectoryCounter() {
            return this.directoryCounter;
        }

        @Override // org.apache.commons.io.file.Counters.PathCounters
        public Counter getFileCounter() {
            return this.fileCounter;
        }

        public int hashCode() {
            return Objects.hash(this.byteCounter, this.directoryCounter, this.fileCounter);
        }

        @Override // org.apache.commons.io.file.Counters.PathCounters
        public void reset() {
            this.byteCounter.reset();
            this.directoryCounter.reset();
            this.fileCounter.reset();
        }

        public String toString() {
            return String.format("%,d files, %,d directories, %,d bytes", Long.valueOf(this.fileCounter.get()), Long.valueOf(this.directoryCounter.get()), Long.valueOf(this.byteCounter.get()));
        }
    }

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/file/Counters$BigIntegerCounter.class */
    private static final class BigIntegerCounter implements Counter {
        private BigInteger value;

        private BigIntegerCounter() {
            this.value = BigInteger.ZERO;
        }

        @Override // org.apache.commons.io.file.Counters.Counter
        public void add(long val) {
            this.value = this.value.add(BigInteger.valueOf(val));
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Counter)) {
                return false;
            }
            Counter other = (Counter) obj;
            return Objects.equals(this.value, other.getBigInteger());
        }

        @Override // org.apache.commons.io.file.Counters.Counter
        public long get() {
            return this.value.longValueExact();
        }

        @Override // org.apache.commons.io.file.Counters.Counter
        public BigInteger getBigInteger() {
            return this.value;
        }

        @Override // org.apache.commons.io.file.Counters.Counter
        public Long getLong() {
            return Long.valueOf(this.value.longValueExact());
        }

        public int hashCode() {
            return Objects.hash(this.value);
        }

        @Override // org.apache.commons.io.file.Counters.Counter
        public void increment() {
            this.value = this.value.add(BigInteger.ONE);
        }

        @Override // org.apache.commons.io.file.Counters.Counter
        public void reset() {
            this.value = BigInteger.ZERO;
        }

        public String toString() {
            return this.value.toString();
        }
    }

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/file/Counters$BigIntegerPathCounters.class */
    private static final class BigIntegerPathCounters extends AbstractPathCounters {
        protected BigIntegerPathCounters() {
            super(Counters.bigIntegerCounter(), Counters.bigIntegerCounter(), Counters.bigIntegerCounter());
        }
    }

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/file/Counters$Counter.class */
    public interface Counter {
        void add(long j);

        long get();

        BigInteger getBigInteger();

        Long getLong();

        void increment();

        default void reset() {
        }
    }

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/file/Counters$LongCounter.class */
    private static final class LongCounter implements Counter {
        private long value;

        private LongCounter() {
        }

        @Override // org.apache.commons.io.file.Counters.Counter
        public void add(long add) {
            this.value += add;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Counter)) {
                return false;
            }
            Counter other = (Counter) obj;
            return this.value == other.get();
        }

        @Override // org.apache.commons.io.file.Counters.Counter
        public long get() {
            return this.value;
        }

        @Override // org.apache.commons.io.file.Counters.Counter
        public BigInteger getBigInteger() {
            return BigInteger.valueOf(this.value);
        }

        @Override // org.apache.commons.io.file.Counters.Counter
        public Long getLong() {
            return Long.valueOf(this.value);
        }

        public int hashCode() {
            return Objects.hash(Long.valueOf(this.value));
        }

        @Override // org.apache.commons.io.file.Counters.Counter
        public void increment() {
            this.value++;
        }

        @Override // org.apache.commons.io.file.Counters.Counter
        public void reset() {
            this.value = 0L;
        }

        public String toString() {
            return Long.toString(this.value);
        }
    }

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/file/Counters$LongPathCounters.class */
    private static final class LongPathCounters extends AbstractPathCounters {
        protected LongPathCounters() {
            super(Counters.longCounter(), Counters.longCounter(), Counters.longCounter());
        }
    }

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/file/Counters$NoopCounter.class */
    private static final class NoopCounter implements Counter {
        static final NoopCounter INSTANCE = new NoopCounter();

        private NoopCounter() {
        }

        @Override // org.apache.commons.io.file.Counters.Counter
        public void add(long add) {
        }

        @Override // org.apache.commons.io.file.Counters.Counter
        public long get() {
            return 0L;
        }

        @Override // org.apache.commons.io.file.Counters.Counter
        public BigInteger getBigInteger() {
            return BigInteger.ZERO;
        }

        @Override // org.apache.commons.io.file.Counters.Counter
        public Long getLong() {
            return 0L;
        }

        @Override // org.apache.commons.io.file.Counters.Counter
        public void increment() {
        }

        public String toString() {
            return "0";
        }
    }

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/file/Counters$NoopPathCounters.class */
    private static final class NoopPathCounters extends AbstractPathCounters {
        static final NoopPathCounters INSTANCE = new NoopPathCounters();

        private NoopPathCounters() {
            super(Counters.noopCounter(), Counters.noopCounter(), Counters.noopCounter());
        }
    }

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/file/Counters$PathCounters.class */
    public interface PathCounters {
        Counter getByteCounter();

        Counter getDirectoryCounter();

        Counter getFileCounter();

        default void reset() {
        }
    }

    public static Counter bigIntegerCounter() {
        return new BigIntegerCounter();
    }

    public static PathCounters bigIntegerPathCounters() {
        return new BigIntegerPathCounters();
    }

    public static Counter longCounter() {
        return new LongCounter();
    }

    public static PathCounters longPathCounters() {
        return new LongPathCounters();
    }

    public static Counter noopCounter() {
        return NoopCounter.INSTANCE;
    }

    public static PathCounters noopPathCounters() {
        return NoopPathCounters.INSTANCE;
    }
}

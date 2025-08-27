package com.google.common.hash;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import javax.annotation.Nullable;

@Beta
/* loaded from: guava-18.0.jar:com/google/common/hash/Funnels.class */
public final class Funnels {
    private Funnels() {
    }

    public static Funnel<byte[]> byteArrayFunnel() {
        return ByteArrayFunnel.INSTANCE;
    }

    /* loaded from: guava-18.0.jar:com/google/common/hash/Funnels$ByteArrayFunnel.class */
    private enum ByteArrayFunnel implements Funnel<byte[]> {
        INSTANCE;

        @Override // com.google.common.hash.Funnel
        public void funnel(byte[] from, PrimitiveSink into) {
            into.putBytes(from);
        }

        @Override // java.lang.Enum
        public String toString() {
            return "Funnels.byteArrayFunnel()";
        }
    }

    public static Funnel<CharSequence> unencodedCharsFunnel() {
        return UnencodedCharsFunnel.INSTANCE;
    }

    /* loaded from: guava-18.0.jar:com/google/common/hash/Funnels$UnencodedCharsFunnel.class */
    private enum UnencodedCharsFunnel implements Funnel<CharSequence> {
        INSTANCE;

        @Override // com.google.common.hash.Funnel
        public void funnel(CharSequence from, PrimitiveSink into) {
            into.putUnencodedChars(from);
        }

        @Override // java.lang.Enum
        public String toString() {
            return "Funnels.unencodedCharsFunnel()";
        }
    }

    public static Funnel<CharSequence> stringFunnel(Charset charset) {
        return new StringCharsetFunnel(charset);
    }

    /* loaded from: guava-18.0.jar:com/google/common/hash/Funnels$StringCharsetFunnel.class */
    private static class StringCharsetFunnel implements Funnel<CharSequence>, Serializable {
        private final Charset charset;

        StringCharsetFunnel(Charset charset) {
            this.charset = (Charset) Preconditions.checkNotNull(charset);
        }

        @Override // com.google.common.hash.Funnel
        public void funnel(CharSequence from, PrimitiveSink into) {
            into.putString(from, this.charset);
        }

        public String toString() {
            String strValueOf = String.valueOf(String.valueOf(this.charset.name()));
            return new StringBuilder(22 + strValueOf.length()).append("Funnels.stringFunnel(").append(strValueOf).append(")").toString();
        }

        public boolean equals(@Nullable Object o) {
            if (o instanceof StringCharsetFunnel) {
                StringCharsetFunnel funnel = (StringCharsetFunnel) o;
                return this.charset.equals(funnel.charset);
            }
            return false;
        }

        public int hashCode() {
            return StringCharsetFunnel.class.hashCode() ^ this.charset.hashCode();
        }

        Object writeReplace() {
            return new SerializedForm(this.charset);
        }

        /* loaded from: guava-18.0.jar:com/google/common/hash/Funnels$StringCharsetFunnel$SerializedForm.class */
        private static class SerializedForm implements Serializable {
            private final String charsetCanonicalName;
            private static final long serialVersionUID = 0;

            SerializedForm(Charset charset) {
                this.charsetCanonicalName = charset.name();
            }

            private Object readResolve() {
                return Funnels.stringFunnel(Charset.forName(this.charsetCanonicalName));
            }
        }
    }

    public static Funnel<Integer> integerFunnel() {
        return IntegerFunnel.INSTANCE;
    }

    /* loaded from: guava-18.0.jar:com/google/common/hash/Funnels$IntegerFunnel.class */
    private enum IntegerFunnel implements Funnel<Integer> {
        INSTANCE;

        @Override // com.google.common.hash.Funnel
        public void funnel(Integer from, PrimitiveSink into) {
            into.putInt(from.intValue());
        }

        @Override // java.lang.Enum
        public String toString() {
            return "Funnels.integerFunnel()";
        }
    }

    public static <E> Funnel<Iterable<? extends E>> sequentialFunnel(Funnel<E> elementFunnel) {
        return new SequentialFunnel(elementFunnel);
    }

    /* loaded from: guava-18.0.jar:com/google/common/hash/Funnels$SequentialFunnel.class */
    private static class SequentialFunnel<E> implements Funnel<Iterable<? extends E>>, Serializable {
        private final Funnel<E> elementFunnel;

        SequentialFunnel(Funnel<E> elementFunnel) {
            this.elementFunnel = (Funnel) Preconditions.checkNotNull(elementFunnel);
        }

        @Override // com.google.common.hash.Funnel
        public void funnel(Iterable<? extends E> from, PrimitiveSink into) {
            for (E e : from) {
                this.elementFunnel.funnel(e, into);
            }
        }

        public String toString() {
            String strValueOf = String.valueOf(String.valueOf(this.elementFunnel));
            return new StringBuilder(26 + strValueOf.length()).append("Funnels.sequentialFunnel(").append(strValueOf).append(")").toString();
        }

        public boolean equals(@Nullable Object o) {
            if (o instanceof SequentialFunnel) {
                SequentialFunnel<?> funnel = (SequentialFunnel) o;
                return this.elementFunnel.equals(funnel.elementFunnel);
            }
            return false;
        }

        public int hashCode() {
            return SequentialFunnel.class.hashCode() ^ this.elementFunnel.hashCode();
        }
    }

    public static Funnel<Long> longFunnel() {
        return LongFunnel.INSTANCE;
    }

    /* loaded from: guava-18.0.jar:com/google/common/hash/Funnels$LongFunnel.class */
    private enum LongFunnel implements Funnel<Long> {
        INSTANCE;

        @Override // com.google.common.hash.Funnel
        public void funnel(Long from, PrimitiveSink into) {
            into.putLong(from.longValue());
        }

        @Override // java.lang.Enum
        public String toString() {
            return "Funnels.longFunnel()";
        }
    }

    public static OutputStream asOutputStream(PrimitiveSink sink) {
        return new SinkAsStream(sink);
    }

    /* loaded from: guava-18.0.jar:com/google/common/hash/Funnels$SinkAsStream.class */
    private static class SinkAsStream extends OutputStream {
        final PrimitiveSink sink;

        SinkAsStream(PrimitiveSink sink) {
            this.sink = (PrimitiveSink) Preconditions.checkNotNull(sink);
        }

        @Override // java.io.OutputStream
        public void write(int b) {
            this.sink.putByte((byte) b);
        }

        @Override // java.io.OutputStream
        public void write(byte[] bytes) {
            this.sink.putBytes(bytes);
        }

        @Override // java.io.OutputStream
        public void write(byte[] bytes, int off, int len) {
            this.sink.putBytes(bytes, off, len);
        }

        public String toString() {
            String strValueOf = String.valueOf(String.valueOf(this.sink));
            return new StringBuilder(24 + strValueOf.length()).append("Funnels.asOutputStream(").append(strValueOf).append(")").toString();
        }
    }
}

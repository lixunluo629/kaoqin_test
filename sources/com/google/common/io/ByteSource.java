package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.base.Ascii;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.hash.Funnels;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Iterator;

/* loaded from: guava-18.0.jar:com/google/common/io/ByteSource.class */
public abstract class ByteSource {
    private static final int BUF_SIZE = 4096;
    private static final byte[] countBuffer = new byte[4096];

    public abstract InputStream openStream() throws IOException;

    protected ByteSource() {
    }

    public CharSource asCharSource(Charset charset) {
        return new AsCharSource(charset);
    }

    public InputStream openBufferedStream() throws IOException {
        InputStream in = openStream();
        return in instanceof BufferedInputStream ? (BufferedInputStream) in : new BufferedInputStream(in);
    }

    public ByteSource slice(long offset, long length) {
        return new SlicedByteSource(offset, length);
    }

    public boolean isEmpty() throws Throwable {
        Closer closer = Closer.create();
        try {
            try {
                InputStream in = (InputStream) closer.register(openStream());
                return in.read() == -1;
            } catch (Throwable e) {
                throw closer.rethrow(e);
            }
        } finally {
            closer.close();
        }
    }

    public long size() throws Throwable {
        Closer closer = Closer.create();
        try {
            InputStream in = (InputStream) closer.register(openStream());
            long jCountBySkipping = countBySkipping(in);
            closer.close();
            return jCountBySkipping;
        } catch (IOException e) {
            closer.close();
            closer = Closer.create();
            try {
                try {
                    InputStream in2 = (InputStream) closer.register(openStream());
                    long jCountByReading = countByReading(in2);
                    closer.close();
                    return jCountByReading;
                } catch (Throwable e2) {
                    throw closer.rethrow(e2);
                }
            } finally {
                closer.close();
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private long countBySkipping(InputStream in) throws IOException {
        long j = 0;
        while (true) {
            long count = j;
            long skipped = in.skip(Math.min(in.available(), Integer.MAX_VALUE));
            if (skipped <= 0) {
                if (in.read() == -1) {
                    return count;
                }
                if (count == 0 && in.available() == 0) {
                    throw new IOException();
                }
                j = count + 1;
            } else {
                j = count + skipped;
            }
        }
    }

    private long countByReading(InputStream in) throws IOException {
        long j = 0;
        while (true) {
            long count = j;
            long read = in.read(countBuffer);
            if (read != -1) {
                j = count + read;
            } else {
                return count;
            }
        }
    }

    public long copyTo(OutputStream output) throws Throwable {
        RuntimeException runtimeExceptionRethrow;
        Preconditions.checkNotNull(output);
        Closer closer = Closer.create();
        try {
            try {
                InputStream in = (InputStream) closer.register(openStream());
                long jCopy = ByteStreams.copy(in, output);
                closer.close();
                return jCopy;
            } finally {
            }
        } catch (Throwable th) {
            closer.close();
            throw th;
        }
    }

    public long copyTo(ByteSink sink) throws Throwable {
        RuntimeException runtimeExceptionRethrow;
        Preconditions.checkNotNull(sink);
        Closer closer = Closer.create();
        try {
            try {
                InputStream in = (InputStream) closer.register(openStream());
                OutputStream out = (OutputStream) closer.register(sink.openStream());
                long jCopy = ByteStreams.copy(in, out);
                closer.close();
                return jCopy;
            } finally {
            }
        } catch (Throwable th) {
            closer.close();
            throw th;
        }
    }

    public byte[] read() throws Throwable {
        RuntimeException runtimeExceptionRethrow;
        Closer closer = Closer.create();
        try {
            try {
                InputStream in = (InputStream) closer.register(openStream());
                byte[] byteArray = ByteStreams.toByteArray(in);
                closer.close();
                return byteArray;
            } finally {
            }
        } catch (Throwable th) {
            closer.close();
            throw th;
        }
    }

    @Beta
    public <T> T read(ByteProcessor<T> byteProcessor) throws Throwable {
        RuntimeException runtimeExceptionRethrow;
        Preconditions.checkNotNull(byteProcessor);
        Closer closerCreate = Closer.create();
        try {
            try {
                T t = (T) ByteStreams.readBytes((InputStream) closerCreate.register(openStream()), byteProcessor);
                closerCreate.close();
                return t;
            } finally {
            }
        } catch (Throwable th) {
            closerCreate.close();
            throw th;
        }
    }

    public HashCode hash(HashFunction hashFunction) throws Throwable {
        Hasher hasher = hashFunction.newHasher();
        copyTo(Funnels.asOutputStream(hasher));
        return hasher.hash();
    }

    public boolean contentEquals(ByteSource other) throws Throwable {
        RuntimeException runtimeExceptionRethrow;
        int read1;
        Preconditions.checkNotNull(other);
        byte[] buf1 = new byte[4096];
        byte[] buf2 = new byte[4096];
        Closer closer = Closer.create();
        try {
            try {
                InputStream in1 = (InputStream) closer.register(openStream());
                InputStream in2 = (InputStream) closer.register(other.openStream());
                do {
                    read1 = ByteStreams.read(in1, buf1, 0, 4096);
                    int read2 = ByteStreams.read(in2, buf2, 0, 4096);
                    if (read1 != read2 || !Arrays.equals(buf1, buf2)) {
                        return false;
                    }
                } while (read1 == 4096);
                closer.close();
                return true;
            } finally {
            }
        } finally {
            closer.close();
        }
    }

    public static ByteSource concat(Iterable<? extends ByteSource> sources) {
        return new ConcatenatedByteSource(sources);
    }

    public static ByteSource concat(Iterator<? extends ByteSource> sources) {
        return concat(ImmutableList.copyOf(sources));
    }

    public static ByteSource concat(ByteSource... sources) {
        return concat(ImmutableList.copyOf(sources));
    }

    public static ByteSource wrap(byte[] b) {
        return new ByteArrayByteSource(b);
    }

    public static ByteSource empty() {
        return EmptyByteSource.INSTANCE;
    }

    /* loaded from: guava-18.0.jar:com/google/common/io/ByteSource$AsCharSource.class */
    private final class AsCharSource extends CharSource {
        private final Charset charset;

        private AsCharSource(Charset charset) {
            this.charset = (Charset) Preconditions.checkNotNull(charset);
        }

        @Override // com.google.common.io.CharSource
        public Reader openStream() throws IOException {
            return new InputStreamReader(ByteSource.this.openStream(), this.charset);
        }

        public String toString() {
            String strValueOf = String.valueOf(String.valueOf(ByteSource.this.toString()));
            String strValueOf2 = String.valueOf(String.valueOf(this.charset));
            return new StringBuilder(15 + strValueOf.length() + strValueOf2.length()).append(strValueOf).append(".asCharSource(").append(strValueOf2).append(")").toString();
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/io/ByteSource$SlicedByteSource.class */
    private final class SlicedByteSource extends ByteSource {
        private final long offset;
        private final long length;

        private SlicedByteSource(long offset, long length) {
            Preconditions.checkArgument(offset >= 0, "offset (%s) may not be negative", Long.valueOf(offset));
            Preconditions.checkArgument(length >= 0, "length (%s) may not be negative", Long.valueOf(length));
            this.offset = offset;
            this.length = length;
        }

        @Override // com.google.common.io.ByteSource
        public InputStream openStream() throws IOException {
            return sliceStream(ByteSource.this.openStream());
        }

        @Override // com.google.common.io.ByteSource
        public InputStream openBufferedStream() throws IOException {
            return sliceStream(ByteSource.this.openBufferedStream());
        }

        private InputStream sliceStream(InputStream in) throws Throwable {
            if (this.offset > 0) {
                try {
                    ByteStreams.skipFully(in, this.offset);
                } finally {
                }
            }
            return ByteStreams.limit(in, this.length);
        }

        @Override // com.google.common.io.ByteSource
        public ByteSource slice(long offset, long length) {
            Preconditions.checkArgument(offset >= 0, "offset (%s) may not be negative", Long.valueOf(offset));
            Preconditions.checkArgument(length >= 0, "length (%s) may not be negative", Long.valueOf(length));
            long maxLength = this.length - offset;
            return ByteSource.this.slice(this.offset + offset, Math.min(length, maxLength));
        }

        @Override // com.google.common.io.ByteSource
        public boolean isEmpty() throws IOException {
            return this.length == 0 || super.isEmpty();
        }

        public String toString() {
            String strValueOf = String.valueOf(String.valueOf(ByteSource.this.toString()));
            long j = this.offset;
            return new StringBuilder(50 + strValueOf.length()).append(strValueOf).append(".slice(").append(j).append(", ").append(this.length).append(")").toString();
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/io/ByteSource$ByteArrayByteSource.class */
    private static class ByteArrayByteSource extends ByteSource {
        protected final byte[] bytes;

        protected ByteArrayByteSource(byte[] bytes) {
            this.bytes = (byte[]) Preconditions.checkNotNull(bytes);
        }

        @Override // com.google.common.io.ByteSource
        public InputStream openStream() {
            return new ByteArrayInputStream(this.bytes);
        }

        @Override // com.google.common.io.ByteSource
        public InputStream openBufferedStream() throws IOException {
            return openStream();
        }

        @Override // com.google.common.io.ByteSource
        public boolean isEmpty() {
            return this.bytes.length == 0;
        }

        @Override // com.google.common.io.ByteSource
        public long size() {
            return this.bytes.length;
        }

        @Override // com.google.common.io.ByteSource
        public byte[] read() {
            return (byte[]) this.bytes.clone();
        }

        @Override // com.google.common.io.ByteSource
        public long copyTo(OutputStream output) throws IOException {
            output.write(this.bytes);
            return this.bytes.length;
        }

        @Override // com.google.common.io.ByteSource
        public <T> T read(ByteProcessor<T> processor) throws IOException {
            processor.processBytes(this.bytes, 0, this.bytes.length);
            return processor.getResult();
        }

        @Override // com.google.common.io.ByteSource
        public HashCode hash(HashFunction hashFunction) throws IOException {
            return hashFunction.hashBytes(this.bytes);
        }

        public String toString() {
            String strValueOf = String.valueOf(String.valueOf(Ascii.truncate(BaseEncoding.base16().encode(this.bytes), 30, "...")));
            return new StringBuilder(17 + strValueOf.length()).append("ByteSource.wrap(").append(strValueOf).append(")").toString();
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/io/ByteSource$EmptyByteSource.class */
    private static final class EmptyByteSource extends ByteArrayByteSource {
        private static final EmptyByteSource INSTANCE = new EmptyByteSource();

        private EmptyByteSource() {
            super(new byte[0]);
        }

        @Override // com.google.common.io.ByteSource
        public CharSource asCharSource(Charset charset) {
            Preconditions.checkNotNull(charset);
            return CharSource.empty();
        }

        @Override // com.google.common.io.ByteSource.ByteArrayByteSource, com.google.common.io.ByteSource
        public byte[] read() {
            return this.bytes;
        }

        @Override // com.google.common.io.ByteSource.ByteArrayByteSource
        public String toString() {
            return "ByteSource.empty()";
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/io/ByteSource$ConcatenatedByteSource.class */
    private static final class ConcatenatedByteSource extends ByteSource {
        private final Iterable<? extends ByteSource> sources;

        ConcatenatedByteSource(Iterable<? extends ByteSource> sources) {
            this.sources = (Iterable) Preconditions.checkNotNull(sources);
        }

        @Override // com.google.common.io.ByteSource
        public InputStream openStream() throws IOException {
            return new MultiInputStream(this.sources.iterator());
        }

        @Override // com.google.common.io.ByteSource
        public boolean isEmpty() throws IOException {
            for (ByteSource source : this.sources) {
                if (!source.isEmpty()) {
                    return false;
                }
            }
            return true;
        }

        @Override // com.google.common.io.ByteSource
        public long size() throws IOException {
            long result = 0;
            for (ByteSource source : this.sources) {
                result += source.size();
            }
            return result;
        }

        public String toString() {
            String strValueOf = String.valueOf(String.valueOf(this.sources));
            return new StringBuilder(19 + strValueOf.length()).append("ByteSource.concat(").append(strValueOf).append(")").toString();
        }
    }
}

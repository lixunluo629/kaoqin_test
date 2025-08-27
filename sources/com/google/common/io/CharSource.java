package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.base.Ascii;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

/* loaded from: guava-18.0.jar:com/google/common/io/CharSource.class */
public abstract class CharSource {
    public abstract Reader openStream() throws IOException;

    protected CharSource() {
    }

    public BufferedReader openBufferedStream() throws IOException {
        Reader reader = openStream();
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
    }

    public long copyTo(Appendable appendable) throws Throwable {
        RuntimeException runtimeExceptionRethrow;
        Preconditions.checkNotNull(appendable);
        Closer closer = Closer.create();
        try {
            try {
                Reader reader = (Reader) closer.register(openStream());
                long jCopy = CharStreams.copy(reader, appendable);
                closer.close();
                return jCopy;
            } finally {
            }
        } catch (Throwable th) {
            closer.close();
            throw th;
        }
    }

    public long copyTo(CharSink sink) throws Throwable {
        RuntimeException runtimeExceptionRethrow;
        Preconditions.checkNotNull(sink);
        Closer closer = Closer.create();
        try {
            try {
                Reader reader = (Reader) closer.register(openStream());
                Writer writer = (Writer) closer.register(sink.openStream());
                long jCopy = CharStreams.copy(reader, writer);
                closer.close();
                return jCopy;
            } finally {
            }
        } catch (Throwable th) {
            closer.close();
            throw th;
        }
    }

    public String read() throws Throwable {
        RuntimeException runtimeExceptionRethrow;
        Closer closer = Closer.create();
        try {
            try {
                Reader reader = (Reader) closer.register(openStream());
                String string = CharStreams.toString(reader);
                closer.close();
                return string;
            } finally {
            }
        } catch (Throwable th) {
            closer.close();
            throw th;
        }
    }

    @Nullable
    public String readFirstLine() throws Throwable {
        RuntimeException runtimeExceptionRethrow;
        Closer closer = Closer.create();
        try {
            try {
                BufferedReader reader = (BufferedReader) closer.register(openBufferedStream());
                String line = reader.readLine();
                closer.close();
                return line;
            } finally {
            }
        } catch (Throwable th) {
            closer.close();
            throw th;
        }
    }

    public ImmutableList<String> readLines() throws Throwable {
        RuntimeException runtimeExceptionRethrow;
        Closer closer = Closer.create();
        try {
            try {
                BufferedReader reader = (BufferedReader) closer.register(openBufferedStream());
                List<String> result = Lists.newArrayList();
                while (true) {
                    String line = reader.readLine();
                    if (line != null) {
                        result.add(line);
                    } else {
                        ImmutableList<String> immutableListCopyOf = ImmutableList.copyOf((Collection) result);
                        closer.close();
                        return immutableListCopyOf;
                    }
                }
            } finally {
            }
        } catch (Throwable th) {
            closer.close();
            throw th;
        }
    }

    @Beta
    public <T> T readLines(LineProcessor<T> lineProcessor) throws Throwable {
        RuntimeException runtimeExceptionRethrow;
        Preconditions.checkNotNull(lineProcessor);
        Closer closerCreate = Closer.create();
        try {
            try {
                T t = (T) CharStreams.readLines((Reader) closerCreate.register(openStream()), lineProcessor);
                closerCreate.close();
                return t;
            } finally {
            }
        } catch (Throwable th) {
            closerCreate.close();
            throw th;
        }
    }

    public boolean isEmpty() throws Throwable {
        Closer closer = Closer.create();
        try {
            try {
                Reader reader = (Reader) closer.register(openStream());
                return reader.read() == -1;
            } catch (Throwable e) {
                throw closer.rethrow(e);
            }
        } finally {
            closer.close();
        }
    }

    public static CharSource concat(Iterable<? extends CharSource> sources) {
        return new ConcatenatedCharSource(sources);
    }

    public static CharSource concat(Iterator<? extends CharSource> sources) {
        return concat(ImmutableList.copyOf(sources));
    }

    public static CharSource concat(CharSource... sources) {
        return concat(ImmutableList.copyOf(sources));
    }

    public static CharSource wrap(CharSequence charSequence) {
        return new CharSequenceCharSource(charSequence);
    }

    public static CharSource empty() {
        return EmptyCharSource.INSTANCE;
    }

    /* loaded from: guava-18.0.jar:com/google/common/io/CharSource$CharSequenceCharSource.class */
    private static class CharSequenceCharSource extends CharSource {
        private static final Splitter LINE_SPLITTER = Splitter.on(Pattern.compile("\r\n|\n|\r"));
        private final CharSequence seq;

        protected CharSequenceCharSource(CharSequence seq) {
            this.seq = (CharSequence) Preconditions.checkNotNull(seq);
        }

        @Override // com.google.common.io.CharSource
        public Reader openStream() {
            return new CharSequenceReader(this.seq);
        }

        @Override // com.google.common.io.CharSource
        public String read() {
            return this.seq.toString();
        }

        @Override // com.google.common.io.CharSource
        public boolean isEmpty() {
            return this.seq.length() == 0;
        }

        private Iterable<String> lines() {
            return new Iterable<String>() { // from class: com.google.common.io.CharSource.CharSequenceCharSource.1
                @Override // java.lang.Iterable
                public Iterator<String> iterator() {
                    return new AbstractIterator<String>() { // from class: com.google.common.io.CharSource.CharSequenceCharSource.1.1
                        Iterator<String> lines;

                        {
                            this.lines = CharSequenceCharSource.LINE_SPLITTER.split(CharSequenceCharSource.this.seq).iterator();
                        }

                        /* JADX INFO: Access modifiers changed from: protected */
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // com.google.common.collect.AbstractIterator
                        public String computeNext() {
                            if (this.lines.hasNext()) {
                                String next = this.lines.next();
                                if (this.lines.hasNext() || !next.isEmpty()) {
                                    return next;
                                }
                            }
                            return endOfData();
                        }
                    };
                }
            };
        }

        @Override // com.google.common.io.CharSource
        public String readFirstLine() {
            Iterator<String> lines = lines().iterator();
            if (lines.hasNext()) {
                return lines.next();
            }
            return null;
        }

        @Override // com.google.common.io.CharSource
        public ImmutableList<String> readLines() {
            return ImmutableList.copyOf(lines());
        }

        @Override // com.google.common.io.CharSource
        public <T> T readLines(LineProcessor<T> processor) throws IOException {
            for (String line : lines()) {
                if (!processor.processLine(line)) {
                    break;
                }
            }
            return processor.getResult();
        }

        public String toString() {
            String strValueOf = String.valueOf(String.valueOf(Ascii.truncate(this.seq, 30, "...")));
            return new StringBuilder(17 + strValueOf.length()).append("CharSource.wrap(").append(strValueOf).append(")").toString();
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/io/CharSource$EmptyCharSource.class */
    private static final class EmptyCharSource extends CharSequenceCharSource {
        private static final EmptyCharSource INSTANCE = new EmptyCharSource();

        private EmptyCharSource() {
            super("");
        }

        @Override // com.google.common.io.CharSource.CharSequenceCharSource
        public String toString() {
            return "CharSource.empty()";
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/io/CharSource$ConcatenatedCharSource.class */
    private static final class ConcatenatedCharSource extends CharSource {
        private final Iterable<? extends CharSource> sources;

        ConcatenatedCharSource(Iterable<? extends CharSource> sources) {
            this.sources = (Iterable) Preconditions.checkNotNull(sources);
        }

        @Override // com.google.common.io.CharSource
        public Reader openStream() throws IOException {
            return new MultiReader(this.sources.iterator());
        }

        @Override // com.google.common.io.CharSource
        public boolean isEmpty() throws IOException {
            for (CharSource source : this.sources) {
                if (!source.isEmpty()) {
                    return false;
                }
            }
            return true;
        }

        public String toString() {
            String strValueOf = String.valueOf(String.valueOf(this.sources));
            return new StringBuilder(19 + strValueOf.length()).append("CharSource.concat(").append(strValueOf).append(")").toString();
        }
    }
}

package org.apache.commons.io.input;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Duration;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.httpclient.cookie.Cookie2;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.ThreadUtils;
import org.apache.commons.io.build.AbstractOrigin;
import org.apache.commons.io.build.AbstractOriginSupplier;
import org.apache.commons.io.build.AbstractStreamBuilder;
import org.apache.commons.io.file.PathUtils;
import org.apache.commons.io.file.attribute.FileTimes;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/Tailer.class */
public class Tailer implements Runnable, AutoCloseable {
    private static final int DEFAULT_DELAY_MILLIS = 1000;
    private static final String RAF_READ_ONLY_MODE = "r";
    private static final Charset DEFAULT_CHARSET = Charset.defaultCharset();
    private final byte[] inbuf;
    private final Tailable tailable;
    private final Charset charset;
    private final Duration delayDuration;
    private final boolean tailAtEnd;
    private final TailerListener listener;
    private final boolean reOpen;
    private volatile boolean run;

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/Tailer$RandomAccessResourceBridge.class */
    public interface RandomAccessResourceBridge extends Closeable {
        long getPointer() throws IOException;

        int read(byte[] bArr) throws IOException;

        void seek(long j) throws IOException;
    }

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/Tailer$Tailable.class */
    public interface Tailable {
        RandomAccessResourceBridge getRandomAccess(String str) throws FileNotFoundException;

        boolean isNewer(FileTime fileTime) throws IOException;

        FileTime lastModifiedFileTime() throws IOException;

        long size() throws IOException;
    }

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/Tailer$Builder.class */
    public static class Builder extends AbstractStreamBuilder<Tailer, Builder> {
        private static final Duration DEFAULT_DELAY_DURATION = Duration.ofMillis(1000);
        private Tailable tailable;
        private TailerListener tailerListener;
        private boolean tailFromEnd;
        private boolean reOpen;
        private Duration delayDuration = DEFAULT_DELAY_DURATION;
        private boolean startThread = true;
        private ExecutorService executorService = Executors.newSingleThreadExecutor(Builder::newDaemonThread);

        @Override // org.apache.commons.io.build.AbstractOriginSupplier
        protected /* bridge */ /* synthetic */ AbstractOriginSupplier setOrigin(AbstractOrigin abstractOrigin) {
            return setOrigin((AbstractOrigin<?, ?>) abstractOrigin);
        }

        private static Thread newDaemonThread(Runnable runnable) {
            Thread thread = new Thread(runnable, "commons-io-tailer");
            thread.setDaemon(true);
            return thread;
        }

        @Override // org.apache.commons.io.function.IOSupplier
        public Tailer get() {
            Tailer tailer = new Tailer(this.tailable, getCharset(), this.tailerListener, this.delayDuration, this.tailFromEnd, this.reOpen, getBufferSize());
            if (this.startThread) {
                this.executorService.submit(tailer);
            }
            return tailer;
        }

        public Builder setDelayDuration(Duration delayDuration) {
            this.delayDuration = delayDuration != null ? delayDuration : DEFAULT_DELAY_DURATION;
            return this;
        }

        public Builder setExecutorService(ExecutorService executorService) {
            this.executorService = (ExecutorService) Objects.requireNonNull(executorService, "executorService");
            return this;
        }

        @Override // org.apache.commons.io.build.AbstractOriginSupplier
        protected Builder setOrigin(AbstractOrigin<?, ?> origin) {
            setTailable(new TailablePath(origin.getPath(), new LinkOption[0]));
            return (Builder) super.setOrigin(origin);
        }

        public Builder setReOpen(boolean reOpen) {
            this.reOpen = reOpen;
            return this;
        }

        public Builder setStartThread(boolean startThread) {
            this.startThread = startThread;
            return this;
        }

        public Builder setTailable(Tailable tailable) {
            this.tailable = (Tailable) Objects.requireNonNull(tailable, "tailable");
            return this;
        }

        public Builder setTailerListener(TailerListener tailerListener) {
            this.tailerListener = (TailerListener) Objects.requireNonNull(tailerListener, "tailerListener");
            return this;
        }

        public Builder setTailFromEnd(boolean end) {
            this.tailFromEnd = end;
            return this;
        }
    }

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/Tailer$RandomAccessFileBridge.class */
    private static final class RandomAccessFileBridge implements RandomAccessResourceBridge {
        private final RandomAccessFile randomAccessFile;

        private RandomAccessFileBridge(File file, String mode) throws FileNotFoundException {
            this.randomAccessFile = new RandomAccessFile(file, mode);
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            this.randomAccessFile.close();
        }

        @Override // org.apache.commons.io.input.Tailer.RandomAccessResourceBridge
        public long getPointer() throws IOException {
            return this.randomAccessFile.getFilePointer();
        }

        @Override // org.apache.commons.io.input.Tailer.RandomAccessResourceBridge
        public int read(byte[] b) throws IOException {
            return this.randomAccessFile.read(b);
        }

        @Override // org.apache.commons.io.input.Tailer.RandomAccessResourceBridge
        public void seek(long position) throws IOException {
            this.randomAccessFile.seek(position);
        }
    }

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/Tailer$TailablePath.class */
    private static final class TailablePath implements Tailable {
        private final Path path;
        private final LinkOption[] linkOptions;

        private TailablePath(Path path, LinkOption... linkOptions) {
            this.path = (Path) Objects.requireNonNull(path, Cookie2.PATH);
            this.linkOptions = linkOptions;
        }

        Path getPath() {
            return this.path;
        }

        @Override // org.apache.commons.io.input.Tailer.Tailable
        public RandomAccessResourceBridge getRandomAccess(String mode) throws FileNotFoundException {
            return new RandomAccessFileBridge(this.path.toFile(), mode);
        }

        @Override // org.apache.commons.io.input.Tailer.Tailable
        public boolean isNewer(FileTime fileTime) throws IOException {
            return PathUtils.isNewer(this.path, fileTime, this.linkOptions);
        }

        @Override // org.apache.commons.io.input.Tailer.Tailable
        public FileTime lastModifiedFileTime() throws IOException {
            return Files.getLastModifiedTime(this.path, this.linkOptions);
        }

        @Override // org.apache.commons.io.input.Tailer.Tailable
        public long size() throws IOException {
            return Files.size(this.path);
        }

        public String toString() {
            return "TailablePath [file=" + this.path + ", linkOptions=" + Arrays.toString(this.linkOptions) + "]";
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Deprecated
    public static Tailer create(File file, Charset charset, TailerListener listener, long delayMillis, boolean end, boolean reOpen, int bufferSize) {
        return ((Builder) builder().setFile(file)).setTailerListener(listener).setCharset(charset).setDelayDuration(Duration.ofMillis(delayMillis)).setTailFromEnd(end).setReOpen(reOpen).setBufferSize(bufferSize).get();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Deprecated
    public static Tailer create(File file, TailerListener listener) {
        return ((Builder) builder().setFile(file)).setTailerListener(listener).get();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Deprecated
    public static Tailer create(File file, TailerListener listener, long delayMillis) {
        return ((Builder) builder().setFile(file)).setTailerListener(listener).setDelayDuration(Duration.ofMillis(delayMillis)).get();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Deprecated
    public static Tailer create(File file, TailerListener listener, long delayMillis, boolean end) {
        return ((Builder) builder().setFile(file)).setTailerListener(listener).setDelayDuration(Duration.ofMillis(delayMillis)).setTailFromEnd(end).get();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Deprecated
    public static Tailer create(File file, TailerListener listener, long delayMillis, boolean end, boolean reOpen) {
        return ((Builder) builder().setFile(file)).setTailerListener(listener).setDelayDuration(Duration.ofMillis(delayMillis)).setTailFromEnd(end).setReOpen(reOpen).get();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Deprecated
    public static Tailer create(File file, TailerListener listener, long delayMillis, boolean end, boolean reOpen, int bufferSize) {
        return ((Builder) builder().setFile(file)).setTailerListener(listener).setDelayDuration(Duration.ofMillis(delayMillis)).setTailFromEnd(end).setReOpen(reOpen).setBufferSize(bufferSize).get();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Deprecated
    public static Tailer create(File file, TailerListener listener, long delayMillis, boolean end, int bufferSize) {
        return ((Builder) builder().setFile(file)).setTailerListener(listener).setDelayDuration(Duration.ofMillis(delayMillis)).setTailFromEnd(end).setBufferSize(bufferSize).get();
    }

    @Deprecated
    public Tailer(File file, Charset charset, TailerListener listener, long delayMillis, boolean end, boolean reOpen, int bufSize) {
        this(new TailablePath(file.toPath(), new LinkOption[0]), charset, listener, Duration.ofMillis(delayMillis), end, reOpen, bufSize);
    }

    @Deprecated
    public Tailer(File file, TailerListener listener) {
        this(file, listener, 1000L);
    }

    @Deprecated
    public Tailer(File file, TailerListener listener, long delayMillis) {
        this(file, listener, delayMillis, false);
    }

    @Deprecated
    public Tailer(File file, TailerListener listener, long delayMillis, boolean end) {
        this(file, listener, delayMillis, end, 8192);
    }

    @Deprecated
    public Tailer(File file, TailerListener listener, long delayMillis, boolean end, boolean reOpen) {
        this(file, listener, delayMillis, end, reOpen, 8192);
    }

    @Deprecated
    public Tailer(File file, TailerListener listener, long delayMillis, boolean end, boolean reOpen, int bufferSize) {
        this(file, DEFAULT_CHARSET, listener, delayMillis, end, reOpen, bufferSize);
    }

    @Deprecated
    public Tailer(File file, TailerListener listener, long delayMillis, boolean end, int bufferSize) {
        this(file, listener, delayMillis, end, false, bufferSize);
    }

    private Tailer(Tailable tailable, Charset charset, TailerListener listener, Duration delayDuration, boolean end, boolean reOpen, int bufferSize) {
        this.run = true;
        this.tailable = (Tailable) Objects.requireNonNull(tailable, "tailable");
        this.listener = (TailerListener) Objects.requireNonNull(listener, "listener");
        this.delayDuration = delayDuration;
        this.tailAtEnd = end;
        this.inbuf = IOUtils.byteArray(bufferSize);
        listener.init(this);
        this.reOpen = reOpen;
        this.charset = charset;
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        this.run = false;
    }

    @Deprecated
    public long getDelay() {
        return this.delayDuration.toMillis();
    }

    public Duration getDelayDuration() {
        return this.delayDuration;
    }

    public File getFile() {
        if (this.tailable instanceof TailablePath) {
            return ((TailablePath) this.tailable).getPath().toFile();
        }
        throw new IllegalStateException("Cannot extract java.io.File from " + this.tailable.getClass().getName());
    }

    protected boolean getRun() {
        return this.run;
    }

    public Tailable getTailable() {
        return this.tailable;
    }

    private long readLines(RandomAccessResourceBridge reader) throws IOException {
        int num;
        ByteArrayOutputStream lineBuf = new ByteArrayOutputStream(64);
        try {
            long pos = reader.getPointer();
            long rePos = pos;
            boolean seenCR = false;
            while (getRun() && (num = reader.read(this.inbuf)) != -1) {
                for (int i = 0; i < num; i++) {
                    byte ch2 = this.inbuf[i];
                    switch (ch2) {
                        case 10:
                            seenCR = false;
                            this.listener.handle(new String(lineBuf.toByteArray(), this.charset));
                            lineBuf.reset();
                            rePos = pos + i + 1;
                            break;
                        case 13:
                            if (seenCR) {
                                lineBuf.write(13);
                            }
                            seenCR = true;
                            break;
                        default:
                            if (seenCR) {
                                seenCR = false;
                                this.listener.handle(new String(lineBuf.toByteArray(), this.charset));
                                lineBuf.reset();
                                rePos = pos + i + 1;
                            }
                            lineBuf.write(ch2);
                            break;
                    }
                }
                pos = reader.getPointer();
            }
            reader.seek(rePos);
            if (this.listener instanceof TailerListenerAdapter) {
                ((TailerListenerAdapter) this.listener).endOfFileReached();
            }
            long j = rePos;
            lineBuf.close();
            return j;
        } catch (Throwable th) {
            try {
                lineBuf.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        RandomAccessResourceBridge reader = null;
        try {
            try {
                try {
                    FileTime last = FileTimes.EPOCH;
                    long position = 0;
                    while (getRun() && reader == null) {
                        try {
                            reader = this.tailable.getRandomAccess("r");
                        } catch (FileNotFoundException e) {
                            this.listener.fileNotFound();
                        }
                        if (reader == null) {
                            ThreadUtils.sleep(this.delayDuration);
                        } else {
                            position = this.tailAtEnd ? this.tailable.size() : 0L;
                            last = this.tailable.lastModifiedFileTime();
                            reader.seek(position);
                        }
                    }
                    while (getRun()) {
                        boolean newer = this.tailable.isNewer(last);
                        long length = this.tailable.size();
                        if (length < position) {
                            this.listener.fileRotated();
                            RandomAccessResourceBridge save = reader;
                            try {
                                try {
                                    reader = this.tailable.getRandomAccess("r");
                                    try {
                                        readLines(save);
                                    } catch (IOException ioe) {
                                        this.listener.handle(ioe);
                                    }
                                    position = 0;
                                    if (save != null) {
                                        save.close();
                                    }
                                } catch (Throwable th) {
                                    if (save != null) {
                                        try {
                                            save.close();
                                        } catch (Throwable th2) {
                                            th.addSuppressed(th2);
                                        }
                                    }
                                    throw th;
                                }
                            } catch (FileNotFoundException e2) {
                                this.listener.fileNotFound();
                                ThreadUtils.sleep(this.delayDuration);
                            }
                        } else {
                            if (length > position) {
                                position = readLines(reader);
                                last = this.tailable.lastModifiedFileTime();
                            } else if (newer) {
                                reader.seek(0L);
                                position = readLines(reader);
                                last = this.tailable.lastModifiedFileTime();
                            }
                            if (this.reOpen && reader != null) {
                                reader.close();
                            }
                            ThreadUtils.sleep(this.delayDuration);
                            if (getRun() && this.reOpen) {
                                reader = this.tailable.getRandomAccess("r");
                                reader.seek(position);
                            }
                        }
                    }
                    try {
                        IOUtils.close(reader);
                    } catch (IOException e3) {
                        this.listener.handle(e3);
                    }
                    close();
                } catch (InterruptedException e4) {
                    Thread.currentThread().interrupt();
                    this.listener.handle(e4);
                    try {
                        IOUtils.close(reader);
                    } catch (IOException e5) {
                        this.listener.handle(e5);
                    }
                    close();
                }
            } catch (Exception e6) {
                this.listener.handle(e6);
                try {
                    IOUtils.close(reader);
                } catch (IOException e7) {
                    this.listener.handle(e7);
                }
                close();
            }
        } catch (Throwable th3) {
            try {
                IOUtils.close(reader);
            } catch (IOException e8) {
                this.listener.handle(e8);
            }
            close();
            throw th3;
        }
    }

    @Deprecated
    public void stop() {
        close();
    }
}

package org.apache.commons.compress.archivers.zip;

import java.io.File;
import java.io.IOException;
import java.util.Deque;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.compress.parallel.FileBasedScatterGatherBackingStore;
import org.apache.commons.compress.parallel.InputStreamSupplier;
import org.apache.commons.compress.parallel.ScatterGatherBackingStore;
import org.apache.commons.compress.parallel.ScatterGatherBackingStoreSupplier;

/* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/archivers/zip/ParallelScatterZipCreator.class */
public class ParallelScatterZipCreator {
    private final Deque<ScatterZipOutputStream> streams;
    private final ExecutorService es;
    private final ScatterGatherBackingStoreSupplier backingStoreSupplier;
    private final Deque<Future<? extends ScatterZipOutputStream>> futures;
    private final long startedAt;
    private long compressionDoneAt;
    private long scatterDoneAt;
    private final ThreadLocal<ScatterZipOutputStream> tlScatterStreams;

    /* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/archivers/zip/ParallelScatterZipCreator$DefaultBackingStoreSupplier.class */
    private static class DefaultBackingStoreSupplier implements ScatterGatherBackingStoreSupplier {
        final AtomicInteger storeNum;

        private DefaultBackingStoreSupplier() {
            this.storeNum = new AtomicInteger(0);
        }

        @Override // org.apache.commons.compress.parallel.ScatterGatherBackingStoreSupplier
        public ScatterGatherBackingStore get() throws IOException {
            File tempFile = File.createTempFile("parallelscatter", "n" + this.storeNum.incrementAndGet());
            return new FileBasedScatterGatherBackingStore(tempFile);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ScatterZipOutputStream createDeferred(ScatterGatherBackingStoreSupplier scatterGatherBackingStoreSupplier) throws IOException {
        ScatterGatherBackingStore bs = scatterGatherBackingStoreSupplier.get();
        StreamCompressor sc = StreamCompressor.create(-1, bs);
        return new ScatterZipOutputStream(bs, sc);
    }

    public ParallelScatterZipCreator() {
        this(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));
    }

    public ParallelScatterZipCreator(ExecutorService executorService) {
        this(executorService, new DefaultBackingStoreSupplier());
    }

    public ParallelScatterZipCreator(ExecutorService executorService, ScatterGatherBackingStoreSupplier backingStoreSupplier) {
        this.streams = new ConcurrentLinkedDeque();
        this.futures = new ConcurrentLinkedDeque();
        this.startedAt = System.currentTimeMillis();
        this.compressionDoneAt = 0L;
        this.tlScatterStreams = new ThreadLocal<ScatterZipOutputStream>() { // from class: org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator.1
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.lang.ThreadLocal
            public ScatterZipOutputStream initialValue() {
                try {
                    ScatterZipOutputStream scatterStream = ParallelScatterZipCreator.this.createDeferred(ParallelScatterZipCreator.this.backingStoreSupplier);
                    ParallelScatterZipCreator.this.streams.add(scatterStream);
                    return scatterStream;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        this.backingStoreSupplier = backingStoreSupplier;
        this.es = executorService;
    }

    public void addArchiveEntry(ZipArchiveEntry zipArchiveEntry, InputStreamSupplier source) {
        submitStreamAwareCallable(createCallable(zipArchiveEntry, source));
    }

    public void addArchiveEntry(ZipArchiveEntryRequestSupplier zipArchiveEntryRequestSupplier) {
        submitStreamAwareCallable(createCallable(zipArchiveEntryRequestSupplier));
    }

    public final void submit(final Callable<? extends Object> callable) {
        submitStreamAwareCallable(new Callable<ScatterZipOutputStream>() { // from class: org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public ScatterZipOutputStream call() throws Exception {
                callable.call();
                return (ScatterZipOutputStream) ParallelScatterZipCreator.this.tlScatterStreams.get();
            }
        });
    }

    public final void submitStreamAwareCallable(Callable<? extends ScatterZipOutputStream> callable) {
        this.futures.add(this.es.submit(callable));
    }

    public final Callable<ScatterZipOutputStream> createCallable(ZipArchiveEntry zipArchiveEntry, InputStreamSupplier source) {
        int method = zipArchiveEntry.getMethod();
        if (method == -1) {
            throw new IllegalArgumentException("Method must be set on zipArchiveEntry: " + zipArchiveEntry);
        }
        final ZipArchiveEntryRequest zipArchiveEntryRequest = ZipArchiveEntryRequest.createZipArchiveEntryRequest(zipArchiveEntry, source);
        return new Callable<ScatterZipOutputStream>() { // from class: org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public ScatterZipOutputStream call() throws Exception {
                ScatterZipOutputStream scatterStream = (ScatterZipOutputStream) ParallelScatterZipCreator.this.tlScatterStreams.get();
                scatterStream.addArchiveEntry(zipArchiveEntryRequest);
                return scatterStream;
            }
        };
    }

    public final Callable<ScatterZipOutputStream> createCallable(final ZipArchiveEntryRequestSupplier zipArchiveEntryRequestSupplier) {
        return new Callable<ScatterZipOutputStream>() { // from class: org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public ScatterZipOutputStream call() throws Exception {
                ScatterZipOutputStream scatterStream = (ScatterZipOutputStream) ParallelScatterZipCreator.this.tlScatterStreams.get();
                scatterStream.addArchiveEntry(zipArchiveEntryRequestSupplier.get());
                return scatterStream;
            }
        };
    }

    /* JADX WARN: Finally extract failed */
    public void writeTo(ZipArchiveOutputStream targetStream) throws ExecutionException, InterruptedException, IOException {
        try {
            try {
                for (Future<?> future : this.futures) {
                    future.get();
                }
                this.es.shutdown();
                this.es.awaitTermination(60000L, TimeUnit.SECONDS);
                this.compressionDoneAt = System.currentTimeMillis();
                for (Future<? extends ScatterZipOutputStream> future2 : this.futures) {
                    ScatterZipOutputStream scatterStream = future2.get();
                    scatterStream.zipEntryWriter().writeNextZipEntry(targetStream);
                }
                for (ScatterZipOutputStream scatterStream2 : this.streams) {
                    scatterStream2.close();
                }
                this.scatterDoneAt = System.currentTimeMillis();
                closeAll();
            } catch (Throwable th) {
                this.es.shutdown();
                throw th;
            }
        } catch (Throwable th2) {
            closeAll();
            throw th2;
        }
    }

    public ScatterStatistics getStatisticsMessage() {
        return new ScatterStatistics(this.compressionDoneAt - this.startedAt, this.scatterDoneAt - this.compressionDoneAt);
    }

    private void closeAll() {
        for (ScatterZipOutputStream scatterStream : this.streams) {
            try {
                scatterStream.close();
            } catch (IOException e) {
            }
        }
    }
}

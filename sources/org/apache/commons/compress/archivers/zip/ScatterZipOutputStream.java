package org.apache.commons.compress.archivers.zip;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.commons.compress.parallel.FileBasedScatterGatherBackingStore;
import org.apache.commons.compress.parallel.ScatterGatherBackingStore;
import org.apache.commons.compress.utils.BoundedInputStream;

/* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/archivers/zip/ScatterZipOutputStream.class */
public class ScatterZipOutputStream implements Closeable {
    private final ScatterGatherBackingStore backingStore;
    private final StreamCompressor streamCompressor;
    private final Queue<CompressedEntry> items = new ConcurrentLinkedQueue();
    private AtomicBoolean isClosed = new AtomicBoolean();
    private ZipEntryWriter zipEntryWriter = null;

    /* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/archivers/zip/ScatterZipOutputStream$CompressedEntry.class */
    private static class CompressedEntry {
        final ZipArchiveEntryRequest zipArchiveEntryRequest;
        final long crc;
        final long compressedSize;
        final long size;

        public CompressedEntry(ZipArchiveEntryRequest zipArchiveEntryRequest, long crc, long compressedSize, long size) {
            this.zipArchiveEntryRequest = zipArchiveEntryRequest;
            this.crc = crc;
            this.compressedSize = compressedSize;
            this.size = size;
        }

        public ZipArchiveEntry transferToArchiveEntry() {
            ZipArchiveEntry entry = this.zipArchiveEntryRequest.getZipArchiveEntry();
            entry.setCompressedSize(this.compressedSize);
            entry.setSize(this.size);
            entry.setCrc(this.crc);
            entry.setMethod(this.zipArchiveEntryRequest.getMethod());
            return entry;
        }
    }

    public ScatterZipOutputStream(ScatterGatherBackingStore backingStore, StreamCompressor streamCompressor) {
        this.backingStore = backingStore;
        this.streamCompressor = streamCompressor;
    }

    public void addArchiveEntry(ZipArchiveEntryRequest zipArchiveEntryRequest) throws IOException {
        InputStream payloadStream = zipArchiveEntryRequest.getPayloadStream();
        Throwable th = null;
        try {
            try {
                this.streamCompressor.deflate(payloadStream, zipArchiveEntryRequest.getMethod());
                if (payloadStream != null) {
                    if (0 != 0) {
                        try {
                            payloadStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    } else {
                        payloadStream.close();
                    }
                }
                this.items.add(new CompressedEntry(zipArchiveEntryRequest, this.streamCompressor.getCrc32(), this.streamCompressor.getBytesWrittenForLastEntry(), this.streamCompressor.getBytesRead()));
            } finally {
            }
        } catch (Throwable th3) {
            if (payloadStream != null) {
                if (th != null) {
                    try {
                        payloadStream.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    payloadStream.close();
                }
            }
            throw th3;
        }
    }

    public void writeTo(ZipArchiveOutputStream target) throws IOException {
        this.backingStore.closeForWriting();
        InputStream data = this.backingStore.getInputStream();
        Throwable th = null;
        try {
            try {
                for (CompressedEntry compressedEntry : this.items) {
                    BoundedInputStream rawStream = new BoundedInputStream(data, compressedEntry.compressedSize);
                    Throwable th2 = null;
                    try {
                        try {
                            target.addRawArchiveEntry(compressedEntry.transferToArchiveEntry(), rawStream);
                            if (rawStream != null) {
                                if (0 != 0) {
                                    try {
                                        rawStream.close();
                                    } catch (Throwable th3) {
                                        th2.addSuppressed(th3);
                                    }
                                } else {
                                    rawStream.close();
                                }
                            }
                        } catch (Throwable th4) {
                            th2 = th4;
                            throw th4;
                        }
                    } catch (Throwable th5) {
                        if (rawStream != null) {
                            if (th2 != null) {
                                try {
                                    rawStream.close();
                                } catch (Throwable th6) {
                                    th2.addSuppressed(th6);
                                }
                            } else {
                                rawStream.close();
                            }
                        }
                        throw th5;
                    }
                }
                if (data != null) {
                    if (0 != 0) {
                        try {
                            data.close();
                            return;
                        } catch (Throwable th7) {
                            th.addSuppressed(th7);
                            return;
                        }
                    }
                    data.close();
                }
            } catch (Throwable th8) {
                th = th8;
                throw th8;
            }
        } catch (Throwable th9) {
            if (data != null) {
                if (th != null) {
                    try {
                        data.close();
                    } catch (Throwable th10) {
                        th.addSuppressed(th10);
                    }
                } else {
                    data.close();
                }
            }
            throw th9;
        }
    }

    /* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/archivers/zip/ScatterZipOutputStream$ZipEntryWriter.class */
    public static class ZipEntryWriter implements Closeable {
        private final Iterator<CompressedEntry> itemsIterator;
        private final InputStream itemsIteratorData;

        public ZipEntryWriter(ScatterZipOutputStream scatter) throws IOException {
            scatter.backingStore.closeForWriting();
            this.itemsIterator = scatter.items.iterator();
            this.itemsIteratorData = scatter.backingStore.getInputStream();
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            if (this.itemsIteratorData != null) {
                this.itemsIteratorData.close();
            }
        }

        public void writeNextZipEntry(ZipArchiveOutputStream target) throws IOException {
            CompressedEntry compressedEntry = this.itemsIterator.next();
            BoundedInputStream rawStream = new BoundedInputStream(this.itemsIteratorData, compressedEntry.compressedSize);
            Throwable th = null;
            try {
                try {
                    target.addRawArchiveEntry(compressedEntry.transferToArchiveEntry(), rawStream);
                    if (rawStream != null) {
                        if (0 != 0) {
                            try {
                                rawStream.close();
                                return;
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                                return;
                            }
                        }
                        rawStream.close();
                    }
                } catch (Throwable th3) {
                    th = th3;
                    throw th3;
                }
            } catch (Throwable th4) {
                if (rawStream != null) {
                    if (th != null) {
                        try {
                            rawStream.close();
                        } catch (Throwable th5) {
                            th.addSuppressed(th5);
                        }
                    } else {
                        rawStream.close();
                    }
                }
                throw th4;
            }
        }
    }

    public ZipEntryWriter zipEntryWriter() throws IOException {
        if (this.zipEntryWriter == null) {
            this.zipEntryWriter = new ZipEntryWriter(this);
        }
        return this.zipEntryWriter;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (!this.isClosed.compareAndSet(false, true)) {
            return;
        }
        try {
            if (this.zipEntryWriter != null) {
                this.zipEntryWriter.close();
            }
            this.backingStore.close();
        } finally {
            this.streamCompressor.close();
        }
    }

    public static ScatterZipOutputStream fileBased(File file) throws FileNotFoundException {
        return fileBased(file, -1);
    }

    public static ScatterZipOutputStream fileBased(File file, int compressionLevel) throws FileNotFoundException {
        ScatterGatherBackingStore bs = new FileBasedScatterGatherBackingStore(file);
        StreamCompressor sc = StreamCompressor.create(compressionLevel, bs);
        return new ScatterZipOutputStream(bs, sc);
    }
}

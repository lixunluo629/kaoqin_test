package org.terracotta.offheapstore.disk.paging;

import com.alibaba.excel.constant.ExcelXmlConstants;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.offheapstore.paging.OffHeapStorageArea;
import org.terracotta.offheapstore.paging.Page;
import org.terracotta.offheapstore.paging.PageSource;
import org.terracotta.offheapstore.util.DebuggingUtils;
import org.terracotta.offheapstore.util.Retryer;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/disk/paging/MappedPageSource.class */
public class MappedPageSource implements PageSource {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) MappedPageSource.class);
    private static final Retryer ASYNC_FLUSH_EXECUTOR = new Retryer(10, 600, TimeUnit.SECONDS, new ThreadFactory() { // from class: org.terracotta.offheapstore.disk.paging.MappedPageSource.1
        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "MappedByteBufferSource Async Flush Thread");
            t.setDaemon(true);
            return t;
        }
    });
    private final File file;
    private final RandomAccessFile raf;
    private final FileChannel channel;
    private final PowerOfTwoFileAllocator allocator;
    private final IdentityHashMap<MappedPage, Long> pages;
    private final Map<Long, AllocatedRegion> allocated;

    public MappedPageSource(File file) throws IOException {
        this(file, true);
    }

    public MappedPageSource(File file, long size) throws IOException {
        this(file, true, size);
    }

    public MappedPageSource(File file, boolean truncate) throws IOException {
        this(file, truncate, Long.MAX_VALUE);
    }

    public MappedPageSource(File file, boolean truncate, long size) throws IOException {
        this.pages = new IdentityHashMap<>();
        this.allocated = new HashMap();
        if (!file.createNewFile() && file.isDirectory()) {
            throw new IOException("File already exists and is a directory");
        }
        this.file = file;
        this.raf = new RandomAccessFile(file, "rw");
        this.channel = this.raf.getChannel();
        if (truncate) {
            try {
                this.channel.truncate(0L);
            } catch (IOException e) {
                LOGGER.info("Exception prevented truncation of disk store file", (Throwable) e);
            }
        } else if (this.channel.size() > size) {
            throw new IllegalStateException("Existing file is larger than source limit");
        }
        this.allocator = new PowerOfTwoFileAllocator(size);
    }

    public synchronized Long allocateRegion(long size) throws IOException {
        Long address = this.allocator.allocate(size);
        if (address == null) {
            return null;
        }
        this.allocated.put(address, new AllocatedRegion(address.longValue(), size));
        long max = address.longValue() + size;
        try {
            if (max > this.channel.size()) {
                ByteBuffer one = ByteBuffer.allocate(1);
                while (one.hasRemaining()) {
                    this.channel.write(one, max - 1);
                }
            }
        } catch (IOException e) {
            LOGGER.warn("IOException while attempting to extend file " + this.file.getAbsolutePath(), (Throwable) e);
        }
        return address;
    }

    public synchronized void freeRegion(long address) {
        AllocatedRegion r = this.allocated.remove(Long.valueOf(address));
        if (r == null) {
            throw new AssertionError();
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Freeing a {}B region from {} &{}", DebuggingUtils.toBase2SuffixedString(r.size), this.file.getName(), Long.valueOf(r.address));
        }
        this.allocator.free(r.address, r.size);
    }

    public synchronized long claimRegion(long address, long size) throws IOException {
        this.allocator.mark(address, size);
        this.allocated.put(Long.valueOf(address), new AllocatedRegion(address, size));
        return address;
    }

    public FileChannel getReadableChannel() {
        try {
            return new RandomAccessFile(this.file, ExcelXmlConstants.POSITION).getChannel();
        } catch (FileNotFoundException e) {
            throw new AssertionError(e);
        }
    }

    public FileChannel getWritableChannel() {
        try {
            return new RandomAccessFile(this.file, "rw").getChannel();
        } catch (FileNotFoundException e) {
            throw new AssertionError(e);
        }
    }

    public File getFile() {
        return this.file;
    }

    @Override // org.terracotta.offheapstore.paging.PageSource
    public synchronized MappedPage allocate(int size, boolean thief, boolean victim, OffHeapStorageArea owner) throws IOException {
        Long address = allocateRegion(size);
        if (address == null) {
            return null;
        }
        try {
            MappedByteBuffer buffer = this.channel.map(FileChannel.MapMode.READ_WRITE, address.longValue(), size);
            MappedPage page = new MappedPage(buffer);
            this.pages.put(page, address);
            return page;
        } catch (IOException e) {
            freeRegion(address.longValue());
            LOGGER.warn("Mapping a new file section failed", (Throwable) e);
            return null;
        }
    }

    @Override // org.terracotta.offheapstore.paging.PageSource
    public synchronized void free(final Page page) {
        final Long a = this.pages.remove(page);
        if (a == null) {
            throw new AssertionError();
        }
        ASYNC_FLUSH_EXECUTOR.completeAsynchronously(new Runnable() { // from class: org.terracotta.offheapstore.disk.paging.MappedPageSource.2
            @Override // java.lang.Runnable
            public void run() {
                ((MappedByteBuffer) page.asByteBuffer()).force();
                MappedPageSource.this.freeRegion(a.longValue());
            }

            public String toString() {
                return "Asynchronous flush of Page[" + System.identityHashCode(page) + "] (size=" + page.size() + ")";
            }
        });
    }

    public synchronized MappedPage claimPage(long address, long size) throws IOException {
        claimRegion(address, size);
        MappedByteBuffer buffer = this.channel.map(FileChannel.MapMode.READ_WRITE, address, size);
        MappedPage page = new MappedPage(buffer);
        this.pages.put(page, Long.valueOf(address));
        return page;
    }

    public long getAddress(Page underlying) {
        return this.pages.get(underlying).longValue();
    }

    public synchronized void flush() throws IOException {
        if (this.channel.isOpen()) {
            this.channel.force(true);
        }
    }

    public synchronized void close() throws IOException {
        try {
            this.channel.close();
            this.raf.close();
        } catch (Throwable th) {
            this.raf.close();
            throw th;
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/disk/paging/MappedPageSource$AllocatedRegion.class */
    static class AllocatedRegion {
        final long address;
        final long size;

        public AllocatedRegion(long address, long size) {
            this.address = address;
            this.size = size;
        }
    }
}

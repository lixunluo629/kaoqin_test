package org.terracotta.offheapstore.disk.storage;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.FileChannel;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.offheapstore.disk.paging.MappedPageSource;
import org.terracotta.offheapstore.disk.persistent.Persistent;
import org.terracotta.offheapstore.disk.persistent.PersistentStorageEngine;
import org.terracotta.offheapstore.storage.PortabilityBasedStorageEngine;
import org.terracotta.offheapstore.storage.StorageEngine;
import org.terracotta.offheapstore.storage.portability.Portability;
import org.terracotta.offheapstore.storage.portability.WriteContext;
import org.terracotta.offheapstore.util.Factory;
import org.terracotta.offheapstore.util.MemoryUnit;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/disk/storage/FileBackedStorageEngine.class */
public class FileBackedStorageEngine<K, V> extends PortabilityBasedStorageEngine<K, V> implements PersistentStorageEngine<K, V> {
    private static final int MAGIC = 1095582789;
    private static final int MAGIC_CHUNK = 1313753427;
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) FileBackedStorageEngine.class);
    private static final int KEY_HASH_OFFSET = 0;
    private static final int KEY_LENGTH_OFFSET = 4;
    private static final int VALUE_LENGTH_OFFSET = 8;
    private static final int KEY_DATA_OFFSET = 12;
    private final ConcurrentHashMap<Long, FileBackedStorageEngine<K, V>.FileWriteTask> pendingWrites;
    private final ExecutorService writeExecutor;
    private final MappedPageSource source;
    private final FileChannel writeChannel;
    private final AtomicReference<FileChannel> readChannelReference;
    private final TreeMap<Long, FileBackedStorageEngine<K, V>.FileChunk> chunks;
    private final long maxChunkSize;
    private volatile StorageEngine.Owner owner;

    public static <K, V> Factory<FileBackedStorageEngine<K, V>> createFactory(MappedPageSource source, long maxChunkSize, MemoryUnit maxChunkUnit, Portability<? super K> keyPortability, Portability<? super V> valuePortability) {
        return createFactory(source, maxChunkSize, maxChunkUnit, keyPortability, valuePortability, true);
    }

    public static <K, V> Factory<FileBackedStorageEngine<K, V>> createFactory(MappedPageSource source, long maxChunkSize, MemoryUnit maxChunkUnit, Portability<? super K> keyPortability, Portability<? super V> valuePortability, boolean bootstrap) {
        Factory<ExecutorService> executorFactory = new Factory<ExecutorService>() { // from class: org.terracotta.offheapstore.disk.storage.FileBackedStorageEngine.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.terracotta.offheapstore.util.Factory
            public ExecutorService newInstance() {
                return new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());
            }
        };
        return createFactory(source, maxChunkSize, maxChunkUnit, keyPortability, valuePortability, executorFactory, bootstrap);
    }

    public static <K, V> Factory<FileBackedStorageEngine<K, V>> createFactory(final MappedPageSource source, final long maxChunkSize, final MemoryUnit maxChunkUnit, final Portability<? super K> keyPortability, final Portability<? super V> valuePortability, final Factory<ExecutorService> executorFactory, final boolean bootstrap) {
        return new Factory<FileBackedStorageEngine<K, V>>() { // from class: org.terracotta.offheapstore.disk.storage.FileBackedStorageEngine.2
            @Override // org.terracotta.offheapstore.util.Factory
            public FileBackedStorageEngine<K, V> newInstance() {
                return new FileBackedStorageEngine<>(source, maxChunkSize, maxChunkUnit, keyPortability, valuePortability, (ExecutorService) executorFactory.newInstance(), bootstrap);
            }
        };
    }

    public FileBackedStorageEngine(MappedPageSource source, long maxChunkSize, MemoryUnit maxChunkUnit, Portability<? super K> keyPortability, Portability<? super V> valuePortability) {
        this(source, maxChunkSize, maxChunkUnit, (Portability) keyPortability, (Portability) valuePortability, true);
    }

    public FileBackedStorageEngine(MappedPageSource source, long maxChunkSize, MemoryUnit maxChunkUnit, Portability<? super K> keyPortability, Portability<? super V> valuePortability, ExecutorService writer) {
        this(source, maxChunkSize, maxChunkUnit, keyPortability, valuePortability, writer, true);
    }

    public FileBackedStorageEngine(MappedPageSource source, long maxChunkSize, MemoryUnit maxChunkUnit, Portability<? super K> keyPortability, Portability<? super V> valuePortability, boolean bootstrap) {
        this(source, maxChunkSize, maxChunkUnit, keyPortability, valuePortability, new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue()), bootstrap);
    }

    public FileBackedStorageEngine(MappedPageSource source, long maxChunkSize, MemoryUnit maxChunkUnit, Portability<? super K> keyPortability, Portability<? super V> valuePortability, ExecutorService writer, boolean bootstrap) {
        super(keyPortability, valuePortability);
        this.pendingWrites = new ConcurrentHashMap<>();
        this.chunks = new TreeMap<>();
        this.writeExecutor = writer;
        this.writeChannel = source.getWritableChannel();
        this.readChannelReference = new AtomicReference<>(source.getReadableChannel());
        this.source = source;
        this.maxChunkSize = Long.highestOneBit(maxChunkUnit.toBytes(maxChunkSize));
    }

    @Override // org.terracotta.offheapstore.storage.PortabilityBasedStorageEngine
    protected void clearInternal() {
        Iterator<Map.Entry<Long, FileBackedStorageEngine<K, V>.FileChunk>> it = this.chunks.entrySet().iterator();
        while (it.hasNext()) {
            it.next().getValue().clear();
            it.remove();
        }
        if (!this.chunks.isEmpty()) {
            throw new AssertionError("Concurrent modification while clearing!");
        }
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public void destroy() {
        try {
            close();
        } catch (IOException e) {
            LOGGER.warn("Exception while trying to close file backed storage engine", (Throwable) e);
        }
    }

    @Override // org.terracotta.offheapstore.disk.persistent.Persistent
    public void flush() throws IOException {
        Future<Void> flush = this.writeExecutor.submit(new Callable<Void>() { // from class: org.terracotta.offheapstore.disk.storage.FileBackedStorageEngine.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Void call() throws IOException {
                FileBackedStorageEngine.this.writeChannel.force(true);
                return null;
            }
        });
        boolean interrupted = false;
        while (true) {
            try {
                try {
                    flush.get();
                    break;
                } catch (InterruptedException e) {
                    interrupted = true;
                } catch (ExecutionException ex) {
                    Throwable cause = ex.getCause();
                    if (cause instanceof RuntimeException) {
                        throw ((RuntimeException) cause);
                    }
                    if (cause instanceof IOException) {
                        throw ((IOException) cause);
                    }
                    throw new RuntimeException(cause);
                }
            } finally {
                if (interrupted) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    @Override // org.terracotta.offheapstore.disk.persistent.Persistent
    public void close() throws IOException {
        try {
            try {
                this.writeExecutor.shutdownNow();
                if (this.writeExecutor.awaitTermination(60L, TimeUnit.SECONDS)) {
                    LOGGER.debug("FileBackedStorageEngine for " + this.source.getFile().getName() + " terminated successfully");
                } else {
                    LOGGER.warn("FileBackedStorageEngine for " + this.source.getFile().getName() + " timed-out during termination");
                }
                try {
                    this.writeChannel.close();
                    this.readChannelReference.getAndSet(null).close();
                } finally {
                }
            } catch (InterruptedException e) {
                LOGGER.warn("FileBackedStorageEngine for " + this.source.getFile().getName() + " interrupted during termination");
                Thread.currentThread().interrupt();
                try {
                    this.writeChannel.close();
                    this.readChannelReference.getAndSet(null).close();
                } finally {
                    this.readChannelReference.getAndSet(null).close();
                }
            }
        } catch (Throwable th) {
            try {
                this.writeChannel.close();
                this.readChannelReference.getAndSet(null).close();
                throw th;
            } finally {
            }
        }
    }

    @Override // org.terracotta.offheapstore.disk.persistent.Persistent
    public void persist(ObjectOutput output) throws IOException {
        output.writeInt(MAGIC);
        ((Persistent) this.keyPortability).persist(output);
        ((Persistent) this.valuePortability).persist(output);
        output.writeInt(this.chunks.size());
        for (FileBackedStorageEngine<K, V>.FileChunk c : this.chunks.values()) {
            c.persist(output);
        }
    }

    @Override // org.terracotta.offheapstore.disk.persistent.Persistent
    public void bootstrap(ObjectInput input) throws IOException {
        if (!this.chunks.isEmpty()) {
            throw new IllegalStateException();
        }
        if (input.readInt() != MAGIC) {
            throw new IOException("Wrong magic number");
        }
        ((Persistent) this.keyPortability).bootstrap(input);
        ((Persistent) this.valuePortability).bootstrap(input);
        int n = input.readInt();
        for (int i = 0; i < n; i++) {
            FileBackedStorageEngine<K, V>.FileChunk chunk = new FileChunk(input);
            this.chunks.put(Long.valueOf(chunk.baseAddress()), chunk);
        }
        if (hasRecoveryListeners()) {
            for (Long encoding : this.owner.encodingSet()) {
                ByteBuffer binaryKey = readBinaryKey(encoding.longValue());
                ByteBuffer binaryValue = readBinaryValue(encoding.longValue());
                int hash = readKeyHash(encoding.longValue());
                final ByteBuffer binaryKeyForDecode = binaryKey.duplicate();
                final ByteBuffer binaryValueForDecode = binaryValue.duplicate();
                final Thread caller = Thread.currentThread();
                fireRecovered(new Callable<K>() { // from class: org.terracotta.offheapstore.disk.storage.FileBackedStorageEngine.4
                    @Override // java.util.concurrent.Callable
                    public K call() throws Exception {
                        if (caller == Thread.currentThread()) {
                            return (K) FileBackedStorageEngine.this.keyPortability.decode(binaryKeyForDecode.duplicate());
                        }
                        throw new IllegalStateException();
                    }
                }, new Callable<V>() { // from class: org.terracotta.offheapstore.disk.storage.FileBackedStorageEngine.5
                    @Override // java.util.concurrent.Callable
                    public V call() throws Exception {
                        if (caller == Thread.currentThread()) {
                            return (V) FileBackedStorageEngine.this.valuePortability.decode(binaryValueForDecode.duplicate());
                        }
                        throw new IllegalStateException();
                    }
                }, binaryKey, binaryValue, hash, 0, encoding.longValue());
            }
        }
    }

    @Override // org.terracotta.offheapstore.storage.PortabilityBasedStorageEngine
    protected void free(long address) {
        FileBackedStorageEngine<K, V>.FileChunk chunk = findChunk(address);
        chunk.free(address - chunk.baseAddress());
    }

    @Override // org.terracotta.offheapstore.storage.PortabilityBasedStorageEngine
    protected ByteBuffer readKeyBuffer(long address) {
        FileBackedStorageEngine<K, V>.FileChunk chunk = findChunk(address);
        return chunk.readKeyBuffer(address - chunk.baseAddress());
    }

    @Override // org.terracotta.offheapstore.storage.PortabilityBasedStorageEngine
    protected WriteContext getKeyWriteContext(long address) {
        FileBackedStorageEngine<K, V>.FileChunk chunk = findChunk(address);
        return chunk.getKeyWriteContext(address - chunk.baseAddress());
    }

    @Override // org.terracotta.offheapstore.storage.PortabilityBasedStorageEngine
    protected ByteBuffer readValueBuffer(long address) {
        FileBackedStorageEngine<K, V>.FileChunk chunk = findChunk(address);
        return chunk.readValueBuffer(address - chunk.baseAddress());
    }

    @Override // org.terracotta.offheapstore.storage.PortabilityBasedStorageEngine
    protected WriteContext getValueWriteContext(long address) {
        FileBackedStorageEngine<K, V>.FileChunk chunk = findChunk(address);
        return chunk.getValueWriteContext(address - chunk.baseAddress());
    }

    @Override // org.terracotta.offheapstore.storage.PortabilityBasedStorageEngine
    protected Long writeMappingBuffers(ByteBuffer keyBuffer, ByteBuffer valueBuffer, int hash) {
        long nextChunkSize;
        long nextChunkBaseAddress;
        FileBackedStorageEngine<K, V>.FileChunk c;
        Long address;
        for (FileBackedStorageEngine<K, V>.FileChunk c2 : this.chunks.values()) {
            Long address2 = c2.writeMappingBuffers(keyBuffer, valueBuffer, hash);
            if (address2 != null) {
                return Long.valueOf(address2.longValue() + c2.baseAddress());
            }
        }
        do {
            long requiredSize = keyBuffer.remaining() + valueBuffer.remaining() + 12;
            if (Long.bitCount(requiredSize) != 1) {
                requiredSize = Long.highestOneBit(requiredSize) << 1;
            }
            if (this.chunks.isEmpty()) {
                nextChunkSize = requiredSize;
                nextChunkBaseAddress = 0;
            } else {
                FileBackedStorageEngine<K, V>.FileChunk last = this.chunks.lastEntry().getValue();
                nextChunkSize = Math.max(Math.min(last.capacity() << 1, this.maxChunkSize), requiredSize);
                nextChunkBaseAddress = last.baseAddress() + last.capacity();
                if (nextChunkSize < 0) {
                    return null;
                }
            }
            try {
                c = new FileChunk(nextChunkSize, nextChunkBaseAddress);
                this.chunks.put(Long.valueOf(c.baseAddress()), c);
                address = c.writeMappingBuffers(keyBuffer, valueBuffer, hash);
            } catch (OutOfMemoryError e) {
                return null;
            }
        } while (address == null);
        return Long.valueOf(address.longValue() + c.baseAddress());
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public long getAllocatedMemory() {
        long sum = 0;
        for (FileBackedStorageEngine<K, V>.FileChunk c : this.chunks.values()) {
            sum += c.capacity();
        }
        return sum;
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public long getOccupiedMemory() {
        long sum = 0;
        for (FileBackedStorageEngine<K, V>.FileChunk c : this.chunks.values()) {
            sum += c.occupied();
        }
        return sum;
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public long getVitalMemory() {
        return getAllocatedMemory();
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public long getDataSize() {
        long sum = 0;
        for (FileBackedStorageEngine<K, V>.FileChunk c : this.chunks.values()) {
            sum += c.occupied();
        }
        return sum;
    }

    private FileBackedStorageEngine<K, V>.FileChunk findChunk(long address) {
        return this.chunks.floorEntry(Long.valueOf(address)).getValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int readIntFromChannel(long position) throws IOException {
        ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
        int i = 0;
        while (true) {
            int i2 = i;
            if (lengthBuffer.hasRemaining()) {
                int read = readFromChannel(lengthBuffer, position + i2);
                if (read < 0) {
                    throw new EOFException();
                }
                i = i2 + read;
            } else {
                return ((ByteBuffer) lengthBuffer.flip()).getInt();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeIntToChannel(long position, int data) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(data).flip();
        writeBufferToChannel(position, buffer);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeLongToChannel(long position, long data) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(data).flip();
        writeBufferToChannel(position, buffer);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeBufferToChannel(long position, ByteBuffer buffer) throws IOException {
        int i = 0;
        while (true) {
            int i2 = i;
            if (buffer.hasRemaining()) {
                int written = this.writeChannel.write(buffer, position + i2);
                if (written < 0) {
                    throw new EOFException();
                }
                i = i2 + written;
            } else {
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int readFromChannel(ByteBuffer buffer, long position) throws IOException {
        int fromChannel;
        FileChannel current = this.readChannelReference.get();
        if (current == null) {
            throw new IOException("Storage engine is closed");
        }
        try {
            return readFromChannel(current, buffer, position);
        } catch (ClosedChannelException e) {
            boolean interrupted = Thread.interrupted();
            while (true) {
                try {
                    FileChannel current2 = this.readChannelReference.get();
                    try {
                        fromChannel = readFromChannel(current2, buffer, position);
                        break;
                    } catch (ClosedChannelException e2) {
                        interrupted |= Thread.interrupted();
                        FileChannel newChannel = this.source.getReadableChannel();
                        if (!this.readChannelReference.compareAndSet(current2, newChannel)) {
                            newChannel.close();
                        } else {
                            LOGGER.info("Creating new read-channel for " + this.source.getFile().getName() + " as previous one was closed (likely due to interrupt)");
                        }
                    }
                } catch (Throwable th) {
                    if (interrupted) {
                        Thread.currentThread().interrupt();
                    }
                    throw th;
                }
            }
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
            return fromChannel;
        }
    }

    private int readFromChannel(FileChannel channel, ByteBuffer buffer, long position) throws IOException {
        int ret = channel.read(buffer, position);
        if (ret < 0) {
            ret = channel.read(buffer, position);
        }
        return ret;
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public boolean shrink() {
        Lock ownerLock = this.owner.writeLock();
        ownerLock.lock();
        try {
            if (this.chunks.isEmpty()) {
                return false;
            }
            FileBackedStorageEngine<K, V>.FileChunk candidate = this.chunks.lastEntry().getValue();
            for (FileBackedStorageEngine<K, V>.FileChunk c : this.chunks.descendingMap().values()) {
                c.evictAll();
                compress(c);
                compress(candidate);
                if (candidate.occupied() == 0) {
                    this.chunks.remove(Long.valueOf(candidate.baseAddress())).clear();
                    ownerLock.unlock();
                    return true;
                }
            }
            ownerLock.unlock();
            return false;
        } finally {
            ownerLock.unlock();
        }
    }

    private void compress(FileBackedStorageEngine<K, V>.FileChunk from) {
        for (Long encoding : from.encodings()) {
            ByteBuffer keyBuffer = readKeyBuffer(encoding.longValue());
            int keyHash = readKeyHash(encoding.longValue());
            ByteBuffer valueBuffer = readValueBuffer(encoding.longValue());
            Iterator i$ = this.chunks.headMap(Long.valueOf(from.baseAddress()), true).values().iterator();
            while (true) {
                if (!i$.hasNext()) {
                    break;
                }
                FileBackedStorageEngine<K, V>.FileChunk to = i$.next();
                Long address = to.writeMappingBuffers(keyBuffer, valueBuffer, keyHash);
                if (address != null) {
                    long compressed = address.longValue() + to.baseAddress();
                    if (compressed < encoding.longValue() && this.owner.updateEncoding(keyHash, encoding.longValue(), compressed, -1L)) {
                        free(encoding.longValue());
                    } else {
                        free(compressed);
                    }
                }
            }
        }
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public void bind(StorageEngine.Owner m) {
        if (this.owner != null) {
            throw new AssertionError();
        }
        this.owner = m;
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/disk/storage/FileBackedStorageEngine$FileChunk.class */
    class FileChunk {
        private final AATreeFileAllocator allocator;
        private final long filePosition;
        private final long baseAddress;
        private boolean valid = true;

        FileChunk(long size, long baseAddress) throws IOException {
            Long newOffset = FileBackedStorageEngine.this.source.allocateRegion(size);
            if (newOffset == null) {
                StringBuilder sb = new StringBuilder("Storage engine file data area allocation failed:\n");
                sb.append("Allocator: ").append(FileBackedStorageEngine.this.source);
                throw new OutOfMemoryError(sb.toString());
            }
            this.filePosition = newOffset.longValue();
            this.allocator = new AATreeFileAllocator(size);
            this.baseAddress = baseAddress;
        }

        FileChunk(ObjectInput input) throws IOException {
            if (input.readInt() != FileBackedStorageEngine.MAGIC_CHUNK) {
                throw new IOException("Wrong magic number");
            }
            this.filePosition = input.readLong();
            this.baseAddress = input.readLong();
            long size = input.readLong();
            FileBackedStorageEngine.this.source.claimRegion(this.filePosition, size);
            this.allocator = new AATreeFileAllocator(size, input);
        }

        ByteBuffer readKeyBuffer(long address) {
            try {
                long position = this.filePosition + address;
                FileBackedStorageEngine<K, V>.FileWriteTask pending = (FileWriteTask) FileBackedStorageEngine.this.pendingWrites.get(Long.valueOf(position));
                if (pending == null) {
                    int keyLength = FileBackedStorageEngine.this.readIntFromChannel(position + 4);
                    return readBuffer(position + 12, keyLength);
                }
                return pending.getKeyBuffer();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (OutOfMemoryError e2) {
                FileBackedStorageEngine.LOGGER.error("Failed to allocate direct buffer for FileChannel read.  Consider increasing the -XX:MaxDirectMemorySize property to allow enough space for the FileChannel transfer buffers");
                throw e2;
            }
        }

        protected int readPojoHash(long address) {
            try {
                long position = this.filePosition + address;
                FileBackedStorageEngine<K, V>.FileWriteTask pending = (FileWriteTask) FileBackedStorageEngine.this.pendingWrites.get(Long.valueOf(position));
                if (pending == null) {
                    return FileBackedStorageEngine.this.readIntFromChannel(position + 0);
                }
                return ((FileWriteTask) pending).pojoHash;
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (OutOfMemoryError e2) {
                FileBackedStorageEngine.LOGGER.error("Failed to allocate direct buffer for FileChannel read.  Consider increasing the -XX:MaxDirectMemorySize property to allow enough space for the FileChannel transfer buffers");
                throw e2;
            }
        }

        ByteBuffer readValueBuffer(long address) {
            try {
                long position = this.filePosition + address;
                FileBackedStorageEngine<K, V>.FileWriteTask pending = (FileWriteTask) FileBackedStorageEngine.this.pendingWrites.get(Long.valueOf(position));
                if (pending == null) {
                    int keyLength = FileBackedStorageEngine.this.readIntFromChannel(position + 4);
                    int valueLength = FileBackedStorageEngine.this.readIntFromChannel(position + 8);
                    return readBuffer(position + keyLength + 12, valueLength);
                }
                return pending.getValueBuffer();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (OutOfMemoryError e2) {
                FileBackedStorageEngine.LOGGER.error("Failed to allocate direct buffer for FileChannel read.  Consider increasing the -XX:MaxDirectMemorySize property to allow enough space for the FileChannel transfer buffers");
                throw e2;
            }
        }

        ByteBuffer readBuffer(long position, int length) throws EOFException {
            try {
                ByteBuffer data = ByteBuffer.allocate(length);
                int i = 0;
                while (data.hasRemaining()) {
                    int read = FileBackedStorageEngine.this.readFromChannel(data, position + i);
                    if (read < 0) {
                        throw new EOFException();
                    }
                    i += read;
                }
                return (ByteBuffer) data.rewind();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (OutOfMemoryError e2) {
                FileBackedStorageEngine.LOGGER.error("Failed to allocate direct buffer for FileChannel read.  Consider increasing the -XX:MaxDirectMemorySize property to allow enough space for the FileChannel transfer buffers");
                throw e2;
            }
        }

        Long writeMappingBuffers(ByteBuffer keyBuffer, ByteBuffer valueBuffer, int pojoHash) {
            int keyLength = keyBuffer.remaining();
            int valueLength = valueBuffer.remaining();
            long address = this.allocator.allocate(keyLength + valueLength + 12);
            if (address >= 0) {
                long position = this.filePosition + address;
                FileBackedStorageEngine<K, V>.FileWriteTask task = new FileWriteTask(this, position, keyBuffer, valueBuffer, pojoHash);
                FileBackedStorageEngine.this.pendingWrites.put(Long.valueOf(position), task);
                FileBackedStorageEngine.this.writeExecutor.execute(task);
                return Long.valueOf(address);
            }
            return null;
        }

        WriteContext getKeyWriteContext(long address) {
            try {
                long position = this.filePosition + address;
                FileBackedStorageEngine<K, V>.FileWriteTask pending = (FileWriteTask) FileBackedStorageEngine.this.pendingWrites.get(Long.valueOf(position));
                if (pending == null) {
                    int keyLength = FileBackedStorageEngine.this.readIntFromChannel(position + 4);
                    return getDiskWriteContext(position + 12, keyLength);
                }
                if (FileBackedStorageEngine.this.pendingWrites.get(Long.valueOf(position)) != pending) {
                    return getKeyWriteContext(address);
                }
                return getQueuedWriteContext(pending, pending.getKeyBuffer());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        WriteContext getValueWriteContext(long address) {
            try {
                long position = this.filePosition + address;
                FileBackedStorageEngine<K, V>.FileWriteTask pending = (FileWriteTask) FileBackedStorageEngine.this.pendingWrites.get(Long.valueOf(position));
                if (pending == null) {
                    int keyLength = FileBackedStorageEngine.this.readIntFromChannel(position + 4);
                    int valueLength = FileBackedStorageEngine.this.readIntFromChannel(position + 8);
                    return getDiskWriteContext(position + keyLength + 12, valueLength);
                }
                if (FileBackedStorageEngine.this.pendingWrites.get(Long.valueOf(position)) != pending) {
                    return getValueWriteContext(address);
                }
                return getQueuedWriteContext(pending, pending.getValueBuffer());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private WriteContext getDiskWriteContext(final long address, final int max) {
            return new WriteContext() { // from class: org.terracotta.offheapstore.disk.storage.FileBackedStorageEngine.FileChunk.1
                @Override // org.terracotta.offheapstore.storage.portability.WriteContext
                public void setLong(int offset, long value) {
                    if (offset >= 0 && offset < max) {
                        try {
                            FileBackedStorageEngine.this.writeLongToChannel(address + offset, value);
                            return;
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    throw new IllegalArgumentException();
                }

                @Override // org.terracotta.offheapstore.storage.portability.WriteContext
                public void flush() {
                }
            };
        }

        private WriteContext getQueuedWriteContext(final FileBackedStorageEngine<K, V>.FileWriteTask current, final ByteBuffer queuedBuffer) {
            return new WriteContext() { // from class: org.terracotta.offheapstore.disk.storage.FileBackedStorageEngine.FileChunk.2
                @Override // org.terracotta.offheapstore.storage.portability.WriteContext
                public void setLong(int offset, long value) {
                    queuedBuffer.putLong(offset, value);
                }

                @Override // org.terracotta.offheapstore.storage.portability.WriteContext
                public void flush() {
                    FileBackedStorageEngine<K, V>.FileWriteTask flush = new FileWriteTask(current.chunk, current.position, current.keyBuffer, current.valueBuffer, current.pojoHash);
                    FileBackedStorageEngine.this.pendingWrites.put(Long.valueOf(((FileWriteTask) flush).position), flush);
                    FileBackedStorageEngine.this.writeExecutor.execute(flush);
                }
            };
        }

        void free(long address) {
            int keyLength;
            int valueLength;
            try {
                long position = this.filePosition + address;
                FileBackedStorageEngine<K, V>.FileWriteTask pending = (FileWriteTask) FileBackedStorageEngine.this.pendingWrites.remove(Long.valueOf(position));
                if (pending == null) {
                    keyLength = FileBackedStorageEngine.this.readIntFromChannel(position + 4);
                    valueLength = FileBackedStorageEngine.this.readIntFromChannel(position + 8);
                } else {
                    keyLength = pending.getKeyBuffer().remaining();
                    valueLength = pending.getValueBuffer().remaining();
                }
                this.allocator.free(address, keyLength + valueLength + 12);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        synchronized void clear() {
            FileBackedStorageEngine.this.source.freeRegion(this.filePosition);
            this.valid = false;
        }

        long capacity() {
            return this.allocator.capacity();
        }

        long occupied() {
            return this.allocator.occupied();
        }

        long baseAddress() {
            return this.baseAddress;
        }

        void persist(ObjectOutput output) throws IOException {
            output.writeInt(FileBackedStorageEngine.MAGIC_CHUNK);
            output.writeLong(this.filePosition);
            output.writeLong(this.baseAddress);
            output.writeLong(this.allocator.capacity());
            this.allocator.persist(output);
        }

        synchronized boolean isValid() {
            return this.valid;
        }

        Set<Long> encodings() {
            Set<Long> encodings = new HashSet<>();
            for (Long encoding : FileBackedStorageEngine.this.owner.encodingSet()) {
                long relative = encoding.longValue() - baseAddress();
                if (relative >= 0 && relative < capacity()) {
                    encodings.add(encoding);
                }
            }
            return encodings;
        }

        void evictAll() {
            Iterator i$ = encodings().iterator();
            while (i$.hasNext()) {
                long encoding = i$.next().longValue();
                int slot = FileBackedStorageEngine.this.owner.getSlotForHashAndEncoding(readPojoHash(encoding - baseAddress()), encoding, -1L).intValue();
                FileBackedStorageEngine.this.owner.evict(slot, true);
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/disk/storage/FileBackedStorageEngine$FileWriteTask.class */
    class FileWriteTask implements Runnable {
        private final FileBackedStorageEngine<K, V>.FileChunk chunk;
        private final ByteBuffer keyBuffer;
        private final ByteBuffer valueBuffer;
        private final int pojoHash;
        private final long position;

        FileWriteTask(FileBackedStorageEngine<K, V>.FileChunk chunk, long position, ByteBuffer keyBuffer, ByteBuffer valueBuffer, int pojoHash) {
            this.chunk = chunk;
            this.position = position;
            this.keyBuffer = keyBuffer;
            this.valueBuffer = valueBuffer;
            this.pojoHash = pojoHash;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (FileBackedStorageEngine.this.pendingWrites.get(Long.valueOf(this.position)) == this) {
                try {
                    synchronized (this.chunk) {
                        if (this.chunk.isValid()) {
                            try {
                                try {
                                    write();
                                } catch (IOException e) {
                                    FileBackedStorageEngine.LOGGER.warn("Received IOException '{}' during write @ {} : giving up", e.getMessage(), Long.valueOf(this.position));
                                } catch (OutOfMemoryError e2) {
                                    FileBackedStorageEngine.LOGGER.error("Failed to allocate a direct buffer for a FileChannel write.  Consider increasing the -XX:MaxDirectMemorySize property to allow enough space for the FileChannel transfer buffers");
                                    throw e2;
                                } catch (ClosedChannelException e3) {
                                    FileBackedStorageEngine.LOGGER.debug("DiskWriteTask terminated due to closed channel - we must be shutting down", (Throwable) e3);
                                }
                            } catch (IOException e4) {
                                FileBackedStorageEngine.LOGGER.warn("Received IOException '{}' while trying to write @ {} : trying again", e4.getMessage(), Long.valueOf(this.position));
                                write();
                            }
                        }
                    }
                } finally {
                    FileBackedStorageEngine.this.pendingWrites.remove(Long.valueOf(this.position), this);
                }
            }
        }

        private void write() throws IOException {
            ByteBuffer key = getKeyBuffer();
            ByteBuffer value = getValueBuffer();
            int keyLength = key.remaining();
            int valueLength = value.remaining();
            FileBackedStorageEngine.this.writeIntToChannel(this.position + 0, this.pojoHash);
            FileBackedStorageEngine.this.writeIntToChannel(this.position + 4, keyLength);
            FileBackedStorageEngine.this.writeIntToChannel(this.position + 8, valueLength);
            FileBackedStorageEngine.this.writeBufferToChannel(this.position + 12, key);
            FileBackedStorageEngine.this.writeBufferToChannel(this.position + 12 + keyLength, value);
            long size = FileBackedStorageEngine.this.writeChannel.size();
            long expected = this.position + keyLength + valueLength + 12;
            if (size < expected) {
                throw new IOException("File size does not encompass last write [size:" + size + " end-of-write:" + expected);
            }
        }

        ByteBuffer getKeyBuffer() {
            return this.keyBuffer.duplicate();
        }

        ByteBuffer getValueBuffer() {
            return this.valueBuffer.duplicate();
        }
    }

    @Override // org.terracotta.offheapstore.storage.BinaryStorageEngine
    public int readKeyHash(long address) {
        FileBackedStorageEngine<K, V>.FileChunk chunk = findChunk(address);
        return chunk.readPojoHash(address - chunk.baseAddress());
    }
}

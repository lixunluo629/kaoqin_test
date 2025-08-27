package org.aspectj.weaver.tools.cache;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import org.aspectj.util.FileUtil;
import org.aspectj.util.LangUtil;
import org.aspectj.weaver.tools.Trace;
import org.aspectj.weaver.tools.TraceFactory;
import org.aspectj.weaver.tools.cache.AbstractIndexedFileCacheBacking;
import org.aspectj.weaver.tools.cache.CachedClassEntry;
import org.springframework.beans.PropertyAccessor;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/cache/AsynchronousFileCacheBacking.class */
public abstract class AsynchronousFileCacheBacking extends AbstractIndexedFileCacheBacking {
    private static final BlockingQueue<AsyncCommand> commandsQ = new LinkedBlockingQueue();
    private static final ExecutorService execService = Executors.newSingleThreadExecutor();
    private static Future<?> commandsRunner;
    protected final Map<String, AbstractIndexedFileCacheBacking.IndexEntry> index;
    protected final Map<String, AbstractIndexedFileCacheBacking.IndexEntry> exposedIndex;
    protected final Map<String, byte[]> bytesMap;
    protected final Map<String, byte[]> exposedBytes;

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/cache/AsynchronousFileCacheBacking$AsyncCommand.class */
    public interface AsyncCommand {
        AsynchronousFileCacheBacking getCache();
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/cache/AsynchronousFileCacheBacking$AsynchronousFileCacheBackingCreator.class */
    public interface AsynchronousFileCacheBackingCreator<T extends AsynchronousFileCacheBacking> {
        T create(File file);
    }

    protected abstract void removeClassBytes(String str) throws Exception;

    protected abstract Map<String, byte[]> readClassBytes(Map<String, AbstractIndexedFileCacheBacking.IndexEntry> map, File file);

    protected AsynchronousFileCacheBacking(File cacheDir) {
        super(cacheDir);
        this.index = readIndex(cacheDir, getIndexFile());
        this.exposedIndex = Collections.unmodifiableMap(this.index);
        this.bytesMap = readClassBytes(this.index, cacheDir);
        this.exposedBytes = Collections.unmodifiableMap(this.bytesMap);
    }

    @Override // org.aspectj.weaver.tools.cache.AbstractIndexedFileCacheBacking
    protected Map<String, AbstractIndexedFileCacheBacking.IndexEntry> getIndex() {
        return this.index;
    }

    @Override // org.aspectj.weaver.tools.cache.CacheBacking
    public CachedClassEntry get(CachedClassReference ref, byte[] originalBytes) {
        String key = ref.getKey();
        synchronized (this.index) {
            AbstractIndexedFileCacheBacking.IndexEntry indexEntry = this.index.get(key);
            if (indexEntry == null) {
                return null;
            }
            if (crc(originalBytes) != indexEntry.crcClass) {
                if (this.logger != null && this.logger.isTraceEnabled()) {
                    this.logger.debug("get(" + getCacheDirectory() + ") mismatched original class bytes CRC for " + key);
                }
                remove(key);
                return null;
            }
            if (indexEntry.ignored) {
                return new CachedClassEntry(ref, WeavedClassCache.ZERO_BYTES, CachedClassEntry.EntryType.IGNORED);
            }
            synchronized (this.bytesMap) {
                byte[] bytes = this.bytesMap.remove(key);
                if (bytes == null) {
                    return null;
                }
                if (indexEntry.generated) {
                    return new CachedClassEntry(ref, bytes, CachedClassEntry.EntryType.GENERATED);
                }
                return new CachedClassEntry(ref, bytes, CachedClassEntry.EntryType.WEAVED);
            }
        }
    }

    @Override // org.aspectj.weaver.tools.cache.CacheBacking
    public void put(CachedClassEntry entry, byte[] originalBytes) {
        String key = entry.getKey();
        byte[] bytes = entry.isIgnored() ? null : entry.getBytes();
        synchronized (this.index) {
            AbstractIndexedFileCacheBacking.IndexEntry indexEntry = this.index.get(key);
            if (indexEntry != null) {
                return;
            }
            AbstractIndexedFileCacheBacking.IndexEntry indexEntry2 = createIndexEntry(entry, originalBytes);
            this.index.put(key, indexEntry2);
            if (!postCacheCommand(new InsertCommand(this, key, bytes)) && this.logger != null && this.logger.isTraceEnabled()) {
                this.logger.error("put(" + getCacheDirectory() + ") Failed to post insert command for " + key);
            }
            if (this.logger != null && this.logger.isTraceEnabled()) {
                this.logger.debug("put(" + getCacheDirectory() + ")[" + key + "] inserted");
            }
        }
    }

    @Override // org.aspectj.weaver.tools.cache.CacheBacking
    public void remove(CachedClassReference ref) {
        remove(ref.getKey());
    }

    protected AbstractIndexedFileCacheBacking.IndexEntry remove(String key) {
        AbstractIndexedFileCacheBacking.IndexEntry entry;
        synchronized (this.index) {
            entry = this.index.remove(key);
        }
        synchronized (this.bytesMap) {
            this.bytesMap.remove(key);
        }
        if (!postCacheCommand(new RemoveCommand(this, key)) && this.logger != null && this.logger.isTraceEnabled()) {
            this.logger.error("remove(" + getCacheDirectory() + ") Failed to post remove command for " + key);
        }
        if (entry != null) {
            if (!key.equals(entry.key)) {
                if (this.logger != null && this.logger.isTraceEnabled()) {
                    this.logger.error("remove(" + getCacheDirectory() + ") Mismatched keys: " + key + " / " + entry.key);
                }
            } else if (this.logger != null && this.logger.isTraceEnabled()) {
                this.logger.debug("remove(" + getCacheDirectory() + ")[" + key + "] removed");
            }
        }
        return entry;
    }

    public List<AbstractIndexedFileCacheBacking.IndexEntry> getIndexEntries() {
        synchronized (this.index) {
            if (this.index.isEmpty()) {
                return Collections.emptyList();
            }
            return new ArrayList(this.index.values());
        }
    }

    public Map<String, AbstractIndexedFileCacheBacking.IndexEntry> getIndexMap() {
        return this.exposedIndex;
    }

    public Map<String, byte[]> getBytesMap() {
        return this.exposedBytes;
    }

    @Override // org.aspectj.weaver.tools.cache.CacheBacking
    public void clear() {
        synchronized (this.index) {
            this.index.clear();
        }
        if (!postCacheCommand(new ClearCommand(this)) && this.logger != null && this.logger.isTraceEnabled()) {
            this.logger.error("Failed to post clear command for " + getIndexFile());
        }
    }

    protected void executeCommand(AsyncCommand cmd) throws Exception {
        if (cmd instanceof ClearCommand) {
            executeClearCommand();
            return;
        }
        if (cmd instanceof UpdateIndexCommand) {
            executeUpdateIndexCommand();
        } else if (cmd instanceof InsertCommand) {
            executeInsertCommand((InsertCommand) cmd);
        } else {
            if (cmd instanceof RemoveCommand) {
                executeRemoveCommand((RemoveCommand) cmd);
                return;
            }
            throw new UnsupportedOperationException("Unknown command: " + cmd);
        }
    }

    protected void executeClearCommand() throws Exception {
        FileUtil.deleteContents(getIndexFile());
        FileUtil.deleteContents(getCacheDirectory());
    }

    protected void executeUpdateIndexCommand() throws Exception {
        writeIndex(getIndexFile(), getIndexEntries());
    }

    protected void executeInsertCommand(InsertCommand cmd) throws Exception {
        writeIndex(getIndexFile(), getIndexEntries());
        byte[] bytes = cmd.getClassBytes();
        if (bytes != null) {
            writeClassBytes(cmd.getKey(), bytes);
        }
    }

    protected void executeRemoveCommand(RemoveCommand cmd) throws Exception {
        Exception err = null;
        try {
            removeClassBytes(cmd.getKey());
        } catch (Exception e) {
            err = e;
        }
        writeIndex(getIndexFile(), getIndexEntries());
        if (err != null) {
            throw err;
        }
    }

    public String toString() {
        return getClass().getSimpleName() + PropertyAccessor.PROPERTY_KEY_PREFIX + String.valueOf(getCacheDirectory()) + "]";
    }

    protected static final <T extends AsynchronousFileCacheBacking> T createBacking(File file, AsynchronousFileCacheBackingCreator<T> asynchronousFileCacheBackingCreator) {
        final Trace trace = TraceFactory.getTraceFactory().getTrace(AsynchronousFileCacheBacking.class);
        if (!file.exists() && !file.mkdirs()) {
            if (trace != null && trace.isTraceEnabled()) {
                trace.error("Unable to create cache directory at " + file.getAbsolutePath());
                return null;
            }
            return null;
        }
        if (!file.canWrite()) {
            if (trace != null && trace.isTraceEnabled()) {
                trace.error("Cache directory is not writable at " + file.getAbsolutePath());
                return null;
            }
            return null;
        }
        T t = (T) asynchronousFileCacheBackingCreator.create(file);
        synchronized (execService) {
            if (commandsRunner == null) {
                commandsRunner = execService.submit(new Runnable() { // from class: org.aspectj.weaver.tools.cache.AsynchronousFileCacheBacking.1
                    @Override // java.lang.Runnable
                    public void run() {
                        while (true) {
                            try {
                                AsyncCommand cmd = (AsyncCommand) AsynchronousFileCacheBacking.commandsQ.take();
                                try {
                                    AsynchronousFileCacheBacking cache = cmd.getCache();
                                    cache.executeCommand(cmd);
                                } catch (Exception e) {
                                    if (trace != null && trace.isTraceEnabled()) {
                                        trace.error("Failed (" + e.getClass().getSimpleName() + ") to execute " + cmd + ": " + e.getMessage(), e);
                                    }
                                }
                            } catch (InterruptedException e2) {
                                if (trace != null && trace.isTraceEnabled()) {
                                    trace.warn("Interrupted");
                                }
                                Thread.currentThread().interrupt();
                                return;
                            }
                        }
                    }
                });
            }
        }
        if (!postCacheCommand(new UpdateIndexCommand(t)) && trace != null && trace.isTraceEnabled()) {
            trace.warn("Failed to offer update index command to " + file.getAbsolutePath());
        }
        return t;
    }

    public static final boolean postCacheCommand(AsyncCommand cmd) {
        return commandsQ.offer(cmd);
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/cache/AsynchronousFileCacheBacking$AbstractCommand.class */
    public static abstract class AbstractCommand implements AsyncCommand {
        private final AsynchronousFileCacheBacking cache;

        protected AbstractCommand(AsynchronousFileCacheBacking backing) {
            this.cache = backing;
            if (backing == null) {
                throw new IllegalStateException("No backing cache specified");
            }
        }

        @Override // org.aspectj.weaver.tools.cache.AsynchronousFileCacheBacking.AsyncCommand
        public final AsynchronousFileCacheBacking getCache() {
            return this.cache;
        }

        public String toString() {
            return getClass().getSimpleName() + PropertyAccessor.PROPERTY_KEY_PREFIX + getCache() + "]";
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/cache/AsynchronousFileCacheBacking$ClearCommand.class */
    public static class ClearCommand extends AbstractCommand {
        public ClearCommand(AsynchronousFileCacheBacking cache) {
            super(cache);
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/cache/AsynchronousFileCacheBacking$UpdateIndexCommand.class */
    public static class UpdateIndexCommand extends AbstractCommand {
        public UpdateIndexCommand(AsynchronousFileCacheBacking cache) {
            super(cache);
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/cache/AsynchronousFileCacheBacking$KeyedCommand.class */
    public static abstract class KeyedCommand extends AbstractCommand {
        private final String key;

        protected KeyedCommand(AsynchronousFileCacheBacking cache, String keyValue) {
            super(cache);
            if (LangUtil.isEmpty(keyValue)) {
                throw new IllegalStateException("No key value");
            }
            this.key = keyValue;
        }

        public final String getKey() {
            return this.key;
        }

        @Override // org.aspectj.weaver.tools.cache.AsynchronousFileCacheBacking.AbstractCommand
        public String toString() {
            return super.toString() + PropertyAccessor.PROPERTY_KEY_PREFIX + getKey() + "]";
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/cache/AsynchronousFileCacheBacking$RemoveCommand.class */
    public static class RemoveCommand extends KeyedCommand {
        public RemoveCommand(AsynchronousFileCacheBacking cache, String keyValue) {
            super(cache, keyValue);
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/cache/AsynchronousFileCacheBacking$InsertCommand.class */
    public static class InsertCommand extends KeyedCommand {
        private final byte[] bytes;

        public InsertCommand(AsynchronousFileCacheBacking cache, String keyValue, byte[] classBytes) {
            super(cache, keyValue);
            this.bytes = classBytes;
        }

        public final byte[] getClassBytes() {
            return this.bytes;
        }
    }
}

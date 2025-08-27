package org.aspectj.weaver.tools.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import org.aspectj.bridge.MessageUtil;
import org.aspectj.util.FileUtil;
import org.aspectj.util.LangUtil;
import org.aspectj.weaver.tools.cache.AbstractIndexedFileCacheBacking;
import org.aspectj.weaver.tools.cache.CachedClassEntry;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/cache/DefaultFileCacheBacking.class */
public class DefaultFileCacheBacking extends AbstractIndexedFileCacheBacking {
    private final Map<String, AbstractIndexedFileCacheBacking.IndexEntry> index;
    private static final Object LOCK = new Object();

    protected DefaultFileCacheBacking(File cacheDir) {
        super(cacheDir);
        this.index = readIndex();
    }

    public static final DefaultFileCacheBacking createBacking(File cacheDir) {
        if (!cacheDir.exists()) {
            if (!cacheDir.mkdirs()) {
                MessageUtil.error("Unable to create cache directory at " + cacheDir.getName());
                return null;
            }
        } else if (!cacheDir.isDirectory()) {
            MessageUtil.error("Not a cache directory at " + cacheDir.getName());
            return null;
        }
        if (!cacheDir.canWrite()) {
            MessageUtil.error("Cache directory is not writable at " + cacheDir.getName());
            return null;
        }
        return new DefaultFileCacheBacking(cacheDir);
    }

    @Override // org.aspectj.weaver.tools.cache.AbstractIndexedFileCacheBacking
    protected Map<String, AbstractIndexedFileCacheBacking.IndexEntry> getIndex() {
        return this.index;
    }

    @Override // org.aspectj.weaver.tools.cache.AbstractIndexedFileCacheBacking
    protected AbstractIndexedFileCacheBacking.IndexEntry resolveIndexMapEntry(File cacheDir, AbstractIndexedFileCacheBacking.IndexEntry ie) {
        File cacheEntry = new File(cacheDir, ie.key);
        if (ie.ignored || cacheEntry.canRead()) {
            return ie;
        }
        return null;
    }

    private void removeIndexEntry(String key) {
        synchronized (LOCK) {
            this.index.remove(key);
            writeIndex();
        }
    }

    private void addIndexEntry(AbstractIndexedFileCacheBacking.IndexEntry ie) {
        synchronized (LOCK) {
            this.index.put(ie.key, ie);
            writeIndex();
        }
    }

    @Override // org.aspectj.weaver.tools.cache.AbstractIndexedFileCacheBacking
    protected Map<String, AbstractIndexedFileCacheBacking.IndexEntry> readIndex() {
        Map<String, AbstractIndexedFileCacheBacking.IndexEntry> index;
        synchronized (LOCK) {
            index = super.readIndex();
        }
        return index;
    }

    @Override // org.aspectj.weaver.tools.cache.AbstractIndexedFileCacheBacking
    protected void writeIndex() {
        synchronized (LOCK) {
            super.writeIndex();
        }
    }

    @Override // org.aspectj.weaver.tools.cache.CacheBacking
    public void clear() {
        int numDeleted;
        File cacheDir = getCacheDirectory();
        synchronized (LOCK) {
            numDeleted = FileUtil.deleteContents(cacheDir);
        }
        if (numDeleted > 0 && this.logger != null && this.logger.isTraceEnabled()) {
            this.logger.info("clear(" + cacheDir + ") deleted");
        }
    }

    public static CacheBacking createBacking(String scope) {
        String cache = System.getProperty("aj.weaving.cache.dir");
        if (cache == null) {
            return null;
        }
        File cacheDir = new File(cache, scope);
        return createBacking(cacheDir);
    }

    @Override // org.aspectj.weaver.tools.cache.AbstractIndexedFileCacheBacking, org.aspectj.weaver.tools.cache.CacheBacking
    public String[] getKeys(final String regex) {
        File cacheDirectory = getCacheDirectory();
        File[] files = cacheDirectory.listFiles(new FilenameFilter() { // from class: org.aspectj.weaver.tools.cache.DefaultFileCacheBacking.1
            @Override // java.io.FilenameFilter
            public boolean accept(File file, String s) {
                if (s.matches(regex)) {
                    return true;
                }
                return false;
            }
        });
        if (LangUtil.isEmpty(files)) {
            return EMPTY_KEYS;
        }
        String[] keys = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            keys[i] = files[i].getName();
        }
        return keys;
    }

    @Override // org.aspectj.weaver.tools.cache.CacheBacking
    public CachedClassEntry get(CachedClassReference ref, byte[] originalBytes) {
        byte[] bytes;
        File cacheDirectory = getCacheDirectory();
        String refKey = ref.getKey();
        File cacheFile = new File(cacheDirectory, refKey);
        AbstractIndexedFileCacheBacking.IndexEntry ie = this.index.get(refKey);
        if (ie == null) {
            delete(cacheFile);
            return null;
        }
        if (crc(originalBytes) != ie.crcClass) {
            delete(cacheFile);
            return null;
        }
        if (ie.ignored) {
            return new CachedClassEntry(ref, WeavedClassCache.ZERO_BYTES, CachedClassEntry.EntryType.IGNORED);
        }
        if (cacheFile.canRead() && (bytes = read(cacheFile, ie.crcWeaved)) != null) {
            if (!ie.generated) {
                return new CachedClassEntry(ref, bytes, CachedClassEntry.EntryType.WEAVED);
            }
            return new CachedClassEntry(ref, bytes, CachedClassEntry.EntryType.GENERATED);
        }
        return null;
    }

    @Override // org.aspectj.weaver.tools.cache.CacheBacking
    public void put(CachedClassEntry entry, byte[] originalBytes) {
        boolean writeEntryBytes;
        File cacheDirectory = getCacheDirectory();
        String refKey = entry.getKey();
        AbstractIndexedFileCacheBacking.IndexEntry ie = this.index.get(refKey);
        File cacheFile = new File(cacheDirectory, refKey);
        if (ie != null && (ie.ignored != entry.isIgnored() || ie.generated != entry.isGenerated() || crc(originalBytes) != ie.crcClass)) {
            delete(cacheFile);
            writeEntryBytes = true;
        } else {
            writeEntryBytes = !cacheFile.exists();
        }
        if (writeEntryBytes) {
            AbstractIndexedFileCacheBacking.IndexEntry ie2 = createIndexEntry(entry, originalBytes);
            if (!entry.isIgnored()) {
                ie2.crcWeaved = write(cacheFile, entry.getBytes());
            }
            addIndexEntry(ie2);
        }
    }

    @Override // org.aspectj.weaver.tools.cache.CacheBacking
    public void remove(CachedClassReference ref) {
        File cacheDirectory = getCacheDirectory();
        String refKey = ref.getKey();
        File cacheFile = new File(cacheDirectory, refKey);
        synchronized (LOCK) {
            removeIndexEntry(refKey);
            delete(cacheFile);
        }
    }

    @Override // org.aspectj.weaver.tools.cache.AbstractFileCacheBacking
    protected void delete(File file) {
        synchronized (LOCK) {
            super.delete(file);
        }
    }

    protected byte[] read(File file, long expectedCRC) {
        byte[] bytes = null;
        synchronized (LOCK) {
            FileInputStream fis = null;
            try {
                try {
                    fis = new FileInputStream(file);
                    bytes = FileUtil.readAsByteArray(fis);
                    close(fis, file);
                } catch (Exception e) {
                    if (this.logger != null && this.logger.isTraceEnabled()) {
                        this.logger.warn("read(" + file.getAbsolutePath() + ") failed (" + e.getClass().getSimpleName() + ") to read contents: " + e.getMessage(), e);
                    }
                    close(fis, file);
                }
                if (bytes == null || crc(bytes) != expectedCRC) {
                    delete(file);
                    return null;
                }
                return bytes;
            } catch (Throwable th) {
                close((InputStream) null, file);
                throw th;
            }
        }
    }

    protected long write(File file, byte[] bytes) {
        synchronized (LOCK) {
            if (file.exists()) {
                return -1L;
            }
            OutputStream out = null;
            try {
                try {
                    out = new FileOutputStream(file);
                    out.write(bytes);
                    close(out, file);
                    return crc(bytes);
                } catch (Throwable th) {
                    close(out, file);
                    throw th;
                }
            } catch (Exception e) {
                if (this.logger != null && this.logger.isTraceEnabled()) {
                    this.logger.warn("write(" + file.getAbsolutePath() + ") failed (" + e.getClass().getSimpleName() + ") to write contents: " + e.getMessage(), e);
                }
                delete(file);
                close(out, file);
                return -1L;
            }
        }
    }
}

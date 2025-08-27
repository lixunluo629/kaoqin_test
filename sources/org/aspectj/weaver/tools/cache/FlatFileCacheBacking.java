package org.aspectj.weaver.tools.cache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.util.Map;
import java.util.TreeMap;
import org.aspectj.util.FileUtil;
import org.aspectj.util.LangUtil;
import org.aspectj.weaver.tools.cache.AbstractIndexedFileCacheBacking;
import org.aspectj.weaver.tools.cache.AsynchronousFileCacheBacking;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/cache/FlatFileCacheBacking.class */
public class FlatFileCacheBacking extends AsynchronousFileCacheBacking {
    private static final AsynchronousFileCacheBacking.AsynchronousFileCacheBackingCreator<FlatFileCacheBacking> defaultCreator = new AsynchronousFileCacheBacking.AsynchronousFileCacheBackingCreator<FlatFileCacheBacking>() { // from class: org.aspectj.weaver.tools.cache.FlatFileCacheBacking.1
        @Override // org.aspectj.weaver.tools.cache.AsynchronousFileCacheBacking.AsynchronousFileCacheBackingCreator
        public FlatFileCacheBacking create(File cacheDir) {
            return new FlatFileCacheBacking(cacheDir);
        }
    };

    public FlatFileCacheBacking(File cacheDir) {
        super(cacheDir);
    }

    public static final FlatFileCacheBacking createBacking(File cacheDir) {
        return (FlatFileCacheBacking) createBacking(cacheDir, defaultCreator);
    }

    @Override // org.aspectj.weaver.tools.cache.AsynchronousFileCacheBacking
    protected Map<String, byte[]> readClassBytes(Map<String, AbstractIndexedFileCacheBacking.IndexEntry> indexMap, File cacheDir) {
        return readClassBytes(indexMap, cacheDir.listFiles());
    }

    protected Map<String, byte[]> readClassBytes(Map<String, AbstractIndexedFileCacheBacking.IndexEntry> indexMap, File[] files) throws StreamCorruptedException {
        Map<String, byte[]> result = new TreeMap<>();
        if (LangUtil.isEmpty(files)) {
            return result;
        }
        for (File file : files) {
            if (file.isFile()) {
                String key = file.getName();
                if (AbstractIndexedFileCacheBacking.INDEX_FILE.equalsIgnoreCase(key)) {
                    continue;
                } else {
                    AbstractIndexedFileCacheBacking.IndexEntry entry = indexMap.get(key);
                    if (entry == null || entry.ignored) {
                        if (this.logger != null && this.logger.isTraceEnabled()) {
                            this.logger.info("readClassBytes(" + key + ") remove orphan/ignored: " + file.getAbsolutePath());
                        }
                        FileUtil.deleteContents(file);
                    } else {
                        try {
                            byte[] bytes = FileUtil.readAsByteArray(file);
                            long crc = crc(bytes);
                            if (crc != entry.crcWeaved) {
                                throw new StreamCorruptedException("Mismatched CRC - expected=" + entry.crcWeaved + "/got=" + crc);
                            }
                            result.put(key, bytes);
                            if (this.logger != null && this.logger.isTraceEnabled()) {
                                this.logger.debug("readClassBytes(" + key + ") cached from " + file.getAbsolutePath());
                            }
                        } catch (IOException e) {
                            if (this.logger != null && this.logger.isTraceEnabled()) {
                                this.logger.error("Failed (" + e.getClass().getSimpleName() + ") to read bytes from " + file.getAbsolutePath() + ": " + e.getMessage());
                            }
                            indexMap.remove(key);
                            FileUtil.deleteContents(file);
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override // org.aspectj.weaver.tools.cache.AbstractIndexedFileCacheBacking
    protected AbstractIndexedFileCacheBacking.IndexEntry resolveIndexMapEntry(File cacheDir, AbstractIndexedFileCacheBacking.IndexEntry ie) {
        File cacheEntry = new File(cacheDir, ie.key);
        if (ie.ignored || cacheEntry.canRead()) {
            return ie;
        }
        return null;
    }

    @Override // org.aspectj.weaver.tools.cache.AbstractFileCacheBacking
    protected void writeClassBytes(String key, byte[] bytes) throws Exception {
        File dir = getCacheDirectory();
        File file = new File(dir, key);
        FileOutputStream out = new FileOutputStream(file);
        try {
            out.write(bytes);
            out.close();
        } catch (Throwable th) {
            out.close();
            throw th;
        }
    }

    @Override // org.aspectj.weaver.tools.cache.AsynchronousFileCacheBacking
    protected void removeClassBytes(String key) throws Exception {
        File dir = getCacheDirectory();
        File file = new File(dir, key);
        if (file.exists() && !file.delete()) {
            throw new StreamCorruptedException("Failed to delete " + file.getAbsolutePath());
        }
    }
}

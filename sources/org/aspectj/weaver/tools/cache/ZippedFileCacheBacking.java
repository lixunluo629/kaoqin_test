package org.aspectj.weaver.tools.cache;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StreamCorruptedException;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import org.aspectj.util.FileUtil;
import org.aspectj.util.LangUtil;
import org.aspectj.weaver.tools.cache.AbstractIndexedFileCacheBacking;
import org.aspectj.weaver.tools.cache.AsynchronousFileCacheBacking;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/cache/ZippedFileCacheBacking.class */
public class ZippedFileCacheBacking extends AsynchronousFileCacheBacking {
    public static final String ZIP_FILE = "cache.zip";
    private static final AsynchronousFileCacheBacking.AsynchronousFileCacheBackingCreator<ZippedFileCacheBacking> defaultCreator = new AsynchronousFileCacheBacking.AsynchronousFileCacheBackingCreator<ZippedFileCacheBacking>() { // from class: org.aspectj.weaver.tools.cache.ZippedFileCacheBacking.1
        @Override // org.aspectj.weaver.tools.cache.AsynchronousFileCacheBacking.AsynchronousFileCacheBackingCreator
        public ZippedFileCacheBacking create(File cacheDir) {
            return new ZippedFileCacheBacking(cacheDir);
        }
    };
    private final File zipFile;

    public ZippedFileCacheBacking(File cacheDir) {
        super(cacheDir);
        this.zipFile = new File(cacheDir, ZIP_FILE);
    }

    public File getZipFile() {
        return this.zipFile;
    }

    public static final ZippedFileCacheBacking createBacking(File cacheDir) {
        return (ZippedFileCacheBacking) createBacking(cacheDir, defaultCreator);
    }

    @Override // org.aspectj.weaver.tools.cache.AbstractFileCacheBacking
    protected void writeClassBytes(String key, byte[] bytes) throws Exception {
        File outFile = getZipFile();
        try {
            Map<String, byte[]> entriesMap = readZipClassBytes(outFile);
            if (entriesMap.isEmpty()) {
                entriesMap = Collections.singletonMap(key, bytes);
            } else {
                entriesMap.put(key, bytes);
            }
            try {
                writeZipClassBytes(outFile, entriesMap);
            } catch (Exception e) {
                if (this.logger != null && this.logger.isTraceEnabled()) {
                    this.logger.warn("writeClassBytes(" + outFile + ")[" + key + "] failed (" + e.getClass().getSimpleName() + ") to write updated data: " + e.getMessage(), e);
                }
                FileUtil.deleteContents(outFile);
            }
        } catch (Exception e2) {
            if (this.logger != null && this.logger.isTraceEnabled()) {
                this.logger.warn("writeClassBytes(" + outFile + ")[" + key + "] failed (" + e2.getClass().getSimpleName() + ") to read current data: " + e2.getMessage(), e2);
            }
            FileUtil.deleteContents(outFile);
        }
    }

    @Override // org.aspectj.weaver.tools.cache.AsynchronousFileCacheBacking
    protected void removeClassBytes(String key) throws Exception {
        File outFile = getZipFile();
        try {
            Map<String, byte[]> entriesMap = readZipClassBytes(outFile);
            if (!entriesMap.isEmpty() && entriesMap.remove(key) == null) {
                return;
            }
            try {
                writeZipClassBytes(outFile, entriesMap);
            } catch (Exception e) {
                if (this.logger != null && this.logger.isTraceEnabled()) {
                    this.logger.warn("removeClassBytes(" + outFile + ")[" + key + "] failed (" + e.getClass().getSimpleName() + ") to write updated data: " + e.getMessage(), e);
                }
                FileUtil.deleteContents(outFile);
            }
        } catch (Exception e2) {
            if (this.logger != null && this.logger.isTraceEnabled()) {
                this.logger.warn("removeClassBytes(" + outFile + ")[" + key + "] failed (" + e2.getClass().getSimpleName() + ") to read current data: " + e2.getMessage(), e2);
            }
            FileUtil.deleteContents(outFile);
        }
    }

    @Override // org.aspectj.weaver.tools.cache.AsynchronousFileCacheBacking
    protected Map<String, byte[]> readClassBytes(Map<String, AbstractIndexedFileCacheBacking.IndexEntry> indexMap, File cacheDir) {
        Map<String, byte[]> entriesMap;
        File dataFile = new File(cacheDir, ZIP_FILE);
        boolean okEntries = true;
        try {
            entriesMap = readZipClassBytes(dataFile);
        } catch (Exception e) {
            if (this.logger != null && this.logger.isTraceEnabled()) {
                this.logger.warn("Failed (" + e.getClass().getSimpleName() + ") to read zip entries from " + dataFile + ": " + e.getMessage(), e);
            }
            entriesMap = new TreeMap();
            okEntries = false;
        }
        if (!syncClassBytesEntries(dataFile, indexMap, entriesMap)) {
            okEntries = false;
        }
        if (!okEntries) {
            FileUtil.deleteContents(dataFile);
            if (!entriesMap.isEmpty()) {
                entriesMap.clear();
            }
        }
        syncIndexEntries(dataFile, indexMap, entriesMap);
        return entriesMap;
    }

    protected Collection<String> syncIndexEntries(File dataFile, Map<String, AbstractIndexedFileCacheBacking.IndexEntry> indexMap, Map<String, byte[]> entriesMap) {
        Collection<String> toDelete = null;
        for (Map.Entry<String, AbstractIndexedFileCacheBacking.IndexEntry> ie : indexMap.entrySet()) {
            String key = ie.getKey();
            AbstractIndexedFileCacheBacking.IndexEntry indexEntry = ie.getValue();
            if (!indexEntry.ignored && !entriesMap.containsKey(key)) {
                if (this.logger != null && this.logger.isTraceEnabled()) {
                    this.logger.debug("syncIndexEntries(" + dataFile + ")[" + key + "] no class bytes");
                }
                if (toDelete == null) {
                    toDelete = new TreeSet<>();
                }
                toDelete.add(key);
            }
        }
        if (toDelete == null) {
            return Collections.emptySet();
        }
        Iterator<String> it = toDelete.iterator();
        while (it.hasNext()) {
            indexMap.remove(it.next());
        }
        return toDelete;
    }

    protected boolean syncClassBytesEntries(File dataFile, Map<String, AbstractIndexedFileCacheBacking.IndexEntry> indexMap, Map<String, byte[]> entriesMap) {
        boolean okEntries = true;
        for (Map.Entry<String, byte[]> bytesEntry : entriesMap.entrySet()) {
            String key = bytesEntry.getKey();
            AbstractIndexedFileCacheBacking.IndexEntry indexEntry = indexMap.get(key);
            if (indexEntry == null || indexEntry.ignored) {
                if (this.logger != null && this.logger.isTraceEnabled()) {
                    this.logger.debug("syncClassBytesEntries(" + dataFile + ")[" + key + "] bad index entry");
                }
                okEntries = false;
            } else {
                long crc = crc(bytesEntry.getValue());
                if (crc != indexEntry.crcWeaved) {
                    if (this.logger != null && this.logger.isTraceEnabled()) {
                        this.logger.debug("syncClassBytesEntries(" + dataFile + ")[" + key + "] mismatched CRC - expected=" + indexEntry.crcWeaved + "/got=" + crc);
                    }
                    indexMap.remove(key);
                    okEntries = false;
                }
            }
        }
        return okEntries;
    }

    @Override // org.aspectj.weaver.tools.cache.AbstractIndexedFileCacheBacking
    protected AbstractIndexedFileCacheBacking.IndexEntry resolveIndexMapEntry(File cacheDir, AbstractIndexedFileCacheBacking.IndexEntry ie) {
        if (cacheDir.exists()) {
            return ie;
        }
        return null;
    }

    /* JADX WARN: Finally extract failed */
    public static final Map<String, byte[]> readZipClassBytes(File file) throws IOException {
        if (!file.canRead()) {
            return Collections.emptyMap();
        }
        Map<String, byte[]> result = new TreeMap<>();
        byte[] copyBuf = new byte[4096];
        ByteArrayOutputStream out = new ByteArrayOutputStream(copyBuf.length);
        ZipFile zipFile = new ZipFile(file);
        try {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries != null && entries.hasMoreElements()) {
                ZipEntry e = entries.nextElement();
                String name = e.getName();
                if (!LangUtil.isEmpty(name)) {
                    out.reset();
                    InputStream zipStream = zipFile.getInputStream(e);
                    try {
                        for (int nRead = zipStream.read(copyBuf); nRead != -1; nRead = zipStream.read(copyBuf)) {
                            out.write(copyBuf, 0, nRead);
                        }
                        zipStream.close();
                        byte[] data = out.toByteArray();
                        byte[] prev = result.put(name, data);
                        if (prev != null) {
                            throw new StreamCorruptedException("Multiple entries for " + name);
                        }
                    } catch (Throwable th) {
                        zipStream.close();
                        throw th;
                    }
                }
            }
            return result;
        } finally {
            zipFile.close();
        }
    }

    public static final void writeZipClassBytes(File file, Map<String, byte[]> entriesMap) throws IOException {
        if (entriesMap.isEmpty()) {
            FileUtil.deleteContents(file);
            return;
        }
        File zipDir = file.getParentFile();
        if (!zipDir.exists() && !zipDir.mkdirs()) {
            throw new IOException("Failed to create path to " + zipDir.getAbsolutePath());
        }
        ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(file), 4096));
        try {
            for (Map.Entry<String, byte[]> bytesEntry : entriesMap.entrySet()) {
                String key = bytesEntry.getKey();
                byte[] bytes = bytesEntry.getValue();
                zipOut.putNextEntry(new ZipEntry(key));
                zipOut.write(bytes);
                zipOut.closeEntry();
            }
        } finally {
            zipOut.close();
        }
    }
}

package org.aspectj.weaver.tools.cache;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import org.aspectj.util.LangUtil;
import org.springframework.beans.PropertyAccessor;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/cache/AbstractIndexedFileCacheBacking.class */
public abstract class AbstractIndexedFileCacheBacking extends AbstractFileCacheBacking {
    public static final String INDEX_FILE = "cache.idx";
    protected static final IndexEntry[] EMPTY_INDEX = new IndexEntry[0];
    protected static final String[] EMPTY_KEYS = new String[0];
    private final File indexFile;

    protected abstract Map<String, IndexEntry> getIndex();

    protected AbstractIndexedFileCacheBacking(File cacheDir) {
        super(cacheDir);
        this.indexFile = new File(cacheDir, INDEX_FILE);
    }

    public File getIndexFile() {
        return this.indexFile;
    }

    @Override // org.aspectj.weaver.tools.cache.CacheBacking
    public String[] getKeys(String regex) {
        Map<String, IndexEntry> index = getIndex();
        if (index == null || index.isEmpty()) {
            return EMPTY_KEYS;
        }
        Collection<String> matches = new LinkedList<>();
        synchronized (index) {
            for (String key : index.keySet()) {
                if (key.matches(regex)) {
                    matches.add(key);
                }
            }
        }
        if (matches.isEmpty()) {
            return EMPTY_KEYS;
        }
        return (String[]) matches.toArray(new String[matches.size()]);
    }

    protected Map<String, IndexEntry> readIndex() {
        return readIndex(getCacheDirectory(), getIndexFile());
    }

    protected void writeIndex() {
        writeIndex(getIndexFile());
    }

    protected void writeIndex(File file) {
        try {
            writeIndex(file, getIndex());
        } catch (Exception e) {
            if (this.logger != null && this.logger.isTraceEnabled()) {
                this.logger.warn("writeIndex(" + file + ") " + e.getClass().getSimpleName() + ": " + e.getMessage(), e);
            }
        }
    }

    protected Map<String, IndexEntry> readIndex(File cacheDir, File cacheFile) {
        Map<String, IndexEntry> indexMap = new TreeMap<>();
        IndexEntry[] idxValues = readIndex(cacheFile);
        if (LangUtil.isEmpty(idxValues)) {
            if (this.logger != null && this.logger.isTraceEnabled()) {
                this.logger.debug("readIndex(" + cacheFile + ") no index entries");
            }
            return indexMap;
        }
        for (IndexEntry ie : idxValues) {
            IndexEntry resEntry = resolveIndexMapEntry(cacheDir, ie);
            if (resEntry != null) {
                indexMap.put(resEntry.key, resEntry);
            } else if (this.logger != null && this.logger.isTraceEnabled()) {
                this.logger.debug("readIndex(" + cacheFile + ") skip " + ie.key);
            }
        }
        return indexMap;
    }

    protected IndexEntry resolveIndexMapEntry(File cacheDir, IndexEntry ie) {
        return ie;
    }

    public IndexEntry[] readIndex(File indexFile) {
        if (!indexFile.canRead()) {
            return EMPTY_INDEX;
        }
        ObjectInputStream ois = null;
        try {
            try {
                ois = new ObjectInputStream(new FileInputStream(indexFile));
                IndexEntry[] indexEntryArr = (IndexEntry[]) ois.readObject();
                close(ois, indexFile);
                return indexEntryArr;
            } catch (Exception e) {
                if (this.logger != null && this.logger.isTraceEnabled()) {
                    this.logger.error("Failed (" + e.getClass().getSimpleName() + ") to read index from " + indexFile.getAbsolutePath() + " : " + e.getMessage(), e);
                }
                delete(indexFile);
                close(ois, indexFile);
                return EMPTY_INDEX;
            }
        } catch (Throwable th) {
            close(ois, indexFile);
            throw th;
        }
    }

    protected void writeIndex(File indexFile, Map<String, ? extends IndexEntry> index) throws IOException {
        writeIndex(indexFile, LangUtil.isEmpty(index) ? Collections.emptyList() : index.values());
    }

    protected void writeIndex(File indexFile, IndexEntry... entries) throws IOException {
        writeIndex(indexFile, LangUtil.isEmpty(entries) ? Collections.emptyList() : Arrays.asList(entries));
    }

    protected void writeIndex(File indexFile, Collection<? extends IndexEntry> entries) throws IOException {
        File indexDir = indexFile.getParentFile();
        if (!indexDir.exists() && !indexDir.mkdirs()) {
            throw new IOException("Failed to create path to " + indexFile.getAbsolutePath());
        }
        int numEntries = LangUtil.isEmpty(entries) ? 0 : entries.size();
        IndexEntry[] entryValues = numEntries <= 0 ? null : (IndexEntry[]) entries.toArray(new IndexEntry[numEntries]);
        if (LangUtil.isEmpty(entryValues)) {
            if (indexFile.exists() && !indexFile.delete()) {
                throw new StreamCorruptedException("Failed to clean up index file at " + indexFile.getAbsolutePath());
            }
        } else {
            ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(indexFile), 4096));
            try {
                oos.writeObject(entryValues);
                close(oos, indexFile);
            } catch (Throwable th) {
                close(oos, indexFile);
                throw th;
            }
        }
    }

    public static final IndexEntry createIndexEntry(CachedClassEntry classEntry, byte[] originalBytes) {
        if (classEntry == null) {
            return null;
        }
        IndexEntry indexEntry = new IndexEntry();
        indexEntry.key = classEntry.getKey();
        indexEntry.generated = classEntry.isGenerated();
        indexEntry.ignored = classEntry.isIgnored();
        indexEntry.crcClass = crc(originalBytes);
        if (!classEntry.isIgnored()) {
            indexEntry.crcWeaved = crc(classEntry.getBytes());
        }
        return indexEntry;
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/cache/AbstractIndexedFileCacheBacking$IndexEntry.class */
    public static class IndexEntry implements Serializable, Cloneable {
        private static final long serialVersionUID = 756391290557029363L;
        public String key;
        public boolean generated;
        public boolean ignored;
        public long crcClass;
        public long crcWeaved;

        /* renamed from: clone, reason: merged with bridge method [inline-methods] */
        public IndexEntry m4873clone() {
            try {
                return (IndexEntry) getClass().cast(super.clone());
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException("Failed to clone: " + toString() + ": " + e.getMessage(), e);
            }
        }

        public int hashCode() {
            return (int) (this.key.hashCode() + (this.generated ? 1 : 0) + (this.ignored ? 1 : 0) + this.crcClass + this.crcWeaved);
        }

        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            IndexEntry other = (IndexEntry) obj;
            if (this.key.equals(other.key) && this.ignored == other.ignored && this.generated == other.generated && this.crcClass == other.crcClass && this.crcWeaved == other.crcWeaved) {
                return true;
            }
            return false;
        }

        public String toString() {
            return this.key + PropertyAccessor.PROPERTY_KEY_PREFIX + (this.generated ? "generated" : "ignored") + "];crcClass=0x" + Long.toHexString(this.crcClass) + ";crcWeaved=0x" + Long.toHexString(this.crcWeaved);
        }
    }
}

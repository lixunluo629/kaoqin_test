package org.ehcache.impl.persistence;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.ehcache.CachePersistenceException;
import org.ehcache.impl.internal.concurrent.ConcurrentHashMap;
import org.ehcache.impl.serialization.TransientStateHolder;
import org.ehcache.spi.persistence.StateHolder;
import org.ehcache.spi.persistence.StateRepository;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/persistence/FileBasedStateRepository.class */
class FileBasedStateRepository implements StateRepository, Closeable {
    private static final String HOLDER_FILE_PREFIX = "holder-";
    private static final String HOLDER_FILE_SUFFIX = ".bin";
    private final File dataDirectory;
    private final ConcurrentMap<String, Tuple> knownHolders;
    private final AtomicInteger nextIndex = new AtomicInteger();

    FileBasedStateRepository(File directory) throws IOException, CachePersistenceException {
        if (directory == null) {
            throw new NullPointerException("directory must be non null");
        }
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory + " is not a directory");
        }
        this.dataDirectory = directory;
        this.knownHolders = new ConcurrentHashMap();
        loadMaps();
    }

    @SuppressFBWarnings({"NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE"})
    private void loadMaps() throws IOException, CachePersistenceException {
        try {
            File[] arr$ = this.dataDirectory.listFiles(new FilenameFilter() { // from class: org.ehcache.impl.persistence.FileBasedStateRepository.1
                @Override // java.io.FilenameFilter
                public boolean accept(File dir, String name) {
                    return name.endsWith(FileBasedStateRepository.HOLDER_FILE_SUFFIX);
                }
            });
            for (File file : arr$) {
                FileInputStream fis = new FileInputStream(file);
                try {
                    ObjectInputStream oin = new ObjectInputStream(fis);
                    try {
                        String name = (String) oin.readObject();
                        Tuple tuple = (Tuple) oin.readObject();
                        if (this.nextIndex.get() <= tuple.index) {
                            this.nextIndex.set(tuple.index + 1);
                        }
                        this.knownHolders.put(name, tuple);
                        oin.close();
                        fis.close();
                    } finally {
                    }
                } finally {
                }
            }
        } catch (Exception e) {
            this.knownHolders.clear();
            throw new CachePersistenceException("Failed to load existing StateRepository data", e);
        }
    }

    private void saveMaps() throws IOException {
        for (Map.Entry<String, Tuple> entry : this.knownHolders.entrySet()) {
            File outFile = new File(this.dataDirectory, createFileName(entry));
            FileOutputStream fos = new FileOutputStream(outFile);
            try {
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                try {
                    oos.writeObject(entry.getKey());
                    oos.writeObject(entry.getValue());
                    oos.close();
                } finally {
                }
            } finally {
                fos.close();
            }
        }
    }

    private String createFileName(Map.Entry<String, Tuple> entry) {
        return HOLDER_FILE_PREFIX + entry.getValue().index + "-" + FileUtils.safeIdentifier(entry.getKey(), false) + HOLDER_FILE_SUFFIX;
    }

    @Override // org.ehcache.spi.persistence.StateRepository
    public <K extends Serializable, V extends Serializable> StateHolder<K, V> getPersistentStateHolder(String str, Class<K> cls, Class<V> cls2) {
        Tuple tuplePutIfAbsent = this.knownHolders.get(str);
        if (tuplePutIfAbsent == null) {
            TransientStateHolder transientStateHolder = new TransientStateHolder();
            tuplePutIfAbsent = this.knownHolders.putIfAbsent(str, new Tuple(this.nextIndex.getAndIncrement(), transientStateHolder));
            if (tuplePutIfAbsent == null) {
                return transientStateHolder;
            }
        }
        return (StateHolder<K, V>) tuplePutIfAbsent.holder;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        saveMaps();
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/persistence/FileBasedStateRepository$Tuple.class */
    static class Tuple implements Serializable {
        final int index;
        final StateHolder<?, ?> holder;

        Tuple(int index, StateHolder<?, ?> holder) {
            this.index = index;
            this.holder = holder;
        }
    }
}

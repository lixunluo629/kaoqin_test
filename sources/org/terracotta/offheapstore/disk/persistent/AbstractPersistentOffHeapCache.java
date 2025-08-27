package org.terracotta.offheapstore.disk.persistent;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.IntBuffer;
import java.nio.MappedByteBuffer;
import java.util.concurrent.locks.Lock;
import org.terracotta.offheapstore.AbstractOffHeapClockCache;
import org.terracotta.offheapstore.disk.paging.MappedPageSource;
import org.terracotta.offheapstore.util.FindbugsSuppressWarnings;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/disk/persistent/AbstractPersistentOffHeapCache.class */
public abstract class AbstractPersistentOffHeapCache<K, V> extends AbstractOffHeapClockCache<K, V> implements Persistent {
    private static final int MAGIC = 1229737033;

    public AbstractPersistentOffHeapCache(MappedPageSource tableSource, PersistentStorageEngine<? super K, ? super V> storageEngine, boolean bootstrap) {
        super(tableSource, storageEngine, bootstrap);
    }

    public AbstractPersistentOffHeapCache(MappedPageSource tableSource, PersistentStorageEngine<? super K, ? super V> storageEngine, int tableSize, boolean bootstrap) {
        super(tableSource, storageEngine, tableSize, bootstrap);
    }

    @Override // org.terracotta.offheapstore.disk.persistent.Persistent
    public void flush() throws IOException {
        Lock l = writeLock();
        l.lock();
        try {
            ((MappedByteBuffer) this.hashTablePage.asByteBuffer()).force();
            ((Persistent) this.storageEngine).flush();
            l.unlock();
        } catch (Throwable th) {
            l.unlock();
            throw th;
        }
    }

    @Override // org.terracotta.offheapstore.disk.persistent.Persistent
    public void close() throws IOException {
        Lock l = writeLock();
        l.lock();
        try {
            ((MappedPageSource) this.tableSource).close();
            ((Persistent) this.storageEngine).close();
            l.unlock();
        } catch (Throwable th) {
            l.unlock();
            throw th;
        }
    }

    @Override // org.terracotta.offheapstore.disk.persistent.Persistent
    public void persist(ObjectOutput output) throws IOException {
        Lock l = writeLock();
        l.lock();
        try {
            output.writeInt(MAGIC);
            output.writeLong(((MappedPageSource) this.tableSource).getAddress(this.hashTablePage));
            output.writeInt(this.hashTablePage.size());
            output.writeInt(this.reprobeLimit);
            ((Persistent) this.storageEngine).persist(output);
            l.unlock();
        } catch (Throwable th) {
            l.unlock();
            throw th;
        }
    }

    @Override // org.terracotta.offheapstore.disk.persistent.Persistent
    @FindbugsSuppressWarnings({"VO_VOLATILE_INCREMENT"})
    public void bootstrap(ObjectInput input) throws IOException {
        Lock l = writeLock();
        l.lock();
        try {
            if (this.hashtable != null) {
                throw new IllegalStateException();
            }
            if (input.readInt() != MAGIC) {
                throw new IOException("Wrong magic number");
            }
            long tableAddress = input.readLong();
            long tableCapacity = input.readInt();
            this.hashTablePage = ((MappedPageSource) this.tableSource).claimPage(tableAddress, tableCapacity);
            this.hashtable = this.hashTablePage.asIntBuffer();
            this.reprobeLimit = input.readInt();
            this.hashtable.clear();
            while (this.hashtable.hasRemaining()) {
                IntBuffer entry = (IntBuffer) this.hashtable.slice().limit(4);
                if (isPresent(entry)) {
                    this.size++;
                    added(entry);
                } else if (isRemoved(entry)) {
                    this.removedSlots++;
                }
                this.hashtable.position(this.hashtable.position() + 4);
            }
            ((Persistent) this.storageEngine).bootstrap(input);
            l.unlock();
        } catch (Throwable th) {
            l.unlock();
            throw th;
        }
    }
}

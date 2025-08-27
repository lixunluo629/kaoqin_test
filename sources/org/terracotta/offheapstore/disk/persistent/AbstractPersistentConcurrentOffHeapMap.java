package org.terracotta.offheapstore.disk.persistent;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import org.terracotta.offheapstore.Segment;
import org.terracotta.offheapstore.concurrent.AbstractConcurrentOffHeapMap;
import org.terracotta.offheapstore.util.Factory;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/disk/persistent/AbstractPersistentConcurrentOffHeapMap.class */
public abstract class AbstractPersistentConcurrentOffHeapMap<K, V> extends AbstractConcurrentOffHeapMap<K, V> implements Persistent {
    private static final int MAGIC = 1463898952;

    public AbstractPersistentConcurrentOffHeapMap(Factory<? extends Segment<K, V>> segmentFactory) {
        super(segmentFactory);
    }

    public AbstractPersistentConcurrentOffHeapMap(Factory<? extends Segment<K, V>> segmentFactory, int concurrency) {
        super(segmentFactory, concurrency);
    }

    @Override // org.terracotta.offheapstore.disk.persistent.Persistent
    public void flush() throws IOException {
        Segment[] arr$ = this.segments;
        for (Segment segment : arr$) {
            ((Persistent) segment).flush();
        }
    }

    @Override // org.terracotta.offheapstore.disk.persistent.Persistent
    public void persist(ObjectOutput output) throws IOException {
        output.writeInt(MAGIC);
        output.writeInt(this.segments.length);
        Segment[] arr$ = this.segments;
        for (Segment segment : arr$) {
            ((Persistent) segment).persist(output);
        }
    }

    @Override // org.terracotta.offheapstore.disk.persistent.Persistent
    public void close() throws IOException {
        Segment[] arr$ = this.segments;
        for (Segment segment : arr$) {
            ((Persistent) segment).close();
        }
    }

    @Override // org.terracotta.offheapstore.disk.persistent.Persistent
    public void bootstrap(ObjectInput input) throws IOException {
        Segment[] arr$ = this.segments;
        for (Segment segment : arr$) {
            ((Persistent) segment).bootstrap(input);
        }
    }

    protected static int readSegmentCount(ObjectInput input) throws IOException {
        if (input.readInt() != MAGIC) {
            throw new IOException("Wrong magic number");
        }
        return input.readInt();
    }
}

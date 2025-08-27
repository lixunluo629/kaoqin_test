package org.ehcache.impl.internal.store.disk;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import org.ehcache.core.spi.service.ExecutionService;
import org.terracotta.offheapstore.util.Factory;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/disk/DiskWriteThreadPool.class */
public class DiskWriteThreadPool implements Factory<ExecutorService> {
    private final ExecutionService executionService;
    private final String poolAlias;
    private final int threads;
    private final List<ExecutorService> writers = new CopyOnWriteArrayList();
    private int index = 0;

    public DiskWriteThreadPool(ExecutionService executionService, String poolAlias, int threads) {
        this.executionService = executionService;
        this.poolAlias = poolAlias;
        this.threads = threads;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.terracotta.offheapstore.util.Factory
    public ExecutorService newInstance() throws IllegalArgumentException {
        ExecutorService writer;
        if (this.writers.size() < this.threads) {
            writer = this.executionService.getOrderedExecutor(this.poolAlias, new LinkedBlockingQueue());
            this.writers.add(writer);
        } else {
            List<ExecutorService> list = this.writers;
            int i = this.index;
            this.index = i + 1;
            writer = list.get(i);
            if (this.index == this.writers.size()) {
                this.index = 0;
            }
        }
        return writer;
    }
}

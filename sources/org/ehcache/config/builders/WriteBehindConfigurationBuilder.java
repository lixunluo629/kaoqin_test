package org.ehcache.config.builders;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.concurrent.TimeUnit;
import org.ehcache.config.Builder;
import org.ehcache.impl.config.loaderwriter.writebehind.DefaultBatchingConfiguration;
import org.ehcache.impl.config.loaderwriter.writebehind.DefaultWriteBehindConfiguration;
import org.ehcache.spi.loaderwriter.WriteBehindConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/config/builders/WriteBehindConfigurationBuilder.class */
public abstract class WriteBehindConfigurationBuilder implements Builder<WriteBehindConfiguration> {
    protected int concurrency;
    protected int queueSize;
    protected String threadPoolAlias;

    public abstract WriteBehindConfigurationBuilder queueSize(int i);

    public abstract WriteBehindConfigurationBuilder concurrencyLevel(int i);

    public abstract WriteBehindConfigurationBuilder useThreadPool(String str);

    private WriteBehindConfigurationBuilder() {
        this.concurrency = 1;
        this.queueSize = Integer.MAX_VALUE;
        this.threadPoolAlias = null;
    }

    private WriteBehindConfigurationBuilder(WriteBehindConfigurationBuilder other) {
        this.concurrency = 1;
        this.queueSize = Integer.MAX_VALUE;
        this.threadPoolAlias = null;
        this.concurrency = other.concurrency;
        this.queueSize = other.queueSize;
        this.threadPoolAlias = other.threadPoolAlias;
    }

    public static BatchedWriteBehindConfigurationBuilder newBatchedWriteBehindConfiguration(long maxDelay, TimeUnit maxDelayUnit, int batchSize) {
        return new BatchedWriteBehindConfigurationBuilder(maxDelay, maxDelayUnit, batchSize);
    }

    public static UnBatchedWriteBehindConfigurationBuilder newUnBatchedWriteBehindConfiguration() {
        return new UnBatchedWriteBehindConfigurationBuilder();
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/config/builders/WriteBehindConfigurationBuilder$BatchedWriteBehindConfigurationBuilder.class */
    public static final class BatchedWriteBehindConfigurationBuilder extends WriteBehindConfigurationBuilder {
        private TimeUnit maxDelayUnit;
        private long maxDelay;
        private int batchSize;
        private boolean coalescing;

        private BatchedWriteBehindConfigurationBuilder(long maxDelay, TimeUnit maxDelayUnit, int batchSize) throws IllegalArgumentException {
            super();
            this.coalescing = false;
            setMaxWriteDelay(maxDelay, maxDelayUnit);
            setBatchSize(batchSize);
        }

        private BatchedWriteBehindConfigurationBuilder(BatchedWriteBehindConfigurationBuilder other) {
            super();
            this.coalescing = false;
            this.maxDelay = other.maxDelay;
            this.maxDelayUnit = other.maxDelayUnit;
            this.coalescing = other.coalescing;
            this.batchSize = other.batchSize;
        }

        public BatchedWriteBehindConfigurationBuilder enableCoalescing() {
            BatchedWriteBehindConfigurationBuilder otherBuilder = new BatchedWriteBehindConfigurationBuilder(this);
            otherBuilder.coalescing = true;
            return otherBuilder;
        }

        public BatchedWriteBehindConfigurationBuilder disableCoalescing() {
            BatchedWriteBehindConfigurationBuilder otherBuilder = new BatchedWriteBehindConfigurationBuilder(this);
            otherBuilder.coalescing = false;
            return otherBuilder;
        }

        public BatchedWriteBehindConfigurationBuilder batchSize(int batchSize) throws IllegalArgumentException {
            BatchedWriteBehindConfigurationBuilder otherBuilder = new BatchedWriteBehindConfigurationBuilder(this);
            otherBuilder.setBatchSize(batchSize);
            return otherBuilder;
        }

        private void setBatchSize(int batchSize) throws IllegalArgumentException {
            if (batchSize < 1) {
                throw new IllegalArgumentException("Batch size must be a positive integer, was: " + batchSize);
            }
            this.batchSize = batchSize;
        }

        public BatchedWriteBehindConfigurationBuilder maxWriteDelay(long maxDelay, TimeUnit maxDelayUnit) throws IllegalArgumentException {
            BatchedWriteBehindConfigurationBuilder otherBuilder = new BatchedWriteBehindConfigurationBuilder(this);
            otherBuilder.setMaxWriteDelay(maxDelay, maxDelayUnit);
            return otherBuilder;
        }

        private void setMaxWriteDelay(long maxDelay, TimeUnit maxDelayUnit) throws IllegalArgumentException {
            if (maxDelay < 1) {
                throw new IllegalArgumentException("Max batch delay must be positive, was: " + maxDelay + SymbolConstants.SPACE_SYMBOL + maxDelayUnit);
            }
            this.maxDelay = maxDelay;
            this.maxDelayUnit = maxDelayUnit;
        }

        @Override // org.ehcache.config.builders.WriteBehindConfigurationBuilder
        public BatchedWriteBehindConfigurationBuilder queueSize(int size) {
            if (size < 1) {
                throw new IllegalArgumentException("Queue size must be positive, was: " + size);
            }
            BatchedWriteBehindConfigurationBuilder otherBuilder = new BatchedWriteBehindConfigurationBuilder(this);
            otherBuilder.queueSize = size;
            return otherBuilder;
        }

        @Override // org.ehcache.config.builders.WriteBehindConfigurationBuilder
        public BatchedWriteBehindConfigurationBuilder concurrencyLevel(int concurrency) {
            if (concurrency < 1) {
                throw new IllegalArgumentException("Concurrency must be positive, was: " + concurrency);
            }
            BatchedWriteBehindConfigurationBuilder otherBuilder = new BatchedWriteBehindConfigurationBuilder(this);
            otherBuilder.concurrency = concurrency;
            return otherBuilder;
        }

        @Override // org.ehcache.config.builders.WriteBehindConfigurationBuilder
        public BatchedWriteBehindConfigurationBuilder useThreadPool(String alias) {
            BatchedWriteBehindConfigurationBuilder otherBuilder = new BatchedWriteBehindConfigurationBuilder(this);
            otherBuilder.threadPoolAlias = alias;
            return otherBuilder;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // org.ehcache.config.Builder
        public WriteBehindConfiguration build() {
            return buildWith(new DefaultBatchingConfiguration(this.maxDelay, this.maxDelayUnit, this.batchSize, this.coalescing));
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/config/builders/WriteBehindConfigurationBuilder$UnBatchedWriteBehindConfigurationBuilder.class */
    public static class UnBatchedWriteBehindConfigurationBuilder extends WriteBehindConfigurationBuilder {
        private UnBatchedWriteBehindConfigurationBuilder() {
            super();
        }

        private UnBatchedWriteBehindConfigurationBuilder(UnBatchedWriteBehindConfigurationBuilder other) {
            super();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // org.ehcache.config.Builder
        public WriteBehindConfiguration build() {
            return buildWith(null);
        }

        @Override // org.ehcache.config.builders.WriteBehindConfigurationBuilder
        public UnBatchedWriteBehindConfigurationBuilder queueSize(int size) {
            if (size < 1) {
                throw new IllegalArgumentException("Queue size must be positive, was: " + size);
            }
            UnBatchedWriteBehindConfigurationBuilder otherBuilder = new UnBatchedWriteBehindConfigurationBuilder(this);
            otherBuilder.queueSize = size;
            return otherBuilder;
        }

        @Override // org.ehcache.config.builders.WriteBehindConfigurationBuilder
        public UnBatchedWriteBehindConfigurationBuilder concurrencyLevel(int concurrency) {
            if (concurrency < 1) {
                throw new IllegalArgumentException("Concurrency must be positive, was: " + concurrency);
            }
            UnBatchedWriteBehindConfigurationBuilder otherBuilder = new UnBatchedWriteBehindConfigurationBuilder(this);
            otherBuilder.concurrency = concurrency;
            return otherBuilder;
        }

        @Override // org.ehcache.config.builders.WriteBehindConfigurationBuilder
        public UnBatchedWriteBehindConfigurationBuilder useThreadPool(String alias) {
            UnBatchedWriteBehindConfigurationBuilder otherBuilder = new UnBatchedWriteBehindConfigurationBuilder(this);
            otherBuilder.threadPoolAlias = alias;
            return otherBuilder;
        }
    }

    WriteBehindConfiguration buildWith(WriteBehindConfiguration.BatchingConfiguration batching) {
        return new DefaultWriteBehindConfiguration(this.threadPoolAlias, this.concurrency, this.queueSize, batching);
    }
}

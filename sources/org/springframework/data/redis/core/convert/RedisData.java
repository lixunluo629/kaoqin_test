package org.springframework.data.redis.core.convert;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.util.Assert;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/RedisData.class */
public class RedisData {
    private String keyspace;
    private String id;
    private Bucket bucket;
    private Set<IndexedData> indexedData;
    private Long timeToLive;

    public RedisData() {
        this((Map<byte[], byte[]>) Collections.emptyMap());
    }

    public RedisData(Map<byte[], byte[]> raw) {
        this(Bucket.newBucketFromRawMap(raw));
    }

    public RedisData(Bucket bucket) {
        Assert.notNull(bucket, "Bucket must not be null!");
        this.bucket = bucket;
        this.indexedData = new HashSet();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public Long getTimeToLive() {
        return this.timeToLive;
    }

    public void addIndexedData(IndexedData index) {
        Assert.notNull(index, "IndexedData to add must not be null!");
        this.indexedData.add(index);
    }

    public void addIndexedData(Collection<IndexedData> indexes) {
        Assert.notNull(indexes, "IndexedData to add must not be null!");
        this.indexedData.addAll(indexes);
    }

    public Set<IndexedData> getIndexedData() {
        return Collections.unmodifiableSet(this.indexedData);
    }

    public String getKeyspace() {
        return this.keyspace;
    }

    public void setKeyspace(String keyspace) {
        this.keyspace = keyspace;
    }

    public Bucket getBucket() {
        return this.bucket;
    }

    public void setTimeToLive(Long timeToLive) {
        this.timeToLive = timeToLive;
    }

    public void setTimeToLive(Long timeToLive, TimeUnit timeUnit) {
        Assert.notNull(timeToLive, "TimeToLive must not be null when used with TimeUnit!");
        Assert.notNull(timeToLive, "TimeUnit must not be null!");
        setTimeToLive(Long.valueOf(TimeUnit.SECONDS.convert(timeToLive.longValue(), timeUnit)));
    }

    public String toString() {
        return "RedisDataObject [key=" + this.keyspace + ":" + this.id + ", hash=" + this.bucket + "]";
    }
}

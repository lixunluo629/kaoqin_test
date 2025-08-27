package org.springframework.data.redis.core;

import java.util.Set;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.convert.GeoIndexedPropertyValue;
import org.springframework.data.redis.core.convert.IndexedData;
import org.springframework.data.redis.core.convert.RedisConverter;
import org.springframework.data.redis.core.convert.RemoveIndexedData;
import org.springframework.data.redis.core.convert.SimpleIndexedPropertyValue;
import org.springframework.data.redis.util.ByteUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/IndexWriter.class */
class IndexWriter {
    private final RedisConnection connection;
    private final RedisConverter converter;

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/IndexWriter$IndexWriteMode.class */
    private enum IndexWriteMode {
        CREATE,
        UPDATE,
        PARTIAL_UPDATE
    }

    public IndexWriter(RedisConnection connection, RedisConverter converter) {
        Assert.notNull(connection, "RedisConnection cannot be null!");
        Assert.notNull(converter, "RedisConverter cannot be null!");
        this.connection = connection;
        this.converter = converter;
    }

    public void createIndexes(Object key, Iterable<IndexedData> indexValues) {
        createOrUpdateIndexes(key, indexValues, IndexWriteMode.CREATE);
    }

    public void updateIndexes(Object key, Iterable<IndexedData> indexValues) {
        createOrUpdateIndexes(key, indexValues, IndexWriteMode.PARTIAL_UPDATE);
    }

    public void deleteAndUpdateIndexes(Object key, Iterable<IndexedData> indexValues) {
        createOrUpdateIndexes(key, indexValues, IndexWriteMode.UPDATE);
    }

    private void createOrUpdateIndexes(Object key, Iterable<IndexedData> indexValues, IndexWriteMode writeMode) {
        IndexedData data;
        Assert.notNull(key, "Key must not be null!");
        if (indexValues == null) {
            return;
        }
        byte[] binKey = toBytes(key);
        if (ObjectUtils.nullSafeEquals(IndexWriteMode.UPDATE, writeMode)) {
            if (indexValues.iterator().hasNext() && (data = indexValues.iterator().next()) != null && data.getKeyspace() != null) {
                removeKeyFromIndexes(data.getKeyspace(), binKey);
            }
        } else if (ObjectUtils.nullSafeEquals(IndexWriteMode.PARTIAL_UPDATE, writeMode)) {
            removeKeyFromExistingIndexes(binKey, indexValues);
        }
        addKeyToIndexes(binKey, indexValues);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v4, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r1v7, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r2v5, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r2v7, types: [byte[], byte[][]] */
    public void removeKeyFromIndexes(String keyspace, Object key) {
        Assert.notNull(key, "Key must not be null!");
        byte[] binKey = toBytes(key);
        byte[] indexHelperKey = ByteUtils.concatAll(new byte[]{toBytes(keyspace + ":"), binKey, toBytes(":idx")});
        for (byte[] indexKey : this.connection.sMembers(indexHelperKey)) {
            DataType type = this.connection.type(indexKey);
            if (DataType.ZSET.equals(type)) {
                this.connection.zRem(indexKey, new byte[]{binKey});
            } else {
                this.connection.sRem(indexKey, new byte[]{binKey});
            }
        }
        this.connection.del(new byte[]{indexHelperKey});
    }

    public void removeAllIndexes(String keyspace) {
        Set<byte[]> potentialIndex = this.connection.keys(toBytes(keyspace + ":*"));
        if (!potentialIndex.isEmpty()) {
            this.connection.del((byte[][]) potentialIndex.toArray((Object[]) new byte[potentialIndex.size()]));
        }
    }

    private void removeKeyFromExistingIndexes(byte[] key, Iterable<IndexedData> indexValues) {
        for (IndexedData indexData : indexValues) {
            removeKeyFromExistingIndexes(key, indexData);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v7, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r2v9, types: [byte[], byte[][]] */
    protected void removeKeyFromExistingIndexes(byte[] key, IndexedData indexedData) {
        Assert.notNull(indexedData, "IndexedData must not be null!");
        Set<byte[]> existingKeys = this.connection.keys(toBytes(indexedData.getKeyspace() + ":" + indexedData.getIndexName() + ":*"));
        if (!CollectionUtils.isEmpty(existingKeys)) {
            for (byte[] existingKey : existingKeys) {
                if (indexedData instanceof GeoIndexedPropertyValue) {
                    this.connection.geoRemove(existingKey, new byte[]{key});
                } else {
                    this.connection.sRem(existingKey, new byte[]{key});
                }
            }
        }
    }

    private void addKeyToIndexes(byte[] key, Iterable<IndexedData> indexValues) {
        for (IndexedData indexData : indexValues) {
            addKeyToIndex(key, indexData);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v10, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r1v22, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r2v14, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r2v24, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r2v29, types: [byte[], byte[][]] */
    protected void addKeyToIndex(byte[] key, IndexedData indexedData) {
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(indexedData, "IndexedData must not be null!");
        if (indexedData instanceof RemoveIndexedData) {
            return;
        }
        if (indexedData instanceof SimpleIndexedPropertyValue) {
            Object value = ((SimpleIndexedPropertyValue) indexedData).getValue();
            if (value == null) {
                return;
            }
            byte[] indexKey = ByteUtils.concat(toBytes(indexedData.getKeyspace() + ":" + indexedData.getIndexName() + ":"), toBytes(value));
            this.connection.sAdd(indexKey, new byte[]{key});
            this.connection.sAdd(ByteUtils.concatAll(new byte[]{toBytes(indexedData.getKeyspace() + ":"), key, toBytes(":idx")}), new byte[]{indexKey});
            return;
        }
        if (indexedData instanceof GeoIndexedPropertyValue) {
            GeoIndexedPropertyValue geoIndexedData = (GeoIndexedPropertyValue) indexedData;
            if (geoIndexedData.getValue() == null) {
                return;
            }
            byte[] indexKey2 = toBytes(indexedData.getKeyspace() + ":" + indexedData.getIndexName());
            this.connection.geoAdd(indexKey2, geoIndexedData.getPoint(), key);
            this.connection.sAdd(ByteUtils.concatAll(new byte[]{toBytes(indexedData.getKeyspace() + ":"), key, toBytes(":idx")}), new byte[]{indexKey2});
            return;
        }
        throw new IllegalArgumentException(String.format("Cannot write index data for unknown index type %s", indexedData.getClass()));
    }

    private byte[] toBytes(Object source) {
        if (source == null) {
            return new byte[0];
        }
        if (source instanceof byte[]) {
            return (byte[]) source;
        }
        if (this.converter.getConversionService().canConvert(source.getClass(), byte[].class)) {
            return (byte[]) this.converter.getConversionService().convert(source, byte[].class);
        }
        throw new InvalidDataAccessApiUsageException(String.format("Cannot convert %s to binary representation for index key generation. Are you missing a Converter? Did you register a non PathBasedRedisIndexDefinition that might apply to a complex type?", source.getClass()));
    }
}

package org.springframework.data.redis.support.atomic;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.BoundKeyOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.Assert;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/support/atomic/RedisAtomicLong.class */
public class RedisAtomicLong extends Number implements Serializable, BoundKeyOperations<String> {
    private static final long serialVersionUID = 1;
    private volatile String key;
    private ValueOperations<String, Long> operations;
    private RedisOperations<String, Long> generalOps;

    public RedisAtomicLong(String redisCounter, RedisConnectionFactory factory) {
        this(redisCounter, factory, (Long) null);
    }

    public RedisAtomicLong(String redisCounter, RedisConnectionFactory factory, long initialValue) {
        this(redisCounter, factory, Long.valueOf(initialValue));
    }

    private RedisAtomicLong(String redisCounter, RedisConnectionFactory factory, Long initialValue) {
        Assert.hasText(redisCounter, "a valid counter name is required");
        Assert.notNull(factory, "a valid factory is required");
        RedisTemplate<String, Long> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Long.class));
        redisTemplate.setExposeConnection(true);
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.afterPropertiesSet();
        this.key = redisCounter;
        this.generalOps = redisTemplate;
        this.operations = this.generalOps.opsForValue();
        if (initialValue == null) {
            initializeIfAbsent();
        } else {
            set(initialValue.longValue());
        }
    }

    public RedisAtomicLong(String redisCounter, RedisOperations<String, Long> template) {
        this(redisCounter, template, (Long) null);
    }

    public RedisAtomicLong(String redisCounter, RedisOperations<String, Long> template, long initialValue) {
        this(redisCounter, template, Long.valueOf(initialValue));
    }

    private RedisAtomicLong(String redisCounter, RedisOperations<String, Long> template, Long initialValue) {
        Assert.hasText(redisCounter, "a valid counter name is required");
        Assert.notNull(template, "a valid template is required");
        Assert.notNull(template.getKeySerializer(), "a valid key serializer in template is required");
        Assert.notNull(template.getValueSerializer(), "a valid value serializer in template is required");
        this.key = redisCounter;
        this.generalOps = template;
        this.operations = this.generalOps.opsForValue();
        if (initialValue == null) {
            initializeIfAbsent();
        } else {
            set(initialValue.longValue());
        }
    }

    private void initializeIfAbsent() {
        this.operations.setIfAbsent(this.key, 0L);
    }

    public long get() {
        Long value = this.operations.get(this.key);
        if (value != null) {
            return value.longValue();
        }
        throw new DataRetrievalFailureException(String.format("The key '%s' seems to no longer exist.", this.key));
    }

    public void set(long newValue) {
        this.operations.set(this.key, Long.valueOf(newValue));
    }

    public long getAndSet(long newValue) {
        Long value = this.operations.getAndSet(this.key, Long.valueOf(newValue));
        if (value != null) {
            return value.longValue();
        }
        return 0L;
    }

    public boolean compareAndSet(final long expect, final long update) {
        return ((Boolean) this.generalOps.execute(new SessionCallback<Boolean>() { // from class: org.springframework.data.redis.support.atomic.RedisAtomicLong.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.SessionCallback
            public Boolean execute(RedisOperations operations) {
                operations.watch((Collection) Collections.singleton(RedisAtomicLong.this.key));
                if (expect == RedisAtomicLong.this.get()) {
                    RedisAtomicLong.this.generalOps.multi();
                    RedisAtomicLong.this.set(update);
                    if (operations.exec() != null) {
                        return true;
                    }
                }
                return false;
            }
        })).booleanValue();
    }

    public long getAndIncrement() {
        return incrementAndGet() - serialVersionUID;
    }

    public long getAndDecrement() {
        return decrementAndGet() + serialVersionUID;
    }

    public long getAndAdd(long delta) {
        return addAndGet(delta) - delta;
    }

    public long incrementAndGet() {
        return this.operations.increment((ValueOperations<String, Long>) this.key, serialVersionUID).longValue();
    }

    public long decrementAndGet() {
        return this.operations.increment((ValueOperations<String, Long>) this.key, -1L).longValue();
    }

    public long addAndGet(long delta) {
        return this.operations.increment((ValueOperations<String, Long>) this.key, delta).longValue();
    }

    public String toString() {
        return Long.toString(get());
    }

    @Override // java.lang.Number
    public int intValue() {
        return (int) get();
    }

    @Override // java.lang.Number
    public long longValue() {
        return get();
    }

    @Override // java.lang.Number
    public float floatValue() {
        return get();
    }

    @Override // java.lang.Number
    public double doubleValue() {
        return get();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.data.redis.core.BoundKeyOperations
    public String getKey() {
        return this.key;
    }

    @Override // org.springframework.data.redis.core.BoundKeyOperations
    public Boolean expire(long timeout, TimeUnit unit) {
        return this.generalOps.expire(this.key, timeout, unit);
    }

    @Override // org.springframework.data.redis.core.BoundKeyOperations
    public Boolean expireAt(Date date) {
        return this.generalOps.expireAt(this.key, date);
    }

    @Override // org.springframework.data.redis.core.BoundKeyOperations
    public Long getExpire() {
        return this.generalOps.getExpire(this.key);
    }

    @Override // org.springframework.data.redis.core.BoundKeyOperations
    public Boolean persist() {
        return this.generalOps.persist(this.key);
    }

    @Override // org.springframework.data.redis.core.BoundKeyOperations
    public void rename(String newKey) {
        this.generalOps.rename(this.key, newKey);
        this.key = newKey;
    }

    @Override // org.springframework.data.redis.core.BoundKeyOperations
    public DataType getType() {
        return DataType.STRING;
    }
}

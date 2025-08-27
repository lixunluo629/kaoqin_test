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

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/support/atomic/RedisAtomicInteger.class */
public class RedisAtomicInteger extends Number implements Serializable, BoundKeyOperations<String> {
    private static final long serialVersionUID = 1;
    private volatile String key;
    private ValueOperations<String, Integer> operations;
    private RedisOperations<String, Integer> generalOps;

    public RedisAtomicInteger(String redisCounter, RedisConnectionFactory factory) {
        this(redisCounter, factory, (Integer) null);
    }

    public RedisAtomicInteger(String redisCounter, RedisConnectionFactory factory, int initialValue) {
        this(redisCounter, factory, Integer.valueOf(initialValue));
    }

    public RedisAtomicInteger(String redisCounter, RedisOperations<String, Integer> template) {
        this(redisCounter, template, (Integer) null);
    }

    public RedisAtomicInteger(String redisCounter, RedisOperations<String, Integer> template, int initialValue) {
        this(redisCounter, template, Integer.valueOf(initialValue));
    }

    private RedisAtomicInteger(String redisCounter, RedisConnectionFactory factory, Integer initialValue) {
        RedisTemplate<String, Integer> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Integer.class));
        redisTemplate.setExposeConnection(true);
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.afterPropertiesSet();
        this.key = redisCounter;
        this.generalOps = redisTemplate;
        this.operations = this.generalOps.opsForValue();
        if (initialValue == null) {
            initializeIfAbsent();
        } else {
            set(initialValue.intValue());
        }
    }

    private RedisAtomicInteger(String redisCounter, RedisOperations<String, Integer> template, Integer initialValue) {
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
            set(initialValue.intValue());
        }
    }

    private void initializeIfAbsent() {
        this.operations.setIfAbsent(this.key, 0);
    }

    public int get() {
        Integer value = this.operations.get(this.key);
        if (value != null) {
            return value.intValue();
        }
        throw new DataRetrievalFailureException(String.format("The key '%s' seems to no longer exist.", this.key));
    }

    public void set(int newValue) {
        this.operations.set(this.key, Integer.valueOf(newValue));
    }

    public int getAndSet(int newValue) {
        Integer value = this.operations.getAndSet(this.key, Integer.valueOf(newValue));
        if (value != null) {
            return value.intValue();
        }
        return 0;
    }

    public boolean compareAndSet(final int expect, final int update) {
        return ((Boolean) this.generalOps.execute(new SessionCallback<Boolean>() { // from class: org.springframework.data.redis.support.atomic.RedisAtomicInteger.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.SessionCallback
            public Boolean execute(RedisOperations operations) {
                operations.watch((Collection) Collections.singleton(RedisAtomicInteger.this.key));
                if (expect == RedisAtomicInteger.this.get()) {
                    RedisAtomicInteger.this.generalOps.multi();
                    RedisAtomicInteger.this.set(update);
                    if (operations.exec() != null) {
                        return true;
                    }
                }
                return false;
            }
        })).booleanValue();
    }

    public int getAndIncrement() {
        return incrementAndGet() - 1;
    }

    public int getAndDecrement() {
        return decrementAndGet() + 1;
    }

    public int getAndAdd(int delta) {
        return addAndGet(delta) - delta;
    }

    public int incrementAndGet() {
        return this.operations.increment((ValueOperations<String, Integer>) this.key, serialVersionUID).intValue();
    }

    public int decrementAndGet() {
        return this.operations.increment((ValueOperations<String, Integer>) this.key, -1L).intValue();
    }

    public int addAndGet(int delta) {
        return this.operations.increment((ValueOperations<String, Integer>) this.key, delta).intValue();
    }

    public String toString() {
        return Integer.toString(get());
    }

    @Override // java.lang.Number
    public int intValue() {
        return get();
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

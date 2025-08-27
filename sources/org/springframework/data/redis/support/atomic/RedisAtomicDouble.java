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

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/support/atomic/RedisAtomicDouble.class */
public class RedisAtomicDouble extends Number implements Serializable, BoundKeyOperations<String> {
    private static final long serialVersionUID = 1;
    private volatile String key;
    private ValueOperations<String, Double> operations;
    private RedisOperations<String, Double> generalOps;

    public RedisAtomicDouble(String redisCounter, RedisConnectionFactory factory) {
        this(redisCounter, factory, (Double) null);
    }

    public RedisAtomicDouble(String redisCounter, RedisConnectionFactory factory, double initialValue) {
        this(redisCounter, factory, Double.valueOf(initialValue));
    }

    private RedisAtomicDouble(String redisCounter, RedisConnectionFactory factory, Double initialValue) {
        Assert.hasText(redisCounter, "a valid counter name is required");
        Assert.notNull(factory, "a valid factory is required");
        RedisTemplate<String, Double> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Double.class));
        redisTemplate.setExposeConnection(true);
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.afterPropertiesSet();
        this.key = redisCounter;
        this.generalOps = redisTemplate;
        this.operations = this.generalOps.opsForValue();
        if (initialValue == null) {
            initializeIfAbsent();
        } else {
            set(initialValue.doubleValue());
        }
    }

    public RedisAtomicDouble(String redisCounter, RedisOperations<String, Double> template) {
        this(redisCounter, template, (Double) null);
    }

    public RedisAtomicDouble(String redisCounter, RedisOperations<String, Double> template, double initialValue) {
        this(redisCounter, template, Double.valueOf(initialValue));
    }

    private RedisAtomicDouble(String redisCounter, RedisOperations<String, Double> template, Double initialValue) {
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
            set(initialValue.doubleValue());
        }
    }

    private void initializeIfAbsent() {
        this.operations.setIfAbsent(this.key, Double.valueOf(0.0d));
    }

    public double get() {
        Double value = this.operations.get(this.key);
        if (value != null) {
            return value.doubleValue();
        }
        throw new DataRetrievalFailureException(String.format("The key '%s' seems to no longer exist.", this.key));
    }

    public void set(double newValue) {
        this.operations.set(this.key, Double.valueOf(newValue));
    }

    public double getAndSet(double newValue) {
        Double value = this.operations.getAndSet(this.key, Double.valueOf(newValue));
        if (value != null) {
            return value.doubleValue();
        }
        return 0.0d;
    }

    public boolean compareAndSet(final double expect, final double update) {
        return ((Boolean) this.generalOps.execute(new SessionCallback<Boolean>() { // from class: org.springframework.data.redis.support.atomic.RedisAtomicDouble.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.SessionCallback
            public Boolean execute(RedisOperations operations) {
                operations.watch((Collection) Collections.singleton(RedisAtomicDouble.this.key));
                if (expect == RedisAtomicDouble.this.get()) {
                    RedisAtomicDouble.this.generalOps.multi();
                    RedisAtomicDouble.this.set(update);
                    if (operations.exec() != null) {
                        return true;
                    }
                }
                return false;
            }
        })).booleanValue();
    }

    public double getAndIncrement() {
        return incrementAndGet() - 1.0d;
    }

    public double getAndDecrement() {
        return decrementAndGet() + 1.0d;
    }

    public double getAndAdd(double delta) {
        return addAndGet(delta) - delta;
    }

    public double incrementAndGet() {
        return this.operations.increment((ValueOperations<String, Double>) this.key, 1.0d).doubleValue();
    }

    public double decrementAndGet() {
        return this.operations.increment((ValueOperations<String, Double>) this.key, -1.0d).doubleValue();
    }

    public double addAndGet(double delta) {
        return this.operations.increment((ValueOperations<String, Double>) this.key, delta).doubleValue();
    }

    public String toString() {
        return Double.toString(get());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.data.redis.core.BoundKeyOperations
    public String getKey() {
        return this.key;
    }

    @Override // org.springframework.data.redis.core.BoundKeyOperations
    public DataType getType() {
        return DataType.STRING;
    }

    @Override // org.springframework.data.redis.core.BoundKeyOperations
    public Long getExpire() {
        return this.generalOps.getExpire(this.key);
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
    public Boolean persist() {
        return this.generalOps.persist(this.key);
    }

    @Override // org.springframework.data.redis.core.BoundKeyOperations
    public void rename(String newKey) {
        this.generalOps.rename(this.key, newKey);
        this.key = newKey;
    }

    @Override // java.lang.Number
    public double doubleValue() {
        return get();
    }

    @Override // java.lang.Number
    public float floatValue() {
        return (float) get();
    }

    @Override // java.lang.Number
    public int intValue() {
        return (int) get();
    }

    @Override // java.lang.Number
    public long longValue() {
        return (long) get();
    }
}

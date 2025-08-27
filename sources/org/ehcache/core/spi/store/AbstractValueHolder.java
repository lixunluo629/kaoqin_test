package org.ehcache.core.spi.store;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import org.ehcache.core.spi.store.Store;
import org.ehcache.expiry.Duration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/spi/store/AbstractValueHolder.class */
public abstract class AbstractValueHolder<V> implements Store.ValueHolder<V> {
    private final long id;
    private final long creationTime;
    private volatile long lastAccessTime;
    private volatile long expirationTime;
    private volatile long hits;
    private static final AtomicLongFieldUpdater<AbstractValueHolder> HITS_UPDATER = AtomicLongFieldUpdater.newUpdater(AbstractValueHolder.class, "hits");
    private static final AtomicLongFieldUpdater<AbstractValueHolder> ACCESSTIME_UPDATER = AtomicLongFieldUpdater.newUpdater(AbstractValueHolder.class, "lastAccessTime");
    private static final AtomicLongFieldUpdater<AbstractValueHolder> EXPIRATIONTIME_UPDATER = AtomicLongFieldUpdater.newUpdater(AbstractValueHolder.class, "expirationTime");

    protected abstract TimeUnit nativeTimeUnit();

    protected AbstractValueHolder(long id, long creationTime) {
        this(id, creationTime, -1L);
    }

    protected AbstractValueHolder(long id, long creationTime, long expirationTime) {
        this.id = id;
        this.creationTime = creationTime;
        this.expirationTime = expirationTime;
        this.lastAccessTime = creationTime;
    }

    @Override // org.ehcache.core.spi.store.Store.ValueHolder
    public long creationTime(TimeUnit unit) {
        return unit.convert(this.creationTime, nativeTimeUnit());
    }

    public void setExpirationTime(long expirationTime, TimeUnit unit) {
        if (expirationTime == -1) {
            updateExpirationTime(-1L);
        } else {
            if (expirationTime <= 0) {
                throw new IllegalArgumentException("invalid expiration time: " + expirationTime);
            }
            updateExpirationTime(nativeTimeUnit().convert(expirationTime, unit));
        }
    }

    private void updateExpirationTime(long update) {
        long current;
        do {
            current = this.expirationTime;
            if (current >= update) {
                return;
            }
        } while (!EXPIRATIONTIME_UPDATER.compareAndSet(this, current, update));
    }

    public void accessed(long now, Duration expiration) {
        long newExpirationTime;
        TimeUnit timeUnit = nativeTimeUnit();
        if (expiration != null) {
            if (expiration.isInfinite()) {
                setExpirationTime(-1L, null);
            } else {
                long millis = timeUnit.convert(expiration.getLength(), expiration.getTimeUnit());
                if (millis == Long.MAX_VALUE) {
                    newExpirationTime = Long.MAX_VALUE;
                } else {
                    newExpirationTime = now + millis;
                    if (newExpirationTime < 0) {
                        newExpirationTime = Long.MAX_VALUE;
                    }
                }
                setExpirationTime(newExpirationTime, timeUnit);
            }
        }
        setLastAccessTime(now, timeUnit);
        HITS_UPDATER.getAndIncrement(this);
    }

    @Override // org.ehcache.core.spi.store.Store.ValueHolder
    public long expirationTime(TimeUnit unit) {
        long expire = this.expirationTime;
        if (expire == -1) {
            return -1L;
        }
        return unit.convert(expire, nativeTimeUnit());
    }

    @Override // org.ehcache.core.spi.store.Store.ValueHolder
    public boolean isExpired(long expirationTime, TimeUnit unit) {
        long expire = this.expirationTime;
        return expire != -1 && expire <= nativeTimeUnit().convert(expirationTime, unit);
    }

    @Override // org.ehcache.core.spi.store.Store.ValueHolder
    public long lastAccessTime(TimeUnit unit) {
        return unit.convert(this.lastAccessTime, nativeTimeUnit());
    }

    public void setLastAccessTime(long lastAccessTime, TimeUnit unit) {
        long current;
        long update = unit.convert(lastAccessTime, nativeTimeUnit());
        do {
            current = this.lastAccessTime;
            if (current >= update) {
                return;
            }
        } while (!ACCESSTIME_UPDATER.compareAndSet(this, current, update));
    }

    public int hashCode() {
        int result = (31 * 1) + ((int) (this.creationTime ^ (this.creationTime >>> 32)));
        return (31 * ((31 * result) + ((int) (this.lastAccessTime ^ (this.lastAccessTime >>> 32))))) + ((int) (this.expirationTime ^ (this.expirationTime >>> 32)));
    }

    public boolean equals(Object obj) {
        if (obj instanceof AbstractValueHolder) {
            AbstractValueHolder<?> other = (AbstractValueHolder) obj;
            return other.creationTime(nativeTimeUnit()) == this.creationTime && creationTime(other.nativeTimeUnit()) == other.creationTime && other.expirationTime(nativeTimeUnit()) == this.expirationTime && expirationTime(other.nativeTimeUnit()) == other.expirationTime && other.lastAccessTime(nativeTimeUnit()) == this.lastAccessTime && lastAccessTime(other.nativeTimeUnit()) == other.lastAccessTime;
        }
        return false;
    }

    @Override // org.ehcache.core.spi.store.Store.ValueHolder
    public float hitRate(long now, TimeUnit unit) {
        long endTime = TimeUnit.NANOSECONDS.convert(now, TimeUnit.MILLISECONDS);
        long startTime = TimeUnit.NANOSECONDS.convert(this.creationTime, nativeTimeUnit());
        float duration = (endTime - startTime) / TimeUnit.NANOSECONDS.convert(1L, unit);
        return this.hits / duration;
    }

    @Override // org.ehcache.core.spi.store.Store.ValueHolder
    public long hits() {
        return this.hits;
    }

    protected void setHits(long hits) {
        HITS_UPDATER.set(this, hits);
    }

    @Override // org.ehcache.core.spi.store.Store.ValueHolder
    public long getId() {
        return this.id;
    }

    public String toString() {
        return String.format("%s", value());
    }
}

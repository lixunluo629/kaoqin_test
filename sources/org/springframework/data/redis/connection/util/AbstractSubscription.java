package org.springframework.data.redis.connection.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisInvalidSubscriptionException;
import org.springframework.data.redis.connection.Subscription;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/util/AbstractSubscription.class */
public abstract class AbstractSubscription implements Subscription {
    private final Collection<ByteArrayWrapper> channels;
    private final Collection<ByteArrayWrapper> patterns;
    private final AtomicBoolean alive;
    private final MessageListener listener;

    protected abstract void doSubscribe(byte[]... bArr);

    protected abstract void doUnsubscribe(boolean z, byte[]... bArr);

    protected abstract void doPsubscribe(byte[]... bArr);

    protected abstract void doPUnsubscribe(boolean z, byte[]... bArr);

    protected abstract void doClose();

    protected AbstractSubscription(MessageListener listener) {
        this(listener, (byte[][]) null, (byte[][]) null);
    }

    protected AbstractSubscription(MessageListener listener, byte[][] channels, byte[][] patterns) {
        this.channels = new ArrayList(2);
        this.patterns = new ArrayList(2);
        this.alive = new AtomicBoolean(true);
        Assert.notNull(listener, "MessageListener must not be null!");
        this.listener = listener;
        synchronized (this.channels) {
            add(this.channels, channels);
        }
        synchronized (this.patterns) {
            add(this.patterns, patterns);
        }
    }

    @Override // org.springframework.data.redis.connection.Subscription
    public void close() {
        doClose();
    }

    @Override // org.springframework.data.redis.connection.Subscription
    public MessageListener getListener() {
        return this.listener;
    }

    @Override // org.springframework.data.redis.connection.Subscription
    public Collection<byte[]> getChannels() {
        Collection<byte[]> collectionClone;
        synchronized (this.channels) {
            collectionClone = clone(this.channels);
        }
        return collectionClone;
    }

    @Override // org.springframework.data.redis.connection.Subscription
    public Collection<byte[]> getPatterns() {
        Collection<byte[]> collectionClone;
        synchronized (this.patterns) {
            collectionClone = clone(this.patterns);
        }
        return collectionClone;
    }

    @Override // org.springframework.data.redis.connection.Subscription
    public void pSubscribe(byte[]... patterns) {
        checkPulse();
        Assert.notEmpty(patterns, "at least one pattern required");
        synchronized (this.patterns) {
            add(this.patterns, patterns);
        }
        doPsubscribe(patterns);
    }

    @Override // org.springframework.data.redis.connection.Subscription
    public void pUnsubscribe() {
        pUnsubscribe((byte[][]) null);
    }

    @Override // org.springframework.data.redis.connection.Subscription
    public void subscribe(byte[]... channels) {
        checkPulse();
        Assert.notEmpty(channels, "at least one channel required");
        synchronized (this.channels) {
            add(this.channels, channels);
        }
        doSubscribe(channels);
    }

    @Override // org.springframework.data.redis.connection.Subscription
    public void unsubscribe() {
        unsubscribe((byte[][]) null);
    }

    @Override // org.springframework.data.redis.connection.Subscription
    public void pUnsubscribe(byte[]... patts) {
        if (!isAlive()) {
            return;
        }
        if (ObjectUtils.isEmpty((Object[]) patts)) {
            if (!this.patterns.isEmpty()) {
                synchronized (this.patterns) {
                    doPUnsubscribe(true, (byte[][]) getPatterns().toArray((Object[]) new byte[this.patterns.size()]));
                    this.patterns.clear();
                }
            } else {
                return;
            }
        } else {
            doPUnsubscribe(false, patts);
            synchronized (this.patterns) {
                remove(this.patterns, patts);
            }
        }
        closeIfUnsubscribed();
    }

    @Override // org.springframework.data.redis.connection.Subscription
    public void unsubscribe(byte[]... chans) {
        if (!isAlive()) {
            return;
        }
        if (ObjectUtils.isEmpty((Object[]) chans)) {
            if (!this.channels.isEmpty()) {
                synchronized (this.channels) {
                    doUnsubscribe(true, (byte[][]) getChannels().toArray((Object[]) new byte[this.channels.size()]));
                    this.channels.clear();
                }
            } else {
                return;
            }
        } else {
            doUnsubscribe(false, chans);
            synchronized (this.channels) {
                remove(this.channels, chans);
            }
        }
        closeIfUnsubscribed();
    }

    @Override // org.springframework.data.redis.connection.Subscription
    public boolean isAlive() {
        return this.alive.get();
    }

    private void checkPulse() {
        if (!isAlive()) {
            throw new RedisInvalidSubscriptionException("Subscription has been unsubscribed and cannot be used anymore");
        }
    }

    private void closeIfUnsubscribed() {
        if (this.channels.isEmpty() && this.patterns.isEmpty()) {
            this.alive.set(false);
            doClose();
        }
    }

    private static Collection<byte[]> clone(Collection<ByteArrayWrapper> col) {
        ArrayList arrayList = new ArrayList(col.size());
        for (ByteArrayWrapper wrapper : col) {
            arrayList.add(wrapper.getArray().clone());
        }
        return arrayList;
    }

    private static void add(Collection<ByteArrayWrapper> col, byte[]... bytes) {
        if (!ObjectUtils.isEmpty((Object[]) bytes)) {
            for (byte[] bs : bytes) {
                col.add(new ByteArrayWrapper(bs));
            }
        }
    }

    private static void remove(Collection<ByteArrayWrapper> col, byte[]... bytes) {
        if (!ObjectUtils.isEmpty((Object[]) bytes)) {
            for (byte[] bs : bytes) {
                col.remove(new ByteArrayWrapper(bs));
            }
        }
    }
}

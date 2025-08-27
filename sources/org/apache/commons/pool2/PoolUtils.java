package org.apache.commons.pool2;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/PoolUtils.class */
public final class PoolUtils {

    /* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/PoolUtils$TimerHolder.class */
    static class TimerHolder {
        static final Timer MIN_IDLE_TIMER = new Timer(true);

        TimerHolder() {
        }
    }

    public static void checkRethrow(Throwable t) {
        if (t instanceof ThreadDeath) {
            throw ((ThreadDeath) t);
        }
        if (t instanceof VirtualMachineError) {
            throw ((VirtualMachineError) t);
        }
    }

    public static <T> TimerTask checkMinIdle(ObjectPool<T> pool, int minIdle, long period) throws IllegalArgumentException {
        if (pool == null) {
            throw new IllegalArgumentException("keyedPool must not be null.");
        }
        if (minIdle < 0) {
            throw new IllegalArgumentException("minIdle must be non-negative.");
        }
        TimerTask task = new ObjectPoolMinIdleTimerTask(pool, minIdle);
        getMinIdleTimer().schedule(task, 0L, period);
        return task;
    }

    public static <K, V> TimerTask checkMinIdle(KeyedObjectPool<K, V> keyedPool, K key, int minIdle, long period) throws IllegalArgumentException {
        if (keyedPool == null) {
            throw new IllegalArgumentException("keyedPool must not be null.");
        }
        if (key == null) {
            throw new IllegalArgumentException("key must not be null.");
        }
        if (minIdle < 0) {
            throw new IllegalArgumentException("minIdle must be non-negative.");
        }
        TimerTask task = new KeyedObjectPoolMinIdleTimerTask(keyedPool, key, minIdle);
        getMinIdleTimer().schedule(task, 0L, period);
        return task;
    }

    public static <K, V> Map<K, TimerTask> checkMinIdle(KeyedObjectPool<K, V> keyedPool, Collection<K> keys, int minIdle, long period) throws IllegalArgumentException {
        if (keys == null) {
            throw new IllegalArgumentException("keys must not be null.");
        }
        Map<K, TimerTask> tasks = new HashMap<>(keys.size());
        for (K key : keys) {
            TimerTask task = checkMinIdle(keyedPool, key, minIdle, period);
            tasks.put(key, task);
        }
        return tasks;
    }

    public static <T> void prefill(ObjectPool<T> pool, int count) throws Exception {
        if (pool == null) {
            throw new IllegalArgumentException("pool must not be null.");
        }
        for (int i = 0; i < count; i++) {
            pool.addObject();
        }
    }

    public static <K, V> void prefill(KeyedObjectPool<K, V> keyedPool, K key, int count) throws Exception {
        if (keyedPool == null) {
            throw new IllegalArgumentException("keyedPool must not be null.");
        }
        if (key == null) {
            throw new IllegalArgumentException("key must not be null.");
        }
        for (int i = 0; i < count; i++) {
            keyedPool.addObject(key);
        }
    }

    public static <K, V> void prefill(KeyedObjectPool<K, V> keyedPool, Collection<K> keys, int count) throws Exception {
        if (keys == null) {
            throw new IllegalArgumentException("keys must not be null.");
        }
        Iterator<K> iter = keys.iterator();
        while (iter.hasNext()) {
            prefill(keyedPool, iter.next(), count);
        }
    }

    public static <T> ObjectPool<T> synchronizedPool(ObjectPool<T> pool) {
        if (pool == null) {
            throw new IllegalArgumentException("pool must not be null.");
        }
        return new SynchronizedObjectPool(pool);
    }

    public static <K, V> KeyedObjectPool<K, V> synchronizedPool(KeyedObjectPool<K, V> keyedPool) {
        return new SynchronizedKeyedObjectPool(keyedPool);
    }

    public static <T> PooledObjectFactory<T> synchronizedPooledFactory(PooledObjectFactory<T> factory) {
        return new SynchronizedPooledObjectFactory(factory);
    }

    public static <K, V> KeyedPooledObjectFactory<K, V> synchronizedKeyedPooledFactory(KeyedPooledObjectFactory<K, V> keyedFactory) {
        return new SynchronizedKeyedPooledObjectFactory(keyedFactory);
    }

    public static <T> ObjectPool<T> erodingPool(ObjectPool<T> pool) {
        return erodingPool(pool, 1.0f);
    }

    public static <T> ObjectPool<T> erodingPool(ObjectPool<T> pool, float factor) {
        if (pool == null) {
            throw new IllegalArgumentException("pool must not be null.");
        }
        if (factor <= 0.0f) {
            throw new IllegalArgumentException("factor must be positive.");
        }
        return new ErodingObjectPool(pool, factor);
    }

    public static <K, V> KeyedObjectPool<K, V> erodingPool(KeyedObjectPool<K, V> keyedPool) {
        return erodingPool(keyedPool, 1.0f);
    }

    public static <K, V> KeyedObjectPool<K, V> erodingPool(KeyedObjectPool<K, V> keyedPool, float factor) {
        return erodingPool(keyedPool, factor, false);
    }

    public static <K, V> KeyedObjectPool<K, V> erodingPool(KeyedObjectPool<K, V> keyedPool, float factor, boolean perKey) {
        if (keyedPool == null) {
            throw new IllegalArgumentException("keyedPool must not be null.");
        }
        if (factor <= 0.0f) {
            throw new IllegalArgumentException("factor must be positive.");
        }
        if (perKey) {
            return new ErodingPerKeyKeyedObjectPool(keyedPool, factor);
        }
        return new ErodingKeyedObjectPool(keyedPool, factor);
    }

    private static Timer getMinIdleTimer() {
        return TimerHolder.MIN_IDLE_TIMER;
    }

    /* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/PoolUtils$ObjectPoolMinIdleTimerTask.class */
    private static final class ObjectPoolMinIdleTimerTask<T> extends TimerTask {
        private final int minIdle;
        private final ObjectPool<T> pool;

        ObjectPoolMinIdleTimerTask(ObjectPool<T> pool, int minIdle) throws IllegalArgumentException {
            if (pool == null) {
                throw new IllegalArgumentException("pool must not be null.");
            }
            this.pool = pool;
            this.minIdle = minIdle;
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            boolean success = false;
            try {
                try {
                    if (this.pool.getNumIdle() < this.minIdle) {
                        this.pool.addObject();
                    }
                    success = true;
                } catch (Exception e) {
                    cancel();
                    if (!success) {
                        cancel();
                    }
                }
            } finally {
                if (!success) {
                    cancel();
                }
            }
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("ObjectPoolMinIdleTimerTask");
            sb.append("{minIdle=").append(this.minIdle);
            sb.append(", pool=").append(this.pool);
            sb.append('}');
            return sb.toString();
        }
    }

    /* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/PoolUtils$KeyedObjectPoolMinIdleTimerTask.class */
    private static final class KeyedObjectPoolMinIdleTimerTask<K, V> extends TimerTask {
        private final int minIdle;
        private final K key;
        private final KeyedObjectPool<K, V> keyedPool;

        KeyedObjectPoolMinIdleTimerTask(KeyedObjectPool<K, V> keyedPool, K key, int minIdle) throws IllegalArgumentException {
            if (keyedPool == null) {
                throw new IllegalArgumentException("keyedPool must not be null.");
            }
            this.keyedPool = keyedPool;
            this.key = key;
            this.minIdle = minIdle;
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            try {
                try {
                    if (this.keyedPool.getNumIdle(this.key) < this.minIdle) {
                        this.keyedPool.addObject(this.key);
                    }
                    if (1 == 0) {
                        cancel();
                    }
                } catch (Exception e) {
                    cancel();
                    if (0 == 0) {
                        cancel();
                    }
                }
            } catch (Throwable th) {
                if (0 == 0) {
                    cancel();
                }
                throw th;
            }
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("KeyedObjectPoolMinIdleTimerTask");
            sb.append("{minIdle=").append(this.minIdle);
            sb.append(", key=").append(this.key);
            sb.append(", keyedPool=").append(this.keyedPool);
            sb.append('}');
            return sb.toString();
        }
    }

    /* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/PoolUtils$SynchronizedObjectPool.class */
    private static final class SynchronizedObjectPool<T> implements ObjectPool<T> {
        private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        private final ObjectPool<T> pool;

        SynchronizedObjectPool(ObjectPool<T> pool) throws IllegalArgumentException {
            if (pool == null) {
                throw new IllegalArgumentException("pool must not be null.");
            }
            this.pool = pool;
        }

        @Override // org.apache.commons.pool2.ObjectPool
        public T borrowObject() throws Exception {
            ReentrantReadWriteLock.WriteLock writeLock = this.readWriteLock.writeLock();
            writeLock.lock();
            try {
                return this.pool.borrowObject();
            } finally {
                writeLock.unlock();
            }
        }

        @Override // org.apache.commons.pool2.ObjectPool
        public void returnObject(T obj) {
            ReentrantReadWriteLock.WriteLock writeLock = this.readWriteLock.writeLock();
            writeLock.lock();
            try {
                this.pool.returnObject(obj);
                writeLock.unlock();
            } catch (Exception e) {
                writeLock.unlock();
            } catch (Throwable th) {
                writeLock.unlock();
                throw th;
            }
        }

        @Override // org.apache.commons.pool2.ObjectPool
        public void invalidateObject(T obj) {
            ReentrantReadWriteLock.WriteLock writeLock = this.readWriteLock.writeLock();
            writeLock.lock();
            try {
                this.pool.invalidateObject(obj);
                writeLock.unlock();
            } catch (Exception e) {
                writeLock.unlock();
            } catch (Throwable th) {
                writeLock.unlock();
                throw th;
            }
        }

        @Override // org.apache.commons.pool2.ObjectPool
        public void addObject() throws Exception {
            ReentrantReadWriteLock.WriteLock writeLock = this.readWriteLock.writeLock();
            writeLock.lock();
            try {
                this.pool.addObject();
            } finally {
                writeLock.unlock();
            }
        }

        @Override // org.apache.commons.pool2.ObjectPool
        public int getNumIdle() {
            ReentrantReadWriteLock.ReadLock readLock = this.readWriteLock.readLock();
            readLock.lock();
            try {
                return this.pool.getNumIdle();
            } finally {
                readLock.unlock();
            }
        }

        @Override // org.apache.commons.pool2.ObjectPool
        public int getNumActive() {
            ReentrantReadWriteLock.ReadLock readLock = this.readWriteLock.readLock();
            readLock.lock();
            try {
                return this.pool.getNumActive();
            } finally {
                readLock.unlock();
            }
        }

        @Override // org.apache.commons.pool2.ObjectPool
        public void clear() throws Exception {
            ReentrantReadWriteLock.WriteLock writeLock = this.readWriteLock.writeLock();
            writeLock.lock();
            try {
                this.pool.clear();
            } finally {
                writeLock.unlock();
            }
        }

        @Override // org.apache.commons.pool2.ObjectPool
        public void close() {
            ReentrantReadWriteLock.WriteLock writeLock = this.readWriteLock.writeLock();
            writeLock.lock();
            try {
                this.pool.close();
            } catch (Exception e) {
            } finally {
                writeLock.unlock();
            }
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("SynchronizedObjectPool");
            sb.append("{pool=").append(this.pool);
            sb.append('}');
            return sb.toString();
        }
    }

    /* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/PoolUtils$SynchronizedKeyedObjectPool.class */
    private static final class SynchronizedKeyedObjectPool<K, V> implements KeyedObjectPool<K, V> {
        private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        private final KeyedObjectPool<K, V> keyedPool;

        SynchronizedKeyedObjectPool(KeyedObjectPool<K, V> keyedPool) throws IllegalArgumentException {
            if (keyedPool == null) {
                throw new IllegalArgumentException("keyedPool must not be null.");
            }
            this.keyedPool = keyedPool;
        }

        @Override // org.apache.commons.pool2.KeyedObjectPool
        public V borrowObject(K key) throws Exception {
            ReentrantReadWriteLock.WriteLock writeLock = this.readWriteLock.writeLock();
            writeLock.lock();
            try {
                V vBorrowObject = this.keyedPool.borrowObject(key);
                writeLock.unlock();
                return vBorrowObject;
            } catch (Throwable th) {
                writeLock.unlock();
                throw th;
            }
        }

        @Override // org.apache.commons.pool2.KeyedObjectPool
        public void returnObject(K key, V obj) {
            ReentrantReadWriteLock.WriteLock writeLock = this.readWriteLock.writeLock();
            writeLock.lock();
            try {
                this.keyedPool.returnObject(key, obj);
                writeLock.unlock();
            } catch (Exception e) {
                writeLock.unlock();
            } catch (Throwable th) {
                writeLock.unlock();
                throw th;
            }
        }

        @Override // org.apache.commons.pool2.KeyedObjectPool
        public void invalidateObject(K key, V obj) {
            ReentrantReadWriteLock.WriteLock writeLock = this.readWriteLock.writeLock();
            writeLock.lock();
            try {
                this.keyedPool.invalidateObject(key, obj);
                writeLock.unlock();
            } catch (Exception e) {
                writeLock.unlock();
            } catch (Throwable th) {
                writeLock.unlock();
                throw th;
            }
        }

        @Override // org.apache.commons.pool2.KeyedObjectPool
        public void addObject(K key) throws Exception {
            ReentrantReadWriteLock.WriteLock writeLock = this.readWriteLock.writeLock();
            writeLock.lock();
            try {
                this.keyedPool.addObject(key);
            } finally {
                writeLock.unlock();
            }
        }

        @Override // org.apache.commons.pool2.KeyedObjectPool
        public int getNumIdle(K key) {
            ReentrantReadWriteLock.ReadLock readLock = this.readWriteLock.readLock();
            readLock.lock();
            try {
                int numIdle = this.keyedPool.getNumIdle(key);
                readLock.unlock();
                return numIdle;
            } catch (Throwable th) {
                readLock.unlock();
                throw th;
            }
        }

        @Override // org.apache.commons.pool2.KeyedObjectPool
        public int getNumActive(K key) {
            ReentrantReadWriteLock.ReadLock readLock = this.readWriteLock.readLock();
            readLock.lock();
            try {
                int numActive = this.keyedPool.getNumActive(key);
                readLock.unlock();
                return numActive;
            } catch (Throwable th) {
                readLock.unlock();
                throw th;
            }
        }

        @Override // org.apache.commons.pool2.KeyedObjectPool
        public int getNumIdle() {
            ReentrantReadWriteLock.ReadLock readLock = this.readWriteLock.readLock();
            readLock.lock();
            try {
                return this.keyedPool.getNumIdle();
            } finally {
                readLock.unlock();
            }
        }

        @Override // org.apache.commons.pool2.KeyedObjectPool
        public int getNumActive() {
            ReentrantReadWriteLock.ReadLock readLock = this.readWriteLock.readLock();
            readLock.lock();
            try {
                return this.keyedPool.getNumActive();
            } finally {
                readLock.unlock();
            }
        }

        @Override // org.apache.commons.pool2.KeyedObjectPool
        public void clear() throws Exception {
            ReentrantReadWriteLock.WriteLock writeLock = this.readWriteLock.writeLock();
            writeLock.lock();
            try {
                this.keyedPool.clear();
            } finally {
                writeLock.unlock();
            }
        }

        @Override // org.apache.commons.pool2.KeyedObjectPool
        public void clear(K key) throws Exception {
            ReentrantReadWriteLock.WriteLock writeLock = this.readWriteLock.writeLock();
            writeLock.lock();
            try {
                this.keyedPool.clear(key);
            } finally {
                writeLock.unlock();
            }
        }

        @Override // org.apache.commons.pool2.KeyedObjectPool
        public void close() {
            ReentrantReadWriteLock.WriteLock writeLock = this.readWriteLock.writeLock();
            writeLock.lock();
            try {
                this.keyedPool.close();
            } catch (Exception e) {
            } finally {
                writeLock.unlock();
            }
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("SynchronizedKeyedObjectPool");
            sb.append("{keyedPool=").append(this.keyedPool);
            sb.append('}');
            return sb.toString();
        }
    }

    /* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/PoolUtils$SynchronizedPooledObjectFactory.class */
    private static final class SynchronizedPooledObjectFactory<T> implements PooledObjectFactory<T> {
        private final ReentrantReadWriteLock.WriteLock writeLock = new ReentrantReadWriteLock().writeLock();
        private final PooledObjectFactory<T> factory;

        SynchronizedPooledObjectFactory(PooledObjectFactory<T> factory) throws IllegalArgumentException {
            if (factory == null) {
                throw new IllegalArgumentException("factory must not be null.");
            }
            this.factory = factory;
        }

        @Override // org.apache.commons.pool2.PooledObjectFactory
        public PooledObject<T> makeObject() throws Exception {
            this.writeLock.lock();
            try {
                return this.factory.makeObject();
            } finally {
                this.writeLock.unlock();
            }
        }

        @Override // org.apache.commons.pool2.PooledObjectFactory
        public void destroyObject(PooledObject<T> p) throws Exception {
            this.writeLock.lock();
            try {
                this.factory.destroyObject(p);
            } finally {
                this.writeLock.unlock();
            }
        }

        @Override // org.apache.commons.pool2.PooledObjectFactory
        public boolean validateObject(PooledObject<T> p) {
            this.writeLock.lock();
            try {
                return this.factory.validateObject(p);
            } finally {
                this.writeLock.unlock();
            }
        }

        @Override // org.apache.commons.pool2.PooledObjectFactory
        public void activateObject(PooledObject<T> p) throws Exception {
            this.writeLock.lock();
            try {
                this.factory.activateObject(p);
            } finally {
                this.writeLock.unlock();
            }
        }

        @Override // org.apache.commons.pool2.PooledObjectFactory
        public void passivateObject(PooledObject<T> p) throws Exception {
            this.writeLock.lock();
            try {
                this.factory.passivateObject(p);
            } finally {
                this.writeLock.unlock();
            }
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("SynchronizedPoolableObjectFactory");
            sb.append("{factory=").append(this.factory);
            sb.append('}');
            return sb.toString();
        }
    }

    /* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/PoolUtils$SynchronizedKeyedPooledObjectFactory.class */
    private static final class SynchronizedKeyedPooledObjectFactory<K, V> implements KeyedPooledObjectFactory<K, V> {
        private final ReentrantReadWriteLock.WriteLock writeLock = new ReentrantReadWriteLock().writeLock();
        private final KeyedPooledObjectFactory<K, V> keyedFactory;

        SynchronizedKeyedPooledObjectFactory(KeyedPooledObjectFactory<K, V> keyedFactory) throws IllegalArgumentException {
            if (keyedFactory == null) {
                throw new IllegalArgumentException("keyedFactory must not be null.");
            }
            this.keyedFactory = keyedFactory;
        }

        @Override // org.apache.commons.pool2.KeyedPooledObjectFactory
        public PooledObject<V> makeObject(K key) throws Exception {
            this.writeLock.lock();
            try {
                return this.keyedFactory.makeObject(key);
            } finally {
                this.writeLock.unlock();
            }
        }

        @Override // org.apache.commons.pool2.KeyedPooledObjectFactory
        public void destroyObject(K key, PooledObject<V> p) throws Exception {
            this.writeLock.lock();
            try {
                this.keyedFactory.destroyObject(key, p);
            } finally {
                this.writeLock.unlock();
            }
        }

        @Override // org.apache.commons.pool2.KeyedPooledObjectFactory
        public boolean validateObject(K key, PooledObject<V> p) {
            this.writeLock.lock();
            try {
                boolean zValidateObject = this.keyedFactory.validateObject(key, p);
                this.writeLock.unlock();
                return zValidateObject;
            } catch (Throwable th) {
                this.writeLock.unlock();
                throw th;
            }
        }

        @Override // org.apache.commons.pool2.KeyedPooledObjectFactory
        public void activateObject(K key, PooledObject<V> p) throws Exception {
            this.writeLock.lock();
            try {
                this.keyedFactory.activateObject(key, p);
            } finally {
                this.writeLock.unlock();
            }
        }

        @Override // org.apache.commons.pool2.KeyedPooledObjectFactory
        public void passivateObject(K key, PooledObject<V> p) throws Exception {
            this.writeLock.lock();
            try {
                this.keyedFactory.passivateObject(key, p);
            } finally {
                this.writeLock.unlock();
            }
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("SynchronizedKeyedPoolableObjectFactory");
            sb.append("{keyedFactory=").append(this.keyedFactory);
            sb.append('}');
            return sb.toString();
        }
    }

    /* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/PoolUtils$ErodingFactor.class */
    private static final class ErodingFactor {
        private final float factor;
        private volatile transient long nextShrink;
        private volatile transient int idleHighWaterMark = 1;

        public ErodingFactor(float factor) {
            this.factor = factor;
            this.nextShrink = System.currentTimeMillis() + ((long) (900000.0f * factor));
        }

        public void update(long now, int numIdle) {
            int idle = Math.max(0, numIdle);
            this.idleHighWaterMark = Math.max(idle, this.idleHighWaterMark);
            float minutes = 15.0f + (((-14.0f) / this.idleHighWaterMark) * idle);
            this.nextShrink = now + ((long) (minutes * 60000.0f * this.factor));
        }

        public long getNextShrink() {
            return this.nextShrink;
        }

        public String toString() {
            return "ErodingFactor{factor=" + this.factor + ", idleHighWaterMark=" + this.idleHighWaterMark + '}';
        }
    }

    /* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/PoolUtils$ErodingObjectPool.class */
    private static class ErodingObjectPool<T> implements ObjectPool<T> {
        private final ObjectPool<T> pool;
        private final ErodingFactor factor;

        public ErodingObjectPool(ObjectPool<T> pool, float factor) {
            this.pool = pool;
            this.factor = new ErodingFactor(factor);
        }

        @Override // org.apache.commons.pool2.ObjectPool
        public T borrowObject() throws Exception {
            return this.pool.borrowObject();
        }

        @Override // org.apache.commons.pool2.ObjectPool
        public void returnObject(T obj) {
            boolean discard = false;
            long now = System.currentTimeMillis();
            synchronized (this.pool) {
                if (this.factor.getNextShrink() < now) {
                    int numIdle = this.pool.getNumIdle();
                    if (numIdle > 0) {
                        discard = true;
                    }
                    this.factor.update(now, numIdle);
                }
            }
            try {
                if (discard) {
                    this.pool.invalidateObject(obj);
                } else {
                    this.pool.returnObject(obj);
                }
            } catch (Exception e) {
            }
        }

        @Override // org.apache.commons.pool2.ObjectPool
        public void invalidateObject(T obj) {
            try {
                this.pool.invalidateObject(obj);
            } catch (Exception e) {
            }
        }

        @Override // org.apache.commons.pool2.ObjectPool
        public void addObject() throws Exception {
            this.pool.addObject();
        }

        @Override // org.apache.commons.pool2.ObjectPool
        public int getNumIdle() {
            return this.pool.getNumIdle();
        }

        @Override // org.apache.commons.pool2.ObjectPool
        public int getNumActive() {
            return this.pool.getNumActive();
        }

        @Override // org.apache.commons.pool2.ObjectPool
        public void clear() throws Exception {
            this.pool.clear();
        }

        @Override // org.apache.commons.pool2.ObjectPool
        public void close() {
            try {
                this.pool.close();
            } catch (Exception e) {
            }
        }

        public String toString() {
            return "ErodingObjectPool{factor=" + this.factor + ", pool=" + this.pool + '}';
        }
    }

    /* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/PoolUtils$ErodingKeyedObjectPool.class */
    private static class ErodingKeyedObjectPool<K, V> implements KeyedObjectPool<K, V> {
        private final KeyedObjectPool<K, V> keyedPool;
        private final ErodingFactor erodingFactor;

        public ErodingKeyedObjectPool(KeyedObjectPool<K, V> keyedPool, float factor) {
            this(keyedPool, new ErodingFactor(factor));
        }

        protected ErodingKeyedObjectPool(KeyedObjectPool<K, V> keyedPool, ErodingFactor erodingFactor) {
            if (keyedPool == null) {
                throw new IllegalArgumentException("keyedPool must not be null.");
            }
            this.keyedPool = keyedPool;
            this.erodingFactor = erodingFactor;
        }

        @Override // org.apache.commons.pool2.KeyedObjectPool
        public V borrowObject(K key) throws Exception {
            return this.keyedPool.borrowObject(key);
        }

        @Override // org.apache.commons.pool2.KeyedObjectPool
        public void returnObject(K key, V obj) throws Exception {
            boolean discard = false;
            long now = System.currentTimeMillis();
            ErodingFactor factor = getErodingFactor(key);
            synchronized (this.keyedPool) {
                if (factor.getNextShrink() < now) {
                    int numIdle = getNumIdle(key);
                    if (numIdle > 0) {
                        discard = true;
                    }
                    factor.update(now, numIdle);
                }
            }
            try {
                if (discard) {
                    this.keyedPool.invalidateObject(key, obj);
                } else {
                    this.keyedPool.returnObject(key, obj);
                }
            } catch (Exception e) {
            }
        }

        protected ErodingFactor getErodingFactor(K key) {
            return this.erodingFactor;
        }

        @Override // org.apache.commons.pool2.KeyedObjectPool
        public void invalidateObject(K key, V obj) {
            try {
                this.keyedPool.invalidateObject(key, obj);
            } catch (Exception e) {
            }
        }

        @Override // org.apache.commons.pool2.KeyedObjectPool
        public void addObject(K key) throws Exception {
            this.keyedPool.addObject(key);
        }

        @Override // org.apache.commons.pool2.KeyedObjectPool
        public int getNumIdle() {
            return this.keyedPool.getNumIdle();
        }

        @Override // org.apache.commons.pool2.KeyedObjectPool
        public int getNumIdle(K key) {
            return this.keyedPool.getNumIdle(key);
        }

        @Override // org.apache.commons.pool2.KeyedObjectPool
        public int getNumActive() {
            return this.keyedPool.getNumActive();
        }

        @Override // org.apache.commons.pool2.KeyedObjectPool
        public int getNumActive(K key) {
            return this.keyedPool.getNumActive(key);
        }

        @Override // org.apache.commons.pool2.KeyedObjectPool
        public void clear() throws Exception {
            this.keyedPool.clear();
        }

        @Override // org.apache.commons.pool2.KeyedObjectPool
        public void clear(K key) throws Exception {
            this.keyedPool.clear(key);
        }

        @Override // org.apache.commons.pool2.KeyedObjectPool
        public void close() {
            try {
                this.keyedPool.close();
            } catch (Exception e) {
            }
        }

        protected KeyedObjectPool<K, V> getKeyedPool() {
            return this.keyedPool;
        }

        public String toString() {
            return "ErodingKeyedObjectPool{factor=" + this.erodingFactor + ", keyedPool=" + this.keyedPool + '}';
        }
    }

    /* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/PoolUtils$ErodingPerKeyKeyedObjectPool.class */
    private static final class ErodingPerKeyKeyedObjectPool<K, V> extends ErodingKeyedObjectPool<K, V> {
        private final float factor;
        private final Map<K, ErodingFactor> factors;

        public ErodingPerKeyKeyedObjectPool(KeyedObjectPool<K, V> keyedPool, float factor) {
            super(keyedPool, (ErodingFactor) null);
            this.factors = Collections.synchronizedMap(new HashMap());
            this.factor = factor;
        }

        @Override // org.apache.commons.pool2.PoolUtils.ErodingKeyedObjectPool
        protected ErodingFactor getErodingFactor(K key) {
            ErodingFactor eFactor = this.factors.get(key);
            if (eFactor == null) {
                eFactor = new ErodingFactor(this.factor);
                this.factors.put(key, eFactor);
            }
            return eFactor;
        }

        @Override // org.apache.commons.pool2.PoolUtils.ErodingKeyedObjectPool
        public String toString() {
            return "ErodingPerKeyKeyedObjectPool{factor=" + this.factor + ", keyedPool=" + getKeyedPool() + '}';
        }
    }
}

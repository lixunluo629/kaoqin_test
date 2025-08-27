package org.apache.commons.pool2.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.commons.pool2.KeyedObjectPool;
import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PoolUtils;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectState;
import org.apache.commons.pool2.impl.BaseGenericObjectPool;

/*  JADX ERROR: NullPointerException in pass: ClassModifier
    java.lang.NullPointerException: Cannot invoke "java.util.List.forEach(java.util.function.Consumer)" because "blocks" is null
    	at jadx.core.utils.BlockUtils.collectAllInsns(BlockUtils.java:1029)
    	at jadx.core.dex.visitors.ClassModifier.removeBridgeMethod(ClassModifier.java:245)
    	at jadx.core.dex.visitors.ClassModifier.removeSyntheticMethods(ClassModifier.java:160)
    	at java.base/java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.ClassModifier.visit(ClassModifier.java:65)
    	at jadx.core.dex.visitors.ClassModifier.visit(ClassModifier.java:58)
    */
/* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/impl/GenericKeyedObjectPool.class */
public class GenericKeyedObjectPool<K, T> extends BaseGenericObjectPool<T> implements KeyedObjectPool<K, T>, GenericKeyedObjectPoolMXBean<K> {
    private volatile int maxIdlePerKey;
    private volatile int minIdlePerKey;
    private volatile int maxTotalPerKey;
    private final KeyedPooledObjectFactory<K, T> factory;
    private final boolean fairness;
    private final Map<K, GenericKeyedObjectPool<K, T>.ObjectDeque<T>> poolMap;
    private final List<K> poolKeyList;
    private final ReadWriteLock keyLock;
    private final AtomicInteger numTotal;
    private Iterator<K> evictionKeyIterator;
    private K evictionKey;
    private static final String ONAME_BASE = "org.apache.commons.pool2:type=GenericKeyedObjectPool,name=";

    public GenericKeyedObjectPool(KeyedPooledObjectFactory<K, T> factory) {
        this(factory, new GenericKeyedObjectPoolConfig());
    }

    public GenericKeyedObjectPool(KeyedPooledObjectFactory<K, T> factory, GenericKeyedObjectPoolConfig config) {
        super(config, ONAME_BASE, config.getJmxNamePrefix());
        this.maxIdlePerKey = 8;
        this.minIdlePerKey = 0;
        this.maxTotalPerKey = 8;
        this.poolMap = new ConcurrentHashMap();
        this.poolKeyList = new ArrayList();
        this.keyLock = new ReentrantReadWriteLock(true);
        this.numTotal = new AtomicInteger(0);
        this.evictionKeyIterator = null;
        this.evictionKey = null;
        if (factory == null) {
            jmxUnregister();
            throw new IllegalArgumentException("factory may not be null");
        }
        this.factory = factory;
        this.fairness = config.getFairness();
        setConfig(config);
        startEvictor(getTimeBetweenEvictionRunsMillis());
    }

    @Override // org.apache.commons.pool2.impl.GenericKeyedObjectPoolMXBean
    public int getMaxTotalPerKey() {
        return this.maxTotalPerKey;
    }

    public void setMaxTotalPerKey(int maxTotalPerKey) {
        this.maxTotalPerKey = maxTotalPerKey;
    }

    @Override // org.apache.commons.pool2.impl.GenericKeyedObjectPoolMXBean
    public int getMaxIdlePerKey() {
        return this.maxIdlePerKey;
    }

    public void setMaxIdlePerKey(int maxIdlePerKey) {
        this.maxIdlePerKey = maxIdlePerKey;
    }

    public void setMinIdlePerKey(int minIdlePerKey) {
        this.minIdlePerKey = minIdlePerKey;
    }

    @Override // org.apache.commons.pool2.impl.GenericKeyedObjectPoolMXBean
    public int getMinIdlePerKey() {
        int maxIdlePerKeySave = getMaxIdlePerKey();
        if (this.minIdlePerKey > maxIdlePerKeySave) {
            return maxIdlePerKeySave;
        }
        return this.minIdlePerKey;
    }

    public void setConfig(GenericKeyedObjectPoolConfig conf) {
        setLifo(conf.getLifo());
        setMaxIdlePerKey(conf.getMaxIdlePerKey());
        setMaxTotalPerKey(conf.getMaxTotalPerKey());
        setMaxTotal(conf.getMaxTotal());
        setMinIdlePerKey(conf.getMinIdlePerKey());
        setMaxWaitMillis(conf.getMaxWaitMillis());
        setBlockWhenExhausted(conf.getBlockWhenExhausted());
        setTestOnCreate(conf.getTestOnCreate());
        setTestOnBorrow(conf.getTestOnBorrow());
        setTestOnReturn(conf.getTestOnReturn());
        setTestWhileIdle(conf.getTestWhileIdle());
        setNumTestsPerEvictionRun(conf.getNumTestsPerEvictionRun());
        setMinEvictableIdleTimeMillis(conf.getMinEvictableIdleTimeMillis());
        setSoftMinEvictableIdleTimeMillis(conf.getSoftMinEvictableIdleTimeMillis());
        setTimeBetweenEvictionRunsMillis(conf.getTimeBetweenEvictionRunsMillis());
        setEvictionPolicyClassName(conf.getEvictionPolicyClassName());
        setEvictorShutdownTimeoutMillis(conf.getEvictorShutdownTimeoutMillis());
    }

    public KeyedPooledObjectFactory<K, T> getFactory() {
        return this.factory;
    }

    @Override // org.apache.commons.pool2.KeyedObjectPool
    public T borrowObject(K key) throws Exception {
        return borrowObject(key, getMaxWaitMillis());
    }

    public T borrowObject(K key, long borrowMaxWaitMillis) throws Exception {
        assertOpen();
        PooledObject<T> p = null;
        boolean blockWhenExhausted = getBlockWhenExhausted();
        long waitTime = System.currentTimeMillis();
        GenericKeyedObjectPool<K, T>.ObjectDeque<T> objectDeque = register(key);
        while (p == null) {
            try {
                boolean create = false;
                p = objectDeque.getIdleObjects().pollFirst();
                if (p == null) {
                    p = create(key);
                    if (p != null) {
                        create = true;
                    }
                }
                if (blockWhenExhausted) {
                    if (p == null) {
                        if (borrowMaxWaitMillis < 0) {
                            p = objectDeque.getIdleObjects().takeFirst();
                        } else {
                            p = objectDeque.getIdleObjects().pollFirst(borrowMaxWaitMillis, TimeUnit.MILLISECONDS);
                        }
                    }
                    if (p == null) {
                        throw new NoSuchElementException("Timeout waiting for idle object");
                    }
                } else if (p == null) {
                    throw new NoSuchElementException("Pool exhausted");
                }
                if (!p.allocate()) {
                    p = null;
                }
                if (p != null) {
                    try {
                        this.factory.activateObject(key, p);
                    } catch (Exception e) {
                        try {
                            destroy(key, p, true);
                        } catch (Exception e2) {
                        }
                        p = null;
                        if (create) {
                            NoSuchElementException nsee = new NoSuchElementException("Unable to activate object");
                            nsee.initCause(e);
                            throw nsee;
                        }
                    }
                    if (p != null && (getTestOnBorrow() || (create && getTestOnCreate()))) {
                        boolean validate = false;
                        Throwable validationThrowable = null;
                        try {
                            validate = this.factory.validateObject(key, p);
                        } catch (Throwable t) {
                            PoolUtils.checkRethrow(t);
                            validationThrowable = t;
                        }
                        if (!validate) {
                            try {
                                destroy(key, p, true);
                                this.destroyedByBorrowValidationCount.incrementAndGet();
                            } catch (Exception e3) {
                            }
                            p = null;
                            if (create) {
                                NoSuchElementException nsee2 = new NoSuchElementException("Unable to validate object");
                                nsee2.initCause(validationThrowable);
                                throw nsee2;
                            }
                        }
                    }
                }
            } finally {
                deregister(key);
            }
        }
        updateStatsBorrow(p, System.currentTimeMillis() - waitTime);
        return p.getObject();
    }

    /* JADX WARN: Removed duplicated region for block: B:85:0x01ae  */
    @Override // org.apache.commons.pool2.KeyedObjectPool
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void returnObject(K r6, T r7) {
        /*
            Method dump skipped, instructions count: 444
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.pool2.impl.GenericKeyedObjectPool.returnObject(java.lang.Object, java.lang.Object):void");
    }

    @Override // org.apache.commons.pool2.KeyedObjectPool
    public void invalidateObject(K key, T obj) throws Exception {
        GenericKeyedObjectPool<K, T>.ObjectDeque<T> objectDeque = this.poolMap.get(key);
        PooledObject<T> p = objectDeque.getAllObjects().get(new BaseGenericObjectPool.IdentityWrapper(obj));
        if (p == null) {
            throw new IllegalStateException("Object not currently part of this pool");
        }
        synchronized (p) {
            if (p.getState() != PooledObjectState.INVALID) {
                destroy(key, p, true);
            }
        }
        if (((ObjectDeque) objectDeque).idleObjects.hasTakeWaiters()) {
            addObject(key);
        }
    }

    @Override // org.apache.commons.pool2.KeyedObjectPool
    public void clear() {
        Iterator<K> iter = this.poolMap.keySet().iterator();
        while (iter.hasNext()) {
            clear(iter.next());
        }
    }

    @Override // org.apache.commons.pool2.KeyedObjectPool
    public void clear(K key) {
        GenericKeyedObjectPool<K, T>.ObjectDeque<T> objectDeque = register(key);
        try {
            LinkedBlockingDeque<PooledObject<T>> idleObjects = objectDeque.getIdleObjects();
            for (PooledObject<T> p = idleObjects.poll(); p != null; p = idleObjects.poll()) {
                try {
                    destroy(key, p, true);
                } catch (Exception e) {
                    swallowException(e);
                }
            }
        } finally {
            deregister(key);
        }
    }

    @Override // org.apache.commons.pool2.KeyedObjectPool
    public int getNumActive() {
        return this.numTotal.get() - getNumIdle();
    }

    @Override // org.apache.commons.pool2.impl.BaseGenericObjectPool, org.apache.commons.pool2.KeyedObjectPool
    public int getNumIdle() {
        Iterator<GenericKeyedObjectPool<K, T>.ObjectDeque<T>> iter = this.poolMap.values().iterator();
        int size = 0;
        while (true) {
            int result = size;
            if (iter.hasNext()) {
                size = result + iter.next().getIdleObjects().size();
            } else {
                return result;
            }
        }
    }

    @Override // org.apache.commons.pool2.KeyedObjectPool
    public int getNumActive(K key) {
        GenericKeyedObjectPool<K, T>.ObjectDeque<T> objectDeque = this.poolMap.get(key);
        if (objectDeque != null) {
            return objectDeque.getAllObjects().size() - objectDeque.getIdleObjects().size();
        }
        return 0;
    }

    @Override // org.apache.commons.pool2.KeyedObjectPool
    public int getNumIdle(K key) {
        GenericKeyedObjectPool<K, T>.ObjectDeque<T> objectDeque = this.poolMap.get(key);
        if (objectDeque != null) {
            return objectDeque.getIdleObjects().size();
        }
        return 0;
    }

    @Override // org.apache.commons.pool2.impl.BaseGenericObjectPool, org.apache.commons.pool2.KeyedObjectPool
    public void close() {
        if (isClosed()) {
            return;
        }
        synchronized (this.closeLock) {
            if (isClosed()) {
                return;
            }
            startEvictor(-1L);
            this.closed = true;
            clear();
            jmxUnregister();
            Iterator<GenericKeyedObjectPool<K, T>.ObjectDeque<T>> iter = this.poolMap.values().iterator();
            while (iter.hasNext()) {
                iter.next().getIdleObjects().interuptTakeWaiters();
            }
            clear();
        }
    }

    public void clearOldest() {
        Map<PooledObject<T>, K> map = new TreeMap<>();
        for (Map.Entry<K, GenericKeyedObjectPool<K, T>.ObjectDeque<T>> entry : this.poolMap.entrySet()) {
            K k = entry.getKey();
            GenericKeyedObjectPool<K, T>.ObjectDeque<T> deque = entry.getValue();
            if (deque != null) {
                LinkedBlockingDeque<PooledObject<T>> idleObjects = deque.getIdleObjects();
                Iterator<PooledObject<T>> it = idleObjects.iterator();
                while (it.hasNext()) {
                    PooledObject<T> p = it.next();
                    map.put(p, k);
                }
            }
        }
        int itemsToRemove = ((int) (map.size() * 0.15d)) + 1;
        Iterator<Map.Entry<PooledObject<T>, K>> iter = map.entrySet().iterator();
        while (iter.hasNext() && itemsToRemove > 0) {
            Map.Entry<PooledObject<T>, K> entry2 = iter.next();
            K key = entry2.getValue();
            PooledObject<T> p2 = entry2.getKey();
            boolean destroyed = true;
            try {
                destroyed = destroy(key, p2, false);
            } catch (Exception e) {
                swallowException(e);
            }
            if (destroyed) {
                itemsToRemove--;
            }
        }
    }

    private void reuseCapacity() {
        int maxTotalPerKeySave = getMaxTotalPerKey();
        int maxQueueLength = 0;
        LinkedBlockingDeque<PooledObject<T>> mostLoaded = null;
        K loadedKey = null;
        for (Map.Entry<K, GenericKeyedObjectPool<K, T>.ObjectDeque<T>> entry : this.poolMap.entrySet()) {
            K k = entry.getKey();
            GenericKeyedObjectPool<K, T>.ObjectDeque<T> deque = entry.getValue();
            if (deque != null) {
                LinkedBlockingDeque<PooledObject<T>> pool = deque.getIdleObjects();
                int queueLength = pool.getTakeQueueLength();
                if (getNumActive(k) < maxTotalPerKeySave && queueLength > maxQueueLength) {
                    maxQueueLength = queueLength;
                    mostLoaded = pool;
                    loadedKey = k;
                }
            }
        }
        if (mostLoaded != null) {
            register(loadedKey);
            try {
                try {
                    PooledObject<T> p = create(loadedKey);
                    if (p != null) {
                        addIdleObject(loadedKey, p);
                    }
                } catch (Exception e) {
                    swallowException(e);
                    deregister(loadedKey);
                }
            } finally {
                deregister(loadedKey);
            }
        }
    }

    private boolean hasBorrowWaiters() {
        for (Map.Entry<K, GenericKeyedObjectPool<K, T>.ObjectDeque<T>> entry : this.poolMap.entrySet()) {
            GenericKeyedObjectPool<K, T>.ObjectDeque<T> deque = entry.getValue();
            if (deque != null) {
                LinkedBlockingDeque<PooledObject<T>> pool = deque.getIdleObjects();
                if (pool.hasTakeWaiters()) {
                    return true;
                }
            }
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:84:0x0118 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:90:0x0115 A[SYNTHETIC] */
    @Override // org.apache.commons.pool2.impl.BaseGenericObjectPool
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void evict() throws java.lang.Exception {
        /*
            Method dump skipped, instructions count: 575
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.pool2.impl.GenericKeyedObjectPool.evict():void");
    }

    /* JADX WARN: Failed to check method for inline after forced processorg.apache.commons.pool2.impl.GenericKeyedObjectPool.ObjectDeque.access$208(org.apache.commons.pool2.impl.GenericKeyedObjectPool$ObjectDeque):long */
    /* JADX WARN: Failed to check method for inline after forced processorg.apache.commons.pool2.impl.GenericKeyedObjectPool.ObjectDeque.access$210(org.apache.commons.pool2.impl.GenericKeyedObjectPool$ObjectDeque):long */
    private PooledObject<T> create(K key) throws Exception {
        int maxTotalPerKeySave = getMaxTotalPerKey();
        if (maxTotalPerKeySave < 0) {
            maxTotalPerKeySave = Integer.MAX_VALUE;
        }
        int maxTotal = getMaxTotal();
        GenericKeyedObjectPool<K, T>.ObjectDeque<T> objectDeque = this.poolMap.get(key);
        boolean loop = true;
        while (loop) {
            int newNumTotal = this.numTotal.incrementAndGet();
            if (maxTotal > -1 && newNumTotal > maxTotal) {
                this.numTotal.decrementAndGet();
                if (getNumIdle() == 0) {
                    return null;
                }
                clearOldest();
            } else {
                loop = false;
            }
        }
        Boolean create = null;
        while (create == null) {
            synchronized (((ObjectDeque) objectDeque).makeObjectCountLock) {
                long newCreateCount = objectDeque.getCreateCount().incrementAndGet();
                if (newCreateCount > maxTotalPerKeySave) {
                    objectDeque.getCreateCount().decrementAndGet();
                    if (((ObjectDeque) objectDeque).makeObjectCount == 0) {
                        create = Boolean.FALSE;
                    } else {
                        ((ObjectDeque) objectDeque).makeObjectCountLock.wait();
                    }
                } else {
                    ObjectDeque.access$208(objectDeque);
                    create = Boolean.TRUE;
                }
            }
        }
        if (!create.booleanValue()) {
            this.numTotal.decrementAndGet();
            return null;
        }
        try {
            try {
                PooledObject<T> p = this.factory.makeObject(key);
                synchronized (((ObjectDeque) objectDeque).makeObjectCountLock) {
                    ObjectDeque.access$210(objectDeque);
                    ((ObjectDeque) objectDeque).makeObjectCountLock.notifyAll();
                }
                this.createdCount.incrementAndGet();
                objectDeque.getAllObjects().put(new BaseGenericObjectPool.IdentityWrapper<>(p.getObject()), p);
                return p;
            } catch (Exception e) {
                this.numTotal.decrementAndGet();
                objectDeque.getCreateCount().decrementAndGet();
                throw e;
            }
        } catch (Throwable th) {
            synchronized (((ObjectDeque) objectDeque).makeObjectCountLock) {
                ObjectDeque.access$210(objectDeque);
                ((ObjectDeque) objectDeque).makeObjectCountLock.notifyAll();
                throw th;
            }
        }
    }

    private boolean destroy(K key, PooledObject<T> toDestroy, boolean always) throws Exception {
        GenericKeyedObjectPool<K, T>.ObjectDeque<T> objectDeque = register(key);
        try {
            boolean isIdle = objectDeque.getIdleObjects().remove(toDestroy);
            if (isIdle || always) {
                objectDeque.getAllObjects().remove(new BaseGenericObjectPool.IdentityWrapper(toDestroy.getObject()));
                toDestroy.invalidate();
                try {
                    this.factory.destroyObject(key, toDestroy);
                    objectDeque.getCreateCount().decrementAndGet();
                    this.destroyedCount.incrementAndGet();
                    this.numTotal.decrementAndGet();
                    return true;
                } catch (Throwable th) {
                    objectDeque.getCreateCount().decrementAndGet();
                    this.destroyedCount.incrementAndGet();
                    this.numTotal.decrementAndGet();
                    throw th;
                }
            }
            deregister(key);
            return false;
        } finally {
            deregister(key);
        }
    }

    private GenericKeyedObjectPool<K, T>.ObjectDeque<T> register(K k) {
        Lock lock = this.keyLock.readLock();
        try {
            lock.lock();
            GenericKeyedObjectPool<K, T>.ObjectDeque<T> objectDeque = this.poolMap.get(k);
            if (objectDeque == null) {
                lock.unlock();
                lock = this.keyLock.writeLock();
                lock.lock();
                objectDeque = this.poolMap.get(k);
                if (objectDeque == null) {
                    objectDeque = new ObjectDeque<>(this.fairness);
                    objectDeque.getNumInterested().incrementAndGet();
                    this.poolMap.put(k, objectDeque);
                    this.poolKeyList.add(k);
                } else {
                    objectDeque.getNumInterested().incrementAndGet();
                }
            } else {
                objectDeque.getNumInterested().incrementAndGet();
            }
            lock = lock;
            return objectDeque;
        } finally {
            lock.unlock();
        }
    }

    private void deregister(K k) {
        GenericKeyedObjectPool<K, T>.ObjectDeque<T> objectDeque = this.poolMap.get(k);
        long numInterested = objectDeque.getNumInterested().decrementAndGet();
        if (numInterested == 0 && objectDeque.getCreateCount().get() == 0) {
            Lock writeLock = this.keyLock.writeLock();
            writeLock.lock();
            try {
                if (objectDeque.getCreateCount().get() == 0 && objectDeque.getNumInterested().get() == 0) {
                    this.poolMap.remove(k);
                    this.poolKeyList.remove(k);
                }
            } finally {
                writeLock.unlock();
            }
        }
    }

    @Override // org.apache.commons.pool2.impl.BaseGenericObjectPool
    void ensureMinIdle() throws Exception {
        int minIdlePerKeySave = getMinIdlePerKey();
        if (minIdlePerKeySave < 1) {
            return;
        }
        for (K k : this.poolMap.keySet()) {
            ensureMinIdle(k);
        }
    }

    private void ensureMinIdle(K key) throws Exception {
        GenericKeyedObjectPool<K, T>.ObjectDeque<T> objectDeque = this.poolMap.get(key);
        int deficit = calculateDeficit(objectDeque);
        for (int i = 0; i < deficit && calculateDeficit(objectDeque) > 0; i++) {
            addObject(key);
            if (objectDeque == null) {
                objectDeque = this.poolMap.get(key);
            }
        }
    }

    @Override // org.apache.commons.pool2.KeyedObjectPool
    public void addObject(K key) throws Exception {
        assertOpen();
        register(key);
        try {
            PooledObject<T> p = create(key);
            addIdleObject(key, p);
        } finally {
            deregister(key);
        }
    }

    private void addIdleObject(K key, PooledObject<T> p) throws Exception {
        if (p != null) {
            this.factory.passivateObject(key, p);
            LinkedBlockingDeque<PooledObject<T>> idleObjects = this.poolMap.get(key).getIdleObjects();
            if (getLifo()) {
                idleObjects.addFirst(p);
            } else {
                idleObjects.addLast(p);
            }
        }
    }

    public void preparePool(K key) throws Exception {
        int minIdlePerKeySave = getMinIdlePerKey();
        if (minIdlePerKeySave < 1) {
            return;
        }
        ensureMinIdle(key);
    }

    private int getNumTests() {
        int totalIdle = getNumIdle();
        int numTests = getNumTestsPerEvictionRun();
        if (numTests >= 0) {
            return Math.min(numTests, totalIdle);
        }
        return (int) Math.ceil(totalIdle / Math.abs(numTests));
    }

    private int calculateDeficit(GenericKeyedObjectPool<K, T>.ObjectDeque<T> objectDeque) {
        if (objectDeque == null) {
            return getMinIdlePerKey();
        }
        int maxTotal = getMaxTotal();
        int maxTotalPerKeySave = getMaxTotalPerKey();
        int objectDefecit = getMinIdlePerKey() - objectDeque.getIdleObjects().size();
        if (maxTotalPerKeySave > 0) {
            int growLimit = Math.max(0, maxTotalPerKeySave - objectDeque.getIdleObjects().size());
            objectDefecit = Math.min(objectDefecit, growLimit);
        }
        if (maxTotal > 0) {
            int growLimit2 = Math.max(0, (maxTotal - getNumActive()) - getNumIdle());
            objectDefecit = Math.min(objectDefecit, growLimit2);
        }
        return objectDefecit;
    }

    @Override // org.apache.commons.pool2.impl.GenericKeyedObjectPoolMXBean
    public Map<String, Integer> getNumActivePerKey() {
        HashMap<String, Integer> result = new HashMap<>();
        for (Map.Entry<K, GenericKeyedObjectPool<K, T>.ObjectDeque<T>> entry : this.poolMap.entrySet()) {
            if (entry != null) {
                K key = entry.getKey();
                GenericKeyedObjectPool<K, T>.ObjectDeque<T> objectDequeue = entry.getValue();
                if (key != null && objectDequeue != null) {
                    result.put(key.toString(), Integer.valueOf(objectDequeue.getAllObjects().size() - objectDequeue.getIdleObjects().size()));
                }
            }
        }
        return result;
    }

    @Override // org.apache.commons.pool2.impl.GenericKeyedObjectPoolMXBean
    public int getNumWaiters() {
        int result = 0;
        if (getBlockWhenExhausted()) {
            Iterator<GenericKeyedObjectPool<K, T>.ObjectDeque<T>> iter = this.poolMap.values().iterator();
            while (iter.hasNext()) {
                result += iter.next().getIdleObjects().getTakeQueueLength();
            }
        }
        return result;
    }

    @Override // org.apache.commons.pool2.impl.GenericKeyedObjectPoolMXBean
    public Map<String, Integer> getNumWaitersByKey() {
        Map<String, Integer> result = new HashMap<>();
        for (Map.Entry<K, GenericKeyedObjectPool<K, T>.ObjectDeque<T>> entry : this.poolMap.entrySet()) {
            K k = entry.getKey();
            GenericKeyedObjectPool<K, T>.ObjectDeque<T> deque = entry.getValue();
            if (deque != null) {
                if (getBlockWhenExhausted()) {
                    result.put(k.toString(), Integer.valueOf(deque.getIdleObjects().getTakeQueueLength()));
                } else {
                    result.put(k.toString(), 0);
                }
            }
        }
        return result;
    }

    @Override // org.apache.commons.pool2.impl.GenericKeyedObjectPoolMXBean
    public Map<String, List<DefaultPooledObjectInfo>> listAllObjects() {
        Map<String, List<DefaultPooledObjectInfo>> result = new HashMap<>();
        for (Map.Entry<K, GenericKeyedObjectPool<K, T>.ObjectDeque<T>> entry : this.poolMap.entrySet()) {
            K k = entry.getKey();
            GenericKeyedObjectPool<K, T>.ObjectDeque<T> deque = entry.getValue();
            if (deque != null) {
                List<DefaultPooledObjectInfo> list = new ArrayList<>();
                result.put(k.toString(), list);
                for (PooledObject<T> p : deque.getAllObjects().values()) {
                    list.add(new DefaultPooledObjectInfo(p));
                }
            }
        }
        return result;
    }

    /* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/impl/GenericKeyedObjectPool$ObjectDeque.class */
    private class ObjectDeque<S> {
        private final LinkedBlockingDeque<PooledObject<S>> idleObjects;
        private final AtomicInteger createCount = new AtomicInteger(0);
        private long makeObjectCount = 0;
        private final Object makeObjectCountLock = new Object();
        private final Map<BaseGenericObjectPool.IdentityWrapper<S>, PooledObject<S>> allObjects = new ConcurrentHashMap();
        private final AtomicLong numInterested = new AtomicLong(0);

        /*  JADX ERROR: Failed to decode insn: 0x0005: MOVE_MULTI
            java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[8]
            	at java.base/java.lang.System.arraycopy(Native Method)
            	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
            	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
            	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
            	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
            	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
            	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:464)
            	at jadx.core.ProcessClass.process(ProcessClass.java:69)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:117)
            	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
            	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
            */
        static /* synthetic */ long access$208(org.apache.commons.pool2.impl.GenericKeyedObjectPool.ObjectDeque r8) {
            /*
                r0 = r8
                r1 = r0
                long r1 = r1.makeObjectCount
                // decode failed: arraycopy: source index -1 out of bounds for object array[8]
                r2 = 1
                long r1 = r1 + r2
                r0.makeObjectCount = r1
                return r-1
            */
            throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.pool2.impl.GenericKeyedObjectPool.ObjectDeque.access$208(org.apache.commons.pool2.impl.GenericKeyedObjectPool$ObjectDeque):long");
        }

        /*  JADX ERROR: Failed to decode insn: 0x0005: MOVE_MULTI
            java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[8]
            	at java.base/java.lang.System.arraycopy(Native Method)
            	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
            	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
            	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
            	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
            	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
            	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:464)
            	at jadx.core.ProcessClass.process(ProcessClass.java:69)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:117)
            	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
            	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
            */
        static /* synthetic */ long access$210(org.apache.commons.pool2.impl.GenericKeyedObjectPool.ObjectDeque r8) {
            /*
                r0 = r8
                r1 = r0
                long r1 = r1.makeObjectCount
                // decode failed: arraycopy: source index -1 out of bounds for object array[8]
                r2 = 1
                long r1 = r1 - r2
                r0.makeObjectCount = r1
                return r-1
            */
            throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.pool2.impl.GenericKeyedObjectPool.ObjectDeque.access$210(org.apache.commons.pool2.impl.GenericKeyedObjectPool$ObjectDeque):long");
        }

        public ObjectDeque(boolean fairness) {
            this.idleObjects = new LinkedBlockingDeque<>(fairness);
        }

        public LinkedBlockingDeque<PooledObject<S>> getIdleObjects() {
            return this.idleObjects;
        }

        public AtomicInteger getCreateCount() {
            return this.createCount;
        }

        public AtomicLong getNumInterested() {
            return this.numInterested;
        }

        public Map<BaseGenericObjectPool.IdentityWrapper<S>, PooledObject<S>> getAllObjects() {
            return this.allObjects;
        }

        public String toString() {
            return "ObjectDeque [idleObjects=" + this.idleObjects + ", createCount=" + this.createCount + ", allObjects=" + this.allObjects + ", numInterested=" + this.numInterested + "]";
        }
    }

    @Override // org.apache.commons.pool2.impl.BaseGenericObjectPool, org.apache.commons.pool2.BaseObject
    protected void toStringAppendFields(StringBuilder builder) {
        super.toStringAppendFields(builder);
        builder.append(", maxIdlePerKey=");
        builder.append(this.maxIdlePerKey);
        builder.append(", minIdlePerKey=");
        builder.append(this.minIdlePerKey);
        builder.append(", maxTotalPerKey=");
        builder.append(this.maxTotalPerKey);
        builder.append(", factory=");
        builder.append(this.factory);
        builder.append(", fairness=");
        builder.append(this.fairness);
        builder.append(", poolMap=");
        builder.append(this.poolMap);
        builder.append(", poolKeyList=");
        builder.append(this.poolKeyList);
        builder.append(", keyLock=");
        builder.append(this.keyLock);
        builder.append(", numTotal=");
        builder.append(this.numTotal);
        builder.append(", evictionKeyIterator=");
        builder.append(this.evictionKeyIterator);
        builder.append(", evictionKey=");
        builder.append(this.evictionKey);
    }
}

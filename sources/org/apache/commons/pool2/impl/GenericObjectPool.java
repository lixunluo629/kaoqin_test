package org.apache.commons.pool2.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PoolUtils;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.PooledObjectState;
import org.apache.commons.pool2.UsageTracking;
import org.apache.commons.pool2.impl.BaseGenericObjectPool;

/* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/impl/GenericObjectPool.class */
public class GenericObjectPool<T> extends BaseGenericObjectPool<T> implements ObjectPool<T>, GenericObjectPoolMXBean, UsageTracking<T> {
    private volatile String factoryType;
    private volatile int maxIdle;
    private volatile int minIdle;
    private final PooledObjectFactory<T> factory;
    private final Map<BaseGenericObjectPool.IdentityWrapper<T>, PooledObject<T>> allObjects;
    private final AtomicLong createCount;
    private long makeObjectCount;
    private final Object makeObjectCountLock;
    private final LinkedBlockingDeque<PooledObject<T>> idleObjects;
    private static final String ONAME_BASE = "org.apache.commons.pool2:type=GenericObjectPool,name=";
    private volatile AbandonedConfig abandonedConfig;

    public GenericObjectPool(PooledObjectFactory<T> factory) {
        this(factory, new GenericObjectPoolConfig());
    }

    public GenericObjectPool(PooledObjectFactory<T> factory, GenericObjectPoolConfig config) {
        super(config, ONAME_BASE, config.getJmxNamePrefix());
        this.factoryType = null;
        this.maxIdle = 8;
        this.minIdle = 0;
        this.allObjects = new ConcurrentHashMap();
        this.createCount = new AtomicLong(0L);
        this.makeObjectCount = 0L;
        this.makeObjectCountLock = new Object();
        this.abandonedConfig = null;
        if (factory == null) {
            jmxUnregister();
            throw new IllegalArgumentException("factory may not be null");
        }
        this.factory = factory;
        this.idleObjects = new LinkedBlockingDeque<>(config.getFairness());
        setConfig(config);
        startEvictor(getTimeBetweenEvictionRunsMillis());
    }

    public GenericObjectPool(PooledObjectFactory<T> factory, GenericObjectPoolConfig config, AbandonedConfig abandonedConfig) {
        this(factory, config);
        setAbandonedConfig(abandonedConfig);
    }

    @Override // org.apache.commons.pool2.impl.GenericObjectPoolMXBean
    public int getMaxIdle() {
        return this.maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    @Override // org.apache.commons.pool2.impl.GenericObjectPoolMXBean
    public int getMinIdle() {
        int maxIdleSave = getMaxIdle();
        if (this.minIdle > maxIdleSave) {
            return maxIdleSave;
        }
        return this.minIdle;
    }

    @Override // org.apache.commons.pool2.impl.GenericObjectPoolMXBean
    public boolean isAbandonedConfig() {
        return this.abandonedConfig != null;
    }

    @Override // org.apache.commons.pool2.impl.GenericObjectPoolMXBean
    public boolean getLogAbandoned() {
        AbandonedConfig ac = this.abandonedConfig;
        return ac != null && ac.getLogAbandoned();
    }

    @Override // org.apache.commons.pool2.impl.GenericObjectPoolMXBean
    public boolean getRemoveAbandonedOnBorrow() {
        AbandonedConfig ac = this.abandonedConfig;
        return ac != null && ac.getRemoveAbandonedOnBorrow();
    }

    @Override // org.apache.commons.pool2.impl.GenericObjectPoolMXBean
    public boolean getRemoveAbandonedOnMaintenance() {
        AbandonedConfig ac = this.abandonedConfig;
        return ac != null && ac.getRemoveAbandonedOnMaintenance();
    }

    @Override // org.apache.commons.pool2.impl.GenericObjectPoolMXBean
    public int getRemoveAbandonedTimeout() {
        AbandonedConfig ac = this.abandonedConfig;
        if (ac != null) {
            return ac.getRemoveAbandonedTimeout();
        }
        return Integer.MAX_VALUE;
    }

    public void setConfig(GenericObjectPoolConfig conf) {
        setLifo(conf.getLifo());
        setMaxIdle(conf.getMaxIdle());
        setMinIdle(conf.getMinIdle());
        setMaxTotal(conf.getMaxTotal());
        setMaxWaitMillis(conf.getMaxWaitMillis());
        setBlockWhenExhausted(conf.getBlockWhenExhausted());
        setTestOnCreate(conf.getTestOnCreate());
        setTestOnBorrow(conf.getTestOnBorrow());
        setTestOnReturn(conf.getTestOnReturn());
        setTestWhileIdle(conf.getTestWhileIdle());
        setNumTestsPerEvictionRun(conf.getNumTestsPerEvictionRun());
        setMinEvictableIdleTimeMillis(conf.getMinEvictableIdleTimeMillis());
        setTimeBetweenEvictionRunsMillis(conf.getTimeBetweenEvictionRunsMillis());
        setSoftMinEvictableIdleTimeMillis(conf.getSoftMinEvictableIdleTimeMillis());
        setEvictionPolicyClassName(conf.getEvictionPolicyClassName());
        setEvictorShutdownTimeoutMillis(conf.getEvictorShutdownTimeoutMillis());
    }

    public void setAbandonedConfig(AbandonedConfig abandonedConfig) {
        if (abandonedConfig == null) {
            this.abandonedConfig = null;
            return;
        }
        this.abandonedConfig = new AbandonedConfig();
        this.abandonedConfig.setLogAbandoned(abandonedConfig.getLogAbandoned());
        this.abandonedConfig.setLogWriter(abandonedConfig.getLogWriter());
        this.abandonedConfig.setRemoveAbandonedOnBorrow(abandonedConfig.getRemoveAbandonedOnBorrow());
        this.abandonedConfig.setRemoveAbandonedOnMaintenance(abandonedConfig.getRemoveAbandonedOnMaintenance());
        this.abandonedConfig.setRemoveAbandonedTimeout(abandonedConfig.getRemoveAbandonedTimeout());
        this.abandonedConfig.setUseUsageTracking(abandonedConfig.getUseUsageTracking());
    }

    public PooledObjectFactory<T> getFactory() {
        return this.factory;
    }

    @Override // org.apache.commons.pool2.ObjectPool
    public T borrowObject() throws Exception {
        return borrowObject(getMaxWaitMillis());
    }

    public T borrowObject(long borrowMaxWaitMillis) throws Exception {
        assertOpen();
        AbandonedConfig ac = this.abandonedConfig;
        if (ac != null && ac.getRemoveAbandonedOnBorrow() && getNumIdle() < 2 && getNumActive() > getMaxTotal() - 3) {
            removeAbandoned(ac);
        }
        PooledObject<T> p = null;
        boolean blockWhenExhausted = getBlockWhenExhausted();
        long waitTime = System.currentTimeMillis();
        while (p == null) {
            boolean create = false;
            p = this.idleObjects.pollFirst();
            if (p == null) {
                p = create();
                if (p != null) {
                    create = true;
                }
            }
            if (blockWhenExhausted) {
                if (p == null) {
                    if (borrowMaxWaitMillis < 0) {
                        p = this.idleObjects.takeFirst();
                    } else {
                        p = this.idleObjects.pollFirst(borrowMaxWaitMillis, TimeUnit.MILLISECONDS);
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
                    this.factory.activateObject(p);
                } catch (Exception e) {
                    try {
                        destroy(p);
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
                        validate = this.factory.validateObject(p);
                    } catch (Throwable t) {
                        PoolUtils.checkRethrow(t);
                        validationThrowable = t;
                    }
                    if (validate) {
                        continue;
                    } else {
                        try {
                            destroy(p);
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
        }
        updateStatsBorrow(p, System.currentTimeMillis() - waitTime);
        return p.getObject();
    }

    @Override // org.apache.commons.pool2.ObjectPool
    public void returnObject(T obj) {
        PooledObject<T> p = this.allObjects.get(new BaseGenericObjectPool.IdentityWrapper(obj));
        if (p == null) {
            if (!isAbandonedConfig()) {
                throw new IllegalStateException("Returned object not currently part of this pool");
            }
            return;
        }
        synchronized (p) {
            PooledObjectState state = p.getState();
            if (state != PooledObjectState.ALLOCATED) {
                throw new IllegalStateException("Object has already been returned to this pool or is invalid");
            }
            p.markReturning();
        }
        long activeTime = p.getActiveTimeMillis();
        if (getTestOnReturn() && !this.factory.validateObject(p)) {
            try {
                destroy(p);
            } catch (Exception e) {
                swallowException(e);
            }
            try {
                ensureIdle(1, false);
            } catch (Exception e2) {
                swallowException(e2);
            }
            updateStatsReturn(activeTime);
            return;
        }
        try {
            this.factory.passivateObject(p);
            if (!p.deallocate()) {
                throw new IllegalStateException("Object has already been returned to this pool or is invalid");
            }
            int maxIdleSave = getMaxIdle();
            if (isClosed() || (maxIdleSave > -1 && maxIdleSave <= this.idleObjects.size())) {
                try {
                    destroy(p);
                } catch (Exception e3) {
                    swallowException(e3);
                }
            } else {
                if (getLifo()) {
                    this.idleObjects.addFirst(p);
                } else {
                    this.idleObjects.addLast(p);
                }
                if (isClosed()) {
                    clear();
                }
            }
            updateStatsReturn(activeTime);
        } catch (Exception e1) {
            swallowException(e1);
            try {
                destroy(p);
            } catch (Exception e4) {
                swallowException(e4);
            }
            try {
                ensureIdle(1, false);
            } catch (Exception e5) {
                swallowException(e5);
            }
            updateStatsReturn(activeTime);
        }
    }

    @Override // org.apache.commons.pool2.ObjectPool
    public void invalidateObject(T obj) throws Exception {
        PooledObject<T> p = this.allObjects.get(new BaseGenericObjectPool.IdentityWrapper(obj));
        if (p == null) {
            if (isAbandonedConfig()) {
            } else {
                throw new IllegalStateException("Invalidated object not currently part of this pool");
            }
        } else {
            synchronized (p) {
                if (p.getState() != PooledObjectState.INVALID) {
                    destroy(p);
                }
            }
            ensureIdle(1, false);
        }
    }

    @Override // org.apache.commons.pool2.ObjectPool
    public void clear() {
        PooledObject<T> pooledObjectPoll = this.idleObjects.poll();
        while (true) {
            PooledObject<T> p = pooledObjectPoll;
            if (p != null) {
                try {
                    destroy(p);
                } catch (Exception e) {
                    swallowException(e);
                }
                pooledObjectPoll = this.idleObjects.poll();
            } else {
                return;
            }
        }
    }

    @Override // org.apache.commons.pool2.ObjectPool
    public int getNumActive() {
        return this.allObjects.size() - this.idleObjects.size();
    }

    @Override // org.apache.commons.pool2.impl.BaseGenericObjectPool, org.apache.commons.pool2.KeyedObjectPool
    public int getNumIdle() {
        return this.idleObjects.size();
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
            this.idleObjects.interuptTakeWaiters();
        }
    }

    @Override // org.apache.commons.pool2.impl.BaseGenericObjectPool
    public void evict() throws Exception {
        boolean evict;
        assertOpen();
        if (this.idleObjects.size() > 0) {
            EvictionPolicy<T> evictionPolicy = getEvictionPolicy();
            synchronized (this.evictionLock) {
                EvictionConfig evictionConfig = new EvictionConfig(getMinEvictableIdleTimeMillis(), getSoftMinEvictableIdleTimeMillis(), getMinIdle());
                boolean testWhileIdle = getTestWhileIdle();
                int i = 0;
                int m = getNumTests();
                while (i < m) {
                    if (this.evictionIterator == null || !this.evictionIterator.hasNext()) {
                        this.evictionIterator = new BaseGenericObjectPool.EvictionIterator(this.idleObjects);
                    }
                    if (!this.evictionIterator.hasNext()) {
                        return;
                    }
                    try {
                        PooledObject<T> underTest = this.evictionIterator.next();
                        if (!underTest.startEvictionTest()) {
                            i--;
                        } else {
                            try {
                                evict = evictionPolicy.evict(evictionConfig, underTest, this.idleObjects.size());
                            } catch (Throwable t) {
                                PoolUtils.checkRethrow(t);
                                swallowException(new Exception(t));
                                evict = false;
                            }
                            if (evict) {
                                destroy(underTest);
                                this.destroyedByEvictorCount.incrementAndGet();
                            } else {
                                if (testWhileIdle) {
                                    boolean active = false;
                                    try {
                                        this.factory.activateObject(underTest);
                                        active = true;
                                    } catch (Exception e) {
                                        destroy(underTest);
                                        this.destroyedByEvictorCount.incrementAndGet();
                                    }
                                    if (active) {
                                        if (!this.factory.validateObject(underTest)) {
                                            destroy(underTest);
                                            this.destroyedByEvictorCount.incrementAndGet();
                                        } else {
                                            try {
                                                this.factory.passivateObject(underTest);
                                            } catch (Exception e2) {
                                                destroy(underTest);
                                                this.destroyedByEvictorCount.incrementAndGet();
                                            }
                                        }
                                    }
                                }
                                if (!underTest.endEvictionTest(this.idleObjects)) {
                                }
                            }
                        }
                    } catch (NoSuchElementException e3) {
                        i--;
                        this.evictionIterator = null;
                    }
                    i++;
                }
            }
        }
        AbandonedConfig ac = this.abandonedConfig;
        if (ac != null && ac.getRemoveAbandonedOnMaintenance()) {
            removeAbandoned(ac);
        }
    }

    public void preparePool() throws Exception {
        if (getMinIdle() < 1) {
            return;
        }
        ensureMinIdle();
    }

    private PooledObject<T> create() throws Exception {
        int localMaxTotal = getMaxTotal();
        if (localMaxTotal < 0) {
            localMaxTotal = Integer.MAX_VALUE;
        }
        Boolean create = null;
        while (create == null) {
            synchronized (this.makeObjectCountLock) {
                long newCreateCount = this.createCount.incrementAndGet();
                if (newCreateCount > localMaxTotal) {
                    this.createCount.decrementAndGet();
                    if (this.makeObjectCount == 0) {
                        create = Boolean.FALSE;
                    } else {
                        this.makeObjectCountLock.wait();
                    }
                } else {
                    this.makeObjectCount++;
                    create = Boolean.TRUE;
                }
            }
        }
        try {
            if (!create.booleanValue()) {
                return null;
            }
            try {
                PooledObject<T> p = this.factory.makeObject();
                synchronized (this.makeObjectCountLock) {
                    this.makeObjectCount--;
                    this.makeObjectCountLock.notifyAll();
                }
                AbandonedConfig ac = this.abandonedConfig;
                if (ac != null && ac.getLogAbandoned()) {
                    p.setLogAbandoned(true);
                }
                this.createdCount.incrementAndGet();
                this.allObjects.put(new BaseGenericObjectPool.IdentityWrapper<>(p.getObject()), p);
                return p;
            } catch (Exception e) {
                this.createCount.decrementAndGet();
                throw e;
            }
        } catch (Throwable th) {
            synchronized (this.makeObjectCountLock) {
                this.makeObjectCount--;
                this.makeObjectCountLock.notifyAll();
                throw th;
            }
        }
    }

    private void destroy(PooledObject<T> toDestroy) throws Exception {
        toDestroy.invalidate();
        this.idleObjects.remove(toDestroy);
        this.allObjects.remove(new BaseGenericObjectPool.IdentityWrapper(toDestroy.getObject()));
        try {
            this.factory.destroyObject(toDestroy);
        } finally {
            this.destroyedCount.incrementAndGet();
            this.createCount.decrementAndGet();
        }
    }

    @Override // org.apache.commons.pool2.impl.BaseGenericObjectPool
    void ensureMinIdle() throws Exception {
        ensureIdle(getMinIdle(), true);
    }

    private void ensureIdle(int idleCount, boolean always) throws Exception {
        PooledObject<T> p;
        if (idleCount < 1 || isClosed()) {
            return;
        }
        if (!always && !this.idleObjects.hasTakeWaiters()) {
            return;
        }
        while (this.idleObjects.size() < idleCount && (p = create()) != null) {
            if (getLifo()) {
                this.idleObjects.addFirst(p);
            } else {
                this.idleObjects.addLast(p);
            }
        }
        if (isClosed()) {
            clear();
        }
    }

    @Override // org.apache.commons.pool2.ObjectPool
    public void addObject() throws Exception {
        assertOpen();
        if (this.factory == null) {
            throw new IllegalStateException("Cannot add objects without a factory.");
        }
        PooledObject<T> p = create();
        addIdleObject(p);
    }

    private void addIdleObject(PooledObject<T> p) throws Exception {
        if (p != null) {
            this.factory.passivateObject(p);
            if (getLifo()) {
                this.idleObjects.addFirst(p);
            } else {
                this.idleObjects.addLast(p);
            }
        }
    }

    private int getNumTests() {
        int numTestsPerEvictionRun = getNumTestsPerEvictionRun();
        if (numTestsPerEvictionRun >= 0) {
            return Math.min(numTestsPerEvictionRun, this.idleObjects.size());
        }
        return (int) Math.ceil(this.idleObjects.size() / Math.abs(numTestsPerEvictionRun));
    }

    private void removeAbandoned(AbandonedConfig ac) {
        long now = System.currentTimeMillis();
        long timeout = now - (ac.getRemoveAbandonedTimeout() * 1000);
        ArrayList<PooledObject<T>> remove = new ArrayList<>();
        for (PooledObject<T> pooledObject : this.allObjects.values()) {
            synchronized (pooledObject) {
                if (pooledObject.getState() == PooledObjectState.ALLOCATED && pooledObject.getLastUsedTime() <= timeout) {
                    pooledObject.markAbandoned();
                    remove.add(pooledObject);
                }
            }
        }
        Iterator<PooledObject<T>> itr = remove.iterator();
        while (itr.hasNext()) {
            PooledObject<T> pooledObject2 = itr.next();
            if (ac.getLogAbandoned()) {
                pooledObject2.printStackTrace(ac.getLogWriter());
            }
            try {
                invalidateObject(pooledObject2.getObject());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override // org.apache.commons.pool2.UsageTracking
    public void use(T pooledObject) {
        AbandonedConfig ac = this.abandonedConfig;
        if (ac != null && ac.getUseUsageTracking()) {
            PooledObject<T> wrapper = this.allObjects.get(new BaseGenericObjectPool.IdentityWrapper(pooledObject));
            wrapper.use();
        }
    }

    @Override // org.apache.commons.pool2.impl.GenericObjectPoolMXBean
    public int getNumWaiters() {
        if (getBlockWhenExhausted()) {
            return this.idleObjects.getTakeQueueLength();
        }
        return 0;
    }

    @Override // org.apache.commons.pool2.impl.GenericObjectPoolMXBean
    public String getFactoryType() {
        if (this.factoryType == null) {
            StringBuilder result = new StringBuilder();
            result.append(this.factory.getClass().getName());
            result.append('<');
            Class<?> pooledObjectType = PoolImplUtils.getFactoryType(this.factory.getClass());
            result.append(pooledObjectType.getName());
            result.append('>');
            this.factoryType = result.toString();
        }
        return this.factoryType;
    }

    @Override // org.apache.commons.pool2.impl.GenericObjectPoolMXBean
    public Set<DefaultPooledObjectInfo> listAllObjects() {
        Set<DefaultPooledObjectInfo> result = new HashSet<>(this.allObjects.size());
        for (PooledObject<T> p : this.allObjects.values()) {
            result.add(new DefaultPooledObjectInfo(p));
        }
        return result;
    }

    @Override // org.apache.commons.pool2.impl.BaseGenericObjectPool, org.apache.commons.pool2.BaseObject
    protected void toStringAppendFields(StringBuilder builder) {
        super.toStringAppendFields(builder);
        builder.append(", factoryType=");
        builder.append(this.factoryType);
        builder.append(", maxIdle=");
        builder.append(this.maxIdle);
        builder.append(", minIdle=");
        builder.append(this.minIdle);
        builder.append(", factory=");
        builder.append(this.factory);
        builder.append(", allObjects=");
        builder.append(this.allObjects);
        builder.append(", createCount=");
        builder.append(this.createCount);
        builder.append(", idleObjects=");
        builder.append(this.idleObjects);
        builder.append(", abandonedConfig=");
        builder.append(this.abandonedConfig);
    }
}

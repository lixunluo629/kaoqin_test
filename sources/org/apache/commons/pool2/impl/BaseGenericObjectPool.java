package org.apache.commons.pool2.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.management.ManagementFactory;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import org.apache.commons.pool2.BaseObject;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.SwallowedExceptionListener;
import org.springframework.beans.PropertyAccessor;

/* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/impl/BaseGenericObjectPool.class */
public abstract class BaseGenericObjectPool<T> extends BaseObject {
    public static final int MEAN_TIMING_STATS_CACHE_SIZE = 100;
    private final boolean fairness;
    private volatile EvictionPolicy<T> evictionPolicy;
    private final WeakReference<ClassLoader> factoryClassLoader;
    private final ObjectName oname;
    private final String creationStackTrace;
    private volatile int maxTotal = -1;
    private volatile boolean blockWhenExhausted = true;
    private volatile long maxWaitMillis = -1;
    private volatile boolean lifo = true;
    private volatile boolean testOnCreate = false;
    private volatile boolean testOnBorrow = false;
    private volatile boolean testOnReturn = false;
    private volatile boolean testWhileIdle = false;
    private volatile long timeBetweenEvictionRunsMillis = -1;
    private volatile int numTestsPerEvictionRun = 3;
    private volatile long minEvictableIdleTimeMillis = BaseObjectPoolConfig.DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS;
    private volatile long softMinEvictableIdleTimeMillis = -1;
    private volatile long evictorShutdownTimeoutMillis = 10000;
    final Object closeLock = new Object();
    volatile boolean closed = false;
    final Object evictionLock = new Object();
    private BaseGenericObjectPool<T>.Evictor evictor = null;
    BaseGenericObjectPool<T>.EvictionIterator evictionIterator = null;
    private final AtomicLong borrowedCount = new AtomicLong(0);
    private final AtomicLong returnedCount = new AtomicLong(0);
    final AtomicLong createdCount = new AtomicLong(0);
    final AtomicLong destroyedCount = new AtomicLong(0);
    final AtomicLong destroyedByEvictorCount = new AtomicLong(0);
    final AtomicLong destroyedByBorrowValidationCount = new AtomicLong(0);
    private final BaseGenericObjectPool<T>.StatsStore activeTimes = new StatsStore(100);
    private final BaseGenericObjectPool<T>.StatsStore idleTimes = new StatsStore(100);
    private final BaseGenericObjectPool<T>.StatsStore waitTimes = new StatsStore(100);
    private final AtomicLong maxBorrowWaitTimeMillis = new AtomicLong(0);
    private volatile SwallowedExceptionListener swallowedExceptionListener = null;

    public abstract void close();

    public abstract void evict() throws Exception;

    abstract void ensureMinIdle() throws Exception;

    public abstract int getNumIdle();

    public BaseGenericObjectPool(BaseObjectPoolConfig config, String jmxNameBase, String jmxNamePrefix) {
        if (config.getJmxEnabled()) {
            this.oname = jmxRegister(config, jmxNameBase, jmxNamePrefix);
        } else {
            this.oname = null;
        }
        this.creationStackTrace = getStackTrace(new Exception());
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            this.factoryClassLoader = null;
        } else {
            this.factoryClassLoader = new WeakReference<>(cl);
        }
        this.fairness = config.getFairness();
    }

    public final int getMaxTotal() {
        return this.maxTotal;
    }

    public final void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public final boolean getBlockWhenExhausted() {
        return this.blockWhenExhausted;
    }

    public final void setBlockWhenExhausted(boolean blockWhenExhausted) {
        this.blockWhenExhausted = blockWhenExhausted;
    }

    public final long getMaxWaitMillis() {
        return this.maxWaitMillis;
    }

    public final void setMaxWaitMillis(long maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }

    public final boolean getLifo() {
        return this.lifo;
    }

    public final boolean getFairness() {
        return this.fairness;
    }

    public final void setLifo(boolean lifo) {
        this.lifo = lifo;
    }

    public final boolean getTestOnCreate() {
        return this.testOnCreate;
    }

    public final void setTestOnCreate(boolean testOnCreate) {
        this.testOnCreate = testOnCreate;
    }

    public final boolean getTestOnBorrow() {
        return this.testOnBorrow;
    }

    public final void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public final boolean getTestOnReturn() {
        return this.testOnReturn;
    }

    public final void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public final boolean getTestWhileIdle() {
        return this.testWhileIdle;
    }

    public final void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public final long getTimeBetweenEvictionRunsMillis() {
        return this.timeBetweenEvictionRunsMillis;
    }

    public final void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
        startEvictor(timeBetweenEvictionRunsMillis);
    }

    public final int getNumTestsPerEvictionRun() {
        return this.numTestsPerEvictionRun;
    }

    public final void setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
        this.numTestsPerEvictionRun = numTestsPerEvictionRun;
    }

    public final long getMinEvictableIdleTimeMillis() {
        return this.minEvictableIdleTimeMillis;
    }

    public final void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    public final long getSoftMinEvictableIdleTimeMillis() {
        return this.softMinEvictableIdleTimeMillis;
    }

    public final void setSoftMinEvictableIdleTimeMillis(long softMinEvictableIdleTimeMillis) {
        this.softMinEvictableIdleTimeMillis = softMinEvictableIdleTimeMillis;
    }

    public final String getEvictionPolicyClassName() {
        return this.evictionPolicy.getClass().getName();
    }

    public final void setEvictionPolicyClassName(String evictionPolicyClassName) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        Class<?> clazz;
        try {
            try {
                clazz = Class.forName(evictionPolicyClassName, true, Thread.currentThread().getContextClassLoader());
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException("Unable to create EvictionPolicy instance of type " + evictionPolicyClassName, e);
            } catch (IllegalAccessException e2) {
                throw new IllegalArgumentException("Unable to create EvictionPolicy instance of type " + evictionPolicyClassName, e2);
            } catch (InstantiationException e3) {
                throw new IllegalArgumentException("Unable to create EvictionPolicy instance of type " + evictionPolicyClassName, e3);
            }
        } catch (ClassNotFoundException e4) {
            clazz = Class.forName(evictionPolicyClassName);
        }
        Object policy = clazz.newInstance();
        if (policy instanceof EvictionPolicy) {
            EvictionPolicy<T> evicPolicy = (EvictionPolicy) policy;
            this.evictionPolicy = evicPolicy;
            return;
        }
        throw new IllegalArgumentException(PropertyAccessor.PROPERTY_KEY_PREFIX + evictionPolicyClassName + "] does not implement EvictionPolicy");
    }

    public final long getEvictorShutdownTimeoutMillis() {
        return this.evictorShutdownTimeoutMillis;
    }

    public final void setEvictorShutdownTimeoutMillis(long evictorShutdownTimeoutMillis) {
        this.evictorShutdownTimeoutMillis = evictorShutdownTimeoutMillis;
    }

    public final boolean isClosed() {
        return this.closed;
    }

    protected EvictionPolicy<T> getEvictionPolicy() {
        return this.evictionPolicy;
    }

    final void assertOpen() throws IllegalStateException {
        if (isClosed()) {
            throw new IllegalStateException("Pool not open");
        }
    }

    final void startEvictor(long delay) {
        synchronized (this.evictionLock) {
            if (null != this.evictor) {
                EvictionTimer.cancel(this.evictor, this.evictorShutdownTimeoutMillis, TimeUnit.MILLISECONDS);
                this.evictor = null;
                this.evictionIterator = null;
            }
            if (delay > 0) {
                this.evictor = new Evictor();
                EvictionTimer.schedule(this.evictor, delay, delay);
            }
        }
    }

    public final ObjectName getJmxName() {
        return this.oname;
    }

    public final String getCreationStackTrace() {
        return this.creationStackTrace;
    }

    public final long getBorrowedCount() {
        return this.borrowedCount.get();
    }

    public final long getReturnedCount() {
        return this.returnedCount.get();
    }

    public final long getCreatedCount() {
        return this.createdCount.get();
    }

    public final long getDestroyedCount() {
        return this.destroyedCount.get();
    }

    public final long getDestroyedByEvictorCount() {
        return this.destroyedByEvictorCount.get();
    }

    public final long getDestroyedByBorrowValidationCount() {
        return this.destroyedByBorrowValidationCount.get();
    }

    public final long getMeanActiveTimeMillis() {
        return this.activeTimes.getMean();
    }

    public final long getMeanIdleTimeMillis() {
        return this.idleTimes.getMean();
    }

    public final long getMeanBorrowWaitTimeMillis() {
        return this.waitTimes.getMean();
    }

    public final long getMaxBorrowWaitTimeMillis() {
        return this.maxBorrowWaitTimeMillis.get();
    }

    public final SwallowedExceptionListener getSwallowedExceptionListener() {
        return this.swallowedExceptionListener;
    }

    public final void setSwallowedExceptionListener(SwallowedExceptionListener swallowedExceptionListener) {
        this.swallowedExceptionListener = swallowedExceptionListener;
    }

    final void swallowException(Exception e) {
        SwallowedExceptionListener listener = getSwallowedExceptionListener();
        if (listener == null) {
            return;
        }
        try {
            listener.onSwallowException(e);
        } catch (OutOfMemoryError oome) {
            throw oome;
        } catch (VirtualMachineError vme) {
            throw vme;
        } catch (Throwable th) {
        }
    }

    final void updateStatsBorrow(PooledObject<T> p, long waitTime) {
        long currentMax;
        this.borrowedCount.incrementAndGet();
        this.idleTimes.add(p.getIdleTimeMillis());
        this.waitTimes.add(waitTime);
        do {
            currentMax = this.maxBorrowWaitTimeMillis.get();
            if (currentMax >= waitTime) {
                return;
            }
        } while (!this.maxBorrowWaitTimeMillis.compareAndSet(currentMax, waitTime));
    }

    final void updateStatsReturn(long activeTime) {
        this.returnedCount.incrementAndGet();
        this.activeTimes.add(activeTime);
    }

    final void jmxUnregister() {
        if (this.oname != null) {
            try {
                ManagementFactory.getPlatformMBeanServer().unregisterMBean(this.oname);
            } catch (InstanceNotFoundException e) {
                swallowException(e);
            } catch (MBeanRegistrationException e2) {
                swallowException(e2);
            }
        }
    }

    private ObjectName jmxRegister(BaseObjectPoolConfig config, String jmxNameBase, String jmxNamePrefix) {
        ObjectName objName;
        ObjectName objectName = null;
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        int i = 1;
        boolean registered = false;
        String base = config.getJmxNameBase();
        if (base == null) {
            base = jmxNameBase;
        }
        while (!registered) {
            if (i == 1) {
                try {
                    objName = new ObjectName(base + jmxNamePrefix);
                } catch (NotCompliantMBeanException e) {
                    registered = true;
                } catch (MalformedObjectNameException e2) {
                    if (BaseObjectPoolConfig.DEFAULT_JMX_NAME_PREFIX.equals(jmxNamePrefix) && jmxNameBase.equals(base)) {
                        registered = true;
                    } else {
                        jmxNamePrefix = BaseObjectPoolConfig.DEFAULT_JMX_NAME_PREFIX;
                        base = jmxNameBase;
                    }
                } catch (InstanceAlreadyExistsException e3) {
                    i++;
                } catch (MBeanRegistrationException e4) {
                    registered = true;
                }
            } else {
                objName = new ObjectName(base + jmxNamePrefix + i);
            }
            mbs.registerMBean(this, objName);
            objectName = objName;
            registered = true;
        }
        return objectName;
    }

    private String getStackTrace(Exception e) {
        Writer w = new StringWriter();
        PrintWriter pw = new PrintWriter(w);
        e.printStackTrace(pw);
        return w.toString();
    }

    /* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/impl/BaseGenericObjectPool$Evictor.class */
    class Evictor extends TimerTask {
        Evictor() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            ClassLoader savedClassLoader = Thread.currentThread().getContextClassLoader();
            try {
                if (BaseGenericObjectPool.this.factoryClassLoader != null) {
                    ClassLoader cl = (ClassLoader) BaseGenericObjectPool.this.factoryClassLoader.get();
                    if (cl == null) {
                        cancel();
                        Thread.currentThread().setContextClassLoader(savedClassLoader);
                        return;
                    }
                    Thread.currentThread().setContextClassLoader(cl);
                }
                try {
                    try {
                        BaseGenericObjectPool.this.evict();
                    } catch (Exception e) {
                        BaseGenericObjectPool.this.swallowException(e);
                    }
                } catch (OutOfMemoryError oome) {
                    oome.printStackTrace(System.err);
                }
                try {
                    BaseGenericObjectPool.this.ensureMinIdle();
                } catch (Exception e2) {
                    BaseGenericObjectPool.this.swallowException(e2);
                }
                Thread.currentThread().setContextClassLoader(savedClassLoader);
            } catch (Throwable th) {
                Thread.currentThread().setContextClassLoader(savedClassLoader);
                throw th;
            }
        }
    }

    /* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/impl/BaseGenericObjectPool$StatsStore.class */
    private class StatsStore {
        private final AtomicLong[] values;
        private final int size;
        private int index;

        public StatsStore(int size) {
            this.size = size;
            this.values = new AtomicLong[size];
            for (int i = 0; i < size; i++) {
                this.values[i] = new AtomicLong(-1L);
            }
        }

        public synchronized void add(long value) {
            this.values[this.index].set(value);
            this.index++;
            if (this.index == this.size) {
                this.index = 0;
            }
        }

        public long getMean() {
            double result = 0.0d;
            int counter = 0;
            for (int i = 0; i < this.size; i++) {
                long value = this.values[i].get();
                if (value != -1) {
                    counter++;
                    result = (result * ((counter - 1) / counter)) + (value / counter);
                }
            }
            return (long) result;
        }

        public String toString() {
            return "StatsStore [values=" + Arrays.toString(this.values) + ", size=" + this.size + ", index=" + this.index + "]";
        }
    }

    /* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/impl/BaseGenericObjectPool$EvictionIterator.class */
    class EvictionIterator implements Iterator<PooledObject<T>> {
        private final Deque<PooledObject<T>> idleObjects;
        private final Iterator<PooledObject<T>> idleObjectIterator;

        EvictionIterator(Deque<PooledObject<T>> idleObjects) {
            this.idleObjects = idleObjects;
            if (BaseGenericObjectPool.this.getLifo()) {
                this.idleObjectIterator = idleObjects.descendingIterator();
            } else {
                this.idleObjectIterator = idleObjects.iterator();
            }
        }

        public Deque<PooledObject<T>> getIdleObjects() {
            return this.idleObjects;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.idleObjectIterator.hasNext();
        }

        @Override // java.util.Iterator
        public PooledObject<T> next() {
            return this.idleObjectIterator.next();
        }

        @Override // java.util.Iterator
        public void remove() {
            this.idleObjectIterator.remove();
        }
    }

    /* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/impl/BaseGenericObjectPool$IdentityWrapper.class */
    static class IdentityWrapper<T> {
        private final T instance;

        public IdentityWrapper(T instance) {
            this.instance = instance;
        }

        public int hashCode() {
            return System.identityHashCode(this.instance);
        }

        public boolean equals(Object other) {
            return (other instanceof IdentityWrapper) && ((IdentityWrapper) other).instance == this.instance;
        }

        public T getObject() {
            return this.instance;
        }

        public String toString() {
            return "IdentityWrapper [instance=" + this.instance + "]";
        }
    }

    @Override // org.apache.commons.pool2.BaseObject
    protected void toStringAppendFields(StringBuilder builder) {
        builder.append("maxTotal=");
        builder.append(this.maxTotal);
        builder.append(", blockWhenExhausted=");
        builder.append(this.blockWhenExhausted);
        builder.append(", maxWaitMillis=");
        builder.append(this.maxWaitMillis);
        builder.append(", lifo=");
        builder.append(this.lifo);
        builder.append(", fairness=");
        builder.append(this.fairness);
        builder.append(", testOnCreate=");
        builder.append(this.testOnCreate);
        builder.append(", testOnBorrow=");
        builder.append(this.testOnBorrow);
        builder.append(", testOnReturn=");
        builder.append(this.testOnReturn);
        builder.append(", testWhileIdle=");
        builder.append(this.testWhileIdle);
        builder.append(", timeBetweenEvictionRunsMillis=");
        builder.append(this.timeBetweenEvictionRunsMillis);
        builder.append(", numTestsPerEvictionRun=");
        builder.append(this.numTestsPerEvictionRun);
        builder.append(", minEvictableIdleTimeMillis=");
        builder.append(this.minEvictableIdleTimeMillis);
        builder.append(", softMinEvictableIdleTimeMillis=");
        builder.append(this.softMinEvictableIdleTimeMillis);
        builder.append(", evictionPolicy=");
        builder.append(this.evictionPolicy);
        builder.append(", closeLock=");
        builder.append(this.closeLock);
        builder.append(", closed=");
        builder.append(this.closed);
        builder.append(", evictionLock=");
        builder.append(this.evictionLock);
        builder.append(", evictor=");
        builder.append(this.evictor);
        builder.append(", evictionIterator=");
        builder.append(this.evictionIterator);
        builder.append(", factoryClassLoader=");
        builder.append(this.factoryClassLoader);
        builder.append(", oname=");
        builder.append(this.oname);
        builder.append(", creationStackTrace=");
        builder.append(this.creationStackTrace);
        builder.append(", borrowedCount=");
        builder.append(this.borrowedCount);
        builder.append(", returnedCount=");
        builder.append(this.returnedCount);
        builder.append(", createdCount=");
        builder.append(this.createdCount);
        builder.append(", destroyedCount=");
        builder.append(this.destroyedCount);
        builder.append(", destroyedByEvictorCount=");
        builder.append(this.destroyedByEvictorCount);
        builder.append(", destroyedByBorrowValidationCount=");
        builder.append(this.destroyedByBorrowValidationCount);
        builder.append(", activeTimes=");
        builder.append(this.activeTimes);
        builder.append(", idleTimes=");
        builder.append(this.idleTimes);
        builder.append(", waitTimes=");
        builder.append(this.waitTimes);
        builder.append(", maxBorrowWaitTimeMillis=");
        builder.append(this.maxBorrowWaitTimeMillis);
        builder.append(", swallowedExceptionListener=");
        builder.append(this.swallowedExceptionListener);
    }
}

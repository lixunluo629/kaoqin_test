package org.apache.commons.pool2.proxy;

import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.UsageTracking;

/* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/proxy/ProxiedObjectPool.class */
public class ProxiedObjectPool<T> implements ObjectPool<T> {
    private final ObjectPool<T> pool;
    private final ProxySource<T> proxySource;

    public ProxiedObjectPool(ObjectPool<T> pool, ProxySource<T> proxySource) {
        this.pool = pool;
        this.proxySource = proxySource;
    }

    @Override // org.apache.commons.pool2.ObjectPool
    public T borrowObject() throws Exception {
        UsageTracking<T> usageTracking = null;
        if (this.pool instanceof UsageTracking) {
            usageTracking = (UsageTracking) this.pool;
        }
        T pooledObject = this.pool.borrowObject();
        T proxy = this.proxySource.createProxy(pooledObject, usageTracking);
        return proxy;
    }

    @Override // org.apache.commons.pool2.ObjectPool
    public void returnObject(T proxy) throws Exception {
        T pooledObject = this.proxySource.resolveProxy(proxy);
        this.pool.returnObject(pooledObject);
    }

    @Override // org.apache.commons.pool2.ObjectPool
    public void invalidateObject(T proxy) throws Exception {
        T pooledObject = this.proxySource.resolveProxy(proxy);
        this.pool.invalidateObject(pooledObject);
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
        this.pool.close();
    }

    public String toString() {
        return "ProxiedObjectPool [pool=" + this.pool + ", proxySource=" + this.proxySource + "]";
    }
}

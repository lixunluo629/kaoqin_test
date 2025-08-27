package org.apache.commons.pool2.proxy;

import org.apache.commons.pool2.KeyedObjectPool;
import org.apache.commons.pool2.UsageTracking;

/* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/proxy/ProxiedKeyedObjectPool.class */
public class ProxiedKeyedObjectPool<K, V> implements KeyedObjectPool<K, V> {
    private final KeyedObjectPool<K, V> pool;
    private final ProxySource<V> proxySource;

    public ProxiedKeyedObjectPool(KeyedObjectPool<K, V> pool, ProxySource<V> proxySource) {
        this.pool = pool;
        this.proxySource = proxySource;
    }

    @Override // org.apache.commons.pool2.KeyedObjectPool
    public V borrowObject(K key) throws Exception {
        UsageTracking<V> usageTracking = null;
        if (this.pool instanceof UsageTracking) {
            usageTracking = (UsageTracking) this.pool;
        }
        V pooledObject = this.pool.borrowObject(key);
        V proxy = this.proxySource.createProxy(pooledObject, usageTracking);
        return proxy;
    }

    @Override // org.apache.commons.pool2.KeyedObjectPool
    public void returnObject(K key, V proxy) throws Exception {
        V pooledObject = this.proxySource.resolveProxy(proxy);
        this.pool.returnObject(key, pooledObject);
    }

    @Override // org.apache.commons.pool2.KeyedObjectPool
    public void invalidateObject(K key, V proxy) throws Exception {
        V pooledObject = this.proxySource.resolveProxy(proxy);
        this.pool.invalidateObject(key, pooledObject);
    }

    @Override // org.apache.commons.pool2.KeyedObjectPool
    public void addObject(K key) throws Exception {
        this.pool.addObject(key);
    }

    @Override // org.apache.commons.pool2.KeyedObjectPool
    public int getNumIdle(K key) {
        return this.pool.getNumIdle(key);
    }

    @Override // org.apache.commons.pool2.KeyedObjectPool
    public int getNumActive(K key) {
        return this.pool.getNumActive(key);
    }

    @Override // org.apache.commons.pool2.KeyedObjectPool
    public int getNumIdle() {
        return this.pool.getNumIdle();
    }

    @Override // org.apache.commons.pool2.KeyedObjectPool
    public int getNumActive() {
        return this.pool.getNumActive();
    }

    @Override // org.apache.commons.pool2.KeyedObjectPool
    public void clear() throws Exception {
        this.pool.clear();
    }

    @Override // org.apache.commons.pool2.KeyedObjectPool
    public void clear(K key) throws Exception {
        this.pool.clear(key);
    }

    @Override // org.apache.commons.pool2.KeyedObjectPool
    public void close() {
        this.pool.close();
    }

    public String toString() {
        return "ProxiedKeyedObjectPool [pool=" + this.pool + ", proxySource=" + this.proxySource + "]";
    }
}

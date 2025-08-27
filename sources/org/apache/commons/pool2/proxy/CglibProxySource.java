package org.apache.commons.pool2.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Factory;
import org.apache.commons.pool2.UsageTracking;

/* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/proxy/CglibProxySource.class */
public class CglibProxySource<T> implements ProxySource<T> {
    private final Class<? extends T> superclass;

    public CglibProxySource(Class<? extends T> superclass) {
        this.superclass = superclass;
    }

    @Override // org.apache.commons.pool2.proxy.ProxySource
    public T createProxy(T t, UsageTracking<T> usageTracking) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(this.superclass);
        enhancer.setCallback(new CglibProxyHandler(t, usageTracking));
        return (T) enhancer.create();
    }

    @Override // org.apache.commons.pool2.proxy.ProxySource
    public T resolveProxy(T proxy) {
        CglibProxyHandler<T> cglibProxyHandler = (CglibProxyHandler) ((Factory) proxy).getCallback(0);
        T pooledObject = cglibProxyHandler.disableProxy();
        return pooledObject;
    }

    public String toString() {
        return "CglibProxySource [superclass=" + this.superclass + "]";
    }
}

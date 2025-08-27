package org.apache.commons.pool2.proxy;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import org.apache.commons.pool2.UsageTracking;

/* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/proxy/JdkProxySource.class */
public class JdkProxySource<T> implements ProxySource<T> {
    private final ClassLoader classLoader;
    private final Class<?>[] interfaces;

    public JdkProxySource(ClassLoader classLoader, Class<?>[] interfaces) {
        this.classLoader = classLoader;
        this.interfaces = new Class[interfaces.length];
        System.arraycopy(interfaces, 0, this.interfaces, 0, interfaces.length);
    }

    @Override // org.apache.commons.pool2.proxy.ProxySource
    public T createProxy(T t, UsageTracking<T> usageTracking) {
        return (T) Proxy.newProxyInstance(this.classLoader, this.interfaces, new JdkProxyHandler(t, usageTracking));
    }

    @Override // org.apache.commons.pool2.proxy.ProxySource
    public T resolveProxy(T proxy) {
        JdkProxyHandler<T> jdkProxyHandler = (JdkProxyHandler) Proxy.getInvocationHandler(proxy);
        T pooledObject = jdkProxyHandler.disableProxy();
        return pooledObject;
    }

    public String toString() {
        return "JdkProxySource [classLoader=" + this.classLoader + ", interfaces=" + Arrays.toString(this.interfaces) + "]";
    }
}

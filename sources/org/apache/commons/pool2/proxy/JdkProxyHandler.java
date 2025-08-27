package org.apache.commons.pool2.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import org.apache.commons.pool2.UsageTracking;

/* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/proxy/JdkProxyHandler.class */
class JdkProxyHandler<T> extends BaseProxyHandler<T> implements InvocationHandler {
    JdkProxyHandler(T pooledObject, UsageTracking<T> usageTracking) {
        super(pooledObject, usageTracking);
    }

    @Override // java.lang.reflect.InvocationHandler
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return doInvoke(method, args);
    }
}

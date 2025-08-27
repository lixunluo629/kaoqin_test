package org.apache.commons.pool2.proxy;

import java.lang.reflect.Method;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.commons.pool2.UsageTracking;

/* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/proxy/CglibProxyHandler.class */
class CglibProxyHandler<T> extends BaseProxyHandler<T> implements MethodInterceptor {
    CglibProxyHandler(T pooledObject, UsageTracking<T> usageTracking) {
        super(pooledObject, usageTracking);
    }

    @Override // net.sf.cglib.proxy.MethodInterceptor
    public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        return doInvoke(method, args);
    }
}

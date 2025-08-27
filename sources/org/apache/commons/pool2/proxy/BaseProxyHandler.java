package org.apache.commons.pool2.proxy;

import java.lang.reflect.Method;
import org.apache.commons.pool2.UsageTracking;

/* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/proxy/BaseProxyHandler.class */
class BaseProxyHandler<T> {
    private volatile T pooledObject;
    private final UsageTracking<T> usageTracking;

    BaseProxyHandler(T pooledObject, UsageTracking<T> usageTracking) {
        this.pooledObject = pooledObject;
        this.usageTracking = usageTracking;
    }

    T getPooledObject() {
        return this.pooledObject;
    }

    T disableProxy() {
        T result = this.pooledObject;
        this.pooledObject = null;
        return result;
    }

    void validateProxiedObject() {
        if (this.pooledObject == null) {
            throw new IllegalStateException("This object may no longer be used as it has been returned to the Object Pool.");
        }
    }

    Object doInvoke(Method method, Object[] args) throws Throwable {
        validateProxiedObject();
        T object = getPooledObject();
        if (this.usageTracking != null) {
            this.usageTracking.use(object);
        }
        return method.invoke(object, args);
    }

    public String toString() {
        return getClass().getName() + " [pooledObject=" + this.pooledObject + ", usageTracking=" + this.usageTracking + "]";
    }
}

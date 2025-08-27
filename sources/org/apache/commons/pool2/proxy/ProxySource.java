package org.apache.commons.pool2.proxy;

import org.apache.commons.pool2.UsageTracking;

/* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/proxy/ProxySource.class */
interface ProxySource<T> {
    T createProxy(T t, UsageTracking<T> usageTracking);

    T resolveProxy(T t);
}

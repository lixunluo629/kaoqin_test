package org.ehcache.spi.service;

import java.util.Collection;
import org.ehcache.spi.service.Service;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/spi/service/ServiceProvider.class */
public interface ServiceProvider<T extends Service> {
    /* JADX WARN: Incorrect return type in method signature: <U:TT;>(Ljava/lang/Class<TU;>;)TU; */
    Service getService(Class cls);

    <U extends T> Collection<U> getServicesOfType(Class<U> cls);
}

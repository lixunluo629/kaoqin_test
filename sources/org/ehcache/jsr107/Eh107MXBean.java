package org.ehcache.jsr107;

import java.net.URI;
import javax.cache.CacheException;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/Eh107MXBean.class */
abstract class Eh107MXBean {
    private final ObjectName objectName;

    Eh107MXBean(String cacheName, Eh107CacheManager cacheManager, String beanName) {
        this.objectName = createObjectName(cacheName, cacheManager, beanName);
    }

    private String sanitize(String string) {
        return string == null ? "" : string.replaceAll(",|:|=|\n", ".");
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.cache.CacheException */
    private ObjectName createObjectName(String cacheName, Eh107CacheManager cacheManager, String beanName) throws CacheException {
        URI uri = cacheManager.getURI();
        String cacheManagerName = sanitize(uri != null ? uri.toString() : "null");
        try {
            return new ObjectName("javax.cache:type=" + beanName + ",CacheManager=" + cacheManagerName + ",Cache=" + sanitize(cacheName != null ? cacheName : "null"));
        } catch (MalformedObjectNameException e) {
            throw new CacheException(e);
        }
    }

    ObjectName getObjectName() {
        return this.objectName;
    }
}

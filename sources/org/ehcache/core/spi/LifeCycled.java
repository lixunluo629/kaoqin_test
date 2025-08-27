package org.ehcache.core.spi;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/spi/LifeCycled.class */
public interface LifeCycled {
    void init() throws Exception;

    void close() throws Exception;
}

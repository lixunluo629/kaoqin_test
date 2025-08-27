package org.ehcache.core.exceptions;

import org.ehcache.spi.loaderwriter.CacheLoadingException;
import org.ehcache.spi.loaderwriter.CacheWritingException;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/exceptions/ExceptionFactory.class */
public final class ExceptionFactory {
    private ExceptionFactory() {
        throw new UnsupportedOperationException("Thou shalt not instantiate me!");
    }

    public static CacheWritingException newCacheWritingException(Exception e) {
        return new CacheWritingException(e);
    }

    public static CacheLoadingException newCacheLoadingException(Exception e) {
        return new CacheLoadingException(e);
    }
}

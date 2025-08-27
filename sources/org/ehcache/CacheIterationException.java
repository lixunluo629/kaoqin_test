package org.ehcache;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/CacheIterationException.class */
public class CacheIterationException extends RuntimeException {
    private static final long serialVersionUID = -2008756317259206440L;

    public CacheIterationException() {
    }

    public CacheIterationException(String message) {
        super(message);
    }

    public CacheIterationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CacheIterationException(Throwable cause) {
        super(cause);
    }
}

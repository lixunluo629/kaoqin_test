package org.ehcache.spi.loaderwriter;

import java.util.Collections;
import java.util.Map;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/spi/loaderwriter/BulkCacheLoadingException.class */
public class BulkCacheLoadingException extends CacheLoadingException {
    private static final long serialVersionUID = -5296309607929568779L;
    private final Map<?, Exception> failures;
    private final Map<?, ?> successes;

    public BulkCacheLoadingException(Map<?, Exception> failures, Map<?, ?> successes) {
        this.failures = Collections.unmodifiableMap(failures);
        this.successes = Collections.unmodifiableMap(successes);
    }

    public BulkCacheLoadingException(String message, Map<Object, Exception> failures, Map<Object, Object> successes) {
        super(message);
        this.failures = Collections.unmodifiableMap(failures);
        this.successes = Collections.unmodifiableMap(successes);
    }

    public Map<?, Exception> getFailures() {
        return this.failures;
    }

    public Map<?, ?> getSuccesses() {
        return this.successes;
    }
}

package org.ehcache.spi.loaderwriter;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/spi/loaderwriter/BulkCacheWritingException.class */
public class BulkCacheWritingException extends CacheWritingException {
    private static final long serialVersionUID = -9019459887947633422L;
    private final Map<?, Exception> failures;
    private final Set<?> successes;

    public BulkCacheWritingException(Map<?, Exception> failures, Set<?> successes) {
        this.failures = Collections.unmodifiableMap(failures);
        this.successes = Collections.unmodifiableSet(successes);
    }

    public Map<?, Exception> getFailures() {
        return this.failures;
    }

    public Set<?> getSuccesses() {
        return this.successes;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("Failed keys :");
        for (Map.Entry<?, Exception> entry : this.failures.entrySet()) {
            sb.append("\n ").append(entry.getKey()).append(" : ").append(entry.getValue());
        }
        return sb.toString();
    }
}

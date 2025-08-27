package org.springframework.cache.interceptor;

import org.springframework.cache.interceptor.CacheOperation;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/cache/interceptor/CachePutOperation.class */
public class CachePutOperation extends CacheOperation {
    private final String unless;

    public CachePutOperation(Builder b) {
        super(b);
        this.unless = b.unless;
    }

    public String getUnless() {
        return this.unless;
    }

    /* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/cache/interceptor/CachePutOperation$Builder.class */
    public static class Builder extends CacheOperation.Builder {
        private String unless;

        public void setUnless(String unless) {
            this.unless = unless;
        }

        @Override // org.springframework.cache.interceptor.CacheOperation.Builder
        protected StringBuilder getOperationDescription() {
            StringBuilder sb = super.getOperationDescription();
            sb.append(" | unless='");
            sb.append(this.unless);
            sb.append("'");
            return sb;
        }

        @Override // org.springframework.cache.interceptor.CacheOperation.Builder
        public CachePutOperation build() {
            return new CachePutOperation(this);
        }
    }
}

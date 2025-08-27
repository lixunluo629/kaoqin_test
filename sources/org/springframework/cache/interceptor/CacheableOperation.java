package org.springframework.cache.interceptor;

import org.springframework.cache.interceptor.CacheOperation;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/cache/interceptor/CacheableOperation.class */
public class CacheableOperation extends CacheOperation {
    private final String unless;
    private final boolean sync;

    public CacheableOperation(Builder b) {
        super(b);
        this.unless = b.unless;
        this.sync = b.sync;
    }

    public String getUnless() {
        return this.unless;
    }

    public boolean isSync() {
        return this.sync;
    }

    /* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/cache/interceptor/CacheableOperation$Builder.class */
    public static class Builder extends CacheOperation.Builder {
        private String unless;
        private boolean sync;

        public void setUnless(String unless) {
            this.unless = unless;
        }

        public void setSync(boolean sync) {
            this.sync = sync;
        }

        @Override // org.springframework.cache.interceptor.CacheOperation.Builder
        protected StringBuilder getOperationDescription() {
            StringBuilder sb = super.getOperationDescription();
            sb.append(" | unless='");
            sb.append(this.unless);
            sb.append("'");
            sb.append(" | sync='");
            sb.append(this.sync);
            sb.append("'");
            return sb;
        }

        @Override // org.springframework.cache.interceptor.CacheOperation.Builder
        public CacheableOperation build() {
            return new CacheableOperation(this);
        }
    }
}

package org.ehcache.expiry;

import org.ehcache.ValueSupplier;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/expiry/Expirations.class */
public final class Expirations {
    public static Expiry<Object, Object> noExpiration() {
        return NoExpiry.INSTANCE;
    }

    public static Expiry<Object, Object> timeToLiveExpiration(Duration timeToLive) {
        if (timeToLive == null) {
            throw new NullPointerException("Duration cannot be null");
        }
        return new TimeToLiveExpiry(timeToLive);
    }

    public static Expiry<Object, Object> timeToIdleExpiration(Duration timeToIdle) {
        if (timeToIdle == null) {
            throw new NullPointerException("Duration cannot be null");
        }
        return new TimeToIdleExpiry(timeToIdle);
    }

    private Expirations() {
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/expiry/Expirations$BaseExpiry.class */
    private static abstract class BaseExpiry implements Expiry<Object, Object> {
        private final Duration create;
        private final Duration access;
        private final Duration update;

        BaseExpiry(Duration create, Duration access, Duration update) {
            this.create = create;
            this.access = access;
            this.update = update;
        }

        @Override // org.ehcache.expiry.Expiry
        public Duration getExpiryForCreation(Object key, Object value) {
            return this.create;
        }

        @Override // org.ehcache.expiry.Expiry
        public Duration getExpiryForAccess(Object key, ValueSupplier<? extends Object> valueSupplier) {
            return this.access;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            BaseExpiry that = (BaseExpiry) o;
            if (this.access != null) {
                if (!this.access.equals(that.access)) {
                    return false;
                }
            } else if (that.access != null) {
                return false;
            }
            if (this.create != null) {
                if (!this.create.equals(that.create)) {
                    return false;
                }
            } else if (that.create != null) {
                return false;
            }
            return this.update != null ? this.update.equals(that.update) : that.update == null;
        }

        public int hashCode() {
            int result = this.create != null ? this.create.hashCode() : 0;
            return (31 * ((31 * result) + (this.access != null ? this.access.hashCode() : 0))) + (this.update != null ? this.update.hashCode() : 0);
        }

        public String toString() {
            return getClass().getSimpleName() + "{create=" + this.create + ", access=" + this.access + ", update=" + this.update + '}';
        }

        @Override // org.ehcache.expiry.Expiry
        public Duration getExpiryForUpdate(Object key, ValueSupplier<? extends Object> valueSupplier, Object newValue) {
            return this.update;
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/expiry/Expirations$TimeToLiveExpiry.class */
    private static class TimeToLiveExpiry extends BaseExpiry {
        TimeToLiveExpiry(Duration ttl) {
            super(ttl, null, ttl);
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/expiry/Expirations$TimeToIdleExpiry.class */
    private static class TimeToIdleExpiry extends BaseExpiry {
        TimeToIdleExpiry(Duration tti) {
            super(tti, tti, tti);
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/expiry/Expirations$NoExpiry.class */
    private static class NoExpiry extends BaseExpiry {
        private static final Expiry<Object, Object> INSTANCE = new NoExpiry();

        private NoExpiry() {
            super(Duration.INFINITE, null, null);
        }
    }
}

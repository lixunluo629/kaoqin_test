package org.aspectj.weaver.tools.cache;

import org.springframework.beans.PropertyAccessor;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/cache/CachedClassReference.class */
public class CachedClassReference {
    private final String key;
    private final String className;

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/cache/CachedClassReference$EntryType.class */
    enum EntryType {
        GENERATED,
        WEAVED,
        IGNORED
    }

    protected CachedClassReference(String key, CacheKeyResolver resolver) {
        this(key, resolver.keyToClass(key));
    }

    protected CachedClassReference(String key, String className) {
        this.key = key;
        this.className = className;
    }

    public String getKey() {
        return this.key;
    }

    public String getClassName() {
        return this.className;
    }

    public int hashCode() {
        return getKey().hashCode() + getClassName().hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        CachedClassReference other = (CachedClassReference) obj;
        if (getKey().equals(other.getKey()) && getClassName().equals(other.getClassName())) {
            return true;
        }
        return false;
    }

    public String toString() {
        return getClassName() + PropertyAccessor.PROPERTY_KEY_PREFIX + getKey() + "]";
    }
}

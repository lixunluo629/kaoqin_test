package org.aspectj.weaver.tools.cache;

import org.springframework.beans.PropertyAccessor;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/cache/CachedClassEntry.class */
public class CachedClassEntry {
    private final CachedClassReference ref;
    private final byte[] weavedBytes;
    private final EntryType type;

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/cache/CachedClassEntry$EntryType.class */
    enum EntryType {
        GENERATED,
        WEAVED,
        IGNORED
    }

    public CachedClassEntry(CachedClassReference ref, byte[] weavedBytes, EntryType type) {
        this.weavedBytes = weavedBytes;
        this.ref = ref;
        this.type = type;
    }

    public String getClassName() {
        return this.ref.getClassName();
    }

    public byte[] getBytes() {
        return this.weavedBytes;
    }

    public String getKey() {
        return this.ref.getKey();
    }

    public boolean isGenerated() {
        return this.type == EntryType.GENERATED;
    }

    public boolean isWeaved() {
        return this.type == EntryType.WEAVED;
    }

    public boolean isIgnored() {
        return this.type == EntryType.IGNORED;
    }

    public int hashCode() {
        return getClassName().hashCode() + getKey().hashCode() + this.type.hashCode();
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
        CachedClassEntry other = (CachedClassEntry) obj;
        if (getClassName().equals(other.getClassName()) && getKey().equals(other.getKey()) && this.type == other.type) {
            return true;
        }
        return false;
    }

    public String toString() {
        return getClassName() + PropertyAccessor.PROPERTY_KEY_PREFIX + this.type + "]";
    }
}

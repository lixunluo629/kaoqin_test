package com.fasterxml.classmate.util;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.types.TypePlaceHolder;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classmate-1.3.4.jar:com/fasterxml/classmate/util/ResolvedTypeCache.class */
public class ResolvedTypeCache implements Serializable {
    protected final CacheMap _map;

    public ResolvedTypeCache(int maxEntries) {
        this._map = new CacheMap(maxEntries);
    }

    public Key key(Class<?> simpleType) {
        return new Key(simpleType);
    }

    public Key key(Class<?> simpleType, ResolvedType[] tp) {
        int len = tp == null ? 0 : tp.length;
        if (len == 0) {
            return new Key(simpleType);
        }
        for (int i = 0; i < len; i++) {
            if (tp[i] instanceof TypePlaceHolder) {
                return null;
            }
        }
        return new Key(simpleType, tp);
    }

    public synchronized ResolvedType find(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("Null key not allowed");
        }
        return this._map.get(key);
    }

    public synchronized int size() {
        return this._map.size();
    }

    public synchronized void put(Key key, ResolvedType type) {
        if (key == null) {
            throw new IllegalArgumentException("Null key not allowed");
        }
        this._map.put(key, type);
    }

    protected void addForTest(ResolvedType type) {
        List<ResolvedType> tp = type.getTypeParameters();
        ResolvedType[] tpa = (ResolvedType[]) tp.toArray(new ResolvedType[tp.size()]);
        put(key(type.getErasedType(), tpa), type);
    }

    /* loaded from: classmate-1.3.4.jar:com/fasterxml/classmate/util/ResolvedTypeCache$Key.class */
    public static class Key {
        private final Class<?> _erasedType;
        private final ResolvedType[] _typeParameters;
        private final int _hashCode;

        public Key(Class<?> simpleType) {
            this(simpleType, null);
        }

        public Key(Class<?> erasedType, ResolvedType[] tp) {
            if (tp != null && tp.length == 0) {
                tp = null;
            }
            this._erasedType = erasedType;
            this._typeParameters = tp;
            int h = erasedType.getName().hashCode();
            this._hashCode = tp != null ? h + tp.length : h;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("[CacheKey: ");
            sb.append(this._erasedType.getName()).append('(');
            if (this._typeParameters != null) {
                for (int i = 0; i < this._typeParameters.length; i++) {
                    if (i > 0) {
                        sb.append(',');
                    }
                    sb.append(this._typeParameters[i]);
                }
            }
            sb.append(")]");
            return sb.toString();
        }

        public int hashCode() {
            return this._hashCode;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o == null || o.getClass() != getClass()) {
                return false;
            }
            Key other = (Key) o;
            if (other._erasedType != this._erasedType) {
                return false;
            }
            ResolvedType[] otherTP = other._typeParameters;
            if (this._typeParameters == null) {
                return otherTP == null;
            }
            if (otherTP == null || otherTP.length != this._typeParameters.length) {
                return false;
            }
            int len = this._typeParameters.length;
            for (int i = 0; i < len; i++) {
                if (!this._typeParameters[i].equals(otherTP[i])) {
                    return false;
                }
            }
            return true;
        }
    }

    /* loaded from: classmate-1.3.4.jar:com/fasterxml/classmate/util/ResolvedTypeCache$CacheMap.class */
    private static final class CacheMap extends LinkedHashMap<Key, ResolvedType> {
        protected final int _maxEntries;

        public CacheMap(int maxEntries) {
            this._maxEntries = maxEntries;
        }

        @Override // java.util.LinkedHashMap
        protected boolean removeEldestEntry(Map.Entry<Key, ResolvedType> eldest) {
            return size() > this._maxEntries;
        }
    }
}

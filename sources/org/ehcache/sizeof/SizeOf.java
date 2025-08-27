package org.ehcache.sizeof;

import org.ehcache.sizeof.ObjectGraphWalker;
import org.ehcache.sizeof.filters.CombinationSizeOfFilter;
import org.ehcache.sizeof.filters.SizeOfFilter;
import org.ehcache.sizeof.impl.AgentSizeOf;
import org.ehcache.sizeof.impl.ReflectionSizeOf;
import org.ehcache.sizeof.impl.UnsafeSizeOf;
import org.ehcache.sizeof.util.WeakIdentityConcurrentMap;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/sizeof/SizeOf.class */
public abstract class SizeOf {
    private final ObjectGraphWalker walker;

    public abstract long sizeOf(Object obj);

    public SizeOf(SizeOfFilter fieldFilter, boolean caching, boolean bypassFlyweight) {
        ObjectGraphWalker.Visitor visitor;
        if (caching) {
            visitor = new CachingSizeOfVisitor();
        } else {
            visitor = new SizeOfVisitor();
        }
        this.walker = new ObjectGraphWalker(visitor, fieldFilter, bypassFlyweight);
    }

    public long deepSizeOf(VisitorListener listener, Object... obj) {
        return this.walker.walk(listener, obj);
    }

    public long deepSizeOf(Object... obj) {
        return this.walker.walk(null, obj);
    }

    public static SizeOf newInstance(SizeOfFilter... filters) {
        return newInstance(true, true, filters);
    }

    public static SizeOf newInstance(boolean bypassFlyweight, boolean cache, SizeOfFilter... filters) {
        SizeOfFilter filter = new CombinationSizeOfFilter(filters);
        try {
            return new AgentSizeOf(filter, cache, bypassFlyweight);
        } catch (UnsupportedOperationException e) {
            try {
                return new UnsafeSizeOf(filter, cache, bypassFlyweight);
            } catch (UnsupportedOperationException f) {
                try {
                    return new ReflectionSizeOf(filter, cache, bypassFlyweight);
                } catch (UnsupportedOperationException g) {
                    throw new UnsupportedOperationException("A suitable SizeOf engine could not be loaded: " + e + ", " + f + ", " + g);
                }
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/sizeof/SizeOf$SizeOfVisitor.class */
    private class SizeOfVisitor implements ObjectGraphWalker.Visitor {
        private SizeOfVisitor() {
        }

        @Override // org.ehcache.sizeof.ObjectGraphWalker.Visitor
        public long visit(Object object) {
            return SizeOf.this.sizeOf(object);
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/sizeof/SizeOf$CachingSizeOfVisitor.class */
    private class CachingSizeOfVisitor implements ObjectGraphWalker.Visitor {
        private final WeakIdentityConcurrentMap<Class<?>, Long> cache;

        private CachingSizeOfVisitor() {
            this.cache = new WeakIdentityConcurrentMap<>();
        }

        @Override // org.ehcache.sizeof.ObjectGraphWalker.Visitor
        public long visit(Object object) {
            Class<?> klazz = object.getClass();
            Long cachedSize = this.cache.get(klazz);
            if (cachedSize == null) {
                if (klazz.isArray()) {
                    return SizeOf.this.sizeOf(object);
                }
                long size = SizeOf.this.sizeOf(object);
                this.cache.put(klazz, Long.valueOf(size));
                return size;
            }
            return cachedSize.longValue();
        }
    }
}

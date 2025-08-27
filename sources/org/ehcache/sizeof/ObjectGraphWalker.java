package org.ehcache.sizeof;

import java.lang.ref.SoftReference;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Stack;
import org.ehcache.sizeof.filters.SizeOfFilter;
import org.ehcache.sizeof.util.WeakIdentityConcurrentMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/sizeof/ObjectGraphWalker.class */
final class ObjectGraphWalker {
    private static final String VERBOSE_DEBUG_LOGGING = "org.ehcache.sizeof.verboseDebugLogging";
    private static final String CONTINUE_MESSAGE = "The configured limit of {0} object references was reached while attempting to calculate the size of the object graph. Severe performance degradation could occur if the sizing operation continues. This can be avoided by setting the CacheManger or Cache <sizeOfPolicy> element's maxDepthExceededBehavior to \"abort\" or adding stop points with @IgnoreSizeOf annotations. If performance degradation is NOT an issue at the configured limit, raise the limit value using the CacheManager or Cache <sizeOfPolicy> element's maxDepth attribute. For more information, see the Ehcache configuration documentation.";
    private static final String ABORT_MESSAGE = "The configured limit of {0} object references was reached while attempting to calculate the size of the object graph. This can be avoided by adding stop points with @IgnoreSizeOf annotations. Since the CacheManger or Cache <sizeOfPolicy> element's maxDepthExceededBehavior is set to \"abort\", the sizing operation has stopped and the reported cache size is not accurate. If performance degradation is NOT an issue at the configured limit, raise the limit value using the CacheManager or Cache <sizeOfPolicy> element's maxDepth attribute. For more information, see the Ehcache configuration documentation.";
    private final WeakIdentityConcurrentMap<Class<?>, SoftReference<Collection<Field>>> fieldCache = new WeakIdentityConcurrentMap<>();
    private final WeakIdentityConcurrentMap<Class<?>, Boolean> classCache = new WeakIdentityConcurrentMap<>();
    private final boolean bypassFlyweight;
    private final SizeOfFilter sizeOfFilter;
    private final Visitor visitor;
    private static final Logger LOG = LoggerFactory.getLogger((Class<?>) ObjectGraphWalker.class);
    private static final boolean USE_VERBOSE_DEBUG_LOGGING = getVerboseSizeOfDebugLogging();

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/sizeof/ObjectGraphWalker$Visitor.class */
    interface Visitor {
        long visit(Object obj);
    }

    ObjectGraphWalker(Visitor visitor, SizeOfFilter filter, boolean bypassFlyweight) {
        if (visitor == null) {
            throw new NullPointerException("Visitor can't be null");
        }
        if (filter == null) {
            throw new NullPointerException("SizeOfFilter can't be null");
        }
        this.visitor = visitor;
        this.sizeOfFilter = filter;
        this.bypassFlyweight = bypassFlyweight;
    }

    private static boolean getVerboseSizeOfDebugLogging() {
        String verboseString = System.getProperty(VERBOSE_DEBUG_LOGGING, "false").toLowerCase();
        return verboseString.equals("true");
    }

    long walk(Object... root) {
        return walk(null, root);
    }

    long walk(VisitorListener visitorListener, Object... root) {
        StringBuilder traversalDebugMessage;
        if (USE_VERBOSE_DEBUG_LOGGING && LOG.isDebugEnabled()) {
            traversalDebugMessage = new StringBuilder();
        } else {
            traversalDebugMessage = null;
        }
        long result = 0;
        Stack<Object> toVisit = new Stack<>();
        IdentityHashMap<Object, Object> visited = new IdentityHashMap<>();
        if (root != null) {
            if (traversalDebugMessage != null) {
                traversalDebugMessage.append("visiting ");
            }
            for (Object object : root) {
                nullSafeAdd(toVisit, object);
                if (traversalDebugMessage != null && object != null) {
                    traversalDebugMessage.append(object.getClass().getName()).append("@").append(System.identityHashCode(object)).append(", ");
                }
            }
            if (traversalDebugMessage != null) {
                traversalDebugMessage.deleteCharAt(traversalDebugMessage.length() - 2).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
            }
        }
        while (!toVisit.isEmpty()) {
            Object ref = toVisit.pop();
            if (!visited.containsKey(ref)) {
                Class<?> refClass = ref.getClass();
                if (!byPassIfFlyweight(ref) && shouldWalkClass(refClass)) {
                    if (refClass.isArray() && !refClass.getComponentType().isPrimitive()) {
                        for (int i = 0; i < Array.getLength(ref); i++) {
                            nullSafeAdd(toVisit, Array.get(ref, i));
                        }
                    } else {
                        for (Field field : getFilteredFields(refClass)) {
                            try {
                                nullSafeAdd(toVisit, field.get(ref));
                            } catch (IllegalAccessException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
                    long visitSize = this.visitor.visit(ref);
                    if (visitorListener != null) {
                        visitorListener.visited(ref, visitSize);
                    }
                    if (traversalDebugMessage != null) {
                        traversalDebugMessage.append("  ").append(visitSize).append("b\t\t").append(ref.getClass().getName()).append("@").append(System.identityHashCode(ref)).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
                    }
                    result += visitSize;
                } else if (traversalDebugMessage != null) {
                    traversalDebugMessage.append("  ignored\t").append(ref.getClass().getName()).append("@").append(System.identityHashCode(ref)).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
                }
                visited.put(ref, null);
            }
        }
        if (traversalDebugMessage != null) {
            traversalDebugMessage.append("Total size: ").append(result).append(" bytes\n");
            LOG.debug(traversalDebugMessage.toString());
        }
        return result;
    }

    private boolean checkMaxDepth(int maxDepth, boolean abortWhenMaxDepthExceeded, boolean warned, IdentityHashMap<Object, Object> visited) {
        if (visited.size() >= maxDepth) {
            if (abortWhenMaxDepthExceeded) {
                throw new IllegalArgumentException(MessageFormat.format(ABORT_MESSAGE, Integer.valueOf(maxDepth)));
            }
            if (!warned) {
                LOG.warn(MessageFormat.format(CONTINUE_MESSAGE, Integer.valueOf(maxDepth)));
                warned = true;
            }
        }
        return warned;
    }

    private Collection<Field> getFilteredFields(Class<?> refClass) {
        SoftReference<Collection<Field>> ref = this.fieldCache.get(refClass);
        Collection<Field> fieldList = ref != null ? ref.get() : null;
        if (fieldList != null) {
            return fieldList;
        }
        Collection<Field> result = this.sizeOfFilter.filterFields(refClass, getAllFields(refClass));
        if (USE_VERBOSE_DEBUG_LOGGING && LOG.isDebugEnabled()) {
            for (Field field : result) {
                if (Modifier.isTransient(field.getModifiers())) {
                    LOG.debug("SizeOf engine walking transient field '{}' of class {}", field.getName(), refClass.getName());
                }
            }
        }
        this.fieldCache.put(refClass, new SoftReference<>(result));
        return result;
    }

    private boolean shouldWalkClass(Class<?> refClass) {
        Boolean cached = this.classCache.get(refClass);
        if (cached == null) {
            cached = Boolean.valueOf(this.sizeOfFilter.filterClass(refClass));
            this.classCache.put(refClass, cached);
        }
        return cached.booleanValue();
    }

    private static void nullSafeAdd(Stack<Object> toVisit, Object o) {
        if (o != null) {
            toVisit.push(o);
        }
    }

    private static Collection<Field> getAllFields(Class<?> refClass) {
        Collection<Field> fields = new ArrayList<>();
        Class<?> superclass = refClass;
        while (true) {
            Class<?> klazz = superclass;
            if (klazz != null) {
                Field[] arr$ = klazz.getDeclaredFields();
                for (Field field : arr$) {
                    if (!Modifier.isStatic(field.getModifiers()) && !field.getType().isPrimitive()) {
                        try {
                            field.setAccessible(true);
                            fields.add(field);
                        } catch (SecurityException e) {
                            LOG.error("Security settings prevent Ehcache from accessing the subgraph beneath '{}' - cache sizes may be underestimated as a result", field, e);
                        }
                    }
                }
                superclass = klazz.getSuperclass();
            } else {
                return fields;
            }
        }
    }

    private boolean byPassIfFlyweight(Object obj) {
        FlyweightType type;
        return this.bypassFlyweight && (type = FlyweightType.getFlyweightType(obj.getClass())) != null && type.isShared(obj);
    }
}

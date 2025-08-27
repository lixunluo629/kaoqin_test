package javax.el;

import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* loaded from: tomcat-embed-el-8.5.43.jar:javax/el/Util.class */
class Util {
    private static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];
    private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
    private static final CacheValue nullTcclFactory = new CacheValue();
    private static final ConcurrentMap<CacheKey, CacheValue> factoryCache = new ConcurrentHashMap();

    Util() {
    }

    static void handleThrowable(Throwable t) {
        if (t instanceof ThreadDeath) {
            throw ((ThreadDeath) t);
        }
        if (t instanceof VirtualMachineError) {
            throw ((VirtualMachineError) t);
        }
    }

    static String message(ELContext context, String name, Object... props) {
        Locale locale = null;
        if (context != null) {
            locale = context.getLocale();
        }
        if (locale == null) {
            locale = Locale.getDefault();
            if (locale == null) {
                return "";
            }
        }
        ResourceBundle bundle = ResourceBundle.getBundle("javax.el.LocalStrings", locale);
        try {
            String template = bundle.getString(name);
            if (props != null) {
                template = MessageFormat.format(template, props);
            }
            return template;
        } catch (MissingResourceException e) {
            return "Missing Resource: '" + name + "' for Locale " + locale.getDisplayName();
        }
    }

    static ExpressionFactory getExpressionFactory() {
        CacheValue cacheValue;
        ClassLoader tccl = getContextClassLoader();
        if (tccl == null) {
            cacheValue = nullTcclFactory;
        } else {
            CacheKey key = new CacheKey(tccl);
            cacheValue = factoryCache.get(key);
            if (cacheValue == null) {
                CacheValue newCacheValue = new CacheValue();
                cacheValue = factoryCache.putIfAbsent(key, newCacheValue);
                if (cacheValue == null) {
                    cacheValue = newCacheValue;
                }
            }
        }
        Lock readLock = cacheValue.getLock().readLock();
        readLock.lock();
        try {
            ExpressionFactory factory = cacheValue.getExpressionFactory();
            readLock.unlock();
            if (factory == null) {
                Lock writeLock = cacheValue.getLock().writeLock();
                writeLock.lock();
                try {
                    factory = cacheValue.getExpressionFactory();
                    if (factory == null) {
                        factory = ExpressionFactory.newInstance();
                        cacheValue.setExpressionFactory(factory);
                    }
                } finally {
                    writeLock.unlock();
                }
            }
            return factory;
        } catch (Throwable th) {
            readLock.unlock();
            throw th;
        }
    }

    /* loaded from: tomcat-embed-el-8.5.43.jar:javax/el/Util$CacheKey.class */
    private static class CacheKey {
        private final int hash;
        private final WeakReference<ClassLoader> ref;

        public CacheKey(ClassLoader key) {
            this.hash = key.hashCode();
            this.ref = new WeakReference<>(key);
        }

        public int hashCode() {
            return this.hash;
        }

        public boolean equals(Object obj) {
            ClassLoader thisKey;
            if (obj == this) {
                return true;
            }
            return (obj instanceof CacheKey) && (thisKey = this.ref.get()) != null && thisKey == ((CacheKey) obj).ref.get();
        }
    }

    /* loaded from: tomcat-embed-el-8.5.43.jar:javax/el/Util$CacheValue.class */
    private static class CacheValue {
        private final ReadWriteLock lock = new ReentrantReadWriteLock();
        private WeakReference<ExpressionFactory> ref;

        public ReadWriteLock getLock() {
            return this.lock;
        }

        public ExpressionFactory getExpressionFactory() {
            if (this.ref != null) {
                return this.ref.get();
            }
            return null;
        }

        public void setExpressionFactory(ExpressionFactory factory) {
            this.ref = new WeakReference<>(factory);
        }
    }

    static Method findMethod(Class<?> clazz, String methodName, Class<?>[] paramTypes, Object[] paramValues) throws SecurityException {
        if (clazz == null || methodName == null) {
            throw new MethodNotFoundException(message(null, "util.method.notfound", clazz, methodName, paramString(paramTypes)));
        }
        if (paramTypes == null) {
            paramTypes = getTypesFromValues(paramValues);
        }
        Method[] methods = clazz.getMethods();
        List<Wrapper> wrappers = Wrapper.wrap(methods, methodName);
        Wrapper result = findWrapper(clazz, wrappers, methodName, paramTypes, paramValues);
        return getMethod(clazz, (Method) result.unWrap());
    }

    /* JADX WARN: Code restructure failed: missing block: B:59:0x0105, code lost:
    
        r21 = r21 + 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static javax.el.Util.Wrapper findWrapper(java.lang.Class<?> r9, java.util.List<javax.el.Util.Wrapper> r10, java.lang.String r11, java.lang.Class<?>[] r12, java.lang.Object[] r13) {
        /*
            Method dump skipped, instructions count: 732
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.el.Util.findWrapper(java.lang.Class, java.util.List, java.lang.String, java.lang.Class[], java.lang.Object[]):javax.el.Util$Wrapper");
    }

    private static final String paramString(Class<?>[] types) {
        if (types != null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < types.length; i++) {
                if (types[i] == null) {
                    sb.append("null, ");
                } else {
                    sb.append(types[i].getName()).append(", ");
                }
            }
            if (sb.length() > 2) {
                sb.setLength(sb.length() - 2);
            }
            return sb.toString();
        }
        return null;
    }

    private static Wrapper resolveAmbiguousWrapper(Set<Wrapper> candidates, Class<?>[] paramTypes) {
        Wrapper w = candidates.iterator().next();
        int nonMatchIndex = 0;
        Class<?> nonMatchClass = null;
        int i = 0;
        while (true) {
            if (i >= paramTypes.length) {
                break;
            }
            if (w.getParameterTypes()[i] == paramTypes[i]) {
                i++;
            } else {
                nonMatchIndex = i;
                nonMatchClass = paramTypes[i];
                break;
            }
        }
        if (nonMatchClass == null) {
            return null;
        }
        Iterator i$ = candidates.iterator();
        while (i$.hasNext()) {
            if (i$.next().getParameterTypes()[nonMatchIndex] == paramTypes[nonMatchIndex]) {
                return null;
            }
        }
        Class<?> superclass = nonMatchClass.getSuperclass();
        while (true) {
            Class<?> superClass = superclass;
            if (superClass != null) {
                for (Wrapper c : candidates) {
                    if (c.getParameterTypes()[nonMatchIndex].equals(superClass)) {
                        return c;
                    }
                }
                superclass = superClass.getSuperclass();
            } else {
                Wrapper match = null;
                if (Number.class.isAssignableFrom(nonMatchClass)) {
                    Iterator i$2 = candidates.iterator();
                    while (true) {
                        if (!i$2.hasNext()) {
                            break;
                        }
                        Wrapper c2 = i$2.next();
                        Class<?> candidateType = c2.getParameterTypes()[nonMatchIndex];
                        if (Number.class.isAssignableFrom(candidateType) || candidateType.isPrimitive()) {
                            if (match == null) {
                                match = c2;
                            } else {
                                match = null;
                                break;
                            }
                        }
                    }
                }
                return match;
            }
        }
    }

    static boolean isAssignableFrom(Class<?> src, Class<?> target) {
        Class<?> targetClass;
        if (src == null) {
            return true;
        }
        if (target.isPrimitive()) {
            if (target == Boolean.TYPE) {
                targetClass = Boolean.class;
            } else if (target == Character.TYPE) {
                targetClass = Character.class;
            } else if (target == Byte.TYPE) {
                targetClass = Byte.class;
            } else if (target == Short.TYPE) {
                targetClass = Short.class;
            } else if (target == Integer.TYPE) {
                targetClass = Integer.class;
            } else if (target == Long.TYPE) {
                targetClass = Long.class;
            } else if (target == Float.TYPE) {
                targetClass = Float.class;
            } else {
                targetClass = Double.class;
            }
        } else {
            targetClass = target;
        }
        return targetClass.isAssignableFrom(src);
    }

    private static boolean isCoercibleFrom(Object src, Class<?> target) {
        try {
            getExpressionFactory().coerceToType(src, target);
            return true;
        } catch (ELException e) {
            return false;
        }
    }

    private static Class<?>[] getTypesFromValues(Object[] values) {
        if (values == null) {
            return EMPTY_CLASS_ARRAY;
        }
        Class<?>[] result = new Class[values.length];
        for (int i = 0; i < values.length; i++) {
            if (values[i] == null) {
                result[i] = null;
            } else {
                result[i] = values[i].getClass();
            }
        }
        return result;
    }

    static Method getMethod(Class<?> type, Method m) throws NoSuchMethodException, SecurityException {
        Method mp;
        if (m == null || Modifier.isPublic(type.getModifiers())) {
            return m;
        }
        Class<?>[] inf = type.getInterfaces();
        for (Class<?> cls : inf) {
            try {
                Method mp2 = cls.getMethod(m.getName(), m.getParameterTypes());
                mp = getMethod(mp2.getDeclaringClass(), mp2);
            } catch (NoSuchMethodException e) {
            }
            if (mp != null) {
                return mp;
            }
        }
        Class<?> sup = type.getSuperclass();
        if (sup != null) {
            try {
                Method mp3 = sup.getMethod(m.getName(), m.getParameterTypes());
                Method mp4 = getMethod(mp3.getDeclaringClass(), mp3);
                if (mp4 != null) {
                    return mp4;
                }
                return null;
            } catch (NoSuchMethodException e2) {
                return null;
            }
        }
        return null;
    }

    static Constructor<?> findConstructor(Class<?> clazz, Class<?>[] paramTypes, Object[] paramValues) throws SecurityException {
        if (clazz == null) {
            throw new MethodNotFoundException(message(null, "util.method.notfound", null, "<init>", paramString(paramTypes)));
        }
        if (paramTypes == null) {
            paramTypes = getTypesFromValues(paramValues);
        }
        Constructor<?>[] constructors = clazz.getConstructors();
        List<Wrapper> wrappers = Wrapper.wrap(constructors);
        Wrapper result = findWrapper(clazz, wrappers, "<init>", paramTypes, paramValues);
        return getConstructor(clazz, (Constructor) result.unWrap());
    }

    static Constructor<?> getConstructor(Class<?> type, Constructor<?> c) throws NoSuchMethodException, SecurityException {
        if (c == null || Modifier.isPublic(type.getModifiers())) {
            return c;
        }
        Class<?> sup = type.getSuperclass();
        if (sup != null) {
            try {
                Constructor<?> cp = sup.getConstructor(c.getParameterTypes());
                Constructor<?> cp2 = getConstructor(cp.getDeclaringClass(), cp);
                if (cp2 != null) {
                    return cp2;
                }
                return null;
            } catch (NoSuchMethodException e) {
                return null;
            }
        }
        return null;
    }

    static Object[] buildParameters(Class<?>[] parameterTypes, boolean isVarArgs, Object[] params) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, NegativeArraySizeException {
        ExpressionFactory factory = getExpressionFactory();
        Object[] parameters = null;
        if (parameterTypes.length > 0) {
            parameters = new Object[parameterTypes.length];
            if (params == null) {
                params = EMPTY_OBJECT_ARRAY;
            }
            int paramCount = params.length;
            if (isVarArgs) {
                int varArgIndex = parameterTypes.length - 1;
                for (int i = 0; i < varArgIndex; i++) {
                    parameters[i] = factory.coerceToType(params[i], parameterTypes[i]);
                }
                Class<?> varArgClass = parameterTypes[varArgIndex].getComponentType();
                Object varargs = Array.newInstance(varArgClass, paramCount - varArgIndex);
                for (int i2 = varArgIndex; i2 < paramCount; i2++) {
                    Array.set(varargs, i2 - varArgIndex, factory.coerceToType(params[i2], varArgClass));
                }
                parameters[varArgIndex] = varargs;
            } else {
                parameters = new Object[parameterTypes.length];
                for (int i3 = 0; i3 < parameterTypes.length; i3++) {
                    parameters[i3] = factory.coerceToType(params[i3], parameterTypes[i3]);
                }
            }
        }
        return parameters;
    }

    static ClassLoader getContextClassLoader() {
        ClassLoader tccl;
        if (System.getSecurityManager() != null) {
            PrivilegedAction<ClassLoader> pa = new PrivilegedGetTccl();
            tccl = (ClassLoader) AccessController.doPrivileged(pa);
        } else {
            tccl = Thread.currentThread().getContextClassLoader();
        }
        return tccl;
    }

    /* loaded from: tomcat-embed-el-8.5.43.jar:javax/el/Util$Wrapper.class */
    private static abstract class Wrapper {
        public abstract Object unWrap();

        public abstract Class<?>[] getParameterTypes();

        public abstract boolean isVarArgs();

        public abstract boolean isBridge();

        private Wrapper() {
        }

        public static List<Wrapper> wrap(Method[] methods, String name) {
            List<Wrapper> result = new ArrayList<>();
            for (Method method : methods) {
                if (method.getName().equals(name)) {
                    result.add(new MethodWrapper(method));
                }
            }
            return result;
        }

        public static List<Wrapper> wrap(Constructor<?>[] constructors) {
            List<Wrapper> result = new ArrayList<>();
            for (Constructor<?> constructor : constructors) {
                result.add(new ConstructorWrapper(constructor));
            }
            return result;
        }
    }

    /* loaded from: tomcat-embed-el-8.5.43.jar:javax/el/Util$MethodWrapper.class */
    private static class MethodWrapper extends Wrapper {
        private final Method m;

        public MethodWrapper(Method m) {
            super();
            this.m = m;
        }

        @Override // javax.el.Util.Wrapper
        public Object unWrap() {
            return this.m;
        }

        @Override // javax.el.Util.Wrapper
        public Class<?>[] getParameterTypes() {
            return this.m.getParameterTypes();
        }

        @Override // javax.el.Util.Wrapper
        public boolean isVarArgs() {
            return this.m.isVarArgs();
        }

        @Override // javax.el.Util.Wrapper
        public boolean isBridge() {
            return this.m.isBridge();
        }
    }

    /* loaded from: tomcat-embed-el-8.5.43.jar:javax/el/Util$ConstructorWrapper.class */
    private static class ConstructorWrapper extends Wrapper {
        private final Constructor<?> c;

        public ConstructorWrapper(Constructor<?> c) {
            super();
            this.c = c;
        }

        @Override // javax.el.Util.Wrapper
        public Object unWrap() {
            return this.c;
        }

        @Override // javax.el.Util.Wrapper
        public Class<?>[] getParameterTypes() {
            return this.c.getParameterTypes();
        }

        @Override // javax.el.Util.Wrapper
        public boolean isVarArgs() {
            return this.c.isVarArgs();
        }

        @Override // javax.el.Util.Wrapper
        public boolean isBridge() {
            return false;
        }
    }

    /* loaded from: tomcat-embed-el-8.5.43.jar:javax/el/Util$MatchResult.class */
    private static class MatchResult implements Comparable<MatchResult> {
        private final int exact;
        private final int assignable;
        private final int coercible;
        private final boolean bridge;

        public MatchResult(int exact, int assignable, int coercible, boolean bridge) {
            this.exact = exact;
            this.assignable = assignable;
            this.coercible = coercible;
            this.bridge = bridge;
        }

        public int getExact() {
            return this.exact;
        }

        public int getAssignable() {
            return this.assignable;
        }

        public int getCoercible() {
            return this.coercible;
        }

        public boolean isBridge() {
            return this.bridge;
        }

        @Override // java.lang.Comparable
        public int compareTo(MatchResult o) {
            int cmp = Integer.compare(getExact(), o.getExact());
            if (cmp == 0) {
                cmp = Integer.compare(getAssignable(), o.getAssignable());
                if (cmp == 0) {
                    cmp = Integer.compare(getCoercible(), o.getCoercible());
                    if (cmp == 0) {
                        cmp = Boolean.compare(o.isBridge(), isBridge());
                    }
                }
            }
            return cmp;
        }

        public boolean equals(Object o) {
            return o == this || (null != o && getClass().equals(o.getClass()) && ((MatchResult) o).getExact() == getExact() && ((MatchResult) o).getAssignable() == getAssignable() && ((MatchResult) o).getCoercible() == getCoercible() && ((MatchResult) o).isBridge() == isBridge());
        }

        public int hashCode() {
            return (((isBridge() ? 16777216 : 0) ^ (getExact() << 16)) ^ (getAssignable() << 8)) ^ getCoercible();
        }
    }

    /* loaded from: tomcat-embed-el-8.5.43.jar:javax/el/Util$PrivilegedGetTccl.class */
    private static class PrivilegedGetTccl implements PrivilegedAction<ClassLoader> {
        private PrivilegedGetTccl() {
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        public ClassLoader run() {
            return Thread.currentThread().getContextClassLoader();
        }
    }
}

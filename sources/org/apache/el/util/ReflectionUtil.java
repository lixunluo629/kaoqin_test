package org.apache.el.util;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import javax.el.ELException;
import org.apache.el.lang.ELSupport;
import org.apache.el.lang.EvaluationContext;
import org.apache.xmlbeans.XmlErrorCodes;

/* loaded from: tomcat-embed-el-8.5.43.jar:org/apache/el/util/ReflectionUtil.class */
public class ReflectionUtil {
    protected static final String[] PRIMITIVE_NAMES = {"boolean", "byte", "char", XmlErrorCodes.DOUBLE, XmlErrorCodes.FLOAT, XmlErrorCodes.INT, XmlErrorCodes.LONG, "short", "void"};
    protected static final Class<?>[] PRIMITIVES = {Boolean.TYPE, Byte.TYPE, Character.TYPE, Double.TYPE, Float.TYPE, Integer.TYPE, Long.TYPE, Short.TYPE, Void.TYPE};

    private ReflectionUtil() {
    }

    public static Class<?> forName(String name) throws ClassNotFoundException {
        if (null == name || "".equals(name)) {
            return null;
        }
        Class<?> c = forNamePrimitive(name);
        if (c == null) {
            if (name.endsWith("[]")) {
                String nc = name.substring(0, name.length() - 2);
                c = Array.newInstance(Class.forName(nc, true, getContextClassLoader()), 0).getClass();
            } else {
                c = Class.forName(name, true, getContextClassLoader());
            }
        }
        return c;
    }

    protected static Class<?> forNamePrimitive(String name) {
        int p;
        if (name.length() <= 8 && (p = Arrays.binarySearch(PRIMITIVE_NAMES, name)) >= 0) {
            return PRIMITIVES[p];
        }
        return null;
    }

    public static Class<?>[] toTypeArray(String[] s) throws ClassNotFoundException {
        if (s == null) {
            return null;
        }
        Class<?>[] c = new Class[s.length];
        for (int i = 0; i < s.length; i++) {
            c[i] = forName(s[i]);
        }
        return c;
    }

    public static String[] toTypeNameArray(Class<?>[] c) {
        if (c == null) {
            return null;
        }
        String[] s = new String[c.length];
        for (int i = 0; i < c.length; i++) {
            s[i] = c[i].getName();
        }
        return s;
    }

    /* JADX WARN: Code restructure failed: missing block: B:76:0x0160, code lost:
    
        r25 = r25 + 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.reflect.Method getMethod(org.apache.el.lang.EvaluationContext r9, java.lang.Object r10, java.lang.Object r11, java.lang.Class<?>[] r12, java.lang.Object[] r13) throws java.lang.SecurityException, javax.el.MethodNotFoundException {
        /*
            Method dump skipped, instructions count: 840
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.el.util.ReflectionUtil.getMethod(org.apache.el.lang.EvaluationContext, java.lang.Object, java.lang.Object, java.lang.Class[], java.lang.Object[]):java.lang.reflect.Method");
    }

    private static Method resolveAmbiguousMethod(Set<Method> candidates, Class<?>[] paramTypes) {
        Method m = candidates.iterator().next();
        int nonMatchIndex = 0;
        Class<?> nonMatchClass = null;
        int i = 0;
        while (true) {
            if (i >= paramTypes.length) {
                break;
            }
            if (m.getParameterTypes()[i] == paramTypes[i]) {
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
                for (Method c : candidates) {
                    if (c.getParameterTypes()[nonMatchIndex].equals(superClass)) {
                        return c;
                    }
                }
                superclass = superClass.getSuperclass();
            } else {
                Method match = null;
                if (Number.class.isAssignableFrom(nonMatchClass)) {
                    Iterator i$2 = candidates.iterator();
                    while (true) {
                        if (!i$2.hasNext()) {
                            break;
                        }
                        Method c2 = i$2.next();
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

    private static boolean isAssignableFrom(Class<?> src, Class<?> target) {
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

    private static boolean isCoercibleFrom(EvaluationContext ctx, Object src, Class<?> target) {
        try {
            ELSupport.coerceToType(ctx, src, target);
            return true;
        } catch (ELException e) {
            return false;
        }
    }

    private static Method getMethod(Class<?> type, Method m) throws NoSuchMethodException, SecurityException {
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

    private static ClassLoader getContextClassLoader() {
        ClassLoader tccl;
        if (System.getSecurityManager() != null) {
            PrivilegedAction<ClassLoader> pa = new PrivilegedGetTccl();
            tccl = (ClassLoader) AccessController.doPrivileged(pa);
        } else {
            tccl = Thread.currentThread().getContextClassLoader();
        }
        return tccl;
    }

    /* loaded from: tomcat-embed-el-8.5.43.jar:org/apache/el/util/ReflectionUtil$PrivilegedGetTccl.class */
    private static class PrivilegedGetTccl implements PrivilegedAction<ClassLoader> {
        private PrivilegedGetTccl() {
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        public ClassLoader run() {
            return Thread.currentThread().getContextClassLoader();
        }
    }

    /* loaded from: tomcat-embed-el-8.5.43.jar:org/apache/el/util/ReflectionUtil$MatchResult.class */
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
    }
}

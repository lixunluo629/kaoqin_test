package org.springframework.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.UndeclaredThrowableException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.jmx.export.naming.IdentityNamingStrategy;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/ReflectionUtils.class */
public abstract class ReflectionUtils {
    private static final String CGLIB_RENAMED_METHOD_PREFIX = "CGLIB$";
    private static final Method[] NO_METHODS = new Method[0];
    private static final Field[] NO_FIELDS = new Field[0];
    private static final Map<Class<?>, Method[]> declaredMethodsCache = new ConcurrentReferenceHashMap(256);
    private static final Map<Class<?>, Field[]> declaredFieldsCache = new ConcurrentReferenceHashMap(256);
    public static final MethodFilter NON_BRIDGED_METHODS = new MethodFilter() { // from class: org.springframework.util.ReflectionUtils.4
        @Override // org.springframework.util.ReflectionUtils.MethodFilter
        public boolean matches(Method method) {
            return !method.isBridge();
        }
    };
    public static final MethodFilter USER_DECLARED_METHODS = new MethodFilter() { // from class: org.springframework.util.ReflectionUtils.5
        @Override // org.springframework.util.ReflectionUtils.MethodFilter
        public boolean matches(Method method) {
            return (method.isBridge() || method.getDeclaringClass() == Object.class) ? false : true;
        }
    };
    public static final FieldFilter COPYABLE_FIELDS = new FieldFilter() { // from class: org.springframework.util.ReflectionUtils.6
        @Override // org.springframework.util.ReflectionUtils.FieldFilter
        public boolean matches(Field field) {
            return (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) ? false : true;
        }
    };

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/ReflectionUtils$FieldCallback.class */
    public interface FieldCallback {
        void doWith(Field field) throws IllegalAccessException, IllegalArgumentException;
    }

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/ReflectionUtils$FieldFilter.class */
    public interface FieldFilter {
        boolean matches(Field field);
    }

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/ReflectionUtils$MethodCallback.class */
    public interface MethodCallback {
        void doWith(Method method) throws IllegalAccessException, IllegalArgumentException;
    }

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/ReflectionUtils$MethodFilter.class */
    public interface MethodFilter {
        boolean matches(Method method);
    }

    public static Field findField(Class<?> clazz, String name) {
        return findField(clazz, name, null);
    }

    public static Field findField(Class<?> clazz, String name, Class<?> type) {
        Field field;
        Assert.notNull(clazz, "Class must not be null");
        Assert.isTrue((name == null && type == null) ? false : true, "Either name or type of the field must be specified");
        Class<?> superclass = clazz;
        loop0: while (true) {
            Class<?> searchType = superclass;
            if (Object.class != searchType && searchType != null) {
                Field[] fields = getDeclaredFields(searchType);
                int length = fields.length;
                for (int i = 0; i < length; i++) {
                    field = fields[i];
                    if ((name == null || name.equals(field.getName())) && (type == null || type.equals(field.getType()))) {
                        break loop0;
                    }
                }
                superclass = searchType.getSuperclass();
            } else {
                return null;
            }
        }
        return field;
    }

    public static void setField(Field field, Object target, Object value) throws IllegalAccessException, IllegalArgumentException {
        try {
            field.set(target, value);
        } catch (IllegalAccessException ex) {
            handleReflectionException(ex);
            throw new IllegalStateException("Unexpected reflection exception - " + ex.getClass().getName() + ": " + ex.getMessage());
        }
    }

    public static Object getField(Field field, Object target) {
        try {
            return field.get(target);
        } catch (IllegalAccessException ex) {
            handleReflectionException(ex);
            throw new IllegalStateException("Unexpected reflection exception - " + ex.getClass().getName() + ": " + ex.getMessage());
        }
    }

    public static Method findMethod(Class<?> clazz, String name) {
        return findMethod(clazz, name, new Class[0]);
    }

    public static Method findMethod(Class<?> clazz, String name, Class<?>... paramTypes) {
        Method method;
        Assert.notNull(clazz, "Class must not be null");
        Assert.notNull(name, "Method name must not be null");
        Class<?> superclass = clazz;
        loop0: while (true) {
            Class<?> searchType = superclass;
            if (searchType != null) {
                Method[] methods = searchType.isInterface() ? searchType.getMethods() : getDeclaredMethods(searchType);
                int length = methods.length;
                for (int i = 0; i < length; i++) {
                    method = methods[i];
                    if (name.equals(method.getName()) && (paramTypes == null || Arrays.equals(paramTypes, method.getParameterTypes()))) {
                        break loop0;
                    }
                }
                superclass = searchType.getSuperclass();
            } else {
                return null;
            }
        }
        return method;
    }

    public static Object invokeMethod(Method method, Object target) {
        return invokeMethod(method, target, new Object[0]);
    }

    public static Object invokeMethod(Method method, Object target, Object... args) {
        try {
            return method.invoke(target, args);
        } catch (Exception ex) {
            handleReflectionException(ex);
            throw new IllegalStateException("Should never get here");
        }
    }

    public static Object invokeJdbcMethod(Method method, Object target) throws SQLException {
        return invokeJdbcMethod(method, target, new Object[0]);
    }

    public static Object invokeJdbcMethod(Method method, Object target, Object... args) throws SQLException {
        try {
            return method.invoke(target, args);
        } catch (IllegalAccessException ex) {
            handleReflectionException(ex);
            throw new IllegalStateException("Should never get here");
        } catch (InvocationTargetException ex2) {
            if (ex2.getTargetException() instanceof SQLException) {
                throw ((SQLException) ex2.getTargetException());
            }
            handleInvocationTargetException(ex2);
            throw new IllegalStateException("Should never get here");
        }
    }

    public static void handleReflectionException(Exception ex) {
        if (ex instanceof NoSuchMethodException) {
            throw new IllegalStateException("Method not found: " + ex.getMessage());
        }
        if (ex instanceof IllegalAccessException) {
            throw new IllegalStateException("Could not access method: " + ex.getMessage());
        }
        if (ex instanceof InvocationTargetException) {
            handleInvocationTargetException((InvocationTargetException) ex);
        }
        if (ex instanceof RuntimeException) {
            throw ((RuntimeException) ex);
        }
        throw new UndeclaredThrowableException(ex);
    }

    public static void handleInvocationTargetException(InvocationTargetException ex) {
        rethrowRuntimeException(ex.getTargetException());
    }

    public static void rethrowRuntimeException(Throwable ex) {
        if (ex instanceof RuntimeException) {
            throw ((RuntimeException) ex);
        }
        if (ex instanceof Error) {
            throw ((Error) ex);
        }
        throw new UndeclaredThrowableException(ex);
    }

    public static void rethrowException(Throwable ex) throws Exception {
        if (ex instanceof Exception) {
            throw ((Exception) ex);
        }
        if (ex instanceof Error) {
            throw ((Error) ex);
        }
        throw new UndeclaredThrowableException(ex);
    }

    public static boolean declaresException(Method method, Class<?> exceptionType) {
        Assert.notNull(method, "Method must not be null");
        Class<?>[] declaredExceptions = method.getExceptionTypes();
        for (Class<?> declaredException : declaredExceptions) {
            if (declaredException.isAssignableFrom(exceptionType)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPublicStaticFinal(Field field) {
        int modifiers = field.getModifiers();
        return Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers);
    }

    public static boolean isEqualsMethod(Method method) {
        if (method == null || !method.getName().equals("equals")) {
            return false;
        }
        Class<?>[] paramTypes = method.getParameterTypes();
        return paramTypes.length == 1 && paramTypes[0] == Object.class;
    }

    public static boolean isHashCodeMethod(Method method) {
        return method != null && method.getName().equals(IdentityNamingStrategy.HASH_CODE_KEY) && method.getParameterTypes().length == 0;
    }

    public static boolean isToStringMethod(Method method) {
        return method != null && method.getName().equals("toString") && method.getParameterTypes().length == 0;
    }

    public static boolean isObjectMethod(Method method) throws NoSuchMethodException, SecurityException {
        if (method == null) {
            return false;
        }
        try {
            Object.class.getDeclaredMethod(method.getName(), method.getParameterTypes());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isCglibRenamedMethod(Method renamedMethod) {
        String name = renamedMethod.getName();
        if (name.startsWith(CGLIB_RENAMED_METHOD_PREFIX)) {
            int i = name.length() - 1;
            while (i >= 0 && Character.isDigit(name.charAt(i))) {
                i--;
            }
            return i > CGLIB_RENAMED_METHOD_PREFIX.length() && i < name.length() - 1 && name.charAt(i) == '$';
        }
        return false;
    }

    public static void makeAccessible(Field field) {
        if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }
    }

    public static void makeAccessible(Method method) {
        if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible()) {
            method.setAccessible(true);
        }
    }

    public static void makeAccessible(Constructor<?> ctor) {
        if ((!Modifier.isPublic(ctor.getModifiers()) || !Modifier.isPublic(ctor.getDeclaringClass().getModifiers())) && !ctor.isAccessible()) {
            ctor.setAccessible(true);
        }
    }

    public static void doWithLocalMethods(Class<?> clazz, MethodCallback mc) throws SecurityException, IllegalArgumentException {
        Method[] methods = getDeclaredMethods(clazz);
        for (Method method : methods) {
            try {
                mc.doWith(method);
            } catch (IllegalAccessException ex) {
                throw new IllegalStateException("Not allowed to access method '" + method.getName() + "': " + ex);
            }
        }
    }

    public static void doWithMethods(Class<?> clazz, MethodCallback mc) throws SecurityException, IllegalArgumentException {
        doWithMethods(clazz, mc, null);
    }

    public static void doWithMethods(Class<?> clazz, MethodCallback mc, MethodFilter mf) throws SecurityException, IllegalArgumentException {
        Method[] methods = getDeclaredMethods(clazz);
        for (Method method : methods) {
            if (mf == null || mf.matches(method)) {
                try {
                    mc.doWith(method);
                } catch (IllegalAccessException ex) {
                    throw new IllegalStateException("Not allowed to access method '" + method.getName() + "': " + ex);
                }
            }
        }
        if (clazz.getSuperclass() != null) {
            doWithMethods(clazz.getSuperclass(), mc, mf);
            return;
        }
        if (clazz.isInterface()) {
            for (Class<?> superIfc : clazz.getInterfaces()) {
                doWithMethods(superIfc, mc, mf);
            }
        }
    }

    public static Method[] getAllDeclaredMethods(Class<?> leafClass) throws SecurityException, IllegalArgumentException {
        final List<Method> methods = new ArrayList<>(32);
        doWithMethods(leafClass, new MethodCallback() { // from class: org.springframework.util.ReflectionUtils.1
            @Override // org.springframework.util.ReflectionUtils.MethodCallback
            public void doWith(Method method) {
                methods.add(method);
            }
        });
        return (Method[]) methods.toArray(new Method[methods.size()]);
    }

    public static Method[] getUniqueDeclaredMethods(Class<?> leafClass) throws SecurityException, IllegalArgumentException {
        final List<Method> methods = new ArrayList<>(32);
        doWithMethods(leafClass, new MethodCallback() { // from class: org.springframework.util.ReflectionUtils.2
            @Override // org.springframework.util.ReflectionUtils.MethodCallback
            public void doWith(Method method) {
                boolean knownSignature = false;
                Method methodBeingOverriddenWithCovariantReturnType = null;
                Iterator it = methods.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    Method existingMethod = (Method) it.next();
                    if (method.getName().equals(existingMethod.getName()) && Arrays.equals(method.getParameterTypes(), existingMethod.getParameterTypes())) {
                        if (existingMethod.getReturnType() != method.getReturnType() && existingMethod.getReturnType().isAssignableFrom(method.getReturnType())) {
                            methodBeingOverriddenWithCovariantReturnType = existingMethod;
                        } else {
                            knownSignature = true;
                        }
                    }
                }
                if (methodBeingOverriddenWithCovariantReturnType != null) {
                    methods.remove(methodBeingOverriddenWithCovariantReturnType);
                }
                if (!knownSignature && !ReflectionUtils.isCglibRenamedMethod(method)) {
                    methods.add(method);
                }
            }
        });
        return (Method[]) methods.toArray(new Method[methods.size()]);
    }

    private static Method[] getDeclaredMethods(Class<?> clazz) throws SecurityException {
        Assert.notNull(clazz, "Class must not be null");
        Method[] result = declaredMethodsCache.get(clazz);
        if (result == null) {
            Method[] declaredMethods = clazz.getDeclaredMethods();
            List<Method> defaultMethods = findConcreteMethodsOnInterfaces(clazz);
            if (defaultMethods != null) {
                result = new Method[declaredMethods.length + defaultMethods.size()];
                System.arraycopy(declaredMethods, 0, result, 0, declaredMethods.length);
                int index = declaredMethods.length;
                for (Method defaultMethod : defaultMethods) {
                    result[index] = defaultMethod;
                    index++;
                }
            } else {
                result = declaredMethods;
            }
            declaredMethodsCache.put(clazz, result.length == 0 ? NO_METHODS : result);
        }
        return result;
    }

    private static List<Method> findConcreteMethodsOnInterfaces(Class<?> clazz) throws SecurityException {
        List<Method> result = null;
        for (Class<?> ifc : clazz.getInterfaces()) {
            for (Method ifcMethod : ifc.getMethods()) {
                if (!Modifier.isAbstract(ifcMethod.getModifiers())) {
                    if (result == null) {
                        result = new ArrayList<>();
                    }
                    result.add(ifcMethod);
                }
            }
        }
        return result;
    }

    public static void doWithLocalFields(Class<?> clazz, FieldCallback fc) throws IllegalArgumentException {
        for (Field field : getDeclaredFields(clazz)) {
            try {
                fc.doWith(field);
            } catch (IllegalAccessException ex) {
                throw new IllegalStateException("Not allowed to access field '" + field.getName() + "': " + ex);
            }
        }
    }

    public static void doWithFields(Class<?> clazz, FieldCallback fc) throws IllegalArgumentException {
        doWithFields(clazz, fc, null);
    }

    public static void doWithFields(Class<?> clazz, FieldCallback fc, FieldFilter ff) throws IllegalArgumentException {
        Class<?> targetClass = clazz;
        do {
            Field[] fields = getDeclaredFields(targetClass);
            for (Field field : fields) {
                if (ff == null || ff.matches(field)) {
                    try {
                        fc.doWith(field);
                    } catch (IllegalAccessException ex) {
                        throw new IllegalStateException("Not allowed to access field '" + field.getName() + "': " + ex);
                    }
                }
            }
            targetClass = targetClass.getSuperclass();
            if (targetClass == null) {
                return;
            }
        } while (targetClass != Object.class);
    }

    private static Field[] getDeclaredFields(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");
        Field[] result = declaredFieldsCache.get(clazz);
        if (result == null) {
            result = clazz.getDeclaredFields();
            declaredFieldsCache.put(clazz, result.length == 0 ? NO_FIELDS : result);
        }
        return result;
    }

    public static void shallowCopyFieldState(final Object src, final Object dest) throws IllegalArgumentException {
        Assert.notNull(src, "Source for field copy cannot be null");
        Assert.notNull(dest, "Destination for field copy cannot be null");
        if (!src.getClass().isAssignableFrom(dest.getClass())) {
            throw new IllegalArgumentException("Destination class [" + dest.getClass().getName() + "] must be same or subclass as source class [" + src.getClass().getName() + "]");
        }
        doWithFields(src.getClass(), new FieldCallback() { // from class: org.springframework.util.ReflectionUtils.3
            @Override // org.springframework.util.ReflectionUtils.FieldCallback
            public void doWith(Field field) throws IllegalAccessException, IllegalArgumentException {
                ReflectionUtils.makeAccessible(field);
                Object srcValue = field.get(src);
                field.set(dest, srcValue);
            }
        }, COPYABLE_FIELDS);
    }

    public static void clearCache() {
        declaredMethodsCache.clear();
        declaredFieldsCache.clear();
    }
}

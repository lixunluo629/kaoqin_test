package org.springframework.cglib.core;

import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.xmlbeans.XmlErrorCodes;
import org.springframework.asm.Attribute;
import org.springframework.asm.Type;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/cglib/core/ReflectUtils.class */
public class ReflectUtils {
    private static Method DEFINE_CLASS;
    private static Method DEFINE_CLASS_UNSAFE;
    private static final ProtectionDomain PROTECTION_DOMAIN;
    private static final Object UNSAFE;
    private static final Throwable THROWABLE;
    private static final String[] CGLIB_PACKAGES;
    private static final Map primitives = new HashMap(8);
    private static final Map transforms = new HashMap(8);
    private static final ClassLoader defaultLoader = ReflectUtils.class.getClassLoader();
    private static final List<Method> OBJECT_METHODS = new ArrayList();

    private ReflectUtils() {
    }

    static {
        ProtectionDomain protectionDomain;
        Method defineClass;
        Method defineClassUnsafe;
        Object unsafe;
        Throwable throwable = null;
        try {
            protectionDomain = getProtectionDomain(ReflectUtils.class);
            try {
                defineClass = (Method) AccessController.doPrivileged(new PrivilegedExceptionAction() { // from class: org.springframework.cglib.core.ReflectUtils.1
                    @Override // java.security.PrivilegedExceptionAction
                    public Object run() throws Exception {
                        Class loader = Class.forName("java.lang.ClassLoader");
                        Method defineClass2 = loader.getDeclaredMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE, ProtectionDomain.class);
                        defineClass2.setAccessible(true);
                        return defineClass2;
                    }
                });
                defineClassUnsafe = null;
                unsafe = null;
            } catch (Throwable t) {
                throwable = t;
                defineClass = null;
                unsafe = AccessController.doPrivileged((PrivilegedExceptionAction<Object>) new PrivilegedExceptionAction() { // from class: org.springframework.cglib.core.ReflectUtils.2
                    @Override // java.security.PrivilegedExceptionAction
                    public Object run() throws Exception {
                        Class u = Class.forName("sun.misc.Unsafe");
                        Field theUnsafe = u.getDeclaredField("theUnsafe");
                        theUnsafe.setAccessible(true);
                        return theUnsafe.get(null);
                    }
                });
                Class u = Class.forName("sun.misc.Unsafe");
                defineClassUnsafe = u.getMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE, ClassLoader.class, ProtectionDomain.class);
            }
            AccessController.doPrivileged(new PrivilegedExceptionAction() { // from class: org.springframework.cglib.core.ReflectUtils.3
                @Override // java.security.PrivilegedExceptionAction
                public Object run() throws Exception {
                    Method[] methods = Object.class.getDeclaredMethods();
                    for (Method method : methods) {
                        if (!"finalize".equals(method.getName()) && (method.getModifiers() & 24) <= 0) {
                            ReflectUtils.OBJECT_METHODS.add(method);
                        }
                    }
                    return null;
                }
            });
        } catch (Throwable t2) {
            if (throwable == null) {
                throwable = t2;
            }
            protectionDomain = null;
            defineClass = null;
            defineClassUnsafe = null;
            unsafe = null;
        }
        PROTECTION_DOMAIN = protectionDomain;
        DEFINE_CLASS = defineClass;
        DEFINE_CLASS_UNSAFE = defineClassUnsafe;
        UNSAFE = unsafe;
        THROWABLE = throwable;
        CGLIB_PACKAGES = new String[]{"java.lang"};
        primitives.put("byte", Byte.TYPE);
        primitives.put("char", Character.TYPE);
        primitives.put(XmlErrorCodes.DOUBLE, Double.TYPE);
        primitives.put(XmlErrorCodes.FLOAT, Float.TYPE);
        primitives.put(XmlErrorCodes.INT, Integer.TYPE);
        primitives.put(XmlErrorCodes.LONG, Long.TYPE);
        primitives.put("short", Short.TYPE);
        primitives.put("boolean", Boolean.TYPE);
        transforms.put("byte", "B");
        transforms.put("char", "C");
        transforms.put(XmlErrorCodes.DOUBLE, "D");
        transforms.put(XmlErrorCodes.FLOAT, "F");
        transforms.put(XmlErrorCodes.INT, "I");
        transforms.put(XmlErrorCodes.LONG, "J");
        transforms.put("short", "S");
        transforms.put("boolean", "Z");
    }

    public static ProtectionDomain getProtectionDomain(final Class source) {
        if (source == null) {
            return null;
        }
        return (ProtectionDomain) AccessController.doPrivileged(new PrivilegedAction() { // from class: org.springframework.cglib.core.ReflectUtils.4
            @Override // java.security.PrivilegedAction
            public Object run() {
                return source.getProtectionDomain();
            }
        });
    }

    public static Type[] getExceptionTypes(Member member) {
        if (member instanceof Method) {
            return TypeUtils.getTypes(((Method) member).getExceptionTypes());
        }
        if (member instanceof Constructor) {
            return TypeUtils.getTypes(((Constructor) member).getExceptionTypes());
        }
        throw new IllegalArgumentException("Cannot get exception types of a field");
    }

    public static Signature getSignature(Member member) {
        if (member instanceof Method) {
            return new Signature(member.getName(), Type.getMethodDescriptor((Method) member));
        }
        if (member instanceof Constructor) {
            Type[] types = TypeUtils.getTypes(((Constructor) member).getParameterTypes());
            return new Signature("<init>", Type.getMethodDescriptor(Type.VOID_TYPE, types));
        }
        throw new IllegalArgumentException("Cannot get signature of a field");
    }

    public static Constructor findConstructor(String desc) {
        return findConstructor(desc, defaultLoader);
    }

    public static Constructor findConstructor(String desc, ClassLoader loader) {
        try {
            int lparen = desc.indexOf(40);
            String className = desc.substring(0, lparen).trim();
            return getClass(className, loader).getConstructor(parseTypes(desc, loader));
        } catch (ClassNotFoundException e) {
            throw new CodeGenerationException(e);
        } catch (NoSuchMethodException e2) {
            throw new CodeGenerationException(e2);
        }
    }

    public static Method findMethod(String desc) {
        return findMethod(desc, defaultLoader);
    }

    public static Method findMethod(String desc, ClassLoader loader) {
        try {
            int lparen = desc.indexOf(40);
            int dot = desc.lastIndexOf(46, lparen);
            String className = desc.substring(0, dot).trim();
            String methodName = desc.substring(dot + 1, lparen).trim();
            return getClass(className, loader).getDeclaredMethod(methodName, parseTypes(desc, loader));
        } catch (ClassNotFoundException e) {
            throw new CodeGenerationException(e);
        } catch (NoSuchMethodException e2) {
            throw new CodeGenerationException(e2);
        }
    }

    private static Class[] parseTypes(String desc, ClassLoader loader) throws ClassNotFoundException {
        int start;
        int lparen = desc.indexOf(40);
        int rparen = desc.indexOf(41, lparen);
        List params = new ArrayList();
        int i = lparen;
        while (true) {
            start = i + 1;
            int comma = desc.indexOf(44, start);
            if (comma < 0) {
                break;
            }
            params.add(desc.substring(start, comma).trim());
            i = comma;
        }
        if (start < rparen) {
            params.add(desc.substring(start, rparen).trim());
        }
        Class[] types = new Class[params.size()];
        for (int i2 = 0; i2 < types.length; i2++) {
            types[i2] = getClass((String) params.get(i2), loader);
        }
        return types;
    }

    private static Class getClass(String className, ClassLoader loader) throws ClassNotFoundException {
        return getClass(className, loader, CGLIB_PACKAGES);
    }

    private static Class getClass(String className, ClassLoader loader, String[] packages) throws ClassNotFoundException {
        int dimensions = 0;
        int index = 0;
        while (true) {
            int iIndexOf = className.indexOf("[]", index) + 1;
            index = iIndexOf;
            if (iIndexOf <= 0) {
                break;
            }
            dimensions++;
        }
        StringBuffer brackets = new StringBuffer(className.length() - dimensions);
        for (int i = 0; i < dimensions; i++) {
            brackets.append('[');
        }
        String className2 = className.substring(0, className.length() - (2 * dimensions));
        String prefix = dimensions > 0 ? ((Object) brackets) + StandardRoles.L : "";
        String suffix = dimensions > 0 ? ScriptUtils.DEFAULT_STATEMENT_SEPARATOR : "";
        try {
            return Class.forName(prefix + className2 + suffix, false, loader);
        } catch (ClassNotFoundException e) {
            for (String str : packages) {
                try {
                    return Class.forName(prefix + str + '.' + className2 + suffix, false, loader);
                } catch (ClassNotFoundException e2) {
                }
            }
            if (dimensions == 0) {
                Class c = (Class) primitives.get(className2);
                if (c != null) {
                    return c;
                }
            } else {
                String transform = (String) transforms.get(className2);
                if (transform != null) {
                    try {
                        return Class.forName(((Object) brackets) + transform, false, loader);
                    } catch (ClassNotFoundException e3) {
                        throw new ClassNotFoundException(className);
                    }
                }
            }
            throw new ClassNotFoundException(className);
        }
    }

    public static Object newInstance(Class type) {
        return newInstance(type, Constants.EMPTY_CLASS_ARRAY, null);
    }

    public static Object newInstance(Class type, Class[] parameterTypes, Object[] args) {
        return newInstance(getConstructor(type, parameterTypes), args);
    }

    public static Object newInstance(Constructor cstruct, Object[] args) {
        boolean flag = cstruct.isAccessible();
        try {
            if (!flag) {
                try {
                    try {
                        try {
                            cstruct.setAccessible(true);
                        } catch (InvocationTargetException e) {
                            throw new CodeGenerationException(e.getTargetException());
                        }
                    } catch (IllegalAccessException e2) {
                        throw new CodeGenerationException(e2);
                    }
                } catch (InstantiationException e3) {
                    throw new CodeGenerationException(e3);
                }
            }
            Object result = cstruct.newInstance(args);
            if (!flag) {
                cstruct.setAccessible(flag);
            }
            return result;
        } catch (Throwable th) {
            if (!flag) {
                cstruct.setAccessible(flag);
            }
            throw th;
        }
    }

    public static Constructor getConstructor(Class type, Class[] parameterTypes) throws NoSuchMethodException, SecurityException {
        try {
            Constructor constructor = type.getDeclaredConstructor(parameterTypes);
            constructor.setAccessible(true);
            return constructor;
        } catch (NoSuchMethodException e) {
            throw new CodeGenerationException(e);
        }
    }

    public static String[] getNames(Class[] classes) {
        if (classes == null) {
            return null;
        }
        String[] names = new String[classes.length];
        for (int i = 0; i < names.length; i++) {
            names[i] = classes[i].getName();
        }
        return names;
    }

    public static Class[] getClasses(Object[] objects) {
        Class[] classes = new Class[objects.length];
        for (int i = 0; i < objects.length; i++) {
            classes[i] = objects[i].getClass();
        }
        return classes;
    }

    public static Method findNewInstance(Class iface) {
        Method m = findInterfaceMethod(iface);
        if (!m.getName().equals("newInstance")) {
            throw new IllegalArgumentException(iface + " missing newInstance method");
        }
        return m;
    }

    public static Method[] getPropertyMethods(PropertyDescriptor[] properties, boolean read, boolean write) {
        Set methods = new HashSet();
        for (PropertyDescriptor pd : properties) {
            if (read) {
                methods.add(pd.getReadMethod());
            }
            if (write) {
                methods.add(pd.getWriteMethod());
            }
        }
        methods.remove(null);
        return (Method[]) methods.toArray(new Method[methods.size()]);
    }

    public static PropertyDescriptor[] getBeanProperties(Class type) {
        return getPropertiesHelper(type, true, true);
    }

    public static PropertyDescriptor[] getBeanGetters(Class type) {
        return getPropertiesHelper(type, true, false);
    }

    public static PropertyDescriptor[] getBeanSetters(Class type) {
        return getPropertiesHelper(type, false, true);
    }

    private static PropertyDescriptor[] getPropertiesHelper(Class type, boolean read, boolean write) {
        try {
            BeanInfo info = Introspector.getBeanInfo(type, Object.class);
            PropertyDescriptor[] all = info.getPropertyDescriptors();
            if (read && write) {
                return all;
            }
            List properties = new ArrayList(all.length);
            for (PropertyDescriptor pd : all) {
                if ((read && pd.getReadMethod() != null) || (write && pd.getWriteMethod() != null)) {
                    properties.add(pd);
                }
            }
            return (PropertyDescriptor[]) properties.toArray(new PropertyDescriptor[properties.size()]);
        } catch (IntrospectionException e) {
            throw new CodeGenerationException(e);
        }
    }

    public static Method findDeclaredMethod(Class type, String methodName, Class[] parameterTypes) throws NoSuchMethodException {
        Class superclass = type;
        while (true) {
            Class cl = superclass;
            if (cl != null) {
                try {
                    return cl.getDeclaredMethod(methodName, parameterTypes);
                } catch (NoSuchMethodException e) {
                    superclass = cl.getSuperclass();
                }
            } else {
                throw new NoSuchMethodException(methodName);
            }
        }
    }

    public static List addAllMethods(Class type, List list) {
        if (type == Object.class) {
            list.addAll(OBJECT_METHODS);
        } else {
            list.addAll(Arrays.asList(type.getDeclaredMethods()));
        }
        Class superclass = type.getSuperclass();
        if (superclass != null) {
            addAllMethods(superclass, list);
        }
        Class[] interfaces = type.getInterfaces();
        for (Class cls : interfaces) {
            addAllMethods(cls, list);
        }
        return list;
    }

    public static List addAllInterfaces(Class type, List list) {
        Class superclass = type.getSuperclass();
        if (superclass != null) {
            list.addAll(Arrays.asList(type.getInterfaces()));
            addAllInterfaces(superclass, list);
        }
        return list;
    }

    public static Method findInterfaceMethod(Class iface) throws SecurityException {
        if (!iface.isInterface()) {
            throw new IllegalArgumentException(iface + " is not an interface");
        }
        Method[] methods = iface.getDeclaredMethods();
        if (methods.length != 1) {
            throw new IllegalArgumentException("expecting exactly 1 method in " + iface);
        }
        return methods[0];
    }

    public static Class defineClass(String className, byte[] b, ClassLoader loader) throws Exception {
        return defineClass(className, b, loader, PROTECTION_DOMAIN);
    }

    public static Class defineClass(String className, byte[] b, ClassLoader loader, ProtectionDomain protectionDomain) throws Exception {
        Class c;
        if (DEFINE_CLASS != null) {
            Object[] args = {className, b, new Integer(0), new Integer(b.length), protectionDomain};
            c = (Class) DEFINE_CLASS.invoke(loader, args);
        } else if (DEFINE_CLASS_UNSAFE != null) {
            Object[] args2 = {className, b, new Integer(0), new Integer(b.length), loader, protectionDomain};
            c = (Class) DEFINE_CLASS_UNSAFE.invoke(UNSAFE, args2);
        } else {
            throw new CodeGenerationException(THROWABLE);
        }
        Class.forName(className, true, loader);
        return c;
    }

    public static int findPackageProtected(Class[] classes) {
        for (int i = 0; i < classes.length; i++) {
            if (!Modifier.isPublic(classes[i].getModifiers())) {
                return i;
            }
        }
        return 0;
    }

    public static MethodInfo getMethodInfo(final Member member, final int modifiers) {
        final Signature sig = getSignature(member);
        return new MethodInfo() { // from class: org.springframework.cglib.core.ReflectUtils.5
            private ClassInfo ci;

            @Override // org.springframework.cglib.core.MethodInfo
            public ClassInfo getClassInfo() {
                if (this.ci == null) {
                    this.ci = ReflectUtils.getClassInfo(member.getDeclaringClass());
                }
                return this.ci;
            }

            @Override // org.springframework.cglib.core.MethodInfo
            public int getModifiers() {
                return modifiers;
            }

            @Override // org.springframework.cglib.core.MethodInfo
            public Signature getSignature() {
                return sig;
            }

            @Override // org.springframework.cglib.core.MethodInfo
            public Type[] getExceptionTypes() {
                return ReflectUtils.getExceptionTypes(member);
            }

            public Attribute getAttribute() {
                return null;
            }
        };
    }

    public static MethodInfo getMethodInfo(Member member) {
        return getMethodInfo(member, member.getModifiers());
    }

    public static ClassInfo getClassInfo(final Class clazz) {
        final Type type = Type.getType((Class<?>) clazz);
        final Type sc = clazz.getSuperclass() == null ? null : Type.getType((Class<?>) clazz.getSuperclass());
        return new ClassInfo() { // from class: org.springframework.cglib.core.ReflectUtils.6
            @Override // org.springframework.cglib.core.ClassInfo
            public Type getType() {
                return type;
            }

            @Override // org.springframework.cglib.core.ClassInfo
            public Type getSuperType() {
                return sc;
            }

            @Override // org.springframework.cglib.core.ClassInfo
            public Type[] getInterfaces() {
                return TypeUtils.getTypes(clazz.getInterfaces());
            }

            @Override // org.springframework.cglib.core.ClassInfo
            public int getModifiers() {
                return clazz.getModifiers();
            }
        };
    }

    public static Method[] findMethods(String[] namesAndDescriptors, Method[] methods) {
        Map map = new HashMap();
        for (Method method : methods) {
            map.put(method.getName() + Type.getMethodDescriptor(method), method);
        }
        Method[] result = new Method[namesAndDescriptors.length / 2];
        for (int i = 0; i < result.length; i++) {
            result[i] = (Method) map.get(namesAndDescriptors[i * 2] + namesAndDescriptors[(i * 2) + 1]);
            if (result[i] == null) {
            }
        }
        return result;
    }
}

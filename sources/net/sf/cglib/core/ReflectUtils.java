package net.sf.cglib.core;

import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.xmlbeans.XmlErrorCodes;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Type;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: cglib-3.1.jar:net/sf/cglib/core/ReflectUtils.class */
public class ReflectUtils {
    private static final Map primitives = new HashMap(8);
    private static final Map transforms = new HashMap(8);
    private static final ClassLoader defaultLoader;
    private static Method DEFINE_CLASS;
    private static final ProtectionDomain PROTECTION_DOMAIN;
    private static final String[] CGLIB_PACKAGES;
    static Class class$net$sf$cglib$core$ReflectUtils;
    static Class class$java$lang$String;
    static Class array$B;
    static Class class$java$security$ProtectionDomain;
    static Class class$java$lang$Object;

    private ReflectUtils() {
    }

    static {
        Class clsClass$;
        if (class$net$sf$cglib$core$ReflectUtils == null) {
            clsClass$ = class$("net.sf.cglib.core.ReflectUtils");
            class$net$sf$cglib$core$ReflectUtils = clsClass$;
        } else {
            clsClass$ = class$net$sf$cglib$core$ReflectUtils;
        }
        defaultLoader = clsClass$.getClassLoader();
        PROTECTION_DOMAIN = (ProtectionDomain) AccessController.doPrivileged(new PrivilegedAction() { // from class: net.sf.cglib.core.ReflectUtils.1
            @Override // java.security.PrivilegedAction
            public Object run() {
                Class clsClass$2;
                if (ReflectUtils.class$net$sf$cglib$core$ReflectUtils == null) {
                    clsClass$2 = ReflectUtils.class$("net.sf.cglib.core.ReflectUtils");
                    ReflectUtils.class$net$sf$cglib$core$ReflectUtils = clsClass$2;
                } else {
                    clsClass$2 = ReflectUtils.class$net$sf$cglib$core$ReflectUtils;
                }
                return clsClass$2.getProtectionDomain();
            }
        });
        AccessController.doPrivileged(new PrivilegedAction() { // from class: net.sf.cglib.core.ReflectUtils.2
            @Override // java.security.PrivilegedAction
            public Object run() throws ClassNotFoundException {
                Class<?> clsClass$2;
                Class<?> clsClass$3;
                Class<?> clsClass$4;
                try {
                    Class loader = Class.forName("java.lang.ClassLoader");
                    Class<?>[] clsArr = new Class[5];
                    if (ReflectUtils.class$java$lang$String == null) {
                        clsClass$2 = ReflectUtils.class$("java.lang.String");
                        ReflectUtils.class$java$lang$String = clsClass$2;
                    } else {
                        clsClass$2 = ReflectUtils.class$java$lang$String;
                    }
                    clsArr[0] = clsClass$2;
                    if (ReflectUtils.array$B == null) {
                        clsClass$3 = ReflectUtils.class$("[B");
                        ReflectUtils.array$B = clsClass$3;
                    } else {
                        clsClass$3 = ReflectUtils.array$B;
                    }
                    clsArr[1] = clsClass$3;
                    clsArr[2] = Integer.TYPE;
                    clsArr[3] = Integer.TYPE;
                    if (ReflectUtils.class$java$security$ProtectionDomain == null) {
                        clsClass$4 = ReflectUtils.class$("java.security.ProtectionDomain");
                        ReflectUtils.class$java$security$ProtectionDomain = clsClass$4;
                    } else {
                        clsClass$4 = ReflectUtils.class$java$security$ProtectionDomain;
                    }
                    clsArr[4] = clsClass$4;
                    Method unused = ReflectUtils.DEFINE_CLASS = loader.getDeclaredMethod("defineClass", clsArr);
                    ReflectUtils.DEFINE_CLASS.setAccessible(true);
                    return null;
                } catch (ClassNotFoundException e) {
                    throw new CodeGenerationException(e);
                } catch (NoSuchMethodException e2) {
                    throw new CodeGenerationException(e2);
                }
            }
        });
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

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
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
        String prefix = dimensions > 0 ? new StringBuffer().append((Object) brackets).append(StandardRoles.L).toString() : "";
        String suffix = dimensions > 0 ? ScriptUtils.DEFAULT_STATEMENT_SEPARATOR : "";
        try {
            return Class.forName(new StringBuffer().append(prefix).append(className2).append(suffix).toString(), false, loader);
        } catch (ClassNotFoundException e) {
            for (String str : packages) {
                try {
                    return Class.forName(new StringBuffer().append(prefix).append(str).append('.').append(className2).append(suffix).toString(), false, loader);
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
                        return Class.forName(new StringBuffer().append((Object) brackets).append(transform).toString(), false, loader);
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
            try {
                try {
                    cstruct.setAccessible(true);
                    Object result = cstruct.newInstance(args);
                    cstruct.setAccessible(flag);
                    return result;
                } catch (IllegalAccessException e) {
                    throw new CodeGenerationException(e);
                } catch (InvocationTargetException e2) {
                    throw new CodeGenerationException(e2.getTargetException());
                }
            } catch (InstantiationException e3) {
                throw new CodeGenerationException(e3);
            }
        } catch (Throwable th) {
            cstruct.setAccessible(flag);
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
            throw new IllegalArgumentException(new StringBuffer().append(iface).append(" missing newInstance method").toString());
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
        Class clsClass$;
        try {
            if (class$java$lang$Object == null) {
                clsClass$ = class$("java.lang.Object");
                class$java$lang$Object = clsClass$;
            } else {
                clsClass$ = class$java$lang$Object;
            }
            BeanInfo info = Introspector.getBeanInfo(type, clsClass$);
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
        list.addAll(Arrays.asList(type.getDeclaredMethods()));
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
            throw new IllegalArgumentException(new StringBuffer().append(iface).append(" is not an interface").toString());
        }
        Method[] methods = iface.getDeclaredMethods();
        if (methods.length != 1) {
            throw new IllegalArgumentException(new StringBuffer().append("expecting exactly 1 method in ").append(iface).toString());
        }
        return methods[0];
    }

    public static Class defineClass(String className, byte[] b, ClassLoader loader) throws Exception {
        Object[] args = {className, b, new Integer(0), new Integer(b.length), PROTECTION_DOMAIN};
        Class c = (Class) DEFINE_CLASS.invoke(loader, args);
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

    public static MethodInfo getMethodInfo(Member member, int modifiers) {
        Signature sig = getSignature(member);
        return new MethodInfo(member, modifiers, sig) { // from class: net.sf.cglib.core.ReflectUtils.3
            private ClassInfo ci;
            private final Member val$member;
            private final int val$modifiers;
            private final Signature val$sig;

            {
                this.val$member = member;
                this.val$modifiers = modifiers;
                this.val$sig = sig;
            }

            @Override // net.sf.cglib.core.MethodInfo
            public ClassInfo getClassInfo() {
                if (this.ci == null) {
                    this.ci = ReflectUtils.getClassInfo(this.val$member.getDeclaringClass());
                }
                return this.ci;
            }

            @Override // net.sf.cglib.core.MethodInfo
            public int getModifiers() {
                return this.val$modifiers;
            }

            @Override // net.sf.cglib.core.MethodInfo
            public Signature getSignature() {
                return this.val$sig;
            }

            @Override // net.sf.cglib.core.MethodInfo
            public Type[] getExceptionTypes() {
                return ReflectUtils.getExceptionTypes(this.val$member);
            }

            public Attribute getAttribute() {
                return null;
            }
        };
    }

    public static MethodInfo getMethodInfo(Member member) {
        return getMethodInfo(member, member.getModifiers());
    }

    public static ClassInfo getClassInfo(Class clazz) {
        Type type = Type.getType((Class<?>) clazz);
        Type sc = clazz.getSuperclass() == null ? null : Type.getType((Class<?>) clazz.getSuperclass());
        return new ClassInfo(type, sc, clazz) { // from class: net.sf.cglib.core.ReflectUtils.4
            private final Type val$type;
            private final Type val$sc;
            private final Class val$clazz;

            {
                this.val$type = type;
                this.val$sc = sc;
                this.val$clazz = clazz;
            }

            @Override // net.sf.cglib.core.ClassInfo
            public Type getType() {
                return this.val$type;
            }

            @Override // net.sf.cglib.core.ClassInfo
            public Type getSuperType() {
                return this.val$sc;
            }

            @Override // net.sf.cglib.core.ClassInfo
            public Type[] getInterfaces() {
                return TypeUtils.getTypes(this.val$clazz.getInterfaces());
            }

            @Override // net.sf.cglib.core.ClassInfo
            public int getModifiers() {
                return this.val$clazz.getModifiers();
            }
        };
    }

    public static Method[] findMethods(String[] namesAndDescriptors, Method[] methods) {
        Map map = new HashMap();
        for (Method method : methods) {
            map.put(new StringBuffer().append(method.getName()).append(Type.getMethodDescriptor(method)).toString(), method);
        }
        Method[] result = new Method[namesAndDescriptors.length / 2];
        for (int i = 0; i < result.length; i++) {
            result[i] = (Method) map.get(new StringBuffer().append(namesAndDescriptors[i * 2]).append(namesAndDescriptors[(i * 2) + 1]).toString());
            if (result[i] == null) {
            }
        }
        return result;
    }
}

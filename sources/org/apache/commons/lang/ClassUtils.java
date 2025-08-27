package org.apache.commons.lang;

import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.xmlbeans.XmlErrorCodes;
import org.springframework.beans.PropertyAccessor;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/ClassUtils.class */
public class ClassUtils {
    public static final char PACKAGE_SEPARATOR_CHAR = '.';
    public static final char INNER_CLASS_SEPARATOR_CHAR = '$';
    private static final Map wrapperPrimitiveMap;
    private static final Map abbreviationMap;
    private static final Map reverseAbbreviationMap;
    static Class class$java$lang$Boolean;
    static Class class$java$lang$Byte;
    static Class class$java$lang$Character;
    static Class class$java$lang$Short;
    static Class class$java$lang$Integer;
    static Class class$java$lang$Long;
    static Class class$java$lang$Double;
    static Class class$java$lang$Float;
    static Class class$org$apache$commons$lang$ClassUtils;
    public static final String PACKAGE_SEPARATOR = String.valueOf('.');
    public static final String INNER_CLASS_SEPARATOR = String.valueOf('$');
    private static final Map primitiveWrapperMap = new HashMap();

    static {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        Class clsClass$4;
        Class clsClass$5;
        Class clsClass$6;
        Class clsClass$7;
        Class clsClass$8;
        Map map = primitiveWrapperMap;
        Class cls = Boolean.TYPE;
        if (class$java$lang$Boolean == null) {
            clsClass$ = class$("java.lang.Boolean");
            class$java$lang$Boolean = clsClass$;
        } else {
            clsClass$ = class$java$lang$Boolean;
        }
        map.put(cls, clsClass$);
        Map map2 = primitiveWrapperMap;
        Class cls2 = Byte.TYPE;
        if (class$java$lang$Byte == null) {
            clsClass$2 = class$("java.lang.Byte");
            class$java$lang$Byte = clsClass$2;
        } else {
            clsClass$2 = class$java$lang$Byte;
        }
        map2.put(cls2, clsClass$2);
        Map map3 = primitiveWrapperMap;
        Class cls3 = Character.TYPE;
        if (class$java$lang$Character == null) {
            clsClass$3 = class$("java.lang.Character");
            class$java$lang$Character = clsClass$3;
        } else {
            clsClass$3 = class$java$lang$Character;
        }
        map3.put(cls3, clsClass$3);
        Map map4 = primitiveWrapperMap;
        Class cls4 = Short.TYPE;
        if (class$java$lang$Short == null) {
            clsClass$4 = class$("java.lang.Short");
            class$java$lang$Short = clsClass$4;
        } else {
            clsClass$4 = class$java$lang$Short;
        }
        map4.put(cls4, clsClass$4);
        Map map5 = primitiveWrapperMap;
        Class cls5 = Integer.TYPE;
        if (class$java$lang$Integer == null) {
            clsClass$5 = class$("java.lang.Integer");
            class$java$lang$Integer = clsClass$5;
        } else {
            clsClass$5 = class$java$lang$Integer;
        }
        map5.put(cls5, clsClass$5);
        Map map6 = primitiveWrapperMap;
        Class cls6 = Long.TYPE;
        if (class$java$lang$Long == null) {
            clsClass$6 = class$("java.lang.Long");
            class$java$lang$Long = clsClass$6;
        } else {
            clsClass$6 = class$java$lang$Long;
        }
        map6.put(cls6, clsClass$6);
        Map map7 = primitiveWrapperMap;
        Class cls7 = Double.TYPE;
        if (class$java$lang$Double == null) {
            clsClass$7 = class$("java.lang.Double");
            class$java$lang$Double = clsClass$7;
        } else {
            clsClass$7 = class$java$lang$Double;
        }
        map7.put(cls7, clsClass$7);
        Map map8 = primitiveWrapperMap;
        Class cls8 = Float.TYPE;
        if (class$java$lang$Float == null) {
            clsClass$8 = class$("java.lang.Float");
            class$java$lang$Float = clsClass$8;
        } else {
            clsClass$8 = class$java$lang$Float;
        }
        map8.put(cls8, clsClass$8);
        primitiveWrapperMap.put(Void.TYPE, Void.TYPE);
        wrapperPrimitiveMap = new HashMap();
        for (Class primitiveClass : primitiveWrapperMap.keySet()) {
            Class wrapperClass = (Class) primitiveWrapperMap.get(primitiveClass);
            if (!primitiveClass.equals(wrapperClass)) {
                wrapperPrimitiveMap.put(wrapperClass, primitiveClass);
            }
        }
        abbreviationMap = new HashMap();
        reverseAbbreviationMap = new HashMap();
        addAbbreviation(XmlErrorCodes.INT, "I");
        addAbbreviation("boolean", "Z");
        addAbbreviation(XmlErrorCodes.FLOAT, "F");
        addAbbreviation(XmlErrorCodes.LONG, "J");
        addAbbreviation("short", "S");
        addAbbreviation("byte", "B");
        addAbbreviation(XmlErrorCodes.DOUBLE, "D");
        addAbbreviation("char", "C");
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    private static void addAbbreviation(String primitive, String abbreviation) {
        abbreviationMap.put(primitive, abbreviation);
        reverseAbbreviationMap.put(abbreviation, primitive);
    }

    public static String getShortClassName(Object object, String valueIfNull) {
        if (object == null) {
            return valueIfNull;
        }
        return getShortClassName(object.getClass());
    }

    public static String getShortClassName(Class cls) {
        if (cls == null) {
            return "";
        }
        return getShortClassName(cls.getName());
    }

    public static String getShortClassName(String className) {
        if (className == null || className.length() == 0) {
            return "";
        }
        StrBuilder arrayPrefix = new StrBuilder();
        if (className.startsWith(PropertyAccessor.PROPERTY_KEY_PREFIX)) {
            while (className.charAt(0) == '[') {
                className = className.substring(1);
                arrayPrefix.append("[]");
            }
            if (className.charAt(0) == 'L' && className.charAt(className.length() - 1) == ';') {
                className = className.substring(1, className.length() - 1);
            }
        }
        if (reverseAbbreviationMap.containsKey(className)) {
            className = (String) reverseAbbreviationMap.get(className);
        }
        int lastDotIdx = className.lastIndexOf(46);
        int innerIdx = className.indexOf(36, lastDotIdx == -1 ? 0 : lastDotIdx + 1);
        String out = className.substring(lastDotIdx + 1);
        if (innerIdx != -1) {
            out = out.replace('$', '.');
        }
        return new StringBuffer().append(out).append(arrayPrefix).toString();
    }

    public static String getPackageName(Object object, String valueIfNull) {
        if (object == null) {
            return valueIfNull;
        }
        return getPackageName(object.getClass());
    }

    public static String getPackageName(Class cls) {
        if (cls == null) {
            return "";
        }
        return getPackageName(cls.getName());
    }

    public static String getPackageName(String className) {
        if (className == null || className.length() == 0) {
            return "";
        }
        while (className.charAt(0) == '[') {
            className = className.substring(1);
        }
        if (className.charAt(0) == 'L' && className.charAt(className.length() - 1) == ';') {
            className = className.substring(1);
        }
        int i = className.lastIndexOf(46);
        if (i == -1) {
            return "";
        }
        return className.substring(0, i);
    }

    public static List getAllSuperclasses(Class cls) {
        if (cls == null) {
            return null;
        }
        List classes = new ArrayList();
        Class superclass = cls.getSuperclass();
        while (true) {
            Class superclass2 = superclass;
            if (superclass2 != null) {
                classes.add(superclass2);
                superclass = superclass2.getSuperclass();
            } else {
                return classes;
            }
        }
    }

    public static List getAllInterfaces(Class cls) {
        if (cls == null) {
            return null;
        }
        List interfacesFound = new ArrayList();
        getAllInterfaces(cls, interfacesFound);
        return interfacesFound;
    }

    private static void getAllInterfaces(Class cls, List interfacesFound) {
        while (cls != null) {
            Class[] interfaces = cls.getInterfaces();
            for (int i = 0; i < interfaces.length; i++) {
                if (!interfacesFound.contains(interfaces[i])) {
                    interfacesFound.add(interfaces[i]);
                    getAllInterfaces(interfaces[i], interfacesFound);
                }
            }
            cls = cls.getSuperclass();
        }
    }

    public static List convertClassNamesToClasses(List classNames) {
        if (classNames == null) {
            return null;
        }
        List classes = new ArrayList(classNames.size());
        Iterator it = classNames.iterator();
        while (it.hasNext()) {
            String className = (String) it.next();
            try {
                classes.add(Class.forName(className));
            } catch (Exception e) {
                classes.add(null);
            }
        }
        return classes;
    }

    public static List convertClassesToClassNames(List classes) {
        if (classes == null) {
            return null;
        }
        List classNames = new ArrayList(classes.size());
        Iterator it = classes.iterator();
        while (it.hasNext()) {
            Class cls = (Class) it.next();
            if (cls == null) {
                classNames.add(null);
            } else {
                classNames.add(cls.getName());
            }
        }
        return classNames;
    }

    public static boolean isAssignable(Class[] classArray, Class[] toClassArray) {
        return isAssignable(classArray, toClassArray, false);
    }

    public static boolean isAssignable(Class[] classArray, Class[] toClassArray, boolean autoboxing) {
        if (!ArrayUtils.isSameLength(classArray, toClassArray)) {
            return false;
        }
        if (classArray == null) {
            classArray = ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        if (toClassArray == null) {
            toClassArray = ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        for (int i = 0; i < classArray.length; i++) {
            if (!isAssignable(classArray[i], toClassArray[i], autoboxing)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAssignable(Class cls, Class toClass) {
        return isAssignable(cls, toClass, false);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static boolean isAssignable(Class cls, Class cls2, boolean autoboxing) {
        if (cls2 == 0) {
            return false;
        }
        if (cls == null) {
            return !cls2.isPrimitive();
        }
        if (autoboxing) {
            if (cls.isPrimitive() && !cls2.isPrimitive()) {
                cls = primitiveToWrapper(cls);
                if (cls == null) {
                    return false;
                }
            }
            if (cls2.isPrimitive() && !cls.isPrimitive()) {
                cls = wrapperToPrimitive(cls);
                if (cls == null) {
                    return false;
                }
            }
        }
        if (cls.equals(cls2)) {
            return true;
        }
        if (cls.isPrimitive()) {
            if (!cls2.isPrimitive()) {
                return false;
            }
            if (Integer.TYPE.equals(cls)) {
                return Long.TYPE.equals(cls2) || Float.TYPE.equals(cls2) || Double.TYPE.equals(cls2);
            }
            if (Long.TYPE.equals(cls)) {
                return Float.TYPE.equals(cls2) || Double.TYPE.equals(cls2);
            }
            if (Boolean.TYPE.equals(cls) || Double.TYPE.equals(cls)) {
                return false;
            }
            if (Float.TYPE.equals(cls)) {
                return Double.TYPE.equals(cls2);
            }
            if (Character.TYPE.equals(cls)) {
                return Integer.TYPE.equals(cls2) || Long.TYPE.equals(cls2) || Float.TYPE.equals(cls2) || Double.TYPE.equals(cls2);
            }
            if (Short.TYPE.equals(cls)) {
                return Integer.TYPE.equals(cls2) || Long.TYPE.equals(cls2) || Float.TYPE.equals(cls2) || Double.TYPE.equals(cls2);
            }
            if (Byte.TYPE.equals(cls)) {
                return Short.TYPE.equals(cls2) || Integer.TYPE.equals(cls2) || Long.TYPE.equals(cls2) || Float.TYPE.equals(cls2) || Double.TYPE.equals(cls2);
            }
            return false;
        }
        return cls2.isAssignableFrom(cls);
    }

    public static Class primitiveToWrapper(Class cls) {
        Class convertedClass = cls;
        if (cls != null && cls.isPrimitive()) {
            convertedClass = (Class) primitiveWrapperMap.get(cls);
        }
        return convertedClass;
    }

    public static Class[] primitivesToWrappers(Class[] classes) {
        if (classes == null) {
            return null;
        }
        if (classes.length == 0) {
            return classes;
        }
        Class[] convertedClasses = new Class[classes.length];
        for (int i = 0; i < classes.length; i++) {
            convertedClasses[i] = primitiveToWrapper(classes[i]);
        }
        return convertedClasses;
    }

    public static Class wrapperToPrimitive(Class cls) {
        return (Class) wrapperPrimitiveMap.get(cls);
    }

    public static Class[] wrappersToPrimitives(Class[] classes) {
        if (classes == null) {
            return null;
        }
        if (classes.length == 0) {
            return classes;
        }
        Class[] convertedClasses = new Class[classes.length];
        for (int i = 0; i < classes.length; i++) {
            convertedClasses[i] = wrapperToPrimitive(classes[i]);
        }
        return convertedClasses;
    }

    public static boolean isInnerClass(Class cls) {
        return cls != null && cls.getName().indexOf(36) >= 0;
    }

    public static Class getClass(ClassLoader classLoader, String className, boolean initialize) throws ClassNotFoundException {
        Class clazz;
        try {
            if (abbreviationMap.containsKey(className)) {
                String clsName = new StringBuffer().append(PropertyAccessor.PROPERTY_KEY_PREFIX).append(abbreviationMap.get(className)).toString();
                clazz = Class.forName(clsName, initialize, classLoader).getComponentType();
            } else {
                clazz = Class.forName(toCanonicalName(className), initialize, classLoader);
            }
            return clazz;
        } catch (ClassNotFoundException ex) {
            int lastDotIndex = className.lastIndexOf(46);
            if (lastDotIndex != -1) {
                try {
                    return getClass(classLoader, new StringBuffer().append(className.substring(0, lastDotIndex)).append('$').append(className.substring(lastDotIndex + 1)).toString(), initialize);
                } catch (ClassNotFoundException e) {
                    throw ex;
                }
            }
            throw ex;
        }
    }

    public static Class getClass(ClassLoader classLoader, String className) throws ClassNotFoundException {
        return getClass(classLoader, className, true);
    }

    public static Class getClass(String className) throws ClassNotFoundException {
        return getClass(className, true);
    }

    public static Class getClass(String className, boolean initialize) throws ClassNotFoundException {
        ClassLoader classLoader;
        Class clsClass$;
        ClassLoader contextCL = Thread.currentThread().getContextClassLoader();
        if (contextCL == null) {
            if (class$org$apache$commons$lang$ClassUtils == null) {
                clsClass$ = class$("org.apache.commons.lang.ClassUtils");
                class$org$apache$commons$lang$ClassUtils = clsClass$;
            } else {
                clsClass$ = class$org$apache$commons$lang$ClassUtils;
            }
            classLoader = clsClass$.getClassLoader();
        } else {
            classLoader = contextCL;
        }
        ClassLoader loader = classLoader;
        return getClass(loader, className, initialize);
    }

    public static Method getPublicMethod(Class cls, String methodName, Class[] parameterTypes) throws NoSuchMethodException, SecurityException {
        Method declaredMethod = cls.getMethod(methodName, parameterTypes);
        if (Modifier.isPublic(declaredMethod.getDeclaringClass().getModifiers())) {
            return declaredMethod;
        }
        List<Class> candidateClasses = new ArrayList();
        candidateClasses.addAll(getAllInterfaces(cls));
        candidateClasses.addAll(getAllSuperclasses(cls));
        for (Class candidateClass : candidateClasses) {
            if (Modifier.isPublic(candidateClass.getModifiers())) {
                try {
                    Method candidateMethod = candidateClass.getMethod(methodName, parameterTypes);
                    if (Modifier.isPublic(candidateMethod.getDeclaringClass().getModifiers())) {
                        return candidateMethod;
                    }
                } catch (NoSuchMethodException e) {
                }
            }
        }
        throw new NoSuchMethodException(new StringBuffer().append("Can't find a public method for ").append(methodName).append(SymbolConstants.SPACE_SYMBOL).append(ArrayUtils.toString(parameterTypes)).toString());
    }

    private static String toCanonicalName(String className) {
        String className2 = StringUtils.deleteWhitespace(className);
        if (className2 == null) {
            throw new NullArgumentException("className");
        }
        if (className2.endsWith("[]")) {
            StrBuilder classNameBuffer = new StrBuilder();
            while (className2.endsWith("[]")) {
                className2 = className2.substring(0, className2.length() - 2);
                classNameBuffer.append(PropertyAccessor.PROPERTY_KEY_PREFIX);
            }
            String abbreviation = (String) abbreviationMap.get(className2);
            if (abbreviation != null) {
                classNameBuffer.append(abbreviation);
            } else {
                classNameBuffer.append(StandardRoles.L).append(className2).append(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR);
            }
            className2 = classNameBuffer.toString();
        }
        return className2;
    }

    public static Class[] toClass(Object[] array) {
        if (array == null) {
            return null;
        }
        if (array.length == 0) {
            return ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        Class[] classes = new Class[array.length];
        for (int i = 0; i < array.length; i++) {
            classes[i] = array[i] == null ? null : array[i].getClass();
        }
        return classes;
    }

    public static String getShortCanonicalName(Object object, String valueIfNull) {
        if (object == null) {
            return valueIfNull;
        }
        return getShortCanonicalName(object.getClass().getName());
    }

    public static String getShortCanonicalName(Class cls) {
        if (cls == null) {
            return "";
        }
        return getShortCanonicalName(cls.getName());
    }

    public static String getShortCanonicalName(String canonicalName) {
        return getShortClassName(getCanonicalName(canonicalName));
    }

    public static String getPackageCanonicalName(Object object, String valueIfNull) {
        if (object == null) {
            return valueIfNull;
        }
        return getPackageCanonicalName(object.getClass().getName());
    }

    public static String getPackageCanonicalName(Class cls) {
        if (cls == null) {
            return "";
        }
        return getPackageCanonicalName(cls.getName());
    }

    public static String getPackageCanonicalName(String canonicalName) {
        return getPackageName(getCanonicalName(canonicalName));
    }

    private static String getCanonicalName(String className) {
        String className2 = StringUtils.deleteWhitespace(className);
        if (className2 == null) {
            return null;
        }
        int dim = 0;
        while (className2.startsWith(PropertyAccessor.PROPERTY_KEY_PREFIX)) {
            dim++;
            className2 = className2.substring(1);
        }
        if (dim < 1) {
            return className2;
        }
        if (className2.startsWith(StandardRoles.L)) {
            className2 = className2.substring(1, className2.endsWith(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR) ? className2.length() - 1 : className2.length());
        } else if (className2.length() > 0) {
            className2 = (String) reverseAbbreviationMap.get(className2.substring(0, 1));
        }
        StrBuilder canonicalClassNameBuffer = new StrBuilder(className2);
        for (int i = 0; i < dim; i++) {
            canonicalClassNameBuffer.append("[]");
        }
        return canonicalClassNameBuffer.toString();
    }
}

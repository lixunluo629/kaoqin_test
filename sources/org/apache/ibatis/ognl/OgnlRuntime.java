package org.apache.ibatis.ognl;

import ch.qos.logback.core.rolling.helper.DateTokenConverter;
import com.alibaba.excel.constant.ExcelXmlConstants;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.beans.BeanInfo;
import java.beans.IndexedPropertyDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.Permission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.ibatis.ognl.enhance.ExpressionCompiler;
import org.apache.ibatis.ognl.enhance.OgnlExpressionCompiler;
import org.apache.ibatis.ognl.internal.ClassCache;
import org.apache.ibatis.ognl.internal.ClassCacheImpl;
import org.apache.xmlbeans.XmlErrorCodes;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/OgnlRuntime.class */
public class OgnlRuntime {
    private static final String SET_PREFIX = "set";
    private static final String GET_PREFIX = "get";
    private static final String IS_PREFIX = "is";
    private static final int HEX_LENGTH = 8;
    private static final String NULL_OBJECT_STRING = "<null>";
    static ClassCacheInspector _cacheInspector;
    private static OgnlExpressionCompiler _compiler;
    private static final Class[] EMPTY_CLASS_ARRAY;
    private static IdentityHashMap PRIMITIVE_WRAPPER_CLASSES;
    private static final Map NUMERIC_CASTS;
    private static final Map NUMERIC_VALUES;
    private static final Map NUMERIC_LITERALS;
    private static final Map NUMERIC_DEFAULTS;
    public static final ArgsCompatbilityReport NoArgsReport;
    public static final Object NotFound = new Object();
    public static final List NotFoundList = new ArrayList();
    public static final Map NotFoundMap = new HashMap();
    public static final Object[] NoArguments = new Object[0];
    public static final Class[] NoArgumentTypes = new Class[0];
    public static final Object NoConversionPossible = "org.apache.ibatis.ognl.NoConversionPossible";
    public static int INDEXED_PROPERTY_NONE = 0;
    public static int INDEXED_PROPERTY_INT = 1;
    public static int INDEXED_PROPERTY_OBJECT = 2;
    public static final String NULL_STRING = "" + ((Object) null);
    private static final Map HEX_PADDING = new HashMap();
    private static boolean _jdk15 = false;
    private static boolean _jdkChecked = false;
    static final ClassCache _methodAccessors = new ClassCacheImpl();
    static final ClassCache _propertyAccessors = new ClassCacheImpl();
    static final ClassCache _elementsAccessors = new ClassCacheImpl();
    static final ClassCache _nullHandlers = new ClassCacheImpl();
    static final ClassCache _propertyDescriptorCache = new ClassCacheImpl();
    static final ClassCache _constructorCache = new ClassCacheImpl();
    static final ClassCache _staticMethodCache = new ClassCacheImpl();
    static final ClassCache _instanceMethodCache = new ClassCacheImpl();
    static final ClassCache _invokePermissionCache = new ClassCacheImpl();
    static final ClassCache _fieldCache = new ClassCacheImpl();
    static final List _superclasses = new ArrayList();
    static final ClassCache[] _declaredMethods = {new ClassCacheImpl(), new ClassCacheImpl()};
    static final Map _primitiveTypes = new HashMap(101);
    static final ClassCache _primitiveDefaults = new ClassCacheImpl();
    static final Map _methodParameterTypesCache = new HashMap(101);
    static final Map _genericMethodParameterTypesCache = new HashMap(101);
    static final Map _ctorParameterTypesCache = new HashMap(101);
    static SecurityManager _securityManager = System.getSecurityManager();
    static final EvaluationPool _evaluationPool = new EvaluationPool();
    static final ObjectArrayPool _objectArrayPool = new ObjectArrayPool();
    static final Map<Method, Boolean> _methodAccessCache = new ConcurrentHashMap();
    static final Map<Method, Boolean> _methodPermCache = new ConcurrentHashMap();
    static final ClassPropertyMethodCache cacheSetMethod = new ClassPropertyMethodCache();
    static final ClassPropertyMethodCache cacheGetMethod = new ClassPropertyMethodCache();

    static {
        try {
            Class.forName("org.apache.ibatis.javassist.ClassPool");
            _compiler = new ExpressionCompiler();
            EMPTY_CLASS_ARRAY = new Class[0];
            PRIMITIVE_WRAPPER_CLASSES = new IdentityHashMap();
            PRIMITIVE_WRAPPER_CLASSES.put(Boolean.TYPE, Boolean.class);
            PRIMITIVE_WRAPPER_CLASSES.put(Boolean.class, Boolean.TYPE);
            PRIMITIVE_WRAPPER_CLASSES.put(Byte.TYPE, Byte.class);
            PRIMITIVE_WRAPPER_CLASSES.put(Byte.class, Byte.TYPE);
            PRIMITIVE_WRAPPER_CLASSES.put(Character.TYPE, Character.class);
            PRIMITIVE_WRAPPER_CLASSES.put(Character.class, Character.TYPE);
            PRIMITIVE_WRAPPER_CLASSES.put(Short.TYPE, Short.class);
            PRIMITIVE_WRAPPER_CLASSES.put(Short.class, Short.TYPE);
            PRIMITIVE_WRAPPER_CLASSES.put(Integer.TYPE, Integer.class);
            PRIMITIVE_WRAPPER_CLASSES.put(Integer.class, Integer.TYPE);
            PRIMITIVE_WRAPPER_CLASSES.put(Long.TYPE, Long.class);
            PRIMITIVE_WRAPPER_CLASSES.put(Long.class, Long.TYPE);
            PRIMITIVE_WRAPPER_CLASSES.put(Float.TYPE, Float.class);
            PRIMITIVE_WRAPPER_CLASSES.put(Float.class, Float.TYPE);
            PRIMITIVE_WRAPPER_CLASSES.put(Double.TYPE, Double.class);
            PRIMITIVE_WRAPPER_CLASSES.put(Double.class, Double.TYPE);
            NUMERIC_CASTS = new HashMap();
            NUMERIC_CASTS.put(Double.class, "(double)");
            NUMERIC_CASTS.put(Float.class, "(float)");
            NUMERIC_CASTS.put(Integer.class, "(int)");
            NUMERIC_CASTS.put(Long.class, "(long)");
            NUMERIC_CASTS.put(BigDecimal.class, "(double)");
            NUMERIC_CASTS.put(BigInteger.class, "");
            NUMERIC_VALUES = new HashMap();
            NUMERIC_VALUES.put(Double.class, "doubleValue()");
            NUMERIC_VALUES.put(Float.class, "floatValue()");
            NUMERIC_VALUES.put(Integer.class, "intValue()");
            NUMERIC_VALUES.put(Long.class, "longValue()");
            NUMERIC_VALUES.put(Short.class, "shortValue()");
            NUMERIC_VALUES.put(Byte.class, "byteValue()");
            NUMERIC_VALUES.put(BigDecimal.class, "doubleValue()");
            NUMERIC_VALUES.put(BigInteger.class, "doubleValue()");
            NUMERIC_VALUES.put(Boolean.class, "booleanValue()");
            NUMERIC_LITERALS = new HashMap();
            NUMERIC_LITERALS.put(Integer.class, "");
            NUMERIC_LITERALS.put(Integer.TYPE, "");
            NUMERIC_LITERALS.put(Long.class, "l");
            NUMERIC_LITERALS.put(Long.TYPE, "l");
            NUMERIC_LITERALS.put(BigInteger.class, DateTokenConverter.CONVERTER_KEY);
            NUMERIC_LITERALS.put(Float.class, ExcelXmlConstants.CELL_FORMULA_TAG);
            NUMERIC_LITERALS.put(Float.TYPE, ExcelXmlConstants.CELL_FORMULA_TAG);
            NUMERIC_LITERALS.put(Double.class, DateTokenConverter.CONVERTER_KEY);
            NUMERIC_LITERALS.put(Double.TYPE, DateTokenConverter.CONVERTER_KEY);
            NUMERIC_LITERALS.put(BigInteger.class, DateTokenConverter.CONVERTER_KEY);
            NUMERIC_LITERALS.put(BigDecimal.class, DateTokenConverter.CONVERTER_KEY);
            NUMERIC_DEFAULTS = new HashMap();
            NUMERIC_DEFAULTS.put(Boolean.class, Boolean.FALSE);
            NUMERIC_DEFAULTS.put(Byte.class, new Byte((byte) 0));
            NUMERIC_DEFAULTS.put(Short.class, new Short((short) 0));
            NUMERIC_DEFAULTS.put(Character.class, new Character((char) 0));
            NUMERIC_DEFAULTS.put(Integer.class, new Integer(0));
            NUMERIC_DEFAULTS.put(Long.class, new Long(0L));
            NUMERIC_DEFAULTS.put(Float.class, new Float(0.0f));
            NUMERIC_DEFAULTS.put(Double.class, new Double(0.0d));
            NUMERIC_DEFAULTS.put(BigInteger.class, new BigInteger("0"));
            NUMERIC_DEFAULTS.put(BigDecimal.class, new BigDecimal(0.0d));
            PropertyAccessor p = new ArrayPropertyAccessor();
            setPropertyAccessor(Object.class, new ObjectPropertyAccessor());
            setPropertyAccessor(byte[].class, p);
            setPropertyAccessor(short[].class, p);
            setPropertyAccessor(char[].class, p);
            setPropertyAccessor(int[].class, p);
            setPropertyAccessor(long[].class, p);
            setPropertyAccessor(float[].class, p);
            setPropertyAccessor(double[].class, p);
            setPropertyAccessor(Object[].class, p);
            setPropertyAccessor(List.class, new ListPropertyAccessor());
            setPropertyAccessor(Map.class, new MapPropertyAccessor());
            setPropertyAccessor(Set.class, new SetPropertyAccessor());
            setPropertyAccessor(Iterator.class, new IteratorPropertyAccessor());
            setPropertyAccessor(Enumeration.class, new EnumerationPropertyAccessor());
            ElementsAccessor e = new ArrayElementsAccessor();
            setElementsAccessor(Object.class, new ObjectElementsAccessor());
            setElementsAccessor(byte[].class, e);
            setElementsAccessor(short[].class, e);
            setElementsAccessor(char[].class, e);
            setElementsAccessor(int[].class, e);
            setElementsAccessor(long[].class, e);
            setElementsAccessor(float[].class, e);
            setElementsAccessor(double[].class, e);
            setElementsAccessor(Object[].class, e);
            setElementsAccessor(Collection.class, new CollectionElementsAccessor());
            setElementsAccessor(Map.class, new MapElementsAccessor());
            setElementsAccessor(Iterator.class, new IteratorElementsAccessor());
            setElementsAccessor(Enumeration.class, new EnumerationElementsAccessor());
            setElementsAccessor(Number.class, new NumberElementsAccessor());
            NullHandler nh = new ObjectNullHandler();
            setNullHandler(Object.class, nh);
            setNullHandler(byte[].class, nh);
            setNullHandler(short[].class, nh);
            setNullHandler(char[].class, nh);
            setNullHandler(int[].class, nh);
            setNullHandler(long[].class, nh);
            setNullHandler(float[].class, nh);
            setNullHandler(double[].class, nh);
            setNullHandler(Object[].class, nh);
            MethodAccessor ma = new ObjectMethodAccessor();
            setMethodAccessor(Object.class, ma);
            setMethodAccessor(byte[].class, ma);
            setMethodAccessor(short[].class, ma);
            setMethodAccessor(char[].class, ma);
            setMethodAccessor(int[].class, ma);
            setMethodAccessor(long[].class, ma);
            setMethodAccessor(float[].class, ma);
            setMethodAccessor(double[].class, ma);
            setMethodAccessor(Object[].class, ma);
            _primitiveTypes.put("boolean", Boolean.TYPE);
            _primitiveTypes.put("byte", Byte.TYPE);
            _primitiveTypes.put("short", Short.TYPE);
            _primitiveTypes.put("char", Character.TYPE);
            _primitiveTypes.put(XmlErrorCodes.INT, Integer.TYPE);
            _primitiveTypes.put(XmlErrorCodes.LONG, Long.TYPE);
            _primitiveTypes.put(XmlErrorCodes.FLOAT, Float.TYPE);
            _primitiveTypes.put(XmlErrorCodes.DOUBLE, Double.TYPE);
            _primitiveDefaults.put(Boolean.TYPE, Boolean.FALSE);
            _primitiveDefaults.put(Boolean.class, Boolean.FALSE);
            _primitiveDefaults.put(Byte.TYPE, new Byte((byte) 0));
            _primitiveDefaults.put(Byte.class, new Byte((byte) 0));
            _primitiveDefaults.put(Short.TYPE, new Short((short) 0));
            _primitiveDefaults.put(Short.class, new Short((short) 0));
            _primitiveDefaults.put(Character.TYPE, new Character((char) 0));
            _primitiveDefaults.put(Integer.TYPE, new Integer(0));
            _primitiveDefaults.put(Long.TYPE, new Long(0L));
            _primitiveDefaults.put(Float.TYPE, new Float(0.0f));
            _primitiveDefaults.put(Double.TYPE, new Double(0.0d));
            _primitiveDefaults.put(BigInteger.class, new BigInteger("0"));
            _primitiveDefaults.put(BigDecimal.class, new BigDecimal(0.0d));
            NoArgsReport = new ArgsCompatbilityReport(0, new boolean[0]);
        } catch (ClassNotFoundException e2) {
            throw new IllegalArgumentException("Javassist library is missing in classpath! Please add missed dependency!", e2);
        } catch (RuntimeException e3) {
            throw new IllegalStateException("Javassist library cannot be loaded, is it restricted by runtime environment?");
        }
    }

    public static void clearCache() {
        _methodParameterTypesCache.clear();
        _ctorParameterTypesCache.clear();
        _propertyDescriptorCache.clear();
        _constructorCache.clear();
        _staticMethodCache.clear();
        _instanceMethodCache.clear();
        _invokePermissionCache.clear();
        _fieldCache.clear();
        _superclasses.clear();
        _declaredMethods[0].clear();
        _declaredMethods[1].clear();
        _methodAccessCache.clear();
        _methodPermCache.clear();
    }

    public static boolean isJdk15() throws ClassNotFoundException {
        if (_jdkChecked) {
            return _jdk15;
        }
        try {
            Class.forName("java.lang.annotation.Annotation");
            _jdk15 = true;
        } catch (Exception e) {
        }
        _jdkChecked = true;
        return _jdk15;
    }

    public static String getNumericValueGetter(Class type) {
        return (String) NUMERIC_VALUES.get(type);
    }

    public static Class getPrimitiveWrapperClass(Class primitiveClass) {
        return (Class) PRIMITIVE_WRAPPER_CLASSES.get(primitiveClass);
    }

    public static String getNumericCast(Class type) {
        return (String) NUMERIC_CASTS.get(type);
    }

    public static String getNumericLiteral(Class type) {
        return (String) NUMERIC_LITERALS.get(type);
    }

    public static void setCompiler(OgnlExpressionCompiler compiler) {
        _compiler = compiler;
    }

    public static OgnlExpressionCompiler getCompiler() {
        return _compiler;
    }

    public static void compileExpression(OgnlContext context, Node expression, Object root) throws Exception {
        _compiler.compileExpression(context, expression, root);
    }

    public static Class getTargetClass(Object o) {
        if (o == null) {
            return null;
        }
        return o instanceof Class ? (Class) o : o.getClass();
    }

    public static String getBaseName(Object o) {
        if (o == null) {
            return null;
        }
        return getClassBaseName(o.getClass());
    }

    public static String getClassBaseName(Class c) {
        String s = c.getName();
        return s.substring(s.lastIndexOf(46) + 1);
    }

    public static String getClassName(Object o, boolean fullyQualified) {
        if (!(o instanceof Class)) {
            o = o.getClass();
        }
        return getClassName((Class) o, fullyQualified);
    }

    public static String getClassName(Class c, boolean fullyQualified) {
        return fullyQualified ? c.getName() : getClassBaseName(c);
    }

    public static String getPackageName(Object o) {
        if (o == null) {
            return null;
        }
        return getClassPackageName(o.getClass());
    }

    public static String getClassPackageName(Class c) {
        String s = c.getName();
        int i = s.lastIndexOf(46);
        if (i < 0) {
            return null;
        }
        return s.substring(0, i);
    }

    public static String getPointerString(int num) {
        StringBuffer result = new StringBuffer();
        String hex = Integer.toHexString(num);
        Integer l = new Integer(hex.length());
        String str = (String) HEX_PADDING.get(l);
        String pad = str;
        if (str == null) {
            StringBuffer pb = new StringBuffer();
            for (int i = hex.length(); i < 8; i++) {
                pb.append('0');
            }
            pad = new String(pb);
            HEX_PADDING.put(l, pad);
        }
        result.append(pad);
        result.append(hex);
        return new String(result);
    }

    public static String getPointerString(Object o) {
        return getPointerString(o == null ? 0 : System.identityHashCode(o));
    }

    public static String getUniqueDescriptor(Object object, boolean fullyQualified) throws IllegalArgumentException {
        StringBuffer result = new StringBuffer();
        if (object != null) {
            if (object instanceof Proxy) {
                Class interfaceClass = object.getClass().getInterfaces()[0];
                result.append(getClassName(interfaceClass, fullyQualified));
                result.append('^');
                object = Proxy.getInvocationHandler(object);
            }
            result.append(getClassName(object, fullyQualified));
            result.append('@');
            result.append(getPointerString(object));
        } else {
            result.append(NULL_OBJECT_STRING);
        }
        return new String(result);
    }

    public static String getUniqueDescriptor(Object object) {
        return getUniqueDescriptor(object, false);
    }

    public static Object[] toArray(List list) {
        Object[] result;
        int size = list.size();
        if (size == 0) {
            result = NoArguments;
        } else {
            result = getObjectArrayPool().create(list.size());
            for (int i = 0; i < size; i++) {
                result[i] = list.get(i);
            }
        }
        return result;
    }

    public static Class[] getParameterTypes(Method m) {
        Class[] clsArr;
        synchronized (_methodParameterTypesCache) {
            Class[] clsArr2 = (Class[]) _methodParameterTypesCache.get(m);
            Class[] result = clsArr2;
            if (clsArr2 == null) {
                Map map = _methodParameterTypesCache;
                Class[] parameterTypes = m.getParameterTypes();
                result = parameterTypes;
                map.put(m, parameterTypes);
            }
            clsArr = result;
        }
        return clsArr;
    }

    public static Class[] findParameterTypes(Class type, Method m) {
        Type[] genTypes = m.getGenericParameterTypes();
        Class[] types = new Class[genTypes.length];
        boolean noGenericParameter = true;
        int i = 0;
        while (true) {
            if (i >= genTypes.length) {
                break;
            }
            if (Class.class.isInstance(genTypes[i])) {
                types[i] = (Class) genTypes[i];
                i++;
            } else {
                noGenericParameter = false;
                break;
            }
        }
        if (noGenericParameter) {
            return types;
        }
        if (type == null || !isJdk15()) {
            return getParameterTypes(m);
        }
        Type typeGenericSuperclass = type.getGenericSuperclass();
        if (typeGenericSuperclass == null || !ParameterizedType.class.isInstance(typeGenericSuperclass) || m.getDeclaringClass().getTypeParameters() == null) {
            return getParameterTypes(m);
        }
        Class[] types2 = (Class[]) _genericMethodParameterTypesCache.get(m);
        if (types2 != null) {
            ParameterizedType genericSuperclass = (ParameterizedType) typeGenericSuperclass;
            if (Arrays.equals(types2, genericSuperclass.getActualTypeArguments())) {
                return types2;
            }
        }
        ParameterizedType param = (ParameterizedType) typeGenericSuperclass;
        TypeVariable[] declaredTypes = m.getDeclaringClass().getTypeParameters();
        Class[] types3 = new Class[genTypes.length];
        for (int i2 = 0; i2 < genTypes.length; i2++) {
            TypeVariable paramType = null;
            if (TypeVariable.class.isInstance(genTypes[i2])) {
                paramType = (TypeVariable) genTypes[i2];
            } else if (GenericArrayType.class.isInstance(genTypes[i2])) {
                paramType = (TypeVariable) ((GenericArrayType) genTypes[i2]).getGenericComponentType();
            } else {
                if (ParameterizedType.class.isInstance(genTypes[i2])) {
                    types3[i2] = (Class) ((ParameterizedType) genTypes[i2]).getRawType();
                } else if (Class.class.isInstance(genTypes[i2])) {
                    types3[i2] = (Class) genTypes[i2];
                }
            }
            Class resolved = resolveType(param, paramType, declaredTypes);
            if (resolved != null) {
                if (GenericArrayType.class.isInstance(genTypes[i2])) {
                    resolved = Array.newInstance((Class<?>) resolved, 0).getClass();
                }
                types3[i2] = resolved;
            } else {
                types3[i2] = m.getParameterTypes()[i2];
            }
        }
        synchronized (_genericMethodParameterTypesCache) {
            _genericMethodParameterTypesCache.put(m, types3);
        }
        return types3;
    }

    static Class resolveType(ParameterizedType param, TypeVariable var, TypeVariable[] declaredTypes) {
        if (param.getActualTypeArguments().length < 1) {
            return null;
        }
        for (int i = 0; i < declaredTypes.length; i++) {
            if (!TypeVariable.class.isInstance(param.getActualTypeArguments()[i]) && declaredTypes[i].getName().equals(var.getName())) {
                return (Class) param.getActualTypeArguments()[i];
            }
        }
        return null;
    }

    static Class findType(Type[] types, Class type) {
        for (int i = 0; i < types.length; i++) {
            if (Class.class.isInstance(types[i]) && type.isAssignableFrom((Class) types[i])) {
                return (Class) types[i];
            }
        }
        return null;
    }

    public static Class[] getParameterTypes(Constructor c) {
        Class[] clsArr = (Class[]) _ctorParameterTypesCache.get(c);
        Class[] result = clsArr;
        if (clsArr == null) {
            synchronized (_ctorParameterTypesCache) {
                Class[] clsArr2 = (Class[]) _ctorParameterTypesCache.get(c);
                result = clsArr2;
                if (clsArr2 == null) {
                    Map map = _ctorParameterTypesCache;
                    Class[] parameterTypes = c.getParameterTypes();
                    result = parameterTypes;
                    map.put(c, parameterTypes);
                }
            }
        }
        return result;
    }

    public static SecurityManager getSecurityManager() {
        return _securityManager;
    }

    public static void setSecurityManager(SecurityManager value) {
        _securityManager = value;
    }

    public static Permission getPermission(Method method) {
        Permission result;
        Class mc = method.getDeclaringClass();
        synchronized (_invokePermissionCache) {
            Map permissions = (Map) _invokePermissionCache.get(mc);
            if (permissions == null) {
                ClassCache classCache = _invokePermissionCache;
                HashMap map = new HashMap(101);
                permissions = map;
                classCache.put(mc, map);
            }
            Permission permission = (Permission) permissions.get(method.getName());
            result = permission;
            if (permission == null) {
                result = new OgnlInvokePermission("invoke." + mc.getName() + "." + method.getName());
                permissions.put(method.getName(), result);
            }
        }
        return result;
    }

    public static Object invokeMethod(Object target, Method method, Object[] argsArray) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Object result;
        boolean syncInvoke = false;
        boolean checkPermission = false;
        synchronized (method) {
            if (_methodAccessCache.get(method) == null) {
                if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible()) {
                    _methodAccessCache.put(method, Boolean.TRUE);
                } else {
                    _methodAccessCache.put(method, Boolean.FALSE);
                }
            }
            if (_methodAccessCache.get(method) == Boolean.TRUE) {
                syncInvoke = true;
            }
            if (_methodPermCache.get(method) == null) {
                if (_securityManager != null) {
                    try {
                        _securityManager.checkPermission(getPermission(method));
                        _methodPermCache.put(method, Boolean.TRUE);
                    } catch (SecurityException e) {
                        _methodPermCache.put(method, Boolean.FALSE);
                        throw new IllegalAccessException("Method [" + method + "] cannot be accessed.");
                    }
                } else {
                    _methodPermCache.put(method, Boolean.TRUE);
                }
            }
            if (_methodPermCache.get(method) == Boolean.FALSE) {
                checkPermission = true;
            }
        }
        if (syncInvoke) {
            synchronized (method) {
                if (checkPermission) {
                    try {
                        _securityManager.checkPermission(getPermission(method));
                    } catch (SecurityException e2) {
                        throw new IllegalAccessException("Method [" + method + "] cannot be accessed.");
                    }
                }
                method.setAccessible(true);
                result = method.invoke(target, argsArray);
                method.setAccessible(false);
            }
        } else {
            if (checkPermission) {
                try {
                    _securityManager.checkPermission(getPermission(method));
                } catch (SecurityException e3) {
                    throw new IllegalAccessException("Method [" + method + "] cannot be accessed.");
                }
            }
            result = method.invoke(target, argsArray);
        }
        return result;
    }

    public static final Class getArgClass(Object arg) {
        if (arg == null) {
            return null;
        }
        Class c = arg.getClass();
        if (c == Boolean.class) {
            return Boolean.TYPE;
        }
        if (c.getSuperclass() == Number.class) {
            if (c == Integer.class) {
                return Integer.TYPE;
            }
            if (c == Double.class) {
                return Double.TYPE;
            }
            if (c == Byte.class) {
                return Byte.TYPE;
            }
            if (c == Long.class) {
                return Long.TYPE;
            }
            if (c == Float.class) {
                return Float.TYPE;
            }
            if (c == Short.class) {
                return Short.TYPE;
            }
        } else if (c == Character.class) {
            return Character.TYPE;
        }
        return c;
    }

    public static Class[] getArgClasses(Object[] args) {
        if (args == null) {
            return null;
        }
        Class[] argClasses = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            argClasses[i] = getArgClass(args[i]);
        }
        return argClasses;
    }

    public static final boolean isTypeCompatible(Object object, Class c) {
        if (object == null) {
            return true;
        }
        ArgsCompatbilityReport report = new ArgsCompatbilityReport(0, new boolean[1]);
        if (!isTypeCompatible(getArgClass(object), c, 0, report) || report.conversionNeeded[0]) {
            return false;
        }
        return true;
    }

    public static final boolean isTypeCompatible(Class parameterClass, Class methodArgumentClass, int index, ArgsCompatbilityReport report) {
        if (parameterClass == null) {
            report.score += 500;
            return true;
        }
        if (parameterClass == methodArgumentClass) {
            return true;
        }
        if (methodArgumentClass.isArray()) {
            if (parameterClass.isArray()) {
                Class<?> pct = parameterClass.getComponentType();
                Class mct = methodArgumentClass.getComponentType();
                if (mct.isAssignableFrom(pct)) {
                    report.score += 25;
                    return true;
                }
            }
            if (Collection.class.isAssignableFrom(parameterClass)) {
                Class mct2 = methodArgumentClass.getComponentType();
                if (mct2 == Object.class) {
                    report.conversionNeeded[index] = true;
                    report.score += 30;
                    return true;
                }
                return false;
            }
        } else if (Collection.class.isAssignableFrom(methodArgumentClass)) {
            if (parameterClass.isArray()) {
                report.conversionNeeded[index] = true;
                report.score += 50;
                return true;
            }
            if (Collection.class.isAssignableFrom(parameterClass)) {
                if (methodArgumentClass.isAssignableFrom(parameterClass)) {
                    report.score += 2;
                    return true;
                }
                report.conversionNeeded[index] = true;
                report.score += 50;
                return true;
            }
        }
        if (methodArgumentClass.isAssignableFrom(parameterClass)) {
            report.score += 40;
            return true;
        }
        if (parameterClass.isPrimitive()) {
            Class ptc = (Class) PRIMITIVE_WRAPPER_CLASSES.get(parameterClass);
            if (methodArgumentClass == ptc) {
                report.score += 2;
                return true;
            }
            if (methodArgumentClass.isAssignableFrom(ptc)) {
                report.score += 10;
                return true;
            }
            return false;
        }
        return false;
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/OgnlRuntime$ArgsCompatbilityReport.class */
    public static class ArgsCompatbilityReport {
        int score;
        boolean[] conversionNeeded;

        public ArgsCompatbilityReport(int score, boolean[] conversionNeeded) {
            this.score = score;
            this.conversionNeeded = conversionNeeded;
        }
    }

    public static boolean areArgsCompatible(Object[] args, Class[] classes) {
        ArgsCompatbilityReport report = areArgsCompatible(getArgClasses(args), classes, null);
        if (report == null) {
            return false;
        }
        boolean[] arr$ = report.conversionNeeded;
        for (boolean conversionNeeded : arr$) {
            if (conversionNeeded) {
                return false;
            }
        }
        return true;
    }

    public static ArgsCompatbilityReport areArgsCompatible(Class[] args, Class[] classes, Method m) {
        boolean varArgs = m != null && isJdk15() && m.isVarArgs();
        if (args == null || args.length == 0) {
            if (classes == null || classes.length == 0) {
                return NoArgsReport;
            }
            return null;
        }
        if (args.length != classes.length && !varArgs) {
            return null;
        }
        if (varArgs) {
            ArgsCompatbilityReport report = new ArgsCompatbilityReport(1000, new boolean[args.length]);
            if (classes.length - 1 > args.length) {
                return null;
            }
            int count = classes.length - 1;
            for (int index = 0; index < count; index++) {
                if (!isTypeCompatible(args[index], classes[index], index, report)) {
                    return null;
                }
            }
            Class varArgsType = classes[classes.length - 1].getComponentType();
            int count2 = args.length;
            for (int index2 = classes.length - 1; index2 < count2; index2++) {
                if (!isTypeCompatible(args[index2], varArgsType, index2, report)) {
                    return null;
                }
            }
            return report;
        }
        ArgsCompatbilityReport report2 = new ArgsCompatbilityReport(0, new boolean[args.length]);
        int count3 = args.length;
        for (int index3 = 0; index3 < count3; index3++) {
            if (!isTypeCompatible(args[index3], classes[index3], index3, report2)) {
                return null;
            }
        }
        return report2;
    }

    public static final boolean isMoreSpecific(Class[] classes1, Class[] classes2) {
        int count = classes1.length;
        for (int index = 0; index < count; index++) {
            Class c1 = classes1[index];
            Class c2 = classes2[index];
            if (c1 != c2) {
                if (c1.isPrimitive()) {
                    return true;
                }
                if (c1.isAssignableFrom(c2)) {
                    return false;
                }
                if (c2.isAssignableFrom(c1)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String getModifierString(int modifiers) {
        String result;
        if (Modifier.isPublic(modifiers)) {
            result = "public";
        } else if (Modifier.isProtected(modifiers)) {
            result = "protected";
        } else if (Modifier.isPrivate(modifiers)) {
            result = "private";
        } else {
            result = "";
        }
        if (Modifier.isStatic(modifiers)) {
            result = "static " + result;
        }
        if (Modifier.isFinal(modifiers)) {
            result = "final " + result;
        }
        if (Modifier.isNative(modifiers)) {
            result = "native " + result;
        }
        if (Modifier.isSynchronized(modifiers)) {
            result = "synchronized " + result;
        }
        if (Modifier.isTransient(modifiers)) {
            result = "transient " + result;
        }
        return result;
    }

    /* JADX WARN: Removed duplicated region for block: B:8:0x001e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.Class classForName(org.apache.ibatis.ognl.OgnlContext r5, java.lang.String r6) throws java.lang.ClassNotFoundException {
        /*
            java.util.Map r0 = org.apache.ibatis.ognl.OgnlRuntime._primitiveTypes
            r1 = r6
            java.lang.Object r0 = r0.get(r1)
            java.lang.Class r0 = (java.lang.Class) r0
            r7 = r0
            r0 = r7
            if (r0 != 0) goto L2b
            r0 = r5
            if (r0 == 0) goto L1e
            r0 = r5
            org.apache.ibatis.ognl.ClassResolver r0 = r0.getClassResolver()
            r1 = r0
            r8 = r1
            if (r0 != 0) goto L22
        L1e:
            org.apache.ibatis.ognl.ClassResolver r0 = org.apache.ibatis.ognl.OgnlContext.DEFAULT_CLASS_RESOLVER
            r8 = r0
        L22:
            r0 = r8
            r1 = r6
            r2 = r5
            java.lang.Class r0 = r0.classForName(r1, r2)
            r7 = r0
        L2b:
            r0 = r7
            if (r0 != 0) goto L4b
            java.lang.ClassNotFoundException r0 = new java.lang.ClassNotFoundException
            r1 = r0
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r3 = r2
            r3.<init>()
            java.lang.String r3 = "Unable to resolve class: "
            java.lang.StringBuilder r2 = r2.append(r3)
            r3 = r6
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2)
            throw r0
        L4b:
            r0 = r7
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.ibatis.ognl.OgnlRuntime.classForName(org.apache.ibatis.ognl.OgnlContext, java.lang.String):java.lang.Class");
    }

    public static boolean isInstance(OgnlContext context, Object value, String className) throws OgnlException {
        try {
            Class c = classForName(context, className);
            return c.isInstance(value);
        } catch (ClassNotFoundException e) {
            throw new OgnlException("No such class: " + className, e);
        }
    }

    public static Object getPrimitiveDefaultValue(Class forClass) {
        return _primitiveDefaults.get(forClass);
    }

    public static Object getNumericDefaultValue(Class forClass) {
        return NUMERIC_DEFAULTS.get(forClass);
    }

    public static Object getConvertedType(OgnlContext context, Object target, Member member, String propertyName, Object value, Class type) {
        return context.getTypeConverter().convertValue(context, target, member, propertyName, value, type);
    }

    public static boolean getConvertedTypes(OgnlContext context, Object target, Member member, String propertyName, Class[] parameterTypes, Object[] args, Object[] newArgs) {
        boolean result = false;
        if (parameterTypes.length == args.length) {
            result = true;
            int ilast = parameterTypes.length - 1;
            for (int i = 0; result && i <= ilast; i++) {
                Object arg = args[i];
                Class type = parameterTypes[i];
                if (isTypeCompatible(arg, type)) {
                    newArgs[i] = arg;
                } else {
                    Object v = getConvertedType(context, target, member, propertyName, arg, type);
                    if (v == NoConversionPossible) {
                        result = false;
                    } else {
                        newArgs[i] = v;
                    }
                }
            }
        }
        return result;
    }

    public static Constructor getConvertedConstructorAndArgs(OgnlContext context, Object target, List constructors, Object[] args, Object[] newArgs) {
        Constructor result = null;
        TypeConverter converter = context.getTypeConverter();
        if (converter != null && constructors != null) {
            int icount = constructors.size();
            for (int i = 0; result == null && i < icount; i++) {
                Constructor ctor = (Constructor) constructors.get(i);
                Class[] parameterTypes = getParameterTypes(ctor);
                if (getConvertedTypes(context, target, ctor, null, parameterTypes, args, newArgs)) {
                    result = ctor;
                }
            }
        }
        return result;
    }

    public static Method getAppropriateMethod(OgnlContext context, Object source, Object target, String propertyName, String methodName, List methods, Object[] args, Object[] actualArgs) {
        Method result = null;
        if (methods != null) {
            Class typeClass = target != null ? target.getClass() : null;
            if (typeClass == null && source != null && Class.class.isInstance(source)) {
                typeClass = (Class) source;
            }
            Class[] argClasses = getArgClasses(args);
            MatchingMethod mm = findBestMethod(methods, typeClass, methodName, argClasses);
            if (mm != null) {
                result = mm.mMethod;
                Class[] mParameterTypes = mm.mParameterTypes;
                System.arraycopy(args, 0, actualArgs, 0, args.length);
                for (int j = 0; j < mParameterTypes.length; j++) {
                    Class type = mParameterTypes[j];
                    if (mm.report.conversionNeeded[j] || (type.isPrimitive() && actualArgs[j] == null)) {
                        actualArgs[j] = getConvertedType(context, source, result, propertyName, args[j], type);
                    }
                }
            }
        }
        if (result == null) {
            result = getConvertedMethodAndArgs(context, target, propertyName, methods, args, actualArgs);
        }
        return result;
    }

    public static Method getConvertedMethodAndArgs(OgnlContext context, Object target, String propertyName, List methods, Object[] args, Object[] newArgs) {
        Method result = null;
        TypeConverter converter = context.getTypeConverter();
        if (converter != null && methods != null) {
            int icount = methods.size();
            for (int i = 0; result == null && i < icount; i++) {
                Method m = (Method) methods.get(i);
                Class[] parameterTypes = findParameterTypes(target != null ? target.getClass() : null, m);
                if (getConvertedTypes(context, target, m, propertyName, parameterTypes, args, newArgs)) {
                    result = m;
                }
            }
        }
        return result;
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/OgnlRuntime$MatchingMethod.class */
    private static class MatchingMethod {
        Method mMethod;
        int score;
        ArgsCompatbilityReport report;
        Class[] mParameterTypes;

        private MatchingMethod(Method method, int score, ArgsCompatbilityReport report, Class[] mParameterTypes) {
            this.mMethod = method;
            this.score = score;
            this.report = report;
            this.mParameterTypes = mParameterTypes;
        }
    }

    private static MatchingMethod findBestMethod(List methods, Class typeClass, String name, Class[] argClasses) {
        MatchingMethod mm = null;
        IllegalArgumentException failure = null;
        int icount = methods.size();
        for (int i = 0; i < icount; i++) {
            Method m = (Method) methods.get(i);
            Class[] mParameterTypes = findParameterTypes(typeClass, m);
            ArgsCompatbilityReport report = areArgsCompatible(argClasses, mParameterTypes, m);
            if (report != null) {
                String methodName = m.getName();
                int score = report.score;
                if (!name.equals(methodName)) {
                    if (name.equalsIgnoreCase(methodName)) {
                        score += 200;
                    } else if (methodName.toLowerCase().endsWith(name.toLowerCase())) {
                        score += 500;
                    } else {
                        score += 5000;
                    }
                }
                if (mm == null || mm.score > score) {
                    mm = new MatchingMethod(m, score, report, mParameterTypes);
                    failure = null;
                } else if (mm.score == score) {
                    if (Arrays.equals(mm.mMethod.getParameterTypes(), m.getParameterTypes()) && mm.mMethod.getName().equals(m.getName())) {
                        boolean retsAreEqual = mm.mMethod.getReturnType().equals(m.getReturnType());
                        if (mm.mMethod.getDeclaringClass().isAssignableFrom(m.getDeclaringClass())) {
                            if (!retsAreEqual && !mm.mMethod.getReturnType().isAssignableFrom(m.getReturnType())) {
                                System.err.println("Two methods with same method signature but return types conflict? \"" + mm.mMethod + "\" and \"" + m + "\" please report!");
                            }
                            mm = new MatchingMethod(m, score, report, mParameterTypes);
                            failure = null;
                        } else if (!m.getDeclaringClass().isAssignableFrom(mm.mMethod.getDeclaringClass())) {
                            System.err.println("Two methods with same method signature but not providing classes assignable? \"" + mm.mMethod + "\" and \"" + m + "\" please report!");
                        } else if (!retsAreEqual && !m.getReturnType().isAssignableFrom(mm.mMethod.getReturnType())) {
                            System.err.println("Two methods with same method signature but return types conflict? \"" + mm.mMethod + "\" and \"" + m + "\" please report!");
                        }
                    } else if (isJdk15() && (m.isVarArgs() || mm.mMethod.isVarArgs())) {
                        if (!m.isVarArgs() || mm.mMethod.isVarArgs()) {
                            if (!m.isVarArgs() && mm.mMethod.isVarArgs()) {
                                mm = new MatchingMethod(m, score, report, mParameterTypes);
                                failure = null;
                            } else {
                                System.err.println("Two vararg methods with same score(" + score + "): \"" + mm.mMethod + "\" and \"" + m + "\" please report!");
                            }
                        }
                    } else {
                        int scoreCurr = 0;
                        int scoreOther = 0;
                        for (int j = 0; j < argClasses.length; j++) {
                            Class argClass = argClasses[j];
                            Class mcClass = mm.mParameterTypes[j];
                            Class moClass = mParameterTypes[j];
                            if (argClass == null) {
                                if (mcClass != moClass) {
                                    if (mcClass.isAssignableFrom(moClass)) {
                                        scoreOther += 1000;
                                    } else if (moClass.isAssignableFrom(moClass)) {
                                        scoreCurr += 1000;
                                    } else {
                                        failure = new IllegalArgumentException("Can't decide wich method to use: \"" + mm.mMethod + "\" or \"" + m + SymbolConstants.QUOTES_SYMBOL);
                                    }
                                }
                            } else if (mcClass != moClass) {
                                if (mcClass == argClass) {
                                    scoreOther += 100;
                                } else if (moClass == argClass) {
                                    scoreCurr += 100;
                                } else {
                                    failure = new IllegalArgumentException("Can't decide wich method to use: \"" + mm.mMethod + "\" or \"" + m + SymbolConstants.QUOTES_SYMBOL);
                                }
                            }
                        }
                        if (scoreCurr == scoreOther) {
                            if (failure == null) {
                                System.err.println("Two methods with same score(" + score + "): \"" + mm.mMethod + "\" and \"" + m + "\" please report!");
                            }
                        } else if (scoreCurr > scoreOther) {
                            mm = new MatchingMethod(m, score, report, mParameterTypes);
                            failure = null;
                        }
                    }
                }
            }
        }
        if (failure != null) {
            throw failure;
        }
        return mm;
    }

    public static Object callAppropriateMethod(OgnlContext context, Object source, Object target, String methodName, String propertyName, List methods, Object[] args) throws MethodFailedException {
        Throwable reason;
        Object[] varArgs;
        Object[] actualArgs = _objectArrayPool.create(args.length);
        try {
            try {
                try {
                    try {
                        Method method = getAppropriateMethod(context, source, target, propertyName, methodName, methods, args, actualArgs);
                        if (method == null || !isMethodAccessible(context, source, method, propertyName)) {
                            StringBuffer buffer = new StringBuffer();
                            String className = target != null ? target.getClass().getName() + "." : "";
                            int ilast = args.length - 1;
                            for (int i = 0; i <= ilast; i++) {
                                Object arg = args[i];
                                buffer.append(arg == null ? NULL_STRING : arg.getClass().getName());
                                if (i < ilast) {
                                    buffer.append(", ");
                                }
                            }
                            throw new NoSuchMethodException(className + methodName + "(" + ((Object) buffer) + ")");
                        }
                        Object[] convertedArgs = actualArgs;
                        if (isJdk15() && method.isVarArgs()) {
                            Class[] parmTypes = method.getParameterTypes();
                            int i2 = 0;
                            while (true) {
                                if (i2 >= parmTypes.length) {
                                    break;
                                }
                                if (parmTypes[i2].isArray()) {
                                    convertedArgs = new Object[i2 + 1];
                                    System.arraycopy(actualArgs, 0, convertedArgs, 0, convertedArgs.length);
                                    if (actualArgs.length > i2) {
                                        ArrayList varArgsList = new ArrayList();
                                        for (int j = i2; j < actualArgs.length; j++) {
                                            if (actualArgs[j] != null) {
                                                varArgsList.add(actualArgs[j]);
                                            }
                                        }
                                        varArgs = varArgsList.toArray();
                                    } else {
                                        varArgs = new Object[0];
                                    }
                                    convertedArgs[i2] = varArgs;
                                } else {
                                    i2++;
                                }
                            }
                        }
                        Object objInvokeMethod = invokeMethod(target, method, convertedArgs);
                        _objectArrayPool.recycle(actualArgs);
                        return objInvokeMethod;
                    } catch (NoSuchMethodException e) {
                        reason = e;
                        _objectArrayPool.recycle(actualArgs);
                        throw new MethodFailedException(source, methodName, reason);
                    }
                } catch (IllegalAccessException e2) {
                    reason = e2;
                    _objectArrayPool.recycle(actualArgs);
                    throw new MethodFailedException(source, methodName, reason);
                }
            } catch (InvocationTargetException e3) {
                reason = e3.getTargetException();
                _objectArrayPool.recycle(actualArgs);
                throw new MethodFailedException(source, methodName, reason);
            }
        } catch (Throwable th) {
            _objectArrayPool.recycle(actualArgs);
            throw th;
        }
    }

    public static Object callStaticMethod(OgnlContext context, String className, String methodName, Object[] args) throws OgnlException, ClassNotFoundException {
        try {
            Class targetClass = classForName(context, className);
            if (targetClass == null) {
                throw new ClassNotFoundException("Unable to resolve class with name " + className);
            }
            MethodAccessor ma = getMethodAccessor(targetClass);
            return ma.callStaticMethod(context, targetClass, methodName, args);
        } catch (ClassNotFoundException ex) {
            throw new MethodFailedException(className, methodName, ex);
        }
    }

    public static Object callMethod(OgnlContext context, Object target, String methodName, String propertyName, Object[] args) throws OgnlException {
        return callMethod(context, target, methodName == null ? propertyName : methodName, args);
    }

    public static Object callMethod(OgnlContext context, Object target, String methodName, Object[] args) throws OgnlException {
        if (target == null) {
            throw new NullPointerException("target is null for method " + methodName);
        }
        return getMethodAccessor(target.getClass()).callMethod(context, target, methodName, args);
    }

    public static Object callConstructor(OgnlContext context, String className, Object[] args) throws OgnlException {
        Throwable reason;
        Object[] actualArgs = args;
        try {
            try {
                Constructor ctor = null;
                Class[] ctorParameterTypes = null;
                Class target = classForName(context, className);
                List constructors = getConstructors(target);
                int icount = constructors.size();
                for (int i = 0; i < icount; i++) {
                    Constructor c = (Constructor) constructors.get(i);
                    Class[] cParameterTypes = getParameterTypes(c);
                    if (areArgsCompatible(args, cParameterTypes) && (ctor == null || isMoreSpecific(cParameterTypes, ctorParameterTypes))) {
                        ctor = c;
                        ctorParameterTypes = cParameterTypes;
                    }
                }
                if (ctor == null) {
                    actualArgs = _objectArrayPool.create(args.length);
                    Constructor convertedConstructorAndArgs = getConvertedConstructorAndArgs(context, target, constructors, args, actualArgs);
                    ctor = convertedConstructorAndArgs;
                    if (convertedConstructorAndArgs == null) {
                        throw new NoSuchMethodException();
                    }
                }
                if (!context.getMemberAccess().isAccessible(context, target, ctor, null)) {
                    throw new IllegalAccessException("access denied to " + target.getName() + "()");
                }
                Object objNewInstance = ctor.newInstance(actualArgs);
                if (actualArgs != args) {
                    _objectArrayPool.recycle(actualArgs);
                }
                return objNewInstance;
            } catch (ClassNotFoundException e) {
                reason = e;
                if (actualArgs != args) {
                    _objectArrayPool.recycle(actualArgs);
                }
                throw new MethodFailedException(className, "new", reason);
            } catch (IllegalAccessException e2) {
                reason = e2;
                if (actualArgs != args) {
                    _objectArrayPool.recycle(actualArgs);
                }
                throw new MethodFailedException(className, "new", reason);
            } catch (InstantiationException e3) {
                reason = e3;
                if (actualArgs != args) {
                    _objectArrayPool.recycle(actualArgs);
                }
                throw new MethodFailedException(className, "new", reason);
            } catch (NoSuchMethodException e4) {
                reason = e4;
                if (actualArgs != args) {
                    _objectArrayPool.recycle(actualArgs);
                }
                throw new MethodFailedException(className, "new", reason);
            } catch (InvocationTargetException e5) {
                reason = e5.getTargetException();
                if (actualArgs != args) {
                    _objectArrayPool.recycle(actualArgs);
                }
                throw new MethodFailedException(className, "new", reason);
            }
        } catch (Throwable th) {
            if (actualArgs != args) {
                _objectArrayPool.recycle(actualArgs);
            }
            throw th;
        }
    }

    @Deprecated
    public static final Object getMethodValue(OgnlContext context, Object target, String propertyName) throws IllegalAccessException, OgnlException, NoSuchMethodException, IntrospectionException {
        return getMethodValue(context, target, propertyName, false);
    }

    public static final Object getMethodValue(OgnlContext context, Object target, String propertyName, boolean checkAccessAndExistence) throws IllegalAccessException, OgnlException, NoSuchMethodException, IllegalArgumentException, IntrospectionException {
        Object result = null;
        Method m = getGetMethod(context, target == null ? null : target.getClass(), propertyName);
        if (m == null) {
            m = getReadMethod(target == null ? null : target.getClass(), propertyName, null);
        }
        if (checkAccessAndExistence && (m == null || !context.getMemberAccess().isAccessible(context, target, m, propertyName))) {
            result = NotFound;
        }
        if (result == null) {
            if (m != null) {
                try {
                    result = invokeMethod(target, m, NoArguments);
                } catch (InvocationTargetException ex) {
                    throw new OgnlException(propertyName, ex.getTargetException());
                }
            } else {
                throw new NoSuchMethodException(propertyName);
            }
        }
        return result;
    }

    @Deprecated
    public static boolean setMethodValue(OgnlContext context, Object target, String propertyName, Object value) throws IllegalAccessException, OgnlException, NoSuchMethodException, IntrospectionException {
        return setMethodValue(context, target, propertyName, value, false);
    }

    public static boolean setMethodValue(OgnlContext context, Object target, String propertyName, Object value, boolean checkAccessAndExistence) throws IllegalAccessException, OgnlException, NoSuchMethodException, IntrospectionException {
        boolean result = true;
        Method m = getSetMethod(context, target == null ? null : target.getClass(), propertyName);
        if (checkAccessAndExistence && (m == null || !context.getMemberAccess().isAccessible(context, target, m, propertyName))) {
            result = false;
        }
        if (result) {
            if (m != null) {
                Object[] args = _objectArrayPool.create(value);
                try {
                    callAppropriateMethod(context, target, target, m.getName(), propertyName, Collections.nCopies(1, m), args);
                    _objectArrayPool.recycle(args);
                } catch (Throwable th) {
                    _objectArrayPool.recycle(args);
                    throw th;
                }
            } else {
                result = false;
            }
        }
        return result;
    }

    public static List getConstructors(Class targetClass) {
        List list = (List) _constructorCache.get(targetClass);
        List result = list;
        if (list == null) {
            synchronized (_constructorCache) {
                List list2 = (List) _constructorCache.get(targetClass);
                result = list2;
                if (list2 == null) {
                    ClassCache classCache = _constructorCache;
                    List listAsList = Arrays.asList(targetClass.getConstructors());
                    result = listAsList;
                    classCache.put(targetClass, listAsList);
                }
            }
        }
        return result;
    }

    public static Map getMethods(Class targetClass, boolean staticMethods) {
        ClassCache cache = staticMethods ? _staticMethodCache : _instanceMethodCache;
        Map map = (Map) cache.get(targetClass);
        Map result = map;
        if (map == null) {
            synchronized (cache) {
                Map map2 = (Map) cache.get(targetClass);
                result = map2;
                if (map2 == null) {
                    result = new HashMap(23);
                    collectMethods(targetClass, result, staticMethods);
                    cache.put(targetClass, result);
                }
            }
        }
        return result;
    }

    private static void collectMethods(Class c, Map result, boolean staticMethods) throws SecurityException {
        Method[] ma = c.getDeclaredMethods();
        int icount = ma.length;
        for (int i = 0; i < icount; i++) {
            if (c.isInterface()) {
                if (isDefaultMethod(ma[i])) {
                    addMethodToResult(result, ma[i]);
                }
            } else if (isMethodCallable(ma[i]) && Modifier.isStatic(ma[i].getModifiers()) == staticMethods) {
                addMethodToResult(result, ma[i]);
            }
        }
        Class superclass = c.getSuperclass();
        if (superclass != null) {
            collectMethods(superclass, result, staticMethods);
        }
        Class[] arr$ = c.getInterfaces();
        for (Class iface : arr$) {
            collectMethods(iface, result, staticMethods);
        }
    }

    private static void addMethodToResult(Map result, Method method) {
        List ml = (List) result.get(method.getName());
        if (ml == null) {
            String name = method.getName();
            ArrayList arrayList = new ArrayList();
            ml = arrayList;
            result.put(name, arrayList);
        }
        ml.add(method);
    }

    private static boolean isDefaultMethod(Method method) {
        return (method.getModifiers() & 1033) == 1 && method.getDeclaringClass().isInterface();
    }

    public static Map getAllMethods(Class targetClass, boolean staticMethods) {
        ClassCache cache = staticMethods ? _staticMethodCache : _instanceMethodCache;
        Map map = (Map) cache.get(targetClass);
        Map result = map;
        if (map == null) {
            synchronized (cache) {
                Map map2 = (Map) cache.get(targetClass);
                result = map2;
                if (map2 == null) {
                    result = new HashMap(23);
                    for (Class c = targetClass; c != null; c = c.getSuperclass()) {
                        Method[] ma = c.getMethods();
                        int icount = ma.length;
                        for (int i = 0; i < icount; i++) {
                            if (isMethodCallable(ma[i]) && Modifier.isStatic(ma[i].getModifiers()) == staticMethods) {
                                List ml = (List) result.get(ma[i].getName());
                                if (ml == null) {
                                    String name = ma[i].getName();
                                    ArrayList arrayList = new ArrayList();
                                    ml = arrayList;
                                    result.put(name, arrayList);
                                }
                                ml.add(ma[i]);
                            }
                        }
                    }
                    cache.put(targetClass, result);
                }
            }
        }
        return result;
    }

    public static List getMethods(Class targetClass, String name, boolean staticMethods) {
        return (List) getMethods(targetClass, staticMethods).get(name);
    }

    public static List getAllMethods(Class targetClass, String name, boolean staticMethods) {
        return (List) getAllMethods(targetClass, staticMethods).get(name);
    }

    public static Map getFields(Class targetClass) {
        Map map = (Map) _fieldCache.get(targetClass);
        Map result = map;
        if (map == null) {
            synchronized (_fieldCache) {
                Map map2 = (Map) _fieldCache.get(targetClass);
                result = map2;
                if (map2 == null) {
                    result = new HashMap(23);
                    Field[] fa = targetClass.getDeclaredFields();
                    for (int i = 0; i < fa.length; i++) {
                        result.put(fa[i].getName(), fa[i]);
                    }
                    _fieldCache.put(targetClass, result);
                }
            }
        }
        return result;
    }

    public static Field getField(Class inClass, String name) {
        Object o;
        Field result = null;
        Object o2 = getFields(inClass).get(name);
        if (o2 == null) {
            synchronized (_fieldCache) {
                Object o3 = getFields(inClass).get(name);
                if (o3 == null) {
                    _superclasses.clear();
                    for (Class sc = inClass; sc != null && (o = getFields(sc).get(name)) != NotFound; sc = sc.getSuperclass()) {
                        _superclasses.add(sc);
                        Field field = (Field) o;
                        result = field;
                        if (field != null) {
                            break;
                        }
                    }
                    int icount = _superclasses.size();
                    for (int i = 0; i < icount; i++) {
                        getFields((Class) _superclasses.get(i)).put(name, result == null ? NotFound : result);
                    }
                } else if (o3 instanceof Field) {
                    result = (Field) o3;
                } else if (null == NotFound) {
                    result = null;
                }
            }
        } else if (o2 instanceof Field) {
            result = (Field) o2;
        } else if (null == NotFound) {
            result = null;
        }
        return result;
    }

    @Deprecated
    public static Object getFieldValue(OgnlContext context, Object target, String propertyName) throws NoSuchFieldException {
        return getFieldValue(context, target, propertyName, false);
    }

    public static Object getFieldValue(OgnlContext context, Object target, String propertyName, boolean checkAccessAndExistence) throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException {
        Object result = null;
        Field f = getField(target == null ? null : target.getClass(), propertyName);
        if (checkAccessAndExistence && (f == null || !context.getMemberAccess().isAccessible(context, target, f, propertyName))) {
            result = NotFound;
        }
        if (result == null) {
            if (f == null) {
                throw new NoSuchFieldException(propertyName);
            }
            try {
                if (!Modifier.isStatic(f.getModifiers())) {
                    Object state = context.getMemberAccess().setup(context, target, f, propertyName);
                    result = f.get(target);
                    context.getMemberAccess().restore(context, target, f, propertyName, state);
                } else {
                    throw new NoSuchFieldException(propertyName);
                }
            } catch (IllegalAccessException e) {
                throw new NoSuchFieldException(propertyName);
            }
        }
        return result;
    }

    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0054 A[Catch: all -> 0x0071, IllegalAccessException -> 0x0089, PHI: r10
  0x0054: PHI (r10v1 'value' java.lang.Object) = (r10v0 'value' java.lang.Object), (r10v2 'value' java.lang.Object) binds: [B:13:0x003e, B:15:0x0051] A[DONT_GENERATE, DONT_INLINE], TryCatch #0 {all -> 0x0071, blocks: (B:12:0x0035, B:14:0x0041, B:16:0x0054), top: B:29:0x0035, outer: #1 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean setFieldValue(org.apache.ibatis.ognl.OgnlContext r7, java.lang.Object r8, java.lang.String r9, java.lang.Object r10) throws org.apache.ibatis.ognl.OgnlException {
        /*
            r0 = 0
            r11 = r0
            r0 = r8
            if (r0 != 0) goto Lb
            r0 = 0
            goto Lf
        Lb:
            r0 = r8
            java.lang.Class r0 = r0.getClass()     // Catch: java.lang.IllegalAccessException -> L89
        Lf:
            r1 = r9
            java.lang.reflect.Field r0 = getField(r0, r1)     // Catch: java.lang.IllegalAccessException -> L89
            r12 = r0
            r0 = r12
            if (r0 == 0) goto L86
            r0 = r12
            int r0 = r0.getModifiers()     // Catch: java.lang.IllegalAccessException -> L89
            boolean r0 = java.lang.reflect.Modifier.isStatic(r0)     // Catch: java.lang.IllegalAccessException -> L89
            if (r0 != 0) goto L86
            r0 = r7
            org.apache.ibatis.ognl.MemberAccess r0 = r0.getMemberAccess()     // Catch: java.lang.IllegalAccessException -> L89
            r1 = r7
            r2 = r8
            r3 = r12
            r4 = r9
            java.lang.Object r0 = r0.setup(r1, r2, r3, r4)     // Catch: java.lang.IllegalAccessException -> L89
            r13 = r0
            r0 = r10
            r1 = r12
            java.lang.Class r1 = r1.getType()     // Catch: java.lang.Throwable -> L71 java.lang.IllegalAccessException -> L89
            boolean r0 = isTypeCompatible(r0, r1)     // Catch: java.lang.Throwable -> L71 java.lang.IllegalAccessException -> L89
            if (r0 != 0) goto L54
            r0 = r7
            r1 = r8
            r2 = r12
            r3 = r9
            r4 = r10
            r5 = r12
            java.lang.Class r5 = r5.getType()     // Catch: java.lang.Throwable -> L71 java.lang.IllegalAccessException -> L89
            java.lang.Object r0 = getConvertedType(r0, r1, r2, r3, r4, r5)     // Catch: java.lang.Throwable -> L71 java.lang.IllegalAccessException -> L89
            r1 = r0
            r10 = r1
            if (r0 == 0) goto L5e
        L54:
            r0 = r12
            r1 = r8
            r2 = r10
            r0.set(r1, r2)     // Catch: java.lang.Throwable -> L71 java.lang.IllegalAccessException -> L89
            r0 = 1
            r11 = r0
        L5e:
            r0 = r7
            org.apache.ibatis.ognl.MemberAccess r0 = r0.getMemberAccess()     // Catch: java.lang.IllegalAccessException -> L89
            r1 = r7
            r2 = r8
            r3 = r12
            r4 = r9
            r5 = r13
            r0.restore(r1, r2, r3, r4, r5)     // Catch: java.lang.IllegalAccessException -> L89
            goto L86
        L71:
            r14 = move-exception
            r0 = r7
            org.apache.ibatis.ognl.MemberAccess r0 = r0.getMemberAccess()     // Catch: java.lang.IllegalAccessException -> L89
            r1 = r7
            r2 = r8
            r3 = r12
            r4 = r9
            r5 = r13
            r0.restore(r1, r2, r3, r4, r5)     // Catch: java.lang.IllegalAccessException -> L89
            r0 = r14
            throw r0     // Catch: java.lang.IllegalAccessException -> L89
        L86:
            goto L97
        L89:
            r12 = move-exception
            org.apache.ibatis.ognl.NoSuchPropertyException r0 = new org.apache.ibatis.ognl.NoSuchPropertyException
            r1 = r0
            r2 = r8
            r3 = r9
            r4 = r12
            r1.<init>(r2, r3, r4)
            throw r0
        L97:
            r0 = r11
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.ibatis.ognl.OgnlRuntime.setFieldValue(org.apache.ibatis.ognl.OgnlContext, java.lang.Object, java.lang.String, java.lang.Object):boolean");
    }

    public static boolean isFieldAccessible(OgnlContext context, Object target, Class inClass, String propertyName) {
        return isFieldAccessible(context, target, getField(inClass, propertyName), propertyName);
    }

    public static boolean isFieldAccessible(OgnlContext context, Object target, Field field, String propertyName) {
        return context.getMemberAccess().isAccessible(context, target, field, propertyName);
    }

    public static boolean hasField(OgnlContext context, Object target, Class inClass, String propertyName) {
        Field f = getField(inClass, propertyName);
        return f != null && isFieldAccessible(context, target, f, propertyName);
    }

    public static Object getStaticField(OgnlContext context, String className, String fieldName) throws NoSuchFieldException, OgnlException {
        Exception reason;
        try {
            Class c = classForName(context, className);
            if (c == null) {
                throw new OgnlException("Unable to find class " + className + " when resolving field name of " + fieldName);
            }
            if (fieldName.equals("class")) {
                return c;
            }
            if (isJdk15() && c.isEnum()) {
                try {
                    return Enum.valueOf(c, fieldName);
                } catch (IllegalArgumentException e) {
                }
            }
            Field f = c.getField(fieldName);
            if (!Modifier.isStatic(f.getModifiers())) {
                throw new OgnlException("Field " + fieldName + " of class " + className + " is not static");
            }
            return f.get(null);
        } catch (ClassNotFoundException e2) {
            reason = e2;
            throw new OgnlException("Could not get static field " + fieldName + " from class " + className, reason);
        } catch (IllegalAccessException e3) {
            reason = e3;
            throw new OgnlException("Could not get static field " + fieldName + " from class " + className, reason);
        } catch (NoSuchFieldException e4) {
            reason = e4;
            throw new OgnlException("Could not get static field " + fieldName + " from class " + className, reason);
        } catch (SecurityException e5) {
            reason = e5;
            throw new OgnlException("Could not get static field " + fieldName + " from class " + className, reason);
        }
    }

    private static String capitalizeBeanPropertyName(String propertyName) {
        if (propertyName.length() == 1) {
            return propertyName.toUpperCase();
        }
        if (propertyName.startsWith("get") && propertyName.endsWith("()") && Character.isUpperCase(propertyName.substring(3, 4).charAt(0))) {
            return propertyName;
        }
        if (propertyName.startsWith("set") && propertyName.endsWith(")") && Character.isUpperCase(propertyName.substring(3, 4).charAt(0))) {
            return propertyName;
        }
        if (propertyName.startsWith("is") && propertyName.endsWith("()") && Character.isUpperCase(propertyName.substring(2, 3).charAt(0))) {
            return propertyName;
        }
        char first = propertyName.charAt(0);
        char second = propertyName.charAt(1);
        if (Character.isLowerCase(first) && Character.isUpperCase(second)) {
            return propertyName;
        }
        char[] chars = propertyName.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0033 A[PHI: r9
  0x0033: PHI (r9v1 'result' java.util.List) = (r9v0 'result' java.util.List), (r9v11 'result' java.util.List) binds: [B:7:0x0020, B:9:0x0030] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00c7 A[PHI: r19
  0x00c7: PHI (r19v1 'isIs' boolean) = (r19v0 'isIs' boolean), (r19v0 'isIs' boolean), (r19v2 'isIs' boolean) binds: [B:28:0x00ad, B:30:0x00b7, B:32:0x00c4] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.util.List getDeclaredMethods(java.lang.Class r6, java.lang.String r7, boolean r8) {
        /*
            Method dump skipped, instructions count: 346
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.ibatis.ognl.OgnlRuntime.getDeclaredMethods(java.lang.Class, java.lang.String, boolean):java.util.List");
    }

    static boolean isMethodCallable(Method m) {
        if ((isJdk15() && m.isSynthetic()) || Modifier.isVolatile(m.getModifiers())) {
            return false;
        }
        return true;
    }

    public static Method getGetMethod(OgnlContext context, Class targetClass, String propertyName) throws OgnlException, IntrospectionException {
        Method method = cacheGetMethod.get(targetClass, propertyName);
        if (method != null) {
            return method;
        }
        if (cacheGetMethod.containsKey(targetClass, propertyName)) {
            return null;
        }
        Method method2 = _getGetMethod(context, targetClass, propertyName);
        cacheGetMethod.put(targetClass, propertyName, method2);
        return method2;
    }

    private static Method _getGetMethod(OgnlContext context, Class targetClass, String propertyName) throws OgnlException, IntrospectionException {
        Method result = null;
        List methods = getDeclaredMethods(targetClass, propertyName, false);
        if (methods != null) {
            int i = 0;
            int icount = methods.size();
            while (true) {
                if (i >= icount) {
                    break;
                }
                Method m = (Method) methods.get(i);
                Class[] mParameterTypes = findParameterTypes(targetClass, m);
                if (mParameterTypes.length != 0) {
                    i++;
                } else {
                    result = m;
                    break;
                }
            }
        }
        return result;
    }

    public static boolean isMethodAccessible(OgnlContext context, Object target, Method method, String propertyName) {
        return method != null && context.getMemberAccess().isAccessible(context, target, method, propertyName);
    }

    public static boolean hasGetMethod(OgnlContext context, Object target, Class targetClass, String propertyName) throws OgnlException, IntrospectionException {
        return isMethodAccessible(context, target, getGetMethod(context, targetClass, propertyName), propertyName);
    }

    public static Method getSetMethod(OgnlContext context, Class targetClass, String propertyName) throws OgnlException, IntrospectionException {
        Method method = cacheSetMethod.get(targetClass, propertyName);
        if (method != null) {
            return method;
        }
        if (cacheSetMethod.containsKey(targetClass, propertyName)) {
            return null;
        }
        Method method2 = _getSetMethod(context, targetClass, propertyName);
        cacheSetMethod.put(targetClass, propertyName, method2);
        return method2;
    }

    private static Method _getSetMethod(OgnlContext context, Class targetClass, String propertyName) throws OgnlException, IntrospectionException {
        Method result = null;
        List methods = getDeclaredMethods(targetClass, propertyName, true);
        if (methods != null) {
            int i = 0;
            int icount = methods.size();
            while (true) {
                if (i >= icount) {
                    break;
                }
                Method m = (Method) methods.get(i);
                Class[] mParameterTypes = findParameterTypes(targetClass, m);
                if (mParameterTypes.length != 1) {
                    i++;
                } else {
                    result = m;
                    break;
                }
            }
        }
        return result;
    }

    public static final boolean hasSetMethod(OgnlContext context, Object target, Class targetClass, String propertyName) throws OgnlException, IntrospectionException {
        return isMethodAccessible(context, target, getSetMethod(context, targetClass, propertyName), propertyName);
    }

    public static final boolean hasGetProperty(OgnlContext context, Object target, Object oname) throws OgnlException, IntrospectionException {
        Class targetClass = target == null ? null : target.getClass();
        String name = oname.toString();
        return hasGetMethod(context, target, targetClass, name) || hasField(context, target, targetClass, name);
    }

    public static final boolean hasSetProperty(OgnlContext context, Object target, Object oname) throws OgnlException, IntrospectionException {
        Class targetClass = target == null ? null : target.getClass();
        String name = oname.toString();
        return hasSetMethod(context, target, targetClass, name) || hasField(context, target, targetClass, name);
    }

    private static final boolean indexMethodCheck(List methods) {
        boolean result = false;
        if (methods.size() > 0) {
            Method fm = (Method) methods.get(0);
            Class[] fmpt = getParameterTypes(fm);
            int fmpc = fmpt.length;
            Class lastMethodClass = fm.getDeclaringClass();
            result = true;
            for (int i = 1; result && i < methods.size(); i++) {
                Method m = (Method) methods.get(i);
                Class c = m.getDeclaringClass();
                if (lastMethodClass == c) {
                    result = false;
                } else {
                    Class[] mpt = getParameterTypes(fm);
                    int mpc = fmpt.length;
                    if (fmpc != mpc) {
                        result = false;
                    }
                    int j = 0;
                    while (true) {
                        if (j >= fmpc) {
                            break;
                        }
                        if (fmpt[j] == mpt[j]) {
                            j++;
                        } else {
                            result = false;
                            break;
                        }
                    }
                }
                lastMethodClass = c;
            }
        }
        return result;
    }

    static void findObjectIndexedPropertyDescriptors(Class targetClass, Map intoMap) throws OgnlException {
        Map allMethods = getMethods(targetClass, false);
        Map pairs = new HashMap(101);
        for (String methodName : allMethods.keySet()) {
            List methods = (List) allMethods.get(methodName);
            if (indexMethodCheck(methods)) {
                boolean isGet = false;
                Method m = (Method) methods.get(0);
                boolean isSet = methodName.startsWith("set");
                if (!isSet) {
                    boolean zStartsWith = methodName.startsWith("get");
                    isGet = zStartsWith;
                    if (zStartsWith) {
                    }
                }
                if (methodName.length() > 3) {
                    String propertyName = Introspector.decapitalize(methodName.substring(3));
                    Class[] parameterTypes = getParameterTypes(m);
                    int parameterCount = parameterTypes.length;
                    if (isGet && parameterCount == 1 && m.getReturnType() != Void.TYPE) {
                        List pair = (List) pairs.get(propertyName);
                        if (pair == null) {
                            ArrayList arrayList = new ArrayList();
                            pair = arrayList;
                            pairs.put(propertyName, arrayList);
                        }
                        pair.add(m);
                    }
                    if (isSet && parameterCount == 2 && m.getReturnType() == Void.TYPE) {
                        List pair2 = (List) pairs.get(propertyName);
                        if (pair2 == null) {
                            ArrayList arrayList2 = new ArrayList();
                            pair2 = arrayList2;
                            pairs.put(propertyName, arrayList2);
                        }
                        pair2.add(m);
                    }
                }
            }
        }
        for (String propertyName2 : pairs.keySet()) {
            List methods2 = (List) pairs.get(propertyName2);
            if (methods2.size() == 2) {
                Method method1 = (Method) methods2.get(0);
                Method method2 = (Method) methods2.get(1);
                Method setMethod = method1.getParameterTypes().length == 2 ? method1 : method2;
                Method getMethod = setMethod == method1 ? method2 : method1;
                Class keyType = getMethod.getParameterTypes()[0];
                Class propertyType = getMethod.getReturnType();
                if (keyType == setMethod.getParameterTypes()[0] && propertyType == setMethod.getParameterTypes()[1]) {
                    try {
                        ObjectIndexedPropertyDescriptor propertyDescriptor = new ObjectIndexedPropertyDescriptor(propertyName2, propertyType, getMethod, setMethod);
                        intoMap.put(propertyName2, propertyDescriptor);
                    } catch (Exception ex) {
                        throw new OgnlException("creating object indexed property descriptor for '" + propertyName2 + "' in " + targetClass, ex);
                    }
                }
            }
        }
    }

    public static Map getPropertyDescriptors(Class targetClass) throws OgnlException, IntrospectionException {
        Map map = (Map) _propertyDescriptorCache.get(targetClass);
        Map result = map;
        if (map == null) {
            synchronized (_propertyDescriptorCache) {
                Map map2 = (Map) _propertyDescriptorCache.get(targetClass);
                result = map2;
                if (map2 == null) {
                    PropertyDescriptor[] pda = Introspector.getBeanInfo(targetClass).getPropertyDescriptors();
                    result = new HashMap(101);
                    int icount = pda.length;
                    for (int i = 0; i < icount; i++) {
                        if (pda[i].getReadMethod() != null && !isMethodCallable(pda[i].getReadMethod())) {
                            pda[i].setReadMethod(findClosestMatchingMethod(targetClass, pda[i].getReadMethod(), pda[i].getName(), pda[i].getPropertyType(), true));
                        }
                        if (pda[i].getWriteMethod() != null && !isMethodCallable(pda[i].getWriteMethod())) {
                            pda[i].setWriteMethod(findClosestMatchingMethod(targetClass, pda[i].getWriteMethod(), pda[i].getName(), pda[i].getPropertyType(), false));
                        }
                        result.put(pda[i].getName(), pda[i]);
                    }
                    findObjectIndexedPropertyDescriptors(targetClass, result);
                    _propertyDescriptorCache.put(targetClass, result);
                }
            }
        }
        return result;
    }

    public static PropertyDescriptor getPropertyDescriptor(Class targetClass, String propertyName) throws OgnlException, IntrospectionException {
        if (targetClass == null) {
            return null;
        }
        return (PropertyDescriptor) getPropertyDescriptors(targetClass).get(propertyName);
    }

    static Method findClosestMatchingMethod(Class targetClass, Method m, String propertyName, Class propertyType, boolean isReadMethod) {
        List methods = getDeclaredMethods(targetClass, propertyName, !isReadMethod);
        if (methods != null) {
            for (Object method1 : methods) {
                Method method = (Method) method1;
                if (method.getName().equals(m.getName()) && m.getReturnType().isAssignableFrom(m.getReturnType()) && method.getReturnType() == propertyType && method.getParameterTypes().length == m.getParameterTypes().length) {
                    return method;
                }
            }
        }
        return m;
    }

    public static PropertyDescriptor[] getPropertyDescriptorsArray(Class targetClass) throws IntrospectionException {
        PropertyDescriptor[] result = null;
        if (targetClass != null) {
            PropertyDescriptor[] propertyDescriptorArr = (PropertyDescriptor[]) _propertyDescriptorCache.get(targetClass);
            result = propertyDescriptorArr;
            if (propertyDescriptorArr == null) {
                synchronized (_propertyDescriptorCache) {
                    PropertyDescriptor[] propertyDescriptorArr2 = (PropertyDescriptor[]) _propertyDescriptorCache.get(targetClass);
                    result = propertyDescriptorArr2;
                    if (propertyDescriptorArr2 == null) {
                        ClassCache classCache = _propertyDescriptorCache;
                        PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(targetClass).getPropertyDescriptors();
                        result = propertyDescriptors;
                        classCache.put(targetClass, propertyDescriptors);
                    }
                }
            }
        }
        return result;
    }

    public static PropertyDescriptor getPropertyDescriptorFromArray(Class targetClass, String name) throws IntrospectionException {
        PropertyDescriptor result = null;
        PropertyDescriptor[] pda = getPropertyDescriptorsArray(targetClass);
        int icount = pda.length;
        for (int i = 0; result == null && i < icount; i++) {
            if (pda[i].getName().compareTo(name) == 0) {
                result = pda[i];
            }
        }
        return result;
    }

    public static void setMethodAccessor(Class cls, MethodAccessor accessor) {
        synchronized (_methodAccessors) {
            _methodAccessors.put(cls, accessor);
        }
    }

    public static MethodAccessor getMethodAccessor(Class cls) throws OgnlException {
        MethodAccessor answer = (MethodAccessor) getHandler(cls, _methodAccessors);
        if (answer != null) {
            return answer;
        }
        throw new OgnlException("No method accessor for " + cls);
    }

    public static void setPropertyAccessor(Class cls, PropertyAccessor accessor) {
        synchronized (_propertyAccessors) {
            _propertyAccessors.put(cls, accessor);
        }
    }

    public static PropertyAccessor getPropertyAccessor(Class cls) throws OgnlException {
        PropertyAccessor answer = (PropertyAccessor) getHandler(cls, _propertyAccessors);
        if (answer != null) {
            return answer;
        }
        throw new OgnlException("No property accessor for class " + cls);
    }

    public static ElementsAccessor getElementsAccessor(Class cls) throws OgnlException {
        ElementsAccessor answer = (ElementsAccessor) getHandler(cls, _elementsAccessors);
        if (answer != null) {
            return answer;
        }
        throw new OgnlException("No elements accessor for class " + cls);
    }

    public static void setElementsAccessor(Class cls, ElementsAccessor accessor) {
        synchronized (_elementsAccessors) {
            _elementsAccessors.put(cls, accessor);
        }
    }

    public static NullHandler getNullHandler(Class cls) throws OgnlException {
        NullHandler answer = (NullHandler) getHandler(cls, _nullHandlers);
        if (answer != null) {
            return answer;
        }
        throw new OgnlException("No null handler for class " + cls);
    }

    public static void setNullHandler(Class cls, NullHandler handler) {
        synchronized (_nullHandlers) {
            _nullHandlers.put(cls, handler);
        }
    }

    private static Object getHandler(Class forClass, ClassCache handlers) {
        Class keyFound;
        Object obj = handlers.get(forClass);
        Object answer = obj;
        if (obj == null) {
            synchronized (handlers) {
                Object obj2 = handlers.get(forClass);
                answer = obj2;
                if (obj2 == null) {
                    if (forClass.isArray()) {
                        answer = handlers.get(Object[].class);
                        keyFound = null;
                    } else {
                        keyFound = forClass;
                        Class c = forClass;
                        loop0: while (true) {
                            if (c == null) {
                                break;
                            }
                            answer = handlers.get(c);
                            if (answer == null) {
                                Class[] interfaces = c.getInterfaces();
                                for (Class iface : interfaces) {
                                    answer = handlers.get(iface);
                                    if (answer == null) {
                                        answer = getHandler(iface, handlers);
                                    }
                                    if (answer != null) {
                                        keyFound = iface;
                                        break loop0;
                                    }
                                }
                                c = c.getSuperclass();
                            } else {
                                keyFound = c;
                                break;
                            }
                        }
                    }
                    if (answer != null && keyFound != forClass) {
                        handlers.put(forClass, answer);
                    }
                }
            }
        }
        return answer;
    }

    public static Object getProperty(OgnlContext context, Object source, Object name) throws OgnlException {
        if (source == null) {
            throw new OgnlException("source is null for getProperty(null, \"" + name + "\")");
        }
        PropertyAccessor accessor = getPropertyAccessor(getTargetClass(source));
        if (accessor == null) {
            throw new OgnlException("No property accessor for " + getTargetClass(source).getName());
        }
        return accessor.getProperty(context, source, name);
    }

    public static void setProperty(OgnlContext context, Object target, Object name, Object value) throws OgnlException {
        if (target == null) {
            throw new OgnlException("target is null for setProperty(null, \"" + name + "\", " + value + ")");
        }
        PropertyAccessor accessor = getPropertyAccessor(getTargetClass(target));
        if (accessor == null) {
            throw new OgnlException("No property accessor for " + getTargetClass(target).getName());
        }
        accessor.setProperty(context, target, name, value);
    }

    public static int getIndexedPropertyType(OgnlContext context, Class sourceClass, String name) throws OgnlException, IntrospectionException {
        int result = INDEXED_PROPERTY_NONE;
        try {
            PropertyDescriptor pd = getPropertyDescriptor(sourceClass, name);
            if (pd != null) {
                if (pd instanceof IndexedPropertyDescriptor) {
                    result = INDEXED_PROPERTY_INT;
                } else if (pd instanceof ObjectIndexedPropertyDescriptor) {
                    result = INDEXED_PROPERTY_OBJECT;
                }
            }
            return result;
        } catch (Exception ex) {
            throw new OgnlException("problem determining if '" + name + "' is an indexed property", ex);
        }
    }

    public static Object getIndexedProperty(OgnlContext context, Object source, String name, Object index) throws OgnlException {
        Class<?> cls;
        Method m;
        Object[] args = _objectArrayPool.create(index);
        try {
            if (source == null) {
                cls = null;
            } else {
                try {
                    cls = source.getClass();
                } catch (OgnlException ex) {
                    throw ex;
                } catch (Exception ex2) {
                    throw new OgnlException("getting indexed property descriptor for '" + name + "'", ex2);
                }
            }
            IndexedPropertyDescriptor propertyDescriptor = getPropertyDescriptor(cls, name);
            if (propertyDescriptor instanceof IndexedPropertyDescriptor) {
                m = propertyDescriptor.getIndexedReadMethod();
            } else if (propertyDescriptor instanceof ObjectIndexedPropertyDescriptor) {
                m = ((ObjectIndexedPropertyDescriptor) propertyDescriptor).getIndexedReadMethod();
            } else {
                throw new OgnlException("property '" + name + "' is not an indexed property");
            }
            Object objCallMethod = callMethod(context, source, m.getName(), args);
            _objectArrayPool.recycle(args);
            return objCallMethod;
        } catch (Throwable th) {
            _objectArrayPool.recycle(args);
            throw th;
        }
    }

    public static void setIndexedProperty(OgnlContext context, Object source, String name, Object index, Object value) throws OgnlException {
        Class<?> cls;
        Method m;
        Object[] args = _objectArrayPool.create(index, value);
        try {
            if (source == null) {
                cls = null;
            } else {
                try {
                    cls = source.getClass();
                } catch (OgnlException ex) {
                    throw ex;
                } catch (Exception ex2) {
                    throw new OgnlException("getting indexed property descriptor for '" + name + "'", ex2);
                }
            }
            IndexedPropertyDescriptor propertyDescriptor = getPropertyDescriptor(cls, name);
            if (propertyDescriptor instanceof IndexedPropertyDescriptor) {
                m = propertyDescriptor.getIndexedWriteMethod();
            } else if (propertyDescriptor instanceof ObjectIndexedPropertyDescriptor) {
                m = ((ObjectIndexedPropertyDescriptor) propertyDescriptor).getIndexedWriteMethod();
            } else {
                throw new OgnlException("property '" + name + "' is not an indexed property");
            }
            callMethod(context, source, m.getName(), args);
            _objectArrayPool.recycle(args);
        } catch (Throwable th) {
            _objectArrayPool.recycle(args);
            throw th;
        }
    }

    public static EvaluationPool getEvaluationPool() {
        return _evaluationPool;
    }

    public static ObjectArrayPool getObjectArrayPool() {
        return _objectArrayPool;
    }

    public static void setClassCacheInspector(ClassCacheInspector inspector) {
        _cacheInspector = inspector;
        _propertyDescriptorCache.setClassInspector(_cacheInspector);
        _constructorCache.setClassInspector(_cacheInspector);
        _staticMethodCache.setClassInspector(_cacheInspector);
        _instanceMethodCache.setClassInspector(_cacheInspector);
        _invokePermissionCache.setClassInspector(_cacheInspector);
        _fieldCache.setClassInspector(_cacheInspector);
        _declaredMethods[0].setClassInspector(_cacheInspector);
        _declaredMethods[1].setClassInspector(_cacheInspector);
    }

    public static Method getMethod(OgnlContext context, Class target, String name, Node[] children, boolean includeStatic) throws Exception {
        Class[] parms;
        if (children != null && children.length > 0) {
            parms = new Class[children.length];
            Class currType = context.getCurrentType();
            Class currAccessor = context.getCurrentAccessor();
            Object cast = context.get(ExpressionCompiler.PRE_CAST);
            context.setCurrentObject(context.getRoot());
            context.setCurrentType(context.getRoot() != null ? context.getRoot().getClass() : null);
            context.setCurrentAccessor(null);
            context.setPreviousType(null);
            for (int i = 0; i < children.length; i++) {
                children[i].toGetSourceString(context, context.getRoot());
                parms[i] = context.getCurrentType();
            }
            context.put(ExpressionCompiler.PRE_CAST, cast);
            context.setCurrentType(currType);
            context.setCurrentAccessor(currAccessor);
            context.setCurrentObject(target);
        } else {
            parms = EMPTY_CLASS_ARRAY;
        }
        List methods = getMethods(target, name, includeStatic);
        if (methods == null) {
            return null;
        }
        for (int i2 = 0; i2 < methods.size(); i2++) {
            Method m = (Method) methods.get(i2);
            boolean varArgs = isJdk15() && m.isVarArgs();
            if (parms.length == m.getParameterTypes().length || varArgs) {
                Class[] mparms = m.getParameterTypes();
                boolean matched = true;
                int p = 0;
                while (true) {
                    if (p >= mparms.length) {
                        break;
                    }
                    if (!varArgs || !mparms[p].isArray()) {
                        if (parms[p] == null) {
                            matched = false;
                            break;
                        }
                        if (parms[p] != mparms[p] && (!mparms[p].isPrimitive() || Character.TYPE == mparms[p] || Byte.TYPE == mparms[p] || !Number.class.isAssignableFrom(parms[p]) || getPrimitiveWrapperClass(parms[p]) != mparms[p])) {
                            break;
                        }
                    }
                    p++;
                }
                matched = false;
                if (matched) {
                    return m;
                }
            }
        }
        return null;
    }

    public static Method getReadMethod(Class target, String name) {
        return getReadMethod(target, name, null);
    }

    public static Method getReadMethod(Class target, String name, Class[] argClasses) {
        Method ret;
        MatchingMethod mm;
        MatchingMethod mm2;
        try {
            if (name.indexOf(34) >= 0) {
                name = name.replaceAll(SymbolConstants.QUOTES_SYMBOL, "");
            }
            String name2 = name.toLowerCase();
            BeanInfo info = Introspector.getBeanInfo(target);
            MethodDescriptor[] methods = info.getMethodDescriptors();
            ArrayList<Method> candidates = new ArrayList<>();
            for (int i = 0; i < methods.length; i++) {
                if (isMethodCallable(methods[i].getMethod()) && ((methods[i].getName().equalsIgnoreCase(name2) || methods[i].getName().toLowerCase().equals("get" + name2) || methods[i].getName().toLowerCase().equals("has" + name2) || methods[i].getName().toLowerCase().equals("is" + name2)) && !methods[i].getName().startsWith("set"))) {
                    candidates.add(methods[i].getMethod());
                }
            }
            if (!candidates.isEmpty() && (mm2 = findBestMethod(candidates, target, name2, argClasses)) != null) {
                return mm2.mMethod;
            }
            for (int i2 = 0; i2 < methods.length; i2++) {
                if (isMethodCallable(methods[i2].getMethod()) && methods[i2].getName().equalsIgnoreCase(name2) && !methods[i2].getName().startsWith("set") && !methods[i2].getName().startsWith("get") && !methods[i2].getName().startsWith("is") && !methods[i2].getName().startsWith("has") && methods[i2].getMethod().getReturnType() != Void.TYPE) {
                    Method m = methods[i2].getMethod();
                    if (!candidates.contains(m)) {
                        candidates.add(m);
                    }
                }
            }
            if (!candidates.isEmpty() && (mm = findBestMethod(candidates, target, name2, argClasses)) != null) {
                return mm.mMethod;
            }
            if (!name2.startsWith("get") && (ret = getReadMethod(target, "get" + name2, argClasses)) != null) {
                return ret;
            }
            if (!candidates.isEmpty()) {
                int reqArgCount = argClasses == null ? 0 : argClasses.length;
                Iterator i$ = candidates.iterator();
                while (i$.hasNext()) {
                    Method m2 = i$.next();
                    if (m2.getParameterTypes().length == reqArgCount) {
                        return m2;
                    }
                }
            }
            return null;
        } catch (Throwable t) {
            throw OgnlOps.castToRuntime(t);
        }
    }

    public static Method getWriteMethod(Class target, String name) {
        return getWriteMethod(target, name, null);
    }

    public static Method getWriteMethod(Class target, String name, Class[] argClasses) {
        Method ret;
        MatchingMethod mm;
        MatchingMethod mm2;
        try {
            if (name.indexOf(34) >= 0) {
                name = name.replaceAll(SymbolConstants.QUOTES_SYMBOL, "");
            }
            BeanInfo info = Introspector.getBeanInfo(target);
            MethodDescriptor[] methods = info.getMethodDescriptors();
            ArrayList<Method> candidates = new ArrayList<>();
            for (int i = 0; i < methods.length; i++) {
                if (isMethodCallable(methods[i].getMethod()) && ((methods[i].getName().equalsIgnoreCase(name) || methods[i].getName().toLowerCase().equals(name.toLowerCase()) || methods[i].getName().toLowerCase().equals("set" + name.toLowerCase())) && !methods[i].getName().startsWith("get"))) {
                    candidates.add(methods[i].getMethod());
                }
            }
            if (!candidates.isEmpty() && (mm2 = findBestMethod(candidates, target, name, argClasses)) != null) {
                return mm2.mMethod;
            }
            Method[] cmethods = target.getClass().getMethods();
            for (int i2 = 0; i2 < cmethods.length; i2++) {
                if (isMethodCallable(cmethods[i2]) && ((cmethods[i2].getName().equalsIgnoreCase(name) || cmethods[i2].getName().toLowerCase().equals(name.toLowerCase()) || cmethods[i2].getName().toLowerCase().equals("set" + name.toLowerCase())) && !cmethods[i2].getName().startsWith("get"))) {
                    Method m = methods[i2].getMethod();
                    if (!candidates.contains(m)) {
                        candidates.add(m);
                    }
                }
            }
            if (!candidates.isEmpty() && (mm = findBestMethod(candidates, target, name, argClasses)) != null) {
                return mm.mMethod;
            }
            if (!name.startsWith("set") && (ret = getReadMethod(target, "set" + name, argClasses)) != null) {
                return ret;
            }
            if (!candidates.isEmpty()) {
                int reqArgCount = argClasses == null ? 0 : argClasses.length;
                Iterator i$ = candidates.iterator();
                while (i$.hasNext()) {
                    Method m2 = i$.next();
                    if (m2.getParameterTypes().length == reqArgCount) {
                        return m2;
                    }
                }
                if (argClasses == null && candidates.size() == 1) {
                    return candidates.get(0);
                }
                return null;
            }
            return null;
        } catch (Throwable t) {
            throw OgnlOps.castToRuntime(t);
        }
    }

    public static PropertyDescriptor getProperty(Class target, String name) {
        try {
            BeanInfo info = Introspector.getBeanInfo(target);
            PropertyDescriptor[] pds = info.getPropertyDescriptors();
            for (int i = 0; i < pds.length; i++) {
                if (pds[i].getName().equalsIgnoreCase(name) || pds[i].getName().toLowerCase().equals(name.toLowerCase()) || pds[i].getName().toLowerCase().endsWith(name.toLowerCase())) {
                    return pds[i];
                }
            }
            return null;
        } catch (Throwable t) {
            throw OgnlOps.castToRuntime(t);
        }
    }

    public static boolean isBoolean(String expression) {
        if (expression == null) {
            return false;
        }
        if ("true".equals(expression) || "false".equals(expression) || "!true".equals(expression) || "!false".equals(expression) || "(true)".equals(expression) || "!(true)".equals(expression) || "(false)".equals(expression) || "!(false)".equals(expression) || expression.startsWith("org.apache.ibatis.ognl.OgnlOps")) {
            return true;
        }
        return false;
    }

    public static boolean shouldConvertNumericTypes(OgnlContext context) {
        if (context.getCurrentType() == null || context.getPreviousType() == null) {
            return true;
        }
        return ((context.getCurrentType() == context.getPreviousType() && context.getCurrentType().isPrimitive() && context.getPreviousType().isPrimitive()) || context.getCurrentType() == null || context.getCurrentType().isArray() || context.getPreviousType() == null || context.getPreviousType().isArray()) ? false : true;
    }

    public static String getChildSource(OgnlContext context, Object target, Node child) throws OgnlException {
        return getChildSource(context, target, child, false);
    }

    public static String getChildSource(OgnlContext context, Object target, Node child, boolean forceConversion) throws OgnlException {
        String pre = (String) context.get("_currentChain");
        if (pre == null) {
            pre = "";
        }
        try {
            child.getValue(context, target);
        } catch (ArithmeticException e) {
            context.setCurrentType(Integer.TYPE);
            return "0";
        } catch (NullPointerException e2) {
        } catch (Throwable t) {
            throw OgnlOps.castToRuntime(t);
        }
        try {
            String source = child.toGetSourceString(context, target);
            if (!ASTConst.class.isInstance(child) && (target == null || context.getRoot() != target)) {
                source = pre + source;
            }
            if (context.getRoot() != null) {
                source = ExpressionCompiler.getRootExpression(child, context.getRoot(), context) + source;
                context.setCurrentAccessor(context.getRoot().getClass());
            }
            if (ASTChain.class.isInstance(child)) {
                String cast = (String) context.remove(ExpressionCompiler.PRE_CAST);
                if (cast == null) {
                    cast = "";
                }
                source = cast + source;
            }
            if (source == null || source.trim().length() < 1) {
                source = "null";
            }
            return source;
        } finally {
            RuntimeException runtimeExceptionCastToRuntime = OgnlOps.castToRuntime(t);
        }
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/OgnlRuntime$ClassPropertyMethodCache.class */
    private static final class ClassPropertyMethodCache {
        private static final Method NULL_REPLACEMENT;
        private final ConcurrentHashMap<Class<?>, ConcurrentHashMap<String, Method>> cache = new ConcurrentHashMap<>();

        static {
            try {
                NULL_REPLACEMENT = ClassPropertyMethodCache.class.getDeclaredMethod("get", Class.class, String.class);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        ClassPropertyMethodCache() {
        }

        Method get(Class clazz, String propertyName) {
            ConcurrentHashMap<String, Method> methodsByPropertyName = this.cache.get(clazz);
            if (methodsByPropertyName == null) {
                methodsByPropertyName = new ConcurrentHashMap<>();
                this.cache.put(clazz, methodsByPropertyName);
            }
            Method method = methodsByPropertyName.get(propertyName);
            if (method == NULL_REPLACEMENT) {
                return null;
            }
            return method;
        }

        void put(Class clazz, String propertyName, Method method) {
            ConcurrentHashMap<String, Method> methodsByPropertyName = this.cache.get(clazz);
            if (methodsByPropertyName == null) {
                methodsByPropertyName = new ConcurrentHashMap<>();
                this.cache.put(clazz, methodsByPropertyName);
            }
            methodsByPropertyName.put(propertyName, method == null ? NULL_REPLACEMENT : method);
        }

        boolean containsKey(Class clazz, String propertyName) {
            ConcurrentHashMap<String, Method> methodsByPropertyName = this.cache.get(clazz);
            if (methodsByPropertyName == null) {
                return false;
            }
            return methodsByPropertyName.containsKey(propertyName);
        }
    }
}

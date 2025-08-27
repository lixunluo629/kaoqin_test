package org.apache.tomcat.util;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Hashtable;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.xmlbeans.XmlErrorCodes;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/IntrospectionUtils.class */
public final class IntrospectionUtils {
    private static final Log log = LogFactory.getLog((Class<?>) IntrospectionUtils.class);
    private static final Hashtable<Class<?>, Method[]> objectMethods = new Hashtable<>();

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/IntrospectionUtils$PropertySource.class */
    public interface PropertySource {
        String getProperty(String str);
    }

    public static boolean setProperty(Object o, String name, String value) {
        return setProperty(o, name, value, true);
    }

    public static boolean setProperty(Object o, String name, String value, boolean invokeSetProperty) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (log.isDebugEnabled()) {
            log.debug("IntrospectionUtils: setProperty(" + o.getClass() + SymbolConstants.SPACE_SYMBOL + name + SymbolConstants.EQUAL_SYMBOL + value + ")");
        }
        String setter = "set" + capitalize(name);
        try {
            Method[] methods = findMethods(o.getClass());
            Method setPropertyMethodVoid = null;
            Method setPropertyMethodBool = null;
            for (int i = 0; i < methods.length; i++) {
                Class<?>[] paramT = methods[i].getParameterTypes();
                if (setter.equals(methods[i].getName()) && paramT.length == 1 && "java.lang.String".equals(paramT[0].getName())) {
                    methods[i].invoke(o, value);
                    return true;
                }
            }
            for (int i2 = 0; i2 < methods.length; i2++) {
                boolean ok = true;
                if (setter.equals(methods[i2].getName()) && methods[i2].getParameterTypes().length == 1) {
                    Class<?> paramType = methods[i2].getParameterTypes()[0];
                    Object[] params = new Object[1];
                    if ("java.lang.Integer".equals(paramType.getName()) || XmlErrorCodes.INT.equals(paramType.getName())) {
                        try {
                            params[0] = Integer.valueOf(value);
                        } catch (NumberFormatException e) {
                            ok = false;
                        }
                    } else if ("java.lang.Long".equals(paramType.getName()) || XmlErrorCodes.LONG.equals(paramType.getName())) {
                        try {
                            params[0] = Long.valueOf(value);
                        } catch (NumberFormatException e2) {
                            ok = false;
                        }
                    } else if ("java.lang.Boolean".equals(paramType.getName()) || "boolean".equals(paramType.getName())) {
                        params[0] = Boolean.valueOf(value);
                    } else if ("java.net.InetAddress".equals(paramType.getName())) {
                        try {
                            params[0] = InetAddress.getByName(value);
                        } catch (UnknownHostException e3) {
                            if (log.isDebugEnabled()) {
                                log.debug("IntrospectionUtils: Unable to resolve host name:" + value);
                            }
                            ok = false;
                        }
                    } else if (log.isDebugEnabled()) {
                        log.debug("IntrospectionUtils: Unknown type " + paramType.getName());
                    }
                    if (ok) {
                        methods[i2].invoke(o, params);
                        return true;
                    }
                }
                if ("setProperty".equals(methods[i2].getName())) {
                    if (methods[i2].getReturnType() == Boolean.TYPE) {
                        setPropertyMethodBool = methods[i2];
                    } else {
                        setPropertyMethodVoid = methods[i2];
                    }
                }
            }
            if (!invokeSetProperty) {
                return false;
            }
            if (setPropertyMethodBool == null && setPropertyMethodVoid == null) {
                return false;
            }
            Object[] params2 = {name, value};
            if (setPropertyMethodBool == null) {
                setPropertyMethodVoid.invoke(o, params2);
                return true;
            }
            try {
                return ((Boolean) setPropertyMethodBool.invoke(o, params2)).booleanValue();
            } catch (IllegalArgumentException biae) {
                if (setPropertyMethodVoid == null) {
                    throw biae;
                }
                setPropertyMethodVoid.invoke(o, params2);
                return true;
            }
        } catch (IllegalAccessException iae) {
            log.warn("IntrospectionUtils: IllegalAccessException for " + o.getClass() + SymbolConstants.SPACE_SYMBOL + name + SymbolConstants.EQUAL_SYMBOL + value + ")", iae);
            return false;
        } catch (IllegalArgumentException ex2) {
            log.warn("IAE " + o + SymbolConstants.SPACE_SYMBOL + name + SymbolConstants.SPACE_SYMBOL + value, ex2);
            return false;
        } catch (SecurityException ex1) {
            log.warn("IntrospectionUtils: SecurityException for " + o.getClass() + SymbolConstants.SPACE_SYMBOL + name + SymbolConstants.EQUAL_SYMBOL + value + ")", ex1);
            return false;
        } catch (InvocationTargetException ie) {
            ExceptionUtils.handleThrowable(ie.getCause());
            log.warn("IntrospectionUtils: InvocationTargetException for " + o.getClass() + SymbolConstants.SPACE_SYMBOL + name + SymbolConstants.EQUAL_SYMBOL + value + ")", ie);
            return false;
        }
    }

    public static Object getProperty(Object o, String name) {
        String getter = BeanUtil.PREFIX_GETTER_GET + capitalize(name);
        String isGetter = BeanUtil.PREFIX_GETTER_IS + capitalize(name);
        try {
            Method[] methods = findMethods(o.getClass());
            Method getPropertyMethod = null;
            for (int i = 0; i < methods.length; i++) {
                Class<?>[] paramT = methods[i].getParameterTypes();
                if (getter.equals(methods[i].getName()) && paramT.length == 0) {
                    return methods[i].invoke(o, (Object[]) null);
                }
                if (isGetter.equals(methods[i].getName()) && paramT.length == 0) {
                    return methods[i].invoke(o, (Object[]) null);
                }
                if ("getProperty".equals(methods[i].getName())) {
                    getPropertyMethod = methods[i];
                }
            }
            if (getPropertyMethod == null) {
                return null;
            }
            Object[] params = {name};
            return getPropertyMethod.invoke(o, params);
        } catch (IllegalAccessException iae) {
            log.warn("IntrospectionUtils: IllegalAccessException for " + o.getClass() + SymbolConstants.SPACE_SYMBOL + name + ")", iae);
            return null;
        } catch (IllegalArgumentException ex2) {
            log.warn("IAE " + o + SymbolConstants.SPACE_SYMBOL + name, ex2);
            return null;
        } catch (SecurityException ex1) {
            log.warn("IntrospectionUtils: SecurityException for " + o.getClass() + SymbolConstants.SPACE_SYMBOL + name + ")", ex1);
            return null;
        } catch (InvocationTargetException ie) {
            if (ie.getCause() instanceof NullPointerException) {
                return null;
            }
            ExceptionUtils.handleThrowable(ie.getCause());
            log.warn("IntrospectionUtils: InvocationTargetException for " + o.getClass() + SymbolConstants.SPACE_SYMBOL + name + ")", ie);
            return null;
        }
    }

    public static String replaceProperties(String value, Hashtable<Object, Object> staticProp, PropertySource[] dynamicProp) {
        int prev;
        if (value.indexOf(36) < 0) {
            return value;
        }
        StringBuilder sb = new StringBuilder();
        int length = 0;
        while (true) {
            prev = length;
            int pos = value.indexOf(36, prev);
            if (pos < 0) {
                break;
            }
            if (pos > 0) {
                sb.append(value.substring(prev, pos));
            }
            if (pos == value.length() - 1) {
                sb.append('$');
                length = pos + 1;
            } else if (value.charAt(pos + 1) != '{') {
                sb.append('$');
                length = pos + 1;
            } else {
                int endName = value.indexOf(125, pos);
                if (endName < 0) {
                    sb.append(value.substring(pos));
                    length = value.length();
                } else {
                    String n = value.substring(pos + 2, endName);
                    String v = null;
                    if (staticProp != null) {
                        v = (String) staticProp.get(n);
                    }
                    if (v == null && dynamicProp != null) {
                        for (PropertySource propertySource : dynamicProp) {
                            v = propertySource.getProperty(n);
                            if (v != null) {
                                break;
                            }
                        }
                    }
                    if (v == null) {
                        v = "${" + n + "}";
                    }
                    sb.append(v);
                    length = endName + 1;
                }
            }
        }
        if (prev < value.length()) {
            sb.append(value.substring(prev));
        }
        return sb.toString();
    }

    public static String capitalize(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        char[] chars = name.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }

    public static void clear() {
        objectMethods.clear();
    }

    public static Method[] findMethods(Class<?> c) throws SecurityException {
        Method[] methods = objectMethods.get(c);
        if (methods != null) {
            return methods;
        }
        Method[] methods2 = c.getMethods();
        objectMethods.put(c, methods2);
        return methods2;
    }

    public static Method findMethod(Class<?> c, String name, Class<?>[] params) throws SecurityException {
        Method[] methods = findMethods(c);
        if (methods == null) {
            return null;
        }
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().equals(name)) {
                Class<?>[] methodParams = methods[i].getParameterTypes();
                if (methodParams == null && (params == null || params.length == 0)) {
                    return methods[i];
                }
                if (params == null && (methodParams == null || methodParams.length == 0)) {
                    return methods[i];
                }
                if (params.length != methodParams.length) {
                    continue;
                } else {
                    boolean found = true;
                    int j = 0;
                    while (true) {
                        if (j >= params.length) {
                            break;
                        }
                        if (params[j] == methodParams[j]) {
                            j++;
                        } else {
                            found = false;
                            break;
                        }
                    }
                    if (found) {
                        return methods[i];
                    }
                }
            }
        }
        return null;
    }

    public static Object callMethod1(Object target, String methodN, Object param1, String typeParam1, ClassLoader cl) throws Exception {
        if (target == null || param1 == null) {
            throw new IllegalArgumentException("IntrospectionUtils: Assert: Illegal params " + target + SymbolConstants.SPACE_SYMBOL + param1);
        }
        if (log.isDebugEnabled()) {
            log.debug("IntrospectionUtils: callMethod1 " + target.getClass().getName() + SymbolConstants.SPACE_SYMBOL + param1.getClass().getName() + SymbolConstants.SPACE_SYMBOL + typeParam1);
        }
        Class<?>[] params = new Class[1];
        if (typeParam1 == null) {
            params[0] = param1.getClass();
        } else {
            params[0] = cl.loadClass(typeParam1);
        }
        Method m = findMethod(target.getClass(), methodN, params);
        if (m == null) {
            throw new NoSuchMethodException(target.getClass().getName() + SymbolConstants.SPACE_SYMBOL + methodN);
        }
        try {
            return m.invoke(target, param1);
        } catch (InvocationTargetException ie) {
            ExceptionUtils.handleThrowable(ie.getCause());
            throw ie;
        }
    }

    public static Object callMethodN(Object target, String methodN, Object[] params, Class<?>[] typeParams) throws Exception {
        Method m = findMethod(target.getClass(), methodN, typeParams);
        if (m == null) {
            if (log.isDebugEnabled()) {
                log.debug("IntrospectionUtils: Can't find method " + methodN + " in " + target + " CLASS " + target.getClass());
                return null;
            }
            return null;
        }
        try {
            Object o = m.invoke(target, params);
            if (log.isDebugEnabled()) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName()).append('.').append(methodN).append("( ");
                for (int i = 0; i < params.length; i++) {
                    if (i > 0) {
                        sb.append(", ");
                    }
                    sb.append(params[i]);
                }
                sb.append(")");
                log.debug("IntrospectionUtils:" + sb.toString());
            }
            return o;
        } catch (InvocationTargetException ie) {
            ExceptionUtils.handleThrowable(ie.getCause());
            throw ie;
        }
    }

    public static Object convert(String object, Class<?> paramType) throws NumberFormatException, UnknownHostException {
        Object result = null;
        if ("java.lang.String".equals(paramType.getName())) {
            result = object;
        } else if ("java.lang.Integer".equals(paramType.getName()) || XmlErrorCodes.INT.equals(paramType.getName())) {
            try {
                result = Integer.valueOf(object);
            } catch (NumberFormatException e) {
            }
        } else if ("java.lang.Boolean".equals(paramType.getName()) || "boolean".equals(paramType.getName())) {
            result = Boolean.valueOf(object);
        } else if ("java.net.InetAddress".equals(paramType.getName())) {
            try {
                result = InetAddress.getByName(object);
            } catch (UnknownHostException e2) {
                if (log.isDebugEnabled()) {
                    log.debug("IntrospectionUtils: Unable to resolve host name:" + object);
                }
            }
        } else if (log.isDebugEnabled()) {
            log.debug("IntrospectionUtils: Unknown type " + paramType.getName());
        }
        if (result == null) {
            throw new IllegalArgumentException("Can't convert argument: " + object);
        }
        return result;
    }

    public static boolean isInstance(Class<?> clazz, String type) {
        if (type.equals(clazz.getName())) {
            return true;
        }
        Class<?>[] ifaces = clazz.getInterfaces();
        for (Class<?> iface : ifaces) {
            if (isInstance(iface, type)) {
                return true;
            }
        }
        Class<?> superClazz = clazz.getSuperclass();
        if (superClazz == null) {
            return false;
        }
        return isInstance(superClazz, type);
    }
}

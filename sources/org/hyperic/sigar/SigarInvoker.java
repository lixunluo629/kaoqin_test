package org.hyperic.sigar;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/SigarInvoker.class */
public class SigarInvoker {
    private static HashMap attrCache = new HashMap();
    private static HashMap compatTypes = new HashMap();
    private static HashMap compatAttrs = new HashMap();
    private static final Class[] VOID_SIGNATURE;
    private Class[] ARG_SIGNATURE;
    private Class[] ARG2_SIGNATURE;
    private static final Object[] VOID_ARGS;
    private Object[] ARG_ARGS;
    private String type;
    private boolean typeIsArray;
    private int arrayIdx;
    private boolean hasArrayIdx;
    private int typeArrayType;
    private static final int ARRAY_TYPE_OBJECT = 1;
    private static final int ARRAY_TYPE_DOUBLE = 2;
    private static final int ARRAY_TYPE_LONG = 3;
    private Method typeMethod;
    private SigarProxy sigarProxy;
    private SigarProxyCache handler;
    static Class class$java$lang$String;
    static Class class$org$hyperic$sigar$Sigar;

    static {
        compatTypes.put("NetIfconfig", "NetInterfaceConfig");
        compatTypes.put("NetIfstat", "NetInterfaceStat");
        compatTypes.put("DirStats", "DirStat");
        compatAttrs.put("Utime", "User");
        compatAttrs.put("Stime", "Sys");
        VOID_SIGNATURE = new Class[0];
        VOID_ARGS = new Object[0];
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    protected SigarInvoker() throws Throwable {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        Class[] clsArr = new Class[1];
        if (class$java$lang$String == null) {
            clsClass$ = class$("java.lang.String");
            class$java$lang$String = clsClass$;
        } else {
            clsClass$ = class$java$lang$String;
        }
        clsArr[0] = clsClass$;
        this.ARG_SIGNATURE = clsArr;
        Class[] clsArr2 = new Class[2];
        if (class$java$lang$String == null) {
            clsClass$2 = class$("java.lang.String");
            class$java$lang$String = clsClass$2;
        } else {
            clsClass$2 = class$java$lang$String;
        }
        clsArr2[0] = clsClass$2;
        if (class$java$lang$String == null) {
            clsClass$3 = class$("java.lang.String");
            class$java$lang$String = clsClass$3;
        } else {
            clsClass$3 = class$java$lang$String;
        }
        clsArr2[1] = clsClass$3;
        this.ARG2_SIGNATURE = clsArr2;
        this.ARG_ARGS = new Object[1];
        this.type = null;
        this.typeIsArray = false;
        this.arrayIdx = -1;
        this.hasArrayIdx = false;
    }

    public SigarInvoker(SigarProxy proxy, String type) throws Throwable {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        Class[] clsArr = new Class[1];
        if (class$java$lang$String == null) {
            clsClass$ = class$("java.lang.String");
            class$java$lang$String = clsClass$;
        } else {
            clsClass$ = class$java$lang$String;
        }
        clsArr[0] = clsClass$;
        this.ARG_SIGNATURE = clsArr;
        Class[] clsArr2 = new Class[2];
        if (class$java$lang$String == null) {
            clsClass$2 = class$("java.lang.String");
            class$java$lang$String = clsClass$2;
        } else {
            clsClass$2 = class$java$lang$String;
        }
        clsArr2[0] = clsClass$2;
        if (class$java$lang$String == null) {
            clsClass$3 = class$("java.lang.String");
            class$java$lang$String = clsClass$3;
        } else {
            clsClass$3 = class$java$lang$String;
        }
        clsArr2[1] = clsClass$3;
        this.ARG2_SIGNATURE = clsArr2;
        this.ARG_ARGS = new Object[1];
        this.type = null;
        this.typeIsArray = false;
        this.arrayIdx = -1;
        this.hasArrayIdx = false;
        setProxy(proxy);
        setType(type);
    }

    protected void setProxy(SigarProxy proxy) {
        try {
            this.handler = (SigarProxyCache) Proxy.getInvocationHandler(proxy);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        this.sigarProxy = proxy;
    }

    protected void setType(String val) {
        String alias = (String) compatTypes.get(val);
        if (alias != null) {
            val = alias;
        }
        this.type = val;
    }

    public String getType() {
        return this.type;
    }

    private int getAttributeIndex(String attr) {
        try {
            return Integer.valueOf(attr).intValue();
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private Method getTypeMethod(Object[] args) throws Throwable {
        Class clsClass$;
        Class clsClass$2;
        if (this.typeMethod == null) {
            Class[] sig = VOID_SIGNATURE;
            boolean argIsArrayIdx = false;
            int argLength = 0;
            String getter = new StringBuffer().append(BeanUtil.PREFIX_GETTER_GET).append(getType()).toString();
            if (args != null) {
                argLength = args.length;
                switch (argLength) {
                    case 1:
                        sig = this.ARG_SIGNATURE;
                        break;
                    case 2:
                        sig = this.ARG2_SIGNATURE;
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            }
            try {
                if (class$org$hyperic$sigar$Sigar == null) {
                    clsClass$2 = class$("org.hyperic.sigar.Sigar");
                    class$org$hyperic$sigar$Sigar = clsClass$2;
                } else {
                    clsClass$2 = class$org$hyperic$sigar$Sigar;
                }
                this.typeMethod = clsClass$2.getMethod(getter, sig);
            } catch (Exception e) {
                try {
                    if (class$org$hyperic$sigar$Sigar == null) {
                        clsClass$ = class$("org.hyperic.sigar.Sigar");
                        class$org$hyperic$sigar$Sigar = clsClass$;
                    } else {
                        clsClass$ = class$org$hyperic$sigar$Sigar;
                    }
                    this.typeMethod = clsClass$.getMethod(getter, VOID_SIGNATURE);
                    if (argLength == 1) {
                        argIsArrayIdx = true;
                    }
                } catch (Exception e2) {
                    String msg = new StringBuffer().append("Unable to determine getter for ").append(this.type).toString();
                    throw new SigarException(msg);
                }
            }
            Class typeClass = this.typeMethod.getReturnType();
            if (typeClass.isArray()) {
                this.typeIsArray = true;
                if (argIsArrayIdx) {
                    try {
                        this.arrayIdx = Integer.parseInt((String) args[0]);
                        this.hasArrayIdx = true;
                    } catch (NumberFormatException e3) {
                        String msg2 = new StringBuffer().append(getType()).append(": '").append(args[0]).append("' is not a number").toString();
                        throw new SigarException(msg2);
                    }
                }
                Class componentClass = typeClass.getComponentType();
                if (componentClass.isPrimitive()) {
                    if (componentClass == Double.TYPE) {
                        this.typeArrayType = 2;
                    } else if (componentClass == Long.TYPE) {
                        this.typeArrayType = 3;
                    } else {
                        throw new SigarException(new StringBuffer().append("unsupported array type: ").append(componentClass.getName()).toString());
                    }
                } else {
                    this.typeArrayType = 1;
                }
            } else {
                this.typeIsArray = false;
            }
        }
        return this.typeMethod;
    }

    public Object invoke(Object arg, String attr) throws SigarException {
        Object[] args = null;
        if (arg != null) {
            args = this.ARG_ARGS;
            args[0] = arg;
        }
        return invoke(args, attr);
    }

    private String aobMsg(int idx, int length) {
        return new StringBuffer().append("Array index ").append(idx).append(" out of bounds ").append(length).toString();
    }

    public Object invoke(Object[] args, String attr) throws Throwable {
        Method typeGetter = getTypeMethod(args);
        if (this.hasArrayIdx) {
            args = null;
        }
        try {
            Object typeObject = this.handler.invoke(this.sigarProxy, typeGetter, args);
            if (attr == null) {
                return typeObject;
            }
            if (this.typeIsArray) {
                if (this.hasArrayIdx) {
                    Object[] array = (Object[]) typeObject;
                    if (this.arrayIdx >= array.length) {
                        throw new SigarException(aobMsg(this.arrayIdx, array.length));
                    }
                    typeObject = array[this.arrayIdx];
                } else {
                    int idx = getAttributeIndex(attr);
                    if (idx < 0) {
                        throw new SigarException(new StringBuffer().append("Invalid array index: ").append(attr).toString());
                    }
                    switch (this.typeArrayType) {
                        case 1:
                            Object[] o_array = (Object[]) typeObject;
                            if (idx >= o_array.length) {
                                throw new SigarException(aobMsg(idx, o_array.length));
                            }
                            return o_array[idx];
                        case 2:
                            double[] d_array = (double[]) typeObject;
                            if (idx >= d_array.length) {
                                throw new SigarException(aobMsg(idx, d_array.length));
                            }
                            return new Double(d_array[idx]);
                        case 3:
                            long[] l_array = (long[]) typeObject;
                            if (idx >= l_array.length) {
                                throw new SigarException(aobMsg(idx, l_array.length));
                            }
                            return new Long(l_array[idx]);
                    }
                }
            }
            Method attrGetter = getAttributeMethod(attr);
            try {
                return attrGetter.invoke(typeObject, VOID_ARGS);
            } catch (Throwable t) {
                throw new SigarException(t.getMessage());
            }
        } catch (Throwable t2) {
            String parms = args == null ? "" : Arrays.asList(args).toString();
            String msg = new StringBuffer().append("Failed to invoke ").append(typeGetter.getName()).append(parms).append(": ").append(t2.getMessage()).toString();
            if (t2 instanceof SigarNotImplementedException) {
                throw ((SigarNotImplementedException) t2);
            }
            if (t2 instanceof SigarPermissionDeniedException) {
                throw ((SigarPermissionDeniedException) t2);
            }
            throw new SigarException(msg);
        }
    }

    private Method getAttributeMethod(String attr) throws SigarException, NoSuchMethodException, SecurityException {
        String alias = (String) compatAttrs.get(attr);
        if (alias != null) {
            attr = alias;
        }
        Class type = getTypeMethod(null).getReturnType();
        if (this.hasArrayIdx) {
            type = type.getComponentType();
        }
        synchronized (attrCache) {
            HashMap attrs = (HashMap) attrCache.get(type);
            if (attrs == null) {
                attrs = new HashMap();
                attrCache.put(type, attrs);
            } else {
                Method attrMethod = (Method) attrs.get(attr);
                if (attrMethod != null) {
                    return attrMethod;
                }
            }
            try {
                Method attrMethod2 = type.getMethod(new StringBuffer().append(BeanUtil.PREFIX_GETTER_GET).append(attr).toString(), VOID_SIGNATURE);
                synchronized (attrs) {
                    attrs.put(attr, attrMethod2);
                }
                return attrMethod2;
            } catch (Exception e) {
                String msg = new StringBuffer().append("Failed to invoke get").append(attr).append(": ").append(e.getMessage()).toString();
                throw new SigarException(msg);
            }
        }
    }
}

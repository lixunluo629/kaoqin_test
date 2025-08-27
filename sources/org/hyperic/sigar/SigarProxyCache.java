package org.hyperic.sigar;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import org.hyperic.sigar.util.ReferenceMap;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/SigarProxyCache.class */
public class SigarProxyCache implements InvocationHandler {
    private Sigar sigar;
    private Map cache = ReferenceMap.newInstance();
    public static final int EXPIRE_DEFAULT = 30000;
    private int expire;
    private static final boolean debugEnabled = "debug".equals(System.getProperty("sigar.log"));
    static Class class$org$hyperic$sigar$SigarProxy;
    static Class class$org$hyperic$sigar$Sigar;

    public SigarProxyCache(Sigar sigar, int expire) {
        this.sigar = sigar;
        this.expire = expire;
    }

    public static SigarProxy newInstance() {
        return newInstance(new Sigar());
    }

    public static SigarProxy newInstance(Sigar sigar) {
        return newInstance(sigar, 30000);
    }

    public static SigarProxy newInstance(Sigar sigar, int expire) throws Throwable {
        Class clsClass$;
        Class clsClass$2;
        SigarProxyCache handler = new SigarProxyCache(sigar, expire);
        if (class$org$hyperic$sigar$SigarProxy == null) {
            clsClass$ = class$("org.hyperic.sigar.SigarProxy");
            class$org$hyperic$sigar$SigarProxy = clsClass$;
        } else {
            clsClass$ = class$org$hyperic$sigar$SigarProxy;
        }
        ClassLoader classLoader = clsClass$.getClassLoader();
        Class[] clsArr = new Class[1];
        if (class$org$hyperic$sigar$SigarProxy == null) {
            clsClass$2 = class$("org.hyperic.sigar.SigarProxy");
            class$org$hyperic$sigar$SigarProxy = clsClass$2;
        } else {
            clsClass$2 = class$org$hyperic$sigar$SigarProxy;
        }
        clsArr[0] = clsClass$2;
        SigarProxy proxy = (SigarProxy) Proxy.newProxyInstance(classLoader, clsArr, handler);
        return proxy;
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    private void debug(String msg) {
        SigarLog.getLogger("SigarProxyCache").debug(msg);
    }

    public static void setExpire(SigarProxy proxy, String type, int expire) throws SigarException {
    }

    private static SigarProxyCache getHandler(Object proxy) {
        return (SigarProxyCache) Proxy.getInvocationHandler(proxy);
    }

    public static void clear(Object proxy) {
        getHandler(proxy).cache.clear();
    }

    public static Sigar getSigar(Object proxy) throws Throwable {
        Class<?> clsClass$;
        Class<?> cls = proxy.getClass();
        if (class$org$hyperic$sigar$Sigar == null) {
            clsClass$ = class$("org.hyperic.sigar.Sigar");
            class$org$hyperic$sigar$Sigar = clsClass$;
        } else {
            clsClass$ = class$org$hyperic$sigar$Sigar;
        }
        if (cls == clsClass$) {
            return (Sigar) proxy;
        }
        return getHandler(proxy).sigar;
    }

    private String getDebugArgs(Object[] args, Object argKey) {
        if (args.length == 0) {
            return null;
        }
        StringBuffer dargs = new StringBuffer(args[0].toString());
        for (int i = 1; i < args.length; i++) {
            dargs.append(',').append(args[i].toString());
        }
        if (!dargs.toString().equals(argKey.toString())) {
            dargs.append('/').append(argKey);
        }
        return dargs.toString();
    }

    @Override // java.lang.reflect.InvocationHandler
    public synchronized Object invoke(Object proxy, Method method, Object[] args) throws IllegalAccessException, SigarException, IllegalArgumentException, InvocationTargetException {
        String msg;
        Object retval;
        SigarCacheObject cacheVal = null;
        Object argKey = null;
        Map argMap = null;
        long timeNow = System.currentTimeMillis();
        if (args != null) {
            if (args.length == 1) {
                argKey = args[0];
            } else {
                int hashCode = 0;
                for (Object obj : args) {
                    hashCode ^= obj.hashCode();
                }
                argKey = new Integer(hashCode);
            }
            argMap = (Map) this.cache.get(method);
            if (argMap == null) {
                argMap = ReferenceMap.newInstance();
            } else {
                cacheVal = (SigarCacheObject) argMap.get(argKey);
            }
        } else {
            cacheVal = (SigarCacheObject) this.cache.get(method);
        }
        if (cacheVal == null) {
            cacheVal = new SigarCacheObject();
        }
        String argDebug = "";
        if (debugEnabled && args != null && args.length != 0) {
            argDebug = new StringBuffer().append(" with args=").append(getDebugArgs(args, argKey)).toString();
        }
        if (cacheVal.value != null) {
            if (debugEnabled) {
                debug(new StringBuffer().append("found ").append(method.getName()).append(" in cache").append(argDebug).toString());
            }
            if (timeNow - cacheVal.timestamp > this.expire) {
                if (debugEnabled) {
                    debug(new StringBuffer().append("expiring ").append(method.getName()).append(" from cache").append(argDebug).toString());
                }
                cacheVal.value = null;
            }
        } else if (debugEnabled) {
            debug(new StringBuffer().append(method.getName()).append(" NOT in cache").append(argDebug).toString());
        }
        if (cacheVal.value == null) {
            try {
                retval = method.invoke(this.sigar, args);
                cacheVal.value = retval;
                cacheVal.timestamp = timeNow;
                if (args == null) {
                    this.cache.put(method, cacheVal);
                } else {
                    argMap.put(argKey, cacheVal);
                    this.cache.put(method, argMap);
                }
            } catch (InvocationTargetException e) {
                Throwable t = e.getTargetException();
                if (t instanceof SigarException) {
                    msg = "";
                } else {
                    msg = new StringBuffer().append(t.getClass().getName()).append(": ").toString();
                }
                String msg2 = new StringBuffer().append(msg).append(t.getMessage()).toString();
                if (argKey != null) {
                    msg2 = new StringBuffer().append(msg2).append(": ").append(getDebugArgs(args, argKey)).toString();
                }
                if (t instanceof SigarNotImplementedException) {
                    throw new SigarNotImplementedException(msg2);
                }
                if (t instanceof SigarPermissionDeniedException) {
                    throw new SigarPermissionDeniedException(msg2);
                }
                throw new SigarException(msg2);
            } catch (Exception e2) {
                String msg3 = new StringBuffer().append(e2.getClass().getName()).append(": ").append(e2.getMessage()).toString();
                if (argKey != null) {
                    msg3 = new StringBuffer().append(msg3).append(": ").append(getDebugArgs(args, argKey)).toString();
                }
                throw new SigarException(msg3);
            }
        } else {
            retval = cacheVal.value;
        }
        return retval;
    }
}

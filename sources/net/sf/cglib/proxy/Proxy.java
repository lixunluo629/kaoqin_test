package net.sf.cglib.proxy;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.sf.cglib.core.CodeGenerationException;
import org.springframework.jmx.export.naming.IdentityNamingStrategy;

/* loaded from: cglib-3.1.jar:net/sf/cglib/proxy/Proxy.class */
public class Proxy implements Serializable {
    protected InvocationHandler h;
    private static final CallbackFilter BAD_OBJECT_METHOD_FILTER = new CallbackFilter() { // from class: net.sf.cglib.proxy.Proxy.1
        @Override // net.sf.cglib.proxy.CallbackFilter
        public int accept(Method method) {
            if (method.getDeclaringClass().getName().equals("java.lang.Object")) {
                String name = method.getName();
                if (!name.equals(IdentityNamingStrategy.HASH_CODE_KEY) && !name.equals("equals") && !name.equals("toString")) {
                    return 1;
                }
                return 0;
            }
            return 0;
        }
    };
    static Class class$net$sf$cglib$proxy$Proxy$ProxyImpl;
    static Class class$net$sf$cglib$proxy$InvocationHandler;
    static Class class$net$sf$cglib$proxy$NoOp;

    protected Proxy(InvocationHandler h) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Enhancer.registerCallbacks(getClass(), new Callback[]{h, null});
        this.h = h;
    }

    /* loaded from: cglib-3.1.jar:net/sf/cglib/proxy/Proxy$ProxyImpl.class */
    private static class ProxyImpl extends Proxy {
        protected ProxyImpl(InvocationHandler h) {
            super(h);
        }
    }

    public static InvocationHandler getInvocationHandler(Object proxy) {
        if (!(proxy instanceof ProxyImpl)) {
            throw new IllegalArgumentException("Object is not a proxy");
        }
        return ((Proxy) proxy).h;
    }

    public static Class getProxyClass(ClassLoader loader, Class[] interfaces) {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        Enhancer e = new Enhancer();
        if (class$net$sf$cglib$proxy$Proxy$ProxyImpl == null) {
            clsClass$ = class$("net.sf.cglib.proxy.Proxy$ProxyImpl");
            class$net$sf$cglib$proxy$Proxy$ProxyImpl = clsClass$;
        } else {
            clsClass$ = class$net$sf$cglib$proxy$Proxy$ProxyImpl;
        }
        e.setSuperclass(clsClass$);
        e.setInterfaces(interfaces);
        Class[] clsArr = new Class[2];
        if (class$net$sf$cglib$proxy$InvocationHandler == null) {
            clsClass$2 = class$("net.sf.cglib.proxy.InvocationHandler");
            class$net$sf$cglib$proxy$InvocationHandler = clsClass$2;
        } else {
            clsClass$2 = class$net$sf$cglib$proxy$InvocationHandler;
        }
        clsArr[0] = clsClass$2;
        if (class$net$sf$cglib$proxy$NoOp == null) {
            clsClass$3 = class$("net.sf.cglib.proxy.NoOp");
            class$net$sf$cglib$proxy$NoOp = clsClass$3;
        } else {
            clsClass$3 = class$net$sf$cglib$proxy$NoOp;
        }
        clsArr[1] = clsClass$3;
        e.setCallbackTypes(clsArr);
        e.setCallbackFilter(BAD_OBJECT_METHOD_FILTER);
        e.setUseFactory(false);
        return e.createClass();
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    public static boolean isProxyClass(Class cl) {
        Class clsClass$;
        Class superclass = cl.getSuperclass();
        if (class$net$sf$cglib$proxy$Proxy$ProxyImpl == null) {
            clsClass$ = class$("net.sf.cglib.proxy.Proxy$ProxyImpl");
            class$net$sf$cglib$proxy$Proxy$ProxyImpl = clsClass$;
        } else {
            clsClass$ = class$net$sf$cglib$proxy$Proxy$ProxyImpl;
        }
        return superclass.equals(clsClass$);
    }

    public static Object newProxyInstance(ClassLoader loader, Class[] interfaces, InvocationHandler h) {
        Class<?> clsClass$;
        try {
            Class clazz = getProxyClass(loader, interfaces);
            Class<?>[] clsArr = new Class[1];
            if (class$net$sf$cglib$proxy$InvocationHandler == null) {
                clsClass$ = class$("net.sf.cglib.proxy.InvocationHandler");
                class$net$sf$cglib$proxy$InvocationHandler = clsClass$;
            } else {
                clsClass$ = class$net$sf$cglib$proxy$InvocationHandler;
            }
            clsArr[0] = clsClass$;
            return clazz.getConstructor(clsArr).newInstance(h);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e2) {
            throw new CodeGenerationException(e2);
        }
    }
}

package net.sf.cglib.proxy;

import org.objectweb.asm.Type;

/* loaded from: cglib-3.1.jar:net/sf/cglib/proxy/CallbackInfo.class */
class CallbackInfo {
    private Class cls;
    private CallbackGenerator generator;
    private Type type;
    private static final CallbackInfo[] CALLBACKS;
    static Class class$net$sf$cglib$proxy$NoOp;
    static Class class$net$sf$cglib$proxy$MethodInterceptor;
    static Class class$net$sf$cglib$proxy$InvocationHandler;
    static Class class$net$sf$cglib$proxy$LazyLoader;
    static Class class$net$sf$cglib$proxy$Dispatcher;
    static Class class$net$sf$cglib$proxy$FixedValue;
    static Class class$net$sf$cglib$proxy$ProxyRefDispatcher;

    public static Type[] determineTypes(Class[] callbackTypes) {
        Type[] types = new Type[callbackTypes.length];
        for (int i = 0; i < types.length; i++) {
            types[i] = determineType(callbackTypes[i]);
        }
        return types;
    }

    public static Type[] determineTypes(Callback[] callbacks) {
        Type[] types = new Type[callbacks.length];
        for (int i = 0; i < types.length; i++) {
            types[i] = determineType(callbacks[i]);
        }
        return types;
    }

    public static CallbackGenerator[] getGenerators(Type[] callbackTypes) {
        CallbackGenerator[] generators = new CallbackGenerator[callbackTypes.length];
        for (int i = 0; i < generators.length; i++) {
            generators[i] = getGenerator(callbackTypes[i]);
        }
        return generators;
    }

    static {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        Class clsClass$4;
        Class clsClass$5;
        Class clsClass$6;
        Class clsClass$7;
        CallbackInfo[] callbackInfoArr = new CallbackInfo[7];
        if (class$net$sf$cglib$proxy$NoOp == null) {
            clsClass$ = class$("net.sf.cglib.proxy.NoOp");
            class$net$sf$cglib$proxy$NoOp = clsClass$;
        } else {
            clsClass$ = class$net$sf$cglib$proxy$NoOp;
        }
        callbackInfoArr[0] = new CallbackInfo(clsClass$, NoOpGenerator.INSTANCE);
        if (class$net$sf$cglib$proxy$MethodInterceptor == null) {
            clsClass$2 = class$("net.sf.cglib.proxy.MethodInterceptor");
            class$net$sf$cglib$proxy$MethodInterceptor = clsClass$2;
        } else {
            clsClass$2 = class$net$sf$cglib$proxy$MethodInterceptor;
        }
        callbackInfoArr[1] = new CallbackInfo(clsClass$2, MethodInterceptorGenerator.INSTANCE);
        if (class$net$sf$cglib$proxy$InvocationHandler == null) {
            clsClass$3 = class$("net.sf.cglib.proxy.InvocationHandler");
            class$net$sf$cglib$proxy$InvocationHandler = clsClass$3;
        } else {
            clsClass$3 = class$net$sf$cglib$proxy$InvocationHandler;
        }
        callbackInfoArr[2] = new CallbackInfo(clsClass$3, InvocationHandlerGenerator.INSTANCE);
        if (class$net$sf$cglib$proxy$LazyLoader == null) {
            clsClass$4 = class$("net.sf.cglib.proxy.LazyLoader");
            class$net$sf$cglib$proxy$LazyLoader = clsClass$4;
        } else {
            clsClass$4 = class$net$sf$cglib$proxy$LazyLoader;
        }
        callbackInfoArr[3] = new CallbackInfo(clsClass$4, LazyLoaderGenerator.INSTANCE);
        if (class$net$sf$cglib$proxy$Dispatcher == null) {
            clsClass$5 = class$("net.sf.cglib.proxy.Dispatcher");
            class$net$sf$cglib$proxy$Dispatcher = clsClass$5;
        } else {
            clsClass$5 = class$net$sf$cglib$proxy$Dispatcher;
        }
        callbackInfoArr[4] = new CallbackInfo(clsClass$5, DispatcherGenerator.INSTANCE);
        if (class$net$sf$cglib$proxy$FixedValue == null) {
            clsClass$6 = class$("net.sf.cglib.proxy.FixedValue");
            class$net$sf$cglib$proxy$FixedValue = clsClass$6;
        } else {
            clsClass$6 = class$net$sf$cglib$proxy$FixedValue;
        }
        callbackInfoArr[5] = new CallbackInfo(clsClass$6, FixedValueGenerator.INSTANCE);
        if (class$net$sf$cglib$proxy$ProxyRefDispatcher == null) {
            clsClass$7 = class$("net.sf.cglib.proxy.ProxyRefDispatcher");
            class$net$sf$cglib$proxy$ProxyRefDispatcher = clsClass$7;
        } else {
            clsClass$7 = class$net$sf$cglib$proxy$ProxyRefDispatcher;
        }
        callbackInfoArr[6] = new CallbackInfo(clsClass$7, DispatcherGenerator.PROXY_REF_INSTANCE);
        CALLBACKS = callbackInfoArr;
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    private CallbackInfo(Class cls, CallbackGenerator generator) {
        this.cls = cls;
        this.generator = generator;
        this.type = Type.getType((Class<?>) cls);
    }

    private static Type determineType(Callback callback) {
        if (callback == null) {
            throw new IllegalStateException("Callback is null");
        }
        return determineType(callback.getClass());
    }

    private static Type determineType(Class callbackType) {
        Class cur = null;
        for (int i = 0; i < CALLBACKS.length; i++) {
            CallbackInfo info = CALLBACKS[i];
            if (info.cls.isAssignableFrom(callbackType)) {
                if (cur != null) {
                    throw new IllegalStateException(new StringBuffer().append("Callback implements both ").append(cur).append(" and ").append(info.cls).toString());
                }
                cur = info.cls;
            }
        }
        if (cur == null) {
            throw new IllegalStateException(new StringBuffer().append("Unknown callback type ").append(callbackType).toString());
        }
        return Type.getType((Class<?>) cur);
    }

    private static CallbackGenerator getGenerator(Type callbackType) {
        for (int i = 0; i < CALLBACKS.length; i++) {
            CallbackInfo info = CALLBACKS[i];
            if (info.type.equals(callbackType)) {
                return info.generator;
            }
        }
        throw new IllegalStateException(new StringBuffer().append("Unknown callback type ").append(callbackType).toString());
    }
}

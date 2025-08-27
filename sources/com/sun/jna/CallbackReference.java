package com.sun.jna;

import com.sun.jna.Library;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/* loaded from: jna-3.0.9.jar:com/sun/jna/CallbackReference.class */
class CallbackReference extends WeakReference {
    static final Map callbackMap = new WeakHashMap();
    static final Map altCallbackMap = new WeakHashMap();
    private static final Map allocations = new WeakHashMap();
    private static final Method PROXY_CALLBACK_METHOD;
    Pointer cbstruct;
    CallbackProxy proxy;
    static Class array$Ljava$lang$Object;
    static Class class$com$sun$jna$CallbackProxy;
    static Class class$com$sun$jna$AltCallingConvention;
    static Class class$com$sun$jna$CallbackReference$NativeFunctionProxy;
    static Class class$com$sun$jna$Structure;
    static Class class$com$sun$jna$Structure$ByValue;
    static Class class$com$sun$jna$Pointer;
    static Class class$com$sun$jna$NativeMapped;
    static Class class$java$lang$String;
    static Class class$com$sun$jna$WString;
    static Class array$Ljava$lang$String;
    static Class array$Lcom$sun$jna$WString;
    static Class class$com$sun$jna$Callback;
    static Class class$java$lang$Boolean;
    static Class class$java$lang$Void;
    static Class class$java$lang$Byte;
    static Class class$java$lang$Short;
    static Class class$java$lang$Character;
    static Class class$java$lang$Integer;
    static Class class$java$lang$Long;
    static Class class$java$lang$Float;
    static Class class$java$lang$Double;

    /* loaded from: jna-3.0.9.jar:com/sun/jna/CallbackReference$NativeFunctionProxy.class */
    private interface NativeFunctionProxy {
    }

    private static native synchronized Pointer createNativeCallback(CallbackProxy callbackProxy, Method method, Class[] clsArr, Class cls, int i);

    private static native synchronized void freeNativeCallback(long j);

    static {
        Class clsClass$;
        Class<?> clsClass$2;
        try {
            if (class$com$sun$jna$CallbackProxy == null) {
                clsClass$ = class$("com.sun.jna.CallbackProxy");
                class$com$sun$jna$CallbackProxy = clsClass$;
            } else {
                clsClass$ = class$com$sun$jna$CallbackProxy;
            }
            Class<?>[] clsArr = new Class[1];
            if (array$Ljava$lang$Object == null) {
                clsClass$2 = class$("[Ljava.lang.Object;");
                array$Ljava$lang$Object = clsClass$2;
            } else {
                clsClass$2 = array$Ljava$lang$Object;
            }
            clsArr[0] = clsClass$2;
            PROXY_CALLBACK_METHOD = clsClass$.getMethod(Callback.METHOD_NAME, clsArr);
        } catch (Exception e) {
            throw new Error("Error looking up CallbackProxy.callback() method");
        }
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    public static Callback getCallback(Class type, Pointer p) {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        if (p != null) {
            if (!type.isInterface()) {
                throw new IllegalArgumentException("Callback type must be an interface");
            }
            if (class$com$sun$jna$AltCallingConvention == null) {
                clsClass$ = class$("com.sun.jna.AltCallingConvention");
                class$com$sun$jna$AltCallingConvention = clsClass$;
            } else {
                clsClass$ = class$com$sun$jna$AltCallingConvention;
            }
            Map map = clsClass$.isAssignableFrom(type) ? altCallbackMap : callbackMap;
            synchronized (map) {
                for (Callback cb : map.keySet()) {
                    if (type.isAssignableFrom(cb.getClass())) {
                        CallbackReference cbref = (CallbackReference) map.get(cb);
                        Pointer cbp = cbref != null ? cbref.getTrampoline() : getNativeFunctionPointer(cb);
                        if (p.equals(cbp)) {
                            return cb;
                        }
                    }
                }
                if (class$com$sun$jna$AltCallingConvention == null) {
                    clsClass$2 = class$("com.sun.jna.AltCallingConvention");
                    class$com$sun$jna$AltCallingConvention = clsClass$2;
                } else {
                    clsClass$2 = class$com$sun$jna$AltCallingConvention;
                }
                int ctype = clsClass$2.isAssignableFrom(type) ? 1 : 0;
                Map options = Native.getLibraryOptions(type);
                NativeFunctionHandler h = new NativeFunctionHandler(p, ctype, options);
                ClassLoader classLoader = type.getClassLoader();
                Class[] clsArr = new Class[2];
                clsArr[0] = type;
                if (class$com$sun$jna$CallbackReference$NativeFunctionProxy == null) {
                    clsClass$3 = class$("com.sun.jna.CallbackReference$NativeFunctionProxy");
                    class$com$sun$jna$CallbackReference$NativeFunctionProxy = clsClass$3;
                } else {
                    clsClass$3 = class$com$sun$jna$CallbackReference$NativeFunctionProxy;
                }
                clsArr[1] = clsClass$3;
                Callback cb2 = (Callback) Proxy.newProxyInstance(classLoader, clsArr, h);
                h.options.put("invoking-method", getCallbackMethod(cb2));
                map.put(cb2, null);
                return cb2;
            }
        }
        return null;
    }

    private CallbackReference(Callback callback, int callingConvention) throws Throwable {
        super(callback);
        TypeMapper mapper = Native.getTypeMapper(Native.findCallbackClass(callback.getClass()));
        if (callback instanceof CallbackProxy) {
            this.proxy = (CallbackProxy) callback;
        } else {
            this.proxy = new DefaultCallbackProxy(this, getCallbackMethod(callback), mapper);
        }
        Class[] nativeParamTypes = this.proxy.getParameterTypes();
        Class returnType = this.proxy.getReturnType();
        if (mapper != null) {
            for (int i = 0; i < nativeParamTypes.length; i++) {
                FromNativeConverter rc = mapper.getFromNativeConverter(nativeParamTypes[i]);
                if (rc != null) {
                    nativeParamTypes[i] = rc.nativeType();
                }
            }
            ToNativeConverter tn = mapper.getToNativeConverter(returnType);
            if (tn != null) {
                returnType = tn.nativeType();
            }
        }
        for (int i2 = 0; i2 < nativeParamTypes.length; i2++) {
            nativeParamTypes[i2] = getNativeType(nativeParamTypes[i2]);
            if (!isAllowableNativeType(nativeParamTypes[i2])) {
                String msg = new StringBuffer().append("Callback argument ").append(nativeParamTypes[i2]).append(" requires custom type conversion").toString();
                throw new IllegalArgumentException(msg);
            }
        }
        Class returnType2 = getNativeType(returnType);
        if (!isAllowableNativeType(returnType2)) {
            String msg2 = new StringBuffer().append("Callback return type ").append(returnType2).append(" requires custom type conversion").toString();
            throw new IllegalArgumentException(msg2);
        }
        this.cbstruct = createNativeCallback(this.proxy, PROXY_CALLBACK_METHOD, nativeParamTypes, returnType2, callingConvention);
    }

    /* JADX WARN: Code restructure failed: missing block: B:52:0x00f4, code lost:
    
        if (r0.isAssignableFrom(r5) != false) goto L53;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.lang.Class getNativeType(java.lang.Class r5) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 271
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.jna.CallbackReference.getNativeType(java.lang.Class):java.lang.Class");
    }

    private static Method checkMethod(Method m) {
        if (m.getParameterTypes().length > 256) {
            String msg = new StringBuffer().append("Method signature exceeds the maximum parameter count: ").append(m).toString();
            throw new IllegalArgumentException(msg);
        }
        return m;
    }

    private static Method getCallbackMethod(Callback callback) throws Throwable {
        Class cls = Native.findCallbackClass(callback.getClass());
        Method[] pubMethods = cls.getDeclaredMethods();
        Method[] classMethods = cls.getMethods();
        Set pmethods = new HashSet(Arrays.asList(pubMethods));
        pmethods.retainAll(Arrays.asList(classMethods));
        Iterator i = pmethods.iterator();
        while (i.hasNext()) {
            if (Callback.FORBIDDEN_NAMES.contains(((Method) i.next()).getName())) {
                i.remove();
            }
        }
        Method[] methods = (Method[]) pmethods.toArray(new Method[pmethods.size()]);
        if (methods.length == 1) {
            return checkMethod(methods[0]);
        }
        for (Method m : methods) {
            if (Callback.METHOD_NAME.equals(m.getName())) {
                return checkMethod(m);
            }
        }
        throw new IllegalArgumentException("Callback must implement a single public method, or one public method named 'callback'");
    }

    public Pointer getTrampoline() {
        return this.cbstruct.getPointer(0L);
    }

    protected void finalize() {
        freeNativeCallback(this.cbstruct.peer);
        this.cbstruct.peer = 0L;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Callback getCallback() {
        return (Callback) get();
    }

    private static Pointer getNativeFunctionPointer(Callback cb) {
        if (cb instanceof NativeFunctionProxy) {
            NativeFunctionHandler handler = (NativeFunctionHandler) Proxy.getInvocationHandler(cb);
            return handler.getPointer();
        }
        return null;
    }

    public static Pointer getFunctionPointer(Callback cb) {
        Pointer trampoline;
        if (cb == null) {
            return null;
        }
        Pointer fp = getNativeFunctionPointer(cb);
        if (fp != null) {
            return fp;
        }
        int callingConvention = cb instanceof AltCallingConvention ? 1 : 0;
        Map map = callingConvention == 1 ? altCallbackMap : callbackMap;
        synchronized (map) {
            CallbackReference cbref = (CallbackReference) map.get(cb);
            if (cbref == null) {
                cbref = new CallbackReference(cb, callingConvention);
                map.put(cb, cbref);
            }
            trampoline = cbref.getTrampoline();
        }
        return trampoline;
    }

    /* loaded from: jna-3.0.9.jar:com/sun/jna/CallbackReference$DefaultCallbackProxy.class */
    private class DefaultCallbackProxy implements CallbackProxy {
        private Method callbackMethod;
        private ToNativeConverter toNative;
        private FromNativeConverter[] fromNative;
        private final CallbackReference this$0;

        public DefaultCallbackProxy(CallbackReference callbackReference, Method callbackMethod, TypeMapper mapper) throws Throwable {
            Class clsClass$;
            Class clsClass$2;
            this.this$0 = callbackReference;
            this.callbackMethod = callbackMethod;
            Class[] argTypes = callbackMethod.getParameterTypes();
            Class returnType = callbackMethod.getReturnType();
            this.fromNative = new FromNativeConverter[argTypes.length];
            if (CallbackReference.class$com$sun$jna$NativeMapped == null) {
                clsClass$ = CallbackReference.class$("com.sun.jna.NativeMapped");
                CallbackReference.class$com$sun$jna$NativeMapped = clsClass$;
            } else {
                clsClass$ = CallbackReference.class$com$sun$jna$NativeMapped;
            }
            if (clsClass$.isAssignableFrom(returnType)) {
                this.toNative = NativeMappedConverter.getInstance(returnType);
            } else if (mapper != null) {
                this.toNative = mapper.getToNativeConverter(returnType);
            }
            for (int i = 0; i < this.fromNative.length; i++) {
                if (CallbackReference.class$com$sun$jna$NativeMapped == null) {
                    clsClass$2 = CallbackReference.class$("com.sun.jna.NativeMapped");
                    CallbackReference.class$com$sun$jna$NativeMapped = clsClass$2;
                } else {
                    clsClass$2 = CallbackReference.class$com$sun$jna$NativeMapped;
                }
                if (clsClass$2.isAssignableFrom(argTypes[i])) {
                    this.fromNative[i] = new NativeMappedConverter(argTypes[i]);
                } else if (mapper != null) {
                    this.fromNative[i] = mapper.getFromNativeConverter(argTypes[i]);
                }
            }
            if (!callbackMethod.isAccessible()) {
                try {
                    callbackMethod.setAccessible(true);
                } catch (SecurityException e) {
                    throw new IllegalArgumentException(new StringBuffer().append("Callback method is inaccessible, make sure the interface is public: ").append(callbackMethod).toString());
                }
            }
        }

        private Object callback_inner(Object[] args) throws Throwable {
            Class[] paramTypes = this.callbackMethod.getParameterTypes();
            Object[] callbackArgs = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                Class type = paramTypes[i];
                Object arg = args[i];
                if (this.fromNative[i] != null) {
                    FromNativeContext context = new CallbackParameterContext(type, this.callbackMethod, args, i);
                    arg = this.fromNative[i].fromNative(arg, context);
                }
                callbackArgs[i] = convertArgument(arg, type);
            }
            Object result = null;
            Callback cb = this.this$0.getCallback();
            if (cb != null) {
                try {
                    result = convertResult(this.callbackMethod.invoke(cb, callbackArgs));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e2) {
                    e2.printStackTrace();
                } catch (InvocationTargetException e3) {
                    e3.printStackTrace();
                }
            }
            return result;
        }

        @Override // com.sun.jna.CallbackProxy
        public Object callback(Object[] args) {
            try {
                return callback_inner(args);
            } catch (Throwable t) {
                t.printStackTrace();
                return null;
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:26:0x008b  */
        /* JADX WARN: Removed duplicated region for block: B:63:0x015b  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        private java.lang.Object convertArgument(java.lang.Object r8, java.lang.Class r9) throws java.lang.Throwable {
            /*
                Method dump skipped, instructions count: 375
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.sun.jna.CallbackReference.DefaultCallbackProxy.convertArgument(java.lang.Object, java.lang.Class):java.lang.Object");
        }

        private Object convertResult(Object value) throws Throwable {
            Class clsClass$;
            Class clsClass$2;
            Class clsClass$3;
            Class clsClass$4;
            Class clsClass$5;
            Class clsClass$6;
            Class clsClass$7;
            Class clsClass$8;
            Class clsClass$9;
            Class clsClass$10;
            if (this.toNative != null) {
                value = this.toNative.toNative(value, new CallbackResultContext(this.callbackMethod));
            }
            if (value == null) {
                return null;
            }
            Class cls = value.getClass();
            if (CallbackReference.class$com$sun$jna$Structure == null) {
                clsClass$ = CallbackReference.class$("com.sun.jna.Structure");
                CallbackReference.class$com$sun$jna$Structure = clsClass$;
            } else {
                clsClass$ = CallbackReference.class$com$sun$jna$Structure;
            }
            if (clsClass$.isAssignableFrom(cls)) {
                if (CallbackReference.class$com$sun$jna$Structure$ByValue == null) {
                    clsClass$10 = CallbackReference.class$("com.sun.jna.Structure$ByValue");
                    CallbackReference.class$com$sun$jna$Structure$ByValue = clsClass$10;
                } else {
                    clsClass$10 = CallbackReference.class$com$sun$jna$Structure$ByValue;
                }
                if (clsClass$10.isAssignableFrom(cls)) {
                    return value;
                }
                return ((Structure) value).getPointer();
            }
            if (cls != Boolean.TYPE) {
                if (CallbackReference.class$java$lang$Boolean == null) {
                    clsClass$2 = CallbackReference.class$("java.lang.Boolean");
                    CallbackReference.class$java$lang$Boolean = clsClass$2;
                } else {
                    clsClass$2 = CallbackReference.class$java$lang$Boolean;
                }
                if (cls != clsClass$2) {
                    if (CallbackReference.class$java$lang$String == null) {
                        clsClass$3 = CallbackReference.class$("java.lang.String");
                        CallbackReference.class$java$lang$String = clsClass$3;
                    } else {
                        clsClass$3 = CallbackReference.class$java$lang$String;
                    }
                    if (cls != clsClass$3) {
                        if (CallbackReference.class$com$sun$jna$WString == null) {
                            clsClass$5 = CallbackReference.class$("com.sun.jna.WString");
                            CallbackReference.class$com$sun$jna$WString = clsClass$5;
                        } else {
                            clsClass$5 = CallbackReference.class$com$sun$jna$WString;
                        }
                        if (cls != clsClass$5) {
                            if (CallbackReference.array$Ljava$lang$String == null) {
                                clsClass$6 = CallbackReference.class$("[Ljava.lang.String;");
                                CallbackReference.array$Ljava$lang$String = clsClass$6;
                            } else {
                                clsClass$6 = CallbackReference.array$Ljava$lang$String;
                            }
                            if (cls != clsClass$6) {
                                if (CallbackReference.class$com$sun$jna$WString == null) {
                                    clsClass$8 = CallbackReference.class$("com.sun.jna.WString");
                                    CallbackReference.class$com$sun$jna$WString = clsClass$8;
                                } else {
                                    clsClass$8 = CallbackReference.class$com$sun$jna$WString;
                                }
                                if (cls != clsClass$8) {
                                    if (CallbackReference.class$com$sun$jna$Callback == null) {
                                        clsClass$9 = CallbackReference.class$("com.sun.jna.Callback");
                                        CallbackReference.class$com$sun$jna$Callback = clsClass$9;
                                    } else {
                                        clsClass$9 = CallbackReference.class$com$sun$jna$Callback;
                                    }
                                    if (clsClass$9.isAssignableFrom(cls)) {
                                        return CallbackReference.getFunctionPointer((Callback) value);
                                    }
                                    return value;
                                }
                            }
                            if (CallbackReference.array$Ljava$lang$String == null) {
                                clsClass$7 = CallbackReference.class$("[Ljava.lang.String;");
                                CallbackReference.array$Ljava$lang$String = clsClass$7;
                            } else {
                                clsClass$7 = CallbackReference.array$Ljava$lang$String;
                            }
                            StringArray sa = cls == clsClass$7 ? new StringArray((String[]) value) : new StringArray((WString[]) value);
                            CallbackReference.allocations.put(value, sa);
                            return sa;
                        }
                    }
                    String string = value.toString();
                    if (CallbackReference.class$com$sun$jna$WString == null) {
                        clsClass$4 = CallbackReference.class$("com.sun.jna.WString");
                        CallbackReference.class$com$sun$jna$WString = clsClass$4;
                    } else {
                        clsClass$4 = CallbackReference.class$com$sun$jna$WString;
                    }
                    NativeString ns = new NativeString(string, cls == clsClass$4);
                    CallbackReference.allocations.put(value, ns);
                    return ns.getPointer();
                }
            }
            return Boolean.TRUE.equals(value) ? Function.INTEGER_TRUE : Function.INTEGER_FALSE;
        }

        @Override // com.sun.jna.CallbackProxy
        public Class[] getParameterTypes() {
            return this.callbackMethod.getParameterTypes();
        }

        @Override // com.sun.jna.CallbackProxy
        public Class getReturnType() {
            return this.callbackMethod.getReturnType();
        }
    }

    /* loaded from: jna-3.0.9.jar:com/sun/jna/CallbackReference$NativeFunctionHandler.class */
    private static class NativeFunctionHandler implements InvocationHandler {
        private Function function;
        private Map options = new HashMap();

        public NativeFunctionHandler(Pointer address, int callingConvention, Map libOptions) {
            this.function = new Function(this, address, callingConvention) { // from class: com.sun.jna.CallbackReference.NativeFunctionHandler.1
                private final NativeFunctionHandler this$0;

                {
                    this.this$0 = this;
                }

                @Override // com.sun.jna.Function
                public String getName() throws Throwable {
                    String str = super.getName();
                    if (this.this$0.options.containsKey("invoking-method")) {
                        Method m = (Method) this.this$0.options.get("invoking-method");
                        Class cls = Native.findCallbackClass(m.getDeclaringClass());
                        str = new StringBuffer().append(str).append(" (").append(cls.getName()).append(")").toString();
                    }
                    return str;
                }
            };
            if (libOptions != null) {
                this.options.putAll(libOptions);
            }
        }

        @Override // java.lang.reflect.InvocationHandler
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (Library.Handler.OBJECT_TOSTRING.equals(method)) {
                return new StringBuffer().append("Proxy interface to ").append(this.function).toString();
            }
            if (Library.Handler.OBJECT_HASHCODE.equals(method)) {
                return new Integer(hashCode());
            }
            if (Library.Handler.OBJECT_EQUALS.equals(method)) {
                Object o = args[0];
                if (o == null || !Proxy.isProxyClass(o.getClass())) {
                    return Boolean.FALSE;
                }
                return Function.valueOf(Proxy.getInvocationHandler(o) == this);
            }
            if (Function.isVarArgs(method)) {
                args = Function.concatenateVarArgs(args);
            }
            return this.function.invoke(method.getReturnType(), args, this.options);
        }

        public Pointer getPointer() {
            return this.function;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:77:0x0158  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static boolean isAllowableNativeType(java.lang.Class r4) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 378
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.jna.CallbackReference.isAllowableNativeType(java.lang.Class):boolean");
    }
}

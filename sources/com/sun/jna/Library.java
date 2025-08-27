package com.sun.jna;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import org.springframework.jmx.export.naming.IdentityNamingStrategy;

/* loaded from: jna-3.0.9.jar:com/sun/jna/Library.class */
public interface Library {
    public static final String OPTION_TYPE_MAPPER = "type-mapper";
    public static final String OPTION_FUNCTION_MAPPER = "function-mapper";
    public static final String OPTION_INVOCATION_MAPPER = "invocation-mapper";
    public static final String OPTION_STRUCTURE_ALIGNMENT = "structure-alignment";

    /* renamed from: com.sun.jna.Library$1, reason: invalid class name */
    /* loaded from: jna-3.0.9.jar:com/sun/jna/Library$1.class */
    static class AnonymousClass1 {
        static Class class$java$lang$Object;
        static Class class$com$sun$jna$AltCallingConvention;

        static Class class$(String x0) throws Throwable {
            try {
                return Class.forName(x0);
            } catch (ClassNotFoundException x1) {
                throw new NoClassDefFoundError().initCause(x1);
            }
        }
    }

    /* loaded from: jna-3.0.9.jar:com/sun/jna/Library$Handler.class */
    public static class Handler implements InvocationHandler {
        static final Method OBJECT_TOSTRING;
        static final Method OBJECT_HASHCODE;
        static final Method OBJECT_EQUALS;
        private final NativeLibrary nativeLibrary;
        private final Class interfaceClass;
        private final Map options;
        private FunctionMapper functionMapper;
        private InvocationMapper invocationMapper;
        private final Map functions = new WeakHashMap();
        private int callingConvention;

        static {
            Class clsClass$;
            Class clsClass$2;
            Class clsClass$3;
            Class<?> clsClass$4;
            try {
                if (AnonymousClass1.class$java$lang$Object == null) {
                    clsClass$ = AnonymousClass1.class$("java.lang.Object");
                    AnonymousClass1.class$java$lang$Object = clsClass$;
                } else {
                    clsClass$ = AnonymousClass1.class$java$lang$Object;
                }
                OBJECT_TOSTRING = clsClass$.getMethod("toString", new Class[0]);
                if (AnonymousClass1.class$java$lang$Object == null) {
                    clsClass$2 = AnonymousClass1.class$("java.lang.Object");
                    AnonymousClass1.class$java$lang$Object = clsClass$2;
                } else {
                    clsClass$2 = AnonymousClass1.class$java$lang$Object;
                }
                OBJECT_HASHCODE = clsClass$2.getMethod(IdentityNamingStrategy.HASH_CODE_KEY, new Class[0]);
                if (AnonymousClass1.class$java$lang$Object == null) {
                    clsClass$3 = AnonymousClass1.class$("java.lang.Object");
                    AnonymousClass1.class$java$lang$Object = clsClass$3;
                } else {
                    clsClass$3 = AnonymousClass1.class$java$lang$Object;
                }
                Class<?>[] clsArr = new Class[1];
                if (AnonymousClass1.class$java$lang$Object == null) {
                    clsClass$4 = AnonymousClass1.class$("java.lang.Object");
                    AnonymousClass1.class$java$lang$Object = clsClass$4;
                } else {
                    clsClass$4 = AnonymousClass1.class$java$lang$Object;
                }
                clsArr[0] = clsClass$4;
                OBJECT_EQUALS = clsClass$3.getMethod("equals", clsArr);
            } catch (Exception e) {
                throw new Error("Error retrieving Object.toString() method");
            }
        }

        /* loaded from: jna-3.0.9.jar:com/sun/jna/Library$Handler$FunctionNameMap.class */
        private static class FunctionNameMap implements FunctionMapper {
            private final Map map;

            public FunctionNameMap(Map map) {
                this.map = new HashMap(map);
            }

            @Override // com.sun.jna.FunctionMapper
            public String getFunctionName(NativeLibrary library, Method method) {
                String name = method.getName();
                if (this.map.containsKey(name)) {
                    return (String) this.map.get(name);
                }
                return name;
            }
        }

        public Handler(String libname, Class interfaceClass, Map options) throws Throwable {
            Class clsClass$;
            if (libname == null || "".equals(libname.trim())) {
                throw new IllegalArgumentException(new StringBuffer().append("Invalid library name \"").append(libname).append(SymbolConstants.QUOTES_SYMBOL).toString());
            }
            this.nativeLibrary = NativeLibrary.getInstance(libname);
            this.interfaceClass = interfaceClass;
            this.options = options;
            if (AnonymousClass1.class$com$sun$jna$AltCallingConvention == null) {
                clsClass$ = AnonymousClass1.class$("com.sun.jna.AltCallingConvention");
                AnonymousClass1.class$com$sun$jna$AltCallingConvention = clsClass$;
            } else {
                clsClass$ = AnonymousClass1.class$com$sun$jna$AltCallingConvention;
            }
            this.callingConvention = clsClass$.isAssignableFrom(interfaceClass) ? 1 : 0;
            this.functionMapper = (FunctionMapper) options.get(Library.OPTION_FUNCTION_MAPPER);
            if (this.functionMapper == null) {
                this.functionMapper = new FunctionNameMap(options);
            }
            this.invocationMapper = (InvocationMapper) options.get(Library.OPTION_INVOCATION_MAPPER);
        }

        public NativeLibrary getNativeLibrary() {
            return this.nativeLibrary;
        }

        public String getLibraryName() {
            return this.nativeLibrary.getName();
        }

        public Class getInterfaceClass() {
            return this.interfaceClass;
        }

        /* loaded from: jna-3.0.9.jar:com/sun/jna/Library$Handler$FunctionInfo.class */
        private static class FunctionInfo {
            InvocationHandler handler;
            Function function;
            boolean isVarArgs;
            Map options;

            private FunctionInfo() {
            }

            FunctionInfo(AnonymousClass1 x0) {
                this();
            }
        }

        @Override // java.lang.reflect.InvocationHandler
        public Object invoke(Object proxy, Method method, Object[] inArgs) throws Throwable {
            FunctionInfo f;
            if (OBJECT_TOSTRING.equals(method)) {
                return new StringBuffer().append("Proxy interface to ").append(this.nativeLibrary).toString();
            }
            if (OBJECT_HASHCODE.equals(method)) {
                return new Integer(hashCode());
            }
            if (OBJECT_EQUALS.equals(method)) {
                Object o = inArgs[0];
                if (o == null || !Proxy.isProxyClass(o.getClass())) {
                    return Boolean.FALSE;
                }
                return Function.valueOf(Proxy.getInvocationHandler(o) == this);
            }
            synchronized (this.functions) {
                f = (FunctionInfo) this.functions.get(method);
                if (f == null) {
                    f = new FunctionInfo(null);
                    f.isVarArgs = Function.isVarArgs(method);
                    if (this.invocationMapper != null) {
                        f.handler = this.invocationMapper.getInvocationHandler(this.nativeLibrary, method);
                    }
                    if (f.handler == null) {
                        String methodName = this.functionMapper.getFunctionName(this.nativeLibrary, method);
                        if (methodName == null) {
                            methodName = method.getName();
                        }
                        f.function = this.nativeLibrary.getFunction(methodName, this.callingConvention);
                        f.options = new HashMap(this.options);
                        f.options.put("invoking-method", method);
                    }
                    this.functions.put(method, f);
                }
            }
            if (f.isVarArgs) {
                inArgs = Function.concatenateVarArgs(inArgs);
            }
            if (f.handler != null) {
                return f.handler.invoke(proxy, method, inArgs);
            }
            return f.function.invoke(method.getReturnType(), inArgs, f.options);
        }
    }
}

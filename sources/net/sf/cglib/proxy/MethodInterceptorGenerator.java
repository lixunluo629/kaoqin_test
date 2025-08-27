package net.sf.cglib.proxy;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.cglib.core.ClassEmitter;
import net.sf.cglib.core.ClassInfo;
import net.sf.cglib.core.CodeEmitter;
import net.sf.cglib.core.CollectionUtils;
import net.sf.cglib.core.Constants;
import net.sf.cglib.core.EmitUtils;
import net.sf.cglib.core.Local;
import net.sf.cglib.core.MethodInfo;
import net.sf.cglib.core.ObjectSwitchCallback;
import net.sf.cglib.core.Signature;
import net.sf.cglib.core.Transformer;
import net.sf.cglib.core.TypeUtils;
import net.sf.cglib.proxy.CallbackGenerator;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;

/* loaded from: cglib-3.1.jar:net/sf/cglib/proxy/MethodInterceptorGenerator.class */
class MethodInterceptorGenerator implements CallbackGenerator {
    public static final MethodInterceptorGenerator INSTANCE = new MethodInterceptorGenerator();
    static final String EMPTY_ARGS_NAME = "CGLIB$emptyArgs";
    static final String FIND_PROXY_NAME = "CGLIB$findMethodProxy";
    static final Class[] FIND_PROXY_TYPES;
    private static final Type ABSTRACT_METHOD_ERROR;
    private static final Type METHOD;
    private static final Type REFLECT_UTILS;
    private static final Type METHOD_PROXY;
    private static final Type METHOD_INTERCEPTOR;
    private static final Signature GET_DECLARED_METHODS;
    private static final Signature GET_DECLARING_CLASS;
    private static final Signature FIND_METHODS;
    private static final Signature MAKE_PROXY;
    private static final Signature INTERCEPT;
    private static final Signature FIND_PROXY;
    private static final Signature TO_STRING;
    private static final Transformer METHOD_TO_CLASS;
    private static final Signature CSTRUCT_SIGNATURE;
    static Class class$net$sf$cglib$core$Signature;

    MethodInterceptorGenerator() {
    }

    static {
        Class clsClass$;
        Class[] clsArr = new Class[1];
        if (class$net$sf$cglib$core$Signature == null) {
            clsClass$ = class$("net.sf.cglib.core.Signature");
            class$net$sf$cglib$core$Signature = clsClass$;
        } else {
            clsClass$ = class$net$sf$cglib$core$Signature;
        }
        clsArr[0] = clsClass$;
        FIND_PROXY_TYPES = clsArr;
        ABSTRACT_METHOD_ERROR = TypeUtils.parseType("AbstractMethodError");
        METHOD = TypeUtils.parseType("java.lang.reflect.Method");
        REFLECT_UTILS = TypeUtils.parseType("net.sf.cglib.core.ReflectUtils");
        METHOD_PROXY = TypeUtils.parseType("net.sf.cglib.proxy.MethodProxy");
        METHOD_INTERCEPTOR = TypeUtils.parseType("net.sf.cglib.proxy.MethodInterceptor");
        GET_DECLARED_METHODS = TypeUtils.parseSignature("java.lang.reflect.Method[] getDeclaredMethods()");
        GET_DECLARING_CLASS = TypeUtils.parseSignature("Class getDeclaringClass()");
        FIND_METHODS = TypeUtils.parseSignature("java.lang.reflect.Method[] findMethods(String[], java.lang.reflect.Method[])");
        MAKE_PROXY = new Signature("create", METHOD_PROXY, new Type[]{Constants.TYPE_CLASS, Constants.TYPE_CLASS, Constants.TYPE_STRING, Constants.TYPE_STRING, Constants.TYPE_STRING});
        INTERCEPT = new Signature("intercept", Constants.TYPE_OBJECT, new Type[]{Constants.TYPE_OBJECT, METHOD, Constants.TYPE_OBJECT_ARRAY, METHOD_PROXY});
        FIND_PROXY = new Signature(FIND_PROXY_NAME, METHOD_PROXY, new Type[]{Constants.TYPE_SIGNATURE});
        TO_STRING = TypeUtils.parseSignature("String toString()");
        METHOD_TO_CLASS = new Transformer() { // from class: net.sf.cglib.proxy.MethodInterceptorGenerator.1
            @Override // net.sf.cglib.core.Transformer
            public Object transform(Object value) {
                return ((MethodInfo) value).getClassInfo();
            }
        };
        CSTRUCT_SIGNATURE = TypeUtils.parseConstructor("String, String");
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    private String getMethodField(Signature impl) {
        return new StringBuffer().append(impl.getName()).append("$Method").toString();
    }

    private String getMethodProxyField(Signature impl) {
        return new StringBuffer().append(impl.getName()).append("$Proxy").toString();
    }

    @Override // net.sf.cglib.proxy.CallbackGenerator
    public void generate(ClassEmitter ce, CallbackGenerator.Context context, List methods) {
        Map sigMap = new HashMap();
        Iterator it = methods.iterator();
        while (it.hasNext()) {
            MethodInfo method = (MethodInfo) it.next();
            Signature sig = method.getSignature();
            Signature impl = context.getImplSignature(method);
            String methodField = getMethodField(impl);
            String methodProxyField = getMethodProxyField(impl);
            sigMap.put(sig.toString(), methodProxyField);
            ce.declare_field(26, methodField, METHOD, null);
            ce.declare_field(26, methodProxyField, METHOD_PROXY, null);
            ce.declare_field(26, EMPTY_ARGS_NAME, Constants.TYPE_OBJECT_ARRAY, null);
            CodeEmitter e = ce.begin_method(16, impl, method.getExceptionTypes());
            superHelper(e, method, context);
            e.return_value();
            e.end_method();
            CodeEmitter e2 = context.beginMethod(ce, method);
            Label nullInterceptor = e2.make_label();
            context.emitCallback(e2, context.getIndex(method));
            e2.dup();
            e2.ifnull(nullInterceptor);
            e2.load_this();
            e2.getfield(methodField);
            if (sig.getArgumentTypes().length == 0) {
                e2.getfield(EMPTY_ARGS_NAME);
            } else {
                e2.create_arg_array();
            }
            e2.getfield(methodProxyField);
            e2.invoke_interface(METHOD_INTERCEPTOR, INTERCEPT);
            e2.unbox_or_zero(sig.getReturnType());
            e2.return_value();
            e2.mark(nullInterceptor);
            superHelper(e2, method, context);
            e2.return_value();
            e2.end_method();
        }
        generateFindProxy(ce, sigMap);
    }

    private static void superHelper(CodeEmitter e, MethodInfo method, CallbackGenerator.Context context) {
        if (TypeUtils.isAbstract(method.getModifiers())) {
            e.throw_exception(ABSTRACT_METHOD_ERROR, new StringBuffer().append(method.toString()).append(" is abstract").toString());
            return;
        }
        e.load_this();
        e.load_args();
        context.emitInvoke(e, method);
    }

    @Override // net.sf.cglib.proxy.CallbackGenerator
    public void generateStatic(CodeEmitter e, CallbackGenerator.Context context, List methods) throws Exception {
        e.push(0);
        e.newarray();
        e.putfield(EMPTY_ARGS_NAME);
        Local thisclass = e.make_local();
        Local declaringclass = e.make_local();
        EmitUtils.load_class_this(e);
        e.store_local(thisclass);
        Map methodsByClass = CollectionUtils.bucket(methods, METHOD_TO_CLASS);
        for (ClassInfo classInfo : methodsByClass.keySet()) {
            List classMethods = (List) methodsByClass.get(classInfo);
            e.push(2 * classMethods.size());
            e.newarray(Constants.TYPE_STRING);
            for (int index = 0; index < classMethods.size(); index++) {
                Signature sig = ((MethodInfo) classMethods.get(index)).getSignature();
                e.dup();
                e.push(2 * index);
                e.push(sig.getName());
                e.aastore();
                e.dup();
                e.push((2 * index) + 1);
                e.push(sig.getDescriptor());
                e.aastore();
            }
            EmitUtils.load_class(e, classInfo.getType());
            e.dup();
            e.store_local(declaringclass);
            e.invoke_virtual(Constants.TYPE_CLASS, GET_DECLARED_METHODS);
            e.invoke_static(REFLECT_UTILS, FIND_METHODS);
            for (int index2 = 0; index2 < classMethods.size(); index2++) {
                MethodInfo method = (MethodInfo) classMethods.get(index2);
                Signature sig2 = method.getSignature();
                Signature impl = context.getImplSignature(method);
                e.dup();
                e.push(index2);
                e.array_load(METHOD);
                e.putfield(getMethodField(impl));
                e.load_local(declaringclass);
                e.load_local(thisclass);
                e.push(sig2.getDescriptor());
                e.push(sig2.getName());
                e.push(impl.getName());
                e.invoke_static(METHOD_PROXY, MAKE_PROXY);
                e.putfield(getMethodProxyField(impl));
            }
            e.pop();
        }
    }

    public void generateFindProxy(ClassEmitter ce, Map sigMap) {
        CodeEmitter e = ce.begin_method(9, FIND_PROXY, null);
        e.load_arg(0);
        e.invoke_virtual(Constants.TYPE_OBJECT, TO_STRING);
        ObjectSwitchCallback callback = new ObjectSwitchCallback(this, e, sigMap) { // from class: net.sf.cglib.proxy.MethodInterceptorGenerator.2
            private final CodeEmitter val$e;
            private final Map val$sigMap;
            private final MethodInterceptorGenerator this$0;

            {
                this.this$0 = this;
                this.val$e = e;
                this.val$sigMap = sigMap;
            }

            @Override // net.sf.cglib.core.ObjectSwitchCallback
            public void processCase(Object key, Label end) {
                this.val$e.getfield((String) this.val$sigMap.get(key));
                this.val$e.return_value();
            }

            @Override // net.sf.cglib.core.ObjectSwitchCallback
            public void processDefault() {
                this.val$e.aconst_null();
                this.val$e.return_value();
            }
        };
        EmitUtils.string_switch(e, (String[]) sigMap.keySet().toArray(new String[0]), 1, callback);
        e.end_method();
    }
}

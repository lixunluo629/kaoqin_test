package net.sf.cglib.proxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sf.cglib.core.AbstractClassGenerator;
import net.sf.cglib.core.ClassEmitter;
import net.sf.cglib.core.CodeEmitter;
import net.sf.cglib.core.CodeGenerationException;
import net.sf.cglib.core.CollectionUtils;
import net.sf.cglib.core.Constants;
import net.sf.cglib.core.DuplicatesPredicate;
import net.sf.cglib.core.EmitUtils;
import net.sf.cglib.core.KeyFactory;
import net.sf.cglib.core.Local;
import net.sf.cglib.core.MethodInfo;
import net.sf.cglib.core.MethodInfoTransformer;
import net.sf.cglib.core.MethodWrapper;
import net.sf.cglib.core.ObjectSwitchCallback;
import net.sf.cglib.core.ProcessSwitchCallback;
import net.sf.cglib.core.ReflectUtils;
import net.sf.cglib.core.RejectModifierPredicate;
import net.sf.cglib.core.Signature;
import net.sf.cglib.core.Transformer;
import net.sf.cglib.core.TypeUtils;
import net.sf.cglib.core.VisibilityPredicate;
import net.sf.cglib.proxy.CallbackGenerator;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;

/* loaded from: cglib-3.1.jar:net/sf/cglib/proxy/Enhancer.class */
public class Enhancer extends AbstractClassGenerator {
    private static final CallbackFilter ALL_ZERO = new CallbackFilter() { // from class: net.sf.cglib.proxy.Enhancer.1
        @Override // net.sf.cglib.proxy.CallbackFilter
        public int accept(Method method) {
            return 0;
        }
    };
    private static final AbstractClassGenerator.Source SOURCE;
    private static final EnhancerKey KEY_FACTORY;
    private static final String BOUND_FIELD = "CGLIB$BOUND";
    private static final String THREAD_CALLBACKS_FIELD = "CGLIB$THREAD_CALLBACKS";
    private static final String STATIC_CALLBACKS_FIELD = "CGLIB$STATIC_CALLBACKS";
    private static final String SET_THREAD_CALLBACKS_NAME = "CGLIB$SET_THREAD_CALLBACKS";
    private static final String SET_STATIC_CALLBACKS_NAME = "CGLIB$SET_STATIC_CALLBACKS";
    private static final String CONSTRUCTED_FIELD = "CGLIB$CONSTRUCTED";
    private static final Type FACTORY;
    private static final Type ILLEGAL_STATE_EXCEPTION;
    private static final Type ILLEGAL_ARGUMENT_EXCEPTION;
    private static final Type THREAD_LOCAL;
    private static final Type CALLBACK;
    private static final Type CALLBACK_ARRAY;
    private static final Signature CSTRUCT_NULL;
    private static final Signature SET_THREAD_CALLBACKS;
    private static final Signature SET_STATIC_CALLBACKS;
    private static final Signature NEW_INSTANCE;
    private static final Signature MULTIARG_NEW_INSTANCE;
    private static final Signature SINGLE_NEW_INSTANCE;
    private static final Signature SET_CALLBACK;
    private static final Signature GET_CALLBACK;
    private static final Signature SET_CALLBACKS;
    private static final Signature GET_CALLBACKS;
    private static final Signature THREAD_LOCAL_GET;
    private static final Signature THREAD_LOCAL_SET;
    private static final Signature BIND_CALLBACKS;
    private Class[] interfaces;
    private CallbackFilter filter;
    private Callback[] callbacks;
    private Type[] callbackTypes;
    private boolean classOnly;
    private Class superclass;
    private Class[] argumentTypes;
    private Object[] arguments;
    private boolean useFactory;
    private Long serialVersionUID;
    private boolean interceptDuringConstruction;
    static Class class$net$sf$cglib$proxy$Enhancer;
    static Class class$net$sf$cglib$proxy$Enhancer$EnhancerKey;
    static Class array$Lnet$sf$cglib$proxy$Callback;
    static Class class$java$lang$Object;
    static Class class$net$sf$cglib$proxy$Factory;

    /* loaded from: cglib-3.1.jar:net/sf/cglib/proxy/Enhancer$EnhancerKey.class */
    public interface EnhancerKey {
        Object newInstance(String str, String[] strArr, CallbackFilter callbackFilter, Type[] typeArr, boolean z, boolean z2, Long l);
    }

    static {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        if (class$net$sf$cglib$proxy$Enhancer == null) {
            clsClass$ = class$("net.sf.cglib.proxy.Enhancer");
            class$net$sf$cglib$proxy$Enhancer = clsClass$;
        } else {
            clsClass$ = class$net$sf$cglib$proxy$Enhancer;
        }
        SOURCE = new AbstractClassGenerator.Source(clsClass$.getName());
        if (class$net$sf$cglib$proxy$Enhancer$EnhancerKey == null) {
            clsClass$2 = class$("net.sf.cglib.proxy.Enhancer$EnhancerKey");
            class$net$sf$cglib$proxy$Enhancer$EnhancerKey = clsClass$2;
        } else {
            clsClass$2 = class$net$sf$cglib$proxy$Enhancer$EnhancerKey;
        }
        KEY_FACTORY = (EnhancerKey) KeyFactory.create(clsClass$2);
        FACTORY = TypeUtils.parseType("net.sf.cglib.proxy.Factory");
        ILLEGAL_STATE_EXCEPTION = TypeUtils.parseType("IllegalStateException");
        ILLEGAL_ARGUMENT_EXCEPTION = TypeUtils.parseType("IllegalArgumentException");
        THREAD_LOCAL = TypeUtils.parseType("ThreadLocal");
        CALLBACK = TypeUtils.parseType("net.sf.cglib.proxy.Callback");
        if (array$Lnet$sf$cglib$proxy$Callback == null) {
            clsClass$3 = class$("[Lnet.sf.cglib.proxy.Callback;");
            array$Lnet$sf$cglib$proxy$Callback = clsClass$3;
        } else {
            clsClass$3 = array$Lnet$sf$cglib$proxy$Callback;
        }
        CALLBACK_ARRAY = Type.getType((Class<?>) clsClass$3);
        CSTRUCT_NULL = TypeUtils.parseConstructor("");
        SET_THREAD_CALLBACKS = new Signature(SET_THREAD_CALLBACKS_NAME, Type.VOID_TYPE, new Type[]{CALLBACK_ARRAY});
        SET_STATIC_CALLBACKS = new Signature(SET_STATIC_CALLBACKS_NAME, Type.VOID_TYPE, new Type[]{CALLBACK_ARRAY});
        NEW_INSTANCE = new Signature("newInstance", Constants.TYPE_OBJECT, new Type[]{CALLBACK_ARRAY});
        MULTIARG_NEW_INSTANCE = new Signature("newInstance", Constants.TYPE_OBJECT, new Type[]{Constants.TYPE_CLASS_ARRAY, Constants.TYPE_OBJECT_ARRAY, CALLBACK_ARRAY});
        SINGLE_NEW_INSTANCE = new Signature("newInstance", Constants.TYPE_OBJECT, new Type[]{CALLBACK});
        SET_CALLBACK = new Signature("setCallback", Type.VOID_TYPE, new Type[]{Type.INT_TYPE, CALLBACK});
        GET_CALLBACK = new Signature("getCallback", CALLBACK, new Type[]{Type.INT_TYPE});
        SET_CALLBACKS = new Signature("setCallbacks", Type.VOID_TYPE, new Type[]{CALLBACK_ARRAY});
        GET_CALLBACKS = new Signature("getCallbacks", CALLBACK_ARRAY, new Type[0]);
        THREAD_LOCAL_GET = TypeUtils.parseSignature("Object get()");
        THREAD_LOCAL_SET = TypeUtils.parseSignature("void set(Object)");
        BIND_CALLBACKS = TypeUtils.parseSignature("void CGLIB$BIND_CALLBACKS(Object)");
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    public Enhancer() {
        super(SOURCE);
        this.useFactory = true;
        this.interceptDuringConstruction = true;
    }

    public void setSuperclass(Class superclass) {
        Class clsClass$;
        if (superclass != null && superclass.isInterface()) {
            setInterfaces(new Class[]{superclass});
            return;
        }
        if (superclass != null) {
            if (class$java$lang$Object == null) {
                clsClass$ = class$("java.lang.Object");
                class$java$lang$Object = clsClass$;
            } else {
                clsClass$ = class$java$lang$Object;
            }
            if (superclass.equals(clsClass$)) {
                this.superclass = null;
                return;
            }
        }
        this.superclass = superclass;
    }

    public void setInterfaces(Class[] interfaces) {
        this.interfaces = interfaces;
    }

    public void setCallbackFilter(CallbackFilter filter) {
        this.filter = filter;
    }

    public void setCallback(Callback callback) {
        setCallbacks(new Callback[]{callback});
    }

    public void setCallbacks(Callback[] callbacks) {
        if (callbacks != null && callbacks.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty");
        }
        this.callbacks = callbacks;
    }

    public void setUseFactory(boolean useFactory) {
        this.useFactory = useFactory;
    }

    public void setInterceptDuringConstruction(boolean interceptDuringConstruction) {
        this.interceptDuringConstruction = interceptDuringConstruction;
    }

    public void setCallbackType(Class callbackType) {
        setCallbackTypes(new Class[]{callbackType});
    }

    public void setCallbackTypes(Class[] callbackTypes) {
        if (callbackTypes != null && callbackTypes.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty");
        }
        this.callbackTypes = CallbackInfo.determineTypes(callbackTypes);
    }

    public Object create() {
        this.classOnly = false;
        this.argumentTypes = null;
        return createHelper();
    }

    public Object create(Class[] argumentTypes, Object[] arguments) {
        this.classOnly = false;
        if (argumentTypes == null || arguments == null || argumentTypes.length != arguments.length) {
            throw new IllegalArgumentException("Arguments must be non-null and of equal length");
        }
        this.argumentTypes = argumentTypes;
        this.arguments = arguments;
        return createHelper();
    }

    public Class createClass() {
        this.classOnly = true;
        return (Class) createHelper();
    }

    public void setSerialVersionUID(Long sUID) {
        this.serialVersionUID = sUID;
    }

    private void validate() {
        if (this.classOnly ^ (this.callbacks == null)) {
            if (this.classOnly) {
                throw new IllegalStateException("createClass does not accept callbacks");
            }
            throw new IllegalStateException("Callbacks are required");
        }
        if (this.classOnly && this.callbackTypes == null) {
            throw new IllegalStateException("Callback types are required");
        }
        if (this.callbacks != null && this.callbackTypes != null) {
            if (this.callbacks.length != this.callbackTypes.length) {
                throw new IllegalStateException("Lengths of callback and callback types array must be the same");
            }
            Type[] check = CallbackInfo.determineTypes(this.callbacks);
            for (int i = 0; i < check.length; i++) {
                if (!check[i].equals(this.callbackTypes[i])) {
                    throw new IllegalStateException(new StringBuffer().append("Callback ").append(check[i]).append(" is not assignable to ").append(this.callbackTypes[i]).toString());
                }
            }
        } else if (this.callbacks != null) {
            this.callbackTypes = CallbackInfo.determineTypes(this.callbacks);
        }
        if (this.filter == null) {
            if (this.callbackTypes.length > 1) {
                throw new IllegalStateException("Multiple callback types possible but no filter specified");
            }
            this.filter = ALL_ZERO;
        }
        if (this.interfaces != null) {
            for (int i2 = 0; i2 < this.interfaces.length; i2++) {
                if (this.interfaces[i2] == null) {
                    throw new IllegalStateException("Interfaces cannot be null");
                }
                if (!this.interfaces[i2].isInterface()) {
                    throw new IllegalStateException(new StringBuffer().append(this.interfaces[i2]).append(" is not an interface").toString());
                }
            }
        }
    }

    private Object createHelper() {
        validate();
        if (this.superclass != null) {
            setNamePrefix(this.superclass.getName());
        } else if (this.interfaces != null) {
            setNamePrefix(this.interfaces[ReflectUtils.findPackageProtected(this.interfaces)].getName());
        }
        return super.create(KEY_FACTORY.newInstance(this.superclass != null ? this.superclass.getName() : null, ReflectUtils.getNames(this.interfaces), this.filter, this.callbackTypes, this.useFactory, this.interceptDuringConstruction, this.serialVersionUID));
    }

    @Override // net.sf.cglib.core.AbstractClassGenerator
    protected ClassLoader getDefaultClassLoader() {
        if (this.superclass != null) {
            return this.superclass.getClassLoader();
        }
        if (this.interfaces != null) {
            return this.interfaces[0].getClassLoader();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Signature rename(Signature sig, int index) {
        return new Signature(new StringBuffer().append("CGLIB$").append(sig.getName()).append(PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX).append(index).toString(), sig.getDescriptor());
    }

    public static void getMethods(Class superclass, Class[] interfaces, List methods) {
        getMethods(superclass, interfaces, methods, null, null);
    }

    private static void getMethods(Class superclass, Class[] interfaces, List methods, List interfaceMethods, Set forcePublic) {
        Class clsClass$;
        ReflectUtils.addAllMethods(superclass, methods);
        List target = interfaceMethods != null ? interfaceMethods : methods;
        if (interfaces != null) {
            for (int i = 0; i < interfaces.length; i++) {
                Class cls = interfaces[i];
                if (class$net$sf$cglib$proxy$Factory == null) {
                    clsClass$ = class$("net.sf.cglib.proxy.Factory");
                    class$net$sf$cglib$proxy$Factory = clsClass$;
                } else {
                    clsClass$ = class$net$sf$cglib$proxy$Factory;
                }
                if (cls != clsClass$) {
                    ReflectUtils.addAllMethods(interfaces[i], target);
                }
            }
        }
        if (interfaceMethods != null) {
            if (forcePublic != null) {
                forcePublic.addAll(MethodWrapper.createSet(interfaceMethods));
            }
            methods.addAll(interfaceMethods);
        }
        CollectionUtils.filter(methods, new RejectModifierPredicate(8));
        CollectionUtils.filter(methods, new VisibilityPredicate(superclass, true));
        CollectionUtils.filter(methods, new DuplicatesPredicate());
        CollectionUtils.filter(methods, new RejectModifierPredicate(16));
    }

    @Override // net.sf.cglib.core.ClassGenerator
    public void generateClass(ClassVisitor v) throws Exception {
        Class clsClass$;
        if (this.superclass != null) {
            clsClass$ = this.superclass;
        } else if (class$java$lang$Object == null) {
            clsClass$ = class$("java.lang.Object");
            class$java$lang$Object = clsClass$;
        } else {
            clsClass$ = class$java$lang$Object;
        }
        Class sc = clsClass$;
        if (TypeUtils.isFinal(sc.getModifiers())) {
            throw new IllegalArgumentException(new StringBuffer().append("Cannot subclass final class ").append(sc).toString());
        }
        List constructors = new ArrayList(Arrays.asList(sc.getDeclaredConstructors()));
        filterConstructors(sc, constructors);
        List actualMethods = new ArrayList();
        List interfaceMethods = new ArrayList();
        Set forcePublic = new HashSet();
        getMethods(sc, this.interfaces, actualMethods, interfaceMethods, forcePublic);
        List methods = CollectionUtils.transform(actualMethods, new Transformer(this, forcePublic) { // from class: net.sf.cglib.proxy.Enhancer.2
            private final Set val$forcePublic;
            private final Enhancer this$0;

            {
                this.this$0 = this;
                this.val$forcePublic = forcePublic;
            }

            @Override // net.sf.cglib.core.Transformer
            public Object transform(Object value) {
                Method method = (Method) value;
                int modifiers = 16 | (method.getModifiers() & (-1025) & (-257) & (-33));
                if (this.val$forcePublic.contains(MethodWrapper.create(method))) {
                    modifiers = (modifiers & (-5)) | 1;
                }
                return ReflectUtils.getMethodInfo(method, modifiers);
            }
        });
        ClassEmitter e = new ClassEmitter(v);
        e.begin_class(46, 1, getClassName(), Type.getType((Class<?>) sc), this.useFactory ? TypeUtils.add(TypeUtils.getTypes(this.interfaces), FACTORY) : TypeUtils.getTypes(this.interfaces), "<generated>");
        List constructorInfo = CollectionUtils.transform(constructors, MethodInfoTransformer.getInstance());
        e.declare_field(2, BOUND_FIELD, Type.BOOLEAN_TYPE, null);
        if (!this.interceptDuringConstruction) {
            e.declare_field(2, CONSTRUCTED_FIELD, Type.BOOLEAN_TYPE, null);
        }
        e.declare_field(26, THREAD_CALLBACKS_FIELD, THREAD_LOCAL, null);
        e.declare_field(26, STATIC_CALLBACKS_FIELD, CALLBACK_ARRAY, null);
        if (this.serialVersionUID != null) {
            e.declare_field(26, "serialVersionUID", Type.LONG_TYPE, this.serialVersionUID);
        }
        for (int i = 0; i < this.callbackTypes.length; i++) {
            e.declare_field(2, getCallbackField(i), this.callbackTypes[i], null);
        }
        emitMethods(e, methods, actualMethods);
        emitConstructors(e, constructorInfo);
        emitSetThreadCallbacks(e);
        emitSetStaticCallbacks(e);
        emitBindCallbacks(e);
        if (this.useFactory) {
            int[] keys = getCallbackKeys();
            emitNewInstanceCallbacks(e);
            emitNewInstanceCallback(e);
            emitNewInstanceMultiarg(e, constructorInfo);
            emitGetCallback(e, keys);
            emitSetCallback(e, keys);
            emitGetCallbacks(e);
            emitSetCallbacks(e);
        }
        e.end_class();
    }

    protected void filterConstructors(Class sc, List constructors) {
        CollectionUtils.filter(constructors, new VisibilityPredicate(sc, true));
        if (constructors.size() == 0) {
            throw new IllegalArgumentException(new StringBuffer().append("No visible constructors in ").append(sc).toString());
        }
    }

    @Override // net.sf.cglib.core.AbstractClassGenerator
    protected Object firstInstance(Class type) throws Exception {
        if (this.classOnly) {
            return type;
        }
        return createUsingReflection(type);
    }

    @Override // net.sf.cglib.core.AbstractClassGenerator
    protected Object nextInstance(Object instance) {
        Class protoclass = instance instanceof Class ? (Class) instance : instance.getClass();
        if (this.classOnly) {
            return protoclass;
        }
        if (instance instanceof Factory) {
            if (this.argumentTypes != null) {
                return ((Factory) instance).newInstance(this.argumentTypes, this.arguments, this.callbacks);
            }
            return ((Factory) instance).newInstance(this.callbacks);
        }
        return createUsingReflection(protoclass);
    }

    public static void registerCallbacks(Class generatedClass, Callback[] callbacks) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        setThreadCallbacks(generatedClass, callbacks);
    }

    public static void registerStaticCallbacks(Class generatedClass, Callback[] callbacks) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        setCallbacksHelper(generatedClass, callbacks, SET_STATIC_CALLBACKS_NAME);
    }

    public static boolean isEnhanced(Class type) {
        try {
            getCallbacksSetter(type, SET_THREAD_CALLBACKS_NAME);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    private static void setThreadCallbacks(Class type, Callback[] callbacks) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        setCallbacksHelper(type, callbacks, SET_THREAD_CALLBACKS_NAME);
    }

    private static void setCallbacksHelper(Class type, Callback[] callbacks, String methodName) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            Method setter = getCallbacksSetter(type, methodName);
            setter.invoke(null, callbacks);
        } catch (IllegalAccessException e) {
            throw new CodeGenerationException(e);
        } catch (NoSuchMethodException e2) {
            throw new IllegalArgumentException(new StringBuffer().append(type).append(" is not an enhanced class").toString());
        } catch (InvocationTargetException e3) {
            throw new CodeGenerationException(e3);
        }
    }

    private static Method getCallbacksSetter(Class type, String methodName) throws NoSuchMethodException {
        Class<?> clsClass$;
        Class<?>[] clsArr = new Class[1];
        if (array$Lnet$sf$cglib$proxy$Callback == null) {
            clsClass$ = class$("[Lnet.sf.cglib.proxy.Callback;");
            array$Lnet$sf$cglib$proxy$Callback = clsClass$;
        } else {
            clsClass$ = array$Lnet$sf$cglib$proxy$Callback;
        }
        clsArr[0] = clsClass$;
        return type.getDeclaredMethod(methodName, clsArr);
    }

    private Object createUsingReflection(Class type) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        setThreadCallbacks(type, this.callbacks);
        try {
            if (this.argumentTypes != null) {
                Object objNewInstance = ReflectUtils.newInstance(type, this.argumentTypes, this.arguments);
                setThreadCallbacks(type, null);
                return objNewInstance;
            }
            Object objNewInstance2 = ReflectUtils.newInstance(type);
            setThreadCallbacks(type, null);
            return objNewInstance2;
        } catch (Throwable th) {
            setThreadCallbacks(type, null);
            throw th;
        }
    }

    public static Object create(Class type, Callback callback) {
        Enhancer e = new Enhancer();
        e.setSuperclass(type);
        e.setCallback(callback);
        return e.create();
    }

    public static Object create(Class superclass, Class[] interfaces, Callback callback) {
        Enhancer e = new Enhancer();
        e.setSuperclass(superclass);
        e.setInterfaces(interfaces);
        e.setCallback(callback);
        return e.create();
    }

    public static Object create(Class superclass, Class[] interfaces, CallbackFilter filter, Callback[] callbacks) {
        Enhancer e = new Enhancer();
        e.setSuperclass(superclass);
        e.setInterfaces(interfaces);
        e.setCallbackFilter(filter);
        e.setCallbacks(callbacks);
        return e.create();
    }

    private void emitConstructors(ClassEmitter ce, List constructors) {
        boolean seenNull = false;
        Iterator it = constructors.iterator();
        while (it.hasNext()) {
            MethodInfo constructor = (MethodInfo) it.next();
            CodeEmitter e = EmitUtils.begin_method(ce, constructor, 1);
            e.load_this();
            e.dup();
            e.load_args();
            Signature sig = constructor.getSignature();
            seenNull = seenNull || sig.getDescriptor().equals("()V");
            e.super_invoke_constructor(sig);
            e.invoke_static_this(BIND_CALLBACKS);
            if (!this.interceptDuringConstruction) {
                e.load_this();
                e.push(1);
                e.putfield(CONSTRUCTED_FIELD);
            }
            e.return_value();
            e.end_method();
        }
        if (!this.classOnly && !seenNull && this.arguments == null) {
            throw new IllegalArgumentException("Superclass has no null constructors but no arguments were given");
        }
    }

    private int[] getCallbackKeys() {
        int[] keys = new int[this.callbackTypes.length];
        for (int i = 0; i < this.callbackTypes.length; i++) {
            keys[i] = i;
        }
        return keys;
    }

    private void emitGetCallback(ClassEmitter ce, int[] keys) {
        CodeEmitter e = ce.begin_method(1, GET_CALLBACK, null);
        e.load_this();
        e.invoke_static_this(BIND_CALLBACKS);
        e.load_this();
        e.load_arg(0);
        e.process_switch(keys, new ProcessSwitchCallback(this, e) { // from class: net.sf.cglib.proxy.Enhancer.3
            private final CodeEmitter val$e;
            private final Enhancer this$0;

            {
                this.this$0 = this;
                this.val$e = e;
            }

            @Override // net.sf.cglib.core.ProcessSwitchCallback
            public void processCase(int key, Label end) {
                this.val$e.getfield(Enhancer.getCallbackField(key));
                this.val$e.goTo(end);
            }

            @Override // net.sf.cglib.core.ProcessSwitchCallback
            public void processDefault() {
                this.val$e.pop();
                this.val$e.aconst_null();
            }
        });
        e.return_value();
        e.end_method();
    }

    private void emitSetCallback(ClassEmitter ce, int[] keys) {
        CodeEmitter e = ce.begin_method(1, SET_CALLBACK, null);
        e.load_arg(0);
        e.process_switch(keys, new ProcessSwitchCallback(this, e) { // from class: net.sf.cglib.proxy.Enhancer.4
            private final CodeEmitter val$e;
            private final Enhancer this$0;

            {
                this.this$0 = this;
                this.val$e = e;
            }

            @Override // net.sf.cglib.core.ProcessSwitchCallback
            public void processCase(int key, Label end) {
                this.val$e.load_this();
                this.val$e.load_arg(1);
                this.val$e.checkcast(this.this$0.callbackTypes[key]);
                this.val$e.putfield(Enhancer.getCallbackField(key));
                this.val$e.goTo(end);
            }

            @Override // net.sf.cglib.core.ProcessSwitchCallback
            public void processDefault() {
            }
        });
        e.return_value();
        e.end_method();
    }

    private void emitSetCallbacks(ClassEmitter ce) {
        CodeEmitter e = ce.begin_method(1, SET_CALLBACKS, null);
        e.load_this();
        e.load_arg(0);
        for (int i = 0; i < this.callbackTypes.length; i++) {
            e.dup2();
            e.aaload(i);
            e.checkcast(this.callbackTypes[i]);
            e.putfield(getCallbackField(i));
        }
        e.return_value();
        e.end_method();
    }

    private void emitGetCallbacks(ClassEmitter ce) {
        CodeEmitter e = ce.begin_method(1, GET_CALLBACKS, null);
        e.load_this();
        e.invoke_static_this(BIND_CALLBACKS);
        e.load_this();
        e.push(this.callbackTypes.length);
        e.newarray(CALLBACK);
        for (int i = 0; i < this.callbackTypes.length; i++) {
            e.dup();
            e.push(i);
            e.load_this();
            e.getfield(getCallbackField(i));
            e.aastore();
        }
        e.return_value();
        e.end_method();
    }

    private void emitNewInstanceCallbacks(ClassEmitter ce) {
        CodeEmitter e = ce.begin_method(1, NEW_INSTANCE, null);
        e.load_arg(0);
        e.invoke_static_this(SET_THREAD_CALLBACKS);
        emitCommonNewInstance(e);
    }

    private void emitCommonNewInstance(CodeEmitter e) {
        e.new_instance_this();
        e.dup();
        e.invoke_constructor_this();
        e.aconst_null();
        e.invoke_static_this(SET_THREAD_CALLBACKS);
        e.return_value();
        e.end_method();
    }

    private void emitNewInstanceCallback(ClassEmitter ce) {
        CodeEmitter e = ce.begin_method(1, SINGLE_NEW_INSTANCE, null);
        switch (this.callbackTypes.length) {
            case 0:
                break;
            case 1:
                e.push(1);
                e.newarray(CALLBACK);
                e.dup();
                e.push(0);
                e.load_arg(0);
                e.aastore();
                e.invoke_static_this(SET_THREAD_CALLBACKS);
                break;
            default:
                e.throw_exception(ILLEGAL_STATE_EXCEPTION, "More than one callback object required");
                break;
        }
        emitCommonNewInstance(e);
    }

    private void emitNewInstanceMultiarg(ClassEmitter ce, List constructors) {
        CodeEmitter e = ce.begin_method(1, MULTIARG_NEW_INSTANCE, null);
        e.load_arg(2);
        e.invoke_static_this(SET_THREAD_CALLBACKS);
        e.new_instance_this();
        e.dup();
        e.load_arg(0);
        EmitUtils.constructor_switch(e, constructors, new ObjectSwitchCallback(this, e) { // from class: net.sf.cglib.proxy.Enhancer.5
            private final CodeEmitter val$e;
            private final Enhancer this$0;

            {
                this.this$0 = this;
                this.val$e = e;
            }

            @Override // net.sf.cglib.core.ObjectSwitchCallback
            public void processCase(Object key, Label end) {
                MethodInfo constructor = (MethodInfo) key;
                Type[] types = constructor.getSignature().getArgumentTypes();
                for (int i = 0; i < types.length; i++) {
                    this.val$e.load_arg(1);
                    this.val$e.push(i);
                    this.val$e.aaload();
                    this.val$e.unbox(types[i]);
                }
                this.val$e.invoke_constructor_this(constructor.getSignature());
                this.val$e.goTo(end);
            }

            @Override // net.sf.cglib.core.ObjectSwitchCallback
            public void processDefault() {
                this.val$e.throw_exception(Enhancer.ILLEGAL_ARGUMENT_EXCEPTION, "Constructor not found");
            }
        });
        e.aconst_null();
        e.invoke_static_this(SET_THREAD_CALLBACKS);
        e.return_value();
        e.end_method();
    }

    private void emitMethods(ClassEmitter ce, List methods, List actualMethods) {
        CallbackGenerator[] generators = CallbackInfo.getGenerators(this.callbackTypes);
        Map groups = new HashMap();
        Map indexes = new HashMap();
        Map originalModifiers = new HashMap();
        Map positions = CollectionUtils.getIndexMap(methods);
        Map declToBridge = new HashMap();
        Iterator it1 = methods.iterator();
        Iterator it2 = actualMethods != null ? actualMethods.iterator() : null;
        while (it1.hasNext()) {
            MethodInfo method = (MethodInfo) it1.next();
            Method actualMethod = it2 != null ? (Method) it2.next() : null;
            int index = this.filter.accept(actualMethod);
            if (index >= this.callbackTypes.length) {
                throw new IllegalArgumentException(new StringBuffer().append("Callback filter returned an index that is too large: ").append(index).toString());
            }
            originalModifiers.put(method, new Integer(actualMethod != null ? actualMethod.getModifiers() : method.getModifiers()));
            indexes.put(method, new Integer(index));
            List group = (List) groups.get(generators[index]);
            if (group == null) {
                CallbackGenerator callbackGenerator = generators[index];
                ArrayList arrayList = new ArrayList(methods.size());
                group = arrayList;
                groups.put(callbackGenerator, arrayList);
            }
            group.add(method);
            if (TypeUtils.isBridge(actualMethod.getModifiers())) {
                Set bridges = (Set) declToBridge.get(actualMethod.getDeclaringClass());
                if (bridges == null) {
                    bridges = new HashSet();
                    declToBridge.put(actualMethod.getDeclaringClass(), bridges);
                }
                bridges.add(method.getSignature());
            }
        }
        Map bridgeToTarget = new BridgeMethodResolver(declToBridge).resolveAll();
        Set seenGen = new HashSet();
        CodeEmitter se = ce.getStaticHook();
        se.new_instance(THREAD_LOCAL);
        se.dup();
        se.invoke_constructor(THREAD_LOCAL, CSTRUCT_NULL);
        se.putfield(THREAD_CALLBACKS_FIELD);
        Object[] objArr = new Object[1];
        CallbackGenerator.Context context = new CallbackGenerator.Context(this, originalModifiers, indexes, positions, bridgeToTarget) { // from class: net.sf.cglib.proxy.Enhancer.6
            private final Map val$originalModifiers;
            private final Map val$indexes;
            private final Map val$positions;
            private final Map val$bridgeToTarget;
            private final Enhancer this$0;

            {
                this.this$0 = this;
                this.val$originalModifiers = originalModifiers;
                this.val$indexes = indexes;
                this.val$positions = positions;
                this.val$bridgeToTarget = bridgeToTarget;
            }

            @Override // net.sf.cglib.proxy.CallbackGenerator.Context
            public ClassLoader getClassLoader() {
                return this.this$0.getClassLoader();
            }

            @Override // net.sf.cglib.proxy.CallbackGenerator.Context
            public int getOriginalModifiers(MethodInfo method2) {
                return ((Integer) this.val$originalModifiers.get(method2)).intValue();
            }

            @Override // net.sf.cglib.proxy.CallbackGenerator.Context
            public int getIndex(MethodInfo method2) {
                return ((Integer) this.val$indexes.get(method2)).intValue();
            }

            @Override // net.sf.cglib.proxy.CallbackGenerator.Context
            public void emitCallback(CodeEmitter e, int index2) {
                this.this$0.emitCurrentCallback(e, index2);
            }

            @Override // net.sf.cglib.proxy.CallbackGenerator.Context
            public Signature getImplSignature(MethodInfo method2) {
                return this.this$0.rename(method2.getSignature(), ((Integer) this.val$positions.get(method2)).intValue());
            }

            @Override // net.sf.cglib.proxy.CallbackGenerator.Context
            public void emitInvoke(CodeEmitter e, MethodInfo method2) {
                Signature bridgeTarget = (Signature) this.val$bridgeToTarget.get(method2.getSignature());
                if (bridgeTarget != null) {
                    e.invoke_virtual_this(bridgeTarget);
                    Type retType = method2.getSignature().getReturnType();
                    if (!retType.equals(bridgeTarget.getReturnType())) {
                        e.checkcast(retType);
                        return;
                    }
                    return;
                }
                e.super_invoke(method2.getSignature());
            }

            @Override // net.sf.cglib.proxy.CallbackGenerator.Context
            public CodeEmitter beginMethod(ClassEmitter ce2, MethodInfo method2) {
                CodeEmitter e = EmitUtils.begin_method(ce2, method2);
                if (!this.this$0.interceptDuringConstruction && !TypeUtils.isAbstract(method2.getModifiers())) {
                    Label constructed = e.make_label();
                    e.load_this();
                    e.getfield(Enhancer.CONSTRUCTED_FIELD);
                    e.if_jump(154, constructed);
                    e.load_this();
                    e.load_args();
                    e.super_invoke();
                    e.return_value();
                    e.mark(constructed);
                }
                return e;
            }
        };
        for (int i = 0; i < this.callbackTypes.length; i++) {
            CallbackGenerator gen = generators[i];
            if (!seenGen.contains(gen)) {
                seenGen.add(gen);
                List fmethods = (List) groups.get(gen);
                if (fmethods != null) {
                    try {
                        gen.generate(ce, context, fmethods);
                        gen.generateStatic(se, context, fmethods);
                    } catch (RuntimeException x) {
                        throw x;
                    } catch (Exception x2) {
                        throw new CodeGenerationException(x2);
                    }
                } else {
                    continue;
                }
            }
        }
        se.return_value();
        se.end_method();
    }

    private void emitSetThreadCallbacks(ClassEmitter ce) {
        CodeEmitter e = ce.begin_method(9, SET_THREAD_CALLBACKS, null);
        e.getfield(THREAD_CALLBACKS_FIELD);
        e.load_arg(0);
        e.invoke_virtual(THREAD_LOCAL, THREAD_LOCAL_SET);
        e.return_value();
        e.end_method();
    }

    private void emitSetStaticCallbacks(ClassEmitter ce) {
        CodeEmitter e = ce.begin_method(9, SET_STATIC_CALLBACKS, null);
        e.load_arg(0);
        e.putfield(STATIC_CALLBACKS_FIELD);
        e.return_value();
        e.end_method();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void emitCurrentCallback(CodeEmitter e, int index) {
        e.load_this();
        e.getfield(getCallbackField(index));
        e.dup();
        Label end = e.make_label();
        e.ifnonnull(end);
        e.pop();
        e.load_this();
        e.invoke_static_this(BIND_CALLBACKS);
        e.load_this();
        e.getfield(getCallbackField(index));
        e.mark(end);
    }

    private void emitBindCallbacks(ClassEmitter ce) {
        CodeEmitter e = ce.begin_method(26, BIND_CALLBACKS, null);
        Local me = e.make_local();
        e.load_arg(0);
        e.checkcast_this();
        e.store_local(me);
        Label end = e.make_label();
        e.load_local(me);
        e.getfield(BOUND_FIELD);
        e.if_jump(154, end);
        e.load_local(me);
        e.push(1);
        e.putfield(BOUND_FIELD);
        e.getfield(THREAD_CALLBACKS_FIELD);
        e.invoke_virtual(THREAD_LOCAL, THREAD_LOCAL_GET);
        e.dup();
        Label found_callback = e.make_label();
        e.ifnonnull(found_callback);
        e.pop();
        e.getfield(STATIC_CALLBACKS_FIELD);
        e.dup();
        e.ifnonnull(found_callback);
        e.pop();
        e.goTo(end);
        e.mark(found_callback);
        e.checkcast(CALLBACK_ARRAY);
        e.load_local(me);
        e.swap();
        for (int i = this.callbackTypes.length - 1; i >= 0; i--) {
            if (i != 0) {
                e.dup2();
            }
            e.aaload(i);
            e.checkcast(this.callbackTypes[i]);
            e.putfield(getCallbackField(i));
        }
        e.mark(end);
        e.return_value();
        e.end_method();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getCallbackField(int index) {
        return new StringBuffer().append("CGLIB$CALLBACK_").append(index).toString();
    }
}

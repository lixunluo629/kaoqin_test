package net.sf.cglib.reflect;

import java.lang.reflect.Method;
import net.sf.cglib.core.AbstractClassGenerator;
import net.sf.cglib.core.ClassEmitter;
import net.sf.cglib.core.CodeEmitter;
import net.sf.cglib.core.Constants;
import net.sf.cglib.core.EmitUtils;
import net.sf.cglib.core.KeyFactory;
import net.sf.cglib.core.MethodInfo;
import net.sf.cglib.core.ReflectUtils;
import net.sf.cglib.core.Signature;
import net.sf.cglib.core.TypeUtils;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Type;
import org.springframework.validation.DataBinder;

/* loaded from: cglib-3.1.jar:net/sf/cglib/reflect/MethodDelegate.class */
public abstract class MethodDelegate {
    private static final MethodDelegateKey KEY_FACTORY;
    protected Object target;
    protected String eqMethod;
    static Class class$net$sf$cglib$reflect$MethodDelegate$MethodDelegateKey;
    static Class class$net$sf$cglib$reflect$MethodDelegate;

    /* loaded from: cglib-3.1.jar:net/sf/cglib/reflect/MethodDelegate$MethodDelegateKey.class */
    interface MethodDelegateKey {
        Object newInstance(Class cls, String str, Class cls2);
    }

    public abstract MethodDelegate newInstance(Object obj);

    static {
        Class clsClass$;
        if (class$net$sf$cglib$reflect$MethodDelegate$MethodDelegateKey == null) {
            clsClass$ = class$("net.sf.cglib.reflect.MethodDelegate$MethodDelegateKey");
            class$net$sf$cglib$reflect$MethodDelegate$MethodDelegateKey = clsClass$;
        } else {
            clsClass$ = class$net$sf$cglib$reflect$MethodDelegate$MethodDelegateKey;
        }
        KEY_FACTORY = (MethodDelegateKey) KeyFactory.create(clsClass$, KeyFactory.CLASS_BY_NAME);
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    public static MethodDelegate createStatic(Class targetClass, String methodName, Class iface) {
        Generator gen = new Generator();
        gen.setTargetClass(targetClass);
        gen.setMethodName(methodName);
        gen.setInterface(iface);
        return gen.create();
    }

    public static MethodDelegate create(Object target, String methodName, Class iface) {
        Generator gen = new Generator();
        gen.setTarget(target);
        gen.setMethodName(methodName);
        gen.setInterface(iface);
        return gen.create();
    }

    public boolean equals(Object obj) {
        MethodDelegate other = (MethodDelegate) obj;
        return this.target == other.target && this.eqMethod.equals(other.eqMethod);
    }

    public int hashCode() {
        return this.target.hashCode() ^ this.eqMethod.hashCode();
    }

    public Object getTarget() {
        return this.target;
    }

    /* loaded from: cglib-3.1.jar:net/sf/cglib/reflect/MethodDelegate$Generator.class */
    public static class Generator extends AbstractClassGenerator {
        private static final AbstractClassGenerator.Source SOURCE;
        private static final Type METHOD_DELEGATE;
        private static final Signature NEW_INSTANCE;
        private Object target;
        private Class targetClass;
        private String methodName;
        private Class iface;

        static {
            Class clsClass$;
            if (MethodDelegate.class$net$sf$cglib$reflect$MethodDelegate == null) {
                clsClass$ = MethodDelegate.class$("net.sf.cglib.reflect.MethodDelegate");
                MethodDelegate.class$net$sf$cglib$reflect$MethodDelegate = clsClass$;
            } else {
                clsClass$ = MethodDelegate.class$net$sf$cglib$reflect$MethodDelegate;
            }
            SOURCE = new AbstractClassGenerator.Source(clsClass$.getName());
            METHOD_DELEGATE = TypeUtils.parseType("net.sf.cglib.reflect.MethodDelegate");
            NEW_INSTANCE = new Signature("newInstance", METHOD_DELEGATE, new Type[]{Constants.TYPE_OBJECT});
        }

        public Generator() {
            super(SOURCE);
        }

        public void setTarget(Object target) {
            this.target = target;
            this.targetClass = target.getClass();
        }

        public void setTargetClass(Class targetClass) {
            this.targetClass = targetClass;
        }

        public void setMethodName(String methodName) {
            this.methodName = methodName;
        }

        public void setInterface(Class iface) {
            this.iface = iface;
        }

        @Override // net.sf.cglib.core.AbstractClassGenerator
        protected ClassLoader getDefaultClassLoader() {
            return this.targetClass.getClassLoader();
        }

        public MethodDelegate create() {
            setNamePrefix(this.targetClass.getName());
            Object key = MethodDelegate.KEY_FACTORY.newInstance(this.targetClass, this.methodName, this.iface);
            return (MethodDelegate) super.create(key);
        }

        @Override // net.sf.cglib.core.AbstractClassGenerator
        protected Object firstInstance(Class type) {
            return ((MethodDelegate) ReflectUtils.newInstance(type)).newInstance(this.target);
        }

        @Override // net.sf.cglib.core.AbstractClassGenerator
        protected Object nextInstance(Object instance) {
            return ((MethodDelegate) instance).newInstance(this.target);
        }

        @Override // net.sf.cglib.core.ClassGenerator
        public void generateClass(ClassVisitor v) throws NoSuchMethodException, SecurityException {
            Method proxy = ReflectUtils.findInterfaceMethod(this.iface);
            Method method = this.targetClass.getMethod(this.methodName, proxy.getParameterTypes());
            if (!proxy.getReturnType().isAssignableFrom(method.getReturnType())) {
                throw new IllegalArgumentException("incompatible return types");
            }
            MethodInfo methodInfo = ReflectUtils.getMethodInfo(method);
            boolean isStatic = TypeUtils.isStatic(methodInfo.getModifiers());
            if ((this.target == null) ^ isStatic) {
                throw new IllegalArgumentException(new StringBuffer().append("Static method ").append(isStatic ? "not " : "").append("expected").toString());
            }
            ClassEmitter ce = new ClassEmitter(v);
            ce.begin_class(46, 1, getClassName(), METHOD_DELEGATE, new Type[]{Type.getType((Class<?>) this.iface)}, "<generated>");
            ce.declare_field(26, "eqMethod", Constants.TYPE_STRING, null);
            EmitUtils.null_constructor(ce);
            MethodInfo proxied = ReflectUtils.getMethodInfo(this.iface.getDeclaredMethods()[0]);
            CodeEmitter e = EmitUtils.begin_method(ce, proxied, 1);
            e.load_this();
            e.super_getfield(DataBinder.DEFAULT_OBJECT_NAME, Constants.TYPE_OBJECT);
            e.checkcast(methodInfo.getClassInfo().getType());
            e.load_args();
            e.invoke(methodInfo);
            e.return_value();
            e.end_method();
            CodeEmitter e2 = ce.begin_method(1, NEW_INSTANCE, null);
            e2.new_instance_this();
            e2.dup();
            e2.dup2();
            e2.invoke_constructor_this();
            e2.getfield("eqMethod");
            e2.super_putfield("eqMethod", Constants.TYPE_STRING);
            e2.load_arg(0);
            e2.super_putfield(DataBinder.DEFAULT_OBJECT_NAME, Constants.TYPE_OBJECT);
            e2.return_value();
            e2.end_method();
            CodeEmitter e3 = ce.begin_static();
            e3.push(methodInfo.getSignature().toString());
            e3.putfield("eqMethod");
            e3.return_value();
            e3.end_method();
            ce.end_class();
        }
    }
}

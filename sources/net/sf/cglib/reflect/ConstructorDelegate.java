package net.sf.cglib.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import net.sf.cglib.core.AbstractClassGenerator;
import net.sf.cglib.core.ClassEmitter;
import net.sf.cglib.core.CodeEmitter;
import net.sf.cglib.core.EmitUtils;
import net.sf.cglib.core.KeyFactory;
import net.sf.cglib.core.ReflectUtils;
import net.sf.cglib.core.TypeUtils;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Type;

/* loaded from: cglib-3.1.jar:net/sf/cglib/reflect/ConstructorDelegate.class */
public abstract class ConstructorDelegate {
    private static final ConstructorKey KEY_FACTORY;
    static Class class$net$sf$cglib$reflect$ConstructorDelegate$ConstructorKey;
    static Class class$net$sf$cglib$reflect$ConstructorDelegate;

    /* loaded from: cglib-3.1.jar:net/sf/cglib/reflect/ConstructorDelegate$ConstructorKey.class */
    interface ConstructorKey {
        Object newInstance(String str, String str2);
    }

    static {
        Class clsClass$;
        if (class$net$sf$cglib$reflect$ConstructorDelegate$ConstructorKey == null) {
            clsClass$ = class$("net.sf.cglib.reflect.ConstructorDelegate$ConstructorKey");
            class$net$sf$cglib$reflect$ConstructorDelegate$ConstructorKey = clsClass$;
        } else {
            clsClass$ = class$net$sf$cglib$reflect$ConstructorDelegate$ConstructorKey;
        }
        KEY_FACTORY = (ConstructorKey) KeyFactory.create(clsClass$, KeyFactory.CLASS_BY_NAME);
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    protected ConstructorDelegate() {
    }

    public static ConstructorDelegate create(Class targetClass, Class iface) {
        Generator gen = new Generator();
        gen.setTargetClass(targetClass);
        gen.setInterface(iface);
        return gen.create();
    }

    /* loaded from: cglib-3.1.jar:net/sf/cglib/reflect/ConstructorDelegate$Generator.class */
    public static class Generator extends AbstractClassGenerator {
        private static final AbstractClassGenerator.Source SOURCE;
        private static final Type CONSTRUCTOR_DELEGATE;
        private Class iface;
        private Class targetClass;

        static {
            Class clsClass$;
            if (ConstructorDelegate.class$net$sf$cglib$reflect$ConstructorDelegate == null) {
                clsClass$ = ConstructorDelegate.class$("net.sf.cglib.reflect.ConstructorDelegate");
                ConstructorDelegate.class$net$sf$cglib$reflect$ConstructorDelegate = clsClass$;
            } else {
                clsClass$ = ConstructorDelegate.class$net$sf$cglib$reflect$ConstructorDelegate;
            }
            SOURCE = new AbstractClassGenerator.Source(clsClass$.getName());
            CONSTRUCTOR_DELEGATE = TypeUtils.parseType("net.sf.cglib.reflect.ConstructorDelegate");
        }

        public Generator() {
            super(SOURCE);
        }

        public void setInterface(Class iface) {
            this.iface = iface;
        }

        public void setTargetClass(Class targetClass) {
            this.targetClass = targetClass;
        }

        public ConstructorDelegate create() {
            setNamePrefix(this.targetClass.getName());
            Object key = ConstructorDelegate.KEY_FACTORY.newInstance(this.iface.getName(), this.targetClass.getName());
            return (ConstructorDelegate) super.create(key);
        }

        @Override // net.sf.cglib.core.AbstractClassGenerator
        protected ClassLoader getDefaultClassLoader() {
            return this.targetClass.getClassLoader();
        }

        @Override // net.sf.cglib.core.ClassGenerator
        public void generateClass(ClassVisitor v) throws NoSuchMethodException, SecurityException {
            setNamePrefix(this.targetClass.getName());
            Method newInstance = ReflectUtils.findNewInstance(this.iface);
            if (!newInstance.getReturnType().isAssignableFrom(this.targetClass)) {
                throw new IllegalArgumentException("incompatible return type");
            }
            try {
                Constructor constructor = this.targetClass.getDeclaredConstructor(newInstance.getParameterTypes());
                ClassEmitter ce = new ClassEmitter(v);
                ce.begin_class(46, 1, getClassName(), CONSTRUCTOR_DELEGATE, new Type[]{Type.getType((Class<?>) this.iface)}, "<generated>");
                Type declaring = Type.getType((Class<?>) constructor.getDeclaringClass());
                EmitUtils.null_constructor(ce);
                CodeEmitter e = ce.begin_method(1, ReflectUtils.getSignature(newInstance), ReflectUtils.getExceptionTypes(newInstance));
                e.new_instance(declaring);
                e.dup();
                e.load_args();
                e.invoke_constructor(declaring, ReflectUtils.getSignature(constructor));
                e.return_value();
                e.end_method();
                ce.end_class();
            } catch (NoSuchMethodException e2) {
                throw new IllegalArgumentException("interface does not match any known constructor");
            }
        }

        @Override // net.sf.cglib.core.AbstractClassGenerator
        protected Object firstInstance(Class type) {
            return ReflectUtils.newInstance(type);
        }

        @Override // net.sf.cglib.core.AbstractClassGenerator
        protected Object nextInstance(Object instance) {
            return instance;
        }
    }
}

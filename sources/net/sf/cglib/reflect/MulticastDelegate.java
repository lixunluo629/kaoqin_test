package net.sf.cglib.reflect;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.sf.cglib.core.AbstractClassGenerator;
import net.sf.cglib.core.ClassEmitter;
import net.sf.cglib.core.CodeEmitter;
import net.sf.cglib.core.Constants;
import net.sf.cglib.core.EmitUtils;
import net.sf.cglib.core.Local;
import net.sf.cglib.core.MethodInfo;
import net.sf.cglib.core.ProcessArrayCallback;
import net.sf.cglib.core.ReflectUtils;
import net.sf.cglib.core.Signature;
import net.sf.cglib.core.TypeUtils;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Type;

/* loaded from: cglib-3.1.jar:net/sf/cglib/reflect/MulticastDelegate.class */
public abstract class MulticastDelegate implements Cloneable {
    protected Object[] targets = new Object[0];
    static Class class$net$sf$cglib$reflect$MulticastDelegate;

    public abstract MulticastDelegate add(Object obj);

    public abstract MulticastDelegate newInstance();

    protected MulticastDelegate() {
    }

    public List getTargets() {
        return new ArrayList(Arrays.asList(this.targets));
    }

    protected MulticastDelegate addHelper(Object target) {
        MulticastDelegate copy = newInstance();
        copy.targets = new Object[this.targets.length + 1];
        System.arraycopy(this.targets, 0, copy.targets, 0, this.targets.length);
        copy.targets[this.targets.length] = target;
        return copy;
    }

    public MulticastDelegate remove(Object target) {
        for (int i = this.targets.length - 1; i >= 0; i--) {
            if (this.targets[i].equals(target)) {
                MulticastDelegate copy = newInstance();
                copy.targets = new Object[this.targets.length - 1];
                System.arraycopy(this.targets, 0, copy.targets, 0, i);
                System.arraycopy(this.targets, i + 1, copy.targets, i, (this.targets.length - i) - 1);
                return copy;
            }
        }
        return this;
    }

    public static MulticastDelegate create(Class iface) {
        Generator gen = new Generator();
        gen.setInterface(iface);
        return gen.create();
    }

    /* loaded from: cglib-3.1.jar:net/sf/cglib/reflect/MulticastDelegate$Generator.class */
    public static class Generator extends AbstractClassGenerator {
        private static final AbstractClassGenerator.Source SOURCE;
        private static final Type MULTICAST_DELEGATE;
        private static final Signature NEW_INSTANCE;
        private static final Signature ADD_DELEGATE;
        private static final Signature ADD_HELPER;
        private Class iface;

        static {
            Class clsClass$;
            if (MulticastDelegate.class$net$sf$cglib$reflect$MulticastDelegate == null) {
                clsClass$ = MulticastDelegate.class$("net.sf.cglib.reflect.MulticastDelegate");
                MulticastDelegate.class$net$sf$cglib$reflect$MulticastDelegate = clsClass$;
            } else {
                clsClass$ = MulticastDelegate.class$net$sf$cglib$reflect$MulticastDelegate;
            }
            SOURCE = new AbstractClassGenerator.Source(clsClass$.getName());
            MULTICAST_DELEGATE = TypeUtils.parseType("net.sf.cglib.reflect.MulticastDelegate");
            NEW_INSTANCE = new Signature("newInstance", MULTICAST_DELEGATE, new Type[0]);
            ADD_DELEGATE = new Signature(BeanUtil.PREFIX_ADDER, MULTICAST_DELEGATE, new Type[]{Constants.TYPE_OBJECT});
            ADD_HELPER = new Signature("addHelper", MULTICAST_DELEGATE, new Type[]{Constants.TYPE_OBJECT});
        }

        public Generator() {
            super(SOURCE);
        }

        @Override // net.sf.cglib.core.AbstractClassGenerator
        protected ClassLoader getDefaultClassLoader() {
            return this.iface.getClassLoader();
        }

        public void setInterface(Class iface) {
            this.iface = iface;
        }

        public MulticastDelegate create() {
            Class clsClass$;
            if (MulticastDelegate.class$net$sf$cglib$reflect$MulticastDelegate == null) {
                clsClass$ = MulticastDelegate.class$("net.sf.cglib.reflect.MulticastDelegate");
                MulticastDelegate.class$net$sf$cglib$reflect$MulticastDelegate = clsClass$;
            } else {
                clsClass$ = MulticastDelegate.class$net$sf$cglib$reflect$MulticastDelegate;
            }
            setNamePrefix(clsClass$.getName());
            return (MulticastDelegate) super.create(this.iface.getName());
        }

        @Override // net.sf.cglib.core.ClassGenerator
        public void generateClass(ClassVisitor cv) {
            MethodInfo method = ReflectUtils.getMethodInfo(ReflectUtils.findInterfaceMethod(this.iface));
            ClassEmitter ce = new ClassEmitter(cv);
            ce.begin_class(46, 1, getClassName(), MULTICAST_DELEGATE, new Type[]{Type.getType((Class<?>) this.iface)}, "<generated>");
            EmitUtils.null_constructor(ce);
            emitProxy(ce, method);
            CodeEmitter e = ce.begin_method(1, NEW_INSTANCE, null);
            e.new_instance_this();
            e.dup();
            e.invoke_constructor_this();
            e.return_value();
            e.end_method();
            CodeEmitter e2 = ce.begin_method(1, ADD_DELEGATE, null);
            e2.load_this();
            e2.load_arg(0);
            e2.checkcast(Type.getType((Class<?>) this.iface));
            e2.invoke_virtual_this(ADD_HELPER);
            e2.return_value();
            e2.end_method();
            ce.end_class();
        }

        private void emitProxy(ClassEmitter ce, MethodInfo method) {
            CodeEmitter e = EmitUtils.begin_method(ce, method, 1);
            Type returnType = method.getSignature().getReturnType();
            boolean returns = returnType != Type.VOID_TYPE;
            Local result = null;
            if (returns) {
                result = e.make_local(returnType);
                e.zero_or_null(returnType);
                e.store_local(result);
            }
            e.load_this();
            e.super_getfield("targets", Constants.TYPE_OBJECT_ARRAY);
            Local result2 = result;
            EmitUtils.process_array(e, Constants.TYPE_OBJECT_ARRAY, new ProcessArrayCallback(this, e, method, returns, result2) { // from class: net.sf.cglib.reflect.MulticastDelegate.Generator.1
                private final CodeEmitter val$e;
                private final MethodInfo val$method;
                private final boolean val$returns;
                private final Local val$result2;
                private final Generator this$0;

                {
                    this.this$0 = this;
                    this.val$e = e;
                    this.val$method = method;
                    this.val$returns = returns;
                    this.val$result2 = result2;
                }

                @Override // net.sf.cglib.core.ProcessArrayCallback
                public void processElement(Type type) {
                    this.val$e.checkcast(Type.getType((Class<?>) this.this$0.iface));
                    this.val$e.load_args();
                    this.val$e.invoke(this.val$method);
                    if (this.val$returns) {
                        this.val$e.store_local(this.val$result2);
                    }
                }
            });
            if (returns) {
                e.load_local(result);
            }
            e.return_value();
            e.end_method();
        }

        @Override // net.sf.cglib.core.AbstractClassGenerator
        protected Object firstInstance(Class type) {
            return ((MulticastDelegate) ReflectUtils.newInstance(type)).newInstance();
        }

        @Override // net.sf.cglib.core.AbstractClassGenerator
        protected Object nextInstance(Object instance) {
            return ((MulticastDelegate) instance).newInstance();
        }
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }
}

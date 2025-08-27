package net.sf.cglib.beans;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import net.sf.cglib.core.AbstractClassGenerator;
import net.sf.cglib.core.ClassEmitter;
import net.sf.cglib.core.CodeEmitter;
import net.sf.cglib.core.EmitUtils;
import net.sf.cglib.core.MethodInfo;
import net.sf.cglib.core.ReflectUtils;
import net.sf.cglib.core.Signature;
import net.sf.cglib.core.TypeUtils;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Type;

/* loaded from: cglib-3.1.jar:net/sf/cglib/beans/ImmutableBean.class */
public class ImmutableBean {
    private static final Type ILLEGAL_STATE_EXCEPTION = TypeUtils.parseType("IllegalStateException");
    private static final Signature CSTRUCT_OBJECT = TypeUtils.parseConstructor("Object");
    private static final Class[] OBJECT_CLASSES;
    private static final String FIELD_NAME = "CGLIB$RWBean";
    static Class class$java$lang$Object;
    static Class class$net$sf$cglib$beans$ImmutableBean;

    static {
        Class clsClass$;
        Class[] clsArr = new Class[1];
        if (class$java$lang$Object == null) {
            clsClass$ = class$("java.lang.Object");
            class$java$lang$Object = clsClass$;
        } else {
            clsClass$ = class$java$lang$Object;
        }
        clsArr[0] = clsClass$;
        OBJECT_CLASSES = clsArr;
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    private ImmutableBean() {
    }

    public static Object create(Object bean) {
        Generator gen = new Generator();
        gen.setBean(bean);
        return gen.create();
    }

    /* loaded from: cglib-3.1.jar:net/sf/cglib/beans/ImmutableBean$Generator.class */
    public static class Generator extends AbstractClassGenerator {
        private static final AbstractClassGenerator.Source SOURCE;
        private Object bean;
        private Class target;

        static {
            Class clsClass$;
            if (ImmutableBean.class$net$sf$cglib$beans$ImmutableBean == null) {
                clsClass$ = ImmutableBean.class$("net.sf.cglib.beans.ImmutableBean");
                ImmutableBean.class$net$sf$cglib$beans$ImmutableBean = clsClass$;
            } else {
                clsClass$ = ImmutableBean.class$net$sf$cglib$beans$ImmutableBean;
            }
            SOURCE = new AbstractClassGenerator.Source(clsClass$.getName());
        }

        public Generator() {
            super(SOURCE);
        }

        public void setBean(Object bean) {
            this.bean = bean;
            this.target = bean.getClass();
        }

        @Override // net.sf.cglib.core.AbstractClassGenerator
        protected ClassLoader getDefaultClassLoader() {
            return this.target.getClassLoader();
        }

        public Object create() {
            String name = this.target.getName();
            setNamePrefix(name);
            return super.create(name);
        }

        @Override // net.sf.cglib.core.ClassGenerator
        public void generateClass(ClassVisitor v) {
            Type targetType = Type.getType((Class<?>) this.target);
            ClassEmitter ce = new ClassEmitter(v);
            ce.begin_class(46, 1, getClassName(), targetType, null, "<generated>");
            ce.declare_field(18, ImmutableBean.FIELD_NAME, targetType, null);
            CodeEmitter e = ce.begin_method(1, ImmutableBean.CSTRUCT_OBJECT, null);
            e.load_this();
            e.super_invoke_constructor();
            e.load_this();
            e.load_arg(0);
            e.checkcast(targetType);
            e.putfield(ImmutableBean.FIELD_NAME);
            e.return_value();
            e.end_method();
            PropertyDescriptor[] descriptors = ReflectUtils.getBeanProperties(this.target);
            Method[] getters = ReflectUtils.getPropertyMethods(descriptors, true, false);
            Method[] setters = ReflectUtils.getPropertyMethods(descriptors, false, true);
            for (Method method : getters) {
                MethodInfo getter = ReflectUtils.getMethodInfo(method);
                CodeEmitter e2 = EmitUtils.begin_method(ce, getter, 1);
                e2.load_this();
                e2.getfield(ImmutableBean.FIELD_NAME);
                e2.invoke(getter);
                e2.return_value();
                e2.end_method();
            }
            for (Method method2 : setters) {
                MethodInfo setter = ReflectUtils.getMethodInfo(method2);
                CodeEmitter e3 = EmitUtils.begin_method(ce, setter, 1);
                e3.throw_exception(ImmutableBean.ILLEGAL_STATE_EXCEPTION, "Bean is immutable");
                e3.end_method();
            }
            ce.end_class();
        }

        @Override // net.sf.cglib.core.AbstractClassGenerator
        protected Object firstInstance(Class type) {
            return ReflectUtils.newInstance(type, ImmutableBean.OBJECT_CLASSES, new Object[]{this.bean});
        }

        @Override // net.sf.cglib.core.AbstractClassGenerator
        protected Object nextInstance(Object instance) {
            return firstInstance(instance.getClass());
        }
    }
}

package net.sf.cglib.beans;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import net.sf.cglib.core.AbstractClassGenerator;
import net.sf.cglib.core.ClassEmitter;
import net.sf.cglib.core.CodeEmitter;
import net.sf.cglib.core.Constants;
import net.sf.cglib.core.Converter;
import net.sf.cglib.core.EmitUtils;
import net.sf.cglib.core.KeyFactory;
import net.sf.cglib.core.Local;
import net.sf.cglib.core.MethodInfo;
import net.sf.cglib.core.ReflectUtils;
import net.sf.cglib.core.Signature;
import net.sf.cglib.core.TypeUtils;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Type;

/* loaded from: cglib-3.1.jar:net/sf/cglib/beans/BeanCopier.class */
public abstract class BeanCopier {
    private static final BeanCopierKey KEY_FACTORY;
    private static final Type CONVERTER;
    private static final Type BEAN_COPIER;
    private static final Signature COPY;
    private static final Signature CONVERT;
    static Class class$net$sf$cglib$beans$BeanCopier$BeanCopierKey;
    static Class class$net$sf$cglib$beans$BeanCopier;

    /* loaded from: cglib-3.1.jar:net/sf/cglib/beans/BeanCopier$BeanCopierKey.class */
    interface BeanCopierKey {
        Object newInstance(String str, String str2, boolean z);
    }

    public abstract void copy(Object obj, Object obj2, Converter converter);

    static {
        Class clsClass$;
        if (class$net$sf$cglib$beans$BeanCopier$BeanCopierKey == null) {
            clsClass$ = class$("net.sf.cglib.beans.BeanCopier$BeanCopierKey");
            class$net$sf$cglib$beans$BeanCopier$BeanCopierKey = clsClass$;
        } else {
            clsClass$ = class$net$sf$cglib$beans$BeanCopier$BeanCopierKey;
        }
        KEY_FACTORY = (BeanCopierKey) KeyFactory.create(clsClass$);
        CONVERTER = TypeUtils.parseType("net.sf.cglib.core.Converter");
        BEAN_COPIER = TypeUtils.parseType("net.sf.cglib.beans.BeanCopier");
        COPY = new Signature("copy", Type.VOID_TYPE, new Type[]{Constants.TYPE_OBJECT, Constants.TYPE_OBJECT, CONVERTER});
        CONVERT = TypeUtils.parseSignature("Object convert(Object, Class, Object)");
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    public static BeanCopier create(Class source, Class target, boolean useConverter) {
        Generator gen = new Generator();
        gen.setSource(source);
        gen.setTarget(target);
        gen.setUseConverter(useConverter);
        return gen.create();
    }

    /* loaded from: cglib-3.1.jar:net/sf/cglib/beans/BeanCopier$Generator.class */
    public static class Generator extends AbstractClassGenerator {
        private static final AbstractClassGenerator.Source SOURCE;
        private Class source;
        private Class target;
        private boolean useConverter;

        static {
            Class clsClass$;
            if (BeanCopier.class$net$sf$cglib$beans$BeanCopier == null) {
                clsClass$ = BeanCopier.class$("net.sf.cglib.beans.BeanCopier");
                BeanCopier.class$net$sf$cglib$beans$BeanCopier = clsClass$;
            } else {
                clsClass$ = BeanCopier.class$net$sf$cglib$beans$BeanCopier;
            }
            SOURCE = new AbstractClassGenerator.Source(clsClass$.getName());
        }

        public Generator() {
            super(SOURCE);
        }

        public void setSource(Class source) {
            if (!Modifier.isPublic(source.getModifiers())) {
                setNamePrefix(source.getName());
            }
            this.source = source;
        }

        public void setTarget(Class target) {
            if (!Modifier.isPublic(target.getModifiers())) {
                setNamePrefix(target.getName());
            }
            this.target = target;
        }

        public void setUseConverter(boolean useConverter) {
            this.useConverter = useConverter;
        }

        @Override // net.sf.cglib.core.AbstractClassGenerator
        protected ClassLoader getDefaultClassLoader() {
            return this.source.getClassLoader();
        }

        public BeanCopier create() {
            Object key = BeanCopier.KEY_FACTORY.newInstance(this.source.getName(), this.target.getName(), this.useConverter);
            return (BeanCopier) super.create(key);
        }

        @Override // net.sf.cglib.core.ClassGenerator
        public void generateClass(ClassVisitor v) {
            Type sourceType = Type.getType((Class<?>) this.source);
            Type targetType = Type.getType((Class<?>) this.target);
            ClassEmitter ce = new ClassEmitter(v);
            ce.begin_class(46, 1, getClassName(), BeanCopier.BEAN_COPIER, null, "<generated>");
            EmitUtils.null_constructor(ce);
            CodeEmitter e = ce.begin_method(1, BeanCopier.COPY, null);
            PropertyDescriptor[] getters = ReflectUtils.getBeanGetters(this.source);
            PropertyDescriptor[] setters = ReflectUtils.getBeanGetters(this.target);
            Map names = new HashMap();
            for (int i = 0; i < getters.length; i++) {
                names.put(getters[i].getName(), getters[i]);
            }
            Local targetLocal = e.make_local();
            Local sourceLocal = e.make_local();
            if (this.useConverter) {
                e.load_arg(1);
                e.checkcast(targetType);
                e.store_local(targetLocal);
                e.load_arg(0);
                e.checkcast(sourceType);
                e.store_local(sourceLocal);
            } else {
                e.load_arg(1);
                e.checkcast(targetType);
                e.load_arg(0);
                e.checkcast(sourceType);
            }
            for (PropertyDescriptor setter : setters) {
                PropertyDescriptor getter = (PropertyDescriptor) names.get(setter.getName());
                if (getter != null) {
                    MethodInfo read = ReflectUtils.getMethodInfo(getter.getReadMethod());
                    MethodInfo write = ReflectUtils.getMethodInfo(setter.getWriteMethod());
                    if (this.useConverter) {
                        Type setterType = write.getSignature().getArgumentTypes()[0];
                        e.load_local(targetLocal);
                        e.load_arg(2);
                        e.load_local(sourceLocal);
                        e.invoke(read);
                        e.box(read.getSignature().getReturnType());
                        EmitUtils.load_class(e, setterType);
                        e.push(write.getSignature().getName());
                        e.invoke_interface(BeanCopier.CONVERTER, BeanCopier.CONVERT);
                        e.unbox_or_zero(setterType);
                        e.invoke(write);
                    } else if (compatible(getter, setter)) {
                        e.dup2();
                        e.invoke(read);
                        e.invoke(write);
                    }
                }
            }
            e.return_value();
            e.end_method();
            ce.end_class();
        }

        private static boolean compatible(PropertyDescriptor getter, PropertyDescriptor setter) {
            return setter.getPropertyType().isAssignableFrom(getter.getPropertyType());
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

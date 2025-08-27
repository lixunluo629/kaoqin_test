package net.sf.cglib.core;

import com.drew.metadata.exif.makernotes.LeicaMakernoteDirectory;
import com.mysql.jdbc.MysqlErrorNumbers;
import java.lang.reflect.Method;
import net.sf.cglib.core.AbstractClassGenerator;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;

/* loaded from: cglib-3.1.jar:net/sf/cglib/core/KeyFactory.class */
public abstract class KeyFactory {
    private static final Signature GET_NAME = TypeUtils.parseSignature("String getName()");
    private static final Signature GET_CLASS = TypeUtils.parseSignature("Class getClass()");
    private static final Signature HASH_CODE = TypeUtils.parseSignature("int hashCode()");
    private static final Signature EQUALS = TypeUtils.parseSignature("boolean equals(Object)");
    private static final Signature TO_STRING = TypeUtils.parseSignature("String toString()");
    private static final Signature APPEND_STRING = TypeUtils.parseSignature("StringBuffer append(String)");
    private static final Type KEY_FACTORY = TypeUtils.parseType("net.sf.cglib.core.KeyFactory");
    private static final int[] PRIMES = {11, 73, 179, 331, 521, LeicaMakernoteDirectory.TAG_APPROXIMATE_F_NUMBER, MysqlErrorNumbers.ER_LOCK_DEADLOCK, MysqlErrorNumbers.ER_FK_FAIL_ADD_SYSTEM, 2609, 3691, 5189, 7247, 10037, 13931, 19289, 26627, 36683, 50441, 69403, 95401, 131129, 180179, 247501, 340057, 467063, 641371, 880603, 1209107, 1660097, 2279161, 3129011, 4295723, 5897291, 8095873, 11114263, 15257791, 20946017, 28754629, 39474179, 54189869, 74391461, 102123817, 140194277, 192456917, 264202273, 362693231, 497900099, 683510293, 938313161, 1288102441, 1768288259};
    public static final Customizer CLASS_BY_NAME = new Customizer() { // from class: net.sf.cglib.core.KeyFactory.1
        @Override // net.sf.cglib.core.Customizer
        public void customize(CodeEmitter e, Type type) {
            if (type.equals(Constants.TYPE_CLASS)) {
                e.invoke_virtual(Constants.TYPE_CLASS, KeyFactory.GET_NAME);
            }
        }
    };
    public static final Customizer OBJECT_BY_CLASS = new Customizer() { // from class: net.sf.cglib.core.KeyFactory.2
        @Override // net.sf.cglib.core.Customizer
        public void customize(CodeEmitter e, Type type) {
            e.invoke_virtual(Constants.TYPE_OBJECT, KeyFactory.GET_CLASS);
        }
    };
    static Class class$net$sf$cglib$core$KeyFactory;
    static Class class$java$lang$Object;

    protected KeyFactory() {
    }

    public static KeyFactory create(Class keyInterface) {
        return create(keyInterface, null);
    }

    public static KeyFactory create(Class keyInterface, Customizer customizer) {
        return create(keyInterface.getClassLoader(), keyInterface, customizer);
    }

    public static KeyFactory create(ClassLoader loader, Class keyInterface, Customizer customizer) {
        Generator gen = new Generator();
        gen.setInterface(keyInterface);
        gen.setCustomizer(customizer);
        gen.setClassLoader(loader);
        return gen.create();
    }

    /* loaded from: cglib-3.1.jar:net/sf/cglib/core/KeyFactory$Generator.class */
    public static class Generator extends AbstractClassGenerator {
        private static final AbstractClassGenerator.Source SOURCE;
        private Class keyInterface;
        private Customizer customizer;
        private int constant;
        private int multiplier;

        static {
            Class clsClass$;
            if (KeyFactory.class$net$sf$cglib$core$KeyFactory == null) {
                clsClass$ = KeyFactory.class$("net.sf.cglib.core.KeyFactory");
                KeyFactory.class$net$sf$cglib$core$KeyFactory = clsClass$;
            } else {
                clsClass$ = KeyFactory.class$net$sf$cglib$core$KeyFactory;
            }
            SOURCE = new AbstractClassGenerator.Source(clsClass$.getName());
        }

        public Generator() {
            super(SOURCE);
        }

        @Override // net.sf.cglib.core.AbstractClassGenerator
        protected ClassLoader getDefaultClassLoader() {
            return this.keyInterface.getClassLoader();
        }

        public void setCustomizer(Customizer customizer) {
            this.customizer = customizer;
        }

        public void setInterface(Class keyInterface) {
            this.keyInterface = keyInterface;
        }

        public KeyFactory create() {
            setNamePrefix(this.keyInterface.getName());
            return (KeyFactory) super.create(this.keyInterface.getName());
        }

        public void setHashConstant(int constant) {
            this.constant = constant;
        }

        public void setHashMultiplier(int multiplier) {
            this.multiplier = multiplier;
        }

        @Override // net.sf.cglib.core.AbstractClassGenerator
        protected Object firstInstance(Class type) {
            return ReflectUtils.newInstance(type);
        }

        @Override // net.sf.cglib.core.AbstractClassGenerator
        protected Object nextInstance(Object instance) {
            return instance;
        }

        @Override // net.sf.cglib.core.ClassGenerator
        public void generateClass(ClassVisitor v) {
            Class clsClass$;
            ClassEmitter ce = new ClassEmitter(v);
            Method newInstance = ReflectUtils.findNewInstance(this.keyInterface);
            Class<?> returnType = newInstance.getReturnType();
            if (KeyFactory.class$java$lang$Object == null) {
                clsClass$ = KeyFactory.class$("java.lang.Object");
                KeyFactory.class$java$lang$Object = clsClass$;
            } else {
                clsClass$ = KeyFactory.class$java$lang$Object;
            }
            if (!returnType.equals(clsClass$)) {
                throw new IllegalArgumentException("newInstance method must return Object");
            }
            Type[] parameterTypes = TypeUtils.getTypes(newInstance.getParameterTypes());
            ce.begin_class(46, 1, getClassName(), KeyFactory.KEY_FACTORY, new Type[]{Type.getType((Class<?>) this.keyInterface)}, "<generated>");
            EmitUtils.null_constructor(ce);
            EmitUtils.factory_method(ce, ReflectUtils.getSignature(newInstance));
            int seed = 0;
            CodeEmitter e = ce.begin_method(1, TypeUtils.parseConstructor(parameterTypes), null);
            e.load_this();
            e.super_invoke_constructor();
            e.load_this();
            for (int i = 0; i < parameterTypes.length; i++) {
                seed += parameterTypes[i].hashCode();
                ce.declare_field(18, getFieldName(i), parameterTypes[i], null);
                e.dup();
                e.load_arg(i);
                e.putfield(getFieldName(i));
            }
            e.return_value();
            e.end_method();
            CodeEmitter e2 = ce.begin_method(1, KeyFactory.HASH_CODE, null);
            int hc = this.constant != 0 ? this.constant : KeyFactory.PRIMES[Math.abs(seed) % KeyFactory.PRIMES.length];
            int hm = this.multiplier != 0 ? this.multiplier : KeyFactory.PRIMES[Math.abs(seed * 13) % KeyFactory.PRIMES.length];
            e2.push(hc);
            for (int i2 = 0; i2 < parameterTypes.length; i2++) {
                e2.load_this();
                e2.getfield(getFieldName(i2));
                EmitUtils.hash_code(e2, parameterTypes[i2], hm, this.customizer);
            }
            e2.return_value();
            e2.end_method();
            CodeEmitter e3 = ce.begin_method(1, KeyFactory.EQUALS, null);
            Label fail = e3.make_label();
            e3.load_arg(0);
            e3.instance_of_this();
            e3.if_jump(153, fail);
            for (int i3 = 0; i3 < parameterTypes.length; i3++) {
                e3.load_this();
                e3.getfield(getFieldName(i3));
                e3.load_arg(0);
                e3.checkcast_this();
                e3.getfield(getFieldName(i3));
                EmitUtils.not_equals(e3, parameterTypes[i3], fail, this.customizer);
            }
            e3.push(1);
            e3.return_value();
            e3.mark(fail);
            e3.push(0);
            e3.return_value();
            e3.end_method();
            CodeEmitter e4 = ce.begin_method(1, KeyFactory.TO_STRING, null);
            e4.new_instance(Constants.TYPE_STRING_BUFFER);
            e4.dup();
            e4.invoke_constructor(Constants.TYPE_STRING_BUFFER);
            for (int i4 = 0; i4 < parameterTypes.length; i4++) {
                if (i4 > 0) {
                    e4.push(", ");
                    e4.invoke_virtual(Constants.TYPE_STRING_BUFFER, KeyFactory.APPEND_STRING);
                }
                e4.load_this();
                e4.getfield(getFieldName(i4));
                EmitUtils.append_string(e4, parameterTypes[i4], EmitUtils.DEFAULT_DELIMITERS, this.customizer);
            }
            e4.invoke_virtual(Constants.TYPE_STRING_BUFFER, KeyFactory.TO_STRING);
            e4.return_value();
            e4.end_method();
            ce.end_class();
        }

        private String getFieldName(int arg) {
            return new StringBuffer().append("FIELD_").append(arg).toString();
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

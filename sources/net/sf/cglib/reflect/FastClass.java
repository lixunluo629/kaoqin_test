package net.sf.cglib.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.sf.cglib.core.AbstractClassGenerator;
import net.sf.cglib.core.Constants;
import net.sf.cglib.core.ReflectUtils;
import net.sf.cglib.core.Signature;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Type;

/* loaded from: cglib-3.1.jar:net/sf/cglib/reflect/FastClass.class */
public abstract class FastClass {
    private Class type;
    static Class class$net$sf$cglib$reflect$FastClass;
    static Class class$java$lang$Class;

    public abstract int getIndex(String str, Class[] clsArr);

    public abstract int getIndex(Class[] clsArr);

    public abstract Object invoke(int i, Object obj, Object[] objArr) throws InvocationTargetException;

    public abstract Object newInstance(int i, Object[] objArr) throws InvocationTargetException;

    public abstract int getIndex(Signature signature);

    public abstract int getMaxIndex();

    protected FastClass() {
        throw new Error("Using the FastClass empty constructor--please report to the cglib-devel mailing list");
    }

    protected FastClass(Class type) {
        this.type = type;
    }

    public static FastClass create(Class type) {
        return create(type.getClassLoader(), type);
    }

    public static FastClass create(ClassLoader loader, Class type) {
        Generator gen = new Generator();
        gen.setType(type);
        gen.setClassLoader(loader);
        return gen.create();
    }

    /* loaded from: cglib-3.1.jar:net/sf/cglib/reflect/FastClass$Generator.class */
    public static class Generator extends AbstractClassGenerator {
        private static final AbstractClassGenerator.Source SOURCE;
        private Class type;

        static {
            Class clsClass$;
            if (FastClass.class$net$sf$cglib$reflect$FastClass == null) {
                clsClass$ = FastClass.class$("net.sf.cglib.reflect.FastClass");
                FastClass.class$net$sf$cglib$reflect$FastClass = clsClass$;
            } else {
                clsClass$ = FastClass.class$net$sf$cglib$reflect$FastClass;
            }
            SOURCE = new AbstractClassGenerator.Source(clsClass$.getName());
        }

        public Generator() {
            super(SOURCE);
        }

        public void setType(Class type) {
            this.type = type;
        }

        public FastClass create() {
            setNamePrefix(this.type.getName());
            return (FastClass) super.create(this.type.getName());
        }

        @Override // net.sf.cglib.core.AbstractClassGenerator
        protected ClassLoader getDefaultClassLoader() {
            return this.type.getClassLoader();
        }

        @Override // net.sf.cglib.core.ClassGenerator
        public void generateClass(ClassVisitor v) throws Exception {
            new FastClassEmitter(v, getClassName(), this.type);
        }

        @Override // net.sf.cglib.core.AbstractClassGenerator
        protected Object firstInstance(Class type) {
            Class clsClass$;
            Class[] clsArr = new Class[1];
            if (FastClass.class$java$lang$Class == null) {
                clsClass$ = FastClass.class$("java.lang.Class");
                FastClass.class$java$lang$Class = clsClass$;
            } else {
                clsClass$ = FastClass.class$java$lang$Class;
            }
            clsArr[0] = clsClass$;
            return ReflectUtils.newInstance(type, clsArr, new Object[]{this.type});
        }

        @Override // net.sf.cglib.core.AbstractClassGenerator
        protected Object nextInstance(Object instance) {
            return instance;
        }
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    public Object invoke(String name, Class[] parameterTypes, Object obj, Object[] args) throws InvocationTargetException {
        return invoke(getIndex(name, parameterTypes), obj, args);
    }

    public Object newInstance() throws InvocationTargetException {
        return newInstance(getIndex(Constants.EMPTY_CLASS_ARRAY), (Object[]) null);
    }

    public Object newInstance(Class[] parameterTypes, Object[] args) throws InvocationTargetException {
        return newInstance(getIndex(parameterTypes), args);
    }

    public FastMethod getMethod(Method method) {
        return new FastMethod(this, method);
    }

    public FastConstructor getConstructor(Constructor constructor) {
        return new FastConstructor(this, constructor);
    }

    public FastMethod getMethod(String name, Class[] parameterTypes) {
        try {
            return getMethod(this.type.getMethod(name, parameterTypes));
        } catch (NoSuchMethodException e) {
            throw new NoSuchMethodError(e.getMessage());
        }
    }

    public FastConstructor getConstructor(Class[] parameterTypes) {
        try {
            return getConstructor(this.type.getConstructor(parameterTypes));
        } catch (NoSuchMethodException e) {
            throw new NoSuchMethodError(e.getMessage());
        }
    }

    public String getName() {
        return this.type.getName();
    }

    public Class getJavaClass() {
        return this.type;
    }

    public String toString() {
        return this.type.toString();
    }

    public int hashCode() {
        return this.type.hashCode();
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof FastClass)) {
            return false;
        }
        return this.type.equals(((FastClass) o).type);
    }

    protected static String getSignatureWithoutReturnType(String name, Class[] parameterTypes) {
        StringBuffer sb = new StringBuffer();
        sb.append(name);
        sb.append('(');
        for (Class cls : parameterTypes) {
            sb.append(Type.getDescriptor(cls));
        }
        sb.append(')');
        return sb.toString();
    }
}

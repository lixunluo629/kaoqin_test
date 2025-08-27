package net.sf.cglib.beans;

import net.sf.cglib.core.AbstractClassGenerator;
import net.sf.cglib.core.KeyFactory;
import net.sf.cglib.core.ReflectUtils;
import org.objectweb.asm.ClassVisitor;

/* loaded from: cglib-3.1.jar:net/sf/cglib/beans/BulkBean.class */
public abstract class BulkBean {
    private static final BulkBeanKey KEY_FACTORY;
    protected Class target;
    protected String[] getters;
    protected String[] setters;
    protected Class[] types;
    static Class class$net$sf$cglib$beans$BulkBean$BulkBeanKey;
    static Class class$net$sf$cglib$beans$BulkBean;

    /* loaded from: cglib-3.1.jar:net/sf/cglib/beans/BulkBean$BulkBeanKey.class */
    interface BulkBeanKey {
        Object newInstance(String str, String[] strArr, String[] strArr2, String[] strArr3);
    }

    public abstract void getPropertyValues(Object obj, Object[] objArr);

    public abstract void setPropertyValues(Object obj, Object[] objArr);

    static {
        Class clsClass$;
        if (class$net$sf$cglib$beans$BulkBean$BulkBeanKey == null) {
            clsClass$ = class$("net.sf.cglib.beans.BulkBean$BulkBeanKey");
            class$net$sf$cglib$beans$BulkBean$BulkBeanKey = clsClass$;
        } else {
            clsClass$ = class$net$sf$cglib$beans$BulkBean$BulkBeanKey;
        }
        KEY_FACTORY = (BulkBeanKey) KeyFactory.create(clsClass$);
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    protected BulkBean() {
    }

    public Object[] getPropertyValues(Object bean) {
        Object[] values = new Object[this.getters.length];
        getPropertyValues(bean, values);
        return values;
    }

    public Class[] getPropertyTypes() {
        return (Class[]) this.types.clone();
    }

    public String[] getGetters() {
        return (String[]) this.getters.clone();
    }

    public String[] getSetters() {
        return (String[]) this.setters.clone();
    }

    public static BulkBean create(Class target, String[] getters, String[] setters, Class[] types) {
        Generator gen = new Generator();
        gen.setTarget(target);
        gen.setGetters(getters);
        gen.setSetters(setters);
        gen.setTypes(types);
        return gen.create();
    }

    /* loaded from: cglib-3.1.jar:net/sf/cglib/beans/BulkBean$Generator.class */
    public static class Generator extends AbstractClassGenerator {
        private static final AbstractClassGenerator.Source SOURCE;
        private Class target;
        private String[] getters;
        private String[] setters;
        private Class[] types;

        static {
            Class clsClass$;
            if (BulkBean.class$net$sf$cglib$beans$BulkBean == null) {
                clsClass$ = BulkBean.class$("net.sf.cglib.beans.BulkBean");
                BulkBean.class$net$sf$cglib$beans$BulkBean = clsClass$;
            } else {
                clsClass$ = BulkBean.class$net$sf$cglib$beans$BulkBean;
            }
            SOURCE = new AbstractClassGenerator.Source(clsClass$.getName());
        }

        public Generator() {
            super(SOURCE);
        }

        public void setTarget(Class target) {
            this.target = target;
        }

        public void setGetters(String[] getters) {
            this.getters = getters;
        }

        public void setSetters(String[] setters) {
            this.setters = setters;
        }

        public void setTypes(Class[] types) {
            this.types = types;
        }

        @Override // net.sf.cglib.core.AbstractClassGenerator
        protected ClassLoader getDefaultClassLoader() {
            return this.target.getClassLoader();
        }

        public BulkBean create() {
            setNamePrefix(this.target.getName());
            String targetClassName = this.target.getName();
            String[] typeClassNames = ReflectUtils.getNames(this.types);
            Object key = BulkBean.KEY_FACTORY.newInstance(targetClassName, this.getters, this.setters, typeClassNames);
            return (BulkBean) super.create(key);
        }

        @Override // net.sf.cglib.core.ClassGenerator
        public void generateClass(ClassVisitor v) throws Exception {
            new BulkBeanEmitter(v, getClassName(), this.target, this.getters, this.setters, this.types);
        }

        @Override // net.sf.cglib.core.AbstractClassGenerator
        protected Object firstInstance(Class type) {
            BulkBean instance = (BulkBean) ReflectUtils.newInstance(type);
            instance.target = this.target;
            int length = this.getters.length;
            instance.getters = new String[length];
            System.arraycopy(this.getters, 0, instance.getters, 0, length);
            instance.setters = new String[length];
            System.arraycopy(this.setters, 0, instance.setters, 0, length);
            instance.types = new Class[this.types.length];
            System.arraycopy(this.types, 0, instance.types, 0, this.types.length);
            return instance;
        }

        @Override // net.sf.cglib.core.AbstractClassGenerator
        protected Object nextInstance(Object instance) {
            return instance;
        }
    }
}

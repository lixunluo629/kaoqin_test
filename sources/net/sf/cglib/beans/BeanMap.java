package net.sf.cglib.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sf.cglib.core.AbstractClassGenerator;
import net.sf.cglib.core.KeyFactory;
import net.sf.cglib.core.ReflectUtils;
import org.objectweb.asm.ClassVisitor;

/* loaded from: cglib-3.1.jar:net/sf/cglib/beans/BeanMap.class */
public abstract class BeanMap implements Map {
    public static final int REQUIRE_GETTER = 1;
    public static final int REQUIRE_SETTER = 2;
    protected Object bean;
    static Class class$net$sf$cglib$beans$BeanMap;
    static Class class$net$sf$cglib$beans$BeanMap$Generator$BeanMapKey;

    public abstract BeanMap newInstance(Object obj);

    public abstract Class getPropertyType(String str);

    public abstract Object get(Object obj, Object obj2);

    public abstract Object put(Object obj, Object obj2, Object obj3);

    public static BeanMap create(Object bean) {
        Generator gen = new Generator();
        gen.setBean(bean);
        return gen.create();
    }

    /* loaded from: cglib-3.1.jar:net/sf/cglib/beans/BeanMap$Generator.class */
    public static class Generator extends AbstractClassGenerator {
        private static final AbstractClassGenerator.Source SOURCE;
        private static final BeanMapKey KEY_FACTORY;
        private Object bean;
        private Class beanClass;
        private int require;

        /* loaded from: cglib-3.1.jar:net/sf/cglib/beans/BeanMap$Generator$BeanMapKey.class */
        interface BeanMapKey {
            Object newInstance(Class cls, int i);
        }

        static {
            Class clsClass$;
            Class clsClass$2;
            if (BeanMap.class$net$sf$cglib$beans$BeanMap == null) {
                clsClass$ = BeanMap.class$("net.sf.cglib.beans.BeanMap");
                BeanMap.class$net$sf$cglib$beans$BeanMap = clsClass$;
            } else {
                clsClass$ = BeanMap.class$net$sf$cglib$beans$BeanMap;
            }
            SOURCE = new AbstractClassGenerator.Source(clsClass$.getName());
            if (BeanMap.class$net$sf$cglib$beans$BeanMap$Generator$BeanMapKey == null) {
                clsClass$2 = BeanMap.class$("net.sf.cglib.beans.BeanMap$Generator$BeanMapKey");
                BeanMap.class$net$sf$cglib$beans$BeanMap$Generator$BeanMapKey = clsClass$2;
            } else {
                clsClass$2 = BeanMap.class$net$sf$cglib$beans$BeanMap$Generator$BeanMapKey;
            }
            KEY_FACTORY = (BeanMapKey) KeyFactory.create(clsClass$2, KeyFactory.CLASS_BY_NAME);
        }

        public Generator() {
            super(SOURCE);
        }

        public void setBean(Object bean) {
            this.bean = bean;
            if (bean != null) {
                this.beanClass = bean.getClass();
            }
        }

        public void setBeanClass(Class beanClass) {
            this.beanClass = beanClass;
        }

        public void setRequire(int require) {
            this.require = require;
        }

        @Override // net.sf.cglib.core.AbstractClassGenerator
        protected ClassLoader getDefaultClassLoader() {
            return this.beanClass.getClassLoader();
        }

        public BeanMap create() {
            if (this.beanClass == null) {
                throw new IllegalArgumentException("Class of bean unknown");
            }
            setNamePrefix(this.beanClass.getName());
            return (BeanMap) super.create(KEY_FACTORY.newInstance(this.beanClass, this.require));
        }

        @Override // net.sf.cglib.core.ClassGenerator
        public void generateClass(ClassVisitor v) throws Exception {
            new BeanMapEmitter(v, getClassName(), this.beanClass, this.require);
        }

        @Override // net.sf.cglib.core.AbstractClassGenerator
        protected Object firstInstance(Class type) {
            return ((BeanMap) ReflectUtils.newInstance(type)).newInstance(this.bean);
        }

        @Override // net.sf.cglib.core.AbstractClassGenerator
        protected Object nextInstance(Object instance) {
            return ((BeanMap) instance).newInstance(this.bean);
        }
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    protected BeanMap() {
    }

    protected BeanMap(Object bean) {
        setBean(bean);
    }

    @Override // java.util.Map
    public Object get(Object key) {
        return get(this.bean, key);
    }

    @Override // java.util.Map
    public Object put(Object key, Object value) {
        return put(this.bean, key, value);
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public Object getBean() {
        return this.bean;
    }

    @Override // java.util.Map
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Map
    public boolean containsKey(Object key) {
        return keySet().contains(key);
    }

    @Override // java.util.Map
    public boolean containsValue(Object value) {
        Iterator it = keySet().iterator();
        while (it.hasNext()) {
            Object v = get(it.next());
            if ((value == null && v == null) || value.equals(v)) {
                return true;
            }
        }
        return false;
    }

    @Override // java.util.Map
    public int size() {
        return keySet().size();
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override // java.util.Map
    public Object remove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Map
    public void putAll(Map t) {
        for (Object key : t.keySet()) {
            put(key, t.get(key));
        }
    }

    @Override // java.util.Map
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Map)) {
            return false;
        }
        Map other = (Map) o;
        if (size() != other.size()) {
            return false;
        }
        for (Object key : keySet()) {
            if (!other.containsKey(key)) {
                return false;
            }
            Object v1 = get(key);
            Object v2 = other.get(key);
            if (v1 == null) {
                if (v2 != null) {
                    return false;
                }
            } else if (!v1.equals(v2)) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.Map
    public int hashCode() {
        int code = 0;
        Iterator it = keySet().iterator();
        while (it.hasNext()) {
            Object key = it.next();
            Object value = get(key);
            code += (key == null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode());
        }
        return code;
    }

    @Override // java.util.Map
    public Set entrySet() {
        HashMap copy = new HashMap();
        for (Object key : keySet()) {
            copy.put(key, get(key));
        }
        return Collections.unmodifiableMap(copy).entrySet();
    }

    @Override // java.util.Map
    public Collection values() {
        Set keys = keySet();
        List values = new ArrayList(keys.size());
        Iterator it = keys.iterator();
        while (it.hasNext()) {
            values.add(get(it.next()));
        }
        return Collections.unmodifiableCollection(values);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append('{');
        Iterator it = keySet().iterator();
        while (it.hasNext()) {
            Object key = it.next();
            sb.append(key);
            sb.append('=');
            sb.append(get(key));
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append('}');
        return sb.toString();
    }
}

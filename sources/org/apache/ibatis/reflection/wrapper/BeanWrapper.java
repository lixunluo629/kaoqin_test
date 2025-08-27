package org.apache.ibatis.reflection.wrapper;

import java.util.List;
import org.apache.ibatis.reflection.ExceptionUtil;
import org.apache.ibatis.reflection.MetaClass;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectionException;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.invoker.Invoker;
import org.apache.ibatis.reflection.property.PropertyTokenizer;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/reflection/wrapper/BeanWrapper.class */
public class BeanWrapper extends BaseWrapper {
    private final Object object;
    private final MetaClass metaClass;

    public BeanWrapper(MetaObject metaObject, Object object) {
        super(metaObject);
        this.object = object;
        this.metaClass = MetaClass.forClass(object.getClass(), metaObject.getReflectorFactory());
    }

    @Override // org.apache.ibatis.reflection.wrapper.ObjectWrapper
    public Object get(PropertyTokenizer prop) {
        if (prop.getIndex() != null) {
            Object collection = resolveCollection(prop, this.object);
            return getCollectionValue(prop, collection);
        }
        return getBeanProperty(prop, this.object);
    }

    @Override // org.apache.ibatis.reflection.wrapper.ObjectWrapper
    public void set(PropertyTokenizer prop, Object value) {
        if (prop.getIndex() != null) {
            Object collection = resolveCollection(prop, this.object);
            setCollectionValue(prop, collection, value);
        } else {
            setBeanProperty(prop, this.object, value);
        }
    }

    @Override // org.apache.ibatis.reflection.wrapper.ObjectWrapper
    public String findProperty(String name, boolean useCamelCaseMapping) {
        return this.metaClass.findProperty(name, useCamelCaseMapping);
    }

    @Override // org.apache.ibatis.reflection.wrapper.ObjectWrapper
    public String[] getGetterNames() {
        return this.metaClass.getGetterNames();
    }

    @Override // org.apache.ibatis.reflection.wrapper.ObjectWrapper
    public String[] getSetterNames() {
        return this.metaClass.getSetterNames();
    }

    @Override // org.apache.ibatis.reflection.wrapper.ObjectWrapper
    public Class<?> getSetterType(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            MetaObject metaValue = this.metaObject.metaObjectForProperty(prop.getIndexedName());
            if (metaValue == SystemMetaObject.NULL_META_OBJECT) {
                return this.metaClass.getSetterType(name);
            }
            return metaValue.getSetterType(prop.getChildren());
        }
        return this.metaClass.getSetterType(name);
    }

    @Override // org.apache.ibatis.reflection.wrapper.ObjectWrapper
    public Class<?> getGetterType(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            MetaObject metaValue = this.metaObject.metaObjectForProperty(prop.getIndexedName());
            if (metaValue == SystemMetaObject.NULL_META_OBJECT) {
                return this.metaClass.getGetterType(name);
            }
            return metaValue.getGetterType(prop.getChildren());
        }
        return this.metaClass.getGetterType(name);
    }

    @Override // org.apache.ibatis.reflection.wrapper.ObjectWrapper
    public boolean hasSetter(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            if (this.metaClass.hasSetter(prop.getIndexedName())) {
                MetaObject metaValue = this.metaObject.metaObjectForProperty(prop.getIndexedName());
                if (metaValue == SystemMetaObject.NULL_META_OBJECT) {
                    return this.metaClass.hasSetter(name);
                }
                return metaValue.hasSetter(prop.getChildren());
            }
            return false;
        }
        return this.metaClass.hasSetter(name);
    }

    @Override // org.apache.ibatis.reflection.wrapper.ObjectWrapper
    public boolean hasGetter(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            if (this.metaClass.hasGetter(prop.getIndexedName())) {
                MetaObject metaValue = this.metaObject.metaObjectForProperty(prop.getIndexedName());
                if (metaValue == SystemMetaObject.NULL_META_OBJECT) {
                    return this.metaClass.hasGetter(name);
                }
                return metaValue.hasGetter(prop.getChildren());
            }
            return false;
        }
        return this.metaClass.hasGetter(name);
    }

    @Override // org.apache.ibatis.reflection.wrapper.ObjectWrapper
    public MetaObject instantiatePropertyValue(String name, PropertyTokenizer prop, ObjectFactory objectFactory) {
        Class<?> type = getSetterType(prop.getName());
        try {
            Object newObject = objectFactory.create(type);
            MetaObject metaValue = MetaObject.forObject(newObject, this.metaObject.getObjectFactory(), this.metaObject.getObjectWrapperFactory(), this.metaObject.getReflectorFactory());
            set(prop, newObject);
            return metaValue;
        } catch (Exception e) {
            throw new ReflectionException("Cannot set value of property '" + name + "' because '" + name + "' is null and cannot be instantiated on instance of " + type.getName() + ". Cause:" + e.toString(), e);
        }
    }

    private Object getBeanProperty(PropertyTokenizer prop, Object object) {
        Throwable thUnwrapThrowable;
        try {
            Invoker method = this.metaClass.getGetInvoker(prop.getName());
            try {
                return method.invoke(object, NO_ARGUMENTS);
            } finally {
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable t) {
            throw new ReflectionException("Could not get property '" + prop.getName() + "' from " + object.getClass() + ".  Cause: " + t.toString(), t);
        }
    }

    private void setBeanProperty(PropertyTokenizer prop, Object object, Object value) {
        try {
            Invoker method = this.metaClass.getSetInvoker(prop.getName());
            Object[] params = {value};
            try {
                method.invoke(object, params);
            } catch (Throwable t) {
                throw ExceptionUtil.unwrapThrowable(t);
            }
        } catch (Throwable t2) {
            throw new ReflectionException("Could not set property '" + prop.getName() + "' of '" + object.getClass() + "' with value '" + value + "' Cause: " + t2.toString(), t2);
        }
    }

    @Override // org.apache.ibatis.reflection.wrapper.ObjectWrapper
    public boolean isCollection() {
        return false;
    }

    @Override // org.apache.ibatis.reflection.wrapper.ObjectWrapper
    public void add(Object element) {
        throw new UnsupportedOperationException();
    }

    @Override // org.apache.ibatis.reflection.wrapper.ObjectWrapper
    public <E> void addAll(List<E> list) {
        throw new UnsupportedOperationException();
    }
}

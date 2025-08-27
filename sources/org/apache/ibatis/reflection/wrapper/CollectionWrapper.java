package org.apache.ibatis.reflection.wrapper;

import java.util.Collection;
import java.util.List;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.property.PropertyTokenizer;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/reflection/wrapper/CollectionWrapper.class */
public class CollectionWrapper implements ObjectWrapper {
    private final Collection<Object> object;

    public CollectionWrapper(MetaObject metaObject, Collection<Object> object) {
        this.object = object;
    }

    @Override // org.apache.ibatis.reflection.wrapper.ObjectWrapper
    public Object get(PropertyTokenizer prop) {
        throw new UnsupportedOperationException();
    }

    @Override // org.apache.ibatis.reflection.wrapper.ObjectWrapper
    public void set(PropertyTokenizer prop, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override // org.apache.ibatis.reflection.wrapper.ObjectWrapper
    public String findProperty(String name, boolean useCamelCaseMapping) {
        throw new UnsupportedOperationException();
    }

    @Override // org.apache.ibatis.reflection.wrapper.ObjectWrapper
    public String[] getGetterNames() {
        throw new UnsupportedOperationException();
    }

    @Override // org.apache.ibatis.reflection.wrapper.ObjectWrapper
    public String[] getSetterNames() {
        throw new UnsupportedOperationException();
    }

    @Override // org.apache.ibatis.reflection.wrapper.ObjectWrapper
    public Class<?> getSetterType(String name) {
        throw new UnsupportedOperationException();
    }

    @Override // org.apache.ibatis.reflection.wrapper.ObjectWrapper
    public Class<?> getGetterType(String name) {
        throw new UnsupportedOperationException();
    }

    @Override // org.apache.ibatis.reflection.wrapper.ObjectWrapper
    public boolean hasSetter(String name) {
        throw new UnsupportedOperationException();
    }

    @Override // org.apache.ibatis.reflection.wrapper.ObjectWrapper
    public boolean hasGetter(String name) {
        throw new UnsupportedOperationException();
    }

    @Override // org.apache.ibatis.reflection.wrapper.ObjectWrapper
    public MetaObject instantiatePropertyValue(String name, PropertyTokenizer prop, ObjectFactory objectFactory) {
        throw new UnsupportedOperationException();
    }

    @Override // org.apache.ibatis.reflection.wrapper.ObjectWrapper
    public boolean isCollection() {
        return true;
    }

    @Override // org.apache.ibatis.reflection.wrapper.ObjectWrapper
    public void add(Object element) {
        this.object.add(element);
    }

    @Override // org.apache.ibatis.reflection.wrapper.ObjectWrapper
    public <E> void addAll(List<E> element) {
        this.object.addAll(element);
    }
}

package org.apache.ibatis.reflection.wrapper;

import java.util.List;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.property.PropertyTokenizer;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/reflection/wrapper/ObjectWrapper.class */
public interface ObjectWrapper {
    Object get(PropertyTokenizer propertyTokenizer);

    void set(PropertyTokenizer propertyTokenizer, Object obj);

    String findProperty(String str, boolean z);

    String[] getGetterNames();

    String[] getSetterNames();

    Class<?> getSetterType(String str);

    Class<?> getGetterType(String str);

    boolean hasSetter(String str);

    boolean hasGetter(String str);

    MetaObject instantiatePropertyValue(String str, PropertyTokenizer propertyTokenizer, ObjectFactory objectFactory);

    boolean isCollection();

    void add(Object obj);

    <E> void addAll(List<E> list);
}

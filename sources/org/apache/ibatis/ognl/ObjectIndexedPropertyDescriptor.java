package org.apache.ibatis.ognl;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/ObjectIndexedPropertyDescriptor.class */
public class ObjectIndexedPropertyDescriptor extends PropertyDescriptor {
    private Method indexedReadMethod;
    private Method indexedWriteMethod;
    private Class propertyType;

    public ObjectIndexedPropertyDescriptor(String propertyName, Class propertyType, Method indexedReadMethod, Method indexedWriteMethod) throws IntrospectionException {
        super(propertyName, (Method) null, (Method) null);
        this.propertyType = propertyType;
        this.indexedReadMethod = indexedReadMethod;
        this.indexedWriteMethod = indexedWriteMethod;
    }

    public Method getIndexedReadMethod() {
        return this.indexedReadMethod;
    }

    public Method getIndexedWriteMethod() {
        return this.indexedWriteMethod;
    }

    public Class getPropertyType() {
        return this.propertyType;
    }
}

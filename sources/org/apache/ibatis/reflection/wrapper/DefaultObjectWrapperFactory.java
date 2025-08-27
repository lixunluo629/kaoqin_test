package org.apache.ibatis.reflection.wrapper;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectionException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/reflection/wrapper/DefaultObjectWrapperFactory.class */
public class DefaultObjectWrapperFactory implements ObjectWrapperFactory {
    @Override // org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory
    public boolean hasWrapperFor(Object object) {
        return false;
    }

    @Override // org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory
    public ObjectWrapper getWrapperFor(MetaObject metaObject, Object object) {
        throw new ReflectionException("The DefaultObjectWrapperFactory should never be called to provide an ObjectWrapper.");
    }
}

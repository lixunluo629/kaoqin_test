package org.apache.ibatis.reflection.wrapper;

import org.apache.ibatis.reflection.MetaObject;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/reflection/wrapper/ObjectWrapperFactory.class */
public interface ObjectWrapperFactory {
    boolean hasWrapperFor(Object obj);

    ObjectWrapper getWrapperFor(MetaObject metaObject, Object obj);
}

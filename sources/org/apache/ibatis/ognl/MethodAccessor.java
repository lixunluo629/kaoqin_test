package org.apache.ibatis.ognl;

import java.util.Map;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/MethodAccessor.class */
public interface MethodAccessor {
    Object callStaticMethod(Map map, Class cls, String str, Object[] objArr) throws MethodFailedException;

    Object callMethod(Map map, Object obj, String str, Object[] objArr) throws MethodFailedException;
}

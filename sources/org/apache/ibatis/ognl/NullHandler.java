package org.apache.ibatis.ognl;

import java.util.Map;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/NullHandler.class */
public interface NullHandler {
    Object nullMethodResult(Map map, Object obj, String str, Object[] objArr);

    Object nullPropertyValue(Map map, Object obj, Object obj2);
}

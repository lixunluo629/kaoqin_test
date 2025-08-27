package org.apache.ibatis.ognl;

import java.util.Map;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/ObjectNullHandler.class */
public class ObjectNullHandler implements NullHandler {
    @Override // org.apache.ibatis.ognl.NullHandler
    public Object nullMethodResult(Map context, Object target, String methodName, Object[] args) {
        return null;
    }

    @Override // org.apache.ibatis.ognl.NullHandler
    public Object nullPropertyValue(Map context, Object target, Object property) {
        return null;
    }
}

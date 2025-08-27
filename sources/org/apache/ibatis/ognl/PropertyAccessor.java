package org.apache.ibatis.ognl;

import java.util.Map;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/PropertyAccessor.class */
public interface PropertyAccessor {
    Object getProperty(Map map, Object obj, Object obj2) throws OgnlException;

    void setProperty(Map map, Object obj, Object obj2, Object obj3) throws OgnlException;

    String getSourceAccessor(OgnlContext ognlContext, Object obj, Object obj2);

    String getSourceSetter(OgnlContext ognlContext, Object obj, Object obj2);
}

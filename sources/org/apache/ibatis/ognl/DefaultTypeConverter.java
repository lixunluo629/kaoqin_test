package org.apache.ibatis.ognl;

import java.lang.reflect.Member;
import java.util.Map;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/DefaultTypeConverter.class */
public class DefaultTypeConverter implements TypeConverter {
    public Object convertValue(Map context, Object value, Class toType) {
        return OgnlOps.convertValue(value, toType);
    }

    @Override // org.apache.ibatis.ognl.TypeConverter
    public Object convertValue(Map context, Object target, Member member, String propertyName, Object value, Class toType) {
        return convertValue(context, value, toType);
    }
}

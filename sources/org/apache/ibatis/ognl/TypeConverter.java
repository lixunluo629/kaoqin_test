package org.apache.ibatis.ognl;

import java.lang.reflect.Member;
import java.util.Map;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/TypeConverter.class */
public interface TypeConverter {
    Object convertValue(Map map, Object obj, Member member, String str, Object obj2, Class cls);
}

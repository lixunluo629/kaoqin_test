package org.apache.ibatis.ognl;

import java.lang.reflect.Member;
import java.util.Map;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/MemberAccess.class */
public interface MemberAccess {
    Object setup(Map map, Object obj, Member member, String str);

    void restore(Map map, Object obj, Member member, String str, Object obj2);

    boolean isAccessible(Map map, Object obj, Member member, String str);
}

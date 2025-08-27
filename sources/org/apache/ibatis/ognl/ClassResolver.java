package org.apache.ibatis.ognl;

import java.util.Map;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/ClassResolver.class */
public interface ClassResolver {
    Class classForName(String str, Map map) throws ClassNotFoundException;
}

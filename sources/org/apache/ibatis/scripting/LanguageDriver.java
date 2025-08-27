package org.apache.ibatis.scripting;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.session.Configuration;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/scripting/LanguageDriver.class */
public interface LanguageDriver {
    ParameterHandler createParameterHandler(MappedStatement mappedStatement, Object obj, BoundSql boundSql);

    SqlSource createSqlSource(Configuration configuration, XNode xNode, Class<?> cls);

    SqlSource createSqlSource(Configuration configuration, String str, Class<?> cls);
}

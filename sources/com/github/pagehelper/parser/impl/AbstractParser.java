package com.github.pagehelper.parser.impl;

import com.github.pagehelper.Constant;
import com.github.pagehelper.Dialect;
import com.github.pagehelper.Page;
import com.github.pagehelper.parser.Parser;
import com.github.pagehelper.parser.SqlParser;
import com.github.pagehelper.sqlsource.PageProviderSqlSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;

/* loaded from: pagehelper-3.7.4.jar:com/github/pagehelper/parser/impl/AbstractParser.class */
public abstract class AbstractParser implements Parser, Constant {
    public static final SqlParser sqlParser = new SqlParser();

    @Override // com.github.pagehelper.parser.Parser
    public abstract String getPageSql(String str);

    public static Parser newParser(Dialect dialect) {
        Parser parser = null;
        switch (dialect) {
            case mysql:
            case mariadb:
            case sqlite:
                parser = new MysqlParser();
                break;
            case oracle:
                parser = new OracleParser();
                break;
            case hsqldb:
                parser = new HsqldbParser();
                break;
            case sqlserver:
                parser = new SqlServerParser();
                break;
            case db2:
                parser = new Db2Parser();
                break;
            case postgresql:
                parser = new PostgreSQLParser();
                break;
            case informix:
                parser = new InformixParser();
                break;
        }
        return parser;
    }

    @Override // com.github.pagehelper.parser.Parser
    public boolean isSupportedMappedStatementCache() {
        return true;
    }

    @Override // com.github.pagehelper.parser.Parser
    public String getCountSql(String sql) {
        return sqlParser.getSmartCountSql(sql);
    }

    @Override // com.github.pagehelper.parser.Parser
    public List<ParameterMapping> getPageParameterMapping(Configuration configuration, BoundSql boundSql) {
        List<ParameterMapping> newParameterMappings = new ArrayList<>();
        if (boundSql.getParameterMappings() != null) {
            newParameterMappings.addAll(boundSql.getParameterMappings());
        }
        newParameterMappings.add(new ParameterMapping.Builder(configuration, Constant.PAGEPARAMETER_FIRST, (Class<?>) Integer.class).build());
        newParameterMappings.add(new ParameterMapping.Builder(configuration, Constant.PAGEPARAMETER_SECOND, (Class<?>) Integer.class).build());
        return newParameterMappings;
    }

    @Override // com.github.pagehelper.parser.Parser
    public Map setPageParameter(MappedStatement ms, Object parameterObject, BoundSql boundSql, Page page) {
        Map paramMap;
        if (parameterObject == null) {
            paramMap = new HashMap();
        } else if (parameterObject instanceof Map) {
            paramMap = (Map) parameterObject;
        } else {
            paramMap = new HashMap();
            boolean hasTypeHandler = ms.getConfiguration().getTypeHandlerRegistry().hasTypeHandler(parameterObject.getClass());
            MetaObject metaObject = SystemMetaObject.forObject(parameterObject);
            if (ms.getSqlSource() instanceof PageProviderSqlSource) {
                paramMap.put(Constant.PROVIDER_OBJECT, parameterObject);
            }
            if (!hasTypeHandler) {
                String[] arr$ = metaObject.getGetterNames();
                for (String name : arr$) {
                    paramMap.put(name, metaObject.getValue(name));
                }
            }
            if (boundSql.getParameterMappings() != null && boundSql.getParameterMappings().size() > 0) {
                for (ParameterMapping parameterMapping : boundSql.getParameterMappings()) {
                    String name2 = parameterMapping.getProperty();
                    if (!name2.equals(Constant.PAGEPARAMETER_FIRST) && !name2.equals(Constant.PAGEPARAMETER_SECOND) && paramMap.get(name2) == null && (hasTypeHandler || parameterMapping.getJavaType().equals(parameterObject.getClass()))) {
                        paramMap.put(name2, parameterObject);
                        break;
                    }
                }
            }
        }
        paramMap.put(Constant.ORIGINAL_PARAMETER_OBJECT, parameterObject);
        return paramMap;
    }
}

package com.github.pagehelper;

import com.github.pagehelper.parser.Parser;
import com.github.pagehelper.sqlsource.PageDynamicSqlSource;
import com.github.pagehelper.sqlsource.PageProviderSqlSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.builder.annotation.ProviderSqlSource;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.scripting.xmltags.MixedSqlNode;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.session.Configuration;

/* loaded from: pagehelper-3.7.4.jar:com/github/pagehelper/MSUtils.class */
public class MSUtils implements Constant {
    private static final List<ResultMapping> EMPTY_RESULTMAPPING = new ArrayList(0);
    private Parser parser;

    public MSUtils(Parser parser) {
        this.parser = parser;
    }

    public void processCountMappedStatement(MappedStatement ms, SqlSource sqlSource, Object[] args) {
        args[0] = getMappedStatement(ms, sqlSource, args[1], Constant.SUFFIX_COUNT);
    }

    public void processPageMappedStatement(MappedStatement ms, SqlSource sqlSource, Page page, Object[] args) {
        args[0] = getMappedStatement(ms, sqlSource, args[1], Constant.SUFFIX_PAGE);
        args[1] = setPageParameter((MappedStatement) args[0], args[1], page);
    }

    public Map setPageParameter(MappedStatement ms, Object parameterObject, Page page) {
        BoundSql boundSql = ms.getBoundSql(parameterObject);
        return this.parser.setPageParameter(ms, parameterObject, boundSql, page);
    }

    public MappedStatement getMappedStatement(MappedStatement ms, SqlSource sqlSource, Object parameterObject, String suffix) throws SecurityException {
        MappedStatement qs = null;
        if (ms.getId().endsWith(Constant.SUFFIX_PAGE) || ms.getId().endsWith(Constant.SUFFIX_COUNT)) {
            throw new RuntimeException("分页插件配置错误:请不要在系统中配置多个分页插件(使用Spring时,mybatis-config.xml和Spring<bean>配置方式，请选择其中一种，不要同时配置多个分页插件)！");
        }
        if (this.parser.isSupportedMappedStatementCache()) {
            try {
                qs = ms.getConfiguration().getMappedStatement(ms.getId() + suffix);
            } catch (Exception e) {
            }
        }
        if (qs == null) {
            qs = newMappedStatement(ms, getsqlSource(ms, sqlSource, parameterObject, suffix == Constant.SUFFIX_COUNT), suffix);
            if (this.parser.isSupportedMappedStatementCache()) {
                try {
                    ms.getConfiguration().addMappedStatement(qs);
                } catch (Exception e2) {
                }
            }
        }
        return qs;
    }

    public MappedStatement newMappedStatement(MappedStatement ms, SqlSource sqlSource, String suffix) throws SecurityException {
        String id = ms.getId() + suffix;
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), id, sqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
            StringBuilder keyProperties = new StringBuilder();
            String[] arr$ = ms.getKeyProperties();
            for (String keyProperty : arr$) {
                keyProperties.append(keyProperty).append(",");
            }
            keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
            builder.keyProperty(keyProperties.toString());
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        if (suffix == Constant.SUFFIX_PAGE) {
            builder.resultMaps(ms.getResultMaps());
        } else {
            List<ResultMap> resultMaps = new ArrayList<>();
            ResultMap resultMap = new ResultMap.Builder(ms.getConfiguration(), id, Integer.TYPE, EMPTY_RESULTMAPPING).build();
            resultMaps.add(resultMap);
            builder.resultMaps(resultMaps);
        }
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }

    public SqlSource getsqlSource(MappedStatement ms, SqlSource sqlSource, Object parameterObject, boolean count) {
        MixedSqlNode mixedSqlNode;
        if (sqlSource instanceof DynamicSqlSource) {
            MetaObject msObject = SystemMetaObject.forObject(ms);
            SqlNode sqlNode = (SqlNode) msObject.getValue("sqlSource.rootSqlNode");
            if (sqlNode instanceof MixedSqlNode) {
                mixedSqlNode = (MixedSqlNode) sqlNode;
            } else {
                List<SqlNode> contents = new ArrayList<>(1);
                contents.add(sqlNode);
                mixedSqlNode = new MixedSqlNode(contents);
            }
            return new PageDynamicSqlSource(this, ms.getConfiguration(), mixedSqlNode, Boolean.valueOf(count));
        }
        if (sqlSource instanceof ProviderSqlSource) {
            return new PageProviderSqlSource(this.parser, ms.getConfiguration(), (ProviderSqlSource) sqlSource, Boolean.valueOf(count));
        }
        if (count) {
            return getStaticCountSqlSource(ms.getConfiguration(), sqlSource, parameterObject);
        }
        return getStaticPageSqlSource(ms.getConfiguration(), sqlSource, parameterObject);
    }

    public SqlSource getStaticPageSqlSource(Configuration configuration, SqlSource sqlSource, Object parameterObject) {
        BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
        return new StaticSqlSource(configuration, this.parser.getPageSql(boundSql.getSql()), this.parser.getPageParameterMapping(configuration, boundSql));
    }

    public SqlSource getStaticCountSqlSource(Configuration configuration, SqlSource sqlSource, Object parameterObject) {
        BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
        return new StaticSqlSource(configuration, this.parser.getCountSql(boundSql.getSql()), boundSql.getParameterMappings());
    }
}

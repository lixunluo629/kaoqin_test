package com.github.pagehelper.sqlsource;

import com.github.pagehelper.Constant;
import com.github.pagehelper.parser.Parser;
import java.util.Map;
import org.apache.ibatis.builder.annotation.ProviderSqlSource;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.session.Configuration;

/* loaded from: pagehelper-3.7.4.jar:com/github/pagehelper/sqlsource/PageProviderSqlSource.class */
public class PageProviderSqlSource implements SqlSource, Constant {
    private Configuration configuration;
    private ProviderSqlSource providerSqlSource;
    private Boolean count;
    private Parser parser;

    public PageProviderSqlSource(Parser parser, Configuration configuration, ProviderSqlSource providerSqlSource, Boolean count) {
        this.parser = parser;
        this.configuration = configuration;
        this.providerSqlSource = providerSqlSource;
        this.count = count;
    }

    @Override // org.apache.ibatis.mapping.SqlSource
    public BoundSql getBoundSql(Object parameterObject) {
        BoundSql boundSql;
        if ((parameterObject instanceof Map) && ((Map) parameterObject).containsKey(Constant.PROVIDER_OBJECT)) {
            boundSql = this.providerSqlSource.getBoundSql(((Map) parameterObject).get(Constant.PROVIDER_OBJECT));
        } else {
            boundSql = this.providerSqlSource.getBoundSql(parameterObject);
        }
        if (this.count.booleanValue()) {
            return new BoundSql(this.configuration, this.parser.getCountSql(boundSql.getSql()), boundSql.getParameterMappings(), parameterObject);
        }
        return new BoundSql(this.configuration, this.parser.getPageSql(boundSql.getSql()), this.parser.getPageParameterMapping(this.configuration, boundSql), parameterObject);
    }
}

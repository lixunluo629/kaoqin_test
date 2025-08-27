package com.github.pagehelper.sqlsource;

import com.github.pagehelper.Constant;
import com.github.pagehelper.MSUtils;
import java.util.Map;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.DynamicContext;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.session.Configuration;

/* loaded from: pagehelper-3.7.4.jar:com/github/pagehelper/sqlsource/PageDynamicSqlSource.class */
public class PageDynamicSqlSource implements SqlSource, Constant {
    private Configuration configuration;
    private SqlNode rootSqlNode;
    private Boolean count;
    private MSUtils msUtils;

    public PageDynamicSqlSource(MSUtils msUtils, Configuration configuration, SqlNode rootSqlNode, Boolean count) {
        this.msUtils = msUtils;
        this.configuration = configuration;
        this.rootSqlNode = rootSqlNode;
        this.count = count;
    }

    @Override // org.apache.ibatis.mapping.SqlSource
    public BoundSql getBoundSql(Object parameterObject) {
        DynamicContext context;
        SqlSource sqlSource;
        if (parameterObject != null && (parameterObject instanceof Map) && ((Map) parameterObject).containsKey(Constant.ORIGINAL_PARAMETER_OBJECT)) {
            context = new DynamicContext(this.configuration, ((Map) parameterObject).get(Constant.ORIGINAL_PARAMETER_OBJECT));
        } else {
            context = new DynamicContext(this.configuration, parameterObject);
        }
        this.rootSqlNode.apply(context);
        SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(this.configuration);
        Class<?> parameterType = parameterObject == null ? Object.class : parameterObject.getClass();
        SqlSource sqlSource2 = sqlSourceParser.parse(context.getSql(), parameterType, context.getBindings());
        if (this.count.booleanValue()) {
            sqlSource = this.msUtils.getStaticCountSqlSource(this.configuration, sqlSource2, parameterObject);
        } else {
            sqlSource = this.msUtils.getStaticPageSqlSource(this.configuration, sqlSource2, parameterObject);
        }
        BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
        for (Map.Entry<String, Object> entry : context.getBindings().entrySet()) {
            boundSql.setAdditionalParameter(entry.getKey(), entry.getValue());
        }
        return boundSql;
    }
}

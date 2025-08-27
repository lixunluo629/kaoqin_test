package com.github.pagehelper.parser.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.SqlUtil;
import com.github.pagehelper.parser.SqlServer;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.Configuration;

/* loaded from: pagehelper-3.7.4.jar:com/github/pagehelper/parser/impl/SqlServerParser.class */
public class SqlServerParser extends AbstractParser {
    private static final SqlServer pageSql = new SqlServer();

    @Override // com.github.pagehelper.parser.impl.AbstractParser, com.github.pagehelper.parser.Parser
    public boolean isSupportedMappedStatementCache() {
        return false;
    }

    @Override // com.github.pagehelper.parser.impl.AbstractParser, com.github.pagehelper.parser.Parser
    public List<ParameterMapping> getPageParameterMapping(Configuration configuration, BoundSql boundSql) {
        return boundSql.getParameterMappings();
    }

    @Override // com.github.pagehelper.parser.impl.AbstractParser, com.github.pagehelper.parser.Parser
    public String getPageSql(String sql) {
        Page page = SqlUtil.getLocalPage();
        return pageSql.convertToPageSql(sql, page.getStartRow(), page.getPageSize());
    }

    @Override // com.github.pagehelper.parser.impl.AbstractParser, com.github.pagehelper.parser.Parser
    public Map setPageParameter(MappedStatement ms, Object parameterObject, BoundSql boundSql, Page page) {
        return super.setPageParameter(ms, parameterObject, boundSql, page);
    }
}

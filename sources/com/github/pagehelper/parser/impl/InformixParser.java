package com.github.pagehelper.parser.impl;

import com.github.pagehelper.Constant;
import com.github.pagehelper.Page;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.Configuration;

/* loaded from: pagehelper-3.7.4.jar:com/github/pagehelper/parser/impl/InformixParser.class */
public class InformixParser extends AbstractParser {
    @Override // com.github.pagehelper.parser.impl.AbstractParser, com.github.pagehelper.parser.Parser
    public String getPageSql(String sql) {
        StringBuilder sqlBuilder = new StringBuilder(sql.length() + 40);
        sqlBuilder.append("select skip ? first ? * from ( ");
        sqlBuilder.append(sql);
        sqlBuilder.append(" ) temp_t");
        return sqlBuilder.toString();
    }

    @Override // com.github.pagehelper.parser.impl.AbstractParser, com.github.pagehelper.parser.Parser
    public List<ParameterMapping> getPageParameterMapping(Configuration configuration, BoundSql boundSql) {
        List<ParameterMapping> newParameterMappings = new ArrayList<>();
        newParameterMappings.add(new ParameterMapping.Builder(configuration, Constant.PAGEPARAMETER_FIRST, (Class<?>) Integer.class).build());
        newParameterMappings.add(new ParameterMapping.Builder(configuration, Constant.PAGEPARAMETER_SECOND, (Class<?>) Integer.class).build());
        if (boundSql.getParameterMappings() != null) {
            newParameterMappings.addAll(boundSql.getParameterMappings());
        }
        return newParameterMappings;
    }

    @Override // com.github.pagehelper.parser.impl.AbstractParser, com.github.pagehelper.parser.Parser
    public Map setPageParameter(MappedStatement ms, Object parameterObject, BoundSql boundSql, Page page) {
        Map paramMap = super.setPageParameter(ms, parameterObject, boundSql, page);
        paramMap.put(Constant.PAGEPARAMETER_FIRST, Integer.valueOf(page.getStartRow()));
        paramMap.put(Constant.PAGEPARAMETER_SECOND, Integer.valueOf(page.getPageSize()));
        return paramMap;
    }
}

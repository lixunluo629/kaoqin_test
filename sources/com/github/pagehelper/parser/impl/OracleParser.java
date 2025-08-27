package com.github.pagehelper.parser.impl;

import com.github.pagehelper.Constant;
import com.github.pagehelper.Page;
import java.util.Map;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;

/* loaded from: pagehelper-3.7.4.jar:com/github/pagehelper/parser/impl/OracleParser.class */
public class OracleParser extends AbstractParser {
    @Override // com.github.pagehelper.parser.impl.AbstractParser, com.github.pagehelper.parser.Parser
    public String getPageSql(String sql) {
        StringBuilder sqlBuilder = new StringBuilder(sql.length() + 120);
        sqlBuilder.append("select * from ( select tmp_page.*, rownum row_id from ( ");
        sqlBuilder.append(sql);
        sqlBuilder.append(" ) tmp_page where rownum <= ? ) where row_id > ?");
        return sqlBuilder.toString();
    }

    @Override // com.github.pagehelper.parser.impl.AbstractParser, com.github.pagehelper.parser.Parser
    public Map setPageParameter(MappedStatement ms, Object parameterObject, BoundSql boundSql, Page page) {
        Map paramMap = super.setPageParameter(ms, parameterObject, boundSql, page);
        paramMap.put(Constant.PAGEPARAMETER_FIRST, Integer.valueOf(page.getEndRow()));
        paramMap.put(Constant.PAGEPARAMETER_SECOND, Integer.valueOf(page.getStartRow()));
        return paramMap;
    }
}

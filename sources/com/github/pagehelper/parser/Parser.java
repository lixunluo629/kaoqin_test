package com.github.pagehelper.parser;

import com.github.pagehelper.Page;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.Configuration;

/* loaded from: pagehelper-3.7.4.jar:com/github/pagehelper/parser/Parser.class */
public interface Parser {
    boolean isSupportedMappedStatementCache();

    String getCountSql(String str);

    String getPageSql(String str);

    List<ParameterMapping> getPageParameterMapping(Configuration configuration, BoundSql boundSql);

    Map setPageParameter(MappedStatement mappedStatement, Object obj, BoundSql boundSql, Page page);
}

package com.github.pagehelper;

import java.util.Properties;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

@Intercepts({@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
/* loaded from: pagehelper-3.7.4.jar:com/github/pagehelper/PageHelper.class */
public class PageHelper implements Interceptor {
    private SqlUtil sqlUtil;

    public static Page startPage(int pageNum, int pageSize) {
        return startPage(pageNum, pageSize, true);
    }

    public static Page startPage(int pageNum, int pageSize, boolean count) {
        return startPage(pageNum, pageSize, count, null);
    }

    public static Page startPage(int pageNum, int pageSize, boolean count, Boolean reasonable) {
        return startPage(pageNum, pageSize, count, reasonable, null);
    }

    public static Page startPage(int pageNum, int pageSize, boolean count, Boolean reasonable, Boolean pageSizeZero) {
        Page page = new Page(pageNum, pageSize, count);
        page.setReasonable(reasonable);
        page.setPageSizeZero(pageSizeZero);
        SqlUtil.setLocalPage(page);
        return page;
    }

    public static Page startPage(Object params) {
        Page page = SqlUtil.getPageFromObject(params);
        SqlUtil.setLocalPage(page);
        return page;
    }

    @Override // org.apache.ibatis.plugin.Interceptor
    public Object intercept(Invocation invocation) throws Throwable {
        return this.sqlUtil.processPage(invocation);
    }

    @Override // org.apache.ibatis.plugin.Interceptor
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override // org.apache.ibatis.plugin.Interceptor
    public void setProperties(Properties p) {
        String dialect = p.getProperty("dialect");
        this.sqlUtil = new SqlUtil(dialect);
        this.sqlUtil.setProperties(p);
    }
}

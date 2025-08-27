package com.github.pagehelper;

import com.github.pagehelper.parser.Parser;
import com.github.pagehelper.parser.impl.AbstractParser;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.RowBounds;

/* loaded from: pagehelper-3.7.4.jar:com/github/pagehelper/SqlUtil.class */
public class SqlUtil implements Constant {
    private boolean offsetAsPageNum = false;
    private boolean rowBoundsWithCount = false;
    private boolean pageSizeZero = false;
    private boolean reasonable = false;
    private static Boolean hasRequest;
    private static Class<?> requestClass;
    private static Method getParameterMap;
    private Parser parser;
    private MSUtils msUtils;
    private Dialect dialect;
    private static final ThreadLocal<Page> LOCAL_PAGE = new ThreadLocal<>();
    private static Map<String, String> PARAMS = new HashMap(5);

    static {
        try {
            requestClass = Class.forName("javax.servlet.ServletRequest");
            getParameterMap = requestClass.getMethod("getParameterMap", new Class[0]);
            hasRequest = true;
        } catch (Exception e) {
            hasRequest = false;
        }
    }

    public SqlUtil(String strDialect) {
        if (strDialect == null || "".equals(strDialect)) {
            throw new IllegalArgumentException("Mybatis分页插件无法获取dialect参数!");
        }
        this.dialect = Dialect.of(strDialect);
        this.parser = AbstractParser.newParser(this.dialect);
        this.msUtils = new MSUtils(this.parser);
    }

    public static void setLocalPage(Page page) {
        LOCAL_PAGE.set(page);
    }

    public static Page getLocalPage() {
        return LOCAL_PAGE.get();
    }

    public static void clearLocalPage() {
        LOCAL_PAGE.remove();
    }

    public Page getPage(Object params) {
        Page page = getLocalPage();
        if (page == null) {
            if (params instanceof RowBounds) {
                RowBounds rowBounds = (RowBounds) params;
                if (this.offsetAsPageNum) {
                    page = new Page(rowBounds.getOffset(), rowBounds.getLimit(), this.rowBoundsWithCount);
                } else {
                    page = new Page(rowBounds, this.rowBoundsWithCount);
                    page.setReasonable(false);
                }
            } else {
                page = getPageFromObject(params);
            }
            setLocalPage(page);
        }
        if (page.getReasonable() == null) {
            page.setReasonable(Boolean.valueOf(this.reasonable));
        }
        if (page.getPageSizeZero() == null) {
            page.setPageSizeZero(Boolean.valueOf(this.pageSizeZero));
        }
        return page;
    }

    public static Page getPageFromObject(Object params) {
        MetaObject paramsObject = null;
        if (params == null) {
            throw new NullPointerException("分页查询参数params不能为空!");
        }
        if (hasRequest.booleanValue() && requestClass.isAssignableFrom(params.getClass())) {
            try {
                paramsObject = SystemMetaObject.forObject(getParameterMap.invoke(params, new Object[0]));
            } catch (Exception e) {
            }
        } else {
            paramsObject = SystemMetaObject.forObject(params);
        }
        if (paramsObject == null) {
            throw new NullPointerException("分页查询参数params处理失败!");
        }
        try {
            int pageNum = Integer.parseInt(String.valueOf(getParamValue(paramsObject, "pageNum", true)));
            int pageSize = Integer.parseInt(String.valueOf(getParamValue(paramsObject, "pageSize", true)));
            Object _count = getParamValue(paramsObject, "count", false);
            boolean count = true;
            if (_count != null) {
                count = Boolean.valueOf(String.valueOf(_count)).booleanValue();
            }
            Page page = new Page(pageNum, pageSize, count);
            Object reasonable = getParamValue(paramsObject, "reasonable", false);
            if (reasonable != null) {
                page.setReasonable(Boolean.valueOf(String.valueOf(reasonable)));
            }
            Object pageSizeZero = getParamValue(paramsObject, "pageSizeZero", false);
            if (pageSizeZero != null) {
                page.setPageSizeZero(Boolean.valueOf(String.valueOf(pageSizeZero)));
            }
            return page;
        } catch (NumberFormatException e2) {
            throw new IllegalArgumentException("分页参数不是合法的数字类型!");
        }
    }

    public static Object getParamValue(MetaObject paramsObject, String paramName, boolean required) {
        Object value = null;
        if (paramsObject.hasGetter(PARAMS.get(paramName))) {
            value = paramsObject.getValue(PARAMS.get(paramName));
        }
        if (required && value == null) {
            throw new RuntimeException("分页查询缺少必要的参数:" + PARAMS.get(paramName));
        }
        return value;
    }

    public Object processPage(Invocation invocation) throws Throwable {
        try {
            Object result = _processPage(invocation);
            clearLocalPage();
            return result;
        } catch (Throwable th) {
            clearLocalPage();
            throw th;
        }
    }

    private Object _processPage(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        RowBounds rowBounds = (RowBounds) args[2];
        if (getLocalPage() == null && rowBounds == RowBounds.DEFAULT) {
            return invocation.proceed();
        }
        args[2] = RowBounds.DEFAULT;
        Page page = getPage(rowBounds);
        if (page.getPageSizeZero() != null && page.getPageSizeZero().booleanValue() && page.getPageSize() == 0) {
            Object result = invocation.proceed();
            page.addAll((List) result);
            page.setPageNum(1);
            page.setPageSize(page.size());
            page.setTotal(page.size());
            return page;
        }
        MappedStatement ms = (MappedStatement) args[0];
        SqlSource sqlSource = ms.getSqlSource();
        if (page.isCount()) {
            this.msUtils.processCountMappedStatement(ms, sqlSource, args);
            Object result2 = invocation.proceed();
            page.setTotal(((Integer) ((List) result2).get(0)).intValue());
            if (page.getTotal() == 0) {
                return page;
            }
        }
        if (page.getPageSize() > 0 && ((rowBounds == RowBounds.DEFAULT && page.getPageNum() > 0) || rowBounds != RowBounds.DEFAULT)) {
            this.msUtils.processPageMappedStatement(ms, sqlSource, page, args);
            Object result3 = invocation.proceed();
            page.addAll((List) result3);
        }
        return page;
    }

    public void setProperties(Properties p) {
        String offsetAsPageNum = p.getProperty("offsetAsPageNum");
        this.offsetAsPageNum = Boolean.parseBoolean(offsetAsPageNum);
        String rowBoundsWithCount = p.getProperty("rowBoundsWithCount");
        this.rowBoundsWithCount = Boolean.parseBoolean(rowBoundsWithCount);
        String pageSizeZero = p.getProperty("pageSizeZero");
        this.pageSizeZero = Boolean.parseBoolean(pageSizeZero);
        String reasonable = p.getProperty("reasonable");
        this.reasonable = Boolean.parseBoolean(reasonable);
        PARAMS.put("pageNum", "pageNum");
        PARAMS.put("pageSize", "pageSize");
        PARAMS.put("count", "countSql");
        PARAMS.put("reasonable", "reasonable");
        PARAMS.put("pageSizeZero", "pageSizeZero");
        String params = p.getProperty("params");
        if (params != null && params.length() > 0) {
            String[] ps = params.split("[;|,|&]");
            for (String s : ps) {
                String[] ss = s.split("[=|:]");
                if (ss.length == 2) {
                    PARAMS.put(ss[0], ss[1]);
                }
            }
        }
    }

    public static void testSql(String dialect, String originalSql) {
        testSql(Dialect.of(dialect), originalSql);
    }

    public static void testSql(Dialect dialect, String originalSql) {
        Parser parser = AbstractParser.newParser(dialect);
        if (dialect == Dialect.sqlserver) {
            setLocalPage(new Page(1, 10));
        }
        String countSql = parser.getCountSql(originalSql);
        System.out.println(countSql);
        String pageSql = parser.getPageSql(originalSql);
        System.out.println(pageSql);
        if (dialect == Dialect.sqlserver) {
            clearLocalPage();
        }
    }
}

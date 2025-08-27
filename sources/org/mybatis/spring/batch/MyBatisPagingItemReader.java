package org.mybatis.spring.batch;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/* loaded from: mybatis-spring-1.3.2.jar:org/mybatis/spring/batch/MyBatisPagingItemReader.class */
public class MyBatisPagingItemReader<T> extends AbstractPagingItemReader<T> {
    private String queryId;
    private SqlSessionFactory sqlSessionFactory;
    private SqlSessionTemplate sqlSessionTemplate;
    private Map<String, Object> parameterValues;

    public MyBatisPagingItemReader() {
        setName(ClassUtils.getShortName((Class<?>) MyBatisPagingItemReader.class));
    }

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public void setParameterValues(Map<String, Object> parameterValues) {
        this.parameterValues = parameterValues;
    }

    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        Assert.notNull(this.sqlSessionFactory, "A SqlSessionFactory is required.");
        this.sqlSessionTemplate = new SqlSessionTemplate(this.sqlSessionFactory, ExecutorType.BATCH);
        Assert.notNull(this.queryId, "A queryId is required.");
    }

    protected void doReadPage() {
        Map<String, Object> parameters = new HashMap<>();
        if (this.parameterValues != null) {
            parameters.putAll(this.parameterValues);
        }
        parameters.put("_page", Integer.valueOf(getPage()));
        parameters.put("_pagesize", Integer.valueOf(getPageSize()));
        parameters.put("_skiprows", Integer.valueOf(getPage() * getPageSize()));
        if (this.results == null) {
            this.results = new CopyOnWriteArrayList();
        } else {
            this.results.clear();
        }
        this.results.addAll(this.sqlSessionTemplate.selectList(this.queryId, parameters));
    }

    protected void doJumpToPage(int itemIndex) {
    }
}

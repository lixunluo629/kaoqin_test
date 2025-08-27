package org.mybatis.spring.batch;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/* loaded from: mybatis-spring-1.3.2.jar:org/mybatis/spring/batch/MyBatisCursorItemReader.class */
public class MyBatisCursorItemReader<T> extends AbstractItemCountingItemStreamItemReader<T> implements InitializingBean {
    private String queryId;
    private SqlSessionFactory sqlSessionFactory;
    private SqlSession sqlSession;
    private Map<String, Object> parameterValues;
    private Cursor<T> cursor;
    private Iterator<T> cursorIterator;

    public MyBatisCursorItemReader() {
        setName(ClassUtils.getShortName((Class<?>) MyBatisCursorItemReader.class));
    }

    protected T doRead() throws Exception {
        T next = null;
        if (this.cursorIterator.hasNext()) {
            next = this.cursorIterator.next();
        }
        return next;
    }

    protected void doOpen() throws Exception {
        Map<String, Object> parameters = new HashMap<>();
        if (this.parameterValues != null) {
            parameters.putAll(this.parameterValues);
        }
        this.sqlSession = this.sqlSessionFactory.openSession(ExecutorType.SIMPLE);
        this.cursor = this.sqlSession.selectCursor(this.queryId, parameters);
        this.cursorIterator = this.cursor.iterator();
    }

    protected void doClose() throws Exception {
        if (this.cursor != null) {
            this.cursor.close();
        }
        if (this.sqlSession != null) {
            this.sqlSession.close();
        }
        this.cursorIterator = null;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.sqlSessionFactory, "A SqlSessionFactory is required.");
        Assert.notNull(this.queryId, "A queryId is required.");
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
}

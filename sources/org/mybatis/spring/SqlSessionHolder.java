package org.mybatis.spring;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.transaction.support.ResourceHolderSupport;
import org.springframework.util.Assert;

/* loaded from: mybatis-spring-1.3.2.jar:org/mybatis/spring/SqlSessionHolder.class */
public final class SqlSessionHolder extends ResourceHolderSupport {
    private final SqlSession sqlSession;
    private final ExecutorType executorType;
    private final PersistenceExceptionTranslator exceptionTranslator;

    public SqlSessionHolder(SqlSession sqlSession, ExecutorType executorType, PersistenceExceptionTranslator exceptionTranslator) {
        Assert.notNull(sqlSession, "SqlSession must not be null");
        Assert.notNull(executorType, "ExecutorType must not be null");
        this.sqlSession = sqlSession;
        this.executorType = executorType;
        this.exceptionTranslator = exceptionTranslator;
    }

    public SqlSession getSqlSession() {
        return this.sqlSession;
    }

    public ExecutorType getExecutorType() {
        return this.executorType;
    }

    public PersistenceExceptionTranslator getPersistenceExceptionTranslator() {
        return this.exceptionTranslator;
    }
}

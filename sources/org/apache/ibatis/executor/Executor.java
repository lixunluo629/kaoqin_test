package org.apache.ibatis.executor;

import java.sql.SQLException;
import java.util.List;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.transaction.Transaction;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/executor/Executor.class */
public interface Executor {
    public static final ResultHandler NO_RESULT_HANDLER = null;

    int update(MappedStatement mappedStatement, Object obj) throws SQLException;

    <E> List<E> query(MappedStatement mappedStatement, Object obj, RowBounds rowBounds, ResultHandler resultHandler, CacheKey cacheKey, BoundSql boundSql) throws SQLException;

    <E> List<E> query(MappedStatement mappedStatement, Object obj, RowBounds rowBounds, ResultHandler resultHandler) throws SQLException;

    <E> Cursor<E> queryCursor(MappedStatement mappedStatement, Object obj, RowBounds rowBounds) throws SQLException;

    List<BatchResult> flushStatements() throws SQLException;

    void commit(boolean z) throws SQLException;

    void rollback(boolean z) throws SQLException;

    CacheKey createCacheKey(MappedStatement mappedStatement, Object obj, RowBounds rowBounds, BoundSql boundSql);

    boolean isCached(MappedStatement mappedStatement, CacheKey cacheKey);

    void clearLocalCache();

    void deferLoad(MappedStatement mappedStatement, MetaObject metaObject, String str, CacheKey cacheKey, Class<?> cls);

    Transaction getTransaction();

    void close(boolean z);

    boolean isClosed();

    void setExecutorWrapper(Executor executor);
}

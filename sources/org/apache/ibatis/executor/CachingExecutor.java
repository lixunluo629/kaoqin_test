package org.apache.ibatis.executor;

import java.sql.SQLException;
import java.util.List;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.cache.TransactionalCacheManager;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.transaction.Transaction;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/executor/CachingExecutor.class */
public class CachingExecutor implements Executor {
    private final Executor delegate;
    private final TransactionalCacheManager tcm = new TransactionalCacheManager();

    public CachingExecutor(Executor delegate) {
        this.delegate = delegate;
        delegate.setExecutorWrapper(this);
    }

    @Override // org.apache.ibatis.executor.Executor
    public Transaction getTransaction() {
        return this.delegate.getTransaction();
    }

    @Override // org.apache.ibatis.executor.Executor
    public void close(boolean forceRollback) {
        try {
            if (forceRollback) {
                this.tcm.rollback();
            } else {
                this.tcm.commit();
            }
        } finally {
            this.delegate.close(forceRollback);
        }
    }

    @Override // org.apache.ibatis.executor.Executor
    public boolean isClosed() {
        return this.delegate.isClosed();
    }

    @Override // org.apache.ibatis.executor.Executor
    public int update(MappedStatement ms, Object parameterObject) throws SQLException {
        flushCacheIfRequired(ms);
        return this.delegate.update(ms, parameterObject);
    }

    @Override // org.apache.ibatis.executor.Executor
    public <E> List<E> query(MappedStatement ms, Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler) throws SQLException {
        BoundSql boundSql = ms.getBoundSql(parameterObject);
        CacheKey key = createCacheKey(ms, parameterObject, rowBounds, boundSql);
        return query(ms, parameterObject, rowBounds, resultHandler, key, boundSql);
    }

    @Override // org.apache.ibatis.executor.Executor
    public <E> Cursor<E> queryCursor(MappedStatement ms, Object parameter, RowBounds rowBounds) throws SQLException {
        flushCacheIfRequired(ms);
        return this.delegate.queryCursor(ms, parameter, rowBounds);
    }

    @Override // org.apache.ibatis.executor.Executor
    public <E> List<E> query(MappedStatement ms, Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler, CacheKey key, BoundSql boundSql) throws SQLException {
        Cache cache = ms.getCache();
        if (cache != null) {
            flushCacheIfRequired(ms);
            if (ms.isUseCache() && resultHandler == null) {
                ensureNoOutParams(ms, boundSql);
                List<E> list = (List) this.tcm.getObject(cache, key);
                if (list == null) {
                    list = this.delegate.query(ms, parameterObject, rowBounds, resultHandler, key, boundSql);
                    this.tcm.putObject(cache, key, list);
                }
                return list;
            }
        }
        return this.delegate.query(ms, parameterObject, rowBounds, resultHandler, key, boundSql);
    }

    @Override // org.apache.ibatis.executor.Executor
    public List<BatchResult> flushStatements() throws SQLException {
        return this.delegate.flushStatements();
    }

    @Override // org.apache.ibatis.executor.Executor
    public void commit(boolean required) throws SQLException {
        this.delegate.commit(required);
        this.tcm.commit();
    }

    @Override // org.apache.ibatis.executor.Executor
    public void rollback(boolean required) throws SQLException {
        try {
            this.delegate.rollback(required);
            if (required) {
                this.tcm.rollback();
            }
        } catch (Throwable th) {
            if (required) {
                this.tcm.rollback();
            }
            throw th;
        }
    }

    private void ensureNoOutParams(MappedStatement ms, BoundSql boundSql) {
        if (ms.getStatementType() == StatementType.CALLABLE) {
            for (ParameterMapping parameterMapping : boundSql.getParameterMappings()) {
                if (parameterMapping.getMode() != ParameterMode.IN) {
                    throw new ExecutorException("Caching stored procedures with OUT params is not supported.  Please configure useCache=false in " + ms.getId() + " statement.");
                }
            }
        }
    }

    @Override // org.apache.ibatis.executor.Executor
    public CacheKey createCacheKey(MappedStatement ms, Object parameterObject, RowBounds rowBounds, BoundSql boundSql) {
        return this.delegate.createCacheKey(ms, parameterObject, rowBounds, boundSql);
    }

    @Override // org.apache.ibatis.executor.Executor
    public boolean isCached(MappedStatement ms, CacheKey key) {
        return this.delegate.isCached(ms, key);
    }

    @Override // org.apache.ibatis.executor.Executor
    public void deferLoad(MappedStatement ms, MetaObject resultObject, String property, CacheKey key, Class<?> targetType) {
        this.delegate.deferLoad(ms, resultObject, property, key, targetType);
    }

    @Override // org.apache.ibatis.executor.Executor
    public void clearLocalCache() {
        this.delegate.clearLocalCache();
    }

    private void flushCacheIfRequired(MappedStatement ms) {
        Cache cache = ms.getCache();
        if (cache != null && ms.isFlushCacheRequired()) {
            this.tcm.clear(cache);
        }
    }

    @Override // org.apache.ibatis.executor.Executor
    public void setExecutorWrapper(Executor executor) {
        throw new UnsupportedOperationException("This method should not be called");
    }
}

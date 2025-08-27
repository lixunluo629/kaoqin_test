package org.apache.ibatis.executor;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.statement.StatementUtil;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.logging.jdbc.ConnectionLogger;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.LocalCacheScope;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.type.TypeHandlerRegistry;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/executor/BaseExecutor.class */
public abstract class BaseExecutor implements Executor {
    private static final Log log = LogFactory.getLog((Class<?>) BaseExecutor.class);
    protected Transaction transaction;
    protected Configuration configuration;
    protected int queryStack;
    protected ConcurrentLinkedQueue<DeferredLoad> deferredLoads = new ConcurrentLinkedQueue<>();
    protected PerpetualCache localCache = new PerpetualCache("LocalCache");
    protected PerpetualCache localOutputParameterCache = new PerpetualCache("LocalOutputParameterCache");
    private boolean closed = false;
    protected Executor wrapper = this;

    protected abstract int doUpdate(MappedStatement mappedStatement, Object obj) throws SQLException;

    protected abstract List<BatchResult> doFlushStatements(boolean z) throws SQLException;

    protected abstract <E> List<E> doQuery(MappedStatement mappedStatement, Object obj, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException;

    protected abstract <E> Cursor<E> doQueryCursor(MappedStatement mappedStatement, Object obj, RowBounds rowBounds, BoundSql boundSql) throws SQLException;

    protected BaseExecutor(Configuration configuration, Transaction transaction) {
        this.transaction = transaction;
        this.configuration = configuration;
    }

    @Override // org.apache.ibatis.executor.Executor
    public Transaction getTransaction() {
        if (this.closed) {
            throw new ExecutorException("Executor was closed.");
        }
        return this.transaction;
    }

    /* JADX WARN: Finally extract failed */
    @Override // org.apache.ibatis.executor.Executor
    public void close(boolean forceRollback) {
        try {
            try {
                rollback(forceRollback);
                if (this.transaction != null) {
                    this.transaction.close();
                }
            } catch (Throwable th) {
                if (this.transaction != null) {
                    this.transaction.close();
                }
                throw th;
            }
        } catch (SQLException e) {
            log.warn("Unexpected exception on closing transaction.  Cause: " + e);
        } finally {
            this.transaction = null;
            this.deferredLoads = null;
            this.localCache = null;
            this.localOutputParameterCache = null;
            this.closed = true;
        }
    }

    @Override // org.apache.ibatis.executor.Executor
    public boolean isClosed() {
        return this.closed;
    }

    @Override // org.apache.ibatis.executor.Executor
    public int update(MappedStatement ms, Object parameter) throws SQLException {
        ErrorContext.instance().resource(ms.getResource()).activity("executing an update").object(ms.getId());
        if (this.closed) {
            throw new ExecutorException("Executor was closed.");
        }
        clearLocalCache();
        return doUpdate(ms, parameter);
    }

    @Override // org.apache.ibatis.executor.Executor
    public List<BatchResult> flushStatements() throws SQLException {
        return flushStatements(false);
    }

    public List<BatchResult> flushStatements(boolean isRollBack) throws SQLException {
        if (this.closed) {
            throw new ExecutorException("Executor was closed.");
        }
        return doFlushStatements(isRollBack);
    }

    @Override // org.apache.ibatis.executor.Executor
    public <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler) throws SQLException {
        BoundSql boundSql = ms.getBoundSql(parameter);
        CacheKey key = createCacheKey(ms, parameter, rowBounds, boundSql);
        return query(ms, parameter, rowBounds, resultHandler, key, boundSql);
    }

    @Override // org.apache.ibatis.executor.Executor
    public <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, CacheKey key, BoundSql boundSql) throws SQLException {
        ErrorContext.instance().resource(ms.getResource()).activity("executing a query").object(ms.getId());
        if (this.closed) {
            throw new ExecutorException("Executor was closed.");
        }
        if (this.queryStack == 0 && ms.isFlushCacheRequired()) {
            clearLocalCache();
        }
        try {
            this.queryStack++;
            List<E> list = resultHandler == null ? (List) this.localCache.getObject(key) : null;
            if (list != null) {
                handleLocallyCachedOutputParameters(ms, key, parameter, boundSql);
            } else {
                list = queryFromDatabase(ms, parameter, rowBounds, resultHandler, key, boundSql);
            }
            if (this.queryStack == 0) {
                Iterator<DeferredLoad> it = this.deferredLoads.iterator();
                while (it.hasNext()) {
                    DeferredLoad deferredLoad = it.next();
                    deferredLoad.load();
                }
                this.deferredLoads.clear();
                if (this.configuration.getLocalCacheScope() == LocalCacheScope.STATEMENT) {
                    clearLocalCache();
                }
            }
            return list;
        } finally {
            this.queryStack--;
        }
    }

    @Override // org.apache.ibatis.executor.Executor
    public <E> Cursor<E> queryCursor(MappedStatement ms, Object parameter, RowBounds rowBounds) throws SQLException {
        BoundSql boundSql = ms.getBoundSql(parameter);
        return doQueryCursor(ms, parameter, rowBounds, boundSql);
    }

    @Override // org.apache.ibatis.executor.Executor
    public void deferLoad(MappedStatement ms, MetaObject resultObject, String property, CacheKey key, Class<?> targetType) {
        if (this.closed) {
            throw new ExecutorException("Executor was closed.");
        }
        DeferredLoad deferredLoad = new DeferredLoad(resultObject, property, key, this.localCache, this.configuration, targetType);
        if (deferredLoad.canLoad()) {
            deferredLoad.load();
        } else {
            this.deferredLoads.add(new DeferredLoad(resultObject, property, key, this.localCache, this.configuration, targetType));
        }
    }

    @Override // org.apache.ibatis.executor.Executor
    public CacheKey createCacheKey(MappedStatement ms, Object parameterObject, RowBounds rowBounds, BoundSql boundSql) {
        Object value;
        if (this.closed) {
            throw new ExecutorException("Executor was closed.");
        }
        CacheKey cacheKey = new CacheKey();
        cacheKey.update(ms.getId());
        cacheKey.update(Integer.valueOf(rowBounds.getOffset()));
        cacheKey.update(Integer.valueOf(rowBounds.getLimit()));
        cacheKey.update(boundSql.getSql());
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        TypeHandlerRegistry typeHandlerRegistry = ms.getConfiguration().getTypeHandlerRegistry();
        for (ParameterMapping parameterMapping : parameterMappings) {
            if (parameterMapping.getMode() != ParameterMode.OUT) {
                String propertyName = parameterMapping.getProperty();
                if (boundSql.hasAdditionalParameter(propertyName)) {
                    value = boundSql.getAdditionalParameter(propertyName);
                } else if (parameterObject == null) {
                    value = null;
                } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                    value = parameterObject;
                } else {
                    MetaObject metaObject = this.configuration.newMetaObject(parameterObject);
                    value = metaObject.getValue(propertyName);
                }
                Object value2 = value;
                cacheKey.update(value2);
            }
        }
        if (this.configuration.getEnvironment() != null) {
            cacheKey.update(this.configuration.getEnvironment().getId());
        }
        return cacheKey;
    }

    @Override // org.apache.ibatis.executor.Executor
    public boolean isCached(MappedStatement ms, CacheKey key) {
        return this.localCache.getObject(key) != null;
    }

    @Override // org.apache.ibatis.executor.Executor
    public void commit(boolean required) throws SQLException {
        if (this.closed) {
            throw new ExecutorException("Cannot commit, transaction is already closed");
        }
        clearLocalCache();
        flushStatements();
        if (required) {
            this.transaction.commit();
        }
    }

    @Override // org.apache.ibatis.executor.Executor
    public void rollback(boolean required) throws SQLException {
        if (!this.closed) {
            try {
                clearLocalCache();
                flushStatements(true);
                if (required) {
                    this.transaction.rollback();
                }
            } catch (Throwable th) {
                if (required) {
                    this.transaction.rollback();
                }
                throw th;
            }
        }
    }

    @Override // org.apache.ibatis.executor.Executor
    public void clearLocalCache() {
        if (!this.closed) {
            this.localCache.clear();
            this.localOutputParameterCache.clear();
        }
    }

    protected void closeStatement(Statement statement) throws SQLException {
        if (statement != null) {
            try {
                if (!statement.isClosed()) {
                    statement.close();
                }
            } catch (SQLException e) {
            }
        }
    }

    protected void applyTransactionTimeout(Statement statement) throws SQLException {
        StatementUtil.applyTransactionTimeout(statement, Integer.valueOf(statement.getQueryTimeout()), this.transaction.getTimeout());
    }

    private void handleLocallyCachedOutputParameters(MappedStatement ms, CacheKey key, Object parameter, BoundSql boundSql) {
        Object cachedParameter;
        if (ms.getStatementType() == StatementType.CALLABLE && (cachedParameter = this.localOutputParameterCache.getObject(key)) != null && parameter != null) {
            MetaObject metaCachedParameter = this.configuration.newMetaObject(cachedParameter);
            MetaObject metaParameter = this.configuration.newMetaObject(parameter);
            for (ParameterMapping parameterMapping : boundSql.getParameterMappings()) {
                if (parameterMapping.getMode() != ParameterMode.IN) {
                    String parameterName = parameterMapping.getProperty();
                    Object cachedValue = metaCachedParameter.getValue(parameterName);
                    metaParameter.setValue(parameterName, cachedValue);
                }
            }
        }
    }

    private <E> List<E> queryFromDatabase(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, CacheKey key, BoundSql boundSql) throws SQLException {
        this.localCache.putObject(key, ExecutionPlaceholder.EXECUTION_PLACEHOLDER);
        try {
            List<E> list = doQuery(ms, parameter, rowBounds, resultHandler, boundSql);
            this.localCache.removeObject(key);
            this.localCache.putObject(key, list);
            if (ms.getStatementType() == StatementType.CALLABLE) {
                this.localOutputParameterCache.putObject(key, parameter);
            }
            return list;
        } catch (Throwable th) {
            this.localCache.removeObject(key);
            throw th;
        }
    }

    protected Connection getConnection(Log statementLog) throws SQLException {
        Connection connection = this.transaction.getConnection();
        if (statementLog.isDebugEnabled()) {
            return ConnectionLogger.newInstance(connection, statementLog, this.queryStack);
        }
        return connection;
    }

    @Override // org.apache.ibatis.executor.Executor
    public void setExecutorWrapper(Executor wrapper) {
        this.wrapper = wrapper;
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/executor/BaseExecutor$DeferredLoad.class */
    private static class DeferredLoad {
        private final MetaObject resultObject;
        private final String property;
        private final Class<?> targetType;
        private final CacheKey key;
        private final PerpetualCache localCache;
        private final ObjectFactory objectFactory;
        private final ResultExtractor resultExtractor;

        public DeferredLoad(MetaObject resultObject, String property, CacheKey key, PerpetualCache localCache, Configuration configuration, Class<?> targetType) {
            this.resultObject = resultObject;
            this.property = property;
            this.key = key;
            this.localCache = localCache;
            this.objectFactory = configuration.getObjectFactory();
            this.resultExtractor = new ResultExtractor(configuration, this.objectFactory);
            this.targetType = targetType;
        }

        public boolean canLoad() {
            return (this.localCache.getObject(this.key) == null || this.localCache.getObject(this.key) == ExecutionPlaceholder.EXECUTION_PLACEHOLDER) ? false : true;
        }

        public void load() {
            List<Object> list = (List) this.localCache.getObject(this.key);
            Object value = this.resultExtractor.extractObjectFromList(list, this.targetType);
            this.resultObject.setValue(this.property, value);
        }
    }
}

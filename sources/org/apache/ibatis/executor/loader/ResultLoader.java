package org.apache.ibatis.executor.loader;

import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.ResultExtractor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.TransactionFactory;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/executor/loader/ResultLoader.class */
public class ResultLoader {
    protected final Configuration configuration;
    protected final Executor executor;
    protected final MappedStatement mappedStatement;
    protected final Object parameterObject;
    protected final Class<?> targetType;
    protected final ObjectFactory objectFactory;
    protected final CacheKey cacheKey;
    protected final BoundSql boundSql;
    protected final ResultExtractor resultExtractor;
    protected final long creatorThreadId = Thread.currentThread().getId();
    protected boolean loaded;
    protected Object resultObject;

    public ResultLoader(Configuration config, Executor executor, MappedStatement mappedStatement, Object parameterObject, Class<?> targetType, CacheKey cacheKey, BoundSql boundSql) {
        this.configuration = config;
        this.executor = executor;
        this.mappedStatement = mappedStatement;
        this.parameterObject = parameterObject;
        this.targetType = targetType;
        this.objectFactory = this.configuration.getObjectFactory();
        this.cacheKey = cacheKey;
        this.boundSql = boundSql;
        this.resultExtractor = new ResultExtractor(this.configuration, this.objectFactory);
    }

    public Object loadResult() throws SQLException {
        List<Object> list = selectList();
        this.resultObject = this.resultExtractor.extractObjectFromList(list, this.targetType);
        return this.resultObject;
    }

    private <E> List<E> selectList() throws SQLException {
        Executor localExecutor = this.executor;
        if (Thread.currentThread().getId() != this.creatorThreadId || localExecutor.isClosed()) {
            localExecutor = newExecutor();
        }
        try {
            List<E> listQuery = localExecutor.query(this.mappedStatement, this.parameterObject, RowBounds.DEFAULT, Executor.NO_RESULT_HANDLER, this.cacheKey, this.boundSql);
            if (localExecutor != this.executor) {
                localExecutor.close(false);
            }
            return listQuery;
        } catch (Throwable th) {
            if (localExecutor != this.executor) {
                localExecutor.close(false);
            }
            throw th;
        }
    }

    private Executor newExecutor() {
        Environment environment = this.configuration.getEnvironment();
        if (environment == null) {
            throw new ExecutorException("ResultLoader could not load lazily.  Environment was not configured.");
        }
        DataSource ds = environment.getDataSource();
        if (ds == null) {
            throw new ExecutorException("ResultLoader could not load lazily.  DataSource was not configured.");
        }
        TransactionFactory transactionFactory = environment.getTransactionFactory();
        Transaction tx = transactionFactory.newTransaction(ds, null, false);
        return this.configuration.newExecutor(tx, ExecutorType.SIMPLE);
    }

    public boolean wasNull() {
        return this.resultObject == null;
    }
}

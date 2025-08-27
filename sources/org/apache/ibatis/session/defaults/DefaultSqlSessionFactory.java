package org.apache.ibatis.session.defaults;

import java.sql.Connection;
import java.sql.SQLException;
import org.apache.ibatis.exceptions.ExceptionFactory;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.TransactionIsolationLevel;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/session/defaults/DefaultSqlSessionFactory.class */
public class DefaultSqlSessionFactory implements SqlSessionFactory {
    private final Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override // org.apache.ibatis.session.SqlSessionFactory
    public SqlSession openSession() {
        return openSessionFromDataSource(this.configuration.getDefaultExecutorType(), null, false);
    }

    @Override // org.apache.ibatis.session.SqlSessionFactory
    public SqlSession openSession(boolean autoCommit) {
        return openSessionFromDataSource(this.configuration.getDefaultExecutorType(), null, autoCommit);
    }

    @Override // org.apache.ibatis.session.SqlSessionFactory
    public SqlSession openSession(ExecutorType execType) {
        return openSessionFromDataSource(execType, null, false);
    }

    @Override // org.apache.ibatis.session.SqlSessionFactory
    public SqlSession openSession(TransactionIsolationLevel level) {
        return openSessionFromDataSource(this.configuration.getDefaultExecutorType(), level, false);
    }

    @Override // org.apache.ibatis.session.SqlSessionFactory
    public SqlSession openSession(ExecutorType execType, TransactionIsolationLevel level) {
        return openSessionFromDataSource(execType, level, false);
    }

    @Override // org.apache.ibatis.session.SqlSessionFactory
    public SqlSession openSession(ExecutorType execType, boolean autoCommit) {
        return openSessionFromDataSource(execType, null, autoCommit);
    }

    @Override // org.apache.ibatis.session.SqlSessionFactory
    public SqlSession openSession(Connection connection) {
        return openSessionFromConnection(this.configuration.getDefaultExecutorType(), connection);
    }

    @Override // org.apache.ibatis.session.SqlSessionFactory
    public SqlSession openSession(ExecutorType execType, Connection connection) {
        return openSessionFromConnection(execType, connection);
    }

    @Override // org.apache.ibatis.session.SqlSessionFactory, org.apache.ibatis.session.SqlSession
    public Configuration getConfiguration() {
        return this.configuration;
    }

    private SqlSession openSessionFromDataSource(ExecutorType execType, TransactionIsolationLevel level, boolean autoCommit) {
        Transaction tx = null;
        try {
            try {
                Environment environment = this.configuration.getEnvironment();
                TransactionFactory transactionFactory = getTransactionFactoryFromEnvironment(environment);
                tx = transactionFactory.newTransaction(environment.getDataSource(), level, autoCommit);
                Executor executor = this.configuration.newExecutor(tx, execType);
                DefaultSqlSession defaultSqlSession = new DefaultSqlSession(this.configuration, executor, autoCommit);
                ErrorContext.instance().reset();
                return defaultSqlSession;
            } catch (Exception e) {
                closeTransaction(tx);
                throw ExceptionFactory.wrapException("Error opening session.  Cause: " + e, e);
            }
        } catch (Throwable th) {
            ErrorContext.instance().reset();
            throw th;
        }
    }

    private SqlSession openSessionFromConnection(ExecutorType execType, Connection connection) {
        boolean autoCommit;
        try {
            try {
                try {
                    autoCommit = connection.getAutoCommit();
                } catch (SQLException e) {
                    autoCommit = true;
                }
                Environment environment = this.configuration.getEnvironment();
                TransactionFactory transactionFactory = getTransactionFactoryFromEnvironment(environment);
                Transaction tx = transactionFactory.newTransaction(connection);
                Executor executor = this.configuration.newExecutor(tx, execType);
                DefaultSqlSession defaultSqlSession = new DefaultSqlSession(this.configuration, executor, autoCommit);
                ErrorContext.instance().reset();
                return defaultSqlSession;
            } catch (Exception e2) {
                throw ExceptionFactory.wrapException("Error opening session.  Cause: " + e2, e2);
            }
        } catch (Throwable th) {
            ErrorContext.instance().reset();
            throw th;
        }
    }

    private TransactionFactory getTransactionFactoryFromEnvironment(Environment environment) {
        if (environment == null || environment.getTransactionFactory() == null) {
            return new ManagedTransactionFactory();
        }
        return environment.getTransactionFactory();
    }

    private void closeTransaction(Transaction tx) {
        if (tx != null) {
            try {
                tx.close();
            } catch (SQLException e) {
            }
        }
    }
}

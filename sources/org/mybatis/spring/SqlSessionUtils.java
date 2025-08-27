package org.mybatis.spring;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

/* loaded from: mybatis-spring-1.3.2.jar:org/mybatis/spring/SqlSessionUtils.class */
public final class SqlSessionUtils {
    private static final Log LOGGER = LogFactory.getLog((Class<?>) SqlSessionUtils.class);
    private static final String NO_EXECUTOR_TYPE_SPECIFIED = "No ExecutorType specified";
    private static final String NO_SQL_SESSION_FACTORY_SPECIFIED = "No SqlSessionFactory specified";
    private static final String NO_SQL_SESSION_SPECIFIED = "No SqlSession specified";

    private SqlSessionUtils() {
    }

    public static SqlSession getSqlSession(SqlSessionFactory sessionFactory) {
        ExecutorType executorType = sessionFactory.getConfiguration().getDefaultExecutorType();
        return getSqlSession(sessionFactory, executorType, null);
    }

    public static SqlSession getSqlSession(SqlSessionFactory sessionFactory, ExecutorType executorType, PersistenceExceptionTranslator exceptionTranslator) throws IllegalStateException {
        Assert.notNull(sessionFactory, NO_SQL_SESSION_FACTORY_SPECIFIED);
        Assert.notNull(executorType, NO_EXECUTOR_TYPE_SPECIFIED);
        SqlSessionHolder holder = (SqlSessionHolder) TransactionSynchronizationManager.getResource(sessionFactory);
        SqlSession session = sessionHolder(executorType, holder);
        if (session != null) {
            return session;
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Creating a new SqlSession");
        }
        SqlSession session2 = sessionFactory.openSession(executorType);
        registerSessionHolder(sessionFactory, executorType, exceptionTranslator, session2);
        return session2;
    }

    private static void registerSessionHolder(SqlSessionFactory sessionFactory, ExecutorType executorType, PersistenceExceptionTranslator exceptionTranslator, SqlSession session) throws IllegalStateException {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            Environment environment = sessionFactory.getConfiguration().getEnvironment();
            if (environment.getTransactionFactory() instanceof SpringManagedTransactionFactory) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Registering transaction synchronization for SqlSession [" + session + "]");
                }
                SqlSessionHolder holder = new SqlSessionHolder(session, executorType, exceptionTranslator);
                TransactionSynchronizationManager.bindResource(sessionFactory, holder);
                TransactionSynchronizationManager.registerSynchronization(new SqlSessionSynchronization(holder, sessionFactory));
                holder.setSynchronizedWithTransaction(true);
                holder.requested();
                return;
            }
            if (TransactionSynchronizationManager.getResource(environment.getDataSource()) == null) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("SqlSession [" + session + "] was not registered for synchronization because DataSource is not transactional");
                    return;
                }
                return;
            }
            throw new TransientDataAccessResourceException("SqlSessionFactory must be using a SpringManagedTransactionFactory in order to use Spring transaction synchronization");
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("SqlSession [" + session + "] was not registered for synchronization because synchronization is not active");
        }
    }

    private static SqlSession sessionHolder(ExecutorType executorType, SqlSessionHolder holder) {
        SqlSession session = null;
        if (holder != null && holder.isSynchronizedWithTransaction()) {
            if (holder.getExecutorType() != executorType) {
                throw new TransientDataAccessResourceException("Cannot change the ExecutorType when there is an existing transaction");
            }
            holder.requested();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Fetched SqlSession [" + holder.getSqlSession() + "] from current transaction");
            }
            session = holder.getSqlSession();
        }
        return session;
    }

    public static void closeSqlSession(SqlSession session, SqlSessionFactory sessionFactory) {
        Assert.notNull(session, NO_SQL_SESSION_SPECIFIED);
        Assert.notNull(sessionFactory, NO_SQL_SESSION_FACTORY_SPECIFIED);
        SqlSessionHolder holder = (SqlSessionHolder) TransactionSynchronizationManager.getResource(sessionFactory);
        if (holder != null && holder.getSqlSession() == session) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Releasing transactional SqlSession [" + session + "]");
            }
            holder.released();
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Closing non transactional SqlSession [" + session + "]");
            }
            session.close();
        }
    }

    public static boolean isSqlSessionTransactional(SqlSession session, SqlSessionFactory sessionFactory) {
        Assert.notNull(session, NO_SQL_SESSION_SPECIFIED);
        Assert.notNull(sessionFactory, NO_SQL_SESSION_FACTORY_SPECIFIED);
        SqlSessionHolder holder = (SqlSessionHolder) TransactionSynchronizationManager.getResource(sessionFactory);
        return holder != null && holder.getSqlSession() == session;
    }

    /* loaded from: mybatis-spring-1.3.2.jar:org/mybatis/spring/SqlSessionUtils$SqlSessionSynchronization.class */
    private static final class SqlSessionSynchronization extends TransactionSynchronizationAdapter {
        private final SqlSessionHolder holder;
        private final SqlSessionFactory sessionFactory;
        private boolean holderActive = true;

        public SqlSessionSynchronization(SqlSessionHolder holder, SqlSessionFactory sessionFactory) {
            Assert.notNull(holder, "Parameter 'holder' must be not null");
            Assert.notNull(sessionFactory, "Parameter 'sessionFactory' must be not null");
            this.holder = holder;
            this.sessionFactory = sessionFactory;
        }

        @Override // org.springframework.transaction.support.TransactionSynchronizationAdapter, org.springframework.core.Ordered
        public int getOrder() {
            return 999;
        }

        @Override // org.springframework.transaction.support.TransactionSynchronizationAdapter, org.springframework.transaction.support.TransactionSynchronization
        public void suspend() throws IllegalStateException {
            if (this.holderActive) {
                if (SqlSessionUtils.LOGGER.isDebugEnabled()) {
                    SqlSessionUtils.LOGGER.debug("Transaction synchronization suspending SqlSession [" + this.holder.getSqlSession() + "]");
                }
                TransactionSynchronizationManager.unbindResource(this.sessionFactory);
            }
        }

        @Override // org.springframework.transaction.support.TransactionSynchronizationAdapter, org.springframework.transaction.support.TransactionSynchronization
        public void resume() throws IllegalStateException {
            if (this.holderActive) {
                if (SqlSessionUtils.LOGGER.isDebugEnabled()) {
                    SqlSessionUtils.LOGGER.debug("Transaction synchronization resuming SqlSession [" + this.holder.getSqlSession() + "]");
                }
                TransactionSynchronizationManager.bindResource(this.sessionFactory, this.holder);
            }
        }

        @Override // org.springframework.transaction.support.TransactionSynchronizationAdapter, org.springframework.transaction.support.TransactionSynchronization
        public void beforeCommit(boolean readOnly) {
            DataAccessException translated;
            if (TransactionSynchronizationManager.isActualTransactionActive()) {
                try {
                    if (SqlSessionUtils.LOGGER.isDebugEnabled()) {
                        SqlSessionUtils.LOGGER.debug("Transaction synchronization committing SqlSession [" + this.holder.getSqlSession() + "]");
                    }
                    this.holder.getSqlSession().commit();
                } catch (PersistenceException p) {
                    if (this.holder.getPersistenceExceptionTranslator() != null && (translated = this.holder.getPersistenceExceptionTranslator().translateExceptionIfPossible(p)) != null) {
                        throw translated;
                    }
                    throw p;
                }
            }
        }

        @Override // org.springframework.transaction.support.TransactionSynchronizationAdapter, org.springframework.transaction.support.TransactionSynchronization
        public void beforeCompletion() throws IllegalStateException {
            if (!this.holder.isOpen()) {
                if (SqlSessionUtils.LOGGER.isDebugEnabled()) {
                    SqlSessionUtils.LOGGER.debug("Transaction synchronization deregistering SqlSession [" + this.holder.getSqlSession() + "]");
                }
                TransactionSynchronizationManager.unbindResource(this.sessionFactory);
                this.holderActive = false;
                if (SqlSessionUtils.LOGGER.isDebugEnabled()) {
                    SqlSessionUtils.LOGGER.debug("Transaction synchronization closing SqlSession [" + this.holder.getSqlSession() + "]");
                }
                this.holder.getSqlSession().close();
            }
        }

        @Override // org.springframework.transaction.support.TransactionSynchronizationAdapter, org.springframework.transaction.support.TransactionSynchronization
        public void afterCompletion(int status) {
            if (this.holderActive) {
                if (SqlSessionUtils.LOGGER.isDebugEnabled()) {
                    SqlSessionUtils.LOGGER.debug("Transaction synchronization deregistering SqlSession [" + this.holder.getSqlSession() + "]");
                }
                TransactionSynchronizationManager.unbindResourceIfPossible(this.sessionFactory);
                this.holderActive = false;
                if (SqlSessionUtils.LOGGER.isDebugEnabled()) {
                    SqlSessionUtils.LOGGER.debug("Transaction synchronization closing SqlSession [" + this.holder.getSqlSession() + "]");
                }
                this.holder.getSqlSession().close();
            }
            this.holder.reset();
        }
    }
}

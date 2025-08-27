package org.apache.ibatis.session;

import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.reflection.ExceptionUtil;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/session/SqlSessionManager.class */
public class SqlSessionManager implements SqlSessionFactory, SqlSession {
    private final SqlSessionFactory sqlSessionFactory;
    private final ThreadLocal<SqlSession> localSqlSession = new ThreadLocal<>();
    private final SqlSession sqlSessionProxy = (SqlSession) Proxy.newProxyInstance(SqlSessionFactory.class.getClassLoader(), new Class[]{SqlSession.class}, new SqlSessionInterceptor());

    private SqlSessionManager(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public static SqlSessionManager newInstance(Reader reader) {
        return new SqlSessionManager(new SqlSessionFactoryBuilder().build(reader, (String) null, (Properties) null));
    }

    public static SqlSessionManager newInstance(Reader reader, String environment) {
        return new SqlSessionManager(new SqlSessionFactoryBuilder().build(reader, environment, (Properties) null));
    }

    public static SqlSessionManager newInstance(Reader reader, Properties properties) {
        return new SqlSessionManager(new SqlSessionFactoryBuilder().build(reader, (String) null, properties));
    }

    public static SqlSessionManager newInstance(InputStream inputStream) {
        return new SqlSessionManager(new SqlSessionFactoryBuilder().build(inputStream, (String) null, (Properties) null));
    }

    public static SqlSessionManager newInstance(InputStream inputStream, String environment) {
        return new SqlSessionManager(new SqlSessionFactoryBuilder().build(inputStream, environment, (Properties) null));
    }

    public static SqlSessionManager newInstance(InputStream inputStream, Properties properties) {
        return new SqlSessionManager(new SqlSessionFactoryBuilder().build(inputStream, (String) null, properties));
    }

    public static SqlSessionManager newInstance(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionManager(sqlSessionFactory);
    }

    public void startManagedSession() {
        this.localSqlSession.set(openSession());
    }

    public void startManagedSession(boolean autoCommit) {
        this.localSqlSession.set(openSession(autoCommit));
    }

    public void startManagedSession(Connection connection) {
        this.localSqlSession.set(openSession(connection));
    }

    public void startManagedSession(TransactionIsolationLevel level) {
        this.localSqlSession.set(openSession(level));
    }

    public void startManagedSession(ExecutorType execType) {
        this.localSqlSession.set(openSession(execType));
    }

    public void startManagedSession(ExecutorType execType, boolean autoCommit) {
        this.localSqlSession.set(openSession(execType, autoCommit));
    }

    public void startManagedSession(ExecutorType execType, TransactionIsolationLevel level) {
        this.localSqlSession.set(openSession(execType, level));
    }

    public void startManagedSession(ExecutorType execType, Connection connection) {
        this.localSqlSession.set(openSession(execType, connection));
    }

    public boolean isManagedSessionStarted() {
        return this.localSqlSession.get() != null;
    }

    @Override // org.apache.ibatis.session.SqlSessionFactory
    public SqlSession openSession() {
        return this.sqlSessionFactory.openSession();
    }

    @Override // org.apache.ibatis.session.SqlSessionFactory
    public SqlSession openSession(boolean autoCommit) {
        return this.sqlSessionFactory.openSession(autoCommit);
    }

    @Override // org.apache.ibatis.session.SqlSessionFactory
    public SqlSession openSession(Connection connection) {
        return this.sqlSessionFactory.openSession(connection);
    }

    @Override // org.apache.ibatis.session.SqlSessionFactory
    public SqlSession openSession(TransactionIsolationLevel level) {
        return this.sqlSessionFactory.openSession(level);
    }

    @Override // org.apache.ibatis.session.SqlSessionFactory
    public SqlSession openSession(ExecutorType execType) {
        return this.sqlSessionFactory.openSession(execType);
    }

    @Override // org.apache.ibatis.session.SqlSessionFactory
    public SqlSession openSession(ExecutorType execType, boolean autoCommit) {
        return this.sqlSessionFactory.openSession(execType, autoCommit);
    }

    @Override // org.apache.ibatis.session.SqlSessionFactory
    public SqlSession openSession(ExecutorType execType, TransactionIsolationLevel level) {
        return this.sqlSessionFactory.openSession(execType, level);
    }

    @Override // org.apache.ibatis.session.SqlSessionFactory
    public SqlSession openSession(ExecutorType execType, Connection connection) {
        return this.sqlSessionFactory.openSession(execType, connection);
    }

    @Override // org.apache.ibatis.session.SqlSessionFactory, org.apache.ibatis.session.SqlSession
    public Configuration getConfiguration() {
        return this.sqlSessionFactory.getConfiguration();
    }

    @Override // org.apache.ibatis.session.SqlSession
    public <T> T selectOne(String str) {
        return (T) this.sqlSessionProxy.selectOne(str);
    }

    @Override // org.apache.ibatis.session.SqlSession
    public <T> T selectOne(String str, Object obj) {
        return (T) this.sqlSessionProxy.selectOne(str, obj);
    }

    @Override // org.apache.ibatis.session.SqlSession
    public <K, V> Map<K, V> selectMap(String statement, String mapKey) {
        return this.sqlSessionProxy.selectMap(statement, mapKey);
    }

    @Override // org.apache.ibatis.session.SqlSession
    public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey) {
        return this.sqlSessionProxy.selectMap(statement, parameter, mapKey);
    }

    @Override // org.apache.ibatis.session.SqlSession
    public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey, RowBounds rowBounds) {
        return this.sqlSessionProxy.selectMap(statement, parameter, mapKey, rowBounds);
    }

    @Override // org.apache.ibatis.session.SqlSession
    public <T> Cursor<T> selectCursor(String statement) {
        return this.sqlSessionProxy.selectCursor(statement);
    }

    @Override // org.apache.ibatis.session.SqlSession
    public <T> Cursor<T> selectCursor(String statement, Object parameter) {
        return this.sqlSessionProxy.selectCursor(statement, parameter);
    }

    @Override // org.apache.ibatis.session.SqlSession
    public <T> Cursor<T> selectCursor(String statement, Object parameter, RowBounds rowBounds) {
        return this.sqlSessionProxy.selectCursor(statement, parameter, rowBounds);
    }

    @Override // org.apache.ibatis.session.SqlSession
    public <E> List<E> selectList(String statement) {
        return this.sqlSessionProxy.selectList(statement);
    }

    @Override // org.apache.ibatis.session.SqlSession
    public <E> List<E> selectList(String statement, Object parameter) {
        return this.sqlSessionProxy.selectList(statement, parameter);
    }

    @Override // org.apache.ibatis.session.SqlSession
    public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
        return this.sqlSessionProxy.selectList(statement, parameter, rowBounds);
    }

    @Override // org.apache.ibatis.session.SqlSession
    public void select(String statement, ResultHandler handler) {
        this.sqlSessionProxy.select(statement, handler);
    }

    @Override // org.apache.ibatis.session.SqlSession
    public void select(String statement, Object parameter, ResultHandler handler) {
        this.sqlSessionProxy.select(statement, parameter, handler);
    }

    @Override // org.apache.ibatis.session.SqlSession
    public void select(String statement, Object parameter, RowBounds rowBounds, ResultHandler handler) {
        this.sqlSessionProxy.select(statement, parameter, rowBounds, handler);
    }

    @Override // org.apache.ibatis.session.SqlSession
    public int insert(String statement) {
        return this.sqlSessionProxy.insert(statement);
    }

    @Override // org.apache.ibatis.session.SqlSession
    public int insert(String statement, Object parameter) {
        return this.sqlSessionProxy.insert(statement, parameter);
    }

    @Override // org.apache.ibatis.session.SqlSession
    public int update(String statement) {
        return this.sqlSessionProxy.update(statement);
    }

    @Override // org.apache.ibatis.session.SqlSession
    public int update(String statement, Object parameter) {
        return this.sqlSessionProxy.update(statement, parameter);
    }

    @Override // org.apache.ibatis.session.SqlSession
    public int delete(String statement) {
        return this.sqlSessionProxy.delete(statement);
    }

    @Override // org.apache.ibatis.session.SqlSession
    public int delete(String statement, Object parameter) {
        return this.sqlSessionProxy.delete(statement, parameter);
    }

    @Override // org.apache.ibatis.session.SqlSession
    public <T> T getMapper(Class<T> cls) {
        return (T) getConfiguration().getMapper(cls, this);
    }

    @Override // org.apache.ibatis.session.SqlSession
    public Connection getConnection() {
        SqlSession sqlSession = this.localSqlSession.get();
        if (sqlSession == null) {
            throw new SqlSessionException("Error:  Cannot get connection.  No managed session is started.");
        }
        return sqlSession.getConnection();
    }

    @Override // org.apache.ibatis.session.SqlSession
    public void clearCache() {
        SqlSession sqlSession = this.localSqlSession.get();
        if (sqlSession == null) {
            throw new SqlSessionException("Error:  Cannot clear the cache.  No managed session is started.");
        }
        sqlSession.clearCache();
    }

    @Override // org.apache.ibatis.session.SqlSession
    public void commit() {
        SqlSession sqlSession = this.localSqlSession.get();
        if (sqlSession == null) {
            throw new SqlSessionException("Error:  Cannot commit.  No managed session is started.");
        }
        sqlSession.commit();
    }

    @Override // org.apache.ibatis.session.SqlSession
    public void commit(boolean force) {
        SqlSession sqlSession = this.localSqlSession.get();
        if (sqlSession == null) {
            throw new SqlSessionException("Error:  Cannot commit.  No managed session is started.");
        }
        sqlSession.commit(force);
    }

    @Override // org.apache.ibatis.session.SqlSession
    public void rollback() {
        SqlSession sqlSession = this.localSqlSession.get();
        if (sqlSession == null) {
            throw new SqlSessionException("Error:  Cannot rollback.  No managed session is started.");
        }
        sqlSession.rollback();
    }

    @Override // org.apache.ibatis.session.SqlSession
    public void rollback(boolean force) {
        SqlSession sqlSession = this.localSqlSession.get();
        if (sqlSession == null) {
            throw new SqlSessionException("Error:  Cannot rollback.  No managed session is started.");
        }
        sqlSession.rollback(force);
    }

    @Override // org.apache.ibatis.session.SqlSession
    public List<BatchResult> flushStatements() {
        SqlSession sqlSession = this.localSqlSession.get();
        if (sqlSession == null) {
            throw new SqlSessionException("Error:  Cannot rollback.  No managed session is started.");
        }
        return sqlSession.flushStatements();
    }

    @Override // org.apache.ibatis.session.SqlSession, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        SqlSession sqlSession = this.localSqlSession.get();
        if (sqlSession == null) {
            throw new SqlSessionException("Error:  Cannot close.  No managed session is started.");
        }
        try {
            sqlSession.close();
        } finally {
            this.localSqlSession.set(null);
        }
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/session/SqlSessionManager$SqlSessionInterceptor.class */
    private class SqlSessionInterceptor implements InvocationHandler {
        public SqlSessionInterceptor() {
        }

        @Override // java.lang.reflect.InvocationHandler
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Throwable thUnwrapThrowable;
            SqlSession sqlSession = (SqlSession) SqlSessionManager.this.localSqlSession.get();
            if (sqlSession != null) {
                try {
                    return method.invoke(sqlSession, args);
                } finally {
                    thUnwrapThrowable = ExceptionUtil.unwrapThrowable(t);
                }
            }
            SqlSession autoSqlSession = SqlSessionManager.this.openSession();
            try {
                try {
                    Object result = method.invoke(autoSqlSession, args);
                    autoSqlSession.commit();
                    autoSqlSession.close();
                    return result;
                } catch (Throwable th) {
                    autoSqlSession.close();
                    throw th;
                }
            } finally {
            }
        }
    }
}

package org.apache.ibatis.session.defaults;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.exceptions.ExceptionFactory;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.result.DefaultMapResultHandler;
import org.apache.ibatis.executor.result.DefaultResultContext;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/session/defaults/DefaultSqlSession.class */
public class DefaultSqlSession implements SqlSession {
    private final Configuration configuration;
    private final Executor executor;
    private final boolean autoCommit;
    private boolean dirty;
    private List<Cursor<?>> cursorList;

    public DefaultSqlSession(Configuration configuration, Executor executor, boolean autoCommit) {
        this.configuration = configuration;
        this.executor = executor;
        this.dirty = false;
        this.autoCommit = autoCommit;
    }

    public DefaultSqlSession(Configuration configuration, Executor executor) {
        this(configuration, executor, false);
    }

    @Override // org.apache.ibatis.session.SqlSession
    public <T> T selectOne(String str) {
        return (T) selectOne(str, null);
    }

    @Override // org.apache.ibatis.session.SqlSession
    public <T> T selectOne(String statement, Object parameter) {
        List<T> list = selectList(statement, parameter);
        if (list.size() == 1) {
            return list.get(0);
        }
        if (list.size() > 1) {
            throw new TooManyResultsException("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
        }
        return null;
    }

    @Override // org.apache.ibatis.session.SqlSession
    public <K, V> Map<K, V> selectMap(String statement, String mapKey) {
        return selectMap(statement, null, mapKey, RowBounds.DEFAULT);
    }

    @Override // org.apache.ibatis.session.SqlSession
    public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey) {
        return selectMap(statement, parameter, mapKey, RowBounds.DEFAULT);
    }

    @Override // org.apache.ibatis.session.SqlSession
    public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey, RowBounds rowBounds) {
        List<? extends V> list = selectList(statement, parameter, rowBounds);
        DefaultMapResultHandler<K, V> mapResultHandler = new DefaultMapResultHandler<>(mapKey, this.configuration.getObjectFactory(), this.configuration.getObjectWrapperFactory(), this.configuration.getReflectorFactory());
        DefaultResultContext<V> context = new DefaultResultContext<>();
        for (V o : list) {
            context.nextResultObject(o);
            mapResultHandler.handleResult(context);
        }
        return mapResultHandler.getMappedResults();
    }

    @Override // org.apache.ibatis.session.SqlSession
    public <T> Cursor<T> selectCursor(String statement) {
        return selectCursor(statement, null);
    }

    @Override // org.apache.ibatis.session.SqlSession
    public <T> Cursor<T> selectCursor(String statement, Object parameter) {
        return selectCursor(statement, parameter, RowBounds.DEFAULT);
    }

    @Override // org.apache.ibatis.session.SqlSession
    public <T> Cursor<T> selectCursor(String statement, Object parameter, RowBounds rowBounds) {
        try {
            try {
                MappedStatement ms = this.configuration.getMappedStatement(statement);
                Cursor<T> cursor = this.executor.queryCursor(ms, wrapCollection(parameter), rowBounds);
                registerCursor(cursor);
                ErrorContext.instance().reset();
                return cursor;
            } catch (Exception e) {
                throw ExceptionFactory.wrapException("Error querying database.  Cause: " + e, e);
            }
        } catch (Throwable th) {
            ErrorContext.instance().reset();
            throw th;
        }
    }

    @Override // org.apache.ibatis.session.SqlSession
    public <E> List<E> selectList(String statement) {
        return selectList(statement, null);
    }

    @Override // org.apache.ibatis.session.SqlSession
    public <E> List<E> selectList(String statement, Object parameter) {
        return selectList(statement, parameter, RowBounds.DEFAULT);
    }

    @Override // org.apache.ibatis.session.SqlSession
    public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
        try {
            try {
                MappedStatement ms = this.configuration.getMappedStatement(statement);
                List<E> listQuery = this.executor.query(ms, wrapCollection(parameter), rowBounds, Executor.NO_RESULT_HANDLER);
                ErrorContext.instance().reset();
                return listQuery;
            } catch (Exception e) {
                throw ExceptionFactory.wrapException("Error querying database.  Cause: " + e, e);
            }
        } catch (Throwable th) {
            ErrorContext.instance().reset();
            throw th;
        }
    }

    @Override // org.apache.ibatis.session.SqlSession
    public void select(String statement, Object parameter, ResultHandler handler) {
        select(statement, parameter, RowBounds.DEFAULT, handler);
    }

    @Override // org.apache.ibatis.session.SqlSession
    public void select(String statement, ResultHandler handler) {
        select(statement, null, RowBounds.DEFAULT, handler);
    }

    @Override // org.apache.ibatis.session.SqlSession
    public void select(String statement, Object parameter, RowBounds rowBounds, ResultHandler handler) {
        try {
            try {
                MappedStatement ms = this.configuration.getMappedStatement(statement);
                this.executor.query(ms, wrapCollection(parameter), rowBounds, handler);
                ErrorContext.instance().reset();
            } catch (Exception e) {
                throw ExceptionFactory.wrapException("Error querying database.  Cause: " + e, e);
            }
        } catch (Throwable th) {
            ErrorContext.instance().reset();
            throw th;
        }
    }

    @Override // org.apache.ibatis.session.SqlSession
    public int insert(String statement) {
        return insert(statement, null);
    }

    @Override // org.apache.ibatis.session.SqlSession
    public int insert(String statement, Object parameter) {
        return update(statement, parameter);
    }

    @Override // org.apache.ibatis.session.SqlSession
    public int update(String statement) {
        return update(statement, null);
    }

    @Override // org.apache.ibatis.session.SqlSession
    public int update(String statement, Object parameter) {
        try {
            try {
                this.dirty = true;
                MappedStatement ms = this.configuration.getMappedStatement(statement);
                int iUpdate = this.executor.update(ms, wrapCollection(parameter));
                ErrorContext.instance().reset();
                return iUpdate;
            } catch (Exception e) {
                throw ExceptionFactory.wrapException("Error updating database.  Cause: " + e, e);
            }
        } catch (Throwable th) {
            ErrorContext.instance().reset();
            throw th;
        }
    }

    @Override // org.apache.ibatis.session.SqlSession
    public int delete(String statement) {
        return update(statement, null);
    }

    @Override // org.apache.ibatis.session.SqlSession
    public int delete(String statement, Object parameter) {
        return update(statement, parameter);
    }

    @Override // org.apache.ibatis.session.SqlSession
    public void commit() {
        commit(false);
    }

    @Override // org.apache.ibatis.session.SqlSession
    public void commit(boolean force) {
        try {
            try {
                this.executor.commit(isCommitOrRollbackRequired(force));
                this.dirty = false;
                ErrorContext.instance().reset();
            } catch (Exception e) {
                throw ExceptionFactory.wrapException("Error committing transaction.  Cause: " + e, e);
            }
        } catch (Throwable th) {
            ErrorContext.instance().reset();
            throw th;
        }
    }

    @Override // org.apache.ibatis.session.SqlSession
    public void rollback() {
        rollback(false);
    }

    @Override // org.apache.ibatis.session.SqlSession
    public void rollback(boolean force) {
        try {
            try {
                this.executor.rollback(isCommitOrRollbackRequired(force));
                this.dirty = false;
                ErrorContext.instance().reset();
            } catch (Exception e) {
                throw ExceptionFactory.wrapException("Error rolling back transaction.  Cause: " + e, e);
            }
        } catch (Throwable th) {
            ErrorContext.instance().reset();
            throw th;
        }
    }

    @Override // org.apache.ibatis.session.SqlSession
    public List<BatchResult> flushStatements() {
        try {
            try {
                List<BatchResult> listFlushStatements = this.executor.flushStatements();
                ErrorContext.instance().reset();
                return listFlushStatements;
            } catch (Exception e) {
                throw ExceptionFactory.wrapException("Error flushing statements.  Cause: " + e, e);
            }
        } catch (Throwable th) {
            ErrorContext.instance().reset();
            throw th;
        }
    }

    @Override // org.apache.ibatis.session.SqlSession, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        try {
            this.executor.close(isCommitOrRollbackRequired(false));
            closeCursors();
            this.dirty = false;
            ErrorContext.instance().reset();
        } catch (Throwable th) {
            ErrorContext.instance().reset();
            throw th;
        }
    }

    private void closeCursors() {
        if (this.cursorList != null && this.cursorList.size() != 0) {
            for (Cursor<?> cursor : this.cursorList) {
                try {
                    cursor.close();
                } catch (IOException e) {
                    throw ExceptionFactory.wrapException("Error closing cursor.  Cause: " + e, e);
                }
            }
            this.cursorList.clear();
        }
    }

    @Override // org.apache.ibatis.session.SqlSession
    public Configuration getConfiguration() {
        return this.configuration;
    }

    @Override // org.apache.ibatis.session.SqlSession
    public <T> T getMapper(Class<T> cls) {
        return (T) this.configuration.getMapper(cls, this);
    }

    @Override // org.apache.ibatis.session.SqlSession
    public Connection getConnection() {
        try {
            return this.executor.getTransaction().getConnection();
        } catch (SQLException e) {
            throw ExceptionFactory.wrapException("Error getting a new connection.  Cause: " + e, e);
        }
    }

    @Override // org.apache.ibatis.session.SqlSession
    public void clearCache() {
        this.executor.clearLocalCache();
    }

    private <T> void registerCursor(Cursor<T> cursor) {
        if (this.cursorList == null) {
            this.cursorList = new ArrayList();
        }
        this.cursorList.add(cursor);
    }

    private boolean isCommitOrRollbackRequired(boolean force) {
        return (!this.autoCommit && this.dirty) || force;
    }

    private Object wrapCollection(Object object) {
        if (object instanceof Collection) {
            StrictMap<Object> map = new StrictMap<>();
            map.put("collection", object);
            if (object instanceof List) {
                map.put("list", object);
            }
            return map;
        }
        if (object != null && object.getClass().isArray()) {
            StrictMap<Object> map2 = new StrictMap<>();
            map2.put("array", object);
            return map2;
        }
        return object;
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/session/defaults/DefaultSqlSession$StrictMap.class */
    public static class StrictMap<V> extends HashMap<String, V> {
        private static final long serialVersionUID = -5741767162221585340L;

        @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
        public V get(Object obj) {
            if (!super.containsKey(obj)) {
                throw new BindingException("Parameter '" + obj + "' not found. Available parameters are " + keySet());
            }
            return (V) super.get(obj);
        }
    }
}

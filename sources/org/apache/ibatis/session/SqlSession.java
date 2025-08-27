package org.apache.ibatis.session;

import java.io.Closeable;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.BatchResult;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/session/SqlSession.class */
public interface SqlSession extends Closeable {
    <T> T selectOne(String str);

    <T> T selectOne(String str, Object obj);

    <E> List<E> selectList(String str);

    <E> List<E> selectList(String str, Object obj);

    <E> List<E> selectList(String str, Object obj, RowBounds rowBounds);

    <K, V> Map<K, V> selectMap(String str, String str2);

    <K, V> Map<K, V> selectMap(String str, Object obj, String str2);

    <K, V> Map<K, V> selectMap(String str, Object obj, String str2, RowBounds rowBounds);

    <T> Cursor<T> selectCursor(String str);

    <T> Cursor<T> selectCursor(String str, Object obj);

    <T> Cursor<T> selectCursor(String str, Object obj, RowBounds rowBounds);

    void select(String str, Object obj, ResultHandler resultHandler);

    void select(String str, ResultHandler resultHandler);

    void select(String str, Object obj, RowBounds rowBounds, ResultHandler resultHandler);

    int insert(String str);

    int insert(String str, Object obj);

    int update(String str);

    int update(String str, Object obj);

    int delete(String str);

    int delete(String str, Object obj);

    void commit();

    void commit(boolean z);

    void rollback();

    void rollback(boolean z);

    List<BatchResult> flushStatements();

    @Override // java.io.Closeable, java.lang.AutoCloseable
    void close();

    void clearCache();

    Configuration getConfiguration();

    <T> T getMapper(Class<T> cls);

    Connection getConnection();
}

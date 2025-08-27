package org.apache.ibatis.session;

import java.sql.Connection;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/session/SqlSessionFactory.class */
public interface SqlSessionFactory {
    SqlSession openSession();

    SqlSession openSession(boolean z);

    SqlSession openSession(Connection connection);

    SqlSession openSession(TransactionIsolationLevel transactionIsolationLevel);

    SqlSession openSession(ExecutorType executorType);

    SqlSession openSession(ExecutorType executorType, boolean z);

    SqlSession openSession(ExecutorType executorType, TransactionIsolationLevel transactionIsolationLevel);

    SqlSession openSession(ExecutorType executorType, Connection connection);

    Configuration getConfiguration();
}

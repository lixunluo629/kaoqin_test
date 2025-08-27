package org.apache.ibatis.transaction;

import java.sql.Connection;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.ibatis.session.TransactionIsolationLevel;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/transaction/TransactionFactory.class */
public interface TransactionFactory {
    void setProperties(Properties properties);

    Transaction newTransaction(Connection connection);

    Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel transactionIsolationLevel, boolean z);
}

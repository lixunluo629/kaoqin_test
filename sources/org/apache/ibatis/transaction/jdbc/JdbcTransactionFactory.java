package org.apache.ibatis.transaction.jdbc;

import java.sql.Connection;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.ibatis.session.TransactionIsolationLevel;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.TransactionFactory;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/transaction/jdbc/JdbcTransactionFactory.class */
public class JdbcTransactionFactory implements TransactionFactory {
    @Override // org.apache.ibatis.transaction.TransactionFactory
    public void setProperties(Properties props) {
    }

    @Override // org.apache.ibatis.transaction.TransactionFactory
    public Transaction newTransaction(Connection conn) {
        return new JdbcTransaction(conn);
    }

    @Override // org.apache.ibatis.transaction.TransactionFactory
    public Transaction newTransaction(DataSource ds, TransactionIsolationLevel level, boolean autoCommit) {
        return new JdbcTransaction(ds, level, autoCommit);
    }
}

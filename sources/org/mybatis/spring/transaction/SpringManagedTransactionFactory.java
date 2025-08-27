package org.mybatis.spring.transaction;

import java.sql.Connection;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.ibatis.session.TransactionIsolationLevel;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.TransactionFactory;

/* loaded from: mybatis-spring-1.3.2.jar:org/mybatis/spring/transaction/SpringManagedTransactionFactory.class */
public class SpringManagedTransactionFactory implements TransactionFactory {
    @Override // org.apache.ibatis.transaction.TransactionFactory
    public Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit) {
        return new SpringManagedTransaction(dataSource);
    }

    @Override // org.apache.ibatis.transaction.TransactionFactory
    public Transaction newTransaction(Connection conn) {
        throw new UnsupportedOperationException("New Spring transactions require a DataSource");
    }

    @Override // org.apache.ibatis.transaction.TransactionFactory
    public void setProperties(Properties props) {
    }
}

package org.apache.ibatis.transaction.managed;

import java.sql.Connection;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.ibatis.session.TransactionIsolationLevel;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.TransactionFactory;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/transaction/managed/ManagedTransactionFactory.class */
public class ManagedTransactionFactory implements TransactionFactory {
    private boolean closeConnection = true;

    @Override // org.apache.ibatis.transaction.TransactionFactory
    public void setProperties(Properties props) {
        String closeConnectionProperty;
        if (props != null && (closeConnectionProperty = props.getProperty("closeConnection")) != null) {
            this.closeConnection = Boolean.valueOf(closeConnectionProperty).booleanValue();
        }
    }

    @Override // org.apache.ibatis.transaction.TransactionFactory
    public Transaction newTransaction(Connection conn) {
        return new ManagedTransaction(conn, this.closeConnection);
    }

    @Override // org.apache.ibatis.transaction.TransactionFactory
    public Transaction newTransaction(DataSource ds, TransactionIsolationLevel level, boolean autoCommit) {
        return new ManagedTransaction(ds, level, this.closeConnection);
    }
}

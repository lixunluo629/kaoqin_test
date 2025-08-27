package org.mybatis.spring;

import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.transaction.TransactionException;

/* loaded from: mybatis-spring-1.3.2.jar:org/mybatis/spring/MyBatisExceptionTranslator.class */
public class MyBatisExceptionTranslator implements PersistenceExceptionTranslator {
    private final DataSource dataSource;
    private SQLExceptionTranslator exceptionTranslator;

    public MyBatisExceptionTranslator(DataSource dataSource, boolean exceptionTranslatorLazyInit) {
        this.dataSource = dataSource;
        if (!exceptionTranslatorLazyInit) {
            initExceptionTranslator();
        }
    }

    @Override // org.springframework.dao.support.PersistenceExceptionTranslator
    public DataAccessException translateExceptionIfPossible(RuntimeException e) {
        if (e instanceof PersistenceException) {
            if (e.getCause() instanceof PersistenceException) {
                e = (PersistenceException) e.getCause();
            }
            if (e.getCause() instanceof SQLException) {
                initExceptionTranslator();
                return this.exceptionTranslator.translate(e.getMessage() + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR, null, (SQLException) e.getCause());
            }
            if (e.getCause() instanceof TransactionException) {
                throw ((TransactionException) e.getCause());
            }
            return new MyBatisSystemException(e);
        }
        return null;
    }

    private synchronized void initExceptionTranslator() {
        if (this.exceptionTranslator == null) {
            this.exceptionTranslator = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);
        }
    }
}

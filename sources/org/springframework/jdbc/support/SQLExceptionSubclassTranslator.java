package org.springframework.jdbc.support;

import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLInvalidAuthorizationSpecException;
import java.sql.SQLNonTransientConnectionException;
import java.sql.SQLNonTransientException;
import java.sql.SQLRecoverableException;
import java.sql.SQLSyntaxErrorException;
import java.sql.SQLTimeoutException;
import java.sql.SQLTransactionRollbackException;
import java.sql.SQLTransientConnectionException;
import java.sql.SQLTransientException;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.jdbc.BadSqlGrammarException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/SQLExceptionSubclassTranslator.class */
public class SQLExceptionSubclassTranslator extends AbstractFallbackSQLExceptionTranslator {
    public SQLExceptionSubclassTranslator() {
        setFallbackTranslator(new SQLStateSQLExceptionTranslator());
    }

    @Override // org.springframework.jdbc.support.AbstractFallbackSQLExceptionTranslator
    protected DataAccessException doTranslate(String task, String sql, SQLException ex) {
        if (ex instanceof SQLTransientException) {
            if (ex instanceof SQLTransientConnectionException) {
                return new TransientDataAccessResourceException(buildMessage(task, sql, ex), ex);
            }
            if (ex instanceof SQLTransactionRollbackException) {
                return new ConcurrencyFailureException(buildMessage(task, sql, ex), ex);
            }
            if (ex instanceof SQLTimeoutException) {
                return new QueryTimeoutException(buildMessage(task, sql, ex), ex);
            }
            return null;
        }
        if (ex instanceof SQLNonTransientException) {
            if (ex instanceof SQLNonTransientConnectionException) {
                return new DataAccessResourceFailureException(buildMessage(task, sql, ex), ex);
            }
            if (ex instanceof SQLDataException) {
                return new DataIntegrityViolationException(buildMessage(task, sql, ex), ex);
            }
            if (ex instanceof SQLIntegrityConstraintViolationException) {
                return new DataIntegrityViolationException(buildMessage(task, sql, ex), ex);
            }
            if (ex instanceof SQLInvalidAuthorizationSpecException) {
                return new PermissionDeniedDataAccessException(buildMessage(task, sql, ex), ex);
            }
            if (ex instanceof SQLSyntaxErrorException) {
                return new BadSqlGrammarException(task, sql, ex);
            }
            if (ex instanceof SQLFeatureNotSupportedException) {
                return new InvalidDataAccessApiUsageException(buildMessage(task, sql, ex), ex);
            }
            return null;
        }
        if (ex instanceof SQLRecoverableException) {
            return new RecoverableDataAccessException(buildMessage(task, sql, ex), ex);
        }
        return null;
    }
}

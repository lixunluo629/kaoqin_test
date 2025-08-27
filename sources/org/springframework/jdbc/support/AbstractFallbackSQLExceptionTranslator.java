package org.springframework.jdbc.support;

import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.util.Assert;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/AbstractFallbackSQLExceptionTranslator.class */
public abstract class AbstractFallbackSQLExceptionTranslator implements SQLExceptionTranslator {
    protected final Log logger = LogFactory.getLog(getClass());
    private SQLExceptionTranslator fallbackTranslator;

    protected abstract DataAccessException doTranslate(String str, String str2, SQLException sQLException);

    public void setFallbackTranslator(SQLExceptionTranslator fallback) {
        this.fallbackTranslator = fallback;
    }

    public SQLExceptionTranslator getFallbackTranslator() {
        return this.fallbackTranslator;
    }

    @Override // org.springframework.jdbc.support.SQLExceptionTranslator
    public DataAccessException translate(String task, String sql, SQLException ex) {
        DataAccessException dae;
        Assert.notNull(ex, "Cannot translate a null SQLException");
        if (task == null) {
            task = "";
        }
        if (sql == null) {
            sql = "";
        }
        DataAccessException dae2 = doTranslate(task, sql, ex);
        if (dae2 != null) {
            return dae2;
        }
        SQLExceptionTranslator fallback = getFallbackTranslator();
        if (fallback != null && (dae = fallback.translate(task, sql, ex)) != null) {
            return dae;
        }
        return new UncategorizedSQLException(task, sql, ex);
    }

    protected String buildMessage(String task, String sql, SQLException ex) {
        return task + "; SQL [" + sql + "]; " + ex.getMessage();
    }
}

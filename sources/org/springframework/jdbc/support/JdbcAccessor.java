package org.springframework.jdbc.support;

import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/JdbcAccessor.class */
public abstract class JdbcAccessor implements InitializingBean {
    private DataSource dataSource;
    private volatile SQLExceptionTranslator exceptionTranslator;
    protected final Log logger = LogFactory.getLog(getClass());
    private boolean lazyInit = true;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return this.dataSource;
    }

    public void setDatabaseProductName(String dbName) {
        this.exceptionTranslator = new SQLErrorCodeSQLExceptionTranslator(dbName);
    }

    public void setExceptionTranslator(SQLExceptionTranslator exceptionTranslator) {
        this.exceptionTranslator = exceptionTranslator;
    }

    public SQLExceptionTranslator getExceptionTranslator() {
        SQLExceptionTranslator sQLExceptionTranslator;
        SQLExceptionTranslator exceptionTranslator = this.exceptionTranslator;
        if (exceptionTranslator != null) {
            return exceptionTranslator;
        }
        synchronized (this) {
            SQLExceptionTranslator exceptionTranslator2 = this.exceptionTranslator;
            if (exceptionTranslator2 == null) {
                DataSource dataSource = getDataSource();
                if (dataSource != null) {
                    exceptionTranslator2 = new SQLErrorCodeSQLExceptionTranslator(dataSource);
                } else {
                    exceptionTranslator2 = new SQLStateSQLExceptionTranslator();
                }
                this.exceptionTranslator = exceptionTranslator2;
            }
            sQLExceptionTranslator = exceptionTranslator2;
        }
        return sQLExceptionTranslator;
    }

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    public boolean isLazyInit() {
        return this.lazyInit;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        if (getDataSource() == null) {
            throw new IllegalArgumentException("Property 'dataSource' is required");
        }
        if (!isLazyInit()) {
            getExceptionTranslator();
        }
    }
}

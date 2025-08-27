package org.springframework.jdbc.support;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.CannotGetJdbcConnectionException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/DatabaseStartupValidator.class */
public class DatabaseStartupValidator implements InitializingBean {
    public static final int DEFAULT_INTERVAL = 1;
    public static final int DEFAULT_TIMEOUT = 60;
    private DataSource dataSource;
    private String validationQuery;
    protected final Log logger = LogFactory.getLog(getClass());
    private int interval = 1;
    private int timeout = 60;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /* JADX WARN: Finally extract failed */
    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws InterruptedException {
        DataSource dataSource = this.dataSource;
        if (dataSource == null) {
            throw new IllegalArgumentException("Property 'dataSource' is required");
        }
        if (this.validationQuery == null) {
            throw new IllegalArgumentException("Property 'validationQuery' is required");
        }
        try {
            boolean validated = false;
            long beginTime = System.currentTimeMillis();
            long deadLine = beginTime + (this.timeout * 1000);
            SQLException latestEx = null;
            while (!validated && System.currentTimeMillis() < deadLine) {
                Connection con = null;
                Statement stmt = null;
                try {
                    try {
                        con = dataSource.getConnection();
                        stmt = con.createStatement();
                        stmt.execute(this.validationQuery);
                        validated = true;
                        JdbcUtils.closeStatement(stmt);
                        JdbcUtils.closeConnection(con);
                    } catch (SQLException ex) {
                        latestEx = ex;
                        if (this.logger.isDebugEnabled()) {
                            this.logger.debug("Validation query [" + this.validationQuery + "] threw exception", ex);
                        }
                        if (this.logger.isWarnEnabled()) {
                            float rest = (deadLine - System.currentTimeMillis()) / 1000.0f;
                            if (rest > this.interval) {
                                this.logger.warn("Database has not started up yet - retrying in " + this.interval + " seconds (timeout in " + rest + " seconds)");
                            }
                        }
                        JdbcUtils.closeStatement(stmt);
                        JdbcUtils.closeConnection(con);
                    }
                    if (!validated) {
                        Thread.sleep(this.interval * 1000);
                    }
                } catch (Throwable th) {
                    JdbcUtils.closeStatement(stmt);
                    JdbcUtils.closeConnection(con);
                    throw th;
                }
            }
            if (!validated) {
                throw new CannotGetJdbcConnectionException("Database has not started up within " + this.timeout + " seconds", latestEx);
            }
            if (this.logger.isInfoEnabled()) {
                float duration = (System.currentTimeMillis() - beginTime) / 1000.0f;
                this.logger.info("Database startup detected after " + duration + " seconds");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

package org.springframework.jdbc.datasource.embedded;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/embedded/AbstractEmbeddedDatabaseConfigurer.class */
abstract class AbstractEmbeddedDatabaseConfigurer implements EmbeddedDatabaseConfigurer {
    protected final Log logger = LogFactory.getLog(getClass());

    AbstractEmbeddedDatabaseConfigurer() {
    }

    @Override // org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseConfigurer
    public void shutdown(DataSource dataSource, String databaseName) {
        Connection con = null;
        try {
            try {
                con = dataSource.getConnection();
                con.createStatement().execute("SHUTDOWN");
                if (con != null) {
                    try {
                        con.close();
                    } catch (Throwable ex) {
                        this.logger.debug("Could not close JDBC Connection on shutdown", ex);
                    }
                }
            } catch (SQLException ex2) {
                this.logger.warn("Could not shut down embedded database", ex2);
                if (con != null) {
                    try {
                        con.close();
                    } catch (Throwable ex3) {
                        this.logger.debug("Could not close JDBC Connection on shutdown", ex3);
                    }
                }
            }
        } catch (Throwable th) {
            if (con != null) {
                try {
                    con.close();
                } catch (Throwable ex4) {
                    this.logger.debug("Could not close JDBC Connection on shutdown", ex4);
                }
            }
            throw th;
        }
    }
}

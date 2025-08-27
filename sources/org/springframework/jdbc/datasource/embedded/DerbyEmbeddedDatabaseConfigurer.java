package org.springframework.jdbc.datasource.embedded;

import com.mysql.jdbc.SQLError;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.logging.LogFactory;
import org.apache.derby.jdbc.EmbeddedDriver;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/embedded/DerbyEmbeddedDatabaseConfigurer.class */
final class DerbyEmbeddedDatabaseConfigurer implements EmbeddedDatabaseConfigurer {
    private static final String URL_TEMPLATE = "jdbc:derby:memory:%s;%s";
    private static DerbyEmbeddedDatabaseConfigurer instance;

    public static synchronized DerbyEmbeddedDatabaseConfigurer getInstance() {
        if (instance == null) {
            System.setProperty("derby.stream.error.method", OutputStreamFactory.class.getName() + ".getNoopOutputStream");
            instance = new DerbyEmbeddedDatabaseConfigurer();
        }
        return instance;
    }

    private DerbyEmbeddedDatabaseConfigurer() {
    }

    @Override // org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseConfigurer
    public void configureConnectionProperties(ConnectionProperties properties, String databaseName) {
        properties.setDriverClass(EmbeddedDriver.class);
        properties.setUrl(String.format(URL_TEMPLATE, databaseName, "create=true"));
        properties.setUsername("sa");
        properties.setPassword("");
    }

    @Override // org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseConfigurer
    public void shutdown(DataSource dataSource, String databaseName) {
        try {
            new EmbeddedDriver().connect(String.format(URL_TEMPLATE, databaseName, "drop=true"), new Properties());
        } catch (SQLException ex) {
            if (!SQLError.SQL_STATE_CONNECTION_FAILURE.equals(ex.getSQLState())) {
                LogFactory.getLog(getClass()).warn("Could not shut down embedded Derby database", ex);
            }
        }
    }
}

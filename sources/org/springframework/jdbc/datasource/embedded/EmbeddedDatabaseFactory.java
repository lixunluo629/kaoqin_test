package org.springframework.jdbc.datasource.embedded;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.util.Assert;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/embedded/EmbeddedDatabaseFactory.class */
public class EmbeddedDatabaseFactory {
    public static final String DEFAULT_DATABASE_NAME = "testdb";
    private static final Log logger = LogFactory.getLog(EmbeddedDatabaseFactory.class);
    private boolean generateUniqueDatabaseName = false;
    private String databaseName = DEFAULT_DATABASE_NAME;
    private DataSourceFactory dataSourceFactory = new SimpleDriverDataSourceFactory();
    private EmbeddedDatabaseConfigurer databaseConfigurer;
    private DatabasePopulator databasePopulator;
    private DataSource dataSource;

    public void setGenerateUniqueDatabaseName(boolean generateUniqueDatabaseName) {
        this.generateUniqueDatabaseName = generateUniqueDatabaseName;
    }

    public void setDatabaseName(String databaseName) {
        Assert.hasText(databaseName, "Database name is required");
        this.databaseName = databaseName;
    }

    public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
        Assert.notNull(dataSourceFactory, "DataSourceFactory is required");
        this.dataSourceFactory = dataSourceFactory;
    }

    public void setDatabaseType(EmbeddedDatabaseType type) {
        this.databaseConfigurer = EmbeddedDatabaseConfigurerFactory.getConfigurer(type);
    }

    public void setDatabaseConfigurer(EmbeddedDatabaseConfigurer configurer) {
        this.databaseConfigurer = configurer;
    }

    public void setDatabasePopulator(DatabasePopulator populator) {
        this.databasePopulator = populator;
    }

    public EmbeddedDatabase getDatabase() {
        if (this.dataSource == null) {
            initDatabase();
        }
        return new EmbeddedDataSourceProxy(this.dataSource);
    }

    protected void initDatabase() {
        if (this.generateUniqueDatabaseName) {
            setDatabaseName(UUID.randomUUID().toString());
        }
        if (this.databaseConfigurer == null) {
            this.databaseConfigurer = EmbeddedDatabaseConfigurerFactory.getConfigurer(EmbeddedDatabaseType.HSQL);
        }
        this.databaseConfigurer.configureConnectionProperties(this.dataSourceFactory.getConnectionProperties(), this.databaseName);
        this.dataSource = this.dataSourceFactory.getDataSource();
        if (logger.isInfoEnabled()) {
            if (this.dataSource instanceof SimpleDriverDataSource) {
                SimpleDriverDataSource simpleDriverDataSource = (SimpleDriverDataSource) this.dataSource;
                logger.info(String.format("Starting embedded database: url='%s', username='%s'", simpleDriverDataSource.getUrl(), simpleDriverDataSource.getUsername()));
            } else {
                logger.info(String.format("Starting embedded database '%s'", this.databaseName));
            }
        }
        if (this.databasePopulator != null) {
            try {
                DatabasePopulatorUtils.execute(this.databasePopulator, this.dataSource);
            } catch (RuntimeException ex) {
                shutdownDatabase();
                throw ex;
            }
        }
    }

    protected void shutdownDatabase() {
        if (this.dataSource != null) {
            if (logger.isInfoEnabled()) {
                if (this.dataSource instanceof SimpleDriverDataSource) {
                    logger.info(String.format("Shutting down embedded database: url='%s'", ((SimpleDriverDataSource) this.dataSource).getUrl()));
                } else {
                    logger.info(String.format("Shutting down embedded database '%s'", this.databaseName));
                }
            }
            this.databaseConfigurer.shutdown(this.dataSource, this.databaseName);
            this.dataSource = null;
        }
    }

    protected final DataSource getDataSource() {
        return this.dataSource;
    }

    /* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/embedded/EmbeddedDatabaseFactory$EmbeddedDataSourceProxy.class */
    private class EmbeddedDataSourceProxy implements EmbeddedDatabase {
        private final DataSource dataSource;

        public EmbeddedDataSourceProxy(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        @Override // javax.sql.DataSource
        public Connection getConnection() throws SQLException {
            return this.dataSource.getConnection();
        }

        @Override // javax.sql.DataSource
        public Connection getConnection(String username, String password) throws SQLException {
            return this.dataSource.getConnection(username, password);
        }

        @Override // javax.sql.CommonDataSource
        public PrintWriter getLogWriter() throws SQLException {
            return this.dataSource.getLogWriter();
        }

        @Override // javax.sql.CommonDataSource
        public void setLogWriter(PrintWriter out) throws SQLException {
            this.dataSource.setLogWriter(out);
        }

        @Override // javax.sql.CommonDataSource
        public int getLoginTimeout() throws SQLException {
            return this.dataSource.getLoginTimeout();
        }

        @Override // javax.sql.CommonDataSource
        public void setLoginTimeout(int seconds) throws SQLException {
            this.dataSource.setLoginTimeout(seconds);
        }

        @Override // java.sql.Wrapper
        public <T> T unwrap(Class<T> cls) throws SQLException {
            return (T) this.dataSource.unwrap(cls);
        }

        @Override // java.sql.Wrapper
        public boolean isWrapperFor(Class<?> iface) throws SQLException {
            return this.dataSource.isWrapperFor(iface);
        }

        @Override // javax.sql.CommonDataSource
        public Logger getParentLogger() {
            return Logger.getLogger("global");
        }

        @Override // org.springframework.jdbc.datasource.embedded.EmbeddedDatabase
        public void shutdown() {
            EmbeddedDatabaseFactory.this.shutdownDatabase();
        }
    }
}

package org.springframework.jdbc.datasource.embedded;

import java.sql.Driver;
import org.springframework.util.ClassUtils;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/embedded/HsqlEmbeddedDatabaseConfigurer.class */
final class HsqlEmbeddedDatabaseConfigurer extends AbstractEmbeddedDatabaseConfigurer {
    private static HsqlEmbeddedDatabaseConfigurer instance;
    private final Class<? extends Driver> driverClass;

    public static synchronized HsqlEmbeddedDatabaseConfigurer getInstance() throws ClassNotFoundException {
        if (instance == null) {
            instance = new HsqlEmbeddedDatabaseConfigurer(ClassUtils.forName("org.hsqldb.jdbcDriver", HsqlEmbeddedDatabaseConfigurer.class.getClassLoader()));
        }
        return instance;
    }

    private HsqlEmbeddedDatabaseConfigurer(Class<? extends Driver> driverClass) {
        this.driverClass = driverClass;
    }

    @Override // org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseConfigurer
    public void configureConnectionProperties(ConnectionProperties properties, String databaseName) {
        properties.setDriverClass(this.driverClass);
        properties.setUrl("jdbc:hsqldb:mem:" + databaseName);
        properties.setUsername("sa");
        properties.setPassword("");
    }
}

package org.springframework.jdbc.datasource.embedded;

import java.sql.Driver;
import org.springframework.util.ClassUtils;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/embedded/H2EmbeddedDatabaseConfigurer.class */
final class H2EmbeddedDatabaseConfigurer extends AbstractEmbeddedDatabaseConfigurer {
    private static H2EmbeddedDatabaseConfigurer instance;
    private final Class<? extends Driver> driverClass;

    public static synchronized H2EmbeddedDatabaseConfigurer getInstance() throws ClassNotFoundException {
        if (instance == null) {
            instance = new H2EmbeddedDatabaseConfigurer(ClassUtils.forName("org.h2.Driver", H2EmbeddedDatabaseConfigurer.class.getClassLoader()));
        }
        return instance;
    }

    private H2EmbeddedDatabaseConfigurer(Class<? extends Driver> driverClass) {
        this.driverClass = driverClass;
    }

    @Override // org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseConfigurer
    public void configureConnectionProperties(ConnectionProperties properties, String databaseName) {
        properties.setDriverClass(this.driverClass);
        properties.setUrl(String.format("jdbc:h2:mem:%s;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false", databaseName));
        properties.setUsername("sa");
        properties.setPassword("");
    }
}

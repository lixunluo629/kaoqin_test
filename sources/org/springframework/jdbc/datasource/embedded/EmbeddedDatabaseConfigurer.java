package org.springframework.jdbc.datasource.embedded;

import javax.sql.DataSource;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/embedded/EmbeddedDatabaseConfigurer.class */
public interface EmbeddedDatabaseConfigurer {
    void configureConnectionProperties(ConnectionProperties connectionProperties, String str);

    void shutdown(DataSource dataSource, String str);
}

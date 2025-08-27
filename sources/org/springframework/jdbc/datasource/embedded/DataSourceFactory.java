package org.springframework.jdbc.datasource.embedded;

import javax.sql.DataSource;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/embedded/DataSourceFactory.class */
public interface DataSourceFactory {
    ConnectionProperties getConnectionProperties();

    DataSource getDataSource();
}

package org.springframework.jdbc.datasource.embedded;

import java.sql.Driver;
import javax.sql.DataSource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/embedded/SimpleDriverDataSourceFactory.class */
final class SimpleDriverDataSourceFactory implements DataSourceFactory {
    private final SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

    SimpleDriverDataSourceFactory() {
    }

    @Override // org.springframework.jdbc.datasource.embedded.DataSourceFactory
    public ConnectionProperties getConnectionProperties() {
        return new ConnectionProperties() { // from class: org.springframework.jdbc.datasource.embedded.SimpleDriverDataSourceFactory.1
            @Override // org.springframework.jdbc.datasource.embedded.ConnectionProperties
            public void setDriverClass(Class<? extends Driver> driverClass) {
                SimpleDriverDataSourceFactory.this.dataSource.setDriverClass(driverClass);
            }

            @Override // org.springframework.jdbc.datasource.embedded.ConnectionProperties
            public void setUrl(String url) {
                SimpleDriverDataSourceFactory.this.dataSource.setUrl(url);
            }

            @Override // org.springframework.jdbc.datasource.embedded.ConnectionProperties
            public void setUsername(String username) {
                SimpleDriverDataSourceFactory.this.dataSource.setUsername(username);
            }

            @Override // org.springframework.jdbc.datasource.embedded.ConnectionProperties
            public void setPassword(String password) {
                SimpleDriverDataSourceFactory.this.dataSource.setPassword(password);
            }
        };
    }

    @Override // org.springframework.jdbc.datasource.embedded.DataSourceFactory
    public DataSource getDataSource() {
        return this.dataSource;
    }
}

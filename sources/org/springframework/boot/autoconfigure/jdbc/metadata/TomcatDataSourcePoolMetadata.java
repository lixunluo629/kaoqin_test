package org.springframework.boot.autoconfigure.jdbc.metadata;

import org.apache.tomcat.jdbc.pool.ConnectionPool;
import org.apache.tomcat.jdbc.pool.DataSource;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/metadata/TomcatDataSourcePoolMetadata.class */
public class TomcatDataSourcePoolMetadata extends AbstractDataSourcePoolMetadata<DataSource> {
    public TomcatDataSourcePoolMetadata(DataSource dataSource) {
        super(dataSource);
    }

    @Override // org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadata
    public Integer getActive() {
        ConnectionPool pool = getDataSource().getPool();
        return Integer.valueOf(pool != null ? pool.getActive() : 0);
    }

    @Override // org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadata
    public Integer getMax() {
        return Integer.valueOf(getDataSource().getMaxActive());
    }

    @Override // org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadata
    public Integer getMin() {
        return Integer.valueOf(getDataSource().getMinIdle());
    }

    @Override // org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadata
    public String getValidationQuery() {
        return getDataSource().getValidationQuery();
    }
}

package org.springframework.boot.autoconfigure.jdbc.metadata;

import org.apache.commons.dbcp.BasicDataSource;

@Deprecated
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/metadata/CommonsDbcpDataSourcePoolMetadata.class */
public class CommonsDbcpDataSourcePoolMetadata extends AbstractDataSourcePoolMetadata<BasicDataSource> {
    public CommonsDbcpDataSourcePoolMetadata(BasicDataSource dataSource) {
        super(dataSource);
    }

    @Override // org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadata
    public Integer getActive() {
        return Integer.valueOf(getDataSource().getNumActive());
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

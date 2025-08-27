package org.springframework.boot.autoconfigure.jdbc.metadata;

import org.apache.commons.dbcp2.BasicDataSource;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/metadata/CommonsDbcp2DataSourcePoolMetadata.class */
public class CommonsDbcp2DataSourcePoolMetadata extends AbstractDataSourcePoolMetadata<BasicDataSource> {
    public CommonsDbcp2DataSourcePoolMetadata(BasicDataSource dataSource) {
        super(dataSource);
    }

    @Override // org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadata
    public Integer getActive() {
        return Integer.valueOf(getDataSource().getNumActive());
    }

    @Override // org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadata
    public Integer getMax() {
        return Integer.valueOf(getDataSource().getMaxTotal());
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

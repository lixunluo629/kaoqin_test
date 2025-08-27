package org.springframework.boot.autoconfigure.jdbc.metadata;

import javax.sql.DataSource;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/metadata/DataSourcePoolMetadataProvider.class */
public interface DataSourcePoolMetadataProvider {
    DataSourcePoolMetadata getDataSourcePoolMetadata(DataSource dataSource);
}

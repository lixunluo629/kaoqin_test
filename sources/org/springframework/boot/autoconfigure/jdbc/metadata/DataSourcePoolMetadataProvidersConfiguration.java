package org.springframework.boot.autoconfigure.jdbc.metadata;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/metadata/DataSourcePoolMetadataProvidersConfiguration.class */
public class DataSourcePoolMetadataProvidersConfiguration {

    @Configuration
    @ConditionalOnClass({DataSource.class})
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/metadata/DataSourcePoolMetadataProvidersConfiguration$TomcatDataSourcePoolMetadataProviderConfiguration.class */
    static class TomcatDataSourcePoolMetadataProviderConfiguration {
        TomcatDataSourcePoolMetadataProviderConfiguration() {
        }

        @Bean
        public DataSourcePoolMetadataProvider tomcatPoolDataSourceMetadataProvider() {
            return new DataSourcePoolMetadataProvider() { // from class: org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadataProvidersConfiguration.TomcatDataSourcePoolMetadataProviderConfiguration.1
                @Override // org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadataProvider
                public DataSourcePoolMetadata getDataSourcePoolMetadata(javax.sql.DataSource dataSource) {
                    if (dataSource instanceof DataSource) {
                        return new TomcatDataSourcePoolMetadata((DataSource) dataSource);
                    }
                    return null;
                }
            };
        }
    }

    @Configuration
    @ConditionalOnClass({HikariDataSource.class})
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/metadata/DataSourcePoolMetadataProvidersConfiguration$HikariPoolDataSourceMetadataProviderConfiguration.class */
    static class HikariPoolDataSourceMetadataProviderConfiguration {
        HikariPoolDataSourceMetadataProviderConfiguration() {
        }

        @Bean
        public DataSourcePoolMetadataProvider hikariPoolDataSourceMetadataProvider() {
            return new DataSourcePoolMetadataProvider() { // from class: org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadataProvidersConfiguration.HikariPoolDataSourceMetadataProviderConfiguration.1
                @Override // org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadataProvider
                public DataSourcePoolMetadata getDataSourcePoolMetadata(javax.sql.DataSource dataSource) {
                    if (dataSource instanceof HikariDataSource) {
                        return new HikariDataSourcePoolMetadata((HikariDataSource) dataSource);
                    }
                    return null;
                }
            };
        }
    }

    @Configuration
    @ConditionalOnClass({BasicDataSource.class})
    @Deprecated
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/metadata/DataSourcePoolMetadataProvidersConfiguration$CommonsDbcpPoolDataSourceMetadataProviderConfiguration.class */
    static class CommonsDbcpPoolDataSourceMetadataProviderConfiguration {
        CommonsDbcpPoolDataSourceMetadataProviderConfiguration() {
        }

        @Bean
        public DataSourcePoolMetadataProvider commonsDbcpPoolDataSourceMetadataProvider() {
            return new DataSourcePoolMetadataProvider() { // from class: org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadataProvidersConfiguration.CommonsDbcpPoolDataSourceMetadataProviderConfiguration.1
                @Override // org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadataProvider
                public DataSourcePoolMetadata getDataSourcePoolMetadata(javax.sql.DataSource dataSource) {
                    if (dataSource instanceof BasicDataSource) {
                        return new CommonsDbcpDataSourcePoolMetadata((BasicDataSource) dataSource);
                    }
                    return null;
                }
            };
        }
    }

    @Configuration
    @ConditionalOnClass({org.apache.commons.dbcp2.BasicDataSource.class})
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/metadata/DataSourcePoolMetadataProvidersConfiguration$CommonsDbcp2PoolDataSourceMetadataProviderConfiguration.class */
    static class CommonsDbcp2PoolDataSourceMetadataProviderConfiguration {
        CommonsDbcp2PoolDataSourceMetadataProviderConfiguration() {
        }

        @Bean
        public DataSourcePoolMetadataProvider commonsDbcp2PoolDataSourceMetadataProvider() {
            return new DataSourcePoolMetadataProvider() { // from class: org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadataProvidersConfiguration.CommonsDbcp2PoolDataSourceMetadataProviderConfiguration.1
                @Override // org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadataProvider
                public DataSourcePoolMetadata getDataSourcePoolMetadata(javax.sql.DataSource dataSource) {
                    if (dataSource instanceof org.apache.commons.dbcp2.BasicDataSource) {
                        return new CommonsDbcp2DataSourcePoolMetadata((org.apache.commons.dbcp2.BasicDataSource) dataSource);
                    }
                    return null;
                }
            };
        }
    }
}

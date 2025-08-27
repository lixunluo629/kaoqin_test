package org.springframework.boot.autoconfigure.jdbc.metadata;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;
import org.apache.commons.pool2.impl.BaseObjectPoolConfig;
import org.springframework.beans.DirectFieldAccessor;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/metadata/HikariDataSourcePoolMetadata.class */
public class HikariDataSourcePoolMetadata extends AbstractDataSourcePoolMetadata<HikariDataSource> {
    public HikariDataSourcePoolMetadata(HikariDataSource dataSource) {
        super(dataSource);
    }

    @Override // org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadata
    public Integer getActive() {
        try {
            return Integer.valueOf(getHikariPool().getActiveConnections());
        } catch (Exception e) {
            return null;
        }
    }

    private HikariPool getHikariPool() {
        return (HikariPool) new DirectFieldAccessor(getDataSource()).getPropertyValue(BaseObjectPoolConfig.DEFAULT_JMX_NAME_PREFIX);
    }

    @Override // org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadata
    public Integer getMax() {
        return Integer.valueOf(getDataSource().getMaximumPoolSize());
    }

    @Override // org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadata
    public Integer getMin() {
        return Integer.valueOf(getDataSource().getMinimumIdle());
    }

    @Override // org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadata
    public String getValidationQuery() {
        return getDataSource().getConnectionTestQuery();
    }
}

package org.springframework.boot.autoconfigure;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.core.io.ResourceLoader;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.util.Assert;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/AbstractDatabaseInitializer.class */
public abstract class AbstractDatabaseInitializer {
    private static final String PLATFORM_PLACEHOLDER = "@@platform@@";
    private final DataSource dataSource;
    private final ResourceLoader resourceLoader;

    protected abstract boolean isEnabled();

    protected abstract String getSchemaLocation();

    protected AbstractDatabaseInitializer(DataSource dataSource, ResourceLoader resourceLoader) {
        Assert.notNull(dataSource, "DataSource must not be null");
        Assert.notNull(resourceLoader, "ResourceLoader must not be null");
        this.dataSource = dataSource;
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    protected void initialize() throws DataAccessException {
        if (!isEnabled()) {
            return;
        }
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        String schemaLocation = getSchemaLocation();
        if (schemaLocation.contains(PLATFORM_PLACEHOLDER)) {
            String platform = getDatabaseName();
            schemaLocation = schemaLocation.replace(PLATFORM_PLACEHOLDER, platform);
        }
        populator.addScript(this.resourceLoader.getResource(schemaLocation));
        populator.setContinueOnError(true);
        DatabasePopulatorUtils.execute(populator, this.dataSource);
    }

    protected String getDatabaseName() {
        try {
            String productName = JdbcUtils.commonDatabaseName(JdbcUtils.extractDatabaseMetaData(this.dataSource, "getDatabaseProductName").toString());
            DatabaseDriver databaseDriver = DatabaseDriver.fromProductName(productName);
            if (databaseDriver == DatabaseDriver.UNKNOWN) {
                throw new IllegalStateException("Unable to detect database type");
            }
            return databaseDriver.getId();
        } catch (MetaDataAccessException ex) {
            throw new IllegalStateException("Unable to detect database type", ex);
        }
    }
}

package org.springframework.boot.autoconfigure.batch;

import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.AbstractDatabaseInitializer;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/batch/BatchDatabaseInitializer.class */
public class BatchDatabaseInitializer extends AbstractDatabaseInitializer {
    private final BatchProperties properties;

    public BatchDatabaseInitializer(DataSource dataSource, ResourceLoader resourceLoader, BatchProperties properties) {
        super(dataSource, resourceLoader);
        Assert.notNull(properties, "BatchProperties must not be null");
        this.properties = properties;
    }

    @Override // org.springframework.boot.autoconfigure.AbstractDatabaseInitializer
    protected boolean isEnabled() {
        return this.properties.getInitializer().isEnabled();
    }

    @Override // org.springframework.boot.autoconfigure.AbstractDatabaseInitializer
    protected String getSchemaLocation() {
        return this.properties.getSchema();
    }

    @Override // org.springframework.boot.autoconfigure.AbstractDatabaseInitializer
    protected String getDatabaseName() {
        String databaseName = super.getDatabaseName();
        if ("oracle".equals(databaseName)) {
            return "oracle10g";
        }
        return databaseName;
    }
}

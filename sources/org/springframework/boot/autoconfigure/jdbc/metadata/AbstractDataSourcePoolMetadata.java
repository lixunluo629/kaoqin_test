package org.springframework.boot.autoconfigure.jdbc.metadata;

import javax.sql.DataSource;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/metadata/AbstractDataSourcePoolMetadata.class */
public abstract class AbstractDataSourcePoolMetadata<T extends DataSource> implements DataSourcePoolMetadata {
    private final T dataSource;

    protected AbstractDataSourcePoolMetadata(T dataSource) {
        this.dataSource = dataSource;
    }

    @Override // org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadata
    public Float getUsage() {
        Integer maxSize = getMax();
        Integer currentSize = getActive();
        if (maxSize == null || currentSize == null) {
            return null;
        }
        if (maxSize.intValue() < 0) {
            return Float.valueOf(-1.0f);
        }
        if (currentSize.intValue() == 0) {
            return Float.valueOf(0.0f);
        }
        return Float.valueOf(currentSize.intValue() / maxSize.intValue());
    }

    protected final T getDataSource() {
        return this.dataSource;
    }
}

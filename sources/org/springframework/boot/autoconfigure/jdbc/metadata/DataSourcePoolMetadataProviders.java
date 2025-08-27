package org.springframework.boot.autoconfigure.jdbc.metadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.sql.DataSource;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/metadata/DataSourcePoolMetadataProviders.class */
public class DataSourcePoolMetadataProviders implements DataSourcePoolMetadataProvider {
    private final List<DataSourcePoolMetadataProvider> providers;

    public DataSourcePoolMetadataProviders(Collection<? extends DataSourcePoolMetadataProvider> providers) {
        this.providers = providers != null ? new ArrayList<>(providers) : Collections.emptyList();
    }

    @Override // org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadataProvider
    public DataSourcePoolMetadata getDataSourcePoolMetadata(DataSource dataSource) {
        for (DataSourcePoolMetadataProvider provider : this.providers) {
            DataSourcePoolMetadata metadata = provider.getDataSourcePoolMetadata(dataSource);
            if (metadata != null) {
                return metadata;
            }
        }
        return null;
    }
}

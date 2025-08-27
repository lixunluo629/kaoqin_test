package org.springframework.jdbc.datasource.lookup;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.util.Assert;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/lookup/MapDataSourceLookup.class */
public class MapDataSourceLookup implements DataSourceLookup {
    private final Map<String, DataSource> dataSources = new HashMap(4);

    public MapDataSourceLookup() {
    }

    public MapDataSourceLookup(Map<String, DataSource> dataSources) {
        setDataSources(dataSources);
    }

    public MapDataSourceLookup(String dataSourceName, DataSource dataSource) {
        addDataSource(dataSourceName, dataSource);
    }

    public void setDataSources(Map<String, DataSource> dataSources) {
        if (dataSources != null) {
            this.dataSources.putAll(dataSources);
        }
    }

    public Map<String, DataSource> getDataSources() {
        return Collections.unmodifiableMap(this.dataSources);
    }

    public void addDataSource(String dataSourceName, DataSource dataSource) {
        Assert.notNull(dataSourceName, "DataSource name must not be null");
        Assert.notNull(dataSource, "DataSource must not be null");
        this.dataSources.put(dataSourceName, dataSource);
    }

    @Override // org.springframework.jdbc.datasource.lookup.DataSourceLookup
    public DataSource getDataSource(String dataSourceName) throws DataSourceLookupFailureException {
        Assert.notNull(dataSourceName, "DataSource name must not be null");
        DataSource dataSource = this.dataSources.get(dataSourceName);
        if (dataSource == null) {
            throw new DataSourceLookupFailureException("No DataSource with name '" + dataSourceName + "' registered");
        }
        return dataSource;
    }
}

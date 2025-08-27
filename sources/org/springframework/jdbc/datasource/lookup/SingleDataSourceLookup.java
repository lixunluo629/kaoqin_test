package org.springframework.jdbc.datasource.lookup;

import javax.sql.DataSource;
import org.springframework.util.Assert;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/lookup/SingleDataSourceLookup.class */
public class SingleDataSourceLookup implements DataSourceLookup {
    private final DataSource dataSource;

    public SingleDataSourceLookup(DataSource dataSource) {
        Assert.notNull(dataSource, "DataSource must not be null");
        this.dataSource = dataSource;
    }

    @Override // org.springframework.jdbc.datasource.lookup.DataSourceLookup
    public DataSource getDataSource(String dataSourceName) {
        return this.dataSource;
    }
}

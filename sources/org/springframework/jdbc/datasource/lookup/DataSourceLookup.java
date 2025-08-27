package org.springframework.jdbc.datasource.lookup;

import javax.sql.DataSource;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/lookup/DataSourceLookup.class */
public interface DataSourceLookup {
    DataSource getDataSource(String str) throws DataSourceLookupFailureException;
}

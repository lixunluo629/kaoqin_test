package org.springframework.jdbc.datasource.lookup;

import javax.naming.NamingException;
import javax.sql.DataSource;
import org.springframework.jndi.JndiLocatorSupport;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/lookup/JndiDataSourceLookup.class */
public class JndiDataSourceLookup extends JndiLocatorSupport implements DataSourceLookup {
    public JndiDataSourceLookup() {
        setResourceRef(true);
    }

    @Override // org.springframework.jdbc.datasource.lookup.DataSourceLookup
    public DataSource getDataSource(String dataSourceName) throws DataSourceLookupFailureException {
        try {
            return (DataSource) lookup(dataSourceName, DataSource.class);
        } catch (NamingException ex) {
            throw new DataSourceLookupFailureException("Failed to look up JNDI DataSource with name '" + dataSourceName + "'", ex);
        }
    }
}

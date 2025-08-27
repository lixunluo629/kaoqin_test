package org.apache.ibatis.mapping;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/mapping/VendorDatabaseIdProvider.class */
public class VendorDatabaseIdProvider implements DatabaseIdProvider {
    private static final Log log = LogFactory.getLog((Class<?>) VendorDatabaseIdProvider.class);
    private Properties properties;

    @Override // org.apache.ibatis.mapping.DatabaseIdProvider
    public String getDatabaseId(DataSource dataSource) {
        if (dataSource == null) {
            throw new NullPointerException("dataSource cannot be null");
        }
        try {
            return getDatabaseName(dataSource);
        } catch (Exception e) {
            log.error("Could not get a databaseId from dataSource", e);
            return null;
        }
    }

    @Override // org.apache.ibatis.mapping.DatabaseIdProvider
    public void setProperties(Properties p) {
        this.properties = p;
    }

    private String getDatabaseName(DataSource dataSource) throws SQLException {
        String productName = getDatabaseProductName(dataSource);
        if (this.properties != null) {
            for (Map.Entry<Object, Object> property : this.properties.entrySet()) {
                if (productName.contains((String) property.getKey())) {
                    return (String) property.getValue();
                }
            }
            return null;
        }
        return productName;
    }

    private String getDatabaseProductName(DataSource dataSource) throws SQLException {
        Connection con = null;
        try {
            con = dataSource.getConnection();
            DatabaseMetaData metaData = con.getMetaData();
            String databaseProductName = metaData.getDatabaseProductName();
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                }
            }
            return databaseProductName;
        } catch (Throwable th) {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e2) {
                }
            }
            throw th;
        }
    }
}

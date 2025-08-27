package org.apache.ibatis.mapping;

import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/mapping/DatabaseIdProvider.class */
public interface DatabaseIdProvider {
    void setProperties(Properties properties);

    String getDatabaseId(DataSource dataSource) throws SQLException;
}

package org.apache.ibatis.datasource;

import java.util.Properties;
import javax.sql.DataSource;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/datasource/DataSourceFactory.class */
public interface DataSourceFactory {
    void setProperties(Properties properties);

    DataSource getDataSource();
}

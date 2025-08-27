package org.apache.ibatis.datasource.unpooled;

import java.util.Properties;
import javax.sql.DataSource;
import org.apache.ibatis.datasource.DataSourceException;
import org.apache.ibatis.datasource.DataSourceFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/datasource/unpooled/UnpooledDataSourceFactory.class */
public class UnpooledDataSourceFactory implements DataSourceFactory {
    private static final String DRIVER_PROPERTY_PREFIX = "driver.";
    private static final int DRIVER_PROPERTY_PREFIX_LENGTH = DRIVER_PROPERTY_PREFIX.length();
    protected DataSource dataSource = new UnpooledDataSource();

    @Override // org.apache.ibatis.datasource.DataSourceFactory
    public void setProperties(Properties properties) throws NumberFormatException {
        Properties driverProperties = new Properties();
        MetaObject metaDataSource = SystemMetaObject.forObject(this.dataSource);
        for (Object key : properties.keySet()) {
            String propertyName = (String) key;
            if (propertyName.startsWith(DRIVER_PROPERTY_PREFIX)) {
                String value = properties.getProperty(propertyName);
                driverProperties.setProperty(propertyName.substring(DRIVER_PROPERTY_PREFIX_LENGTH), value);
            } else if (metaDataSource.hasSetter(propertyName)) {
                String value2 = (String) properties.get(propertyName);
                Object convertedValue = convertValue(metaDataSource, propertyName, value2);
                metaDataSource.setValue(propertyName, convertedValue);
            } else {
                throw new DataSourceException("Unknown DataSource property: " + propertyName);
            }
        }
        if (driverProperties.size() > 0) {
            metaDataSource.setValue("driverProperties", driverProperties);
        }
    }

    @Override // org.apache.ibatis.datasource.DataSourceFactory
    public DataSource getDataSource() {
        return this.dataSource;
    }

    private Object convertValue(MetaObject metaDataSource, String propertyName, String value) throws NumberFormatException {
        Object convertedValue = value;
        Class<?> targetType = metaDataSource.getSetterType(propertyName);
        if (targetType == Integer.class || targetType == Integer.TYPE) {
            convertedValue = Integer.valueOf(value);
        } else if (targetType == Long.class || targetType == Long.TYPE) {
            convertedValue = Long.valueOf(value);
        } else if (targetType == Boolean.class || targetType == Boolean.TYPE) {
            convertedValue = Boolean.valueOf(value);
        }
        return convertedValue;
    }
}

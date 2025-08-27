package org.springframework.boot.autoconfigure.jdbc;

import com.mysql.jdbc.NonRegisteringDriver;
import io.netty.handler.codec.rtsp.RtspHeaders;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.boot.bind.RelaxedDataBinder;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.util.ClassUtils;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/DataSourceBuilder.class */
public class DataSourceBuilder {
    private static final String[] DATA_SOURCE_TYPE_NAMES = {"org.apache.tomcat.jdbc.pool.DataSource", "com.zaxxer.hikari.HikariDataSource", "org.apache.commons.dbcp.BasicDataSource", "org.apache.commons.dbcp2.BasicDataSource"};
    private Class<? extends DataSource> type;
    private ClassLoader classLoader;
    private Map<String, String> properties = new HashMap();

    public static DataSourceBuilder create() {
        return new DataSourceBuilder(null);
    }

    public static DataSourceBuilder create(ClassLoader classLoader) {
        return new DataSourceBuilder(classLoader);
    }

    public DataSourceBuilder(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public DataSource build() {
        Class<? extends DataSource> type = getType();
        DataSource result = (DataSource) BeanUtils.instantiate(type);
        maybeGetDriverClassName();
        bind(result);
        return result;
    }

    private void maybeGetDriverClassName() {
        if (!this.properties.containsKey("driverClassName") && this.properties.containsKey(RtspHeaders.Values.URL)) {
            String url = this.properties.get(RtspHeaders.Values.URL);
            String driverClass = DatabaseDriver.fromJdbcUrl(url).getDriverClassName();
            this.properties.put("driverClassName", driverClass);
        }
    }

    private void bind(DataSource result) {
        MutablePropertyValues properties = new MutablePropertyValues(this.properties);
        new RelaxedDataBinder(result).withAlias(RtspHeaders.Values.URL, "jdbcUrl").withAlias("username", "user").bind(properties);
    }

    public DataSourceBuilder type(Class<? extends DataSource> type) {
        this.type = type;
        return this;
    }

    public DataSourceBuilder url(String url) {
        this.properties.put(RtspHeaders.Values.URL, url);
        return this;
    }

    public DataSourceBuilder driverClassName(String driverClassName) {
        this.properties.put("driverClassName", driverClassName);
        return this;
    }

    public DataSourceBuilder username(String username) {
        this.properties.put("username", username);
        return this;
    }

    public DataSourceBuilder password(String password) {
        this.properties.put(NonRegisteringDriver.PASSWORD_PROPERTY_KEY, password);
        return this;
    }

    public Class<? extends DataSource> findType() {
        if (this.type != null) {
            return this.type;
        }
        for (String name : DATA_SOURCE_TYPE_NAMES) {
            try {
                return ClassUtils.forName(name, this.classLoader);
            } catch (Exception e) {
            }
        }
        return null;
    }

    private Class<? extends DataSource> getType() {
        Class<? extends DataSource> type = findType();
        if (type != null) {
            return type;
        }
        throw new IllegalStateException("No supported DataSource type found");
    }
}

package org.springframework.boot.autoconfigure.jdbc;

import com.mysql.jdbc.NonRegisteringDriver;
import io.netty.handler.codec.rtsp.RtspHeaders;
import javax.sql.DataSource;
import javax.sql.XADataSource;
import javax.transaction.TransactionManager;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.bind.RelaxedDataBinder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.boot.jta.XADataSourceWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

@AutoConfigureBefore({DataSourceAutoConfiguration.class})
@EnableConfigurationProperties({DataSourceProperties.class})
@Configuration
@ConditionalOnClass({DataSource.class, TransactionManager.class, EmbeddedDatabaseType.class})
@ConditionalOnMissingBean({DataSource.class})
@ConditionalOnBean({XADataSourceWrapper.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/XADataSourceAutoConfiguration.class */
public class XADataSourceAutoConfiguration implements BeanClassLoaderAware {

    @Autowired
    private XADataSourceWrapper wrapper;

    @Autowired
    private DataSourceProperties properties;

    @Autowired(required = false)
    private XADataSource xaDataSource;
    private ClassLoader classLoader;

    @Bean
    public DataSource dataSource() throws Exception {
        XADataSource xaDataSource = this.xaDataSource;
        if (xaDataSource == null) {
            xaDataSource = createXaDataSource();
        }
        return this.wrapper.mo7464wrapDataSource(xaDataSource);
    }

    @Override // org.springframework.beans.factory.BeanClassLoaderAware
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    private XADataSource createXaDataSource() throws LinkageError {
        String className = this.properties.getXa().getDataSourceClassName();
        if (!StringUtils.hasLength(className)) {
            className = DatabaseDriver.fromJdbcUrl(this.properties.determineUrl()).getXaDataSourceClassName();
        }
        Assert.state(StringUtils.hasLength(className), "No XA DataSource class name specified");
        XADataSource dataSource = createXaDataSourceInstance(className);
        bindXaProperties(dataSource, this.properties);
        return dataSource;
    }

    private XADataSource createXaDataSourceInstance(String className) throws LinkageError {
        try {
            Class<?> dataSourceClass = ClassUtils.forName(className, this.classLoader);
            Object instance = BeanUtils.instantiate(dataSourceClass);
            Assert.isInstanceOf(XADataSource.class, instance);
            return (XADataSource) instance;
        } catch (Exception e) {
            throw new IllegalStateException("Unable to create XADataSource instance from '" + className + "'");
        }
    }

    private void bindXaProperties(XADataSource target, DataSourceProperties properties) {
        MutablePropertyValues values = new MutablePropertyValues();
        values.add("user", this.properties.determineUsername());
        values.add(NonRegisteringDriver.PASSWORD_PROPERTY_KEY, this.properties.determinePassword());
        values.add(RtspHeaders.Values.URL, this.properties.determineUrl());
        values.addPropertyValues(properties.getXa().getProperties());
        new RelaxedDataBinder(target).withAlias("user", "username").bind(values);
    }
}

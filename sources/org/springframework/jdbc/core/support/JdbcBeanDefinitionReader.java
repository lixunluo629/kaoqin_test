package org.springframework.jdbc.core.support;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.util.Assert;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/support/JdbcBeanDefinitionReader.class */
public class JdbcBeanDefinitionReader {
    private final PropertiesBeanDefinitionReader propReader;
    private JdbcTemplate jdbcTemplate;

    public JdbcBeanDefinitionReader(BeanDefinitionRegistry beanFactory) {
        this.propReader = new PropertiesBeanDefinitionReader(beanFactory);
    }

    public JdbcBeanDefinitionReader(PropertiesBeanDefinitionReader beanDefinitionReader) {
        Assert.notNull(beanDefinitionReader, "Bean definition reader must not be null");
        this.propReader = beanDefinitionReader;
    }

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        Assert.notNull(jdbcTemplate, "JdbcTemplate must not be null");
        this.jdbcTemplate = jdbcTemplate;
    }

    public void loadBeanDefinitions(String sql) throws DataAccessException, BeansException {
        Assert.notNull(this.jdbcTemplate, "Not fully configured - specify DataSource or JdbcTemplate");
        final Properties props = new Properties();
        this.jdbcTemplate.query(sql, new RowCallbackHandler() { // from class: org.springframework.jdbc.core.support.JdbcBeanDefinitionReader.1
            @Override // org.springframework.jdbc.core.RowCallbackHandler
            public void processRow(ResultSet rs) throws SQLException {
                String beanName = rs.getString(1);
                String property = rs.getString(2);
                String value = rs.getString(3);
                props.setProperty(beanName + '.' + property, value);
            }
        });
        this.propReader.registerBeanDefinitions(props);
    }
}

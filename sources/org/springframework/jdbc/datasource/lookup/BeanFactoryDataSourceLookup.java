package org.springframework.jdbc.datasource.lookup;

import javax.sql.DataSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.util.Assert;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/lookup/BeanFactoryDataSourceLookup.class */
public class BeanFactoryDataSourceLookup implements DataSourceLookup, BeanFactoryAware {
    private BeanFactory beanFactory;

    public BeanFactoryDataSourceLookup() {
    }

    public BeanFactoryDataSourceLookup(BeanFactory beanFactory) {
        Assert.notNull(beanFactory, "BeanFactory is required");
        this.beanFactory = beanFactory;
    }

    @Override // org.springframework.beans.factory.BeanFactoryAware
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override // org.springframework.jdbc.datasource.lookup.DataSourceLookup
    public DataSource getDataSource(String dataSourceName) throws DataSourceLookupFailureException {
        Assert.state(this.beanFactory != null, "BeanFactory is required");
        try {
            return (DataSource) this.beanFactory.getBean(dataSourceName, DataSource.class);
        } catch (BeansException ex) {
            throw new DataSourceLookupFailureException("Failed to look up DataSource bean with name '" + dataSourceName + "'", ex);
        }
    }
}

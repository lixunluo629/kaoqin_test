package org.mybatis.spring.mapper;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.Assert;

/* loaded from: mybatis-spring-1.3.2.jar:org/mybatis/spring/mapper/MapperFactoryBean.class */
public class MapperFactoryBean<T> extends SqlSessionDaoSupport implements FactoryBean<T> {
    private Class<T> mapperInterface;
    private boolean addToConfig = true;

    public MapperFactoryBean() {
    }

    public MapperFactoryBean(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    @Override // org.mybatis.spring.support.SqlSessionDaoSupport, org.springframework.dao.support.DaoSupport
    protected void checkDaoConfig() {
        super.checkDaoConfig();
        Assert.notNull(this.mapperInterface, "Property 'mapperInterface' is required");
        Configuration configuration = getSqlSession().getConfiguration();
        if (this.addToConfig && !configuration.hasMapper(this.mapperInterface)) {
            try {
                try {
                    configuration.addMapper(this.mapperInterface);
                    ErrorContext.instance().reset();
                } catch (Exception e) {
                    this.logger.error("Error while adding the mapper '" + this.mapperInterface + "' to configuration.", e);
                    throw new IllegalArgumentException(e);
                }
            } catch (Throwable th) {
                ErrorContext.instance().reset();
                throw th;
            }
        }
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public T getObject() throws Exception {
        return (T) getSqlSession().getMapper(this.mapperInterface);
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public Class<T> getObjectType() {
        return this.mapperInterface;
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public boolean isSingleton() {
        return true;
    }

    public void setMapperInterface(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public Class<T> getMapperInterface() {
        return this.mapperInterface;
    }

    public void setAddToConfig(boolean addToConfig) {
        this.addToConfig = addToConfig;
    }

    public boolean isAddToConfig() {
        return this.addToConfig;
    }
}

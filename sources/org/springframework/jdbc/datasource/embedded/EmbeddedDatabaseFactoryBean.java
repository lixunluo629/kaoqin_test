package org.springframework.jdbc.datasource.embedded;

import javax.sql.DataSource;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/embedded/EmbeddedDatabaseFactoryBean.class */
public class EmbeddedDatabaseFactoryBean extends EmbeddedDatabaseFactory implements FactoryBean<DataSource>, InitializingBean, DisposableBean {
    private DatabasePopulator databaseCleaner;

    public void setDatabaseCleaner(DatabasePopulator databaseCleaner) {
        this.databaseCleaner = databaseCleaner;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        initDatabase();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.beans.factory.FactoryBean
    public DataSource getObject() {
        return getDataSource();
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public Class<? extends DataSource> getObjectType() {
        return DataSource.class;
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public boolean isSingleton() {
        return true;
    }

    @Override // org.springframework.beans.factory.DisposableBean
    public void destroy() throws DataAccessException {
        if (this.databaseCleaner != null) {
            DatabasePopulatorUtils.execute(this.databaseCleaner, getDataSource());
        }
        shutdownDatabase();
    }
}

package org.springframework.jdbc.datasource.init;

import javax.sql.DataSource;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.util.Assert;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/init/DataSourceInitializer.class */
public class DataSourceInitializer implements InitializingBean, DisposableBean {
    private DataSource dataSource;
    private DatabasePopulator databasePopulator;
    private DatabasePopulator databaseCleaner;
    private boolean enabled = true;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setDatabasePopulator(DatabasePopulator databasePopulator) {
        this.databasePopulator = databasePopulator;
    }

    public void setDatabaseCleaner(DatabasePopulator databaseCleaner) {
        this.databaseCleaner = databaseCleaner;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws DataAccessException {
        execute(this.databasePopulator);
    }

    @Override // org.springframework.beans.factory.DisposableBean
    public void destroy() throws DataAccessException {
        execute(this.databaseCleaner);
    }

    private void execute(DatabasePopulator populator) throws DataAccessException {
        Assert.state(this.dataSource != null, "DataSource must be set");
        if (this.enabled && populator != null) {
            DatabasePopulatorUtils.execute(populator, this.dataSource);
        }
    }
}

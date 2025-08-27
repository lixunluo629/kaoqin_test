package org.springframework.jdbc.datasource.init;

import java.sql.Connection;
import javax.sql.DataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.util.Assert;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/init/DatabasePopulatorUtils.class */
public abstract class DatabasePopulatorUtils {
    public static void execute(DatabasePopulator populator, DataSource dataSource) throws DataAccessException {
        Assert.notNull(populator, "DatabasePopulator must not be null");
        Assert.notNull(dataSource, "DataSource must not be null");
        try {
            Connection connection = DataSourceUtils.getConnection(dataSource);
            try {
                populator.populate(connection);
                DataSourceUtils.releaseConnection(connection, dataSource);
            } catch (Throwable th) {
                DataSourceUtils.releaseConnection(connection, dataSource);
                throw th;
            }
        } catch (Throwable ex) {
            if (ex instanceof ScriptException) {
                throw ((ScriptException) ex);
            }
            throw new UncategorizedScriptException("Failed to execute database script", ex);
        }
    }
}

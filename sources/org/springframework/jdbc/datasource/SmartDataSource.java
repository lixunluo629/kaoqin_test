package org.springframework.jdbc.datasource;

import java.sql.Connection;
import javax.sql.DataSource;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/SmartDataSource.class */
public interface SmartDataSource extends DataSource {
    boolean shouldClose(Connection connection);
}

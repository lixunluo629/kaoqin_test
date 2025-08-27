package org.springframework.jdbc.support;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/DatabaseMetaDataCallback.class */
public interface DatabaseMetaDataCallback {
    Object processMetaData(DatabaseMetaData databaseMetaData) throws SQLException, MetaDataAccessException;
}

package org.springframework.jdbc.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.dao.DataAccessException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/ResultSetExtractor.class */
public interface ResultSetExtractor<T> {
    T extractData(ResultSet resultSet) throws SQLException, DataAccessException;
}

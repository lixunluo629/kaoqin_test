package org.springframework.jdbc.core;

import java.sql.ResultSet;
import java.sql.SQLException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/RowMapper.class */
public interface RowMapper<T> {
    T mapRow(ResultSet resultSet, int i) throws SQLException;
}

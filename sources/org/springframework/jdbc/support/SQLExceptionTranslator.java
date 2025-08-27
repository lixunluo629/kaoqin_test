package org.springframework.jdbc.support;

import java.sql.SQLException;
import org.springframework.dao.DataAccessException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/SQLExceptionTranslator.class */
public interface SQLExceptionTranslator {
    DataAccessException translate(String str, String str2, SQLException sQLException);
}

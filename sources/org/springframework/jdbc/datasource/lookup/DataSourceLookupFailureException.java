package org.springframework.jdbc.datasource.lookup;

import org.springframework.dao.NonTransientDataAccessException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/lookup/DataSourceLookupFailureException.class */
public class DataSourceLookupFailureException extends NonTransientDataAccessException {
    public DataSourceLookupFailureException(String msg) {
        super(msg);
    }

    public DataSourceLookupFailureException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

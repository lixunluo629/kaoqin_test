package org.apache.ibatis.datasource;

import org.apache.ibatis.exceptions.PersistenceException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/datasource/DataSourceException.class */
public class DataSourceException extends PersistenceException {
    private static final long serialVersionUID = -5251396250407091334L;

    public DataSourceException() {
    }

    public DataSourceException(String message) {
        super(message);
    }

    public DataSourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataSourceException(Throwable cause) {
        super(cause);
    }
}

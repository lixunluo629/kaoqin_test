package org.springframework.dao;

/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/dao/QueryTimeoutException.class */
public class QueryTimeoutException extends TransientDataAccessException {
    public QueryTimeoutException(String msg) {
        super(msg);
    }

    public QueryTimeoutException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

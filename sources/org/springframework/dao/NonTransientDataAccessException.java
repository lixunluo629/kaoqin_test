package org.springframework.dao;

/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/dao/NonTransientDataAccessException.class */
public abstract class NonTransientDataAccessException extends DataAccessException {
    public NonTransientDataAccessException(String msg) {
        super(msg);
    }

    public NonTransientDataAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

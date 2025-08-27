package org.springframework.dao;

/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/dao/RecoverableDataAccessException.class */
public class RecoverableDataAccessException extends DataAccessException {
    public RecoverableDataAccessException(String msg) {
        super(msg);
    }

    public RecoverableDataAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

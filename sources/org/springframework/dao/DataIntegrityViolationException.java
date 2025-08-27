package org.springframework.dao;

/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/dao/DataIntegrityViolationException.class */
public class DataIntegrityViolationException extends NonTransientDataAccessException {
    public DataIntegrityViolationException(String msg) {
        super(msg);
    }

    public DataIntegrityViolationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

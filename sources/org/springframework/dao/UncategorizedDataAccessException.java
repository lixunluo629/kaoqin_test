package org.springframework.dao;

/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/dao/UncategorizedDataAccessException.class */
public abstract class UncategorizedDataAccessException extends NonTransientDataAccessException {
    public UncategorizedDataAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

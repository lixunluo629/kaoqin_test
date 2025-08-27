package org.springframework.dao;

/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/dao/PermissionDeniedDataAccessException.class */
public class PermissionDeniedDataAccessException extends NonTransientDataAccessException {
    public PermissionDeniedDataAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

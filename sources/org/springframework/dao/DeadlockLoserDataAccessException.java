package org.springframework.dao;

/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/dao/DeadlockLoserDataAccessException.class */
public class DeadlockLoserDataAccessException extends PessimisticLockingFailureException {
    public DeadlockLoserDataAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

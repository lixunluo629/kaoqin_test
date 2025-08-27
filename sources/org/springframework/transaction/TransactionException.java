package org.springframework.transaction;

import org.springframework.core.NestedRuntimeException;

/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/transaction/TransactionException.class */
public abstract class TransactionException extends NestedRuntimeException {
    public TransactionException(String msg) {
        super(msg);
    }

    public TransactionException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

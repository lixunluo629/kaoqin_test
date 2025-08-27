package org.springframework.transaction;

/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/transaction/TransactionSuspensionNotSupportedException.class */
public class TransactionSuspensionNotSupportedException extends CannotCreateTransactionException {
    public TransactionSuspensionNotSupportedException(String msg) {
        super(msg);
    }

    public TransactionSuspensionNotSupportedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

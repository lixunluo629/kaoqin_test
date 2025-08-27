package org.springframework.transaction;

/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/transaction/IllegalTransactionStateException.class */
public class IllegalTransactionStateException extends TransactionUsageException {
    public IllegalTransactionStateException(String msg) {
        super(msg);
    }

    public IllegalTransactionStateException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

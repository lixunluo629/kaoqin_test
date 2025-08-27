package org.springframework.transaction.support;

import org.springframework.transaction.TransactionException;

/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/transaction/support/TransactionOperations.class */
public interface TransactionOperations {
    <T> T execute(TransactionCallback<T> transactionCallback) throws TransactionException;
}

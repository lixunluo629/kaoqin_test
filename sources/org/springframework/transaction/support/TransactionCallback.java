package org.springframework.transaction.support;

import org.springframework.transaction.TransactionStatus;

/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/transaction/support/TransactionCallback.class */
public interface TransactionCallback<T> {
    T doInTransaction(TransactionStatus transactionStatus);
}

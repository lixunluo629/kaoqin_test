package org.springframework.boot.autoconfigure.jooq;

import org.jooq.Transaction;
import org.springframework.transaction.TransactionStatus;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jooq/SpringTransaction.class */
class SpringTransaction implements Transaction {
    private final TransactionStatus transactionStatus;

    SpringTransaction(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public TransactionStatus getTxStatus() {
        return this.transactionStatus;
    }
}

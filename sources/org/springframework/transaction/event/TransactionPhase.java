package org.springframework.transaction.event;

/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/transaction/event/TransactionPhase.class */
public enum TransactionPhase {
    BEFORE_COMMIT,
    AFTER_COMMIT,
    AFTER_ROLLBACK,
    AFTER_COMPLETION
}

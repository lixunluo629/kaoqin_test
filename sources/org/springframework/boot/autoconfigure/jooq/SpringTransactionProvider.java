package org.springframework.boot.autoconfigure.jooq;

import org.jooq.TransactionContext;
import org.jooq.TransactionProvider;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jooq/SpringTransactionProvider.class */
public class SpringTransactionProvider implements TransactionProvider {
    private final PlatformTransactionManager transactionManager;

    public SpringTransactionProvider(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void begin(TransactionContext context) throws TransactionException {
        TransactionDefinition definition = new DefaultTransactionDefinition(6);
        TransactionStatus status = this.transactionManager.getTransaction(definition);
        context.transaction(new SpringTransaction(status));
    }

    public void commit(TransactionContext ctx) throws TransactionException {
        this.transactionManager.commit(getTransactionStatus(ctx));
    }

    public void rollback(TransactionContext ctx) throws TransactionException {
        this.transactionManager.rollback(getTransactionStatus(ctx));
    }

    private TransactionStatus getTransactionStatus(TransactionContext ctx) {
        SpringTransaction transaction = (SpringTransaction) ctx.transaction();
        return transaction.getTxStatus();
    }
}

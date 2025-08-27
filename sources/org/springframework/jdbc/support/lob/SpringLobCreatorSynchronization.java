package org.springframework.jdbc.support.lob;

import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.util.Assert;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/lob/SpringLobCreatorSynchronization.class */
public class SpringLobCreatorSynchronization extends TransactionSynchronizationAdapter {
    public static final int LOB_CREATOR_SYNCHRONIZATION_ORDER = 800;
    private final LobCreator lobCreator;
    private boolean beforeCompletionCalled = false;

    public SpringLobCreatorSynchronization(LobCreator lobCreator) {
        Assert.notNull(lobCreator, "LobCreator must not be null");
        this.lobCreator = lobCreator;
    }

    @Override // org.springframework.transaction.support.TransactionSynchronizationAdapter, org.springframework.core.Ordered
    public int getOrder() {
        return 800;
    }

    @Override // org.springframework.transaction.support.TransactionSynchronizationAdapter, org.springframework.transaction.support.TransactionSynchronization
    public void beforeCompletion() {
        this.beforeCompletionCalled = true;
        this.lobCreator.close();
    }

    @Override // org.springframework.transaction.support.TransactionSynchronizationAdapter, org.springframework.transaction.support.TransactionSynchronization
    public void afterCompletion(int status) {
        if (!this.beforeCompletionCalled) {
            this.lobCreator.close();
        }
    }
}

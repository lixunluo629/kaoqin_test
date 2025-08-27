package org.springframework.jdbc.support.lob;

import javax.transaction.Synchronization;
import org.springframework.util.Assert;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/lob/JtaLobCreatorSynchronization.class */
public class JtaLobCreatorSynchronization implements Synchronization {
    private final LobCreator lobCreator;
    private boolean beforeCompletionCalled = false;

    public JtaLobCreatorSynchronization(LobCreator lobCreator) {
        Assert.notNull(lobCreator, "LobCreator must not be null");
        this.lobCreator = lobCreator;
    }

    public void beforeCompletion() {
        this.beforeCompletionCalled = true;
        this.lobCreator.close();
    }

    public void afterCompletion(int status) {
        if (!this.beforeCompletionCalled) {
            this.lobCreator.close();
        }
    }
}

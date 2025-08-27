package org.springframework.jdbc.support.lob;

import javax.transaction.TransactionManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/lob/LobCreatorUtils.class */
public abstract class LobCreatorUtils {
    private static final Log logger = LogFactory.getLog(LobCreatorUtils.class);

    public static void registerTransactionSynchronization(LobCreator lobCreator, TransactionManager jtaTransactionManager) throws IllegalStateException {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            logger.debug("Registering Spring transaction synchronization for LobCreator");
            TransactionSynchronizationManager.registerSynchronization(new SpringLobCreatorSynchronization(lobCreator));
            return;
        }
        if (jtaTransactionManager != null) {
            try {
                int jtaStatus = jtaTransactionManager.getStatus();
                if (jtaStatus == 0 || jtaStatus == 1) {
                    logger.debug("Registering JTA transaction synchronization for LobCreator");
                    jtaTransactionManager.getTransaction().registerSynchronization(new JtaLobCreatorSynchronization(lobCreator));
                    return;
                }
            } catch (Throwable ex) {
                throw new TransactionSystemException("Could not register synchronization with JTA TransactionManager", ex);
            }
        }
        throw new IllegalStateException("Active Spring transaction synchronization or active JTA transaction with specified [javax.transaction.TransactionManager] required");
    }
}

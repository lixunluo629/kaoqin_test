package org.springframework.data.transaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.HeuristicCompletionException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/transaction/ChainedTransactionManager.class */
public class ChainedTransactionManager implements PlatformTransactionManager {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) ChainedTransactionManager.class);
    private final List<PlatformTransactionManager> transactionManagers;
    private final SynchronizationManager synchronizationManager;

    public ChainedTransactionManager(PlatformTransactionManager... transactionManagers) {
        this(SpringTransactionSynchronizationManager.INSTANCE, transactionManagers);
    }

    ChainedTransactionManager(SynchronizationManager synchronizationManager, PlatformTransactionManager... transactionManagers) {
        Assert.notNull(synchronizationManager, "SynchronizationManager must not be null!");
        Assert.notNull(transactionManagers, "Transaction managers must not be null!");
        Assert.isTrue(transactionManagers.length > 0, "At least one PlatformTransactionManager must be given!");
        this.synchronizationManager = synchronizationManager;
        this.transactionManagers = Arrays.asList(transactionManagers);
    }

    @Override // org.springframework.transaction.PlatformTransactionManager
    public MultiTransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {
        MultiTransactionStatus mts = new MultiTransactionStatus(this.transactionManagers.get(0));
        if (!this.synchronizationManager.isSynchronizationActive()) {
            this.synchronizationManager.initSynchronization();
            mts.setNewSynchonization();
        }
        try {
            Iterator<PlatformTransactionManager> it = this.transactionManagers.iterator();
            while (it.hasNext()) {
                mts.registerTransactionManager(definition, it.next());
            }
            return mts;
        } catch (Exception ex) {
            Map<PlatformTransactionManager, TransactionStatus> transactionStatuses = mts.getTransactionStatuses();
            for (PlatformTransactionManager transactionManager : this.transactionManagers) {
                try {
                    if (transactionStatuses.get(transactionManager) != null) {
                        transactionManager.rollback(transactionStatuses.get(transactionManager));
                    }
                } catch (Exception ex2) {
                    LOGGER.warn("Rollback exception (" + transactionManager + ") " + ex2.getMessage(), (Throwable) ex2);
                }
            }
            if (mts.isNewSynchonization()) {
                this.synchronizationManager.clearSynchronization();
            }
            throw new CannotCreateTransactionException(ex.getMessage(), ex);
        }
    }

    @Override // org.springframework.transaction.PlatformTransactionManager
    public void commit(TransactionStatus status) throws TransactionException {
        MultiTransactionStatus multiTransactionStatus = (MultiTransactionStatus) status;
        boolean commit = true;
        Exception commitException = null;
        PlatformTransactionManager commitExceptionTransactionManager = null;
        for (PlatformTransactionManager transactionManager : reverse(this.transactionManagers)) {
            if (commit) {
                try {
                    multiTransactionStatus.commit(transactionManager);
                } catch (Exception ex) {
                    commit = false;
                    commitException = ex;
                    commitExceptionTransactionManager = transactionManager;
                }
            } else {
                try {
                    multiTransactionStatus.rollback(transactionManager);
                } catch (Exception ex2) {
                    LOGGER.warn("Rollback exception (after commit) (" + transactionManager + ") " + ex2.getMessage(), (Throwable) ex2);
                }
            }
        }
        if (multiTransactionStatus.isNewSynchonization()) {
            this.synchronizationManager.clearSynchronization();
        }
        if (commitException != null) {
            boolean firstTransactionManagerFailed = commitExceptionTransactionManager == getLastTransactionManager();
            int transactionState = firstTransactionManagerFailed ? 2 : 3;
            throw new HeuristicCompletionException(transactionState, commitException);
        }
    }

    @Override // org.springframework.transaction.PlatformTransactionManager
    public void rollback(TransactionStatus status) throws TransactionException {
        Exception rollbackException = null;
        PlatformTransactionManager rollbackExceptionTransactionManager = null;
        MultiTransactionStatus multiTransactionStatus = (MultiTransactionStatus) status;
        for (PlatformTransactionManager transactionManager : reverse(this.transactionManagers)) {
            try {
                multiTransactionStatus.rollback(transactionManager);
            } catch (Exception ex) {
                if (rollbackException == null) {
                    rollbackException = ex;
                    rollbackExceptionTransactionManager = transactionManager;
                } else {
                    LOGGER.warn("Rollback exception (" + transactionManager + ") " + ex.getMessage(), (Throwable) ex);
                }
            }
        }
        if (multiTransactionStatus.isNewSynchonization()) {
            this.synchronizationManager.clearSynchronization();
        }
        if (rollbackException != null) {
            throw new UnexpectedRollbackException("Rollback exception, originated at (" + rollbackExceptionTransactionManager + ") " + rollbackException.getMessage(), rollbackException);
        }
    }

    private <T> Iterable<T> reverse(Collection<T> collection) {
        List<T> list = new ArrayList<>((Collection<? extends T>) collection);
        Collections.reverse(list);
        return list;
    }

    private PlatformTransactionManager getLastTransactionManager() {
        return this.transactionManagers.get(lastTransactionManagerIndex());
    }

    private int lastTransactionManagerIndex() {
        return this.transactionManagers.size() - 1;
    }
}

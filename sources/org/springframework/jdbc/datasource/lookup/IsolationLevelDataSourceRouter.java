package org.springframework.jdbc.datasource.lookup;

import org.springframework.core.Constants;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/lookup/IsolationLevelDataSourceRouter.class */
public class IsolationLevelDataSourceRouter extends AbstractRoutingDataSource {
    private static final Constants constants = new Constants(TransactionDefinition.class);

    @Override // org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
    protected Object resolveSpecifiedLookupKey(Object lookupKey) {
        if (lookupKey instanceof Integer) {
            return lookupKey;
        }
        if (lookupKey instanceof String) {
            String constantName = (String) lookupKey;
            if (!constantName.startsWith(DefaultTransactionDefinition.PREFIX_ISOLATION)) {
                throw new IllegalArgumentException("Only isolation constants allowed");
            }
            return constants.asNumber(constantName);
        }
        throw new IllegalArgumentException("Invalid lookup key - needs to be isolation level Integer or isolation level name String: " + lookupKey);
    }

    @Override // org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
    protected Object determineCurrentLookupKey() {
        return TransactionSynchronizationManager.getCurrentTransactionIsolationLevel();
    }
}

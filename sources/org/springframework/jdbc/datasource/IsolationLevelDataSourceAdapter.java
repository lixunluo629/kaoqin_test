package org.springframework.jdbc.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import org.springframework.core.Constants;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/IsolationLevelDataSourceAdapter.class */
public class IsolationLevelDataSourceAdapter extends UserCredentialsDataSourceAdapter {
    private static final Constants constants = new Constants(TransactionDefinition.class);
    private Integer isolationLevel;

    public final void setIsolationLevelName(String constantName) throws IllegalArgumentException {
        if (constantName == null || !constantName.startsWith(DefaultTransactionDefinition.PREFIX_ISOLATION)) {
            throw new IllegalArgumentException("Only isolation constants allowed");
        }
        setIsolationLevel(constants.asNumber(constantName).intValue());
    }

    public void setIsolationLevel(int isolationLevel) {
        if (!constants.getValues(DefaultTransactionDefinition.PREFIX_ISOLATION).contains(Integer.valueOf(isolationLevel))) {
            throw new IllegalArgumentException("Only values of isolation constants allowed");
        }
        this.isolationLevel = isolationLevel != -1 ? Integer.valueOf(isolationLevel) : null;
    }

    protected Integer getIsolationLevel() {
        return this.isolationLevel;
    }

    @Override // org.springframework.jdbc.datasource.UserCredentialsDataSourceAdapter
    protected Connection doGetConnection(String username, String password) throws SQLException {
        Connection con = super.doGetConnection(username, password);
        Boolean readOnlyToUse = getCurrentReadOnlyFlag();
        if (readOnlyToUse != null) {
            con.setReadOnly(readOnlyToUse.booleanValue());
        }
        Integer isolationLevelToUse = getCurrentIsolationLevel();
        if (isolationLevelToUse != null) {
            con.setTransactionIsolation(isolationLevelToUse.intValue());
        }
        return con;
    }

    protected Integer getCurrentIsolationLevel() {
        Integer isolationLevelToUse = TransactionSynchronizationManager.getCurrentTransactionIsolationLevel();
        if (isolationLevelToUse == null) {
            isolationLevelToUse = getIsolationLevel();
        }
        return isolationLevelToUse;
    }

    protected Boolean getCurrentReadOnlyFlag() {
        boolean txReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        if (txReadOnly) {
            return Boolean.TRUE;
        }
        return null;
    }
}

package org.springframework.jdbc.core.support;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.util.Assert;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/support/AbstractLobCreatingPreparedStatementCallback.class */
public abstract class AbstractLobCreatingPreparedStatementCallback implements PreparedStatementCallback<Integer> {
    private final LobHandler lobHandler;

    protected abstract void setValues(PreparedStatement preparedStatement, LobCreator lobCreator) throws SQLException, DataAccessException;

    public AbstractLobCreatingPreparedStatementCallback(LobHandler lobHandler) {
        Assert.notNull(lobHandler, "LobHandler must not be null");
        this.lobHandler = lobHandler;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.jdbc.core.PreparedStatementCallback
    public final Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
        LobCreator lobCreator = this.lobHandler.getLobCreator();
        try {
            setValues(ps, lobCreator);
            Integer numValueOf = Integer.valueOf(ps.executeUpdate());
            lobCreator.close();
            return numValueOf;
        } catch (Throwable th) {
            lobCreator.close();
            throw th;
        }
    }
}

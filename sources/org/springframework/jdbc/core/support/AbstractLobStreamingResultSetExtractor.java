package org.springframework.jdbc.core.support;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.LobRetrievalFailureException;
import org.springframework.jdbc.core.ResultSetExtractor;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/support/AbstractLobStreamingResultSetExtractor.class */
public abstract class AbstractLobStreamingResultSetExtractor<T> implements ResultSetExtractor<T> {
    protected abstract void streamData(ResultSet resultSet) throws SQLException, DataAccessException, IOException;

    @Override // org.springframework.jdbc.core.ResultSetExtractor
    public final T extractData(ResultSet rs) throws DataAccessException, SQLException {
        if (!rs.next()) {
            handleNoRowFound();
            return null;
        }
        try {
            streamData(rs);
            if (rs.next()) {
                handleMultipleRowsFound();
            }
            return null;
        } catch (IOException ex) {
            throw new LobRetrievalFailureException("Couldn't stream LOB content", ex);
        }
    }

    protected void handleNoRowFound() throws DataAccessException {
        throw new EmptyResultDataAccessException("LobStreamingResultSetExtractor did not find row in database", 1);
    }

    protected void handleMultipleRowsFound() throws DataAccessException {
        throw new IncorrectResultSizeDataAccessException("LobStreamingResultSetExtractor found multiple rows in database", 1);
    }
}

package org.springframework.jdbc.support.incrementer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/incrementer/AbstractSequenceMaxValueIncrementer.class */
public abstract class AbstractSequenceMaxValueIncrementer extends AbstractDataFieldMaxValueIncrementer {
    protected abstract String getSequenceQuery();

    public AbstractSequenceMaxValueIncrementer() {
    }

    public AbstractSequenceMaxValueIncrementer(DataSource dataSource, String incrementerName) {
        super(dataSource, incrementerName);
    }

    @Override // org.springframework.jdbc.support.incrementer.AbstractDataFieldMaxValueIncrementer
    protected long getNextKey() throws DataAccessException {
        Connection con = DataSourceUtils.getConnection(getDataSource());
        try {
            try {
                Statement stmt = con.createStatement();
                DataSourceUtils.applyTransactionTimeout(stmt, getDataSource());
                ResultSet rs = stmt.executeQuery(getSequenceQuery());
                if (rs.next()) {
                    long j = rs.getLong(1);
                    JdbcUtils.closeResultSet(rs);
                    JdbcUtils.closeStatement(stmt);
                    DataSourceUtils.releaseConnection(con, getDataSource());
                    return j;
                }
                throw new DataAccessResourceFailureException("Sequence query did not return a result");
            } catch (SQLException ex) {
                throw new DataAccessResourceFailureException("Could not obtain sequence value", ex);
            }
        } catch (Throwable th) {
            JdbcUtils.closeResultSet(null);
            JdbcUtils.closeStatement(null);
            DataSourceUtils.releaseConnection(con, getDataSource());
            throw th;
        }
    }
}

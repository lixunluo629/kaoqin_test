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

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/incrementer/AbstractIdentityColumnMaxValueIncrementer.class */
public abstract class AbstractIdentityColumnMaxValueIncrementer extends AbstractColumnMaxValueIncrementer {
    private boolean deleteSpecificValues;
    private long[] valueCache;
    private int nextValueIndex;

    protected abstract String getIncrementStatement();

    protected abstract String getIdentityStatement();

    public AbstractIdentityColumnMaxValueIncrementer() {
        this.deleteSpecificValues = false;
        this.nextValueIndex = -1;
    }

    public AbstractIdentityColumnMaxValueIncrementer(DataSource dataSource, String incrementerName, String columnName) {
        super(dataSource, incrementerName, columnName);
        this.deleteSpecificValues = false;
        this.nextValueIndex = -1;
    }

    public void setDeleteSpecificValues(boolean deleteSpecificValues) {
        this.deleteSpecificValues = deleteSpecificValues;
    }

    public boolean isDeleteSpecificValues() {
        return this.deleteSpecificValues;
    }

    @Override // org.springframework.jdbc.support.incrementer.AbstractDataFieldMaxValueIncrementer
    protected synchronized long getNextKey() throws DataAccessException {
        if (this.nextValueIndex < 0 || this.nextValueIndex >= getCacheSize()) {
            Connection con = DataSourceUtils.getConnection(getDataSource());
            try {
                try {
                    Statement stmt = con.createStatement();
                    DataSourceUtils.applyTransactionTimeout(stmt, getDataSource());
                    this.valueCache = new long[getCacheSize()];
                    this.nextValueIndex = 0;
                    for (int i = 0; i < getCacheSize(); i++) {
                        stmt.executeUpdate(getIncrementStatement());
                        ResultSet rs = stmt.executeQuery(getIdentityStatement());
                        try {
                            if (!rs.next()) {
                                throw new DataAccessResourceFailureException("Identity statement failed after inserting");
                            }
                            this.valueCache[i] = rs.getLong(1);
                            JdbcUtils.closeResultSet(rs);
                        } catch (Throwable th) {
                            JdbcUtils.closeResultSet(rs);
                            throw th;
                        }
                    }
                    stmt.executeUpdate(getDeleteStatement(this.valueCache));
                    JdbcUtils.closeStatement(stmt);
                    DataSourceUtils.releaseConnection(con, getDataSource());
                } catch (SQLException ex) {
                    throw new DataAccessResourceFailureException("Could not increment identity", ex);
                }
            } catch (Throwable th2) {
                JdbcUtils.closeStatement(null);
                DataSourceUtils.releaseConnection(con, getDataSource());
                throw th2;
            }
        }
        long[] jArr = this.valueCache;
        int i2 = this.nextValueIndex;
        this.nextValueIndex = i2 + 1;
        return jArr[i2];
    }

    protected String getDeleteStatement(long[] values) {
        StringBuilder sb = new StringBuilder(64);
        sb.append("delete from ").append(getIncrementerName()).append(" where ").append(getColumnName());
        if (isDeleteSpecificValues()) {
            sb.append(" in (").append(values[0] - 1);
            for (int i = 0; i < values.length - 1; i++) {
                sb.append(", ").append(values[i]);
            }
            sb.append(")");
        } else {
            long maxValue = values[values.length - 1];
            sb.append(" < ").append(maxValue);
        }
        return sb.toString();
    }
}

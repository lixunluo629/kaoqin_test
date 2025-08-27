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

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/incrementer/MySQLMaxValueIncrementer.class */
public class MySQLMaxValueIncrementer extends AbstractColumnMaxValueIncrementer {
    private static final String VALUE_SQL = "select last_insert_id()";
    private long nextId;
    private long maxId;
    private boolean useNewConnection;

    public MySQLMaxValueIncrementer() {
        this.nextId = 0L;
        this.maxId = 0L;
        this.useNewConnection = false;
    }

    public MySQLMaxValueIncrementer(DataSource dataSource, String incrementerName, String columnName) {
        super(dataSource, incrementerName, columnName);
        this.nextId = 0L;
        this.maxId = 0L;
        this.useNewConnection = false;
    }

    public void setUseNewConnection(boolean useNewConnection) {
        this.useNewConnection = useNewConnection;
    }

    @Override // org.springframework.jdbc.support.incrementer.AbstractDataFieldMaxValueIncrementer
    protected synchronized long getNextKey() throws SQLException, DataAccessException {
        Connection con;
        if (this.maxId == this.nextId) {
            Connection con2 = null;
            boolean mustRestoreAutoCommit = false;
            try {
                try {
                    if (this.useNewConnection) {
                        con = getDataSource().getConnection();
                        if (con.getAutoCommit()) {
                            mustRestoreAutoCommit = true;
                            con.setAutoCommit(false);
                        }
                    } else {
                        con = DataSourceUtils.getConnection(getDataSource());
                    }
                    Statement stmt = con.createStatement();
                    if (!this.useNewConnection) {
                        DataSourceUtils.applyTransactionTimeout(stmt, getDataSource());
                    }
                    String columnName = getColumnName();
                    try {
                        stmt.executeUpdate("update " + getIncrementerName() + " set " + columnName + " = last_insert_id(" + columnName + " + " + getCacheSize() + ")");
                        ResultSet rs = stmt.executeQuery(VALUE_SQL);
                        try {
                            if (!rs.next()) {
                                throw new DataAccessResourceFailureException("last_insert_id() failed after executing an update");
                            }
                            this.maxId = rs.getLong(1);
                            JdbcUtils.closeResultSet(rs);
                            this.nextId = (this.maxId - getCacheSize()) + 1;
                            JdbcUtils.closeStatement(stmt);
                            if (con != null) {
                                if (this.useNewConnection) {
                                    try {
                                        con.commit();
                                        if (mustRestoreAutoCommit) {
                                            con.setAutoCommit(true);
                                        }
                                        JdbcUtils.closeConnection(con);
                                    } catch (SQLException e) {
                                        throw new DataAccessResourceFailureException("Unable to commit new sequence value changes for " + getIncrementerName());
                                    }
                                } else {
                                    DataSourceUtils.releaseConnection(con, getDataSource());
                                }
                            }
                        } catch (Throwable th) {
                            JdbcUtils.closeResultSet(rs);
                            throw th;
                        }
                    } catch (SQLException ex) {
                        throw new DataAccessResourceFailureException("Could not increment " + columnName + " for " + getIncrementerName() + " sequence table", ex);
                    }
                } catch (SQLException ex2) {
                    throw new DataAccessResourceFailureException("Could not obtain last_insert_id()", ex2);
                }
            } catch (Throwable th2) {
                JdbcUtils.closeStatement(null);
                if (0 != 0) {
                    if (this.useNewConnection) {
                        try {
                            con2.commit();
                            if (0 != 0) {
                                con2.setAutoCommit(true);
                            }
                            JdbcUtils.closeConnection(null);
                        } catch (SQLException e2) {
                            throw new DataAccessResourceFailureException("Unable to commit new sequence value changes for " + getIncrementerName());
                        }
                    } else {
                        DataSourceUtils.releaseConnection(null, getDataSource());
                    }
                }
                throw th2;
            }
        } else {
            this.nextId++;
        }
        return this.nextId;
    }
}

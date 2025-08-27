package org.springframework.jdbc.support.rowset;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.lang.UsesJava7;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/rowset/ResultSetWrappingSqlRowSet.class */
public class ResultSetWrappingSqlRowSet implements SqlRowSet {
    private static final long serialVersionUID = -4688694393146734764L;
    private final ResultSet resultSet;
    private final SqlRowSetMetaData rowSetMetaData;
    private final Map<String, Integer> columnLabelMap;

    public ResultSetWrappingSqlRowSet(ResultSet resultSet) throws SQLException, InvalidResultSetAccessException {
        this.resultSet = resultSet;
        try {
            this.rowSetMetaData = new ResultSetWrappingSqlRowSetMetaData(resultSet.getMetaData());
            try {
                ResultSetMetaData rsmd = resultSet.getMetaData();
                if (rsmd != null) {
                    int columnCount = rsmd.getColumnCount();
                    this.columnLabelMap = new HashMap(columnCount);
                    for (int i = 1; i <= columnCount; i++) {
                        String key = rsmd.getColumnLabel(i);
                        if (!this.columnLabelMap.containsKey(key)) {
                            this.columnLabelMap.put(key, Integer.valueOf(i));
                        }
                    }
                } else {
                    this.columnLabelMap = Collections.emptyMap();
                }
            } catch (SQLException se) {
                throw new InvalidResultSetAccessException(se);
            }
        } catch (SQLException se2) {
            throw new InvalidResultSetAccessException(se2);
        }
    }

    public final ResultSet getResultSet() {
        return this.resultSet;
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public final SqlRowSetMetaData getMetaData() {
        return this.rowSetMetaData;
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public int findColumn(String columnLabel) throws InvalidResultSetAccessException {
        Integer columnIndex = this.columnLabelMap.get(columnLabel);
        if (columnIndex != null) {
            return columnIndex.intValue();
        }
        try {
            return this.resultSet.findColumn(columnLabel);
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public BigDecimal getBigDecimal(int columnIndex) throws InvalidResultSetAccessException {
        try {
            return this.resultSet.getBigDecimal(columnIndex);
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public BigDecimal getBigDecimal(String columnLabel) throws InvalidResultSetAccessException {
        return getBigDecimal(findColumn(columnLabel));
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public boolean getBoolean(int columnIndex) throws InvalidResultSetAccessException {
        try {
            return this.resultSet.getBoolean(columnIndex);
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public boolean getBoolean(String columnLabel) throws InvalidResultSetAccessException {
        return getBoolean(findColumn(columnLabel));
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public byte getByte(int columnIndex) throws InvalidResultSetAccessException {
        try {
            return this.resultSet.getByte(columnIndex);
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public byte getByte(String columnLabel) throws InvalidResultSetAccessException {
        return getByte(findColumn(columnLabel));
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public Date getDate(int columnIndex) throws InvalidResultSetAccessException {
        try {
            return this.resultSet.getDate(columnIndex);
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public Date getDate(String columnLabel) throws InvalidResultSetAccessException {
        return getDate(findColumn(columnLabel));
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public Date getDate(int columnIndex, Calendar cal) throws InvalidResultSetAccessException {
        try {
            return this.resultSet.getDate(columnIndex, cal);
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public Date getDate(String columnLabel, Calendar cal) throws InvalidResultSetAccessException {
        return getDate(findColumn(columnLabel), cal);
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public double getDouble(int columnIndex) throws InvalidResultSetAccessException {
        try {
            return this.resultSet.getDouble(columnIndex);
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public double getDouble(String columnLabel) throws InvalidResultSetAccessException {
        return getDouble(findColumn(columnLabel));
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public float getFloat(int columnIndex) throws InvalidResultSetAccessException {
        try {
            return this.resultSet.getFloat(columnIndex);
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public float getFloat(String columnLabel) throws InvalidResultSetAccessException {
        return getFloat(findColumn(columnLabel));
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public int getInt(int columnIndex) throws InvalidResultSetAccessException {
        try {
            return this.resultSet.getInt(columnIndex);
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public int getInt(String columnLabel) throws InvalidResultSetAccessException {
        return getInt(findColumn(columnLabel));
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public long getLong(int columnIndex) throws InvalidResultSetAccessException {
        try {
            return this.resultSet.getLong(columnIndex);
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public long getLong(String columnLabel) throws InvalidResultSetAccessException {
        return getLong(findColumn(columnLabel));
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public String getNString(int columnIndex) throws InvalidResultSetAccessException {
        try {
            return this.resultSet.getNString(columnIndex);
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public String getNString(String columnLabel) throws InvalidResultSetAccessException {
        return getNString(findColumn(columnLabel));
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public Object getObject(int columnIndex) throws InvalidResultSetAccessException {
        try {
            return this.resultSet.getObject(columnIndex);
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public Object getObject(String columnLabel) throws InvalidResultSetAccessException {
        return getObject(findColumn(columnLabel));
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public Object getObject(int columnIndex, Map<String, Class<?>> map) throws InvalidResultSetAccessException {
        try {
            return this.resultSet.getObject(columnIndex, map);
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public Object getObject(String columnLabel, Map<String, Class<?>> map) throws InvalidResultSetAccessException {
        return getObject(findColumn(columnLabel), map);
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    @UsesJava7
    public <T> T getObject(int i, Class<T> cls) throws InvalidResultSetAccessException {
        try {
            return (T) this.resultSet.getObject(i, cls);
        } catch (SQLException e) {
            throw new InvalidResultSetAccessException(e);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public <T> T getObject(String str, Class<T> cls) throws InvalidResultSetAccessException {
        return (T) getObject(findColumn(str), cls);
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public short getShort(int columnIndex) throws InvalidResultSetAccessException {
        try {
            return this.resultSet.getShort(columnIndex);
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public short getShort(String columnLabel) throws InvalidResultSetAccessException {
        return getShort(findColumn(columnLabel));
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public String getString(int columnIndex) throws InvalidResultSetAccessException {
        try {
            return this.resultSet.getString(columnIndex);
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public String getString(String columnLabel) throws InvalidResultSetAccessException {
        return getString(findColumn(columnLabel));
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public Time getTime(int columnIndex) throws InvalidResultSetAccessException {
        try {
            return this.resultSet.getTime(columnIndex);
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public Time getTime(String columnLabel) throws InvalidResultSetAccessException {
        return getTime(findColumn(columnLabel));
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public Time getTime(int columnIndex, Calendar cal) throws InvalidResultSetAccessException {
        try {
            return this.resultSet.getTime(columnIndex, cal);
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public Time getTime(String columnLabel, Calendar cal) throws InvalidResultSetAccessException {
        return getTime(findColumn(columnLabel), cal);
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public Timestamp getTimestamp(int columnIndex) throws InvalidResultSetAccessException {
        try {
            return this.resultSet.getTimestamp(columnIndex);
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public Timestamp getTimestamp(String columnLabel) throws InvalidResultSetAccessException {
        return getTimestamp(findColumn(columnLabel));
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public Timestamp getTimestamp(int columnIndex, Calendar cal) throws InvalidResultSetAccessException {
        try {
            return this.resultSet.getTimestamp(columnIndex, cal);
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public Timestamp getTimestamp(String columnLabel, Calendar cal) throws InvalidResultSetAccessException {
        return getTimestamp(findColumn(columnLabel), cal);
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public boolean absolute(int row) throws InvalidResultSetAccessException {
        try {
            return this.resultSet.absolute(row);
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public void afterLast() throws SQLException, InvalidResultSetAccessException {
        try {
            this.resultSet.afterLast();
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public void beforeFirst() throws SQLException, InvalidResultSetAccessException {
        try {
            this.resultSet.beforeFirst();
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public boolean first() throws InvalidResultSetAccessException {
        try {
            return this.resultSet.first();
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public int getRow() throws InvalidResultSetAccessException {
        try {
            return this.resultSet.getRow();
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public boolean isAfterLast() throws InvalidResultSetAccessException {
        try {
            return this.resultSet.isAfterLast();
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public boolean isBeforeFirst() throws InvalidResultSetAccessException {
        try {
            return this.resultSet.isBeforeFirst();
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public boolean isFirst() throws InvalidResultSetAccessException {
        try {
            return this.resultSet.isFirst();
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public boolean isLast() throws InvalidResultSetAccessException {
        try {
            return this.resultSet.isLast();
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public boolean last() throws InvalidResultSetAccessException {
        try {
            return this.resultSet.last();
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public boolean next() throws InvalidResultSetAccessException {
        try {
            return this.resultSet.next();
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public boolean previous() throws InvalidResultSetAccessException {
        try {
            return this.resultSet.previous();
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public boolean relative(int rows) throws InvalidResultSetAccessException {
        try {
            return this.resultSet.relative(rows);
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSet
    public boolean wasNull() throws InvalidResultSetAccessException {
        try {
            return this.resultSet.wasNull();
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }
}

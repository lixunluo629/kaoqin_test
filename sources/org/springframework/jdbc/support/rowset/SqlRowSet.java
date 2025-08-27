package org.springframework.jdbc.support.rowset;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import org.springframework.jdbc.InvalidResultSetAccessException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/rowset/SqlRowSet.class */
public interface SqlRowSet extends Serializable {
    SqlRowSetMetaData getMetaData();

    int findColumn(String str) throws InvalidResultSetAccessException;

    BigDecimal getBigDecimal(int i) throws InvalidResultSetAccessException;

    BigDecimal getBigDecimal(String str) throws InvalidResultSetAccessException;

    boolean getBoolean(int i) throws InvalidResultSetAccessException;

    boolean getBoolean(String str) throws InvalidResultSetAccessException;

    byte getByte(int i) throws InvalidResultSetAccessException;

    byte getByte(String str) throws InvalidResultSetAccessException;

    Date getDate(int i) throws InvalidResultSetAccessException;

    Date getDate(String str) throws InvalidResultSetAccessException;

    Date getDate(int i, Calendar calendar) throws InvalidResultSetAccessException;

    Date getDate(String str, Calendar calendar) throws InvalidResultSetAccessException;

    double getDouble(int i) throws InvalidResultSetAccessException;

    double getDouble(String str) throws InvalidResultSetAccessException;

    float getFloat(int i) throws InvalidResultSetAccessException;

    float getFloat(String str) throws InvalidResultSetAccessException;

    int getInt(int i) throws InvalidResultSetAccessException;

    int getInt(String str) throws InvalidResultSetAccessException;

    long getLong(int i) throws InvalidResultSetAccessException;

    long getLong(String str) throws InvalidResultSetAccessException;

    String getNString(int i) throws InvalidResultSetAccessException;

    String getNString(String str) throws InvalidResultSetAccessException;

    Object getObject(int i) throws InvalidResultSetAccessException;

    Object getObject(String str) throws InvalidResultSetAccessException;

    Object getObject(int i, Map<String, Class<?>> map) throws InvalidResultSetAccessException;

    Object getObject(String str, Map<String, Class<?>> map) throws InvalidResultSetAccessException;

    <T> T getObject(int i, Class<T> cls) throws InvalidResultSetAccessException;

    <T> T getObject(String str, Class<T> cls) throws InvalidResultSetAccessException;

    short getShort(int i) throws InvalidResultSetAccessException;

    short getShort(String str) throws InvalidResultSetAccessException;

    String getString(int i) throws InvalidResultSetAccessException;

    String getString(String str) throws InvalidResultSetAccessException;

    Time getTime(int i) throws InvalidResultSetAccessException;

    Time getTime(String str) throws InvalidResultSetAccessException;

    Time getTime(int i, Calendar calendar) throws InvalidResultSetAccessException;

    Time getTime(String str, Calendar calendar) throws InvalidResultSetAccessException;

    Timestamp getTimestamp(int i) throws InvalidResultSetAccessException;

    Timestamp getTimestamp(String str) throws InvalidResultSetAccessException;

    Timestamp getTimestamp(int i, Calendar calendar) throws InvalidResultSetAccessException;

    Timestamp getTimestamp(String str, Calendar calendar) throws InvalidResultSetAccessException;

    boolean absolute(int i) throws InvalidResultSetAccessException;

    void afterLast() throws InvalidResultSetAccessException;

    void beforeFirst() throws InvalidResultSetAccessException;

    boolean first() throws InvalidResultSetAccessException;

    int getRow() throws InvalidResultSetAccessException;

    boolean isAfterLast() throws InvalidResultSetAccessException;

    boolean isBeforeFirst() throws InvalidResultSetAccessException;

    boolean isFirst() throws InvalidResultSetAccessException;

    boolean isLast() throws InvalidResultSetAccessException;

    boolean last() throws InvalidResultSetAccessException;

    boolean next() throws InvalidResultSetAccessException;

    boolean previous() throws InvalidResultSetAccessException;

    boolean relative(int i) throws InvalidResultSetAccessException;

    boolean wasNull() throws InvalidResultSetAccessException;
}

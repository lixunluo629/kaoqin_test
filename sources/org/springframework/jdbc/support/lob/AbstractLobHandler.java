package org.springframework.jdbc.support.lob;

import java.io.InputStream;
import java.io.Reader;
import java.sql.ResultSet;
import java.sql.SQLException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/lob/AbstractLobHandler.class */
public abstract class AbstractLobHandler implements LobHandler {
    @Override // org.springframework.jdbc.support.lob.LobHandler
    public byte[] getBlobAsBytes(ResultSet rs, String columnName) throws SQLException {
        return getBlobAsBytes(rs, rs.findColumn(columnName));
    }

    @Override // org.springframework.jdbc.support.lob.LobHandler
    public InputStream getBlobAsBinaryStream(ResultSet rs, String columnName) throws SQLException {
        return getBlobAsBinaryStream(rs, rs.findColumn(columnName));
    }

    @Override // org.springframework.jdbc.support.lob.LobHandler
    public String getClobAsString(ResultSet rs, String columnName) throws SQLException {
        return getClobAsString(rs, rs.findColumn(columnName));
    }

    @Override // org.springframework.jdbc.support.lob.LobHandler
    public InputStream getClobAsAsciiStream(ResultSet rs, String columnName) throws SQLException {
        return getClobAsAsciiStream(rs, rs.findColumn(columnName));
    }

    @Override // org.springframework.jdbc.support.lob.LobHandler
    public Reader getClobAsCharacterStream(ResultSet rs, String columnName) throws SQLException {
        return getClobAsCharacterStream(rs, rs.findColumn(columnName));
    }
}

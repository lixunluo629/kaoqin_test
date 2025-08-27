package org.springframework.jdbc.support.lob;

import java.io.InputStream;
import java.io.Reader;
import java.sql.ResultSet;
import java.sql.SQLException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/lob/LobHandler.class */
public interface LobHandler {
    byte[] getBlobAsBytes(ResultSet resultSet, String str) throws SQLException;

    byte[] getBlobAsBytes(ResultSet resultSet, int i) throws SQLException;

    InputStream getBlobAsBinaryStream(ResultSet resultSet, String str) throws SQLException;

    InputStream getBlobAsBinaryStream(ResultSet resultSet, int i) throws SQLException;

    String getClobAsString(ResultSet resultSet, String str) throws SQLException;

    String getClobAsString(ResultSet resultSet, int i) throws SQLException;

    InputStream getClobAsAsciiStream(ResultSet resultSet, String str) throws SQLException;

    InputStream getClobAsAsciiStream(ResultSet resultSet, int i) throws SQLException;

    Reader getClobAsCharacterStream(ResultSet resultSet, String str) throws SQLException;

    Reader getClobAsCharacterStream(ResultSet resultSet, int i) throws SQLException;

    LobCreator getLobCreator();
}

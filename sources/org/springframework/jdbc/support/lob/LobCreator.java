package org.springframework.jdbc.support.lob;

import java.io.Closeable;
import java.io.InputStream;
import java.io.Reader;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/lob/LobCreator.class */
public interface LobCreator extends Closeable {
    void setBlobAsBytes(PreparedStatement preparedStatement, int i, byte[] bArr) throws SQLException;

    void setBlobAsBinaryStream(PreparedStatement preparedStatement, int i, InputStream inputStream, int i2) throws SQLException;

    void setClobAsString(PreparedStatement preparedStatement, int i, String str) throws SQLException;

    void setClobAsAsciiStream(PreparedStatement preparedStatement, int i, InputStream inputStream, int i2) throws SQLException;

    void setClobAsCharacterStream(PreparedStatement preparedStatement, int i, Reader reader, int i2) throws SQLException;

    @Override // java.io.Closeable, java.lang.AutoCloseable
    void close();
}

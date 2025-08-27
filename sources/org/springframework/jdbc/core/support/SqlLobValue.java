package org.springframework.jdbc.core.support;

import java.io.InputStream;
import java.io.Reader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.springframework.jdbc.core.DisposableSqlTypeValue;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/support/SqlLobValue.class */
public class SqlLobValue implements DisposableSqlTypeValue {
    private final Object content;
    private final int length;
    private final LobCreator lobCreator;

    public SqlLobValue(byte[] bytes) {
        this(bytes, new DefaultLobHandler());
    }

    public SqlLobValue(byte[] bytes, LobHandler lobHandler) {
        this.content = bytes;
        this.length = bytes != null ? bytes.length : 0;
        this.lobCreator = lobHandler.getLobCreator();
    }

    public SqlLobValue(String content) {
        this(content, new DefaultLobHandler());
    }

    public SqlLobValue(String content, LobHandler lobHandler) {
        this.content = content;
        this.length = content != null ? content.length() : 0;
        this.lobCreator = lobHandler.getLobCreator();
    }

    public SqlLobValue(InputStream stream, int length) {
        this(stream, length, new DefaultLobHandler());
    }

    public SqlLobValue(InputStream stream, int length, LobHandler lobHandler) {
        this.content = stream;
        this.length = length;
        this.lobCreator = lobHandler.getLobCreator();
    }

    public SqlLobValue(Reader reader, int length) {
        this(reader, length, new DefaultLobHandler());
    }

    public SqlLobValue(Reader reader, int length, LobHandler lobHandler) {
        this.content = reader;
        this.length = length;
        this.lobCreator = lobHandler.getLobCreator();
    }

    @Override // org.springframework.jdbc.core.SqlTypeValue
    public void setTypeValue(PreparedStatement ps, int paramIndex, int sqlType, String typeName) throws SQLException {
        if (sqlType == 2004) {
            if ((this.content instanceof byte[]) || this.content == null) {
                this.lobCreator.setBlobAsBytes(ps, paramIndex, (byte[]) this.content);
                return;
            } else if (this.content instanceof String) {
                this.lobCreator.setBlobAsBytes(ps, paramIndex, ((String) this.content).getBytes());
                return;
            } else {
                if (this.content instanceof InputStream) {
                    this.lobCreator.setBlobAsBinaryStream(ps, paramIndex, (InputStream) this.content, this.length);
                    return;
                }
                throw new IllegalArgumentException("Content type [" + this.content.getClass().getName() + "] not supported for BLOB columns");
            }
        }
        if (sqlType == 2005) {
            if ((this.content instanceof String) || this.content == null) {
                this.lobCreator.setClobAsString(ps, paramIndex, (String) this.content);
                return;
            } else if (this.content instanceof InputStream) {
                this.lobCreator.setClobAsAsciiStream(ps, paramIndex, (InputStream) this.content, this.length);
                return;
            } else {
                if (this.content instanceof Reader) {
                    this.lobCreator.setClobAsCharacterStream(ps, paramIndex, (Reader) this.content, this.length);
                    return;
                }
                throw new IllegalArgumentException("Content type [" + this.content.getClass().getName() + "] not supported for CLOB columns");
            }
        }
        throw new IllegalArgumentException("SqlLobValue only supports SQL types BLOB and CLOB");
    }

    @Override // org.springframework.jdbc.core.DisposableSqlTypeValue
    public void cleanup() {
        this.lobCreator.close();
    }
}

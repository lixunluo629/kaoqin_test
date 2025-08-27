package org.springframework.jdbc.support.lob;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/lob/DefaultLobHandler.class */
public class DefaultLobHandler extends AbstractLobHandler {
    protected final Log logger = LogFactory.getLog(getClass());
    private boolean wrapAsLob = false;
    private boolean streamAsLob = false;
    private boolean createTemporaryLob = false;

    public void setWrapAsLob(boolean wrapAsLob) {
        this.wrapAsLob = wrapAsLob;
    }

    public void setStreamAsLob(boolean streamAsLob) {
        this.streamAsLob = streamAsLob;
    }

    public void setCreateTemporaryLob(boolean createTemporaryLob) {
        this.createTemporaryLob = createTemporaryLob;
    }

    @Override // org.springframework.jdbc.support.lob.LobHandler
    public byte[] getBlobAsBytes(ResultSet rs, int columnIndex) throws SQLException {
        this.logger.debug("Returning BLOB as bytes");
        if (this.wrapAsLob) {
            Blob blob = rs.getBlob(columnIndex);
            return blob.getBytes(1L, (int) blob.length());
        }
        return rs.getBytes(columnIndex);
    }

    @Override // org.springframework.jdbc.support.lob.LobHandler
    public InputStream getBlobAsBinaryStream(ResultSet rs, int columnIndex) throws SQLException {
        this.logger.debug("Returning BLOB as binary stream");
        if (this.wrapAsLob) {
            Blob blob = rs.getBlob(columnIndex);
            return blob.getBinaryStream();
        }
        return rs.getBinaryStream(columnIndex);
    }

    @Override // org.springframework.jdbc.support.lob.LobHandler
    public String getClobAsString(ResultSet rs, int columnIndex) throws SQLException {
        this.logger.debug("Returning CLOB as string");
        if (this.wrapAsLob) {
            Clob clob = rs.getClob(columnIndex);
            return clob.getSubString(1L, (int) clob.length());
        }
        return rs.getString(columnIndex);
    }

    @Override // org.springframework.jdbc.support.lob.LobHandler
    public InputStream getClobAsAsciiStream(ResultSet rs, int columnIndex) throws SQLException {
        this.logger.debug("Returning CLOB as ASCII stream");
        if (this.wrapAsLob) {
            Clob clob = rs.getClob(columnIndex);
            return clob.getAsciiStream();
        }
        return rs.getAsciiStream(columnIndex);
    }

    @Override // org.springframework.jdbc.support.lob.LobHandler
    public Reader getClobAsCharacterStream(ResultSet rs, int columnIndex) throws SQLException {
        this.logger.debug("Returning CLOB as character stream");
        if (this.wrapAsLob) {
            Clob clob = rs.getClob(columnIndex);
            return clob.getCharacterStream();
        }
        return rs.getCharacterStream(columnIndex);
    }

    @Override // org.springframework.jdbc.support.lob.LobHandler
    public LobCreator getLobCreator() {
        return this.createTemporaryLob ? new TemporaryLobCreator() : new DefaultLobCreator();
    }

    /* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/lob/DefaultLobHandler$DefaultLobCreator.class */
    protected class DefaultLobCreator implements LobCreator {
        protected DefaultLobCreator() {
        }

        @Override // org.springframework.jdbc.support.lob.LobCreator
        public void setBlobAsBytes(PreparedStatement ps, int paramIndex, byte[] content) throws SQLException {
            if (!DefaultLobHandler.this.streamAsLob) {
                if (DefaultLobHandler.this.wrapAsLob) {
                    if (content != null) {
                        ps.setBlob(paramIndex, new PassThroughBlob(content));
                    } else {
                        ps.setBlob(paramIndex, (Blob) null);
                    }
                } else {
                    ps.setBytes(paramIndex, content);
                }
            } else if (content != null) {
                ps.setBlob(paramIndex, new ByteArrayInputStream(content), content.length);
            } else {
                ps.setBlob(paramIndex, (Blob) null);
            }
            if (DefaultLobHandler.this.logger.isDebugEnabled()) {
                DefaultLobHandler.this.logger.debug(content != null ? "Set bytes for BLOB with length " + content.length : "Set BLOB to null");
            }
        }

        @Override // org.springframework.jdbc.support.lob.LobCreator
        public void setBlobAsBinaryStream(PreparedStatement ps, int paramIndex, InputStream binaryStream, int contentLength) throws SQLException {
            if (!DefaultLobHandler.this.streamAsLob) {
                if (DefaultLobHandler.this.wrapAsLob) {
                    if (binaryStream != null) {
                        ps.setBlob(paramIndex, new PassThroughBlob(binaryStream, contentLength));
                    } else {
                        ps.setBlob(paramIndex, (Blob) null);
                    }
                } else if (contentLength >= 0) {
                    ps.setBinaryStream(paramIndex, binaryStream, contentLength);
                } else {
                    ps.setBinaryStream(paramIndex, binaryStream);
                }
            } else if (binaryStream != null) {
                if (contentLength >= 0) {
                    ps.setBlob(paramIndex, binaryStream, contentLength);
                } else {
                    ps.setBlob(paramIndex, binaryStream);
                }
            } else {
                ps.setBlob(paramIndex, (Blob) null);
            }
            if (DefaultLobHandler.this.logger.isDebugEnabled()) {
                DefaultLobHandler.this.logger.debug(binaryStream != null ? "Set binary stream for BLOB with length " + contentLength : "Set BLOB to null");
            }
        }

        @Override // org.springframework.jdbc.support.lob.LobCreator
        public void setClobAsString(PreparedStatement ps, int paramIndex, String content) throws SQLException {
            if (!DefaultLobHandler.this.streamAsLob) {
                if (DefaultLobHandler.this.wrapAsLob) {
                    if (content != null) {
                        ps.setClob(paramIndex, new PassThroughClob(content));
                    } else {
                        ps.setClob(paramIndex, (Clob) null);
                    }
                } else {
                    ps.setString(paramIndex, content);
                }
            } else if (content != null) {
                ps.setClob(paramIndex, new StringReader(content), content.length());
            } else {
                ps.setClob(paramIndex, (Clob) null);
            }
            if (DefaultLobHandler.this.logger.isDebugEnabled()) {
                DefaultLobHandler.this.logger.debug(content != null ? "Set string for CLOB with length " + content.length() : "Set CLOB to null");
            }
        }

        @Override // org.springframework.jdbc.support.lob.LobCreator
        public void setClobAsAsciiStream(PreparedStatement ps, int paramIndex, InputStream asciiStream, int contentLength) throws SQLException {
            if (!DefaultLobHandler.this.streamAsLob) {
                if (DefaultLobHandler.this.wrapAsLob) {
                    if (asciiStream != null) {
                        ps.setClob(paramIndex, new PassThroughClob(asciiStream, contentLength));
                    } else {
                        ps.setClob(paramIndex, (Clob) null);
                    }
                } else if (contentLength >= 0) {
                    ps.setAsciiStream(paramIndex, asciiStream, contentLength);
                } else {
                    ps.setAsciiStream(paramIndex, asciiStream);
                }
            } else if (asciiStream != null) {
                try {
                    Reader reader = new InputStreamReader(asciiStream, "US-ASCII");
                    if (contentLength >= 0) {
                        ps.setClob(paramIndex, reader, contentLength);
                    } else {
                        ps.setClob(paramIndex, reader);
                    }
                } catch (UnsupportedEncodingException ex) {
                    throw new SQLException("US-ASCII encoding not supported: " + ex);
                }
            } else {
                ps.setClob(paramIndex, (Clob) null);
            }
            if (DefaultLobHandler.this.logger.isDebugEnabled()) {
                DefaultLobHandler.this.logger.debug(asciiStream != null ? "Set ASCII stream for CLOB with length " + contentLength : "Set CLOB to null");
            }
        }

        @Override // org.springframework.jdbc.support.lob.LobCreator
        public void setClobAsCharacterStream(PreparedStatement ps, int paramIndex, Reader characterStream, int contentLength) throws SQLException {
            if (!DefaultLobHandler.this.streamAsLob) {
                if (DefaultLobHandler.this.wrapAsLob) {
                    if (characterStream != null) {
                        ps.setClob(paramIndex, new PassThroughClob(characterStream, contentLength));
                    } else {
                        ps.setClob(paramIndex, (Clob) null);
                    }
                } else if (contentLength >= 0) {
                    ps.setCharacterStream(paramIndex, characterStream, contentLength);
                } else {
                    ps.setCharacterStream(paramIndex, characterStream);
                }
            } else if (characterStream != null) {
                if (contentLength >= 0) {
                    ps.setClob(paramIndex, characterStream, contentLength);
                } else {
                    ps.setClob(paramIndex, characterStream);
                }
            } else {
                ps.setClob(paramIndex, (Clob) null);
            }
            if (DefaultLobHandler.this.logger.isDebugEnabled()) {
                DefaultLobHandler.this.logger.debug(characterStream != null ? "Set character stream for CLOB with length " + contentLength : "Set CLOB to null");
            }
        }

        @Override // org.springframework.jdbc.support.lob.LobCreator, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
        }
    }
}

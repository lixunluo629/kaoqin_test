package org.springframework.jdbc.support.lob;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.sql.Clob;
import java.sql.SQLException;
import org.springframework.util.FileCopyUtils;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/lob/PassThroughClob.class */
class PassThroughClob implements Clob {
    private String content;
    private Reader characterStream;
    private InputStream asciiStream;
    private long contentLength;

    public PassThroughClob(String content) {
        this.content = content;
        this.contentLength = content.length();
    }

    public PassThroughClob(Reader characterStream, long contentLength) {
        this.characterStream = characterStream;
        this.contentLength = contentLength;
    }

    public PassThroughClob(InputStream asciiStream, long contentLength) {
        this.asciiStream = asciiStream;
        this.contentLength = contentLength;
    }

    @Override // java.sql.Clob
    public long length() throws SQLException {
        return this.contentLength;
    }

    @Override // java.sql.Clob
    public Reader getCharacterStream() throws SQLException {
        try {
            if (this.content != null) {
                return new StringReader(this.content);
            }
            if (this.characterStream != null) {
                return this.characterStream;
            }
            return new InputStreamReader(this.asciiStream, "US-ASCII");
        } catch (UnsupportedEncodingException ex) {
            throw new SQLException("US-ASCII encoding not supported: " + ex);
        }
    }

    @Override // java.sql.Clob
    public InputStream getAsciiStream() throws SQLException {
        try {
            if (this.content != null) {
                return new ByteArrayInputStream(this.content.getBytes("US-ASCII"));
            }
            if (this.characterStream != null) {
                String tempContent = FileCopyUtils.copyToString(this.characterStream);
                return new ByteArrayInputStream(tempContent.getBytes("US-ASCII"));
            }
            return this.asciiStream;
        } catch (UnsupportedEncodingException ex) {
            throw new SQLException("US-ASCII encoding not supported: " + ex);
        } catch (IOException ex2) {
            throw new SQLException("Failed to read stream content: " + ex2);
        }
    }

    @Override // java.sql.Clob
    public Reader getCharacterStream(long pos, long length) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // java.sql.Clob
    public Writer setCharacterStream(long pos) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // java.sql.Clob
    public OutputStream setAsciiStream(long pos) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // java.sql.Clob
    public String getSubString(long pos, int length) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // java.sql.Clob
    public int setString(long pos, String str) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // java.sql.Clob
    public int setString(long pos, String str, int offset, int len) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // java.sql.Clob
    public long position(String searchstr, long start) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // java.sql.Clob
    public long position(Clob searchstr, long start) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // java.sql.Clob
    public void truncate(long len) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // java.sql.Clob
    public void free() throws SQLException {
    }
}

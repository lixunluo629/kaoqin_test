package org.springframework.jdbc.support.lob;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/lob/PassThroughBlob.class */
class PassThroughBlob implements Blob {
    private byte[] content;
    private InputStream binaryStream;
    private long contentLength;

    public PassThroughBlob(byte[] content) {
        this.content = content;
        this.contentLength = content.length;
    }

    public PassThroughBlob(InputStream binaryStream, long contentLength) {
        this.binaryStream = binaryStream;
        this.contentLength = contentLength;
    }

    @Override // java.sql.Blob
    public long length() throws SQLException {
        return this.contentLength;
    }

    @Override // java.sql.Blob
    public InputStream getBinaryStream() throws SQLException {
        return this.content != null ? new ByteArrayInputStream(this.content) : this.binaryStream;
    }

    @Override // java.sql.Blob
    public InputStream getBinaryStream(long pos, long length) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // java.sql.Blob
    public OutputStream setBinaryStream(long pos) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // java.sql.Blob
    public byte[] getBytes(long pos, int length) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // java.sql.Blob
    public int setBytes(long pos, byte[] bytes) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // java.sql.Blob
    public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // java.sql.Blob
    public long position(byte[] pattern, long start) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // java.sql.Blob
    public long position(Blob pattern, long start) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // java.sql.Blob
    public void truncate(long len) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // java.sql.Blob
    public void free() throws SQLException {
    }
}

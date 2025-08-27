package com.mysql.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.Properties;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/NamedPipeSocketFactory.class */
public class NamedPipeSocketFactory implements SocketFactory, SocketMetadata {
    public static final String NAMED_PIPE_PROP_NAME = "namedPipePath";
    private Socket namedPipeSocket;

    /* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/NamedPipeSocketFactory$NamedPipeSocket.class */
    class NamedPipeSocket extends Socket {
        private boolean isClosed = false;
        private RandomAccessFile namedPipeFile;

        NamedPipeSocket(String filePath) throws IOException {
            if (filePath == null || filePath.length() == 0) {
                throw new IOException(Messages.getString("NamedPipeSocketFactory.4"));
            }
            this.namedPipeFile = new RandomAccessFile(filePath, "rw");
        }

        @Override // java.net.Socket, java.io.Closeable, java.lang.AutoCloseable
        public synchronized void close() throws IOException {
            this.namedPipeFile.close();
            this.isClosed = true;
        }

        @Override // java.net.Socket
        public InputStream getInputStream() throws IOException {
            return NamedPipeSocketFactory.this.new RandomAccessFileInputStream(this.namedPipeFile);
        }

        @Override // java.net.Socket
        public OutputStream getOutputStream() throws IOException {
            return NamedPipeSocketFactory.this.new RandomAccessFileOutputStream(this.namedPipeFile);
        }

        @Override // java.net.Socket
        public boolean isClosed() {
            return this.isClosed;
        }
    }

    /* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/NamedPipeSocketFactory$RandomAccessFileInputStream.class */
    class RandomAccessFileInputStream extends InputStream {
        RandomAccessFile raFile;

        RandomAccessFileInputStream(RandomAccessFile file) {
            this.raFile = file;
        }

        @Override // java.io.InputStream
        public int available() throws IOException {
            return -1;
        }

        @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            this.raFile.close();
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            return this.raFile.read();
        }

        @Override // java.io.InputStream
        public int read(byte[] b) throws IOException {
            return this.raFile.read(b);
        }

        @Override // java.io.InputStream
        public int read(byte[] b, int off, int len) throws IOException {
            return this.raFile.read(b, off, len);
        }
    }

    /* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/NamedPipeSocketFactory$RandomAccessFileOutputStream.class */
    class RandomAccessFileOutputStream extends OutputStream {
        RandomAccessFile raFile;

        RandomAccessFileOutputStream(RandomAccessFile file) {
            this.raFile = file;
        }

        @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            this.raFile.close();
        }

        @Override // java.io.OutputStream
        public void write(byte[] b) throws IOException {
            this.raFile.write(b);
        }

        @Override // java.io.OutputStream
        public void write(byte[] b, int off, int len) throws IOException {
            this.raFile.write(b, off, len);
        }

        @Override // java.io.OutputStream
        public void write(int b) throws IOException {
        }
    }

    @Override // com.mysql.jdbc.SocketFactory
    public Socket afterHandshake() throws IOException {
        return this.namedPipeSocket;
    }

    @Override // com.mysql.jdbc.SocketFactory
    public Socket beforeHandshake() throws IOException {
        return this.namedPipeSocket;
    }

    @Override // com.mysql.jdbc.SocketFactory
    public Socket connect(String host, int portNumber, Properties props) throws IOException {
        String namedPipePath = props.getProperty(NAMED_PIPE_PROP_NAME);
        if (namedPipePath == null) {
            namedPipePath = "\\\\.\\pipe\\MySQL";
        } else if (namedPipePath.length() == 0) {
            throw new SocketException(Messages.getString("NamedPipeSocketFactory.2") + NAMED_PIPE_PROP_NAME + Messages.getString("NamedPipeSocketFactory.3"));
        }
        this.namedPipeSocket = new NamedPipeSocket(namedPipePath);
        return this.namedPipeSocket;
    }

    @Override // com.mysql.jdbc.SocketMetadata
    public boolean isLocallyConnected(ConnectionImpl conn) throws SQLException {
        return true;
    }
}

package org.apache.catalina.connector;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import org.apache.catalina.security.SecurityUtil;
import org.apache.tomcat.util.res.StringManager;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/connector/CoyoteInputStream.class */
public class CoyoteInputStream extends ServletInputStream {
    protected static final StringManager sm = StringManager.getManager((Class<?>) CoyoteInputStream.class);
    protected InputBuffer ib;

    protected CoyoteInputStream(InputBuffer ib) {
        this.ib = ib;
    }

    void clear() {
        this.ib = null;
    }

    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        checkNonBlockingRead();
        if (SecurityUtil.isPackageProtectionEnabled()) {
            try {
                Integer result = (Integer) AccessController.doPrivileged(new PrivilegedExceptionAction<Integer>() { // from class: org.apache.catalina.connector.CoyoteInputStream.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedExceptionAction
                    public Integer run() throws IOException {
                        Integer integer = Integer.valueOf(CoyoteInputStream.this.ib.readByte());
                        return integer;
                    }
                });
                return result.intValue();
            } catch (PrivilegedActionException pae) {
                Exception e = pae.getException();
                if (e instanceof IOException) {
                    throw ((IOException) e);
                }
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return this.ib.readByte();
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        if (SecurityUtil.isPackageProtectionEnabled()) {
            try {
                Integer result = (Integer) AccessController.doPrivileged(new PrivilegedExceptionAction<Integer>() { // from class: org.apache.catalina.connector.CoyoteInputStream.2
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedExceptionAction
                    public Integer run() throws IOException {
                        Integer integer = Integer.valueOf(CoyoteInputStream.this.ib.available());
                        return integer;
                    }
                });
                return result.intValue();
            } catch (PrivilegedActionException pae) {
                Exception e = pae.getException();
                if (e instanceof IOException) {
                    throw ((IOException) e);
                }
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return this.ib.available();
    }

    @Override // java.io.InputStream
    public int read(final byte[] b) throws IOException {
        checkNonBlockingRead();
        if (SecurityUtil.isPackageProtectionEnabled()) {
            try {
                Integer result = (Integer) AccessController.doPrivileged(new PrivilegedExceptionAction<Integer>() { // from class: org.apache.catalina.connector.CoyoteInputStream.3
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedExceptionAction
                    public Integer run() throws IOException {
                        Integer integer = Integer.valueOf(CoyoteInputStream.this.ib.read(b, 0, b.length));
                        return integer;
                    }
                });
                return result.intValue();
            } catch (PrivilegedActionException pae) {
                Exception e = pae.getException();
                if (e instanceof IOException) {
                    throw ((IOException) e);
                }
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return this.ib.read(b, 0, b.length);
    }

    @Override // java.io.InputStream
    public int read(final byte[] b, final int off, final int len) throws IOException {
        checkNonBlockingRead();
        if (SecurityUtil.isPackageProtectionEnabled()) {
            try {
                Integer result = (Integer) AccessController.doPrivileged(new PrivilegedExceptionAction<Integer>() { // from class: org.apache.catalina.connector.CoyoteInputStream.4
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedExceptionAction
                    public Integer run() throws IOException {
                        Integer integer = Integer.valueOf(CoyoteInputStream.this.ib.read(b, off, len));
                        return integer;
                    }
                });
                return result.intValue();
            } catch (PrivilegedActionException pae) {
                Exception e = pae.getException();
                if (e instanceof IOException) {
                    throw ((IOException) e);
                }
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return this.ib.read(b, off, len);
    }

    public int read(final ByteBuffer b) throws IOException {
        checkNonBlockingRead();
        if (SecurityUtil.isPackageProtectionEnabled()) {
            try {
                Integer result = (Integer) AccessController.doPrivileged(new PrivilegedExceptionAction<Integer>() { // from class: org.apache.catalina.connector.CoyoteInputStream.5
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedExceptionAction
                    public Integer run() throws IOException {
                        Integer integer = Integer.valueOf(CoyoteInputStream.this.ib.read(b));
                        return integer;
                    }
                });
                return result.intValue();
            } catch (PrivilegedActionException pae) {
                Exception e = pae.getException();
                if (e instanceof IOException) {
                    throw ((IOException) e);
                }
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return this.ib.read(b);
    }

    @Override // javax.servlet.ServletInputStream
    public int readLine(byte[] b, int off, int len) throws IOException {
        return super.readLine(b, off, len);
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws PrivilegedActionException, IOException {
        if (SecurityUtil.isPackageProtectionEnabled()) {
            try {
                AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() { // from class: org.apache.catalina.connector.CoyoteInputStream.6
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedExceptionAction
                    public Void run() throws IOException {
                        CoyoteInputStream.this.ib.close();
                        return null;
                    }
                });
                return;
            } catch (PrivilegedActionException pae) {
                Exception e = pae.getException();
                if (e instanceof IOException) {
                    throw ((IOException) e);
                }
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        this.ib.close();
    }

    @Override // javax.servlet.ServletInputStream
    public boolean isFinished() {
        return this.ib.isFinished();
    }

    @Override // javax.servlet.ServletInputStream
    public boolean isReady() {
        return this.ib.isReady();
    }

    @Override // javax.servlet.ServletInputStream
    public void setReadListener(ReadListener listener) {
        this.ib.setReadListener(listener);
    }

    private void checkNonBlockingRead() {
        if (!this.ib.isBlocking() && !this.ib.isReady()) {
            throw new IllegalStateException(sm.getString("coyoteInputStream.nbNotready"));
        }
    }
}

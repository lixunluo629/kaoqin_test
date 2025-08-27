package io.netty.handler.ssl;

import io.netty.internal.tcnative.SSL;
import io.netty.internal.tcnative.SSLContext;
import java.util.concurrent.locks.Lock;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/OpenSslServerSessionContext.class */
public final class OpenSslServerSessionContext extends OpenSslSessionContext {
    OpenSslServerSessionContext(ReferenceCountedOpenSslContext context, OpenSslKeyMaterialProvider provider) {
        super(context, provider);
    }

    @Override // javax.net.ssl.SSLSessionContext
    public void setSessionTimeout(int seconds) {
        if (seconds < 0) {
            throw new IllegalArgumentException();
        }
        Lock writerLock = this.context.ctxLock.writeLock();
        writerLock.lock();
        try {
            SSLContext.setSessionCacheTimeout(this.context.ctx, seconds);
        } finally {
            writerLock.unlock();
        }
    }

    @Override // javax.net.ssl.SSLSessionContext
    public int getSessionTimeout() {
        Lock readerLock = this.context.ctxLock.readLock();
        readerLock.lock();
        try {
            return (int) SSLContext.getSessionCacheTimeout(this.context.ctx);
        } finally {
            readerLock.unlock();
        }
    }

    @Override // javax.net.ssl.SSLSessionContext
    public void setSessionCacheSize(int size) {
        if (size < 0) {
            throw new IllegalArgumentException();
        }
        Lock writerLock = this.context.ctxLock.writeLock();
        writerLock.lock();
        try {
            SSLContext.setSessionCacheSize(this.context.ctx, size);
        } finally {
            writerLock.unlock();
        }
    }

    @Override // javax.net.ssl.SSLSessionContext
    public int getSessionCacheSize() {
        Lock readerLock = this.context.ctxLock.readLock();
        readerLock.lock();
        try {
            return (int) SSLContext.getSessionCacheSize(this.context.ctx);
        } finally {
            readerLock.unlock();
        }
    }

    @Override // io.netty.handler.ssl.OpenSslSessionContext
    public void setSessionCacheEnabled(boolean enabled) {
        long mode = enabled ? SSL.SSL_SESS_CACHE_SERVER : SSL.SSL_SESS_CACHE_OFF;
        Lock writerLock = this.context.ctxLock.writeLock();
        writerLock.lock();
        try {
            SSLContext.setSessionCacheMode(this.context.ctx, mode);
            writerLock.unlock();
        } catch (Throwable th) {
            writerLock.unlock();
            throw th;
        }
    }

    @Override // io.netty.handler.ssl.OpenSslSessionContext
    public boolean isSessionCacheEnabled() {
        Lock readerLock = this.context.ctxLock.readLock();
        readerLock.lock();
        try {
            return SSLContext.getSessionCacheMode(this.context.ctx) == SSL.SSL_SESS_CACHE_SERVER;
        } finally {
            readerLock.unlock();
        }
    }

    public boolean setSessionIdContext(byte[] sidCtx) {
        Lock writerLock = this.context.ctxLock.writeLock();
        writerLock.lock();
        try {
            boolean sessionIdContext = SSLContext.setSessionIdContext(this.context.ctx, sidCtx);
            writerLock.unlock();
            return sessionIdContext;
        } catch (Throwable th) {
            writerLock.unlock();
            throw th;
        }
    }
}

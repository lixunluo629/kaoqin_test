package io.netty.handler.ssl;

import io.netty.internal.tcnative.SSLContext;
import java.util.concurrent.locks.Lock;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/OpenSslSessionStats.class */
public final class OpenSslSessionStats {
    private final ReferenceCountedOpenSslContext context;

    OpenSslSessionStats(ReferenceCountedOpenSslContext context) {
        this.context = context;
    }

    public long number() {
        Lock readerLock = this.context.ctxLock.readLock();
        readerLock.lock();
        try {
            long jSessionNumber = SSLContext.sessionNumber(this.context.ctx);
            readerLock.unlock();
            return jSessionNumber;
        } catch (Throwable th) {
            readerLock.unlock();
            throw th;
        }
    }

    public long connect() {
        Lock readerLock = this.context.ctxLock.readLock();
        readerLock.lock();
        try {
            long jSessionConnect = SSLContext.sessionConnect(this.context.ctx);
            readerLock.unlock();
            return jSessionConnect;
        } catch (Throwable th) {
            readerLock.unlock();
            throw th;
        }
    }

    public long connectGood() {
        Lock readerLock = this.context.ctxLock.readLock();
        readerLock.lock();
        try {
            long jSessionConnectGood = SSLContext.sessionConnectGood(this.context.ctx);
            readerLock.unlock();
            return jSessionConnectGood;
        } catch (Throwable th) {
            readerLock.unlock();
            throw th;
        }
    }

    public long connectRenegotiate() {
        Lock readerLock = this.context.ctxLock.readLock();
        readerLock.lock();
        try {
            long jSessionConnectRenegotiate = SSLContext.sessionConnectRenegotiate(this.context.ctx);
            readerLock.unlock();
            return jSessionConnectRenegotiate;
        } catch (Throwable th) {
            readerLock.unlock();
            throw th;
        }
    }

    public long accept() {
        Lock readerLock = this.context.ctxLock.readLock();
        readerLock.lock();
        try {
            long jSessionAccept = SSLContext.sessionAccept(this.context.ctx);
            readerLock.unlock();
            return jSessionAccept;
        } catch (Throwable th) {
            readerLock.unlock();
            throw th;
        }
    }

    public long acceptGood() {
        Lock readerLock = this.context.ctxLock.readLock();
        readerLock.lock();
        try {
            long jSessionAcceptGood = SSLContext.sessionAcceptGood(this.context.ctx);
            readerLock.unlock();
            return jSessionAcceptGood;
        } catch (Throwable th) {
            readerLock.unlock();
            throw th;
        }
    }

    public long acceptRenegotiate() {
        Lock readerLock = this.context.ctxLock.readLock();
        readerLock.lock();
        try {
            long jSessionAcceptRenegotiate = SSLContext.sessionAcceptRenegotiate(this.context.ctx);
            readerLock.unlock();
            return jSessionAcceptRenegotiate;
        } catch (Throwable th) {
            readerLock.unlock();
            throw th;
        }
    }

    public long hits() {
        Lock readerLock = this.context.ctxLock.readLock();
        readerLock.lock();
        try {
            long jSessionHits = SSLContext.sessionHits(this.context.ctx);
            readerLock.unlock();
            return jSessionHits;
        } catch (Throwable th) {
            readerLock.unlock();
            throw th;
        }
    }

    public long cbHits() {
        Lock readerLock = this.context.ctxLock.readLock();
        readerLock.lock();
        try {
            long jSessionCbHits = SSLContext.sessionCbHits(this.context.ctx);
            readerLock.unlock();
            return jSessionCbHits;
        } catch (Throwable th) {
            readerLock.unlock();
            throw th;
        }
    }

    public long misses() {
        Lock readerLock = this.context.ctxLock.readLock();
        readerLock.lock();
        try {
            long jSessionMisses = SSLContext.sessionMisses(this.context.ctx);
            readerLock.unlock();
            return jSessionMisses;
        } catch (Throwable th) {
            readerLock.unlock();
            throw th;
        }
    }

    public long timeouts() {
        Lock readerLock = this.context.ctxLock.readLock();
        readerLock.lock();
        try {
            long jSessionTimeouts = SSLContext.sessionTimeouts(this.context.ctx);
            readerLock.unlock();
            return jSessionTimeouts;
        } catch (Throwable th) {
            readerLock.unlock();
            throw th;
        }
    }

    public long cacheFull() {
        Lock readerLock = this.context.ctxLock.readLock();
        readerLock.lock();
        try {
            long jSessionCacheFull = SSLContext.sessionCacheFull(this.context.ctx);
            readerLock.unlock();
            return jSessionCacheFull;
        } catch (Throwable th) {
            readerLock.unlock();
            throw th;
        }
    }

    public long ticketKeyFail() {
        Lock readerLock = this.context.ctxLock.readLock();
        readerLock.lock();
        try {
            long jSessionTicketKeyFail = SSLContext.sessionTicketKeyFail(this.context.ctx);
            readerLock.unlock();
            return jSessionTicketKeyFail;
        } catch (Throwable th) {
            readerLock.unlock();
            throw th;
        }
    }

    public long ticketKeyNew() {
        Lock readerLock = this.context.ctxLock.readLock();
        readerLock.lock();
        try {
            long jSessionTicketKeyNew = SSLContext.sessionTicketKeyNew(this.context.ctx);
            readerLock.unlock();
            return jSessionTicketKeyNew;
        } catch (Throwable th) {
            readerLock.unlock();
            throw th;
        }
    }

    public long ticketKeyRenew() {
        Lock readerLock = this.context.ctxLock.readLock();
        readerLock.lock();
        try {
            long jSessionTicketKeyRenew = SSLContext.sessionTicketKeyRenew(this.context.ctx);
            readerLock.unlock();
            return jSessionTicketKeyRenew;
        } catch (Throwable th) {
            readerLock.unlock();
            throw th;
        }
    }

    public long ticketKeyResume() {
        Lock readerLock = this.context.ctxLock.readLock();
        readerLock.lock();
        try {
            long jSessionTicketKeyResume = SSLContext.sessionTicketKeyResume(this.context.ctx);
            readerLock.unlock();
            return jSessionTicketKeyResume;
        } catch (Throwable th) {
            readerLock.unlock();
            throw th;
        }
    }
}

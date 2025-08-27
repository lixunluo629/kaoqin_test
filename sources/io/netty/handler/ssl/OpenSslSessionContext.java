package io.netty.handler.ssl;

import io.netty.internal.tcnative.SSL;
import io.netty.internal.tcnative.SSLContext;
import io.netty.internal.tcnative.SessionTicketKey;
import io.netty.util.internal.ObjectUtil;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.Lock;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSessionContext;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/OpenSslSessionContext.class */
public abstract class OpenSslSessionContext implements SSLSessionContext {
    private static final Enumeration<byte[]> EMPTY = new EmptyEnumeration();
    private final OpenSslSessionStats stats;
    private final OpenSslKeyMaterialProvider provider;
    final ReferenceCountedOpenSslContext context;

    public abstract void setSessionCacheEnabled(boolean z);

    public abstract boolean isSessionCacheEnabled();

    OpenSslSessionContext(ReferenceCountedOpenSslContext context, OpenSslKeyMaterialProvider provider) {
        this.context = context;
        this.provider = provider;
        this.stats = new OpenSslSessionStats(context);
    }

    final boolean useKeyManager() {
        return this.provider != null;
    }

    @Override // javax.net.ssl.SSLSessionContext
    public SSLSession getSession(byte[] bytes) {
        ObjectUtil.checkNotNull(bytes, "bytes");
        return null;
    }

    @Override // javax.net.ssl.SSLSessionContext
    public Enumeration<byte[]> getIds() {
        return EMPTY;
    }

    @Deprecated
    public void setTicketKeys(byte[] keys) {
        if (keys.length % 48 != 0) {
            throw new IllegalArgumentException("keys.length % 48 != 0");
        }
        SessionTicketKey[] tickets = new SessionTicketKey[keys.length / 48];
        int i = 0;
        int a = 0;
        while (i < tickets.length) {
            byte[] name = Arrays.copyOfRange(keys, a, 16);
            int a2 = a + 16;
            byte[] hmacKey = Arrays.copyOfRange(keys, a2, 16);
            int i2 = i + 16;
            byte[] aesKey = Arrays.copyOfRange(keys, a2, 16);
            a = a2 + 16;
            tickets[i2] = new SessionTicketKey(name, hmacKey, aesKey);
            i = i2 + 1;
        }
        Lock writerLock = this.context.ctxLock.writeLock();
        writerLock.lock();
        try {
            SSLContext.clearOptions(this.context.ctx, SSL.SSL_OP_NO_TICKET);
            SSLContext.setSessionTicketKeys(this.context.ctx, tickets);
            writerLock.unlock();
        } catch (Throwable th) {
            writerLock.unlock();
            throw th;
        }
    }

    public void setTicketKeys(OpenSslSessionTicketKey... keys) {
        ObjectUtil.checkNotNull(keys, "keys");
        SessionTicketKey[] ticketKeys = new SessionTicketKey[keys.length];
        for (int i = 0; i < ticketKeys.length; i++) {
            ticketKeys[i] = keys[i].key;
        }
        Lock writerLock = this.context.ctxLock.writeLock();
        writerLock.lock();
        try {
            SSLContext.clearOptions(this.context.ctx, SSL.SSL_OP_NO_TICKET);
            SSLContext.setSessionTicketKeys(this.context.ctx, ticketKeys);
            writerLock.unlock();
        } catch (Throwable th) {
            writerLock.unlock();
            throw th;
        }
    }

    public OpenSslSessionStats stats() {
        return this.stats;
    }

    final void destroy() {
        if (this.provider != null) {
            this.provider.destroy();
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/OpenSslSessionContext$EmptyEnumeration.class */
    private static final class EmptyEnumeration implements Enumeration<byte[]> {
        private EmptyEnumeration() {
        }

        @Override // java.util.Enumeration
        public boolean hasMoreElements() {
            return false;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Enumeration
        public byte[] nextElement() {
            throw new NoSuchElementException();
        }
    }
}

package org.bouncycastle.crypto.io;

import java.io.IOException;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/io/CipherIOException.class */
public class CipherIOException extends IOException {
    private static final long serialVersionUID = 1;
    private final Throwable cause;

    public CipherIOException(String str, Throwable th) {
        super(str);
        this.cause = th;
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.cause;
    }
}

package org.bouncycastle.pkcs;

import java.io.IOException;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/pkcs/PKCSIOException.class */
public class PKCSIOException extends IOException {
    private Throwable cause;

    public PKCSIOException(String str, Throwable th) {
        super(str);
        this.cause = th;
    }

    public PKCSIOException(String str) {
        super(str);
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.cause;
    }
}

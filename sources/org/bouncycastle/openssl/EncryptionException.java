package org.bouncycastle.openssl;

import java.io.IOException;

/* JADX WARN: Classes with same name are omitted:
  bcpkix-jdk15on-1.64.jar:org/bouncycastle/openssl/EncryptionException.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/openssl/EncryptionException.class */
public class EncryptionException extends IOException {
    private Throwable cause;

    public EncryptionException(String str) {
        super(str);
    }

    public EncryptionException(String str, Throwable th) {
        super(str);
        this.cause = th;
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.cause;
    }
}

package org.bouncycastle.openssl;

import java.io.IOException;

/* JADX WARN: Classes with same name are omitted:
  bcpkix-jdk15on-1.64.jar:org/bouncycastle/openssl/PEMException.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/openssl/PEMException.class */
public class PEMException extends IOException {
    Exception underlying;

    public PEMException(String str) {
        super(str);
    }

    public PEMException(String str, Exception exc) {
        super(str);
        this.underlying = exc;
    }

    public Exception getUnderlyingException() {
        return this.underlying;
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.underlying;
    }
}

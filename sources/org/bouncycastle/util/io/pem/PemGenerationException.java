package org.bouncycastle.util.io.pem;

import java.io.IOException;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/util/io/pem/PemGenerationException.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/util/io/pem/PemGenerationException.class */
public class PemGenerationException extends IOException {
    private Throwable cause;

    public PemGenerationException(String str, Throwable th) {
        super(str);
        this.cause = th;
    }

    public PemGenerationException(String str) {
        super(str);
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.cause;
    }
}

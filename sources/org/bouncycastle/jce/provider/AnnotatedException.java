package org.bouncycastle.jce.provider;

import org.bouncycastle.jce.exception.ExtException;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/jce/provider/AnnotatedException.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/provider/AnnotatedException.class */
public class AnnotatedException extends Exception implements ExtException {
    private Throwable _underlyingException;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AnnotatedException(String str, Throwable th) {
        super(str);
        this._underlyingException = th;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AnnotatedException(String str) {
        this(str, null);
    }

    Throwable getUnderlyingException() {
        return this._underlyingException;
    }

    @Override // java.lang.Throwable, org.bouncycastle.jce.exception.ExtException
    public Throwable getCause() {
        return this._underlyingException;
    }
}

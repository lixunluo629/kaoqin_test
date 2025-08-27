package org.bouncycastle.pkix.jcajce;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/pkix/jcajce/AnnotatedException.class */
class AnnotatedException extends Exception {
    private Throwable _underlyingException;

    public AnnotatedException(String str, Throwable th) {
        super(str);
        this._underlyingException = th;
    }

    public AnnotatedException(String str) {
        this(str, null);
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this._underlyingException;
    }
}

package org.bouncycastle.util.encoders;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/util/encoders/DecoderException.class */
public class DecoderException extends IllegalStateException {
    private Throwable cause;

    DecoderException(String str, Throwable th) {
        super(str);
        this.cause = th;
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.cause;
    }
}

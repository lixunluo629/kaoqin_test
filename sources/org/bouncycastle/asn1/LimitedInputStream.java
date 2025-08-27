package org.bouncycastle.asn1;

import java.io.InputStream;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/LimitedInputStream.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/LimitedInputStream.class */
abstract class LimitedInputStream extends InputStream {
    protected final InputStream _in;
    private int _limit;

    LimitedInputStream(InputStream inputStream, int i) {
        this._in = inputStream;
        this._limit = i;
    }

    int getRemaining() {
        return this._limit;
    }

    protected void setParentEofDetect(boolean z) {
        if (this._in instanceof IndefiniteLengthInputStream) {
            ((IndefiniteLengthInputStream) this._in).setEofOn00(z);
        }
    }
}

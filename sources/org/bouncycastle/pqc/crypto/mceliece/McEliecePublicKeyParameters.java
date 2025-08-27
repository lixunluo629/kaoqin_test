package org.bouncycastle.pqc.crypto.mceliece;

import org.bouncycastle.pqc.math.linearalgebra.GF2Matrix;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/pqc/crypto/mceliece/McEliecePublicKeyParameters.class */
public class McEliecePublicKeyParameters extends McElieceKeyParameters {
    private int n;
    private int t;
    private GF2Matrix g;

    public McEliecePublicKeyParameters(int i, int i2, GF2Matrix gF2Matrix) {
        super(false, null);
        this.n = i;
        this.t = i2;
        this.g = new GF2Matrix(gF2Matrix);
    }

    public int getN() {
        return this.n;
    }

    public int getT() {
        return this.t;
    }

    public GF2Matrix getG() {
        return this.g;
    }

    public int getK() {
        return this.g.getNumRows();
    }
}

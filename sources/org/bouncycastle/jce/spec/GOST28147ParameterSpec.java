package org.bouncycastle.jce.spec;

import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle.crypto.engines.GOST28147Engine;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/jce/spec/GOST28147ParameterSpec.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/spec/GOST28147ParameterSpec.class */
public class GOST28147ParameterSpec implements AlgorithmParameterSpec {
    private byte[] iv;
    private byte[] sBox;

    public GOST28147ParameterSpec(byte[] bArr) {
        this.iv = null;
        this.sBox = null;
        this.sBox = new byte[bArr.length];
        System.arraycopy(bArr, 0, this.sBox, 0, bArr.length);
    }

    public GOST28147ParameterSpec(byte[] bArr, byte[] bArr2) {
        this(bArr);
        this.iv = new byte[bArr2.length];
        System.arraycopy(bArr2, 0, this.iv, 0, bArr2.length);
    }

    public GOST28147ParameterSpec(String str) {
        this.iv = null;
        this.sBox = null;
        this.sBox = GOST28147Engine.getSBox(str);
    }

    public GOST28147ParameterSpec(String str, byte[] bArr) {
        this(str);
        this.iv = new byte[bArr.length];
        System.arraycopy(bArr, 0, this.iv, 0, bArr.length);
    }

    public byte[] getSbox() {
        return this.sBox;
    }

    public byte[] getIV() {
        if (this.iv == null) {
            return null;
        }
        byte[] bArr = new byte[this.iv.length];
        System.arraycopy(this.iv, 0, bArr, 0, bArr.length);
        return bArr;
    }
}

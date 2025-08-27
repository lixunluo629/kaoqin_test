package org.bouncycastle.crypto.params;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/params/DESedeParameters.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/params/DESedeParameters.class */
public class DESedeParameters extends DESParameters {
    public static final int DES_EDE_KEY_LENGTH = 24;

    public DESedeParameters(byte[] bArr) {
        super(bArr);
        if (isWeakKey(bArr, 0, bArr.length)) {
            throw new IllegalArgumentException("attempt to create weak DESede key");
        }
    }

    public static boolean isWeakKey(byte[] bArr, int i, int i2) {
        for (int i3 = i; i3 < i2; i3 += 8) {
            if (DESParameters.isWeakKey(bArr, i3)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isWeakKey(byte[] bArr, int i) {
        return isWeakKey(bArr, i, bArr.length - i);
    }
}

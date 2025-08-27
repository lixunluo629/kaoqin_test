package org.bouncycastle.util.encoders;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/util/encoders/Translator.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/util/encoders/Translator.class */
public interface Translator {
    int getEncodedBlockSize();

    int encode(byte[] bArr, int i, int i2, byte[] bArr2, int i3);

    int getDecodedBlockSize();

    int decode(byte[] bArr, int i, int i2, byte[] bArr2, int i3);
}

package org.bouncycastle.crypto;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/CharToByteConverter.class */
public interface CharToByteConverter {
    String getType();

    byte[] convert(char[] cArr);
}

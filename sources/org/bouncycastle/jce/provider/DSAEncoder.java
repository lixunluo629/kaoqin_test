package org.bouncycastle.jce.provider;

import java.io.IOException;
import java.math.BigInteger;

/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/provider/DSAEncoder.class */
public interface DSAEncoder {
    byte[] encode(BigInteger bigInteger, BigInteger bigInteger2) throws IOException;

    BigInteger[] decode(byte[] bArr) throws IOException;
}

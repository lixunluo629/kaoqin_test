package org.bouncycastle.cert.crmf;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/cert/crmf/EncryptedValuePadder.class */
public interface EncryptedValuePadder {
    byte[] getPaddedData(byte[] bArr);

    byte[] getUnpaddedData(byte[] bArr);
}

package org.bouncycastle.crypto.agreement.kdf;

import org.apache.commons.compress.archivers.tar.TarConstants;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.DerivationFunction;
import org.bouncycastle.crypto.DerivationParameters;
import org.bouncycastle.crypto.Digest;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/agreement/kdf/DHKEKGenerator.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/agreement/kdf/DHKEKGenerator.class */
public class DHKEKGenerator implements DerivationFunction {
    private final Digest digest;
    private DERObjectIdentifier algorithm;
    private int keySize;
    private byte[] z;
    private byte[] partyAInfo;

    public DHKEKGenerator(Digest digest) {
        this.digest = digest;
    }

    @Override // org.bouncycastle.crypto.DerivationFunction
    public void init(DerivationParameters derivationParameters) {
        DHKDFParameters dHKDFParameters = (DHKDFParameters) derivationParameters;
        this.algorithm = dHKDFParameters.getAlgorithm();
        this.keySize = dHKDFParameters.getKeySize();
        this.z = dHKDFParameters.getZ();
        this.partyAInfo = dHKDFParameters.getExtraInfo();
    }

    @Override // org.bouncycastle.crypto.DerivationFunction
    public Digest getDigest() {
        return this.digest;
    }

    @Override // org.bouncycastle.crypto.DerivationFunction
    public int generateBytes(byte[] bArr, int i, int i2) throws DataLengthException, IllegalArgumentException {
        if (bArr.length - i2 < i) {
            throw new DataLengthException("output buffer too small");
        }
        long j = i2;
        int digestSize = this.digest.getDigestSize();
        if (j > TarConstants.MAXSIZE) {
            throw new IllegalArgumentException("Output length too large");
        }
        int i3 = (int) (((j + digestSize) - 1) / digestSize);
        byte[] bArr2 = new byte[this.digest.getDigestSize()];
        int i4 = 1;
        for (int i5 = 0; i5 < i3; i5++) {
            this.digest.update(this.z, 0, this.z.length);
            ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
            ASN1EncodableVector aSN1EncodableVector2 = new ASN1EncodableVector();
            aSN1EncodableVector2.add(this.algorithm);
            aSN1EncodableVector2.add(new DEROctetString(integerToBytes(i4)));
            aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector2));
            if (this.partyAInfo != null) {
                aSN1EncodableVector.add(new DERTaggedObject(true, 0, new DEROctetString(this.partyAInfo)));
            }
            aSN1EncodableVector.add(new DERTaggedObject(true, 2, new DEROctetString(integerToBytes(this.keySize))));
            byte[] dEREncoded = new DERSequence(aSN1EncodableVector).getDEREncoded();
            this.digest.update(dEREncoded, 0, dEREncoded.length);
            this.digest.doFinal(bArr2, 0);
            if (i2 > digestSize) {
                System.arraycopy(bArr2, 0, bArr, i, digestSize);
                i += digestSize;
                i2 -= digestSize;
            } else {
                System.arraycopy(bArr2, 0, bArr, i, i2);
            }
            i4++;
        }
        this.digest.reset();
        return i2;
    }

    private byte[] integerToBytes(int i) {
        return new byte[]{(byte) (i >> 24), (byte) (i >> 16), (byte) (i >> 8), (byte) i};
    }
}

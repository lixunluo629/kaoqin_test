package org.bouncycastle.mozilla;

import java.io.ByteArrayInputStream;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.mozilla.PublicKeyAndChallenge;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;

/* JADX WARN: Classes with same name are omitted:
  bcpkix-jdk15on-1.64.jar:org/bouncycastle/mozilla/SignedPublicKeyAndChallenge.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/mozilla/SignedPublicKeyAndChallenge.class */
public class SignedPublicKeyAndChallenge extends ASN1Encodable {
    private ASN1Sequence spkacSeq;
    private PublicKeyAndChallenge pkac;
    private AlgorithmIdentifier signatureAlgorithm;
    private DERBitString signature;

    private static ASN1Sequence toDERSequence(byte[] bArr) {
        try {
            return (ASN1Sequence) new ASN1InputStream(new ByteArrayInputStream(bArr)).readObject();
        } catch (Exception e) {
            throw new IllegalArgumentException("badly encoded request");
        }
    }

    public SignedPublicKeyAndChallenge(byte[] bArr) {
        this.spkacSeq = toDERSequence(bArr);
        this.pkac = PublicKeyAndChallenge.getInstance(this.spkacSeq.getObjectAt(0));
        this.signatureAlgorithm = AlgorithmIdentifier.getInstance(this.spkacSeq.getObjectAt(1));
        this.signature = (DERBitString) this.spkacSeq.getObjectAt(2);
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        return this.spkacSeq;
    }

    public PublicKeyAndChallenge getPublicKeyAndChallenge() {
        return this.pkac;
    }

    public boolean verify() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, NoSuchProviderException {
        return verify(null);
    }

    public boolean verify(String str) throws SignatureException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException {
        Signature signature = str == null ? Signature.getInstance(this.signatureAlgorithm.getObjectId().getId()) : Signature.getInstance(this.signatureAlgorithm.getObjectId().getId(), str);
        signature.initVerify(getPublicKey(str));
        signature.update(new DERBitString(this.pkac).getBytes());
        return signature.verify(this.signature.getBytes());
    }

    public PublicKey getPublicKey(String str) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException {
        SubjectPublicKeyInfo subjectPublicKeyInfo = this.pkac.getSubjectPublicKeyInfo();
        try {
            return KeyFactory.getInstance(subjectPublicKeyInfo.getAlgorithmId().getObjectId().getId(), str).generatePublic(new X509EncodedKeySpec(new DERBitString(subjectPublicKeyInfo).getBytes()));
        } catch (InvalidKeySpecException e) {
            throw new InvalidKeyException("error encoding public key");
        }
    }
}

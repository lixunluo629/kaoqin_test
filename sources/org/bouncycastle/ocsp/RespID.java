package org.bouncycastle.ocsp;

import java.security.MessageDigest;
import java.security.PublicKey;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.ocsp.ResponderID;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;

/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/ocsp/RespID.class */
public class RespID {
    ResponderID id;

    public RespID(ResponderID responderID) {
        this.id = responderID;
    }

    public RespID(X500Principal x500Principal) {
        this.id = new ResponderID(X500Name.getInstance(x500Principal.getEncoded()));
    }

    public RespID(PublicKey publicKey) throws OCSPException {
        try {
            MessageDigest messageDigestCreateDigestInstance = OCSPUtil.createDigestInstance("SHA1", null);
            messageDigestCreateDigestInstance.update(SubjectPublicKeyInfo.getInstance(new ASN1InputStream(publicKey.getEncoded()).readObject()).getPublicKeyData().getBytes());
            this.id = new ResponderID(new DEROctetString(messageDigestCreateDigestInstance.digest()));
        } catch (Exception e) {
            throw new OCSPException("problem creating ID: " + e, e);
        }
    }

    public ResponderID toASN1Object() {
        return this.id;
    }

    public boolean equals(Object obj) {
        if (obj instanceof RespID) {
            return this.id.equals(((RespID) obj).id);
        }
        return false;
    }

    public int hashCode() {
        return this.id.hashCode();
    }
}

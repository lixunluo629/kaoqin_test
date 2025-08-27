package org.bouncycastle.asn1;

import org.bouncycastle.util.Arrays;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/ASN1ObjectIdentifier.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/ASN1ObjectIdentifier.class */
public class ASN1ObjectIdentifier extends DERObjectIdentifier {

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/ASN1ObjectIdentifier$OidHandle.class */
    private static class OidHandle {
        private final int key;
        private final byte[] enc;

        OidHandle(byte[] bArr) {
            this.key = Arrays.hashCode(bArr);
            this.enc = bArr;
        }

        public int hashCode() {
            return this.key;
        }

        public boolean equals(Object obj) {
            if (obj instanceof OidHandle) {
                return Arrays.areEqual(this.enc, ((OidHandle) obj).enc);
            }
            return false;
        }
    }

    public ASN1ObjectIdentifier(String str) {
        super(str);
    }

    ASN1ObjectIdentifier(byte[] bArr) {
        super(bArr);
    }

    public ASN1ObjectIdentifier branch(String str) {
        return new ASN1ObjectIdentifier(getId() + "." + str);
    }
}

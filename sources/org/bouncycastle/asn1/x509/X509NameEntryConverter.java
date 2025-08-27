package org.bouncycastle.asn1.x509;

import java.io.IOException;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DERPrintableString;
import org.bouncycastle.util.Strings;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/x509/X509NameEntryConverter.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/x509/X509NameEntryConverter.class */
public abstract class X509NameEntryConverter {
    protected DERObject convertHexEncoded(String str, int i) throws IOException {
        String lowerCase = Strings.toLowerCase(str);
        byte[] bArr = new byte[(lowerCase.length() - i) / 2];
        for (int i2 = 0; i2 != bArr.length; i2++) {
            char cCharAt = lowerCase.charAt((i2 * 2) + i);
            char cCharAt2 = lowerCase.charAt((i2 * 2) + i + 1);
            if (cCharAt < 'a') {
                bArr[i2] = (byte) ((cCharAt - '0') << 4);
            } else {
                bArr[i2] = (byte) (((cCharAt - 'a') + 10) << 4);
            }
            if (cCharAt2 < 'a') {
                int i3 = i2;
                bArr[i3] = (byte) (bArr[i3] | ((byte) (cCharAt2 - '0')));
            } else {
                int i4 = i2;
                bArr[i4] = (byte) (bArr[i4] | ((byte) ((cCharAt2 - 'a') + 10)));
            }
        }
        return new ASN1InputStream(bArr).readObject();
    }

    protected boolean canBePrintable(String str) {
        return DERPrintableString.isPrintableString(str);
    }

    public abstract DERObject getConvertedValue(DERObjectIdentifier dERObjectIdentifier, String str);
}

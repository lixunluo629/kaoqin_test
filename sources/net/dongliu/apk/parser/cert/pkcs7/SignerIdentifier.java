package net.dongliu.apk.parser.cert.pkcs7;

import java.nio.ByteBuffer;
import net.dongliu.apk.parser.cert.asn1.Asn1Class;
import net.dongliu.apk.parser.cert.asn1.Asn1Field;
import net.dongliu.apk.parser.cert.asn1.Asn1Tagging;
import net.dongliu.apk.parser.cert.asn1.Asn1Type;

@Asn1Class(type = Asn1Type.CHOICE)
/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/cert/pkcs7/SignerIdentifier.class */
public class SignerIdentifier {

    @Asn1Field(type = Asn1Type.SEQUENCE)
    public IssuerAndSerialNumber issuerAndSerialNumber;

    @Asn1Field(type = Asn1Type.OCTET_STRING, tagging = Asn1Tagging.IMPLICIT, tagNumber = 0)
    public ByteBuffer subjectKeyIdentifier;

    public SignerIdentifier() {
    }

    public SignerIdentifier(IssuerAndSerialNumber issuerAndSerialNumber) {
        this.issuerAndSerialNumber = issuerAndSerialNumber;
    }
}

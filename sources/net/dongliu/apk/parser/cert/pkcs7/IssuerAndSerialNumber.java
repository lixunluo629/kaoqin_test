package net.dongliu.apk.parser.cert.pkcs7;

import java.math.BigInteger;
import net.dongliu.apk.parser.cert.asn1.Asn1Class;
import net.dongliu.apk.parser.cert.asn1.Asn1Field;
import net.dongliu.apk.parser.cert.asn1.Asn1OpaqueObject;
import net.dongliu.apk.parser.cert.asn1.Asn1Type;

@Asn1Class(type = Asn1Type.SEQUENCE)
/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/cert/pkcs7/IssuerAndSerialNumber.class */
public class IssuerAndSerialNumber {

    @Asn1Field(index = 0, type = Asn1Type.ANY)
    public Asn1OpaqueObject issuer;

    @Asn1Field(index = 1, type = Asn1Type.INTEGER)
    public BigInteger certificateSerialNumber;

    public IssuerAndSerialNumber() {
    }

    public IssuerAndSerialNumber(Asn1OpaqueObject issuer, BigInteger certificateSerialNumber) {
        this.issuer = issuer;
        this.certificateSerialNumber = certificateSerialNumber;
    }
}

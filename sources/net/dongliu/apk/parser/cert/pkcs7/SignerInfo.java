package net.dongliu.apk.parser.cert.pkcs7;

import java.nio.ByteBuffer;
import java.util.List;
import net.dongliu.apk.parser.cert.asn1.Asn1Class;
import net.dongliu.apk.parser.cert.asn1.Asn1Field;
import net.dongliu.apk.parser.cert.asn1.Asn1OpaqueObject;
import net.dongliu.apk.parser.cert.asn1.Asn1Tagging;
import net.dongliu.apk.parser.cert.asn1.Asn1Type;

@Asn1Class(type = Asn1Type.SEQUENCE)
/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/cert/pkcs7/SignerInfo.class */
public class SignerInfo {

    @Asn1Field(index = 0, type = Asn1Type.INTEGER)
    public int version;

    @Asn1Field(index = 1, type = Asn1Type.CHOICE)
    public SignerIdentifier sid;

    @Asn1Field(index = 2, type = Asn1Type.SEQUENCE)
    public AlgorithmIdentifier digestAlgorithm;

    @Asn1Field(index = 3, type = Asn1Type.SET_OF, tagging = Asn1Tagging.IMPLICIT, tagNumber = 0, optional = true)
    public Asn1OpaqueObject signedAttrs;

    @Asn1Field(index = 4, type = Asn1Type.SEQUENCE)
    public AlgorithmIdentifier signatureAlgorithm;

    @Asn1Field(index = 5, type = Asn1Type.OCTET_STRING)
    public ByteBuffer signature;

    @Asn1Field(index = 6, type = Asn1Type.SET_OF, tagging = Asn1Tagging.IMPLICIT, tagNumber = 1, optional = true)
    public List<Attribute> unsignedAttrs;
}

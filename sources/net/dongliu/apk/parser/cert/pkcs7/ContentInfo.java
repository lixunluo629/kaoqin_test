package net.dongliu.apk.parser.cert.pkcs7;

import net.dongliu.apk.parser.cert.asn1.Asn1Class;
import net.dongliu.apk.parser.cert.asn1.Asn1Field;
import net.dongliu.apk.parser.cert.asn1.Asn1OpaqueObject;
import net.dongliu.apk.parser.cert.asn1.Asn1Tagging;
import net.dongliu.apk.parser.cert.asn1.Asn1Type;

@Asn1Class(type = Asn1Type.SEQUENCE)
/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/cert/pkcs7/ContentInfo.class */
public class ContentInfo {

    @Asn1Field(index = 1, type = Asn1Type.OBJECT_IDENTIFIER)
    public String contentType;

    @Asn1Field(index = 2, type = Asn1Type.ANY, tagging = Asn1Tagging.EXPLICIT, tagNumber = 0)
    public Asn1OpaqueObject content;
}

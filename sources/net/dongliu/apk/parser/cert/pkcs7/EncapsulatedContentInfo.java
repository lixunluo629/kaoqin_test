package net.dongliu.apk.parser.cert.pkcs7;

import java.nio.ByteBuffer;
import net.dongliu.apk.parser.cert.asn1.Asn1Class;
import net.dongliu.apk.parser.cert.asn1.Asn1Field;
import net.dongliu.apk.parser.cert.asn1.Asn1Tagging;
import net.dongliu.apk.parser.cert.asn1.Asn1Type;

@Asn1Class(type = Asn1Type.SEQUENCE)
/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/cert/pkcs7/EncapsulatedContentInfo.class */
public class EncapsulatedContentInfo {

    @Asn1Field(index = 0, type = Asn1Type.OBJECT_IDENTIFIER)
    public String contentType;

    @Asn1Field(index = 1, type = Asn1Type.OCTET_STRING, tagging = Asn1Tagging.EXPLICIT, tagNumber = 0, optional = true)
    public ByteBuffer content;

    public EncapsulatedContentInfo() {
    }

    public EncapsulatedContentInfo(String contentTypeOid) {
        this.contentType = contentTypeOid;
    }
}

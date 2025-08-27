package net.dongliu.apk.parser.cert.pkcs7;

import java.util.List;
import net.dongliu.apk.parser.cert.asn1.Asn1Class;
import net.dongliu.apk.parser.cert.asn1.Asn1Field;
import net.dongliu.apk.parser.cert.asn1.Asn1OpaqueObject;
import net.dongliu.apk.parser.cert.asn1.Asn1Type;

@Asn1Class(type = Asn1Type.SEQUENCE)
/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/cert/pkcs7/Attribute.class */
public class Attribute {

    @Asn1Field(index = 0, type = Asn1Type.OBJECT_IDENTIFIER)
    public String attrType;

    @Asn1Field(index = 1, type = Asn1Type.SET_OF)
    public List<Asn1OpaqueObject> attrValues;
}

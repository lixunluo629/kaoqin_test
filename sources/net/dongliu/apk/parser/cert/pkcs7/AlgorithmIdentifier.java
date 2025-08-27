package net.dongliu.apk.parser.cert.pkcs7;

import net.dongliu.apk.parser.cert.asn1.Asn1Class;
import net.dongliu.apk.parser.cert.asn1.Asn1Field;
import net.dongliu.apk.parser.cert.asn1.Asn1OpaqueObject;
import net.dongliu.apk.parser.cert.asn1.Asn1Type;

@Asn1Class(type = Asn1Type.SEQUENCE)
/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/cert/pkcs7/AlgorithmIdentifier.class */
public class AlgorithmIdentifier {

    @Asn1Field(index = 0, type = Asn1Type.OBJECT_IDENTIFIER)
    public String algorithm;

    @Asn1Field(index = 1, type = Asn1Type.ANY, optional = true)
    public Asn1OpaqueObject parameters;

    public AlgorithmIdentifier() {
    }

    public AlgorithmIdentifier(String algorithmOid, Asn1OpaqueObject parameters) {
        this.algorithm = algorithmOid;
        this.parameters = parameters;
    }
}

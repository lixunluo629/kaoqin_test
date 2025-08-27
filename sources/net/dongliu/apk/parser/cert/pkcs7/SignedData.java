package net.dongliu.apk.parser.cert.pkcs7;

import java.nio.ByteBuffer;
import java.util.List;
import net.dongliu.apk.parser.cert.asn1.Asn1Class;
import net.dongliu.apk.parser.cert.asn1.Asn1Field;
import net.dongliu.apk.parser.cert.asn1.Asn1OpaqueObject;
import net.dongliu.apk.parser.cert.asn1.Asn1Tagging;
import net.dongliu.apk.parser.cert.asn1.Asn1Type;

@Asn1Class(type = Asn1Type.SEQUENCE)
/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/cert/pkcs7/SignedData.class */
public class SignedData {

    @Asn1Field(index = 0, type = Asn1Type.INTEGER)
    public int version;

    @Asn1Field(index = 1, type = Asn1Type.SET_OF)
    public List<AlgorithmIdentifier> digestAlgorithms;

    @Asn1Field(index = 2, type = Asn1Type.SEQUENCE)
    public EncapsulatedContentInfo encapContentInfo;

    @Asn1Field(index = 3, type = Asn1Type.SET_OF, tagging = Asn1Tagging.IMPLICIT, tagNumber = 0, optional = true)
    public List<Asn1OpaqueObject> certificates;

    @Asn1Field(index = 4, type = Asn1Type.SET_OF, tagging = Asn1Tagging.IMPLICIT, tagNumber = 1, optional = true)
    public List<ByteBuffer> crls;

    @Asn1Field(index = 5, type = Asn1Type.SET_OF)
    public List<SignerInfo> signerInfos;
}

package net.dongliu.apk.parser.parser;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import net.dongliu.apk.parser.bean.CertificateMeta;
import net.dongliu.apk.parser.cert.asn1.Asn1BerParser;
import net.dongliu.apk.parser.cert.asn1.Asn1DecodingException;
import net.dongliu.apk.parser.cert.asn1.Asn1OpaqueObject;
import net.dongliu.apk.parser.cert.pkcs7.ContentInfo;
import net.dongliu.apk.parser.cert.pkcs7.Pkcs7Constants;
import net.dongliu.apk.parser.cert.pkcs7.SignedData;
import net.dongliu.apk.parser.utils.Buffers;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/parser/JSSECertificateParser.class */
class JSSECertificateParser extends CertificateParser {
    public JSSECertificateParser(byte[] data) {
        super(data);
    }

    @Override // net.dongliu.apk.parser.parser.CertificateParser
    public List<CertificateMeta> parse() throws CertificateException {
        try {
            ContentInfo contentInfo = (ContentInfo) Asn1BerParser.parse(ByteBuffer.wrap(this.data), ContentInfo.class);
            if (!Pkcs7Constants.OID_SIGNED_DATA.equals(contentInfo.contentType)) {
                throw new CertificateException("Unsupported ContentInfo.contentType: " + contentInfo.contentType);
            }
            try {
                SignedData signedData = (SignedData) Asn1BerParser.parse(contentInfo.content.getEncoded(), SignedData.class);
                List<Asn1OpaqueObject> encodedCertificates = signedData.certificates;
                CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
                List<X509Certificate> result = new ArrayList<>(encodedCertificates.size());
                for (int i = 0; i < encodedCertificates.size(); i++) {
                    Asn1OpaqueObject encodedCertificate = encodedCertificates.get(i);
                    byte[] encodedForm = Buffers.readBytes(encodedCertificate.getEncoded());
                    Certificate certificate = certFactory.generateCertificate(new ByteArrayInputStream(encodedForm));
                    result.add((X509Certificate) certificate);
                }
                return CertificateMetas.from(result);
            } catch (Asn1DecodingException e) {
                throw new CertificateException(e);
            }
        } catch (Asn1DecodingException e2) {
            throw new CertificateException(e2);
        }
    }
}

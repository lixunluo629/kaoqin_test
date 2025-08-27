package net.dongliu.apk.parser.parser;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import net.dongliu.apk.parser.struct.signingv2.ApkSigningBlock;
import net.dongliu.apk.parser.struct.signingv2.Digest;
import net.dongliu.apk.parser.struct.signingv2.Signature;
import net.dongliu.apk.parser.struct.signingv2.SignerBlock;
import net.dongliu.apk.parser.utils.Buffers;
import net.dongliu.apk.parser.utils.Unsigned;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/parser/ApkSignBlockParser.class */
public class ApkSignBlockParser {
    private ByteBuffer data;
    private static final int PSS_SHA_256 = 257;
    private static final int PSS_SHA_512 = 258;
    private static final int PKCS1_SHA_256 = 259;
    private static final int PKCS1_SHA_512 = 260;
    private static final int ECDSA_SHA_256 = 513;
    private static final int ECDSA_SHA_512 = 514;
    private static final int DSA_SHA_256 = 769;

    public ApkSignBlockParser(ByteBuffer data) {
        this.data = data.order(ByteOrder.LITTLE_ENDIAN);
    }

    public ApkSigningBlock parse() throws CertificateException {
        List<SignerBlock> signerBlocks = new ArrayList<>();
        while (this.data.hasRemaining()) {
            int id = this.data.getInt();
            int size = Unsigned.ensureUInt(this.data.getInt());
            if (id == 1896449818) {
                ByteBuffer signingV2Buffer = Buffers.sliceAndSkip(this.data, size);
                while (signingV2Buffer.hasRemaining()) {
                    SignerBlock signerBlock = readSigningV2(signingV2Buffer);
                    signerBlocks.add(signerBlock);
                }
            } else {
                Buffers.position(this.data, this.data.position() + size);
            }
        }
        return new ApkSigningBlock(signerBlocks);
    }

    private SignerBlock readSigningV2(ByteBuffer buffer) throws CertificateException {
        ByteBuffer buffer2 = readLenPrefixData(buffer);
        ByteBuffer signedData = readLenPrefixData(buffer2);
        ByteBuffer digestsData = readLenPrefixData(signedData);
        List<Digest> digests = readDigests(digestsData);
        ByteBuffer certificateData = readLenPrefixData(signedData);
        List<X509Certificate> certificates = readCertificates(certificateData);
        ByteBuffer attributesData = readLenPrefixData(signedData);
        readAttributes(attributesData);
        ByteBuffer signaturesData = readLenPrefixData(buffer2);
        List<Signature> signatures = readSignatures(signaturesData);
        readLenPrefixData(buffer2);
        return new SignerBlock(digests, certificates, signatures);
    }

    private List<Digest> readDigests(ByteBuffer buffer) {
        List<Digest> list = new ArrayList<>();
        while (buffer.hasRemaining()) {
            ByteBuffer digestData = readLenPrefixData(buffer);
            int algorithmID = digestData.getInt();
            byte[] digest = Buffers.readBytes(digestData);
            list.add(new Digest(algorithmID, digest));
        }
        return list;
    }

    private List<X509Certificate> readCertificates(ByteBuffer buffer) throws CertificateException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        List<X509Certificate> certificates = new ArrayList<>();
        while (buffer.hasRemaining()) {
            ByteBuffer certificateData = readLenPrefixData(buffer);
            Certificate certificate = certificateFactory.generateCertificate(new ByteArrayInputStream(Buffers.readBytes(certificateData)));
            certificates.add((X509Certificate) certificate);
        }
        return certificates;
    }

    private void readAttributes(ByteBuffer buffer) {
        while (buffer.hasRemaining()) {
            ByteBuffer attributeData = readLenPrefixData(buffer);
            attributeData.getInt();
        }
    }

    private List<Signature> readSignatures(ByteBuffer buffer) {
        List<Signature> signatures = new ArrayList<>();
        while (buffer.hasRemaining()) {
            ByteBuffer signatureData = readLenPrefixData(buffer);
            int algorithmID = signatureData.getInt();
            int signatureDataLen = Unsigned.ensureUInt(signatureData.getInt());
            byte[] signature = Buffers.readBytes(signatureData, signatureDataLen);
            signatures.add(new Signature(algorithmID, signature));
        }
        return signatures;
    }

    private ByteBuffer readLenPrefixData(ByteBuffer buffer) {
        int len = Unsigned.ensureUInt(buffer.getInt());
        return Buffers.sliceAndSkip(buffer, len);
    }
}

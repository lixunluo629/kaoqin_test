package com.itextpdf.kernel.crypto.securityhandler;

import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfEncryptor;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.security.IExternalDecryptionProcess;
import com.moredian.onpremise.core.utils.RSAUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.CMSEnvelopedData;
import org.bouncycastle.cms.RecipientInformation;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/crypto/securityhandler/EncryptionUtils.class */
final class EncryptionUtils {
    EncryptionUtils() {
    }

    static byte[] generateSeed(int seedLength) throws NoSuchAlgorithmException {
        byte[] seedBytes;
        try {
            KeyGenerator key = KeyGenerator.getInstance(RSAUtils.AES_KEY_ALGORITHM);
            key.init(192, new SecureRandom());
            SecretKey sk = key.generateKey();
            seedBytes = new byte[seedLength];
            System.arraycopy(sk.getEncoded(), 0, seedBytes, 0, seedLength);
        } catch (NoSuchAlgorithmException e) {
            seedBytes = SecureRandom.getSeed(seedLength);
        }
        return seedBytes;
    }

    static byte[] fetchEnvelopedData(Key certificateKey, Certificate certificate, String certificateKeyProvider, IExternalDecryptionProcess externalDecryptionProcess, PdfArray recipients) {
        boolean foundRecipient = false;
        byte[] envelopedData = null;
        try {
            X509CertificateHolder certHolder = new X509CertificateHolder(certificate.getEncoded());
            if (externalDecryptionProcess == null) {
                for (int i = 0; i < recipients.size(); i++) {
                    PdfString recipient = recipients.getAsString(i);
                    try {
                        CMSEnvelopedData data = new CMSEnvelopedData(recipient.getValueBytes());
                        for (RecipientInformation recipientInfo : data.getRecipientInfos().getRecipients()) {
                            if (recipientInfo.getRID().match(certHolder) && !foundRecipient) {
                                envelopedData = PdfEncryptor.getContent(recipientInfo, (PrivateKey) certificateKey, certificateKeyProvider);
                                foundRecipient = true;
                            }
                        }
                    } catch (Exception f) {
                        throw new PdfException(PdfException.PdfDecryption, (Throwable) f);
                    }
                }
            } else {
                for (int i2 = 0; i2 < recipients.size(); i2++) {
                    PdfString recipient2 = recipients.getAsString(i2);
                    try {
                        CMSEnvelopedData data2 = new CMSEnvelopedData(recipient2.getValueBytes());
                        RecipientInformation recipientInfo2 = data2.getRecipientInfos().get(externalDecryptionProcess.getCmsRecipientId());
                        if (recipientInfo2 != null) {
                            envelopedData = recipientInfo2.getContent(externalDecryptionProcess.getCmsRecipient());
                            foundRecipient = true;
                        }
                    } catch (Exception f2) {
                        throw new PdfException(PdfException.PdfDecryption, (Throwable) f2);
                    }
                }
            }
            if (!foundRecipient || envelopedData == null) {
                throw new PdfException(PdfException.BadCertificateAndKey);
            }
            return envelopedData;
        } catch (Exception f3) {
            throw new PdfException(PdfException.PdfDecryption, (Throwable) f3);
        }
    }

    static byte[] cipherBytes(X509Certificate x509certificate, byte[] abyte0, AlgorithmIdentifier algorithmidentifier) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(algorithmidentifier.getAlgorithm().getId());
        try {
            cipher.init(1, x509certificate);
        } catch (InvalidKeyException e) {
            cipher.init(1, x509certificate.getPublicKey());
        }
        return cipher.doFinal(abyte0);
    }

    static DERForRecipientParams calculateDERForRecipientParams(byte[] in) throws GeneralSecurityException, IOException {
        DERForRecipientParams parameters = new DERForRecipientParams();
        AlgorithmParameterGenerator algorithmparametergenerator = AlgorithmParameterGenerator.getInstance("1.2.840.113549.3.2");
        AlgorithmParameters algorithmparameters = algorithmparametergenerator.generateParameters();
        ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(algorithmparameters.getEncoded("ASN.1"));
        ASN1InputStream asn1inputstream = new ASN1InputStream(bytearrayinputstream);
        ASN1Primitive derobject = asn1inputstream.readObject();
        KeyGenerator keygenerator = KeyGenerator.getInstance("1.2.840.113549.3.2");
        keygenerator.init(128);
        SecretKey secretkey = keygenerator.generateKey();
        Cipher cipher = Cipher.getInstance("1.2.840.113549.3.2");
        cipher.init(1, secretkey, algorithmparameters);
        parameters.abyte0 = secretkey.getEncoded();
        parameters.abyte1 = cipher.doFinal(in);
        parameters.algorithmIdentifier = new AlgorithmIdentifier(new ASN1ObjectIdentifier("1.2.840.113549.3.2"), (ASN1Encodable) derobject);
        return parameters;
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/crypto/securityhandler/EncryptionUtils$DERForRecipientParams.class */
    static class DERForRecipientParams {
        byte[] abyte0;
        byte[] abyte1;
        AlgorithmIdentifier algorithmIdentifier;

        DERForRecipientParams() {
        }
    }
}

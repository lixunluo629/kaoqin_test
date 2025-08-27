package com.itextpdf.kernel.crypto.securityhandler;

import com.itextpdf.io.util.StreamUtil;
import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.crypto.securityhandler.EncryptionUtils;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfLiteral;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.security.IExternalDecryptionProcess;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DEROutputStream;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.asn1.cms.EncryptedContentInfo;
import org.bouncycastle.asn1.cms.EnvelopedData;
import org.bouncycastle.asn1.cms.IssuerAndSerialNumber;
import org.bouncycastle.asn1.cms.KeyTransRecipientInfo;
import org.bouncycastle.asn1.cms.RecipientIdentifier;
import org.bouncycastle.asn1.cms.RecipientInfo;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.TBSCertificateStructure;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/crypto/securityhandler/PubKeySecurityHandler.class */
public abstract class PubKeySecurityHandler extends SecurityHandler {
    private static final int SEED_LENGTH = 20;
    private static final long serialVersionUID = -6093031394871440268L;
    private List<PublicKeyRecipient> recipients;
    private byte[] seed = EncryptionUtils.generateSeed(20);
    static final /* synthetic */ boolean $assertionsDisabled;

    protected abstract void setPubSecSpecificHandlerDicEntries(PdfDictionary pdfDictionary, boolean z, boolean z2);

    protected abstract String getDigestAlgorithm();

    protected abstract void initKey(byte[] bArr, int i);

    static {
        $assertionsDisabled = !PubKeySecurityHandler.class.desiredAssertionStatus();
    }

    protected PubKeySecurityHandler() {
        this.recipients = null;
        this.recipients = new ArrayList();
    }

    protected byte[] computeGlobalKey(String messageDigestAlgorithm, boolean encryptMetadata) throws NoSuchAlgorithmException {
        try {
            MessageDigest md = MessageDigest.getInstance(messageDigestAlgorithm);
            md.update(getSeed());
            for (int i = 0; i < getRecipientsSize(); i++) {
                byte[] encodedRecipient = getEncodedRecipient(i);
                md.update(encodedRecipient);
            }
            if (!encryptMetadata) {
                md.update(new byte[]{-1, -1, -1, -1});
            }
            return md.digest();
        } catch (Exception e) {
            throw new PdfException(PdfException.PdfEncryption, (Throwable) e);
        }
    }

    protected static byte[] computeGlobalKeyOnReading(PdfDictionary encryptionDictionary, PrivateKey certificateKey, Certificate certificate, String certificateKeyProvider, IExternalDecryptionProcess externalDecryptionProcess, boolean encryptMetadata, String digestAlgorithm) throws NoSuchAlgorithmException {
        PdfArray recipients = encryptionDictionary.getAsArray(PdfName.Recipients);
        if (recipients == null) {
            recipients = encryptionDictionary.getAsDictionary(PdfName.CF).getAsDictionary(PdfName.DefaultCryptFilter).getAsArray(PdfName.Recipients);
        }
        byte[] envelopedData = EncryptionUtils.fetchEnvelopedData(certificateKey, certificate, certificateKeyProvider, externalDecryptionProcess, recipients);
        try {
            MessageDigest md = MessageDigest.getInstance(digestAlgorithm);
            md.update(envelopedData, 0, 20);
            for (int i = 0; i < recipients.size(); i++) {
                byte[] encodedRecipient = recipients.getAsString(i).getValueBytes();
                md.update(encodedRecipient);
            }
            if (!encryptMetadata) {
                md.update(new byte[]{-1, -1, -1, -1});
            }
            byte[] encryptionKey = md.digest();
            return encryptionKey;
        } catch (Exception f) {
            throw new PdfException(PdfException.PdfDecryption, (Throwable) f);
        }
    }

    protected void addAllRecipients(Certificate[] certs, int[] permissions) {
        if (certs != null) {
            for (int i = 0; i < certs.length; i++) {
                addRecipient(certs[i], permissions[i]);
            }
        }
    }

    protected PdfArray createRecipientsArray() {
        try {
            PdfArray recipients = getEncodedRecipients();
            return recipients;
        } catch (Exception e) {
            throw new PdfException(PdfException.PdfEncryption, (Throwable) e);
        }
    }

    protected void initKeyAndFillDictionary(PdfDictionary encryptionDictionary, Certificate[] certs, int[] permissions, boolean encryptMetadata, boolean embeddedFilesOnly) throws NoSuchAlgorithmException {
        addAllRecipients(certs, permissions);
        Integer keyLen = encryptionDictionary.getAsInt(PdfName.Length);
        int keyLength = keyLen != null ? keyLen.intValue() : 40;
        String digestAlgorithm = getDigestAlgorithm();
        byte[] digest = computeGlobalKey(digestAlgorithm, encryptMetadata);
        initKey(digest, keyLength);
        setPubSecSpecificHandlerDicEntries(encryptionDictionary, encryptMetadata, embeddedFilesOnly);
    }

    protected void initKeyAndReadDictionary(PdfDictionary encryptionDictionary, Key certificateKey, Certificate certificate, String certificateKeyProvider, IExternalDecryptionProcess externalDecryptionProcess, boolean encryptMetadata) throws NoSuchAlgorithmException {
        String digestAlgorithm = getDigestAlgorithm();
        byte[] encryptionKey = computeGlobalKeyOnReading(encryptionDictionary, (PrivateKey) certificateKey, certificate, certificateKeyProvider, externalDecryptionProcess, encryptMetadata, digestAlgorithm);
        Integer keyLen = encryptionDictionary.getAsInt(PdfName.Length);
        int keyLength = keyLen != null ? keyLen.intValue() : 40;
        initKey(encryptionKey, keyLength);
    }

    private void addRecipient(Certificate cert, int permission) {
        this.recipients.add(new PublicKeyRecipient(cert, permission));
    }

    private byte[] getSeed() {
        byte[] clonedSeed = new byte[this.seed.length];
        System.arraycopy(this.seed, 0, clonedSeed, 0, this.seed.length);
        return clonedSeed;
    }

    private int getRecipientsSize() {
        return this.recipients.size();
    }

    private byte[] getEncodedRecipient(int index) throws GeneralSecurityException, IOException {
        PublicKeyRecipient recipient = this.recipients.get(index);
        byte[] cms = recipient.getCms();
        if (cms != null) {
            return cms;
        }
        Certificate certificate = recipient.getCertificate();
        int permission = ((recipient.getPermission() | (-3904)) & (-4)) + 1;
        byte[] pkcs7input = new byte[24];
        byte one = (byte) permission;
        byte two = (byte) (permission >> 8);
        byte three = (byte) (permission >> 16);
        byte four = (byte) (permission >> 24);
        System.arraycopy(this.seed, 0, pkcs7input, 0, 20);
        pkcs7input[20] = four;
        pkcs7input[21] = three;
        pkcs7input[22] = two;
        pkcs7input[23] = one;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DEROutputStream k = new DEROutputStream(baos);
        ASN1Primitive obj = createDERForRecipient(pkcs7input, (X509Certificate) certificate);
        k.writeObject(obj);
        byte[] cms2 = baos.toByteArray();
        recipient.setCms(cms2);
        return cms2;
    }

    private PdfArray getEncodedRecipients() {
        PdfArray EncodedRecipients = new PdfArray();
        for (int i = 0; i < this.recipients.size(); i++) {
            try {
                byte[] cms = getEncodedRecipient(i);
                EncodedRecipients.add(new PdfLiteral(StreamUtil.createEscapedString(cms)));
            } catch (IOException e) {
                EncodedRecipients = null;
            } catch (GeneralSecurityException e2) {
                EncodedRecipients = null;
            }
        }
        return EncodedRecipients;
    }

    private ASN1Primitive createDERForRecipient(byte[] in, X509Certificate cert) throws GeneralSecurityException, IOException {
        EncryptionUtils.DERForRecipientParams parameters = EncryptionUtils.calculateDERForRecipientParams(in);
        KeyTransRecipientInfo keytransrecipientinfo = computeRecipientInfo(cert, parameters.abyte0);
        DEROctetString deroctetstring = new DEROctetString(parameters.abyte1);
        DERSet derset = new DERSet((ASN1Encodable) new RecipientInfo(keytransrecipientinfo));
        EncryptedContentInfo encryptedcontentinfo = new EncryptedContentInfo(PKCSObjectIdentifiers.data, parameters.algorithmIdentifier, (ASN1OctetString) deroctetstring);
        EnvelopedData env = new EnvelopedData(null, derset, encryptedcontentinfo, (ASN1Set) null);
        ContentInfo contentinfo = new ContentInfo(PKCSObjectIdentifiers.envelopedData, (ASN1Encodable) env);
        return contentinfo.toASN1Primitive();
    }

    private KeyTransRecipientInfo computeRecipientInfo(X509Certificate x509certificate, byte[] abyte0) throws GeneralSecurityException, IOException {
        ASN1InputStream asn1inputstream = new ASN1InputStream(new ByteArrayInputStream(x509certificate.getTBSCertificate()));
        TBSCertificateStructure tbscertificatestructure = TBSCertificateStructure.getInstance(asn1inputstream.readObject());
        if (!$assertionsDisabled && tbscertificatestructure == null) {
            throw new AssertionError();
        }
        AlgorithmIdentifier algorithmidentifier = tbscertificatestructure.getSubjectPublicKeyInfo().getAlgorithm();
        IssuerAndSerialNumber issuerandserialnumber = new IssuerAndSerialNumber(tbscertificatestructure.getIssuer(), tbscertificatestructure.getSerialNumber().getValue());
        byte[] cipheredBytes = EncryptionUtils.cipherBytes(x509certificate, abyte0, algorithmidentifier);
        DEROctetString deroctetstring = new DEROctetString(cipheredBytes);
        RecipientIdentifier recipId = new RecipientIdentifier(issuerandserialnumber);
        return new KeyTransRecipientInfo(recipId, algorithmidentifier, deroctetstring);
    }
}

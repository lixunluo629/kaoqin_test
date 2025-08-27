package com.itextpdf.kernel.pdf;

import com.itextpdf.io.source.ByteBuffer;
import com.itextpdf.io.util.SystemUtil;
import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.crypto.IDecryptor;
import com.itextpdf.kernel.crypto.OutputStreamEncryption;
import com.itextpdf.kernel.crypto.securityhandler.PubKeySecurityHandler;
import com.itextpdf.kernel.crypto.securityhandler.PubSecHandlerUsingAes128;
import com.itextpdf.kernel.crypto.securityhandler.PubSecHandlerUsingAes256;
import com.itextpdf.kernel.crypto.securityhandler.PubSecHandlerUsingStandard128;
import com.itextpdf.kernel.crypto.securityhandler.PubSecHandlerUsingStandard40;
import com.itextpdf.kernel.crypto.securityhandler.SecurityHandler;
import com.itextpdf.kernel.crypto.securityhandler.StandardHandlerUsingAes128;
import com.itextpdf.kernel.crypto.securityhandler.StandardHandlerUsingAes256;
import com.itextpdf.kernel.crypto.securityhandler.StandardHandlerUsingStandard128;
import com.itextpdf.kernel.crypto.securityhandler.StandardHandlerUsingStandard40;
import com.itextpdf.kernel.crypto.securityhandler.StandardSecurityHandler;
import com.itextpdf.kernel.security.IExternalDecryptionProcess;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/PdfEncryption.class */
public class PdfEncryption extends PdfObjectWrapper<PdfDictionary> {
    private static final long serialVersionUID = -6864863940808467156L;
    private static final int STANDARD_ENCRYPTION_40 = 2;
    private static final int STANDARD_ENCRYPTION_128 = 3;
    private static final int AES_128 = 4;
    private static final int AES_256 = 5;
    private static long seq = SystemUtil.getTimeBasedSeed();
    private int cryptoMode;
    private Long permissions;
    private boolean encryptMetadata;
    private boolean embeddedFilesOnly;
    private byte[] documentId;
    private SecurityHandler securityHandler;

    public PdfEncryption(byte[] userPassword, byte[] ownerPassword, int permissions, int encryptionType, byte[] documentId, PdfVersion version) {
        super(new PdfDictionary());
        this.documentId = documentId;
        if (version != null && version.compareTo(PdfVersion.PDF_2_0) >= 0) {
            permissions = fixAccessibilityPermissionPdf20(permissions);
        }
        int revision = setCryptoMode(encryptionType);
        switch (revision) {
            case 2:
                StandardHandlerUsingStandard40 handlerStd40 = new StandardHandlerUsingStandard40(getPdfObject(), userPassword, ownerPassword, permissions, this.encryptMetadata, this.embeddedFilesOnly, documentId);
                this.permissions = Long.valueOf(handlerStd40.getPermissions());
                this.securityHandler = handlerStd40;
                break;
            case 3:
                StandardHandlerUsingStandard128 handlerStd128 = new StandardHandlerUsingStandard128(getPdfObject(), userPassword, ownerPassword, permissions, this.encryptMetadata, this.embeddedFilesOnly, documentId);
                this.permissions = Long.valueOf(handlerStd128.getPermissions());
                this.securityHandler = handlerStd128;
                break;
            case 4:
                StandardHandlerUsingAes128 handlerAes128 = new StandardHandlerUsingAes128(getPdfObject(), userPassword, ownerPassword, permissions, this.encryptMetadata, this.embeddedFilesOnly, documentId);
                this.permissions = Long.valueOf(handlerAes128.getPermissions());
                this.securityHandler = handlerAes128;
                break;
            case 5:
                StandardHandlerUsingAes256 handlerAes256 = new StandardHandlerUsingAes256(getPdfObject(), userPassword, ownerPassword, permissions, this.encryptMetadata, this.embeddedFilesOnly, version);
                this.permissions = Long.valueOf(handlerAes256.getPermissions());
                this.securityHandler = handlerAes256;
                break;
        }
    }

    public PdfEncryption(Certificate[] certs, int[] permissions, int encryptionType, PdfVersion version) {
        super(new PdfDictionary());
        if (version != null && version.compareTo(PdfVersion.PDF_2_0) >= 0) {
            for (int i = 0; i < permissions.length; i++) {
                permissions[i] = fixAccessibilityPermissionPdf20(permissions[i]);
            }
        }
        int revision = setCryptoMode(encryptionType);
        switch (revision) {
            case 2:
                this.securityHandler = new PubSecHandlerUsingStandard40(getPdfObject(), certs, permissions, this.encryptMetadata, this.embeddedFilesOnly);
                break;
            case 3:
                this.securityHandler = new PubSecHandlerUsingStandard128(getPdfObject(), certs, permissions, this.encryptMetadata, this.embeddedFilesOnly);
                break;
            case 4:
                this.securityHandler = new PubSecHandlerUsingAes128(getPdfObject(), certs, permissions, this.encryptMetadata, this.embeddedFilesOnly);
                break;
            case 5:
                this.securityHandler = new PubSecHandlerUsingAes256(getPdfObject(), certs, permissions, this.encryptMetadata, this.embeddedFilesOnly);
                break;
        }
    }

    public PdfEncryption(PdfDictionary pdfDict, byte[] password, byte[] documentId) {
        super(pdfDict);
        setForbidRelease();
        this.documentId = documentId;
        int revision = readAndSetCryptoModeForStdHandler(pdfDict);
        switch (revision) {
            case 2:
                StandardHandlerUsingStandard40 handlerStd40 = new StandardHandlerUsingStandard40(getPdfObject(), password, documentId, this.encryptMetadata);
                this.permissions = Long.valueOf(handlerStd40.getPermissions());
                this.securityHandler = handlerStd40;
                break;
            case 3:
                StandardHandlerUsingStandard128 handlerStd128 = new StandardHandlerUsingStandard128(getPdfObject(), password, documentId, this.encryptMetadata);
                this.permissions = Long.valueOf(handlerStd128.getPermissions());
                this.securityHandler = handlerStd128;
                break;
            case 4:
                StandardHandlerUsingAes128 handlerAes128 = new StandardHandlerUsingAes128(getPdfObject(), password, documentId, this.encryptMetadata);
                this.permissions = Long.valueOf(handlerAes128.getPermissions());
                this.securityHandler = handlerAes128;
                break;
            case 5:
                StandardHandlerUsingAes256 aes256Handler = new StandardHandlerUsingAes256(getPdfObject(), password);
                this.permissions = Long.valueOf(aes256Handler.getPermissions());
                this.encryptMetadata = aes256Handler.isEncryptMetadata();
                this.securityHandler = aes256Handler;
                break;
        }
    }

    public PdfEncryption(PdfDictionary pdfDict, Key certificateKey, Certificate certificate, String certificateKeyProvider, IExternalDecryptionProcess externalDecryptionProcess) {
        super(pdfDict);
        setForbidRelease();
        int revision = readAndSetCryptoModeForPubSecHandler(pdfDict);
        switch (revision) {
            case 2:
                this.securityHandler = new PubSecHandlerUsingStandard40(getPdfObject(), certificateKey, certificate, certificateKeyProvider, externalDecryptionProcess, this.encryptMetadata);
                break;
            case 3:
                this.securityHandler = new PubSecHandlerUsingStandard128(getPdfObject(), certificateKey, certificate, certificateKeyProvider, externalDecryptionProcess, this.encryptMetadata);
                break;
            case 4:
                this.securityHandler = new PubSecHandlerUsingAes128(getPdfObject(), certificateKey, certificate, certificateKeyProvider, externalDecryptionProcess, this.encryptMetadata);
                break;
            case 5:
                this.securityHandler = new PubSecHandlerUsingAes256(getPdfObject(), certificateKey, certificate, certificateKeyProvider, externalDecryptionProcess, this.encryptMetadata);
                break;
        }
    }

    public static byte[] generateNewDocumentId() throws NoSuchAlgorithmException {
        try {
            MessageDigest md5 = MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
            long time = SystemUtil.getTimeBasedSeed();
            long mem = SystemUtil.getFreeMemory();
            StringBuilder sbAppend = new StringBuilder().append(time).append("+").append(mem).append("+");
            long j = seq;
            seq = j + 1;
            String s = sbAppend.append(j).toString();
            return md5.digest(s.getBytes(StandardCharsets.ISO_8859_1));
        } catch (Exception e) {
            throw new PdfException(PdfException.PdfEncryption, (Throwable) e);
        }
    }

    public static PdfObject createInfoId(byte[] id, boolean modified) {
        if (modified) {
            return createInfoId(id, generateNewDocumentId());
        }
        return createInfoId(id, id);
    }

    public static PdfObject createInfoId(byte[] firstId, byte[] secondId) {
        if (firstId.length < 16) {
            firstId = padByteArrayTo16(firstId);
        }
        if (secondId.length < 16) {
            secondId = padByteArrayTo16(secondId);
        }
        ByteBuffer buf = new ByteBuffer(90);
        buf.append(91).append(60);
        for (byte b : firstId) {
            buf.appendHex(b);
        }
        buf.append(62).append(60);
        for (byte b2 : secondId) {
            buf.appendHex(b2);
        }
        buf.append(62).append(93);
        return new PdfLiteral(buf.toByteArray());
    }

    private static byte[] padByteArrayTo16(byte[] documentId) {
        byte[] paddingBytes = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        System.arraycopy(documentId, 0, paddingBytes, 0, documentId.length);
        return paddingBytes;
    }

    public Long getPermissions() {
        return this.permissions;
    }

    public int getCryptoMode() {
        return this.cryptoMode;
    }

    public boolean isMetadataEncrypted() {
        return this.encryptMetadata;
    }

    public boolean isEmbeddedFilesOnly() {
        return this.embeddedFilesOnly;
    }

    public byte[] getDocumentId() {
        return this.documentId;
    }

    public void setHashKeyForNextObject(int objNumber, int objGeneration) {
        this.securityHandler.setHashKeyForNextObject(objNumber, objGeneration);
    }

    public OutputStreamEncryption getEncryptionStream(OutputStream os) {
        return this.securityHandler.getEncryptionStream(os);
    }

    public byte[] encryptByteArray(byte[] b) {
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        OutputStreamEncryption ose = getEncryptionStream(ba);
        try {
            ose.write(b);
            ose.finish();
            return ba.toByteArray();
        } catch (IOException e) {
            throw new PdfException(PdfException.PdfEncryption, (Throwable) e);
        }
    }

    public byte[] decryptByteArray(byte[] b) {
        try {
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            IDecryptor dec = this.securityHandler.getDecryptor();
            byte[] b2 = dec.update(b, 0, b.length);
            if (b2 != null) {
                ba.write(b2);
            }
            byte[] b22 = dec.finish();
            if (b22 != null) {
                ba.write(b22);
            }
            return ba.toByteArray();
        } catch (IOException e) {
            throw new PdfException(PdfException.PdfEncryption, (Throwable) e);
        }
    }

    public boolean isOpenedWithFullPermission() {
        if (!(this.securityHandler instanceof PubKeySecurityHandler) && (this.securityHandler instanceof StandardSecurityHandler)) {
            return ((StandardSecurityHandler) this.securityHandler).isUsedOwnerPassword();
        }
        return true;
    }

    public byte[] computeUserPassword(byte[] ownerPassword) {
        byte[] userPassword = null;
        if (this.securityHandler instanceof StandardHandlerUsingStandard40) {
            userPassword = ((StandardHandlerUsingStandard40) this.securityHandler).computeUserPassword(ownerPassword, getPdfObject());
        }
        return userPassword;
    }

    @Override // com.itextpdf.kernel.pdf.PdfObjectWrapper
    public void flush() {
        super.flush();
    }

    @Override // com.itextpdf.kernel.pdf.PdfObjectWrapper
    protected boolean isWrappedObjectMustBeIndirect() {
        return true;
    }

    private void setKeyLength(int keyLength) {
        if (keyLength != 40) {
            getPdfObject().put(PdfName.Length, new PdfNumber(keyLength));
        }
    }

    private int setCryptoMode(int mode) {
        return setCryptoMode(mode, 0);
    }

    private int setCryptoMode(int mode, int length) {
        int revision;
        this.cryptoMode = mode;
        this.encryptMetadata = (mode & 8) != 8;
        this.embeddedFilesOnly = (mode & 24) == 24;
        switch (mode & 7) {
            case 0:
                this.encryptMetadata = true;
                this.embeddedFilesOnly = false;
                setKeyLength(40);
                revision = 2;
                break;
            case 1:
                this.embeddedFilesOnly = false;
                if (length > 0) {
                    setKeyLength(length);
                } else {
                    setKeyLength(128);
                }
                revision = 3;
                break;
            case 2:
                setKeyLength(128);
                revision = 4;
                break;
            case 3:
                setKeyLength(256);
                revision = 5;
                break;
            default:
                throw new PdfException(PdfException.NoValidEncryptionMode);
        }
        return revision;
    }

    private int readAndSetCryptoModeForStdHandler(PdfDictionary encDict) {
        int cryptoMode;
        int length = 0;
        PdfNumber rValue = encDict.getAsNumber(PdfName.R);
        if (rValue == null) {
            throw new PdfException(PdfException.IllegalRValue);
        }
        int revision = rValue.intValue();
        switch (revision) {
            case 2:
                cryptoMode = 0;
                break;
            case 3:
                PdfNumber lengthValue = encDict.getAsNumber(PdfName.Length);
                if (lengthValue == null) {
                    throw new PdfException(PdfException.IllegalLengthValue);
                }
                length = lengthValue.intValue();
                if (length > 128 || length < 40 || length % 8 != 0) {
                    throw new PdfException(PdfException.IllegalLengthValue);
                }
                cryptoMode = 1;
                break;
            case 4:
                PdfDictionary dic = (PdfDictionary) encDict.get(PdfName.CF);
                if (dic == null) {
                    throw new PdfException(PdfException.CfNotFoundEncryption);
                }
                PdfDictionary dic2 = (PdfDictionary) dic.get(PdfName.StdCF);
                if (dic2 == null) {
                    throw new PdfException(PdfException.StdcfNotFoundEncryption);
                }
                if (PdfName.V2.equals(dic2.get(PdfName.CFM))) {
                    cryptoMode = 1;
                } else if (PdfName.AESV2.equals(dic2.get(PdfName.CFM))) {
                    cryptoMode = 2;
                } else {
                    throw new PdfException(PdfException.NoCompatibleEncryptionFound);
                }
                PdfBoolean em = encDict.getAsBoolean(PdfName.EncryptMetadata);
                if (em != null && !em.getValue()) {
                    cryptoMode |= 8;
                    break;
                }
                break;
            case 5:
            case 6:
                cryptoMode = 3;
                PdfBoolean em5 = encDict.getAsBoolean(PdfName.EncryptMetadata);
                if (em5 != null && !em5.getValue()) {
                    cryptoMode = 3 | 8;
                    break;
                }
                break;
            default:
                throw new PdfException(PdfException.UnknownEncryptionTypeREq1).setMessageParams(rValue);
        }
        int revision2 = setCryptoMode(cryptoMode, length);
        return revision2;
    }

    private int readAndSetCryptoModeForPubSecHandler(PdfDictionary encDict) {
        int cryptoMode;
        int length;
        PdfNumber vValue = encDict.getAsNumber(PdfName.V);
        if (vValue == null) {
            throw new PdfException(PdfException.IllegalVValue);
        }
        int v = vValue.intValue();
        switch (v) {
            case 1:
                cryptoMode = 0;
                length = 40;
                break;
            case 2:
                PdfNumber lengthValue = encDict.getAsNumber(PdfName.Length);
                if (lengthValue == null) {
                    throw new PdfException(PdfException.IllegalLengthValue);
                }
                length = lengthValue.intValue();
                if (length > 128 || length < 40 || length % 8 != 0) {
                    throw new PdfException(PdfException.IllegalLengthValue);
                }
                cryptoMode = 1;
                break;
            case 3:
            default:
                throw new PdfException(PdfException.UnknownEncryptionTypeVEq1, vValue);
            case 4:
            case 5:
                PdfDictionary dic = encDict.getAsDictionary(PdfName.CF);
                if (dic == null) {
                    throw new PdfException(PdfException.CfNotFoundEncryption);
                }
                PdfDictionary dic2 = (PdfDictionary) dic.get(PdfName.DefaultCryptFilter);
                if (dic2 == null) {
                    throw new PdfException(PdfException.DefaultcryptfilterNotFoundEncryption);
                }
                if (PdfName.V2.equals(dic2.get(PdfName.CFM))) {
                    cryptoMode = 1;
                    length = 128;
                } else if (PdfName.AESV2.equals(dic2.get(PdfName.CFM))) {
                    cryptoMode = 2;
                    length = 128;
                } else if (PdfName.AESV3.equals(dic2.get(PdfName.CFM))) {
                    cryptoMode = 3;
                    length = 256;
                } else {
                    throw new PdfException(PdfException.NoCompatibleEncryptionFound);
                }
                PdfBoolean em = dic2.getAsBoolean(PdfName.EncryptMetadata);
                if (em != null && !em.getValue()) {
                    cryptoMode |= 8;
                    break;
                }
                break;
        }
        return setCryptoMode(cryptoMode, length);
    }

    private int fixAccessibilityPermissionPdf20(int permissions) {
        return permissions | 512;
    }
}

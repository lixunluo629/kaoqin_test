package com.itextpdf.kernel.crypto.securityhandler;

import com.itextpdf.kernel.crypto.AesDecryptor;
import com.itextpdf.kernel.crypto.IDecryptor;
import com.itextpdf.kernel.crypto.OutputStreamAesEncryption;
import com.itextpdf.kernel.crypto.OutputStreamEncryption;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfBoolean;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.security.IExternalDecryptionProcess;
import java.io.OutputStream;
import java.security.Key;
import java.security.cert.Certificate;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/crypto/securityhandler/PubSecHandlerUsingAes128.class */
public class PubSecHandlerUsingAes128 extends PubKeySecurityHandler {
    private static final byte[] salt = {115, 65, 108, 84};
    private static final long serialVersionUID = -6752298218106272395L;

    public PubSecHandlerUsingAes128(PdfDictionary encryptionDictionary, Certificate[] certs, int[] permissions, boolean encryptMetadata, boolean embeddedFilesOnly) {
        initKeyAndFillDictionary(encryptionDictionary, certs, permissions, encryptMetadata, embeddedFilesOnly);
    }

    public PubSecHandlerUsingAes128(PdfDictionary encryptionDictionary, Key certificateKey, Certificate certificate, String certificateKeyProvider, IExternalDecryptionProcess externalDecryptionProcess, boolean encryptMetadata) {
        initKeyAndReadDictionary(encryptionDictionary, certificateKey, certificate, certificateKeyProvider, externalDecryptionProcess, encryptMetadata);
    }

    @Override // com.itextpdf.kernel.crypto.securityhandler.SecurityHandler
    public OutputStreamEncryption getEncryptionStream(OutputStream os) {
        return new OutputStreamAesEncryption(os, this.nextObjectKey, 0, this.nextObjectKeySize);
    }

    @Override // com.itextpdf.kernel.crypto.securityhandler.SecurityHandler
    public IDecryptor getDecryptor() {
        return new AesDecryptor(this.nextObjectKey, 0, this.nextObjectKeySize);
    }

    @Override // com.itextpdf.kernel.crypto.securityhandler.SecurityHandler
    public void setHashKeyForNextObject(int objNumber, int objGeneration) {
        this.md5.reset();
        this.extra[0] = (byte) objNumber;
        this.extra[1] = (byte) (objNumber >> 8);
        this.extra[2] = (byte) (objNumber >> 16);
        this.extra[3] = (byte) objGeneration;
        this.extra[4] = (byte) (objGeneration >> 8);
        this.md5.update(this.mkey);
        this.md5.update(this.extra);
        this.md5.update(salt);
        this.nextObjectKey = this.md5.digest();
        this.nextObjectKeySize = this.mkey.length + 5;
        if (this.nextObjectKeySize > 16) {
            this.nextObjectKeySize = 16;
        }
    }

    @Override // com.itextpdf.kernel.crypto.securityhandler.PubKeySecurityHandler
    protected String getDigestAlgorithm() {
        return "SHA-1";
    }

    @Override // com.itextpdf.kernel.crypto.securityhandler.PubKeySecurityHandler
    protected void initKey(byte[] globalKey, int keyLength) {
        this.mkey = new byte[keyLength / 8];
        System.arraycopy(globalKey, 0, this.mkey, 0, this.mkey.length);
    }

    @Override // com.itextpdf.kernel.crypto.securityhandler.PubKeySecurityHandler
    protected void setPubSecSpecificHandlerDicEntries(PdfDictionary encryptionDictionary, boolean encryptMetadata, boolean embeddedFilesOnly) {
        encryptionDictionary.put(PdfName.Filter, PdfName.Adobe_PubSec);
        encryptionDictionary.put(PdfName.SubFilter, PdfName.Adbe_pkcs7_s5);
        encryptionDictionary.put(PdfName.R, new PdfNumber(4));
        encryptionDictionary.put(PdfName.V, new PdfNumber(4));
        PdfArray recipients = createRecipientsArray();
        PdfDictionary stdcf = new PdfDictionary();
        stdcf.put(PdfName.Recipients, recipients);
        if (!encryptMetadata) {
            stdcf.put(PdfName.EncryptMetadata, PdfBoolean.FALSE);
        }
        stdcf.put(PdfName.CFM, PdfName.AESV2);
        stdcf.put(PdfName.Length, new PdfNumber(128));
        PdfDictionary cf = new PdfDictionary();
        cf.put(PdfName.DefaultCryptFilter, stdcf);
        encryptionDictionary.put(PdfName.CF, cf);
        if (embeddedFilesOnly) {
            encryptionDictionary.put(PdfName.EFF, PdfName.DefaultCryptFilter);
            encryptionDictionary.put(PdfName.StrF, PdfName.Identity);
            encryptionDictionary.put(PdfName.StmF, PdfName.Identity);
        } else {
            encryptionDictionary.put(PdfName.StrF, PdfName.DefaultCryptFilter);
            encryptionDictionary.put(PdfName.StmF, PdfName.DefaultCryptFilter);
        }
    }
}

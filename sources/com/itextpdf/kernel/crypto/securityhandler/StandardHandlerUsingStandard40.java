package com.itextpdf.kernel.crypto.securityhandler;

import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.crypto.ARCFOUREncryption;
import com.itextpdf.kernel.crypto.BadPasswordException;
import com.itextpdf.kernel.crypto.IDecryptor;
import com.itextpdf.kernel.crypto.OutputStreamEncryption;
import com.itextpdf.kernel.crypto.OutputStreamStandardEncryption;
import com.itextpdf.kernel.crypto.StandardDecryptor;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import java.io.OutputStream;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/crypto/securityhandler/StandardHandlerUsingStandard40.class */
public class StandardHandlerUsingStandard40 extends StandardSecurityHandler {
    protected static final byte[] pad = {40, -65, 78, 94, 78, 117, -118, 65, 100, 0, 78, 86, -1, -6, 1, 8, 46, 46, 0, -74, -48, 104, 62, Byte.MIN_VALUE, 47, 12, -87, -2, 100, 83, 105, 122};
    protected static final byte[] metadataPad = {-1, -1, -1, -1};
    private static final long serialVersionUID = -7951837491441953183L;
    protected byte[] documentId;
    protected int keyLength;
    protected ARCFOUREncryption arcfour = new ARCFOUREncryption();

    public StandardHandlerUsingStandard40(PdfDictionary encryptionDictionary, byte[] userPassword, byte[] ownerPassword, int permissions, boolean encryptMetadata, boolean embeddedFilesOnly, byte[] documentId) {
        initKeyAndFillDictionary(encryptionDictionary, userPassword, ownerPassword, permissions, encryptMetadata, embeddedFilesOnly, documentId);
    }

    public StandardHandlerUsingStandard40(PdfDictionary encryptionDictionary, byte[] password, byte[] documentId, boolean encryptMetadata) {
        initKeyAndReadDictionary(encryptionDictionary, password, documentId, encryptMetadata);
    }

    @Override // com.itextpdf.kernel.crypto.securityhandler.SecurityHandler
    public OutputStreamEncryption getEncryptionStream(OutputStream os) {
        return new OutputStreamStandardEncryption(os, this.nextObjectKey, 0, this.nextObjectKeySize);
    }

    @Override // com.itextpdf.kernel.crypto.securityhandler.SecurityHandler
    public IDecryptor getDecryptor() {
        return new StandardDecryptor(this.nextObjectKey, 0, this.nextObjectKeySize);
    }

    public byte[] computeUserPassword(byte[] ownerPassword, PdfDictionary encryptionDictionary) {
        byte[] ownerKey = getIsoBytes(encryptionDictionary.getAsString(PdfName.O));
        byte[] userPad = computeOwnerKey(ownerKey, padPassword(ownerPassword));
        for (int i = 0; i < userPad.length; i++) {
            boolean match = true;
            int j = 0;
            while (true) {
                if (j >= userPad.length - i) {
                    break;
                }
                if (userPad[i + j] == pad[j]) {
                    j++;
                } else {
                    match = false;
                    break;
                }
            }
            if (match) {
                byte[] userPassword = new byte[i];
                System.arraycopy(userPad, 0, userPassword, 0, i);
                return userPassword;
            }
        }
        return userPad;
    }

    protected void calculatePermissions(int permissions) {
        this.permissions = (permissions | (-64)) & (-4);
    }

    protected byte[] computeOwnerKey(byte[] userPad, byte[] ownerPad) {
        byte[] ownerKey = new byte[32];
        byte[] digest = this.md5.digest(ownerPad);
        this.arcfour.prepareARCFOURKey(digest, 0, 5);
        this.arcfour.encryptARCFOUR(userPad, ownerKey);
        return ownerKey;
    }

    protected void computeGlobalEncryptionKey(byte[] userPad, byte[] ownerKey, boolean encryptMetadata) {
        this.mkey = new byte[this.keyLength / 8];
        this.md5.reset();
        this.md5.update(userPad);
        this.md5.update(ownerKey);
        byte[] ext = {(byte) this.permissions, (byte) (this.permissions >> 8), (byte) (this.permissions >> 16), (byte) (this.permissions >> 24)};
        this.md5.update(ext, 0, 4);
        if (this.documentId != null) {
            this.md5.update(this.documentId);
        }
        if (!encryptMetadata) {
            this.md5.update(metadataPad);
        }
        byte[] digest = new byte[this.mkey.length];
        System.arraycopy(this.md5.digest(), 0, digest, 0, this.mkey.length);
        System.arraycopy(digest, 0, this.mkey, 0, this.mkey.length);
    }

    protected byte[] computeUserKey() {
        byte[] userKey = new byte[32];
        this.arcfour.prepareARCFOURKey(this.mkey);
        this.arcfour.encryptARCFOUR(pad, userKey);
        return userKey;
    }

    protected void setSpecificHandlerDicEntries(PdfDictionary encryptionDictionary, boolean encryptMetadata, boolean embeddedFilesOnly) {
        encryptionDictionary.put(PdfName.R, new PdfNumber(2));
        encryptionDictionary.put(PdfName.V, new PdfNumber(1));
    }

    protected boolean isValidPassword(byte[] uValue, byte[] userKey) {
        return !equalsArray(uValue, userKey, 32);
    }

    private void initKeyAndFillDictionary(PdfDictionary encryptionDictionary, byte[] userPassword, byte[] ownerPassword, int permissions, boolean encryptMetadata, boolean embeddedFilesOnly, byte[] documentId) {
        byte[] ownerPassword2 = generateOwnerPasswordIfNullOrEmpty(ownerPassword);
        calculatePermissions(permissions);
        this.documentId = documentId;
        this.keyLength = getKeyLength(encryptionDictionary);
        byte[] userPad = padPassword(userPassword);
        byte[] ownerPad = padPassword(ownerPassword2);
        byte[] ownerKey = computeOwnerKey(userPad, ownerPad);
        computeGlobalEncryptionKey(userPad, ownerKey, encryptMetadata);
        byte[] userKey = computeUserKey();
        setStandardHandlerDicEntries(encryptionDictionary, userKey, ownerKey);
        setSpecificHandlerDicEntries(encryptionDictionary, encryptMetadata, embeddedFilesOnly);
    }

    private void initKeyAndReadDictionary(PdfDictionary encryptionDictionary, byte[] password, byte[] documentId, boolean encryptMetadata) {
        byte[] uValue = getIsoBytes(encryptionDictionary.getAsString(PdfName.U));
        byte[] oValue = getIsoBytes(encryptionDictionary.getAsString(PdfName.O));
        PdfNumber pValue = (PdfNumber) encryptionDictionary.get(PdfName.P);
        this.permissions = pValue.longValue();
        this.documentId = documentId;
        this.keyLength = getKeyLength(encryptionDictionary);
        byte[] paddedPassword = padPassword(password);
        checkPassword(encryptMetadata, uValue, oValue, paddedPassword);
    }

    private void checkPassword(boolean encryptMetadata, byte[] uValue, byte[] oValue, byte[] paddedPassword) {
        byte[] userPad = computeOwnerKey(oValue, paddedPassword);
        computeGlobalEncryptionKey(userPad, oValue, encryptMetadata);
        byte[] userKey = computeUserKey();
        if (isValidPassword(uValue, userKey)) {
            computeGlobalEncryptionKey(paddedPassword, oValue, encryptMetadata);
            byte[] userKey2 = computeUserKey();
            if (isValidPassword(uValue, userKey2)) {
                throw new BadPasswordException(PdfException.BadUserPassword);
            }
            this.usedOwnerPassword = false;
        }
    }

    private byte[] padPassword(byte[] password) {
        byte[] userPad = new byte[32];
        if (password == null) {
            System.arraycopy(pad, 0, userPad, 0, 32);
        } else {
            System.arraycopy(password, 0, userPad, 0, Math.min(password.length, 32));
            if (password.length < 32) {
                System.arraycopy(pad, 0, userPad, password.length, 32 - password.length);
            }
        }
        return userPad;
    }

    private int getKeyLength(PdfDictionary encryptionDict) {
        Integer keyLength = encryptionDict.getAsInt(PdfName.Length);
        if (keyLength != null) {
            return keyLength.intValue();
        }
        return 40;
    }
}

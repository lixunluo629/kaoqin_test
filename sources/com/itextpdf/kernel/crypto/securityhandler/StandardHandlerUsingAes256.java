package com.itextpdf.kernel.crypto.securityhandler;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.io.util.StreamUtil;
import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.crypto.AESCipherCBCnoPad;
import com.itextpdf.kernel.crypto.AesDecryptor;
import com.itextpdf.kernel.crypto.BadPasswordException;
import com.itextpdf.kernel.crypto.IDecryptor;
import com.itextpdf.kernel.crypto.IVGenerator;
import com.itextpdf.kernel.crypto.OutputStreamAesEncryption;
import com.itextpdf.kernel.crypto.OutputStreamEncryption;
import com.itextpdf.kernel.pdf.PdfBoolean;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfLiteral;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfVersion;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import org.bouncycastle.crypto.DataLengthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/crypto/securityhandler/StandardHandlerUsingAes256.class */
public class StandardHandlerUsingAes256 extends StandardSecurityHandler {
    private static final long serialVersionUID = -8365943606887257386L;
    private static final int VALIDATION_SALT_OFFSET = 32;
    private static final int KEY_SALT_OFFSET = 40;
    private static final int SALT_LENGTH = 8;
    private boolean isPdf2;
    protected boolean encryptMetadata;

    public StandardHandlerUsingAes256(PdfDictionary encryptionDictionary, byte[] userPassword, byte[] ownerPassword, int permissions, boolean encryptMetadata, boolean embeddedFilesOnly, PdfVersion version) {
        this.isPdf2 = version != null && version.compareTo(PdfVersion.PDF_2_0) >= 0;
        initKeyAndFillDictionary(encryptionDictionary, userPassword, ownerPassword, permissions, encryptMetadata, embeddedFilesOnly);
    }

    public StandardHandlerUsingAes256(PdfDictionary encryptionDictionary, byte[] password) {
        initKeyAndReadDictionary(encryptionDictionary, password);
    }

    public boolean isEncryptMetadata() {
        return this.encryptMetadata;
    }

    @Override // com.itextpdf.kernel.crypto.securityhandler.SecurityHandler
    public void setHashKeyForNextObject(int objNumber, int objGeneration) {
    }

    @Override // com.itextpdf.kernel.crypto.securityhandler.SecurityHandler
    public OutputStreamEncryption getEncryptionStream(OutputStream os) {
        return new OutputStreamAesEncryption(os, this.nextObjectKey, 0, this.nextObjectKeySize);
    }

    @Override // com.itextpdf.kernel.crypto.securityhandler.SecurityHandler
    public IDecryptor getDecryptor() {
        return new AesDecryptor(this.nextObjectKey, 0, this.nextObjectKeySize);
    }

    private void initKeyAndFillDictionary(PdfDictionary encryptionDictionary, byte[] userPassword, byte[] ownerPassword, int permissions, boolean encryptMetadata, boolean embeddedFilesOnly) {
        byte[] ownerPassword2 = generateOwnerPasswordIfNullOrEmpty(ownerPassword);
        int permissions2 = (permissions | (-3904)) & (-4);
        try {
            if (userPassword == null) {
                userPassword = new byte[0];
            } else if (userPassword.length > 127) {
                userPassword = Arrays.copyOf(userPassword, 127);
            }
            if (ownerPassword2.length > 127) {
                ownerPassword2 = Arrays.copyOf(ownerPassword2, 127);
            }
            byte[] userValAndKeySalt = IVGenerator.getIV(16);
            byte[] ownerValAndKeySalt = IVGenerator.getIV(16);
            this.nextObjectKey = IVGenerator.getIV(32);
            this.nextObjectKeySize = 32;
            byte[] hash = computeHash(userPassword, userValAndKeySalt, 0, 8);
            byte[] userKey = Arrays.copyOf(hash, 48);
            System.arraycopy(userValAndKeySalt, 0, userKey, 32, 16);
            byte[] hash2 = computeHash(userPassword, userValAndKeySalt, 8, 8);
            AESCipherCBCnoPad ac = new AESCipherCBCnoPad(true, hash2);
            byte[] ueKey = ac.processBlock(this.nextObjectKey, 0, this.nextObjectKey.length);
            byte[] hash3 = computeHash(ownerPassword2, ownerValAndKeySalt, 0, 8, userKey);
            byte[] ownerKey = Arrays.copyOf(hash3, 48);
            System.arraycopy(ownerValAndKeySalt, 0, ownerKey, 32, 16);
            byte[] hash4 = computeHash(ownerPassword2, ownerValAndKeySalt, 8, 8, userKey);
            AESCipherCBCnoPad ac2 = new AESCipherCBCnoPad(true, hash4);
            byte[] oeKey = ac2.processBlock(this.nextObjectKey, 0, this.nextObjectKey.length);
            byte[] permsp = IVGenerator.getIV(16);
            permsp[0] = (byte) permissions2;
            permsp[1] = (byte) (permissions2 >> 8);
            permsp[2] = (byte) (permissions2 >> 16);
            permsp[3] = (byte) (permissions2 >> 24);
            permsp[4] = -1;
            permsp[5] = -1;
            permsp[6] = -1;
            permsp[7] = -1;
            permsp[8] = encryptMetadata ? (byte) 84 : (byte) 70;
            permsp[9] = 97;
            permsp[10] = 100;
            permsp[11] = 98;
            AESCipherCBCnoPad ac3 = new AESCipherCBCnoPad(true, this.nextObjectKey);
            byte[] aes256Perms = ac3.processBlock(permsp, 0, permsp.length);
            this.permissions = permissions2;
            this.encryptMetadata = encryptMetadata;
            setStandardHandlerDicEntries(encryptionDictionary, userKey, ownerKey);
            setAES256DicEntries(encryptionDictionary, oeKey, ueKey, aes256Perms, encryptMetadata, embeddedFilesOnly);
        } catch (Exception ex) {
            throw new PdfException(PdfException.PdfEncryption, (Throwable) ex);
        }
    }

    private void setAES256DicEntries(PdfDictionary encryptionDictionary, byte[] oeKey, byte[] ueKey, byte[] aes256Perms, boolean encryptMetadata, boolean embeddedFilesOnly) {
        int i;
        encryptionDictionary.put(PdfName.OE, new PdfLiteral(StreamUtil.createEscapedString(oeKey)));
        encryptionDictionary.put(PdfName.UE, new PdfLiteral(StreamUtil.createEscapedString(ueKey)));
        encryptionDictionary.put(PdfName.Perms, new PdfLiteral(StreamUtil.createEscapedString(aes256Perms)));
        PdfName pdfName = PdfName.R;
        if (!this.isPdf2) {
            i = 5;
        } else {
            i = 6;
        }
        encryptionDictionary.put(pdfName, new PdfNumber(i));
        encryptionDictionary.put(PdfName.V, new PdfNumber(5));
        PdfDictionary stdcf = new PdfDictionary();
        stdcf.put(PdfName.Length, new PdfNumber(32));
        if (!encryptMetadata) {
            encryptionDictionary.put(PdfName.EncryptMetadata, PdfBoolean.FALSE);
        }
        if (embeddedFilesOnly) {
            stdcf.put(PdfName.AuthEvent, PdfName.EFOpen);
            encryptionDictionary.put(PdfName.EFF, PdfName.StdCF);
            encryptionDictionary.put(PdfName.StrF, PdfName.Identity);
            encryptionDictionary.put(PdfName.StmF, PdfName.Identity);
        } else {
            stdcf.put(PdfName.AuthEvent, PdfName.DocOpen);
            encryptionDictionary.put(PdfName.StrF, PdfName.StdCF);
            encryptionDictionary.put(PdfName.StmF, PdfName.StdCF);
        }
        stdcf.put(PdfName.CFM, PdfName.AESV3);
        PdfDictionary cf = new PdfDictionary();
        cf.put(PdfName.StdCF, stdcf);
        encryptionDictionary.put(PdfName.CF, cf);
    }

    private void initKeyAndReadDictionary(PdfDictionary encryptionDictionary, byte[] password) {
        try {
            if (password == null) {
                password = new byte[0];
            } else if (password.length > 127) {
                password = Arrays.copyOf(password, 127);
            }
            this.isPdf2 = encryptionDictionary.getAsNumber(PdfName.R).getValue() == 6.0d;
            byte[] oValue = getIsoBytes(encryptionDictionary.getAsString(PdfName.O));
            byte[] uValue = getIsoBytes(encryptionDictionary.getAsString(PdfName.U));
            byte[] oeValue = getIsoBytes(encryptionDictionary.getAsString(PdfName.OE));
            byte[] ueValue = getIsoBytes(encryptionDictionary.getAsString(PdfName.UE));
            byte[] perms = getIsoBytes(encryptionDictionary.getAsString(PdfName.Perms));
            PdfNumber pValue = (PdfNumber) encryptionDictionary.get(PdfName.P);
            this.permissions = pValue.longValue();
            byte[] hash = computeHash(password, oValue, 32, 8, uValue);
            this.usedOwnerPassword = compareArray(hash, oValue, 32);
            if (this.usedOwnerPassword) {
                byte[] hash2 = computeHash(password, oValue, 40, 8, uValue);
                AESCipherCBCnoPad ac = new AESCipherCBCnoPad(false, hash2);
                this.nextObjectKey = ac.processBlock(oeValue, 0, oeValue.length);
            } else {
                byte[] hash3 = computeHash(password, uValue, 32, 8);
                if (!compareArray(hash3, uValue, 32)) {
                    throw new BadPasswordException(PdfException.BadUserPassword);
                }
                byte[] hash4 = computeHash(password, uValue, 40, 8);
                AESCipherCBCnoPad ac2 = new AESCipherCBCnoPad(false, hash4);
                this.nextObjectKey = ac2.processBlock(ueValue, 0, ueValue.length);
            }
            this.nextObjectKeySize = 32;
            AESCipherCBCnoPad ac3 = new AESCipherCBCnoPad(false, this.nextObjectKey);
            byte[] decPerms = ac3.processBlock(perms, 0, perms.length);
            if (decPerms[9] != 97 || decPerms[10] != 100 || decPerms[11] != 98) {
                throw new BadPasswordException(PdfException.BadUserPassword);
            }
            int permissionsDecoded = (decPerms[0] & 255) | ((decPerms[1] & 255) << 8) | ((decPerms[2] & 255) << 16) | ((decPerms[3] & 255) << 24);
            boolean encryptMetadata = decPerms[8] == 84;
            Boolean encryptMetadataEntry = encryptionDictionary.getAsBool(PdfName.EncryptMetadata);
            if (permissionsDecoded != this.permissions || (encryptMetadataEntry != null && encryptMetadata != encryptMetadataEntry.booleanValue())) {
                Logger logger = LoggerFactory.getLogger((Class<?>) StandardHandlerUsingAes256.class);
                logger.error(LogMessageConstant.ENCRYPTION_ENTRIES_P_AND_ENCRYPT_METADATA_NOT_CORRESPOND_PERMS_ENTRY);
            }
            this.permissions = permissionsDecoded;
            this.encryptMetadata = encryptMetadata;
        } catch (BadPasswordException ex) {
            throw ex;
        } catch (Exception ex2) {
            throw new PdfException(PdfException.PdfEncryption, (Throwable) ex2);
        }
    }

    private byte[] computeHash(byte[] password, byte[] salt, int saltOffset, int saltLen) throws NoSuchAlgorithmException {
        return computeHash(password, salt, saltOffset, saltLen, null);
    }

    private byte[] computeHash(byte[] password, byte[] salt, int saltOffset, int saltLen, byte[] userKey) throws IllegalStateException, DataLengthException, NoSuchAlgorithmException {
        MessageDigest mdSha256 = MessageDigest.getInstance("SHA-256");
        mdSha256.update(password);
        mdSha256.update(salt, saltOffset, saltLen);
        if (userKey != null) {
            mdSha256.update(userKey);
        }
        byte[] k = mdSha256.digest();
        if (this.isPdf2) {
            MessageDigest mdSha384 = MessageDigest.getInstance("SHA-384");
            MessageDigest mdSha512 = MessageDigest.getInstance("SHA-512");
            int userKeyLen = userKey != null ? userKey.length : 0;
            int passAndUserKeyLen = password.length + userKeyLen;
            int roundNum = 0;
            while (true) {
                int k1RepLen = passAndUserKeyLen + k.length;
                byte[] k1 = new byte[k1RepLen * 64];
                System.arraycopy(password, 0, k1, 0, password.length);
                System.arraycopy(k, 0, k1, password.length, k.length);
                if (userKey != null) {
                    System.arraycopy(userKey, 0, k1, password.length + k.length, userKeyLen);
                }
                for (int i = 1; i < 64; i++) {
                    System.arraycopy(k1, 0, k1, k1RepLen * i, k1RepLen);
                }
                AESCipherCBCnoPad cipher = new AESCipherCBCnoPad(true, Arrays.copyOf(k, 16), Arrays.copyOfRange(k, 16, 32));
                byte[] e = cipher.processBlock(k1, 0, k1.length);
                MessageDigest md = null;
                BigInteger i2 = new BigInteger(1, Arrays.copyOf(e, 16));
                int remainder = i2.remainder(BigInteger.valueOf(3L)).intValue();
                switch (remainder) {
                    case 0:
                        md = mdSha256;
                        break;
                    case 1:
                        md = mdSha384;
                        break;
                    case 2:
                        md = mdSha512;
                        break;
                }
                k = md.digest(e);
                roundNum++;
                if (roundNum > 63) {
                    int condVal = e[e.length - 1] & 255;
                    if (condVal <= roundNum - 32) {
                        k = k.length == 32 ? k : Arrays.copyOf(k, 32);
                    }
                }
            }
        }
        return k;
    }

    private static boolean compareArray(byte[] a, byte[] b, int len) {
        for (int k = 0; k < len; k++) {
            if (a[k] != b[k]) {
                return false;
            }
        }
        return true;
    }
}

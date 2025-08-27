package com.itextpdf.kernel.crypto.securityhandler;

import com.itextpdf.io.source.ByteUtils;
import com.itextpdf.io.util.StreamUtil;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfEncryption;
import com.itextpdf.kernel.pdf.PdfLiteral;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfString;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/crypto/securityhandler/StandardSecurityHandler.class */
public abstract class StandardSecurityHandler extends SecurityHandler {
    protected static final int PERMS_MASK_1_FOR_REVISION_2 = -64;
    protected static final int PERMS_MASK_1_FOR_REVISION_3_OR_GREATER = -3904;
    protected static final int PERMS_MASK_2 = -4;
    private static final long serialVersionUID = 5414978568831015690L;
    protected long permissions;
    protected boolean usedOwnerPassword = true;

    public long getPermissions() {
        return this.permissions;
    }

    public boolean isUsedOwnerPassword() {
        return this.usedOwnerPassword;
    }

    protected void setStandardHandlerDicEntries(PdfDictionary encryptionDictionary, byte[] userKey, byte[] ownerKey) {
        encryptionDictionary.put(PdfName.Filter, PdfName.Standard);
        encryptionDictionary.put(PdfName.O, new PdfLiteral(StreamUtil.createEscapedString(ownerKey)));
        encryptionDictionary.put(PdfName.U, new PdfLiteral(StreamUtil.createEscapedString(userKey)));
        encryptionDictionary.put(PdfName.P, new PdfNumber(this.permissions));
    }

    protected byte[] generateOwnerPasswordIfNullOrEmpty(byte[] ownerPassword) {
        if (ownerPassword == null || ownerPassword.length == 0) {
            ownerPassword = this.md5.digest(PdfEncryption.generateNewDocumentId());
        }
        return ownerPassword;
    }

    protected byte[] getIsoBytes(PdfString string) {
        return ByteUtils.getIsoBytes(string.getValue());
    }

    protected static boolean equalsArray(byte[] ar1, byte[] ar2, int size) {
        for (int k = 0; k < size; k++) {
            if (ar1[k] != ar2[k]) {
                return false;
            }
        }
        return true;
    }
}

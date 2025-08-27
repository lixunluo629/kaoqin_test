package com.itextpdf.kernel.crypto.securityhandler;

import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.crypto.IDecryptor;
import com.itextpdf.kernel.crypto.OutputStreamEncryption;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/crypto/securityhandler/SecurityHandler.class */
public abstract class SecurityHandler implements Serializable {
    private static final long serialVersionUID = 7980424575363686173L;
    protected byte[] nextObjectKey;
    protected int nextObjectKeySize;
    protected transient MessageDigest md5;
    protected byte[] mkey = new byte[0];
    protected byte[] extra = new byte[5];

    public abstract OutputStreamEncryption getEncryptionStream(OutputStream outputStream);

    public abstract IDecryptor getDecryptor();

    protected SecurityHandler() {
        safeInitMessageDigest();
    }

    public void setHashKeyForNextObject(int objNumber, int objGeneration) {
        this.md5.reset();
        this.extra[0] = (byte) objNumber;
        this.extra[1] = (byte) (objNumber >> 8);
        this.extra[2] = (byte) (objNumber >> 16);
        this.extra[3] = (byte) objGeneration;
        this.extra[4] = (byte) (objGeneration >> 8);
        this.md5.update(this.mkey);
        this.md5.update(this.extra);
        this.nextObjectKey = this.md5.digest();
        this.nextObjectKeySize = this.mkey.length + 5;
        if (this.nextObjectKeySize > 16) {
            this.nextObjectKeySize = 16;
        }
    }

    private void safeInitMessageDigest() {
        try {
            this.md5 = MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
        } catch (Exception e) {
            throw new PdfException(PdfException.PdfEncryption, (Throwable) e);
        }
    }

    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
        in.defaultReadObject();
        safeInitMessageDigest();
    }
}

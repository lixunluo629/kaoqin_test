package com.itextpdf.kernel.crypto;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/crypto/AESCipher.class */
public class AESCipher {
    private PaddedBufferedBlockCipher bp;

    public AESCipher(boolean forEncryption, byte[] key, byte[] iv) throws IllegalArgumentException {
        BlockCipher aes = new AESFastEngine();
        BlockCipher cbc = new CBCBlockCipher(aes);
        this.bp = new PaddedBufferedBlockCipher(cbc);
        KeyParameter kp = new KeyParameter(key);
        ParametersWithIV piv = new ParametersWithIV(kp, iv);
        this.bp.init(forEncryption, piv);
    }

    public byte[] update(byte[] inp, int inpOff, int inpLen) throws IllegalStateException, DataLengthException {
        byte[] outp;
        int neededLen = this.bp.getUpdateOutputSize(inpLen);
        if (neededLen > 0) {
            outp = new byte[neededLen];
        } else {
            outp = new byte[0];
        }
        this.bp.processBytes(inp, inpOff, inpLen, outp, 0);
        return outp;
    }

    public byte[] doFinal() {
        int neededLen = this.bp.getOutputSize(0);
        byte[] outp = new byte[neededLen];
        try {
            int n = this.bp.doFinal(outp, 0);
            if (n != outp.length) {
                byte[] outp2 = new byte[n];
                System.arraycopy(outp, 0, outp2, 0, n);
                return outp2;
            }
            return outp;
        } catch (Exception e) {
            return outp;
        }
    }
}

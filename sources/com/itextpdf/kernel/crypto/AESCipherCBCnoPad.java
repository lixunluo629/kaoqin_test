package com.itextpdf.kernel.crypto;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/crypto/AESCipherCBCnoPad.class */
public class AESCipherCBCnoPad {
    private BlockCipher cbc;

    public AESCipherCBCnoPad(boolean forEncryption, byte[] key) throws IllegalArgumentException {
        BlockCipher aes = new AESFastEngine();
        this.cbc = new CBCBlockCipher(aes);
        KeyParameter kp = new KeyParameter(key);
        this.cbc.init(forEncryption, kp);
    }

    public AESCipherCBCnoPad(boolean forEncryption, byte[] key, byte[] initVector) throws IllegalArgumentException {
        BlockCipher aes = new AESFastEngine();
        this.cbc = new CBCBlockCipher(aes);
        KeyParameter kp = new KeyParameter(key);
        ParametersWithIV piv = new ParametersWithIV(kp, initVector);
        this.cbc.init(forEncryption, piv);
    }

    public byte[] processBlock(byte[] inp, int inpOff, int inpLen) throws IllegalStateException, DataLengthException {
        if (inpLen % this.cbc.getBlockSize() != 0) {
            throw new IllegalArgumentException("Not multiple of block: " + inpLen);
        }
        byte[] outp = new byte[inpLen];
        int baseOffset = 0;
        while (inpLen > 0) {
            this.cbc.processBlock(inp, inpOff, outp, baseOffset);
            inpLen -= this.cbc.getBlockSize();
            baseOffset += this.cbc.getBlockSize();
            inpOff += this.cbc.getBlockSize();
        }
        return outp;
    }
}

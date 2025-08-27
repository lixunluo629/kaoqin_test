package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/TlsMac.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/tls/TlsMac.class */
public class TlsMac {
    protected long seqNo;
    protected HMac mac;

    public TlsMac(Digest digest, byte[] bArr, int i, int i2) {
        this.mac = new HMac(digest);
        this.mac.init(new KeyParameter(bArr, i, i2));
        this.seqNo = 0L;
    }

    public int getSize() {
        return this.mac.getMacSize();
    }

    /*  JADX ERROR: Failed to decode insn: 0x0010: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[8]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:117)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    public byte[] calculateMac(short r9, byte[] r10, int r11, int r12) {
        /*
            r8 = this;
            java.io.ByteArrayOutputStream r0 = new java.io.ByteArrayOutputStream
            r1 = r0
            r2 = 13
            r1.<init>(r2)
            r13 = r0
            r0 = r8
            r1 = r0
            long r1 = r1.seqNo
            // decode failed: arraycopy: source index -1 out of bounds for object array[8]
            r2 = 1
            long r1 = r1 + r2
            r0.seqNo = r1
            r0 = r13
            org.bouncycastle.crypto.tls.TlsUtils.writeUint64(r-1, r0)
            r-1 = r9
            r0 = r13
            org.bouncycastle.crypto.tls.TlsUtils.writeUint8(r-1, r0)
            r-1 = r13
            org.bouncycastle.crypto.tls.TlsUtils.writeVersion(r-1)
            r-1 = r12
            r0 = r13
            org.bouncycastle.crypto.tls.TlsUtils.writeUint16(r-1, r0)
            goto L3c
            r14 = move-exception
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            r1 = r0
            java.lang.String r2 = "Internal error during mac calculation"
            r1.<init>(r2)
            throw r0
            r-1 = r13
            r-1.toByteArray()
            r14 = r-1
            r-1 = r8
            org.bouncycastle.crypto.macs.HMac r-1 = r-1.mac
            r0 = r14
            r1 = 0
            r2 = r14
            int r2 = r2.length
            r-1.update(r0, r1, r2)
            r-1 = r8
            org.bouncycastle.crypto.macs.HMac r-1 = r-1.mac
            r0 = r10
            r1 = r11
            r2 = r12
            r-1.update(r0, r1, r2)
            r-1 = r8
            org.bouncycastle.crypto.macs.HMac r-1 = r-1.mac
            r-1.getMacSize()
            byte[] r-1 = new byte[r-1]
            r15 = r-1
            r-1 = r8
            org.bouncycastle.crypto.macs.HMac r-1 = r-1.mac
            r0 = r15
            r1 = 0
            r-1.doFinal(r0, r1)
            r-1 = r15
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: org.bouncycastle.crypto.tls.TlsMac.calculateMac(short, byte[], int, int):byte[]");
    }
}

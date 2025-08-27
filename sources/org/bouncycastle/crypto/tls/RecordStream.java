package org.bouncycastle.crypto.tls;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.bouncycastle.util.io.SimpleOutputStream;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/RecordStream.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/tls/RecordStream.class */
class RecordStream {
    private TlsProtocolHandler handler;
    private InputStream is;
    private OutputStream os;
    private TlsCompression readCompression;
    private TlsCompression writeCompression;
    private TlsCipher readCipher;
    private TlsCipher writeCipher;
    private ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    private CombinedHash hash = new CombinedHash();

    /* renamed from: org.bouncycastle.crypto.tls.RecordStream$1, reason: invalid class name */
    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/RecordStream$1.class */
    class AnonymousClass1 extends SimpleOutputStream {
        AnonymousClass1() {
        }

        @Override // java.io.OutputStream
        public void write(byte[] bArr, int i, int i2) throws IOException {
            RecordStream.access$100(RecordStream.this).update(bArr, i, i2);
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/RecordStream$SequenceNumber.class */
    private static class SequenceNumber {
        private long value;
        private boolean exhausted;

        private SequenceNumber() {
            this.value = 0L;
            this.exhausted = false;
        }

        /*  JADX ERROR: Failed to decode insn: 0x001C: MOVE_MULTI
            java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
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
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:464)
            	at jadx.core.ProcessClass.process(ProcessClass.java:69)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
            	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
            	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
            */
        synchronized long nextValue(short r7) throws org.bouncycastle.crypto.tls.TlsFatalAlert {
            /*
                r6 = this;
                r0 = r6
                boolean r0 = r0.exhausted
                if (r0 == 0) goto L10
                org.bouncycastle.crypto.tls.TlsFatalAlert r0 = new org.bouncycastle.crypto.tls.TlsFatalAlert
                r1 = r0
                r2 = r7
                r1.<init>(r2)
                throw r0
                r0 = r6
                long r0 = r0.value
                r8 = r0
                r0 = r6
                r1 = r0
                long r1 = r1.value
                r2 = 1
                long r1 = r1 + r2
                // decode failed: arraycopy: source index -1 out of bounds for object array[6]
                r0.value = r1
                r0 = 0
                int r-1 = (r-1 > r0 ? 1 : (r-1 == r0 ? 0 : -1))
                if (r-1 != 0) goto L2a
                r-1 = r6
                r0 = 1
                r-1.exhausted = r0
                r-1 = r8
                return r-1
            */
            throw new UnsupportedOperationException("Method not decompiled: org.bouncycastle.crypto.tls.RecordStream.SequenceNumber.nextValue(short):long");
        }

        /* synthetic */ SequenceNumber(AnonymousClass1 anonymousClass1) {
            this();
        }
    }

    RecordStream(TlsProtocolHandler tlsProtocolHandler, InputStream inputStream, OutputStream outputStream) {
        this.readCompression = null;
        this.writeCompression = null;
        this.readCipher = null;
        this.writeCipher = null;
        this.handler = tlsProtocolHandler;
        this.is = inputStream;
        this.os = outputStream;
        this.readCompression = new TlsNullCompression();
        this.writeCompression = this.readCompression;
        this.readCipher = new TlsNullCipher();
        this.writeCipher = this.readCipher;
    }

    void clientCipherSpecDecided(TlsCompression tlsCompression, TlsCipher tlsCipher) {
        this.writeCompression = tlsCompression;
        this.writeCipher = tlsCipher;
    }

    void serverClientSpecReceived() {
        this.readCompression = this.writeCompression;
        this.readCipher = this.writeCipher;
    }

    public void readData() throws IOException {
        short uint8 = TlsUtils.readUint8(this.is);
        TlsUtils.checkVersion(this.is, this.handler);
        byte[] bArrDecodeAndVerify = decodeAndVerify(uint8, this.is, TlsUtils.readUint16(this.is));
        this.handler.processData(uint8, bArrDecodeAndVerify, 0, bArrDecodeAndVerify.length);
    }

    protected byte[] decodeAndVerify(short s, InputStream inputStream, int i) throws IOException {
        byte[] bArr = new byte[i];
        TlsUtils.readFully(bArr, inputStream);
        byte[] bArrDecodeCiphertext = this.readCipher.decodeCiphertext(s, bArr, 0, bArr.length);
        OutputStream outputStreamDecompress = this.readCompression.decompress(this.buffer);
        if (outputStreamDecompress == this.buffer) {
            return bArrDecodeCiphertext;
        }
        outputStreamDecompress.write(bArrDecodeCiphertext, 0, bArrDecodeCiphertext.length);
        outputStreamDecompress.flush();
        return getBufferContents();
    }

    protected void writeMessage(short s, byte[] bArr, int i, int i2) throws IOException {
        byte[] bArrEncodePlaintext;
        if (s == 22) {
            updateHandshakeData(bArr, i, i2);
        }
        OutputStream outputStreamCompress = this.writeCompression.compress(this.buffer);
        if (outputStreamCompress == this.buffer) {
            bArrEncodePlaintext = this.writeCipher.encodePlaintext(s, bArr, i, i2);
        } else {
            outputStreamCompress.write(bArr, i, i2);
            outputStreamCompress.flush();
            byte[] bufferContents = getBufferContents();
            bArrEncodePlaintext = this.writeCipher.encodePlaintext(s, bufferContents, 0, bufferContents.length);
        }
        byte[] bArr2 = new byte[bArrEncodePlaintext.length + 5];
        TlsUtils.writeUint8(s, bArr2, 0);
        TlsUtils.writeVersion(bArr2, 1);
        TlsUtils.writeUint16(bArrEncodePlaintext.length, bArr2, 3);
        System.arraycopy(bArrEncodePlaintext, 0, bArr2, 5, bArrEncodePlaintext.length);
        this.os.write(bArr2);
        this.os.flush();
    }

    void updateHandshakeData(byte[] bArr, int i, int i2) {
        this.hash.update(bArr, i, i2);
    }

    byte[] getCurrentHash() {
        return doFinal(new CombinedHash(this.hash));
    }

    protected void close() throws IOException {
        IOException iOException = null;
        try {
            this.is.close();
        } catch (IOException e) {
            iOException = e;
        }
        try {
            this.os.close();
        } catch (IOException e2) {
            iOException = e2;
        }
        if (iOException != null) {
            throw iOException;
        }
    }

    protected void flush() throws IOException {
        this.os.flush();
    }

    private byte[] getBufferContents() {
        byte[] byteArray = this.buffer.toByteArray();
        this.buffer.reset();
        return byteArray;
    }

    private static byte[] doFinal(CombinedHash combinedHash) {
        byte[] bArr = new byte[combinedHash.getDigestSize()];
        combinedHash.doFinal(bArr, 0);
        return bArr;
    }
}

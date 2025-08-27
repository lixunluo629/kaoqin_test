package org.bouncycastle.cms.jcajce;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.InflaterInputStream;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.operator.InputExpander;
import org.bouncycastle.operator.InputExpanderProvider;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/cms/jcajce/ZlibExpanderProvider.class */
public class ZlibExpanderProvider implements InputExpanderProvider {
    private final long limit;

    /* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/cms/jcajce/ZlibExpanderProvider$LimitedInputStream.class */
    private static class LimitedInputStream extends FilterInputStream {
        private long remaining;

        public LimitedInputStream(InputStream inputStream, long j) {
            super(inputStream);
            this.remaining = j;
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
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:117)
            	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
            	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
            */
        @Override // java.io.FilterInputStream, java.io.InputStream
        public int read() throws java.io.IOException {
            /*
                r6 = this;
                r0 = r6
                long r0 = r0.remaining
                r1 = 0
                int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
                if (r0 < 0) goto L27
                r0 = r6
                java.io.InputStream r0 = r0.in
                int r0 = r0.read()
                r7 = r0
                r0 = r7
                if (r0 < 0) goto L25
                r0 = r6
                r1 = r0
                long r1 = r1.remaining
                r2 = 1
                long r1 = r1 - r2
                // decode failed: arraycopy: source index -1 out of bounds for object array[6]
                r0.remaining = r1
                r0 = 0
                int r-1 = (r-1 > r0 ? 1 : (r-1 == r0 ? 0 : -1))
                if (r-1 < 0) goto L27
                r0 = r7
                return r0
                org.bouncycastle.util.io.StreamOverflowException r0 = new org.bouncycastle.util.io.StreamOverflowException
                r1 = r0
                java.lang.String r2 = "expanded byte limit exceeded"
                r1.<init>(r2)
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: org.bouncycastle.cms.jcajce.ZlibExpanderProvider.LimitedInputStream.read():int");
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public int read(byte[] bArr, int i, int i2) throws IOException {
            if (i2 < 1) {
                return super.read(bArr, i, i2);
            }
            if (this.remaining < 1) {
                read();
                return -1;
            }
            int i3 = ((FilterInputStream) this).in.read(bArr, i, this.remaining > ((long) i2) ? i2 : (int) this.remaining);
            if (i3 > 0) {
                this.remaining -= i3;
            }
            return i3;
        }
    }

    public ZlibExpanderProvider() {
        this.limit = -1L;
    }

    public ZlibExpanderProvider(long j) {
        this.limit = j;
    }

    @Override // org.bouncycastle.operator.InputExpanderProvider
    public InputExpander get(final AlgorithmIdentifier algorithmIdentifier) {
        return new InputExpander() { // from class: org.bouncycastle.cms.jcajce.ZlibExpanderProvider.1
            @Override // org.bouncycastle.operator.InputExpander
            public AlgorithmIdentifier getAlgorithmIdentifier() {
                return algorithmIdentifier;
            }

            @Override // org.bouncycastle.operator.InputExpander
            public InputStream getInputStream(InputStream inputStream) {
                FilterInputStream inflaterInputStream = new InflaterInputStream(inputStream);
                if (ZlibExpanderProvider.this.limit >= 0) {
                    inflaterInputStream = new LimitedInputStream(inflaterInputStream, ZlibExpanderProvider.this.limit);
                }
                return inflaterInputStream;
            }
        };
    }
}

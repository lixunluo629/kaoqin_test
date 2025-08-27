package org.bouncycastle.est;

import io.netty.handler.codec.http.HttpHeaders;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import org.bouncycastle.est.HttpUtil;
import org.bouncycastle.util.Properties;
import org.bouncycastle.util.Strings;

/*  JADX ERROR: NullPointerException in pass: ClassModifier
    java.lang.NullPointerException: Cannot invoke "java.util.List.forEach(java.util.function.Consumer)" because "blocks" is null
    	at jadx.core.utils.BlockUtils.collectAllInsns(BlockUtils.java:1029)
    	at jadx.core.dex.visitors.ClassModifier.removeBridgeMethod(ClassModifier.java:245)
    	at jadx.core.dex.visitors.ClassModifier.removeSyntheticMethods(ClassModifier.java:160)
    	at java.base/java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.ClassModifier.visit(ClassModifier.java:65)
    */
/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/est/ESTResponse.class */
public class ESTResponse {
    private final ESTRequest originalRequest;
    private final HttpUtil.Headers headers;
    private final byte[] lineBuffer;
    private final Source source;
    private String HttpVersion;
    private int statusCode;
    private String statusMessage;
    private InputStream inputStream;
    private Long contentLength;
    private long read = 0;
    private Long absoluteReadLimit;
    private static final Long ZERO = 0L;

    /* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/est/ESTResponse$PrintingInputStream.class */
    private class PrintingInputStream extends InputStream {
        private final InputStream src;

        private PrintingInputStream(InputStream inputStream) {
            this.src = inputStream;
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            int i = this.src.read();
            System.out.print(String.valueOf((char) i));
            return i;
        }

        @Override // java.io.InputStream
        public int available() throws IOException {
            return this.src.available();
        }

        @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            this.src.close();
        }
    }

    public ESTResponse(ESTRequest eSTRequest, Source source) throws IOException {
        this.originalRequest = eSTRequest;
        this.source = source;
        if (source instanceof LimitedSource) {
            this.absoluteReadLimit = ((LimitedSource) source).getAbsoluteReadLimit();
        }
        Set<String> setAsKeySet = Properties.asKeySet("org.bouncycastle.debug.est");
        if (setAsKeySet.contains("input") || setAsKeySet.contains("all")) {
            this.inputStream = new PrintingInputStream(source.getInputStream());
        } else {
            this.inputStream = source.getInputStream();
        }
        this.headers = new HttpUtil.Headers();
        this.lineBuffer = new byte[1024];
        process();
    }

    private void process() throws IOException {
        this.HttpVersion = readStringIncluding(' ');
        this.statusCode = Integer.parseInt(readStringIncluding(' '));
        this.statusMessage = readStringIncluding('\n');
        String stringIncluding = readStringIncluding('\n');
        while (true) {
            String str = stringIncluding;
            if (str.length() <= 0) {
                break;
            }
            int iIndexOf = str.indexOf(58);
            if (iIndexOf > -1) {
                this.headers.add(Strings.toLowerCase(str.substring(0, iIndexOf).trim()), str.substring(iIndexOf + 1).trim());
            }
            stringIncluding = readStringIncluding('\n');
        }
        this.contentLength = getContentLength();
        if (this.statusCode == 204 || this.statusCode == 202) {
            if (this.contentLength == null) {
                this.contentLength = 0L;
            } else if (this.statusCode == 204 && this.contentLength.longValue() > 0) {
                throw new IOException("Got HTTP status 204 but Content-length > 0.");
            }
        }
        if (this.contentLength == null) {
            throw new IOException("No Content-length header.");
        }
        if (this.contentLength.equals(ZERO)) {
            this.inputStream = new InputStream() { // from class: org.bouncycastle.est.ESTResponse.1
                @Override // java.io.InputStream
                public int read() throws IOException {
                    return -1;
                }
            };
        }
        if (this.contentLength != null) {
            if (this.contentLength.longValue() < 0) {
                throw new IOException("Server returned negative content length: " + this.absoluteReadLimit);
            }
            if (this.absoluteReadLimit != null && this.contentLength.longValue() >= this.absoluteReadLimit.longValue()) {
                throw new IOException("Content length longer than absolute read limit: " + this.absoluteReadLimit + " Content-Length: " + this.contentLength);
            }
        }
        this.inputStream = wrapWithCounter(this.inputStream, this.absoluteReadLimit);
        if (HttpHeaders.Values.BASE64.equalsIgnoreCase(getHeader("content-transfer-encoding"))) {
            this.inputStream = new CTEBase64InputStream(this.inputStream, getContentLength());
        }
    }

    public String getHeader(String str) {
        return this.headers.getFirstValue(str);
    }

    protected InputStream wrapWithCounter(final InputStream inputStream, final Long l) {
        return new InputStream() { // from class: org.bouncycastle.est.ESTResponse.2
            /* JADX WARN: Failed to check method for inline after forced processorg.bouncycastle.est.ESTResponse.access$108(org.bouncycastle.est.ESTResponse):long */
            @Override // java.io.InputStream
            public int read() throws IOException {
                int i = inputStream.read();
                if (i > -1) {
                    ESTResponse.access$108(ESTResponse.this);
                    if (l != null && ESTResponse.this.read >= l.longValue()) {
                        throw new IOException("Absolute Read Limit exceeded: " + l);
                    }
                }
                return i;
            }

            @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
            public void close() throws IOException {
                if (ESTResponse.this.contentLength != null && ESTResponse.this.contentLength.longValue() - 1 > ESTResponse.this.read) {
                    throw new IOException("Stream closed before limit fully read, Read: " + ESTResponse.this.read + " ContentLength: " + ESTResponse.this.contentLength);
                }
                if (inputStream.available() > 0) {
                    throw new IOException("Stream closed with extra content in pipe that exceeds content length.");
                }
                inputStream.close();
            }
        };
    }

    protected String readStringIncluding(char c) throws IOException {
        int i;
        int i2 = 0;
        do {
            i = this.inputStream.read();
            int i3 = i2;
            i2++;
            this.lineBuffer[i3] = (byte) i;
            if (i2 < this.lineBuffer.length) {
                if (i == c) {
                    break;
                }
            } else {
                throw new IOException("Server sent line > " + this.lineBuffer.length);
            }
        } while (i > -1);
        if (i == -1) {
            throw new EOFException();
        }
        return new String(this.lineBuffer, 0, i2).trim();
    }

    public ESTRequest getOriginalRequest() {
        return this.originalRequest;
    }

    public HttpUtil.Headers getHeaders() {
        return this.headers;
    }

    public String getHttpVersion() {
        return this.HttpVersion;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public String getStatusMessage() {
        return this.statusMessage;
    }

    public InputStream getInputStream() {
        return this.inputStream;
    }

    public Source getSource() {
        return this.source;
    }

    public Long getContentLength() {
        String firstValue = this.headers.getFirstValue("Content-Length");
        if (firstValue == null) {
            return null;
        }
        try {
            return Long.valueOf(Long.parseLong(firstValue));
        } catch (RuntimeException e) {
            throw new RuntimeException("Content Length: '" + firstValue + "' invalid. " + e.getMessage());
        }
    }

    public void close() throws IOException {
        if (this.inputStream != null) {
            this.inputStream.close();
        }
        this.source.close();
    }

    /*  JADX ERROR: Failed to decode insn: 0x0005: MOVE_MULTI
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
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    static /* synthetic */ long access$108(org.bouncycastle.est.ESTResponse r8) {
        /*
            r0 = r8
            r1 = r0
            long r1 = r1.read
            // decode failed: arraycopy: source index -1 out of bounds for object array[8]
            r2 = 1
            long r1 = r1 + r2
            r0.read = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: org.bouncycastle.est.ESTResponse.access$108(org.bouncycastle.est.ESTResponse):long");
    }

    static {
    }
}

package io.netty.handler.ssl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyException;
import java.security.cert.CertificateException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/PemReader.class */
final class PemReader {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) PemReader.class);
    private static final Pattern CERT_PATTERN = Pattern.compile("-+BEGIN\\s+.*CERTIFICATE[^-]*-+(?:\\s|\\r|\\n)+([a-z0-9+/=\\r\\n]+)-+END\\s+.*CERTIFICATE[^-]*-+", 2);
    private static final Pattern KEY_PATTERN = Pattern.compile("-+BEGIN\\s+.*PRIVATE\\s+KEY[^-]*-+(?:\\s|\\r|\\n)+([a-z0-9+/=\\r\\n]+)-+END\\s+.*PRIVATE\\s+KEY[^-]*-+", 2);

    static ByteBuf[] readCertificates(File file) throws IOException, CertificateException {
        try {
            InputStream in = new FileInputStream(file);
            try {
                return readCertificates(in);
            } finally {
                safeClose(in);
            }
        } catch (FileNotFoundException e) {
            throw new CertificateException("could not find certificate file: " + file);
        }
    }

    /* JADX WARN: Incorrect condition in loop: B:8:0x002d */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    static io.netty.buffer.ByteBuf[] readCertificates(java.io.InputStream r5) throws java.security.cert.CertificateException {
        /*
            r0 = r5
            java.lang.String r0 = readContent(r0)     // Catch: java.io.IOException -> L8
            r6 = r0
            goto L14
        L8:
            r7 = move-exception
            java.security.cert.CertificateException r0 = new java.security.cert.CertificateException
            r1 = r0
            java.lang.String r2 = "failed to read certificate input stream"
            r3 = r7
            r1.<init>(r2, r3)
            throw r0
        L14:
            java.util.ArrayList r0 = new java.util.ArrayList
            r1 = r0
            r1.<init>()
            r7 = r0
            java.util.regex.Pattern r0 = io.netty.handler.ssl.PemReader.CERT_PATTERN
            r1 = r6
            java.util.regex.Matcher r0 = r0.matcher(r1)
            r8 = r0
            r0 = 0
            r9 = r0
        L27:
            r0 = r8
            r1 = r9
            boolean r0 = r0.find(r1)
            if (r0 != 0) goto L33
            goto L5f
        L33:
            r0 = r8
            r1 = 1
            java.lang.String r0 = r0.group(r1)
            java.nio.charset.Charset r1 = io.netty.util.CharsetUtil.US_ASCII
            io.netty.buffer.ByteBuf r0 = io.netty.buffer.Unpooled.copiedBuffer(r0, r1)
            r10 = r0
            r0 = r10
            io.netty.buffer.ByteBuf r0 = io.netty.handler.codec.base64.Base64.decode(r0)
            r11 = r0
            r0 = r10
            boolean r0 = r0.release()
            r0 = r7
            r1 = r11
            boolean r0 = r0.add(r1)
            r0 = r8
            int r0 = r0.end()
            r9 = r0
            goto L27
        L5f:
            r0 = r7
            boolean r0 = r0.isEmpty()
            if (r0 == 0) goto L72
            java.security.cert.CertificateException r0 = new java.security.cert.CertificateException
            r1 = r0
            java.lang.String r2 = "found no certificates in input stream"
            r1.<init>(r2)
            throw r0
        L72:
            r0 = r7
            r1 = 0
            io.netty.buffer.ByteBuf[] r1 = new io.netty.buffer.ByteBuf[r1]
            java.lang.Object[] r0 = r0.toArray(r1)
            io.netty.buffer.ByteBuf[] r0 = (io.netty.buffer.ByteBuf[]) r0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.ssl.PemReader.readCertificates(java.io.InputStream):io.netty.buffer.ByteBuf[]");
    }

    static ByteBuf readPrivateKey(File file) throws KeyException, IOException {
        try {
            InputStream in = new FileInputStream(file);
            try {
                return readPrivateKey(in);
            } finally {
                safeClose(in);
            }
        } catch (FileNotFoundException e) {
            throw new KeyException("could not find key file: " + file);
        }
    }

    static ByteBuf readPrivateKey(InputStream in) throws KeyException {
        try {
            String content = readContent(in);
            Matcher m = KEY_PATTERN.matcher(content);
            if (!m.find()) {
                throw new KeyException("could not find a PKCS #8 private key in input stream (see https://netty.io/wiki/sslcontextbuilder-and-private-key.html for more information)");
            }
            ByteBuf base64 = Unpooled.copiedBuffer(m.group(1), CharsetUtil.US_ASCII);
            ByteBuf der = Base64.decode(base64);
            base64.release();
            return der;
        } catch (IOException e) {
            throw new KeyException("failed to read key input stream", e);
        }
    }

    private static String readContent(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            byte[] buf = new byte[8192];
            while (true) {
                int ret = in.read(buf);
                if (ret >= 0) {
                    out.write(buf, 0, ret);
                } else {
                    String string = out.toString(CharsetUtil.US_ASCII.name());
                    safeClose(out);
                    return string;
                }
            }
        } catch (Throwable th) {
            safeClose(out);
            throw th;
        }
    }

    private static void safeClose(InputStream in) throws IOException {
        try {
            in.close();
        } catch (IOException e) {
            logger.warn("Failed to close a stream.", (Throwable) e);
        }
    }

    private static void safeClose(OutputStream out) throws IOException {
        try {
            out.close();
        } catch (IOException e) {
            logger.warn("Failed to close a stream.", (Throwable) e);
        }
    }

    private PemReader() {
    }
}

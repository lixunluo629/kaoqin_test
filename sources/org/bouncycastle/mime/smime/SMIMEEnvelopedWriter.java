package org.bouncycastle.mime.smime;

import io.netty.handler.codec.http.HttpHeaders;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import org.bouncycastle.cms.CMSAttributeTableGenerator;
import org.bouncycastle.cms.CMSEnvelopedDataStreamGenerator;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.OriginatorInformation;
import org.bouncycastle.cms.RecipientInfoGenerator;
import org.bouncycastle.mime.Headers;
import org.bouncycastle.mime.MimeIOException;
import org.bouncycastle.mime.MimeWriter;
import org.bouncycastle.mime.encoding.Base64OutputStream;
import org.bouncycastle.operator.OutputEncryptor;
import org.bouncycastle.util.Strings;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/mime/smime/SMIMEEnvelopedWriter.class */
public class SMIMEEnvelopedWriter extends MimeWriter {
    private final CMSEnvelopedDataStreamGenerator envGen;
    private final OutputEncryptor outEnc;
    private final OutputStream mimeOut;
    private final String contentTransferEncoding;

    /* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/mime/smime/SMIMEEnvelopedWriter$Builder.class */
    public static class Builder {
        private static final String[] stdHeaders = {"Content-Type", "Content-Disposition", HttpHeaders.Names.CONTENT_TRANSFER_ENCODING, "Content-Description"};
        private static final String[] stdValues = {"application/pkcs7-mime; name=\"smime.p7m\"; smime-type=enveloped-data", "attachment; filename=\"smime.p7m\"", HttpHeaders.Values.BASE64, "S/MIME Encrypted Message"};
        private final CMSEnvelopedDataStreamGenerator envGen = new CMSEnvelopedDataStreamGenerator();
        private final Map<String, String> headers = new LinkedHashMap();
        String contentTransferEncoding = HttpHeaders.Values.BASE64;

        public Builder() {
            for (int i = 0; i != stdHeaders.length; i++) {
                this.headers.put(stdHeaders[i], stdValues[i]);
            }
        }

        public Builder setBufferSize(int i) {
            this.envGen.setBufferSize(i);
            return this;
        }

        public Builder setUnprotectedAttributeGenerator(CMSAttributeTableGenerator cMSAttributeTableGenerator) {
            this.envGen.setUnprotectedAttributeGenerator(cMSAttributeTableGenerator);
            return this;
        }

        public Builder setOriginatorInfo(OriginatorInformation originatorInformation) {
            this.envGen.setOriginatorInfo(originatorInformation);
            return this;
        }

        public Builder withHeader(String str, String str2) {
            this.headers.put(str, str2);
            return this;
        }

        public Builder addRecipientInfoGenerator(RecipientInfoGenerator recipientInfoGenerator) {
            this.envGen.addRecipientInfoGenerator(recipientInfoGenerator);
            return this;
        }

        public SMIMEEnvelopedWriter build(OutputStream outputStream, OutputEncryptor outputEncryptor) {
            return new SMIMEEnvelopedWriter(this, outputEncryptor, outputStream);
        }
    }

    /* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/mime/smime/SMIMEEnvelopedWriter$ContentOutputStream.class */
    private class ContentOutputStream extends OutputStream {
        private final OutputStream main;
        private final OutputStream backing;

        ContentOutputStream(OutputStream outputStream, OutputStream outputStream2) {
            this.main = outputStream;
            this.backing = outputStream2;
        }

        @Override // java.io.OutputStream
        public void write(int i) throws IOException {
            this.main.write(i);
        }

        @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            this.main.close();
            if (this.backing != null) {
                this.backing.close();
            }
        }
    }

    private SMIMEEnvelopedWriter(Builder builder, OutputEncryptor outputEncryptor, OutputStream outputStream) {
        super(new Headers(mapToLines(builder.headers), builder.contentTransferEncoding));
        this.envGen = builder.envGen;
        this.contentTransferEncoding = builder.contentTransferEncoding;
        this.outEnc = outputEncryptor;
        this.mimeOut = outputStream;
    }

    @Override // org.bouncycastle.mime.MimeWriter
    public OutputStream getContentStream() throws IOException {
        this.headers.dumpHeaders(this.mimeOut);
        this.mimeOut.write(Strings.toByteArray("\r\n"));
        try {
            if (!HttpHeaders.Values.BASE64.equals(this.contentTransferEncoding)) {
                return new ContentOutputStream(this.envGen.open(SMimeUtils.createUnclosable(this.mimeOut), this.outEnc), null);
            }
            Base64OutputStream base64OutputStream = new Base64OutputStream(this.mimeOut);
            return new ContentOutputStream(this.envGen.open(SMimeUtils.createUnclosable(base64OutputStream), this.outEnc), base64OutputStream);
        } catch (CMSException e) {
            throw new MimeIOException(e.getMessage(), e);
        }
    }
}

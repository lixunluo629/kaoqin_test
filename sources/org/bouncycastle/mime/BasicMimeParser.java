package org.bouncycastle.mime;

import io.netty.handler.codec.http.HttpHeaders;
import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.mime.encoding.Base64InputStream;
import org.bouncycastle.mime.encoding.QuotedPrintableInputStream;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/mime/BasicMimeParser.class */
public class BasicMimeParser implements MimeParser {
    private final InputStream src;
    private final MimeParserContext parserContext;
    private final String defaultContentTransferEncoding;
    private Headers headers;
    private boolean isMultipart;
    private final String boundary;

    public BasicMimeParser(InputStream inputStream) throws IOException {
        this(null, new Headers(inputStream, "7bit"), inputStream);
    }

    public BasicMimeParser(MimeParserContext mimeParserContext, InputStream inputStream) throws IOException {
        this(mimeParserContext, new Headers(inputStream, mimeParserContext.getDefaultContentTransferEncoding()), inputStream);
    }

    public BasicMimeParser(Headers headers, InputStream inputStream) {
        this(null, headers, inputStream);
    }

    public BasicMimeParser(MimeParserContext mimeParserContext, Headers headers, InputStream inputStream) {
        this.isMultipart = false;
        if (headers.isMultipart()) {
            this.isMultipart = true;
            this.boundary = headers.getBoundary();
        } else {
            this.boundary = null;
        }
        this.headers = headers;
        this.parserContext = mimeParserContext;
        this.src = inputStream;
        this.defaultContentTransferEncoding = mimeParserContext != null ? mimeParserContext.getDefaultContentTransferEncoding() : "7bit";
    }

    @Override // org.bouncycastle.mime.MimeParser
    public void parse(MimeParserListener mimeParserListener) throws IOException {
        MimeContext mimeContextCreateContext = mimeParserListener.createContext(this.parserContext, this.headers);
        if (!this.isMultipart) {
            mimeParserListener.object(this.parserContext, this.headers, processStream(this.headers, mimeContextCreateContext.applyContext(this.headers, this.src)));
            return;
        }
        MimeMultipartContext mimeMultipartContext = (MimeMultipartContext) mimeContextCreateContext;
        String str = ScriptUtils.DEFAULT_COMMENT_PREFIX + this.boundary;
        boolean z = false;
        int i = 0;
        LineReader lineReader = new LineReader(this.src);
        while (true) {
            String line = lineReader.readLine();
            if (line == null || ScriptUtils.DEFAULT_COMMENT_PREFIX.equals(line)) {
                return;
            }
            if (z) {
                BoundaryLimitedInputStream boundaryLimitedInputStream = new BoundaryLimitedInputStream(this.src, this.boundary);
                Headers headers = new Headers(boundaryLimitedInputStream, this.defaultContentTransferEncoding);
                int i2 = i;
                i++;
                InputStream inputStreamApplyContext = mimeMultipartContext.createContext(i2).applyContext(headers, boundaryLimitedInputStream);
                mimeParserListener.object(this.parserContext, headers, processStream(headers, inputStreamApplyContext));
                if (inputStreamApplyContext.read() >= 0) {
                    throw new IOException("MIME object not fully processed");
                }
            } else if (str.equals(line)) {
                z = true;
                BoundaryLimitedInputStream boundaryLimitedInputStream2 = new BoundaryLimitedInputStream(this.src, this.boundary);
                Headers headers2 = new Headers(boundaryLimitedInputStream2, this.defaultContentTransferEncoding);
                int i3 = i;
                i++;
                InputStream inputStreamApplyContext2 = mimeMultipartContext.createContext(i3).applyContext(headers2, boundaryLimitedInputStream2);
                mimeParserListener.object(this.parserContext, headers2, processStream(headers2, inputStreamApplyContext2));
                if (inputStreamApplyContext2.read() >= 0) {
                    throw new IOException("MIME object not fully processed");
                }
            } else {
                continue;
            }
        }
    }

    public boolean isMultipart() {
        return this.isMultipart;
    }

    private InputStream processStream(Headers headers, InputStream inputStream) {
        return headers.getContentTransferEncoding().equals(HttpHeaders.Values.BASE64) ? new Base64InputStream(inputStream) : headers.getContentTransferEncoding().equals(HttpHeaders.Values.QUOTED_PRINTABLE) ? new QuotedPrintableInputStream(inputStream) : inputStream;
    }
}

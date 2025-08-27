package org.apache.commons.httpclient.methods.multipart;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.util.EncodingUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/methods/multipart/MultipartRequestEntity.class */
public class MultipartRequestEntity implements RequestEntity {
    private static final Log log;
    private static final String MULTIPART_FORM_CONTENT_TYPE = "multipart/form-data";
    private static byte[] MULTIPART_CHARS;
    protected Part[] parts;
    private byte[] multipartBoundary;
    private HttpMethodParams params;
    static Class class$org$apache$commons$httpclient$methods$multipart$MultipartRequestEntity;

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        Class clsClass$;
        if (class$org$apache$commons$httpclient$methods$multipart$MultipartRequestEntity == null) {
            clsClass$ = class$("org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity");
            class$org$apache$commons$httpclient$methods$multipart$MultipartRequestEntity = clsClass$;
        } else {
            clsClass$ = class$org$apache$commons$httpclient$methods$multipart$MultipartRequestEntity;
        }
        log = LogFactory.getLog(clsClass$);
        MULTIPART_CHARS = EncodingUtil.getAsciiBytes("-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    private static byte[] generateMultipartBoundary() {
        Random rand = new Random();
        byte[] bytes = new byte[rand.nextInt(11) + 30];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)];
        }
        return bytes;
    }

    public MultipartRequestEntity(Part[] parts, HttpMethodParams params) {
        if (parts == null) {
            throw new IllegalArgumentException("parts cannot be null");
        }
        if (params == null) {
            throw new IllegalArgumentException("params cannot be null");
        }
        this.parts = parts;
        this.params = params;
    }

    protected byte[] getMultipartBoundary() {
        if (this.multipartBoundary == null) {
            String temp = (String) this.params.getParameter(HttpMethodParams.MULTIPART_BOUNDARY);
            if (temp != null) {
                this.multipartBoundary = EncodingUtil.getAsciiBytes(temp);
            } else {
                this.multipartBoundary = generateMultipartBoundary();
            }
        }
        return this.multipartBoundary;
    }

    @Override // org.apache.commons.httpclient.methods.RequestEntity
    public boolean isRepeatable() {
        for (int i = 0; i < this.parts.length; i++) {
            if (!this.parts[i].isRepeatable()) {
                return false;
            }
        }
        return true;
    }

    @Override // org.apache.commons.httpclient.methods.RequestEntity
    public void writeRequest(OutputStream out) throws IOException {
        Part.sendParts(out, this.parts, getMultipartBoundary());
    }

    @Override // org.apache.commons.httpclient.methods.RequestEntity
    public long getContentLength() {
        try {
            return Part.getLengthOfParts(this.parts, getMultipartBoundary());
        } catch (Exception e) {
            log.error("An exception occurred while getting the length of the parts", e);
            return 0L;
        }
    }

    @Override // org.apache.commons.httpclient.methods.RequestEntity
    public String getContentType() {
        StringBuffer buffer = new StringBuffer("multipart/form-data");
        buffer.append("; boundary=");
        buffer.append(EncodingUtil.getAsciiString(getMultipartBoundary()));
        return buffer.toString();
    }
}

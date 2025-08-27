package org.apache.commons.httpclient.methods;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/methods/InputStreamRequestEntity.class */
public class InputStreamRequestEntity implements RequestEntity {
    public static final int CONTENT_LENGTH_AUTO = -2;
    private static final Log LOG;
    private long contentLength;
    private InputStream content;
    private byte[] buffer;
    private String contentType;
    static Class class$org$apache$commons$httpclient$methods$InputStreamRequestEntity;

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        Class clsClass$;
        if (class$org$apache$commons$httpclient$methods$InputStreamRequestEntity == null) {
            clsClass$ = class$("org.apache.commons.httpclient.methods.InputStreamRequestEntity");
            class$org$apache$commons$httpclient$methods$InputStreamRequestEntity = clsClass$;
        } else {
            clsClass$ = class$org$apache$commons$httpclient$methods$InputStreamRequestEntity;
        }
        LOG = LogFactory.getLog(clsClass$);
    }

    public InputStreamRequestEntity(InputStream content) {
        this(content, (String) null);
    }

    public InputStreamRequestEntity(InputStream content, String contentType) {
        this(content, -2L, contentType);
    }

    public InputStreamRequestEntity(InputStream content, long contentLength) {
        this(content, contentLength, null);
    }

    public InputStreamRequestEntity(InputStream content, long contentLength, String contentType) {
        this.buffer = null;
        if (content == null) {
            throw new IllegalArgumentException("The content cannot be null");
        }
        this.content = content;
        this.contentLength = contentLength;
        this.contentType = contentType;
    }

    @Override // org.apache.commons.httpclient.methods.RequestEntity
    public String getContentType() {
        return this.contentType;
    }

    private void bufferContent() throws IOException {
        if (this.buffer == null && this.content != null) {
            try {
                ByteArrayOutputStream tmp = new ByteArrayOutputStream();
                byte[] data = new byte[4096];
                while (true) {
                    int l = this.content.read(data);
                    if (l >= 0) {
                        tmp.write(data, 0, l);
                    } else {
                        this.buffer = tmp.toByteArray();
                        this.content = null;
                        this.contentLength = this.buffer.length;
                        return;
                    }
                }
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
                this.buffer = null;
                this.content = null;
                this.contentLength = 0L;
            }
        }
    }

    @Override // org.apache.commons.httpclient.methods.RequestEntity
    public boolean isRepeatable() {
        return this.buffer != null;
    }

    @Override // org.apache.commons.httpclient.methods.RequestEntity
    public void writeRequest(OutputStream out) throws IOException {
        if (this.content == null) {
            if (this.buffer != null) {
                out.write(this.buffer);
                return;
            }
            throw new IllegalStateException("Content must be set before entity is written");
        }
        byte[] tmp = new byte[4096];
        int total = 0;
        while (true) {
            int i = this.content.read(tmp);
            if (i >= 0) {
                out.write(tmp, 0, i);
                total += i;
            } else {
                return;
            }
        }
    }

    @Override // org.apache.commons.httpclient.methods.RequestEntity
    public long getContentLength() throws IOException {
        if (this.contentLength == -2 && this.buffer == null) {
            bufferContent();
        }
        return this.contentLength;
    }

    public InputStream getContent() {
        return this.content;
    }
}

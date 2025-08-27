package org.apache.commons.httpclient.methods.multipart;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.httpclient.util.EncodingUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/methods/multipart/StringPart.class */
public class StringPart extends PartBase {
    private static final Log LOG;
    public static final String DEFAULT_CONTENT_TYPE = "text/plain";
    public static final String DEFAULT_CHARSET = "US-ASCII";
    public static final String DEFAULT_TRANSFER_ENCODING = "8bit";
    private byte[] content;
    private String value;
    static Class class$org$apache$commons$httpclient$methods$multipart$StringPart;

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        Class clsClass$;
        if (class$org$apache$commons$httpclient$methods$multipart$StringPart == null) {
            clsClass$ = class$("org.apache.commons.httpclient.methods.multipart.StringPart");
            class$org$apache$commons$httpclient$methods$multipart$StringPart = clsClass$;
        } else {
            clsClass$ = class$org$apache$commons$httpclient$methods$multipart$StringPart;
        }
        LOG = LogFactory.getLog(clsClass$);
    }

    public StringPart(String name, String value, String charset) {
        super(name, "text/plain", charset == null ? "US-ASCII" : charset, DEFAULT_TRANSFER_ENCODING);
        if (value == null) {
            throw new IllegalArgumentException("Value may not be null");
        }
        if (value.indexOf(0) != -1) {
            throw new IllegalArgumentException("NULs may not be present in string parts");
        }
        this.value = value;
    }

    public StringPart(String name, String value) {
        this(name, value, null);
    }

    private byte[] getContent() {
        if (this.content == null) {
            this.content = EncodingUtil.getBytes(this.value, getCharSet());
        }
        return this.content;
    }

    @Override // org.apache.commons.httpclient.methods.multipart.Part
    protected void sendData(OutputStream out) throws IOException {
        LOG.trace("enter sendData(OutputStream)");
        out.write(getContent());
    }

    @Override // org.apache.commons.httpclient.methods.multipart.Part
    protected long lengthOfData() throws IOException {
        LOG.trace("enter lengthOfData()");
        return getContent().length;
    }

    @Override // org.apache.commons.httpclient.methods.multipart.PartBase
    public void setCharSet(String charSet) {
        super.setCharSet(charSet);
        this.content = null;
    }
}

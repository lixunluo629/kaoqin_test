package org.apache.tomcat.util.descriptor.web;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.buf.B2CConverter;
import org.apache.tomcat.util.res.StringManager;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/descriptor/web/XmlEncodingBase.class */
public abstract class XmlEncodingBase {
    private static final StringManager sm = StringManager.getManager((Class<?>) XmlEncodingBase.class);
    private static Log log = LogFactory.getLog((Class<?>) XmlEncodingBase.class);
    private Charset charset = StandardCharsets.UTF_8;

    @Deprecated
    public void setEncoding(String encoding) {
        try {
            this.charset = B2CConverter.getCharset(encoding);
        } catch (UnsupportedEncodingException e) {
            log.warn(sm.getString("xmlEncodingBase.encodingInvalid", encoding, this.charset.name()), e);
        }
    }

    @Deprecated
    public String getEncoding() {
        return this.charset.name();
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public Charset getCharset() {
        return this.charset;
    }
}

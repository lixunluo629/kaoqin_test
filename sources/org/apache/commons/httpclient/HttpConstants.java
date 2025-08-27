package org.apache.commons.httpclient;

import java.io.UnsupportedEncodingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/HttpConstants.class */
public class HttpConstants {
    public static final String HTTP_ELEMENT_CHARSET = "US-ASCII";
    public static final String DEFAULT_CONTENT_CHARSET = "ISO-8859-1";
    private static final Log LOG;
    static Class class$org$apache$commons$httpclient$HttpConstants;

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        Class clsClass$;
        if (class$org$apache$commons$httpclient$HttpConstants == null) {
            clsClass$ = class$("org.apache.commons.httpclient.HttpConstants");
            class$org$apache$commons$httpclient$HttpConstants = clsClass$;
        } else {
            clsClass$ = class$org$apache$commons$httpclient$HttpConstants;
        }
        LOG = LogFactory.getLog(clsClass$);
    }

    public static byte[] getBytes(String data) {
        if (data == null) {
            throw new IllegalArgumentException("Parameter may not be null");
        }
        try {
            return data.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException e) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("Unsupported encoding: US-ASCII. System default encoding used");
            }
            return data.getBytes();
        }
    }

    public static String getString(byte[] data, int offset, int length) {
        if (data == null) {
            throw new IllegalArgumentException("Parameter may not be null");
        }
        try {
            return new String(data, offset, length, "US-ASCII");
        } catch (UnsupportedEncodingException e) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("Unsupported encoding: US-ASCII. System default encoding used");
            }
            return new String(data, offset, length);
        }
    }

    public static String getString(byte[] data) {
        return getString(data, 0, data.length);
    }

    public static byte[] getContentBytes(String data, String charset) {
        if (data == null) {
            throw new IllegalArgumentException("Parameter may not be null");
        }
        if (charset == null || charset.equals("")) {
            charset = "ISO-8859-1";
        }
        try {
            return data.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            if (LOG.isWarnEnabled()) {
                LOG.warn(new StringBuffer().append("Unsupported encoding: ").append(charset).append(". HTTP default encoding used").toString());
            }
            try {
                return data.getBytes("ISO-8859-1");
            } catch (UnsupportedEncodingException e2) {
                if (LOG.isWarnEnabled()) {
                    LOG.warn("Unsupported encoding: ISO-8859-1. System encoding used");
                }
                return data.getBytes();
            }
        }
    }

    public static String getContentString(byte[] data, int offset, int length, String charset) {
        if (data == null) {
            throw new IllegalArgumentException("Parameter may not be null");
        }
        if (charset == null || charset.equals("")) {
            charset = "ISO-8859-1";
        }
        try {
            return new String(data, offset, length, charset);
        } catch (UnsupportedEncodingException e) {
            if (LOG.isWarnEnabled()) {
                LOG.warn(new StringBuffer().append("Unsupported encoding: ").append(charset).append(". Default HTTP encoding used").toString());
            }
            try {
                return new String(data, offset, length, "ISO-8859-1");
            } catch (UnsupportedEncodingException e2) {
                if (LOG.isWarnEnabled()) {
                    LOG.warn("Unsupported encoding: ISO-8859-1. System encoding used");
                }
                return new String(data, offset, length);
            }
        }
    }

    public static String getContentString(byte[] data, String charset) {
        return getContentString(data, 0, data.length, charset);
    }

    public static byte[] getContentBytes(String data) {
        return getContentBytes(data, null);
    }

    public static String getContentString(byte[] data, int offset, int length) {
        return getContentString(data, offset, length, null);
    }

    public static String getContentString(byte[] data) {
        return getContentString(data, null);
    }

    public static byte[] getAsciiBytes(String data) {
        if (data == null) {
            throw new IllegalArgumentException("Parameter may not be null");
        }
        try {
            return data.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("HttpClient requires ASCII support");
        }
    }

    public static String getAsciiString(byte[] data, int offset, int length) {
        if (data == null) {
            throw new IllegalArgumentException("Parameter may not be null");
        }
        try {
            return new String(data, offset, length, "US-ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("HttpClient requires ASCII support");
        }
    }

    public static String getAsciiString(byte[] data) {
        return getAsciiString(data, 0, data.length);
    }
}

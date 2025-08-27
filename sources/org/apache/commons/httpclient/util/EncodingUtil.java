package org.apache.commons.httpclient.util;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.UnsupportedEncodingException;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.httpclient.HttpClientError;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/util/EncodingUtil.class */
public class EncodingUtil {
    private static final String DEFAULT_CHARSET = "ISO-8859-1";
    private static final Log LOG;
    static Class class$org$apache$commons$httpclient$util$EncodingUtil;

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        Class clsClass$;
        if (class$org$apache$commons$httpclient$util$EncodingUtil == null) {
            clsClass$ = class$("org.apache.commons.httpclient.util.EncodingUtil");
            class$org$apache$commons$httpclient$util$EncodingUtil = clsClass$;
        } else {
            clsClass$ = class$org$apache$commons$httpclient$util$EncodingUtil;
        }
        LOG = LogFactory.getLog(clsClass$);
    }

    public static String formUrlEncode(NameValuePair[] pairs, String charset) {
        try {
            return doFormUrlEncode(pairs, charset);
        } catch (UnsupportedEncodingException e) {
            LOG.error(new StringBuffer().append("Encoding not supported: ").append(charset).toString());
            try {
                return doFormUrlEncode(pairs, "ISO-8859-1");
            } catch (UnsupportedEncodingException e2) {
                throw new HttpClientError("Encoding not supported: ISO-8859-1");
            }
        }
    }

    private static String doFormUrlEncode(NameValuePair[] pairs, String charset) throws UnsupportedEncodingException {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < pairs.length; i++) {
            URLCodec codec = new URLCodec();
            NameValuePair pair = pairs[i];
            if (pair.getName() != null) {
                if (i > 0) {
                    buf.append("&");
                }
                buf.append(codec.encode(pair.getName(), charset));
                buf.append(SymbolConstants.EQUAL_SYMBOL);
                if (pair.getValue() != null) {
                    buf.append(codec.encode(pair.getValue(), charset));
                }
            }
        }
        return buf.toString();
    }

    public static String getString(byte[] data, int offset, int length, String charset) {
        if (data == null) {
            throw new IllegalArgumentException("Parameter may not be null");
        }
        if (charset == null || charset.length() == 0) {
            throw new IllegalArgumentException("charset may not be null or empty");
        }
        try {
            return new String(data, offset, length, charset);
        } catch (UnsupportedEncodingException e) {
            if (LOG.isWarnEnabled()) {
                LOG.warn(new StringBuffer().append("Unsupported encoding: ").append(charset).append(". System encoding used").toString());
            }
            return new String(data, offset, length);
        }
    }

    public static String getString(byte[] data, String charset) {
        return getString(data, 0, data.length, charset);
    }

    public static byte[] getBytes(String data, String charset) {
        if (data == null) {
            throw new IllegalArgumentException("data may not be null");
        }
        if (charset == null || charset.length() == 0) {
            throw new IllegalArgumentException("charset may not be null or empty");
        }
        try {
            return data.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            if (LOG.isWarnEnabled()) {
                LOG.warn(new StringBuffer().append("Unsupported encoding: ").append(charset).append(". System encoding used.").toString());
            }
            return data.getBytes();
        }
    }

    public static byte[] getAsciiBytes(String data) {
        if (data == null) {
            throw new IllegalArgumentException("Parameter may not be null");
        }
        try {
            return data.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new HttpClientError("HttpClient requires ASCII support");
        }
    }

    public static String getAsciiString(byte[] data, int offset, int length) {
        if (data == null) {
            throw new IllegalArgumentException("Parameter may not be null");
        }
        try {
            return new String(data, offset, length, "US-ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new HttpClientError("HttpClient requires ASCII support");
        }
    }

    public static String getAsciiString(byte[] data) {
        return getAsciiString(data, 0, data.length);
    }

    private EncodingUtil() {
    }
}

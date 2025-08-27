package org.apache.commons.httpclient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.httpclient.util.EncodingUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/HttpParser.class */
public class HttpParser {
    private static final Log LOG;
    static Class class$org$apache$commons$httpclient$HttpParser;

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        Class clsClass$;
        if (class$org$apache$commons$httpclient$HttpParser == null) {
            clsClass$ = class$("org.apache.commons.httpclient.HttpParser");
            class$org$apache$commons$httpclient$HttpParser = clsClass$;
        } else {
            clsClass$ = class$org$apache$commons$httpclient$HttpParser;
        }
        LOG = LogFactory.getLog(clsClass$);
    }

    private HttpParser() {
    }

    public static byte[] readRawLine(InputStream inputStream) throws IOException {
        int ch2;
        LOG.trace("enter HttpParser.readRawLine()");
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        do {
            ch2 = inputStream.read();
            if (ch2 < 0) {
                break;
            }
            buf.write(ch2);
        } while (ch2 != 10);
        if (buf.size() == 0) {
            return null;
        }
        return buf.toByteArray();
    }

    public static String readLine(InputStream inputStream, String charset) throws IOException {
        LOG.trace("enter HttpParser.readLine(InputStream, String)");
        byte[] rawdata = readRawLine(inputStream);
        if (rawdata == null) {
            return null;
        }
        int len = rawdata.length;
        int offset = 0;
        if (len > 0 && rawdata[len - 1] == 10) {
            offset = 0 + 1;
            if (len > 1 && rawdata[len - 2] == 13) {
                offset++;
            }
        }
        String result = EncodingUtil.getString(rawdata, 0, len - offset, charset);
        if (Wire.HEADER_WIRE.enabled()) {
            String logoutput = result;
            if (offset == 2) {
                logoutput = new StringBuffer().append(result).append("\r\n").toString();
            } else if (offset == 1) {
                logoutput = new StringBuffer().append(result).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR).toString();
            }
            Wire.HEADER_WIRE.input(logoutput);
        }
        return result;
    }

    public static String readLine(InputStream inputStream) throws IOException {
        LOG.trace("enter HttpParser.readLine(InputStream)");
        return readLine(inputStream, "US-ASCII");
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:0x00c8, code lost:
    
        if (r9 == null) goto L27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x00cb, code lost:
    
        r0.add(new org.apache.commons.httpclient.Header(r9, r10.toString()));
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x00ee, code lost:
    
        return (org.apache.commons.httpclient.Header[]) r0.toArray(new org.apache.commons.httpclient.Header[r0.size()]);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static org.apache.commons.httpclient.Header[] parseHeaders(java.io.InputStream r6, java.lang.String r7) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 239
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.httpclient.HttpParser.parseHeaders(java.io.InputStream, java.lang.String):org.apache.commons.httpclient.Header[]");
    }

    public static Header[] parseHeaders(InputStream is) throws IOException {
        LOG.trace("enter HeaderParser.parseHeaders(InputStream, String)");
        return parseHeaders(is, "US-ASCII");
    }
}

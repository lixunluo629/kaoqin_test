package org.apache.xmlbeans.impl.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/SniffedXmlReader.class */
public class SniffedXmlReader extends BufferedReader {
    public static int MAX_SNIFFED_CHARS = 192;
    private static Charset dummy1 = Charset.forName("UTF-8");
    private static Charset dummy2 = Charset.forName("UTF-16");
    private static Charset dummy3 = Charset.forName("UTF-16BE");
    private static Charset dummy4 = Charset.forName("UTF-16LE");
    private static Charset dummy5 = Charset.forName("ISO-8859-1");
    private static Charset dummy6 = Charset.forName("US-ASCII");
    private static Charset dummy7 = Charset.forName("Cp1252");
    private String _encoding;

    public SniffedXmlReader(Reader reader) throws IOException {
        super(reader);
        this._encoding = sniffForXmlDecl();
    }

    private int readAsMuchAsPossible(char[] buf, int startAt, int len) throws IOException {
        int total;
        int count;
        int i = 0;
        while (true) {
            total = i;
            if (total >= len || (count = read(buf, startAt + total, len - total)) < 0) {
                break;
            }
            i = total + count;
        }
        return total;
    }

    private String sniffForXmlDecl() throws IOException {
        mark(MAX_SNIFFED_CHARS);
        try {
            char[] buf = new char[MAX_SNIFFED_CHARS];
            int limit = readAsMuchAsPossible(buf, 0, MAX_SNIFFED_CHARS);
            String strExtractXmlDeclEncoding = SniffedXmlInputStream.extractXmlDeclEncoding(buf, 0, limit);
            reset();
            return strExtractXmlDeclEncoding;
        } catch (Throwable th) {
            reset();
            throw th;
        }
    }

    public String getXmlEncoding() {
        return this._encoding;
    }
}

package org.apache.xmlbeans.impl.common;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/SniffedXmlInputStream.class */
public class SniffedXmlInputStream extends BufferedInputStream {
    public static int MAX_SNIFFED_BYTES;
    private static Charset dummy1;
    private static Charset dummy2;
    private static Charset dummy3;
    private static Charset dummy4;
    private static Charset dummy5;
    private static Charset dummy6;
    private static Charset dummy7;
    private String _encoding;
    private static char[] WHITESPACE;
    private static char[] NOTNAME;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !SniffedXmlInputStream.class.desiredAssertionStatus();
        MAX_SNIFFED_BYTES = 192;
        dummy1 = Charset.forName("UTF-8");
        dummy2 = Charset.forName("UTF-16");
        dummy3 = Charset.forName("UTF-16BE");
        dummy4 = Charset.forName("UTF-16LE");
        dummy5 = Charset.forName("ISO-8859-1");
        dummy6 = Charset.forName("US-ASCII");
        dummy7 = Charset.forName("Cp1252");
        WHITESPACE = new char[]{' ', '\r', '\t', '\n'};
        NOTNAME = new char[]{'=', ' ', '\r', '\t', '\n', '?', '>', '<', '\'', '\"'};
    }

    public SniffedXmlInputStream(InputStream stream) throws IOException {
        String encoding;
        super(stream);
        this._encoding = sniffFourBytes();
        if (this._encoding != null && this._encoding.equals("IBM037") && (encoding = sniffForXmlDecl(this._encoding)) != null) {
            this._encoding = encoding;
        }
        if (this._encoding == null) {
            this._encoding = sniffForXmlDecl("UTF-8");
        }
        if (this._encoding == null) {
            this._encoding = "UTF-8";
        }
    }

    private int readAsMuchAsPossible(byte[] buf, int startAt, int len) throws IOException {
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

    private String sniffFourBytes() throws IOException {
        mark(4);
        try {
            byte[] buf = new byte[4];
            if (readAsMuchAsPossible(buf, 0, 4) < 4) {
                return null;
            }
            long result = ((-16777216) & (buf[0] << 24)) | (16711680 & (buf[1] << 16)) | (65280 & (buf[2] << 8)) | (255 & buf[3]);
            if (result == 65279) {
                reset();
                return "UCS-4";
            }
            if (result == -131072) {
                reset();
                return "UCS-4";
            }
            if (result == 60) {
                reset();
                return "UCS-4BE";
            }
            if (result == 1006632960) {
                reset();
                return "UCS-4LE";
            }
            if (result == 3932223) {
                reset();
                return "UTF-16BE";
            }
            if (result == 1006649088) {
                reset();
                return "UTF-16LE";
            }
            if (result == 1010792557) {
                reset();
                return null;
            }
            if (result == 1282385812) {
                reset();
                return "IBM037";
            }
            if ((result & (-65536)) == -16842752) {
                reset();
                return "UTF-16";
            }
            if ((result & (-65536)) == -131072) {
                reset();
                return "UTF-16";
            }
            if ((result & (-256)) == -272908544) {
                reset();
                return "UTF-8";
            }
            reset();
            return null;
        } finally {
            reset();
        }
    }

    private String sniffForXmlDecl(String encoding) throws IOException {
        int count;
        mark(MAX_SNIFFED_BYTES);
        try {
            byte[] bytebuf = new byte[MAX_SNIFFED_BYTES];
            int bytelimit = readAsMuchAsPossible(bytebuf, 0, MAX_SNIFFED_BYTES);
            Charset charset = Charset.forName(encoding);
            Reader reader = new InputStreamReader(new ByteArrayInputStream(bytebuf, 0, bytelimit), charset);
            char[] buf = new char[bytelimit];
            int limit = 0;
            while (limit < bytelimit && (count = reader.read(buf, limit, bytelimit - limit)) >= 0) {
                limit += count;
            }
            String strExtractXmlDeclEncoding = extractXmlDeclEncoding(buf, 0, limit);
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

    static String extractXmlDeclEncoding(char[] buf, int offset, int size) {
        int limit = offset + size;
        int xmlpi = firstIndexOf("<?xml", buf, offset, limit);
        if (xmlpi >= 0) {
            int i = xmlpi + 5;
            ScannedAttribute attr = new ScannedAttribute();
            while (i < limit) {
                i = scanAttribute(buf, i, limit, attr);
                if (i < 0) {
                    return null;
                }
                if (attr.name.equals("encoding")) {
                    return attr.value;
                }
            }
            return null;
        }
        return null;
    }

    private static int firstIndexOf(String s, char[] buf, int startAt, int limit) {
        if (!$assertionsDisabled && s.length() <= 0) {
            throw new AssertionError();
        }
        char[] lookFor = s.toCharArray();
        char firstchar = lookFor[0];
        int limit2 = limit - lookFor.length;
        while (startAt < limit2) {
            if (buf[startAt] == firstchar) {
                for (int i = 1; i < lookFor.length; i++) {
                    if (buf[startAt + i] != lookFor[i]) {
                        break;
                    }
                }
                return startAt;
            }
            startAt++;
        }
        return -1;
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0028, code lost:
    
        r6 = r6 + 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static int nextNonmatchingByte(char[] r4, char[] r5, int r6, int r7) {
        /*
        L0:
            r0 = r6
            r1 = r7
            if (r0 >= r1) goto L2e
            r0 = r5
            r1 = r6
            char r0 = r0[r1]
            r8 = r0
            r0 = 0
            r9 = r0
        Ld:
            r0 = r9
            r1 = r4
            int r1 = r1.length
            if (r0 >= r1) goto L26
            r0 = r8
            r1 = r4
            r2 = r9
            char r1 = r1[r2]
            if (r0 != r1) goto L20
            goto L28
        L20:
            int r9 = r9 + 1
            goto Ld
        L26:
            r0 = r6
            return r0
        L28:
            int r6 = r6 + 1
            goto L0
        L2e:
            r0 = -1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.xmlbeans.impl.common.SniffedXmlInputStream.nextNonmatchingByte(char[], char[], int, int):int");
    }

    private static int nextMatchingByte(char[] lookFor, char[] buf, int startAt, int limit) {
        while (startAt < limit) {
            char c = buf[startAt];
            for (char c2 : lookFor) {
                if (c == c2) {
                    return startAt;
                }
            }
            startAt++;
        }
        return -1;
    }

    private static int nextMatchingByte(char lookFor, char[] buf, int startAt, int limit) {
        while (startAt < limit) {
            if (buf[startAt] != lookFor) {
                startAt++;
            } else {
                return startAt;
            }
        }
        return -1;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/SniffedXmlInputStream$ScannedAttribute.class */
    private static class ScannedAttribute {
        public String name;
        public String value;

        private ScannedAttribute() {
        }
    }

    private static int scanAttribute(char[] buf, int startAt, int limit, ScannedAttribute attr) {
        int nameEnd;
        int equals;
        int valEndquote;
        int nameStart = nextNonmatchingByte(WHITESPACE, buf, startAt, limit);
        if (nameStart < 0 || (nameEnd = nextMatchingByte(NOTNAME, buf, nameStart, limit)) < 0 || (equals = nextNonmatchingByte(WHITESPACE, buf, nameEnd, limit)) < 0 || buf[equals] != '=') {
            return -1;
        }
        int valQuote = nextNonmatchingByte(WHITESPACE, buf, equals + 1, limit);
        if ((buf[valQuote] != '\'' && buf[valQuote] != '\"') || (valEndquote = nextMatchingByte(buf[valQuote], buf, valQuote + 1, limit)) < 0) {
            return -1;
        }
        attr.name = new String(buf, nameStart, nameEnd - nameStart);
        attr.value = new String(buf, valQuote + 1, (valEndquote - valQuote) - 1);
        return valEndquote + 1;
    }
}

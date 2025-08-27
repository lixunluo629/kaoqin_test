package io.netty.handler.codec.http;

import io.netty.util.CharsetUtil;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/QueryStringEncoder.class */
public class QueryStringEncoder {
    private final Charset charset;
    private final StringBuilder uriBuilder;
    private boolean hasParams;
    private static final byte WRITE_UTF_UNKNOWN = 63;
    private static final char[] CHAR_MAP = "0123456789ABCDEF".toCharArray();

    public QueryStringEncoder(String uri) {
        this(uri, HttpConstants.DEFAULT_CHARSET);
    }

    public QueryStringEncoder(String uri, Charset charset) {
        ObjectUtil.checkNotNull(charset, "charset");
        this.uriBuilder = new StringBuilder(uri);
        this.charset = CharsetUtil.UTF_8.equals(charset) ? null : charset;
    }

    public void addParam(String name, String value) {
        ObjectUtil.checkNotNull(name, "name");
        if (this.hasParams) {
            this.uriBuilder.append('&');
        } else {
            this.uriBuilder.append('?');
            this.hasParams = true;
        }
        encodeComponent(name);
        if (value != null) {
            this.uriBuilder.append('=');
            encodeComponent(value);
        }
    }

    private void encodeComponent(CharSequence s) {
        if (this.charset == null) {
            encodeUtf8Component(s);
        } else {
            encodeNonUtf8Component(s);
        }
    }

    public URI toUri() throws URISyntaxException {
        return new URI(toString());
    }

    public String toString() {
        return this.uriBuilder.toString();
    }

    private void encodeNonUtf8Component(CharSequence s) {
        char cCharAt;
        char[] buf = null;
        int i = 0;
        int len = s.length();
        while (i < len) {
            char c = s.charAt(i);
            if (dontNeedEncoding(c)) {
                this.uriBuilder.append(c);
                i++;
            } else {
                int index = 0;
                if (buf == null) {
                    buf = new char[s.length() - i];
                }
                do {
                    buf[index] = c;
                    index++;
                    i++;
                    if (i >= s.length()) {
                        break;
                    }
                    cCharAt = s.charAt(i);
                    c = cCharAt;
                } while (!dontNeedEncoding(cCharAt));
                byte[] bytes = new String(buf, 0, index).getBytes(this.charset);
                for (byte b : bytes) {
                    appendEncoded(b);
                }
            }
        }
    }

    private void encodeUtf8Component(CharSequence s) {
        int len = s.length();
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if (!dontNeedEncoding(c)) {
                encodeUtf8Component(s, i, len);
                return;
            }
        }
        this.uriBuilder.append(s);
    }

    private void encodeUtf8Component(CharSequence s, int encodingStart, int len) {
        if (encodingStart > 0) {
            this.uriBuilder.append(s, 0, encodingStart);
        }
        encodeUtf8ComponentSlow(s, encodingStart, len);
    }

    private void encodeUtf8ComponentSlow(CharSequence s, int start, int len) {
        int i = start;
        while (i < len) {
            char c = s.charAt(i);
            if (c < 128) {
                if (dontNeedEncoding(c)) {
                    this.uriBuilder.append(c);
                } else {
                    appendEncoded(c);
                }
            } else if (c < 2048) {
                appendEncoded(192 | (c >> 6));
                appendEncoded(128 | (c & '?'));
            } else if (StringUtil.isSurrogate(c)) {
                if (!Character.isHighSurrogate(c)) {
                    appendEncoded(63);
                } else {
                    i++;
                    if (i == s.length()) {
                        appendEncoded(63);
                        return;
                    }
                    writeUtf8Surrogate(c, s.charAt(i));
                }
            } else {
                appendEncoded(224 | (c >> '\f'));
                appendEncoded(128 | ((c >> 6) & 63));
                appendEncoded(128 | (c & '?'));
            }
            i++;
        }
    }

    private void writeUtf8Surrogate(char c, char c2) {
        if (!Character.isLowSurrogate(c2)) {
            appendEncoded(63);
            appendEncoded(Character.isHighSurrogate(c2) ? '?' : c2);
            return;
        }
        int codePoint = Character.toCodePoint(c, c2);
        appendEncoded(240 | (codePoint >> 18));
        appendEncoded(128 | ((codePoint >> 12) & 63));
        appendEncoded(128 | ((codePoint >> 6) & 63));
        appendEncoded(128 | (codePoint & 63));
    }

    private void appendEncoded(int b) {
        this.uriBuilder.append('%').append(forDigit(b >> 4)).append(forDigit(b));
    }

    private static char forDigit(int digit) {
        return CHAR_MAP[digit & 15];
    }

    private static boolean dontNeedEncoding(char ch2) {
        return (ch2 >= 'a' && ch2 <= 'z') || (ch2 >= 'A' && ch2 <= 'Z') || ((ch2 >= '0' && ch2 <= '9') || ch2 == '-' || ch2 == '_' || ch2 == '.' || ch2 == '*');
    }
}

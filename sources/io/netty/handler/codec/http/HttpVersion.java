package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.ObjectUtil;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bouncycastle.i18n.TextBundle;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/HttpVersion.class */
public class HttpVersion implements Comparable<HttpVersion> {
    private static final String HTTP_1_0_STRING = "HTTP/1.0";
    private static final String HTTP_1_1_STRING = "HTTP/1.1";
    private final String protocolName;
    private final int majorVersion;
    private final int minorVersion;
    private final String text;
    private final boolean keepAliveDefault;
    private final byte[] bytes;
    private static final Pattern VERSION_PATTERN = Pattern.compile("(\\S+)/(\\d+)\\.(\\d+)");
    public static final HttpVersion HTTP_1_0 = new HttpVersion("HTTP", 1, 0, false, true);
    public static final HttpVersion HTTP_1_1 = new HttpVersion("HTTP", 1, 1, true, true);

    public static HttpVersion valueOf(String text) {
        ObjectUtil.checkNotNull(text, TextBundle.TEXT_ENTRY);
        String text2 = text.trim();
        if (text2.isEmpty()) {
            throw new IllegalArgumentException("text is empty (possibly HTTP/0.9)");
        }
        HttpVersion version = version0(text2);
        if (version == null) {
            version = new HttpVersion(text2, true);
        }
        return version;
    }

    private static HttpVersion version0(String text) {
        if ("HTTP/1.1".equals(text)) {
            return HTTP_1_1;
        }
        if ("HTTP/1.0".equals(text)) {
            return HTTP_1_0;
        }
        return null;
    }

    public HttpVersion(String text, boolean keepAliveDefault) {
        ObjectUtil.checkNotNull(text, TextBundle.TEXT_ENTRY);
        String text2 = text.trim().toUpperCase();
        if (text2.isEmpty()) {
            throw new IllegalArgumentException("empty text");
        }
        Matcher m = VERSION_PATTERN.matcher(text2);
        if (!m.matches()) {
            throw new IllegalArgumentException("invalid version format: " + text2);
        }
        this.protocolName = m.group(1);
        this.majorVersion = Integer.parseInt(m.group(2));
        this.minorVersion = Integer.parseInt(m.group(3));
        this.text = this.protocolName + '/' + this.majorVersion + '.' + this.minorVersion;
        this.keepAliveDefault = keepAliveDefault;
        this.bytes = null;
    }

    public HttpVersion(String protocolName, int majorVersion, int minorVersion, boolean keepAliveDefault) {
        this(protocolName, majorVersion, minorVersion, keepAliveDefault, false);
    }

    private HttpVersion(String protocolName, int majorVersion, int minorVersion, boolean keepAliveDefault, boolean bytes) {
        ObjectUtil.checkNotNull(protocolName, "protocolName");
        String protocolName2 = protocolName.trim().toUpperCase();
        if (protocolName2.isEmpty()) {
            throw new IllegalArgumentException("empty protocolName");
        }
        for (int i = 0; i < protocolName2.length(); i++) {
            if (Character.isISOControl(protocolName2.charAt(i)) || Character.isWhitespace(protocolName2.charAt(i))) {
                throw new IllegalArgumentException("invalid character in protocolName");
            }
        }
        ObjectUtil.checkPositiveOrZero(majorVersion, "majorVersion");
        ObjectUtil.checkPositiveOrZero(minorVersion, "minorVersion");
        this.protocolName = protocolName2;
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
        this.text = protocolName2 + '/' + majorVersion + '.' + minorVersion;
        this.keepAliveDefault = keepAliveDefault;
        if (bytes) {
            this.bytes = this.text.getBytes(CharsetUtil.US_ASCII);
        } else {
            this.bytes = null;
        }
    }

    public String protocolName() {
        return this.protocolName;
    }

    public int majorVersion() {
        return this.majorVersion;
    }

    public int minorVersion() {
        return this.minorVersion;
    }

    public String text() {
        return this.text;
    }

    public boolean isKeepAliveDefault() {
        return this.keepAliveDefault;
    }

    public String toString() {
        return text();
    }

    public int hashCode() {
        return (((protocolName().hashCode() * 31) + majorVersion()) * 31) + minorVersion();
    }

    public boolean equals(Object o) {
        if (!(o instanceof HttpVersion)) {
            return false;
        }
        HttpVersion that = (HttpVersion) o;
        return minorVersion() == that.minorVersion() && majorVersion() == that.majorVersion() && protocolName().equals(that.protocolName());
    }

    @Override // java.lang.Comparable
    public int compareTo(HttpVersion o) {
        int v = protocolName().compareTo(o.protocolName());
        if (v != 0) {
            return v;
        }
        int v2 = majorVersion() - o.majorVersion();
        if (v2 != 0) {
            return v2;
        }
        return minorVersion() - o.minorVersion();
    }

    void encode(ByteBuf buf) {
        if (this.bytes == null) {
            buf.writeCharSequence(this.text, CharsetUtil.US_ASCII);
        } else {
            buf.writeBytes(this.bytes);
        }
    }
}

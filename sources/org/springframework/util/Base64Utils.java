package org.springframework.util;

import java.nio.charset.Charset;
import java.util.Base64;
import javax.xml.bind.DatatypeConverter;
import org.springframework.lang.UsesJava8;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/Base64Utils.class */
public abstract class Base64Utils {
    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    private static final Base64Delegate delegate;

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/Base64Utils$Base64Delegate.class */
    interface Base64Delegate {
        byte[] encode(byte[] bArr);

        byte[] decode(byte[] bArr);

        byte[] encodeUrlSafe(byte[] bArr);

        byte[] decodeUrlSafe(byte[] bArr);
    }

    static {
        Base64Delegate delegateToUse = null;
        if (ClassUtils.isPresent("java.util.Base64", Base64Utils.class.getClassLoader())) {
            delegateToUse = new JdkBase64Delegate();
        } else if (ClassUtils.isPresent("org.apache.commons.codec.binary.Base64", Base64Utils.class.getClassLoader())) {
            delegateToUse = new CommonsCodecBase64Delegate();
        }
        delegate = delegateToUse;
    }

    private static void assertDelegateAvailable() {
        Assert.state(delegate != null, "Neither Java 8 nor Apache Commons Codec found - Base64 encoding between byte arrays not supported");
    }

    public static byte[] encode(byte[] src) {
        assertDelegateAvailable();
        return delegate.encode(src);
    }

    public static byte[] decode(byte[] src) {
        assertDelegateAvailable();
        return delegate.decode(src);
    }

    public static byte[] encodeUrlSafe(byte[] src) {
        assertDelegateAvailable();
        return delegate.encodeUrlSafe(src);
    }

    public static byte[] decodeUrlSafe(byte[] src) {
        assertDelegateAvailable();
        return delegate.decodeUrlSafe(src);
    }

    public static String encodeToString(byte[] src) {
        if (src == null) {
            return null;
        }
        if (src.length == 0) {
            return "";
        }
        if (delegate != null) {
            return new String(delegate.encode(src), DEFAULT_CHARSET);
        }
        return DatatypeConverter.printBase64Binary(src);
    }

    public static byte[] decodeFromString(String src) {
        if (src == null) {
            return null;
        }
        if (src.isEmpty()) {
            return new byte[0];
        }
        if (delegate != null) {
            return delegate.decode(src.getBytes(DEFAULT_CHARSET));
        }
        return DatatypeConverter.parseBase64Binary(src);
    }

    public static String encodeToUrlSafeString(byte[] src) {
        assertDelegateAvailable();
        return new String(delegate.encodeUrlSafe(src), DEFAULT_CHARSET);
    }

    public static byte[] decodeFromUrlSafeString(String src) {
        assertDelegateAvailable();
        return delegate.decodeUrlSafe(src.getBytes(DEFAULT_CHARSET));
    }

    @UsesJava8
    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/Base64Utils$JdkBase64Delegate.class */
    static class JdkBase64Delegate implements Base64Delegate {
        JdkBase64Delegate() {
        }

        @Override // org.springframework.util.Base64Utils.Base64Delegate
        public byte[] encode(byte[] src) {
            if (src == null || src.length == 0) {
                return src;
            }
            return Base64.getEncoder().encode(src);
        }

        @Override // org.springframework.util.Base64Utils.Base64Delegate
        public byte[] decode(byte[] src) {
            if (src == null || src.length == 0) {
                return src;
            }
            return Base64.getDecoder().decode(src);
        }

        @Override // org.springframework.util.Base64Utils.Base64Delegate
        public byte[] encodeUrlSafe(byte[] src) {
            if (src == null || src.length == 0) {
                return src;
            }
            return Base64.getUrlEncoder().encode(src);
        }

        @Override // org.springframework.util.Base64Utils.Base64Delegate
        public byte[] decodeUrlSafe(byte[] src) {
            if (src == null || src.length == 0) {
                return src;
            }
            return Base64.getUrlDecoder().decode(src);
        }
    }

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/Base64Utils$CommonsCodecBase64Delegate.class */
    static class CommonsCodecBase64Delegate implements Base64Delegate {
        private final org.apache.commons.codec.binary.Base64 base64 = new org.apache.commons.codec.binary.Base64();
        private final org.apache.commons.codec.binary.Base64 base64UrlSafe = new org.apache.commons.codec.binary.Base64(0, null, true);

        CommonsCodecBase64Delegate() {
        }

        @Override // org.springframework.util.Base64Utils.Base64Delegate
        public byte[] encode(byte[] src) {
            return this.base64.encode(src);
        }

        @Override // org.springframework.util.Base64Utils.Base64Delegate
        public byte[] decode(byte[] src) {
            return this.base64.decode(src);
        }

        @Override // org.springframework.util.Base64Utils.Base64Delegate
        public byte[] encodeUrlSafe(byte[] src) {
            return this.base64UrlSafe.encode(src);
        }

        @Override // org.springframework.util.Base64Utils.Base64Delegate
        public byte[] decodeUrlSafe(byte[] src) {
            return this.base64UrlSafe.decode(src);
        }
    }
}

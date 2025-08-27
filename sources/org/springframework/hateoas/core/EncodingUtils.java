package org.springframework.hateoas.core;

import java.io.UnsupportedEncodingException;
import org.springframework.util.Assert;
import org.springframework.web.util.UriUtils;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/core/EncodingUtils.class */
public final class EncodingUtils {
    private static final String ENCODING = "UTF-8";

    private EncodingUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static String encodePath(Object source) {
        Assert.notNull(source, "Path value must not be null!");
        try {
            return UriUtils.encodePath(source.toString(), "UTF-8");
        } catch (UnsupportedEncodingException o_O) {
            throw new IllegalStateException(o_O);
        }
    }

    public static String encodeParameter(Object source) {
        Assert.notNull(source, "Request parameter value must not be null!");
        try {
            return UriUtils.encodeQueryParam(source.toString(), "UTF-8");
        } catch (UnsupportedEncodingException o_O) {
            throw new IllegalStateException(o_O);
        }
    }

    public static String encodeFragment(Object source) {
        Assert.notNull(source, "Fragment value must not be null!");
        try {
            return UriUtils.encodeFragment(source.toString(), "UTF-8");
        } catch (UnsupportedEncodingException o_O) {
            throw new IllegalStateException(o_O);
        }
    }
}

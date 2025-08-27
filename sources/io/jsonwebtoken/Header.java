package io.jsonwebtoken;

import io.jsonwebtoken.Header;
import java.util.Map;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/Header.class */
public interface Header<T extends Header<T>> extends Map<String, Object> {
    public static final String JWT_TYPE = "JWT";
    public static final String TYPE = "typ";
    public static final String CONTENT_TYPE = "cty";
    public static final String COMPRESSION_ALGORITHM = "calg";

    String getType();

    T setType(String str);

    String getContentType();

    T setContentType(String str);

    String getCompressionAlgorithm();

    T setCompressionAlgorithm(String str);
}

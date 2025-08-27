package org.springframework.web.util;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import org.springframework.util.Assert;
import org.springframework.web.util.HierarchicalUriComponents;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/util/UriUtils.class */
public abstract class UriUtils {
    public static String encodeScheme(String scheme, String encoding) throws UnsupportedEncodingException {
        return HierarchicalUriComponents.encodeUriComponent(scheme, encoding, HierarchicalUriComponents.Type.SCHEME);
    }

    public static String encodeAuthority(String authority, String encoding) throws UnsupportedEncodingException {
        return HierarchicalUriComponents.encodeUriComponent(authority, encoding, HierarchicalUriComponents.Type.AUTHORITY);
    }

    public static String encodeUserInfo(String userInfo, String encoding) throws UnsupportedEncodingException {
        return HierarchicalUriComponents.encodeUriComponent(userInfo, encoding, HierarchicalUriComponents.Type.USER_INFO);
    }

    public static String encodeHost(String host, String encoding) throws UnsupportedEncodingException {
        return HierarchicalUriComponents.encodeUriComponent(host, encoding, HierarchicalUriComponents.Type.HOST_IPV4);
    }

    public static String encodePort(String port, String encoding) throws UnsupportedEncodingException {
        return HierarchicalUriComponents.encodeUriComponent(port, encoding, HierarchicalUriComponents.Type.PORT);
    }

    public static String encodePath(String path, String encoding) throws UnsupportedEncodingException {
        return HierarchicalUriComponents.encodeUriComponent(path, encoding, HierarchicalUriComponents.Type.PATH);
    }

    public static String encodePathSegment(String segment, String encoding) throws UnsupportedEncodingException {
        return HierarchicalUriComponents.encodeUriComponent(segment, encoding, HierarchicalUriComponents.Type.PATH_SEGMENT);
    }

    public static String encodeQuery(String query, String encoding) throws UnsupportedEncodingException {
        return HierarchicalUriComponents.encodeUriComponent(query, encoding, HierarchicalUriComponents.Type.QUERY);
    }

    public static String encodeQueryParam(String queryParam, String encoding) throws UnsupportedEncodingException {
        return HierarchicalUriComponents.encodeUriComponent(queryParam, encoding, HierarchicalUriComponents.Type.QUERY_PARAM);
    }

    public static String encodeFragment(String fragment, String encoding) throws UnsupportedEncodingException {
        return HierarchicalUriComponents.encodeUriComponent(fragment, encoding, HierarchicalUriComponents.Type.FRAGMENT);
    }

    public static String encode(String source, String encoding) throws UnsupportedEncodingException {
        HierarchicalUriComponents.Type type = HierarchicalUriComponents.Type.URI;
        return HierarchicalUriComponents.encodeUriComponent(source, encoding, type);
    }

    public static String decode(String source, String encoding) throws UnsupportedEncodingException {
        if (source == null) {
            return null;
        }
        Assert.hasLength(encoding, "Encoding must not be empty");
        int length = source.length();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(length);
        boolean changed = false;
        int i = 0;
        while (i < length) {
            int ch2 = source.charAt(i);
            if (ch2 == 37) {
                if (i + 2 < length) {
                    char hex1 = source.charAt(i + 1);
                    char hex2 = source.charAt(i + 2);
                    int u = Character.digit(hex1, 16);
                    int l = Character.digit(hex2, 16);
                    if (u == -1 || l == -1) {
                        throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + SymbolConstants.QUOTES_SYMBOL);
                    }
                    bos.write((char) ((u << 4) + l));
                    i += 2;
                    changed = true;
                } else {
                    throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + SymbolConstants.QUOTES_SYMBOL);
                }
            } else {
                bos.write(ch2);
            }
            i++;
        }
        return changed ? new String(bos.toByteArray(), encoding) : source;
    }

    public static String extractFileExtension(String path) {
        int end = path.indexOf(63);
        int fragmentIndex = path.indexOf(35);
        if (fragmentIndex != -1 && (end == -1 || fragmentIndex < end)) {
            end = fragmentIndex;
        }
        if (end == -1) {
            end = path.length();
        }
        int begin = path.lastIndexOf(47, end) + 1;
        int paramIndex = path.indexOf(59, begin);
        int end2 = (paramIndex == -1 || paramIndex >= end) ? end : paramIndex;
        int extIndex = path.lastIndexOf(46, end2);
        if (extIndex != -1 && extIndex > begin) {
            return path.substring(extIndex + 1, end2);
        }
        return null;
    }
}

package io.netty.util.internal;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/internal/ResourcesUtil.class */
public final class ResourcesUtil {
    public static File getFile(Class resourceClass, String fileName) {
        try {
            return new File(URLDecoder.decode(resourceClass.getResource(fileName).getFile(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return new File(resourceClass.getResource(fileName).getFile());
        }
    }

    private ResourcesUtil() {
    }
}

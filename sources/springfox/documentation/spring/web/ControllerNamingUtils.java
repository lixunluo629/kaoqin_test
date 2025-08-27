package springfox.documentation.spring.web;

import java.io.UnsupportedEncodingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.util.UriUtils;

/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/ControllerNamingUtils.class */
public class ControllerNamingUtils {
    private static Logger log = LoggerFactory.getLogger((Class<?>) ControllerNamingUtils.class);
    private static final String ISO_8859_1 = "ISO-8859-1";

    public static String pathRoot(String requestPattern) {
        Assert.notNull(requestPattern);
        Assert.hasText(requestPattern);
        log.info("Resolving path root for {}", requestPattern);
        String requestPattern2 = requestPattern.startsWith("/") ? requestPattern : "/" + requestPattern;
        int idx = requestPattern2.indexOf("/", 1);
        if (idx > -1) {
            return requestPattern2.substring(0, idx);
        }
        return requestPattern2;
    }

    public static String pathRootEncoded(String requestPattern) {
        return encode(pathRoot(requestPattern));
    }

    public static String encode(String path) {
        try {
            path = UriUtils.encodePath(path, "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            log.error("Could not encode:" + path, (Throwable) e);
        }
        return path;
    }

    public static String decode(String path) {
        try {
            path = UriUtils.decode(path, "ISO-8859-1");
        } catch (Exception e) {
            log.error("Could not decode:" + path, (Throwable) e);
        }
        return path;
    }
}

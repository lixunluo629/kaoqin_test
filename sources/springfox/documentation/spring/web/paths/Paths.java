package springfox.documentation.spring.web.paths;

import com.google.common.base.Strings;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/paths/Paths.class */
public class Paths {
    private static final Pattern FIRST_PATH_FRAGMENT_REGEX = Pattern.compile("^([/]?[\\w\\-\\.]+[/]?)");

    private Paths() {
        throw new UnsupportedOperationException();
    }

    public static String splitCamelCase(String s, String separator) {
        if (Strings.isNullOrEmpty(s)) {
            return "";
        }
        return s.replaceAll(String.format("%s|%s|%s", "(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])", "(?<=[A-Za-z])(?=[^A-Za-z])"), separator);
    }

    public static String maybeChompLeadingSlash(String path) {
        if (Strings.isNullOrEmpty(path) || !path.startsWith("/")) {
            return path;
        }
        return path.replaceFirst("^/", "");
    }

    public static String maybeChompTrailingSlash(String path) {
        if (Strings.isNullOrEmpty(path) || !path.endsWith("/")) {
            return path;
        }
        return path.replaceFirst("/$", "");
    }

    public static String firstPathSegment(String path) {
        if (Strings.isNullOrEmpty(path)) {
            return path;
        }
        Matcher matcher = FIRST_PATH_FRAGMENT_REGEX.matcher(path);
        if (matcher.find()) {
            return maybeChompTrailingSlash(matcher.group());
        }
        return path;
    }

    public static String sanitizeRequestMappingPattern(String requestMappingPattern) {
        String result = requestMappingPattern.replaceAll("\\{([^}]*?):.*?\\}", "{$1}");
        return result.isEmpty() ? "/" : result;
    }

    public static String removeAdjacentForwardSlashes(String candidate) {
        return candidate.replaceAll("(?<!(http:|https:))//", "/");
    }
}

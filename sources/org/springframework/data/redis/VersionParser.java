package org.springframework.data.redis;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/VersionParser.class */
public class VersionParser {
    private static final Pattern VERSION_MATCHER = Pattern.compile("([0-9]+)\\.([0-9]+)(\\.([0-9]+))?(.*)");

    public static Version parseVersion(String version) {
        if (version == null) {
            return Version.UNKNOWN;
        }
        Matcher matcher = VERSION_MATCHER.matcher(version);
        if (matcher.matches()) {
            String major = matcher.group(1);
            String minor = matcher.group(2);
            String patch = matcher.group(4);
            return new Version(Integer.parseInt(major), minor != null ? Integer.parseInt(minor) : 0, patch != null ? Integer.parseInt(patch) : 0);
        }
        return Version.UNKNOWN;
    }
}

package org.springframework.data.redis.connection.jedis;

import java.io.IOException;
import java.util.Properties;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.data.redis.Version;
import org.springframework.data.redis.VersionParser;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/jedis/JedisVersionUtil.class */
public class JedisVersionUtil {
    private static Version jedisVersion = parseVersion(resolveJedisVersion());

    public static Version jedisVersion() {
        return jedisVersion;
    }

    static Version parseVersion(String version) {
        return VersionParser.parseVersion(version);
    }

    public static boolean atLeastJedis24() {
        return atLeast("2.4");
    }

    private static String resolveJedisVersion() {
        String version = Jedis.class.getPackage().getImplementationVersion();
        if (!StringUtils.hasText(version)) {
            try {
                Properties props = PropertiesLoaderUtils.loadAllProperties("META-INF/maven/redis.clients/jedis/pom.properties");
                if (props.containsKey("version")) {
                    version = props.getProperty("version");
                }
            } catch (IOException e) {
            }
        }
        return version;
    }

    public static boolean atLeast(String version) {
        return jedisVersion.compareTo(parseVersion(version)) >= 0;
    }

    public static boolean atMost(String version) {
        return jedisVersion.compareTo(parseVersion(version)) <= 0;
    }
}

package org.springframework.boot.logging;

import java.util.Properties;
import org.springframework.core.env.PropertyResolver;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/logging/LogFile.class */
public class LogFile {
    public static final String FILE_PROPERTY = "logging.file";
    public static final String PATH_PROPERTY = "logging.path";
    private final String file;
    private final String path;

    LogFile(String file) {
        this(file, null);
    }

    LogFile(String file, String path) {
        Assert.isTrue(StringUtils.hasLength(file) || StringUtils.hasLength(path), "File or Path must not be empty");
        this.file = file;
        this.path = path;
    }

    public void applyToSystemProperties() {
        applyTo(System.getProperties());
    }

    public void applyTo(Properties properties) {
        put(properties, LoggingApplicationListener.LOG_PATH, this.path);
        put(properties, LoggingApplicationListener.LOG_FILE, toString());
    }

    private void put(Properties properties, String key, String value) {
        if (StringUtils.hasLength(value)) {
            properties.put(key, value);
        }
    }

    public String toString() {
        if (StringUtils.hasLength(this.file)) {
            return this.file;
        }
        String path = this.path;
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        return StringUtils.applyRelativePath(path, "spring.log");
    }

    public static LogFile get(PropertyResolver propertyResolver) {
        String file = propertyResolver.getProperty("logging.file");
        String path = propertyResolver.getProperty("logging.path");
        if (StringUtils.hasLength(file) || StringUtils.hasLength(path)) {
            return new LogFile(file, path);
        }
        return null;
    }
}

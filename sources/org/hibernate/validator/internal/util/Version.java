package org.hibernate.validator.internal.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/util/Version.class */
public final class Version {
    private static final Pattern JAVA_VERSION_PATTERN = Pattern.compile("^(?:1\\.)?(\\d+)$");
    private static Log LOG = LoggerFactory.make();
    private static int JAVA_RELEASE = determineJavaRelease(System.getProperty("java.specification.version"));

    static {
        LOG.version(getVersionString());
    }

    public static String getVersionString() {
        return Version.class.getPackage().getImplementationVersion();
    }

    public static void touch() {
    }

    public static int getJavaRelease() {
        return JAVA_RELEASE;
    }

    public static int determineJavaRelease(String specificationVersion) {
        if (specificationVersion != null && !specificationVersion.trim().isEmpty()) {
            Matcher matcher = JAVA_VERSION_PATTERN.matcher(specificationVersion);
            if (matcher.find()) {
                return Integer.valueOf(matcher.group(1)).intValue();
            }
        }
        LOG.unknownJvmVersion(specificationVersion);
        return 6;
    }

    private Version() {
    }
}

package org.springframework.boot.logging;

import org.springframework.boot.ApplicationPid;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.core.env.Environment;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/logging/LoggingSystemProperties.class */
class LoggingSystemProperties {
    static final String PID_KEY = "PID";
    static final String EXCEPTION_CONVERSION_WORD = "LOG_EXCEPTION_CONVERSION_WORD";
    static final String CONSOLE_LOG_PATTERN = "CONSOLE_LOG_PATTERN";
    static final String FILE_LOG_PATTERN = "FILE_LOG_PATTERN";
    static final String LOG_LEVEL_PATTERN = "LOG_LEVEL_PATTERN";
    private final Environment environment;

    LoggingSystemProperties(Environment environment) {
        this.environment = environment;
    }

    public void apply() {
        apply(null);
    }

    public void apply(LogFile logFile) {
        RelaxedPropertyResolver propertyResolver = RelaxedPropertyResolver.ignoringUnresolvableNestedPlaceholders(this.environment, "logging.");
        setSystemProperty(propertyResolver, "LOG_EXCEPTION_CONVERSION_WORD", "exception-conversion-word");
        setSystemProperty("PID", new ApplicationPid().toString());
        setSystemProperty(propertyResolver, "CONSOLE_LOG_PATTERN", "pattern.console");
        setSystemProperty(propertyResolver, "FILE_LOG_PATTERN", "pattern.file");
        setSystemProperty(propertyResolver, "LOG_LEVEL_PATTERN", "pattern.level");
        if (logFile != null) {
            logFile.applyToSystemProperties();
        }
    }

    private void setSystemProperty(RelaxedPropertyResolver propertyResolver, String systemPropertyName, String propertyName) {
        setSystemProperty(systemPropertyName, propertyResolver.getProperty(propertyName));
    }

    private void setSystemProperty(String name, String value) {
        if (System.getProperty(name) == null && value != null) {
            System.setProperty(name, value);
        }
    }
}

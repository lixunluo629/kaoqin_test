package org.springframework.boot.logging.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.rolling.FixedWindowRollingPolicy;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy;
import ch.qos.logback.core.util.FileSize;
import ch.qos.logback.core.util.OptionHelper;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.logging.LogFile;
import org.springframework.boot.logging.LoggingInitializationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.env.PropertySourcesPropertyResolver;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/logging/logback/DefaultLogbackConfiguration.class */
class DefaultLogbackConfiguration {
    private static final String CONSOLE_LOG_PATTERN = "%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} %clr(${LOG_LEVEL_PATTERN:-%5p}) %clr(${PID:- }){magenta} %clr(---){faint} %clr([%15.15t]){faint} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}";
    private static final String FILE_LOG_PATTERN = "%d{yyyy-MM-dd HH:mm:ss.SSS} ${LOG_LEVEL_PATTERN:-%5p} ${PID:- } --- [%t] %-40.40logger{39} : %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}";
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private final PropertyResolver patterns;
    private final LogFile logFile;

    DefaultLogbackConfiguration(LoggingInitializationContext initializationContext, LogFile logFile) {
        this.patterns = getPatternsResolver(initializationContext.getEnvironment());
        this.logFile = logFile;
    }

    private PropertyResolver getPatternsResolver(Environment environment) {
        if (environment == null) {
            return new PropertySourcesPropertyResolver(null);
        }
        return RelaxedPropertyResolver.ignoringUnresolvableNestedPlaceholders(environment, "logging.pattern.");
    }

    public void apply(LogbackConfigurator config) {
        synchronized (config.getConfigurationLock()) {
            base(config);
            Appender<ILoggingEvent> consoleAppender = consoleAppender(config);
            if (this.logFile != null) {
                Appender<ILoggingEvent> fileAppender = fileAppender(config, this.logFile.toString());
                config.root(Level.INFO, consoleAppender, fileAppender);
            } else {
                config.root(Level.INFO, consoleAppender);
            }
        }
    }

    private void base(LogbackConfigurator config) {
        config.conversionRule("clr", ColorConverter.class);
        config.conversionRule("wex", WhitespaceThrowableProxyConverter.class);
        config.conversionRule("wEx", ExtendedWhitespaceThrowableProxyConverter.class);
        LevelRemappingAppender debugRemapAppender = new LevelRemappingAppender("org.springframework.boot");
        config.start(debugRemapAppender);
        config.appender("DEBUG_LEVEL_REMAPPER", debugRemapAppender);
        config.logger("org.apache.catalina.startup.DigesterFactory", Level.ERROR);
        config.logger("org.apache.catalina.util.LifecycleBase", Level.ERROR);
        config.logger(TomcatEmbeddedServletContainerFactory.DEFAULT_PROTOCOL, Level.WARN);
        config.logger("org.apache.sshd.common.util.SecurityUtils", Level.WARN);
        config.logger("org.apache.tomcat.util.net.NioSelectorPool", Level.WARN);
        config.logger("org.crsh.plugin", Level.WARN);
        config.logger("org.crsh.ssh", Level.WARN);
        config.logger("org.eclipse.jetty.util.component.AbstractLifeCycle", Level.ERROR);
        config.logger("org.hibernate.validator.internal.util.Version", Level.WARN);
        config.logger("org.springframework.boot.actuate.autoconfigure.CrshAutoConfiguration", Level.WARN);
        config.logger("org.springframework.boot.actuate.endpoint.jmx", null, false, debugRemapAppender);
        config.logger("org.thymeleaf", null, false, debugRemapAppender);
    }

    private Appender<ILoggingEvent> consoleAppender(LogbackConfigurator config) {
        ConsoleAppender<ILoggingEvent> appender = new ConsoleAppender<>();
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        String logPattern = this.patterns.getProperty("console", CONSOLE_LOG_PATTERN);
        encoder.setPattern(OptionHelper.substVars(logPattern, config.getContext()));
        encoder.setCharset(UTF8);
        config.start(encoder);
        appender.setEncoder(encoder);
        config.appender("CONSOLE", appender);
        return appender;
    }

    private Appender<ILoggingEvent> fileAppender(LogbackConfigurator config, String logFile) {
        RollingFileAppender<ILoggingEvent> appender = new RollingFileAppender<>();
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        String logPattern = this.patterns.getProperty("file", FILE_LOG_PATTERN);
        encoder.setPattern(OptionHelper.substVars(logPattern, config.getContext()));
        appender.setEncoder(encoder);
        config.start(encoder);
        appender.setFile(logFile);
        setRollingPolicy(appender, config, logFile);
        setMaxFileSize(appender, config);
        config.appender("FILE", appender);
        return appender;
    }

    private void setRollingPolicy(RollingFileAppender<ILoggingEvent> appender, LogbackConfigurator config, String logFile) {
        FixedWindowRollingPolicy rollingPolicy = new FixedWindowRollingPolicy();
        rollingPolicy.setFileNamePattern(logFile + ".%i");
        appender.setRollingPolicy(rollingPolicy);
        rollingPolicy.setParent(appender);
        config.start(rollingPolicy);
    }

    private void setMaxFileSize(RollingFileAppender<ILoggingEvent> appender, LogbackConfigurator config) {
        SizeBasedTriggeringPolicy<ILoggingEvent> triggeringPolicy = new SizeBasedTriggeringPolicy<>();
        try {
            triggeringPolicy.setMaxFileSize(FileSize.valueOf("10MB"));
        } catch (NoSuchMethodError e) {
            Method method = ReflectionUtils.findMethod(SizeBasedTriggeringPolicy.class, "setMaxFileSize", String.class);
            ReflectionUtils.invokeMethod(method, triggeringPolicy, "10MB");
        }
        appender.setTriggeringPolicy(triggeringPolicy);
        config.start(triggeringPolicy);
    }
}

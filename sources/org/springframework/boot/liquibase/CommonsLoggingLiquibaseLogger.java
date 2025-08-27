package org.springframework.boot.liquibase;

import liquibase.logging.LogLevel;
import liquibase.logging.core.AbstractLogger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/liquibase/CommonsLoggingLiquibaseLogger.class */
public class CommonsLoggingLiquibaseLogger extends AbstractLogger {
    public static final int PRIORITY = 10;
    private Log logger;

    public void setName(String name) {
        this.logger = createLogger(name);
    }

    protected Log createLogger(String name) {
        return LogFactory.getLog(name);
    }

    public void setLogLevel(String logLevel, String logFile) {
        super.setLogLevel(logLevel);
    }

    public void severe(String message) {
        if (isEnabled(LogLevel.SEVERE)) {
            this.logger.error(buildMessage(message));
        }
    }

    public void severe(String message, Throwable e) {
        if (isEnabled(LogLevel.SEVERE)) {
            this.logger.error(buildMessage(message), e);
        }
    }

    public void warning(String message) {
        if (isEnabled(LogLevel.WARNING)) {
            this.logger.warn(buildMessage(message));
        }
    }

    public void warning(String message, Throwable e) {
        if (isEnabled(LogLevel.WARNING)) {
            this.logger.warn(buildMessage(message), e);
        }
    }

    public void info(String message) {
        if (isEnabled(LogLevel.INFO)) {
            this.logger.info(buildMessage(message));
        }
    }

    public void info(String message, Throwable e) {
        if (isEnabled(LogLevel.INFO)) {
            this.logger.info(buildMessage(message), e);
        }
    }

    public void debug(String message) {
        if (isEnabled(LogLevel.DEBUG)) {
            this.logger.debug(buildMessage(message));
        }
    }

    public void debug(String message, Throwable e) {
        if (isEnabled(LogLevel.DEBUG)) {
            this.logger.debug(buildMessage(message), e);
        }
    }

    public int getPriority() {
        return 10;
    }

    /* renamed from: org.springframework.boot.liquibase.CommonsLoggingLiquibaseLogger$1, reason: invalid class name */
    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/liquibase/CommonsLoggingLiquibaseLogger$1.class */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$liquibase$logging$LogLevel = new int[LogLevel.values().length];

        static {
            try {
                $SwitchMap$liquibase$logging$LogLevel[LogLevel.DEBUG.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$liquibase$logging$LogLevel[LogLevel.INFO.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$liquibase$logging$LogLevel[LogLevel.WARNING.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$liquibase$logging$LogLevel[LogLevel.SEVERE.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    private boolean isEnabled(LogLevel level) {
        if (this.logger != null) {
            switch (AnonymousClass1.$SwitchMap$liquibase$logging$LogLevel[level.ordinal()]) {
                case 1:
                    return this.logger.isDebugEnabled();
                case 2:
                    return this.logger.isInfoEnabled();
                case 3:
                    return this.logger.isWarnEnabled();
                case 4:
                    return this.logger.isErrorEnabled();
                default:
                    return false;
            }
        }
        return false;
    }
}

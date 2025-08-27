package org.springframework.boot.logging.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.LoggerContextVO;
import ch.qos.logback.core.AppenderBase;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Marker;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/logging/logback/LevelRemappingAppender.class */
public class LevelRemappingAppender extends AppenderBase<ILoggingEvent> {
    private static final Map<Level, Level> DEFAULT_REMAPS = Collections.singletonMap(Level.INFO, Level.DEBUG);
    private String destinationLogger;
    private Map<Level, Level> remapLevels;

    public LevelRemappingAppender() {
        this.destinationLogger = "ROOT";
        this.remapLevels = DEFAULT_REMAPS;
    }

    public LevelRemappingAppender(String destinationLogger) {
        this.destinationLogger = "ROOT";
        this.remapLevels = DEFAULT_REMAPS;
        this.destinationLogger = destinationLogger;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // ch.qos.logback.core.AppenderBase
    public void append(ILoggingEvent event) {
        AppendableLogger logger = getLogger(this.destinationLogger);
        Level remapped = this.remapLevels.get(event.getLevel());
        logger.callAppenders(remapped != null ? new RemappedLoggingEvent(event) : event);
    }

    protected AppendableLogger getLogger(String name) {
        LoggerContext loggerContext = (LoggerContext) this.context;
        return new AppendableLogger(loggerContext.getLogger(name));
    }

    public void setDestinationLogger(String destinationLogger) {
        Assert.hasLength(destinationLogger, "DestinationLogger must not be empty");
        this.destinationLogger = destinationLogger;
    }

    public void setRemapLevels(String remapLevels) {
        Assert.hasLength(remapLevels, "RemapLevels must not be empty");
        this.remapLevels = new HashMap();
        for (String remap : StringUtils.commaDelimitedListToStringArray(remapLevels)) {
            String[] split = StringUtils.split(remap, "->");
            Assert.notNull(split, "Remap element '" + remap + "' must contain '->'");
            this.remapLevels.put(Level.toLevel(split[0]), Level.toLevel(split[1]));
        }
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/logging/logback/LevelRemappingAppender$AppendableLogger.class */
    protected static class AppendableLogger {
        private Logger logger;

        public AppendableLogger(Logger logger) {
            this.logger = logger;
        }

        public void callAppenders(ILoggingEvent event) {
            if (this.logger.isEnabledFor(event.getLevel())) {
                this.logger.callAppenders(event);
            }
        }
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/logging/logback/LevelRemappingAppender$RemappedLoggingEvent.class */
    private class RemappedLoggingEvent implements ILoggingEvent {
        private final ILoggingEvent event;

        RemappedLoggingEvent(ILoggingEvent event) {
            this.event = event;
        }

        @Override // ch.qos.logback.classic.spi.ILoggingEvent
        public String getThreadName() {
            return this.event.getThreadName();
        }

        @Override // ch.qos.logback.classic.spi.ILoggingEvent
        public Level getLevel() {
            Level remappedLevel = (Level) LevelRemappingAppender.this.remapLevels.get(this.event.getLevel());
            return remappedLevel != null ? remappedLevel : this.event.getLevel();
        }

        @Override // ch.qos.logback.classic.spi.ILoggingEvent
        public String getMessage() {
            return this.event.getMessage();
        }

        @Override // ch.qos.logback.classic.spi.ILoggingEvent
        public Object[] getArgumentArray() {
            return this.event.getArgumentArray();
        }

        @Override // ch.qos.logback.classic.spi.ILoggingEvent
        public String getFormattedMessage() {
            return this.event.getFormattedMessage();
        }

        @Override // ch.qos.logback.classic.spi.ILoggingEvent
        public String getLoggerName() {
            return this.event.getLoggerName();
        }

        @Override // ch.qos.logback.classic.spi.ILoggingEvent
        public LoggerContextVO getLoggerContextVO() {
            return this.event.getLoggerContextVO();
        }

        @Override // ch.qos.logback.classic.spi.ILoggingEvent
        public IThrowableProxy getThrowableProxy() {
            return this.event.getThrowableProxy();
        }

        @Override // ch.qos.logback.classic.spi.ILoggingEvent
        public StackTraceElement[] getCallerData() {
            return this.event.getCallerData();
        }

        @Override // ch.qos.logback.classic.spi.ILoggingEvent
        public boolean hasCallerData() {
            return this.event.hasCallerData();
        }

        @Override // ch.qos.logback.classic.spi.ILoggingEvent
        public Marker getMarker() {
            return this.event.getMarker();
        }

        @Override // ch.qos.logback.classic.spi.ILoggingEvent
        public Map<String, String> getMDCPropertyMap() {
            return this.event.getMDCPropertyMap();
        }

        @Override // ch.qos.logback.classic.spi.ILoggingEvent
        @Deprecated
        public Map<String, String> getMdc() {
            return this.event.getMDCPropertyMap();
        }

        @Override // ch.qos.logback.classic.spi.ILoggingEvent
        public long getTimeStamp() {
            return this.event.getTimeStamp();
        }

        @Override // ch.qos.logback.classic.spi.ILoggingEvent, ch.qos.logback.core.spi.DeferredProcessingAware
        public void prepareForDeferredProcessing() {
            this.event.prepareForDeferredProcessing();
        }
    }
}

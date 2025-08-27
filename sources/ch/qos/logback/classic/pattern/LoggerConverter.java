package ch.qos.logback.classic.pattern;

import ch.qos.logback.classic.spi.ILoggingEvent;

/* JADX WARN: Classes with same name are omitted:
  logback-classic-1.2.3.jar:ch/qos/logback/classic/pattern/LoggerConverter.class
 */
/* loaded from: logback-classic-1.2.3.jar.bak:ch/qos/logback/classic/pattern/LoggerConverter.class */
public class LoggerConverter extends NamedConverter {
    @Override // ch.qos.logback.classic.pattern.NamedConverter
    protected String getFullyQualifiedName(ILoggingEvent event) {
        return event.getLoggerName();
    }
}

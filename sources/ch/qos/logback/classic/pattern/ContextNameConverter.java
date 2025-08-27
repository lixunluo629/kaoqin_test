package ch.qos.logback.classic.pattern;

import ch.qos.logback.classic.spi.ILoggingEvent;

/* JADX WARN: Classes with same name are omitted:
  logback-classic-1.2.3.jar:ch/qos/logback/classic/pattern/ContextNameConverter.class
 */
/* loaded from: logback-classic-1.2.3.jar.bak:ch/qos/logback/classic/pattern/ContextNameConverter.class */
public class ContextNameConverter extends ClassicConverter {
    @Override // ch.qos.logback.core.pattern.Converter
    public String convert(ILoggingEvent event) {
        return event.getLoggerContextVO().getName();
    }
}

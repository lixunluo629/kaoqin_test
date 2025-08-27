package ch.qos.logback.classic.pattern;

import ch.qos.logback.classic.spi.ILoggingEvent;

/* JADX WARN: Classes with same name are omitted:
  logback-classic-1.2.3.jar:ch/qos/logback/classic/pattern/MethodOfCallerConverter.class
 */
/* loaded from: logback-classic-1.2.3.jar.bak:ch/qos/logback/classic/pattern/MethodOfCallerConverter.class */
public class MethodOfCallerConverter extends ClassicConverter {
    @Override // ch.qos.logback.core.pattern.Converter
    public String convert(ILoggingEvent le) {
        StackTraceElement[] cda = le.getCallerData();
        if (cda != null && cda.length > 0) {
            return cda[0].getMethodName();
        }
        return "?";
    }
}

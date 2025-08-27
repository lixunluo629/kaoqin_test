package org.jboss.logging;

/* loaded from: jboss-logging-3.3.2.Final.jar:org/jboss/logging/JDKLoggerProvider.class */
final class JDKLoggerProvider extends AbstractMdcLoggerProvider implements LoggerProvider {
    JDKLoggerProvider() {
    }

    @Override // org.jboss.logging.LoggerProvider
    public Logger getLogger(String name) {
        return new JDKLogger(name);
    }
}

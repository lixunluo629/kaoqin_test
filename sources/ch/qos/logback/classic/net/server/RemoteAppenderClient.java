package ch.qos.logback.classic.net.server;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.net.server.Client;

/* JADX WARN: Classes with same name are omitted:
  logback-classic-1.2.3.jar:ch/qos/logback/classic/net/server/RemoteAppenderClient.class
 */
/* loaded from: logback-classic-1.2.3.jar.bak:ch/qos/logback/classic/net/server/RemoteAppenderClient.class */
interface RemoteAppenderClient extends Client {
    void setLoggerContext(LoggerContext loggerContext);
}

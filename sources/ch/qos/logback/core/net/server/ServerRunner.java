package ch.qos.logback.core.net.server;

import ch.qos.logback.core.net.server.Client;
import ch.qos.logback.core.spi.ContextAware;
import java.io.IOException;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/net/server/ServerRunner.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/net/server/ServerRunner.class */
public interface ServerRunner<T extends Client> extends ContextAware, Runnable {
    boolean isRunning();

    void stop() throws IOException;

    void accept(ClientVisitor<T> clientVisitor);
}

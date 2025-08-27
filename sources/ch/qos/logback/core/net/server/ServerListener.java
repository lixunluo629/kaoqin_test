package ch.qos.logback.core.net.server;

import ch.qos.logback.core.net.server.Client;
import java.io.Closeable;
import java.io.IOException;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/net/server/ServerListener.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/net/server/ServerListener.class */
public interface ServerListener<T extends Client> extends Closeable {
    T acceptClient() throws InterruptedException, IOException;

    @Override // java.io.Closeable, java.lang.AutoCloseable
    void close();
}

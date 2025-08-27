package ch.qos.logback.core.net.server;

import java.io.Closeable;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/net/server/Client.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/net/server/Client.class */
public interface Client extends Runnable, Closeable {
    void close();
}

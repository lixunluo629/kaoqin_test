package ch.qos.logback.core.net.server;

import ch.qos.logback.core.net.server.Client;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/net/server/ClientVisitor.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/net/server/ClientVisitor.class */
public interface ClientVisitor<T extends Client> {
    void visit(T t);
}

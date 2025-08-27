package ch.qos.logback.core.net;

import java.util.concurrent.LinkedBlockingDeque;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/net/QueueFactory.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/net/QueueFactory.class */
public class QueueFactory {
    public <E> LinkedBlockingDeque<E> newLinkedBlockingDeque(int capacity) {
        int actualCapacity = capacity < 1 ? 1 : capacity;
        return new LinkedBlockingDeque<>(actualCapacity);
    }
}

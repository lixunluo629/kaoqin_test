package ch.qos.logback.core.status;

import java.util.List;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/status/StatusManager.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/status/StatusManager.class */
public interface StatusManager {
    void add(Status status);

    List<Status> getCopyOfStatusList();

    int getCount();

    boolean add(StatusListener statusListener);

    void remove(StatusListener statusListener);

    void clear();

    List<StatusListener> getCopyOfStatusListenerList();
}

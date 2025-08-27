package ch.qos.logback.core.status;

import java.util.ArrayList;
import java.util.List;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/status/StatusListenerAsList.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/status/StatusListenerAsList.class */
public class StatusListenerAsList implements StatusListener {
    List<Status> statusList = new ArrayList();

    @Override // ch.qos.logback.core.status.StatusListener
    public void addStatusEvent(Status status) {
        this.statusList.add(status);
    }

    public List<Status> getStatusList() {
        return this.statusList;
    }
}

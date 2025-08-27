package ch.qos.logback.core.joran.conditional;

import ch.qos.logback.core.joran.event.InPlayListener;
import ch.qos.logback.core.joran.event.SaxEvent;
import java.util.ArrayList;
import java.util.List;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/joran/conditional/ThenActionState.class
 */
/* compiled from: ThenOrElseActionBase.java */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/joran/conditional/ThenActionState.class */
class ThenActionState implements InPlayListener {
    List<SaxEvent> eventList = new ArrayList();
    boolean isRegistered = false;

    ThenActionState() {
    }

    @Override // ch.qos.logback.core.joran.event.InPlayListener
    public void inPlay(SaxEvent event) {
        this.eventList.add(event);
    }
}

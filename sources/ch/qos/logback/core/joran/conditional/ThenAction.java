package ch.qos.logback.core.joran.conditional;

import ch.qos.logback.core.joran.event.SaxEvent;
import java.util.List;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/joran/conditional/ThenAction.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/joran/conditional/ThenAction.class */
public class ThenAction extends ThenOrElseActionBase {
    @Override // ch.qos.logback.core.joran.conditional.ThenOrElseActionBase
    void registerEventList(IfAction ifAction, List<SaxEvent> eventList) {
        ifAction.setThenSaxEventList(eventList);
    }
}

package ch.qos.logback.core.joran.conditional;

import ch.qos.logback.core.joran.event.SaxEvent;
import java.util.List;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/joran/conditional/IfState.class
 */
/* compiled from: IfAction.java */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/joran/conditional/IfState.class */
class IfState {
    Boolean boolResult;
    List<SaxEvent> thenSaxEventList;
    List<SaxEvent> elseSaxEventList;
    boolean active;

    IfState() {
    }
}

package ch.qos.logback.core.joran.event.stax;

import javax.xml.stream.Location;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/joran/event/stax/EndEvent.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/joran/event/stax/EndEvent.class */
public class EndEvent extends StaxEvent {
    public EndEvent(String name, Location location) {
        super(name, location);
    }

    public String toString() {
        return "EndEvent(" + getName() + ")  [" + this.location.getLineNumber() + "," + this.location.getColumnNumber() + "]";
    }
}

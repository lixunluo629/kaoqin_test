package ch.qos.logback.core.joran.event.stax;

import javax.xml.stream.Location;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/joran/event/stax/StaxEvent.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/joran/event/stax/StaxEvent.class */
public class StaxEvent {
    final String name;
    final Location location;

    StaxEvent(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return this.name;
    }

    public Location getLocation() {
        return this.location;
    }
}

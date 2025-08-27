package ch.qos.logback.core.joran.event.stax;

import javax.xml.stream.Location;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/joran/event/stax/BodyEvent.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/joran/event/stax/BodyEvent.class */
public class BodyEvent extends StaxEvent {
    private String text;

    BodyEvent(String text, Location location) {
        super(null, location);
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    void append(String txt) {
        this.text += txt;
    }

    public String toString() {
        return "BodyEvent(" + getText() + ")" + this.location.getLineNumber() + "," + this.location.getColumnNumber();
    }
}

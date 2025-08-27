package ch.qos.logback.core.joran.event;

import org.xml.sax.Locator;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/joran/event/BodyEvent.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/joran/event/BodyEvent.class */
public class BodyEvent extends SaxEvent {
    private String text;

    BodyEvent(String text, Locator locator) {
        super(null, null, null, locator);
        this.text = text;
    }

    public String getText() {
        if (this.text != null) {
            return this.text.trim();
        }
        return this.text;
    }

    public String toString() {
        return "BodyEvent(" + getText() + ")" + this.locator.getLineNumber() + "," + this.locator.getColumnNumber();
    }

    public void append(String str) {
        this.text += str;
    }
}

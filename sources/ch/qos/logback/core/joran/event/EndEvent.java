package ch.qos.logback.core.joran.event;

import org.xml.sax.Locator;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/joran/event/EndEvent.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/joran/event/EndEvent.class */
public class EndEvent extends SaxEvent {
    EndEvent(String namespaceURI, String localName, String qName, Locator locator) {
        super(namespaceURI, localName, qName, locator);
    }

    public String toString() {
        return "  EndEvent(" + getQName() + ")  [" + this.locator.getLineNumber() + "," + this.locator.getColumnNumber() + "]";
    }
}

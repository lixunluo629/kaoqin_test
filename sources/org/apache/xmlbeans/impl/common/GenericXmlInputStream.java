package org.apache.xmlbeans.impl.common;

import org.apache.xmlbeans.xml.stream.ReferenceResolver;
import org.apache.xmlbeans.xml.stream.XMLEvent;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLName;
import org.apache.xmlbeans.xml.stream.XMLStreamException;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/GenericXmlInputStream.class */
public class GenericXmlInputStream implements XMLInputStream {
    private boolean _initialized;
    private EventItem _nextEvent;
    private int _elementCount;
    private GenericXmlInputStream _master;

    public GenericXmlInputStream() {
        this._master = this;
        this._elementCount = 1;
    }

    private GenericXmlInputStream(GenericXmlInputStream master) {
        this._master = master;
        master.ensureInit();
        this._nextEvent = master._nextEvent;
    }

    protected XMLEvent nextEvent() throws XMLStreamException {
        throw new RuntimeException("nextEvent not overridden");
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/GenericXmlInputStream$EventItem.class */
    private class EventItem {
        final XMLEvent _event;
        EventItem _next;

        EventItem(XMLEvent e) {
            this._event = e;
        }

        int getType() {
            return this._event.getType();
        }

        boolean hasName() {
            return this._event.hasName();
        }

        XMLName getName() {
            return this._event.getName();
        }
    }

    private void ensureInit() {
        if (!this._master._initialized) {
            try {
                this._master._nextEvent = getNextEvent();
                this._master._initialized = true;
            } catch (XMLStreamException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private EventItem getNextEvent() throws XMLStreamException {
        XMLEvent e = nextEvent();
        if (e == null) {
            return null;
        }
        return new EventItem(e);
    }

    @Override // org.apache.xmlbeans.xml.stream.XMLInputStream
    public XMLEvent next() throws XMLStreamException {
        ensureInit();
        EventItem currentEvent = this._nextEvent;
        if (this._nextEvent != null) {
            if (this._nextEvent._next == null) {
                this._nextEvent._next = this._master.getNextEvent();
            }
            this._nextEvent = this._nextEvent._next;
        }
        if (currentEvent == null) {
            return null;
        }
        if (currentEvent.getType() == 4) {
            int i = this._elementCount - 1;
            this._elementCount = i;
            if (i <= 0) {
                this._nextEvent = null;
            }
        } else if (currentEvent.getType() == 2) {
            this._elementCount++;
        }
        return currentEvent._event;
    }

    @Override // org.apache.xmlbeans.xml.stream.XMLInputStream
    public boolean hasNext() throws XMLStreamException {
        ensureInit();
        return this._nextEvent != null;
    }

    @Override // org.apache.xmlbeans.xml.stream.XMLInputStream
    public void skip() throws XMLStreamException {
        next();
    }

    @Override // org.apache.xmlbeans.xml.stream.XMLInputStream
    public void skipElement() throws XMLStreamException {
        ensureInit();
        while (this._nextEvent != null && this._nextEvent.getType() != 2) {
            next();
        }
        int count = 0;
        while (this._nextEvent != null) {
            int type = next().getType();
            if (type == 2) {
                count++;
            } else if (type == 4) {
                count--;
                if (count == 0) {
                    return;
                }
            } else {
                continue;
            }
            next();
        }
    }

    @Override // org.apache.xmlbeans.xml.stream.XMLInputStream
    public XMLEvent peek() throws XMLStreamException {
        ensureInit();
        return this._nextEvent._event;
    }

    @Override // org.apache.xmlbeans.xml.stream.XMLInputStream
    public boolean skip(int eventType) throws XMLStreamException {
        ensureInit();
        while (this._nextEvent != null) {
            if (this._nextEvent.getType() != eventType) {
                next();
            } else {
                return true;
            }
        }
        return false;
    }

    @Override // org.apache.xmlbeans.xml.stream.XMLInputStream
    public boolean skip(XMLName name) throws XMLStreamException {
        ensureInit();
        while (this._nextEvent != null) {
            if (!this._nextEvent.hasName() || !this._nextEvent.getName().equals(name)) {
                next();
            } else {
                return true;
            }
        }
        return false;
    }

    @Override // org.apache.xmlbeans.xml.stream.XMLInputStream
    public boolean skip(XMLName name, int eventType) throws XMLStreamException {
        ensureInit();
        while (this._nextEvent != null) {
            if (this._nextEvent.getType() != eventType || !this._nextEvent.hasName() || !this._nextEvent.getName().equals(name)) {
                next();
            } else {
                return true;
            }
        }
        return false;
    }

    @Override // org.apache.xmlbeans.xml.stream.XMLInputStream
    public XMLInputStream getSubStream() throws XMLStreamException {
        ensureInit();
        GenericXmlInputStream subStream = new GenericXmlInputStream(this);
        subStream.skip(2);
        return subStream;
    }

    @Override // org.apache.xmlbeans.xml.stream.XMLInputStream
    public void close() throws XMLStreamException {
    }

    @Override // org.apache.xmlbeans.xml.stream.XMLInputStream
    public ReferenceResolver getReferenceResolver() {
        ensureInit();
        throw new RuntimeException("Not impl");
    }

    @Override // org.apache.xmlbeans.xml.stream.XMLInputStream
    public void setReferenceResolver(ReferenceResolver resolver) {
        ensureInit();
        throw new RuntimeException("Not impl");
    }
}

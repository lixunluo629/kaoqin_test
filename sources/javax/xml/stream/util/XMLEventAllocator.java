package javax.xml.stream.util;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

/* loaded from: stax-api-1.0.1.jar:javax/xml/stream/util/XMLEventAllocator.class */
public interface XMLEventAllocator {
    XMLEventAllocator newInstance();

    XMLEvent allocate(XMLStreamReader xMLStreamReader) throws XMLStreamException;

    void allocate(XMLStreamReader xMLStreamReader, XMLEventConsumer xMLEventConsumer) throws XMLStreamException;
}

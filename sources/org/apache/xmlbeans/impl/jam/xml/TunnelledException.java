package org.apache.xmlbeans.impl.jam.xml;

import javax.xml.stream.XMLStreamException;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/xml/TunnelledException.class */
public class TunnelledException extends RuntimeException {
    private XMLStreamException mXSE;

    public TunnelledException(XMLStreamException xse) {
        this.mXSE = null;
        this.mXSE = xse;
    }

    public XMLStreamException getXMLStreamException() {
        return this.mXSE;
    }
}

package org.apache.xmlbeans;

import org.apache.xmlbeans.xml.stream.XMLStreamException;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XMLStreamValidationException.class */
public class XMLStreamValidationException extends XMLStreamException {
    private XmlError _xmlError;

    public XMLStreamValidationException(XmlError xmlError) {
        super(xmlError.toString());
        this._xmlError = xmlError;
    }

    public XmlError getXmlError() {
        return this._xmlError;
    }
}

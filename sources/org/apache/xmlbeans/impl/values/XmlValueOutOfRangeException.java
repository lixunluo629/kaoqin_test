package org.apache.xmlbeans.impl.values;

import org.apache.xmlbeans.XmlError;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/XmlValueOutOfRangeException.class */
public class XmlValueOutOfRangeException extends IllegalArgumentException {
    public XmlValueOutOfRangeException() {
    }

    public XmlValueOutOfRangeException(String message) {
        super(message);
    }

    public XmlValueOutOfRangeException(String code, Object[] args) {
        super(XmlError.formattedMessage(code, args));
    }
}

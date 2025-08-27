package org.apache.xmlbeans.impl.values;

import org.apache.xmlbeans.XmlError;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/XmlValueNotSupportedException.class */
public class XmlValueNotSupportedException extends XmlValueOutOfRangeException {
    public XmlValueNotSupportedException() {
    }

    public XmlValueNotSupportedException(String message) {
        super(message);
    }

    public XmlValueNotSupportedException(String code, Object[] args) {
        super(XmlError.formattedMessage(code, args));
    }
}

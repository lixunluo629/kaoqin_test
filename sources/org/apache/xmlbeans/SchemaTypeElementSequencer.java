package org.apache.xmlbeans;

import javax.xml.namespace.QName;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/SchemaTypeElementSequencer.class */
public interface SchemaTypeElementSequencer {
    boolean next(QName qName);

    boolean peek(QName qName);
}

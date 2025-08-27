package org.apache.xmlbeans;

import javax.xml.namespace.QName;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/SchemaAnnotation.class */
public interface SchemaAnnotation extends SchemaComponent {

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/SchemaAnnotation$Attribute.class */
    public interface Attribute {
        QName getName();

        String getValue();

        String getValueUri();
    }

    XmlObject[] getApplicationInformation();

    XmlObject[] getUserInformation();

    Attribute[] getAttributes();
}

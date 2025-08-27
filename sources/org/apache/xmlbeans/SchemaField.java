package org.apache.xmlbeans;

import java.math.BigInteger;
import javax.xml.namespace.QName;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/SchemaField.class */
public interface SchemaField {
    QName getName();

    boolean isAttribute();

    boolean isNillable();

    SchemaType getType();

    BigInteger getMinOccurs();

    BigInteger getMaxOccurs();

    String getDefaultText();

    XmlAnySimpleType getDefaultValue();

    boolean isDefault();

    boolean isFixed();

    Object getUserData();
}

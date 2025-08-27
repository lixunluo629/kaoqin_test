package org.apache.xmlbeans;

import javax.xml.namespace.QName;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/SchemaAttributeModel.class */
public interface SchemaAttributeModel {
    public static final int NONE = 0;
    public static final int STRICT = 1;
    public static final int LAX = 2;
    public static final int SKIP = 3;

    SchemaLocalAttribute[] getAttributes();

    SchemaLocalAttribute getAttribute(QName qName);

    QNameSet getWildcardSet();

    int getWildcardProcess();
}

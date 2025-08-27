package org.apache.xmlbeans;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/SchemaLocalAttribute.class */
public interface SchemaLocalAttribute extends SchemaField, SchemaAnnotated {
    public static final int PROHIBITED = 1;
    public static final int OPTIONAL = 2;
    public static final int REQUIRED = 3;

    int getUse();
}

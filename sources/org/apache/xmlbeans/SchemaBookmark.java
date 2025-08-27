package org.apache.xmlbeans;

import org.apache.xmlbeans.XmlCursor;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/SchemaBookmark.class */
public class SchemaBookmark extends XmlCursor.XmlBookmark {
    private Object _value;

    public SchemaBookmark(Object value) {
        this._value = value;
    }

    public Object getValue() {
        return this._value;
    }
}

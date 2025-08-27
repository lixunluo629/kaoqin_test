package org.apache.xmlbeans.impl.schema;

import org.apache.xmlbeans.SchemaStringEnumEntry;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/schema/SchemaStringEnumEntryImpl.class */
public class SchemaStringEnumEntryImpl implements SchemaStringEnumEntry {
    private String _string;
    private int _int;
    private String _enumName;

    public SchemaStringEnumEntryImpl(String str, int i, String enumName) {
        this._string = str;
        this._int = i;
        this._enumName = enumName;
    }

    @Override // org.apache.xmlbeans.SchemaStringEnumEntry
    public String getString() {
        return this._string;
    }

    @Override // org.apache.xmlbeans.SchemaStringEnumEntry
    public int getIntValue() {
        return this._int;
    }

    @Override // org.apache.xmlbeans.SchemaStringEnumEntry
    public String getEnumName() {
        return this._enumName;
    }
}

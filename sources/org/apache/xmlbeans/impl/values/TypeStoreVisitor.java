package org.apache.xmlbeans.impl.values;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaField;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/TypeStoreVisitor.class */
public interface TypeStoreVisitor {
    boolean visit(QName qName);

    int get_elementflags();

    String get_default_text();

    SchemaField get_schema_field();
}

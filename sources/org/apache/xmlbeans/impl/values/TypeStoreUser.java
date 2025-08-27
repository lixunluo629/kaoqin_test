package org.apache.xmlbeans.impl.values;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.SchemaField;
import org.apache.xmlbeans.SchemaType;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/TypeStoreUser.class */
public interface TypeStoreUser {
    void attach_store(TypeStore typeStore);

    SchemaType get_schema_type();

    TypeStore get_store();

    void invalidate_value();

    boolean uses_invalidate_value();

    String build_text(NamespaceManager namespaceManager);

    boolean build_nil();

    void invalidate_nilvalue();

    void invalidate_element_order();

    void validate_now();

    void disconnect_store();

    TypeStoreUser create_element_user(QName qName, QName qName2);

    TypeStoreUser create_attribute_user(QName qName);

    SchemaType get_element_type(QName qName, QName qName2);

    SchemaType get_attribute_type(QName qName);

    String get_default_element_text(QName qName);

    String get_default_attribute_text(QName qName);

    int get_elementflags(QName qName);

    int get_attributeflags(QName qName);

    SchemaField get_attribute_field(QName qName);

    boolean is_child_element_order_sensitive();

    QNameSet get_element_ending_delimiters(QName qName);

    TypeStoreVisitor new_visitor();
}

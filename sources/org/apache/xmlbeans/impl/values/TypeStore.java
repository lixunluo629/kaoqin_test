package org.apache.xmlbeans.impl.values;

import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.SchemaField;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeLoader;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.common.ValidatorListener;
import org.apache.xmlbeans.impl.common.XmlLocale;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/TypeStore.class */
public interface TypeStore extends NamespaceManager {
    public static final int WS_UNSPECIFIED = 0;
    public static final int WS_PRESERVE = 1;
    public static final int WS_REPLACE = 2;
    public static final int WS_COLLAPSE = 3;
    public static final int NILLABLE = 1;
    public static final int HASDEFAULT = 2;
    public static final int FIXED = 4;

    XmlCursor new_cursor();

    void validate(ValidatorListener validatorListener);

    SchemaTypeLoader get_schematypeloader();

    TypeStoreUser change_type(SchemaType schemaType);

    TypeStoreUser substitute(QName qName, SchemaType schemaType);

    boolean is_attribute();

    QName get_xsi_type();

    void invalidate_text();

    String fetch_text(int i);

    void store_text(String str);

    String compute_default_text();

    int compute_flags();

    boolean validate_on_set();

    SchemaField get_schema_field();

    void invalidate_nil();

    boolean find_nil();

    int count_elements(QName qName);

    int count_elements(QNameSet qNameSet);

    TypeStoreUser find_element_user(QName qName, int i);

    TypeStoreUser find_element_user(QNameSet qNameSet, int i);

    void find_all_element_users(QName qName, List list);

    void find_all_element_users(QNameSet qNameSet, List list);

    TypeStoreUser insert_element_user(QName qName, int i);

    TypeStoreUser insert_element_user(QNameSet qNameSet, QName qName, int i);

    TypeStoreUser add_element_user(QName qName);

    void remove_element(QName qName, int i);

    void remove_element(QNameSet qNameSet, int i);

    TypeStoreUser find_attribute_user(QName qName);

    TypeStoreUser add_attribute_user(QName qName);

    void remove_attribute(QName qName);

    TypeStoreUser copy_contents_from(TypeStore typeStore);

    TypeStoreUser copy(SchemaTypeLoader schemaTypeLoader, SchemaType schemaType, XmlOptions xmlOptions);

    void array_setter(XmlObject[] xmlObjectArr, QName qName);

    void visit_elements(TypeStoreVisitor typeStoreVisitor);

    XmlObject[] exec_query(String str, XmlOptions xmlOptions) throws XmlException;

    Object get_root_object();

    XmlLocale get_locale();
}

package org.apache.xmlbeans.impl.values;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.GDate;
import org.apache.xmlbeans.GDuration;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.StringEnumAbstractBase;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.schema.SchemaTypeImpl;
import org.apache.xmlbeans.impl.schema.SchemaTypeVisitorImpl;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/XmlComplexContentImpl.class */
public class XmlComplexContentImpl extends XmlObjectBase {
    private SchemaTypeImpl _schemaType;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !XmlComplexContentImpl.class.desiredAssertionStatus();
    }

    public XmlComplexContentImpl(SchemaType type) {
        this._schemaType = (SchemaTypeImpl) type;
        initComplexType(true, true);
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.XmlObject
    public SchemaType schemaType() {
        return this._schemaType;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    public String compute_text(NamespaceManager nsm) {
        return null;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected final void set_String(String v) {
        if (!$assertionsDisabled && this._schemaType.getContentType() == 2) {
            throw new AssertionError();
        }
        if (this._schemaType.getContentType() != 4 && !this._schemaType.isNoType()) {
            throw new IllegalArgumentException("Type does not allow for textual content: " + this._schemaType);
        }
        super.set_String(v);
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    public void set_text(String str) {
        if (!$assertionsDisabled && this._schemaType.getContentType() != 4 && !this._schemaType.isNoType()) {
            throw new AssertionError();
        }
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void update_from_complex_content() {
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    public void set_nil() {
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    public boolean equal_to(XmlObject complexObject) {
        if (!this._schemaType.equals(complexObject.schemaType())) {
            return false;
        }
        return true;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected int value_hash_code() {
        throw new IllegalStateException("Complex types cannot be used as hash keys");
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.impl.values.TypeStoreUser
    public TypeStoreVisitor new_visitor() {
        return new SchemaTypeVisitorImpl(this._schemaType.getContentModel());
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.impl.values.TypeStoreUser
    public boolean is_child_element_order_sensitive() {
        return schemaType().isOrderSensitive();
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.impl.values.TypeStoreUser
    public int get_elementflags(QName eltName) {
        SchemaProperty prop = schemaType().getElementProperty(eltName);
        if (prop == null) {
            return 0;
        }
        if (prop.hasDefault() == 1 || prop.hasFixed() == 1 || prop.hasNillable() == 1) {
            return -1;
        }
        return (prop.hasDefault() == 0 ? 0 : 2) | (prop.hasFixed() == 0 ? 0 : 4) | (prop.hasNillable() == 0 ? 0 : 1);
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.impl.values.TypeStoreUser
    public String get_default_attribute_text(QName attrName) {
        return super.get_default_attribute_text(attrName);
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.impl.values.TypeStoreUser
    public String get_default_element_text(QName eltName) {
        SchemaProperty prop = schemaType().getElementProperty(eltName);
        if (prop == null) {
            return "";
        }
        return prop.getDefaultText();
    }

    protected void unionArraySetterHelper(Object[] sources, QName elemName) {
        TypeStoreUser typeStoreUserFind_element_user;
        int n = sources == null ? 0 : sources.length;
        TypeStore store = get_store();
        int m = store.count_elements(elemName);
        while (m > n) {
            store.remove_element(elemName, m - 1);
            m--;
        }
        for (int i = 0; i < n; i++) {
            if (i >= m) {
                typeStoreUserFind_element_user = store.add_element_user(elemName);
            } else {
                typeStoreUserFind_element_user = store.find_element_user(elemName, i);
            }
            TypeStoreUser user = typeStoreUserFind_element_user;
            ((XmlObjectBase) user).objectSet(sources[i]);
        }
    }

    protected SimpleValue[] arraySetterHelper(int sourcesLength, QName elemName) {
        TypeStoreUser typeStoreUserFind_element_user;
        SimpleValue[] dests = new SimpleValue[sourcesLength];
        TypeStore store = get_store();
        int m = store.count_elements(elemName);
        while (m > sourcesLength) {
            store.remove_element(elemName, m - 1);
            m--;
        }
        for (int i = 0; i < sourcesLength; i++) {
            if (i >= m) {
                typeStoreUserFind_element_user = store.add_element_user(elemName);
            } else {
                typeStoreUserFind_element_user = store.find_element_user(elemName, i);
            }
            TypeStoreUser user = typeStoreUserFind_element_user;
            dests[i] = (SimpleValue) user;
        }
        return dests;
    }

    protected SimpleValue[] arraySetterHelper(int sourcesLength, QName elemName, QNameSet set) {
        TypeStoreUser typeStoreUserFind_element_user;
        SimpleValue[] dests = new SimpleValue[sourcesLength];
        TypeStore store = get_store();
        int m = store.count_elements(set);
        while (m > sourcesLength) {
            store.remove_element(set, m - 1);
            m--;
        }
        for (int i = 0; i < sourcesLength; i++) {
            if (i >= m) {
                typeStoreUserFind_element_user = store.add_element_user(elemName);
            } else {
                typeStoreUserFind_element_user = store.find_element_user(set, i);
            }
            TypeStoreUser user = typeStoreUserFind_element_user;
            dests[i] = (SimpleValue) user;
        }
        return dests;
    }

    protected void arraySetterHelper(boolean[] sources, QName elemName) {
        TypeStoreUser typeStoreUserFind_element_user;
        int n = sources == null ? 0 : sources.length;
        TypeStore store = get_store();
        int m = store.count_elements(elemName);
        while (m > n) {
            store.remove_element(elemName, m - 1);
            m--;
        }
        for (int i = 0; i < n; i++) {
            if (i >= m) {
                typeStoreUserFind_element_user = store.add_element_user(elemName);
            } else {
                typeStoreUserFind_element_user = store.find_element_user(elemName, i);
            }
            TypeStoreUser user = typeStoreUserFind_element_user;
            ((XmlObjectBase) user).set(sources[i]);
        }
    }

    protected void arraySetterHelper(float[] sources, QName elemName) {
        TypeStoreUser typeStoreUserFind_element_user;
        int n = sources == null ? 0 : sources.length;
        TypeStore store = get_store();
        int m = store.count_elements(elemName);
        while (m > n) {
            store.remove_element(elemName, m - 1);
            m--;
        }
        for (int i = 0; i < n; i++) {
            if (i >= m) {
                typeStoreUserFind_element_user = store.add_element_user(elemName);
            } else {
                typeStoreUserFind_element_user = store.find_element_user(elemName, i);
            }
            TypeStoreUser user = typeStoreUserFind_element_user;
            ((XmlObjectBase) user).set(sources[i]);
        }
    }

    protected void arraySetterHelper(double[] sources, QName elemName) {
        TypeStoreUser typeStoreUserFind_element_user;
        int n = sources == null ? 0 : sources.length;
        TypeStore store = get_store();
        int m = store.count_elements(elemName);
        while (m > n) {
            store.remove_element(elemName, m - 1);
            m--;
        }
        for (int i = 0; i < n; i++) {
            if (i >= m) {
                typeStoreUserFind_element_user = store.add_element_user(elemName);
            } else {
                typeStoreUserFind_element_user = store.find_element_user(elemName, i);
            }
            TypeStoreUser user = typeStoreUserFind_element_user;
            ((XmlObjectBase) user).set(sources[i]);
        }
    }

    protected void arraySetterHelper(byte[] sources, QName elemName) {
        TypeStoreUser typeStoreUserFind_element_user;
        int n = sources == null ? 0 : sources.length;
        TypeStore store = get_store();
        int m = store.count_elements(elemName);
        while (m > n) {
            store.remove_element(elemName, m - 1);
            m--;
        }
        for (int i = 0; i < n; i++) {
            if (i >= m) {
                typeStoreUserFind_element_user = store.add_element_user(elemName);
            } else {
                typeStoreUserFind_element_user = store.find_element_user(elemName, i);
            }
            TypeStoreUser user = typeStoreUserFind_element_user;
            ((XmlObjectBase) user).set(sources[i]);
        }
    }

    protected void arraySetterHelper(short[] sources, QName elemName) {
        TypeStoreUser typeStoreUserFind_element_user;
        int n = sources == null ? 0 : sources.length;
        TypeStore store = get_store();
        int m = store.count_elements(elemName);
        while (m > n) {
            store.remove_element(elemName, m - 1);
            m--;
        }
        for (int i = 0; i < n; i++) {
            if (i >= m) {
                typeStoreUserFind_element_user = store.add_element_user(elemName);
            } else {
                typeStoreUserFind_element_user = store.find_element_user(elemName, i);
            }
            TypeStoreUser user = typeStoreUserFind_element_user;
            ((XmlObjectBase) user).set(sources[i]);
        }
    }

    protected void arraySetterHelper(int[] sources, QName elemName) {
        TypeStoreUser typeStoreUserFind_element_user;
        int n = sources == null ? 0 : sources.length;
        TypeStore store = get_store();
        int m = store.count_elements(elemName);
        while (m > n) {
            store.remove_element(elemName, m - 1);
            m--;
        }
        for (int i = 0; i < n; i++) {
            if (i >= m) {
                typeStoreUserFind_element_user = store.add_element_user(elemName);
            } else {
                typeStoreUserFind_element_user = store.find_element_user(elemName, i);
            }
            TypeStoreUser user = typeStoreUserFind_element_user;
            ((XmlObjectBase) user).set(sources[i]);
        }
    }

    protected void arraySetterHelper(long[] sources, QName elemName) {
        TypeStoreUser typeStoreUserFind_element_user;
        int n = sources == null ? 0 : sources.length;
        TypeStore store = get_store();
        int m = store.count_elements(elemName);
        while (m > n) {
            store.remove_element(elemName, m - 1);
            m--;
        }
        for (int i = 0; i < n; i++) {
            if (i >= m) {
                typeStoreUserFind_element_user = store.add_element_user(elemName);
            } else {
                typeStoreUserFind_element_user = store.find_element_user(elemName, i);
            }
            TypeStoreUser user = typeStoreUserFind_element_user;
            ((XmlObjectBase) user).set(sources[i]);
        }
    }

    protected void arraySetterHelper(BigDecimal[] sources, QName elemName) {
        TypeStoreUser typeStoreUserFind_element_user;
        int n = sources == null ? 0 : sources.length;
        TypeStore store = get_store();
        int m = store.count_elements(elemName);
        while (m > n) {
            store.remove_element(elemName, m - 1);
            m--;
        }
        for (int i = 0; i < n; i++) {
            if (i >= m) {
                typeStoreUserFind_element_user = store.add_element_user(elemName);
            } else {
                typeStoreUserFind_element_user = store.find_element_user(elemName, i);
            }
            TypeStoreUser user = typeStoreUserFind_element_user;
            ((XmlObjectBase) user).set(sources[i]);
        }
    }

    protected void arraySetterHelper(BigInteger[] sources, QName elemName) {
        TypeStoreUser typeStoreUserFind_element_user;
        int n = sources == null ? 0 : sources.length;
        TypeStore store = get_store();
        int m = store.count_elements(elemName);
        while (m > n) {
            store.remove_element(elemName, m - 1);
            m--;
        }
        for (int i = 0; i < n; i++) {
            if (i >= m) {
                typeStoreUserFind_element_user = store.add_element_user(elemName);
            } else {
                typeStoreUserFind_element_user = store.find_element_user(elemName, i);
            }
            TypeStoreUser user = typeStoreUserFind_element_user;
            ((XmlObjectBase) user).set(sources[i]);
        }
    }

    protected void arraySetterHelper(String[] sources, QName elemName) {
        TypeStoreUser typeStoreUserFind_element_user;
        int n = sources == null ? 0 : sources.length;
        TypeStore store = get_store();
        int m = store.count_elements(elemName);
        while (m > n) {
            store.remove_element(elemName, m - 1);
            m--;
        }
        for (int i = 0; i < n; i++) {
            if (i >= m) {
                typeStoreUserFind_element_user = store.add_element_user(elemName);
            } else {
                typeStoreUserFind_element_user = store.find_element_user(elemName, i);
            }
            TypeStoreUser user = typeStoreUserFind_element_user;
            ((XmlObjectBase) user).set(sources[i]);
        }
    }

    protected void arraySetterHelper(byte[][] sources, QName elemName) {
        TypeStoreUser typeStoreUserFind_element_user;
        int n = sources == null ? 0 : sources.length;
        TypeStore store = get_store();
        int m = store.count_elements(elemName);
        while (m > n) {
            store.remove_element(elemName, m - 1);
            m--;
        }
        for (int i = 0; i < n; i++) {
            if (i >= m) {
                typeStoreUserFind_element_user = store.add_element_user(elemName);
            } else {
                typeStoreUserFind_element_user = store.find_element_user(elemName, i);
            }
            TypeStoreUser user = typeStoreUserFind_element_user;
            ((XmlObjectBase) user).set(sources[i]);
        }
    }

    protected void arraySetterHelper(GDate[] sources, QName elemName) {
        TypeStoreUser typeStoreUserFind_element_user;
        int n = sources == null ? 0 : sources.length;
        TypeStore store = get_store();
        int m = store.count_elements(elemName);
        while (m > n) {
            store.remove_element(elemName, m - 1);
            m--;
        }
        for (int i = 0; i < n; i++) {
            if (i >= m) {
                typeStoreUserFind_element_user = store.add_element_user(elemName);
            } else {
                typeStoreUserFind_element_user = store.find_element_user(elemName, i);
            }
            TypeStoreUser user = typeStoreUserFind_element_user;
            ((XmlObjectBase) user).set(sources[i]);
        }
    }

    protected void arraySetterHelper(GDuration[] sources, QName elemName) {
        TypeStoreUser typeStoreUserFind_element_user;
        int n = sources == null ? 0 : sources.length;
        TypeStore store = get_store();
        int m = store.count_elements(elemName);
        while (m > n) {
            store.remove_element(elemName, m - 1);
            m--;
        }
        for (int i = 0; i < n; i++) {
            if (i >= m) {
                typeStoreUserFind_element_user = store.add_element_user(elemName);
            } else {
                typeStoreUserFind_element_user = store.find_element_user(elemName, i);
            }
            TypeStoreUser user = typeStoreUserFind_element_user;
            ((XmlObjectBase) user).set(sources[i]);
        }
    }

    protected void arraySetterHelper(Calendar[] sources, QName elemName) {
        TypeStoreUser typeStoreUserFind_element_user;
        int n = sources == null ? 0 : sources.length;
        TypeStore store = get_store();
        int m = store.count_elements(elemName);
        while (m > n) {
            store.remove_element(elemName, m - 1);
            m--;
        }
        for (int i = 0; i < n; i++) {
            if (i >= m) {
                typeStoreUserFind_element_user = store.add_element_user(elemName);
            } else {
                typeStoreUserFind_element_user = store.find_element_user(elemName, i);
            }
            TypeStoreUser user = typeStoreUserFind_element_user;
            ((XmlObjectBase) user).set(sources[i]);
        }
    }

    protected void arraySetterHelper(Date[] sources, QName elemName) {
        TypeStoreUser typeStoreUserFind_element_user;
        int n = sources == null ? 0 : sources.length;
        TypeStore store = get_store();
        int m = store.count_elements(elemName);
        while (m > n) {
            store.remove_element(elemName, m - 1);
            m--;
        }
        for (int i = 0; i < n; i++) {
            if (i >= m) {
                typeStoreUserFind_element_user = store.add_element_user(elemName);
            } else {
                typeStoreUserFind_element_user = store.find_element_user(elemName, i);
            }
            TypeStoreUser user = typeStoreUserFind_element_user;
            ((XmlObjectBase) user).set(sources[i]);
        }
    }

    protected void arraySetterHelper(QName[] sources, QName elemName) {
        TypeStoreUser typeStoreUserFind_element_user;
        int n = sources == null ? 0 : sources.length;
        TypeStore store = get_store();
        int m = store.count_elements(elemName);
        while (m > n) {
            store.remove_element(elemName, m - 1);
            m--;
        }
        for (int i = 0; i < n; i++) {
            if (i >= m) {
                typeStoreUserFind_element_user = store.add_element_user(elemName);
            } else {
                typeStoreUserFind_element_user = store.find_element_user(elemName, i);
            }
            TypeStoreUser user = typeStoreUserFind_element_user;
            ((XmlObjectBase) user).set(sources[i]);
        }
    }

    protected void arraySetterHelper(StringEnumAbstractBase[] sources, QName elemName) {
        TypeStoreUser typeStoreUserFind_element_user;
        int n = sources == null ? 0 : sources.length;
        TypeStore store = get_store();
        int m = store.count_elements(elemName);
        while (m > n) {
            store.remove_element(elemName, m - 1);
            m--;
        }
        for (int i = 0; i < n; i++) {
            if (i >= m) {
                typeStoreUserFind_element_user = store.add_element_user(elemName);
            } else {
                typeStoreUserFind_element_user = store.find_element_user(elemName, i);
            }
            TypeStoreUser user = typeStoreUserFind_element_user;
            ((XmlObjectBase) user).set(sources[i]);
        }
    }

    protected void arraySetterHelper(List[] sources, QName elemName) {
        TypeStoreUser typeStoreUserFind_element_user;
        int n = sources == null ? 0 : sources.length;
        TypeStore store = get_store();
        int m = store.count_elements(elemName);
        while (m > n) {
            store.remove_element(elemName, m - 1);
            m--;
        }
        for (int i = 0; i < n; i++) {
            if (i >= m) {
                typeStoreUserFind_element_user = store.add_element_user(elemName);
            } else {
                typeStoreUserFind_element_user = store.find_element_user(elemName, i);
            }
            TypeStoreUser user = typeStoreUserFind_element_user;
            ((XmlObjectBase) user).set(sources[i]);
        }
    }

    protected void unionArraySetterHelper(Object[] sources, QName elemName, QNameSet set) {
        TypeStoreUser typeStoreUserFind_element_user;
        int n = sources == null ? 0 : sources.length;
        TypeStore store = get_store();
        int m = store.count_elements(set);
        while (m > n) {
            store.remove_element(set, m - 1);
            m--;
        }
        for (int i = 0; i < n; i++) {
            if (i >= m) {
                typeStoreUserFind_element_user = store.add_element_user(elemName);
            } else {
                typeStoreUserFind_element_user = store.find_element_user(set, i);
            }
            TypeStoreUser user = typeStoreUserFind_element_user;
            ((XmlObjectBase) user).objectSet(sources[i]);
        }
    }

    protected void arraySetterHelper(boolean[] sources, QName elemName, QNameSet set) {
        TypeStoreUser typeStoreUserFind_element_user;
        int n = sources == null ? 0 : sources.length;
        TypeStore store = get_store();
        int m = store.count_elements(set);
        while (m > n) {
            store.remove_element(set, m - 1);
            m--;
        }
        for (int i = 0; i < n; i++) {
            if (i >= m) {
                typeStoreUserFind_element_user = store.add_element_user(elemName);
            } else {
                typeStoreUserFind_element_user = store.find_element_user(set, i);
            }
            TypeStoreUser user = typeStoreUserFind_element_user;
            ((XmlObjectBase) user).set(sources[i]);
        }
    }

    protected void arraySetterHelper(float[] sources, QName elemName, QNameSet set) {
        TypeStoreUser typeStoreUserFind_element_user;
        int n = sources == null ? 0 : sources.length;
        TypeStore store = get_store();
        int m = store.count_elements(set);
        while (m > n) {
            store.remove_element(set, m - 1);
            m--;
        }
        for (int i = 0; i < n; i++) {
            if (i >= m) {
                typeStoreUserFind_element_user = store.add_element_user(elemName);
            } else {
                typeStoreUserFind_element_user = store.find_element_user(set, i);
            }
            TypeStoreUser user = typeStoreUserFind_element_user;
            ((XmlObjectBase) user).set(sources[i]);
        }
    }

    protected void arraySetterHelper(double[] sources, QName elemName, QNameSet set) {
        TypeStoreUser typeStoreUserFind_element_user;
        int n = sources == null ? 0 : sources.length;
        TypeStore store = get_store();
        int m = store.count_elements(set);
        while (m > n) {
            store.remove_element(set, m - 1);
            m--;
        }
        for (int i = 0; i < n; i++) {
            if (i >= m) {
                typeStoreUserFind_element_user = store.add_element_user(elemName);
            } else {
                typeStoreUserFind_element_user = store.find_element_user(set, i);
            }
            TypeStoreUser user = typeStoreUserFind_element_user;
            ((XmlObjectBase) user).set(sources[i]);
        }
    }

    protected void arraySetterHelper(byte[] sources, QName elemName, QNameSet set) {
        TypeStoreUser typeStoreUserFind_element_user;
        int n = sources == null ? 0 : sources.length;
        TypeStore store = get_store();
        int m = store.count_elements(set);
        while (m > n) {
            store.remove_element(set, m - 1);
            m--;
        }
        for (int i = 0; i < n; i++) {
            if (i >= m) {
                typeStoreUserFind_element_user = store.add_element_user(elemName);
            } else {
                typeStoreUserFind_element_user = store.find_element_user(set, i);
            }
            TypeStoreUser user = typeStoreUserFind_element_user;
            ((XmlObjectBase) user).set(sources[i]);
        }
    }

    protected void arraySetterHelper(short[] sources, QName elemName, QNameSet set) {
        TypeStoreUser typeStoreUserFind_element_user;
        int n = sources == null ? 0 : sources.length;
        TypeStore store = get_store();
        int m = store.count_elements(set);
        while (m > n) {
            store.remove_element(set, m - 1);
            m--;
        }
        for (int i = 0; i < n; i++) {
            if (i >= m) {
                typeStoreUserFind_element_user = store.add_element_user(elemName);
            } else {
                typeStoreUserFind_element_user = store.find_element_user(set, i);
            }
            TypeStoreUser user = typeStoreUserFind_element_user;
            ((XmlObjectBase) user).set(sources[i]);
        }
    }

    protected void arraySetterHelper(int[] sources, QName elemName, QNameSet set) {
        TypeStoreUser typeStoreUserFind_element_user;
        int n = sources == null ? 0 : sources.length;
        TypeStore store = get_store();
        int m = store.count_elements(set);
        while (m > n) {
            store.remove_element(set, m - 1);
            m--;
        }
        for (int i = 0; i < n; i++) {
            if (i >= m) {
                typeStoreUserFind_element_user = store.add_element_user(elemName);
            } else {
                typeStoreUserFind_element_user = store.find_element_user(set, i);
            }
            TypeStoreUser user = typeStoreUserFind_element_user;
            ((XmlObjectBase) user).set(sources[i]);
        }
    }

    protected void arraySetterHelper(long[] sources, QName elemName, QNameSet set) {
        TypeStoreUser typeStoreUserFind_element_user;
        int n = sources == null ? 0 : sources.length;
        TypeStore store = get_store();
        int m = store.count_elements(set);
        while (m > n) {
            store.remove_element(set, m - 1);
            m--;
        }
        for (int i = 0; i < n; i++) {
            if (i >= m) {
                typeStoreUserFind_element_user = store.add_element_user(elemName);
            } else {
                typeStoreUserFind_element_user = store.find_element_user(set, i);
            }
            TypeStoreUser user = typeStoreUserFind_element_user;
            ((XmlObjectBase) user).set(sources[i]);
        }
    }

    protected void arraySetterHelper(BigDecimal[] sources, QName elemName, QNameSet set) {
        TypeStoreUser typeStoreUserFind_element_user;
        int n = sources == null ? 0 : sources.length;
        TypeStore store = get_store();
        int m = store.count_elements(set);
        while (m > n) {
            store.remove_element(set, m - 1);
            m--;
        }
        for (int i = 0; i < n; i++) {
            if (i >= m) {
                typeStoreUserFind_element_user = store.add_element_user(elemName);
            } else {
                typeStoreUserFind_element_user = store.find_element_user(set, i);
            }
            TypeStoreUser user = typeStoreUserFind_element_user;
            ((XmlObjectBase) user).set(sources[i]);
        }
    }

    protected void arraySetterHelper(BigInteger[] sources, QName elemName, QNameSet set) {
        TypeStoreUser typeStoreUserFind_element_user;
        int n = sources == null ? 0 : sources.length;
        TypeStore store = get_store();
        int m = store.count_elements(set);
        while (m > n) {
            store.remove_element(set, m - 1);
            m--;
        }
        for (int i = 0; i < n; i++) {
            if (i >= m) {
                typeStoreUserFind_element_user = store.add_element_user(elemName);
            } else {
                typeStoreUserFind_element_user = store.find_element_user(set, i);
            }
            TypeStoreUser user = typeStoreUserFind_element_user;
            ((XmlObjectBase) user).set(sources[i]);
        }
    }

    protected void arraySetterHelper(String[] sources, QName elemName, QNameSet set) {
        TypeStoreUser typeStoreUserFind_element_user;
        int n = sources == null ? 0 : sources.length;
        TypeStore store = get_store();
        int m = store.count_elements(set);
        while (m > n) {
            store.remove_element(set, m - 1);
            m--;
        }
        for (int i = 0; i < n; i++) {
            if (i >= m) {
                typeStoreUserFind_element_user = store.add_element_user(elemName);
            } else {
                typeStoreUserFind_element_user = store.find_element_user(set, i);
            }
            TypeStoreUser user = typeStoreUserFind_element_user;
            ((XmlObjectBase) user).set(sources[i]);
        }
    }

    protected void arraySetterHelper(byte[][] sources, QName elemName, QNameSet set) {
        TypeStoreUser typeStoreUserFind_element_user;
        int n = sources == null ? 0 : sources.length;
        TypeStore store = get_store();
        int m = store.count_elements(set);
        while (m > n) {
            store.remove_element(set, m - 1);
            m--;
        }
        for (int i = 0; i < n; i++) {
            if (i >= m) {
                typeStoreUserFind_element_user = store.add_element_user(elemName);
            } else {
                typeStoreUserFind_element_user = store.find_element_user(set, i);
            }
            TypeStoreUser user = typeStoreUserFind_element_user;
            ((XmlObjectBase) user).set(sources[i]);
        }
    }

    protected void arraySetterHelper(GDate[] sources, QName elemName, QNameSet set) {
        TypeStoreUser typeStoreUserFind_element_user;
        int n = sources == null ? 0 : sources.length;
        TypeStore store = get_store();
        int m = store.count_elements(set);
        while (m > n) {
            store.remove_element(set, m - 1);
            m--;
        }
        for (int i = 0; i < n; i++) {
            if (i >= m) {
                typeStoreUserFind_element_user = store.add_element_user(elemName);
            } else {
                typeStoreUserFind_element_user = store.find_element_user(set, i);
            }
            TypeStoreUser user = typeStoreUserFind_element_user;
            ((XmlObjectBase) user).set(sources[i]);
        }
    }

    protected void arraySetterHelper(GDuration[] sources, QName elemName, QNameSet set) {
        TypeStoreUser typeStoreUserFind_element_user;
        int n = sources == null ? 0 : sources.length;
        TypeStore store = get_store();
        int m = store.count_elements(set);
        while (m > n) {
            store.remove_element(set, m - 1);
            m--;
        }
        for (int i = 0; i < n; i++) {
            if (i >= m) {
                typeStoreUserFind_element_user = store.add_element_user(elemName);
            } else {
                typeStoreUserFind_element_user = store.find_element_user(set, i);
            }
            TypeStoreUser user = typeStoreUserFind_element_user;
            ((XmlObjectBase) user).set(sources[i]);
        }
    }

    protected void arraySetterHelper(Calendar[] sources, QName elemName, QNameSet set) {
        TypeStoreUser typeStoreUserFind_element_user;
        int n = sources == null ? 0 : sources.length;
        TypeStore store = get_store();
        int m = store.count_elements(set);
        while (m > n) {
            store.remove_element(set, m - 1);
            m--;
        }
        for (int i = 0; i < n; i++) {
            if (i >= m) {
                typeStoreUserFind_element_user = store.add_element_user(elemName);
            } else {
                typeStoreUserFind_element_user = store.find_element_user(set, i);
            }
            TypeStoreUser user = typeStoreUserFind_element_user;
            ((XmlObjectBase) user).set(sources[i]);
        }
    }

    protected void arraySetterHelper(Date[] sources, QName elemName, QNameSet set) {
        TypeStoreUser typeStoreUserFind_element_user;
        int n = sources == null ? 0 : sources.length;
        TypeStore store = get_store();
        int m = store.count_elements(set);
        while (m > n) {
            store.remove_element(set, m - 1);
            m--;
        }
        for (int i = 0; i < n; i++) {
            if (i >= m) {
                typeStoreUserFind_element_user = store.add_element_user(elemName);
            } else {
                typeStoreUserFind_element_user = store.find_element_user(set, i);
            }
            TypeStoreUser user = typeStoreUserFind_element_user;
            ((XmlObjectBase) user).set(sources[i]);
        }
    }

    protected void arraySetterHelper(QName[] sources, QName elemName, QNameSet set) {
        TypeStoreUser typeStoreUserFind_element_user;
        int n = sources == null ? 0 : sources.length;
        TypeStore store = get_store();
        int m = store.count_elements(set);
        while (m > n) {
            store.remove_element(set, m - 1);
            m--;
        }
        for (int i = 0; i < n; i++) {
            if (i >= m) {
                typeStoreUserFind_element_user = store.add_element_user(elemName);
            } else {
                typeStoreUserFind_element_user = store.find_element_user(set, i);
            }
            TypeStoreUser user = typeStoreUserFind_element_user;
            ((XmlObjectBase) user).set(sources[i]);
        }
    }

    protected void arraySetterHelper(StringEnumAbstractBase[] sources, QName elemName, QNameSet set) {
        TypeStoreUser typeStoreUserFind_element_user;
        int n = sources == null ? 0 : sources.length;
        TypeStore store = get_store();
        int m = store.count_elements(set);
        while (m > n) {
            store.remove_element(set, m - 1);
            m--;
        }
        for (int i = 0; i < n; i++) {
            if (i >= m) {
                typeStoreUserFind_element_user = store.add_element_user(elemName);
            } else {
                typeStoreUserFind_element_user = store.find_element_user(set, i);
            }
            TypeStoreUser user = typeStoreUserFind_element_user;
            ((XmlObjectBase) user).set(sources[i]);
        }
    }

    protected void arraySetterHelper(List[] sources, QName elemName, QNameSet set) {
        TypeStoreUser typeStoreUserFind_element_user;
        int n = sources == null ? 0 : sources.length;
        TypeStore store = get_store();
        int m = store.count_elements(set);
        while (m > n) {
            store.remove_element(set, m - 1);
            m--;
        }
        for (int i = 0; i < n; i++) {
            if (i >= m) {
                typeStoreUserFind_element_user = store.add_element_user(elemName);
            } else {
                typeStoreUserFind_element_user = store.find_element_user(set, i);
            }
            TypeStoreUser user = typeStoreUserFind_element_user;
            ((XmlObjectBase) user).set(sources[i]);
        }
    }

    protected void arraySetterHelper(XmlObject[] sources, QName elemName) {
        TypeStoreUser typeStoreUserFind_element_user;
        TypeStore store = get_store();
        if (sources == null || sources.length == 0) {
            for (int m = store.count_elements(elemName); m > 0; m--) {
                store.remove_element(elemName, 0);
            }
            return;
        }
        int m2 = store.count_elements(elemName);
        int startSrc = 0;
        int startDest = 0;
        int i = 0;
        while (true) {
            if (i >= sources.length) {
                break;
            }
            if (!sources[i].isImmutable()) {
                XmlCursor c = sources[i].newCursor();
                if (c.toParent() && c.getObject() == this) {
                    c.dispose();
                    break;
                }
                c.dispose();
            }
            i++;
        }
        if (i < sources.length) {
            TypeStoreUser current = store.find_element_user(elemName, 0);
            if (current == sources[i]) {
                int j = 0;
                while (j < i) {
                    TypeStoreUser user = store.insert_element_user(elemName, j);
                    ((XmlObjectBase) user).set(sources[j]);
                    j++;
                }
                while (true) {
                    i++;
                    j++;
                    if (i >= sources.length) {
                        break;
                    }
                    XmlCursor c2 = sources[i].isImmutable() ? null : sources[i].newCursor();
                    if (c2 != null && c2.toParent() && c2.getObject() == this) {
                        c2.dispose();
                        TypeStoreUser current2 = store.find_element_user(elemName, j);
                        if (current2 != sources[i]) {
                            break;
                        }
                    } else {
                        c2.dispose();
                        TypeStoreUser user2 = store.insert_element_user(elemName, j);
                        ((XmlObjectBase) user2).set(sources[i]);
                    }
                }
                startDest = j;
                startSrc = i;
                m2 = store.count_elements(elemName);
            }
        }
        for (int j2 = i; j2 < sources.length; j2++) {
            TypeStoreUser user3 = store.add_element_user(elemName);
            ((XmlObjectBase) user3).set(sources[j2]);
        }
        int n = i;
        while (m2 > (n - startSrc) + startDest) {
            store.remove_element(elemName, m2 - 1);
            m2--;
        }
        int i2 = startSrc;
        int j3 = startDest;
        while (i2 < n) {
            if (j3 >= m2) {
                typeStoreUserFind_element_user = store.add_element_user(elemName);
            } else {
                typeStoreUserFind_element_user = store.find_element_user(elemName, j3);
            }
            TypeStoreUser user4 = typeStoreUserFind_element_user;
            ((XmlObjectBase) user4).set(sources[i2]);
            i2++;
            j3++;
        }
    }

    protected void arraySetterHelper(XmlObject[] sources, QName elemName, QNameSet set) {
        TypeStoreUser typeStoreUserFind_element_user;
        TypeStore store = get_store();
        if (sources == null || sources.length == 0) {
            for (int m = store.count_elements(set); m > 0; m--) {
                store.remove_element(set, 0);
            }
            return;
        }
        int m2 = store.count_elements(set);
        int startSrc = 0;
        int startDest = 0;
        int i = 0;
        while (true) {
            if (i >= sources.length) {
                break;
            }
            if (!sources[i].isImmutable()) {
                XmlCursor c = sources[i].newCursor();
                if (c.toParent() && c.getObject() == this) {
                    c.dispose();
                    break;
                }
                c.dispose();
            }
            i++;
        }
        if (i < sources.length) {
            TypeStoreUser current = store.find_element_user(set, 0);
            if (current == sources[i]) {
                int j = 0;
                while (j < i) {
                    TypeStoreUser user = store.insert_element_user(set, elemName, j);
                    ((XmlObjectBase) user).set(sources[j]);
                    j++;
                }
                while (true) {
                    i++;
                    j++;
                    if (i >= sources.length) {
                        break;
                    }
                    XmlCursor c2 = sources[i].isImmutable() ? null : sources[i].newCursor();
                    if (c2 != null && c2.toParent() && c2.getObject() == this) {
                        c2.dispose();
                        TypeStoreUser current2 = store.find_element_user(set, j);
                        if (current2 != sources[i]) {
                            break;
                        }
                    } else {
                        c2.dispose();
                        TypeStoreUser user2 = store.insert_element_user(set, elemName, j);
                        ((XmlObjectBase) user2).set(sources[i]);
                    }
                }
                startDest = j;
                startSrc = i;
                m2 = store.count_elements(elemName);
            }
        }
        for (int j2 = i; j2 < sources.length; j2++) {
            TypeStoreUser user3 = store.add_element_user(elemName);
            ((XmlObjectBase) user3).set(sources[j2]);
        }
        int n = i;
        while (m2 > (n - startSrc) + startDest) {
            store.remove_element(set, m2 - 1);
            m2--;
        }
        int i2 = startSrc;
        int j3 = startDest;
        while (i2 < n) {
            if (j3 >= m2) {
                typeStoreUserFind_element_user = store.add_element_user(elemName);
            } else {
                typeStoreUserFind_element_user = store.find_element_user(set, j3);
            }
            TypeStoreUser user4 = typeStoreUserFind_element_user;
            ((XmlObjectBase) user4).set(sources[i2]);
            i2++;
            j3++;
        }
    }
}

package org.apache.xmlbeans.impl.values;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlAnySimpleType;
import org.apache.xmlbeans.XmlErrorCodes;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.common.PrefixResolver;
import org.apache.xmlbeans.impl.common.QNameHelper;
import org.apache.xmlbeans.impl.common.ValidationContext;
import org.apache.xmlbeans.impl.common.XMLChar;
import org.apache.xmlbeans.impl.schema.BuiltinSchemaTypeSystem;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/JavaQNameHolder.class */
public class JavaQNameHolder extends XmlObjectBase {
    private QName _value;
    private static final NamespaceManager PRETTY_PREFIXER;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !JavaQNameHolder.class.desiredAssertionStatus();
        PRETTY_PREFIXER = new PrettyNamespaceManager();
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.XmlObject
    public SchemaType schemaType() {
        return BuiltinSchemaTypeSystem.ST_QNAME;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected int get_wscanon_rule() {
        return 1;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/JavaQNameHolder$PrettyNamespaceManager.class */
    private static class PrettyNamespaceManager implements NamespaceManager {
        private PrettyNamespaceManager() {
        }

        @Override // org.apache.xmlbeans.impl.values.NamespaceManager
        public String find_prefix_for_nsuri(String nsuri, String suggested_prefix) {
            return QNameHelper.suggestPrefix(nsuri);
        }

        @Override // org.apache.xmlbeans.impl.common.PrefixResolver
        public String getNamespaceForPrefix(String prefix) {
            throw new RuntimeException("Should not be called");
        }
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    public String compute_text(NamespaceManager nsm) {
        if (nsm == null) {
            nsm = PRETTY_PREFIXER;
        }
        String namespace = this._value.getNamespaceURI();
        String localPart = this._value.getLocalPart();
        if (namespace == null || namespace.length() == 0) {
            return localPart;
        }
        String prefix = nsm.find_prefix_for_nsuri(namespace, null);
        if ($assertionsDisabled || prefix != null) {
            return "".equals(prefix) ? localPart : prefix + ":" + localPart;
        }
        throw new AssertionError();
    }

    public static QName validateLexical(String v, ValidationContext context, PrefixResolver resolver) {
        QName name;
        try {
            name = parse(v, resolver);
        } catch (XmlValueOutOfRangeException e) {
            context.invalid(e.getMessage());
            name = null;
        }
        return name;
    }

    private static QName parse(String v, PrefixResolver resolver) {
        String prefix;
        String localname;
        int end = v.length();
        while (end > 0 && XMLChar.isSpace(v.charAt(end - 1))) {
            end--;
        }
        int start = 0;
        while (start < end && XMLChar.isSpace(v.charAt(start))) {
            start++;
        }
        int firstcolon = v.indexOf(58, start);
        if (firstcolon >= 0) {
            prefix = v.substring(start, firstcolon);
            localname = v.substring(firstcolon + 1, end);
        } else {
            prefix = "";
            localname = v.substring(start, end);
        }
        if (prefix.length() > 0 && !XMLChar.isValidNCName(prefix)) {
            throw new XmlValueOutOfRangeException(XmlErrorCodes.QNAME, new Object[]{"Prefix not a valid NCName in '" + v + "'"});
        }
        if (!XMLChar.isValidNCName(localname)) {
            throw new XmlValueOutOfRangeException(XmlErrorCodes.QNAME, new Object[]{"Localname not a valid NCName in '" + v + "'"});
        }
        String uri = resolver == null ? null : resolver.getNamespaceForPrefix(prefix);
        if (uri == null) {
            if (prefix.length() > 0) {
                throw new XmlValueOutOfRangeException(XmlErrorCodes.QNAME, new Object[]{"Can't resolve prefix '" + prefix + "'"});
            }
            uri = "";
        }
        if (prefix != null && prefix.length() > 0) {
            return new QName(uri, localname, prefix);
        }
        return new QName(uri, localname);
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_text(String s) {
        PrefixResolver resolver = NamespaceContext.getCurrent();
        if (resolver == null && has_store()) {
            resolver = get_store();
        }
        this._value = parse(s, resolver);
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_QName(QName name) {
        if (!$assertionsDisabled && name == null) {
            throw new AssertionError();
        }
        if (has_store()) {
            get_store().find_prefix_for_nsuri(name.getNamespaceURI(), null);
        }
        this._value = name;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_xmlanysimple(XmlAnySimpleType value) {
        this._value = parse(value.getStringValue(), NamespaceContext.getCurrent());
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_nil() {
        this._value = null;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.SimpleValue
    public QName getQNameValue() {
        check_dated();
        return this._value;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected boolean equal_to(XmlObject obj) {
        return this._value.equals(((XmlObjectBase) obj).qNameValue());
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected int value_hash_code() {
        return this._value.hashCode();
    }
}

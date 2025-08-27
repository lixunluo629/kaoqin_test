package org.apache.xmlbeans.impl.values;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlAnySimpleType;
import org.apache.xmlbeans.XmlErrorCodes;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlSimpleList;
import org.apache.xmlbeans.impl.common.PrefixResolver;
import org.apache.xmlbeans.impl.common.QNameHelper;
import org.apache.xmlbeans.impl.common.ValidationContext;
import org.apache.xmlbeans.impl.common.XMLChar;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/XmlListImpl.class */
public class XmlListImpl extends XmlObjectBase implements XmlAnySimpleType {
    private SchemaType _schemaType;
    private XmlSimpleList _value;
    private XmlSimpleList _jvalue;
    private static final String[] EMPTY_STRINGARRAY = new String[0];

    public XmlListImpl(SchemaType type, boolean complex) {
        this._schemaType = type;
        initComplexType(complex, false);
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.XmlObject
    public SchemaType schemaType() {
        return this._schemaType;
    }

    private static String nullAsEmpty(String s) {
        if (s == null) {
            return "";
        }
        return s;
    }

    private static String compute_list_text(List xList) {
        if (xList.size() == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        sb.append(nullAsEmpty(((SimpleValue) xList.get(0)).getStringValue()));
        for (int i = 1; i < xList.size(); i++) {
            sb.append(' ');
            sb.append(nullAsEmpty(((SimpleValue) xList.get(i)).getStringValue()));
        }
        return sb.toString();
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected String compute_text(NamespaceManager nsm) {
        return compute_list_text(this._value);
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected boolean is_defaultable_ws(String v) {
        try {
            XmlSimpleList savedValue = this._value;
            set_text(v);
            this._value = savedValue;
            return false;
        } catch (XmlValueOutOfRangeException e) {
            return true;
        }
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_text(String s) {
        if (_validateOnSet() && !this._schemaType.matchPatternFacet(s)) {
            throw new XmlValueOutOfRangeException(XmlErrorCodes.DATATYPE_VALID$PATTERN_VALID, new Object[]{"list", s, QNameHelper.readable(this._schemaType)});
        }
        SchemaType itemType = this._schemaType.getListItemType();
        XmlSimpleList newval = lex(s, itemType, _voorVc, has_store() ? get_store() : null);
        if (_validateOnSet()) {
            validateValue(newval, this._schemaType, _voorVc);
        }
        this._value = newval;
        this._jvalue = null;
    }

    public static String[] split_list(String s) {
        if (s.length() == 0) {
            return EMPTY_STRINGARRAY;
        }
        List result = new ArrayList();
        int i = 0;
        while (true) {
            if (i < s.length() && XMLChar.isSpace(s.charAt(i))) {
                i++;
            } else {
                if (i >= s.length()) {
                    return (String[]) result.toArray(EMPTY_STRINGARRAY);
                }
                int start = i;
                while (i < s.length() && !XMLChar.isSpace(s.charAt(i))) {
                    i++;
                }
                result.add(s.substring(start, i));
            }
        }
    }

    /* JADX WARN: Finally extract failed */
    public static XmlSimpleList lex(String s, SchemaType itemType, ValidationContext ctx, PrefixResolver resolver) {
        String[] parts = split_list(s);
        XmlAnySimpleType[] newArray = new XmlAnySimpleType[parts.length];
        boolean pushed = false;
        if (resolver != null) {
            NamespaceContext.push(new NamespaceContext(resolver));
            pushed = true;
        }
        for (int i = 0; i < parts.length; i++) {
            try {
                try {
                    newArray[i] = itemType.newValue(parts[i]);
                } catch (XmlValueOutOfRangeException e) {
                    ctx.invalid("list", new Object[]{"item '" + parts[i] + "' is not a valid value of " + QNameHelper.readable(itemType)});
                }
            } catch (Throwable th) {
                if (pushed) {
                    NamespaceContext.pop();
                }
                throw th;
            }
        }
        if (pushed) {
            NamespaceContext.pop();
        }
        return new XmlSimpleList(Arrays.asList(newArray));
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_nil() {
        this._value = null;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.SimpleValue
    public List xgetListValue() {
        check_dated();
        return this._value;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.SimpleValue
    public List getListValue() {
        check_dated();
        if (this._value == null) {
            return null;
        }
        if (this._jvalue != null) {
            return this._jvalue;
        }
        List javaResult = new ArrayList();
        for (int i = 0; i < this._value.size(); i++) {
            javaResult.add(java_value((XmlObject) this._value.get(i)));
        }
        this._jvalue = new XmlSimpleList(javaResult);
        return this._jvalue;
    }

    private static boolean permits_inner_space(XmlObject obj) {
        switch (((SimpleValue) obj).instanceType().getPrimitiveType().getBuiltinTypeCode()) {
            case 1:
            case 2:
            case 6:
            case 12:
                return true;
            default:
                return false;
        }
    }

    private static boolean contains_white_space(String s) {
        return s.indexOf(32) >= 0 || s.indexOf(9) >= 0 || s.indexOf(10) >= 0 || s.indexOf(13) >= 0;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    public void set_list(List list) {
        SchemaType itemType = this._schemaType.getListItemType();
        boolean pushed = false;
        if (has_store()) {
            NamespaceContext.push(new NamespaceContext(get_store()));
            pushed = true;
        }
        try {
            XmlAnySimpleType[] newval = new XmlAnySimpleType[list.size()];
            for (int i = 0; i < list.size(); i++) {
                Object entry = list.get(i);
                if ((entry instanceof XmlObject) && permits_inner_space((XmlObject) list.get(i))) {
                    String stringrep = list.get(i).toString();
                    if (contains_white_space(stringrep)) {
                        throw new XmlValueOutOfRangeException();
                    }
                }
                newval[i] = itemType.newValue(entry);
            }
            XmlSimpleList xList = new XmlSimpleList(Arrays.asList(newval));
            if (pushed) {
                NamespaceContext.pop();
            }
            if (_validateOnSet()) {
                validateValue(xList, this._schemaType, _voorVc);
            }
            this._value = xList;
            this._jvalue = null;
        } catch (Throwable th) {
            if (pushed) {
                NamespaceContext.pop();
            }
            throw th;
        }
    }

    public static void validateValue(XmlSimpleList items, SchemaType sType, ValidationContext context) {
        int i;
        int i2;
        int i3;
        XmlObject[] enumvals = sType.getEnumerationValues();
        if (enumvals != null) {
            int i4 = 0;
            while (true) {
                if (i4 < enumvals.length) {
                    if (equal_xmlLists(items, ((XmlObjectBase) enumvals[i4]).xlistValue())) {
                        break;
                    } else {
                        i4++;
                    }
                } else {
                    context.invalid(XmlErrorCodes.DATATYPE_ENUM_VALID, new Object[]{"list", items, QNameHelper.readable(sType)});
                    break;
                }
            }
        }
        XmlObject o = sType.getFacet(0);
        if (o != null && (i3 = ((SimpleValue) o).getIntValue()) != items.size()) {
            context.invalid(XmlErrorCodes.DATATYPE_LENGTH_VALID$LIST_LENGTH, new Object[]{items, new Integer(items.size()), new Integer(i3), QNameHelper.readable(sType)});
        }
        XmlObject o2 = sType.getFacet(1);
        if (o2 != null && (i2 = ((SimpleValue) o2).getIntValue()) > items.size()) {
            context.invalid(XmlErrorCodes.DATATYPE_MIN_LENGTH_VALID$LIST_LENGTH, new Object[]{items, new Integer(items.size()), new Integer(i2), QNameHelper.readable(sType)});
        }
        XmlObject o3 = sType.getFacet(2);
        if (o3 != null && (i = ((SimpleValue) o3).getIntValue()) < items.size()) {
            context.invalid(XmlErrorCodes.DATATYPE_MAX_LENGTH_VALID$LIST_LENGTH, new Object[]{items, new Integer(items.size()), new Integer(i), QNameHelper.readable(sType)});
        }
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected boolean equal_to(XmlObject obj) {
        return equal_xmlLists(this._value, ((XmlObjectBase) obj).xlistValue());
    }

    private static boolean equal_xmlLists(List a, List b) {
        if (a.size() != b.size()) {
            return false;
        }
        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).equals(b.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected int value_hash_code() {
        int i;
        if (this._value == null) {
            return 0;
        }
        int hash = this._value.size();
        int incr = this._value.size() / 9;
        if (incr < 1) {
            incr = 1;
        }
        int i2 = 0;
        while (true) {
            i = i2;
            if (i >= this._value.size()) {
                break;
            }
            hash = (hash * 19) + this._value.get(i).hashCode();
            i2 = i + incr;
        }
        if (i < this._value.size()) {
            hash = (hash * 19) + this._value.get(i).hashCode();
        }
        return hash;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void validate_simpleval(String lexical, ValidationContext ctx) {
        validateValue((XmlSimpleList) xlistValue(), schemaType(), ctx);
    }
}

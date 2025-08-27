package org.apache.xmlbeans.impl.xb.xmlschema.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlLanguage;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.xmlschema.LangAttribute;
import org.springframework.web.servlet.tags.form.AbstractHtmlElementTag;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlschema/impl/LangAttributeImpl.class */
public class LangAttributeImpl extends XmlComplexContentImpl implements LangAttribute {
    private static final long serialVersionUID = 1;
    private static final QName LANG$0 = new QName("http://www.w3.org/XML/1998/namespace", AbstractHtmlElementTag.LANG_ATTRIBUTE);

    public LangAttributeImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlschema.LangAttribute
    public String getLang() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(LANG$0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlschema.LangAttribute
    public XmlLanguage xgetLang() {
        XmlLanguage target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlLanguage) get_store().find_attribute_user(LANG$0);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlschema.LangAttribute
    public boolean isSetLang() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(LANG$0) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlschema.LangAttribute
    public void setLang(String lang) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(LANG$0);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(LANG$0);
            }
            target.setStringValue(lang);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlschema.LangAttribute
    public void xsetLang(XmlLanguage lang) {
        synchronized (monitor()) {
            check_orphaned();
            XmlLanguage target = (XmlLanguage) get_store().find_attribute_user(LANG$0);
            if (target == null) {
                target = (XmlLanguage) get_store().add_attribute_user(LANG$0);
            }
            target.set(lang);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlschema.LangAttribute
    public void unsetLang() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(LANG$0);
        }
    }
}

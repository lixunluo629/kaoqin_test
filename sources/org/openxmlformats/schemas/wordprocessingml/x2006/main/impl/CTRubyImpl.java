package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRuby;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyContent;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyPr;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/impl/CTRubyImpl.class */
public class CTRubyImpl extends XmlComplexContentImpl implements CTRuby {
    private static final QName RUBYPR$0 = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "rubyPr");
    private static final QName RT$2 = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "rt");
    private static final QName RUBYBASE$4 = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "rubyBase");

    public CTRubyImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRuby
    public CTRubyPr getRubyPr() {
        synchronized (monitor()) {
            check_orphaned();
            CTRubyPr cTRubyPr = (CTRubyPr) get_store().find_element_user(RUBYPR$0, 0);
            if (cTRubyPr == null) {
                return null;
            }
            return cTRubyPr;
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRuby
    public void setRubyPr(CTRubyPr cTRubyPr) {
        synchronized (monitor()) {
            check_orphaned();
            CTRubyPr cTRubyPr2 = (CTRubyPr) get_store().find_element_user(RUBYPR$0, 0);
            if (cTRubyPr2 == null) {
                cTRubyPr2 = (CTRubyPr) get_store().add_element_user(RUBYPR$0);
            }
            cTRubyPr2.set(cTRubyPr);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRuby
    public CTRubyPr addNewRubyPr() {
        CTRubyPr cTRubyPr;
        synchronized (monitor()) {
            check_orphaned();
            cTRubyPr = (CTRubyPr) get_store().add_element_user(RUBYPR$0);
        }
        return cTRubyPr;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRuby
    public CTRubyContent getRt() {
        synchronized (monitor()) {
            check_orphaned();
            CTRubyContent cTRubyContent = (CTRubyContent) get_store().find_element_user(RT$2, 0);
            if (cTRubyContent == null) {
                return null;
            }
            return cTRubyContent;
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRuby
    public void setRt(CTRubyContent cTRubyContent) {
        synchronized (monitor()) {
            check_orphaned();
            CTRubyContent cTRubyContent2 = (CTRubyContent) get_store().find_element_user(RT$2, 0);
            if (cTRubyContent2 == null) {
                cTRubyContent2 = (CTRubyContent) get_store().add_element_user(RT$2);
            }
            cTRubyContent2.set(cTRubyContent);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRuby
    public CTRubyContent addNewRt() {
        CTRubyContent cTRubyContent;
        synchronized (monitor()) {
            check_orphaned();
            cTRubyContent = (CTRubyContent) get_store().add_element_user(RT$2);
        }
        return cTRubyContent;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRuby
    public CTRubyContent getRubyBase() {
        synchronized (monitor()) {
            check_orphaned();
            CTRubyContent cTRubyContent = (CTRubyContent) get_store().find_element_user(RUBYBASE$4, 0);
            if (cTRubyContent == null) {
                return null;
            }
            return cTRubyContent;
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRuby
    public void setRubyBase(CTRubyContent cTRubyContent) {
        synchronized (monitor()) {
            check_orphaned();
            CTRubyContent cTRubyContent2 = (CTRubyContent) get_store().find_element_user(RUBYBASE$4, 0);
            if (cTRubyContent2 == null) {
                cTRubyContent2 = (CTRubyContent) get_store().add_element_user(RUBYBASE$4);
            }
            cTRubyContent2.set(cTRubyContent);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRuby
    public CTRubyContent addNewRubyBase() {
        CTRubyContent cTRubyContent;
        synchronized (monitor()) {
            check_orphaned();
            cTRubyContent = (CTRubyContent) get_store().add_element_user(RUBYBASE$4);
        }
        return cTRubyContent;
    }
}

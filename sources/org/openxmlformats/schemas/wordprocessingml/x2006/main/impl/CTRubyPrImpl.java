package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLang;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyAlign;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyPr;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/impl/CTRubyPrImpl.class */
public class CTRubyPrImpl extends XmlComplexContentImpl implements CTRubyPr {
    private static final QName RUBYALIGN$0 = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "rubyAlign");
    private static final QName HPS$2 = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "hps");
    private static final QName HPSRAISE$4 = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "hpsRaise");
    private static final QName HPSBASETEXT$6 = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "hpsBaseText");
    private static final QName LID$8 = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "lid");
    private static final QName DIRTY$10 = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "dirty");

    public CTRubyPrImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyPr
    public CTRubyAlign getRubyAlign() {
        synchronized (monitor()) {
            check_orphaned();
            CTRubyAlign cTRubyAlign = (CTRubyAlign) get_store().find_element_user(RUBYALIGN$0, 0);
            if (cTRubyAlign == null) {
                return null;
            }
            return cTRubyAlign;
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyPr
    public void setRubyAlign(CTRubyAlign cTRubyAlign) {
        synchronized (monitor()) {
            check_orphaned();
            CTRubyAlign cTRubyAlign2 = (CTRubyAlign) get_store().find_element_user(RUBYALIGN$0, 0);
            if (cTRubyAlign2 == null) {
                cTRubyAlign2 = (CTRubyAlign) get_store().add_element_user(RUBYALIGN$0);
            }
            cTRubyAlign2.set(cTRubyAlign);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyPr
    public CTRubyAlign addNewRubyAlign() {
        CTRubyAlign cTRubyAlign;
        synchronized (monitor()) {
            check_orphaned();
            cTRubyAlign = (CTRubyAlign) get_store().add_element_user(RUBYALIGN$0);
        }
        return cTRubyAlign;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyPr
    public CTHpsMeasure getHps() {
        synchronized (monitor()) {
            check_orphaned();
            CTHpsMeasure cTHpsMeasure = (CTHpsMeasure) get_store().find_element_user(HPS$2, 0);
            if (cTHpsMeasure == null) {
                return null;
            }
            return cTHpsMeasure;
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyPr
    public void setHps(CTHpsMeasure cTHpsMeasure) {
        synchronized (monitor()) {
            check_orphaned();
            CTHpsMeasure cTHpsMeasure2 = (CTHpsMeasure) get_store().find_element_user(HPS$2, 0);
            if (cTHpsMeasure2 == null) {
                cTHpsMeasure2 = (CTHpsMeasure) get_store().add_element_user(HPS$2);
            }
            cTHpsMeasure2.set(cTHpsMeasure);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyPr
    public CTHpsMeasure addNewHps() {
        CTHpsMeasure cTHpsMeasure;
        synchronized (monitor()) {
            check_orphaned();
            cTHpsMeasure = (CTHpsMeasure) get_store().add_element_user(HPS$2);
        }
        return cTHpsMeasure;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyPr
    public CTHpsMeasure getHpsRaise() {
        synchronized (monitor()) {
            check_orphaned();
            CTHpsMeasure cTHpsMeasure = (CTHpsMeasure) get_store().find_element_user(HPSRAISE$4, 0);
            if (cTHpsMeasure == null) {
                return null;
            }
            return cTHpsMeasure;
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyPr
    public void setHpsRaise(CTHpsMeasure cTHpsMeasure) {
        synchronized (monitor()) {
            check_orphaned();
            CTHpsMeasure cTHpsMeasure2 = (CTHpsMeasure) get_store().find_element_user(HPSRAISE$4, 0);
            if (cTHpsMeasure2 == null) {
                cTHpsMeasure2 = (CTHpsMeasure) get_store().add_element_user(HPSRAISE$4);
            }
            cTHpsMeasure2.set(cTHpsMeasure);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyPr
    public CTHpsMeasure addNewHpsRaise() {
        CTHpsMeasure cTHpsMeasure;
        synchronized (monitor()) {
            check_orphaned();
            cTHpsMeasure = (CTHpsMeasure) get_store().add_element_user(HPSRAISE$4);
        }
        return cTHpsMeasure;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyPr
    public CTHpsMeasure getHpsBaseText() {
        synchronized (monitor()) {
            check_orphaned();
            CTHpsMeasure cTHpsMeasure = (CTHpsMeasure) get_store().find_element_user(HPSBASETEXT$6, 0);
            if (cTHpsMeasure == null) {
                return null;
            }
            return cTHpsMeasure;
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyPr
    public void setHpsBaseText(CTHpsMeasure cTHpsMeasure) {
        synchronized (monitor()) {
            check_orphaned();
            CTHpsMeasure cTHpsMeasure2 = (CTHpsMeasure) get_store().find_element_user(HPSBASETEXT$6, 0);
            if (cTHpsMeasure2 == null) {
                cTHpsMeasure2 = (CTHpsMeasure) get_store().add_element_user(HPSBASETEXT$6);
            }
            cTHpsMeasure2.set(cTHpsMeasure);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyPr
    public CTHpsMeasure addNewHpsBaseText() {
        CTHpsMeasure cTHpsMeasure;
        synchronized (monitor()) {
            check_orphaned();
            cTHpsMeasure = (CTHpsMeasure) get_store().add_element_user(HPSBASETEXT$6);
        }
        return cTHpsMeasure;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyPr
    public CTLang getLid() {
        synchronized (monitor()) {
            check_orphaned();
            CTLang cTLang = (CTLang) get_store().find_element_user(LID$8, 0);
            if (cTLang == null) {
                return null;
            }
            return cTLang;
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyPr
    public void setLid(CTLang cTLang) {
        synchronized (monitor()) {
            check_orphaned();
            CTLang cTLang2 = (CTLang) get_store().find_element_user(LID$8, 0);
            if (cTLang2 == null) {
                cTLang2 = (CTLang) get_store().add_element_user(LID$8);
            }
            cTLang2.set(cTLang);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyPr
    public CTLang addNewLid() {
        CTLang cTLang;
        synchronized (monitor()) {
            check_orphaned();
            cTLang = (CTLang) get_store().add_element_user(LID$8);
        }
        return cTLang;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyPr
    public CTOnOff getDirty() {
        synchronized (monitor()) {
            check_orphaned();
            CTOnOff cTOnOff = (CTOnOff) get_store().find_element_user(DIRTY$10, 0);
            if (cTOnOff == null) {
                return null;
            }
            return cTOnOff;
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyPr
    public boolean isSetDirty() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(DIRTY$10) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyPr
    public void setDirty(CTOnOff cTOnOff) {
        synchronized (monitor()) {
            check_orphaned();
            CTOnOff cTOnOff2 = (CTOnOff) get_store().find_element_user(DIRTY$10, 0);
            if (cTOnOff2 == null) {
                cTOnOff2 = (CTOnOff) get_store().add_element_user(DIRTY$10);
            }
            cTOnOff2.set(cTOnOff);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyPr
    public CTOnOff addNewDirty() {
        CTOnOff cTOnOff;
        synchronized (monitor()) {
            check_orphaned();
            cTOnOff = (CTOnOff) get_store().add_element_user(DIRTY$10);
        }
        return cTOnOff;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyPr
    public void unsetDirty() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(DIRTY$10, 0);
        }
    }
}

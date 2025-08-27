package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocDefaults;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLatentStyles;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles;
import org.springframework.web.servlet.tags.form.AbstractHtmlElementTag;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/impl/CTStylesImpl.class */
public class CTStylesImpl extends XmlComplexContentImpl implements CTStyles {
    private static final QName DOCDEFAULTS$0 = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "docDefaults");
    private static final QName LATENTSTYLES$2 = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "latentStyles");
    private static final QName STYLE$4 = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", AbstractHtmlElementTag.STYLE_ATTRIBUTE);

    public CTStylesImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles
    public CTDocDefaults getDocDefaults() {
        synchronized (monitor()) {
            check_orphaned();
            CTDocDefaults cTDocDefaults = (CTDocDefaults) get_store().find_element_user(DOCDEFAULTS$0, 0);
            if (cTDocDefaults == null) {
                return null;
            }
            return cTDocDefaults;
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles
    public boolean isSetDocDefaults() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(DOCDEFAULTS$0) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles
    public void setDocDefaults(CTDocDefaults cTDocDefaults) {
        synchronized (monitor()) {
            check_orphaned();
            CTDocDefaults cTDocDefaults2 = (CTDocDefaults) get_store().find_element_user(DOCDEFAULTS$0, 0);
            if (cTDocDefaults2 == null) {
                cTDocDefaults2 = (CTDocDefaults) get_store().add_element_user(DOCDEFAULTS$0);
            }
            cTDocDefaults2.set(cTDocDefaults);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles
    public CTDocDefaults addNewDocDefaults() {
        CTDocDefaults cTDocDefaults;
        synchronized (monitor()) {
            check_orphaned();
            cTDocDefaults = (CTDocDefaults) get_store().add_element_user(DOCDEFAULTS$0);
        }
        return cTDocDefaults;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles
    public void unsetDocDefaults() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(DOCDEFAULTS$0, 0);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles
    public CTLatentStyles getLatentStyles() {
        synchronized (monitor()) {
            check_orphaned();
            CTLatentStyles cTLatentStyles = (CTLatentStyles) get_store().find_element_user(LATENTSTYLES$2, 0);
            if (cTLatentStyles == null) {
                return null;
            }
            return cTLatentStyles;
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles
    public boolean isSetLatentStyles() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(LATENTSTYLES$2) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles
    public void setLatentStyles(CTLatentStyles cTLatentStyles) {
        synchronized (monitor()) {
            check_orphaned();
            CTLatentStyles cTLatentStyles2 = (CTLatentStyles) get_store().find_element_user(LATENTSTYLES$2, 0);
            if (cTLatentStyles2 == null) {
                cTLatentStyles2 = (CTLatentStyles) get_store().add_element_user(LATENTSTYLES$2);
            }
            cTLatentStyles2.set(cTLatentStyles);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles
    public CTLatentStyles addNewLatentStyles() {
        CTLatentStyles cTLatentStyles;
        synchronized (monitor()) {
            check_orphaned();
            cTLatentStyles = (CTLatentStyles) get_store().add_element_user(LATENTSTYLES$2);
        }
        return cTLatentStyles;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles
    public void unsetLatentStyles() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(LATENTSTYLES$2, 0);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles
    public List<CTStyle> getStyleList() {
        1StyleList r0;
        synchronized (monitor()) {
            check_orphaned();
            r0 = new 1StyleList(this);
        }
        return r0;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles
    public CTStyle[] getStyleArray() {
        CTStyle[] cTStyleArr;
        synchronized (monitor()) {
            check_orphaned();
            ArrayList arrayList = new ArrayList();
            get_store().find_all_element_users(STYLE$4, arrayList);
            cTStyleArr = new CTStyle[arrayList.size()];
            arrayList.toArray(cTStyleArr);
        }
        return cTStyleArr;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles
    public CTStyle getStyleArray(int i) {
        CTStyle cTStyle;
        synchronized (monitor()) {
            check_orphaned();
            cTStyle = (CTStyle) get_store().find_element_user(STYLE$4, i);
            if (cTStyle == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return cTStyle;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles
    public int sizeOfStyleArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(STYLE$4);
        }
        return iCount_elements;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles
    public void setStyleArray(CTStyle[] cTStyleArr) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(cTStyleArr, STYLE$4);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles
    public void setStyleArray(int i, CTStyle cTStyle) {
        synchronized (monitor()) {
            check_orphaned();
            CTStyle cTStyle2 = (CTStyle) get_store().find_element_user(STYLE$4, i);
            if (cTStyle2 == null) {
                throw new IndexOutOfBoundsException();
            }
            cTStyle2.set(cTStyle);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles
    public CTStyle insertNewStyle(int i) {
        CTStyle cTStyle;
        synchronized (monitor()) {
            check_orphaned();
            cTStyle = (CTStyle) get_store().insert_element_user(STYLE$4, i);
        }
        return cTStyle;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles
    public CTStyle addNewStyle() {
        CTStyle cTStyle;
        synchronized (monitor()) {
            check_orphaned();
            cTStyle = (CTStyle) get_store().add_element_user(STYLE$4);
        }
        return cTStyle;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles
    public void removeStyle(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(STYLE$4, i);
        }
    }
}

package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtCell;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtContentCell;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtEndPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtPr;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/impl/CTSdtCellImpl.class */
public class CTSdtCellImpl extends XmlComplexContentImpl implements CTSdtCell {
    private static final QName SDTPR$0 = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "sdtPr");
    private static final QName SDTENDPR$2 = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "sdtEndPr");
    private static final QName SDTCONTENT$4 = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "sdtContent");

    public CTSdtCellImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtCell
    public CTSdtPr getSdtPr() {
        synchronized (monitor()) {
            check_orphaned();
            CTSdtPr cTSdtPr = (CTSdtPr) get_store().find_element_user(SDTPR$0, 0);
            if (cTSdtPr == null) {
                return null;
            }
            return cTSdtPr;
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtCell
    public boolean isSetSdtPr() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(SDTPR$0) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtCell
    public void setSdtPr(CTSdtPr cTSdtPr) {
        synchronized (monitor()) {
            check_orphaned();
            CTSdtPr cTSdtPr2 = (CTSdtPr) get_store().find_element_user(SDTPR$0, 0);
            if (cTSdtPr2 == null) {
                cTSdtPr2 = (CTSdtPr) get_store().add_element_user(SDTPR$0);
            }
            cTSdtPr2.set(cTSdtPr);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtCell
    public CTSdtPr addNewSdtPr() {
        CTSdtPr cTSdtPr;
        synchronized (monitor()) {
            check_orphaned();
            cTSdtPr = (CTSdtPr) get_store().add_element_user(SDTPR$0);
        }
        return cTSdtPr;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtCell
    public void unsetSdtPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(SDTPR$0, 0);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtCell
    public CTSdtEndPr getSdtEndPr() {
        synchronized (monitor()) {
            check_orphaned();
            CTSdtEndPr cTSdtEndPr = (CTSdtEndPr) get_store().find_element_user(SDTENDPR$2, 0);
            if (cTSdtEndPr == null) {
                return null;
            }
            return cTSdtEndPr;
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtCell
    public boolean isSetSdtEndPr() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(SDTENDPR$2) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtCell
    public void setSdtEndPr(CTSdtEndPr cTSdtEndPr) {
        synchronized (monitor()) {
            check_orphaned();
            CTSdtEndPr cTSdtEndPr2 = (CTSdtEndPr) get_store().find_element_user(SDTENDPR$2, 0);
            if (cTSdtEndPr2 == null) {
                cTSdtEndPr2 = (CTSdtEndPr) get_store().add_element_user(SDTENDPR$2);
            }
            cTSdtEndPr2.set(cTSdtEndPr);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtCell
    public CTSdtEndPr addNewSdtEndPr() {
        CTSdtEndPr cTSdtEndPr;
        synchronized (monitor()) {
            check_orphaned();
            cTSdtEndPr = (CTSdtEndPr) get_store().add_element_user(SDTENDPR$2);
        }
        return cTSdtEndPr;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtCell
    public void unsetSdtEndPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(SDTENDPR$2, 0);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtCell
    public CTSdtContentCell getSdtContent() {
        synchronized (monitor()) {
            check_orphaned();
            CTSdtContentCell cTSdtContentCell = (CTSdtContentCell) get_store().find_element_user(SDTCONTENT$4, 0);
            if (cTSdtContentCell == null) {
                return null;
            }
            return cTSdtContentCell;
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtCell
    public boolean isSetSdtContent() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(SDTCONTENT$4) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtCell
    public void setSdtContent(CTSdtContentCell cTSdtContentCell) {
        synchronized (monitor()) {
            check_orphaned();
            CTSdtContentCell cTSdtContentCell2 = (CTSdtContentCell) get_store().find_element_user(SDTCONTENT$4, 0);
            if (cTSdtContentCell2 == null) {
                cTSdtContentCell2 = (CTSdtContentCell) get_store().add_element_user(SDTCONTENT$4);
            }
            cTSdtContentCell2.set(cTSdtContentCell);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtCell
    public CTSdtContentCell addNewSdtContent() {
        CTSdtContentCell cTSdtContentCell;
        synchronized (monitor()) {
            check_orphaned();
            cTSdtContentCell = (CTSdtContentCell) get_store().add_element_user(SDTCONTENT$4);
        }
        return cTSdtContentCell;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtCell
    public void unsetSdtContent() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(SDTCONTENT$4, 0);
        }
    }
}

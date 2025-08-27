package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeStyle;
import org.openxmlformats.schemas.presentationml.x2006.main.CTConnector;
import org.openxmlformats.schemas.presentationml.x2006.main.CTConnectorNonVisual;
import org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify;
import org.springframework.web.servlet.tags.form.AbstractHtmlElementTag;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/impl/CTConnectorImpl.class */
public class CTConnectorImpl extends XmlComplexContentImpl implements CTConnector {
    private static final QName NVCXNSPPR$0 = new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "nvCxnSpPr");
    private static final QName SPPR$2 = new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "spPr");
    private static final QName STYLE$4 = new QName("http://schemas.openxmlformats.org/presentationml/2006/main", AbstractHtmlElementTag.STYLE_ATTRIBUTE);
    private static final QName EXTLST$6 = new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "extLst");

    public CTConnectorImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.presentationml.x2006.main.CTConnector
    public CTConnectorNonVisual getNvCxnSpPr() {
        synchronized (monitor()) {
            check_orphaned();
            CTConnectorNonVisual cTConnectorNonVisual = (CTConnectorNonVisual) get_store().find_element_user(NVCXNSPPR$0, 0);
            if (cTConnectorNonVisual == null) {
                return null;
            }
            return cTConnectorNonVisual;
        }
    }

    @Override // org.openxmlformats.schemas.presentationml.x2006.main.CTConnector
    public void setNvCxnSpPr(CTConnectorNonVisual cTConnectorNonVisual) {
        synchronized (monitor()) {
            check_orphaned();
            CTConnectorNonVisual cTConnectorNonVisual2 = (CTConnectorNonVisual) get_store().find_element_user(NVCXNSPPR$0, 0);
            if (cTConnectorNonVisual2 == null) {
                cTConnectorNonVisual2 = (CTConnectorNonVisual) get_store().add_element_user(NVCXNSPPR$0);
            }
            cTConnectorNonVisual2.set(cTConnectorNonVisual);
        }
    }

    @Override // org.openxmlformats.schemas.presentationml.x2006.main.CTConnector
    public CTConnectorNonVisual addNewNvCxnSpPr() {
        CTConnectorNonVisual cTConnectorNonVisual;
        synchronized (monitor()) {
            check_orphaned();
            cTConnectorNonVisual = (CTConnectorNonVisual) get_store().add_element_user(NVCXNSPPR$0);
        }
        return cTConnectorNonVisual;
    }

    @Override // org.openxmlformats.schemas.presentationml.x2006.main.CTConnector
    public CTShapeProperties getSpPr() {
        synchronized (monitor()) {
            check_orphaned();
            CTShapeProperties cTShapeProperties = (CTShapeProperties) get_store().find_element_user(SPPR$2, 0);
            if (cTShapeProperties == null) {
                return null;
            }
            return cTShapeProperties;
        }
    }

    @Override // org.openxmlformats.schemas.presentationml.x2006.main.CTConnector
    public void setSpPr(CTShapeProperties cTShapeProperties) {
        synchronized (monitor()) {
            check_orphaned();
            CTShapeProperties cTShapeProperties2 = (CTShapeProperties) get_store().find_element_user(SPPR$2, 0);
            if (cTShapeProperties2 == null) {
                cTShapeProperties2 = (CTShapeProperties) get_store().add_element_user(SPPR$2);
            }
            cTShapeProperties2.set(cTShapeProperties);
        }
    }

    @Override // org.openxmlformats.schemas.presentationml.x2006.main.CTConnector
    public CTShapeProperties addNewSpPr() {
        CTShapeProperties cTShapeProperties;
        synchronized (monitor()) {
            check_orphaned();
            cTShapeProperties = (CTShapeProperties) get_store().add_element_user(SPPR$2);
        }
        return cTShapeProperties;
    }

    @Override // org.openxmlformats.schemas.presentationml.x2006.main.CTConnector
    public CTShapeStyle getStyle() {
        synchronized (monitor()) {
            check_orphaned();
            CTShapeStyle cTShapeStyle = (CTShapeStyle) get_store().find_element_user(STYLE$4, 0);
            if (cTShapeStyle == null) {
                return null;
            }
            return cTShapeStyle;
        }
    }

    @Override // org.openxmlformats.schemas.presentationml.x2006.main.CTConnector
    public boolean isSetStyle() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(STYLE$4) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.presentationml.x2006.main.CTConnector
    public void setStyle(CTShapeStyle cTShapeStyle) {
        synchronized (monitor()) {
            check_orphaned();
            CTShapeStyle cTShapeStyle2 = (CTShapeStyle) get_store().find_element_user(STYLE$4, 0);
            if (cTShapeStyle2 == null) {
                cTShapeStyle2 = (CTShapeStyle) get_store().add_element_user(STYLE$4);
            }
            cTShapeStyle2.set(cTShapeStyle);
        }
    }

    @Override // org.openxmlformats.schemas.presentationml.x2006.main.CTConnector
    public CTShapeStyle addNewStyle() {
        CTShapeStyle cTShapeStyle;
        synchronized (monitor()) {
            check_orphaned();
            cTShapeStyle = (CTShapeStyle) get_store().add_element_user(STYLE$4);
        }
        return cTShapeStyle;
    }

    @Override // org.openxmlformats.schemas.presentationml.x2006.main.CTConnector
    public void unsetStyle() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(STYLE$4, 0);
        }
    }

    @Override // org.openxmlformats.schemas.presentationml.x2006.main.CTConnector
    public CTExtensionListModify getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            CTExtensionListModify cTExtensionListModifyFind_element_user = get_store().find_element_user(EXTLST$6, 0);
            if (cTExtensionListModifyFind_element_user == null) {
                return null;
            }
            return cTExtensionListModifyFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.presentationml.x2006.main.CTConnector
    public boolean isSetExtLst() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(EXTLST$6) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.presentationml.x2006.main.CTConnector
    public void setExtLst(CTExtensionListModify cTExtensionListModify) {
        synchronized (monitor()) {
            check_orphaned();
            CTExtensionListModify cTExtensionListModifyFind_element_user = get_store().find_element_user(EXTLST$6, 0);
            if (cTExtensionListModifyFind_element_user == null) {
                cTExtensionListModifyFind_element_user = (CTExtensionListModify) get_store().add_element_user(EXTLST$6);
            }
            cTExtensionListModifyFind_element_user.set(cTExtensionListModify);
        }
    }

    @Override // org.openxmlformats.schemas.presentationml.x2006.main.CTConnector
    public CTExtensionListModify addNewExtLst() {
        CTExtensionListModify cTExtensionListModifyAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTExtensionListModifyAdd_element_user = get_store().add_element_user(EXTLST$6);
        }
        return cTExtensionListModifyAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.presentationml.x2006.main.CTConnector
    public void unsetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(EXTLST$6, 0);
        }
    }
}

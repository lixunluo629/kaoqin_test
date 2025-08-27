package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.main.CTConnectionSite;
import org.openxmlformats.schemas.drawingml.x2006.main.CTConnectionSiteList;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/impl/CTConnectionSiteListImpl.class */
public class CTConnectionSiteListImpl extends XmlComplexContentImpl implements CTConnectionSiteList {
    private static final QName CXN$0 = new QName(XSSFRelation.NS_DRAWINGML, "cxn");

    public CTConnectionSiteListImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTConnectionSiteList
    public List<CTConnectionSite> getCxnList() {
        1CxnList r0;
        synchronized (monitor()) {
            check_orphaned();
            r0 = new 1CxnList(this);
        }
        return r0;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTConnectionSiteList
    public CTConnectionSite[] getCxnArray() {
        CTConnectionSite[] cTConnectionSiteArr;
        synchronized (monitor()) {
            check_orphaned();
            ArrayList arrayList = new ArrayList();
            get_store().find_all_element_users(CXN$0, arrayList);
            cTConnectionSiteArr = new CTConnectionSite[arrayList.size()];
            arrayList.toArray(cTConnectionSiteArr);
        }
        return cTConnectionSiteArr;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTConnectionSiteList
    public CTConnectionSite getCxnArray(int i) {
        CTConnectionSite cTConnectionSiteFind_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTConnectionSiteFind_element_user = get_store().find_element_user(CXN$0, i);
            if (cTConnectionSiteFind_element_user == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return cTConnectionSiteFind_element_user;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTConnectionSiteList
    public int sizeOfCxnArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(CXN$0);
        }
        return iCount_elements;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTConnectionSiteList
    public void setCxnArray(CTConnectionSite[] cTConnectionSiteArr) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper((XmlObject[]) cTConnectionSiteArr, CXN$0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTConnectionSiteList
    public void setCxnArray(int i, CTConnectionSite cTConnectionSite) {
        synchronized (monitor()) {
            check_orphaned();
            CTConnectionSite cTConnectionSiteFind_element_user = get_store().find_element_user(CXN$0, i);
            if (cTConnectionSiteFind_element_user == null) {
                throw new IndexOutOfBoundsException();
            }
            cTConnectionSiteFind_element_user.set(cTConnectionSite);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTConnectionSiteList
    public CTConnectionSite insertNewCxn(int i) {
        CTConnectionSite cTConnectionSiteInsert_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTConnectionSiteInsert_element_user = get_store().insert_element_user(CXN$0, i);
        }
        return cTConnectionSiteInsert_element_user;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTConnectionSiteList
    public CTConnectionSite addNewCxn() {
        CTConnectionSite cTConnectionSiteAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTConnectionSiteAdd_element_user = get_store().add_element_user(CXN$0);
        }
        return cTConnectionSiteAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTConnectionSiteList
    public void removeCxn(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(CXN$0, i);
        }
    }
}

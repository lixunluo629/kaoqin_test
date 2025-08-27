package com.microsoft.schemas.vml.impl;

import com.microsoft.schemas.vml.CTH;
import com.microsoft.schemas.vml.CTHandles;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/vml/impl/CTHandlesImpl.class */
public class CTHandlesImpl extends XmlComplexContentImpl implements CTHandles {
    private static final QName H$0 = new QName("urn:schemas-microsoft-com:vml", "h");

    public CTHandlesImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // com.microsoft.schemas.vml.CTHandles
    public List<CTH> getHList() {
        1HList r0;
        synchronized (monitor()) {
            check_orphaned();
            r0 = new 1HList(this);
        }
        return r0;
    }

    @Override // com.microsoft.schemas.vml.CTHandles
    public CTH[] getHArray() {
        CTH[] cthArr;
        synchronized (monitor()) {
            check_orphaned();
            ArrayList arrayList = new ArrayList();
            get_store().find_all_element_users(H$0, arrayList);
            cthArr = new CTH[arrayList.size()];
            arrayList.toArray(cthArr);
        }
        return cthArr;
    }

    @Override // com.microsoft.schemas.vml.CTHandles
    public CTH getHArray(int i) {
        CTH cth;
        synchronized (monitor()) {
            check_orphaned();
            cth = (CTH) get_store().find_element_user(H$0, i);
            if (cth == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return cth;
    }

    @Override // com.microsoft.schemas.vml.CTHandles
    public int sizeOfHArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(H$0);
        }
        return iCount_elements;
    }

    @Override // com.microsoft.schemas.vml.CTHandles
    public void setHArray(CTH[] cthArr) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(cthArr, H$0);
        }
    }

    @Override // com.microsoft.schemas.vml.CTHandles
    public void setHArray(int i, CTH cth) {
        synchronized (monitor()) {
            check_orphaned();
            CTH cth2 = (CTH) get_store().find_element_user(H$0, i);
            if (cth2 == null) {
                throw new IndexOutOfBoundsException();
            }
            cth2.set(cth);
        }
    }

    @Override // com.microsoft.schemas.vml.CTHandles
    public CTH insertNewH(int i) {
        CTH cth;
        synchronized (monitor()) {
            check_orphaned();
            cth = (CTH) get_store().insert_element_user(H$0, i);
        }
        return cth;
    }

    @Override // com.microsoft.schemas.vml.CTHandles
    public CTH addNewH() {
        CTH cth;
        synchronized (monitor()) {
            check_orphaned();
            cth = (CTH) get_store().add_element_user(H$0);
        }
        return cth;
    }

    @Override // com.microsoft.schemas.vml.CTHandles
    public void removeH(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(H$0, i);
        }
    }
}

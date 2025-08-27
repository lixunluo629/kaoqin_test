package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalDefinedName;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalDefinedNames;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/impl/CTExternalDefinedNamesImpl.class */
public class CTExternalDefinedNamesImpl extends XmlComplexContentImpl implements CTExternalDefinedNames {
    private static final QName DEFINEDNAME$0 = new QName(XSSFRelation.NS_SPREADSHEETML, "definedName");

    public CTExternalDefinedNamesImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalDefinedNames
    public List<CTExternalDefinedName> getDefinedNameList() {
        1DefinedNameList r0;
        synchronized (monitor()) {
            check_orphaned();
            r0 = new 1DefinedNameList(this);
        }
        return r0;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalDefinedNames
    public CTExternalDefinedName[] getDefinedNameArray() {
        CTExternalDefinedName[] cTExternalDefinedNameArr;
        synchronized (monitor()) {
            check_orphaned();
            ArrayList arrayList = new ArrayList();
            get_store().find_all_element_users(DEFINEDNAME$0, arrayList);
            cTExternalDefinedNameArr = new CTExternalDefinedName[arrayList.size()];
            arrayList.toArray(cTExternalDefinedNameArr);
        }
        return cTExternalDefinedNameArr;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalDefinedNames
    public CTExternalDefinedName getDefinedNameArray(int i) {
        CTExternalDefinedName cTExternalDefinedName;
        synchronized (monitor()) {
            check_orphaned();
            cTExternalDefinedName = (CTExternalDefinedName) get_store().find_element_user(DEFINEDNAME$0, i);
            if (cTExternalDefinedName == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return cTExternalDefinedName;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalDefinedNames
    public int sizeOfDefinedNameArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(DEFINEDNAME$0);
        }
        return iCount_elements;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalDefinedNames
    public void setDefinedNameArray(CTExternalDefinedName[] cTExternalDefinedNameArr) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(cTExternalDefinedNameArr, DEFINEDNAME$0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalDefinedNames
    public void setDefinedNameArray(int i, CTExternalDefinedName cTExternalDefinedName) {
        synchronized (monitor()) {
            check_orphaned();
            CTExternalDefinedName cTExternalDefinedName2 = (CTExternalDefinedName) get_store().find_element_user(DEFINEDNAME$0, i);
            if (cTExternalDefinedName2 == null) {
                throw new IndexOutOfBoundsException();
            }
            cTExternalDefinedName2.set(cTExternalDefinedName);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalDefinedNames
    public CTExternalDefinedName insertNewDefinedName(int i) {
        CTExternalDefinedName cTExternalDefinedName;
        synchronized (monitor()) {
            check_orphaned();
            cTExternalDefinedName = (CTExternalDefinedName) get_store().insert_element_user(DEFINEDNAME$0, i);
        }
        return cTExternalDefinedName;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalDefinedNames
    public CTExternalDefinedName addNewDefinedName() {
        CTExternalDefinedName cTExternalDefinedName;
        synchronized (monitor()) {
            check_orphaned();
            cTExternalDefinedName = (CTExternalDefinedName) get_store().add_element_user(DEFINEDNAME$0);
        }
        return cTExternalDefinedName;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalDefinedNames
    public void removeDefinedName(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(DEFINEDNAME$0, i);
        }
    }
}

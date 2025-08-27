package com.microsoft.schemas.vml.impl;

import com.alibaba.excel.constant.ExcelXmlConstants;
import com.microsoft.schemas.vml.CTF;
import com.microsoft.schemas.vml.CTFormulas;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/vml/impl/CTFormulasImpl.class */
public class CTFormulasImpl extends XmlComplexContentImpl implements CTFormulas {
    private static final QName F$0 = new QName("urn:schemas-microsoft-com:vml", ExcelXmlConstants.CELL_FORMULA_TAG);

    public CTFormulasImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // com.microsoft.schemas.vml.CTFormulas
    public List<CTF> getFList() {
        1FList r0;
        synchronized (monitor()) {
            check_orphaned();
            r0 = new 1FList(this);
        }
        return r0;
    }

    @Override // com.microsoft.schemas.vml.CTFormulas
    public CTF[] getFArray() {
        CTF[] ctfArr;
        synchronized (monitor()) {
            check_orphaned();
            ArrayList arrayList = new ArrayList();
            get_store().find_all_element_users(F$0, arrayList);
            ctfArr = new CTF[arrayList.size()];
            arrayList.toArray(ctfArr);
        }
        return ctfArr;
    }

    @Override // com.microsoft.schemas.vml.CTFormulas
    public CTF getFArray(int i) {
        CTF ctf;
        synchronized (monitor()) {
            check_orphaned();
            ctf = (CTF) get_store().find_element_user(F$0, i);
            if (ctf == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return ctf;
    }

    @Override // com.microsoft.schemas.vml.CTFormulas
    public int sizeOfFArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(F$0);
        }
        return iCount_elements;
    }

    @Override // com.microsoft.schemas.vml.CTFormulas
    public void setFArray(CTF[] ctfArr) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(ctfArr, F$0);
        }
    }

    @Override // com.microsoft.schemas.vml.CTFormulas
    public void setFArray(int i, CTF ctf) {
        synchronized (monitor()) {
            check_orphaned();
            CTF ctf2 = (CTF) get_store().find_element_user(F$0, i);
            if (ctf2 == null) {
                throw new IndexOutOfBoundsException();
            }
            ctf2.set(ctf);
        }
    }

    @Override // com.microsoft.schemas.vml.CTFormulas
    public CTF insertNewF(int i) {
        CTF ctf;
        synchronized (monitor()) {
            check_orphaned();
            ctf = (CTF) get_store().insert_element_user(F$0, i);
        }
        return ctf;
    }

    @Override // com.microsoft.schemas.vml.CTFormulas
    public CTF addNewF() {
        CTF ctf;
        synchronized (monitor()) {
            check_orphaned();
            ctf = (CTF) get_store().add_element_user(F$0);
        }
        return ctf;
    }

    @Override // com.microsoft.schemas.vml.CTFormulas
    public void removeF(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(F$0, i);
        }
    }
}

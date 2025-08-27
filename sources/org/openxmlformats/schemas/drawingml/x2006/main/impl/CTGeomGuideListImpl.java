package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuide;
import org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuideList;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/impl/CTGeomGuideListImpl.class */
public class CTGeomGuideListImpl extends XmlComplexContentImpl implements CTGeomGuideList {
    private static final QName GD$0 = new QName(XSSFRelation.NS_DRAWINGML, "gd");

    public CTGeomGuideListImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuideList
    public List<CTGeomGuide> getGdList() {
        1GdList r0;
        synchronized (monitor()) {
            check_orphaned();
            r0 = new 1GdList(this);
        }
        return r0;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuideList
    public CTGeomGuide[] getGdArray() {
        CTGeomGuide[] cTGeomGuideArr;
        synchronized (monitor()) {
            check_orphaned();
            ArrayList arrayList = new ArrayList();
            get_store().find_all_element_users(GD$0, arrayList);
            cTGeomGuideArr = new CTGeomGuide[arrayList.size()];
            arrayList.toArray(cTGeomGuideArr);
        }
        return cTGeomGuideArr;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuideList
    public CTGeomGuide getGdArray(int i) {
        CTGeomGuide cTGeomGuide;
        synchronized (monitor()) {
            check_orphaned();
            cTGeomGuide = (CTGeomGuide) get_store().find_element_user(GD$0, i);
            if (cTGeomGuide == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return cTGeomGuide;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuideList
    public int sizeOfGdArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(GD$0);
        }
        return iCount_elements;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuideList
    public void setGdArray(CTGeomGuide[] cTGeomGuideArr) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(cTGeomGuideArr, GD$0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuideList
    public void setGdArray(int i, CTGeomGuide cTGeomGuide) {
        synchronized (monitor()) {
            check_orphaned();
            CTGeomGuide cTGeomGuide2 = (CTGeomGuide) get_store().find_element_user(GD$0, i);
            if (cTGeomGuide2 == null) {
                throw new IndexOutOfBoundsException();
            }
            cTGeomGuide2.set(cTGeomGuide);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuideList
    public CTGeomGuide insertNewGd(int i) {
        CTGeomGuide cTGeomGuide;
        synchronized (monitor()) {
            check_orphaned();
            cTGeomGuide = (CTGeomGuide) get_store().insert_element_user(GD$0, i);
        }
        return cTGeomGuide;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuideList
    public CTGeomGuide addNewGd() {
        CTGeomGuide cTGeomGuide;
        synchronized (monitor()) {
            check_orphaned();
            cTGeomGuide = (CTGeomGuide) get_store().add_element_user(GD$0);
        }
        return cTGeomGuide;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuideList
    public void removeGd(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(GD$0, i);
        }
    }
}

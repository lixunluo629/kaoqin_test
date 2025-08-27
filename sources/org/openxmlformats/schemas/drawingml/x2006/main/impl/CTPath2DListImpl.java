package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.commons.httpclient.cookie.Cookie2;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPath2D;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DList;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/impl/CTPath2DListImpl.class */
public class CTPath2DListImpl extends XmlComplexContentImpl implements CTPath2DList {
    private static final QName PATH$0 = new QName(XSSFRelation.NS_DRAWINGML, Cookie2.PATH);

    public CTPath2DListImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DList
    public List<CTPath2D> getPathList() {
        1PathList r0;
        synchronized (monitor()) {
            check_orphaned();
            r0 = new 1PathList(this);
        }
        return r0;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DList
    public CTPath2D[] getPathArray() {
        CTPath2D[] cTPath2DArr;
        synchronized (monitor()) {
            check_orphaned();
            ArrayList arrayList = new ArrayList();
            get_store().find_all_element_users(PATH$0, arrayList);
            cTPath2DArr = new CTPath2D[arrayList.size()];
            arrayList.toArray(cTPath2DArr);
        }
        return cTPath2DArr;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DList
    public CTPath2D getPathArray(int i) {
        CTPath2D cTPath2D;
        synchronized (monitor()) {
            check_orphaned();
            cTPath2D = (CTPath2D) get_store().find_element_user(PATH$0, i);
            if (cTPath2D == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return cTPath2D;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DList
    public int sizeOfPathArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(PATH$0);
        }
        return iCount_elements;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DList
    public void setPathArray(CTPath2D[] cTPath2DArr) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(cTPath2DArr, PATH$0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DList
    public void setPathArray(int i, CTPath2D cTPath2D) {
        synchronized (monitor()) {
            check_orphaned();
            CTPath2D cTPath2D2 = (CTPath2D) get_store().find_element_user(PATH$0, i);
            if (cTPath2D2 == null) {
                throw new IndexOutOfBoundsException();
            }
            cTPath2D2.set(cTPath2D);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DList
    public CTPath2D insertNewPath(int i) {
        CTPath2D cTPath2D;
        synchronized (monitor()) {
            check_orphaned();
            cTPath2D = (CTPath2D) get_store().insert_element_user(PATH$0, i);
        }
        return cTPath2D;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DList
    public CTPath2D addNewPath() {
        CTPath2D cTPath2D;
        synchronized (monitor()) {
            check_orphaned();
            cTPath2D = (CTPath2D) get_store().add_element_user(PATH$0);
        }
        return cTPath2D;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DList
    public void removePath(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PATH$0, i);
        }
    }
}

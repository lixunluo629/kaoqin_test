package com.microsoft.schemas.office.visio.x2012.main.impl;

import com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType;
import com.microsoft.schemas.office.visio.x2012.main.ShapesType;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/visio/x2012/main/impl/ShapesTypeImpl.class */
public class ShapesTypeImpl extends XmlComplexContentImpl implements ShapesType {
    private static final QName SHAPE$0 = new QName("http://schemas.microsoft.com/office/visio/2012/main", "Shape");

    public ShapesTypeImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapesType
    public List<ShapeSheetType> getShapeList() {
        1ShapeList r0;
        synchronized (monitor()) {
            check_orphaned();
            r0 = new 1ShapeList(this);
        }
        return r0;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapesType
    public ShapeSheetType[] getShapeArray() {
        ShapeSheetType[] shapeSheetTypeArr;
        synchronized (monitor()) {
            check_orphaned();
            ArrayList arrayList = new ArrayList();
            get_store().find_all_element_users(SHAPE$0, arrayList);
            shapeSheetTypeArr = new ShapeSheetType[arrayList.size()];
            arrayList.toArray(shapeSheetTypeArr);
        }
        return shapeSheetTypeArr;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapesType
    public ShapeSheetType getShapeArray(int i) {
        ShapeSheetType shapeSheetType;
        synchronized (monitor()) {
            check_orphaned();
            shapeSheetType = (ShapeSheetType) get_store().find_element_user(SHAPE$0, i);
            if (shapeSheetType == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return shapeSheetType;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapesType
    public int sizeOfShapeArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(SHAPE$0);
        }
        return iCount_elements;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapesType
    public void setShapeArray(ShapeSheetType[] shapeSheetTypeArr) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(shapeSheetTypeArr, SHAPE$0);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapesType
    public void setShapeArray(int i, ShapeSheetType shapeSheetType) {
        synchronized (monitor()) {
            check_orphaned();
            ShapeSheetType shapeSheetType2 = (ShapeSheetType) get_store().find_element_user(SHAPE$0, i);
            if (shapeSheetType2 == null) {
                throw new IndexOutOfBoundsException();
            }
            shapeSheetType2.set(shapeSheetType);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapesType
    public ShapeSheetType insertNewShape(int i) {
        ShapeSheetType shapeSheetType;
        synchronized (monitor()) {
            check_orphaned();
            shapeSheetType = (ShapeSheetType) get_store().insert_element_user(SHAPE$0, i);
        }
        return shapeSheetType;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapesType
    public ShapeSheetType addNewShape() {
        ShapeSheetType shapeSheetType;
        synchronized (monitor()) {
            check_orphaned();
            shapeSheetType = (ShapeSheetType) get_store().add_element_user(SHAPE$0);
        }
        return shapeSheetType;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapesType
    public void removeShape(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(SHAPE$0, i);
        }
    }
}

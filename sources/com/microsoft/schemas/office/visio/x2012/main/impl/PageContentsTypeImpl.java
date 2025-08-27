package com.microsoft.schemas.office.visio.x2012.main.impl;

import com.microsoft.schemas.office.visio.x2012.main.ConnectsType;
import com.microsoft.schemas.office.visio.x2012.main.PageContentsType;
import com.microsoft.schemas.office.visio.x2012.main.ShapesType;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/visio/x2012/main/impl/PageContentsTypeImpl.class */
public class PageContentsTypeImpl extends XmlComplexContentImpl implements PageContentsType {
    private static final QName SHAPES$0 = new QName("http://schemas.microsoft.com/office/visio/2012/main", "Shapes");
    private static final QName CONNECTS$2 = new QName("http://schemas.microsoft.com/office/visio/2012/main", "Connects");

    public PageContentsTypeImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageContentsType
    public ShapesType getShapes() {
        synchronized (monitor()) {
            check_orphaned();
            ShapesType shapesType = (ShapesType) get_store().find_element_user(SHAPES$0, 0);
            if (shapesType == null) {
                return null;
            }
            return shapesType;
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageContentsType
    public boolean isSetShapes() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(SHAPES$0) != 0;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageContentsType
    public void setShapes(ShapesType shapesType) {
        synchronized (monitor()) {
            check_orphaned();
            ShapesType shapesType2 = (ShapesType) get_store().find_element_user(SHAPES$0, 0);
            if (shapesType2 == null) {
                shapesType2 = (ShapesType) get_store().add_element_user(SHAPES$0);
            }
            shapesType2.set(shapesType);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageContentsType
    public ShapesType addNewShapes() {
        ShapesType shapesType;
        synchronized (monitor()) {
            check_orphaned();
            shapesType = (ShapesType) get_store().add_element_user(SHAPES$0);
        }
        return shapesType;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageContentsType
    public void unsetShapes() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(SHAPES$0, 0);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageContentsType
    public ConnectsType getConnects() {
        synchronized (monitor()) {
            check_orphaned();
            ConnectsType connectsType = (ConnectsType) get_store().find_element_user(CONNECTS$2, 0);
            if (connectsType == null) {
                return null;
            }
            return connectsType;
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageContentsType
    public boolean isSetConnects() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(CONNECTS$2) != 0;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageContentsType
    public void setConnects(ConnectsType connectsType) {
        synchronized (monitor()) {
            check_orphaned();
            ConnectsType connectsType2 = (ConnectsType) get_store().find_element_user(CONNECTS$2, 0);
            if (connectsType2 == null) {
                connectsType2 = (ConnectsType) get_store().add_element_user(CONNECTS$2);
            }
            connectsType2.set(connectsType);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageContentsType
    public ConnectsType addNewConnects() {
        ConnectsType connectsType;
        synchronized (monitor()) {
            check_orphaned();
            connectsType = (ConnectsType) get_store().add_element_user(CONNECTS$2);
        }
        return connectsType;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageContentsType
    public void unsetConnects() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(CONNECTS$2, 0);
        }
    }
}

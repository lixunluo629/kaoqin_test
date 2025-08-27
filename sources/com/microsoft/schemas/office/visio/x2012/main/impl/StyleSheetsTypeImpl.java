package com.microsoft.schemas.office.visio.x2012.main.impl;

import com.microsoft.schemas.office.visio.x2012.main.StyleSheetType;
import com.microsoft.schemas.office.visio.x2012.main.StyleSheetsType;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/visio/x2012/main/impl/StyleSheetsTypeImpl.class */
public class StyleSheetsTypeImpl extends XmlComplexContentImpl implements StyleSheetsType {
    private static final QName STYLESHEET$0 = new QName("http://schemas.microsoft.com/office/visio/2012/main", "StyleSheet");

    public StyleSheetsTypeImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.StyleSheetsType
    public List<StyleSheetType> getStyleSheetList() {
        1StyleSheetList r0;
        synchronized (monitor()) {
            check_orphaned();
            r0 = new 1StyleSheetList(this);
        }
        return r0;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.StyleSheetsType
    public StyleSheetType[] getStyleSheetArray() {
        StyleSheetType[] styleSheetTypeArr;
        synchronized (monitor()) {
            check_orphaned();
            ArrayList arrayList = new ArrayList();
            get_store().find_all_element_users(STYLESHEET$0, arrayList);
            styleSheetTypeArr = new StyleSheetType[arrayList.size()];
            arrayList.toArray(styleSheetTypeArr);
        }
        return styleSheetTypeArr;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.StyleSheetsType
    public StyleSheetType getStyleSheetArray(int i) {
        StyleSheetType styleSheetType;
        synchronized (monitor()) {
            check_orphaned();
            styleSheetType = (StyleSheetType) get_store().find_element_user(STYLESHEET$0, i);
            if (styleSheetType == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return styleSheetType;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.StyleSheetsType
    public int sizeOfStyleSheetArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(STYLESHEET$0);
        }
        return iCount_elements;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.StyleSheetsType
    public void setStyleSheetArray(StyleSheetType[] styleSheetTypeArr) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(styleSheetTypeArr, STYLESHEET$0);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.StyleSheetsType
    public void setStyleSheetArray(int i, StyleSheetType styleSheetType) {
        synchronized (monitor()) {
            check_orphaned();
            StyleSheetType styleSheetType2 = (StyleSheetType) get_store().find_element_user(STYLESHEET$0, i);
            if (styleSheetType2 == null) {
                throw new IndexOutOfBoundsException();
            }
            styleSheetType2.set(styleSheetType);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.StyleSheetsType
    public StyleSheetType insertNewStyleSheet(int i) {
        StyleSheetType styleSheetType;
        synchronized (monitor()) {
            check_orphaned();
            styleSheetType = (StyleSheetType) get_store().insert_element_user(STYLESHEET$0, i);
        }
        return styleSheetType;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.StyleSheetsType
    public StyleSheetType addNewStyleSheet() {
        StyleSheetType styleSheetType;
        synchronized (monitor()) {
            check_orphaned();
            styleSheetType = (StyleSheetType) get_store().add_element_user(STYLESHEET$0);
        }
        return styleSheetType;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.StyleSheetsType
    public void removeStyleSheet(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(STYLESHEET$0, i);
        }
    }
}

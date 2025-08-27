package com.microsoft.schemas.office.visio.x2012.main.impl;

import com.microsoft.schemas.office.visio.x2012.main.PageType;
import com.microsoft.schemas.office.visio.x2012.main.PagesType;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/visio/x2012/main/impl/PagesTypeImpl.class */
public class PagesTypeImpl extends XmlComplexContentImpl implements PagesType {
    private static final QName PAGE$0 = new QName("http://schemas.microsoft.com/office/visio/2012/main", "Page");

    public PagesTypeImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PagesType
    public List<PageType> getPageList() {
        1PageList r0;
        synchronized (monitor()) {
            check_orphaned();
            r0 = new 1PageList(this);
        }
        return r0;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PagesType
    public PageType[] getPageArray() {
        PageType[] pageTypeArr;
        synchronized (monitor()) {
            check_orphaned();
            ArrayList arrayList = new ArrayList();
            get_store().find_all_element_users(PAGE$0, arrayList);
            pageTypeArr = new PageType[arrayList.size()];
            arrayList.toArray(pageTypeArr);
        }
        return pageTypeArr;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PagesType
    public PageType getPageArray(int i) {
        PageType pageType;
        synchronized (monitor()) {
            check_orphaned();
            pageType = (PageType) get_store().find_element_user(PAGE$0, i);
            if (pageType == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return pageType;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PagesType
    public int sizeOfPageArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(PAGE$0);
        }
        return iCount_elements;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PagesType
    public void setPageArray(PageType[] pageTypeArr) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(pageTypeArr, PAGE$0);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PagesType
    public void setPageArray(int i, PageType pageType) {
        synchronized (monitor()) {
            check_orphaned();
            PageType pageType2 = (PageType) get_store().find_element_user(PAGE$0, i);
            if (pageType2 == null) {
                throw new IndexOutOfBoundsException();
            }
            pageType2.set(pageType);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PagesType
    public PageType insertNewPage(int i) {
        PageType pageType;
        synchronized (monitor()) {
            check_orphaned();
            pageType = (PageType) get_store().insert_element_user(PAGE$0, i);
        }
        return pageType;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PagesType
    public PageType addNewPage() {
        PageType pageType;
        synchronized (monitor()) {
            check_orphaned();
            pageType = (PageType) get_store().add_element_user(PAGE$0);
        }
        return pageType;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PagesType
    public void removePage(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PAGE$0, i);
        }
    }
}

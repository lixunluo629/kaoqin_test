package com.microsoft.schemas.office.visio.x2012.main.impl;

import com.microsoft.schemas.office.visio.x2012.main.ConnectType;
import com.microsoft.schemas.office.visio.x2012.main.ConnectsType;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/visio/x2012/main/impl/ConnectsTypeImpl.class */
public class ConnectsTypeImpl extends XmlComplexContentImpl implements ConnectsType {
    private static final QName CONNECT$0 = new QName("http://schemas.microsoft.com/office/visio/2012/main", "Connect");

    public ConnectsTypeImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ConnectsType
    public List<ConnectType> getConnectList() {
        1ConnectList r0;
        synchronized (monitor()) {
            check_orphaned();
            r0 = new 1ConnectList(this);
        }
        return r0;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ConnectsType
    public ConnectType[] getConnectArray() {
        ConnectType[] connectTypeArr;
        synchronized (monitor()) {
            check_orphaned();
            ArrayList arrayList = new ArrayList();
            get_store().find_all_element_users(CONNECT$0, arrayList);
            connectTypeArr = new ConnectType[arrayList.size()];
            arrayList.toArray(connectTypeArr);
        }
        return connectTypeArr;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ConnectsType
    public ConnectType getConnectArray(int i) {
        ConnectType connectType;
        synchronized (monitor()) {
            check_orphaned();
            connectType = (ConnectType) get_store().find_element_user(CONNECT$0, i);
            if (connectType == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return connectType;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ConnectsType
    public int sizeOfConnectArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(CONNECT$0);
        }
        return iCount_elements;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ConnectsType
    public void setConnectArray(ConnectType[] connectTypeArr) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(connectTypeArr, CONNECT$0);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ConnectsType
    public void setConnectArray(int i, ConnectType connectType) {
        synchronized (monitor()) {
            check_orphaned();
            ConnectType connectType2 = (ConnectType) get_store().find_element_user(CONNECT$0, i);
            if (connectType2 == null) {
                throw new IndexOutOfBoundsException();
            }
            connectType2.set(connectType);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ConnectsType
    public ConnectType insertNewConnect(int i) {
        ConnectType connectType;
        synchronized (monitor()) {
            check_orphaned();
            connectType = (ConnectType) get_store().insert_element_user(CONNECT$0, i);
        }
        return connectType;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ConnectsType
    public ConnectType addNewConnect() {
        ConnectType connectType;
        synchronized (monitor()) {
            check_orphaned();
            connectType = (ConnectType) get_store().add_element_user(CONNECT$0);
        }
        return connectType;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ConnectsType
    public void removeConnect(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(CONNECT$0, i);
        }
    }
}

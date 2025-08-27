package org.apache.xmlbeans.impl.xb.xmlconfig.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig;
import org.apache.xmlbeans.impl.xb.xmlconfig.JavaNameList;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/impl/ExtensionconfigImpl.class */
public class ExtensionconfigImpl extends XmlComplexContentImpl implements Extensionconfig {
    private static final long serialVersionUID = 1;
    private static final QName INTERFACE$0 = new QName("http://xml.apache.org/xmlbeans/2004/02/xbean/config", JamXmlElements.INTERFACE);
    private static final QName PREPOSTSET$2 = new QName("http://xml.apache.org/xmlbeans/2004/02/xbean/config", "prePostSet");
    private static final QName FOR$4 = new QName("", "for");

    public ExtensionconfigImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig
    public Extensionconfig.Interface[] getInterfaceArray() {
        Extensionconfig.Interface[] result;
        synchronized (monitor()) {
            check_orphaned();
            List targetList = new ArrayList();
            get_store().find_all_element_users(INTERFACE$0, targetList);
            result = new Extensionconfig.Interface[targetList.size()];
            targetList.toArray(result);
        }
        return result;
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig
    public Extensionconfig.Interface getInterfaceArray(int i) {
        Extensionconfig.Interface target;
        synchronized (monitor()) {
            check_orphaned();
            target = (Extensionconfig.Interface) get_store().find_element_user(INTERFACE$0, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig
    public int sizeOfInterfaceArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(INTERFACE$0);
        }
        return iCount_elements;
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig
    public void setInterfaceArray(Extensionconfig.Interface[] xinterfaceArray) {
        check_orphaned();
        arraySetterHelper(xinterfaceArray, INTERFACE$0);
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig
    public void setInterfaceArray(int i, Extensionconfig.Interface xinterface) {
        generatedSetterHelperImpl(xinterface, INTERFACE$0, i, (short) 2);
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig
    public Extensionconfig.Interface insertNewInterface(int i) {
        Extensionconfig.Interface target;
        synchronized (monitor()) {
            check_orphaned();
            target = (Extensionconfig.Interface) get_store().insert_element_user(INTERFACE$0, i);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig
    public Extensionconfig.Interface addNewInterface() {
        Extensionconfig.Interface target;
        synchronized (monitor()) {
            check_orphaned();
            target = (Extensionconfig.Interface) get_store().add_element_user(INTERFACE$0);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig
    public void removeInterface(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(INTERFACE$0, i);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig
    public Extensionconfig.PrePostSet getPrePostSet() {
        synchronized (monitor()) {
            check_orphaned();
            Extensionconfig.PrePostSet target = (Extensionconfig.PrePostSet) get_store().find_element_user(PREPOSTSET$2, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig
    public boolean isSetPrePostSet() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(PREPOSTSET$2) != 0;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig
    public void setPrePostSet(Extensionconfig.PrePostSet prePostSet) {
        generatedSetterHelperImpl(prePostSet, PREPOSTSET$2, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig
    public Extensionconfig.PrePostSet addNewPrePostSet() {
        Extensionconfig.PrePostSet target;
        synchronized (monitor()) {
            check_orphaned();
            target = (Extensionconfig.PrePostSet) get_store().add_element_user(PREPOSTSET$2);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig
    public void unsetPrePostSet() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PREPOSTSET$2, 0);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig
    public Object getFor() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(FOR$4);
            if (target == null) {
                return null;
            }
            return target.getObjectValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig
    public JavaNameList xgetFor() {
        JavaNameList target;
        synchronized (monitor()) {
            check_orphaned();
            target = (JavaNameList) get_store().find_attribute_user(FOR$4);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig
    public boolean isSetFor() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(FOR$4) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig
    public void setFor(Object xfor) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(FOR$4);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(FOR$4);
            }
            target.setObjectValue(xfor);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig
    public void xsetFor(JavaNameList xfor) {
        synchronized (monitor()) {
            check_orphaned();
            JavaNameList target = (JavaNameList) get_store().find_attribute_user(FOR$4);
            if (target == null) {
                target = (JavaNameList) get_store().add_attribute_user(FOR$4);
            }
            target.set(xfor);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig
    public void unsetFor() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(FOR$4);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/impl/ExtensionconfigImpl$InterfaceImpl.class */
    public static class InterfaceImpl extends XmlComplexContentImpl implements Extensionconfig.Interface {
        private static final long serialVersionUID = 1;
        private static final QName STATICHANDLER$0 = new QName("http://xml.apache.org/xmlbeans/2004/02/xbean/config", "staticHandler");
        private static final QName NAME$2 = new QName("", "name");

        public InterfaceImpl(SchemaType sType) {
            super(sType);
        }

        @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig.Interface
        public String getStaticHandler() {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_element_user(STATICHANDLER$0, 0);
                if (target == null) {
                    return null;
                }
                return target.getStringValue();
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig.Interface
        public XmlString xgetStaticHandler() {
            XmlString target;
            synchronized (monitor()) {
                check_orphaned();
                target = (XmlString) get_store().find_element_user(STATICHANDLER$0, 0);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig.Interface
        public void setStaticHandler(String staticHandler) {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_element_user(STATICHANDLER$0, 0);
                if (target == null) {
                    target = (SimpleValue) get_store().add_element_user(STATICHANDLER$0);
                }
                target.setStringValue(staticHandler);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig.Interface
        public void xsetStaticHandler(XmlString staticHandler) {
            synchronized (monitor()) {
                check_orphaned();
                XmlString target = (XmlString) get_store().find_element_user(STATICHANDLER$0, 0);
                if (target == null) {
                    target = (XmlString) get_store().add_element_user(STATICHANDLER$0);
                }
                target.set(staticHandler);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig.Interface
        public String getName() {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(NAME$2);
                if (target == null) {
                    return null;
                }
                return target.getStringValue();
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig.Interface
        public XmlString xgetName() {
            XmlString target;
            synchronized (monitor()) {
                check_orphaned();
                target = (XmlString) get_store().find_attribute_user(NAME$2);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig.Interface
        public boolean isSetName() {
            boolean z;
            synchronized (monitor()) {
                check_orphaned();
                z = get_store().find_attribute_user(NAME$2) != null;
            }
            return z;
        }

        @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig.Interface
        public void setName(String name) {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(NAME$2);
                if (target == null) {
                    target = (SimpleValue) get_store().add_attribute_user(NAME$2);
                }
                target.setStringValue(name);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig.Interface
        public void xsetName(XmlString name) {
            synchronized (monitor()) {
                check_orphaned();
                XmlString target = (XmlString) get_store().find_attribute_user(NAME$2);
                if (target == null) {
                    target = (XmlString) get_store().add_attribute_user(NAME$2);
                }
                target.set(name);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig.Interface
        public void unsetName() {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_attribute(NAME$2);
            }
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/impl/ExtensionconfigImpl$PrePostSetImpl.class */
    public static class PrePostSetImpl extends XmlComplexContentImpl implements Extensionconfig.PrePostSet {
        private static final long serialVersionUID = 1;
        private static final QName STATICHANDLER$0 = new QName("http://xml.apache.org/xmlbeans/2004/02/xbean/config", "staticHandler");

        public PrePostSetImpl(SchemaType sType) {
            super(sType);
        }

        @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig.PrePostSet
        public String getStaticHandler() {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_element_user(STATICHANDLER$0, 0);
                if (target == null) {
                    return null;
                }
                return target.getStringValue();
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig.PrePostSet
        public XmlString xgetStaticHandler() {
            XmlString target;
            synchronized (monitor()) {
                check_orphaned();
                target = (XmlString) get_store().find_element_user(STATICHANDLER$0, 0);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig.PrePostSet
        public void setStaticHandler(String staticHandler) {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_element_user(STATICHANDLER$0, 0);
                if (target == null) {
                    target = (SimpleValue) get_store().add_element_user(STATICHANDLER$0);
                }
                target.setStringValue(staticHandler);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig.PrePostSet
        public void xsetStaticHandler(XmlString staticHandler) {
            synchronized (monitor()) {
                check_orphaned();
                XmlString target = (XmlString) get_store().find_element_user(STATICHANDLER$0, 0);
                if (target == null) {
                    target = (XmlString) get_store().add_element_user(STATICHANDLER$0);
                }
                target.set(staticHandler);
            }
        }
    }
}

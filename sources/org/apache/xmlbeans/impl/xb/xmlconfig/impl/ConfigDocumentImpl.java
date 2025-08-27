package org.apache.xmlbeans.impl.xb.xmlconfig.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument;
import org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig;
import org.apache.xmlbeans.impl.xb.xmlconfig.Nsconfig;
import org.apache.xmlbeans.impl.xb.xmlconfig.Qnameconfig;
import org.apache.xmlbeans.impl.xb.xmlconfig.Usertypeconfig;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/impl/ConfigDocumentImpl.class */
public class ConfigDocumentImpl extends XmlComplexContentImpl implements ConfigDocument {
    private static final long serialVersionUID = 1;
    private static final QName CONFIG$0 = new QName("http://xml.apache.org/xmlbeans/2004/02/xbean/config", "config");

    public ConfigDocumentImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument
    public ConfigDocument.Config getConfig() {
        synchronized (monitor()) {
            check_orphaned();
            ConfigDocument.Config target = (ConfigDocument.Config) get_store().find_element_user(CONFIG$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument
    public void setConfig(ConfigDocument.Config config) {
        generatedSetterHelperImpl(config, CONFIG$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument
    public ConfigDocument.Config addNewConfig() {
        ConfigDocument.Config target;
        synchronized (monitor()) {
            check_orphaned();
            target = (ConfigDocument.Config) get_store().add_element_user(CONFIG$0);
        }
        return target;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/impl/ConfigDocumentImpl$ConfigImpl.class */
    public static class ConfigImpl extends XmlComplexContentImpl implements ConfigDocument.Config {
        private static final long serialVersionUID = 1;
        private static final QName NAMESPACE$0 = new QName("http://xml.apache.org/xmlbeans/2004/02/xbean/config", "namespace");
        private static final QName QNAME$2 = new QName("http://xml.apache.org/xmlbeans/2004/02/xbean/config", "qname");
        private static final QName EXTENSION$4 = new QName("http://xml.apache.org/xmlbeans/2004/02/xbean/config", "extension");
        private static final QName USERTYPE$6 = new QName("http://xml.apache.org/xmlbeans/2004/02/xbean/config", "usertype");

        public ConfigImpl(SchemaType sType) {
            super(sType);
        }

        @Override // org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument.Config
        public Nsconfig[] getNamespaceArray() {
            Nsconfig[] result;
            synchronized (monitor()) {
                check_orphaned();
                List targetList = new ArrayList();
                get_store().find_all_element_users(NAMESPACE$0, targetList);
                result = new Nsconfig[targetList.size()];
                targetList.toArray(result);
            }
            return result;
        }

        @Override // org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument.Config
        public Nsconfig getNamespaceArray(int i) {
            Nsconfig target;
            synchronized (monitor()) {
                check_orphaned();
                target = (Nsconfig) get_store().find_element_user(NAMESPACE$0, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument.Config
        public int sizeOfNamespaceArray() {
            int iCount_elements;
            synchronized (monitor()) {
                check_orphaned();
                iCount_elements = get_store().count_elements(NAMESPACE$0);
            }
            return iCount_elements;
        }

        @Override // org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument.Config
        public void setNamespaceArray(Nsconfig[] namespaceArray) {
            check_orphaned();
            arraySetterHelper(namespaceArray, NAMESPACE$0);
        }

        @Override // org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument.Config
        public void setNamespaceArray(int i, Nsconfig namespace) {
            generatedSetterHelperImpl(namespace, NAMESPACE$0, i, (short) 2);
        }

        @Override // org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument.Config
        public Nsconfig insertNewNamespace(int i) {
            Nsconfig target;
            synchronized (monitor()) {
                check_orphaned();
                target = (Nsconfig) get_store().insert_element_user(NAMESPACE$0, i);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument.Config
        public Nsconfig addNewNamespace() {
            Nsconfig target;
            synchronized (monitor()) {
                check_orphaned();
                target = (Nsconfig) get_store().add_element_user(NAMESPACE$0);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument.Config
        public void removeNamespace(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(NAMESPACE$0, i);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument.Config
        public Qnameconfig[] getQnameArray() {
            Qnameconfig[] result;
            synchronized (monitor()) {
                check_orphaned();
                List targetList = new ArrayList();
                get_store().find_all_element_users(QNAME$2, targetList);
                result = new Qnameconfig[targetList.size()];
                targetList.toArray(result);
            }
            return result;
        }

        @Override // org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument.Config
        public Qnameconfig getQnameArray(int i) {
            Qnameconfig target;
            synchronized (monitor()) {
                check_orphaned();
                target = (Qnameconfig) get_store().find_element_user(QNAME$2, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument.Config
        public int sizeOfQnameArray() {
            int iCount_elements;
            synchronized (monitor()) {
                check_orphaned();
                iCount_elements = get_store().count_elements(QNAME$2);
            }
            return iCount_elements;
        }

        @Override // org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument.Config
        public void setQnameArray(Qnameconfig[] qnameArray) {
            check_orphaned();
            arraySetterHelper(qnameArray, QNAME$2);
        }

        @Override // org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument.Config
        public void setQnameArray(int i, Qnameconfig qname) {
            generatedSetterHelperImpl(qname, QNAME$2, i, (short) 2);
        }

        @Override // org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument.Config
        public Qnameconfig insertNewQname(int i) {
            Qnameconfig target;
            synchronized (monitor()) {
                check_orphaned();
                target = (Qnameconfig) get_store().insert_element_user(QNAME$2, i);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument.Config
        public Qnameconfig addNewQname() {
            Qnameconfig target;
            synchronized (monitor()) {
                check_orphaned();
                target = (Qnameconfig) get_store().add_element_user(QNAME$2);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument.Config
        public void removeQname(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(QNAME$2, i);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument.Config
        public Extensionconfig[] getExtensionArray() {
            Extensionconfig[] result;
            synchronized (monitor()) {
                check_orphaned();
                List targetList = new ArrayList();
                get_store().find_all_element_users(EXTENSION$4, targetList);
                result = new Extensionconfig[targetList.size()];
                targetList.toArray(result);
            }
            return result;
        }

        @Override // org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument.Config
        public Extensionconfig getExtensionArray(int i) {
            Extensionconfig target;
            synchronized (monitor()) {
                check_orphaned();
                target = (Extensionconfig) get_store().find_element_user(EXTENSION$4, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument.Config
        public int sizeOfExtensionArray() {
            int iCount_elements;
            synchronized (monitor()) {
                check_orphaned();
                iCount_elements = get_store().count_elements(EXTENSION$4);
            }
            return iCount_elements;
        }

        @Override // org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument.Config
        public void setExtensionArray(Extensionconfig[] extensionArray) {
            check_orphaned();
            arraySetterHelper(extensionArray, EXTENSION$4);
        }

        @Override // org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument.Config
        public void setExtensionArray(int i, Extensionconfig extension) {
            generatedSetterHelperImpl(extension, EXTENSION$4, i, (short) 2);
        }

        @Override // org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument.Config
        public Extensionconfig insertNewExtension(int i) {
            Extensionconfig target;
            synchronized (monitor()) {
                check_orphaned();
                target = (Extensionconfig) get_store().insert_element_user(EXTENSION$4, i);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument.Config
        public Extensionconfig addNewExtension() {
            Extensionconfig target;
            synchronized (monitor()) {
                check_orphaned();
                target = (Extensionconfig) get_store().add_element_user(EXTENSION$4);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument.Config
        public void removeExtension(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(EXTENSION$4, i);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument.Config
        public Usertypeconfig[] getUsertypeArray() {
            Usertypeconfig[] result;
            synchronized (monitor()) {
                check_orphaned();
                List targetList = new ArrayList();
                get_store().find_all_element_users(USERTYPE$6, targetList);
                result = new Usertypeconfig[targetList.size()];
                targetList.toArray(result);
            }
            return result;
        }

        @Override // org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument.Config
        public Usertypeconfig getUsertypeArray(int i) {
            Usertypeconfig target;
            synchronized (monitor()) {
                check_orphaned();
                target = (Usertypeconfig) get_store().find_element_user(USERTYPE$6, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument.Config
        public int sizeOfUsertypeArray() {
            int iCount_elements;
            synchronized (monitor()) {
                check_orphaned();
                iCount_elements = get_store().count_elements(USERTYPE$6);
            }
            return iCount_elements;
        }

        @Override // org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument.Config
        public void setUsertypeArray(Usertypeconfig[] usertypeArray) {
            check_orphaned();
            arraySetterHelper(usertypeArray, USERTYPE$6);
        }

        @Override // org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument.Config
        public void setUsertypeArray(int i, Usertypeconfig usertype) {
            generatedSetterHelperImpl(usertype, USERTYPE$6, i, (short) 2);
        }

        @Override // org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument.Config
        public Usertypeconfig insertNewUsertype(int i) {
            Usertypeconfig target;
            synchronized (monitor()) {
                check_orphaned();
                target = (Usertypeconfig) get_store().insert_element_user(USERTYPE$6, i);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument.Config
        public Usertypeconfig addNewUsertype() {
            Usertypeconfig target;
            synchronized (monitor()) {
                check_orphaned();
                target = (Usertypeconfig) get_store().add_element_user(USERTYPE$6);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument.Config
        public void removeUsertype(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(USERTYPE$6, i);
            }
        }
    }
}

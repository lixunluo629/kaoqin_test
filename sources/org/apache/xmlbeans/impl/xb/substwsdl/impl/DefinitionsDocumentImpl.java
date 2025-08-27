package org.apache.xmlbeans.impl.xb.substwsdl.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument;
import org.apache.xmlbeans.impl.xb.substwsdl.TImport;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/substwsdl/impl/DefinitionsDocumentImpl.class */
public class DefinitionsDocumentImpl extends XmlComplexContentImpl implements DefinitionsDocument {
    private static final long serialVersionUID = 1;
    private static final QName DEFINITIONS$0 = new QName("http://www.apache.org/internal/xmlbeans/wsdlsubst", "definitions");

    public DefinitionsDocumentImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument
    public DefinitionsDocument.Definitions getDefinitions() {
        synchronized (monitor()) {
            check_orphaned();
            DefinitionsDocument.Definitions target = (DefinitionsDocument.Definitions) get_store().find_element_user(DEFINITIONS$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument
    public void setDefinitions(DefinitionsDocument.Definitions definitions) {
        generatedSetterHelperImpl(definitions, DEFINITIONS$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument
    public DefinitionsDocument.Definitions addNewDefinitions() {
        DefinitionsDocument.Definitions target;
        synchronized (monitor()) {
            check_orphaned();
            target = (DefinitionsDocument.Definitions) get_store().add_element_user(DEFINITIONS$0);
        }
        return target;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/substwsdl/impl/DefinitionsDocumentImpl$DefinitionsImpl.class */
    public static class DefinitionsImpl extends XmlComplexContentImpl implements DefinitionsDocument.Definitions {
        private static final long serialVersionUID = 1;
        private static final QName IMPORT$0 = new QName("http://www.apache.org/internal/xmlbeans/wsdlsubst", DefaultBeanDefinitionDocumentReader.IMPORT_ELEMENT);
        private static final QName TYPES$2 = new QName("http://www.apache.org/internal/xmlbeans/wsdlsubst", "types");
        private static final QName MESSAGE$4 = new QName("http://www.apache.org/internal/xmlbeans/wsdlsubst", ConstraintHelper.MESSAGE);
        private static final QName BINDING$6 = new QName("http://www.apache.org/internal/xmlbeans/wsdlsubst", "binding");
        private static final QName PORTTYPE$8 = new QName("http://www.apache.org/internal/xmlbeans/wsdlsubst", "portType");
        private static final QName SERVICE$10 = new QName("http://www.apache.org/internal/xmlbeans/wsdlsubst", "service");

        public DefinitionsImpl(SchemaType sType) {
            super(sType);
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public TImport[] getImportArray() {
            TImport[] result;
            synchronized (monitor()) {
                check_orphaned();
                List targetList = new ArrayList();
                get_store().find_all_element_users(IMPORT$0, targetList);
                result = new TImport[targetList.size()];
                targetList.toArray(result);
            }
            return result;
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public TImport getImportArray(int i) {
            TImport target;
            synchronized (monitor()) {
                check_orphaned();
                target = (TImport) get_store().find_element_user(IMPORT$0, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public int sizeOfImportArray() {
            int iCount_elements;
            synchronized (monitor()) {
                check_orphaned();
                iCount_elements = get_store().count_elements(IMPORT$0);
            }
            return iCount_elements;
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public void setImportArray(TImport[] ximportArray) {
            check_orphaned();
            arraySetterHelper(ximportArray, IMPORT$0);
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public void setImportArray(int i, TImport ximport) {
            generatedSetterHelperImpl(ximport, IMPORT$0, i, (short) 2);
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public TImport insertNewImport(int i) {
            TImport target;
            synchronized (monitor()) {
                check_orphaned();
                target = (TImport) get_store().insert_element_user(IMPORT$0, i);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public TImport addNewImport() {
            TImport target;
            synchronized (monitor()) {
                check_orphaned();
                target = (TImport) get_store().add_element_user(IMPORT$0);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public void removeImport(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(IMPORT$0, i);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public XmlObject[] getTypesArray() {
            XmlObject[] result;
            synchronized (monitor()) {
                check_orphaned();
                List targetList = new ArrayList();
                get_store().find_all_element_users(TYPES$2, targetList);
                result = new XmlObject[targetList.size()];
                targetList.toArray(result);
            }
            return result;
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public XmlObject getTypesArray(int i) {
            XmlObject target;
            synchronized (monitor()) {
                check_orphaned();
                target = (XmlObject) get_store().find_element_user(TYPES$2, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public int sizeOfTypesArray() {
            int iCount_elements;
            synchronized (monitor()) {
                check_orphaned();
                iCount_elements = get_store().count_elements(TYPES$2);
            }
            return iCount_elements;
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public void setTypesArray(XmlObject[] typesArray) {
            check_orphaned();
            arraySetterHelper(typesArray, TYPES$2);
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public void setTypesArray(int i, XmlObject types) {
            generatedSetterHelperImpl(types, TYPES$2, i, (short) 2);
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public XmlObject insertNewTypes(int i) {
            XmlObject target;
            synchronized (monitor()) {
                check_orphaned();
                target = (XmlObject) get_store().insert_element_user(TYPES$2, i);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public XmlObject addNewTypes() {
            XmlObject target;
            synchronized (monitor()) {
                check_orphaned();
                target = (XmlObject) get_store().add_element_user(TYPES$2);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public void removeTypes(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(TYPES$2, i);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public XmlObject[] getMessageArray() {
            XmlObject[] result;
            synchronized (monitor()) {
                check_orphaned();
                List targetList = new ArrayList();
                get_store().find_all_element_users(MESSAGE$4, targetList);
                result = new XmlObject[targetList.size()];
                targetList.toArray(result);
            }
            return result;
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public XmlObject getMessageArray(int i) {
            XmlObject target;
            synchronized (monitor()) {
                check_orphaned();
                target = (XmlObject) get_store().find_element_user(MESSAGE$4, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public int sizeOfMessageArray() {
            int iCount_elements;
            synchronized (monitor()) {
                check_orphaned();
                iCount_elements = get_store().count_elements(MESSAGE$4);
            }
            return iCount_elements;
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public void setMessageArray(XmlObject[] messageArray) {
            check_orphaned();
            arraySetterHelper(messageArray, MESSAGE$4);
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public void setMessageArray(int i, XmlObject message) {
            generatedSetterHelperImpl(message, MESSAGE$4, i, (short) 2);
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public XmlObject insertNewMessage(int i) {
            XmlObject target;
            synchronized (monitor()) {
                check_orphaned();
                target = (XmlObject) get_store().insert_element_user(MESSAGE$4, i);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public XmlObject addNewMessage() {
            XmlObject target;
            synchronized (monitor()) {
                check_orphaned();
                target = (XmlObject) get_store().add_element_user(MESSAGE$4);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public void removeMessage(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(MESSAGE$4, i);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public XmlObject[] getBindingArray() {
            XmlObject[] result;
            synchronized (monitor()) {
                check_orphaned();
                List targetList = new ArrayList();
                get_store().find_all_element_users(BINDING$6, targetList);
                result = new XmlObject[targetList.size()];
                targetList.toArray(result);
            }
            return result;
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public XmlObject getBindingArray(int i) {
            XmlObject target;
            synchronized (monitor()) {
                check_orphaned();
                target = (XmlObject) get_store().find_element_user(BINDING$6, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public int sizeOfBindingArray() {
            int iCount_elements;
            synchronized (monitor()) {
                check_orphaned();
                iCount_elements = get_store().count_elements(BINDING$6);
            }
            return iCount_elements;
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public void setBindingArray(XmlObject[] bindingArray) {
            check_orphaned();
            arraySetterHelper(bindingArray, BINDING$6);
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public void setBindingArray(int i, XmlObject binding) {
            generatedSetterHelperImpl(binding, BINDING$6, i, (short) 2);
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public XmlObject insertNewBinding(int i) {
            XmlObject target;
            synchronized (monitor()) {
                check_orphaned();
                target = (XmlObject) get_store().insert_element_user(BINDING$6, i);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public XmlObject addNewBinding() {
            XmlObject target;
            synchronized (monitor()) {
                check_orphaned();
                target = (XmlObject) get_store().add_element_user(BINDING$6);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public void removeBinding(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(BINDING$6, i);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public XmlObject[] getPortTypeArray() {
            XmlObject[] result;
            synchronized (monitor()) {
                check_orphaned();
                List targetList = new ArrayList();
                get_store().find_all_element_users(PORTTYPE$8, targetList);
                result = new XmlObject[targetList.size()];
                targetList.toArray(result);
            }
            return result;
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public XmlObject getPortTypeArray(int i) {
            XmlObject target;
            synchronized (monitor()) {
                check_orphaned();
                target = (XmlObject) get_store().find_element_user(PORTTYPE$8, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public int sizeOfPortTypeArray() {
            int iCount_elements;
            synchronized (monitor()) {
                check_orphaned();
                iCount_elements = get_store().count_elements(PORTTYPE$8);
            }
            return iCount_elements;
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public void setPortTypeArray(XmlObject[] portTypeArray) {
            check_orphaned();
            arraySetterHelper(portTypeArray, PORTTYPE$8);
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public void setPortTypeArray(int i, XmlObject portType) {
            generatedSetterHelperImpl(portType, PORTTYPE$8, i, (short) 2);
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public XmlObject insertNewPortType(int i) {
            XmlObject target;
            synchronized (monitor()) {
                check_orphaned();
                target = (XmlObject) get_store().insert_element_user(PORTTYPE$8, i);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public XmlObject addNewPortType() {
            XmlObject target;
            synchronized (monitor()) {
                check_orphaned();
                target = (XmlObject) get_store().add_element_user(PORTTYPE$8);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public void removePortType(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(PORTTYPE$8, i);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public XmlObject[] getServiceArray() {
            XmlObject[] result;
            synchronized (monitor()) {
                check_orphaned();
                List targetList = new ArrayList();
                get_store().find_all_element_users(SERVICE$10, targetList);
                result = new XmlObject[targetList.size()];
                targetList.toArray(result);
            }
            return result;
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public XmlObject getServiceArray(int i) {
            XmlObject target;
            synchronized (monitor()) {
                check_orphaned();
                target = (XmlObject) get_store().find_element_user(SERVICE$10, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public int sizeOfServiceArray() {
            int iCount_elements;
            synchronized (monitor()) {
                check_orphaned();
                iCount_elements = get_store().count_elements(SERVICE$10);
            }
            return iCount_elements;
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public void setServiceArray(XmlObject[] serviceArray) {
            check_orphaned();
            arraySetterHelper(serviceArray, SERVICE$10);
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public void setServiceArray(int i, XmlObject service) {
            generatedSetterHelperImpl(service, SERVICE$10, i, (short) 2);
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public XmlObject insertNewService(int i) {
            XmlObject target;
            synchronized (monitor()) {
                check_orphaned();
                target = (XmlObject) get_store().insert_element_user(SERVICE$10, i);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public XmlObject addNewService() {
            XmlObject target;
            synchronized (monitor()) {
                check_orphaned();
                target = (XmlObject) get_store().add_element_user(SERVICE$10);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions
        public void removeService(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(SERVICE$10, i);
            }
        }
    }
}

package org.apache.xmlbeans.impl.xb.xsdownload.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlToken;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemaEntry;
import org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemasDocument;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdownload/impl/DownloadedSchemasDocumentImpl.class */
public class DownloadedSchemasDocumentImpl extends XmlComplexContentImpl implements DownloadedSchemasDocument {
    private static final long serialVersionUID = 1;
    private static final QName DOWNLOADEDSCHEMAS$0 = new QName("http://www.bea.com/2003/01/xmlbean/xsdownload", "downloaded-schemas");

    public DownloadedSchemasDocumentImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemasDocument
    public DownloadedSchemasDocument.DownloadedSchemas getDownloadedSchemas() {
        synchronized (monitor()) {
            check_orphaned();
            DownloadedSchemasDocument.DownloadedSchemas target = (DownloadedSchemasDocument.DownloadedSchemas) get_store().find_element_user(DOWNLOADEDSCHEMAS$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemasDocument
    public void setDownloadedSchemas(DownloadedSchemasDocument.DownloadedSchemas downloadedSchemas) {
        generatedSetterHelperImpl(downloadedSchemas, DOWNLOADEDSCHEMAS$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemasDocument
    public DownloadedSchemasDocument.DownloadedSchemas addNewDownloadedSchemas() {
        DownloadedSchemasDocument.DownloadedSchemas target;
        synchronized (monitor()) {
            check_orphaned();
            target = (DownloadedSchemasDocument.DownloadedSchemas) get_store().add_element_user(DOWNLOADEDSCHEMAS$0);
        }
        return target;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdownload/impl/DownloadedSchemasDocumentImpl$DownloadedSchemasImpl.class */
    public static class DownloadedSchemasImpl extends XmlComplexContentImpl implements DownloadedSchemasDocument.DownloadedSchemas {
        private static final long serialVersionUID = 1;
        private static final QName ENTRY$0 = new QName("http://www.bea.com/2003/01/xmlbean/xsdownload", BeanDefinitionParserDelegate.ENTRY_ELEMENT);
        private static final QName DEFAULTDIRECTORY$2 = new QName("", "defaultDirectory");

        public DownloadedSchemasImpl(SchemaType sType) {
            super(sType);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemasDocument.DownloadedSchemas
        public DownloadedSchemaEntry[] getEntryArray() {
            DownloadedSchemaEntry[] result;
            synchronized (monitor()) {
                check_orphaned();
                List targetList = new ArrayList();
                get_store().find_all_element_users(ENTRY$0, targetList);
                result = new DownloadedSchemaEntry[targetList.size()];
                targetList.toArray(result);
            }
            return result;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemasDocument.DownloadedSchemas
        public DownloadedSchemaEntry getEntryArray(int i) {
            DownloadedSchemaEntry target;
            synchronized (monitor()) {
                check_orphaned();
                target = (DownloadedSchemaEntry) get_store().find_element_user(ENTRY$0, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemasDocument.DownloadedSchemas
        public int sizeOfEntryArray() {
            int iCount_elements;
            synchronized (monitor()) {
                check_orphaned();
                iCount_elements = get_store().count_elements(ENTRY$0);
            }
            return iCount_elements;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemasDocument.DownloadedSchemas
        public void setEntryArray(DownloadedSchemaEntry[] entryArray) {
            check_orphaned();
            arraySetterHelper(entryArray, ENTRY$0);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemasDocument.DownloadedSchemas
        public void setEntryArray(int i, DownloadedSchemaEntry entry) {
            generatedSetterHelperImpl(entry, ENTRY$0, i, (short) 2);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemasDocument.DownloadedSchemas
        public DownloadedSchemaEntry insertNewEntry(int i) {
            DownloadedSchemaEntry target;
            synchronized (monitor()) {
                check_orphaned();
                target = (DownloadedSchemaEntry) get_store().insert_element_user(ENTRY$0, i);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemasDocument.DownloadedSchemas
        public DownloadedSchemaEntry addNewEntry() {
            DownloadedSchemaEntry target;
            synchronized (monitor()) {
                check_orphaned();
                target = (DownloadedSchemaEntry) get_store().add_element_user(ENTRY$0);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemasDocument.DownloadedSchemas
        public void removeEntry(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(ENTRY$0, i);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemasDocument.DownloadedSchemas
        public String getDefaultDirectory() {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(DEFAULTDIRECTORY$2);
                if (target == null) {
                    return null;
                }
                return target.getStringValue();
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemasDocument.DownloadedSchemas
        public XmlToken xgetDefaultDirectory() {
            XmlToken target;
            synchronized (monitor()) {
                check_orphaned();
                target = (XmlToken) get_store().find_attribute_user(DEFAULTDIRECTORY$2);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemasDocument.DownloadedSchemas
        public boolean isSetDefaultDirectory() {
            boolean z;
            synchronized (monitor()) {
                check_orphaned();
                z = get_store().find_attribute_user(DEFAULTDIRECTORY$2) != null;
            }
            return z;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemasDocument.DownloadedSchemas
        public void setDefaultDirectory(String defaultDirectory) {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(DEFAULTDIRECTORY$2);
                if (target == null) {
                    target = (SimpleValue) get_store().add_attribute_user(DEFAULTDIRECTORY$2);
                }
                target.setStringValue(defaultDirectory);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemasDocument.DownloadedSchemas
        public void xsetDefaultDirectory(XmlToken defaultDirectory) {
            synchronized (monitor()) {
                check_orphaned();
                XmlToken target = (XmlToken) get_store().find_attribute_user(DEFAULTDIRECTORY$2);
                if (target == null) {
                    target = (XmlToken) get_store().add_attribute_user(DEFAULTDIRECTORY$2);
                }
                target.set(defaultDirectory);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemasDocument.DownloadedSchemas
        public void unsetDefaultDirectory() {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_attribute(DEFAULTDIRECTORY$2);
            }
        }
    }
}

package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import com.itextpdf.kernel.xmp.PdfConst;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlAnyURI;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.xsdschema.AppinfoDocument;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/AppinfoDocumentImpl.class */
public class AppinfoDocumentImpl extends XmlComplexContentImpl implements AppinfoDocument {
    private static final long serialVersionUID = 1;
    private static final QName APPINFO$0 = new QName("http://www.w3.org/2001/XMLSchema", "appinfo");

    public AppinfoDocumentImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.AppinfoDocument
    public AppinfoDocument.Appinfo getAppinfo() {
        synchronized (monitor()) {
            check_orphaned();
            AppinfoDocument.Appinfo target = (AppinfoDocument.Appinfo) get_store().find_element_user(APPINFO$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.AppinfoDocument
    public void setAppinfo(AppinfoDocument.Appinfo appinfo) {
        generatedSetterHelperImpl(appinfo, APPINFO$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.AppinfoDocument
    public AppinfoDocument.Appinfo addNewAppinfo() {
        AppinfoDocument.Appinfo target;
        synchronized (monitor()) {
            check_orphaned();
            target = (AppinfoDocument.Appinfo) get_store().add_element_user(APPINFO$0);
        }
        return target;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/AppinfoDocumentImpl$AppinfoImpl.class */
    public static class AppinfoImpl extends XmlComplexContentImpl implements AppinfoDocument.Appinfo {
        private static final long serialVersionUID = 1;
        private static final QName SOURCE$0 = new QName("", PdfConst.Source);

        public AppinfoImpl(SchemaType sType) {
            super(sType);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.AppinfoDocument.Appinfo
        public String getSource() {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(SOURCE$0);
                if (target == null) {
                    return null;
                }
                return target.getStringValue();
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.AppinfoDocument.Appinfo
        public XmlAnyURI xgetSource() {
            XmlAnyURI target;
            synchronized (monitor()) {
                check_orphaned();
                target = (XmlAnyURI) get_store().find_attribute_user(SOURCE$0);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.AppinfoDocument.Appinfo
        public boolean isSetSource() {
            boolean z;
            synchronized (monitor()) {
                check_orphaned();
                z = get_store().find_attribute_user(SOURCE$0) != null;
            }
            return z;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.AppinfoDocument.Appinfo
        public void setSource(String source) {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(SOURCE$0);
                if (target == null) {
                    target = (SimpleValue) get_store().add_attribute_user(SOURCE$0);
                }
                target.setStringValue(source);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.AppinfoDocument.Appinfo
        public void xsetSource(XmlAnyURI source) {
            synchronized (monitor()) {
                check_orphaned();
                XmlAnyURI target = (XmlAnyURI) get_store().find_attribute_user(SOURCE$0);
                if (target == null) {
                    target = (XmlAnyURI) get_store().add_attribute_user(SOURCE$0);
                }
                target.set(source);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.AppinfoDocument.Appinfo
        public void unsetSource() {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_attribute(SOURCE$0);
            }
        }
    }
}

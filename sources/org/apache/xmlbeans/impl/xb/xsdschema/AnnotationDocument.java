package org.apache.xmlbeans.impl.xb.xsdschema;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlID;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.xb.xsdschema.AppinfoDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.DocumentationDocument;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/AnnotationDocument.class */
public interface AnnotationDocument extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(AnnotationDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("annotationb034doctype");

    Annotation getAnnotation();

    void setAnnotation(Annotation annotation);

    Annotation addNewAnnotation();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/AnnotationDocument$Annotation.class */
    public interface Annotation extends OpenAttrs {
        public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Annotation.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("annotation5abfelemtype");

        AppinfoDocument.Appinfo[] getAppinfoArray();

        AppinfoDocument.Appinfo getAppinfoArray(int i);

        int sizeOfAppinfoArray();

        void setAppinfoArray(AppinfoDocument.Appinfo[] appinfoArr);

        void setAppinfoArray(int i, AppinfoDocument.Appinfo appinfo);

        AppinfoDocument.Appinfo insertNewAppinfo(int i);

        AppinfoDocument.Appinfo addNewAppinfo();

        void removeAppinfo(int i);

        DocumentationDocument.Documentation[] getDocumentationArray();

        DocumentationDocument.Documentation getDocumentationArray(int i);

        int sizeOfDocumentationArray();

        void setDocumentationArray(DocumentationDocument.Documentation[] documentationArr);

        void setDocumentationArray(int i, DocumentationDocument.Documentation documentation);

        DocumentationDocument.Documentation insertNewDocumentation(int i);

        DocumentationDocument.Documentation addNewDocumentation();

        void removeDocumentation(int i);

        String getId();

        XmlID xgetId();

        boolean isSetId();

        void setId(String str);

        void xsetId(XmlID xmlID);

        void unsetId();

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/AnnotationDocument$Annotation$Factory.class */
        public static final class Factory {
            public static Annotation newInstance() {
                return (Annotation) XmlBeans.getContextTypeLoader().newInstance(Annotation.type, null);
            }

            public static Annotation newInstance(XmlOptions options) {
                return (Annotation) XmlBeans.getContextTypeLoader().newInstance(Annotation.type, options);
            }

            private Factory() {
            }
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/AnnotationDocument$Factory.class */
    public static final class Factory {
        public static AnnotationDocument newInstance() {
            return (AnnotationDocument) XmlBeans.getContextTypeLoader().newInstance(AnnotationDocument.type, null);
        }

        public static AnnotationDocument newInstance(XmlOptions options) {
            return (AnnotationDocument) XmlBeans.getContextTypeLoader().newInstance(AnnotationDocument.type, options);
        }

        public static AnnotationDocument parse(String xmlAsString) throws XmlException {
            return (AnnotationDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, AnnotationDocument.type, (XmlOptions) null);
        }

        public static AnnotationDocument parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (AnnotationDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, AnnotationDocument.type, options);
        }

        public static AnnotationDocument parse(File file) throws XmlException, IOException {
            return (AnnotationDocument) XmlBeans.getContextTypeLoader().parse(file, AnnotationDocument.type, (XmlOptions) null);
        }

        public static AnnotationDocument parse(File file, XmlOptions options) throws XmlException, IOException {
            return (AnnotationDocument) XmlBeans.getContextTypeLoader().parse(file, AnnotationDocument.type, options);
        }

        public static AnnotationDocument parse(URL u) throws XmlException, IOException {
            return (AnnotationDocument) XmlBeans.getContextTypeLoader().parse(u, AnnotationDocument.type, (XmlOptions) null);
        }

        public static AnnotationDocument parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (AnnotationDocument) XmlBeans.getContextTypeLoader().parse(u, AnnotationDocument.type, options);
        }

        public static AnnotationDocument parse(InputStream is) throws XmlException, IOException {
            return (AnnotationDocument) XmlBeans.getContextTypeLoader().parse(is, AnnotationDocument.type, (XmlOptions) null);
        }

        public static AnnotationDocument parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (AnnotationDocument) XmlBeans.getContextTypeLoader().parse(is, AnnotationDocument.type, options);
        }

        public static AnnotationDocument parse(Reader r) throws XmlException, IOException {
            return (AnnotationDocument) XmlBeans.getContextTypeLoader().parse(r, AnnotationDocument.type, (XmlOptions) null);
        }

        public static AnnotationDocument parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (AnnotationDocument) XmlBeans.getContextTypeLoader().parse(r, AnnotationDocument.type, options);
        }

        public static AnnotationDocument parse(XMLStreamReader sr) throws XmlException {
            return (AnnotationDocument) XmlBeans.getContextTypeLoader().parse(sr, AnnotationDocument.type, (XmlOptions) null);
        }

        public static AnnotationDocument parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (AnnotationDocument) XmlBeans.getContextTypeLoader().parse(sr, AnnotationDocument.type, options);
        }

        public static AnnotationDocument parse(Node node) throws XmlException {
            return (AnnotationDocument) XmlBeans.getContextTypeLoader().parse(node, AnnotationDocument.type, (XmlOptions) null);
        }

        public static AnnotationDocument parse(Node node, XmlOptions options) throws XmlException {
            return (AnnotationDocument) XmlBeans.getContextTypeLoader().parse(node, AnnotationDocument.type, options);
        }

        public static AnnotationDocument parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (AnnotationDocument) XmlBeans.getContextTypeLoader().parse(xis, AnnotationDocument.type, (XmlOptions) null);
        }

        public static AnnotationDocument parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (AnnotationDocument) XmlBeans.getContextTypeLoader().parse(xis, AnnotationDocument.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, AnnotationDocument.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, AnnotationDocument.type, options);
        }

        private Factory() {
        }
    }
}

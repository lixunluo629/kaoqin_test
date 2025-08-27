package org.apache.xmlbeans.impl.xb.ltgfmt;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlID;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlToken;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/ltgfmt/TestCase.class */
public interface TestCase extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(TestCase.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLTOOLS").resolveHandle("testcase939btype");

    String getDescription();

    XmlString xgetDescription();

    boolean isSetDescription();

    void setDescription(String str);

    void xsetDescription(XmlString xmlString);

    void unsetDescription();

    Files getFiles();

    void setFiles(Files files);

    Files addNewFiles();

    String getId();

    XmlID xgetId();

    boolean isSetId();

    void setId(String str);

    void xsetId(XmlID xmlID);

    void unsetId();

    String getOrigin();

    XmlToken xgetOrigin();

    boolean isSetOrigin();

    void setOrigin(String str);

    void xsetOrigin(XmlToken xmlToken);

    void unsetOrigin();

    boolean getModified();

    XmlBoolean xgetModified();

    boolean isSetModified();

    void setModified(boolean z);

    void xsetModified(XmlBoolean xmlBoolean);

    void unsetModified();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/ltgfmt/TestCase$Files.class */
    public interface Files extends XmlObject {
        public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Files.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLTOOLS").resolveHandle("files7c3eelemtype");

        FileDesc[] getFileArray();

        FileDesc getFileArray(int i);

        int sizeOfFileArray();

        void setFileArray(FileDesc[] fileDescArr);

        void setFileArray(int i, FileDesc fileDesc);

        FileDesc insertNewFile(int i);

        FileDesc addNewFile();

        void removeFile(int i);

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/ltgfmt/TestCase$Files$Factory.class */
        public static final class Factory {
            public static Files newInstance() {
                return (Files) XmlBeans.getContextTypeLoader().newInstance(Files.type, null);
            }

            public static Files newInstance(XmlOptions options) {
                return (Files) XmlBeans.getContextTypeLoader().newInstance(Files.type, options);
            }

            private Factory() {
            }
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/ltgfmt/TestCase$Factory.class */
    public static final class Factory {
        public static TestCase newInstance() {
            return (TestCase) XmlBeans.getContextTypeLoader().newInstance(TestCase.type, null);
        }

        public static TestCase newInstance(XmlOptions options) {
            return (TestCase) XmlBeans.getContextTypeLoader().newInstance(TestCase.type, options);
        }

        public static TestCase parse(String xmlAsString) throws XmlException {
            return (TestCase) XmlBeans.getContextTypeLoader().parse(xmlAsString, TestCase.type, (XmlOptions) null);
        }

        public static TestCase parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (TestCase) XmlBeans.getContextTypeLoader().parse(xmlAsString, TestCase.type, options);
        }

        public static TestCase parse(File file) throws XmlException, IOException {
            return (TestCase) XmlBeans.getContextTypeLoader().parse(file, TestCase.type, (XmlOptions) null);
        }

        public static TestCase parse(File file, XmlOptions options) throws XmlException, IOException {
            return (TestCase) XmlBeans.getContextTypeLoader().parse(file, TestCase.type, options);
        }

        public static TestCase parse(URL u) throws XmlException, IOException {
            return (TestCase) XmlBeans.getContextTypeLoader().parse(u, TestCase.type, (XmlOptions) null);
        }

        public static TestCase parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (TestCase) XmlBeans.getContextTypeLoader().parse(u, TestCase.type, options);
        }

        public static TestCase parse(InputStream is) throws XmlException, IOException {
            return (TestCase) XmlBeans.getContextTypeLoader().parse(is, TestCase.type, (XmlOptions) null);
        }

        public static TestCase parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (TestCase) XmlBeans.getContextTypeLoader().parse(is, TestCase.type, options);
        }

        public static TestCase parse(Reader r) throws XmlException, IOException {
            return (TestCase) XmlBeans.getContextTypeLoader().parse(r, TestCase.type, (XmlOptions) null);
        }

        public static TestCase parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (TestCase) XmlBeans.getContextTypeLoader().parse(r, TestCase.type, options);
        }

        public static TestCase parse(XMLStreamReader sr) throws XmlException {
            return (TestCase) XmlBeans.getContextTypeLoader().parse(sr, TestCase.type, (XmlOptions) null);
        }

        public static TestCase parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (TestCase) XmlBeans.getContextTypeLoader().parse(sr, TestCase.type, options);
        }

        public static TestCase parse(Node node) throws XmlException {
            return (TestCase) XmlBeans.getContextTypeLoader().parse(node, TestCase.type, (XmlOptions) null);
        }

        public static TestCase parse(Node node, XmlOptions options) throws XmlException {
            return (TestCase) XmlBeans.getContextTypeLoader().parse(node, TestCase.type, options);
        }

        public static TestCase parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (TestCase) XmlBeans.getContextTypeLoader().parse(xis, TestCase.type, (XmlOptions) null);
        }

        public static TestCase parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (TestCase) XmlBeans.getContextTypeLoader().parse(xis, TestCase.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, TestCase.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, TestCase.type, options);
        }

        private Factory() {
        }
    }
}

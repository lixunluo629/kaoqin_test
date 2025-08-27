package org.apache.xmlbeans.impl.xb.ltgfmt;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/ltgfmt/TestsDocument.class */
public interface TestsDocument extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(TestsDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLTOOLS").resolveHandle("tests5621doctype");

    Tests getTests();

    void setTests(Tests tests);

    Tests addNewTests();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/ltgfmt/TestsDocument$Tests.class */
    public interface Tests extends XmlObject {
        public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Tests.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLTOOLS").resolveHandle("tests9d6eelemtype");

        TestCase[] getTestArray();

        TestCase getTestArray(int i);

        int sizeOfTestArray();

        void setTestArray(TestCase[] testCaseArr);

        void setTestArray(int i, TestCase testCase);

        TestCase insertNewTest(int i);

        TestCase addNewTest();

        void removeTest(int i);

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/ltgfmt/TestsDocument$Tests$Factory.class */
        public static final class Factory {
            public static Tests newInstance() {
                return (Tests) XmlBeans.getContextTypeLoader().newInstance(Tests.type, null);
            }

            public static Tests newInstance(XmlOptions options) {
                return (Tests) XmlBeans.getContextTypeLoader().newInstance(Tests.type, options);
            }

            private Factory() {
            }
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/ltgfmt/TestsDocument$Factory.class */
    public static final class Factory {
        public static TestsDocument newInstance() {
            return (TestsDocument) XmlBeans.getContextTypeLoader().newInstance(TestsDocument.type, null);
        }

        public static TestsDocument newInstance(XmlOptions options) {
            return (TestsDocument) XmlBeans.getContextTypeLoader().newInstance(TestsDocument.type, options);
        }

        public static TestsDocument parse(String xmlAsString) throws XmlException {
            return (TestsDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, TestsDocument.type, (XmlOptions) null);
        }

        public static TestsDocument parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (TestsDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, TestsDocument.type, options);
        }

        public static TestsDocument parse(File file) throws XmlException, IOException {
            return (TestsDocument) XmlBeans.getContextTypeLoader().parse(file, TestsDocument.type, (XmlOptions) null);
        }

        public static TestsDocument parse(File file, XmlOptions options) throws XmlException, IOException {
            return (TestsDocument) XmlBeans.getContextTypeLoader().parse(file, TestsDocument.type, options);
        }

        public static TestsDocument parse(URL u) throws XmlException, IOException {
            return (TestsDocument) XmlBeans.getContextTypeLoader().parse(u, TestsDocument.type, (XmlOptions) null);
        }

        public static TestsDocument parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (TestsDocument) XmlBeans.getContextTypeLoader().parse(u, TestsDocument.type, options);
        }

        public static TestsDocument parse(InputStream is) throws XmlException, IOException {
            return (TestsDocument) XmlBeans.getContextTypeLoader().parse(is, TestsDocument.type, (XmlOptions) null);
        }

        public static TestsDocument parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (TestsDocument) XmlBeans.getContextTypeLoader().parse(is, TestsDocument.type, options);
        }

        public static TestsDocument parse(Reader r) throws XmlException, IOException {
            return (TestsDocument) XmlBeans.getContextTypeLoader().parse(r, TestsDocument.type, (XmlOptions) null);
        }

        public static TestsDocument parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (TestsDocument) XmlBeans.getContextTypeLoader().parse(r, TestsDocument.type, options);
        }

        public static TestsDocument parse(XMLStreamReader sr) throws XmlException {
            return (TestsDocument) XmlBeans.getContextTypeLoader().parse(sr, TestsDocument.type, (XmlOptions) null);
        }

        public static TestsDocument parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (TestsDocument) XmlBeans.getContextTypeLoader().parse(sr, TestsDocument.type, options);
        }

        public static TestsDocument parse(Node node) throws XmlException {
            return (TestsDocument) XmlBeans.getContextTypeLoader().parse(node, TestsDocument.type, (XmlOptions) null);
        }

        public static TestsDocument parse(Node node, XmlOptions options) throws XmlException {
            return (TestsDocument) XmlBeans.getContextTypeLoader().parse(node, TestsDocument.type, options);
        }

        public static TestsDocument parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (TestsDocument) XmlBeans.getContextTypeLoader().parse(xis, TestsDocument.type, (XmlOptions) null);
        }

        public static TestsDocument parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (TestsDocument) XmlBeans.getContextTypeLoader().parse(xis, TestsDocument.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, TestsDocument.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, TestsDocument.type, options);
        }

        private Factory() {
        }
    }
}

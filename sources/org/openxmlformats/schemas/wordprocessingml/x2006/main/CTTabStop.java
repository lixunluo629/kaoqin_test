package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigInteger;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabTlc;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTTabStop.class */
public interface CTTabStop extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTTabStop.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("cttabstop5ebbtype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTTabStop$Factory.class */
    public static final class Factory {
        public static CTTabStop newInstance() {
            return (CTTabStop) POIXMLTypeLoader.newInstance(CTTabStop.type, null);
        }

        public static CTTabStop newInstance(XmlOptions xmlOptions) {
            return (CTTabStop) POIXMLTypeLoader.newInstance(CTTabStop.type, xmlOptions);
        }

        public static CTTabStop parse(String str) throws XmlException {
            return (CTTabStop) POIXMLTypeLoader.parse(str, CTTabStop.type, (XmlOptions) null);
        }

        public static CTTabStop parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTTabStop) POIXMLTypeLoader.parse(str, CTTabStop.type, xmlOptions);
        }

        public static CTTabStop parse(File file) throws XmlException, IOException {
            return (CTTabStop) POIXMLTypeLoader.parse(file, CTTabStop.type, (XmlOptions) null);
        }

        public static CTTabStop parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTabStop) POIXMLTypeLoader.parse(file, CTTabStop.type, xmlOptions);
        }

        public static CTTabStop parse(URL url) throws XmlException, IOException {
            return (CTTabStop) POIXMLTypeLoader.parse(url, CTTabStop.type, (XmlOptions) null);
        }

        public static CTTabStop parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTabStop) POIXMLTypeLoader.parse(url, CTTabStop.type, xmlOptions);
        }

        public static CTTabStop parse(InputStream inputStream) throws XmlException, IOException {
            return (CTTabStop) POIXMLTypeLoader.parse(inputStream, CTTabStop.type, (XmlOptions) null);
        }

        public static CTTabStop parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTabStop) POIXMLTypeLoader.parse(inputStream, CTTabStop.type, xmlOptions);
        }

        public static CTTabStop parse(Reader reader) throws XmlException, IOException {
            return (CTTabStop) POIXMLTypeLoader.parse(reader, CTTabStop.type, (XmlOptions) null);
        }

        public static CTTabStop parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTabStop) POIXMLTypeLoader.parse(reader, CTTabStop.type, xmlOptions);
        }

        public static CTTabStop parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTTabStop) POIXMLTypeLoader.parse(xMLStreamReader, CTTabStop.type, (XmlOptions) null);
        }

        public static CTTabStop parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTTabStop) POIXMLTypeLoader.parse(xMLStreamReader, CTTabStop.type, xmlOptions);
        }

        public static CTTabStop parse(Node node) throws XmlException {
            return (CTTabStop) POIXMLTypeLoader.parse(node, CTTabStop.type, (XmlOptions) null);
        }

        public static CTTabStop parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTTabStop) POIXMLTypeLoader.parse(node, CTTabStop.type, xmlOptions);
        }

        public static CTTabStop parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTTabStop) POIXMLTypeLoader.parse(xMLInputStream, CTTabStop.type, (XmlOptions) null);
        }

        public static CTTabStop parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTTabStop) POIXMLTypeLoader.parse(xMLInputStream, CTTabStop.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTabStop.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTabStop.type, xmlOptions);
        }

        private Factory() {
        }
    }

    STTabJc.Enum getVal();

    STTabJc xgetVal();

    void setVal(STTabJc.Enum r1);

    void xsetVal(STTabJc sTTabJc);

    STTabTlc.Enum getLeader();

    STTabTlc xgetLeader();

    boolean isSetLeader();

    void setLeader(STTabTlc.Enum r1);

    void xsetLeader(STTabTlc sTTabTlc);

    void unsetLeader();

    BigInteger getPos();

    STSignedTwipsMeasure xgetPos();

    void setPos(BigInteger bigInteger);

    void xsetPos(STSignedTwipsMeasure sTSignedTwipsMeasure);
}

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
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTLsdException.class */
public interface CTLsdException extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTLsdException.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctlsdexceptiona296type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTLsdException$Factory.class */
    public static final class Factory {
        public static CTLsdException newInstance() {
            return (CTLsdException) POIXMLTypeLoader.newInstance(CTLsdException.type, null);
        }

        public static CTLsdException newInstance(XmlOptions xmlOptions) {
            return (CTLsdException) POIXMLTypeLoader.newInstance(CTLsdException.type, xmlOptions);
        }

        public static CTLsdException parse(String str) throws XmlException {
            return (CTLsdException) POIXMLTypeLoader.parse(str, CTLsdException.type, (XmlOptions) null);
        }

        public static CTLsdException parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTLsdException) POIXMLTypeLoader.parse(str, CTLsdException.type, xmlOptions);
        }

        public static CTLsdException parse(File file) throws XmlException, IOException {
            return (CTLsdException) POIXMLTypeLoader.parse(file, CTLsdException.type, (XmlOptions) null);
        }

        public static CTLsdException parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLsdException) POIXMLTypeLoader.parse(file, CTLsdException.type, xmlOptions);
        }

        public static CTLsdException parse(URL url) throws XmlException, IOException {
            return (CTLsdException) POIXMLTypeLoader.parse(url, CTLsdException.type, (XmlOptions) null);
        }

        public static CTLsdException parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLsdException) POIXMLTypeLoader.parse(url, CTLsdException.type, xmlOptions);
        }

        public static CTLsdException parse(InputStream inputStream) throws XmlException, IOException {
            return (CTLsdException) POIXMLTypeLoader.parse(inputStream, CTLsdException.type, (XmlOptions) null);
        }

        public static CTLsdException parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLsdException) POIXMLTypeLoader.parse(inputStream, CTLsdException.type, xmlOptions);
        }

        public static CTLsdException parse(Reader reader) throws XmlException, IOException {
            return (CTLsdException) POIXMLTypeLoader.parse(reader, CTLsdException.type, (XmlOptions) null);
        }

        public static CTLsdException parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLsdException) POIXMLTypeLoader.parse(reader, CTLsdException.type, xmlOptions);
        }

        public static CTLsdException parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTLsdException) POIXMLTypeLoader.parse(xMLStreamReader, CTLsdException.type, (XmlOptions) null);
        }

        public static CTLsdException parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTLsdException) POIXMLTypeLoader.parse(xMLStreamReader, CTLsdException.type, xmlOptions);
        }

        public static CTLsdException parse(Node node) throws XmlException {
            return (CTLsdException) POIXMLTypeLoader.parse(node, CTLsdException.type, (XmlOptions) null);
        }

        public static CTLsdException parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTLsdException) POIXMLTypeLoader.parse(node, CTLsdException.type, xmlOptions);
        }

        public static CTLsdException parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTLsdException) POIXMLTypeLoader.parse(xMLInputStream, CTLsdException.type, (XmlOptions) null);
        }

        public static CTLsdException parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTLsdException) POIXMLTypeLoader.parse(xMLInputStream, CTLsdException.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTLsdException.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTLsdException.type, xmlOptions);
        }

        private Factory() {
        }
    }

    String getName();

    STString xgetName();

    void setName(String str);

    void xsetName(STString sTString);

    STOnOff.Enum getLocked();

    STOnOff xgetLocked();

    boolean isSetLocked();

    void setLocked(STOnOff.Enum r1);

    void xsetLocked(STOnOff sTOnOff);

    void unsetLocked();

    BigInteger getUiPriority();

    STDecimalNumber xgetUiPriority();

    boolean isSetUiPriority();

    void setUiPriority(BigInteger bigInteger);

    void xsetUiPriority(STDecimalNumber sTDecimalNumber);

    void unsetUiPriority();

    STOnOff.Enum getSemiHidden();

    STOnOff xgetSemiHidden();

    boolean isSetSemiHidden();

    void setSemiHidden(STOnOff.Enum r1);

    void xsetSemiHidden(STOnOff sTOnOff);

    void unsetSemiHidden();

    STOnOff.Enum getUnhideWhenUsed();

    STOnOff xgetUnhideWhenUsed();

    boolean isSetUnhideWhenUsed();

    void setUnhideWhenUsed(STOnOff.Enum r1);

    void xsetUnhideWhenUsed(STOnOff sTOnOff);

    void unsetUnhideWhenUsed();

    STOnOff.Enum getQFormat();

    STOnOff xgetQFormat();

    boolean isSetQFormat();

    void setQFormat(STOnOff.Enum r1);

    void xsetQFormat(STOnOff sTOnOff);

    void unsetQFormat();
}

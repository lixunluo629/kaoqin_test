package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigInteger;
import java.net.URL;
import java.util.List;
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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTLatentStyles.class */
public interface CTLatentStyles extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTLatentStyles.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctlatentstyles2e3atype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTLatentStyles$Factory.class */
    public static final class Factory {
        public static CTLatentStyles newInstance() {
            return (CTLatentStyles) POIXMLTypeLoader.newInstance(CTLatentStyles.type, null);
        }

        public static CTLatentStyles newInstance(XmlOptions xmlOptions) {
            return (CTLatentStyles) POIXMLTypeLoader.newInstance(CTLatentStyles.type, xmlOptions);
        }

        public static CTLatentStyles parse(String str) throws XmlException {
            return (CTLatentStyles) POIXMLTypeLoader.parse(str, CTLatentStyles.type, (XmlOptions) null);
        }

        public static CTLatentStyles parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTLatentStyles) POIXMLTypeLoader.parse(str, CTLatentStyles.type, xmlOptions);
        }

        public static CTLatentStyles parse(File file) throws XmlException, IOException {
            return (CTLatentStyles) POIXMLTypeLoader.parse(file, CTLatentStyles.type, (XmlOptions) null);
        }

        public static CTLatentStyles parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLatentStyles) POIXMLTypeLoader.parse(file, CTLatentStyles.type, xmlOptions);
        }

        public static CTLatentStyles parse(URL url) throws XmlException, IOException {
            return (CTLatentStyles) POIXMLTypeLoader.parse(url, CTLatentStyles.type, (XmlOptions) null);
        }

        public static CTLatentStyles parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLatentStyles) POIXMLTypeLoader.parse(url, CTLatentStyles.type, xmlOptions);
        }

        public static CTLatentStyles parse(InputStream inputStream) throws XmlException, IOException {
            return (CTLatentStyles) POIXMLTypeLoader.parse(inputStream, CTLatentStyles.type, (XmlOptions) null);
        }

        public static CTLatentStyles parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLatentStyles) POIXMLTypeLoader.parse(inputStream, CTLatentStyles.type, xmlOptions);
        }

        public static CTLatentStyles parse(Reader reader) throws XmlException, IOException {
            return (CTLatentStyles) POIXMLTypeLoader.parse(reader, CTLatentStyles.type, (XmlOptions) null);
        }

        public static CTLatentStyles parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLatentStyles) POIXMLTypeLoader.parse(reader, CTLatentStyles.type, xmlOptions);
        }

        public static CTLatentStyles parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTLatentStyles) POIXMLTypeLoader.parse(xMLStreamReader, CTLatentStyles.type, (XmlOptions) null);
        }

        public static CTLatentStyles parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTLatentStyles) POIXMLTypeLoader.parse(xMLStreamReader, CTLatentStyles.type, xmlOptions);
        }

        public static CTLatentStyles parse(Node node) throws XmlException {
            return (CTLatentStyles) POIXMLTypeLoader.parse(node, CTLatentStyles.type, (XmlOptions) null);
        }

        public static CTLatentStyles parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTLatentStyles) POIXMLTypeLoader.parse(node, CTLatentStyles.type, xmlOptions);
        }

        public static CTLatentStyles parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTLatentStyles) POIXMLTypeLoader.parse(xMLInputStream, CTLatentStyles.type, (XmlOptions) null);
        }

        public static CTLatentStyles parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTLatentStyles) POIXMLTypeLoader.parse(xMLInputStream, CTLatentStyles.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTLatentStyles.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTLatentStyles.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<CTLsdException> getLsdExceptionList();

    CTLsdException[] getLsdExceptionArray();

    CTLsdException getLsdExceptionArray(int i);

    int sizeOfLsdExceptionArray();

    void setLsdExceptionArray(CTLsdException[] cTLsdExceptionArr);

    void setLsdExceptionArray(int i, CTLsdException cTLsdException);

    CTLsdException insertNewLsdException(int i);

    CTLsdException addNewLsdException();

    void removeLsdException(int i);

    STOnOff.Enum getDefLockedState();

    STOnOff xgetDefLockedState();

    boolean isSetDefLockedState();

    void setDefLockedState(STOnOff.Enum r1);

    void xsetDefLockedState(STOnOff sTOnOff);

    void unsetDefLockedState();

    BigInteger getDefUIPriority();

    STDecimalNumber xgetDefUIPriority();

    boolean isSetDefUIPriority();

    void setDefUIPriority(BigInteger bigInteger);

    void xsetDefUIPriority(STDecimalNumber sTDecimalNumber);

    void unsetDefUIPriority();

    STOnOff.Enum getDefSemiHidden();

    STOnOff xgetDefSemiHidden();

    boolean isSetDefSemiHidden();

    void setDefSemiHidden(STOnOff.Enum r1);

    void xsetDefSemiHidden(STOnOff sTOnOff);

    void unsetDefSemiHidden();

    STOnOff.Enum getDefUnhideWhenUsed();

    STOnOff xgetDefUnhideWhenUsed();

    boolean isSetDefUnhideWhenUsed();

    void setDefUnhideWhenUsed(STOnOff.Enum r1);

    void xsetDefUnhideWhenUsed(STOnOff sTOnOff);

    void unsetDefUnhideWhenUsed();

    STOnOff.Enum getDefQFormat();

    STOnOff xgetDefQFormat();

    boolean isSetDefQFormat();

    void setDefQFormat(STOnOff.Enum r1);

    void xsetDefQFormat(STOnOff sTOnOff);

    void unsetDefQFormat();

    BigInteger getCount();

    STDecimalNumber xgetCount();

    boolean isSetCount();

    void setCount(BigInteger bigInteger);

    void xsetCount(STDecimalNumber sTDecimalNumber);

    void unsetCount();
}

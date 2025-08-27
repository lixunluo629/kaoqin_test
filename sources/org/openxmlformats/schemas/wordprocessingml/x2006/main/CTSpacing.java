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
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STLineSpacingRule;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTSpacing.class */
public interface CTSpacing extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTSpacing.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctspacingff2ftype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTSpacing$Factory.class */
    public static final class Factory {
        public static CTSpacing newInstance() {
            return (CTSpacing) POIXMLTypeLoader.newInstance(CTSpacing.type, null);
        }

        public static CTSpacing newInstance(XmlOptions xmlOptions) {
            return (CTSpacing) POIXMLTypeLoader.newInstance(CTSpacing.type, xmlOptions);
        }

        public static CTSpacing parse(String str) throws XmlException {
            return (CTSpacing) POIXMLTypeLoader.parse(str, CTSpacing.type, (XmlOptions) null);
        }

        public static CTSpacing parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTSpacing) POIXMLTypeLoader.parse(str, CTSpacing.type, xmlOptions);
        }

        public static CTSpacing parse(File file) throws XmlException, IOException {
            return (CTSpacing) POIXMLTypeLoader.parse(file, CTSpacing.type, (XmlOptions) null);
        }

        public static CTSpacing parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSpacing) POIXMLTypeLoader.parse(file, CTSpacing.type, xmlOptions);
        }

        public static CTSpacing parse(URL url) throws XmlException, IOException {
            return (CTSpacing) POIXMLTypeLoader.parse(url, CTSpacing.type, (XmlOptions) null);
        }

        public static CTSpacing parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSpacing) POIXMLTypeLoader.parse(url, CTSpacing.type, xmlOptions);
        }

        public static CTSpacing parse(InputStream inputStream) throws XmlException, IOException {
            return (CTSpacing) POIXMLTypeLoader.parse(inputStream, CTSpacing.type, (XmlOptions) null);
        }

        public static CTSpacing parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSpacing) POIXMLTypeLoader.parse(inputStream, CTSpacing.type, xmlOptions);
        }

        public static CTSpacing parse(Reader reader) throws XmlException, IOException {
            return (CTSpacing) POIXMLTypeLoader.parse(reader, CTSpacing.type, (XmlOptions) null);
        }

        public static CTSpacing parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSpacing) POIXMLTypeLoader.parse(reader, CTSpacing.type, xmlOptions);
        }

        public static CTSpacing parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTSpacing) POIXMLTypeLoader.parse(xMLStreamReader, CTSpacing.type, (XmlOptions) null);
        }

        public static CTSpacing parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTSpacing) POIXMLTypeLoader.parse(xMLStreamReader, CTSpacing.type, xmlOptions);
        }

        public static CTSpacing parse(Node node) throws XmlException {
            return (CTSpacing) POIXMLTypeLoader.parse(node, CTSpacing.type, (XmlOptions) null);
        }

        public static CTSpacing parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTSpacing) POIXMLTypeLoader.parse(node, CTSpacing.type, xmlOptions);
        }

        public static CTSpacing parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTSpacing) POIXMLTypeLoader.parse(xMLInputStream, CTSpacing.type, (XmlOptions) null);
        }

        public static CTSpacing parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTSpacing) POIXMLTypeLoader.parse(xMLInputStream, CTSpacing.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTSpacing.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTSpacing.type, xmlOptions);
        }

        private Factory() {
        }
    }

    BigInteger getBefore();

    STTwipsMeasure xgetBefore();

    boolean isSetBefore();

    void setBefore(BigInteger bigInteger);

    void xsetBefore(STTwipsMeasure sTTwipsMeasure);

    void unsetBefore();

    BigInteger getBeforeLines();

    STDecimalNumber xgetBeforeLines();

    boolean isSetBeforeLines();

    void setBeforeLines(BigInteger bigInteger);

    void xsetBeforeLines(STDecimalNumber sTDecimalNumber);

    void unsetBeforeLines();

    STOnOff.Enum getBeforeAutospacing();

    STOnOff xgetBeforeAutospacing();

    boolean isSetBeforeAutospacing();

    void setBeforeAutospacing(STOnOff.Enum r1);

    void xsetBeforeAutospacing(STOnOff sTOnOff);

    void unsetBeforeAutospacing();

    BigInteger getAfter();

    STTwipsMeasure xgetAfter();

    boolean isSetAfter();

    void setAfter(BigInteger bigInteger);

    void xsetAfter(STTwipsMeasure sTTwipsMeasure);

    void unsetAfter();

    BigInteger getAfterLines();

    STDecimalNumber xgetAfterLines();

    boolean isSetAfterLines();

    void setAfterLines(BigInteger bigInteger);

    void xsetAfterLines(STDecimalNumber sTDecimalNumber);

    void unsetAfterLines();

    STOnOff.Enum getAfterAutospacing();

    STOnOff xgetAfterAutospacing();

    boolean isSetAfterAutospacing();

    void setAfterAutospacing(STOnOff.Enum r1);

    void xsetAfterAutospacing(STOnOff sTOnOff);

    void unsetAfterAutospacing();

    BigInteger getLine();

    STSignedTwipsMeasure xgetLine();

    boolean isSetLine();

    void setLine(BigInteger bigInteger);

    void xsetLine(STSignedTwipsMeasure sTSignedTwipsMeasure);

    void unsetLine();

    STLineSpacingRule.Enum getLineRule();

    STLineSpacingRule xgetLineRule();

    boolean isSetLineRule();

    void setLineRule(STLineSpacingRule.Enum r1);

    void xsetLineRule(STLineSpacingRule sTLineSpacingRule);

    void unsetLineRule();
}

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
import org.apache.xmlbeans.XmlBase64Binary;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STAlgClass;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STAlgType;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STCryptProv;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STDocProtect;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTDocProtect.class */
public interface CTDocProtect extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTDocProtect.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctdocprotectc611type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTDocProtect$Factory.class */
    public static final class Factory {
        public static CTDocProtect newInstance() {
            return (CTDocProtect) POIXMLTypeLoader.newInstance(CTDocProtect.type, null);
        }

        public static CTDocProtect newInstance(XmlOptions xmlOptions) {
            return (CTDocProtect) POIXMLTypeLoader.newInstance(CTDocProtect.type, xmlOptions);
        }

        public static CTDocProtect parse(String str) throws XmlException {
            return (CTDocProtect) POIXMLTypeLoader.parse(str, CTDocProtect.type, (XmlOptions) null);
        }

        public static CTDocProtect parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTDocProtect) POIXMLTypeLoader.parse(str, CTDocProtect.type, xmlOptions);
        }

        public static CTDocProtect parse(File file) throws XmlException, IOException {
            return (CTDocProtect) POIXMLTypeLoader.parse(file, CTDocProtect.type, (XmlOptions) null);
        }

        public static CTDocProtect parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTDocProtect) POIXMLTypeLoader.parse(file, CTDocProtect.type, xmlOptions);
        }

        public static CTDocProtect parse(URL url) throws XmlException, IOException {
            return (CTDocProtect) POIXMLTypeLoader.parse(url, CTDocProtect.type, (XmlOptions) null);
        }

        public static CTDocProtect parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTDocProtect) POIXMLTypeLoader.parse(url, CTDocProtect.type, xmlOptions);
        }

        public static CTDocProtect parse(InputStream inputStream) throws XmlException, IOException {
            return (CTDocProtect) POIXMLTypeLoader.parse(inputStream, CTDocProtect.type, (XmlOptions) null);
        }

        public static CTDocProtect parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTDocProtect) POIXMLTypeLoader.parse(inputStream, CTDocProtect.type, xmlOptions);
        }

        public static CTDocProtect parse(Reader reader) throws XmlException, IOException {
            return (CTDocProtect) POIXMLTypeLoader.parse(reader, CTDocProtect.type, (XmlOptions) null);
        }

        public static CTDocProtect parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTDocProtect) POIXMLTypeLoader.parse(reader, CTDocProtect.type, xmlOptions);
        }

        public static CTDocProtect parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTDocProtect) POIXMLTypeLoader.parse(xMLStreamReader, CTDocProtect.type, (XmlOptions) null);
        }

        public static CTDocProtect parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTDocProtect) POIXMLTypeLoader.parse(xMLStreamReader, CTDocProtect.type, xmlOptions);
        }

        public static CTDocProtect parse(Node node) throws XmlException {
            return (CTDocProtect) POIXMLTypeLoader.parse(node, CTDocProtect.type, (XmlOptions) null);
        }

        public static CTDocProtect parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTDocProtect) POIXMLTypeLoader.parse(node, CTDocProtect.type, xmlOptions);
        }

        public static CTDocProtect parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTDocProtect) POIXMLTypeLoader.parse(xMLInputStream, CTDocProtect.type, (XmlOptions) null);
        }

        public static CTDocProtect parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTDocProtect) POIXMLTypeLoader.parse(xMLInputStream, CTDocProtect.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTDocProtect.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTDocProtect.type, xmlOptions);
        }

        private Factory() {
        }
    }

    STDocProtect.Enum getEdit();

    STDocProtect xgetEdit();

    boolean isSetEdit();

    void setEdit(STDocProtect.Enum r1);

    void xsetEdit(STDocProtect sTDocProtect);

    void unsetEdit();

    STOnOff.Enum getFormatting();

    STOnOff xgetFormatting();

    boolean isSetFormatting();

    void setFormatting(STOnOff.Enum r1);

    void xsetFormatting(STOnOff sTOnOff);

    void unsetFormatting();

    STOnOff.Enum getEnforcement();

    STOnOff xgetEnforcement();

    boolean isSetEnforcement();

    void setEnforcement(STOnOff.Enum r1);

    void xsetEnforcement(STOnOff sTOnOff);

    void unsetEnforcement();

    STCryptProv.Enum getCryptProviderType();

    STCryptProv xgetCryptProviderType();

    boolean isSetCryptProviderType();

    void setCryptProviderType(STCryptProv.Enum r1);

    void xsetCryptProviderType(STCryptProv sTCryptProv);

    void unsetCryptProviderType();

    STAlgClass.Enum getCryptAlgorithmClass();

    STAlgClass xgetCryptAlgorithmClass();

    boolean isSetCryptAlgorithmClass();

    void setCryptAlgorithmClass(STAlgClass.Enum r1);

    void xsetCryptAlgorithmClass(STAlgClass sTAlgClass);

    void unsetCryptAlgorithmClass();

    STAlgType.Enum getCryptAlgorithmType();

    STAlgType xgetCryptAlgorithmType();

    boolean isSetCryptAlgorithmType();

    void setCryptAlgorithmType(STAlgType.Enum r1);

    void xsetCryptAlgorithmType(STAlgType sTAlgType);

    void unsetCryptAlgorithmType();

    BigInteger getCryptAlgorithmSid();

    STDecimalNumber xgetCryptAlgorithmSid();

    boolean isSetCryptAlgorithmSid();

    void setCryptAlgorithmSid(BigInteger bigInteger);

    void xsetCryptAlgorithmSid(STDecimalNumber sTDecimalNumber);

    void unsetCryptAlgorithmSid();

    BigInteger getCryptSpinCount();

    STDecimalNumber xgetCryptSpinCount();

    boolean isSetCryptSpinCount();

    void setCryptSpinCount(BigInteger bigInteger);

    void xsetCryptSpinCount(STDecimalNumber sTDecimalNumber);

    void unsetCryptSpinCount();

    String getCryptProvider();

    STString xgetCryptProvider();

    boolean isSetCryptProvider();

    void setCryptProvider(String str);

    void xsetCryptProvider(STString sTString);

    void unsetCryptProvider();

    byte[] getAlgIdExt();

    STLongHexNumber xgetAlgIdExt();

    boolean isSetAlgIdExt();

    void setAlgIdExt(byte[] bArr);

    void xsetAlgIdExt(STLongHexNumber sTLongHexNumber);

    void unsetAlgIdExt();

    String getAlgIdExtSource();

    STString xgetAlgIdExtSource();

    boolean isSetAlgIdExtSource();

    void setAlgIdExtSource(String str);

    void xsetAlgIdExtSource(STString sTString);

    void unsetAlgIdExtSource();

    byte[] getCryptProviderTypeExt();

    STLongHexNumber xgetCryptProviderTypeExt();

    boolean isSetCryptProviderTypeExt();

    void setCryptProviderTypeExt(byte[] bArr);

    void xsetCryptProviderTypeExt(STLongHexNumber sTLongHexNumber);

    void unsetCryptProviderTypeExt();

    String getCryptProviderTypeExtSource();

    STString xgetCryptProviderTypeExtSource();

    boolean isSetCryptProviderTypeExtSource();

    void setCryptProviderTypeExtSource(String str);

    void xsetCryptProviderTypeExtSource(STString sTString);

    void unsetCryptProviderTypeExtSource();

    byte[] getHash();

    XmlBase64Binary xgetHash();

    boolean isSetHash();

    void setHash(byte[] bArr);

    void xsetHash(XmlBase64Binary xmlBase64Binary);

    void unsetHash();

    byte[] getSalt();

    XmlBase64Binary xgetSalt();

    boolean isSetSalt();

    void setSalt(byte[] bArr);

    void xsetSalt(XmlBase64Binary xmlBase64Binary);

    void unsetSalt();
}

package com.microsoft.schemas.office.x2006.digsig;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlAnyURI;
import org.apache.xmlbeans.XmlBase64Binary;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlInt;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/x2006/digsig/CTSignatureInfoV1.class */
public interface CTSignatureInfoV1 extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTSignatureInfoV1.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3F193EE11A2F798ACF65489B9E6078").resolveHandle("ctsignatureinfov13a5ftype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/x2006/digsig/CTSignatureInfoV1$Factory.class */
    public static final class Factory {
        public static CTSignatureInfoV1 newInstance() {
            return (CTSignatureInfoV1) POIXMLTypeLoader.newInstance(CTSignatureInfoV1.type, null);
        }

        public static CTSignatureInfoV1 newInstance(XmlOptions xmlOptions) {
            return (CTSignatureInfoV1) POIXMLTypeLoader.newInstance(CTSignatureInfoV1.type, xmlOptions);
        }

        public static CTSignatureInfoV1 parse(String str) throws XmlException {
            return (CTSignatureInfoV1) POIXMLTypeLoader.parse(str, CTSignatureInfoV1.type, (XmlOptions) null);
        }

        public static CTSignatureInfoV1 parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTSignatureInfoV1) POIXMLTypeLoader.parse(str, CTSignatureInfoV1.type, xmlOptions);
        }

        public static CTSignatureInfoV1 parse(File file) throws XmlException, IOException {
            return (CTSignatureInfoV1) POIXMLTypeLoader.parse(file, CTSignatureInfoV1.type, (XmlOptions) null);
        }

        public static CTSignatureInfoV1 parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSignatureInfoV1) POIXMLTypeLoader.parse(file, CTSignatureInfoV1.type, xmlOptions);
        }

        public static CTSignatureInfoV1 parse(URL url) throws XmlException, IOException {
            return (CTSignatureInfoV1) POIXMLTypeLoader.parse(url, CTSignatureInfoV1.type, (XmlOptions) null);
        }

        public static CTSignatureInfoV1 parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSignatureInfoV1) POIXMLTypeLoader.parse(url, CTSignatureInfoV1.type, xmlOptions);
        }

        public static CTSignatureInfoV1 parse(InputStream inputStream) throws XmlException, IOException {
            return (CTSignatureInfoV1) POIXMLTypeLoader.parse(inputStream, CTSignatureInfoV1.type, (XmlOptions) null);
        }

        public static CTSignatureInfoV1 parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSignatureInfoV1) POIXMLTypeLoader.parse(inputStream, CTSignatureInfoV1.type, xmlOptions);
        }

        public static CTSignatureInfoV1 parse(Reader reader) throws XmlException, IOException {
            return (CTSignatureInfoV1) POIXMLTypeLoader.parse(reader, CTSignatureInfoV1.type, (XmlOptions) null);
        }

        public static CTSignatureInfoV1 parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSignatureInfoV1) POIXMLTypeLoader.parse(reader, CTSignatureInfoV1.type, xmlOptions);
        }

        public static CTSignatureInfoV1 parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTSignatureInfoV1) POIXMLTypeLoader.parse(xMLStreamReader, CTSignatureInfoV1.type, (XmlOptions) null);
        }

        public static CTSignatureInfoV1 parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTSignatureInfoV1) POIXMLTypeLoader.parse(xMLStreamReader, CTSignatureInfoV1.type, xmlOptions);
        }

        public static CTSignatureInfoV1 parse(Node node) throws XmlException {
            return (CTSignatureInfoV1) POIXMLTypeLoader.parse(node, CTSignatureInfoV1.type, (XmlOptions) null);
        }

        public static CTSignatureInfoV1 parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTSignatureInfoV1) POIXMLTypeLoader.parse(node, CTSignatureInfoV1.type, xmlOptions);
        }

        public static CTSignatureInfoV1 parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTSignatureInfoV1) POIXMLTypeLoader.parse(xMLInputStream, CTSignatureInfoV1.type, (XmlOptions) null);
        }

        public static CTSignatureInfoV1 parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTSignatureInfoV1) POIXMLTypeLoader.parse(xMLInputStream, CTSignatureInfoV1.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTSignatureInfoV1.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTSignatureInfoV1.type, xmlOptions);
        }

        private Factory() {
        }
    }

    String getSetupID();

    STUniqueIdentifierWithBraces xgetSetupID();

    void setSetupID(String str);

    void xsetSetupID(STUniqueIdentifierWithBraces sTUniqueIdentifierWithBraces);

    String getSignatureText();

    STSignatureText xgetSignatureText();

    void setSignatureText(String str);

    void xsetSignatureText(STSignatureText sTSignatureText);

    byte[] getSignatureImage();

    XmlBase64Binary xgetSignatureImage();

    void setSignatureImage(byte[] bArr);

    void xsetSignatureImage(XmlBase64Binary xmlBase64Binary);

    String getSignatureComments();

    STSignatureComments xgetSignatureComments();

    void setSignatureComments(String str);

    void xsetSignatureComments(STSignatureComments sTSignatureComments);

    String getWindowsVersion();

    STVersion xgetWindowsVersion();

    void setWindowsVersion(String str);

    void xsetWindowsVersion(STVersion sTVersion);

    String getOfficeVersion();

    STVersion xgetOfficeVersion();

    void setOfficeVersion(String str);

    void xsetOfficeVersion(STVersion sTVersion);

    String getApplicationVersion();

    STVersion xgetApplicationVersion();

    void setApplicationVersion(String str);

    void xsetApplicationVersion(STVersion sTVersion);

    int getMonitors();

    STPositiveInteger xgetMonitors();

    void setMonitors(int i);

    void xsetMonitors(STPositiveInteger sTPositiveInteger);

    int getHorizontalResolution();

    STPositiveInteger xgetHorizontalResolution();

    void setHorizontalResolution(int i);

    void xsetHorizontalResolution(STPositiveInteger sTPositiveInteger);

    int getVerticalResolution();

    STPositiveInteger xgetVerticalResolution();

    void setVerticalResolution(int i);

    void xsetVerticalResolution(STPositiveInteger sTPositiveInteger);

    int getColorDepth();

    STPositiveInteger xgetColorDepth();

    void setColorDepth(int i);

    void xsetColorDepth(STPositiveInteger sTPositiveInteger);

    String getSignatureProviderId();

    STUniqueIdentifierWithBraces xgetSignatureProviderId();

    void setSignatureProviderId(String str);

    void xsetSignatureProviderId(STUniqueIdentifierWithBraces sTUniqueIdentifierWithBraces);

    String getSignatureProviderUrl();

    STSignatureProviderUrl xgetSignatureProviderUrl();

    void setSignatureProviderUrl(String str);

    void xsetSignatureProviderUrl(STSignatureProviderUrl sTSignatureProviderUrl);

    int getSignatureProviderDetails();

    XmlInt xgetSignatureProviderDetails();

    void setSignatureProviderDetails(int i);

    void xsetSignatureProviderDetails(XmlInt xmlInt);

    int getSignatureType();

    STSignatureType xgetSignatureType();

    void setSignatureType(int i);

    void xsetSignatureType(STSignatureType sTSignatureType);

    String getDelegateSuggestedSigner();

    XmlString xgetDelegateSuggestedSigner();

    boolean isSetDelegateSuggestedSigner();

    void setDelegateSuggestedSigner(String str);

    void xsetDelegateSuggestedSigner(XmlString xmlString);

    void unsetDelegateSuggestedSigner();

    String getDelegateSuggestedSigner2();

    XmlString xgetDelegateSuggestedSigner2();

    boolean isSetDelegateSuggestedSigner2();

    void setDelegateSuggestedSigner2(String str);

    void xsetDelegateSuggestedSigner2(XmlString xmlString);

    void unsetDelegateSuggestedSigner2();

    String getDelegateSuggestedSignerEmail();

    XmlString xgetDelegateSuggestedSignerEmail();

    boolean isSetDelegateSuggestedSignerEmail();

    void setDelegateSuggestedSignerEmail(String str);

    void xsetDelegateSuggestedSignerEmail(XmlString xmlString);

    void unsetDelegateSuggestedSignerEmail();

    String getManifestHashAlgorithm();

    XmlAnyURI xgetManifestHashAlgorithm();

    boolean isSetManifestHashAlgorithm();

    void setManifestHashAlgorithm(String str);

    void xsetManifestHashAlgorithm(XmlAnyURI xmlAnyURI);

    void unsetManifestHashAlgorithm();
}

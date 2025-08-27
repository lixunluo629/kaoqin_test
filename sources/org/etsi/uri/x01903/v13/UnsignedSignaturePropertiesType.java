package org.etsi.uri.x01903.v13;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.List;
import javax.xml.stream.XMLStreamReader;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlID;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/etsi/uri/x01903/v13/UnsignedSignaturePropertiesType.class */
public interface UnsignedSignaturePropertiesType extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(UnsignedSignaturePropertiesType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3F193EE11A2F798ACF65489B9E6078").resolveHandle("unsignedsignaturepropertiestypecf32type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/etsi/uri/x01903/v13/UnsignedSignaturePropertiesType$Factory.class */
    public static final class Factory {
        public static UnsignedSignaturePropertiesType newInstance() {
            return (UnsignedSignaturePropertiesType) POIXMLTypeLoader.newInstance(UnsignedSignaturePropertiesType.type, null);
        }

        public static UnsignedSignaturePropertiesType newInstance(XmlOptions xmlOptions) {
            return (UnsignedSignaturePropertiesType) POIXMLTypeLoader.newInstance(UnsignedSignaturePropertiesType.type, xmlOptions);
        }

        public static UnsignedSignaturePropertiesType parse(String str) throws XmlException {
            return (UnsignedSignaturePropertiesType) POIXMLTypeLoader.parse(str, UnsignedSignaturePropertiesType.type, (XmlOptions) null);
        }

        public static UnsignedSignaturePropertiesType parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (UnsignedSignaturePropertiesType) POIXMLTypeLoader.parse(str, UnsignedSignaturePropertiesType.type, xmlOptions);
        }

        public static UnsignedSignaturePropertiesType parse(File file) throws XmlException, IOException {
            return (UnsignedSignaturePropertiesType) POIXMLTypeLoader.parse(file, UnsignedSignaturePropertiesType.type, (XmlOptions) null);
        }

        public static UnsignedSignaturePropertiesType parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (UnsignedSignaturePropertiesType) POIXMLTypeLoader.parse(file, UnsignedSignaturePropertiesType.type, xmlOptions);
        }

        public static UnsignedSignaturePropertiesType parse(URL url) throws XmlException, IOException {
            return (UnsignedSignaturePropertiesType) POIXMLTypeLoader.parse(url, UnsignedSignaturePropertiesType.type, (XmlOptions) null);
        }

        public static UnsignedSignaturePropertiesType parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (UnsignedSignaturePropertiesType) POIXMLTypeLoader.parse(url, UnsignedSignaturePropertiesType.type, xmlOptions);
        }

        public static UnsignedSignaturePropertiesType parse(InputStream inputStream) throws XmlException, IOException {
            return (UnsignedSignaturePropertiesType) POIXMLTypeLoader.parse(inputStream, UnsignedSignaturePropertiesType.type, (XmlOptions) null);
        }

        public static UnsignedSignaturePropertiesType parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (UnsignedSignaturePropertiesType) POIXMLTypeLoader.parse(inputStream, UnsignedSignaturePropertiesType.type, xmlOptions);
        }

        public static UnsignedSignaturePropertiesType parse(Reader reader) throws XmlException, IOException {
            return (UnsignedSignaturePropertiesType) POIXMLTypeLoader.parse(reader, UnsignedSignaturePropertiesType.type, (XmlOptions) null);
        }

        public static UnsignedSignaturePropertiesType parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (UnsignedSignaturePropertiesType) POIXMLTypeLoader.parse(reader, UnsignedSignaturePropertiesType.type, xmlOptions);
        }

        public static UnsignedSignaturePropertiesType parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (UnsignedSignaturePropertiesType) POIXMLTypeLoader.parse(xMLStreamReader, UnsignedSignaturePropertiesType.type, (XmlOptions) null);
        }

        public static UnsignedSignaturePropertiesType parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (UnsignedSignaturePropertiesType) POIXMLTypeLoader.parse(xMLStreamReader, UnsignedSignaturePropertiesType.type, xmlOptions);
        }

        public static UnsignedSignaturePropertiesType parse(Node node) throws XmlException {
            return (UnsignedSignaturePropertiesType) POIXMLTypeLoader.parse(node, UnsignedSignaturePropertiesType.type, (XmlOptions) null);
        }

        public static UnsignedSignaturePropertiesType parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (UnsignedSignaturePropertiesType) POIXMLTypeLoader.parse(node, UnsignedSignaturePropertiesType.type, xmlOptions);
        }

        public static UnsignedSignaturePropertiesType parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (UnsignedSignaturePropertiesType) POIXMLTypeLoader.parse(xMLInputStream, UnsignedSignaturePropertiesType.type, (XmlOptions) null);
        }

        public static UnsignedSignaturePropertiesType parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (UnsignedSignaturePropertiesType) POIXMLTypeLoader.parse(xMLInputStream, UnsignedSignaturePropertiesType.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, UnsignedSignaturePropertiesType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, UnsignedSignaturePropertiesType.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<CounterSignatureType> getCounterSignatureList();

    CounterSignatureType[] getCounterSignatureArray();

    CounterSignatureType getCounterSignatureArray(int i);

    int sizeOfCounterSignatureArray();

    void setCounterSignatureArray(CounterSignatureType[] counterSignatureTypeArr);

    void setCounterSignatureArray(int i, CounterSignatureType counterSignatureType);

    CounterSignatureType insertNewCounterSignature(int i);

    CounterSignatureType addNewCounterSignature();

    void removeCounterSignature(int i);

    List<XAdESTimeStampType> getSignatureTimeStampList();

    XAdESTimeStampType[] getSignatureTimeStampArray();

    XAdESTimeStampType getSignatureTimeStampArray(int i);

    int sizeOfSignatureTimeStampArray();

    void setSignatureTimeStampArray(XAdESTimeStampType[] xAdESTimeStampTypeArr);

    void setSignatureTimeStampArray(int i, XAdESTimeStampType xAdESTimeStampType);

    XAdESTimeStampType insertNewSignatureTimeStamp(int i);

    XAdESTimeStampType addNewSignatureTimeStamp();

    void removeSignatureTimeStamp(int i);

    List<CompleteCertificateRefsType> getCompleteCertificateRefsList();

    CompleteCertificateRefsType[] getCompleteCertificateRefsArray();

    CompleteCertificateRefsType getCompleteCertificateRefsArray(int i);

    int sizeOfCompleteCertificateRefsArray();

    void setCompleteCertificateRefsArray(CompleteCertificateRefsType[] completeCertificateRefsTypeArr);

    void setCompleteCertificateRefsArray(int i, CompleteCertificateRefsType completeCertificateRefsType);

    CompleteCertificateRefsType insertNewCompleteCertificateRefs(int i);

    CompleteCertificateRefsType addNewCompleteCertificateRefs();

    void removeCompleteCertificateRefs(int i);

    List<CompleteRevocationRefsType> getCompleteRevocationRefsList();

    CompleteRevocationRefsType[] getCompleteRevocationRefsArray();

    CompleteRevocationRefsType getCompleteRevocationRefsArray(int i);

    int sizeOfCompleteRevocationRefsArray();

    void setCompleteRevocationRefsArray(CompleteRevocationRefsType[] completeRevocationRefsTypeArr);

    void setCompleteRevocationRefsArray(int i, CompleteRevocationRefsType completeRevocationRefsType);

    CompleteRevocationRefsType insertNewCompleteRevocationRefs(int i);

    CompleteRevocationRefsType addNewCompleteRevocationRefs();

    void removeCompleteRevocationRefs(int i);

    List<CompleteCertificateRefsType> getAttributeCertificateRefsList();

    CompleteCertificateRefsType[] getAttributeCertificateRefsArray();

    CompleteCertificateRefsType getAttributeCertificateRefsArray(int i);

    int sizeOfAttributeCertificateRefsArray();

    void setAttributeCertificateRefsArray(CompleteCertificateRefsType[] completeCertificateRefsTypeArr);

    void setAttributeCertificateRefsArray(int i, CompleteCertificateRefsType completeCertificateRefsType);

    CompleteCertificateRefsType insertNewAttributeCertificateRefs(int i);

    CompleteCertificateRefsType addNewAttributeCertificateRefs();

    void removeAttributeCertificateRefs(int i);

    List<CompleteRevocationRefsType> getAttributeRevocationRefsList();

    CompleteRevocationRefsType[] getAttributeRevocationRefsArray();

    CompleteRevocationRefsType getAttributeRevocationRefsArray(int i);

    int sizeOfAttributeRevocationRefsArray();

    void setAttributeRevocationRefsArray(CompleteRevocationRefsType[] completeRevocationRefsTypeArr);

    void setAttributeRevocationRefsArray(int i, CompleteRevocationRefsType completeRevocationRefsType);

    CompleteRevocationRefsType insertNewAttributeRevocationRefs(int i);

    CompleteRevocationRefsType addNewAttributeRevocationRefs();

    void removeAttributeRevocationRefs(int i);

    List<XAdESTimeStampType> getSigAndRefsTimeStampList();

    XAdESTimeStampType[] getSigAndRefsTimeStampArray();

    XAdESTimeStampType getSigAndRefsTimeStampArray(int i);

    int sizeOfSigAndRefsTimeStampArray();

    void setSigAndRefsTimeStampArray(XAdESTimeStampType[] xAdESTimeStampTypeArr);

    void setSigAndRefsTimeStampArray(int i, XAdESTimeStampType xAdESTimeStampType);

    XAdESTimeStampType insertNewSigAndRefsTimeStamp(int i);

    XAdESTimeStampType addNewSigAndRefsTimeStamp();

    void removeSigAndRefsTimeStamp(int i);

    List<XAdESTimeStampType> getRefsOnlyTimeStampList();

    XAdESTimeStampType[] getRefsOnlyTimeStampArray();

    XAdESTimeStampType getRefsOnlyTimeStampArray(int i);

    int sizeOfRefsOnlyTimeStampArray();

    void setRefsOnlyTimeStampArray(XAdESTimeStampType[] xAdESTimeStampTypeArr);

    void setRefsOnlyTimeStampArray(int i, XAdESTimeStampType xAdESTimeStampType);

    XAdESTimeStampType insertNewRefsOnlyTimeStamp(int i);

    XAdESTimeStampType addNewRefsOnlyTimeStamp();

    void removeRefsOnlyTimeStamp(int i);

    List<CertificateValuesType> getCertificateValuesList();

    CertificateValuesType[] getCertificateValuesArray();

    CertificateValuesType getCertificateValuesArray(int i);

    int sizeOfCertificateValuesArray();

    void setCertificateValuesArray(CertificateValuesType[] certificateValuesTypeArr);

    void setCertificateValuesArray(int i, CertificateValuesType certificateValuesType);

    CertificateValuesType insertNewCertificateValues(int i);

    CertificateValuesType addNewCertificateValues();

    void removeCertificateValues(int i);

    List<RevocationValuesType> getRevocationValuesList();

    RevocationValuesType[] getRevocationValuesArray();

    RevocationValuesType getRevocationValuesArray(int i);

    int sizeOfRevocationValuesArray();

    void setRevocationValuesArray(RevocationValuesType[] revocationValuesTypeArr);

    void setRevocationValuesArray(int i, RevocationValuesType revocationValuesType);

    RevocationValuesType insertNewRevocationValues(int i);

    RevocationValuesType addNewRevocationValues();

    void removeRevocationValues(int i);

    List<CertificateValuesType> getAttrAuthoritiesCertValuesList();

    CertificateValuesType[] getAttrAuthoritiesCertValuesArray();

    CertificateValuesType getAttrAuthoritiesCertValuesArray(int i);

    int sizeOfAttrAuthoritiesCertValuesArray();

    void setAttrAuthoritiesCertValuesArray(CertificateValuesType[] certificateValuesTypeArr);

    void setAttrAuthoritiesCertValuesArray(int i, CertificateValuesType certificateValuesType);

    CertificateValuesType insertNewAttrAuthoritiesCertValues(int i);

    CertificateValuesType addNewAttrAuthoritiesCertValues();

    void removeAttrAuthoritiesCertValues(int i);

    List<RevocationValuesType> getAttributeRevocationValuesList();

    RevocationValuesType[] getAttributeRevocationValuesArray();

    RevocationValuesType getAttributeRevocationValuesArray(int i);

    int sizeOfAttributeRevocationValuesArray();

    void setAttributeRevocationValuesArray(RevocationValuesType[] revocationValuesTypeArr);

    void setAttributeRevocationValuesArray(int i, RevocationValuesType revocationValuesType);

    RevocationValuesType insertNewAttributeRevocationValues(int i);

    RevocationValuesType addNewAttributeRevocationValues();

    void removeAttributeRevocationValues(int i);

    List<XAdESTimeStampType> getArchiveTimeStampList();

    XAdESTimeStampType[] getArchiveTimeStampArray();

    XAdESTimeStampType getArchiveTimeStampArray(int i);

    int sizeOfArchiveTimeStampArray();

    void setArchiveTimeStampArray(XAdESTimeStampType[] xAdESTimeStampTypeArr);

    void setArchiveTimeStampArray(int i, XAdESTimeStampType xAdESTimeStampType);

    XAdESTimeStampType insertNewArchiveTimeStamp(int i);

    XAdESTimeStampType addNewArchiveTimeStamp();

    void removeArchiveTimeStamp(int i);

    String getId();

    XmlID xgetId();

    boolean isSetId();

    void setId(String str);

    void xsetId(XmlID xmlID);

    void unsetId();
}

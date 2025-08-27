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
import org.w3.x2000.x09.xmldsig.CanonicalizationMethodType;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/etsi/uri/x01903/v13/GenericTimeStampType.class */
public interface GenericTimeStampType extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(GenericTimeStampType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3F193EE11A2F798ACF65489B9E6078").resolveHandle("generictimestamptypecdadtype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/etsi/uri/x01903/v13/GenericTimeStampType$Factory.class */
    public static final class Factory {
        public static GenericTimeStampType newInstance() {
            return (GenericTimeStampType) POIXMLTypeLoader.newInstance(GenericTimeStampType.type, null);
        }

        public static GenericTimeStampType newInstance(XmlOptions xmlOptions) {
            return (GenericTimeStampType) POIXMLTypeLoader.newInstance(GenericTimeStampType.type, xmlOptions);
        }

        public static GenericTimeStampType parse(String str) throws XmlException {
            return (GenericTimeStampType) POIXMLTypeLoader.parse(str, GenericTimeStampType.type, (XmlOptions) null);
        }

        public static GenericTimeStampType parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (GenericTimeStampType) POIXMLTypeLoader.parse(str, GenericTimeStampType.type, xmlOptions);
        }

        public static GenericTimeStampType parse(File file) throws XmlException, IOException {
            return (GenericTimeStampType) POIXMLTypeLoader.parse(file, GenericTimeStampType.type, (XmlOptions) null);
        }

        public static GenericTimeStampType parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (GenericTimeStampType) POIXMLTypeLoader.parse(file, GenericTimeStampType.type, xmlOptions);
        }

        public static GenericTimeStampType parse(URL url) throws XmlException, IOException {
            return (GenericTimeStampType) POIXMLTypeLoader.parse(url, GenericTimeStampType.type, (XmlOptions) null);
        }

        public static GenericTimeStampType parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (GenericTimeStampType) POIXMLTypeLoader.parse(url, GenericTimeStampType.type, xmlOptions);
        }

        public static GenericTimeStampType parse(InputStream inputStream) throws XmlException, IOException {
            return (GenericTimeStampType) POIXMLTypeLoader.parse(inputStream, GenericTimeStampType.type, (XmlOptions) null);
        }

        public static GenericTimeStampType parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (GenericTimeStampType) POIXMLTypeLoader.parse(inputStream, GenericTimeStampType.type, xmlOptions);
        }

        public static GenericTimeStampType parse(Reader reader) throws XmlException, IOException {
            return (GenericTimeStampType) POIXMLTypeLoader.parse(reader, GenericTimeStampType.type, (XmlOptions) null);
        }

        public static GenericTimeStampType parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (GenericTimeStampType) POIXMLTypeLoader.parse(reader, GenericTimeStampType.type, xmlOptions);
        }

        public static GenericTimeStampType parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (GenericTimeStampType) POIXMLTypeLoader.parse(xMLStreamReader, GenericTimeStampType.type, (XmlOptions) null);
        }

        public static GenericTimeStampType parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (GenericTimeStampType) POIXMLTypeLoader.parse(xMLStreamReader, GenericTimeStampType.type, xmlOptions);
        }

        public static GenericTimeStampType parse(Node node) throws XmlException {
            return (GenericTimeStampType) POIXMLTypeLoader.parse(node, GenericTimeStampType.type, (XmlOptions) null);
        }

        public static GenericTimeStampType parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (GenericTimeStampType) POIXMLTypeLoader.parse(node, GenericTimeStampType.type, xmlOptions);
        }

        public static GenericTimeStampType parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (GenericTimeStampType) POIXMLTypeLoader.parse(xMLInputStream, GenericTimeStampType.type, (XmlOptions) null);
        }

        public static GenericTimeStampType parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (GenericTimeStampType) POIXMLTypeLoader.parse(xMLInputStream, GenericTimeStampType.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, GenericTimeStampType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, GenericTimeStampType.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<IncludeType> getIncludeList();

    IncludeType[] getIncludeArray();

    IncludeType getIncludeArray(int i);

    int sizeOfIncludeArray();

    void setIncludeArray(IncludeType[] includeTypeArr);

    void setIncludeArray(int i, IncludeType includeType);

    IncludeType insertNewInclude(int i);

    IncludeType addNewInclude();

    void removeInclude(int i);

    List<ReferenceInfoType> getReferenceInfoList();

    ReferenceInfoType[] getReferenceInfoArray();

    ReferenceInfoType getReferenceInfoArray(int i);

    int sizeOfReferenceInfoArray();

    void setReferenceInfoArray(ReferenceInfoType[] referenceInfoTypeArr);

    void setReferenceInfoArray(int i, ReferenceInfoType referenceInfoType);

    ReferenceInfoType insertNewReferenceInfo(int i);

    ReferenceInfoType addNewReferenceInfo();

    void removeReferenceInfo(int i);

    CanonicalizationMethodType getCanonicalizationMethod();

    boolean isSetCanonicalizationMethod();

    void setCanonicalizationMethod(CanonicalizationMethodType canonicalizationMethodType);

    CanonicalizationMethodType addNewCanonicalizationMethod();

    void unsetCanonicalizationMethod();

    List<EncapsulatedPKIDataType> getEncapsulatedTimeStampList();

    EncapsulatedPKIDataType[] getEncapsulatedTimeStampArray();

    EncapsulatedPKIDataType getEncapsulatedTimeStampArray(int i);

    int sizeOfEncapsulatedTimeStampArray();

    void setEncapsulatedTimeStampArray(EncapsulatedPKIDataType[] encapsulatedPKIDataTypeArr);

    void setEncapsulatedTimeStampArray(int i, EncapsulatedPKIDataType encapsulatedPKIDataType);

    EncapsulatedPKIDataType insertNewEncapsulatedTimeStamp(int i);

    EncapsulatedPKIDataType addNewEncapsulatedTimeStamp();

    void removeEncapsulatedTimeStamp(int i);

    List<AnyType> getXMLTimeStampList();

    AnyType[] getXMLTimeStampArray();

    AnyType getXMLTimeStampArray(int i);

    int sizeOfXMLTimeStampArray();

    void setXMLTimeStampArray(AnyType[] anyTypeArr);

    void setXMLTimeStampArray(int i, AnyType anyType);

    AnyType insertNewXMLTimeStamp(int i);

    AnyType addNewXMLTimeStamp();

    void removeXMLTimeStamp(int i);

    String getId();

    XmlID xgetId();

    boolean isSetId();

    void setId(String str);

    void xsetId(XmlID xmlID);

    void unsetId();
}

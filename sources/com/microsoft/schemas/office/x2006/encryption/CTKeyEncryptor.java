package com.microsoft.schemas.office.x2006.encryption;

import com.microsoft.schemas.office.x2006.keyEncryptor.certificate.CTCertificateKeyEncryptor;
import com.microsoft.schemas.office.x2006.keyEncryptor.password.CTPasswordKeyEncryptor;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.StringEnumAbstractBase;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlToken;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/x2006/encryption/CTKeyEncryptor.class */
public interface CTKeyEncryptor extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTKeyEncryptor.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3F193EE11A2F798ACF65489B9E6078").resolveHandle("ctkeyencryptor1205type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/x2006/encryption/CTKeyEncryptor$Factory.class */
    public static final class Factory {
        public static CTKeyEncryptor newInstance() {
            return (CTKeyEncryptor) POIXMLTypeLoader.newInstance(CTKeyEncryptor.type, null);
        }

        public static CTKeyEncryptor newInstance(XmlOptions xmlOptions) {
            return (CTKeyEncryptor) POIXMLTypeLoader.newInstance(CTKeyEncryptor.type, xmlOptions);
        }

        public static CTKeyEncryptor parse(String str) throws XmlException {
            return (CTKeyEncryptor) POIXMLTypeLoader.parse(str, CTKeyEncryptor.type, (XmlOptions) null);
        }

        public static CTKeyEncryptor parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTKeyEncryptor) POIXMLTypeLoader.parse(str, CTKeyEncryptor.type, xmlOptions);
        }

        public static CTKeyEncryptor parse(File file) throws XmlException, IOException {
            return (CTKeyEncryptor) POIXMLTypeLoader.parse(file, CTKeyEncryptor.type, (XmlOptions) null);
        }

        public static CTKeyEncryptor parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTKeyEncryptor) POIXMLTypeLoader.parse(file, CTKeyEncryptor.type, xmlOptions);
        }

        public static CTKeyEncryptor parse(URL url) throws XmlException, IOException {
            return (CTKeyEncryptor) POIXMLTypeLoader.parse(url, CTKeyEncryptor.type, (XmlOptions) null);
        }

        public static CTKeyEncryptor parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTKeyEncryptor) POIXMLTypeLoader.parse(url, CTKeyEncryptor.type, xmlOptions);
        }

        public static CTKeyEncryptor parse(InputStream inputStream) throws XmlException, IOException {
            return (CTKeyEncryptor) POIXMLTypeLoader.parse(inputStream, CTKeyEncryptor.type, (XmlOptions) null);
        }

        public static CTKeyEncryptor parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTKeyEncryptor) POIXMLTypeLoader.parse(inputStream, CTKeyEncryptor.type, xmlOptions);
        }

        public static CTKeyEncryptor parse(Reader reader) throws XmlException, IOException {
            return (CTKeyEncryptor) POIXMLTypeLoader.parse(reader, CTKeyEncryptor.type, (XmlOptions) null);
        }

        public static CTKeyEncryptor parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTKeyEncryptor) POIXMLTypeLoader.parse(reader, CTKeyEncryptor.type, xmlOptions);
        }

        public static CTKeyEncryptor parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTKeyEncryptor) POIXMLTypeLoader.parse(xMLStreamReader, CTKeyEncryptor.type, (XmlOptions) null);
        }

        public static CTKeyEncryptor parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTKeyEncryptor) POIXMLTypeLoader.parse(xMLStreamReader, CTKeyEncryptor.type, xmlOptions);
        }

        public static CTKeyEncryptor parse(Node node) throws XmlException {
            return (CTKeyEncryptor) POIXMLTypeLoader.parse(node, CTKeyEncryptor.type, (XmlOptions) null);
        }

        public static CTKeyEncryptor parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTKeyEncryptor) POIXMLTypeLoader.parse(node, CTKeyEncryptor.type, xmlOptions);
        }

        public static CTKeyEncryptor parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTKeyEncryptor) POIXMLTypeLoader.parse(xMLInputStream, CTKeyEncryptor.type, (XmlOptions) null);
        }

        public static CTKeyEncryptor parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTKeyEncryptor) POIXMLTypeLoader.parse(xMLInputStream, CTKeyEncryptor.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTKeyEncryptor.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTKeyEncryptor.type, xmlOptions);
        }

        private Factory() {
        }
    }

    /* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/x2006/encryption/CTKeyEncryptor$Uri.class */
    public interface Uri extends XmlToken {
        public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Uri.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3F193EE11A2F798ACF65489B9E6078").resolveHandle("uribad9attrtype");
        public static final Enum HTTP_SCHEMAS_MICROSOFT_COM_OFFICE_2006_KEY_ENCRYPTOR_PASSWORD = Enum.forString("http://schemas.microsoft.com/office/2006/keyEncryptor/password");
        public static final Enum HTTP_SCHEMAS_MICROSOFT_COM_OFFICE_2006_KEY_ENCRYPTOR_CERTIFICATE = Enum.forString("http://schemas.microsoft.com/office/2006/keyEncryptor/certificate");
        public static final int INT_HTTP_SCHEMAS_MICROSOFT_COM_OFFICE_2006_KEY_ENCRYPTOR_PASSWORD = 1;
        public static final int INT_HTTP_SCHEMAS_MICROSOFT_COM_OFFICE_2006_KEY_ENCRYPTOR_CERTIFICATE = 2;

        /* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/x2006/encryption/CTKeyEncryptor$Uri$Enum.class */
        public static final class Enum extends StringEnumAbstractBase {
            static final int INT_HTTP_SCHEMAS_MICROSOFT_COM_OFFICE_2006_KEY_ENCRYPTOR_PASSWORD = 1;
            static final int INT_HTTP_SCHEMAS_MICROSOFT_COM_OFFICE_2006_KEY_ENCRYPTOR_CERTIFICATE = 2;
            public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new Enum[]{new Enum("http://schemas.microsoft.com/office/2006/keyEncryptor/password", 1), new Enum("http://schemas.microsoft.com/office/2006/keyEncryptor/certificate", 2)});
            private static final long serialVersionUID = 1;

            public static Enum forString(String str) {
                return (Enum) table.forString(str);
            }

            public static Enum forInt(int i) {
                return (Enum) table.forInt(i);
            }

            private Enum(String str, int i) {
                super(str, i);
            }

            private Object readResolve() {
                return forInt(intValue());
            }
        }

        /* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/x2006/encryption/CTKeyEncryptor$Uri$Factory.class */
        public static final class Factory {
            public static Uri newValue(Object obj) {
                return (Uri) Uri.type.newValue(obj);
            }

            public static Uri newInstance() {
                return (Uri) POIXMLTypeLoader.newInstance(Uri.type, null);
            }

            public static Uri newInstance(XmlOptions xmlOptions) {
                return (Uri) POIXMLTypeLoader.newInstance(Uri.type, xmlOptions);
            }

            private Factory() {
            }
        }

        StringEnumAbstractBase enumValue();

        void set(StringEnumAbstractBase stringEnumAbstractBase);
    }

    CTPasswordKeyEncryptor getEncryptedPasswordKey();

    boolean isSetEncryptedPasswordKey();

    void setEncryptedPasswordKey(CTPasswordKeyEncryptor cTPasswordKeyEncryptor);

    CTPasswordKeyEncryptor addNewEncryptedPasswordKey();

    void unsetEncryptedPasswordKey();

    CTCertificateKeyEncryptor getEncryptedCertificateKey();

    boolean isSetEncryptedCertificateKey();

    void setEncryptedCertificateKey(CTCertificateKeyEncryptor cTCertificateKeyEncryptor);

    CTCertificateKeyEncryptor addNewEncryptedCertificateKey();

    void unsetEncryptedCertificateKey();

    Uri.Enum getUri();

    Uri xgetUri();

    boolean isSetUri();

    void setUri(Uri.Enum r1);

    void xsetUri(Uri uri);

    void unsetUri();
}

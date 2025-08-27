package org.apache.xmlbeans;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.Calendar;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlGYear.class */
public interface XmlGYear extends XmlAnySimpleType {
    public static final SchemaType type = XmlBeans.getBuiltinTypeSystem().typeForHandle("_BI_gYear");

    Calendar getCalendarValue();

    void setCalendarValue(Calendar calendar);

    GDate getGDateValue();

    void setGDateValue(GDate gDate);

    int getIntValue();

    void setIntValue(int i);

    Calendar calendarValue();

    void set(Calendar calendar);

    GDate gDateValue();

    void set(GDateSpecification gDateSpecification);

    int intValue();

    void set(int i);

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlGYear$Factory.class */
    public static final class Factory {
        public static XmlGYear newInstance() {
            return (XmlGYear) XmlBeans.getContextTypeLoader().newInstance(XmlGYear.type, null);
        }

        public static XmlGYear newInstance(XmlOptions options) {
            return (XmlGYear) XmlBeans.getContextTypeLoader().newInstance(XmlGYear.type, options);
        }

        public static XmlGYear newValue(Object obj) {
            return (XmlGYear) XmlGYear.type.newValue(obj);
        }

        public static XmlGYear parse(String s) throws XmlException {
            return (XmlGYear) XmlBeans.getContextTypeLoader().parse(s, XmlGYear.type, (XmlOptions) null);
        }

        public static XmlGYear parse(String s, XmlOptions options) throws XmlException {
            return (XmlGYear) XmlBeans.getContextTypeLoader().parse(s, XmlGYear.type, options);
        }

        public static XmlGYear parse(File f) throws XmlException, IOException {
            return (XmlGYear) XmlBeans.getContextTypeLoader().parse(f, XmlGYear.type, (XmlOptions) null);
        }

        public static XmlGYear parse(File f, XmlOptions options) throws XmlException, IOException {
            return (XmlGYear) XmlBeans.getContextTypeLoader().parse(f, XmlGYear.type, options);
        }

        public static XmlGYear parse(URL u) throws XmlException, IOException {
            return (XmlGYear) XmlBeans.getContextTypeLoader().parse(u, XmlGYear.type, (XmlOptions) null);
        }

        public static XmlGYear parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (XmlGYear) XmlBeans.getContextTypeLoader().parse(u, XmlGYear.type, options);
        }

        public static XmlGYear parse(InputStream is) throws XmlException, IOException {
            return (XmlGYear) XmlBeans.getContextTypeLoader().parse(is, XmlGYear.type, (XmlOptions) null);
        }

        public static XmlGYear parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (XmlGYear) XmlBeans.getContextTypeLoader().parse(is, XmlGYear.type, options);
        }

        public static XmlGYear parse(Reader r) throws XmlException, IOException {
            return (XmlGYear) XmlBeans.getContextTypeLoader().parse(r, XmlGYear.type, (XmlOptions) null);
        }

        public static XmlGYear parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (XmlGYear) XmlBeans.getContextTypeLoader().parse(r, XmlGYear.type, options);
        }

        public static XmlGYear parse(Node node) throws XmlException {
            return (XmlGYear) XmlBeans.getContextTypeLoader().parse(node, XmlGYear.type, (XmlOptions) null);
        }

        public static XmlGYear parse(Node node, XmlOptions options) throws XmlException {
            return (XmlGYear) XmlBeans.getContextTypeLoader().parse(node, XmlGYear.type, options);
        }

        public static XmlGYear parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (XmlGYear) XmlBeans.getContextTypeLoader().parse(xis, XmlGYear.type, (XmlOptions) null);
        }

        public static XmlGYear parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (XmlGYear) XmlBeans.getContextTypeLoader().parse(xis, XmlGYear.type, options);
        }

        public static XmlGYear parse(XMLStreamReader xsr) throws XmlException {
            return (XmlGYear) XmlBeans.getContextTypeLoader().parse(xsr, XmlGYear.type, (XmlOptions) null);
        }

        public static XmlGYear parse(XMLStreamReader xsr, XmlOptions options) throws XmlException {
            return (XmlGYear) XmlBeans.getContextTypeLoader().parse(xsr, XmlGYear.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, XmlGYear.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, XmlGYear.type, options);
        }

        private Factory() {
        }
    }
}

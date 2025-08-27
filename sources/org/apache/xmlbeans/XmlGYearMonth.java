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

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlGYearMonth.class */
public interface XmlGYearMonth extends XmlAnySimpleType {
    public static final SchemaType type = XmlBeans.getBuiltinTypeSystem().typeForHandle("_BI_gYearMonth");

    Calendar getCalendarValue();

    void setCalendarValue(Calendar calendar);

    GDate getGDateValue();

    void setGDateValue(GDate gDate);

    Calendar calendarValue();

    void set(Calendar calendar);

    GDate gDateValue();

    void set(GDateSpecification gDateSpecification);

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlGYearMonth$Factory.class */
    public static final class Factory {
        public static XmlGYearMonth newInstance() {
            return (XmlGYearMonth) XmlBeans.getContextTypeLoader().newInstance(XmlGYearMonth.type, null);
        }

        public static XmlGYearMonth newInstance(XmlOptions options) {
            return (XmlGYearMonth) XmlBeans.getContextTypeLoader().newInstance(XmlGYearMonth.type, options);
        }

        public static XmlGYearMonth newValue(Object obj) {
            return (XmlGYearMonth) XmlGYearMonth.type.newValue(obj);
        }

        public static XmlGYearMonth parse(String s) throws XmlException {
            return (XmlGYearMonth) XmlBeans.getContextTypeLoader().parse(s, XmlGYearMonth.type, (XmlOptions) null);
        }

        public static XmlGYearMonth parse(String s, XmlOptions options) throws XmlException {
            return (XmlGYearMonth) XmlBeans.getContextTypeLoader().parse(s, XmlGYearMonth.type, options);
        }

        public static XmlGYearMonth parse(File f) throws XmlException, IOException {
            return (XmlGYearMonth) XmlBeans.getContextTypeLoader().parse(f, XmlGYearMonth.type, (XmlOptions) null);
        }

        public static XmlGYearMonth parse(File f, XmlOptions options) throws XmlException, IOException {
            return (XmlGYearMonth) XmlBeans.getContextTypeLoader().parse(f, XmlGYearMonth.type, options);
        }

        public static XmlGYearMonth parse(URL u) throws XmlException, IOException {
            return (XmlGYearMonth) XmlBeans.getContextTypeLoader().parse(u, XmlGYearMonth.type, (XmlOptions) null);
        }

        public static XmlGYearMonth parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (XmlGYearMonth) XmlBeans.getContextTypeLoader().parse(u, XmlGYearMonth.type, options);
        }

        public static XmlGYearMonth parse(InputStream is) throws XmlException, IOException {
            return (XmlGYearMonth) XmlBeans.getContextTypeLoader().parse(is, XmlGYearMonth.type, (XmlOptions) null);
        }

        public static XmlGYearMonth parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (XmlGYearMonth) XmlBeans.getContextTypeLoader().parse(is, XmlGYearMonth.type, options);
        }

        public static XmlGYearMonth parse(Reader r) throws XmlException, IOException {
            return (XmlGYearMonth) XmlBeans.getContextTypeLoader().parse(r, XmlGYearMonth.type, (XmlOptions) null);
        }

        public static XmlGYearMonth parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (XmlGYearMonth) XmlBeans.getContextTypeLoader().parse(r, XmlGYearMonth.type, options);
        }

        public static XmlGYearMonth parse(Node node) throws XmlException {
            return (XmlGYearMonth) XmlBeans.getContextTypeLoader().parse(node, XmlGYearMonth.type, (XmlOptions) null);
        }

        public static XmlGYearMonth parse(Node node, XmlOptions options) throws XmlException {
            return (XmlGYearMonth) XmlBeans.getContextTypeLoader().parse(node, XmlGYearMonth.type, options);
        }

        public static XmlGYearMonth parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (XmlGYearMonth) XmlBeans.getContextTypeLoader().parse(xis, XmlGYearMonth.type, (XmlOptions) null);
        }

        public static XmlGYearMonth parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (XmlGYearMonth) XmlBeans.getContextTypeLoader().parse(xis, XmlGYearMonth.type, options);
        }

        public static XmlGYearMonth parse(XMLStreamReader xsr) throws XmlException {
            return (XmlGYearMonth) XmlBeans.getContextTypeLoader().parse(xsr, XmlGYearMonth.type, (XmlOptions) null);
        }

        public static XmlGYearMonth parse(XMLStreamReader xsr, XmlOptions options) throws XmlException {
            return (XmlGYearMonth) XmlBeans.getContextTypeLoader().parse(xsr, XmlGYearMonth.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, XmlGYearMonth.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, XmlGYearMonth.type, options);
        }

        private Factory() {
        }
    }
}

package org.apache.xmlbeans;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlDate.class */
public interface XmlDate extends XmlAnySimpleType {
    public static final SchemaType type = XmlBeans.getBuiltinTypeSystem().typeForHandle("_BI_date");

    Calendar calendarValue();

    void set(Calendar calendar);

    GDate gDateValue();

    void set(GDateSpecification gDateSpecification);

    Date dateValue();

    void set(Date date);

    Calendar getCalendarValue();

    void setCalendarValue(Calendar calendar);

    GDate getGDateValue();

    void setGDateValue(GDate gDate);

    Date getDateValue();

    void setDateValue(Date date);

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlDate$Factory.class */
    public static final class Factory {
        public static XmlDate newInstance() {
            return (XmlDate) XmlBeans.getContextTypeLoader().newInstance(XmlDate.type, null);
        }

        public static XmlDate newInstance(XmlOptions options) {
            return (XmlDate) XmlBeans.getContextTypeLoader().newInstance(XmlDate.type, options);
        }

        public static XmlDate newValue(Object obj) {
            return (XmlDate) XmlDate.type.newValue(obj);
        }

        public static XmlDate parse(String s) throws XmlException {
            return (XmlDate) XmlBeans.getContextTypeLoader().parse(s, XmlDate.type, (XmlOptions) null);
        }

        public static XmlDate parse(String s, XmlOptions options) throws XmlException {
            return (XmlDate) XmlBeans.getContextTypeLoader().parse(s, XmlDate.type, options);
        }

        public static XmlDate parse(File f) throws XmlException, IOException {
            return (XmlDate) XmlBeans.getContextTypeLoader().parse(f, XmlDate.type, (XmlOptions) null);
        }

        public static XmlDate parse(File f, XmlOptions options) throws XmlException, IOException {
            return (XmlDate) XmlBeans.getContextTypeLoader().parse(f, XmlDate.type, options);
        }

        public static XmlDate parse(URL u) throws XmlException, IOException {
            return (XmlDate) XmlBeans.getContextTypeLoader().parse(u, XmlDate.type, (XmlOptions) null);
        }

        public static XmlDate parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (XmlDate) XmlBeans.getContextTypeLoader().parse(u, XmlDate.type, options);
        }

        public static XmlDate parse(InputStream is) throws XmlException, IOException {
            return (XmlDate) XmlBeans.getContextTypeLoader().parse(is, XmlDate.type, (XmlOptions) null);
        }

        public static XmlDate parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (XmlDate) XmlBeans.getContextTypeLoader().parse(is, XmlDate.type, options);
        }

        public static XmlDate parse(Reader r) throws XmlException, IOException {
            return (XmlDate) XmlBeans.getContextTypeLoader().parse(r, XmlDate.type, (XmlOptions) null);
        }

        public static XmlDate parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (XmlDate) XmlBeans.getContextTypeLoader().parse(r, XmlDate.type, options);
        }

        public static XmlDate parse(Node node) throws XmlException {
            return (XmlDate) XmlBeans.getContextTypeLoader().parse(node, XmlDate.type, (XmlOptions) null);
        }

        public static XmlDate parse(Node node, XmlOptions options) throws XmlException {
            return (XmlDate) XmlBeans.getContextTypeLoader().parse(node, XmlDate.type, options);
        }

        public static XmlDate parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (XmlDate) XmlBeans.getContextTypeLoader().parse(xis, XmlDate.type, (XmlOptions) null);
        }

        public static XmlDate parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (XmlDate) XmlBeans.getContextTypeLoader().parse(xis, XmlDate.type, options);
        }

        public static XmlDate parse(XMLStreamReader xsr) throws XmlException {
            return (XmlDate) XmlBeans.getContextTypeLoader().parse(xsr, XmlDate.type, (XmlOptions) null);
        }

        public static XmlDate parse(XMLStreamReader xsr, XmlOptions options) throws XmlException {
            return (XmlDate) XmlBeans.getContextTypeLoader().parse(xsr, XmlDate.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, XmlDate.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, XmlDate.type, options);
        }

        private Factory() {
        }
    }
}
